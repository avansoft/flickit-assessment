package org.flickit.assessment.core.adapter.out.persistence.attributeinsight;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.flickit.assessment.core.application.domain.AttributeInsight;
import org.flickit.assessment.data.jpa.core.attributeinsight.AttributeInsightJpaEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AttributeInsightMapper {

    static AttributeInsight mapToDomain(AttributeInsightJpaEntity entity) {
        return new AttributeInsight(entity.getAssessmentResultId(),
            entity.getAttributeId(),
            entity.getAiInsight(),
            entity.getAssessorInsight(),
            entity.getAiInsightTime(),
            entity.getAssessorInsightTime(),
            entity.getAiInputPath(),
            entity.getApproved(),
            entity.getLastModificationTime());
    }

    public static AttributeInsightJpaEntity mapToJpaEntity(AttributeInsight attributeInsight) {
        return new AttributeInsightJpaEntity(
            attributeInsight.getAssessmentResultId(),
            attributeInsight.getAttributeId(),
            attributeInsight.getAiInsight(),
            attributeInsight.getAssessorInsight(),
            attributeInsight.getAiInsightTime(),
            attributeInsight.getAssessorInsightTime(),
            attributeInsight.getAiInputPath(),
            attributeInsight.isApproved(),
            attributeInsight.getLastModificationTime()
        );
    }
}
