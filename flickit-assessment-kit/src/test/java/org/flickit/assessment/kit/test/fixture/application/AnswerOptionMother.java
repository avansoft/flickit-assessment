package org.flickit.assessment.kit.test.fixture.application;

import org.flickit.assessment.kit.application.domain.AnswerOption;

public class AnswerOptionMother {

    private static Long id = 1L;
    private static int index = 1;
    private static Long answerRangeId = 123L;

    public static AnswerOption createSimpleAnswerOption() {
        return new AnswerOption(id++,
            "title" + id,
            index++,
            answerRangeId++);
    }

    public static AnswerOption createAnswerOption(long answerRangeId, String title, int index) {
        return new AnswerOption(
            id++,
            title,
            index,
            answerRangeId
        );
    }
}
