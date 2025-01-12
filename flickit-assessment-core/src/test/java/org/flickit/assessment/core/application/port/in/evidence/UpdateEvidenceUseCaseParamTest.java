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

class UpdateEvidenceUseCaseParamTest {

    @Test
    void testUpdateEvidenceParam_IdIsEmpty_ErrorMessage() {
        UUID lastModifiedById = UUID.randomUUID();
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> new UpdateEvidenceUseCase.Param(null,"new_description", "POSITIVE", lastModifiedById));
        assertThat(throwable).hasMessage("id: " + UPDATE_EVIDENCE_ID_NOT_NULL);
    }

    @Test
    void testUpdateEvidenceParam_TypeIsNotValid_ErrorMessage() {
        var id = UUID.randomUUID();
        UUID lastModifiedById = UUID.randomUUID();
        var throwable = assertThrows(ConstraintViolationException.class,
                () -> new UpdateEvidenceUseCase.Param(id, "new_description", "pos", lastModifiedById));
        assertThat(throwable).hasMessage("type: " + UPDATE_EVIDENCE_TYPE_INVALID);
    }

    @Test
    void testUpdateEvidenceParam_TypeIsNull_NoErrorMessage() {
        var id = UUID.randomUUID();
        UUID lastModifiedById = UUID.randomUUID();

        assertDoesNotThrow(() -> new UpdateEvidenceUseCase.Param(id, "new_description", null, lastModifiedById));
    }

    @Test
    void testUpdateEvidenceParam_DescriptionIsBlank_ErrorMessage() {
        var id = UUID.randomUUID();
        UUID lastModifiedById = UUID.randomUUID();
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> new UpdateEvidenceUseCase.Param(id,"    ", "NEGATIVE", lastModifiedById));
        assertThat(throwable).hasMessage("description: " + UPDATE_EVIDENCE_DESC_NOT_BLANK);
    }

    @Test
    void testUpdateEvidenceParam_DescriptionIsLessThanMin_ErrorMessage() {
        var id = UUID.randomUUID();
        UUID lastModifiedById = UUID.randomUUID();
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> new UpdateEvidenceUseCase.Param(id,"ab", "POSITIVE", lastModifiedById));
        assertThat(throwable).hasMessage("description: " + UPDATE_EVIDENCE_DESC_MIN_SIZE);
    }

    @Test
    void testUpdateEvidenceParam_DescriptionSizeIsEqualToMin_Success() {
        assertDoesNotThrow(() -> new UpdateEvidenceUseCase.Param(UUID.randomUUID(),"abc", "NEGATIVE", UUID.randomUUID()));
    }

    @Test
    void testUpdateEvidenceParam_DescriptionSizeIsGreaterThanMax_ErrorMessage() {
        var id = UUID.randomUUID();
        var desc = randomAlphabetic(501);
        UUID lastModifiedById = UUID.randomUUID();
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> new UpdateEvidenceUseCase.Param(id, desc, "POSITIVE", lastModifiedById));
        assertThat(throwable).hasMessage("description: " + UPDATE_EVIDENCE_DESC_MAX_SIZE);
    }

    @Test
    void testUpdateEvidenceParam_DescriptionSizeIsEqualToMax_Success() {
        assertDoesNotThrow(() -> new UpdateEvidenceUseCase.Param(
            UUID.randomUUID(),
            randomAlphabetic(200),
            "NEGATIVE",
            UUID.randomUUID()
        ));
    }

    @Test
    void testUpdateEvidenceParam_lastModifiedByIdIsEmpty_ErrorMessage() {
        UUID id = UUID.randomUUID();
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> new UpdateEvidenceUseCase.Param(id,"new_description", "POSITIVE", null));
        assertThat(throwable).hasMessage("currentUserId: " + COMMON_CURRENT_USER_ID_NOT_NULL);
    }
}
