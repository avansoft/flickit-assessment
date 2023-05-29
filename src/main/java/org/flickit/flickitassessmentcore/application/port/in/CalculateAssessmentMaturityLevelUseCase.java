package org.flickit.flickitassessmentcore.application.port.in;

import org.flickit.flickitassessmentcore.domain.MaturityLevel;

import java.util.UUID;

public interface CalculateAssessmentMaturityLevelUseCase {

    MaturityLevel calculateAssessmentMaturityLevel(UUID assessmentId);

}
