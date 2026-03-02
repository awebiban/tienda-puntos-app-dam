package tienda.puntos.app.model.dto;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tienda.puntos.app.repository.entity.Reward;
import tienda.puntos.app.utils.Views;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RewardDTO {

    @JsonView(Views.Detalle.class)
    private Long id;

    @JsonView(Views.Detalle.class)
    private Long storeId;

    @JsonView(Views.Resumen.class)
    private String name;

    @JsonView(Views.Resumen.class)
    private String description;

    @JsonView(Views.Resumen.class)
    private int pointsCost;

    @JsonView(Views.Resumen.class)
    private String imageUrl;

    @JsonView(Views.Resumen.class)
    private boolean isVisible;


    public static RewardDTO convertToDTO(Reward entity) {
        if (entity == null)
            return null;

        return RewardDTO.builder()
                .id(entity.getId())
                .storeId(entity.getStore() != null ? entity.getStore().getId() : null)
                .name(entity.getName())
                .description(entity.getDescription())
                .pointsCost(entity.getPointsCost())
                .imageUrl(entity.getImageUrl())
                .isVisible(entity.isVisible())
                .build();
    }


    public static Reward convertToEntity(RewardDTO dto) {
        if (dto == null)
            return null;

        Reward entity = new Reward();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPointsCost(dto.getPointsCost());
        entity.setImageUrl(dto.getImageUrl());
        entity.setVisible(dto.isVisible());

        return entity;
    }
}
