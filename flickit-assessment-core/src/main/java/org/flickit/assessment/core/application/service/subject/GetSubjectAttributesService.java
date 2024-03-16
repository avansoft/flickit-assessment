package org.flickit.assessment.core.application.service.subject;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.exception.AccessDeniedException;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.core.application.port.in.subject.GetSubjectAttributesUseCase;
import org.flickit.assessment.core.application.port.out.assessment.CheckUserAssessmentAccessPort;
import org.flickit.assessment.core.application.port.out.subject.CheckSubjectKitExistencePort;
import org.flickit.assessment.core.application.port.out.subject.LoadSubjectAttributesPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.flickit.assessment.common.error.ErrorMessageKey.COMMON_CURRENT_USER_NOT_ALLOWED;
import static org.flickit.assessment.core.common.ErrorMessageKey.GET_SUBJECT_ATTRIBUTES_SUBJECT_ID_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetSubjectAttributesService implements GetSubjectAttributesUseCase {

    private final CheckUserAssessmentAccessPort checkUserAssessmentAccessPort;
    private final CheckSubjectKitExistencePort checkSubjectKitExistencePort;
    private final LoadSubjectAttributesPort loadSubjectAttributesPort;

    @Override
    public Result getSubjectAttributes(Param param) {
        if (!checkUserAssessmentAccessPort.hasAccess(param.getAssessmentId(), param.getCurrentUserId()))
            throw new AccessDeniedException(COMMON_CURRENT_USER_NOT_ALLOWED);

        if (!checkSubjectKitExistencePort.existsByIdAndAssessmentId(param.getSubjectId(), param.getAssessmentId()))
            throw new ResourceNotFoundException(GET_SUBJECT_ATTRIBUTES_SUBJECT_ID_NOT_FOUND);

        var subjectAttributes = loadSubjectAttributesPort.loadBySubjectIdAndAssessmentId(param.getSubjectId(),
            param.getAssessmentId());
        return new Result(subjectAttributes);
    }
}
