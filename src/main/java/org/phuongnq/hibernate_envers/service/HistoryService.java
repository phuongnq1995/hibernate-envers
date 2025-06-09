package org.phuongnq.hibernate_envers.service;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.phuongnq.hibernate_envers.config.audit.CustomRevisionEntity;
import org.phuongnq.hibernate_envers.model.mapper.RevisionEntityMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {

  private final EntityManager entityManager;
  private final JdbcClient jdbcClient;

  public void getHistory(String module) {
    List<CustomRevisionEntity> revisions = jdbcClient
        .sql("SELECT * FROM tracking_rev_info WHERE module = :module")
        .param("module", module)
        .query(new RevisionEntityMapper())
        .list();

    for (CustomRevisionEntity revision : revisions) {
      log.info("=========================================");
      log.info("Rev Id: {}, timestamp: {} by userId: {}", revision.getId(), revision.getTimestamp(), revision.getUserId());

      List<String> entityNames = jdbcClient.
          sql("SELECT entityname FROM revchanges WHERE rev = :rev")
          .param("rev", revision.getId())
          .query(String.class)
          .list();

      log.info("================ Details ===================");

      entityNames.forEach(entityName -> {
        try {

          List results = AuditReaderFactory.get(entityManager)
              .createQuery()
              .forRevisionsOfEntityWithChanges(Class.forName(entityName), false)
              .add(AuditEntity.revisionNumber().eq(revision.getId()))
              .getResultList();

          for ( Object entry : results ) {
            final Object[] array = (Object[]) entry;
            final Set<String> propertiesChanged = (Set<String>) array[3];
            log.info("{}, changed properties: {}", entityName, propertiesChanged );
          }
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
        }
      });

      log.info("=========================================");
    }
  }
}
