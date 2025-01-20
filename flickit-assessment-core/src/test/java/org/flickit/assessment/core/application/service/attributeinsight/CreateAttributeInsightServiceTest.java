package org.flickit.assessment.core.application.service.attributeinsight;

import org.flickit.assessment.common.application.domain.assessment.AssessmentAccessChecker;
import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.core.application.domain.AssessmentResult;
import org.flickit.assessment.core.application.domain.AttributeInsight;
import org.flickit.assessment.core.application.port.in.attributeinsight.CreateAttributeInsightUseCase;
import org.flickit.assessment.core.application.port.out.assessmentresult.LoadAssessmentResultPort;
import org.flickit.assessment.core.application.port.out.attributeinsight.CreateAttributeInsightPort;
import org.flickit.assessment.core.application.port.out.attributeinsight.LoadAttributeInsightPort;
import org.flickit.assessment.core.application.port.out.attributeinsight.UpdateAttributeInsightPort;
import org.flickit.assessment.core.test.fixture.application.AssessmentResultMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static org.flickit.assessment.common.application.domain.assessment.AssessmentPermission.CREATE_ATTRIBUTE_INSIGHT;
import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.flickit.assessment.core.common.ErrorMessageKey.CREATE_ATTRIBUTE_INSIGHT_ASSESSMENT_RESULT_NOT_FOUND;
import static org.flickit.assessment.core.test.fixture.application.AttributeInsightMother.simpleAttributeAiInsight;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAttributeInsightServiceTest {

    @InjectMocks
    private CreateAttributeInsightService service;

    @Mock
    private AssessmentAccessChecker assessmentAccessChecker;

    @Mock
    private LoadAttributeInsightPort loadAttributeInsightPort;

    @Mock
    private LoadAssessmentResultPort loadAssessmentResultPort;

    @Mock
    private UpdateAttributeInsightPort updateAttributeInsightPort;

    @Mock
    private CreateAttributeInsightPort createAttributeInsightPort;

    private final AssessmentResult assessmentResult = AssessmentResultMother.validResult();

    @Test
    void testCreateAttributeInsight_whenCurrentUserDoesNotHaveRequiredPermission_thenThrowAccessDeniedException() {
        var param = createParam(CreateAttributeInsightUseCase.Param.ParamBuilder::build);

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), param.getCurrentUserId(), CREATE_ATTRIBUTE_INSIGHT))
            .thenReturn(false);

        var throwable = assertThrows(AccessDeniedException.class, () -> service.createAttributeInsight(param));
        assertEquals(COMMON_CURRENT_USER_NOT_ALLOWED, throwable.getMessage());

        verifyNoInteractions(loadAttributeInsightPort,
            loadAssessmentResultPort,
            updateAttributeInsightPort,
            createAttributeInsightPort);
    }

    @Test
    void testCreateAttributeInsight_whenNoAssessmentResultExists_thenThrowsResourceNotFoundException() {
        var param = createParam(CreateAttributeInsightUseCase.Param.ParamBuilder::build);

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), param.getCurrentUserId(), CREATE_ATTRIBUTE_INSIGHT))
            .thenReturn(true);
        when(loadAssessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.empty());

        var throwable = assertThrows(ResourceNotFoundException.class, () -> service.createAttributeInsight(param));
        assertEquals(CREATE_ATTRIBUTE_INSIGHT_ASSESSMENT_RESULT_NOT_FOUND, throwable.getMessage());

        verifyNoInteractions(loadAttributeInsightPort, updateAttributeInsightPort, createAttributeInsightPort);
    }

    @Test
    void testCreateAttributeInsight_whenAttributeInsightDoesNotExist_thenCreateAttributeInsight() {
        var param = createParam(CreateAttributeInsightUseCase.Param.ParamBuilder::build);

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), param.getCurrentUserId(), CREATE_ATTRIBUTE_INSIGHT))
            .thenReturn(true);
        when(loadAssessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.of(assessmentResult));
        when(loadAttributeInsightPort.load(assessmentResult.getId(), param.getAttributeId())).thenReturn(Optional.empty());

        service.createAttributeInsight(param);

        ArgumentCaptor<AttributeInsight> captor = ArgumentCaptor.forClass(AttributeInsight.class);
        verify(createAttributeInsightPort).persist(captor.capture());
        assertEquals(assessmentResult.getId(), captor.getValue().getAssessmentResultId());
        assertEquals(param.getAttributeId(), captor.getValue().getAttributeId());
        assertEquals(param.getAssessorInsight(), captor.getValue().getAssessorInsight());
        assertNotNull(captor.getValue().getAssessorInsightTime());
        assertTrue(captor.getValue().isApproved());
        assertNotNull(captor.getValue().getLastModificationTime());

        verifyNoInteractions(updateAttributeInsightPort);
    }

    @Test
    void testCreateAttributeInsight_whenAttributeInsightExists_thenUpdateAttributeInsight() {
        var param = createParam(CreateAttributeInsightUseCase.Param.ParamBuilder::build);
        AttributeInsight attributeInsight = simpleAttributeAiInsight();

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), param.getCurrentUserId(), CREATE_ATTRIBUTE_INSIGHT))
            .thenReturn(true);
        when(loadAssessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.of(assessmentResult));
        when(loadAttributeInsightPort.load(assessmentResult.getId(), param.getAttributeId())).thenReturn(Optional.of(attributeInsight));

        service.createAttributeInsight(param);

        ArgumentCaptor<UpdateAttributeInsightPort.AssessorParam> captor = ArgumentCaptor.forClass(UpdateAttributeInsightPort.AssessorParam.class);
        verify(updateAttributeInsightPort).updateAssessorInsight(captor.capture());
        assertEquals(assessmentResult.getId(), captor.getValue().assessmentResultId());
        assertEquals(param.getAttributeId(), captor.getValue().attributeId());
        assertEquals(param.getAssessorInsight(), captor.getValue().assessorInsight());
        assertNotNull(captor.getValue().assessorInsightTime());
        assertTrue(captor.getValue().isApproved());
        assertNotNull(captor.getValue().lastModificationTime());

        verifyNoInteractions(createAttributeInsightPort);
    }

    private CreateAttributeInsightUseCase.Param createParam(Consumer<CreateAttributeInsightUseCase.Param.ParamBuilder> changer) {
        var paramBuilder = paramBuilder();
        changer.accept(paramBuilder);
        return paramBuilder.build();
    }

    private CreateAttributeInsightUseCase.Param.ParamBuilder paramBuilder() {
        return CreateAttributeInsightUseCase.Param.builder()
            .assessmentId(UUID.randomUUID())
            .attributeId(123L)
            .assessorInsight("assessorInsight")
            .currentUserId(UUID.randomUUID());
    }
}
