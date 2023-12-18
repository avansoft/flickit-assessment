package org.flickit.assessment.kit.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessageKey {

    public static final String COMMON_CURRENT_USER_NOT_ALLOWED = "common.currentUser.notAllowed";

    public static final String TRANSLATE_KIT_DSL_UNABLE_TO_PARSE_JSON = "translate-kit-dsl.unable.to.parse.json";

    public static final String FIND_MATURITY_LEVEL_ID_NOT_FOUND = "find-maturity-level.id.notFound";
    public static final String FIND_KIT_ID_NOT_FOUND = "find-kit.id.notFound";

    public static final String UPDATE_KIT_BY_DSL_KIT_ID_NOT_NULL = "update-kit-by-dsl.kitId.notNull";
    public static final String UPDATE_KIT_BY_DSL_DSL_CONTENT_NOT_NULL = "update-kit-by-dsl.dsl-content.notNull";

    public static final String UPDATE_KIT_BY_DSL_ADDITION_UNSUPPORTED = "update-kit-by-dsl.addition.unsupported";
    public static final String UPDATE_KIT_BY_DSL_DELETION_UNSUPPORTED = "update-kit-by-dsl.deletion.unsupported";

    public static final String UPDATE_KIT_BY_DSL_ANSWER_OPTION_NOT_FOUND= "update-kit-by-dsl.answer-option.notFound";

    public static final String GRANT_USER_ACCESS_TO_KIT_KIT_ID_NOT_NULL = "grant-user-access-to-kit.kitId.notNull";
    public static final String GRANT_USER_ACCESS_TO_KIT_EMAIL_NOT_NULL = "grant-user-access-to-kit.email.notNull";
    public static final String GRANT_USER_ACCESS_TO_KIT_CURRENT_USER_ID_NOT_NULL = "grant-user-access-to-kit.currentUserId.notNull";
    public static final String GRANT_USER_ACCESS_TO_KIT_KIT_ID_NOT_FOUND = "grant-user-access-to-kit.kitId.notFound";
    public static final String GRANT_USER_ACCESS_TO_KIT_EXPERT_GROUP_OWNER_NOT_FOUND = "grant-user-access-to-kit.expertGroupOwner.notFound";
    public static final String GRANT_USER_ACCESS_TO_KIT_EMAIL_NOT_FOUND = "grant-user-access-to-kit.email.notFound";

    public static final String DELETE_USER_ACCESS_KIT_ID_NOT_NULL = "delete-user-access.kitId.notNull";
    public static final String DELETE_USER_ACCESS_EMAIL_NOT_NULL = "delete-user-access.email.notNull";
    public static final String DELETE_USER_ACCESS_CURRENT_USER_ID_NOT_NULL = "delete-user-access.currentUserId.notNull";
    public static final String DELETE_USER_ACCESS_KIT_NOT_FOUND = "delete-user-access.kit.notFound";
    public static final String DELETE_USER_ACCESS_USER_NOT_FOUND = "delete-user-access.user.notFound";
    public static final String DELETE_USER_ACCESS_KIT_USER_NOT_FOUND = "delete-user-access.kit-user.notFound";
    public static final String DELETE_USER_ACCESS_KIT_ID_NOT_FOUND = "delete-user-access.kitId.notFound";
    public static final String DELETE_USER_ACCESS_EXPERT_GROUP_OWNER_NOT_FOUND = "delete-user-access.expertGroupOwner.notFound";

    public static String entityNameSingleFirst(String fieldName) {
        return String.format("entities.%s.single.first", fieldName);
    }

    public static String entityNamePlural(String fieldName) {
        return String.format("entities.%s.plural", fieldName);
    }

}
