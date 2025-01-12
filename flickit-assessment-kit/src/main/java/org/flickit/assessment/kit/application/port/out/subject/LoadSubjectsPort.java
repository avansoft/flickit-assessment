package org.flickit.assessment.kit.application.port.out.subject;

import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.kit.application.domain.Subject;

import java.util.List;

public interface LoadSubjectsPort {

    /**
     * Loads subjects associated with a specific kit ID and kit's last version,
     * ordered by their index.
     *
     * @param kitVersionId The kitVersionID of the kit for which subjects are to be loaded.
     * @return A list of subjects associated with the given kit ID, ordered by index.
     * @throws ResourceNotFoundException if the kit ID is not found.
     */
    List<Subject> loadByKitVersionId(long kitVersionId);

    /**
     * Retrieves a paginated list of {@code Subject} entities associated with the specified kit version ID.
     *
     * @param kitVersionId the unique identifier of the kit version to filter the subjects by.
     * @param page the page number to retrieve, starting from 0 (0-based index).
     * @param size the number of records to retrieve per page.
     * @return a {@code PaginatedResponse<Subject>} containing the subjects for the specified kit version and pagination settings.
     */
    PaginatedResponse<Subject> loadPaginatedByKitVersionId(long kitVersionId, int page, int size);

    List<Subject> loadSubjectsWithoutAttribute(long kitVersionId);

    PaginatedResponse<Subject> loadWithAttributesByKitVersionId(long kitVersionId, int page, int size);
}
