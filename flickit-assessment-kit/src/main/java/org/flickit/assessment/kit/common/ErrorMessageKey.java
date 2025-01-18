package org.flickit.assessment.kit.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessageKey {

    public static final String TRANSLATE_KIT_DSL_UNABLE_TO_PARSE_JSON = "translate-kit-dsl.unable.to.parse.json";

    public static final String KIT_ID_NOT_FOUND = "kit.id.notFound";
    public static final String EXPERT_GROUP_ID_NOT_FOUND = "expert-group.id.notFound";
    public static final String QUESTIONNAIRE_ID_NOT_FOUND = "questionnaire.id.notFound";
    public static final String QUESTION_ID_NOT_FOUND = "question.id.notFound";
    public static final String ATTRIBUTE_ID_NOT_FOUND = "attribute.id.notFound";
    public static final String MATURITY_LEVEL_ID_NOT_FOUND = "maturity-level.id.notFound";
    public static final String KIT_VERSION_ID_NOT_FOUND = "kit-version.id.notFound";
    public static final String LEVEL_COMPETENCE_ID_NOT_FOUND = "level-competence.id.notFound";
    public static final String SUBJECT_ID_NOT_FOUND = "subject-id.notFound";
    public static final String QUESTION_IMPACT_ID_NOT_FOUND = "question-impact.id.notFound";
    public static final String ANSWER_OPTION_ID_NOT_FOUND = "answer-option.id.notFound";
    public static final String ANSWER_RANGE_ID_NOT_FOUND = "answer-range.id.notFound";
    public static final String KIT_CUSTOM_ID_NOT_FOUND = "kit-custom.id.notFound";

    public static final String UPDATE_KIT_BY_DSL_KIT_ID_NOT_NULL = "update-kit-by-dsl.kitId.notNull";
    public static final String UPDATE_KIT_BY_DSL_KIT_DSL_ID_NOT_NULL = "update-kit-by-dsl.kit-dsl-id.notNull";
    public static final String UPDATE_KIT_BY_DSL_ADDITION_UNSUPPORTED = "update-kit-by-dsl.addition.unsupported";
    public static final String UPDATE_KIT_BY_DSL_DELETION_UNSUPPORTED = "update-kit-by-dsl.deletion.unsupported";
    public static final String UPDATE_KIT_BY_DSL_CHANGE_UNSUPPORTED = "update-kit-by-dsl.change.unsupported";

    public static final String GET_KIT_DSL_DOWNLOAD_LINK_FILE_PATH_NOT_FOUND = "get-kit-dsl-download-link.filePath.notFound";
    public static final String GET_KIT_DSL_DOWNLOAD_LINK_KIT_ID_NOT_NULL =  "get-kit-dsl-download-link.kitId.notNull";

    public static final String GRANT_USER_ACCESS_TO_KIT_KIT_ID_NOT_NULL = "grant-user-access-to-kit.kitId.notNull";
    public static final String GRANT_USER_ACCESS_TO_KIT_USER_ID_NOT_NULL = "grant-user-access-to-kit.userId.notNull";
    public static final String GRANT_USER_ACCESS_TO_KIT_KIT_ID_NOT_FOUND = "grant-user-access-to-kit.kitId.notFound";
    public static final String GRANT_USER_ACCESS_TO_KIT_USER_ID_NOT_FOUND = "grant-user-access-to-kit.userId.notFound";
    public static final String GRANT_USER_ACCESS_TO_KIT_USER_ID_DUPLICATE = "grant-user-access-to-kit.userId.duplicate";

    public static final String GET_KIT_USER_LIST_KIT_ID_NOT_NULL = "get-kit-user-list.kit-id.notNull";
    public static final String GET_KIT_USER_LIST_PAGE_MIN = "get-kit-user-list.page.min";
    public static final String GET_KIT_USER_LIST_SIZE_MIN = "get-kit-user-list.size.min";
    public static final String GET_KIT_USER_LIST_SIZE_MAX = "get-kit-user-list.size.max";

    public static final String GET_KIT_TAG_LIST_PAGE_MIN = "get-kit-tag-list.page.min";
    public static final String GET_KIT_TAG_LIST_SIZE_MIN = "get-kit-tag-list.size.min";
    public static final String GET_KIT_TAG_LIST_SIZE_MAX = "get-kit-tag-list.size.max";

    public static final String GET_KIT_LIST_IS_PRIVATE_NOT_NULL = "get-kit-list.isPrivate.notNull";
    public static final String GET_KIT_LIST_PAGE_MIN = "get-kit-list.page.min";
    public static final String GET_KIT_LIST_SIZE_MIN = "get-kit-list.size.min";
    public static final String GET_KIT_LIST_SIZE_MAX = "get-kit-list.size.max";

    public static final String SEARCH_KIT_OPTIONS_PAGE_MIN = "search-kit-options.page.min";
    public static final String SEARCH_KIT_OPTION_SIZE_MIN = "search-kit-options.size.min";
    public static final String SEARCH_KIT_OPTIONS_SIZE_MAX = "search-kit-options.size.max";

    public static final String GET_KIT_MINIMAL_INFO_KIT_ID_NOT_NULL = "get-kit-minimal-info.kitId.notNull";
    public static final String GET_KIT_MINIMAL_INFO_KIT_ID_NOT_FOUND = "get-kit-minimal-info.kitId.notFound";

    public static final String DELETE_KIT_USER_ACCESS_KIT_ID_NOT_NULL = "delete-kit-user-access.kitId.notNull";
    public static final String DELETE_KIT_USER_ACCESS_USER_ID_NOT_NULL = "delete-kit-user-access.userId.notNull";
    public static final String DELETE_KIT_USER_ACCESS_KIT_USER_NOT_FOUND = "delete-kit-user-access.kit-user.notFound";
    public static final String DELETE_KIT_USER_ACCESS_KIT_ID_NOT_FOUND = "delete-kit-user-access.kitId.notFound";
    public static final String DELETE_KIT_USER_ACCESS_USER_NOT_FOUND = "delete-kit-user-access.user.notFound";
    public static final String DELETE_KIT_USER_ACCESS_USER_IS_EXPERT_GROUP_OWNER = "delete-kit-user-access.userId.isExpertGroupOwner";

    public static final String DELETE_KIT_KIT_ID_NOT_NULL = "delete-kit.kitId.notNull";
    public static final String DELETE_KIT_HAS_ASSESSMENT = "delete-kit.hasAssessment";

    public static final String UPLOAD_KIT_DSL_KIT_NOT_NULL = "upload-kit.dsl-file.notNull";
    public static final String UPLOAD_KIT_DSL_EXPERT_GROUP_ID_NOT_NULL = "upload-kit.expert-group-id.notNull";
    public static final String UPLOAD_KIT_DSL_DSL_HAS_ERROR = "upload-kit.dsl.has-error";

    public static final String CREATE_KIT_BY_DSL_KIT_DSL_ID_NOT_NULL = "create-kit-by-dsl.kit-dsl-id.notNull";
    public static final String CREATE_KIT_BY_DSL_KIT_DSL_NOT_FOUND = "create-kit-by-dsl.kit-dsl.notFound";
    public static final String CREATE_KIT_BY_DSL_TITLE_NOT_NULL = "create-kit-by-dsl.title.notNull";
    public static final String CREATE_KIT_BY_DSL_TITLE_SIZE_MIN = "create-kit-by-dsl.title.size.min";
    public static final String CREATE_KIT_BY_DSL_TITLE_SIZE_MAX = "create-kit-by-dsl.title.size.max";
    public static final String CREATE_KIT_BY_DSL_KIT_TITLE_DUPLICATE = "create-kit-by-dsl.title.duplicate";
    public static final String CREATE_KIT_BY_DSL_SUMMARY_NOT_NULL = "create-kit-by-dsl.summary.notNull";
    public static final String CREATE_KIT_BY_DSL_SUMMARY_SIZE_MIN = "create-kit-by-dsl.summary.size.min";
    public static final String CREATE_KIT_BY_DSL_SUMMARY_SIZE_MAX = "create-kit-by-dsl.summary.size.max";
    public static final String CREATE_KIT_BY_DSL_ABOUT_NOT_NULL = "create-kit-by-dsl.about.notNull";
    public static final String CREATE_KIT_BY_DSL_ABOUT_SIZE_MIN = "create-kit-by-dsl.about.size.min";
    public static final String CREATE_KIT_BY_DSL_ABOUT_SIZE_MAX = "create-kit-by-dsl.about.size.max";
    public static final String CREATE_KIT_BY_DSL_IS_PRIVATE_NOT_NULL = "create-kit-by-dsl.isPrivate.notNull";
    public static final String CREATE_KIT_BY_DSL_TAG_IDS_NOT_NULL = "create-kit-by-dsl.tag-ids.notNull";
    public static final String CREATE_KIT_BY_DSL_EXPERT_GROUP_ID_NOT_NULL = "create-kit-by-dsl.expert-group-id.notNull";

    public static final String GET_KIT_STATS_KIT_ID_NOT_NULL = "get-kit-stats.kitId.notNull";

    public static final String GET_KIT_EDITABLE_INFO_KIT_ID_NOT_NULL = "get-kit-editable-info.kitId.notNull";

    public static final String UPDATE_KIT_INFO_KIT_ID_NOT_NULL = "update-kit-info.kitId.notNull";
    public static final String UPDATE_KIT_INFO_KIT_ID_NOT_FOUND = "update-kit-info.kitId.notFound";
    public static final String UPDATE_KIT_INFO_TITLE_SIZE_MIN = "update-kit-info.title.size.min";
    public static final String UPDATE_KIT_INFO_TITLE_SIZE_MAX = "update-kit-info.title.size.max";
    public static final String UPDATE_KIT_INFO_SUMMARY_SIZE_MIN = "update-kit-info.summary.size.min";
    public static final String UPDATE_KIT_INFO_SUMMARY_SIZE_MAX = "update-kit-info.summary.size.max";
    public static final String UPDATE_KIT_INFO_ABOUT_SIZE_MIN = "update-kit-info.about.size.min";
    public static final String UPDATE_KIT_INFO_ABOUT_SIZE_MAX = "update-kit-info.about.size.max";
    public static final String UPDATE_KIT_INFO_TAGS_SIZE_MIN = "update-kit-info.tags.size.min";
    public static final String UPDATE_KIT_INFO_TAG_ID_NOT_FOUND = "update-kit-info.tagId.notFound";
    public static final String UPDATE_KIT_INFO_KIT_LANGUAGE_INVALID = "update-kit-info.kitLanguage.invalid";

    public static final String GET_KIT_DETAIL_KIT_ID_NOT_NULL = "get-kit-detail.kitId.notNull";

    public static final String GET_KIT_SUBJECT_DETAIL_KIT_ID_NOT_NULL = "get-kit-subject-detail.kitId.notNull";
    public static final String GET_KIT_SUBJECT_DETAIL_SUBJECT_ID_NOT_NULL = "get-kit-subject-detail.subjectId.notNull";
    public static final String GET_KIT_SUBJECT_DETAIL_SUBJECT_ID_NOT_FOUND = "get-kit-subject-detail.subjectId.notFound";

    public static final String GET_KIT_ATTRIBUTE_DETAIL_KIT_ID_NOT_NULL = "get-kit-attribute-detail.kitId.notNull";
    public static final String GET_KIT_ATTRIBUTE_DETAIL_ATTRIBUTE_ID_NOT_NULL = "get-kit-attribute-detail.attributeId.notNull";
    public static final String GET_KIT_ATTRIBUTE_DETAIL_ATTRIBUTE_ID_NOT_FOUND = "get-kit-attribute-detail.attributeId.notFound";

    public static final String GET_KIT_QUESTIONNAIRE_KIT_ID_NOT_NULL = "get-kit-questionnaire.kitId.notNull";
    public static final String GET_KIT_QUESTIONNAIRE_QUESTIONNAIRE_ID_NOT_NULL = "get-kit-questionnaire.questionnaireId.notNull";

    public static final String GET_KIT_QUESTION_DETAIL_KIT_ID_NOT_NULL = "get-kit-question-detail.kitId.notNull";
    public static final String GET_KIT_QUESTION_DETAIL_QUESTION_ID_NOT_NULL = "get-kit-question-detail.questionId.notNull";

    public static final String GET_ATTRIBUTE_LEVEL_QUESTIONS_KIT_ID_NOT_NULL = "get-attribute-level-questions.kitId.notNull";
    public static final String GET_ATTRIBUTE_LEVEL_QUESTIONS_ATTRIBUTE_ID_NOT_NULL = "get-attribute-level-questions.attributeId.notNull";
    public static final String GET_ATTRIBUTE_LEVEL_QUESTIONS_MATURITY_LEVEL_ID_NOT_NULL = "get-attribute-level-questions.maturityLevel.notNull";

    public static final String TOGGLE_KIT_LIKE_KIT_ID_NOT_NULL = "toggle-kit-like.kitId.notNull";

    public static final String GET_EXPERT_GROUP_KIT_LIST_EXPERT_GROUP_ID_NOT_NULL = "get-expert-group-kit-list.expertGroupId.notNull";
    public static final String GET_EXPERT_GROUP_KIT_LIST_PAGE_MIN = "get-expert-group-kit-list.page.min";
    public static final String GET_EXPERT_GROUP_KIT_LIST_SIZE_MIN = "get-expert-group-kit-list.size.min";
    public static final String GET_EXPERT_GROUP_KIT_LIST_SIZE_MAX = "get-expert-group-kit-list.size.max";

    public static final String GET_PUBLISHED_KIT_KIT_ID_NOT_NULL = "get-published-kit.kitId.notNull";

    public static final String CREATE_SUBJECT_KIT_VERSION_ID_NOT_NULL = "create-subject.kitVersionId.notNull";
    public static final String CREATE_SUBJECT_INDEX_NOT_NULL = "create-subject.index.notNull";
    public static final String CREATE_SUBJECT_TITLE_NOT_NULL = "create-subject.title.notNull";
    public static final String CREATE_SUBJECT_TITLE_SIZE_MIN = "create-subject.title.size.min";
    public static final String CREATE_SUBJECT_TITLE_SIZE_MAX = "create-subject.title.size.max";
    public static final String CREATE_SUBJECT_DESCRIPTION_NOT_NULL = "create-subject.description.notNull";
    public static final String CREATE_SUBJECT_DESCRIPTION_SIZE_MIN = "create-subject.description.size.min";
    public static final String CREATE_SUBJECT_DESCRIPTION_SIZE_MAX = "create-subject.description.size.max";
    public static final String CREATE_SUBJECT_WEIGHT_NOT_NULL = "create-subject.weight.notNull";
    public static final String CREATE_SUBJECT_INDEX_DUPLICATE = "create-subject.index.duplicate";
    public static final String CREATE_SUBJECT_TITLE_DUPLICATE = "create-subject.title.duplicate";
    public static final String CREATE_SUBJECT_CODE_DUPLICATE = "create-subject-code.duplicate";

    public static final String CREATE_ATTRIBUTE_KIT_VERSION_ID_NOT_NULL = "create-attribute.kitVersionId.notNull";
    public static final String CREATE_ATTRIBUTE_INDEX_NOT_NULL = "create-attribute.index.notNull";
    public static final String CREATE_ATTRIBUTE_TITLE_NOT_NULL = "create-attribute.title.notNull";
    public static final String CREATE_ATTRIBUTE_TITLE_MIN_SIZE = "create-attribute.title.min.size";
    public static final String CREATE_ATTRIBUTE_TITLE_MAX_SIZE = "create-attribute.title.max.size";
    public static final String CREATE_ATTRIBUTE_DESCRIPTION_NOT_NULL = "create-attribute.description.notNull";
    public static final String CREATE_ATTRIBUTE_DESCRIPTION_SIZE_MIN = "create-attribute.description.size.min";
    public static final String CREATE_ATTRIBUTE_DESCRIPTION_SIZE_MAX = "create-attribute.description.size.max";
    public static final String CREATE_ATTRIBUTE_WEIGHT_NOT_NULL = "create-attribute.weight.notNull";
    public static final String CREATE_ATTRIBUTE_SUBJECT_ID_NOT_NULL = "create-attribute.subjectId.notNull";
    public static final String CREATE_ATTRIBUTE_CODE_DUPLICATE = "create-attribute.code.duplicate";
    public static final String CREATE_ATTRIBUTE_INDEX_DUPLICATE = "create-attribute.index.duplicate";
    public static final String CREATE_ATTRIBUTE_SUBJECT_ID_NOT_FOUND = "create-attribute.subjectId.notFound";

    public static final String CREATE_MATURITY_LEVEL_KIT_VERSION_ID_NOT_NULL = "create-maturity-level.kitVersionId.notNull";
    public static final String CREATE_MATURITY_LEVEL_INDEX_NOT_NULL = "create-maturity-level.index.notNull";
    public static final String CREATE_MATURITY_LEVEL_TITLE_NOT_NULL = "create-maturity-level.title.notNull";
    public static final String CREATE_MATURITY_LEVEL_TITLE_SIZE_MIN = "create-maturity-level.title.size.min";
    public static final String CREATE_MATURITY_LEVEL_TITLE_SIZE_MAX = "create-maturity-level.title.size.max";
    public static final String CREATE_MATURITY_LEVEL_DESCRIPTION_NOT_NULL = "create-maturity-level.description.notNull";
    public static final String CREATE_MATURITY_LEVEL_DESCRIPTION_SIZE_MIN = "create-maturity-level.description.size.min";
    public static final String CREATE_MATURITY_LEVEL_DESCRIPTION_SIZE_MAX = "create-maturity-level.description.size.max";
    public static final String CREATE_MATURITY_LEVEL_VALUE_NOT_NULL = "create-maturity-level.value.notNull";
    public static final String CREATE_MATURITY_LEVEL_CODE_DUPLICATE = "create-maturity-level.code.duplicate";
    public static final String CREATE_MATURITY_LEVEL_INDEX_DUPLICATE = "create-maturity-level.index.duplicate";
    public static final String CREATE_MATURITY_LEVEL_TITLE_DUPLICATE = "create-maturity-level.title.duplicate";
    public static final String CREATE_MATURITY_LEVEL_VALUE_DUPLICATE = "create-maturity-level.value.duplicate";

    public static final String CREATE_QUESTIONNAIRE_KIT_VERSION_ID_NOT_NULL = "create-questionnaire.kitVersionId.notNull";
    public static final String CREATE_QUESTIONNAIRE_INDEX_NOT_NULL = "create-questionnaire.index.notNull";
    public static final String CREATE_QUESTIONNAIRE_TITLE_NOT_NULL = "create-questionnaire.title.notNull";
    public static final String CREATE_QUESTIONNAIRE_TITLE_SIZE_MIN = "create-questionnaire.title.size.min";
    public static final String CREATE_QUESTIONNAIRE_TITLE_SIZE_MAX = "create-questionnaire.title.size.max";
    public static final String CREATE_QUESTIONNAIRE_DESCRIPTION_NOT_NULL = "create-questionnaire.description.notNull";
    public static final String CREATE_QUESTIONNAIRE_DESCRIPTION_SIZE_MIN = "create-questionnaire.description.size.min";
    public static final String CREATE_QUESTIONNAIRE_DESCRIPTION_SIZE_MAX = "create-questionnaire.description.size.max";
    public static final String CREATE_QUESTIONNAIRE_INDEX_DUPLICATE = "create-questionnaire.index.duplicate";
    public static final String CREATE_QUESTIONNAIRE_TITLE_DUPLICATE = "create-questionnaire.title.duplicate";

    public static final String UPDATE_MATURITY_LEVEL_MATURITY_LEVEL_ID_NOT_NULL = "update-maturity-level.maturityLevelId.notNull";
    public static final String UPDATE_MATURITY_LEVEL_KIT_VERSION_ID_NOT_NULL = "update-maturity-level.kitVersionId.notNull";
    public static final String UPDATE_MATURITY_LEVEL_TITLE_NOT_NULL = "update-maturity-level.title.notNull";
    public static final String UPDATE_MATURITY_LEVEL_TITLE_SIZE_MIN = "update-maturity-level.title.size.min";
    public static final String UPDATE_MATURITY_LEVEL_TITLE_SIZE_MAX = "update-maturity-level.title.size.max";
    public static final String UPDATE_MATURITY_LEVEL_INDEX_NOT_NULL = "update-maturity-level.index.notNull";
    public static final String UPDATE_MATURITY_LEVEL_VALUE_NOT_NULL = "update-maturity-level.value.notNull";
    public static final String UPDATE_MATURITY_LEVEL_DESCRIPTION_NOT_NULL = "update-maturity-level.description.notNull";
    public static final String UPDATE_MATURITY_LEVEL_DESCRIPTION_SIZE_MIN = "update-maturity-level.description.size.min";
    public static final String UPDATE_MATURITY_LEVEL_DESCRIPTION_SIZE_MAX = "update-maturity-level.description.size.max";

    public static final String CREATE_LEVEL_COMPETENCE_KIT_VERSION_ID_NOT_NULL = "create-level-competence.kitVersionId.notNull";
    public static final String CREATE_LEVEL_COMPETENCE_AFFECTED_LEVEL_ID_NOT_NULL = "create-level-competence.affectedLevelId.notNull";
    public static final String CREATE_LEVEL_COMPETENCE_EFFECTIVE_LEVEL_ID_NOT_NULL = "create-level-competence.effectiveLevelId.notNull";
    public static final String CREATE_LEVEL_COMPETENCE_VALUE_NOT_NULL = "create-level-competence.value.notNull";
    public static final String CREATE_LEVEL_COMPETENCE_VALUE_MIN = "create-level-competence.value.min";
    public static final String CREATE_LEVEL_COMPETENCE_VALUE_MAX = "create-level-competence.value.max";
    public static final String CREATE_LEVEL_COMPETENCE_DUPLICATE = "create-level-competence.duplicate";

    public static final String UPDATE_LEVEL_COMPETENCE_LEVEL_COMPETENCE_ID_NOT_NULL = "update-level-competence.levelCompetenceId.notNull";
    public static final String UPDATE_LEVEL_COMPETENCE_KIT_VERSION_ID_NOT_NULL = "update-level-competence.kitVersionId.notNull";
    public static final String UPDATE_LEVEL_COMPETENCE_VALUE_NOT_NULL = "update-level-competence.value.notNull";
    public static final String UPDATE_LEVEL_COMPETENCE_VALUE_MIN = "update-level-competence.value.min";
    public static final String UPDATE_LEVEL_COMPETENCE_VALUE_MAX = "update-level-competence.value.max";

    public static final String DELETE_MATURITY_LEVEL_MATURITY_LEVEL_ID_NOT_NULL = "delete-maturity-level.maturityLevelId.notNull";
    public static final String DELETE_MATURITY_LEVEL_KIT_VERSION_ID_NOT_NULL = "delete-maturity-level.kitVersionId.notNull";

    public static final String DELETE_LEVEL_COMPETENCE_KIT_VERSION_ID_NOT_NULL = "delete-level-competence.kitVersionId.notNull";
    public static final String DELETE_LEVEL_COMPETENCE_LEVEL_COMPETENCE_ID_NOT_NULL = "delete-level-competence.levelCompetenceId.notNull";
    public static final String DELETE_LEVEL_COMPETENCE_ID_NOT_FOUND = "delete-level-competence.id.notFound";

    public static final String UPDATE_MATURITY_LEVEL_ORDERS_KIT_VERSION_ID_NOT_NULL = "update-maturity-level-orders.kitVersionId.notNull";
    public static final String UPDATE_MATURITY_LEVEL_ORDERS_ORDERS_NOT_NULL = "update-maturity-level-orders.orders.notNull";
    public static final String UPDATE_MATURITY_LEVEL_ORDERS_ORDERS_SIZE_MIN = "update-maturity-level-orders.size.min";
    public static final String UPDATE_MATURITY_LEVEL_ORDERS_MATURITY_LEVEL_ID_NOT_NULL = "update-maturity-level-orders.maturityLevelId.notNull";
    public static final String UPDATE_MATURITY_LEVEL_ORDERS_MATURITY_LEVEL_INDEX_NOT_NULL = "update-maturity-level-orders.maturityLevel.index.notNull";
    public static final String UPDATE_MATURITY_LEVEL_ORDERS_MATURITY_LEVEL_INDEX_MIN = "update-maturity-level-orders.maturityLevel.index.min";

    public static final String CREATE_ASSESSMENT_KIT_TITLE_NOT_NULL = "create-assessment-kit.title.notNull";
    public static final String CREATE_ASSESSMENT_KIT_TITLE_SIZE_MIN = "create-assessment-kit.title.min";
    public static final String CREATE_ASSESSMENT_KIT_TITLE_SIZE_MAX = "create-assessment-kit.title.max";
    public static final String CREATE_ASSESSMENT_KIT_SUMMARY_NOT_NULL = "create-assessment-kit.summary.notNull";
    public static final String CREATE_ASSESSMENT_KIT_SUMMARY_SIZE_MIN = "create-assessment-kit.summary.size.min";
    public static final String CREATE_ASSESSMENT_KIT_SUMMARY_SIZE_MAX = "create-assessment-kit.summary.size.max";
    public static final String CREATE_ASSESSMENT_KIT_ABOUT_NOT_NULL = "create-assessment-kit.about.notNull";
    public static final String CREATE_ASSESSMENT_KIT_ABOUT_SIZE_MIN = "create-assessment-kit.about.size.min";
    public static final String CREATE_ASSESSMENT_KIT_ABOUT_SIZE_MAX = "create-assessment-kit.about.size.max";
    public static final String CREATE_ASSESSMENT_KIT_EXPERT_GROUP_ID_NOT_NULL = "create-assessment-kit.expertGroupId.notNull";
    public static final String CREATE_ASSESSMENT_KIT_IS_PRIVATE_NOT_NULL = "create-assessment-kit.isPrivate.notNull";
    public static final String CREATE_ASSESSMENT_KIT_TAG_IDS_NOT_NULL = "create-assessment-kit.tagIds.notNull";
    public static final String CREATE_ASSESSMENT_KIT_STATUS_DUPLICATE = "create-assessment-kit.kitStatus.duplicate";

    public static final String ACTIVATE_KIT_VERSION_KIT_VERSION_ID_NOT_NULL = "activate-kit-version.kitVersionId.notNull";
    public static final String ACTIVATE_KIT_VERSION_STATUS_INVALID = "activate-kit-version.status.invalid";
    public static final String ACTIVATE_KIT_VERSION_INVALID = "activate-kit-version.invalid";

    public static final String GET_KIT_MATURITY_LEVELS_KIT_VERSION_ID_NOT_NULL = "get-kit-maturity-levels-kitVersionId-notNull";
    public static final String GET_KIT_MATURITY_LEVELS_SIZE_MIN = "get-kit-maturity-levels-size-min";
    public static final String GET_KIT_MATURITY_LEVELS_SIZE_MAX = "get-kit-maturity-levels-size-max";
    public static final String GET_KIT_MATURITY_LEVELS_PAGE_MIN = "get-kit-maturity-levels-page-min";

    public static final String GET_LEVEL_COMPETENCES_KIT_VERSION_ID_NOT_NULL = "get-level-competences.kitVersionId.notNull";

    public static final String DELETE_SUBJECT_SUBJECT_ID_NOT_NULL = "delete-subject.id.notNull";
    public static final String DELETE_SUBJECT_KIT_VERSION_ID_NOT_NULL = "delete-subject.kitVersionId.notNull";
    public static final String DELETE_SUBJECT_KIT_DELETION_UNSUPPORTED = "delete-subject.deletion.notAllowed";

    public static final String GET_SUBJECT_LIST_KIT_VERSION_ID_NOT_NULL = "get-subject-list.kitVersionId.notNull";
    public static final String GET_SUBJECT_LIST_SIZE_MIN = "get-subject-list.size.min";
    public static final String GET_SUBJECT_LIST_SIZE_MAX = "get-subject-list.size.max";
    public static final String GET_SUBJECT_LIST_PAGE_MIN = "get-subject-list.page.min";

    public static final String UPDATE_SUBJECT_KIT_VERSION_ID_NOT_NULL = "update-subject.kitVersionId.notNull";
    public static final String UPDATE_SUBJECT_SUBJECT_ID_NOT_NULL = "update-subject.subjectId.notNull";
    public static final String UPDATE_SUBJECT_INDEX_NOT_NULL = "update-subject.index.notNull";
    public static final String UPDATE_SUBJECT_TITLE_NOT_NULL = "update-subject.title.notNull";
    public static final String UPDATE_SUBJECT_TITLE_SIZE_MIN = "update-subject.title.size.min";
    public static final String UPDATE_SUBJECT_TITLE_SIZE_MAX = "update-subject.title.size.max";
    public static final String UPDATE_SUBJECT_DESCRIPTION_NOT_NULL = "update-subject.description.notNull";
    public static final String UPDATE_SUBJECT_DESCRIPTION_SIZE_MIN = "update-subject.description.size.min";
    public static final String UPDATE_SUBJECT_DESCRIPTION_SIZE_MAX = "update-subject.description.size.max";
    public static final String UPDATE_SUBJECT_WEIGHT_NOT_NULL = "update-subject.weight.notNull";

    public static final String UPDATE_SUBJECT_ORDERS_KIT_VERSION_ID_NOT_NULL = "update-subject-orders.kitVersionId.notNull";
    public static final String UPDATE_SUBJECT_ORDERS_SUBJECTS_NOT_NULL = "update-subject-orders.subjects.notNull";
    public static final String UPDATE_SUBJECT_ORDERS_SUBJECTS_SIZE_MIN = "update-subject-orders.subjects.size.min";
    public static final String UPDATE_SUBJECT_ORDERS_SUBJECT_ID_NOT_NULL = "update-subject-orders.subjectId.notNull";
    public static final String UPDATE_SUBJECT_ORDERS_INDEX_NOT_NULL = "update-subject-orders.index.notNull";
    public static final String UPDATE_SUBJECT_ORDERS_INDEX_MIN = "update-subject-orders.index.min";

    public static final String DELETE_ATTRIBUTE_KIT_VERSION_ID_NOT_NULL = "delete-attribute.kitVersionId.notNull";
    public static final String DELETE_ATTRIBUTE_ATTRIBUTE_ID_NOT_NULL = "delete-attribute.attributeId.notNull";
    public static final String DELETE_ATTRIBUTE_NOT_ALLOWED = "delete-attribute.notAllowed";

    public static final String UPDATE_ATTRIBUTE_ORDERS_KIT_VERSION_ID_NOT_NULL = "update-attribute-orders.kitVersionId.notNull";
    public static final String UPDATE_ATTRIBUTE_ORDERS_ATTRIBUTES_NOT_NULL = "update-attribute-orders.attributes.notNull";
    public static final String UPDATE_ATTRIBUTE_ORDERS_ATTRIBUTES_SIZE_MIN = "update-attribute-orders.attributes.size.min";
    public static final String UPDATE_ATTRIBUTE_ORDERS_ATTRIBUTE_ID_NOT_NULL = "update-attribute-orders.attributeId.notNull";
    public static final String UPDATE_ATTRIBUTE_ORDERS_INDEX_NOT_NULL = "update-attribute-orders.index.notNull";
    public static final String UPDATE_ATTRIBUTE_ORDERS_SUBJECT_ID_NOT_NULL = "update-attribute-orders.subjectId.notNull";
    public static final String UPDATE_ATTRIBUTE_ORDERS_INDEX_MIN = "update-attribute-orders.index.min";

    public static final String UPDATE_QUESTIONNAIRE_ORDERS_KIT_VERSION_ID_NOT_NULL = "update-questionnaire-orders.kitVersionId.notNull";
    public static final String UPDATE_QUESTIONNAIRE_ORDERS_ORDERS_NOT_NULL = "update-questionnaire-orders.orders.notNull";
    public static final String UPDATE_QUESTIONNAIRE_ORDERS_ORDERS_SIZE_MIN = "update-questionnaire-orders.orders.size.min";
    public static final String UPDATE_QUESTIONNAIRE_ORDERS_QUESTIONNAIRE_ID_NOT_NULL = "update-questionnaire-orders.questionnaireId.notNull";
    public static final String UPDATE_QUESTIONNAIRE_ORDERS_QUESTIONNAIRE_INDEX_NOT_NULL = "update-questionnaire-orders.questionnaireIndex.notNull";
    public static final String UPDATE_QUESTIONNAIRE_ORDERS_QUESTIONNAIRE_INDEX_MIN = "update-questionnaire-orders.questionnaireIndex.min";

    public static final String UPDATE_ATTRIBUTE_KIT_VERSION_ID_NOT_NULL = "update-attribute.kitVersionId.notNull";
    public static final String UPDATE_ATTRIBUTE_ATTRIBUTE_ID_NOT_NULL = "update-attribute.attributeId.notNull";
    public static final String UPDATE_ATTRIBUTE_TITLE_NOT_BLANK = "update-attribute.title.notBlank";
    public static final String UPDATE_ATTRIBUTE_TITLE_SIZE_MIN = "update-attribute.title.size.min";
    public static final String UPDATE_ATTRIBUTE_TITLE_SIZE_MAX = "update-attribute.title.size.max";
    public static final String UPDATE_ATTRIBUTE_DESCRIPTION_NOT_BLANK = "update-attribute.description.notBlank";
    public static final String UPDATE_ATTRIBUTE_DESCRIPTION_SIZE_MIN = "update-attribute.description.size.min";
    public static final String UPDATE_ATTRIBUTE_DESCRIPTION_SIZE_MAX = "update-attribute.description.size.max";
    public static final String UPDATE_ATTRIBUTE_SUBJECT_ID_NOT_NULL = "update-attribute.subjectId.notNull";
    public static final String UPDATE_ATTRIBUTE_INDEX_NOT_NULL = "update-attribute.index.notNull";
    public static final String UPDATE_ATTRIBUTE_WEIGHT_NOT_NULL = "update-attribute.weight.notNull";

    public static final String DELETE_QUESTION_KIT_VERSION_ID_NOT_NULL = "delete-question.kitVersionId.notNull";
    public static final String DELETE_QUESTION_QUESTION_ID_NOT_NULL = "delete-question.questionId.notNull";
    public static final String DELETE_QUESTION_ID_NOT_FOUND = "delete-question.id.notFound";
    public static final String DELETE_QUESTION_NOT_ALLOWED = "delete-question.notAllowed";

    public static final String UPDATE_QUESTIONNAIRE_KIT_VERSION_ID_NOT_NULL = "update-questionnaire.kitVersionId.notNull";
    public static final String UPDATE_QUESTIONNAIRE_QUESTIONNAIRE_ID_NOT_NULL = "update-questionnaire.questionnaireId.notNull";
    public static final String UPDATE_QUESTIONNAIRE_INDEX_NOT_NULL = "update-questionnaire.index.notNull";
    public static final String UPDATE_QUESTIONNAIRE_TITLE_NOT_NULL = "update-questionnaire.title.notNull";
    public static final String UPDATE_QUESTIONNAIRE_TITLE_SIZE_MIN = "update-questionnaire.title.size.min";
    public static final String UPDATE_QUESTIONNAIRE_TITLE_SIZE_MAX = "update-questionnaire.title.size.max";
    public static final String UPDATE_QUESTIONNAIRE_DESCRIPTION_NOT_NULL = "update-questionnaire.description.notNull";
    public static final String UPDATE_QUESTIONNAIRE_DESCRIPTION_SIZE_MIN = "update-questionnaire.description.size.min";
    public static final String UPDATE_QUESTIONNAIRE_DESCRIPTION_SIZE_MAX = "update-questionnaire.description.size.max";

    public static final String CREATE_QUESTION_KIT_VERSION_ID_NOT_NULL = "create-question.kitVersionId.notNull";
    public static final String CREATE_QUESTION_INDEX_NOT_NULL = "create-question.index.notNull";
    public static final String CREATE_QUESTION_TITLE_NOT_NULL = "create-question.title.notNull";
    public static final String CREATE_QUESTION_TITLE_SIZE_MIN = "create-question.title.size.min";
    public static final String CREATE_QUESTION_TITLE_SIZE_MAX = "create-question.title.size.max";
    public static final String CREATE_QUESTION_HINT_SIZE_MIN = "create-question.hint.size.min";
    public static final String CREATE_QUESTION_HINT_SIZE_MAX = "create-question.hint.size.max";
    public static final String CREATE_QUESTION_MAY_NOT_BE_APPLICABLE_NOT_NULL = "create-question.mayNotBeApplicable.notNull";
    public static final String CREATE_QUESTION_ADVISABLE_NOT_NULL = "create-question.advisable.notNull";
    public static final String CREATE_QUESTION_QUESTIONNAIRE_ID_NOT_NULL= "create-question.questionnaireId.notNull";
    public static final String CREATE_QUESTION_INDEX_DUPLICATE = "create-question.index.duplicate";
    public static final String CREATE_QUESTION_IMPACT_DUPLICATE = "create-question-impact.duplicate";

    public static final String GET_ATTRIBUTES_KIT_VERSION_ID_NOT_NULL = "get-attributes.kitVersionId.notNull";
    public static final String GET_ATTRIBUTES_PAGE_MIN = "get-attributes.page.min";
    public static final String GET_ATTRIBUTES_SIZE_MIN = "get-attributes.size.min";
    public static final String GET_ATTRIBUTES_SIZE_MAX = "get-attributes.size.max";

    public static final String CREATE_QUESTION_IMPACT_KIT_VERSION_ID_NOT_NULL = "create-question-impact.kitVersionId.notNull";
    public static final String CREATE_QUESTION_IMPACT_ATTRIBUTE_ID_NOT_NULL = "create-question-impact.attributeId.notNull";
    public static final String CREATE_QUESTION_IMPACT_MATURITY_LEVEL_ID_NOT_NULL = "create-question-impact.maturityLevelId.notNull";
    public static final String CREATE_QUESTION_IMPACT_WEIGHT_NOT_NULL = "create-question-impact.weight.notNull";
    public static final String CREATE_QUESTION_IMPACT_QUESTION_ID_NOT_NULL = "create-question-impact.questionId.notNull";

    public static final String GET_QUESTION_IMPACTS_QUESTION_ID_NOT_NULL = "get-question-impacts.questionId.notNull";
    public static final String GET_QUESTION_IMPACTS_KIT_VERSION_ID_NOT_NULL = "get-question-impacts.kitVersionId.notNull";

    public static final String DELETE_QUESTIONNAIRE_KIT_VERSION_ID_NOT_NULL = "delete-questionnaire.kitVersionId.notNull";
    public static final String DELETE_QUESTIONNAIRE_QUESTIONNAIRE_ID_NOT_NULL = "delete-questionnaire.questionnaireId.notNull";
    public static final String DELETE_QUESTIONNAIRE_NOT_ALLOWED = "delete-questionnaire.notAllowed";

    public static final String GET_QUESTIONNAIRES_KIT_VERSION_ID_NOT_NULL = "get-questionnaires.kitVersionId.notNull";
    public static final String GET_QUESTIONNAIRES_PAGE_MIN = "get-questionnaires.page.min";
    public static final String GET_QUESTIONNAIRES_SIZE_MIN = "get-questionnaires.size.min";
    public static final String GET_QUESTIONNAIRES_SIZE_MAX = "get-questionnaires.size.max";

    public static final String UPDATE_QUESTION_KIT_VERSION_ID_NOT_NULL = "update-question.kitVersionId.notNull";
    public static final String UPDATE_QUESTION_QUESTION_ID_NOT_NULL = "update-question.questionId.notNull";
    public static final String UPDATE_QUESTION_INDEX_NOT_NULL = "update-question.index.notNull";
    public static final String UPDATE_QUESTION_TITLE_NOT_NULL = "update-question.title.notNull";
    public static final String UPDATE_QUESTION_TITLE_SIZE_MIN = "update-question.title.size.min";
    public static final String UPDATE_QUESTION_TITLE_SIZE_MAX = "update-question.title.size.max";
    public static final String UPDATE_QUESTION_HINT_SIZE_MIN = "update-question.hint.size.min";
    public static final String UPDATE_QUESTION_HINT_SIZE_MAX = "update-question.hint.size.max";
    public static final String UPDATE_QUESTION_MAY_NOT_BE_APPLICABLE_NOT_NULL = "update-question.myNotBeApplicable.notNull";
    public static final String UPDATE_QUESTION_ADVISABLE_NOT_NULL = "update-question.advisable.notNull";
    public static final String UPDATE_QUESTION_ANSWER_RANGE_ID_NOT_UPDATABLE = "update-question.answerRangeId.notUpdatable";

    public static final String UPDATE_QUESTIONS_ORDER_KIT_VERSION_ID_NOT_NULL = "update-questions-order.kitVersionId.notNull";
    public static final String UPDATE_QUESTIONS_ORDER_ORDERS_NOT_NULL = "update-questions-order.orders.notNull";
    public static final String UPDATE_QUESTIONS_ORDERS_ORDERS_SIZE_MIN = "update-questions-orders.orders.size.min";
    public static final String UPDATE_QUESTIONS_ORDER_QUESTIONNAIRE_ID_NOT_NULL = "update-questions-order.questionnaireId.notNull";
    public static final String UPDATE_QUESTIONS_ORDER_QUESTION_ID_NOT_NULL = "update-questions-order.questionId.notNull";
    public static final String UPDATE_QUESTIONS_ORDER_INDEX_NOT_NULL = "update-questions-order.index.notNull";
    public static final String UPDATE_QUESTIONS_ORDERS_INDEX_MIN = "update-questions-orders.index.min";

    public static final String UPDATE_QUESTION_IMPACT_KIT_VERSION_ID_NOT_NULL = "update-question-impact.kitVersionId.notNull";
    public static final String UPDATE_QUESTION_IMPACT_QUESTION_IMPACT_ID_NOT_NULL = "update-question-impact.questionImpactId.notNull";
    public static final String UPDATE_QUESTION_IMPACT_WEIGHT_NOT_NULL = "update-question-impact.weight.notNull";
    public static final String UPDATE_QUESTION_IMPACT_ATTRIBUTE_ID_NOT_NULL = "update-question-impact.attributeId.notNull";
    public static final String UPDATE_QUESTION_IMPACT_MATURITY_LEVEL_ID_NOT_NULL = "update-question-impact.maturityLevelId.notNull";

    public static final String GET_KIT_VERSION_KIT_VERSION_ID_NOT_NULL = "get-kit-version.kitVersionId.notNull";

    public static final String CLONE_KIT_KIT_ID_NOT_NULL = "clone-kit.kitId.notNull";
    public static final String CLONE_KIT_NOT_ALLOWED = "clone-kit.notAllowed";

    public static final String DELETE_QUESTION_IMPACT_QUESTION_IMPACT_ID_NOT_NULL = "delete-question-impact.questionImpactId.notNull";
    public static final String DELETE_QUESTION_IMPACT_KIT_VERSION_ID_NOT_NULL = "delete-question-impact.kitVersionId.notNull";

    public static final String DELETE_ANSWER_OPTION_ANSWER_OPTION_ID_NOT_NULL = "delete-answer-option.answerOptionId.notNull";
    public static final String DELETE_ANSWER_OPTION_KIT_VERSION_ID_NOT_NULL = "delete-answer-option.kitVersionId.notNull";

    public static final String GET_QUESTION_OPTIONS_KIT_VERSION_ID_NOT_NULL = "get-question-options.kitVersionId.notNull";
    public static final String GET_QUESTION_OPTIONS_QUESTION_ID_NOT_NULL = "get-question-options.questionId.notNull";

    public static final String GET_QUESTIONNAIRE_QUESTIONS_KIT_VERSION_ID_NOT_NULL = "get-questionnaire-questions-kitVersionId.notNull";
    public static final String GET_QUESTIONNAIRE_QUESTIONS_QUESTIONNAIRE_ID_NOT_NULL = "get-questionnaire-questions-questionnaireId.notNull";
    public static final String GET_QUESTIONNAIRE_QUESTIONS_PAGE_MIN = "get-questionnaire-questions.page.min";
    public static final String GET_QUESTIONNAIRE_QUESTIONS_SIZE_MIN = "get-questionnaire-questions.size.min";
    public static final String GET_QUESTIONNAIRE_QUESTIONS_SIZE_MAX = "get-questionnaire-questions.size.max";

    public static final String GET_ANSWER_RANGE_LIST_KIT_VERSION_ID_NOT_NULL = "get-answer-range-list.kitVersionId.notNull";
    public static final String GET_ANSWER_RANGE_LIST_PAGE_MIN = "get-answer-range-list.page.min";
    public static final String GET_ANSWER_RANGE_LIST_SIZE_MIN = "get-answer-range-list.size.min";
    public static final String GET_ANSWER_RANGE_LIST_SIZE_MAX = "get-answer-range-list.size.max";

    public static final String DELETE_KIT_VERSION_KIT_VERSION_ID_NOT_NULL = "delete-kit-version.kitVersionId.notNull";
    public static final String DELETE_KIT_VERSION_NOT_ALLOWED = "delete.kitVersion.notAllowed";

    public static final String UPDATE_ANSWER_RANGE_KIT_VERSION_ID_NOT_NULL = "update-answer-range.kitVersionId.notNull";
    public static final String UPDATE_ANSWER_RANGE_ANSWER_RANGE_ID_NOT_NULL = "update-answer-range.answerRangeId.notNull";
    public static final String UPDATE_ANSWER_RANGE_TITLE_SIZE_MIN = "update-answer-range.title.size.min";
    public static final String UPDATE_ANSWER_RANGE_TITLE_SIZE_MAX = "update-answer-range.title.size.max";
    public static final String UPDATE_ANSWER_RANGE_REUSABLE_NOT_NULL = "update-answer-range.reusable.notNull";
    public static final String UPDATE_ANSWER_RANGE_TITLE_NOT_NULL = "update-answer-range.title.notNull";
    public static final String UPDATE_ANSWER_RANGE_NOT_ALLOWED = "update-answer-range.notAllowed";

    public static final String UPDATE_ANSWER_OPTION_KIT_VERSION_ID_NOT_NULL = "update-answer-option.kitVersionId.notNull";
    public static final String UPDATE_ANSWER_OPTION_ANSWER_OPTION_ID_NOT_NULL = "update-answer-option.answerOptionId.notNull";
    public static final String UPDATE_ANSWER_OPTION_INDEX_NOT_NULL = "update-answer-option.index.notNull";
    public static final String UPDATE_ANSWER_OPTION_TITLE_NOT_NULL = "update-answer-option.title.notNull";
    public static final String UPDATE_ANSWER_OPTION_TITLE_SIZE_MAX = "update-answer-option.title.size.max";
    public static final String UPDATE_ANSWER_OPTION_VALUE_NOT_NULL = "update-answer-option.value.notNull";

    public static final String CREATE_ANSWER_OPTION_KIT_VERSION_ID_NOT_NULL = "create-answer-option.kitVersionId.notNull";
    public static final String CREATE_ANSWER_OPTION_QUESTION_NOT_NULL = "create-answer-option.questionId.notNull";
    public static final String CREATE_ANSWER_OPTION_VALUE_NOT_NULL = "create-answer-option.value.notNull";
    public static final String CREATE_ANSWER_OPTION_INDEX_NOT_NULL = "create-answer-option.index.notNull";
    public static final String CREATE_ANSWER_OPTION_TITLE_NOT_BLANK = "create-answer-option.title.notBlank";
    public static final String CREATE_ANSWER_OPTION_TITLE_SIZE_MAX = "create-answer-option.title.size.max";
    public static final String CREATE_ANSWER_OPTION_INDEX_DUPLICATE = "create-answer-option.index.duplicate";
    public static final String CREATE_ANSWER_OPTION_ANSWER_RANGE_REUSABLE = "create-answer-option.answerRange.reusable";

    public static final String CREATE_ANSWER_RANGE_KIT_VERSION_ID_NOT_NULL = "create-answer-range.kitVersionId.notNull";
    public static final String CREATE_ANSWER_RANGE_TITLE_NOT_BLANK = "create-answer-range.title.notBlank";
    public static final String CREATE_ANSWER_RANGE_TITLE_SIZE_MIN = "create-answer-range.title.size.min";
    public static final String CREATE_ANSWER_RANGE_TITLE_SIZE_MAX = "create-answer-range.title.size.max";
    public static final String CREATE_ANSWER_RANGE_TITLE_DUPLICATE = "create-answer-range.title.duplicate";

    public static final String CREATE_ANSWER_RANGE_OPTION_KIT_VERSION_ID_NOT_NULL = "create-answer-range-option.kitVersionId.notNull";
    public static final String CREATE_ANSWER_RANGE_OPTION_ANSWER_RANGE_ID_NOT_NULL = "create-answer-range-option.answerRangeId.notNull";
    public static final String CREATE_ANSWER_RANGE_OPTION_INDEX_NOT_NULL = "create-answer-range-option.index.notNull";
    public static final String CREATE_ANSWER_RANGE_OPTION_TITLE_NOT_BLANK = "create-answer-range-option.title.notBlank";
    public static final String CREATE_ANSWER_RANGE_OPTION_TITLE_SIZE_MAX = "create-answer-range-option.title.size.max";
    public static final String CREATE_ANSWER_RANGE_OPTION_VALUE_NOT_NULL = "create-answer-range-option.value.notNull";
    public static final String CREATE_ANSWER_RANGE_OPTION_ANSWER_RANGE_NON_REUSABLE = "create-answer-range-option.answerRange.non-reusable";

    public static final String CREATE_KIT_CUSTOM_KIT_ID_NOT_NULL = "create-kit-custom.kitId.notNull";
    public static final String CREATE_KIT_CUSTOM_TITLE_NOT_NULL = "create-kit-custom.title.notNull";
    public static final String CREATE_KIT_CUSTOM_TITLE_SIZE_MIN = "create-kit-custom.title.size.min";
    public static final String CREATE_KIT_CUSTOM_TITLE_SIZE_MAX = "create-kit-custom.title.size.max";
    public static final String CREATE_KIT_CUSTOM_DATA_NOT_NULL = "create-kit-custom.data.notNull";
    public static final String CREATE_KIT_CUSTOM_EMPTY_CUSTOM_NOT_ALLOWED = "create-kit-custom.emptyCustom.notAllowed";
    public static final String CREATE_KIT_CUSTOM_SUBJECT_ID_NOT_NULL = "create-kit-custom.subjectId.notNull";
    public static final String CREATE_KIT_CUSTOM_SUBJECT_WEIGHT_NOT_NULL = "create-kit-custom.subjectWeight.notNull";
    public static final String CREATE_KIT_CUSTOM_ATTRIBUTE_ID_NOT_NULL = "create-kit-custom.attributeId.notNull";
    public static final String CREATE_KIT_CUSTOM_ATTRIBUTE_WEIGHT_NOT_NULL = "create-kit-custom.attributeWeight.notNull";
    public static final String CREATE_KIT_CUSTOM_TITLE_DUPLICATE = "create-kit-custom.title.duplicate";

    public static final String VALIDATE_KIT_VERSION_KIT_VERSION_ID_NOT_NULL = "validate-kit-version.kitVersionId.notNull";
    public static final String VALIDATE_KIT_VERSION_QUESTION_ANSWER_RANGE_NOT_NULL = "validate-kit-version.question-answer-range.notNull";
    public static final String VALIDATE_KIT_VERSION_QUESTION_IMPACT_NOT_NULL = "validate-kit-version.question-impact.notNull";
    public static final String VALIDATE_KIT_VERSION_ANSWER_RANGE_LOW_OPTIONS = "validate-kit-version.answerRange.lowOptions";
    public static final String VALIDATE_KIT_VERSION_ATTRIBUTE_QUESTION_IMPACT_NOT_NULL = "validate-kit-version.attribute-question-impact.notNull";
    public static final String VALIDATE_KIT_VERSION_SUBJECT_ATTRIBUTE_NOT_NULL = "validate-kit-version.subject-attribute.notNull";
    public static final String VALIDATE_KIT_VERSION_QUESTIONNAIRE_QUESTION_NOT_NULL = "validate-kit-version.questionnaire-question.notNull";
    public static final String VALIDATE_KIT_VERSION_SUBJECT_NOT_NULL = "validate-kit-version.subject.notNull";
    public static final String VALIDATE_KIT_VERSION_QUESTION_NOT_NULL= "validate-kit-version.question.notNull";
    public static final String VALIDATE_KIT_VERSION_QUESTIONNAIRE_NOT_NULL= "validate-kit-version.questionnaire.notNull";
    public static final String VALIDATE_KIT_VERSION_MATURITY_LEVEL_NOT_NULL= "validate-kit-version.maturityLevel.notNull";

    public static final String UPDATE_KIT_CUSTOM_KIT_CUSTOM_ID_NOT_NULL = "update-kit-custom.kitCustomId.notNull";
    public static final String UPDATE_KIT_CUSTOM_KIT_ID_NOT_NULL = "update-kit-custom.kitId.notNull";
    public static final String UPDATE_KIT_CUSTOM_TITLE_NOT_NULL = "update-kit-custom.title.notNull";
    public static final String UPDATE_KIT_CUSTOM_TITLE_SIZE_MIN = "update-kit-custom.title.size.min";
    public static final String UPDATE_KIT_CUSTOM_TITLE_SIZE_MAX = "update-kit-custom.title.size.max";
    public static final String UPDATE_KIT_CUSTOM_DATA_NOT_NULL = "update-kit-custom.data.notNull";
    public static final String UPDATE_KIT_CUSTOM_EMPTY_CUSTOM_NOT_ALLOWED = "update-kit-custom.emptyCustom.notAllowed";
    public static final String UPDATE_KIT_CUSTOM_SUBJECT_ID_NOT_NULL = "update-kit-custom.subjectId.notNull";
    public static final String UPDATE_KIT_CUSTOM_SUBJECT_WEIGHT_NOT_NULL = "update-kit-custom.subjectWeight.notNull";
    public static final String UPDATE_KIT_CUSTOM_ATTRIBUTE_ID_NOT_NULL = "update-kit-custom.attributeId.notNull";
    public static final String UPDATE_KIT_CUSTOM_ATTRIBUTE_WEIGHT_NOT_NULL = "update-kit-custom.attributeWeight.notNull";

    public static final String GET_KIT_CUSTOM_SUBJECT_KIT_ID_NOT_NULL = "get-kit-custom-subject.kitId.notNull";
    public static final String GET_KIT_CUSTOM_SUBJECT_PAGE_MIN = "get-kit-custom-subject.page.min";
    public static final String GET_KIT_CUSTOM_SUBJECT_SIZE_MIN = "get-kit-custom-subject.size.min";
    public static final String GET_KIT_CUSTOM_SUBJECT_SIZE_MAX = "get-kit-custom-subject.size.max";

    public static final String GET_KIT_CUSTOM_KIT_CUSTOM_ID_NOT_NULL = "get-kit-custom.kitCustomId.notNull";

    public static final String GET_KIT_DSL_KIT_ID_NOT_NULL = "get-kit-dsl.kitId.notNull";
    public static final String GET_KIT_DSL_NOT_AVAILABLE = "get-kit-dsl.notAvailable";

    public static String entityNameSingleFirst(String fieldName) {
        return "entities.%s.single.first".formatted(fieldName);
    }

    public static String entityNamePlural(String fieldName) {
        return "entities.%s.plural".formatted(fieldName);
    }
}
