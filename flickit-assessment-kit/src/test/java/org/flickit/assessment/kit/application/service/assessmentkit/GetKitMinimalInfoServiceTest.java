package org.flickit.assessment.kit.application.service.assessmentkit;

import org.flickit.assessment.kit.application.port.out.assessmentkit.LoadKitMinimalInfoPort;
import org.flickit.assessment.kit.application.port.in.assessmentkit.GetKitMinimalInfoUseCase.Result;
import org.flickit.assessment.kit.application.port.in.assessmentkit.GetKitMinimalInfoUseCase.Param;
import org.flickit.assessment.kit.application.port.in.assessmentkit.GetKitMinimalInfoUseCase.ExpertGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class GetKitMinimalInfoServiceTest {

    @Mock
    private LoadKitMinimalInfoPort loadKitMinimalInfoPort;
    @InjectMocks
    private GetKitMinimalInfoService service;

    @Test
    void testGetKitMinimalInfo() {
        // Arrange
        Param param = new Param(1L);
        Result expectedResult = new Result(1L, "title1", new ExpertGroup(1L, "eTitle1"));

        Mockito.when(loadKitMinimalInfoPort.loadKitMinimalInfo(param.getKitId())).thenReturn(expectedResult);
        // Act
        Result actualResult = service.getKitMinimalInfo(param);

        // Assert
        assertThat(actualResult).isEqualTo(expectedResult);
        Mockito.verify(loadKitMinimalInfoPort, Mockito.times(1)).loadKitMinimalInfo(param.getKitId());
    }
}
