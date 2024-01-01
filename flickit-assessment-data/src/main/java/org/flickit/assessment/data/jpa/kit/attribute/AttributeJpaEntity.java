package org.flickit.assessment.data.jpa.kit.attribute;


import jakarta.persistence.*;
import lombok.*;
import org.flickit.assessment.data.jpa.kit.subject.SubjectJpaEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "baseinfo_qualityattribute")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AttributeJpaEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "baseinfo_qualityattribute_id_seq")
    @SequenceGenerator(name = "baseinfo_qualityattribute_id_seq", sequenceName = "baseinfo_qualityattribute_id_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "index", nullable = false)
    private Integer index;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "weight", nullable = false)
    private Integer weight;

    @Column(name = "creation_time", nullable = false)
    private LocalDateTime creationTime;

    @Column(name = "last_modification_date", nullable = false)
    private LocalDateTime lastModificationTime;

    @Column(name = "assessment_kit_id", nullable = false)
    private Long assessmentKitId;

    public AttributeJpaEntity(Long id, String code, String title, Integer index, String description, Integer weight,
                              LocalDateTime creationTime, LocalDateTime lastModificationTime, Long assessmentKitId) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.index = index;
        this.description = description;
        this.weight = weight;
        this.creationTime = creationTime;
        this.lastModificationTime = lastModificationTime;
        this.assessmentKitId = assessmentKitId;
    }

    @ManyToOne
    @JoinColumn(name = "assessment_subject_id", referencedColumnName = "id")
    private SubjectJpaEntity subject;
}
