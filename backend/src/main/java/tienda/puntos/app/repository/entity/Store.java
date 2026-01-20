package tienda.puntos.app.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Companies company;
}

// IDEA DE ID UNICO PARA LAS LOYALTY CARDS
// id unico y no se repite, nunca.
// tiene convinacion de 128 bits (36 caracteres)

// @Id
// @GeneratedValue(generator = "UUID")
// @GenericGenerator(
// name = "UUID",
// strategy = "org.hibernate.id.UUIDGenerator"
// )
// private UUID id;