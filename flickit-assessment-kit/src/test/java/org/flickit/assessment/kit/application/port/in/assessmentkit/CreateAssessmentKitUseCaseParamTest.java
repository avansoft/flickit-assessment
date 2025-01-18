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

class CreateAssessmentKitUseCaseParamTest {

    @Test
    void testCreateAssessmentKitUseCaseParam_titleParamViolatesConstraint_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.title(null)));
        assertThat(throwable).hasMessage("title: " + CREATE_ASSESSMENT_KIT_TITLE_NOT_NULL);

        throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.title("  ab  ")));
        assertThat(throwable).hasMessage("title: " + CREATE_ASSESSMENT_KIT_TITLE_SIZE_MIN);

        throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.title(RandomStringUtils.randomAlphanumeric(101))));
        assertThat(throwable).hasMessage("title: " + CREATE_ASSESSMENT_KIT_TITLE_SIZE_MAX);
    }


    @Test
    void testCreateAssessmentKitUseCaseParam_summaryParamViolatesConstraint_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.summary(null)));
        assertThat(throwable).hasMessage("summary: " + CREATE_ASSESSMENT_KIT_SUMMARY_NOT_NULL);

        throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.summary("  ab  ")));
        assertThat(throwable).hasMessage("summary: " + CREATE_ASSESSMENT_KIT_SUMMARY_SIZE_MIN);

        throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.summary(RandomStringUtils.randomAlphanumeric(1001))));
        assertThat(throwable).hasMessage("summary: " + CREATE_ASSESSMENT_KIT_SUMMARY_SIZE_MAX);
    }

    @Test
    void testCreateAssessmentKitUseCaseParam_aboutParamViolatesConstraint_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.about(null)));
        assertThat(throwable).hasMessage("about: " + CREATE_ASSESSMENT_KIT_ABOUT_NOT_NULL);

        throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.about("  ab  ")));
        assertThat(throwable).hasMessage("about: " + CREATE_ASSESSMENT_KIT_ABOUT_SIZE_MIN);

        throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.about(RandomStringUtils.randomAlphanumeric(1001))));
        assertThat(throwable).hasMessage("about: " + CREATE_ASSESSMENT_KIT_ABOUT_SIZE_MAX);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void testCreateAssessmentKitUseCaseParam_whenLangParamViolatesConstraints_thenSetLangToDefault(String lang) {
        var param = createParam(b -> b.lang(lang));
        assertEquals("EN", param.getLang());

        param = createParam(b -> b.lang("FR"));
        assertEquals("EN", param.getLang());
    }

    @Test
    void testCreateAssessmentKitUseCaseParam_isPrivateIsNull_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.isPrivate(null)));
        assertThat(throwable).hasMessage("isPrivate: " + CREATE_ASSESSMENT_KIT_IS_PRIVATE_NOT_NULL);
    }

    @Test
    void testCreateAssessmentKitUseCaseParam_expertGroupIdIsNull_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.expertGroupId(null)));
        assertThat(throwable).hasMessage("expertGroupId: " + CREATE_ASSESSMENT_KIT_EXPERT_GROUP_ID_NOT_NULL);
    }

    @Test
    void testCreateAssessmentKitUseCaseParam_tagIdsViolateConstraints_ErrorMessage() {
        var throwableNull = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.tagIds(null)));
        assertThat(throwableNull).hasMessage("tagIds: " + CREATE_ASSESSMENT_KIT_TAG_IDS_NOT_NULL);

        var throwableEmpty = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.tagIds(List.of())));
        assertThat(throwableEmpty).hasMessage("tagIds: " + CREATE_ASSESSMENT_KIT_TAG_IDS_NOT_NULL);
    }

    @Test
    void testCreateAssessmentKitUseCaseParam_currentUserIdIsNull_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.currentUserId(null)));
        assertThat(throwable).hasMessage("currentUserId: " + COMMON_CURRENT_USER_ID_NOT_NULL);
    }

    private CreateAssessmentKitUseCase.Param createParam(Consumer<CreateAssessmentKitUseCase.Param.ParamBuilder> changer) {
        var paramBuilder = paramBuilder();
        changer.accept(paramBuilder);
        return paramBuilder.build();
    }

    private CreateAssessmentKitUseCase.Param.ParamBuilder paramBuilder() {
        return CreateAssessmentKitUseCase.Param.builder()
            .title("Enterprise")
            .summary("summary")
            .about("about")
            .lang("EN")
            .isPrivate(true)
            .expertGroupId(123L)
            .tagIds(List.of(1L, 2L, 3L))
            .currentUserId(UUID.randomUUID());
    }
}
