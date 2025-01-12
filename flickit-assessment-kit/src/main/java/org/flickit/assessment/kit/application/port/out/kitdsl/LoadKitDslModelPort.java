package org.flickit.assessment.kit.application.port.out.kitdsl;

import org.flickit.assessment.kit.application.domain.dsl.AssessmentKitDslModel;

public interface LoadKitDslModelPort {

    AssessmentKitDslModel load(long kitVersionId);
}
