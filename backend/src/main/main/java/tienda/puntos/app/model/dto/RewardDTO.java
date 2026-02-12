package tienda.puntos.app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tienda.puntos.app.repository.entity.Reward;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RewardDTO {

    private Long id;
    private Long storeId; // Usamos el ID para evitar redundancia y ciclos
    private String name;
    private String description;
    private int pointsCost;
    private String imageUrl;
    private boolean isVisible;

    /**
     * Convierte la Entidad a DTO
     */
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

    /**
     * Convierte el DTO a Entidad
     */
    public static Reward convertToEntity(RewardDTO dto) {
        if (dto == null)
            return null;

        Reward entity = new Reward();
        entity.setId(dto.getId());
        // El setStore(Store) se suele manejar en el Service buscando por
        // dto.getStoreId()
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPointsCost(dto.getPointsCost());
        entity.setImageUrl(dto.getImageUrl());
        entity.setVisible(dto.isVisible());

        return entity;
    }
}
