package org.flickit.assessment.core.application.service.attributeinsight;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.application.domain.assessment.AssessmentAccessChecker;
import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.core.application.domain.AttributeInsight;
import org.flickit.assessment.core.application.port.in.attributeinsight.CreateAttributeInsightUseCase;
import org.flickit.assessment.core.application.port.out.assessmentresult.LoadAssessmentResultPort;
import org.flickit.assessment.core.application.port.out.attributeinsight.CreateAttributeInsightPort;
import org.flickit.assessment.core.application.port.out.attributeinsight.LoadAttributeInsightPort;
import org.flickit.assessment.core.application.port.out.attributeinsight.UpdateAttributeInsightPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.flickit.assessment.common.application.domain.assessment.AssessmentPermission.CREATE_ATTRIBUTE_INSIGHT;
import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.flickit.assessment.core.common.ErrorMessageKey.CREATE_ATTRIBUTE_INSIGHT_ASSESSMENT_RESULT_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateAttributeInsightService implements CreateAttributeInsightUseCase {

    private final AssessmentAccessChecker assessmentAccessChecker;
    private final LoadAssessmentResultPort loadAssessmentResultPort;
    private final LoadAttributeInsightPort loadAttributeInsightPort;
    private final UpdateAttributeInsightPort updateAttributeInsightPort;
    private final CreateAttributeInsightPort createAttributeInsightPort;

    @Override
    public void createAttributeInsight(Param param) {
        if (!assessmentAccessChecker.isAuthorized(param.getAssessmentId(), param.getCurrentUserId(), CREATE_ATTRIBUTE_INSIGHT))
            throw new AccessDeniedException(COMMON_CURRENT_USER_NOT_ALLOWED);

        var assessmentResult = loadAssessmentResultPort.loadByAssessmentId(param.getAssessmentId())
            .orElseThrow(() -> new ResourceNotFoundException(CREATE_ATTRIBUTE_INSIGHT_ASSESSMENT_RESULT_NOT_FOUND));

        var attributeInsight = loadAttributeInsightPort.load(assessmentResult.getId(), param.getAttributeId());

        if (attributeInsight.isPresent())
            updateAttributeInsightPort.updateAssessorInsight(toUpdateParam(assessmentResult.getId(), param.getAttributeId(), param.getAssessorInsight()));
        else
            createAttributeInsightPort.persist(toAttributeInsight(assessmentResult.getId(), param.getAttributeId(), param.getAssessorInsight()));
    }

    private static UpdateAttributeInsightPort.AssessorParam toUpdateParam(UUID assessmentResultId, long attributeId, String assessorInsight) {
        return new UpdateAttributeInsightPort.AssessorParam(assessmentResultId,
            attributeId,
            assessorInsight,
            LocalDateTime.now(),
            true,
            LocalDateTime.now());
    }

    private static AttributeInsight toAttributeInsight(UUID assessmentResultId, long attributeId, String assessorInsight) {
        return new AttributeInsight(assessmentResultId,
            attributeId,
            null,
            assessorInsight,
            null,
            LocalDateTime.now(),
            null,
            true,
            LocalDateTime.now());
    }
}
