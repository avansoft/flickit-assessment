package org.flickit.assessment.core.application.port.in.assessment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.flickit.assessment.common.application.SelfValidating;
import org.flickit.assessment.common.application.domain.notification.HasNotificationCmd;
import org.flickit.assessment.core.application.domain.notification.GrantAccessToReportNotificationCmd;

import java.util.UUID;

import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_ID_NOT_NULL;
import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_EMAIL_FORMAT_NOT_VALID;
import static org.flickit.assessment.core.common.ErrorMessageKey.GRANT_ACCESS_TO_REPORT_ASSESSMENT_ID_NOT_NULL;
import static org.flickit.assessment.core.common.ErrorMessageKey.GRANT_ACCESS_TO_REPORT_EMAIL_NOT_NULL;

public interface GrantAccessToReportUseCase {

    Result grantAccessToReport(Param param);

    @Value
    @EqualsAndHashCode(callSuper = false)
    class Param extends SelfValidating<Param> {

        @NotNull(message = GRANT_ACCESS_TO_REPORT_ASSESSMENT_ID_NOT_NULL)
        UUID assessmentId;

        @NotNull(message = GRANT_ACCESS_TO_REPORT_EMAIL_NOT_NULL)
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = COMMON_EMAIL_FORMAT_NOT_VALID)
        String email;

        @NotNull(message = COMMON_CURRENT_USER_ID_NOT_NULL)
        UUID currentUserId;

        @Builder
        public Param(UUID assessmentId, String email, UUID currentUserId) {
            this.assessmentId = assessmentId;
            this.email = email;
            this.currentUserId = currentUserId;
            this.validateSelf();
        }

    }

    record Result(GrantAccessToReportNotificationCmd notificationCmd) implements HasNotificationCmd {
    }
}
