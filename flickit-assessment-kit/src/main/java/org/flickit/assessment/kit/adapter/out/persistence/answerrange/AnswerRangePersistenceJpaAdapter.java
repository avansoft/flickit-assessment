package org.flickit.assessment.kit.adapter.out.persistence.answerrange;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.data.jpa.kit.answeroption.AnswerOptionJpaEntity;
import org.flickit.assessment.data.jpa.kit.answeroption.AnswerOptionJpaRepository;
import org.flickit.assessment.data.jpa.kit.answerrange.AnswerRangeJpaEntity;
import org.flickit.assessment.data.jpa.kit.answerrange.AnswerRangeJpaRepository;
import org.flickit.assessment.data.jpa.kit.seq.KitDbSequenceGenerators;
import org.flickit.assessment.kit.adapter.out.persistence.answeroption.AnswerOptionMapper;
import org.flickit.assessment.kit.application.domain.AnswerOption;
import org.flickit.assessment.kit.application.domain.AnswerRange;
import org.flickit.assessment.kit.application.port.out.answerange.LoadAnswerRangesPort;
import org.flickit.assessment.kit.application.port.out.answerrange.CreateAnswerRangePort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.flickit.assessment.kit.adapter.out.persistence.answerrange.AnswerRangeMapper.toDomainModel;

@Component
@RequiredArgsConstructor
public class AnswerRangePersistenceJpaAdapter implements
    CreateAnswerRangePort,
    LoadAnswerRangesPort {

    private final AnswerRangeJpaRepository repository;
    private final AnswerOptionJpaRepository answerOptionRepository;
    private final KitDbSequenceGenerators sequenceGenerators;

    @Override
    public long persist(Param param) {
        var entity = AnswerRangeMapper.toJpaEntity(param);
        entity.setId(sequenceGenerators.generateAnswerRangeId());
        return repository.save(entity).getId();
    }

    @Override
    public PaginatedResponse<AnswerRange> loadByKitVersionId(long kitVersionId, int page, int size) {
        var order = AnswerRangeJpaEntity.Fields.creationTime;
        var sort = Sort.Direction.ASC;
        var pageResult = repository.findByKitVersionIdAndReusableTrue(kitVersionId, PageRequest.of(page, size, sort, order));
        List<Long> answerRangeEntityIds = pageResult.getContent().stream().map(AnswerRangeJpaEntity::getId).toList();
        var answerRangeIdToAnswerOptionsMap = answerOptionRepository.findAllByAnswerRangeIdInAndKitVersionId(answerRangeEntityIds, kitVersionId,
                Sort.by(AnswerOptionJpaEntity.Fields.index)).stream()
            .collect(groupingBy(AnswerOptionJpaEntity::getAnswerRangeId, toList()));

        var answerRanges = pageResult.getContent().stream()
            .map(entity -> {
                List<AnswerOption> options = answerRangeIdToAnswerOptionsMap.get(entity.getId()).stream()
                    .map(AnswerOptionMapper::mapToDomainModel)
                    .toList();
                return toDomainModel(entity, options);
            }).toList();

        return new PaginatedResponse<>(
            answerRanges,
            pageResult.getNumber(),
            pageResult.getSize(),
            order,
            sort.name().toLowerCase(),
            (int) pageResult.getTotalElements()
        );
    }
}
