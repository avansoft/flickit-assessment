package org.flickit.assessment.core.application.port.out.confidencelevel;

import org.flickit.assessment.core.application.domain.AssessmentResult;

import java.util.UUID;

public interface LoadConfidenceLevelCalculateInfoPort {

    AssessmentResult load(UUID assessmentId);
}
