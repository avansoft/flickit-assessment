package org.flickit.assessment.kit.application.service.assessmentkit;

import org.flickit.assessment.common.application.domain.crud.PaginatedResponse;
import org.flickit.assessment.kit.application.domain.AssessmentKit;
import org.flickit.assessment.kit.application.port.in.assessmentkit.SearchKitOptionsUseCase;
import org.flickit.assessment.kit.application.port.out.assessmentkit.SearchKitOptionsPort;
import org.flickit.assessment.kit.test.fixture.application.AssessmentKitMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchKitOptionsServiceTest {

    @InjectMocks
    private SearchKitOptionsService service;

    @Mock
    private SearchKitOptionsPort port;

    @Test
    void testSearchKitOptions_ValidInput_ValidResult() {
        var param = createParam(SearchKitOptionsUseCase.Param.ParamBuilder::build);

        AssessmentKit assessmentKit = AssessmentKitMother.simpleKit();
        List<AssessmentKit> items = List.of(assessmentKit);
        PaginatedResponse<AssessmentKit> kitPaginatedResponse = new PaginatedResponse<>(items,
            0,
            10,
            Sort.Direction.ASC.name().toLowerCase(),
            "title",
            items.size());

        when(port.searchKitOptions(any(SearchKitOptionsPort.Param.class))).thenReturn(kitPaginatedResponse);

        var response = service.searchKitOptions(param);

        for (SearchKitOptionsUseCase.KitListItem item: response.getItems()) {
            assertEquals(assessmentKit.getId(), item.id());
            assertEquals(assessmentKit.getTitle(), item.title());
        }
    }

    private SearchKitOptionsUseCase.Param createParam(Consumer<SearchKitOptionsUseCase.Param.ParamBuilder> changer) {
        var paramBuilder = paramBuilder();
        changer.accept(paramBuilder);
        return paramBuilder.build();
    }

    private SearchKitOptionsUseCase.Param.ParamBuilder paramBuilder() {
        return SearchKitOptionsUseCase.Param.builder()
            .query("query")
            .page(0)
            .size(50)
            .currentUserId(UUID.randomUUID());
    }
}
