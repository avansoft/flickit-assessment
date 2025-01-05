package org.flickit.assessment.users.application.service.space;

import lombok.RequiredArgsConstructor;
import org.flickit.assessment.users.application.domain.Space;
import org.flickit.assessment.users.application.domain.SpaceUserAccess;
import org.flickit.assessment.users.application.port.in.space.CreateSpaceUseCase;
import org.flickit.assessment.users.application.port.out.space.CreateSpacePort;
import org.flickit.assessment.users.application.port.out.spaceuseraccess.CreateSpaceUserAccessPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.flickit.assessment.common.util.SlugCodeUtil.generateSlugCode;

@Service
@RequiredArgsConstructor
public class CreateSpaceService implements CreateSpaceUseCase {

    private final CreateSpacePort createSpacePort;
    private final CreateSpaceUserAccessPort createSpaceUserAccessPort;

    @Override
    public Result createSpace(Param param) {
        long id = createSpacePort.persist(mapToDomain(param.getTitle(), param.getCurrentUserId()));

        createOwnerAccessToSpace(id, param.getCurrentUserId(), param.getCurrentUserId());
        return new Result(id);
    }

    private Space mapToDomain(String title, UUID currentUserId) {
        LocalDateTime creationTime = LocalDateTime.now();
        return new Space(null,
            generateSlugCode(title),
            title,
            currentUserId,
            creationTime,
            creationTime,
            currentUserId,
            currentUserId
        );
    }

    private void createOwnerAccessToSpace(long id, UUID invitee, UUID inviter) {
        createSpaceUserAccessPort.persist(new SpaceUserAccess(id, invitee, inviter, LocalDateTime.now()));
    }
}
