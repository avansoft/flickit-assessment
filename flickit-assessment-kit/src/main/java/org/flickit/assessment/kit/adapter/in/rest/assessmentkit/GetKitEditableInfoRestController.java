package org.flickit.assessment.kit.adapter.in.rest.assessmentkit;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.config.jwt.UserContext;
import org.flickit.assessment.kit.application.domain.KitLanguage;
import org.flickit.assessment.kit.application.port.in.assessmentkit.GetKitEditableInfoUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class GetKitEditableInfoRestController {

    private final GetKitEditableInfoUseCase useCase;
    private final UserContext userContext;

    @GetMapping("/assessment-kits/{kitId}/info")
    public ResponseEntity<GetKitEditableInfoResponseDto> getKitEditableInfo(@PathVariable("kitId") Long kitId) {
        UUID currentUserId = userContext.getUser().id();
        var kitEditableInfo = useCase.getKitEditableInfo(toParam(kitId, currentUserId));
        return new ResponseEntity<>(toResponse(kitEditableInfo), HttpStatus.OK);
    }

    private GetKitEditableInfoUseCase.Param toParam(Long kitId, UUID currentUserId) {
        return new GetKitEditableInfoUseCase.Param(kitId, currentUserId);
    }

    private GetKitEditableInfoResponseDto toResponse(GetKitEditableInfoUseCase.KitEditableInfo kitEditableInfo) {
        return new GetKitEditableInfoResponseDto(
            kitEditableInfo.id(),
            kitEditableInfo.title(),
            kitEditableInfo.summary(),
            KitLanguage.valueOf(kitEditableInfo.lang()).getTitle(),
            kitEditableInfo.published(),
            kitEditableInfo.isPrivate(),
            kitEditableInfo.price(),
            kitEditableInfo.about(),
            kitEditableInfo.tags(),
            kitEditableInfo.editable(),
            kitEditableInfo.hasActiveVersion()
        );
    }
}
