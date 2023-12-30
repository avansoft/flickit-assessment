package org.flickit.assessment.core.application.service.attribute;

import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.core.application.port.in.attribute.GetAttributeScoreDetailUseCase;
import org.flickit.assessment.core.application.port.out.assessment.CheckUserAssessmentAccessPort;
import org.flickit.assessment.core.application.port.out.attribute.LoadAttributeScoreDetailPort;
import org.flickit.assessment.core.test.fixture.application.QuestionScoreMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.flickit.assessment.core.application.port.in.attribute.GetAttributeScoreDetailUseCase.Param;
import static org.flickit.assessment.core.application.port.in.attribute.GetAttributeScoreDetailUseCase.QuestionScore;
import static org.flickit.assessment.core.test.fixture.application.QuestionScoreMother.questionWithScore;
import static org.flickit.assessment.core.test.fixture.application.QuestionScoreMother.questionWithoutAnswer;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAttributeScoreDetailServiceTest {

    @InjectMocks
    private GetAttributeScoreDetailService service;

    @Mock
    private LoadAttributeScoreDetailPort loadAttributeScoreDetailPort;

    @Mock
    private CheckUserAssessmentAccessPort checkUserAssessmentAccessPort;

    @Test
    void testGetAttributeScoreDetail_ValidParam() {
        UUID assessmentId = UUID.randomUUID();
        long attributeId = 123L;
        long maturityLevelId = 124L;
        UUID currentUserId = UUID.randomUUID();
        Param param = new Param(
            assessmentId,
            attributeId,
            maturityLevelId,
            currentUserId);

        QuestionScore questionWithFullScore = questionWithScore(4, 1.0);
        QuestionScore questionWithHalfScore = questionWithScore(2, 0.5);
        QuestionScore questionWithoutScore = questionWithScore(1, 0.0);
        QuestionScore questionWithoutAnswer = questionWithoutAnswer(4);
        QuestionScore questionMarkedAsNotApplicable = QuestionScoreMother.questionMarkedAsNotApplicable();

        List<QuestionScore> scores = List.of(
            questionWithFullScore,
            questionWithHalfScore,
            questionWithoutScore,
            questionWithoutAnswer,
            questionMarkedAsNotApplicable
            );

        when(loadAttributeScoreDetailPort.loadScoreDetail(assessmentId, attributeId, maturityLevelId)).thenReturn(scores);
        when(checkUserAssessmentAccessPort.hasAccess(assessmentId, currentUserId)).thenReturn(true);

        GetAttributeScoreDetailUseCase.Result result = service.getAttributeScoreDetail(param);

        assertNotNull(result);
        assertEquals(5, result.gainedScore());
        assertEquals(11, result.totalScore());
        assertEquals(5, result.questionScores().size());
        //order of QuestionScore items should be equal to order of port items
        assertEquals(questionWithFullScore, result.questionScores().get(0));
        assertEquals(questionWithHalfScore, result.questionScores().get(1));
        assertEquals(questionWithoutScore, result.questionScores().get(2));
        assertEquals(questionWithoutAnswer, result.questionScores().get(3));
        assertEquals(questionMarkedAsNotApplicable, result.questionScores().get(4));
    }

    @Test
    void testGetAttributeScoreDetail_ValidParam_NoQuestionScore() {
        UUID assessmentId = UUID.randomUUID();
        long attributeId = 123L;
        long maturityLevelId = 124L;
        UUID currentUserId = UUID.randomUUID();
        Param param = new Param(
            assessmentId,
            attributeId,
            maturityLevelId,
            currentUserId);

        List<QuestionScore> scores = List.of();
        when(loadAttributeScoreDetailPort.loadScoreDetail(assessmentId, attributeId, maturityLevelId)).thenReturn(scores);
        when(checkUserAssessmentAccessPort.hasAccess(assessmentId, currentUserId)).thenReturn(true);

        GetAttributeScoreDetailUseCase.Result result = service.getAttributeScoreDetail(param);

        assertNotNull(result);
        assertEquals(0, result.gainedScore());
        assertEquals(0, result.totalScore());
        assertTrue(result.questionScores().isEmpty());
    }

    @Test
    void testGetAttributeScoreDetail_InvalidCurrentUser_ThrowsException() {
        UUID assessmentId = UUID.randomUUID();
        long attributeId = 123L;
        long maturityLevelId = 124L;
        UUID currentUserId = UUID.randomUUID();
        Param param = new Param(
            assessmentId,
            attributeId,
            maturityLevelId,
            currentUserId);

        when(checkUserAssessmentAccessPort.hasAccess(assessmentId, currentUserId)).thenReturn(false);

        var throwable = assertThrows(AccessDeniedException.class,
            () -> service.getAttributeScoreDetail(param));
        assertThat(throwable).hasMessage(COMMON_CURRENT_USER_NOT_ALLOWED);
    }
}
