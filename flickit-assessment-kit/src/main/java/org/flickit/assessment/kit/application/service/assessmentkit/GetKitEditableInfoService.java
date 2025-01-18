package org.flickit.assessment.kit.application.service.assessmentkit;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.kit.application.domain.AssessmentKit;
import org.flickit.assessment.kit.application.domain.ExpertGroup;
import org.flickit.assessment.kit.application.domain.KitTag;
import org.flickit.assessment.kit.application.port.in.assessmentkit.GetKitEditableInfoUseCase;
import org.flickit.assessment.kit.application.port.out.assessmentkit.LoadAssessmentKitPort;
import org.flickit.assessment.kit.application.port.out.expertgroup.LoadKitExpertGroupPort;
import org.flickit.assessment.kit.application.port.out.expertgroupaccess.CheckExpertGroupAccessPort;
import org.flickit.assessment.kit.application.port.out.kittag.LoadKitTagListPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetKitEditableInfoService implements GetKitEditableInfoUseCase {

    private final LoadAssessmentKitPort loadAssessmentKitPort;
    private final LoadKitTagListPort loadKitTagListPort;
    private final CheckExpertGroupAccessPort checkExpertGroupAccessPort;
    private final LoadKitExpertGroupPort loadKitExpertGroupPort;

    @Override
    public KitEditableInfo getKitEditableInfo(Param param) {
        ExpertGroup expertGroup = loadKitExpertGroupPort.loadKitExpertGroup(param.getKitId());
        if (!checkExpertGroupAccessPort.checkIsMember(expertGroup.getId(), param.getCurrentUserId()))
            throw new AccessDeniedException(COMMON_CURRENT_USER_NOT_ALLOWED);

        AssessmentKit assessmentKit = loadAssessmentKitPort.load(param.getKitId());
        List<KitTag> tags = loadKitTagListPort.loadByKitId(param.getKitId());

        return new KitEditableInfo(
            assessmentKit.getId(),
            assessmentKit.getTitle(),
            assessmentKit.getSummary(),
            assessmentKit.getLang(),
            assessmentKit.isPublished(),
            assessmentKit.isPrivate(),
            0D,
            assessmentKit.getAbout(),
            tags,
            expertGroup.getOwnerId().equals(param.getCurrentUserId()),
            assessmentKit.getActiveVersionId() != null);
    }
}
