package org.flickit.assessment.kit.adapter.out.persistence.attribute;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.data.jpa.kit.attribute.AttributeJpaEntity;
import org.flickit.assessment.data.jpa.kit.attribute.AttributeJpaEntity.Fields;
import org.flickit.assessment.data.jpa.kit.attribute.AttributeJpaRepository;
import org.flickit.assessment.data.jpa.kit.seq.KitDbSequenceGenerators;
import org.flickit.assessment.data.jpa.kit.subject.SubjectJpaEntity;
import org.flickit.assessment.data.jpa.kit.subject.SubjectJpaRepository;
import org.flickit.assessment.kit.adapter.out.persistence.subject.SubjectMapper;
import org.flickit.assessment.kit.application.domain.Attribute;
import org.flickit.assessment.kit.application.domain.AttributeWithSubject;
import org.flickit.assessment.kit.application.port.out.attribute.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.flickit.assessment.kit.adapter.out.persistence.attribute.AttributeMapper.mapToDomainModel;
import static org.flickit.assessment.kit.adapter.out.persistence.attribute.AttributeMapper.mapToJpaEntity;
import static org.flickit.assessment.kit.common.ErrorMessageKey.*;

@Component
@RequiredArgsConstructor
public class AttributePersistenceJpaAdapter implements
    UpdateAttributePort,
    CreateAttributePort,
    LoadAttributePort,
    CountAttributeImpactfulQuestionsPort,
    DeleteAttributePort,
    LoadAttributesPort {

    private final AttributeJpaRepository repository;
    private final SubjectJpaRepository subjectRepository;
    private final KitDbSequenceGenerators sequenceGenerators;

    @Override
    public void update(UpdateAttributePort.Param param) {
        if (!repository.existsByIdAndKitVersionId(param.id(), param.kitVersionId()))
            throw new ResourceNotFoundException(ATTRIBUTE_ID_NOT_FOUND);

        repository.update(param.id(),
            param.kitVersionId(),
            param.code(),
            param.title(),
            param.index(),
            param.description(),
            param.weight(),
            param.lastModificationTime(),
            param.lastModifiedBy(),
            param.subjectId());
    }

    @Override
    public void updateOrders(UpdateOrderParam param) {
        List<Long> ids = param.orders().stream()
            .map(UpdateOrderParam.AttributeOrder::attributeId)
            .toList();

        Map<AttributeJpaEntity.EntityId, Integer> idToIndex = param.orders().stream()
            .collect(Collectors.toMap(
                ml -> new AttributeJpaEntity.EntityId(ml.attributeId(), param.kitVersionId()),
                UpdateOrderParam.AttributeOrder::index
            ));
        List<AttributeJpaEntity> entities =
            repository.findAllByIdInAndKitVersionIdAndSubjectId(ids, param.kitVersionId(), param.subjectId());
        if (entities.size() != param.orders().size())
            throw new ResourceNotFoundException(ATTRIBUTE_ID_NOT_FOUND);

        entities.forEach(x -> {
            int newIndex = idToIndex.get(new AttributeJpaEntity.EntityId(x.getId(), param.kitVersionId()));
            x.setIndex(newIndex);
            x.setLastModificationTime(param.lastModificationTime());
            x.setLastModifiedBy(param.lastModifiedBy());
        });
        repository.saveAll(entities);
    }

    @Override
    public Long persist(Attribute attribute, Long subjectId, Long kitVersionId) {
        var subjectEntityId = new SubjectJpaEntity.EntityId(subjectId, kitVersionId);
        SubjectJpaEntity subjectJpaEntity = subjectRepository.findById(subjectEntityId)
            .orElseThrow(() -> new ResourceNotFoundException(CREATE_ATTRIBUTE_SUBJECT_ID_NOT_FOUND));

        var entity = mapToJpaEntity(attribute, subjectJpaEntity);
        entity.setId(sequenceGenerators.generateAttributeId());
        return repository.save(entity).getId();
    }

    @Override
    public Attribute load(Long attributeId, Long kitVersionId) {
        var attribute = repository.findByIdAndKitVersionId(attributeId, kitVersionId)
            .orElseThrow(() -> new ResourceNotFoundException(GET_KIT_ATTRIBUTE_DETAIL_ATTRIBUTE_ID_NOT_FOUND));
        return mapToDomainModel(attribute);
    }

    @Override
    public List<Attribute> loadUnimpactedAttributes(long kitVersionId) {
        return repository.findAllByKitVersionIdAndWithoutImpact(kitVersionId)
            .stream()
            .map(AttributeMapper::mapToDomainModel)
            .toList();
    }

    @Override
    public int countQuestions(long attributeId, long kitVersionId) {
        return repository.countAttributeImpactfulQuestions(attributeId, kitVersionId);
    }

    @Override
    public List<Attribute> loadAllByIdsAndKitVersionId(List<Long> attributeIds, long kitVersionId) {
        return repository.findAllByIdInAndKitVersionId(attributeIds, kitVersionId).stream()
            .map(AttributeMapper::mapToDomainModel)
            .toList();
    }

    @Override
    public void delete(long attributeId, long kitVersionId) {
        if (!repository.existsByIdAndKitVersionId(attributeId, kitVersionId))
            throw new ResourceNotFoundException(ATTRIBUTE_ID_NOT_FOUND);

        repository.deleteByIdAndKitVersionId(attributeId, kitVersionId);
    }

    @Override
    public PaginatedResponse<AttributeWithSubject> loadByKitVersionId(long kitVersionId, int size, int page) {
        var pageResult = repository.findAllByKitVersionId(kitVersionId, PageRequest.of(page, size));

        var items = pageResult.getContent().stream()
            .map(x -> new AttributeWithSubject(mapToDomainModel(x.getAttribute()), SubjectMapper.mapToDomainModel(x.getSubject(), null)))
            .toList();

        return new PaginatedResponse<>(items,
            pageResult.getNumber(),
            pageResult.getSize(),
            Fields.index,
            Sort.Direction.ASC.name().toLowerCase(),
            (int) pageResult.getTotalElements());
    }
}
