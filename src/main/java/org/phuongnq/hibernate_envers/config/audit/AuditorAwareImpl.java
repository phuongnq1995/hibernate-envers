package org.phuongnq.hibernate_envers.config.audit;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;

public class AuditorAwareImpl implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    String auditor = "System";

    return Optional.of(auditor);
  }
}
