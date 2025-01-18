package org.flickit.assessment.kit.application.port.in.assessmentkit;

import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_ID_NOT_NULL;
import static org.flickit.assessment.kit.common.ErrorMessageKey.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateKitByDslUseCaseParamTest {

    @Test
    void testCreateKitByDslUseCaseParam_kitDslIdParamViolatesConstraints_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.kitDslId(null)));
        assertThat(throwable).hasMessage("kitDslId: " + CREATE_KIT_BY_DSL_KIT_DSL_ID_NOT_NULL);
    }

    @Test
    void testCreateKitByDslUseCaseParam_isPrivateParamViolatesConstraints_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.isPrivate(null)));
        assertThat(throwable).hasMessage("isPrivate: " + CREATE_KIT_BY_DSL_IS_PRIVATE_NOT_NULL);
    }

    @Test
    void testCreateKitByDslUseCaseParam_expertGroupIdParamViolatesConstraints_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.expertGroupId(null)));
        assertThat(throwable).hasMessage("expertGroupId: " + CREATE_KIT_BY_DSL_EXPERT_GROUP_ID_NOT_NULL);
    }

    @Test
    void testCreateKitByDslUseCaseParam_titleParamViolatesConstraints_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.title(null)));
        assertThat(throwable).hasMessage("title: " + CREATE_KIT_BY_DSL_TITLE_NOT_NULL);

        throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.title("ab")));
        assertThat(throwable).hasMessage("title: " + CREATE_KIT_BY_DSL_TITLE_SIZE_MIN);

        throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.title(RandomStringUtils.randomAlphanumeric(101))));
        assertThat(throwable).hasMessage("title: " + CREATE_KIT_BY_DSL_TITLE_SIZE_MAX);
    }

    @Test
    void testCreateKitByDslUseCaseParam_summaryParamViolatesConstraints_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.summary(null)));
        assertThat(throwable).hasMessage("summary: " + CREATE_KIT_BY_DSL_SUMMARY_NOT_NULL);

        throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.summary("ab")));
        assertThat(throwable).hasMessage("summary: " + CREATE_KIT_BY_DSL_SUMMARY_SIZE_MIN);

        throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.summary(RandomStringUtils.randomAlphanumeric(1001))));
        assertThat(throwable).hasMessage("summary: " + CREATE_KIT_BY_DSL_SUMMARY_SIZE_MAX);
    }

    @Test
    void testCreateKitByDslUseCaseParam_aboutParamViolatesConstraints_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.about(null)));
        assertThat(throwable).hasMessage("about: " + CREATE_KIT_BY_DSL_ABOUT_NOT_NULL);

        throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.about("ab")));
        assertThat(throwable).hasMessage("about: " + CREATE_KIT_BY_DSL_ABOUT_SIZE_MIN);

        throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.about(RandomStringUtils.randomAlphanumeric(1001))));
        assertThat(throwable).hasMessage("about: " + CREATE_KIT_BY_DSL_ABOUT_SIZE_MAX);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void testCreateKitByDslUseCaseParam_whenLangParamViolatesConstraints_thenDefaultValue(String lang) {
        var param = createParam(b -> b.lang(lang));
        assertEquals("EN", param.getLang());

        param = createParam(b -> b.lang("FR"));
        assertEquals("EN", param.getLang());
    }

    @Test
    void testCreateKitByDslUseCaseParam_tagIdsParamViolatesConstraints_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.tagIds(null)));
        assertThat(throwable).hasMessage("tagIds: " + CREATE_KIT_BY_DSL_TAG_IDS_NOT_NULL);
    }

    @Test
    void testCreateKitByDslUseCaseParam_currentUserIdParamViolatesConstraints_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.currentUserId(null)));
        assertThat(throwable).hasMessage("currentUserId: " + COMMON_CURRENT_USER_ID_NOT_NULL);
    }

    private CreateKitByDslUseCase.Param createParam(Consumer<CreateKitByDslUseCase.Param.ParamBuilder> changer) {
        var param = paramBuilder();
        changer.accept(param);
        return param.build();
    }

    private CreateKitByDslUseCase.Param.ParamBuilder paramBuilder() {
        return CreateKitByDslUseCase.Param.builder()
            .kitDslId(1L)
            .isPrivate(false)
            .expertGroupId(1L)
            .title("title")
            .summary("summary")
            .about("about")
            .lang("EN")
            .tagIds(List.of(1L, 2L))
            .currentUserId(UUID.randomUUID());
    }
}
