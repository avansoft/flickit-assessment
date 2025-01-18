package org.flickit.assessment.kit.adapter.in.rest.assessmentkit;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.config.jwt.UserContext;
import org.flickit.assessment.kit.application.port.in.assessmentkit.CreateAssessmentKitUseCase;
import org.flickit.assessment.kit.application.port.in.assessmentkit.CreateAssessmentKitUseCase.Param;
import org.flickit.assessment.kit.application.port.in.assessmentkit.CreateAssessmentKitUseCase.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CreateAssessmentKitRestController {

    private final CreateAssessmentKitUseCase useCase;
    private final UserContext userContext;

    @PostMapping("/assessment-kits")
    ResponseEntity<CreateAssessmentKitResponseDto> createAssessmentKit(@RequestBody CreateAssessmentKitRequestDto requestDto) {
        var currentUserId = userContext.getUser().id();
        var result = useCase.createAssessmentKit(toParam(requestDto, currentUserId));
        return new ResponseEntity<>(toResponseDto(result), HttpStatus.OK);
    }

    private Param toParam(CreateAssessmentKitRequestDto requestDto, UUID currentUserId) {
        return new Param(requestDto.title(),
            requestDto.summary(),
            requestDto.about(),
            requestDto.lang(),
            requestDto.isPrivate(),
            requestDto.expertGroupId(),
            requestDto.tagIds(),
            currentUserId);
    }

    private CreateAssessmentKitResponseDto toResponseDto(Result result) {
        return new CreateAssessmentKitResponseDto(result.kitId());
    }
}
