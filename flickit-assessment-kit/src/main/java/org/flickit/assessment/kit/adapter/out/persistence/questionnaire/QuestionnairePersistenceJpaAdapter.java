package org.flickit.assessment.kit.adapter.out.persistence.questionnaire;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.data.jpa.kit.assessmentkit.AssessmentKitJpaRepository;
import org.flickit.assessment.data.jpa.kit.question.QuestionJpaEntity;
import org.flickit.assessment.data.jpa.kit.question.QuestionJpaRepository;
import org.flickit.assessment.data.jpa.kit.questionnaire.QuestionnaireJpaEntity;
import org.flickit.assessment.data.jpa.kit.questionnaire.QuestionnaireJpaRepository;
import org.flickit.assessment.data.jpa.kit.seq.KitDbSequenceGenerators;
import org.flickit.assessment.data.jpa.kit.subject.SubjectJpaEntity;
import org.flickit.assessment.data.jpa.kit.subject.SubjectJpaRepository;
import org.flickit.assessment.kit.adapter.out.persistence.question.QuestionMapper;
import org.flickit.assessment.kit.application.domain.Question;
import org.flickit.assessment.kit.application.domain.Questionnaire;
import org.flickit.assessment.kit.application.port.out.questionnaire.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toMap;
import static org.flickit.assessment.kit.common.ErrorMessageKey.KIT_ID_NOT_FOUND;
import static org.flickit.assessment.kit.common.ErrorMessageKey.QUESTIONNAIRE_ID_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class QuestionnairePersistenceJpaAdapter implements
    CreateQuestionnairePort,
    UpdateQuestionnairePort,
    LoadQuestionnairesPort,
    LoadKitQuestionnaireDetailPort,
    DeleteQuestionnairePort {

    private final QuestionnaireJpaRepository repository;
    private final AssessmentKitJpaRepository assessmentKitRepository;
    private final QuestionJpaRepository questionRepository;
    private final SubjectJpaRepository subjectRepository;
    private final KitDbSequenceGenerators sequenceGenerators;

    @Override
    public Long persist(Questionnaire questionnaire, long kitVersionId, UUID createdBy) {
        var entity = QuestionnaireMapper.mapToJpaEntityToPersist(questionnaire, kitVersionId, createdBy);
        entity.setId(sequenceGenerators.generateQuestionnaireId());
        return repository.save(entity).getId();
    }

    @Override
    public void update(UpdateQuestionnairePort.Param param) {
        if (!repository.existsByIdAndKitVersionId(param.id(), param.kitVersionId()))
            throw new ResourceNotFoundException(QUESTIONNAIRE_ID_NOT_FOUND);

        repository.update(param.id(),
            param.kitVersionId(),
            param.title(),
            param.code(),
            param.index(),
            param.description(),
            param.lastModificationTime(),
            param.lastModifiedBy());
    }

    @Override
    public void updateOrders(UpdateQuestionnairePort.UpdateOrderParam param) {
        Map<Long, Integer> idToIndex = param.orders().stream()
            .collect(toMap(
                UpdateOrderParam.QuestionnaireOrder::questionnaireId,
                UpdateOrderParam.QuestionnaireOrder::index));

        List<QuestionnaireJpaEntity> entities = repository.findAllByIdInAndKitVersionId(idToIndex.keySet(), param.kitVersionId());
        if (entities.size() != param.orders().size())
            throw new ResourceNotFoundException(QUESTIONNAIRE_ID_NOT_FOUND);

        entities.forEach(x -> {
            int newIndex = idToIndex.get(x.getId());
            x.setIndex(newIndex);
            x.setLastModificationTime(param.lastModificationTime());
            x.setLastModifiedBy(param.lastModifiedBy());
        });
        repository.saveAll(entities);
    }

    @Override
    public List<Questionnaire> loadByKitId(Long kitId) {
        var kitVersionId = assessmentKitRepository.findById(kitId)
            .orElseThrow(() -> new ResourceNotFoundException(KIT_ID_NOT_FOUND))
            .getKitVersionId();

        return repository.findAllByKitVersionIdOrderByIndex(kitVersionId).stream()
            .map(QuestionnaireMapper::mapToDomainModel)
            .toList();
    }

    @Override
    public List<Questionnaire> loadQuestionnairesWithoutQuestion(long kitVersionId) {
        return repository.findAllByKitVersionIdAndWithoutQuestion(kitVersionId).stream()
            .map(QuestionnaireMapper::mapToDomainModel)
            .toList();
    }

    @Override
    public PaginatedResponse<LoadQuestionnairesPort.Result> loadAllByKitVersionId(long kitVersionId, int page, int size) {
        var pageResult = repository.findAllWithQuestionCountByKitVersionId(kitVersionId, PageRequest.of(page, size));
        var items = pageResult.getContent().stream()
            .map(e -> new LoadQuestionnairesPort.Result(QuestionnaireMapper.mapToDomainModel(e.getQuestionnaire()),
                e.getQuestionCount()))
            .toList();

        return new PaginatedResponse<>(
            items,
            pageResult.getNumber(),
            pageResult.getSize(),
            QuestionnaireJpaEntity.Fields.index,
            Sort.Direction.ASC.name().toLowerCase(),
            (int) pageResult.getTotalElements()
        );
    }

    @Override
    public LoadKitQuestionnaireDetailPort.Result loadKitQuestionnaireDetail(Long questionnaireId, Long kitVersionId) {
        QuestionnaireJpaEntity questionnaireEntity = repository.findByIdAndKitVersionId(questionnaireId, kitVersionId)
            .orElseThrow(() ->  new ResourceNotFoundException(QUESTIONNAIRE_ID_NOT_FOUND));

        List<QuestionJpaEntity> questionEntities = questionRepository.findAllByQuestionnaireIdAndKitVersionIdOrderByIndex(questionnaireId, kitVersionId, null)
            .getContent();
        List<SubjectJpaEntity> subjectEntities = subjectRepository.findAllByQuestionnaireIdAndKitVersionId(questionnaireId, kitVersionId);

        List<String> relatedSubjects = subjectEntities.stream()
            .map(SubjectJpaEntity::getTitle)
            .toList();

        List<Question> questions = questionEntities.stream()
            .map(QuestionMapper::mapToDomainModel)
            .toList();

        return new LoadKitQuestionnaireDetailPort.Result(questionEntities.size(),
            relatedSubjects,
            questionnaireEntity.getDescription(),
            questions);
    }

    @Override
    public void delete(long questionnaireId, long kitVersionId) {
        if (!repository.existsByIdAndKitVersionId(questionnaireId, kitVersionId))
            throw new ResourceNotFoundException(QUESTIONNAIRE_ID_NOT_FOUND);

        repository.deleteByIdAndKitVersionId(questionnaireId, kitVersionId);
    }
}
