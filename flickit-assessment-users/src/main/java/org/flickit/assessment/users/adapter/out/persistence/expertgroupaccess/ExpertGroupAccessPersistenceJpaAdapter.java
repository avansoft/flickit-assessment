package org.flickit.assessment.users.adapter.out.persistence.expertgroupaccess;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.data.jpa.users.expertgroup.ExpertGroupMembersView;
import org.flickit.assessment.data.jpa.users.expertgroupaccess.ExpertGroupAccessJpaEntity;
import org.flickit.assessment.data.jpa.users.expertgroupaccess.ExpertGroupAccessJpaRepository;
import org.flickit.assessment.users.application.domain.ExpertGroupAccess;
import org.flickit.assessment.users.application.port.out.expertgroupaccess.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static org.flickit.assessment.users.common.ErrorMessageKey.CONFIRM_EXPERT_GROUP_INVITATION_LINK_INVALID;

@Component
@RequiredArgsConstructor
public class ExpertGroupAccessPersistenceJpaAdapter implements
    CreateExpertGroupAccessPort,
    LoadExpertGroupMembersPort,
    InviteExpertGroupMemberPort,
    LoadExpertGroupMemberStatusPort,
    LoadExpertGroupAccessPort,
    ConfirmExpertGroupInvitationPort {

    private final ExpertGroupAccessJpaRepository repository;

    @Override
    public PaginatedResponse<Member> loadExpertGroupMembers(long expertGroupId, int status, int page, int size) {
        var pageResult = repository.findExpertGroupMembers(expertGroupId, status,
            PageRequest.of(page, size, Sort.Direction.DESC, ExpertGroupAccessJpaEntity.Fields.CREATION_TIME));

        var items = pageResult
            .stream()
            .map(ExpertGroupAccessPersistenceJpaAdapter::mapToResult)
            .toList();

        return new PaginatedResponse<>(
            items,
            pageResult.getNumber(),
            pageResult.getSize(),
            ExpertGroupAccessJpaEntity.Fields.CREATION_TIME,
            Sort.Direction.DESC.name().toLowerCase(),
            (int) pageResult.getTotalElements()
        );
    }

    @Override
    public void persist(CreateExpertGroupAccessPort.Param param) {
        ExpertGroupAccessJpaEntity unsavedEntity = ExpertGroupAccessMapper.mapCreateParamToJpaEntity(param);
        repository.save(unsavedEntity);
    }

    @Override
    public void invite(InviteExpertGroupMemberPort.Param param) {
        ExpertGroupAccessJpaEntity unsavedEntity = (ExpertGroupAccessMapper.mapInviteParamToJpaEntity(param));
        repository.save(unsavedEntity);
    }

    private static LoadExpertGroupMembersPort.Member mapToResult(ExpertGroupMembersView view) {
        return new LoadExpertGroupMembersPort.Member(
            view.getId(),
            view.getEmail(),
            view.getDisplayName(),
            view.getBio(),
            view.getPicture(),
            view.getLinkedin(),
            view.getStatus(),
            view.getInviteExpirationDate());
    }

    @Override
    public Optional<Integer> getMemberStatus(long expertGroupId, UUID userId) {
        return repository.findExpertGroupMemberStatus(expertGroupId, userId);
    }

    @Override
    public ExpertGroupAccess loadExpertGroupAccess(long expertGroupId, UUID userId) {
        var result =  repository.findByExpertGroupIdAndAndUserId(expertGroupId, userId)
            .orElseThrow(() -> new ResourceNotFoundException(CONFIRM_EXPERT_GROUP_INVITATION_LINK_INVALID));
        return mapToDomain (result);
    }

    private static ExpertGroupAccess mapToDomain(ExpertGroupAccessJpaEntity entity) {
        return new ExpertGroupAccess(
            entity.getInviteExpirationDate(),
            entity.getInviteToken(),
            entity.getStatus());

    }

    @Override
    public void confirmInvitation(UUID inviteToken) {
        repository.confirmInvitation(inviteToken);
    }
}
