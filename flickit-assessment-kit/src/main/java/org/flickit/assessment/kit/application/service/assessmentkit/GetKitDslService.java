package org.flickit.assessment.kit.application.service.assessmentkit;

import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.common.exception.ValidationException;
import org.flickit.assessment.kit.application.domain.AssessmentKit;
import org.flickit.assessment.kit.application.domain.dsl.AssessmentKitDslModel;
import org.flickit.assessment.kit.application.port.in.assessmentkit.GetKitDslUseCase;
import org.flickit.assessment.kit.application.port.out.assessmentkit.LoadAssessmentKitPort;
import org.flickit.assessment.kit.application.port.out.expertgroupaccess.CheckExpertGroupAccessPort;
import org.flickit.assessment.kit.application.port.out.kitdsl.LoadKitDslModelPort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.flickit.assessment.kit.common.ErrorMessageKey.GET_KIT_DSL_NOT_AVAILABLE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetKitDslService implements GetKitDslUseCase {

    private final LoadAssessmentKitPort loadAssessmentKitPort;
    private final CheckExpertGroupAccessPort checkExpertGroupAccessPort;
    private final LoadKitDslModelPort loadKitDslModelPort;

    @Override
    public AssessmentKitDslModel getKitDsl(Param param) {
        AssessmentKit kit = loadAssessmentKitPort.load(param.getKitId());
        if (!checkExpertGroupAccessPort.checkIsMember(kit.getExpertGroupId(), param.getCurrentUserId()))
            throw new AccessDeniedException(COMMON_CURRENT_USER_NOT_ALLOWED);

        Long activeVersionId = kit.getActiveVersionId();
        if (activeVersionId == null)
            throw new ValidationException(GET_KIT_DSL_NOT_AVAILABLE);

        return loadKitDslModelPort.load(activeVersionId);
    }
}
