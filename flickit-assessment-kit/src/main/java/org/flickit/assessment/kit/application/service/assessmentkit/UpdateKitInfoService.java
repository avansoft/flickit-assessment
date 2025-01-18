package org.flickit.assessment.kit.application.service.assessmentkit;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.kit.application.port.in.assessmentkit.UpdateKitInfoUseCase;
import org.flickit.assessment.kit.application.port.out.assessmentkit.UpdateKitInfoPort;
import org.flickit.assessment.kit.application.port.out.expertgroup.LoadKitExpertGroupPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.flickit.assessment.common.util.SlugCodeUtil.generateSlugCode;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateKitInfoService implements UpdateKitInfoUseCase {

    private final LoadKitExpertGroupPort loadKitExpertGroupPort;
    private final UpdateKitInfoPort updateKitInfoPort;

    @Override
    public void updateKitInfo(Param param) {
        validateCurrentUser(param.getKitId(), param.getCurrentUserId());
        if (containsNonNullParam(param))
            updateKitInfoPort.update(toPortParam(param));

    }

    private void validateCurrentUser(Long kitId, UUID currentUserId) {
        var expertGroup = loadKitExpertGroupPort.loadKitExpertGroup(kitId);
        if (!Objects.equals(expertGroup.getOwnerId(), currentUserId))
            throw new AccessDeniedException(COMMON_CURRENT_USER_NOT_ALLOWED);

    }

    private boolean containsNonNullParam(Param param) {
        return Objects.nonNull(param.getTitle()) ||
            Objects.nonNull(param.getSummary()) ||
            Objects.nonNull(param.getLang()) ||
            Objects.nonNull(param.getPublished()) ||
            Objects.nonNull(param.getIsPrivate()) ||
            Objects.nonNull(param.getPrice()) ||
            Objects.nonNull(param.getAbout()) ||
            Objects.nonNull(param.getTags());
    }

    private UpdateKitInfoPort.Param toPortParam(Param param) {
        return new UpdateKitInfoPort.Param(
            param.getKitId(),
            param.getTitle() != null ? generateSlugCode(param.getTitle()) : null,
            param.getTitle(),
            param.getSummary(),
            param.getLang(),
            param.getPublished(),
            param.getIsPrivate(),
            param.getPrice(),
            param.getAbout(),
            param.getTags() != null ? new HashSet<>(param.getTags()) : null,
            param.getCurrentUserId(),
            LocalDateTime.now()
        );
    }
}
