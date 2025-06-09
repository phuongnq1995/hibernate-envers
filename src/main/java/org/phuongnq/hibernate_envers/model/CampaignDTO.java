package org.phuongnq.hibernate_envers.model;

import jakarta.validation.constraints.Size;
import java.util.UUID;


public class CampaignDTO {

    private UUID id;

    @Size(max = 255)
    private String name;

    private UUID store;

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

    public UUID getStore() {
        return store;
    }

    public void setStore(final UUID store) {
        this.store = store;
    }

}
