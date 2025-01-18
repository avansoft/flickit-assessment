package org.flickit.assessment.kit.application.port.in.assessmentkit;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.flickit.assessment.common.application.SelfValidating;
import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;

import java.util.UUID;

import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_ID_NOT_NULL;
import static org.flickit.assessment.kit.common.ErrorMessageKey.*;

public interface SearchKitOptionsUseCase {

    PaginatedResponse<KitListItem> searchKitOptions(Param param);

    @Value
    @EqualsAndHashCode(callSuper = false)
    class Param extends SelfValidating<Param> {

        String query;

        @Min(value = 0, message = SEARCH_KIT_OPTIONS_PAGE_MIN)
        int page;

        @Min(value = 1, message = SEARCH_KIT_OPTION_SIZE_MIN)
        @Max(value = 100, message = SEARCH_KIT_OPTIONS_SIZE_MAX)
        int size;

        @NotNull(message = COMMON_CURRENT_USER_ID_NOT_NULL)
        UUID currentUserId;

        @Builder
        public Param(String query, int page, int size, UUID currentUserId) {
            this.query = query;
            this.page = page;
            this.size = size;
            this.currentUserId = currentUserId;
            this.validateSelf();
        }
    }

    record KitListItem(long id, String title, boolean isPrivate) {}
}
