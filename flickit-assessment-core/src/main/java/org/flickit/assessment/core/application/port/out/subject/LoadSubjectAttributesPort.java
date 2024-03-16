package org.flickit.assessment.core.application.port.out.subject;

import org.flickit.assessment.core.application.port.in.subject.GetSubjectAttributesUseCase.SubjectAttribute;

import java.util.List;
import java.util.UUID;

public interface LoadSubjectAttributesPort {

    List<SubjectAttribute> loadBySubjectIdAndAssessmentId(Long subjectId, UUID assessmentId);
}
