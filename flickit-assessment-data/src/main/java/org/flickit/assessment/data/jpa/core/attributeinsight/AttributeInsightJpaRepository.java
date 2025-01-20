package org.flickit.assessment.data.jpa.core.attributeinsight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttributeInsightJpaRepository extends JpaRepository<AttributeInsightJpaEntity, AttributeInsightJpaEntity.EntityId> {

    Optional<AttributeInsightJpaEntity> findByAssessmentResultIdAndAttributeId(UUID assessmentResultId, Long attributeId);

    boolean existsByAssessmentResultIdAndAttributeId(UUID assessmentResultId, long attributeId);

    @Query("""
            SELECT ai
            FROM AttributeInsightJpaEntity ai
            JOIN AssessmentResultJpaEntity ar ON ai.assessmentResultId = ar.id
            RIGHT JOIN AttributeJpaEntity att ON ai.attributeId  = att.id AND ar.kitVersionId = att.kitVersionId
            WHERE ar.id = :assessmentResultId
        """)
    List<AttributeInsightJpaEntity> findByAssessmentResultId(@Param("assessmentResultId") UUID assessmentResultId);

    @Modifying
    @Query("""
            UPDATE AttributeInsightJpaEntity a
            SET a.aiInsight = :aiInsight,
                a.aiInsightTime = :aiInsightTime,
                a.aiInputPath = :aiInputPath,
                a.approved = :isApproved,
                a.lastModificationTime = :lastModificationTime
            WHERE a.assessmentResultId = :assessmentResultId AND a.attributeId = :attributeId
        """)
    void updateAiInsight(@Param("assessmentResultId") UUID assessmentResultId,
                         @Param("attributeId") Long attributeId,
                         @Param("aiInsight") String aiInsight,
                         @Param("aiInsightTime") LocalDateTime aiInsightTime,
                         @Param("aiInputPath") String aiInputPath,
                         @Param("isApproved") boolean isApproved,
                         @Param("lastModificationTime") LocalDateTime lastModificationTime);

    @Modifying
    @Query("""
            UPDATE AttributeInsightJpaEntity a
            SET a.assessorInsight = :assessorInsight,
                a.assessorInsightTime = :assessorInsightTime,
                a.approved = :isApproved,
                a.lastModificationTime = :lastModificationTime
            WHERE a.assessmentResultId = :assessmentResultId AND a.attributeId = :attributeId
        """)
    void updateAssessorInsight(@Param("assessmentResultId") UUID assessmentResultId,
                               @Param("attributeId") Long attributeId,
                               @Param("assessorInsight") String assessorInsight,
                               @Param("assessorInsightTime") LocalDateTime assessorInsightTime,
                               @Param("isApproved") boolean isApproved,
                               @Param("lastModificationTime") LocalDateTime lastModificationTime);

    @Modifying
    @Query("""
            UPDATE AttributeInsightJpaEntity a
            SET a.approved = true,
                a.lastModificationTime = :lastModificationTime
            WHERE a.assessmentResultId = :assessmentResultId AND a.attributeId = :attributeId
        """)
    void approve(@Param("assessmentResultId") UUID assessmentResultId,
                 @Param("attributeId") long attributeId,
                 @Param("lastModificationTime") LocalDateTime lastModificationTime);
}
