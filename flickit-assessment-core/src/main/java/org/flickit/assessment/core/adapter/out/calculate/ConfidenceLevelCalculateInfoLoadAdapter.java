package org.flickit.assessment.core.adapter.out.calculate;

import lombok.AllArgsConstructor;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.core.adapter.out.persistence.kit.attribute.AttributeMapper;
import org.flickit.assessment.core.adapter.out.persistence.kit.subject.SubjectMapper;
import org.flickit.assessment.core.adapter.out.persistence.kit.question.QuestionMapper;
import org.flickit.assessment.core.adapter.out.persistence.kit.questionimpact.QuestionImpactMother;
import org.flickit.assessment.core.application.domain.*;
import org.flickit.assessment.core.application.port.out.assessmentresult.LoadConfidenceLevelCalculateInfoPort;
import org.flickit.assessment.data.jpa.core.answer.AnswerJpaEntity;
import org.flickit.assessment.data.jpa.core.answer.AnswerJpaRepository;
import org.flickit.assessment.data.jpa.core.assessment.AssessmentJpaEntity;
import org.flickit.assessment.data.jpa.core.assessmentresult.AssessmentResultJpaEntity;
import org.flickit.assessment.data.jpa.core.assessmentresult.AssessmentResultJpaRepository;
import org.flickit.assessment.data.jpa.core.attributevalue.QualityAttributeValueJpaEntity;
import org.flickit.assessment.data.jpa.core.attributevalue.QualityAttributeValueJpaRepository;
import org.flickit.assessment.data.jpa.core.subjectvalue.SubjectValueJpaEntity;
import org.flickit.assessment.data.jpa.core.subjectvalue.SubjectValueJpaRepository;
import org.flickit.assessment.data.jpa.kit.attribute.AttributeJpaEntity;
import org.flickit.assessment.data.jpa.kit.subject.SubjectJpaEntity;
import org.flickit.assessment.data.jpa.kit.subject.SubjectJpaRepository;
import org.flickit.assessment.data.jpa.kit.question.QuestionJoinQuestionImpactView;
import org.flickit.assessment.data.jpa.kit.question.QuestionJpaRepository;
import org.flickit.assessment.data.jpa.kit.questionimpact.QuestionImpactJpaEntity;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.stream.Collectors.*;
import static org.flickit.assessment.core.adapter.out.persistence.assessment.AssessmentMapper.mapToDomainModel;
import static org.flickit.assessment.core.common.ErrorMessageKey.CALCULATE_CONFIDENCE_ASSESSMENT_RESULT_NOT_FOUND;

@Component
@AllArgsConstructor
public class ConfidenceLevelCalculateInfoLoadAdapter implements LoadConfidenceLevelCalculateInfoPort {

    private final AssessmentResultJpaRepository assessmentResultRepo;
    private final AnswerJpaRepository answerRepo;
    private final QualityAttributeValueJpaRepository attributeValueRepo;
    private final SubjectValueJpaRepository subjectValueRepo;
    private final QuestionJpaRepository questionRepository;
    private final SubjectJpaRepository subjectRepository;

    record Context(List<AnswerJpaEntity> allAnswerEntities,
                   List<QualityAttributeValueJpaEntity> allAttributeValueEntities,
                   List<SubjectValueJpaEntity> subjectValueEntities,
                   Map<Long, SubjectJpaEntity> subjectIdToDto,
                   Map<Long, Integer> attributeIdToWeightMap,
                   Map<Long, List<QuestionImpactJpaEntity>> questionIdToImpactMap) {
    }

    @Override
    public AssessmentResult load(UUID assessmentId) {
        AssessmentResultJpaEntity assessmentResultEntity = assessmentResultRepo.findFirstByAssessment_IdOrderByLastModificationTimeDesc(assessmentId)
            .orElseThrow(() -> new ResourceNotFoundException(CALCULATE_CONFIDENCE_ASSESSMENT_RESULT_NOT_FOUND));
        UUID assessmentResultId = assessmentResultEntity.getId();
        Long assessmentKitId = assessmentResultEntity.getAssessment().getAssessmentKitId();

        /*
         load all subjectValue and attributeValue entities
         that are already saved with this assessmentResult
         */
        var subjectValueEntities = subjectValueRepo.findByAssessmentResultId(assessmentResultId);
        var allAttributeValueEntities = attributeValueRepo.findByAssessmentResultId(assessmentResultId);

        /*
        load all subjects and their related attributes (by assessmentKit)
        and create some useful utility maps
        */
        List<SubjectJpaEntity> subjectsWithAttributes = subjectRepository.loadByKitIdWithAttributes(assessmentKitId);
        Map<Long, SubjectJpaEntity> subjectIdToEntity = subjectsWithAttributes.stream()
            .collect(toMap(SubjectJpaEntity::getId, x -> x, (s1, s2) -> s1));
        Map<Long, Integer> qaIdToWeightMap = subjectsWithAttributes.stream()
            .flatMap(x -> x.getAttributes().stream())
            .collect(toMap(AttributeJpaEntity::getId, AttributeJpaEntity::getWeight));
        Map<Long, List<QualityAttribute>> subjectIdToAttribute = subjectsWithAttributes.stream()
            .collect(toMap(SubjectJpaEntity::getId,
                map -> map.getAttributes().stream().map(AttributeMapper::mapToDomainModel).toList()
            ));

        // load all questions with their impacts (by assessmentKit)
        List<QuestionJoinQuestionImpactView> allQuestionsJoinImpactViews = questionRepository.loadByAssessmentKitId(assessmentKitId);
        Map<Long, List<QuestionJoinQuestionImpactView>> questionIdToViewMap = allQuestionsJoinImpactViews.stream()
            .collect(groupingBy(x -> x.getQuestion().getId()));
        Map<Long, List<QuestionImpactJpaEntity>> questionIdToImpactMap = questionIdToViewMap.values().stream()
            .collect(toMap(map -> map.stream().findFirst().orElseThrow().getQuestion().getId(),
                map -> map.stream().map(QuestionJoinQuestionImpactView::getQuestionImpact).filter(Objects::nonNull).toList()));


        // load all answers submitted with this assessmentResult
        var allAnswerEntities = answerRepo.findByAssessmentResultId(assessmentResultId);

        Context context = new Context(
            allAnswerEntities,
            allAttributeValueEntities,
            subjectValueEntities,
            subjectIdToEntity,
            qaIdToWeightMap,
            questionIdToImpactMap);

        Map<Long, QualityAttributeValue> attributeIdToValueMap = buildAttributeValues(context);

        List<SubjectValue> subjectValues = buildSubjectValues(attributeIdToValueMap, subjectIdToAttribute, context);

        return new AssessmentResult(
            assessmentResultId,
            buildAssessment(assessmentResultEntity.getAssessment()),
            subjectValues);
    }

