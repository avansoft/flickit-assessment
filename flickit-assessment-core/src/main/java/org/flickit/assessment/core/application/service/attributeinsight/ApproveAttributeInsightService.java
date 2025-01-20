package org.flickit.assessment.core.application.service.attributeinsight;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.application.domain.assessment.AssessmentAccessChecker;
import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.core.application.port.in.attributeinsight.ApproveAttributeInsightUseCase;
import org.flickit.assessment.core.application.port.out.attributeinsight.ApproveAttributeInsightPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.flickit.assessment.common.application.domain.assessment.AssessmentPermission.APPROVE_ATTRIBUTE_INSIGHT;
import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;

@Service
@Transactional
@RequiredArgsConstructor
public class ApproveAttributeInsightService implements ApproveAttributeInsightUseCase {

    private final ApproveAttributeInsightPort approveAttributeInsightPort;
    private final AssessmentAccessChecker assessmentAccessChecker;

    @Override
    public void approveAttributeInsight(Param param) {
        UUID assessmentId = param.getAssessmentId();
        if (!assessmentAccessChecker.isAuthorized(assessmentId, param.getCurrentUserId(), APPROVE_ATTRIBUTE_INSIGHT))
            throw new AccessDeniedException(COMMON_CURRENT_USER_NOT_ALLOWED);

        approveAttributeInsightPort.approve(assessmentId, param.getAttributeId(), LocalDateTime.now());
    }
}
