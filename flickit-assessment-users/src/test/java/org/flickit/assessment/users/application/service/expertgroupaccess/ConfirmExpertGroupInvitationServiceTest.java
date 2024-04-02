package org.flickit.assessment.users.application.service.expertgroupaccess;

import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.common.exception.ValidationException;
import org.flickit.assessment.users.application.port.in.expertgroupaccess.ConfirmExpertGroupInvitationUseCase.Param;
import org.flickit.assessment.users.application.port.out.expertgroupaccess.LoadExpertGroupAccessPort;
import org.flickit.assessment.users.application.port.out.expertgroupaccess.ConfirmExpertGroupInvitationPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.flickit.assessment.users.common.ErrorMessageKey.CONFIRM_EXPERT_GROUP_INVITATION_INPUT_DATA_INVALID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConfirmExpertGroupInvitationServiceTest {

    @InjectMocks
    private ConfirmExpertGroupInvitationService service;
    @Mock
    private LoadExpertGroupAccessPort loadExpertGroupAccessPort;
    @Mock
    private ConfirmExpertGroupInvitationPort confirmExpertGroupInvitationPort;

    @Test
    @DisplayName("Confirm invite member invitation with valid parameters causes success confirmation")
    void testInviteMemberConfirmation_validParameters_success() {
        UUID currentUserId = UUID.randomUUID();
        UUID inviteToken = UUID.randomUUID();
        long expertGroupId = 0L;
        Param param = new Param(expertGroupId, inviteToken, currentUserId);
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(7);

        when(loadExpertGroupAccessPort.loadExpirationDate(expertGroupId, inviteToken, currentUserId)).thenReturn(expirationDate);
        doNothing().when(confirmExpertGroupInvitationPort).confirmInvitation(isA(UUID.class));

        assertDoesNotThrow(() -> service.confirmInvitation(param));

        verify(loadExpertGroupAccessPort).loadExpirationDate(anyLong(), any(UUID.class), any(UUID.class));
        verify(confirmExpertGroupInvitationPort).confirmInvitation(any(UUID.class));
    }

    @Test
    @DisplayName("Confirm invite member invitation with invalid parameters causes ResourceNotFoundException")
    void testConfirmInviteMember_invalidInputData_fail() {
        UUID currentUserId = UUID.randomUUID();
        UUID inviteToken = UUID.randomUUID();
        long expertGroupId = 0L;
        Param param = new Param(expertGroupId, inviteToken, currentUserId);

        when(loadExpertGroupAccessPort.loadExpirationDate(anyLong(), any(UUID.class), any(UUID.class))).
            thenThrow(new ResourceNotFoundException(CONFIRM_EXPERT_GROUP_INVITATION_INPUT_DATA_INVALID));

        assertThrows(ResourceNotFoundException.class, () -> service.confirmInvitation(param));

        verify(loadExpertGroupAccessPort).loadExpirationDate(anyLong(), any(UUID.class), any(UUID.class));
        verifyNoInteractions(confirmExpertGroupInvitationPort);
    }

    @Test
    @DisplayName("Confirm invite member invitation with expired invite token causes validationException")
    void testConfirmInviteMember_expiredInviteToken_fail() {
        UUID currentUserId = UUID.randomUUID();
        UUID inviteToken = UUID.randomUUID();
        long expertGroupId = 0L;
        Param param = new Param(expertGroupId, inviteToken, currentUserId);
        LocalDateTime expirationDate = LocalDateTime.now().minusDays(1);

        when(loadExpertGroupAccessPort.loadExpirationDate(anyLong(), any(UUID.class), any(UUID.class))).
            thenReturn(expirationDate);

        assertThrows(ValidationException.class, () -> service.confirmInvitation(param));

        verify(loadExpertGroupAccessPort).loadExpirationDate(anyLong(), any(UUID.class), any(UUID.class));
        verifyNoInteractions(confirmExpertGroupInvitationPort);
    }
}