    /**
     * build attributeValues domain
     * with all information needed for calculate their maturity levels
     * @param context all previously loaded data
     * @return a map of each attributeId to it's corresponding attributeValue
     */
    private Map<Long, QualityAttributeValue> buildAttributeValues(Context context) {
        Map<Long, QualityAttributeValue> attributeIdToValueMap = new HashMap<>();
        for (QualityAttributeValueJpaEntity qavEntity : context.allAttributeValueEntities) {
            List<Question> impactfulQuestions = questionsWithImpact(qavEntity.getQualityAttributeId(), context);
            List<Answer> impactfulAnswers = answersOfImpactfulQuestions(impactfulQuestions, context);
            QualityAttribute attribute = new QualityAttribute(
                qavEntity.getQualityAttributeId(),
                context.attributeIdToWeightMap.get(qavEntity.getQualityAttributeId()),
                impactfulQuestions
            );

            var attributeValue = new QualityAttributeValue(qavEntity.getId(), attribute, impactfulAnswers);

            attributeIdToValueMap.put(attribute.getId(), attributeValue);
        }
        return attributeIdToValueMap;
    }

    /**
     * @param attributeId id of intended attribute to extract its impactful questions
     * @param context all previously loaded data
     * @return list of questions with at least one impact on the given attribute
     */
    private List<Question> questionsWithImpact(Long attributeId, Context context) {
        return context.questionIdToImpactMap.entrySet().stream()
            .filter(q -> q.getValue().stream().anyMatch(i -> i.getAttributeId().equals(attributeId)))
            .map(q -> QuestionMapper.mapToDomainModel(q.getKey(), q.getValue().stream().map(QuestionImpactMother::mapToDomainModel).toList()))
            .toList();
    }

    /**
     * @param impactfulQuestions subset of questions extracted in {@linkplain ConfidenceLevelCalculateInfoLoadAdapter#questionsWithImpact} method
     * @param context all previously loaded data
     * @return list of answers related to the given list of questions,
     * it is possible that no answer is submitted for any of these questions
     * returning list has {minSize = 0}, {maxSize = size of input questionList}
     */
    private List<Answer> answersOfImpactfulQuestions(List<Question> impactfulQuestions, Context context) {
        Set<Long> impactfulQuestionIds = impactfulQuestions.stream()
            .map(Question::getId)
            .collect(toSet());

        return context.allAnswerEntities.stream()
            .filter(a -> impactfulQuestionIds.contains(a.getQuestionId()))
            .map(entity -> {
                AnswerOption answerOption = null;
                if (entity.getAnswerOptionId() != null) {
                    answerOption = new AnswerOption(entity.getAnswerOptionId(), entity.getQuestionId(), null);
                }
                return new Answer(
                    entity.getId(),
                    answerOption,
                    entity.getQuestionId(),
                    entity.getConfidenceLevelId(),
                    entity.getIsNotApplicable());
            }).toList();
    }

    /**
     * build subjectValues domain with all information needed for calculate their maturity levels
     *
     * @param attributeIdToValueMap map of attributeIds to their corresponding value
     * @param subjectIdToAttribute map of subjectId to list of it's attributes
     * @param context               all previously loaded data
     * @return list of subjectValues
     */
    private static List<SubjectValue> buildSubjectValues(Map<Long, QualityAttributeValue> attributeIdToValueMap, Map<Long, List<QualityAttribute>> subjectIdToAttribute, Context context) {
        List<SubjectValue> subjectValues = new ArrayList<>();
        for (SubjectValueJpaEntity svEntity : context.subjectValueEntities) {
            SubjectJpaEntity entity = context.subjectIdToDto.get(svEntity.getSubjectId());
            List<QualityAttribute> attributes = subjectIdToAttribute.get(entity.getId());
            List<QualityAttributeValue> qavList = attributes.stream()
                .map(q -> attributeIdToValueMap.get(q.getId()))
                .filter(Objects::nonNull)
                .toList();
            if (qavList.isEmpty()) {
                continue;
            }
            subjectValues.add(new SubjectValue(svEntity.getId(), SubjectMapper.mapToDomainModel(entity, attributes), qavList));
        }
        return subjectValues;
    }

    /**
     * @param assessmentEntity loaded assessment entity
     * @return assessment with all information needed for calculation
     */
    private Assessment buildAssessment(AssessmentJpaEntity assessmentEntity) {
        AssessmentKit kit = new AssessmentKit(assessmentEntity.getAssessmentKitId(), null);
        return mapToDomainModel(assessmentEntity, kit);
    }
}
