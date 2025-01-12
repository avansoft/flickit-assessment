package org.flickit.assessment.kit.adapter.out.persistence.maturitylevel;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.data.jpa.kit.levelcompetence.LevelCompetenceJpaRepository;
import org.flickit.assessment.data.jpa.kit.maturitylevel.MaturityLevelJpaEntity;
import org.flickit.assessment.data.jpa.kit.maturitylevel.MaturityLevelJpaEntity.EntityId;
import org.flickit.assessment.data.jpa.kit.maturitylevel.MaturityLevelJpaRepository;
import org.flickit.assessment.data.jpa.kit.seq.KitDbSequenceGenerators;
import org.flickit.assessment.kit.application.domain.MaturityLevel;
import org.flickit.assessment.kit.application.port.out.maturitylevel.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.flickit.assessment.kit.adapter.out.persistence.maturitylevel.MaturityLevelMapper.mapToDomainModel;
import static org.flickit.assessment.kit.adapter.out.persistence.maturitylevel.MaturityLevelMapper.mapToJpaEntityToPersist;
import static org.flickit.assessment.kit.common.ErrorMessageKey.MATURITY_LEVEL_ID_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class MaturityLevelPersistenceJpaAdapter implements
    CreateMaturityLevelPort,
    DeleteMaturityLevelPort,
    UpdateMaturityLevelPort,
    LoadAttributeMaturityLevelsPort,
    LoadMaturityLevelsPort {

    private final MaturityLevelJpaRepository repository;
    private final LevelCompetenceJpaRepository levelCompetenceRepository;
    private final KitDbSequenceGenerators sequenceGenerators;

    @Override
    public Long persist(MaturityLevel level, Long kitVersionId, UUID createdBy) {
        var entity = mapToJpaEntityToPersist(level, kitVersionId, createdBy);
        entity.setId(sequenceGenerators.generateMaturityLevelId());
        return repository.save(entity).getId();
    }

    @Override
    public void delete(Long id, Long kitVersionId) {
        if (!repository.existsByIdAndKitVersionId(id, kitVersionId))
            throw new ResourceNotFoundException(MATURITY_LEVEL_ID_NOT_FOUND);
        repository.deleteByIdAndKitVersionId(id, kitVersionId);
    }

    @Override
    public void updateAll(List<MaturityLevel> maturityLevels, Long kitVersionId, UUID lastModifiedBy) {
        Map<EntityId, MaturityLevel> idToModel = maturityLevels.stream()
            .collect(toMap(
                ml -> new EntityId(ml.getId(), kitVersionId),
                ml -> ml
            ));

        Set<Long> ids = maturityLevels.stream()
            .map(MaturityLevel::getId)
            .collect(Collectors.toSet());

        List<MaturityLevelJpaEntity> entities = repository.findAllByIdInAndKitVersionId(ids, kitVersionId);
        entities.forEach(x -> {
            MaturityLevel newLevel = idToModel.get(new EntityId(x.getId(), kitVersionId));
            x.setIndex(newLevel.getIndex());
            x.setTitle(newLevel.getTitle());
            x.setValue(newLevel.getValue());
            x.setDescription(newLevel.getDescription());
            x.setLastModificationTime(LocalDateTime.now());
            x.setLastModifiedBy(lastModifiedBy);
        });
        repository.saveAll(entities);
        repository.flush();
    }

    @Override
    public void update(MaturityLevel maturityLevel, Long kitVersionId, LocalDateTime lastModificationTime, UUID lastModifiedBy) {
        if (!repository.existsByIdAndKitVersionId(maturityLevel.getId(), kitVersionId))
            throw new ResourceNotFoundException(MATURITY_LEVEL_ID_NOT_FOUND);

        repository.update(maturityLevel.getId(), kitVersionId, maturityLevel.getCode(), maturityLevel.getIndex(), maturityLevel.getTitle(),
            maturityLevel.getDescription(), maturityLevel.getValue(), lastModificationTime, lastModifiedBy);
    }

    @Override
    public void updateOrders(UpdateOrderParam param) {
        Map<EntityId, UpdateOrderParam.MaturityLevelOrder> idToIndex = param.orders().stream()
            .collect(Collectors.toMap(
                ml -> new EntityId(ml.maturityLevelId(), param.kitVersionId()),
                ml -> ml
            ));

        Set<Long> ids = param.orders().stream()
            .map(UpdateOrderParam.MaturityLevelOrder::maturityLevelId)
            .collect(Collectors.toSet());

        List<MaturityLevelJpaEntity> entities = repository.findAllByIdInAndKitVersionId(ids, param.kitVersionId());
        if (entities.size() != param.orders().size())
            throw new ResourceNotFoundException(MATURITY_LEVEL_ID_NOT_FOUND);

        entities.forEach(x -> {
            UpdateOrderParam.MaturityLevelOrder newLevel = idToIndex.get(new EntityId(x.getId(), param.kitVersionId()));
            x.setIndex(newLevel.index());
            x.setValue(newLevel.value());
            x.setLastModificationTime(param.lastModificationTime());
            x.setLastModifiedBy(param.lastModifiedBy());
        });
        repository.saveAll(entities);
    }

    @Override
    public List<MaturityLevel> loadAllByKitVersionId(Long kitVersionId) {
        var maturityLevelEntities = repository.findAllByKitVersionIdOrderByIndex(kitVersionId);

        var levelIds = maturityLevelEntities.stream()
            .map(MaturityLevelJpaEntity::getId)
            .toList();

        var levelCompetenceEntities = levelCompetenceRepository.findAllByAffectedLevelIdInAndKitVersionId(levelIds, kitVersionId);
        return mapToDomainModel(maturityLevelEntities, levelCompetenceEntities);
    }

    @Override
    public List<LoadAttributeMaturityLevelsPort.Result> loadAttributeLevels(long attributeId, long kitVersionId) {
        return repository.loadAttributeLevels(attributeId, kitVersionId).stream()
            .map(e -> new LoadAttributeMaturityLevelsPort.Result(e.getId(), e.getTitle(), e.getIndex(), e.getQuestionCount()))
            .toList();
    }

    @Override
    public PaginatedResponse<MaturityLevel> loadByKitVersionId(long kitVersionId, Integer size, Integer page) {
        String sort = MaturityLevelJpaEntity.Fields.index;
        Sort.Direction sortDirection = Sort.Direction.ASC;

        var pageResult = repository.findByKitVersionId(kitVersionId,
            PageRequest.of(page, size, sortDirection, sort));

        var maturityLevels = pageResult.getContent().stream()
            .map(MaturityLevelMapper::mapToDomainModel)
            .toList();

        return new PaginatedResponse<>(maturityLevels,
            pageResult.getNumber(),
            pageResult.getSize(),
            sort,
            sortDirection.name().toLowerCase(),
            (int) pageResult.getTotalElements());
    }

    @Override
    public List<MaturityLevel> loadByKitVersionId(long kitVersionId, Collection<Long> ids) {
        return repository.findAllByIdInAndKitVersionId(ids, kitVersionId).stream()
            .map(MaturityLevelMapper::mapToDomainModel)
            .toList();
    }
}
