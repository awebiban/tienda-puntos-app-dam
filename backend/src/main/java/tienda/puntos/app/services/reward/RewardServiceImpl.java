package tienda.puntos.app.services.reward;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tienda.puntos.app.model.dto.RewardDTO;
import tienda.puntos.app.repository.dao.RewardRepository;

@Service
public class RewardServiceImpl implements RewardService {

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
        return RewardDTO.convertToDTO(this.rewardRepository.save(RewardDTO.convertToEntity(rewardDTO)));
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
