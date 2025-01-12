package org.flickit.assessment.core.application.port.in.evidence;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_ID_NOT_NULL;
import static org.flickit.assessment.core.common.ErrorMessageKey.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AddEvidenceUseCaseParamTest {

    @Test
    void testAddEvidenceParam_DescriptionIsBlank_ErrorMessage() {
        UUID assessmentId = UUID.randomUUID();
        UUID createdById = UUID.randomUUID();
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> new AddEvidenceUseCase.Param("    ", assessmentId, 1L, "POSITIVE", createdById));
        assertThat(throwable).hasMessage("description: " + ADD_EVIDENCE_DESC_NOT_BLANK);
    }

    @Test
    void testAddEvidenceParam_DescriptionIsLessThanMin_ErrorMessage() {
        UUID assessmentId = UUID.randomUUID();
        UUID createdById = UUID.randomUUID();
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> new AddEvidenceUseCase.Param("ab", assessmentId, 1L, "POSITIVE", createdById));
        assertThat(throwable).hasMessage("description: " + ADD_EVIDENCE_DESC_SIZE_MIN);
    }

    @Test
    void testAddEvidenceParam_DescriptionSizeIsEqualToMin_Success() {
        UUID createdById = UUID.randomUUID();
        assertDoesNotThrow(
            () -> new AddEvidenceUseCase.Param("abc", UUID.randomUUID(), 1L, "POSITIVE", createdById));
    }

    @Test
    void testAddEvidenceParam_DescriptionSizeIsGreaterThanMax_ErrorMessage() {
        var assessmentId = UUID.randomUUID();
        UUID createdById = UUID.randomUUID();
        var desc = randomAlphabetic(501);
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> new AddEvidenceUseCase.Param(desc, assessmentId, 1L, "POSITIVE", createdById));
        assertThat(throwable).hasMessage("description: " + ADD_EVIDENCE_DESC_SIZE_MAX);
    }

    @Test
    void testAddEvidenceParam_DescriptionSizeIsEqualToMax_Success() {
        UUID createdById = UUID.randomUUID();
        assertDoesNotThrow(
            () -> new AddEvidenceUseCase.Param(randomAlphabetic(500), UUID.randomUUID(), 1L, "POSITIVE", createdById));
    }

    @Test
    void testAddEvidenceParam_CreatedByIdIsNull_ErrorMessage() {
        UUID assessmentId = UUID.randomUUID();
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> new AddEvidenceUseCase.Param("desc", assessmentId, 1L, "POSITIVE", null));
        assertThat(throwable).hasMessage("createdById: " + COMMON_CURRENT_USER_ID_NOT_NULL);
    }

    @Test
    void testAddEvidenceParam_AssessmentIdIsNull_ErrorMessage() {
        UUID createdById = UUID.randomUUID();
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> new AddEvidenceUseCase.Param("desc", null, 1L, "POSITIVE", createdById));
        assertThat(throwable).hasMessage("assessmentId: " + ADD_EVIDENCE_ASSESSMENT_ID_NOT_NULL);
    }

    @Test
    void testAddEvidenceParam_QuestionIdIsNull_ErrorMessage() {
        UUID assessmentId = UUID.randomUUID();
        UUID createdById = UUID.randomUUID();
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> new AddEvidenceUseCase.Param("desc", assessmentId, null, "POSITIVE", createdById)
        );
        assertThat(throwable).hasMessage("questionId: " + ADD_EVIDENCE_QUESTION_ID_NOT_NULL);
    }

    @Test
    void testAddEvidenceParam_EvidenceTypeTitleIsNotValid_ErrorMessage() {
        UUID assessmentId = UUID.randomUUID();
        UUID createdById = UUID.randomUUID();
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> new AddEvidenceUseCase.Param("desc", assessmentId, 1L, "notValid", createdById)
        );
        assertThat(throwable).hasMessage("type: " + ADD_EVIDENCE_TYPE_INVALID);
    }

    @Test
    void testAddEvidenceParam_TypeIsNull_NoErrorMessage() {
        UUID assessmentId = UUID.randomUUID();
        UUID createdById = UUID.randomUUID();

        assertDoesNotThrow(() -> new AddEvidenceUseCase.Param("desc", assessmentId, 1L, null, createdById));
    }
}
