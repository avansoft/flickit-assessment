package org.flickit.flickitassessmentcore.adapter.in.rest.evidence;

import org.flickit.flickitassessmentcore.application.domain.Evidence;

import java.util.List;

public record GetEvidenceListResponseDto(List<Evidence> evidences) {
}
