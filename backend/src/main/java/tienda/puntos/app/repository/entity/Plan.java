package tienda.puntos.app.repository.entity;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tienda.puntos.app.utils.Plans;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private Plans planName; // Nombre del plan
    private float price; // Precio del plan

    @Column(name = "max_stores")
    private int maxStores; // Numero maximo de tiendas por plan

    @Column(name = "max_users")
    private int maxUsers; // Numero maximo de usuarios por plan
    private boolean active; // Plan activo o no activo

    // Company relacion 1 a 1
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "plan")
    @JsonIgnore
    private Companies company;

}
