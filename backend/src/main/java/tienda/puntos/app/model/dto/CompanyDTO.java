package tienda.puntos.app.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tienda.puntos.app.repository.entity.Company;
import tienda.puntos.app.utils.SubscriptionStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder // Util para crear objetos rapidamente
public class CompanyDTO {

    private Long id;
    private UserDTO ownerDTO;
    private PlanDTO planDTO;
    private String legalName;
    private String cif;
    private SubscriptionStatus subscriptionStatus;
    private LocalDateTime nextBillingDate;

    public static CompanyDTO convertToDTO(Company entity) {
        if (entity == null)
            return null;

        return CompanyDTO.builder()
                .id(entity.getId())
                .ownerDTO(UserDTO.convertToDTO(entity.getOwner()))
                .planDTO(PlanDTO.convertToDTO(entity.getPlan()))
                .legalName(entity.getLegalName())
                .cif(entity.getCif())
                .subscriptionStatus(entity.getSubscriptionStatus())
                .nextBillingDate(entity.getNextBillingDate())
                .build();
    }

    public static Company convertToEntity(CompanyDTO dto) {
        if (dto == null)
            return null;

        Company entity = new Company();
        entity.setId(dto.getId());
        entity.setOwner(UserDTO.convertToEntity(dto.getOwnerDTO()));
        entity.setPlan(PlanDTO.convertToEntity(dto.getPlanDTO()));
        entity.setLegalName(dto.getLegalName());
        entity.setCif(dto.getCif());
        entity.setSubscriptionStatus(dto.getSubscriptionStatus());
        entity.setNextBillingDate(dto.getNextBillingDate());

        // Nota: El Set<Store> no se suele pasar en el DTO de creación para evitar
        // ciclos
        return entity;
    }
}