package org.flickit.assessment.core.application.service.attributeinsight;

import org.flickit.assessment.common.application.domain.assessment.AssessmentAccessChecker;
import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.core.application.domain.AttributeInsight;
import org.flickit.assessment.core.application.port.in.attributeinsight.GetAttributeInsightUseCase;
import org.flickit.assessment.core.application.port.out.assessmentresult.LoadAssessmentResultPort;
import org.flickit.assessment.core.application.port.out.attributeinsight.LoadAttributeInsightPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.flickit.assessment.common.application.domain.assessment.AssessmentPermission.CREATE_ATTRIBUTE_INSIGHT;
import static org.flickit.assessment.common.application.domain.assessment.AssessmentPermission.VIEW_SUBJECT_REPORT;
import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.flickit.assessment.core.common.ErrorMessageKey.GET_ATTRIBUTE_INSIGHT_ASSESSMENT_RESULT_NOT_FOUND;
import static org.flickit.assessment.core.test.fixture.application.AssessmentResultMother.validResult;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAttributeInsightServiceTest {

    @InjectMocks
    private GetAttributeInsightService service;

    @Mock
    private AssessmentAccessChecker assessmentAccessChecker;

    @Mock
    private LoadAttributeInsightPort loadAttributeInsightPort;

    @Mock
    private LoadAssessmentResultPort assessmentResultPort;

    @Test
    void testGetAttributeInsight_UserDoesNotHaveRequiredPermission_ThrowAccessDeniedException() {
        var currentUserId = UUID.randomUUID();
        var param = new GetAttributeInsightUseCase.Param(UUID.randomUUID(), 1L, currentUserId);

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, VIEW_SUBJECT_REPORT)).thenReturn(false);

        var throwable = assertThrows(AccessDeniedException.class, () -> service.getInsight(param));
        assertEquals(COMMON_CURRENT_USER_NOT_ALLOWED, throwable.getMessage());

        verifyNoInteractions(loadAttributeInsightPort, assessmentResultPort);
    }

    @Test
    void testGetAttributeInsight_AssessmentResultDoesNotExist_ThrowResourceNotFoundException() {
        var currentUserId = UUID.randomUUID();
        var param = new GetAttributeInsightUseCase.Param(UUID.randomUUID(), 123L, currentUserId);

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, VIEW_SUBJECT_REPORT)).thenReturn(true);
        when(assessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.empty());

        var throwable = assertThrows(ResourceNotFoundException.class, () -> service.getInsight(param));
        assertEquals(GET_ATTRIBUTE_INSIGHT_ASSESSMENT_RESULT_NOT_FOUND, throwable.getMessage());

        verifyNoInteractions(loadAttributeInsightPort);
    }

    @Test
    void testGetAttributeInsight_AttributeInsightDoesNotExist_UserHasCreateInsightPermission() {
        var attributeId = 1L;
        var currentUserId = UUID.randomUUID();
        var param = new GetAttributeInsightUseCase.Param(UUID.randomUUID(), attributeId, currentUserId);
        var assessmentResult = validResult();

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, VIEW_SUBJECT_REPORT)).thenReturn(true);
        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, CREATE_ATTRIBUTE_INSIGHT)).thenReturn(true);
        when(assessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.of(assessmentResult));
        when(loadAttributeInsightPort.load(assessmentResult.getId(), attributeId)).thenReturn(Optional.empty());

        var result = assertDoesNotThrow(() -> service.getInsight(param));
        assertNotNull(result);
        assertNull(result.aiInsight());
        assertNull(result.assessorInsight());
        assertTrue(result.editable());
        assertFalse(result.approved());
    }

    @Test
    void testGetAttributeInsight_AttributeInsightDoesNotExist_UserDoesNotHaveCreateInsightPermission() {
        var attributeId = 1L;
        var currentUserId = UUID.randomUUID();
        var param = new GetAttributeInsightUseCase.Param(UUID.randomUUID(), attributeId, currentUserId);
        var assessmentResult = validResult();

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, VIEW_SUBJECT_REPORT)).thenReturn(true);
        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, CREATE_ATTRIBUTE_INSIGHT)).thenReturn(false);
        when(assessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.of(assessmentResult));
        when(loadAttributeInsightPort.load(assessmentResult.getId(), attributeId)).thenReturn(Optional.empty());

        var result = assertDoesNotThrow(() -> service.getInsight(param));
        assertNotNull(result);
        assertNull(result.aiInsight());
        assertNull(result.assessorInsight());
        assertFalse(result.editable());
        assertFalse(result.approved());
    }

    @Test
    void testGetAttributeInsight_AssessorInsightIsNotNullAndAfterThanAiInsightAndIsValidBasedOnInsightTime_ReturnAssessorInsight() {
        var attributeId = 1L;
        var currentUserId = UUID.randomUUID();
        var param = new GetAttributeInsightUseCase.Param(UUID.randomUUID(), attributeId, currentUserId);
        var assessmentResult = validResult();
        var attributeInsight = new AttributeInsight(assessmentResult.getId(),
            attributeId,
            "ai insight ",
            "assessor insight",
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(1),
            "input path",
            true,
            LocalDateTime.now().plusDays(1));

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, VIEW_SUBJECT_REPORT)).thenReturn(true);
        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, CREATE_ATTRIBUTE_INSIGHT)).thenReturn(true);
        when(assessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.of(assessmentResult));
        when(loadAttributeInsightPort.load(assessmentResult.getId(), attributeId)).thenReturn(Optional.of(attributeInsight));

        var result = service.getInsight(param);
        assertNotNull(result);
        assertNull(result.aiInsight());
        assertNotNull(result.assessorInsight());
        assertEquals(attributeInsight.getAssessorInsight(), result.assessorInsight().insight());
        assertEquals(attributeInsight.getAssessorInsightTime(), result.assessorInsight().creationTime());
        assertTrue(result.assessorInsight().isValid());
        assertTrue(result.editable());
        assertTrue(result.approved());
    }

    @Test
    void testGetAttributeInsight_AssessorInsightIsNotNullAndAfterThanAiInsightAndIsValidBasedOnLastModificationTime_ReturnAssessorInsight() {
        var attributeId = 1L;
        var currentUserId = UUID.randomUUID();
        var param = new GetAttributeInsightUseCase.Param(UUID.randomUUID(), attributeId, currentUserId);
        var assessmentResult = validResult();
        var attributeInsight = new AttributeInsight(assessmentResult.getId(),
            attributeId,
            "ai insight ",
            "assessor insight",
            LocalDateTime.now().minusDays(2),
            LocalDateTime.now().minusDays(1),
            "input path",
            true,
            LocalDateTime.now().plusDays(1));

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, VIEW_SUBJECT_REPORT)).thenReturn(true);
        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, CREATE_ATTRIBUTE_INSIGHT)).thenReturn(true);
        when(assessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.of(assessmentResult));
        when(loadAttributeInsightPort.load(assessmentResult.getId(), attributeId)).thenReturn(Optional.of(attributeInsight));

        var result = service.getInsight(param);
        assertNotNull(result);
        assertNull(result.aiInsight());
        assertNotNull(result.assessorInsight());
        assertEquals(attributeInsight.getAssessorInsight(), result.assessorInsight().insight());
        assertEquals(attributeInsight.getAssessorInsightTime(), result.assessorInsight().creationTime());
        assertTrue(result.assessorInsight().isValid());
        assertTrue(result.editable());
        assertTrue(result.approved());
    }

    @Test
    void testGetAttributeInsight_AssessorInsightIsNotNullAndBeforeThanAiInsightAndAiInsightIsValid_ReturnAiInsight() {
        var attributeId = 1L;
        var currentUserId = UUID.randomUUID();
        var param = new GetAttributeInsightUseCase.Param(UUID.randomUUID(), attributeId, currentUserId);
        var assessmentResult = validResult();
        var attributeInsight = new AttributeInsight(assessmentResult.getId(),
            attributeId,
            "ai insight ",
            "assessor insight",
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now(),
            "input path",
            true,
            LocalDateTime.now().plusDays(1));

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, VIEW_SUBJECT_REPORT)).thenReturn(true);
        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, CREATE_ATTRIBUTE_INSIGHT)).thenReturn(true);
        when(assessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.of(assessmentResult));
        when(loadAttributeInsightPort.load(assessmentResult.getId(), attributeId)).thenReturn(Optional.of(attributeInsight));

        var result = service.getInsight(param);
        assertNotNull(result);
        assertNotNull(result.aiInsight());
        assertNull(result.assessorInsight());
        assertEquals(attributeInsight.getAiInsight(), result.aiInsight().insight());
        assertEquals(attributeInsight.getAiInsightTime(), result.aiInsight().creationTime());
        assertTrue(result.aiInsight().isValid());
        assertTrue(result.editable());
        assertTrue(result.approved());
    }

    @Test
    void testGetAttributeInsight_AssessorInsightIsNotNullAndBeforeThanAiInsightAndAiInsightIsNotValid_ReturnAiInsight() {
        var attributeId = 1L;
        var currentUserId = UUID.randomUUID();
        var param = new GetAttributeInsightUseCase.Param(UUID.randomUUID(), attributeId, currentUserId);
        var assessmentResult = validResult();
        var attributeInsight = new AttributeInsight(assessmentResult.getId(),
            attributeId,
            "ai insight ",
            "assessor insight",
            LocalDateTime.now().minusDays(1),
            LocalDateTime.now().minusDays(2),
            "input path",
            true,
            LocalDateTime.now().minusDays(1));

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, VIEW_SUBJECT_REPORT)).thenReturn(true);
        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, CREATE_ATTRIBUTE_INSIGHT)).thenReturn(true);
        when(assessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.of(assessmentResult));
        when(loadAttributeInsightPort.load(assessmentResult.getId(), attributeId)).thenReturn(Optional.of(attributeInsight));

        var result = service.getInsight(param);
        assertNotNull(result);
        assertNotNull(result.aiInsight());
        assertNull(result.assessorInsight());
        assertEquals(attributeInsight.getAiInsight(), result.aiInsight().insight());
        assertEquals(attributeInsight.getAiInsightTime(), result.aiInsight().creationTime());
        assertFalse(result.aiInsight().isValid());
        assertTrue(result.editable());
        assertTrue(result.approved());
    }

    @Test
    void testGetAttributeInsight_AssessorInsightIsNotNullAndAfterThanAiInsightAndIsNotValid_ReturnAssessorInsight() {
        var attributeId = 1L;
        var currentUserId = UUID.randomUUID();
        var param = new GetAttributeInsightUseCase.Param(UUID.randomUUID(), attributeId, currentUserId);
        var assessmentResult = validResult();
        var attributeInsight = new AttributeInsight(assessmentResult.getId(),
            attributeId,
            "ai insight ",
            "assessor insight",
            LocalDateTime.now().minusDays(2),
            LocalDateTime.now().minusDays(1),
            "input path",
            true,
            LocalDateTime.now().minusDays(1));

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, VIEW_SUBJECT_REPORT)).thenReturn(true);
        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, CREATE_ATTRIBUTE_INSIGHT)).thenReturn(true);
        when(assessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.of(assessmentResult));
        when(loadAttributeInsightPort.load(assessmentResult.getId(), attributeId)).thenReturn(Optional.of(attributeInsight));

        var result = service.getInsight(param);
        assertNotNull(result);
        assertNull(result.aiInsight());
        assertNotNull(result.assessorInsight());
        assertEquals(attributeInsight.getAssessorInsight(), result.assessorInsight().insight());
        assertEquals(attributeInsight.getAssessorInsightTime(), result.assessorInsight().creationTime());
        assertFalse(result.assessorInsight().isValid());
        assertTrue(result.editable());
        assertTrue(result.approved());
    }

    @Test
    void testGetAttributeInsight_AssessorInsightIsNull_AiInsightIsNotValid_ReturnAiInsight() {
        var attributeId = 1L;
        var currentUserId = UUID.randomUUID();
        var param = new GetAttributeInsightUseCase.Param(UUID.randomUUID(), attributeId, currentUserId);
        var assessmentResult = validResult();
        var attributeInsight = new AttributeInsight(assessmentResult.getId(),
            attributeId,
            "ai insight ",
            null,
            LocalDateTime.now().minusDays(1),
            null,
            "input path",
            false,
            LocalDateTime.now().minusDays(1));

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, VIEW_SUBJECT_REPORT)).thenReturn(true);
        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, CREATE_ATTRIBUTE_INSIGHT)).thenReturn(true);
        when(assessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.of(assessmentResult));
        when(loadAttributeInsightPort.load(assessmentResult.getId(), attributeId)).thenReturn(Optional.of(attributeInsight));

        var result = service.getInsight(param);
        assertNotNull(result);
        assertNotNull(result.aiInsight());
        assertEquals(attributeInsight.getAiInsight(), result.aiInsight().insight());
        assertEquals(attributeInsight.getAiInsightTime(), result.aiInsight().creationTime());
        assertFalse(result.aiInsight().isValid());
        assertNull(result.assessorInsight());
        assertTrue(result.editable());
        assertFalse(result.approved());
    }

    @Test
    void testGetAttributeInsight_AssessorInsightIsNull_AiInsightIsValidBasedOnInsightTime_ReturnAiInsight() {
        var attributeId = 1L;
        var currentUserId = UUID.randomUUID();
        var param = new GetAttributeInsightUseCase.Param(UUID.randomUUID(), attributeId, currentUserId);
        var assessmentResult = validResult();
        var attributeInsight = new AttributeInsight(assessmentResult.getId(),
            attributeId,
            "ai insight ",
            null,
            LocalDateTime.now().plusDays(1),
            null,
            "input path",
            false,
            LocalDateTime.now().plusDays(1));

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, VIEW_SUBJECT_REPORT)).thenReturn(true);
        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, CREATE_ATTRIBUTE_INSIGHT)).thenReturn(true);
        when(assessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.of(assessmentResult));
        when(loadAttributeInsightPort.load(assessmentResult.getId(), attributeId)).thenReturn(Optional.of(attributeInsight));

        var result = service.getInsight(param);
        assertNotNull(result);
        assertNotNull(result.aiInsight());
        assertEquals(attributeInsight.getAiInsight(), result.aiInsight().insight());
        assertEquals(attributeInsight.getAiInsightTime(), result.aiInsight().creationTime());
        assertTrue(result.aiInsight().isValid());
        assertNull(result.assessorInsight());
        assertTrue(result.editable());
        assertFalse(result.approved());
    }

    @Test
    void testGetAttributeInsight_AssessorInsightIsNull_AiInsightIsValidBasedOnLastModificationTime_ReturnAiInsight() {
        var attributeId = 1L;
        var currentUserId = UUID.randomUUID();
        var param = new GetAttributeInsightUseCase.Param(UUID.randomUUID(), attributeId, currentUserId);
        var assessmentResult = validResult();
        var attributeInsight = new AttributeInsight(assessmentResult.getId(),
            attributeId,
            "ai insight ",
            null,
            LocalDateTime.now().minusDays(1),
            null,
            "input path",
            false,
            LocalDateTime.now().plusDays(1));

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, VIEW_SUBJECT_REPORT)).thenReturn(true);
        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), currentUserId, CREATE_ATTRIBUTE_INSIGHT)).thenReturn(true);
        when(assessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.of(assessmentResult));
        when(loadAttributeInsightPort.load(assessmentResult.getId(), attributeId)).thenReturn(Optional.of(attributeInsight));

        var result = service.getInsight(param);
        assertNotNull(result);
        assertNotNull(result.aiInsight());
        assertEquals(attributeInsight.getAiInsight(), result.aiInsight().insight());
        assertEquals(attributeInsight.getAiInsightTime(), result.aiInsight().creationTime());
        assertTrue(result.aiInsight().isValid());
        assertNull(result.assessorInsight());
        assertTrue(result.editable());
        assertFalse(result.approved());
    }
}
