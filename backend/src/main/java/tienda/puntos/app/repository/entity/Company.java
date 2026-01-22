package tienda.puntos.app.repository.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tienda.puntos.app.utils.SubscriptionStatus;

@Entity
@Table(name = "companies", uniqueConstraints = {
        @UniqueConstraint(columnNames = "owner_id")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Dueño de la empresa fiscal (1 a 1)
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    /**
     * Plan contratado
     * N Companies -> 1 Plan
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Column(name = "legal_name", nullable = false)
    private String legalName;

    @Column(name = "tax_id", nullable = false, unique = true)
    private String cif;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_status", nullable = false)
    private SubscriptionStatus subscriptionStatus;

    @Column(name = "next_billing_date")
    private LocalDateTime nextBillingDate;

    /**
     * 1 Company -> N Stores
     */
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Store> stores = new HashSet<>();
}