package ma.akwa.portalrh.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class RecommendationRequest {
    @JsonProperty("orders")
    private List<ClientOrderItem> clientOrders;
    @JsonProperty("query_client_id")
    private int targetClientId;
    @JsonProperty("top_k")
    private Integer topK;

}
