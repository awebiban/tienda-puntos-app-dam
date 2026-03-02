package tienda.puntos.app.services.reward;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tienda.puntos.app.model.dto.RewardDTO;
import tienda.puntos.app.repository.dao.RewardRepository;
import tienda.puntos.app.repository.entity.Reward;
import tienda.puntos.app.repository.entity.Store;

@Service
public class RewardServiceImpl implements RewardService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RewardRepository rewardRepository;

    @Override
    public List<RewardDTO> findAllRewards() {
        return this.rewardRepository.findAll()
                .stream()
                .map(RewardDTO::convertToDTO)
                .toList();
    }

    @Override
    public RewardDTO findRewardById(Long rewardId) {
        return this.rewardRepository.findById(rewardId)
                .map(RewardDTO::convertToDTO)
                .orElseThrow(() -> new RuntimeException(
                        "Error: La recomenpensa con ID" + rewardId + " no existe o no se encuentra"));
    }

    @Override
    public List<RewardDTO> findRewardsByStore(Long storeId) {
        return this.rewardRepository.findRewardsByStore(storeId)
                .stream()
                .map(RewardDTO::convertToDTO)
                .toList();
    }

    @Override
    public void activeReward(Long rewardId) {
        this.rewardRepository.activeReward(rewardId);
    }

    @Override
    public void disableReward(Long rewardId) {
        this.rewardRepository.disableReward(rewardId);
    }

    @Override
    public RewardDTO saveReward(RewardDTO rewardDTO) {
        // 1. Convertimos el DTO a Entidad
        Reward entity = RewardDTO.convertToEntity(rewardDTO);

        // 2. Creamos el "objeto hueco" (Proxy) de la tienda usando el ID que viene de
        // Angular
        // Nota: 'entityManager' debe estar inyectado en tu servicio con
        // @PersistenceContext
        Store storeProxy = entityManager.getReference(Store.class, rewardDTO.getStoreId());

        // 3. Se lo asignamos a la entidad. Ahora 'entity.getStore()' ya no es NULL.
        entity.setStore(storeProxy);

        // 4. Al guardar, Hibernate solo usará el ID del proxy para la columna store_id
        Reward saved = rewardRepository.save(entity);

        return RewardDTO.convertToDTO(saved);
    }

    @Override
    public RewardDTO updateReward(Long id, RewardDTO rewardDTO) {
        return this.rewardRepository.findById(id)
                .map(reward -> {
                    reward.setName(rewardDTO.getName());
                    reward.setDescription(rewardDTO.getDescription());
                    reward.setPointsCost(rewardDTO.getPointsCost());
                    reward.setImageUrl(createImageFileName(id, rewardDTO.getName(), rewardDTO.getImageUrl()));
                    return RewardDTO.convertToDTO(this.rewardRepository.save(reward));
                })
                .orElseThrow(() -> new RuntimeException(
                        "Error: La recomenpensa con ID" + id + " no existe o no se encuentra"));
    }

    private String createImageFileName(Long rewardId, String rewardName, String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return "reward_" + rewardId + "_" + rewardName.replaceAll("\\s+", "_") + extension;
    }

}
