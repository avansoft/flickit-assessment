package org.flickit.assessment.kit.adapter.out.persistence.kitdsl;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.data.jpa.kit.answeroption.AnswerOptionJpaEntity;
import org.flickit.assessment.data.jpa.kit.answerrange.AnswerRangeJoinOptionView;
import org.flickit.assessment.data.jpa.kit.answerrange.AnswerRangeJpaEntity;
import org.flickit.assessment.data.jpa.kit.answerrange.AnswerRangeJpaRepository;
import org.flickit.assessment.data.jpa.kit.attribute.AttributeJoinSubjectView;
import org.flickit.assessment.data.jpa.kit.attribute.AttributeJpaEntity;
import org.flickit.assessment.data.jpa.kit.attribute.AttributeJpaRepository;
import org.flickit.assessment.data.jpa.kit.levelcompetence.LevelCompetenceJpaEntity;
import org.flickit.assessment.data.jpa.kit.maturitylevel.MaturityJoinCompetenceView;
import org.flickit.assessment.data.jpa.kit.maturitylevel.MaturityLevelJpaEntity;
import org.flickit.assessment.data.jpa.kit.maturitylevel.MaturityLevelJpaRepository;
import org.flickit.assessment.data.jpa.kit.question.QuestionJoinQuestionImpactView;
import org.flickit.assessment.data.jpa.kit.question.QuestionJpaEntity;
import org.flickit.assessment.data.jpa.kit.question.QuestionJpaRepository;
import org.flickit.assessment.data.jpa.kit.questionimpact.QuestionImpactJpaEntity;
import org.flickit.assessment.data.jpa.kit.questionnaire.QuestionnaireJpaEntity;
import org.flickit.assessment.data.jpa.kit.questionnaire.QuestionnaireJpaRepository;
import org.flickit.assessment.kit.adapter.out.persistence.answeroption.AnswerOptionMapper;
import org.flickit.assessment.kit.adapter.out.persistence.answerrange.AnswerRangeMapper;
import org.flickit.assessment.kit.adapter.out.persistence.attribute.AttributeMapper;
import org.flickit.assessment.kit.adapter.out.persistence.maturitylevel.MaturityLevelMapper;
import org.flickit.assessment.kit.adapter.out.persistence.question.QuestionMapper;
import org.flickit.assessment.kit.adapter.out.persistence.questionnaire.QuestionnaireMapper;
import org.flickit.assessment.kit.adapter.out.persistence.subject.SubjectMapper;
import org.flickit.assessment.kit.application.domain.dsl.*;
import org.flickit.assessment.kit.application.port.out.kitdsl.LoadKitDslModelPort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;

@Component
@RequiredArgsConstructor
public class LoadKitDslModelAdapter implements LoadKitDslModelPort {

    private final QuestionnaireJpaRepository questionnaireRepository;
    private final AttributeJpaRepository attributeRepository;
    private final QuestionJpaRepository questionRepository;
    private final MaturityLevelJpaRepository maturityLevelRepository;
    private final AnswerRangeJpaRepository answerRangeRepository;

    @Override
    public AssessmentKitDslModel load(long kitVersionId) {
        var subjectWithAttributeViews = attributeRepository.findAllByKitVersionId(kitVersionId, Pageable.unpaged());
        var levelWithCompetenceViews = maturityLevelRepository.findAllByKitVersionIdWithCompetence(kitVersionId);
        var rangeWithOptionViews = answerRangeRepository.findAllWithOptionsByKitVersionId(kitVersionId);
        var questionnaireEntities = questionnaireRepository.findAllByKitVersionId(kitVersionId);
        var questionToImpactViews = questionRepository.loadByKitVersionId(kitVersionId);

        var questionnaireDslModels = questionnaireEntities.stream()
            .map(QuestionnaireMapper::mapToDslModel)
            .sorted(comparingInt(QuestionnaireDslModel::getIndex))
            .toList();

        var rangeToOptions = rangeWithOptionViews.stream()
            .collect(groupingBy(AnswerRangeJoinOptionView::getAnswerRange,
                mapping(AnswerRangeJoinOptionView::getAnswerOption, toList())));

        var rangeDslModels = rangeToOptions.entrySet().stream()
            .filter(e -> e.getKey().isReusable())
            .filter(e -> e.getValue().size() > 1)
            .map(e -> {
                var dslOptions = e.getValue().stream()
                    .map(AnswerOptionMapper::mapToDslModel)
                    .toList();
                return AnswerRangeMapper.mapToDslModel(e.getKey(), dslOptions);
            })
            .toList();

        var attributeDslModels = subjectWithAttributeViews.stream()
            .map(AttributeMapper::mapToDslModel)
            .sorted(comparingInt(AttributeDslModel::getIndex))
            .toList();

        var subjectDslModels = subjectWithAttributeViews.stream()
            .collect(groupingBy(AttributeJoinSubjectView::getSubject))
            .keySet().stream()
            .map(SubjectMapper::mapToDslModel)
            .sorted(comparingInt(SubjectDslModel::getIndex))
            .toList();

        var levelToCompetences = levelWithCompetenceViews.stream()
            .collect(groupingBy(MaturityJoinCompetenceView::getMaturityLevel,
                mapping(MaturityJoinCompetenceView::getLevelCompetence, toList())));

        var levelIdToLevel = levelWithCompetenceViews.stream()
            .collect(Collectors.toMap(e -> e.getMaturityLevel().getId(),
                MaturityJoinCompetenceView::getMaturityLevel,
                (existing, duplicate) -> existing));

        var levelDslModels = createLevelDslModels(levelToCompetences, levelIdToLevel);

        var questionToImpacts = questionToImpactViews.stream()
            .collect(Collectors.groupingBy(QuestionJoinQuestionImpactView::getQuestion,
                Collectors.mapping(QuestionJoinQuestionImpactView::getQuestionImpact, Collectors.toList())));

        var questionnaireIdToCode = questionnaireEntities.stream()
            .collect(Collectors.toMap(QuestionnaireJpaEntity::getId, QuestionnaireJpaEntity::getCode));

        var attrIdToCode = subjectWithAttributeViews.stream()
            .map(AttributeJoinSubjectView::getAttribute)
            .collect(Collectors.toMap(AttributeJpaEntity::getId, AttributeJpaEntity::getCode));

        var questionDslModels = createQuestionDslModels(questionToImpacts,
            rangeToOptions,
            questionnaireIdToCode,
            attrIdToCode,
            levelIdToLevel);

        return AssessmentKitDslModel.builder()
            .questionnaires(questionnaireDslModels)
            .attributes(attributeDslModels)
            .questions(questionDslModels)
            .subjects(subjectDslModels)
            .maturityLevels(levelDslModels)
            .answerRanges(rangeDslModels)
            .build();

    }

