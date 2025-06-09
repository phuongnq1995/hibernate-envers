package org.phuongnq.hibernate_envers.config;

import org.phuongnq.hibernate_envers.config.audit.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableJpaRepositories("org.phuongnq.hibernate_envers.repos")
@EnableTransactionManagement
public class DomainConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

  /*@Bean
  public CustomTrackingRevisionListener customRevisionListener() {
    return new CustomTrackingRevisionListener();
  }*/
}
