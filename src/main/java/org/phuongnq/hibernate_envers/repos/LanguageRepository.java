package org.phuongnq.hibernate_envers.repos;

import java.util.UUID;
import org.phuongnq.hibernate_envers.domain.Language;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LanguageRepository extends JpaRepository<Language, UUID> {
}
