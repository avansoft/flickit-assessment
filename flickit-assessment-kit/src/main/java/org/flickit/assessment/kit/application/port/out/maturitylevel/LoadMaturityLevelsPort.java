package org.flickit.assessment.kit.application.port.out.maturitylevel;


import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.common.exception.ResourceNotFoundException;
import org.flickit.assessment.kit.application.domain.MaturityLevel;

import java.util.Collection;
import java.util.List;

public interface LoadMaturityLevelsPort {

    /**
     * Loads a page of maturity levels associated with a specific kit version ID,
     * ordered by their index.
     *
     * @param kitVersionId The kitVersionId of the kit for which maturity levels are to be loaded.
     * @param size The size of page
     * @param page The number of page
     * @return A page of maturity level associated with the given kit version ID, ordered by index.
     * @throws ResourceNotFoundException if the kit version ID is not found.
     */
    PaginatedResponse<MaturityLevel> loadByKitVersionId(long kitVersionId, Integer size, Integer page);

    /**
     * Loads all maturity levels associated with a specific kit version ID,
     * ordered by their index.
     *
     * @param kitVersionId The kitVersionId of the kit for which maturity levels are to be loaded.
     * @return A list of all maturity level associated with the given kit version ID, ordered by index.
     * @throws ResourceNotFoundException if the kit version ID is not found.
     */
    List<MaturityLevel> loadAllByKitVersionId(Long kitVersionId);

    /**
     * Loads maturity levels associated with a specific kit version ID,
     * ordered by their index.
     *
     * @param kitVersionId The kitVersionId of the kit for which maturity levels are to be loaded.
     * @param ids          The collection of maturity levels id
     * @return A list of maturity level associated with the given kit version ID, ordered by index.
     * @throws ResourceNotFoundException if the kit version ID is not found.
     */
    List<MaturityLevel> loadByKitVersionId(long kitVersionId, Collection<Long> ids);
}
