package org.flickit.assessment.kit.application.service.assessmentkit;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.application.domain.notification.SendNotification;
import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.kit.application.port.in.assessmentkit.ToggleKitLikeUseCase;
import org.flickit.assessment.kit.application.port.out.assessmentkit.LoadAssessmentKitPort;
import org.flickit.assessment.kit.application.port.out.kitlike.CheckKitLikeExistencePort;
import org.flickit.assessment.kit.application.port.out.kitlike.CountKitLikePort;
import org.flickit.assessment.kit.application.port.out.kitlike.CreateKitLikePort;
import org.flickit.assessment.kit.application.port.out.kitlike.DeleteKitLikePort;
import org.flickit.assessment.kit.application.port.out.kituseraccess.CheckKitUserAccessPort;
import org.flickit.assessment.kit.application.service.assessmentkit.notification.ToggleKitLikeNotificationCmd;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;

@Service
@Transactional
@RequiredArgsConstructor
public class ToggleKitLikeService implements ToggleKitLikeUseCase {

    private final LoadAssessmentKitPort loadKitPort;
    private final CheckKitUserAccessPort checkKitUserAccessPort;
    private final CheckKitLikeExistencePort checkKitLikeExistencePort;
    private final CreateKitLikePort createKitLikePort;
    private final DeleteKitLikePort deleteKitLikePort;
    private final CountKitLikePort countKitLikePort;

    @Override
    @SendNotification
    public Result toggleKitLike(Param param) {
        var kit = loadKitPort.load(param.getKitId());
        if (kit.isPrivate() && !checkKitUserAccessPort.hasAccess(param.getKitId(), param.getCurrentUserId()))
            throw new AccessDeniedException(COMMON_CURRENT_USER_NOT_ALLOWED);

        boolean liked = false;
        if (!checkKitLikeExistencePort.exist(param.getKitId(), param.getCurrentUserId())) {
            createKitLikePort.create(param.getKitId(), param.getCurrentUserId());
            liked = true;
        } else
            deleteKitLikePort.delete(param.getKitId(), param.getCurrentUserId());

        int likesCount = countKitLikePort.countByKitId(param.getKitId());
        return new Result(new ToggleKitLikeNotificationCmd(kit.getCreatedBy(),
            kit.getId(),
            param.getCurrentUserId(),
            likesCount,
            liked),
            likesCount,
            liked);
    }
}
