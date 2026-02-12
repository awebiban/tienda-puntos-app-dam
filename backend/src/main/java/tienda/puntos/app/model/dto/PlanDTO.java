package tienda.puntos.app.model.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tienda.puntos.app.repository.entity.Plan;
import tienda.puntos.app.utils.Plans;
import tienda.puntos.app.utils.Views;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanDTO {

    @JsonView(Views.Detalle.class)
    private Long id;

    @JsonView(Views.Resumen.class)
    private Plans planName;

    @JsonView(Views.Resumen.class)
    private BigDecimal price;

    @JsonView(Views.Resumen.class)
    private int maxStores;

    @JsonView(Views.Resumen.class)
    private int maxUsers;

    @JsonView(Views.Resumen.class)
    private boolean active;

    /**
     * Convierte la Entidad a DTO
     */
    public static PlanDTO convertToDTO(Plan entity) {
        if (entity == null)
            return null;

        return PlanDTO.builder()
                .id(entity.getId())
                .planName(entity.getPlanName())
                .price(entity.getPrice())
                .maxStores(entity.getMaxStores())
                .maxUsers(entity.getMaxUsers())
                .active(entity.isActive())
                .build();
    }

    /**
     * Convierte el DTO a Entidad
     */
    public static Plan convertToEntity(PlanDTO dto) {
        if (dto == null)
            return null;

        Plan entity = new Plan();
        entity.setId(dto.getId());
        entity.setPlanName(dto.getPlanName());
        entity.setPrice(dto.getPrice());
        entity.setMaxStores(dto.getMaxStores());
        entity.setMaxUsers(dto.getMaxUsers());
        entity.setActive(dto.isActive());

        // No mapeamos la lista de compañías aquí por rendimiento y seguridad
        return entity;
    }
}