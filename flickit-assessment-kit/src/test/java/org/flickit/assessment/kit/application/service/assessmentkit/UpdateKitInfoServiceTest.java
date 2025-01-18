package org.flickit.assessment.kit.application.service.assessmentkit;

import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.kit.application.domain.AssessmentKit;
import org.flickit.assessment.kit.application.domain.ExpertGroup;
import org.flickit.assessment.kit.application.port.in.assessmentkit.UpdateKitInfoUseCase;
import org.flickit.assessment.kit.application.port.in.assessmentkit.UpdateKitInfoUseCase.Param;
import org.flickit.assessment.kit.application.port.out.assessmentkit.UpdateKitInfoPort;
import org.flickit.assessment.kit.application.port.out.expertgroup.LoadKitExpertGroupPort;
import org.flickit.assessment.kit.test.fixture.application.AssessmentKitMother;
import org.flickit.assessment.kit.test.fixture.application.ExpertGroupMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.flickit.assessment.common.util.SlugCodeUtil.generateSlugCode;
import static org.flickit.assessment.kit.common.ErrorMessageKey.KIT_ID_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateKitInfoServiceTest {

    @InjectMocks
    private UpdateKitInfoService service;

    @Mock
    private LoadKitExpertGroupPort loadKitExpertGroupPort;

    @Mock
    private UpdateKitInfoPort updateKitInfoPort;

    @Test
    void testUpdateKitInfo_KitNotFound_ErrorMessage() {
        ExpertGroup expertGroup = ExpertGroupMother.createExpertGroup();
        UUID currentUserId = expertGroup.getOwnerId();
        var param = createParam(b -> b.currentUserId(currentUserId));

        when(loadKitExpertGroupPort.loadKitExpertGroup(param.getKitId())).thenThrow(new ResourceNotFoundException(KIT_ID_NOT_FOUND));

        var throwable = assertThrows(ResourceNotFoundException.class,
            () -> service.updateKitInfo(param));
        assertThat(throwable).hasMessage(KIT_ID_NOT_FOUND);
    }

    @Test
    void testUpdateKitInfo_CurrentUserNotAllowed_ErrorMessage() {
        ExpertGroup expertGroup = ExpertGroupMother.createExpertGroup();
        var param = createParam(UpdateKitInfoUseCase.Param.ParamBuilder::build);

        when(loadKitExpertGroupPort.loadKitExpertGroup(param.getKitId())).thenReturn(expertGroup);

        var throwable = assertThrows(AccessDeniedException.class,
            () -> service.updateKitInfo(param));
        assertThat(throwable).hasMessage(COMMON_CURRENT_USER_NOT_ALLOWED);
    }

    @Test
    void testUpdateKitInfo_EditTitle_ValidResults() {
        ExpertGroup expertGroup = ExpertGroupMother.createExpertGroup();
        UUID currentUserId = expertGroup.getOwnerId();
        var param = createParam(b -> b.title("new title").currentUserId(currentUserId));
        String newCode = generateSlugCode(param.getTitle());

        when(loadKitExpertGroupPort.loadKitExpertGroup(param.getKitId())).thenReturn(expertGroup);
        doNothing().when(updateKitInfoPort).update(any());

        service.updateKitInfo(param);

        ArgumentCaptor<UpdateKitInfoPort.Param> portParam = ArgumentCaptor.forClass(UpdateKitInfoPort.Param.class);
        verify(updateKitInfoPort, times(1)).update(portParam.capture());

        assertEquals(param.getKitId(), portParam.getValue().kitId());
        assertEquals(param.getTitle(), portParam.getValue().title());
        assertEquals(newCode, portParam.getValue().code());
    }

    @Test
    void testUpdateKitInfo_EditSummary_ValidResults() {
        ExpertGroup expertGroup = ExpertGroupMother.createExpertGroup();
        UUID currentUserId = expertGroup.getOwnerId();
        var param = createParam(b -> b.summary("new summary").currentUserId(currentUserId));

        when(loadKitExpertGroupPort.loadKitExpertGroup(param.getKitId())).thenReturn(expertGroup);
        doNothing().when(updateKitInfoPort).update(any());

        service.updateKitInfo(param);

        ArgumentCaptor<UpdateKitInfoPort.Param> portParam = ArgumentCaptor.forClass(UpdateKitInfoPort.Param.class);
        verify(updateKitInfoPort, times(1)).update(portParam.capture());

        assertEquals(param.getKitId(), portParam.getValue().kitId());
        assertEquals(param.getSummary(), portParam.getValue().summary());
    }

    @Test
    void testUpdateKitInfo_EditPublished_ValidResults() {
        ExpertGroup expertGroup = ExpertGroupMother.createExpertGroup();
        UUID currentUserId = expertGroup.getOwnerId();
        var param = createParam(b -> b.published(false).currentUserId(currentUserId));

        when(loadKitExpertGroupPort.loadKitExpertGroup(param.getKitId())).thenReturn(expertGroup);
        doNothing().when(updateKitInfoPort).update(any());

        service.updateKitInfo(param);

        ArgumentCaptor<UpdateKitInfoPort.Param> portParam = ArgumentCaptor.forClass(UpdateKitInfoPort.Param.class);
        verify(updateKitInfoPort, times(1)).update(portParam.capture());

        assertEquals(param.getKitId(), portParam.getValue().kitId());
        assertEquals(param.getPublished(), portParam.getValue().published());
    }

    @Test
    void testUpdateKitInfo_EditIsPrivate_ValidResults() {
        ExpertGroup expertGroup = ExpertGroupMother.createExpertGroup();
        UUID currentUserId = expertGroup.getOwnerId();
        var param = createParam(b -> b.isPrivate(true).currentUserId(currentUserId));

        when(loadKitExpertGroupPort.loadKitExpertGroup(param.getKitId())).thenReturn(expertGroup);
        doNothing().when(updateKitInfoPort).update(any());

        service.updateKitInfo(param);

        ArgumentCaptor<UpdateKitInfoPort.Param> portParam = ArgumentCaptor.forClass(UpdateKitInfoPort.Param.class);
        verify(updateKitInfoPort, times(1)).update(portParam.capture());

        assertEquals(param.getKitId(), portParam.getValue().kitId());
        assertEquals(param.getIsPrivate(), portParam.getValue().isPrivate());
    }

    @Test
    void testUpdateKitInfo_EditPrice_ValidResults() {
        ExpertGroup expertGroup = ExpertGroupMother.createExpertGroup();
        UUID currentUserId = expertGroup.getOwnerId();
        var param = createParam(b -> b.price(2d).currentUserId(currentUserId));

        when(loadKitExpertGroupPort.loadKitExpertGroup(param.getKitId())).thenReturn(expertGroup);
        doNothing().when(updateKitInfoPort).update(any());

        service.updateKitInfo(param);

        ArgumentCaptor<UpdateKitInfoPort.Param> portParam = ArgumentCaptor.forClass(UpdateKitInfoPort.Param.class);
        verify(updateKitInfoPort, times(1)).update(portParam.capture());

        assertEquals(param.getKitId(), portParam.getValue().kitId());
        assertEquals(param.getPrice(), portParam.getValue().price());
    }

    @Test
    void testUpdateKitInfo_EditAbout_ValidResults() {
        ExpertGroup expertGroup = ExpertGroupMother.createExpertGroup();
        UUID currentUserId = expertGroup.getOwnerId();
        var param = createParam(b -> b.about("new about").currentUserId(currentUserId));

        when(loadKitExpertGroupPort.loadKitExpertGroup(param.getKitId())).thenReturn(expertGroup);
        doNothing().when(updateKitInfoPort).update(any());

        service.updateKitInfo(param);

        ArgumentCaptor<UpdateKitInfoPort.Param> portParam = ArgumentCaptor.forClass(UpdateKitInfoPort.Param.class);
        verify(updateKitInfoPort, times(1)).update(portParam.capture());

        assertEquals(param.getKitId(), portParam.getValue().kitId());
        assertEquals(param.getAbout(), portParam.getValue().about());
    }

    @Test
    void testUpdateKitInfo_EditLang_ValidResults() {
        ExpertGroup expertGroup = ExpertGroupMother.createExpertGroup();
        UUID currentUserId = expertGroup.getOwnerId();
        var param = createParam(b -> b.lang("FA").currentUserId(currentUserId));

        when(loadKitExpertGroupPort.loadKitExpertGroup(param.getKitId())).thenReturn(expertGroup);
        doNothing().when(updateKitInfoPort).update(any());

        service.updateKitInfo(param);

        ArgumentCaptor<UpdateKitInfoPort.Param> portParam = ArgumentCaptor.forClass(UpdateKitInfoPort.Param.class);
        verify(updateKitInfoPort, times(1)).update(portParam.capture());

        assertEquals(param.getKitId(), portParam.getValue().kitId());
        assertEquals(param.getLang(), portParam.getValue().lang());
    }

    @Test
    void testUpdateKitInfo_EditTags_ValidResults() {
        ExpertGroup expertGroup = ExpertGroupMother.createExpertGroup();
        UUID currentUserId = expertGroup.getOwnerId();
        var param = createParam(b -> b.tags(List.of(3L)).currentUserId(currentUserId));

        when(loadKitExpertGroupPort.loadKitExpertGroup(param.getKitId())).thenReturn(expertGroup);
        doNothing().when(updateKitInfoPort).update(any());

        service.updateKitInfo(param);

        ArgumentCaptor<UpdateKitInfoPort.Param> portParam = ArgumentCaptor.forClass(UpdateKitInfoPort.Param.class);
        verify(updateKitInfoPort, times(1)).update(portParam.capture());

        assertEquals(param.getKitId(), portParam.getValue().kitId());
        assertIterableEquals(param.getTags(), portParam.getValue().tags());
    }

    @Test
    void testUpdateKitInfo_EditNothing_ValidResults() {
        ExpertGroup expertGroup = ExpertGroupMother.createExpertGroup();
        UUID currentUserId = expertGroup.getOwnerId();
        AssessmentKit assessmentKit = AssessmentKitMother.simpleKit();
        var param = createParam(b -> b.kitId(assessmentKit.getId()).currentUserId(currentUserId));

        when(loadKitExpertGroupPort.loadKitExpertGroup(param.getKitId())).thenReturn(expertGroup);

        service.updateKitInfo(param);

        verify(updateKitInfoPort, never()).update(any());
    }

    private UpdateKitInfoUseCase.Param createParam(Consumer<Param.ParamBuilder> changer) {
        var param = paramBuilder();
        changer.accept(param);
        return param.build();
    }

    private UpdateKitInfoUseCase.Param.ParamBuilder paramBuilder() {
        return UpdateKitInfoUseCase.Param.builder()
            .kitId(1L)
            .currentUserId(UUID.randomUUID());
    }
}
