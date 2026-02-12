package tienda.puntos.app.model.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tienda.puntos.app.repository.entity.Store;
import tienda.puntos.app.utils.Views;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreDTO {

    @JsonView(Views.Resumen.class)
    private Long id;

    @JsonIgnoreProperties({ "id", "ownerDTO", "planDTO", "subscriptionStatus", "nextBillingDate" })
    @JsonView(Views.Detalle.class)
    private CompanyDTO companyDTO;

    @JsonView(Views.Resumen.class)
    private String name;

    @JsonView(Views.Resumen.class)
    private String category;

    @JsonView(Views.Resumen.class)
    private String address;

    @JsonView(Views.Resumen.class)
    private String imageUrl;

    @JsonView(Views.Resumen.class)
    private int pointsRatio;

    @JsonView(Views.Resumen.class)
    private boolean isVisible;

    // Incluimos las recompensas para que el cliente sepa qué puede canjear
    @JsonView(Views.Detalle.class)
    @JsonIgnore
    private Set<RewardDTO> rewardsList;

    /**
     * Convierte la Entidad a DTO
     */
    public static StoreDTO convertToDTO(Store entity) {
        if (entity == null)
            return null;

        return StoreDTO.builder()
                .id(entity.getId())
                .companyDTO(CompanyDTO.convertToDTO(entity.getCompany()))
                .name(entity.getName())
                .category(entity.getCategory())
                .address(entity.getAddress())
                .imageUrl(entity.getImageUrl())
                .pointsRatio(entity.getPointsRatio())
                .isVisible(entity.isVisible())
                .rewardsList(entity.getRewardsList() != null ? entity.getRewardsList().stream()
                        .map(RewardDTO::convertToDTO)
                        .collect(Collectors.toSet()) : null)
                .build();
    }

    /**
     * Convierte el DTO a Entidad
     */
    public static Store convertToEntity(StoreDTO dto) {
        if (dto == null)
            return null;

        Store entity = new Store();
        entity.setId(dto.getId());
        entity.setCompany(CompanyDTO.convertToEntity(dto.getCompanyDTO()));
        entity.setName(dto.getName());
        entity.setCategory(dto.getCategory());
        entity.setAddress(dto.getAddress());
        entity.setImageUrl(dto.getImageUrl());
        entity.setPointsRatio(dto.getPointsRatio());
        entity.setVisible(dto.isVisible());

        if (dto.getRewardsList() != null) {
            entity.setRewardsList(dto.getRewardsList().stream()
                    .map(RewardDTO::convertToEntity)
                    .collect(Collectors.toSet()));
        }

        return entity;
    }
}