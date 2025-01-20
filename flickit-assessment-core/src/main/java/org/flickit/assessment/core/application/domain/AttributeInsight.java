package org.flickit.assessment.core.application.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class AttributeInsight {

    private final UUID assessmentResultId;
    private final Long attributeId;
    private final String aiInsight;
    private final String assessorInsight;
    private final LocalDateTime aiInsightTime;
    private final LocalDateTime assessorInsightTime;
    private final String aiInputPath;
    private final boolean approved;
    private final LocalDateTime lastModificationTime;
}
