package org.flickit.assessment.core.application.port.in.questionnaire;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.flickit.assessment.common.application.SelfValidating;
import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.core.application.domain.ConfidenceLevel;

import java.util.List;
import java.util.UUID;

import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_ID_NOT_NULL;
import static org.flickit.assessment.core.common.ErrorMessageKey.*;

public interface GetAssessmentQuestionnaireQuestionListUseCase {

    PaginatedResponse<Result> getQuestionnaireQuestionList(Param param);

    @Value
    @EqualsAndHashCode(callSuper = true)
    class Param extends SelfValidating<Param> {

        @NotNull(message = GET_ASSESSMENT_QUESTIONNAIRE_QUESTION_LIST_ASSESSMENT_ID_NOT_NULL)
        UUID assessmentId;

        @NotNull(message = GET_ASSESSMENT_QUESTIONNAIRE_QUESTION_LIST_QUESTIONNAIRE_ID_NOT_NULL)
        Long questionnaireId;

        @Min(value = 1, message = GET_ASSESSMENT_QUESTIONNAIRE_QUESTION_LIST_SIZE_MIN)
        @Max(value = 100, message = GET_ASSESSMENT_QUESTIONNAIRE_QUESTION_LIST_SIZE_MAX)
        int size;

        @Min(value = 0, message = GET_ASSESSMENT_QUESTIONNAIRE_QUESTION_LIST_PAGE_MIN)
        int page;

        @NotNull(message = COMMON_CURRENT_USER_ID_NOT_NULL)
        UUID currentUserId;

        public Param(UUID assessmentId, Long questionnaireId, int size, int page, UUID currentUserId) {
            this.assessmentId = assessmentId;
            this.questionnaireId = questionnaireId;
            this.size = size;
            this.page = page;
            this.currentUserId = currentUserId;
            this.validateSelf();
        }
    }

    record Result(Long id,
                  Integer index,
                  String title,
                  String hint,
                  Boolean mayNotBeApplicable,
                  List<Option> options,
                  QuestionAnswer answer,
                  Issues issues) {
    }

    record Option(Long id, Integer index, String title) {
    }

    record QuestionAnswer(Option selectedOption,
                          ConfidenceLevel confidenceLevel,
                          Boolean isNotApplicable) {
    }

    record Issues(boolean isUnanswered,
                  Boolean isAnsweredWithLowConfidence,
                  Boolean isAnsweredWithoutEvidences,
                  int unresolvedCommentsCount){
    }
}
