package org.flickit.assessment.kit.application.domain;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AssessmentKit {

    private final long id;
    private final String code;
    private final String title;
    private final String summary;
    private final String about;
    private final String lang;
    private final LocalDateTime creationTime;
    private final LocalDateTime lastModificationTime;
    private final boolean published;
    private final boolean isPrivate;
    private final long expertGroupId;
    private final List<Subject> subjects;
    private final List<MaturityLevel> maturityLevels;
    private final List<Questionnaire> questionnaires;
    private final List<AnswerRange> reusableAnswerRanges;
    private final Long activeVersionId;

    /**
     * Represents the version ID of the draft. This field is not always required and may be {@code null}.
     * When a draft version is necessary, this field is set to the appropriate version ID.
     */
    @Setter
    @Nullable
    private Long draftVersionId;
}
