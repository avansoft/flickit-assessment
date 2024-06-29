package org.flickit.assessment.core.adapter.out.persistence.evidenceattachment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.flickit.assessment.core.application.port.out.evidenceattachment.LoadEvidenceAttachmentListPort.Result;
import org.flickit.assessment.data.jpa.core.evidenceattachment.EvidenceAttachmentJpaEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EvidenceAttachmentMapper {

    static Result mapEvidenceAttachment(EvidenceAttachmentJpaEntity evidenceAttachment) {
        return new Result(evidenceAttachment.getId(), evidenceAttachment.getEvidenceId(),
            evidenceAttachment.getFile(), evidenceAttachment.getDescription());
    }
}
