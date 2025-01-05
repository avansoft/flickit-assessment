package org.flickit.assessment.common.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@ToString
@Validated
@ConfigurationProperties("app.spec")
public class AppSpecProperties {

    @NotBlank
    private String host;

    @NotBlank
    private String expertGroupInviteUrlPath = "account/expert-group-invitation";

    @NotBlank
    private String name = "Flickit";

    @NotBlank
    private String logo;

    @NotBlank
    private String favIcon;

    private String supportEmail;

    @Valid
    private Email email = new Email();

    @Setter
    @Getter
    @ToString
    public static class Email {

        @NotBlank
        String fromDisplayName = "Flickit";
    }

    @Setter
    @Getter
    @ToString
    public static class Space {
        int maxPersonalSpaces = 1;
        int maxPersonalSpaceAssessments = 2;
        int maxPersonalSpaceMembers = 3;
    }
}
