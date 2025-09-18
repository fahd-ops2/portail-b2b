package ma.akwa.portalrh.reclamation.mapper;

import ma.akwa.portalrh.client.entities.Client;
import ma.akwa.portalrh.client.repository.ClientRepository;
import ma.akwa.portalrh.commande.entities.Order;
import ma.akwa.portalrh.commande.repository.OrderRepository;
import ma.akwa.portalrh.reclamation.dto.ReclamationRequest;
import ma.akwa.portalrh.reclamation.dto.ReclamationResponse;
import ma.akwa.portalrh.reclamation.entities.Reclamation;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import ma.akwa.portalrh.common.enums.TypeRc;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public abstract class ReclamationMapper {

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected ClientRepository clientRepository;

    @Mapping(target = "order", source = "orderId", qualifiedByName = "mapOrderIdToOrder")
    @Mapping(target = "client", source = "clientId", qualifiedByName = "mapClientIdToClient")
    @Mapping(target = "createdAt", ignore = true) // Géré par @PrePersist
    @Mapping(target = "status", ignore = true) // Défaut dans l'entité
    @Mapping(target = "filePath", ignore = true) // Géré dans le service
    public abstract Reclamation toEntity(ReclamationRequest request);

    @AfterMapping
    protected void mapType(@MappingTarget Reclamation reclamation, ReclamationRequest request) {
        if (request.getType() != null && !request.getType().isBlank()) {
            reclamation.setType(TypeRc.valueOf(request.getType()));
        }
    }

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "type", source = "type", qualifiedByName = "enumToString")
    @Mapping(target = "date", source = "createdAt", qualifiedByName = "mapLocalDateTimeToLocalDate")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "mapLocalDateTimeToLocalDate")
    public abstract ReclamationResponse toResponse(Reclamation reclamation);

    @Mapping(target = "order", source = "orderId", qualifiedByName = "mapOrderIdToOrder")
    @Mapping(target = "client", source = "clientId", qualifiedByName = "mapClientIdToClient")
    @Mapping(target = "createdAt", source = "date", qualifiedByName = "mapLocalDateToLocalDateTime")
    @Mapping(target = "filePath", ignore = true)
    public abstract void updateEntityFromRequest(ReclamationRequest request, @MappingTarget Reclamation reclamation);

    @AfterMapping
    protected void mapTypeUpdate(@MappingTarget Reclamation reclamation, ReclamationRequest request) {
        if (request.getType() != null && !request.getType().isBlank()) {
            reclamation.setType(TypeRc.valueOf(request.getType()));
        }
    }

    @Named("enumToString")
    protected String enumToString(TypeRc type) {
        return type != null ? type.name() : null;
    }

    @Named("mapOrderIdToOrder")
    protected Order mapOrderIdToOrder(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            return null;
        }
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande avec l'ID " + orderId + " non trouvée"));
    }

    @Named("mapClientIdToClient")
    protected Client mapClientIdToClient(Long clientId) {
        if (clientId == null) {
            return null;
        }
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client avec l'ID " + clientId + " non trouvé"));
    }

    @Named("mapLocalDateToLocalDateTime")
    protected LocalDateTime mapLocalDateToLocalDateTime(LocalDate date) {
        return date != null ? date.atStartOfDay() : LocalDateTime.now();
    }

    @Named("mapLocalDateTimeToLocalDate")
    protected LocalDate mapLocalDateTimeToLocalDate(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate() : null;
    }
}