package org.flickit.assessment.users.application.service.spaceuseraccess;

import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.users.application.domain.Space;
import org.flickit.assessment.users.application.port.in.spaceuseraccess.UpdateSpaceLastSeenUseCase;
import org.flickit.assessment.users.application.port.out.spaceuseraccess.CheckSpaceAccessPort;
import org.flickit.assessment.users.application.port.out.spaceuseraccess.UpdateSpaceLastSeenPort;
import org.flickit.assessment.users.test.fixture.application.SpaceMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateSpaceLastSeenServiceTest {

    @InjectMocks
    UpdateSpaceLastSeenService service;

    @Mock
    CheckSpaceAccessPort checkSpaceAccessPort;

    @Mock
    UpdateSpaceLastSeenPort updateSpaceLastSeenPort;

    @Test
    void testUpdateSpaceLastSeenService_isMember_success() {
        UUID currentUserId = UUID.randomUUID();
        Space space = SpaceMother.createPersonalSpace(currentUserId);
        UpdateSpaceLastSeenUseCase.Param param = new UpdateSpaceLastSeenUseCase.Param(space.getId(), currentUserId);

        when(checkSpaceAccessPort.checkIsMember(space.getId(), currentUserId)).thenReturn(true);
        doNothing().when(updateSpaceLastSeenPort).updateLastSeen(anyLong(), any(UUID.class), any(LocalDateTime.class));

        assertDoesNotThrow(()-> service.updateLastSeen(param));

        verify(checkSpaceAccessPort).checkIsMember(space.getId(), space.getOwnerId());
        verify(updateSpaceLastSeenPort).updateLastSeen(anyLong(), any(UUID.class), any(LocalDateTime.class));
    }

    @Test
    void testUpdateSpaceLastSeenService_isNotMember_accessDenied() {
        UUID ownerId = UUID.randomUUID();
        Space space = SpaceMother.createPersonalSpace(ownerId);
        UUID currentUserId = UUID.randomUUID();
        UpdateSpaceLastSeenUseCase.Param param = new UpdateSpaceLastSeenUseCase.Param(space.getId(), currentUserId);

        when(checkSpaceAccessPort.checkIsMember(space.getId(), currentUserId)).thenReturn(false);

        var throwable = assertThrows(AccessDeniedException.class, ()-> service.updateLastSeen(param));
        assertEquals(COMMON_CURRENT_USER_NOT_ALLOWED, throwable.getMessage());

        verify(checkSpaceAccessPort).checkIsMember(space.getId(), param.getCurrentUserId());
    }
}
