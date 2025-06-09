package org.phuongnq.hibernate_envers.config.audit;

import java.util.UUID;
import org.hibernate.envers.EntityTrackingRevisionListener;
import org.hibernate.envers.RevisionType;

public class CustomTrackingRevisionListener implements EntityTrackingRevisionListener {

  @Override
  public void entityChanged(Class entityClass,
      String entityName,
      Object entityId,
      RevisionType revisionType,
      Object revisionEntity) {
    String type = entityClass.getName();
    ((CustomRevisionEntity) revisionEntity).addModifiedEntityType(type);
  }

  @Override
  public void newRevision(Object revisionEntity) {
    CustomRevisionEntity customRevisionEntity = (CustomRevisionEntity) revisionEntity;
    customRevisionEntity.setUserId(UUID.randomUUID());
    customRevisionEntity.setModule(CurrentModuleAudit.INSTANCE.get());
  }
}