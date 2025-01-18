package org.flickit.assessment.kit.adapter.in.rest.assessmentkit;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.config.jwt.UserContext;
import org.flickit.assessment.kit.application.port.in.assessmentkit.CreateKitByDslUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CreateKitByDslRestController {

    private final CreateKitByDslUseCase useCase;
    private final UserContext userContext;

    @PostMapping("/assessment-kits/create-by-dsl")
    public ResponseEntity<CreateKitByDslResponseDto> create(@RequestBody CreateKitByDslRequestDto request) {
        UUID currentUserId = userContext.getUser().id();
        Long kitId = useCase.create(toParam(request, currentUserId));
        return new ResponseEntity<>(toResponse(kitId), HttpStatus.CREATED);
    }

    private CreateKitByDslUseCase.Param toParam(CreateKitByDslRequestDto request, UUID currentUserId) {
        return new CreateKitByDslUseCase.Param(
            request.title(),
            request.summary(),
            request.about(),
            request.lang(),
            request.isPrivate(),
            request.kitDslId(),
            request.expertGroupId(),
            request.tagIds(),
            currentUserId);
    }

    private CreateKitByDslResponseDto toResponse(Long kitId) {
        return new CreateKitByDslResponseDto(kitId);
    }
}
