package org.flickit.assessment.core.application.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class AssessmentReport {

    private final UUID id;
    private final UUID assessmentResultId;
    private final AssessmentReportMetadata metadata;
}