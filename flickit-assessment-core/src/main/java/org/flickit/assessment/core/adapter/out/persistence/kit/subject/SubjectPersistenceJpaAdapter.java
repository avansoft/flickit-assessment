package org.flickit.assessment.core.adapter.out.persistence.kit.subject;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.core.adapter.out.persistence.kit.attribute.AttributeMapper;
import org.flickit.assessment.core.application.domain.QualityAttribute;
import org.flickit.assessment.core.application.domain.Subject;
import org.flickit.assessment.core.application.port.out.subject.CheckSubjectKitExistencePort;
import org.flickit.assessment.core.application.port.out.subject.LoadSubjectPort;
import org.flickit.assessment.core.application.port.out.subject.LoadSubjectsPort;
import org.flickit.assessment.data.jpa.kit.subject.SubjectJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.flickit.assessment.core.adapter.out.persistence.kit.subject.SubjectMapper.mapToDomainModel;

@Component("coreSubjectPersistenceJpaAdapter")
@RequiredArgsConstructor
public class SubjectPersistenceJpaAdapter implements
    LoadSubjectsPort,
    LoadSubjectPort,
    CheckSubjectKitExistencePort {

    private final SubjectJpaRepository repository;

    @Override
    public List<Subject> loadByKitVersionIdWithAttributes(Long kitVersionId) {
        var views = repository.loadByKitVersionIdWithAttributes(kitVersionId);

        return views.stream().map(entity -> {
            List<QualityAttribute> attributes = entity.getAttributes().stream()
                .map(AttributeMapper::mapToDomainModel)
                .toList();

            return mapToDomainModel(entity, attributes);
        }).toList();
    }

    @Override
    public Optional<Subject> loadByIdAndKitVersionId(long id, long kitVersionId) {
        return repository.findByIdAndKitVersionId(id, kitVersionId)
            .map(entity -> mapToDomainModel(entity, null));
    }

    @Override
    public boolean existsByIdAndAssessmentId(Long subjectId, UUID assessmentId) {
        return repository.existsByIdAndAssessmentId(subjectId, assessmentId);
    }
}
