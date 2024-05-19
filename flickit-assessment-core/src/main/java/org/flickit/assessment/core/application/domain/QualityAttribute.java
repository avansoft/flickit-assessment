package org.flickit.assessment.core.application.domain;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class QualityAttribute {

    private final long id;
    private final int weight;

    /** This field is set when required (e.g., calculate) */
    @Nullable
    private final List<Question> questions;
}
