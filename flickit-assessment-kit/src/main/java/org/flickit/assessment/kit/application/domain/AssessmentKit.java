package org.flickit.assessment.kit.application.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class AssessmentKit {

    private final long id;
    private final String code;
    private final String title;
    private final String summary;
    private final String about;
    private final LocalDateTime creationTime;
    private final LocalDateTime lastModificationTime;
    private final UUID createdBy;
    private final boolean published;
    private final boolean isPrivate;
    private final long expertGroupId;
    private final List<Subject> subjects;
    private final List<MaturityLevel> maturityLevels;
    private final List<Questionnaire> questionnaires;
    private final Long kitVersionId;


    public static String generateSlugCode(String title) {
        return title
            .toLowerCase()
            .strip()
            .replaceAll("\\s+", "-");
    }
}
