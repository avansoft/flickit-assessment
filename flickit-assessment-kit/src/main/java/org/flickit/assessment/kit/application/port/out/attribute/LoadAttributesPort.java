package org.flickit.assessment.kit.application.port.out.attribute;

import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.kit.application.domain.Attribute;
import org.flickit.assessment.kit.application.domain.AttributeWithSubject;

import java.util.List;

public interface LoadAttributesPort {

    PaginatedResponse<AttributeWithSubject> loadByKitVersionId(long kitVersionId, int size, int page);

    List<Attribute> loadAllByIdsAndKitVersionId(List<Long> attributeIds, long kitVersionId);

    List<Attribute> loadUnimpactedAttributes(long kitVersionId);
}
