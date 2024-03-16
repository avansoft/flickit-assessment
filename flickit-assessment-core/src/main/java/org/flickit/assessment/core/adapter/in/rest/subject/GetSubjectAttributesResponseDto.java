package org.flickit.assessment.core.adapter.in.rest.subject;

import org.flickit.assessment.core.application.port.in.subject.GetSubjectAttributesUseCase.SubjectAttribute;

import java.util.List;

public record GetSubjectAttributesResponseDto(List<SubjectAttribute> items) {
}
