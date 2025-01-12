package org.flickit.assessment.kit.adapter.out.persistence.subject;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.data.jpa.kit.attribute.AttributeJpaEntity;
import org.flickit.assessment.data.jpa.kit.attribute.AttributeJpaRepository;
import org.flickit.assessment.data.jpa.kit.seq.KitDbSequenceGenerators;
import org.flickit.assessment.data.jpa.kit.subject.SubjectJoinAttributeView;
import org.flickit.assessment.data.jpa.kit.subject.SubjectJpaEntity;
import org.flickit.assessment.data.jpa.kit.subject.SubjectJpaRepository;
import org.flickit.assessment.kit.adapter.out.persistence.attribute.AttributeMapper;
import org.flickit.assessment.kit.application.domain.Attribute;
import org.flickit.assessment.kit.application.domain.Subject;
import org.flickit.assessment.kit.application.port.out.subject.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.flickit.assessment.kit.adapter.out.persistence.subject.SubjectMapper.mapToDomainModel;
import static org.flickit.assessment.kit.common.ErrorMessageKey.GET_KIT_SUBJECT_DETAIL_SUBJECT_ID_NOT_FOUND;
import static org.flickit.assessment.kit.common.ErrorMessageKey.SUBJECT_ID_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class SubjectPersistenceJpaAdapter implements
    UpdateSubjectByDslPort,
    CreateSubjectPort,
    LoadSubjectsPort,
    LoadSubjectPort,
    DeleteSubjectPort,
    UpdateSubjectPort {

    private final SubjectJpaRepository repository;
    private final AttributeJpaRepository attributeRepository;
    private final KitDbSequenceGenerators sequenceGenerators;

    @Override
    public void update(UpdateSubjectByDslPort.Param param) {
        repository.update(param.id(),
            param.kitVersionId(),
            param.title(),
            param.index(),
            param.description(),
            param.lastModificationTime(),
            param.lastModifiedBy()
        );
    }

    @Override
    public Long persist(CreateSubjectPort.Param param) {
        var entity = SubjectMapper.mapToJpaEntity(param);
        entity.setId(sequenceGenerators.generateSubjectId());
        return repository.save(entity).getId();
    }

    @Override
    public List<Subject> loadByKitVersionId(long kitVersionId) {
        List<SubjectJpaEntity> subjectEntities = repository.findAllByKitVersionIdOrderByIndex(kitVersionId);
        List<Long> subjectEntityIds = subjectEntities.stream().map(SubjectJpaEntity::getId).toList();
        List<AttributeJpaEntity> attributeEntities = attributeRepository.findAllBySubjectIdInAndKitVersionId(subjectEntityIds, kitVersionId);
        Map<Long, List<AttributeJpaEntity>> subjectIdToAttrEntities = attributeEntities.stream()
            .collect(Collectors.groupingBy(AttributeJpaEntity::getSubjectId));

        return subjectEntities.stream()
            .map(e -> SubjectMapper.mapToDomainModel(e,
                Optional.ofNullable(subjectIdToAttrEntities.get(e.getId()))
                    .orElse(Collections.emptyList()).stream()
                    .map(AttributeMapper::mapToDomainModel)
                    .toList()))
            .toList();
    }

    @Override
    public PaginatedResponse<Subject> loadPaginatedByKitVersionId(long kitVersionId, int page, int size) {
        Page<SubjectJpaEntity> pageResult = repository.findByKitVersionId(kitVersionId,
            PageRequest.of(page, size, Sort.Direction.ASC, SubjectJpaEntity.Fields.index));
        return new PaginatedResponse<>(
            pageResult.stream().map(s -> SubjectMapper.mapToDomainModel(s, null)).toList(),
            pageResult.getNumber(),
            pageResult.getSize(),
            SubjectJpaEntity.Fields.index,
            Sort.Direction.ASC.name().toLowerCase(),
            (int) pageResult.getTotalElements());
    }

    @Override
    public List<Subject> loadSubjectsWithoutAttribute(long kitVersionId) {
        return repository.findAllByKitVersionIdAndWithoutAttributes(kitVersionId)
            .stream()
            .map(e -> SubjectMapper.mapToDomainModel(e, null))
            .toList();
    }

    @Override
    public Subject load(long subjectId, long kitVersionId) {
        var subjectEntity = repository.findByIdAndKitVersionId (subjectId, kitVersionId)
            .orElseThrow(() -> new ResourceNotFoundException(GET_KIT_SUBJECT_DETAIL_SUBJECT_ID_NOT_FOUND));
        List<AttributeJpaEntity> attributeEntities = attributeRepository.findAllBySubjectIdAndKitVersionId(subjectId, kitVersionId);
        return mapToDomainModel(subjectEntity,
            attributeEntities.stream().map(AttributeMapper::mapToDomainModel).toList());
    }

    @Override
    public PaginatedResponse<Subject> loadWithAttributesByKitVersionId(long kitVersionId,
                                                                       int page,
                                                                       int size) {
        var views = repository.findWithAttributesByKitVersionId(kitVersionId);
        var subjectEntityToSubjectAttrView = views.stream()
            .collect(Collectors.groupingBy(SubjectJoinAttributeView::getSubject));

        Map<Long, Subject> subjectIdToSubject = subjectEntityToSubjectAttrView.entrySet().stream()
            .map(e -> {
                List<Attribute> attributes = e.getValue().stream()
                    .map(x -> AttributeMapper.mapToDomainModel(x.getAttribute()))
                    .sorted(Comparator.comparing(Attribute::getIndex))
                    .toList();
                return mapToDomainModel(e.getKey(), attributes);
            })
            .collect(Collectors.toMap(Subject::getId, Function.identity()));

        Page<SubjectJpaEntity> subjectEntitesPage = repository.findByKitVersionId(kitVersionId,
            PageRequest.of(page, size, Sort.Direction.ASC, SubjectJpaEntity.Fields.index));
        List<Subject> items = subjectEntitesPage.getContent().stream()
            .map(e -> subjectIdToSubject.get(e.getId()))
            .toList();

        return new PaginatedResponse<>(
            items,
            subjectEntitesPage.getNumber(),
            subjectEntitesPage.getSize(),
            SubjectJpaEntity.Fields.index,
            Sort.Direction.ASC.name().toLowerCase(),
            (int) subjectEntitesPage.getTotalElements()
        );
    }

    @Override
    public void delete(long subjectId, long kitVersionId) {
        if (!repository.existsByIdAndKitVersionId(subjectId, kitVersionId))
            throw new ResourceNotFoundException(SUBJECT_ID_NOT_FOUND);

        repository.deleteByIdAndKitVersionId(subjectId, kitVersionId);
    }

    @Override
    public void update(UpdateSubjectPort.Param param) {
        if (!repository.existsByIdAndKitVersionId(param.id(), param.kitVersionId()))
            throw new ResourceNotFoundException(SUBJECT_ID_NOT_FOUND);

        repository.update(param.id(),
            param.kitVersionId(),
            param.code(),
            param.title(),
            param.index(),
            param.description(),
            param.weight(),
            param.lastModificationTime(),
            param.lastModifiedBy());
    }

    @Override
    public void updateOrders(UpdateOrderParam param) {
        Map<Long, Integer> idToIndex = param.orders().stream()
            .collect(Collectors.toMap(
                UpdateOrderParam.SubjectOrder::subjectId,
                UpdateOrderParam.SubjectOrder::index));

        List<SubjectJpaEntity> entities = repository.findAllByIdInAndKitVersionId(idToIndex.keySet(), param.kitVersionId());
        if (entities.size() != param.orders().size())
            throw new ResourceNotFoundException(SUBJECT_ID_NOT_FOUND);

        entities.forEach(x -> {
            int newIndex = idToIndex.get(x.getId());
            x.setIndex(newIndex);
            x.setLastModificationTime(param.lastModificationTime());
            x.setLastModifiedBy(param.lastModifiedBy());
        });
        repository.saveAll(entities);
    }
}
