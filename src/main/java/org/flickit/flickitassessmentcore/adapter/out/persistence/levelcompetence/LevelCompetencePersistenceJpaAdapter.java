package org.flickit.flickitassessmentcore.adapter.out.persistence.levelcompetence;

import lombok.RequiredArgsConstructor;
import org.flickit.flickitassessmentcore.application.port.out.levelcompetence.LoadLevelCompetenceByMaturityLevelPort;
import org.flickit.flickitassessmentcore.domain.LevelCompetence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class LevelCompetencePersistenceJpaAdapter implements LoadLevelCompetenceByMaturityLevelPort {

    @Value("${flickit-platform.host}")
    private String flickitPlatformHost;

    @Override
    public Result loadLevelCompetenceByMaturityLevelId(Param param) {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofSeconds(10))
            .setReadTimeout(Duration.ofSeconds(10))
            .messageConverters(new MappingJackson2HttpMessageConverter());
        RestTemplate restTemplate = restTemplateBuilder.build();
        String url = String.format("%s/api/internal/maturitylevel/%d/levelcompetence", flickitPlatformHost, param.maturityLevelId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Long>> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<List<LevelCompetenceDto>> responseEntity = restTemplate.exchange(
            url,
            HttpMethod.GET,
            requestEntity,
            new ParameterizedTypeReference<List<LevelCompetenceDto>>() {
            }
        );
        return LevelCompetenceMapper.toResult(responseEntity.getBody());
    }

    record LevelCompetenceDto(Long id,
                              Long maturityLevelId,
                              Integer value,
                              Long maturityLevelCompetenceId) {}

}
