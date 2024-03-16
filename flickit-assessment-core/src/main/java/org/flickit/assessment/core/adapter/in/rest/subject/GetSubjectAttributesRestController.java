package org.flickit.assessment.core.adapter.in.rest.subject;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.config.jwt.UserContext;
import org.flickit.assessment.core.application.port.in.subject.GetSubjectAttributesUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class GetSubjectAttributesRestController {

    private final UserContext userContext;
    private final GetSubjectAttributesUseCase useCase;

    @GetMapping("/assessments/{assessmentId}/subjects/{subjectId}/attributes-info")
    public ResponseEntity<GetSubjectAttributesResponseDto> getSubjectProgress(
        @PathVariable("assessmentId") UUID assessmentId,
        @PathVariable("subjectId") Long subjectId) {
        UUID currentUserId = userContext.getUser().id();
        var response = toResponse(useCase.getSubjectAttributes(toParam(assessmentId, subjectId, currentUserId)));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private GetSubjectAttributesUseCase.Param toParam(UUID assessmentId, Long subjectId, UUID currentUserId) {
        return new GetSubjectAttributesUseCase.Param(assessmentId, subjectId, currentUserId);
    }

    private GetSubjectAttributesResponseDto toResponse(GetSubjectAttributesUseCase.Result result) {
        return new GetSubjectAttributesResponseDto(result.subjectAttributes());
    }
}
