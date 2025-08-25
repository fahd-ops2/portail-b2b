package ma.akwa.portalrh.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class RecommendationResponse {
    private int client_id;
    private List<RecommendationItem> recommendations;

}