package org.flickit.assessment.core.application.port.in.evidence;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
        @Size(max = 500, message = ADD_EVIDENCE_DESC_SIZE_MAX)
        String description;

        @NotNull(message = ADD_EVIDENCE_ASSESSMENT_ID_NOT_NULL)
        UUID assessmentId;

        @NotNull(message = ADD_EVIDENCE_QUESTION_ID_NOT_NULL)
        Long questionId;

        @NotNull(message = COMMON_CURRENT_USER_ID_NOT_NULL)
        UUID createdById;

        @EnumValue(enumClass = EvidenceType.class, message = ADD_EVIDENCE_TYPE_INVALID)
        String type;

        public Param(String description,
                     UUID assessmentId,
                     Long questionId,
                     String type,
                     UUID createdById) {
            this.description = description;
            this.assessmentId = assessmentId;
            this.questionId = questionId;
            this.createdById = createdById;
            this.type = type;
            this.validateSelf();
        }
    }

    record Result(UUID id) {
    }
}
