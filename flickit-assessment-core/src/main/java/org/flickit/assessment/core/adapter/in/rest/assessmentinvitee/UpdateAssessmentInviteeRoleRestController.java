package org.flickit.assessment.core.adapter.in.rest.assessmentinvitee;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.config.jwt.UserContext;
import org.flickit.assessment.core.application.port.in.assessmentinvitee.UpdateAssessmentInviteeRoleUseCase;
import org.flickit.assessment.core.application.port.in.assessmentinvitee.UpdateAssessmentInviteeRoleUseCase.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UpdateAssessmentInviteeRoleRestController {

    private final UserContext userContext;
    private final UpdateAssessmentInviteeRoleUseCase useCase;

    @PutMapping("/assessments/invitees/{id}")
    public ResponseEntity<Void> inviteSpaceMember(@PathVariable("id") UUID inviteId,
                                                  @RequestBody UpdateAssessmentInviteeRoleRequestDto request) {
        var currentUserId = userContext.getUser().id();
        useCase.editRole(toParam(inviteId, request, currentUserId));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    Param toParam(UUID inviteId, UpdateAssessmentInviteeRoleRequestDto requestDto, UUID currentUserId) {
        return new Param(inviteId, requestDto.roleId(), currentUserId);
    }
}
