package tienda.puntos.app.services.Plan;

import java.util.List;

import org.jspecify.annotations.Nullable;

import tienda.puntos.app.model.dto.PlanDTO;

public interface PlanService {

    @Nullable
    List<PlanDTO> findAll();

    @Nullable
    PlanDTO findById(Long planId);

    @Nullable
    PlanDTO save(PlanDTO plan);

    void disable(int planId);

    void active(int planId);

}
