package org.flickit.assessment.core.adapter.out.persistence.spaceinvitee;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.core.application.port.out.space.CreateSpaceInvitationPort;
import org.flickit.assessment.data.jpa.users.spaceinvitee.SpaceInviteeJpaEntity;
import org.flickit.assessment.data.jpa.users.spaceinvitee.SpaceInviteeJpaRepository;
import org.springframework.stereotype.Component;

import static org.flickit.assessment.core.adapter.out.persistence.spaceinvitee.SpaceInviteeMapper.mapToJpaEntity;

@Component("coreSpaceInviteePersistenceJpaAdapter")
@RequiredArgsConstructor
public class SpaceInviteePersistenceJpaAdapter implements CreateSpaceInvitationPort {

    private final SpaceInviteeJpaRepository repository;

    @Override
    public void persist(CreateSpaceInvitationPort.Param param) {
        var invitation = repository.findBySpaceIdAndEmail(param.spaceId(), param.email());

        SpaceInviteeJpaEntity entity;
        entity = invitation.map(SpaceInviteeJpaEntity -> mapToJpaEntity(invitation.get().getId(), param))
            .orElseGet(() -> mapToJpaEntity(null, param));

        repository.save(entity);
    }
}
