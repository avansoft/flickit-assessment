package org.flickit.assessment.kit.application.service.assessmentkit;

import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.common.exception.ValidationException;
import org.flickit.assessment.kit.application.domain.Attribute;
import org.flickit.assessment.kit.application.domain.dsl.AssessmentKitDslModel;
import org.flickit.assessment.kit.application.port.in.assessmentkit.GetKitDslUseCase;
import org.flickit.assessment.kit.application.port.out.assessmentkit.LoadAssessmentKitPort;
import org.flickit.assessment.kit.application.port.out.expertgroupaccess.CheckExpertGroupAccessPort;
import org.flickit.assessment.kit.application.port.out.kitdsl.LoadKitDslModelPort;
import org.flickit.assessment.kit.test.fixture.application.AssessmentKitMother;
import org.flickit.assessment.kit.test.fixture.application.AttributeMother;
import org.flickit.assessment.kit.test.fixture.application.SubjectMother;
import org.flickit.assessment.kit.test.fixture.application.dsl.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.function.Consumer;

import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.flickit.assessment.kit.common.ErrorMessageKey.GET_KIT_DSL_NOT_AVAILABLE;
import static org.flickit.assessment.kit.test.fixture.application.AnswerRangeMother.createReusableAnswerRangeWithTwoOptions;
import static org.flickit.assessment.kit.test.fixture.application.Constants.*;
import static org.flickit.assessment.kit.test.fixture.application.MaturityLevelMother.levelThree;
import static org.flickit.assessment.kit.test.fixture.application.MaturityLevelMother.levelTwo;
import static org.flickit.assessment.kit.test.fixture.application.QuestionMother.createQuestion;
import static org.flickit.assessment.kit.test.fixture.application.QuestionnaireMother.questionnaireWithTitle;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetKitDslServiceTest {

    @InjectMocks
    private GetKitDslService service;

    @Mock
    private LoadAssessmentKitPort loadAssessmentKitPort;

    @Mock
    CheckExpertGroupAccessPort checkExpertGroupAccessPort;

    @Mock
    LoadKitDslModelPort loadKitDslModelPort;

    @Test
    void testGetKitDsl_userIsNotExpertGroupMember_throwsAccessDeniedException() {
        var param = createParam(GetKitDslUseCase.Param.ParamBuilder::build);
        var kit = AssessmentKitMother.simpleKit();

        when(loadAssessmentKitPort.load(param.getKitId())).thenReturn(kit);
        when(checkExpertGroupAccessPort.checkIsMember(kit.getExpertGroupId(), param.getCurrentUserId())).thenReturn(false);

        var throwable = assertThrows(AccessDeniedException.class, () -> service.getKitDsl(param));
        assertEquals(COMMON_CURRENT_USER_NOT_ALLOWED, throwable.getMessage());
    }

    @Test
    void testGetKitDsl_activeKitVersionNotFound_throwsValidationException() {
        var param = createParam(GetKitDslUseCase.Param.ParamBuilder::build);
        var kit = AssessmentKitMother.kitWithKitVersionId(null);

        when(loadAssessmentKitPort.load(param.getKitId())).thenReturn(kit);
        when(checkExpertGroupAccessPort.checkIsMember(kit.getExpertGroupId(), param.getCurrentUserId())).thenReturn(true);

        var throwable = assertThrows(ValidationException.class, () -> service.getKitDsl(param));
        assertEquals(GET_KIT_DSL_NOT_AVAILABLE, throwable.getMessageKey());
    }

    @Test
    void testGetKitDsl_vaLidParams_success() {
        var param = createParam(GetKitDslUseCase.Param.ParamBuilder::build);
        var kit = AssessmentKitMother.kitWithKitVersionId(param.getKitId());

        var dslQuestionnaire = QuestionnaireDslModelMother.domainToDslModel(questionnaireWithTitle(QUESTIONNAIRE_TITLE1));
        Attribute attrOne = AttributeMother.attributeWithTitle("attr1");
        Attribute attrTwo = AttributeMother.attributeWithTitle("attr2");

        var questionOne = createQuestion(QUESTION_CODE1, QUESTION_TITLE1, 1, "", Boolean.FALSE, Boolean.TRUE, 153L, 1L);
        var questionTwo = createQuestion(QUESTION_CODE2, QUESTION_TITLE2, 2, "", Boolean.FALSE, Boolean.TRUE, 153L, 1L);
        var subject = SubjectMother.subjectWithAttributes(SUBJECTS_TITLE1, Arrays.asList(attrOne, attrTwo));
        var subjectDslModel = SubjectDslModelMother.domainToDslModel(subject);
        var dslAttrOne = AttributeDslModelMother.domainToDslModel(attrOne, e -> e.subjectCode(subject.getCode()));
        var dslAttrTwo = AttributeDslModelMother.domainToDslModel(attrTwo, e -> e.subjectCode(subject.getCode()));
        var dslQuestionOne = QuestionDslModelMother.domainToDslModel(questionOne, q -> q
            .answerOptions(List.of())
            .questionnaireCode(QUESTIONNAIRE_TITLE1));
        var dslQuestionTwo = QuestionDslModelMother.domainToDslModel(questionTwo, q -> q
            .answerOptions(List.of())
            .questionnaireCode(QUESTIONNAIRE_TITLE1));
        var dslMaturityLevelTwo = MaturityLevelDslModelMother.domainToDslModel(levelTwo());
        var dslMaturityLevelThree = MaturityLevelDslModelMother.domainToDslModel(levelThree());
        var answerRangeOne = createReusableAnswerRangeWithTwoOptions(1);
        var answerRangeTwo = createReusableAnswerRangeWithTwoOptions(2);
        var dslOptionsRangeOne = answerRangeOne.getAnswerOptions().stream()
            .map(e -> AnswerOptionDslModelMother.answerOptionDslModel(e.getIndex(),
                e.getTitle(),
                e.getValue()))
            .toList();
        var dslRangeOne = AnswerRangeDslModelMother.domainToDslModel(answerRangeOne,
            b-> b.answerOptions(dslOptionsRangeOne));
        var dslRangeTwo = AnswerRangeDslModelMother.domainToDslModel(answerRangeTwo,
            b-> b.answerOptions(dslOptionsRangeOne));

        var kitDslModel = AssessmentKitDslModel.builder()
            .questionnaires(List.of(dslQuestionnaire))
            .attributes(List.of(dslAttrOne, dslAttrTwo))
            .questions(List.of(dslQuestionOne, dslQuestionTwo))
            .subjects(List.of(subjectDslModel))
            .maturityLevels(List.of(dslMaturityLevelTwo, dslMaturityLevelThree))
            .answerRanges(List.of(dslRangeOne, dslRangeTwo))
            .build();

        when(loadAssessmentKitPort.load(param.getKitId())).thenReturn(kit);
        when(checkExpertGroupAccessPort.checkIsMember(kit.getExpertGroupId(), param.getCurrentUserId())).thenReturn(true);
        when(loadKitDslModelPort.load(kit.getActiveVersionId())).thenReturn(kitDslModel);

        var result = service.getKitDsl(param);
        assertEquals(1, result.getQuestionnaires().size());
        assertEquals(QUESTIONNAIRE_TITLE1, result.getQuestionnaires().getFirst().getTitle());
        assertEquals(1, result.getSubjects().size());
        assertTrue(result.getSubjects().stream().anyMatch(a -> a.getCode().equals(subject.getCode())));
        assertEquals(2, result.getQuestions().size());
        assertTrue(result.getQuestions().stream().anyMatch(q -> q.getCode().equals(questionOne.getCode())));
        assertTrue(result.getQuestions().stream().anyMatch(q -> q.getCode().equals(questionTwo.getCode())));
        assertEquals(2, result.getMaturityLevels().size());
        assertTrue(result.getMaturityLevels().stream().anyMatch(m -> m.getCode().equals(levelTwo().getCode())));
        assertTrue(result.getMaturityLevels().stream().anyMatch(m -> m.getCode().equals(levelThree().getCode())));
        assertEquals(2, result.getAnswerRanges().size());
        assertTrue(result.getAnswerRanges().stream().anyMatch(a -> a.getCode().equals(answerRangeOne.getCode())));
        assertTrue(result.getAnswerRanges().stream().anyMatch(a -> a.getCode().equals(answerRangeOne.getCode())));
    }

    private GetKitDslUseCase.Param createParam(Consumer<GetKitDslUseCase.Param.ParamBuilder> changer) {
        var paramBuilder = paramBuilder();
        changer.accept(paramBuilder);
        return paramBuilder.build();
    }

    private GetKitDslUseCase.Param.ParamBuilder paramBuilder() {
        return GetKitDslUseCase.Param.builder()
            .kitId(123L)
            .currentUserId(UUID.randomUUID());
    }
}
