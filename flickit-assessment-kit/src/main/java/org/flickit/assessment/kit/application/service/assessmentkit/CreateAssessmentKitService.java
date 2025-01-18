package org.flickit.assessment.kit.application.service.assessmentkit;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.kit.application.domain.KitVersionStatus;
import org.flickit.assessment.kit.application.port.in.assessmentkit.CreateAssessmentKitUseCase;
import org.flickit.assessment.kit.application.port.out.assessmentkit.CreateAssessmentKitPort;
import org.flickit.assessment.kit.application.port.out.assessmentkittag.CreateKitTagRelationPort;
import org.flickit.assessment.kit.application.port.out.expertgroup.LoadExpertGroupMemberIdsPort;
import org.flickit.assessment.kit.application.port.out.expertgroup.LoadExpertGroupOwnerPort;
import org.flickit.assessment.kit.application.port.out.kituseraccess.GrantUserAccessToKitPort;
import org.flickit.assessment.kit.application.port.out.kitversion.CreateKitVersionPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.flickit.assessment.common.util.SlugCodeUtil.generateSlugCode;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateAssessmentKitService implements CreateAssessmentKitUseCase {

    private final LoadExpertGroupOwnerPort loadExpertGroupOwnerPort;
    private final CreateAssessmentKitPort createAssessmentKitPort;
    private final CreateKitVersionPort createKitVersionPort;
    private final LoadExpertGroupMemberIdsPort loadExpertGroupMemberIdsPort;
    private final GrantUserAccessToKitPort grantUserAccessToKitPort;
    private final CreateKitTagRelationPort createKitTagRelationPort;

    @Override
    public Result createAssessmentKit(Param param) {
        UUID expertGroupOwnerId = loadExpertGroupOwnerPort.loadOwnerId(param.getExpertGroupId());
        if (!Objects.equals(expertGroupOwnerId, param.getCurrentUserId()))
            throw new AccessDeniedException(COMMON_CURRENT_USER_NOT_ALLOWED);

        var kitId = createAssessmentKitPort.persist(toPortParam(param));
        createKitVersionPort.persist(new CreateKitVersionPort.Param(kitId, KitVersionStatus.UPDATING, param.getCurrentUserId()));

        var expertGroupMemberIds = loadExpertGroupMemberIdsPort.loadMemberIds(param.getExpertGroupId())
            .stream()
            .map(LoadExpertGroupMemberIdsPort.Result::userId)
            .toList();
        grantUserAccessToKitPort.grantUsersAccess(kitId, expertGroupMemberIds);
        createKitTagRelationPort.persist(param.getTagIds(), kitId);

        return new Result(kitId);
    }

    private CreateAssessmentKitPort.Param toPortParam(Param param) {
        return new CreateAssessmentKitPort.Param(
            generateSlugCode(param.getTitle()),
            param.getTitle(),
            param.getSummary(),
            param.getAbout(),
            param.getLang(),
            Boolean.FALSE,
            param.getIsPrivate(),
            param.getExpertGroupId(),
            param.getCurrentUserId()
        );
    }
}
