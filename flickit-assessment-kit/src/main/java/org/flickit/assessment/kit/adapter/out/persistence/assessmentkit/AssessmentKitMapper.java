package org.flickit.assessment.kit.adapter.out.persistence.assessmentkit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.flickit.assessment.data.jpa.kit.assessmentkit.AssessmentKitJpaEntity;
import org.flickit.assessment.kit.application.domain.AssessmentKit;
import org.flickit.assessment.kit.application.port.out.assessmentkit.CreateAssessmentKitPort;
import org.flickit.assessment.kit.application.port.out.assessmentkit.UpdateKitInfoPort;

import java.time.LocalDateTime;
import java.util.HashSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AssessmentKitMapper {

    public static AssessmentKitJpaEntity toJpaEntity(CreateAssessmentKitPort.Param param, Long kitVersionId) {
        return new AssessmentKitJpaEntity(
            null,
            param.code(),
            param.title(),
            param.summary(),
            param.about(),
            param.published(),
            param.isPrivate(),
            param.expertGroupId(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            param.createdBy(),
            param.createdBy(),
            new HashSet<>(),
            LocalDateTime.now(),
            kitVersionId
        );
    }

    public static AssessmentKitJpaEntity toJpaEntity(AssessmentKitJpaEntity entity, UpdateKitInfoPort.Param param) {
        return new AssessmentKitJpaEntity(
            entity.getId(),
            param.code() != null ? param.code() : entity.getCode(),
            param.title() != null ? param.title() : entity.getTitle(),
            param.summary() != null ? param.summary() : entity.getSummary(),
            param.about() != null ? param.about() : entity.getAbout(),
            param.published() != null ? param.published() : entity.getPublished(),
            param.isPrivate() != null ? param.isPrivate() : entity.getIsPrivate(),
            entity.getExpertGroupId(),
            entity.getCreationTime(),
            param.lastModificationTime(),
            entity.getCreatedBy(),
            param.currentUserId(),
            entity.getAccessGrantedUsers(),
            entity.getLastMajorModificationTime(),
            entity.getKitVersionId()
        );
    }

    public static AssessmentKit mapToDomainModel(AssessmentKitJpaEntity entity) {
        return new AssessmentKit(
            entity.getId(),
            entity.getCode(),
            entity.getTitle(),
            entity.getSummary(),
            entity.getAbout(),
            entity.getCreationTime(),
            entity.getLastModificationTime(),
            entity.getCreatedBy(),
            entity.getPublished(),
            entity.getIsPrivate(),
            entity.getExpertGroupId(),
            null,
            null,
            null,
            entity.getKitVersionId());
    }
}
