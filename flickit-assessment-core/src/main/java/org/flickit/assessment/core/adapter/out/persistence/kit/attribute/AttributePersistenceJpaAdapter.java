package org.flickit.assessment.core.adapter.out.persistence.kit.attribute;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.core.application.port.in.attribute.GetAttributeScoreDetailUseCase.QuestionScore;
import org.flickit.assessment.core.application.port.in.attribute.GetAttributeScoreDetailUseCase.Questionnaire;
import org.flickit.assessment.core.application.port.in.subject.GetSubjectAttributesUseCase.SubjectAttribute;
import org.flickit.assessment.core.application.port.out.attribute.LoadAttributeScoreDetailPort;
import org.flickit.assessment.core.application.port.out.subject.LoadSubjectAttributesPort;
import org.flickit.assessment.data.jpa.core.answer.AnswerJpaEntity;
import org.flickit.assessment.data.jpa.core.assessmentresult.AssessmentResultJpaRepository;
import org.flickit.assessment.data.jpa.kit.asnweroptionimpact.AnswerOptionImpactJpaEntity;
import org.flickit.assessment.data.jpa.kit.attribute.AttributeJpaRepository;
import org.flickit.assessment.data.jpa.kit.attribute.ImpactFullQuestionsView;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.flickit.assessment.core.common.ErrorMessageKey.GET_ATTRIBUTE_SCORE_DETAIL_ASSESSMENT_RESULT_NOT_FOUND;

@Component("coreAttributePersistenceJpaAdapter")
@RequiredArgsConstructor
public class AttributePersistenceJpaAdapter implements LoadAttributeScoreDetailPort, LoadSubjectAttributesPort {

    private final AttributeJpaRepository repository;
    private final AssessmentResultJpaRepository assessmentResultRepository;

    @Override
    public List<Questionnaire> loadScoreDetail(UUID assessmentId, long attributeId, long maturityLevelId) {
        var assessmentResult = assessmentResultRepository.findFirstByAssessment_IdOrderByLastModificationTimeDesc(assessmentId)
            .orElseThrow(() -> new ResourceNotFoundException(GET_ATTRIBUTE_SCORE_DETAIL_ASSESSMENT_RESULT_NOT_FOUND));

        var questionsView = repository.findImpactFullQuestionsScore(assessmentResult.getId(), attributeId, maturityLevelId);

        return questionsView.stream()
            .collect(Collectors.groupingBy(ImpactFullQuestionsView::getQuestionnaireTitle))
            .entrySet().stream()
            .map(entry -> {
                String questionnaireTitle = entry.getKey();
                List<QuestionScore> questionScores = entry.getValue().stream()
                    .map(view -> new QuestionScore(
                        view.getQuestionIndex(),
                        view.getQuestionTitle(),
                        view.getQuestionImpact().getWeight(),
                        view.getOptionIndex(),
                        view.getOptionTitle(),
                        view.getAnswer() == null ? null : view.getAnswer().getIsNotApplicable(),
                        getScore(view.getAnswer(), view.getOptionImpact()),
                        view.getOptionImpact() == null ? 0 : view.getOptionImpact().getValue() * view.getQuestionImpact().getWeight()
                    ))
                    .sorted(Comparator.comparingInt(QuestionScore::questionIndex))
                    .toList();
                return new Questionnaire(questionnaireTitle, questionScores);
            })
            .sorted(Comparator.comparing(Questionnaire::title))
            .toList();
    }

    private Double getScore(AnswerJpaEntity answer, AnswerOptionImpactJpaEntity optionImpact) {
        if (answer == null) // if no answer is submitted for the question
            return 0.0;
        if(Boolean.TRUE.equals(answer.getIsNotApplicable())) // if there is an answer and notApplicable == true
            return null;
        if(optionImpact == null) // if there exists an answer and notApplicable != true and no option is selected
            return 0.0;
        return optionImpact.getValue();
    }

    @Override
    public List<SubjectAttribute> loadBySubjectIdAndAssessmentId(Long subjectId, UUID assessmentId) {
        var subjectAttrViews = repository.findBySubjectIdAndAssessmentId(subjectId, assessmentId);
        return subjectAttrViews.stream()
            .map(e -> new SubjectAttribute(e.getAttributeId(), e.getMaturityLevelId(), e.getWeight()))
            .toList();
    }
}
