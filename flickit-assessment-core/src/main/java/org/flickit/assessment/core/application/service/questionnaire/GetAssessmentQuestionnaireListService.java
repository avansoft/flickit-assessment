package org.flickit.assessment.core.application.service.questionnaire;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.application.domain.assessment.AssessmentAccessChecker;
import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.core.application.domain.AssessmentResult;
import org.flickit.assessment.core.application.domain.ConfidenceLevel;
import org.flickit.assessment.core.application.domain.QuestionnaireListItem;
import org.flickit.assessment.core.application.port.in.questionnaire.GetAssessmentQuestionnaireListUseCase;
import org.flickit.assessment.core.application.port.out.answer.CountLowConfidenceAnswersPort;
import org.flickit.assessment.core.application.port.out.assessmentresult.LoadAssessmentResultPort;
import org.flickit.assessment.core.application.port.out.evidence.CountEvidencesPort;
import org.flickit.assessment.core.application.port.out.questionnaire.LoadQuestionnairesByAssessmentIdPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.flickit.assessment.common.application.domain.assessment.AssessmentPermission.VIEW_ASSESSMENT_QUESTIONNAIRE_LIST;
import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.flickit.assessment.core.common.ErrorMessageKey.GET_ASSESSMENT_QUESTIONNAIRE_LIST_ASSESSMENT_RESULT_ID_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetAssessmentQuestionnaireListService implements GetAssessmentQuestionnaireListUseCase {

    private final LoadQuestionnairesByAssessmentIdPort loadQuestionnairesByAssessmentIdPort;
    private final AssessmentAccessChecker assessmentAccessChecker;
    private final LoadAssessmentResultPort loadAssessmentResultPort;
    private final CountLowConfidenceAnswersPort lowConfidenceAnswersPort;
    private final CountEvidencesPort countEvidencesPort;

    @Override
    public PaginatedResponse<QuestionnaireListItem> getAssessmentQuestionnaireList(Param param) {
        if (!assessmentAccessChecker.isAuthorized(param.getAssessmentId(), param.getCurrentUserId(), VIEW_ASSESSMENT_QUESTIONNAIRE_LIST))
            throw new AccessDeniedException(COMMON_CURRENT_USER_NOT_ALLOWED);

        var assessmentResult = loadAssessmentResultPort.loadByAssessmentId(param.getAssessmentId())
            .orElseThrow(() -> new ResourceNotFoundException(GET_ASSESSMENT_QUESTIONNAIRE_LIST_ASSESSMENT_RESULT_ID_NOT_FOUND));

        var questionnaires = loadQuestionnairesByAssessmentIdPort.loadAllByAssessmentId(toPortParam(param, assessmentResult));
        return buildResultWithIssues(assessmentResult, questionnaires);
    }

    private LoadQuestionnairesByAssessmentIdPort.Param toPortParam(Param param, AssessmentResult assessmentResult) {
        return new LoadQuestionnairesByAssessmentIdPort.Param(
            assessmentResult,
            param.getSize(),
            param.getPage());
    }

    private PaginatedResponse<QuestionnaireListItem> buildResultWithIssues(AssessmentResult assessmentResult, PaginatedResponse<QuestionnaireListItem> questionnaires) {
        var questionnaireIds = questionnaires.getItems().stream().map(QuestionnaireListItem::id).toList();
        var questionnaireIdToLowConfidenceAnswersCount = lowConfidenceAnswersPort.countWithConfidenceLessThan(
            assessmentResult.getId(), questionnaireIds, ConfidenceLevel.SOMEWHAT_UNSURE);
        var questionnaireIdToUnresolvedCommentsCount = countEvidencesPort.countUnresolvedComments(
            assessmentResult.getAssessment().getId(), questionnaireIds);
        var questionnaireIdToEvidenceCount = countEvidencesPort.countAnsweredQuestionsHavingEvidence(
            assessmentResult.getAssessment().getId(), questionnaireIds);

        var items = questionnaires.getItems().stream()
            .map(i -> buildQuestionnaireWithIssues(i,
                questionnaireIdToLowConfidenceAnswersCount,
                questionnaireIdToUnresolvedCommentsCount,
                questionnaireIdToEvidenceCount))
            .toList();

        return new PaginatedResponse<>(items,
            questionnaires.getPage(),
            questionnaires.getSize(),
            questionnaires.getSort(),
            questionnaires.getOrder(),
            questionnaires.getTotal());
    }

    private QuestionnaireListItem buildQuestionnaireWithIssues(QuestionnaireListItem questionnaireListItem, Map<Long, Integer> lowConfidenceAnswersCount,
                                                               Map<Long, Integer> questionnairesUnresolvedComments, Map<Long, Integer> questionnairesEvidenceCount) {
        return new QuestionnaireListItem(questionnaireListItem.id(),
            questionnaireListItem.title(),
            questionnaireListItem.description(),
            questionnaireListItem.index(),
            questionnaireListItem.questionCount(),
            questionnaireListItem.answerCount(),
            questionnaireListItem.nextQuestion(),
            questionnaireListItem.progress(),
            questionnaireListItem.subjects(),
            new QuestionnaireListItem.Issues(questionnaireListItem.questionCount() - questionnaireListItem.answerCount(),
                lowConfidenceAnswersCount.get(questionnaireListItem.id()) != null
                    ? lowConfidenceAnswersCount.get(questionnaireListItem.id()) : 0,
                (questionnairesEvidenceCount.get(questionnaireListItem.id()) != null)
                    ? questionnaireListItem.answerCount() - questionnairesEvidenceCount.get(questionnaireListItem.id())
                    : questionnaireListItem.answerCount(),
                questionnairesUnresolvedComments.get(questionnaireListItem.id()) != null
                    ? questionnairesUnresolvedComments.get(questionnaireListItem.id()) : 0));
    }
}
