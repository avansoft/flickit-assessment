package org.flickit.assessment.users.application.service.space;

import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.users.application.domain.Space;
import org.flickit.assessment.users.application.port.in.space.GetSpaceUseCase;
import org.flickit.assessment.users.application.port.out.space.LoadSpaceDetailsPort;
import org.flickit.assessment.users.application.port.out.spaceuseraccess.CheckSpaceAccessPort;
import org.flickit.assessment.users.test.fixture.application.SpaceMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.flickit.assessment.users.common.ErrorMessageKey.SPACE_ID_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetSpaceServiceTest {

    @InjectMocks
    GetSpaceService service;

    @Mock
    CheckSpaceAccessPort checkSpaceAccessPort;

    @Mock
    LoadSpaceDetailsPort loadSpaceDetailsPort;

    @Test
    void testGetSpaceService_UserIsOwner_successFullWithEditableTrue() {
        UUID currentUserId = UUID.randomUUID();
        Space space = SpaceMother.createPersonalSpace(currentUserId);
        GetSpaceUseCase.Param param = new GetSpaceUseCase.Param(space.getId(), currentUserId);
        LoadSpaceDetailsPort.Result portResult = new LoadSpaceDetailsPort.Result(space, 1, 1);

        when(checkSpaceAccessPort.checkIsMember(space.getId(), currentUserId)).thenReturn(true);
        when(loadSpaceDetailsPort.loadSpace(space.getId())).thenReturn(portResult);

        var result = service.getSpace(param);

        assertTrue(result.editable(), "'editable' should be true");
        verify(loadSpaceDetailsPort).loadSpace(space.getId());
    }

    @Test
    void testGetSpaceService_UserIsNotOwner_successFullWithEditableFalse() {
        UUID ownerId = UUID.randomUUID();
        Space space = SpaceMother.createPersonalSpace(ownerId);
        UUID currentUserId = UUID.randomUUID();
        GetSpaceUseCase.Param param = new GetSpaceUseCase.Param(space.getId(), currentUserId);
        LoadSpaceDetailsPort.Result portResult = new LoadSpaceDetailsPort.Result(space, 1, 1);

        when(checkSpaceAccessPort.checkIsMember(space.getId(), currentUserId)).thenReturn(true);
        when(loadSpaceDetailsPort.loadSpace(space.getId())).thenReturn(portResult);

        var result = service.getSpace(param);
        assertFalse(result.editable(), "'editable' should be false");
        verify(loadSpaceDetailsPort).loadSpace(anyLong());
    }

    @Test
    void testGetSpace_spaceDoesNotExist_throwException() {
        long spaceId = 0L;
        UUID currentUserId = UUID.randomUUID();
        GetSpaceUseCase.Param param = new GetSpaceUseCase.Param(spaceId, currentUserId);

        when(checkSpaceAccessPort.checkIsMember(spaceId, currentUserId)).thenReturn(true);
        when(loadSpaceDetailsPort.loadSpace(spaceId))
            .thenThrow(new ResourceNotFoundException(SPACE_ID_NOT_FOUND));

        assertThrows(ResourceNotFoundException.class, () -> service.getSpace(param));
        verify(loadSpaceDetailsPort).loadSpace(anyLong());
    }

    @Test
    void testGetSpace_spaceAccessNotFound_accessDeniedException() {
        long spaceId = 0L;
        UUID currentUserId = UUID.randomUUID();
        GetSpaceUseCase.Param param = new GetSpaceUseCase.Param(spaceId, currentUserId);

        when(checkSpaceAccessPort.checkIsMember(spaceId, currentUserId)).thenReturn(false);

        var throwable = assertThrows(AccessDeniedException.class, () -> service.getSpace(param));
        assertEquals(COMMON_CURRENT_USER_NOT_ALLOWED, throwable.getMessage());

        verify(checkSpaceAccessPort).checkIsMember(spaceId, currentUserId);
    }
}
