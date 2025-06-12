package org.phuongnq.hibernate_envers.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableJpaRepositories("org.phuongnq.hibernate_envers.repos")
@EnableTransactionManagement
public class DomainConfig {
}
