package org.flickit.assessment.core.application.service.answerhistory;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.application.domain.assessment.AssessmentAccessChecker;
import org.flickit.assessment.common.application.domain.assessment.AssessmentPermission;
import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.core.application.port.in.answerhistory.GetAnswerHistoryListUseCase;
import org.flickit.assessment.core.application.port.out.answerhistory.LoadAnswerHistoryListPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetAnswerHistoryListService implements GetAnswerHistoryListUseCase {

    private final LoadAnswerHistoryListPort loadAnswerHistoryListPort;
    private final AssessmentAccessChecker assessmentAccessChecker;

    @Override
    public PaginatedResponse<AnswerHistoryListItem> getAnswerHistoryList(Param param) {
        if (!assessmentAccessChecker.isAuthorized(param.getAssessmentId(),
            param.getCurrentUserId(),
            AssessmentPermission.VIEW_ANSWER_HISTORY_LIST))
            throw new AccessDeniedException(COMMON_CURRENT_USER_NOT_ALLOWED);

        var paginatedResponse = loadAnswerHistoryListPort.load(param.getAssessmentId(),
            param.getQuestionId(),
            param.getPage(),
            param.getSize());

        List<AnswerHistoryListItem> items = paginatedResponse.getItems().stream()
            .map(e -> new AnswerHistoryListItem(e.submitTime(),
                e.submitterName(),
                e.confidenceLevel(),
                e.answerOptionIndex(),
                e.isNotApplicable()))
            .toList();

        return new PaginatedResponse<>(items,
            paginatedResponse.getPage(),
            paginatedResponse.getSize(),
            paginatedResponse.getSort(),
            paginatedResponse.getOrder(),
            paginatedResponse.getTotal());
    }
}
