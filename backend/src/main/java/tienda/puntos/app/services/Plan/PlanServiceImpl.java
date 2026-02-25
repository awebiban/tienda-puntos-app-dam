package tienda.puntos.app.services.Plan;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tienda.puntos.app.model.dto.PlanDTO;
import tienda.puntos.app.repository.dao.PlanRepository;
import tienda.puntos.app.repository.entity.Plan;

@Service
public class PlanServiceImpl implements PlanService {

    @Autowired
    private PlanRepository planRepository;

    @Override
    public @Nullable List<PlanDTO> findAll() {
        List<PlanDTO> plans = this.planRepository.findAll()
                .stream()
                .map(PlanDTO::convertToDTO)
                .toList();

        return plans;
    }

    @Override
    public @Nullable PlanDTO findById(Long planId) {
        return PlanDTO.convertToDTO(this.planRepository.findById(planId).orElse(null));
    }

    @Override
    public @Nullable PlanDTO save(PlanDTO plan) {
        Plan p = PlanDTO.convertToEntity(plan);
        return PlanDTO.convertToDTO(this.planRepository.save(p));
    }

    @Override
    public void disable(Long planId) {
        this.planRepository.disable(planId);
    }

    @Override
    public void active(Long planId) {
        this.planRepository.active(planId);
    }

}
