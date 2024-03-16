package org.flickit.assessment.core.application.service.subject;

import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.core.application.port.in.subject.GetSubjectAttributesUseCase;
import org.flickit.assessment.core.application.port.in.subject.GetSubjectAttributesUseCase.Param;
import org.flickit.assessment.core.application.port.out.assessment.CheckUserAssessmentAccessPort;
import org.flickit.assessment.core.application.port.out.subject.CheckSubjectKitExistencePort;
import org.flickit.assessment.core.application.port.out.subject.LoadSubjectAttributesPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetSubjectAttributesServiceTest {

    @InjectMocks
    private GetSubjectAttributesService getSubjectAttributesService;

    @Mock
    private CheckUserAssessmentAccessPort checkUserAssessmentAccessPort;

    @Mock
    private CheckSubjectKitExistencePort checkSubjectKitExistencePort;

    @Mock
    private LoadSubjectAttributesPort loadSubjectAttributesPort;

    @Test
    void testGetSubjectAttributes_validParams_ItemsReturned() {
        UUID assessmentId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();
        Param param = new Param(assessmentId, 1L, currentUserId);
        var subjectAttributes = List.of(new GetSubjectAttributesUseCase.SubjectAttribute(1L, 2L, 3));

        when(checkUserAssessmentAccessPort.hasAccess(assessmentId, currentUserId)).thenReturn(true);
        when(checkSubjectKitExistencePort.existsByIdAndAssessmentId(param.getSubjectId(), assessmentId)).thenReturn(true);
        when(loadSubjectAttributesPort.loadBySubjectIdAndAssessmentId(param.getSubjectId(), assessmentId)).thenReturn(subjectAttributes);

        var result = getSubjectAttributesService.getSubjectAttributes(param);
        assertEquals(1L, result.subjectAttributes().get(0).id());
        assertEquals(2L, result.subjectAttributes().get(0).maturityLevelId());
        assertEquals(3, result.subjectAttributes().get(0).weight());
    }

    @Test
    void testGetSubjectAttributes_InvalidAssessmentId_ThrowsAccessDeniedException() {
        UUID assessmentId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();
        Param param = new Param(assessmentId, 1L, currentUserId);

        when(checkUserAssessmentAccessPort.hasAccess(assessmentId, currentUserId)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> getSubjectAttributesService.getSubjectAttributes(param));
    }

    @Test
    void testGetSubjectAttributes_InvalidSubjectId_ThrowsResourceNotFoundException() {
        UUID assessmentId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();
        Param param = new Param(assessmentId, 1L, currentUserId);

        when(checkUserAssessmentAccessPort.hasAccess(assessmentId, currentUserId)).thenReturn(true);
        when(checkSubjectKitExistencePort.existsByIdAndAssessmentId(param.getSubjectId(), assessmentId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> getSubjectAttributesService.getSubjectAttributes(param));
    }
}
