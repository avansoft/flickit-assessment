package org.flickit.assessment.users.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessageKey {

    public static final String EXPERT_GROUP_ID_NOT_FOUND = "expert-group.id.notFound";
    public static final String USER_BY_EMAIL_NOT_FOUND = "user-by-email.notFound";
    public static final String SPACE_ID_NOT_FOUND = "space.id.notFound";
    public static final String USER_ID_NOT_FOUND = "user.id.notFound";

    public static final String GET_USER_BY_EMAIL_EMAIL_NOT_NULL = "get-user-by-email.email.notNull";

    public static final String GET_EXPERT_GROUP_LIST_PAGE_MIN = "get-expert-group-list.page.min";
    public static final String GET_EXPERT_GROUP_LIST_SIZE_MIN = "get-expert-group-list.size.min";
    public static final String GET_EXPERT_GROUP_LIST_SIZE_MAX = "get-expert-group-list.size.max";

    public static final String UPDATE_EXPERT_GROUP_LAST_SEEN_EXPERT_GROUP_ID_NOT_NULL = "update-expertGroup-lastSeen.expertGroupId.notNull";

    public static final String GET_EXPERT_GROUP_EXPERT_GROUP_NOT_FOUND = "get-expert-group.expert-group.notFound";
    public static final String GET_EXPERT_GROUP_EXPERT_GROUP_ID_NOT_NULL = "get-expert-group.expert-group-id.notNull";

    public static final String CREATE_EXPERT_GROUP_TITLE_NOT_BLANK = "create-expert-group.title.notBlank";
    public static final String CREATE_EXPERT_GROUP_TITLE_SIZE_MIN = "create-expert-group.title.size.min";
    public static final String CREATE_EXPERT_GROUP_TITLE_SIZE_MAX = "create-expert-group.title.size.max";
    public static final String CREATE_EXPERT_GROUP_BIO_NOT_BLANK = "create-expert-group.bio.notBlank";
    public static final String CREATE_EXPERT_GROUP_BIO_SIZE_MIN = "create-expert-group.bio.size.min";
    public static final String CREATE_EXPERT_GROUP_BIO_SIZE_MAX = "create-expert-group.bio.size.max";
    public static final String CREATE_EXPERT_GROUP_ABOUT_NOT_BLANK = "create-expert-group.about.notBlank";
    public static final String CREATE_EXPERT_GROUP_ABOUT_SIZE_MIN = "create-expert-group.about.size.min";
    public static final String CREATE_EXPERT_GROUP_ABOUT_SIZE_MAX = "create-expert-group.about.size.max";
    public static final String CREATE_EXPERT_GROUP_WEBSITE_NOT_URL = "create-expert-group.website.notUrl";
    public static final String CREATE_EXPERT_GROUP_WEBSITE_SIZE_MIN = "create-expert-group.website.size.min";
    public static final String CREATE_EXPERT_GROUP_WEBSITE_SIZE_MAX = "create-expert-group.website.size.max";
    public static final String CREATE_EXPERT_GROUP_TITLE_DUPLICATE = "create-expert-group.title.duplicate";

    public static final String INVITE_EXPERT_GROUP_MEMBER_USER_ID_NOT_NULL = "invite-expert-group-member.user-id.notNull";
    public static final String INVITE_EXPERT_GROUP_MEMBER_EXPERT_GROUP_ID_NOT_NULL = "invite-expert-group-member.expert-group-id.notNull";
    public static final String INVITE_EXPERT_GROUP_MEMBER_EXPERT_GROUP_ID_USER_ID_DUPLICATE = "invite-expert-group-member.expert-group-id-user-id.duplicate";
    public static final String INVITE_EXPERT_GROUP_MEMBER_USER_ID_NOT_FOUND = "invite-expert-group-member.user-id.notFound";
    public static final String INVITE_EXPERT_GROUP_MEMBER_EXPERT_GROUP_ID_NOT_FOUND = "invite-expert-group-member.expert-group-id.notFound";

    public static final String GET_EXPERT_GROUP_MEMBERS_ID_NOT_NULL = "get-expert-group-members.id.notNull";
    public static final String GET_EXPERT_GROUP_MEMBERS_SIZE_MIN = "get-expert-group-members.size.min";
    public static final String GET_EXPERT_GROUP_MEMBERS_SIZE_MAX = "get-expert-group-members.size.max";
    public static final String GET_EXPERT_GROUP_MEMBERS_PAGE_MIN = "get-expert-group-members.page.min";
    public static final String GET_EXPERT_GROUP_MEMBERS_STATUS_INVALID = "get-expert-group-members.status.invalid";

    public static final String DELETE_EXPERT_GROUP_KITS_EXIST = "delete-expert-group.kits.exist";
    public static final String DELETE_EXPERT_GROUP_EXPERT_GROUP_ID_NOT_NULL = "delete-expert-group.expert-group-id.notNull";

    public static final String CONFIRM_EXPERT_GROUP_INVITATION_EXPERT_GROUP_ID_NOT_NULL = "confirm-expert-group-invitation.expert-group-id.notNull";
    public static final String CONFIRM_EXPERT_GROUP_INVITATION_INVITE_TOKEN_NOT_NULL = "confirm-expert-group-invitation.invite-token.notNull";
    public static final String CONFIRM_EXPERT_GROUP_INVITATION_LINK_INVALID = "confirm-expert-group-invitation.invitation-link.notValid";
    public static final String CONFIRM_EXPERT_GROUP_INVITATION_INVITE_TOKEN_EXPIRED = "confirm-expert-group-invitation.invite-token.expired";
    public static final String CONFIRM_EXPERT_GROUP_INVITATION_INVITE_TOKEN_INVALID = "confirm-expert-group-invitation.invite-token.notValid";
    public static final String CONFIRM_EXPERT_GROUP_INVITATION_USER_ID_DUPLICATE = "confirm-expert-group-invitation.userId.duplicate";

    public static final String DELETE_EXPERT_GROUP_MEMBER_USER_ID_NOT_NULL = "delete-expert-group-member.userId.notNull";
    public static final String DELETE_EXPERT_GROUP_MEMBER_EXPERT_GROUP_ID_NOT_NULL = "delete-expert-group-member.expertGroupId.notNull";
    public static final String DELETE_EXPERT_GROUP_MEMBER_USER_ID_NOT_FOUND = "delete-expert-group-member.userId.notFound";

    public static final String ADD_SPACE_MEMBER_SPACE_ID_NOT_NULL = "add-space-member.spaceId.notNull";
    public static final String ADD_SPACE_MEMBER_EMAIL_NOT_NULL = "add-space-member.email.notNull";
    public static final String ADD_SPACE_MEMBER_SPACE_USER_DUPLICATE = "add-space-member.spaceId.userId.duplicate";

    public static final String INVITE_SPACE_MEMBER_SPACE_ID_NOT_NULL = "invite-space-member.spaceId.notNull";
    public static final String INVITE_SPACE_MEMBER_EMAIL_NOT_NULL = "invite-space-member.email.notNull";
    public static final String INVITE_SPACE_MEMBER_SPACE_USER_DUPLICATE = "invite-space-member.spaceId.userId.duplicate";

    public static final String ACCEPT_SPACE_INVITATIONS_USER_ID_NOT_NULL = "accept-space-invitations.userId.notNull";

    public static final String GET_SPACE_MEMBERS_SPACE_ID_NOT_NULL = "get-space-members.spaceId.notNull";
    public static final String GET_SPACE_MEMBERS_SIZE_MIN = "get-space-members.size.min";
    public static final String GET_SPACE_MEMBERS_SIZE_MAX = "get-space-members.size.max";
    public static final String GET_SPACE_MEMBERS_PAGE_MIN = "get-space-members.page.min";

    public static final String CREATE_SPACE_TITLE_NOT_BLANK = "create-space.title.notBlank";
    public static final String CREATE_SPACE_TITLE_SIZE_MIN = "create-space.title.size.min";
    public static final String CREATE_SPACE_TITLE_SIZE_MAX = "create-space.title.size.max";
    public static final String CREATE_SPACE_TYPE_NOT_NULL = "create-space.type.notNull";
    public static final String CREATE_SPACE_TYPE_INVALID = "create-space.type.invalid";
    public static final String CREATE_SPACE_TITLE_DUPLICATE = "create-space.title.duplicate";

    public static final String DELETE_SPACE_MEMBER_USER_ID_NOT_NULL = "delete-space-member.userId.notNull";
    public static final String DELETE_SPACE_MEMBER_SPACE_ID_NOT_NULL = "delete-space-member.spaceId.notNull";
    public static final String DELETE_SPACE_MEMBER_USER_ID_NOT_FOUND = "delete-space-member.userId.notFound";
    public static final String DELETE_SPACE_MEMBER_USER_IS_SPACE_OWNER = "delete-space-member.userId.isSpaceOwner";

    public static final String GET_SPACE_SPACE_ID_NOT_NULL = "get-space.spaceId.notNull";

    public static final String GET_SPACE_LIST_PAGE_MIN = "get-space-list.page.min";
    public static final String GET_SPACE_LIST_SIZE_MIN = "get-space-list.size.min";
    public static final String GET_SPACE_LIST_SIZE_MAX = "get-space-list.size.max";

    public static final String UPDATE_SPACE_LAST_SEEN_SPACE_ID_NOT_NULL = "update-space-lastSeen.spaceId.notNull";

    public static final String DELETE_SPACE_ASSESSMENT_EXIST = "delete-space.assessments.exist";
    public static final String DELETE_SPACE_SPACE_ID_NOT_NULL = "delete-space.spaceId.notNull";

    public static final String GET_SPACE_INVITEES_SPACE_ID_NOT_NULL = "get-space-invitees.spaceId.notNull";
    public static final String GET_SPACE_INVITEES_SIZE_MIN = "get-space-invitees.size.min";
    public static final String GET_SPACE_INVITEES_SIZE_MAX = "get-space-invitees.size.max";
    public static final String GET_SPACE_INVITEES_PAGE_MIN = "get-space-invitees.page.min";

    public static final String UPDATE_SPACE_SPACE_ID_NOT_NULL = "update-space.spaceId.notNull";
    public static final String UPDATE_SPACE_TITLE_NOT_BLANK = "update-space.title.notBlank";
    public static final String UPDATE_SPACE_TITLE_SIZE_MIN = "update-space.title.size.min";
    public static final String UPDATE_SPACE_TITLE_SIZE_MAX = "update-space.title.size.max";

    public static final String LEAVE_SPACE_SPACE_ID_NOT_NULL = "leave-space.spaceId.notNull";
    public static final String LEAVE_SPACE_OWNER_NOT_ALLOWED = "leave-space.owner.notAllowed";

    public static final String UPDATE_EXPERT_GROUP_EXPERT_GROUP_ID_NOT_NULL = "update-expert-group.expertGroupId.notNull";
    public static final String UPDATE_EXPERT_GROUP_TITLE_NOT_BLANK = "update-expert-group.title.notBlank";
    public static final String UPDATE_EXPERT_GROUP_TITLE_SIZE_MIN = "update-expert-group.title.size.min";
    public static final String UPDATE_EXPERT_GROUP_TITLE_SIZE_MAX = "update-expert-group.title.size.max";
    public static final String UPDATE_EXPERT_GROUP_BIO_NOT_BLANK = "update-expert-group.bio.notBlank";
    public static final String UPDATE_EXPERT_GROUP_BIO_SIZE_MIN = "update-expert-group.bio.size.min";
    public static final String UPDATE_EXPERT_GROUP_BIO_SIZE_MAX = "update-expert-group.bio.size.max";
    public static final String UPDATE_EXPERT_GROUP_ABOUT_NOT_BLANK = "update-expert-group.about.notBlank";
    public static final String UPDATE_EXPERT_GROUP_ABOUT_SIZE_MIN = "update-expert-group.about.size.min";
    public static final String UPDATE_EXPERT_GROUP_ABOUT_SIZE_MAX = "update-expert-group.about.size.max";
    public static final String UPDATE_EXPERT_GROUP_WEBSITE_NOT_URL = "update-expert-group.website.notUrl";
    public static final String UPDATE_EXPERT_GROUP_WEBSITE_SIZE_MIN = "update-expert-group.website.size.min";
    public static final String UPDATE_EXPERT_GROUP_WEBSITE_SIZE_MAX = "update-expert-group.website.size.max";

    public static final String UPDATE_EXPERT_GROUP_PICTURE_EXPERT_GROUP_ID_NOT_NULL = "update-expert-group-picture.expertGroupId.notNull";
    public static final String UPDATE_EXPERT_GROUP_PICTURE_PICTURE_NOT_NULL = "update-expert-group-picture.picture.notNull";

    public static final String DELETE_SPACE_INVITATION_INVITE_ID_NOT_NULL = "delete-space-invitation.inviteId.notNull";
    public static final String DELETE_SPACE_INVITATION_INVITE_ID_NOT_FOUND = "delete-space-invitation.inviteId.notFound";

    public static final String DELETE_EXPERT_GROUP_PICTURE_EXPERT_GROUP_ID_NOT_NULL = "delete-expert-group-picture.expertGroupId.notNull";

    public static final String UPDATE_USER_PROFILE_DISPLAY_NAME_NOT_NULL = "update-user-profile.displayName.notNull";
    public static final String UPDATE_USER_PROFILE_DISPLAY_NAME_SIZE_MIN = "update-user-profile.displayName.size.min";
    public static final String UPDATE_USER_PROFILE_DISPLAY_NAME_SIZE_MAX = "update-user-profile.displayName.size.max";
    public static final String UPDATE_USER_PROFILE_BIO_SIZE_MIN = "update-user-profile.bio.size.min";
    public static final String UPDATE_USER_PROFILE_BIO_SIZE_MAX = "update-user-profile.bio.size.max";
    public static final String UPDATE_USER_PROFILE_LINKEDIN_NOT_VALID = "update-user-profile.linkedin.notValid";

    public static final String CREATE_USER_USER_ID_NOT_NULL = "create-user.userId.notNull";
    public static final String CREATE_USER_EMAIL_NOT_NULL = "create-user.email.notNull";
    public static final String CREATE_USER_DISPLAY_NAME_NOT_NULL = "create-user.displayName.notNull";
    public static final String CREATE_USER_DISPLAY_NAME_SIZE_MIN = "create-user.displayName.size.min";
    public static final String CREATE_USER_DISPLAY_NAME_SIZE_MAX = "create-user.displayName.size.max";

    public static final String LEAVE_EXPERT_GROUP_ID_NOT_NULL = "leave-expert-group.expertGroupId.notNull";
    public static final String LEAVE_EXPERT_GROUP_NOT_ALLOWED = "leave-expert-group.owner.notAllowed";

    public static final String UPDATE_USER_PROFILE_PICTURE_NOT_NULL = "update-user-profile-picture.picture.notNull";
}
