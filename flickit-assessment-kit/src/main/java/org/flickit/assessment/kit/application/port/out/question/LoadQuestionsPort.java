package org.flickit.assessment.kit.application.port.out.question;

import org.flickit.assessment.kit.application.domain.Question;

import java.util.List;

public interface LoadQuestionsPort {

    List<Question> loadAllByKitVersionId(long kitVersionId);

    List<Result> loadQuestionsWithoutAnswerRange(long kitVersionId);

    List<Result> loadQuestionsWithoutImpact(long kitVersionId);

    record Result(int questionIndex, long questionnaireId, String questionnaireTitle) {
    }
}
