package tienda.puntos.app.repository.entity;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tienda.puntos.app.utils.SubscriptionStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Companies")
public class Companies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Owner relacion 1 a 1
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "company")
    @JsonIgnore
    @Column(name = "owner_id")
    private Users owner; // Usuario dueño de compañia

    // Plan relacion 1 a 1
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "company")
    @JsonIgnore
    @Column(name = "plan_id")
    private Plan plan;

    @Column(name = "legal_name")
    private String legalName; // Razón Social
    private String cif; // CIF o NIF

    @Column(name = "subscription_status")
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus SubscriptionStatus; // ACTIVE, PAST_DUE, CANCELLED;

    @Column(name = "next_billing_date")
    private Date nextBillingDate; // En principio se actualiza en bbdd

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "company")
    @JsonIgnore
    private Set<Store> stores = new HashSet<>();
}
