package org.flickit.assessment.users.adapter.in.rest.space;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.config.jwt.UserContext;
import org.flickit.assessment.users.application.port.in.space.CreateSpaceUseCase;
import org.flickit.assessment.users.application.port.in.space.CreateSpaceUseCase.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CreateSpaceRestController {

    private final CreateSpaceUseCase useCase;
    private final UserContext userContext;

    @PostMapping("/spaces")
    public ResponseEntity<CreateSpaceResponseDto> createSpace(@RequestBody CreateSpaceRequestDto request) {
        UUID currentUserId = userContext.getUser().id();
        var response = toResponseDto(useCase.createSpace(toParam(request, currentUserId)));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private Param toParam(CreateSpaceRequestDto request, UUID currentUserId) {
        return new Param(request.title(), request.type(), currentUserId);
    }

    private CreateSpaceResponseDto toResponseDto(CreateSpaceUseCase.Result result) {
        return new CreateSpaceResponseDto(result.id());
    }
}
