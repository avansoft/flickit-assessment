package org.flickit.assessment.core.adapter.in.rest.evidence;

import org.flickit.assessment.core.application.port.in.evidence.GetEvidenceTypesUseCase;

import java.util.List;

public record GetEvidenceTypesResponseDto(List<GetEvidenceTypesUseCase.EvidenceTypeItem> evidenceTypeItems) {}
