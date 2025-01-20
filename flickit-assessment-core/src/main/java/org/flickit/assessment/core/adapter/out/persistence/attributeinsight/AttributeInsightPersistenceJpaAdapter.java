package org.flickit.assessment.core.adapter.out.persistence.attributeinsight;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.core.application.domain.AttributeInsight;
import org.flickit.assessment.core.application.port.out.attributeinsight.*;
import org.flickit.assessment.data.jpa.core.assessmentresult.AssessmentResultJpaRepository;
import org.flickit.assessment.data.jpa.core.attributeinsight.AttributeInsightJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.flickit.assessment.core.common.ErrorMessageKey.APPROVE_ATTRIBUTE_INSIGHT_ASSESSMENT_RESULT_NOT_FOUND;
import static org.flickit.assessment.core.common.ErrorMessageKey.ATTRIBUTE_INSIGHT_ID_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class AttributeInsightPersistenceJpaAdapter implements
    LoadAttributeInsightPort,
    CreateAttributeInsightPort,
    UpdateAttributeInsightPort,
    LoadAttributeInsightsPort,
    ApproveAttributeInsightPort {

    private final AttributeInsightJpaRepository repository;
    private final AssessmentResultJpaRepository assessmentResultRepository;

    @Override
    public Optional<AttributeInsight> load(UUID assessmentResultId, Long attributeId) {
        return repository.findByAssessmentResultIdAndAttributeId(assessmentResultId, attributeId)
            .map(AttributeInsightMapper::mapToDomain);
    }

    @Override
    public void persist(AttributeInsight attributeInsight) {
        repository.save(AttributeInsightMapper.mapToJpaEntity(attributeInsight));
    }

    @Override
    public void updateAiInsight(UpdateAttributeInsightPort.AiParam attributeInsight) {
        repository.updateAiInsight(
            attributeInsight.assessmentResultId(),
            attributeInsight.attributeId(),
            attributeInsight.aiInsight(),
            attributeInsight.aiInsightTime(),
            attributeInsight.aiInputPath(),
            attributeInsight.isApproved(),
            attributeInsight.lastModificationTime());
    }

    @Override
    public void updateAssessorInsight(UpdateAttributeInsightPort.AssessorParam attributeInsight) {
        repository.updateAssessorInsight(
            attributeInsight.assessmentResultId(),
            attributeInsight.attributeId(),
            attributeInsight.assessorInsight(),
            attributeInsight.assessorInsightTime(),
            attributeInsight.isApproved(),
            attributeInsight.lastModificationTime()
        );
    }

    @Override
    public List<AttributeInsight> loadInsights(UUID assessmentResultId) {
        return repository.findByAssessmentResultId(assessmentResultId)
            .stream()
            .map(AttributeInsightMapper::mapToDomain)
            .toList();
    }

    @Override
    public void approve(UUID assessmentId, long attributeId, LocalDateTime lastModificationTime) {
        var assessmentResultId = assessmentResultRepository.findFirstByAssessment_IdOrderByLastModificationTimeDesc(assessmentId)
            .orElseThrow(() -> new ResourceNotFoundException(APPROVE_ATTRIBUTE_INSIGHT_ASSESSMENT_RESULT_NOT_FOUND))
            .getId();

        if (!repository.existsByAssessmentResultIdAndAttributeId(assessmentResultId, attributeId))
            throw new ResourceNotFoundException(ATTRIBUTE_INSIGHT_ID_NOT_FOUND);

        repository.approve(assessmentResultId, attributeId, lastModificationTime);
    }
}
