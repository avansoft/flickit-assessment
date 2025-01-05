package org.flickit.assessment.users.adapter.in.rest.space;

import lombok.Builder;

@Builder
public record CreateSpaceRequestDto(String title,
                                    String type) {
}
