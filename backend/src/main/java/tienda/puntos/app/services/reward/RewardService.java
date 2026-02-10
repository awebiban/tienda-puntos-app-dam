package tienda.puntos.app.services.reward;

import java.util.List;

import tienda.puntos.app.model.dto.RewardDTO;

public interface RewardService {

    List<RewardDTO> findAllRewards();

    RewardDTO findRewardById(Long rewardId);

    List<RewardDTO> findRewardsByStore(Long storeId);

    void activeReward(Long rewardId);

    void disableReward(Long rewardId);

    RewardDTO saveReward(RewardDTO rewardDTO);

    RewardDTO updateReward(Long id, RewardDTO rewardDTO);

}
