package org.flickit.assessment.kit.adapter.out.persistence.updatekitbydsl;

import lombok.AllArgsConstructor;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.data.jpa.kit.answeroption.AnswerOptionJpaEntity;
import org.flickit.assessment.data.jpa.kit.answeroption.AnswerOptionJpaRepository;
import org.flickit.assessment.data.jpa.kit.answerrange.AnswerRangeJpaEntity;
import org.flickit.assessment.data.jpa.kit.answerrange.AnswerRangeJpaRepository;
import org.flickit.assessment.data.jpa.kit.assessmentkit.AssessmentKitJpaEntity;
import org.flickit.assessment.data.jpa.kit.assessmentkit.AssessmentKitJpaRepository;
import org.flickit.assessment.data.jpa.kit.attribute.AttributeJpaRepository;
import org.flickit.assessment.data.jpa.kit.levelcompetence.LevelCompetenceJpaRepository;
import org.flickit.assessment.data.jpa.kit.maturitylevel.MaturityLevelJpaRepository;
import org.flickit.assessment.data.jpa.kit.question.QuestionJpaRepository;
import org.flickit.assessment.data.jpa.kit.questionimpact.QuestionImpactJpaRepository;
import org.flickit.assessment.data.jpa.kit.questionnaire.QuestionnaireJpaRepository;
import org.flickit.assessment.data.jpa.kit.subject.SubjectJpaRepository;
import org.flickit.assessment.kit.adapter.out.persistence.answeroption.AnswerOptionMapper;
import org.flickit.assessment.kit.adapter.out.persistence.answerrange.AnswerRangeMapper;
import org.flickit.assessment.kit.adapter.out.persistence.attribute.AttributeMapper;
import org.flickit.assessment.kit.adapter.out.persistence.levelcompetence.MaturityLevelCompetenceMapper;
import org.flickit.assessment.kit.adapter.out.persistence.maturitylevel.MaturityLevelMapper;
import org.flickit.assessment.kit.adapter.out.persistence.question.QuestionMapper;
import org.flickit.assessment.kit.adapter.out.persistence.questionimpact.QuestionImpactMapper;
import org.flickit.assessment.kit.adapter.out.persistence.questionnaire.QuestionnaireMapper;
import org.flickit.assessment.kit.adapter.out.persistence.subject.SubjectMapper;
import org.flickit.assessment.kit.application.domain.*;
import org.flickit.assessment.kit.application.port.out.assessmentkit.LoadAssessmentKitFullInfoPort;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.flickit.assessment.kit.common.ErrorMessageKey.KIT_ID_NOT_FOUND;

@Component
@AllArgsConstructor
public class LoadAssessmentKitFullInfoAdapter implements
    LoadAssessmentKitFullInfoPort {

    private final AssessmentKitJpaRepository repository;
    private final MaturityLevelJpaRepository maturityLevelRepository;
    private final LevelCompetenceJpaRepository levelCompetenceRepository;
    private final SubjectJpaRepository subjectRepository;
    private final AttributeJpaRepository attributeRepository;
    private final QuestionnaireJpaRepository questionnaireRepository;
    private final QuestionJpaRepository questionRepository;
    private final QuestionImpactJpaRepository questionImpactRepository;
    private final AnswerOptionJpaRepository answerOptionRepository;
    private final AnswerRangeJpaRepository answerRangeRepository;

    @Override
    public AssessmentKit load(Long kitId) {
        AssessmentKitJpaEntity entity = repository.findById(kitId).orElseThrow(
            () -> new ResourceNotFoundException(KIT_ID_NOT_FOUND));
        Long activeVersionId = entity.getKitVersionId();

        List<Subject> subjects = subjectRepository.findAllByKitVersionIdOrderByIndex(activeVersionId).stream()
            .map(e -> {
                List<Attribute> attributes = attributeRepository.findAllBySubjectIdAndKitVersionId(e.getId(), activeVersionId).stream()
                    .map(AttributeMapper::mapToDomainModel)
                    .toList();
                return SubjectMapper.mapToDomainModel(e, attributes);})
            .toList();

        List<MaturityLevel> levels = maturityLevelRepository.findAllByKitVersionIdOrderByIndex(activeVersionId).stream()
            .map(MaturityLevelMapper::mapToDomainModel)
            .toList();
        setLevelCompetences(levels, activeVersionId);

        List<Question> questions = questionRepository.findAllByKitVersionId(activeVersionId).stream()
            .map(QuestionMapper::mapToDomainModel)
            .toList();

        Map<Long, List<AnswerOptionJpaEntity>> answerRangeIdToAnswerOptionsMap = answerOptionRepository
            .findAllByKitVersionId(activeVersionId, Sort.by(AnswerOptionJpaEntity.Fields.index)).stream()
            .collect(Collectors.groupingBy(AnswerOptionJpaEntity::getAnswerRangeId, LinkedHashMap::new, Collectors.toList()));

        setQuestionImpacts(questions, activeVersionId);
        setQuestionOptions(questions, answerRangeIdToAnswerOptionsMap);

        List<Questionnaire> questionnaires = questionnaireRepository.findAllByKitVersionIdOrderByIndex(activeVersionId).stream()
            .map(QuestionnaireMapper::mapToDomainModel)
            .toList();
        setQuestions(questionnaires, questions);

        List<AnswerRange> reusableAnswerRanges = answerRangeRepository.findAllByKitVersionId(activeVersionId).stream()
            .filter(AnswerRangeJpaEntity::isReusable)
            .map(a -> {
                var options = answerRangeIdToAnswerOptionsMap.get(a.getId()).stream()
                    .map(AnswerOptionMapper::mapToDomainModel)
                    .toList();
                return AnswerRangeMapper.toDomainModel(a, options);
            })
            .toList();

        return new AssessmentKit(
            kitId,
            entity.getCode(),
            entity.getTitle(),
            entity.getSummary(),
            entity.getAbout(),
            KitLanguage.values()[entity.getLanguageId()].name(),
            entity.getCreationTime(),
            entity.getLastModificationTime(),
            entity.getPublished(),
            entity.getIsPrivate(),
            entity.getExpertGroupId(),
            subjects,
            levels,
            questionnaires,
            reusableAnswerRanges,
            activeVersionId);
    }

    private void setLevelCompetences(List<MaturityLevel> levels, Long kitVersionId) {
        levels.forEach(level -> level.setCompetences(
            levelCompetenceRepository.findByAffectedLevelIdAndKitVersionId(level.getId(), kitVersionId).stream()
                .map(MaturityLevelCompetenceMapper::mapToDomainModel)
                .toList()));
    }

    private void setQuestionImpacts(List<Question> questions, long kitVersionId) {
        questions.forEach(question -> question.setImpacts(
            questionImpactRepository.findAllByQuestionIdAndKitVersionId(question.getId(), kitVersionId).stream()
                .map(QuestionImpactMapper::mapToDomainModel)
                .toList()
        ));
    }

    private void setQuestionOptions(List<Question> questions, Map<Long, List<AnswerOptionJpaEntity>> answerRangeIdToAnswerOptionsMap) {
        questions.forEach(q -> q.setOptions(answerRangeIdToAnswerOptionsMap.get(q.getAnswerRangeId()).stream()
            .map(AnswerOptionMapper::mapToDomainModel)
            .toList()));
    }

    private void setQuestions(List<Questionnaire> questionnaires, List<Question> questions) {
        Map<Long, List<Question>> groupedQuestions = questions.stream().collect(Collectors.groupingBy(Question::getQuestionnaireId));
        questionnaires.forEach(q -> q.setQuestions(groupedQuestions.get(q.getId())));
    }
}
