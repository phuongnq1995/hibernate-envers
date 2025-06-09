package org.phuongnq.hibernate_envers.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Getter
@Setter
@Table(name = "stores")
@Audited(withModifiedFlag = true)
@EntityListeners(AuditingEntityListener.class)
public class Store {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column
    private String name;

    @OneToMany(mappedBy = "store")
    private Set<Campaign> campaigns;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_address_id", unique = true)
    private Address masterAddress;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id", unique = true)
    private Address deliveryAddress;

    @ManyToMany
    @JoinTable(
            name = "stores_languages",
            joinColumns = @JoinColumn(name = "storeId"),
            inverseJoinColumns = @JoinColumn(name = "languageId")
    )
    private Set<Language> languages;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastUpdated;
}
