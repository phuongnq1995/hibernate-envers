package org.phuongnq.hibernate_envers.repos;

import java.util.List;
import java.util.UUID;
import org.phuongnq.hibernate_envers.domain.Address;
import org.phuongnq.hibernate_envers.domain.Language;
import org.phuongnq.hibernate_envers.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StoreRepository extends JpaRepository<Store, UUID> {

    Store findFirstByMasterAddress(Address address);

    Store findFirstByDeliveryAddressAndIdNot(Store store, final UUID id);

    Store findFirstByLanguages(Language language);

    List<Store> findAllByLanguages(Language language);

    boolean existsByMasterAddressId(UUID id);

    boolean existsByDeliveryAddressId(UUID id);

}
