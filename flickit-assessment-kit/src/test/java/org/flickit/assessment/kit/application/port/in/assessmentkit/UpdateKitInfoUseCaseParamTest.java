package org.flickit.assessment.kit.application.port.in.assessmentkit;

import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.RandomStringUtils;
import org.flickit.assessment.common.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_ID_NOT_NULL;
import static org.flickit.assessment.kit.common.ErrorMessageKey.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UpdateKitInfoUseCaseParamTest {

    @Test
    void testUpdateKitInfoUseCaseParam_kitIdParamViolatesConstraints_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.kitId(null)));
        assertThat(throwable).hasMessage("kitId: " + UPDATE_KIT_INFO_KIT_ID_NOT_NULL);
    }

    @Test
    void testUpdateKitInfoUseCaseParam_TitleParamViolatesConstraints_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.title("ab")));
        assertThat(throwable).hasMessage("title: " + UPDATE_KIT_INFO_TITLE_SIZE_MIN);

        throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.title(RandomStringUtils.randomAlphabetic(51))));
        assertThat(throwable).hasMessage("title: " + UPDATE_KIT_INFO_TITLE_SIZE_MAX);
    }

    @Test
    void testUpdateKitInfoUseCaseParam_summaryParamViolatesConstraints_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.summary("ab")));
        assertThat(throwable).hasMessage("summary: " + UPDATE_KIT_INFO_SUMMARY_SIZE_MIN);

        throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.summary(RandomStringUtils.randomAlphabetic(201))));
        assertThat(throwable).hasMessage("summary: " + UPDATE_KIT_INFO_SUMMARY_SIZE_MAX);
    }

    @Test
    void testUpdateKitInfoUseCaseParam_aboutParamViolatesConstraints_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.about("ab")));
        assertThat(throwable).hasMessage("about: " + UPDATE_KIT_INFO_ABOUT_SIZE_MIN);

        throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.about(RandomStringUtils.randomAlphabetic(1001))));
        assertThat(throwable).hasMessage("about: " + UPDATE_KIT_INFO_ABOUT_SIZE_MAX);
    }

    @Test
    void testUpdateKitInfoUseCaseParam_langParamViolatesConstraints_ErrorMessage() {
        var throwable = assertThrows(ValidationException.class,
            () -> createParam(b -> b.lang("FR")));
        assertEquals(UPDATE_KIT_INFO_KIT_LANGUAGE_INVALID, throwable.getMessageKey());
    }

    @Test
    void testUpdateKitInfoUseCaseParam_tagsParamViolatesConstraints_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.tags(new ArrayList<>())));
        assertThat(throwable).hasMessage("tags: " + UPDATE_KIT_INFO_TAGS_SIZE_MIN);
    }

    @Test
    void testUpdateKitInfoUseCaseParam_currentUserIdParamViolatesConstraints_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.currentUserId(null)));
        assertThat(throwable).hasMessage("currentUserId: " + COMMON_CURRENT_USER_ID_NOT_NULL);
    }

    private void createParam(Consumer<UpdateKitInfoUseCase.Param.ParamBuilder> changer) {
        var param = paramBuilder();
        changer.accept(param);
        param.build();
    }

    private UpdateKitInfoUseCase.Param.ParamBuilder paramBuilder() {
        return UpdateKitInfoUseCase.Param.builder()
            .kitId(1L)
            .title("title")
            .summary("summary")
            .about("about")
            .lang("EN")
            .published(true)
            .isPrivate(false)
            .price(10d)
            .tags(List.of(1L))
            .currentUserId(UUID.randomUUID());
    }
}
