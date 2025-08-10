package ma.akwa.portalrh.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientOrderItem {
    @JsonProperty("client_id")
    private int clientId;
    @JsonProperty("product_id")
    private int productId;
    private Integer quantity;
}
