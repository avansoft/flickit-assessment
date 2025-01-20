package org.flickit.assessment.data.jpa.core.attributeinsight;

import jakarta.persistence.*;
import lombok.*;
import org.flickit.assessment.data.jpa.AbstractEntity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@IdClass(AttributeInsightJpaEntity.EntityId.class)
@Table(name = "fac_attribute_insight")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AttributeInsightJpaEntity extends AbstractEntity<AttributeInsightJpaEntity.EntityId> {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "assessment_result_id", nullable = false)
    private UUID assessmentResultId;

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "attribute_id", nullable = false)
    private Long attributeId;

    @Column(name = "ai_insight", nullable = false)
    private String aiInsight;

    @Column(name = "assessor_insight")
    private String assessorInsight;

    @Column(name = "ai_insight_time", nullable = false)
    private LocalDateTime aiInsightTime;

    @Column(name = "assessor_insight_time")
    private LocalDateTime assessorInsightTime;

    @Column(name = "ai_input_path")
    private String aiInputPath;

    @Column(name = "approved", nullable = false)
    private Boolean approved;

    @Column(name = "last_modification_time", nullable = false)
    private LocalDateTime lastModificationTime;

    @Override
    public EntityId getId() {
        return new AttributeInsightJpaEntity.EntityId(assessmentResultId, attributeId);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EntityId implements Serializable {

        private UUID assessmentResultId;
        private Long attributeId;
    }
}
