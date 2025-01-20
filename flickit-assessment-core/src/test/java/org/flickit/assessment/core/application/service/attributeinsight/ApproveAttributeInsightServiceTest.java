package org.flickit.assessment.core.application.service.attributeinsight;

import org.flickit.assessment.common.application.domain.assessment.AssessmentAccessChecker;
import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.core.application.port.in.attributeinsight.ApproveAttributeInsightUseCase;
import org.flickit.assessment.core.application.port.out.attributeinsight.ApproveAttributeInsightPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Consumer;

import static org.flickit.assessment.common.application.domain.assessment.AssessmentPermission.APPROVE_ATTRIBUTE_INSIGHT;
import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApproveAttributeInsightServiceTest {

    @InjectMocks
    private ApproveAttributeInsightService service;

    @Mock
    private ApproveAttributeInsightPort approveAttributeInsightPort;

    @Mock
    private AssessmentAccessChecker assessmentAccessChecker;

    @Test
    void testApproveAttributeInsight_whenCurrentUserDoesNotHaveRequiredPermissions_thenThrowAccessDeniedException() {
        var param = createParam(ApproveAttributeInsightUseCase.Param.ParamBuilder::build);

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(),
            param.getCurrentUserId(),
            APPROVE_ATTRIBUTE_INSIGHT))
            .thenReturn(false);

        var throwable = assertThrows(AccessDeniedException.class, () -> service.approveAttributeInsight(param));
        assertEquals(COMMON_CURRENT_USER_NOT_ALLOWED, throwable.getMessage());

        verifyNoInteractions(approveAttributeInsightPort);
    }

    @Test
    void testApproveAttributeInsight_whenCurrentUserHasRequiredPermission_thenApproveAttributeInsight() {
        var param = createParam(ApproveAttributeInsightUseCase.Param.ParamBuilder::build);

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), param.getCurrentUserId(), APPROVE_ATTRIBUTE_INSIGHT))
            .thenReturn(true);
        doNothing().when(approveAttributeInsightPort).approve(eq(param.getAssessmentId()), eq(param.getAttributeId()), any(LocalDateTime.class));

        service.approveAttributeInsight(param);

        verify(approveAttributeInsightPort).approve(eq(param.getAssessmentId()), eq(param.getAttributeId()), any(LocalDateTime.class));
    }

    private ApproveAttributeInsightUseCase.Param createParam(Consumer<ApproveAttributeInsightUseCase.Param.ParamBuilder> changer) {
        var param = paramBuilder();
        changer.accept(param);
        return param.build();
    }

    private ApproveAttributeInsightUseCase.Param.ParamBuilder paramBuilder() {
        return ApproveAttributeInsightUseCase.Param.builder()
            .assessmentId(UUID.randomUUID())
            .attributeId(1L)
            .currentUserId(UUID.randomUUID());
    }
}
