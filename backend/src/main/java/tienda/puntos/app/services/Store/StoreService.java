package tienda.puntos.app.services.Store;

import java.util.List;

import tienda.puntos.app.model.dto.StoreDTO;

public interface StoreService {

    List<StoreDTO> findAllStores();

    StoreDTO findStoreByID(Long id);

}
