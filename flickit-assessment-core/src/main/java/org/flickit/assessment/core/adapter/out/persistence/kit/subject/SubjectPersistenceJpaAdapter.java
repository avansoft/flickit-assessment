package org.flickit.assessment.core.adapter.out.persistence.kit.subject;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.core.adapter.out.persistence.kit.qualityattribute.QualityAttributeMapper;
import org.flickit.assessment.core.application.domain.QualityAttribute;
import org.flickit.assessment.core.application.domain.Subject;
import org.flickit.assessment.core.application.port.out.subject.LoadSubjectByAssessmentKitIdPort;
import org.flickit.assessment.data.jpa.kit.subject.SubjectJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("coreSubjectPersistenceJpaAdapter")
@RequiredArgsConstructor
public class SubjectPersistenceJpaAdapter implements LoadSubjectByAssessmentKitIdPort {

    private final SubjectJpaRepository repository;

    @Override
    public List<Subject> loadByAssessmentKitId(Long kitId) {
        var views = repository.loadByAssessmentKitId(kitId);

        return views.stream().map(entity -> {
            List<QualityAttribute> attributes = entity.getAttributes().stream()
                .map(QualityAttributeMapper::mapToDomainModel)
                .toList();

            return SubjectMapper.mapToDomainModel(entity, attributes);
        }).toList();
    }
}
