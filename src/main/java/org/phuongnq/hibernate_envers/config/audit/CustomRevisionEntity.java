package org.phuongnq.hibernate_envers.config.audit;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.ModifiedEntityNames;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Setter
@Getter
@Entity(name = "CustomRevisionEntity")
@Table(name = "tracking_rev_info")
@RevisionEntity(CustomTrackingRevisionListener.class)
public class CustomRevisionEntity {

  @Id
  @GeneratedValue
  @RevisionNumber
  private int id;

  @RevisionTimestamp
  private long timestamp;

  private UUID userId;

  private String module;

  @ElementCollection(fetch = FetchType.EAGER)
  @JoinTable(
      name = "REVCHANGES",
      joinColumns = {@JoinColumn(name = "REV")}
  )
  @Column(name = "ENTITYNAME")
  @Fetch(FetchMode.JOIN)
  @ModifiedEntityNames
  private Set<String> modifiedEntityNames = new HashSet();

  public void addModifiedEntityType(String type) {
    modifiedEntityNames.add(type);
  }
}