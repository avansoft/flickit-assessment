package org.flickit.assessment.users.application.service.space;

import org.flickit.assessment.users.application.domain.SpaceUserAccess;
import org.flickit.assessment.users.application.port.in.space.CreateSpaceUseCase;
import org.flickit.assessment.users.application.port.out.space.CreateSpacePort;
import org.flickit.assessment.users.application.port.out.spaceuseraccess.CreateSpaceUserAccessPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateSpaceServiceTest {

    @InjectMocks
    CreateSpaceService service;

    @Mock
    CreateSpacePort createSpacePort;

    @Mock
    CreateSpaceUserAccessPort createSpaceUserAccessPort;

    @Test
    void testCreateSpace_validParams_successful() {
        var param = createParam(CreateSpaceUseCase.Param.ParamBuilder::build);
        long createdSpaceId = 0L;

        when(createSpacePort.persist(any())).thenReturn(createdSpaceId);

        service.createSpace(param);

        ArgumentCaptor<SpaceUserAccess> captor = ArgumentCaptor.forClass(SpaceUserAccess.class);
        verify(createSpaceUserAccessPort).persist(captor.capture());
        var capturedAccess = captor.getValue();
        assertEquals(createdSpaceId, capturedAccess.getSpaceId());
        assertEquals(param.getCurrentUserId(), capturedAccess.getCreatedBy());
        assertEquals(param.getCurrentUserId(), capturedAccess.getUserId());
    }

    private CreateSpaceUseCase.Param createParam(Consumer<CreateSpaceUseCase.Param.ParamBuilder> changer) {
        var paramBuilder = paramBuilder();
        changer.accept(paramBuilder);
        return paramBuilder.build();
    }

    private CreateSpaceUseCase.Param.ParamBuilder paramBuilder() {
        return CreateSpaceUseCase.Param.builder()
            .title("title")
            .type("PERSONAL")
            .currentUserId(UUID.randomUUID());
    }
}
