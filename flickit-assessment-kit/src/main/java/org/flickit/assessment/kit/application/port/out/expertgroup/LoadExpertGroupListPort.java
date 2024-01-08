package org.flickit.assessment.kit.application.port.out.expertgroup;

import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;

import java.util.List;
import java.util.UUID;

public interface LoadExpertGroupListPort {

    PaginatedResponse<Result> loadExpertGroupList(Param param);

    record Param(int page, int size, UUID currentUserId) {
    }

    record Result(Long id, String title, String bio, String picture, Integer publishedKitsCount,
                               Integer membersCount, List<String> members, UUID ownerId) {
    }
}
