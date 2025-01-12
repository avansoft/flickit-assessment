package org.flickit.assessment.kit.adapter.in.rest.assessmentkit;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.config.jwt.UserContext;
import org.flickit.assessment.kit.application.domain.dsl.AssessmentKitDslModel;
import org.flickit.assessment.kit.application.port.in.assessmentkit.GetKitDslUseCase;
import org.flickit.assessment.kit.application.port.in.assessmentkit.GetKitDslUseCase.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class GetKitDslRestController {

    private final UserContext userContext;
    private final GetKitDslUseCase useCase;

    @GetMapping("/assessment-kits/{kitId}/dsl")
    public ResponseEntity<AssessmentKitDslModel> exportKitDsl(@PathVariable Long kitId) {
        AssessmentKitDslModel result = useCase.getKitDsl(toParam(kitId, userContext.getUser().id()));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private Param toParam(Long kitId, UUID currentUserId) {
        return new Param(kitId, currentUserId);
    }
}
