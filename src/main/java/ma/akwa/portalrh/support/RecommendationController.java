package ma.akwa.portalrh.support;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping()
    public RecommendationResponse recommend(@RequestBody RecommendationRequest request) {
        return recommendationService.getRecommendations(request);
    }
}

