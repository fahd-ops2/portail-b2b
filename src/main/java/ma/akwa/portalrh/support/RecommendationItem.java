package ma.akwa.portalrh.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class RecommendationItem {
    private int product_id;
    private double score;

}
