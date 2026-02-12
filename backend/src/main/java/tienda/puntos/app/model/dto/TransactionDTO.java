package tienda.puntos.app.model.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tienda.puntos.app.repository.entity.Transaction;
import tienda.puntos.app.utils.TransactionType;
import tienda.puntos.app.utils.Views;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {

    @JsonView(Views.Detalle.class)
    private Long id;

    @JsonView(Views.Detalle.class)
    private Long loyaltyCardId; // Usamos ID para evitar bucles infinitos en el JSON

    @JsonView(Views.Detalle.class)
    private int amount;

    @JsonView(Views.Detalle.class)
    private TransactionType type;

    @JsonView(Views.Detalle.class)
    private LocalDateTime createdAt;

    /**
     * Convierte la Entidad a DTO
     */
    public static TransactionDTO convertToDTO(Transaction entity) {
        if (entity == null)
            return null;

        return TransactionDTO.builder()
                .id(entity.getId())
                .loyaltyCardId(entity.getLoyaltyCard() != null ? entity.getLoyaltyCard().getId() : null)
                .amount(entity.getAmount())
                .type(entity.getType())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    /**
     * Convierte el DTO a Entidad
     */
    public static Transaction convertToEntity(TransactionDTO dto) {
        if (dto == null)
            return null;

        Transaction entity = new Transaction();
        entity.setId(dto.getId());
        // Nota: La carga de la LoyaltyCard completa normalmente se hace en el Service
        // mediante el loyaltyCardId para asegurar que viene de la BD.
        entity.setAmount(dto.getAmount());
        entity.setType(dto.getType());
        entity.setCreatedAt(dto.getCreatedAt());

        return entity;
    }
}
