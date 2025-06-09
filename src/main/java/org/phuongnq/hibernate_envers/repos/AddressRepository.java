package org.phuongnq.hibernate_envers.repos;

import java.util.UUID;
import org.phuongnq.hibernate_envers.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepository extends JpaRepository<Address, UUID> {
}
