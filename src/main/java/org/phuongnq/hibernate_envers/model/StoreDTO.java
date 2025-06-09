package org.phuongnq.hibernate_envers.model;

import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;


public class StoreDTO {

    private UUID id;

    @Size(max = 255)
    private String name;

    @StoreMasterAddressUnique
    private UUID masterAddress;

    @StoreDeliveryAddressUnique
    private UUID deliveryAddress;

    private List<UUID> languages;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public UUID getMasterAddress() {
        return masterAddress;
    }

    public void setMasterAddress(final UUID masterAddress) {
        this.masterAddress = masterAddress;
    }

    public UUID getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(final UUID deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<UUID> getLanguages() {
        return languages;
    }

    public void setLanguages(final List<UUID> languages) {
        this.languages = languages;
    }

}
