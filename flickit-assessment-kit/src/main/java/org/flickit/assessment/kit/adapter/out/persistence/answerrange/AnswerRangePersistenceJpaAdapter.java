package org.flickit.assessment.kit.adapter.out.persistence.answerrange;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.data.jpa.kit.answeroption.AnswerOptionJpaEntity;
import org.flickit.assessment.data.jpa.kit.answeroption.AnswerOptionJpaRepository;
import org.flickit.assessment.data.jpa.kit.answerrange.AnswerRangeJoinOptionView;
import org.flickit.assessment.data.jpa.kit.answerrange.AnswerRangeJpaEntity;
import org.flickit.assessment.data.jpa.kit.answerrange.AnswerRangeJpaRepository;
import org.flickit.assessment.data.jpa.kit.seq.KitDbSequenceGenerators;
import org.flickit.assessment.kit.adapter.out.persistence.answeroption.AnswerOptionMapper;
import org.flickit.assessment.kit.application.domain.AnswerOption;
import org.flickit.assessment.kit.application.domain.AnswerRange;
import org.flickit.assessment.kit.application.port.out.answerrange.CreateAnswerRangePort;
import org.flickit.assessment.kit.application.port.out.answerrange.LoadAnswerRangePort;
import org.flickit.assessment.kit.application.port.out.answerrange.LoadAnswerRangesPort;
import org.flickit.assessment.kit.application.port.out.answerrange.UpdateAnswerRangePort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.flickit.assessment.kit.adapter.out.persistence.answerrange.AnswerRangeMapper.toDomainModel;
import static org.flickit.assessment.kit.common.ErrorMessageKey.ANSWER_RANGE_ID_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class AnswerRangePersistenceJpaAdapter implements
    CreateAnswerRangePort,
    UpdateAnswerRangePort,
    LoadAnswerRangesPort,
    LoadAnswerRangePort {

    private final AnswerRangeJpaRepository repository;
    private final AnswerOptionJpaRepository answerOptionRepository;
    private final KitDbSequenceGenerators sequenceGenerators;

    @Override
    public long persist(CreateAnswerRangePort.Param param) {
        var entity = AnswerRangeMapper.toJpaEntity(param);
        entity.setId(sequenceGenerators.generateAnswerRangeId());
        return repository.save(entity).getId();
    }

    @Override
    public Map<String, Long> persistAll(List<CreateAnswerRangePort.Param> params) {
        Map<String, Long> codeToId = new HashMap<>();
        List<AnswerRangeJpaEntity> entities = params.stream()
            .map(e -> {
                var entity = AnswerRangeMapper.toJpaEntity(e);
                entity.setId(sequenceGenerators.generateAnswerRangeId());
                codeToId.put(entity.getCode(), entity.getId());
                return entity;
            }).toList();
        repository.saveAll(entities);

        return codeToId;
    }

    @Override
    public PaginatedResponse<AnswerRange> loadByKitVersionId(long kitVersionId, int page, int size) {
        var order = AnswerRangeJpaEntity.Fields.lastModificationTime;
        var sort = Sort.Direction.DESC;
        var pageResult = repository.findByKitVersionIdAndReusableTrue(kitVersionId, PageRequest.of(page, size, sort, order));
        List<Long> answerRangeEntityIds = pageResult.getContent().stream().map(AnswerRangeJpaEntity::getId).toList();
        var answerRangeIdToAnswerOptionsMap = answerOptionRepository.findAllByAnswerRangeIdInAndKitVersionId(answerRangeEntityIds, kitVersionId,
                Sort.by(AnswerOptionJpaEntity.Fields.index)).stream()
            .collect(groupingBy(AnswerOptionJpaEntity::getAnswerRangeId, toList()));

        var answerRanges = pageResult.getContent().stream()
            .map(entity -> {
                List<AnswerOption> options = Optional.ofNullable(answerRangeIdToAnswerOptionsMap.get(entity.getId()))
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(AnswerOptionMapper::mapToDomainModel)
                    .toList();
                return toDomainModel(entity, options);
            })
            .toList();

        return new PaginatedResponse<>(
            answerRanges,
            pageResult.getNumber(),
            pageResult.getSize(),
            order,
            sort.name().toLowerCase(),
            (int) pageResult.getTotalElements()
        );
    }

    @Override
    public List<AnswerRange> loadAnswerRangesWithNotEnoughOptions(long kitVersionId) {
        var rangeViews = repository.findAllReusableWithOptionsByKitVersionId(kitVersionId);

        Map<AnswerRangeJpaEntity, List<AnswerOptionJpaEntity>> answerRangeToOptions = rangeViews.stream()
            .collect(Collectors.groupingBy(
                AnswerRangeJoinOptionView::getAnswerRange,
                Collectors.mapping(AnswerRangeJoinOptionView::getAnswerOption, Collectors.toList())
            ));

        return answerRangeToOptions.entrySet().stream()
            .filter(entry -> entry.getValue().size() < 2) // Filter AnswerRanges with zero or one option
            .map(entry -> AnswerRangeMapper.toDomainModel(entry.getKey(), null))
            .toList();
    }

    @Override
    public void update(UpdateAnswerRangePort.Param param) {
        if (!repository.existsByIdAndKitVersionId(param.answerRangeId(), param.kitVersionId()))
            throw new ResourceNotFoundException(ANSWER_RANGE_ID_NOT_FOUND);

        repository.update(param.answerRangeId(),
            param.kitVersionId(),
            param.title(),
            param.code(),
            param.reusable(),
            param.lastModificationTime(),
            param.lastModifiedBy());
    }

    @Override
    public AnswerRange load(long id, long kitVersionId) {
        return repository.findByIdAndKitVersionId(id, kitVersionId)
            .map(entity -> AnswerRangeMapper.toDomainModel(entity, null))
            .orElseThrow(() -> new ResourceNotFoundException(ANSWER_RANGE_ID_NOT_FOUND));
    }
}
