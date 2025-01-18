package org.flickit.assessment.kit.application.port.in.assessmentkit;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_ID_NOT_NULL;
import static org.flickit.assessment.kit.common.ErrorMessageKey.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SearchKitOptionsUseCaseParamTest {

    @Test
    void testSearchKitOptionsUseCaseParam_PageParamViolatesConstraints_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.page(-1)));
        assertThat(throwable).hasMessage("page: " + SEARCH_KIT_OPTIONS_PAGE_MIN);
    }

    @Test
    void testSearchKitOptionsUseCaseParam_SizeParamViolatesConstraints_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.size(101)));
        assertThat(throwable).hasMessage("size: " + SEARCH_KIT_OPTIONS_SIZE_MAX);

        throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.size(-1)));
        assertThat(throwable).hasMessage("size: " + SEARCH_KIT_OPTION_SIZE_MIN);
    }

    @Test
    void testSearchKitOptionsUseCaseParam_currentUserParamViolatesConstraintsErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.currentUserId(null)));
        assertThat(throwable).hasMessage("currentUserId: " + COMMON_CURRENT_USER_ID_NOT_NULL);
    }

    private void createParam(Consumer<SearchKitOptionsUseCase.Param.ParamBuilder> changer) {
        var paramBuilder = paramBuilder();
        changer.accept(paramBuilder);
        paramBuilder.build();
    }

    private SearchKitOptionsUseCase.Param.ParamBuilder paramBuilder() {
        return SearchKitOptionsUseCase.Param.builder()
            .query("query")
            .page(0)
            .size(50)
            .currentUserId(UUID.randomUUID());
    }
}
