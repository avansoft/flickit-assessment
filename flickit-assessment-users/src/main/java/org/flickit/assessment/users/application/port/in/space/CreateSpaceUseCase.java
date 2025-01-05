package org.flickit.assessment.users.application.port.in.space;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.flickit.assessment.common.application.SelfValidating;
import org.flickit.assessment.common.validation.EnumValue;
import org.flickit.assessment.common.application.domain.space.SpaceType;

import java.util.UUID;

import static org.flickit.assessment.users.common.ErrorMessageKey.*;
import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_ID_NOT_NULL;

public interface CreateSpaceUseCase {

    Result createSpace(Param param);

    @Value
    @EqualsAndHashCode(callSuper = false)
    class Param extends SelfValidating<Param> {

        @NotBlank(message = CREATE_SPACE_TITLE_NOT_BLANK)
        @Size(min = 3, message = CREATE_SPACE_TITLE_SIZE_MIN)
        @Size(max = 100, message = CREATE_SPACE_TITLE_SIZE_MAX)
        String title;

        @NotNull(message = CREATE_SPACE_TYPE_NOT_NULL)
        @EnumValue(enumClass = SpaceType.class, message = CREATE_SPACE_TYPE_INVALID)
        String type;

        @NotNull(message = COMMON_CURRENT_USER_ID_NOT_NULL)
        UUID currentUserId;

        @Builder
        public Param(String title, String type, UUID currentUserId) {
            this.title = title != null ? title.strip() : null;
            this.type = type != null ? type.strip() : SpaceType.PERSONAL.name(); //TODO: Remove the default
            this.currentUserId = currentUserId;
            this.validateSelf();
        }
    }

    record Result(long id) {
    }
}
