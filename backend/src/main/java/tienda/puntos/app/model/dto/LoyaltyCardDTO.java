package tienda.puntos.app.model.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tienda.puntos.app.repository.entity.LoyaltyCard;
import tienda.puntos.app.utils.Views;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoyaltyCardDTO {

    @JsonView(Views.Resumen.class)
    private Long id;

    @JsonView(Views.Detalle.class)
    @JsonIgnoreProperties({ "loyaltyCards", "password", "active" })
    private UserDTO userDTO;

    @JsonView(Views.Detalle.class)
    @JsonIgnoreProperties({ "companyDTO", "rewardsList", "isVisible" })
    private StoreDTO storeDTO;

    @JsonView(Views.Resumen.class)
    private int currentBalance;

    @JsonView(Views.Resumen.class)
    private int totalAccumulated;

    @JsonView(Views.Resumen.class)
    private LocalDateTime lastVisited;

    @JsonView(Views.Detalle.class)
    @JsonIgnore
    @JsonIgnoreProperties({ "transactionList" })
    private Set<TransactionDTO> transactionList;

    /**
     * Convierte la Entidad a DTO
     */
    public static LoyaltyCardDTO convertToDTO(LoyaltyCard entity) {
        if (entity == null)
            return null;

        return LoyaltyCardDTO.builder()
                .id(entity.getId())
                .userDTO(UserDTO.convertToDTO(entity.getUser()))
                .storeDTO(StoreDTO.convertToDTO(entity.getStore()))
                .currentBalance(entity.getCurrentBalance())
                .totalAccumulated(entity.getTotalAccumulated())
                .lastVisited(entity.getLastVisited())
                // Convertimos el Set de transacciones solo si no es nulo
                .transactionList(entity.getTransactionList() != null ? entity.getTransactionList().stream()
                        .map(TransactionDTO::convertToDTO)
                        .collect(Collectors.toSet()) : null)
                .build();
    }

    /**
     * Convierte el DTO a Entidad
     */
    public static LoyaltyCard convertToEntity(LoyaltyCardDTO dto) {
        if (dto == null)
            return null;

        LoyaltyCard entity = new LoyaltyCard();
        entity.setId(dto.getId());
        entity.setUser(UserDTO.convertToEntity(dto.getUserDTO()));
        entity.setStore(StoreDTO.convertToEntity(dto.getStoreDTO()));
        entity.setCurrentBalance(dto.getCurrentBalance());
        entity.setTotalAccumulated(dto.getTotalAccumulated());
        entity.setLastVisited(dto.getLastVisited());

        // Las transacciones normalmente se gestionan a través de su propio servicio,
        // pero las mapeamos si es necesario.
        if (dto.getTransactionList() != null) {
            entity.setTransactionList(dto.getTransactionList().stream()
                    .map(TransactionDTO::convertToEntity)
                    .collect(Collectors.toSet()));
        }

        return entity;
    }
}
