package ma.akwa.portalrh.common.enums;

public enum DeliveryStatus {
    SCHEDULED("Planifiée"),
    IN_PROGRESS("En cours"),
    DELIVERED("Livrée"),
    CANCELLED("Annulée"),
    FAILED("Échouée");

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

