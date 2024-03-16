package org.flickit.assessment.core.application.port.out.subject;

import java.util.UUID;

public interface CheckSubjectKitExistencePort {

    boolean existsByIdAndAssessmentId(Long subjectId, UUID assessmentId);
}
