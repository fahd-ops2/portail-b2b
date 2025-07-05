package ma.akwa.portalrh.livraison.mapper;

import ma.akwa.portalrh.commande.entities.Order;
import ma.akwa.portalrh.livraison.dto.DeliveryRequest;
import ma.akwa.portalrh.livraison.dto.DeliveryResponse;
import ma.akwa.portalrh.livraison.entities.Delivery;
import ma.akwa.portalrh.livreur.entities.Livreur;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    // Map from DeliveryRequest to Delivery entity
    @Mapping(target = "id", ignore = true) // ID auto-généré
    @Mapping(target = "order", source = "orderId", qualifiedByName = "mapOrderIdToOrder")
    @Mapping(target = "livreur", source = "livreurId", qualifiedByName = "mapLivreurIdToLivreur")
    @Mapping(target = "status", source = "status")
    Delivery toEntity(DeliveryRequest request);

    // Map from Delivery entity to DeliveryResponse
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "livreur.id", target = "livreurId")
    @Mapping(source = "status", target = "status")
    DeliveryResponse toResponse(Delivery delivery);

    // Custom mapping for orderId -> Order entity
    @Named("mapOrderIdToOrder")
    default Order mapOrderIdToOrder(Long orderId) {
        if (orderId == null) {
            return null;
        }
        Order order = new Order();
        order.setId(orderId);
        return order;
    }

    // Custom mapping for livreurId -> Livreur entity
    @Named("mapLivreurIdToLivreur")
    default Livreur mapLivreurIdToLivreur(Long livreurId) {
        if (livreurId == null) {
            return null;
        }
        Livreur livreur = new Livreur();
        livreur.setId(livreurId);
        return livreur;
    }
}