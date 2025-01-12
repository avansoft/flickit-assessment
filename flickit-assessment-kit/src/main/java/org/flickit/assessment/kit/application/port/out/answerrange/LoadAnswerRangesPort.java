package org.flickit.assessment.kit.application.port.out.answerrange;

import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.kit.application.domain.AnswerRange;

import java.util.List;

public interface LoadAnswerRangesPort {

    PaginatedResponse<AnswerRange> loadByKitVersionId(long kitVersionId, int page, int size);

    List<AnswerRange> loadAnswerRangesWithNotEnoughOptions(long kitVersionId);
}
