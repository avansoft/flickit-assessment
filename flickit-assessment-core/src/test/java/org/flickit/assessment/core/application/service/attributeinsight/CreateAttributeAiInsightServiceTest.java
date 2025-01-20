package org.flickit.assessment.core.application.service.attributeinsight;

import org.flickit.assessment.common.application.domain.assessment.AssessmentAccessChecker;
import org.flickit.assessment.common.application.port.out.CallAiPromptPort;
import org.flickit.assessment.common.application.port.out.ValidateAssessmentResultPort;
import org.flickit.assessment.common.config.AppAiProperties;
import org.flickit.assessment.common.config.OpenAiProperties;
import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.common.exception.CalculateNotValidException;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.common.exception.ValidationException;
import org.flickit.assessment.core.application.domain.AssessmentResult;
import org.flickit.assessment.core.application.domain.Attribute;
import org.flickit.assessment.core.application.domain.AttributeValue;
import org.flickit.assessment.core.application.domain.MaturityLevel;
import org.flickit.assessment.core.application.port.in.attributeinsight.CreateAttributeAiInsightUseCase;
import org.flickit.assessment.core.application.port.in.attributeinsight.CreateAttributeAiInsightUseCase.Param;
import org.flickit.assessment.core.application.port.out.assessment.GetAssessmentProgressPort;
import org.flickit.assessment.core.application.port.out.assessmentresult.LoadAssessmentResultPort;
import org.flickit.assessment.core.application.port.out.attribute.CreateAttributeScoresFilePort;
import org.flickit.assessment.core.application.port.out.attribute.LoadAttributePort;
import org.flickit.assessment.core.application.port.out.attributeinsight.CreateAttributeInsightPort;
import org.flickit.assessment.core.application.port.out.attributeinsight.LoadAttributeInsightPort;
import org.flickit.assessment.core.application.port.out.attributeinsight.UpdateAttributeInsightPort;
import org.flickit.assessment.core.application.port.out.attributevalue.LoadAttributeValuePort;
import org.flickit.assessment.core.application.port.out.maturitylevel.LoadMaturityLevelsPort;
import org.flickit.assessment.core.application.port.out.minio.UploadAttributeScoresFilePort;
import org.flickit.assessment.core.test.fixture.application.AttributeValueMother;
import org.flickit.assessment.core.test.fixture.application.MaturityLevelMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.prompt.Prompt;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static org.flickit.assessment.common.application.domain.assessment.AssessmentPermission.CREATE_ATTRIBUTE_INSIGHT;
import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_ASSESSMENT_RESULT_NOT_VALID;
import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.flickit.assessment.core.common.ErrorMessageKey.CREATE_ATTRIBUTE_AI_INSIGHT_ALL_QUESTIONS_NOT_ANSWERED;
import static org.flickit.assessment.core.common.ErrorMessageKey.CREATE_ATTRIBUTE_AI_INSIGHT_ASSESSMENT_RESULT_NOT_FOUND;
import static org.flickit.assessment.core.common.MessageKey.ASSESSMENT_AI_IS_DISABLED;
import static org.flickit.assessment.core.test.fixture.application.AssessmentResultMother.invalidResultWithSubjectValues;
import static org.flickit.assessment.core.test.fixture.application.AssessmentResultMother.validResult;
import static org.flickit.assessment.core.test.fixture.application.AttributeInsightMother.simpleAttributeAiInsight;
import static org.flickit.assessment.core.test.fixture.application.AttributeInsightMother.simpleAttributeAiInsightMinInsightTime;
import static org.flickit.assessment.core.test.fixture.application.AttributeMother.simpleAttribute;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAttributeAiInsightServiceTest {

    @InjectMocks
    private CreateAttributeAiInsightService service;

    @Mock
    private AppAiProperties appAiProperties;

    @Mock
    private ValidateAssessmentResultPort validateAssessmentResultPort;

    @Mock
    private LoadAttributeValuePort loadAttributeValuePort;

    @Mock
    private LoadMaturityLevelsPort loadMaturityLevelsPort;

    @Mock
    private AssessmentAccessChecker assessmentAccessChecker;

    @Mock
    private LoadAssessmentResultPort loadAssessmentResultPort;

    @Mock
    private OpenAiProperties openAiProperties;

    @Mock
    private CallAiPromptPort callAiPromptPort;

    @Mock
    private UpdateAttributeInsightPort updateAttributeInsightPort;

    @Mock
    private LoadAttributePort loadAttributePort;

    @Mock
    private LoadAttributeInsightPort loadAttributeInsightPort;

    @Mock
    private CreateAttributeInsightPort createAttributeInsightPort;

    @Mock
    CreateAttributeScoresFilePort generateAttributeValueReportFilePort;

    @Mock
    private UploadAttributeScoresFilePort uploadAttributeScoresFilePort;

    @Mock
    private GetAssessmentProgressPort getAssessmentProgressPort;

    private final Attribute attribute = simpleAttribute();
    private final AssessmentResult assessmentResult = validResult();

    @Test
    void testCreateAttributeAiInsight_whenCurrentUserDoesNotHaveRequiredPermission_thenThrowAccessDeniedException() {
        var param = createParam(CreateAttributeAiInsightUseCase.Param.ParamBuilder::build);

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), param.getCurrentUserId(), CREATE_ATTRIBUTE_INSIGHT)).thenReturn(false);

        var throwable = assertThrows(AccessDeniedException.class, () -> service.createAiInsight(param));
        assertEquals(COMMON_CURRENT_USER_NOT_ALLOWED, throwable.getMessage());

        verifyNoInteractions(loadAttributePort,
            loadAssessmentResultPort,
            loadAttributeInsightPort,
            callAiPromptPort,
            updateAttributeInsightPort,
            loadAttributeValuePort,
            loadMaturityLevelsPort,
            generateAttributeValueReportFilePort,
            uploadAttributeScoresFilePort);
    }

    @Test
    void testCreateAttributeAiInsight_whenAssessmentProgressIsNotCompleted_thenThrowValidationException() {
        var param = createParam(CreateAttributeAiInsightUseCase.Param.ParamBuilder::build);
        var progress = new GetAssessmentProgressPort.Result(param.getAssessmentId(), 10, 11);

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), param.getCurrentUserId(), CREATE_ATTRIBUTE_INSIGHT)).thenReturn(true);
        when(getAssessmentProgressPort.getProgress(param.getAssessmentId())).thenReturn(progress);

        var throwable = assertThrows(ValidationException.class, () -> service.createAiInsight(param));
        assertEquals(CREATE_ATTRIBUTE_AI_INSIGHT_ALL_QUESTIONS_NOT_ANSWERED, throwable.getMessageKey());

        verifyNoInteractions(loadAttributePort,
            loadAssessmentResultPort,
            loadAttributeInsightPort,
            callAiPromptPort,
            updateAttributeInsightPort,
            loadAttributeValuePort,
            loadMaturityLevelsPort,
            generateAttributeValueReportFilePort,
            uploadAttributeScoresFilePort);
    }

    @Test
    void testCreateAttributeAiInsight_whenAssessmentResultIsNotFound_thenThrowResourceNotFoundException() {
        var param = createParam(CreateAttributeAiInsightUseCase.Param.ParamBuilder::build);
        var progress = new GetAssessmentProgressPort.Result(param.getAssessmentId(), 10, 10);

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), param.getCurrentUserId(), CREATE_ATTRIBUTE_INSIGHT)).thenReturn(true);
        when(getAssessmentProgressPort.getProgress(param.getAssessmentId())).thenReturn(progress);
        when(loadAssessmentResultPort.loadByAssessmentId(param.getAssessmentId()))
            .thenThrow(new ResourceNotFoundException(CREATE_ATTRIBUTE_AI_INSIGHT_ASSESSMENT_RESULT_NOT_FOUND));

        var throwable = assertThrows(ResourceNotFoundException.class, () -> service.createAiInsight(param));
        assertEquals(CREATE_ATTRIBUTE_AI_INSIGHT_ASSESSMENT_RESULT_NOT_FOUND, throwable.getMessage());

        verifyNoInteractions(loadAttributeInsightPort,
            callAiPromptPort,
            updateAttributeInsightPort,
            generateAttributeValueReportFilePort,
            uploadAttributeScoresFilePort);
    }

    @Test
    void testCreateAttributeAiInsight_whenCalculatedResultIsNotValid_thenThrowCalculateNotValidException() {
        var invalidResult = invalidResultWithSubjectValues(null);
        var param = createParam(CreateAttributeAiInsightUseCase.Param.ParamBuilder::build);
        var progress = new GetAssessmentProgressPort.Result(param.getAssessmentId(), 10, 10);

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), param.getCurrentUserId(), CREATE_ATTRIBUTE_INSIGHT)).thenReturn(true);
        when(getAssessmentProgressPort.getProgress(param.getAssessmentId())).thenReturn(progress);
        when(loadAssessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.of(invalidResult));
        doThrow(new CalculateNotValidException(COMMON_ASSESSMENT_RESULT_NOT_VALID)).when(validateAssessmentResultPort).validate(param.getAssessmentId());

        var throwable = assertThrows(CalculateNotValidException.class, () -> service.createAiInsight(param));
        assertEquals(COMMON_ASSESSMENT_RESULT_NOT_VALID, throwable.getMessage());

        verifyNoInteractions(loadAttributeInsightPort,
            callAiPromptPort,
            updateAttributeInsightPort,
            generateAttributeValueReportFilePort,
            uploadAttributeScoresFilePort);
    }

    @Test
    void testCreateAttributeAiInsight_whenAiInsightDoesNotExistAndAiEnabled_thenGenerateAndPersistAiInsight() {
        var param = createParam(CreateAttributeAiInsightUseCase.Param.ParamBuilder::build);
        var progress = new GetAssessmentProgressPort.Result(param.getAssessmentId(), 10, 10);

        String fileContent = "file content";
        var file = new CreateAttributeScoresFilePort.Result(new ByteArrayInputStream(fileContent.getBytes()), fileContent);
        AttributeValue attributeValue = AttributeValueMother.hasFullScoreOnLevel23WithWeight(1, attribute.getId());
        var fileReportPath = "path/to/file";
        List<MaturityLevel> maturityLevels = MaturityLevelMother.allLevels();
        var aiReport = "Report Content";
        var prompt = new Prompt("AI prompt");

        when(appAiProperties.isEnabled()).thenReturn(true);
        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), param.getCurrentUserId(), CREATE_ATTRIBUTE_INSIGHT)).thenReturn(true);
        when(getAssessmentProgressPort.getProgress(param.getAssessmentId())).thenReturn(progress);
        doNothing().when(validateAssessmentResultPort).validate(param.getAssessmentId());
        when(loadAssessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.of(assessmentResult));
        when(loadAttributePort.load(attribute.getId(), assessmentResult.getKitVersionId())).thenReturn(attribute);
        when(loadAttributeInsightPort.load(assessmentResult.getId(), param.getAttributeId())).thenReturn(Optional.empty());
        when(openAiProperties.createAttributeAiInsightPrompt(attribute.getTitle(), attribute.getDescription(), assessmentResult.getAssessment().getShortTitle(), file.text())).thenReturn(prompt);
        when(callAiPromptPort.call(prompt)).thenReturn(aiReport);
        when(loadAttributeValuePort.load(assessmentResult.getId(), param.getAttributeId())).thenReturn(attributeValue);
        when(loadMaturityLevelsPort.loadByKitVersionId(assessmentResult.getKitVersionId())).thenReturn(maturityLevels);
        when(generateAttributeValueReportFilePort.generateFile(attributeValue, maturityLevels))
            .thenReturn(file);
        when(appAiProperties.isSaveAiInputFileEnabled()).thenReturn(true);
        when(uploadAttributeScoresFilePort.uploadExcel(eq(file.stream()), any())).thenReturn(fileReportPath);
        doNothing().when(createAttributeInsightPort).persist(any());

        var result = service.createAiInsight(param);
        assertEquals("Report Content", result.content());

        verifyNoInteractions(updateAttributeInsightPort);
    }

    @Test
    void testCreateAttributeAiInsight_whenAiInsightDoesNotExistAndAiEnabledAndSaveFilesDisabled_thenGenerateAndNotSaveFileAndPersistInsight() {
        var param = createParam(CreateAttributeAiInsightUseCase.Param.ParamBuilder::build);
        var progress = new GetAssessmentProgressPort.Result(param.getAssessmentId(), 10, 10);

        String fileContent = "file content";
        var file = new CreateAttributeScoresFilePort.Result(new ByteArrayInputStream(fileContent.getBytes()), fileContent);
        AttributeValue attributeValue = AttributeValueMother.hasFullScoreOnLevel23WithWeight(1, attribute.getId());
        List<MaturityLevel> maturityLevels = MaturityLevelMother.allLevels();
        var aiReport = "Report Content";
        var prompt = new Prompt("AI prompt");

        when(appAiProperties.isEnabled()).thenReturn(true);
        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), param.getCurrentUserId(), CREATE_ATTRIBUTE_INSIGHT)).thenReturn(true);
        when(getAssessmentProgressPort.getProgress(param.getAssessmentId())).thenReturn(progress);
        doNothing().when(validateAssessmentResultPort).validate(param.getAssessmentId());
        when(loadAssessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.of(assessmentResult));
        when(loadAttributePort.load(param.getAttributeId(), assessmentResult.getKitVersionId())).thenReturn(attribute);
        when(loadAttributeInsightPort.load(assessmentResult.getId(), param.getAttributeId())).thenReturn(Optional.empty());
        when(openAiProperties.createAttributeAiInsightPrompt(attribute.getTitle(), attribute.getDescription(), assessmentResult.getAssessment().getShortTitle(), file.text())).thenReturn(prompt);
        when(callAiPromptPort.call(prompt)).thenReturn(aiReport);
        when(loadAttributeValuePort.load(assessmentResult.getId(), param.getAttributeId())).thenReturn(attributeValue);
        when(loadMaturityLevelsPort.loadByKitVersionId(assessmentResult.getKitVersionId())).thenReturn(maturityLevels);
        when(generateAttributeValueReportFilePort.generateFile(attributeValue, maturityLevels))
            .thenReturn(file);
        when(appAiProperties.isSaveAiInputFileEnabled()).thenReturn(false);
        doNothing().when(createAttributeInsightPort).persist(any());

        var result = service.createAiInsight(param);
        assertEquals("Report Content", result.content());

        verifyNoInteractions(updateAttributeInsightPort, uploadAttributeScoresFilePort);
    }

    @Test
    void testCreateAttributeAiInsight_whenAiInsightDoesNotExistAndAiDisabled_thenThrowUnsupportedOperationException() {
        var param = createParam(CreateAttributeAiInsightUseCase.Param.ParamBuilder::build);
        var progress = new GetAssessmentProgressPort.Result(param.getAssessmentId(), 10, 10);

        when(appAiProperties.isEnabled()).thenReturn(false);
        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), param.getCurrentUserId(), CREATE_ATTRIBUTE_INSIGHT)).thenReturn(true);
        when(getAssessmentProgressPort.getProgress(param.getAssessmentId())).thenReturn(progress);
        when(loadAssessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.of(assessmentResult));
        when(loadAttributePort.load(attribute.getId(), assessmentResult.getKitVersionId())).thenReturn(attribute);
        when(loadAttributeInsightPort.load(assessmentResult.getId(), param.getAttributeId())).thenReturn(Optional.empty());

        var throwable = assertThrows(UnsupportedOperationException.class, () -> service.createAiInsight(param));
        assertEquals(ASSESSMENT_AI_IS_DISABLED, throwable.getMessage());

        verifyNoInteractions(updateAttributeInsightPort,
            callAiPromptPort,
            createAttributeInsightPort,
            generateAttributeValueReportFilePort,
            uploadAttributeScoresFilePort,
            loadAttributeValuePort,
            loadMaturityLevelsPort);
    }

    @Test
    void testCreateAttributeAiInsight_whenAiInsightExistsAndInsightTimeIsAfterCalculationTime_thenReturnExistingInsight() {
        var param = createParam(CreateAttributeAiInsightUseCase.Param.ParamBuilder::build);

        var attributeInsight = simpleAttributeAiInsight();
        var progress = new GetAssessmentProgressPort.Result(param.getAssessmentId(), 10, 10);

        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), param.getCurrentUserId(), CREATE_ATTRIBUTE_INSIGHT)).thenReturn(true);
        when(getAssessmentProgressPort.getProgress(param.getAssessmentId())).thenReturn(progress);
        when(loadAssessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.of(assessmentResult));
        when(loadAttributePort.load(attribute.getId(), assessmentResult.getKitVersionId())).thenReturn(attribute);
        when(loadAttributeInsightPort.load(assessmentResult.getId(), param.getAttributeId()))
            .thenReturn(Optional.of(attributeInsight));

        var result = service.createAiInsight(param);
        assertEquals(result.content(), attributeInsight.getAiInsight());

        verifyNoInteractions(callAiPromptPort,
            updateAttributeInsightPort,
            generateAttributeValueReportFilePort,
            uploadAttributeScoresFilePort);
    }

    @Test
    void testCreateAttributeAiInsight_whenAiInsightExistsAndInsightTimeIsBeforeCalculationTime_AiEnabled_thenRegenerateAndUpdateInsight() {
        var param = createParam(CreateAttributeAiInsightUseCase.Param.ParamBuilder::build);
        var progress = new GetAssessmentProgressPort.Result(param.getAssessmentId(), 10, 10);

        var attributeInsight = simpleAttributeAiInsightMinInsightTime();
        String fileContent = "file content";
        var file = new CreateAttributeScoresFilePort.Result(new ByteArrayInputStream(fileContent.getBytes()), fileContent);
        var fileReportPath = "path/to/file";
        AttributeValue attributeValue = AttributeValueMother.hasFullScoreOnLevel23WithWeight(1, attribute.getId());
        List<MaturityLevel> maturityLevels = MaturityLevelMother.allLevels();
        var prompt = new Prompt("AI prompt");

        when(appAiProperties.isEnabled()).thenReturn(true);
        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), param.getCurrentUserId(), CREATE_ATTRIBUTE_INSIGHT)).thenReturn(true);
        when(getAssessmentProgressPort.getProgress(param.getAssessmentId())).thenReturn(progress);
        when(loadAssessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.of(assessmentResult));
        when(loadAttributePort.load(attribute.getId(), assessmentResult.getKitVersionId())).thenReturn(attribute);
        when(loadAttributeInsightPort.load(assessmentResult.getId(), param.getAttributeId())).thenReturn(Optional.of(attributeInsight));
        when(openAiProperties.createAttributeAiInsightPrompt(attribute.getTitle(), attribute.getDescription(), assessmentResult.getAssessment().getShortTitle(), file.text())).thenReturn(prompt);
        when(callAiPromptPort.call(prompt)).thenReturn(attributeInsight.getAiInsight());
        when(loadAttributeValuePort.load(assessmentResult.getId(), param.getAttributeId())).thenReturn(attributeValue);
        when(loadMaturityLevelsPort.loadByKitVersionId(assessmentResult.getKitVersionId())).thenReturn(maturityLevels);
        when(generateAttributeValueReportFilePort.generateFile(attributeValue, maturityLevels))
            .thenReturn(file);
        when(appAiProperties.isSaveAiInputFileEnabled()).thenReturn(true);
        when(uploadAttributeScoresFilePort.uploadExcel(eq(file.stream()), any())).thenReturn(fileReportPath);
        doNothing().when(updateAttributeInsightPort).updateAiInsight(any());

        var result = service.createAiInsight(param);
        assertEquals(attributeInsight.getAiInsight(), result.content());

        ArgumentCaptor<UpdateAttributeInsightPort.AiParam> captor = ArgumentCaptor.forClass(UpdateAttributeInsightPort.AiParam.class);
        verify(updateAttributeInsightPort).updateAiInsight(captor.capture());
        assertEquals(assessmentResult.getId(), captor.getValue().assessmentResultId());
        assertEquals(param.getAttributeId(), captor.getValue().attributeId());
        assertEquals(attributeInsight.getAiInsight(), captor.getValue().aiInsight());
        assertNotNull(captor.getValue().aiInsightTime());
        assertFalse(captor.getValue().isApproved());
        assertNotNull(captor.getValue().lastModificationTime());

        verifyNoInteractions(createAttributeInsightPort);
    }

    @Test
    void testCreateAttributeAiInsight_whenAiInsightExistsAndInsightTimeIsBeforeCalculationTime_AiEnabledSaveFilesDisabled_thenRegenerateAndNotSaveFileAndUpdateInsight() {
        var param = createParam(CreateAttributeAiInsightUseCase.Param.ParamBuilder::build);
        var progress = new GetAssessmentProgressPort.Result(param.getAssessmentId(), 10, 10);

        var attributeInsight = simpleAttributeAiInsightMinInsightTime();
        String fileContent = "file content";
        var file = new CreateAttributeScoresFilePort.Result(new ByteArrayInputStream(fileContent.getBytes()), fileContent);
        AttributeValue attributeValue = AttributeValueMother.hasFullScoreOnLevel23WithWeight(1, attribute.getId());
        List<MaturityLevel> maturityLevels = MaturityLevelMother.allLevels();
        var prompt = new Prompt("AI prompt");

        when(appAiProperties.isEnabled()).thenReturn(true);
        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), param.getCurrentUserId(), CREATE_ATTRIBUTE_INSIGHT)).thenReturn(true);
        when(getAssessmentProgressPort.getProgress(param.getAssessmentId())).thenReturn(progress);
        when(loadAssessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.of(assessmentResult));
        when(loadAttributePort.load(attribute.getId(), assessmentResult.getKitVersionId())).thenReturn(attribute);
        when(loadAttributeInsightPort.load(assessmentResult.getId(), param.getAttributeId())).thenReturn(Optional.of(attributeInsight));
        when(openAiProperties.createAttributeAiInsightPrompt(attribute.getTitle(), attribute.getDescription(), assessmentResult.getAssessment().getShortTitle(), file.text())).thenReturn(prompt);
        when(callAiPromptPort.call(prompt)).thenReturn(attributeInsight.getAiInsight());
        when(loadAttributeValuePort.load(assessmentResult.getId(), param.getAttributeId())).thenReturn(attributeValue);
        when(loadMaturityLevelsPort.loadByKitVersionId(assessmentResult.getKitVersionId())).thenReturn(maturityLevels);
        when(generateAttributeValueReportFilePort.generateFile(attributeValue, maturityLevels))
            .thenReturn(file);
        when(appAiProperties.isSaveAiInputFileEnabled()).thenReturn(false);
        doNothing().when(updateAttributeInsightPort).updateAiInsight(any());

        var result = service.createAiInsight(param);
        assertEquals(attributeInsight.getAiInsight(), result.content());

        ArgumentCaptor<UpdateAttributeInsightPort.AiParam> captor = ArgumentCaptor.forClass(UpdateAttributeInsightPort.AiParam.class);
        verify(updateAttributeInsightPort).updateAiInsight(captor.capture());
        assertEquals(assessmentResult.getId(), captor.getValue().assessmentResultId());
        assertEquals(param.getAttributeId(), captor.getValue().attributeId());
        assertEquals(attributeInsight.getAiInsight(), captor.getValue().aiInsight());
        assertNotNull(captor.getValue().aiInsightTime());
        assertFalse(captor.getValue().isApproved());
        assertNotNull(captor.getValue().lastModificationTime());

        verifyNoInteractions(createAttributeInsightPort, uploadAttributeScoresFilePort);
    }

    @Test
    void testCreateAttributeAiInsight_whenAiInsightExistsAndInsightTimeIsBeforeCalculationTime_AiDisabled_thenThrowUnsupportedOperationException() {
        var param = createParam(CreateAttributeAiInsightUseCase.Param.ParamBuilder::build);
        var attributeInsight = simpleAttributeAiInsightMinInsightTime();
        var progress = new GetAssessmentProgressPort.Result(param.getAssessmentId(), 10, 10);

        when(appAiProperties.isEnabled()).thenReturn(false);
        when(assessmentAccessChecker.isAuthorized(param.getAssessmentId(), param.getCurrentUserId(), CREATE_ATTRIBUTE_INSIGHT)).thenReturn(true);
        when(getAssessmentProgressPort.getProgress(param.getAssessmentId())).thenReturn(progress);
        when(loadAssessmentResultPort.loadByAssessmentId(param.getAssessmentId())).thenReturn(Optional.of(assessmentResult));
        when(loadAttributePort.load(attribute.getId(), assessmentResult.getKitVersionId())).thenReturn(attribute);
        when(loadAttributeInsightPort.load(assessmentResult.getId(), param.getAttributeId())).thenReturn(Optional.of(attributeInsight));

        var throwable = assertThrows(UnsupportedOperationException.class, () -> service.createAiInsight(param));
        assertEquals(ASSESSMENT_AI_IS_DISABLED, throwable.getMessage());

        verifyNoInteractions(callAiPromptPort,
            createAttributeInsightPort,
            updateAttributeInsightPort,
            generateAttributeValueReportFilePort,
            uploadAttributeScoresFilePort);
    }

    private CreateAttributeAiInsightUseCase.Param createParam(Consumer<Param.ParamBuilder> changer) {
        var paramBuilder = paramBuilder();
        changer.accept(paramBuilder);
        return paramBuilder.build();
    }

    private CreateAttributeAiInsightUseCase.Param.ParamBuilder paramBuilder() {
        return CreateAttributeAiInsightUseCase.Param.builder()
            .assessmentId(assessmentResult.getAssessment().getId())
            .attributeId(attribute.getId())
            .currentUserId(UUID.randomUUID());
    }
}
