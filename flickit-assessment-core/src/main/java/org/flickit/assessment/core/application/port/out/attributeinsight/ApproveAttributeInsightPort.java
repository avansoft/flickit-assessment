package org.flickit.assessment.core.application.port.out.attributeinsight;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ApproveAttributeInsightPort {

    void approve(UUID assessmentId, long attributeId, LocalDateTime lastModificationTime);
}
