package org.flickit.assessment.kit.application.service.questionnaire;

import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.data.jpa.kit.questionnaire.QuestionnaireJpaEntity;
import org.flickit.assessment.kit.application.domain.KitVersion;
import org.flickit.assessment.kit.application.domain.Questionnaire;
import org.flickit.assessment.kit.application.port.in.questionnaire.GetQuestionnairesUseCase;
import org.flickit.assessment.kit.application.port.out.expertgroup.LoadExpertGroupMemberIdsPort;
import org.flickit.assessment.kit.application.port.out.kitversion.LoadKitVersionPort;
import org.flickit.assessment.kit.application.port.out.questionnaire.LoadQuestionnairesPort;
import org.flickit.assessment.kit.test.fixture.application.QuestionnaireMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.flickit.assessment.kit.test.fixture.application.AssessmentKitMother.simpleKit;
import static org.flickit.assessment.kit.test.fixture.application.KitVersionMother.createKitVersion;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetQuestionnaireServiceTest {

    @InjectMocks
    private GetQuestionnaireService service;

    @Mock
    private LoadQuestionnairesPort loadQuestionnairesPort;

    @Mock
    private LoadKitVersionPort loadKitVersionPort;

    @Mock
    LoadExpertGroupMemberIdsPort loadExpertGroupMemberIdsPort;

    private final KitVersion kitVersion = createKitVersion(simpleKit());

    @Test
    void testGetQuestionnaire_WhenCurrentUserIsNotMemberOfExpertGroup_ThenThrowAccessDeniedException() {
        var param = createParam(GetQuestionnairesUseCase.Param.ParamBuilder::build);
        List<LoadExpertGroupMemberIdsPort.Result> results = List.of(new LoadExpertGroupMemberIdsPort.Result(UUID.randomUUID()));

        when(loadKitVersionPort.load(param.getKitVersionId())).thenReturn(kitVersion);
        when(loadExpertGroupMemberIdsPort.loadMemberIds(kitVersion.getKit().getExpertGroupId())).thenReturn(results);

        AccessDeniedException throwable = assertThrows(AccessDeniedException.class, () -> service.getQuestionnaires(param));

        assertEquals(COMMON_CURRENT_USER_NOT_ALLOWED, throwable.getMessage());
        verifyNoInteractions(loadQuestionnairesPort);
    }

    @Test
    void testGetQuestionnaire_WhenCurrentUserIsMemberOfExpertGroup_ThenGetQuestionnaires() {
        var param = createParam(GetQuestionnairesUseCase.Param.ParamBuilder::build);
        List<LoadExpertGroupMemberIdsPort.Result> results = List.of(new LoadExpertGroupMemberIdsPort.Result(param.getCurrentUserId()));
        Questionnaire questionnaire1 = QuestionnaireMother.questionnaireWithTitle("title1");
        Questionnaire questionnaire2 = QuestionnaireMother.questionnaireWithTitle("title2");
        List<LoadQuestionnairesPort.Result> items = List.of(new LoadQuestionnairesPort.Result(questionnaire1, 4),
            new LoadQuestionnairesPort.Result(questionnaire2, 5));
        PaginatedResponse<LoadQuestionnairesPort.Result> pageResult = new PaginatedResponse<>(
            items,
            param.getPage(),
            param.getSize(),
            QuestionnaireJpaEntity.Fields.INDEX,
            Sort.Direction.ASC.name().toLowerCase(),
            items.size()
        );

        when(loadKitVersionPort.load(param.getKitVersionId())).thenReturn(kitVersion);
        when(loadExpertGroupMemberIdsPort.loadMemberIds(kitVersion.getKit().getExpertGroupId())).thenReturn(results);
        when(loadQuestionnairesPort.loadAllByKitVersionId(param.getKitVersionId(), param.getPage(), param.getSize())).thenReturn(pageResult);

        var paginatedResponse = assertDoesNotThrow(() -> service.getQuestionnaires(param));
        assertNotNull(paginatedResponse);
        assertEquals(pageResult.getItems().size(), paginatedResponse.getItems().size());
        for (int i = 0; i < pageResult.getItems().size(); i++) {
            var expected = pageResult.getItems().get(i);
            var actual = paginatedResponse.getItems().get(i);
            assertEquals(expected.questionnaire(), actual.questionnaire());
            assertEquals(expected.questionsCount(), actual.questionsCount());
        }
    }

    private GetQuestionnairesUseCase.Param createParam(Consumer<GetQuestionnairesUseCase.Param.ParamBuilder> changer) {
        var paramBuilder = paramBuilder();
        changer.accept(paramBuilder);
        return paramBuilder.build();
    }

    private GetQuestionnairesUseCase.Param.ParamBuilder paramBuilder() {
        return GetQuestionnairesUseCase.Param.builder()
            .kitVersionId(1L)
            .currentUserId(UUID.randomUUID())
            .page(1)
            .size(10);
    }
}