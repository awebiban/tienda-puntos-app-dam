package tienda.puntos.app.services.Store;

import java.util.List;

import tienda.puntos.app.model.dto.StoreDTO;

public interface StoreService {
    List<StoreDTO> findAllStores();

    StoreDTO findStoreByID(Long id);

    List<StoreDTO> findAllByCompanyId(Long companyId);

    void disable(Long storeId);

    void activate(Long storeId);

    StoreDTO save(StoreDTO storeDTO);

    StoreDTO update(Long storeId, StoreDTO storeDTO);
}
