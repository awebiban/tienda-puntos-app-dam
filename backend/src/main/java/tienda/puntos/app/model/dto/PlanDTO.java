package tienda.puntos.app.model.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tienda.puntos.app.repository.entity.Plan;
import tienda.puntos.app.utils.Plans;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanDTO {

    private Long id;
    private Plans planName;
    private BigDecimal price;
    private int maxStores;
    private int maxUsers;
    private boolean active;

    public static PlanDTO convertToDTO(Plan p1) {
        PlanDTO p2 = new PlanDTO();
        p2.setId(p1.getId());
        p2.setPlanName(p1.getPlanName());
        p2.setPrice(p1.getPrice());
        p2.setMaxStores(p1.getMaxStores());
        p2.setMaxUsers(p1.getMaxUsers());
        p2.setActive(p1.isActive());
        return p2;
    }

    public static Plan convertToEntity(PlanDTO p1) {
        Plan p2 = new Plan();
        p2.setId(p1.getId());
        p2.setPlanName(p1.getPlanName());
        p2.setPrice(p1.getPrice());
        p2.setMaxStores(p1.getMaxStores());
        p2.setMaxUsers(p1.getMaxUsers());
        p2.setActive(p1.isActive());
        return p2;
    }
}