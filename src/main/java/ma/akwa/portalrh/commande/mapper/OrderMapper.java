package ma.akwa.portalrh.commande.mapper;

import ma.akwa.portalrh.client.entities.Client;
import ma.akwa.portalrh.commande.dto.OrderRequestDTO;
import ma.akwa.portalrh.commande.dto.OrderResponseDTO;
import ma.akwa.portalrh.commande.entities.Order;
import ma.akwa.portalrh.commande.entities.OrderItem;
import ma.akwa.portalrh.produit.entities.Produit;
import ma.akwa.portalrh.produit.mapper.ProduitMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProduitMapper.class})
public interface OrderMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(source = "items", target = "items")
    @Mapping(source = "clientId", target = "client", qualifiedByName = "mapClientIdToClient")
    Order toOrder(OrderRequestDTO dto);

    List<OrderItem> mapOrderLines(List<OrderRequestDTO.OrderLineDTO> orderLines);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "produit", source = "productId", qualifiedByName = "mapProductIdToProduit")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "unitPrice", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    @Mapping(target = "note", ignore = true)
    OrderItem toOrderItem(OrderRequestDTO.OrderLineDTO orderLineDTO);

    @Mapping(source = "status", target = "status", qualifiedByName = "orderStatusToString")
    @Mapping(source = "items", target = "items")
    @Mapping(source = "client.id", target = "clientId")
    OrderResponseDTO toOrderResponseDTO(Order order);

    List<OrderResponseDTO.OrderLineDTO> mapOrderItemsToOrderLineDTOs(List<OrderItem> items);

    @Mapping(source = "produit.id", target = "productId")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "produit.nom", target = "label")
    OrderResponseDTO.OrderLineDTO toOrderLineDTO(OrderItem orderItem);

    @Named("mapProductIdToProduit")
    default Produit mapProductIdToProduit(String productId) {
        if (productId == null) {
            return null;
        }
        Produit produit = new Produit();
        produit.setId(productId);
        return produit;
    }

    @Named("orderStatusToString")
    default String orderStatusToString(Enum<?> status) {
        return status == null ? null : status.name();
    }

    @Named("mapClientIdToClient")
    default Client mapClientIdToClient(Long clientId) {
        if (clientId == null) {
            return null;
        }
        Client client = new Client();
        client.setId(clientId);
        // Ajout de l'adresse par défaut (à récupérer depuis la base si nécessaire)
        client.setAddress("Adresse par défaut"); // Remplacez par la logique réelle si disponible
        return client;
    }
}