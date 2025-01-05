package org.flickit.assessment.data.jpa.users.space;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "fau_space")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SpaceJpaEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fau_space_id_seq")
    @SequenceGenerator(name = "fau_space_id_seq", sequenceName = "fau_space_id_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "code", length = 100, nullable = false)
    private String code;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name= "type", nullable = false)
    private int type;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "subscription_expiry", nullable = false)
    private LocalDateTime subscriptionExpiry;

    @Column(name = "creation_time", nullable = false)
    private LocalDateTime creationTime;

    @Column(name = "last_modification_time", nullable = false)
    private LocalDateTime lastModificationTime;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "last_modified_by", nullable = false)
    private UUID lastModifiedBy;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "deletion_time", nullable = false)
    private long deletionTime;
}

