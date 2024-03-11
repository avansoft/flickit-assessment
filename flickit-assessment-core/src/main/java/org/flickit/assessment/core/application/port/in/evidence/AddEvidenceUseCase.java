package org.flickit.assessment.core.application.port.in.evidence;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.flickit.assessment.common.application.SelfValidating;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.common.validation.EnumValue;
import org.flickit.assessment.core.application.domain.EvidenceType;

import java.util.UUID;

import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_ID_NOT_NULL;
import static org.flickit.assessment.core.common.ErrorMessageKey.*;

public interface AddEvidenceUseCase {

    /**
     * @throws ResourceNotFoundException if no assessment found by the given assessmentId
     */
    Result addEvidence(Param param);

    @Value
    @EqualsAndHashCode(callSuper = false)
    class Param extends SelfValidating<Param> {

        @NotBlank(message = ADD_EVIDENCE_DESC_NOT_BLANK)
        @Size(min = 3, message = ADD_EVIDENCE_DESC_SIZE_MIN)
        @Size(max = 1000, message = ADD_EVIDENCE_DESC_SIZE_MAX)
        String description;

        @NotNull(message = ADD_EVIDENCE_ASSESSMENT_ID_NOT_NULL)
        UUID assessmentId;

        @NotNull(message = ADD_EVIDENCE_QUESTION_REF_NUM_NOT_NULL)
        UUID questionRefNum;

        @NotNull(message = COMMON_CURRENT_USER_ID_NOT_NULL)
        UUID createdBy;

        @EnumValue(enumClass = EvidenceType.class, message = ADD_EVIDENCE_TYPE_INVALID)
        String type;

        public Param(String description,
                     UUID assessmentId,
                     UUID questionRefNum,
                     String type,
                     UUID createdBy) {
            this.description = description;
            this.assessmentId = assessmentId;
            this.questionRefNum = questionRefNum;
            this.createdBy = createdBy;
            this.type = type;
            this.validateSelf();
        }
    }

    record Result(UUID id) {
    }
}
