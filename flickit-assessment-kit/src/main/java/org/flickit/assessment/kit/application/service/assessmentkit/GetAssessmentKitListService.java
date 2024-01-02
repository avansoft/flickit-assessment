package org.flickit.assessment.kit.application.service.assessmentkit;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.kit.application.port.in.assessmentkit.GetAssessmentKitListUseCase;
import org.flickit.assessment.kit.application.port.out.assessmentkit.LoadAssessmentKitListPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetAssessmentKitListService implements GetAssessmentKitListUseCase {

    private final LoadAssessmentKitListPort loadAssessmentKitListPort;

    @Override
        public PaginatedResponse<AssessmentKitListItem> getAssessmentKitList(Param param) {
        return loadAssessmentKitListPort.loadKitList(toPortParam(param));
    }

    private LoadAssessmentKitListPort.Param toPortParam(Param param) {
        return new LoadAssessmentKitListPort.Param(
            param.getIsPrivate(),
            param.getPage(),
            param.getSize(),
            param.getCurrentUserId()
        );
    }
}
