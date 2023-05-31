package org.flickit.flickitassessmentcore.adapter.out.persistence.assessmentcolor;

import lombok.RequiredArgsConstructor;
import org.flickit.flickitassessmentcore.adapter.out.persistence.entity.AssessmentColorEntity;
import org.flickit.flickitassessmentcore.application.port.in.assessment.AssessmentColorDto;
import org.flickit.flickitassessmentcore.application.port.out.assessmentcolor.LoadAssessmentColorByIdPort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssessmentColorPersistence
    implements LoadAssessmentColorByIdPort {
    private final AssessmentColorJpaRepository assessmentColorJpaRepository;

    @Override
    public AssessmentColorDto loadById(Long id) {
        AssessmentColorEntity assessmentColorEntity = assessmentColorJpaRepository.findById(id).orElse(null);
        if (assessmentColorEntity == null)
            return null;
        return AssessmentColorMapper.mapJpaEntityToColorDto(assessmentColorEntity);
    }
}
