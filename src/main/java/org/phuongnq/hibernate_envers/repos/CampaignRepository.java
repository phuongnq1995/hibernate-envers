package org.phuongnq.hibernate_envers.repos;

import java.util.UUID;
import org.phuongnq.hibernate_envers.domain.Campaign;
import org.phuongnq.hibernate_envers.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CampaignRepository extends JpaRepository<Campaign, UUID> {

    Campaign findFirstByStore(Store store);

}
