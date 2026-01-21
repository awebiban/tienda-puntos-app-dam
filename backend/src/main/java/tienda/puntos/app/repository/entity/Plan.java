package tienda.puntos.app.repository.entity;

import java.math.BigDecimal;
import java.util.Set;
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
import tienda.puntos.app.utils.Plans;

@Entity
@Table(name = "plans")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private Plans planName;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "max_stores", nullable = false)
    private int maxStores;

    @Column(name = "max_users", nullable = false)
    private int maxUsers;

    @Column(nullable = false)
    private boolean active;

    // Relación 1 Plan -> N Companies
    @OneToMany(mappedBy = "plan", fetch = FetchType.LAZY)
    private Set<Company> companiesList;
}
