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
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", source = "orderId", qualifiedByName = "mapOrderIdToOrder")
    @Mapping(target = "livreur", source = "livreurId", qualifiedByName = "mapLivreurIdToLivreur")
    @Mapping(target = "scheduledTime", source = "scheduledTime")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "deliveryAddress", source = "deliveryAddress")
    @Mapping(target = "notes", source = "notes")
    @Mapping(target = "isUrgent", source = "isUrgent")
    Delivery toEntity(DeliveryRequest request);

    // Map from Delivery entity to DeliveryResponse
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "livreur.id", target = "livreurId")
    @Mapping(source = "scheduledTime", target = "scheduledTime")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "deliveryAddress", target = "deliveryAddress")
    @Mapping(source = "notes", target = "notes")
    @Mapping(source = "deliveredAt", target = "deliveredAt")
    @Mapping(source = "isUrgent", target = "isUrgent")
    DeliveryResponse toResponse(Delivery delivery);

    @Named("mapOrderIdToOrder")
    default Order mapOrderIdToOrder(String orderId) {
        if (orderId == null) return null;
        Order order = new Order();
        order.setId(orderId);
        return order;
    }

    @Named("mapLivreurIdToLivreur")
    default Livreur mapLivreurIdToLivreur(Long livreurId) {
        if (livreurId == null) return null;
        Livreur livreur = new Livreur();
        livreur.setId(livreurId);
        return livreur;
    }
}