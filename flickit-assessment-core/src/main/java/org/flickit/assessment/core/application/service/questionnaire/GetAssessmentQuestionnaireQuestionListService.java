package org.flickit.assessment.core.application.service.questionnaire;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.application.domain.assessment.AssessmentAccessChecker;
import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.core.application.domain.Answer;
import org.flickit.assessment.core.application.domain.AnswerOption;
import org.flickit.assessment.core.application.domain.ConfidenceLevel;
import org.flickit.assessment.core.application.domain.Question;
import org.flickit.assessment.core.application.port.in.questionnaire.GetAssessmentQuestionnaireQuestionListUseCase;
import org.flickit.assessment.core.application.port.out.answer.LoadQuestionsAnswerListPort;
import org.flickit.assessment.core.application.port.out.assessmentresult.LoadAssessmentResultPort;
import org.flickit.assessment.core.application.port.out.evidence.CountEvidencesPort;
import org.flickit.assessment.core.application.port.out.question.LoadQuestionnaireQuestionListPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static org.flickit.assessment.common.application.domain.assessment.AssessmentPermission.VIEW_QUESTIONNAIRE_QUESTIONS;
import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.flickit.assessment.core.common.ErrorMessageKey.GET_ASSESSMENT_QUESTIONNAIRE_QUESTION_LIST_ASSESSMENT_ID_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetAssessmentQuestionnaireQuestionListService implements GetAssessmentQuestionnaireQuestionListUseCase {

    private final AssessmentAccessChecker assessmentAccessChecker;
    private final LoadQuestionnaireQuestionListPort loadQuestionnaireQuestionListPort;
    private final LoadAssessmentResultPort loadAssessmentResultPort;
    private final LoadQuestionsAnswerListPort loadQuestionsAnswerListPort;
    private final CountEvidencesPort countEvidencesPort;

    @Override
    public PaginatedResponse<Result> getQuestionnaireQuestionList(Param param) {
        if (!assessmentAccessChecker.isAuthorized(param.getAssessmentId(), param.getCurrentUserId(), VIEW_QUESTIONNAIRE_QUESTIONS))
            throw new AccessDeniedException(COMMON_CURRENT_USER_NOT_ALLOWED);

        var kitVersionId = loadAssessmentResultPort.loadByAssessmentId(param.getAssessmentId())
            .orElseThrow(() -> new ResourceNotFoundException(GET_ASSESSMENT_QUESTIONNAIRE_QUESTION_LIST_ASSESSMENT_ID_NOT_FOUND))
            .getKitVersionId();

        var pageResult = loadQuestionnaireQuestionListPort.loadByQuestionnaireId(param.getQuestionnaireId(),
            kitVersionId,
            param.getSize(),
            param.getPage());

        List<Long> questionIds = pageResult.getItems().stream()
            .map(Question::getId)
            .toList();

        var questionIdToAnswerMap = loadQuestionsAnswerListPort.loadByQuestionIds(param.getAssessmentId(), questionIds)
            .stream()
            .collect(toMap(Answer::getQuestionId, Function.identity()));

        var questionIdToEvidencesCountMap = countEvidencesPort.countAnsweredQuestionsHavingEvidence(param.getAssessmentId(), param.getQuestionnaireId());
        var questionIdToUnresolvedCommentsCountMap = countEvidencesPort.countUnresolvedComments(param.getAssessmentId(), param.getQuestionnaireId());
        var items = pageResult.getItems().stream()
            .map((Question q) -> mapToResult(q,
                questionIdToAnswerMap.get(q.getId()),
                questionIdToEvidencesCountMap.getOrDefault(q.getId(), 0),
                questionIdToUnresolvedCommentsCountMap.getOrDefault(q.getId(), 0)))
            .toList();

        return new PaginatedResponse<>(
            items,
            pageResult.getPage(),
            pageResult.getSize(),
            pageResult.getSort(),
            pageResult.getOrder(),
            pageResult.getTotal());
    }

    private Result mapToResult(Question question, Answer answer, int evidencesCount, int unresolvedCommentsCount) {
        QuestionAnswer answerDto = null;
        if (answer != null) {
            Option answerOption = null;
            if (!Boolean.TRUE.equals(answer.getIsNotApplicable()) && answer.getSelectedOption() != null) {
                answerOption = question.getOptions().stream()
                    .filter(x -> Objects.equals(x.getId(), answer.getSelectedOption().getId()))
                    .map(this::mapToOption)
                    .findAny()
                    .orElse(null);
            }
            ConfidenceLevel confidenceLevel = null;
            if (answerOption != null || Boolean.TRUE.equals(answer.getIsNotApplicable()))
                confidenceLevel = ConfidenceLevel.valueOfById(answer.getConfidenceLevelId());
            answerDto = new QuestionAnswer(answerOption, confidenceLevel, answer.getIsNotApplicable());
        }
        return new Result(
            question.getId(),
            question.getIndex(),
            question.getTitle(),
            question.getHint(),
            question.getMayNotBeApplicable(),
            question.getOptions().stream()
                .map(this::mapToOption)
                .toList(),
            answerDto,
            new Issues(answerDto == null,
                answerDto != null ? answerDto.confidenceLevel().getId() < ConfidenceLevel.SOMEWHAT_UNSURE.getId() : null,
                evidencesCount == 0,
                unresolvedCommentsCount));
    }

    private Option mapToOption(AnswerOption option) {
        return new Option(option.getId(), option.getIndex(), option.getTitle());
    }
}
