package tienda.puntos.app.model.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tienda.puntos.app.repository.entity.LoyaltyCard;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoyaltyCardDTO {

    private Long id;
    private UserDTO userDTO;
    private StoreDTO storeDTO;
    private int currentBalance;
    private int totalAccumulated;
    private LocalDateTime lastVisited;

    // Lista de DTOs para evitar exponer la entidad Transaction directamente
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