    private List<MaturityLevelDslModel> createLevelDslModels(Map<MaturityLevelJpaEntity, List<LevelCompetenceJpaEntity>> levelToCompetences,
                                                                    Map<Long, MaturityLevelJpaEntity> levelIdToLevel) {
        return levelToCompetences.entrySet().stream()
            .map(entry -> {
                var competenceCodeToValue = entry.getValue().stream()
                    .filter(Objects::nonNull)
                    .collect(toMap(e -> levelIdToLevel.get(e.getEffectiveLevelId()).getCode(),
                        LevelCompetenceJpaEntity::getValue));
                return MaturityLevelMapper.mapToDslModel(entry.getKey(), competenceCodeToValue);
            })
            .sorted(comparingInt(MaturityLevelDslModel::getIndex))
            .toList();
    }

    private List<QuestionDslModel> createQuestionDslModels(Map<QuestionJpaEntity, List<QuestionImpactJpaEntity>> questionToImpacts,
                                                           Map<AnswerRangeJpaEntity, List<AnswerOptionJpaEntity>> rangeToOptions,
                                                           Map<Long, String> questionnaireIdToCode,
                                                           Map<Long, String> attrIdToCode,
                                                           Map<Long, MaturityLevelJpaEntity> levelIdToLevel) {
        return questionToImpacts.entrySet().stream()
            .map(e -> {
                var questionEntity = e.getKey();
                var rangeIdToEntity = rangeToOptions.keySet().stream()
                    .collect(Collectors.toMap(AnswerRangeJpaEntity::getId, Function.identity()));
                AnswerRangeJpaEntity answerRangeJpaEntity = rangeIdToEntity.get(questionEntity.getAnswerRangeId());
                var optionIndexToValue = rangeToOptions.get(answerRangeJpaEntity).stream()
                    .collect(Collectors.toMap(AnswerOptionJpaEntity::getIndex, AnswerOptionJpaEntity::getValue));
                var questionnaireCode = questionnaireIdToCode.get(questionEntity.getQuestionnaireId());
                var impacts = e.getValue().stream()
                    .map(i -> QuestionImpactDslModel.builder()
                        .attributeCode(attrIdToCode.get(i.getAttributeId()))
                        .maturityLevel(MaturityLevelDslModel.builder()
                            .code(levelIdToLevel.get(i.getMaturityLevelId()).getCode())
                            .title(levelIdToLevel.get(i.getMaturityLevelId()).getTitle())
                            .build())
                        .weight(i.getWeight())
                        .optionsIndextoValueMap(optionIndexToValue)
                        .build())
                    .toList();

                String rangeCode = answerRangeJpaEntity.isReusable() ? answerRangeJpaEntity.getCode() : null;
                List<AnswerOptionDslModel> answerOptionDslModels = answerRangeJpaEntity.isReusable() ? null :
                    rangeToOptions.get(answerRangeJpaEntity).stream()
                        .map(AnswerOptionMapper::mapToDslModel)
                        .toList();
                return QuestionMapper.mapToDslModel(questionEntity,
                    questionnaireCode,
                    rangeCode,
                    impacts,
                    answerOptionDslModels);
            })
            .sorted(comparing(QuestionDslModel::getQuestionnaireCode).thenComparing(QuestionDslModel::getIndex))
            .toList();
    }
}
