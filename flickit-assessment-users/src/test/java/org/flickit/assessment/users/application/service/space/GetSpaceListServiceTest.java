package org.flickit.assessment.users.application.service.space;

import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.data.jpa.users.spaceuseraccess.SpaceUserAccessJpaEntity;
import org.flickit.assessment.data.jpa.users.user.UserJpaEntity;
import org.flickit.assessment.users.application.port.in.space.GetSpaceListUseCase;
import org.flickit.assessment.users.application.port.out.space.LoadSpaceListPort;
import org.flickit.assessment.users.application.port.out.space.LoadSpaceListPort.Result;
import org.flickit.assessment.users.test.fixture.application.SpaceMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetSpaceListServiceTest {

    @InjectMocks
    private GetSpaceListService service;

    @Mock
    private LoadSpaceListPort loadSpaceListPort;

    @Test
    void testGetSpaceList_validInputs_validResults() {
        int size = 10;
        int page = 0;
        UUID currentUserId = UUID.randomUUID();
        var space1 = SpaceMother.createPersonalSpace(currentUserId);
        var space2 = SpaceMother.createPersonalSpace(UUID.randomUUID());
        String ownerName = "sample name";
        var spacePortList = List.of(
            new LoadSpaceListPort.Result(space1, ownerName, 2, 5),
            new LoadSpaceListPort.Result(space2, ownerName, 4, 3));

        PaginatedResponse<LoadSpaceListPort.Result> paginatedResponse = new PaginatedResponse<>(
            spacePortList,
            page,
            size,
            SpaceUserAccessJpaEntity.Fields.lastSeen,
            Sort.Direction.DESC.name().toLowerCase(),
            spacePortList.size());

        when(loadSpaceListPort.loadSpaceList(currentUserId, page, size)).thenReturn(paginatedResponse);

        GetSpaceListUseCase.Param param = new GetSpaceListUseCase.Param(size, page, currentUserId);
        var result = service.getSpaceList(param);

        assertNotNull(paginatedResponse);
        assertNotNull(result.getItems());
        assertEquals(2, result.getItems().size());
        assertEquals(spacePortList.getFirst().space().getId(), result.getItems().getFirst().id());
        assertEquals(spacePortList.getFirst().space().getTitle(), result.getItems().getFirst().title());
        assertEquals(space1.getOwnerId(), result.getItems().getFirst().owner().id());
        assertEquals(ownerName, result.getItems().getFirst().owner().displayName());
        assertTrue(result.getItems().getFirst().owner().isCurrentUserOwner());
        assertEquals(spacePortList.getFirst().space().getLastModificationTime(), result.getItems().getFirst().lastModificationTime());
        assertEquals(spacePortList.getFirst().assessmentsCount(), result.getItems().getFirst().assessmentsCount());
        assertEquals(spacePortList.getFirst().membersCount(), result.getItems().getFirst().membersCount());

        assertEquals(spacePortList.get(1).space().getId(), result.getItems().get(1).id());
        assertEquals(spacePortList.get(1).space().getTitle(), result.getItems().get(1).title());
        assertEquals(space2.getOwnerId(), result.getItems().get(1).owner().id());
        assertEquals(ownerName, result.getItems().get(1).owner().displayName());
        assertFalse(result.getItems().get(1).owner().isCurrentUserOwner());
        assertEquals(spacePortList.get(1).space().getLastModificationTime(), result.getItems().get(1).lastModificationTime());
        assertEquals(spacePortList.get(1).assessmentsCount(), result.getItems().get(1).assessmentsCount());
        assertEquals(spacePortList.get(1).membersCount(), result.getItems().get(1).membersCount());
    }

    @Test
    void testGetSpaceList_ValidInputs_emptyResults() {
        int page = 0;
        int size = 10;
        UUID currentUserId = UUID.randomUUID();

        PaginatedResponse<Result> paginatedResponse = new PaginatedResponse<>(
            Collections.emptyList(),
            page,
            size,
            UserJpaEntity.Fields.displayName,
            Sort.Direction.ASC.name().toLowerCase(),
            0);
        when(loadSpaceListPort.loadSpaceList(currentUserId, page, size)).thenReturn(paginatedResponse);

        var param = new GetSpaceListUseCase.Param(size, page, currentUserId);
        var result = service.getSpaceList(param);

        assertNotNull(paginatedResponse);
        assertNotNull(result.getItems());
        assertEquals(0, result.getItems().size());
    }
}
