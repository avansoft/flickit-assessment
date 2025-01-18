package org.flickit.assessment.kit.application.service.assessmentkit;

import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.kit.application.domain.KitVersionStatus;
import org.flickit.assessment.kit.application.port.in.assessmentkit.CreateAssessmentKitUseCase;
import org.flickit.assessment.kit.application.port.out.assessmentkit.CreateAssessmentKitPort;
import org.flickit.assessment.kit.application.port.out.assessmentkittag.CreateKitTagRelationPort;
import org.flickit.assessment.kit.application.port.out.expertgroup.LoadExpertGroupMemberIdsPort;
import org.flickit.assessment.kit.application.port.out.expertgroup.LoadExpertGroupOwnerPort;
import org.flickit.assessment.kit.application.port.out.kituseraccess.GrantUserAccessToKitPort;
import org.flickit.assessment.kit.application.port.out.kitversion.CreateKitVersionPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.flickit.assessment.common.util.SlugCodeUtil.generateSlugCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAssessmentKitServiceTest {

    @InjectMocks
    private CreateAssessmentKitService service;

    @Mock
    private LoadExpertGroupOwnerPort loadExpertGroupOwnerPort;

    @Mock
    private CreateAssessmentKitPort createAssessmentKitPort;

    @Mock
    private CreateKitVersionPort createKitVersionPort;

    @Mock
    private LoadExpertGroupMemberIdsPort loadExpertGroupMemberIdsPort;

    @Mock
    private GrantUserAccessToKitPort grantUserAccessToKitPort;

    @Mock
    private CreateKitTagRelationPort createKitTagRelationPort;

    private final UUID ownerId = UUID.randomUUID();

    @Test
    void testCreateAssessmentKit_CurrentUserDoesNotHaveAccess_ShouldFailToCreateAssessmentKit() {
        var param = createParam(CreateAssessmentKitUseCase.Param.ParamBuilder::build);

        when(loadExpertGroupOwnerPort.loadOwnerId(param.getExpertGroupId())).thenReturn(ownerId);

        var throwable = assertThrows(AccessDeniedException.class, () -> service.createAssessmentKit(param));
        assertEquals(COMMON_CURRENT_USER_NOT_ALLOWED, throwable.getMessage());

        verify(loadExpertGroupOwnerPort).loadOwnerId(param.getExpertGroupId());
        verifyNoMoreInteractions(createAssessmentKitPort,
            createKitVersionPort,
            loadExpertGroupMemberIdsPort,
            grantUserAccessToKitPort,
            createKitTagRelationPort);
    }

    @Test
    void testCreateAssessmentKit_validParameters_CreateAssessmentKit() {
        var kitId = 2L;
        var kitVersionId = 22L;
        var param = createParam(b -> b.currentUserId(ownerId));

        when(loadExpertGroupOwnerPort.loadOwnerId(param.getExpertGroupId())).thenReturn(ownerId);
        when(createAssessmentKitPort.persist(any())).thenReturn(kitId);
        when(createKitVersionPort.persist(any())).thenReturn(kitVersionId);
        when(loadExpertGroupMemberIdsPort.loadMemberIds(param.getExpertGroupId()))
            .thenReturn(List.of(new LoadExpertGroupMemberIdsPort.Result(param.getCurrentUserId())));

        var result = service.createAssessmentKit(param);

        assertNotNull(result);
        assertEquals(kitId, result.kitId());

        ArgumentCaptor<CreateAssessmentKitPort.Param> createKitPortParamCaptor = ArgumentCaptor.forClass(CreateAssessmentKitPort.Param.class);
        verify(createAssessmentKitPort).persist(createKitPortParamCaptor.capture());
        assertEquals(generateSlugCode(param.getTitle()), createKitPortParamCaptor.getValue().code());
        assertEquals(param.getTitle(), createKitPortParamCaptor.getValue().title());
        assertEquals(param.getSummary(), createKitPortParamCaptor.getValue().summary());
        assertEquals(param.getAbout(), createKitPortParamCaptor.getValue().about());
        assertEquals(param.getLang(), createKitPortParamCaptor.getValue().lang());
        assertFalse(createKitPortParamCaptor.getValue().published());
        assertEquals(param.getIsPrivate(), createKitPortParamCaptor.getValue().isPrivate());
        assertEquals(param.getExpertGroupId(), createKitPortParamCaptor.getValue().expertGroupId());
        assertEquals(param.getCurrentUserId(), createKitPortParamCaptor.getValue().createdBy());

        ArgumentCaptor<CreateKitVersionPort.Param> createVersionPortParamCaptor = ArgumentCaptor.forClass(CreateKitVersionPort.Param.class);
        verify(createKitVersionPort).persist(createVersionPortParamCaptor.capture());
        assertEquals(kitId, createVersionPortParamCaptor.getValue().kitId());
        assertEquals(KitVersionStatus.UPDATING, createVersionPortParamCaptor.getValue().status());
        assertEquals(param.getCurrentUserId(), createVersionPortParamCaptor.getValue().createdBy());

        verify(grantUserAccessToKitPort, times(1)).grantUsersAccess(kitId, List.of(param.getCurrentUserId()));
        verify(createKitTagRelationPort, times(1)).persist(param.getTagIds(), kitId);
    }

    private CreateAssessmentKitUseCase.Param createParam(Consumer<CreateAssessmentKitUseCase.Param.ParamBuilder> changer) {
        var paramBuilder = paramBuilder();
        changer.accept(paramBuilder);
        return paramBuilder.build();
    }

    private CreateAssessmentKitUseCase.Param.ParamBuilder paramBuilder() {
        return CreateAssessmentKitUseCase.Param.builder()
            .title("Enterprise")
            .summary("summary")
            .about("about")
            .lang("EN")
            .isPrivate(true)
            .expertGroupId(123L)
            .tagIds(List.of(1L, 2L, 3L))
            .currentUserId(UUID.randomUUID());
    }
}
