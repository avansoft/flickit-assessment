package org.flickit.assessment.kit.application.port.out.assessmentkit;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public interface UpdateKitInfoPort {

    void update(Param param);

    record Param(Long kitId,
                 String code,
                 String title,
                 String summary,
                 String lang,
                 Boolean published,
                 Boolean isPrivate,
                 Double price,
                 String about,
                 Set<Long> tags,
                 UUID currentUserId,
                 LocalDateTime lastModificationTime) {
    }
}
