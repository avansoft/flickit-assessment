package org.flickit.assessment.kit.application.service.maturitylevel;

import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.kit.application.domain.AssessmentKit;
import org.flickit.assessment.kit.application.domain.MaturityLevel;
import org.flickit.assessment.kit.application.port.in.maturitylevel.CreateMaturityLevelUseCase;
import org.flickit.assessment.kit.application.port.out.assessmentkit.LoadAssessmentKitPort;
import org.flickit.assessment.kit.application.port.out.expertgroup.LoadExpertGroupOwnerPort;
import org.flickit.assessment.kit.application.port.out.maturitylevel.CreateMaturityLevelPort;
import org.flickit.assessment.kit.test.fixture.application.AssessmentKitMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.function.Consumer;

import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateMaturityLevelServiceTest {

    @InjectMocks
    private CreateMaturityLevelService service;

    @Mock
    private CreateMaturityLevelPort createMaturityLevelPort;

    @Mock
    private LoadExpertGroupOwnerPort loadExpertGroupOwnerPort;

    @Mock
    private LoadAssessmentKitPort loadAssessmentKitPort;

    private final UUID ownerId = UUID.randomUUID();
    private final AssessmentKit kit = AssessmentKitMother.simpleKit();

    @Test
    void testCreateMaturityLevel_WhenCurrentUserIsNotOwner_ShouldThrowAccessDeniedException() {
        var param = createParam(CreateMaturityLevelUseCase.Param.ParamBuilder::build);

        when(loadAssessmentKitPort.load(param.getKitId())).thenReturn(kit);
        when(loadExpertGroupOwnerPort.loadOwnerId(kit.getExpertGroupId())).thenReturn(ownerId);

        var throwable = assertThrows(AccessDeniedException.class, () -> service.createMaturityLevel(param));
        assertEquals(COMMON_CURRENT_USER_NOT_ALLOWED, throwable.getMessage());
    }

    @Test
    void testCreateMaturityLevel_WhenCurrentUserIsOwner_ThenCreateMaturityLevel() {
        long levelId = 123L;
        var param = createParam(b -> b.currentUserId(ownerId));

        when(loadAssessmentKitPort.load(param.getKitId())).thenReturn(kit);
        when(loadExpertGroupOwnerPort.loadOwnerId(kit.getExpertGroupId())).thenReturn(ownerId);
        when(createMaturityLevelPort.persist(any(MaturityLevel.class), anyLong(), any(UUID.class))).thenReturn(levelId);

        long actualLevelId = service.createMaturityLevel(param);
        assertEquals(levelId, actualLevelId);
    }

    private CreateMaturityLevelUseCase.Param createParam(Consumer<CreateMaturityLevelUseCase.Param.ParamBuilder> changer) {
        var paramBuilder = paramBuilder();
        changer.accept(paramBuilder);
        return paramBuilder.build();
    }

    private CreateMaturityLevelUseCase.Param.ParamBuilder paramBuilder() {
        return CreateMaturityLevelUseCase.Param.builder()
            .kitId(1L)
            .index(1)
            .title("title")
            .description("description")
            .value(1)
            .currentUserId(UUID.randomUUID());
    }
}