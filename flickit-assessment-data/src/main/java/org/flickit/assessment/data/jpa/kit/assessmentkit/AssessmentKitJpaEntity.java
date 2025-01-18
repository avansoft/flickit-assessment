package org.flickit.assessment.data.jpa.kit.assessmentkit;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.flickit.assessment.data.jpa.users.user.UserJpaEntity;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "fak_assessment_kit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AssessmentKitJpaEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "summary", nullable = false)
    private String summary;

    @Column(name = "about", nullable = false)
    private String about;

    @Column(name = "published", nullable = false)
    private Boolean published;

    @Column(name = "is_private", nullable = false)
    private Boolean isPrivate;

    @Column(name = "expert_group_id", nullable = false)
    private Long expertGroupId;

    @Column(name = "language_id", nullable = false)
    private Integer languageId;

    @Column(name = "creation_time", nullable = false)
    private LocalDateTime creationTime;

    @Column(name = "last_modification_time", nullable = false)
    private LocalDateTime lastModificationTime;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "last_modified_by", nullable = false)
    private UUID lastModifiedBy;

    @ManyToMany
    @JoinTable(
        name = "fak_kit_user_access",
        joinColumns = @JoinColumn(name = "kit_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserJpaEntity> accessGrantedUsers;

    @Column(name = "last_major_modification_time", nullable = false)
    private LocalDateTime lastMajorModificationTime;

    @Column(name = "kit_version_id")
    private Long kitVersionId;
}
