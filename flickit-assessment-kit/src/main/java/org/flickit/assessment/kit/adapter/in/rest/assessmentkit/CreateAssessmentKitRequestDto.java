package org.flickit.assessment.kit.adapter.in.rest.assessmentkit;

import java.util.List;

public record CreateAssessmentKitRequestDto(String title,
                                            String summary,
                                            String about,
                                            String lang,
                                            Boolean isPrivate,
                                            Long expertGroupId,
                                            List<Long> tagIds) {
}
