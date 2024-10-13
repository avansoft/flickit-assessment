package org.flickit.assessment.kit.application.port.in.subject;

import jakarta.validation.ConstraintViolationException;
import org.flickit.assessment.kit.application.port.in.subject.UpdateSubjectsOrderUseCase.Param;
import org.flickit.assessment.kit.application.port.in.subject.UpdateSubjectsOrderUseCase.SubjectParam;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_ID_NOT_NULL;
import static org.flickit.assessment.kit.common.ErrorMessageKey.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UpdateSubjectsOrderUseCaseParamTest {

    @Test
    void testUpdateSubjectsOrderUseCaseParam_kitVersionIdParamViolatesConstraints_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.kitVersionId(null)));
        assertThat(throwable).hasMessage("kitVersionId: " + UPDATE_SUBJECTS_ORDER_KIT_VERSION_ID_NOT_NULL);
    }

    @Test
    void testUpdateSubjectsOrderUseCaseParam_subjectsParamViolatesConstraints_ErrorMessage() {
        var throwableNullViolation = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.subjects(null)));
        assertThat(throwableNullViolation).hasMessage("subjects: " + UPDATE_SUBJECTS_ORDER_SUBJECTS_NOT_NULL);
        var throwableMinViolation = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.subjects(List.of())));
        assertThat(throwableMinViolation).hasMessage("subjects: " + UPDATE_SUBJECTS_ORDER_SUBJECTS_SIZE_MIN);
    }

    @Test
    void testUpdateSubjectsOrderUseCaseParam_currentUserParamViolatesConstraints_ErrorMessage() {
        var throwable = assertThrows(ConstraintViolationException.class,
            () -> createParam(b -> b.currentUserId(null)));
        assertThat(throwable).hasMessage("currentUserId: " + COMMON_CURRENT_USER_ID_NOT_NULL);
    }

    private void createParam(Consumer<Param.ParamBuilder> changer) {
        var paramBuilder = paramBuilder();
        changer.accept(paramBuilder);
        paramBuilder.build();
    }

    private Param.ParamBuilder paramBuilder() {
        return Param.builder()
            .kitVersionId(1L)
            .subjects(List.of(new SubjectParam(2L, 5)))
            .currentUserId(UUID.randomUUID());
    }

}