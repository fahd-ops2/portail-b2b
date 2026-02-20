package ma.akwa.portalrh.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class RecommendationService {

    @Value("${recommendation.api.url}")
    private String recommendationApiUrl;

    private final RestTemplate restTemplate;

    public RecommendationService() {
        this.restTemplate = new RestTemplate();
    }

    public RecommendationResponse getRecommendations(RecommendationRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        
        HttpEntity<RecommendationRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<RecommendationResponse> response = restTemplate.exchange(
                recommendationApiUrl,
                HttpMethod.POST,
                entity,
                RecommendationResponse.class);

        return response.getBody();
    }
}
