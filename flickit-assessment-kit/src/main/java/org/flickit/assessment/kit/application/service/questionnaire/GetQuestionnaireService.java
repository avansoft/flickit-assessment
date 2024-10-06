package org.flickit.assessment.kit.application.service.questionnaire;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.data.jpa.kit.questionnaire.QuestionnaireJpaEntity;
import org.flickit.assessment.kit.application.domain.KitVersion;
import org.flickit.assessment.kit.application.port.in.questionnaire.GetQuestionnairesUseCase;
import org.flickit.assessment.kit.application.port.out.expertgroup.LoadExpertGroupMemberIdsPort;
import org.flickit.assessment.kit.application.port.out.kitversion.LoadKitVersionPort;
import org.flickit.assessment.kit.application.port.out.questionnaire.LoadQuestionnairesPort;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetQuestionnaireService implements GetQuestionnairesUseCase {

    private final LoadQuestionnairesPort loadQuestionnairesPort;
    private final LoadKitVersionPort loadKitVersionPort;
    private final LoadExpertGroupMemberIdsPort loadExpertGroupMemberIdsPort;

    @Override
    public PaginatedResponse<QuestionnaireListItem> getQuestionnaires(Param param) {
        KitVersion kitVersion = loadKitVersionPort.load(param.getKitVersionId());
        List<UUID> ids = loadExpertGroupMemberIdsPort.loadMemberIds(kitVersion.getKit().getExpertGroupId()).stream()
            .map(LoadExpertGroupMemberIdsPort.Result::userId)
            .toList();
        if (!ids.contains(param.getCurrentUserId()))
            throw new AccessDeniedException(COMMON_CURRENT_USER_NOT_ALLOWED);

        var pageResult = loadQuestionnairesPort.loadAllByKitVersionId(param.getKitVersionId(), param.getPage(), param.getSize());
        List<QuestionnaireListItem> items = pageResult.getItems().stream()
            .map(e -> new QuestionnaireListItem(e.questionnaire(), e.questionsCount()))
            .toList();

        return new PaginatedResponse<>(
            items,
            pageResult.getPage(),
            pageResult.getSize(),
            QuestionnaireJpaEntity.Fields.INDEX,
            Sort.Direction.ASC.name().toLowerCase(),
            pageResult.getTotal()
        );
    }
}