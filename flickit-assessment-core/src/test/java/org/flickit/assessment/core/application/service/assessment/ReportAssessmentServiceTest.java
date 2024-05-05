package org.flickit.assessment.core.application.service.assessment;

import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.core.application.domain.AssessmentColor;
import org.flickit.assessment.core.application.domain.MaturityLevel;
import org.flickit.assessment.core.application.domain.report.AssessmentReport;
import org.flickit.assessment.core.application.domain.report.AssessmentReport.AssessmentReportItem;
import org.flickit.assessment.core.application.domain.report.AssessmentReport.AssessmentReportItem.AssessmentKitItem;
import org.flickit.assessment.core.application.domain.report.AssessmentReport.AssessmentReportItem.AssessmentKitItem.ExpertGroup;
import org.flickit.assessment.core.application.domain.report.AssessmentReport.AttributeReportItem;
import org.flickit.assessment.core.application.domain.report.AssessmentReport.SubjectReportItem;
import org.flickit.assessment.core.application.internal.ValidateAssessmentResult;
import org.flickit.assessment.core.application.port.in.assessment.ReportAssessmentUseCase;
import org.flickit.assessment.core.application.port.out.assessment.CheckAssessmentExistencePort;
import org.flickit.assessment.core.application.port.out.assessment.CheckUserAssessmentAccessPort;
import org.flickit.assessment.core.application.port.out.assessmentresult.LoadAssessmentReportInfoPort;
import org.flickit.assessment.core.test.fixture.application.MaturityLevelMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportAssessmentServiceTest {

    @InjectMocks
    private ReportAssessmentService service;

    @Mock
    private ValidateAssessmentResult validateAssessmentResult;

    @Mock
    private LoadAssessmentReportInfoPort loadReportInfoPort;

    @Mock
    private CheckAssessmentExistencePort checkAssessmentExistencePort;

    @Mock
    private CheckUserAssessmentAccessPort checkUserAssessmentAccessPort;

    @Test
    void testReportAssessment_ValidResult() {
        UUID currentUserId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();

        ReportAssessmentUseCase.Param param = new ReportAssessmentUseCase.Param(assessmentId, currentUserId);

        List<AttributeReportItem> attributes = List.of(new AttributeReportItem(1L, "attrTitle1", 1),
            new AttributeReportItem(2L, "attrTitle2", 2),
            new AttributeReportItem(3L, "attrTitle3", 3));
        ExpertGroup expertGroup = new ExpertGroup(1L, "expertGroupTitle1");
        AssessmentKitItem kit = new AssessmentKitItem(1L, "kitTitle", "kitSummary", 3, expertGroup);
        MaturityLevel assessmentMaturityLevel = MaturityLevelMother.levelThree();
        LocalDateTime lastModificationTime = LocalDateTime.now();
        AssessmentReportItem assessment = new AssessmentReportItem(assessmentId,
            "assessmentTitle",
            kit,
            assessmentMaturityLevel,
            1.5,
            true,
            true,
            AssessmentColor.BLUE,
            lastModificationTime);

        List<MaturityLevel> maturityLevels = MaturityLevelMother.allLevels();
        MaturityLevel softwareLevel = MaturityLevelMother.levelFour();
        MaturityLevel teamLevel = MaturityLevelMother.levelTwo();
        List<SubjectReportItem> subjects = List.of(new SubjectReportItem(1L, "software", 1, "subjectDesc1", softwareLevel),
            new SubjectReportItem(2L, "team", 2, "subjectDesc2", teamLevel));
        AssessmentReport assessmentReport = new AssessmentReport(assessment, attributes, maturityLevels, subjects);

        when(checkAssessmentExistencePort.existsById(param.getAssessmentId())).thenReturn(true);
        when(checkUserAssessmentAccessPort.hasAccess(assessmentId, currentUserId)).thenReturn(true);
        doNothing().when(validateAssessmentResult).validate(param.getAssessmentId());
        when(loadReportInfoPort.load(assessmentId)).thenReturn(assessmentReport);

        ReportAssessmentUseCase.Result result = service.reportAssessment(param);

        assertNotNull(assessmentReport);
        assertNotNull(assessmentReport.assessment());
        assertEquals(assessmentReport.assessment().id(), result.assessment().id());
        assertEquals(assessmentReport.assessment().title(), result.assessment().title());
        assertEquals(assessmentReport.assessment().confidenceValue(), result.assessment().confidenceValue());
        assertEquals(assessmentReport.assessment().isCalculateValid(), result.assessment().isCalculateValid());
        assertEquals(assessmentReport.assessment().isConfidenceValid(), result.assessment().isConfidenceValid());
        assertEquals(assessmentReport.assessment().lastModificationTime(), result.assessment().lastModificationTime());
        assertEquals(assessmentReport.assessment().color(), result.assessment().color());
        assertEquals(assessmentReport.assessment().maturityLevel().getId(), result.assessment().maturityLevel().getId());
        assertEquals(assessmentReport.assessment().maturityLevel().getIndex(), result.assessment().maturityLevel().getIndex());
        assertEquals(assessmentReport.assessment().maturityLevel().getTitle(), result.assessment().maturityLevel().getTitle());
        assertEquals(assessmentReport.assessment().maturityLevel().getValue(), result.assessment().maturityLevel().getValue());
        assertEquals(assessmentReport.assessment().assessmentKit().id(), result.assessment().assessmentKit().id());
        assertEquals(assessmentReport.assessment().assessmentKit().title(), result.assessment().assessmentKit().title());
        assertEquals(assessmentReport.assessment().assessmentKit().summary(), result.assessment().assessmentKit().summary());
        assertEquals(assessmentReport.assessment().assessmentKit().maturityLevelCount(), result.assessment().assessmentKit().maturityLevelCount());
        assertEquals(assessmentReport.assessment().assessmentKit().expertGroup().id(), result.assessment().assessmentKit().expertGroup().id());
        assertEquals(assessmentReport.assessment().assessmentKit().expertGroup().title(), result.assessment().assessmentKit().expertGroup().title());

        assertNotNull(result.topStrengths());
        assertEquals(1, result.topStrengths().size());
        assertNotNull(result.topWeaknesses());
        assertEquals(3, result.topWeaknesses().size());

        assertEquals(assessmentReport.subjects().size(), result.subjects().size());
    }

    @Test
    void testReportAssessment_AssessmentDoesNotExist_ThrowException() {
        UUID currentUserId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();
        ReportAssessmentUseCase.Param param = new ReportAssessmentUseCase.Param(assessmentId, currentUserId);

        when(checkAssessmentExistencePort.existsById(param.getAssessmentId())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.reportAssessment(param));
    }

    @Test
    void testReportAssessment_CurrentUserHasNotAccess_ThrowException() {
        UUID currentUserId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();
        ReportAssessmentUseCase.Param param = new ReportAssessmentUseCase.Param(assessmentId, currentUserId);

        when(checkAssessmentExistencePort.existsById(param.getAssessmentId())).thenReturn(true);
        when(checkUserAssessmentAccessPort.hasAccess(assessmentId, currentUserId)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> service.reportAssessment(param));
    }
}
