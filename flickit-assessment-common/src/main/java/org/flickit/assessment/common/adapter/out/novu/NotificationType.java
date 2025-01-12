package org.flickit.assessment.common.adapter.out.novu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

    GRANT_USER_ASSESSMENT_ROLE,
    GRANT_ACCESS_TO_REPORT,
    CREATE_ASSESSMENT,
    COMPLETE_ASSESSMENT,
    ACCEPT_ASSESSMENT_INVITATION;

    final String code;

    NotificationType() {
        this.code = name().toLowerCase().replace("_", "");
    }
}
