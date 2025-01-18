package org.flickit.assessment.kit.application.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum KitLanguage {

    EN("en"), FA("fa");

    private final String title;

    public int getId() {
        return this.ordinal();
    }
}
