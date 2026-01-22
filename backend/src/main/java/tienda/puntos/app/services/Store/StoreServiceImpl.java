package tienda.puntos.app.services.Store;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tienda.puntos.app.model.dto.StoreDTO;
import tienda.puntos.app.repository.dao.StoreRepository;
import tienda.puntos.app.repository.entity.Store;

@Service
public class StoreServiceImpl implements StoreService {

    private static final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);

    @Autowired
    private StoreRepository storeRepository;

    @Override
    public List<StoreDTO> findAllStores() {
        log.info(this.getClass().getSimpleName()
                + " - findAllStores() - Obtenemos datos de BBDD y transformamos en DTO para enviar a la vista");

        List<Store> listaTiendas = this.storeRepository.findAll();
        List<StoreDTO> listaTiendasDTO = new ArrayList<>();
        StoreDTO dto = null;

        for (Store s : listaTiendas) {
            dto = StoreDTO.convertToDTO(s);
            listaTiendasDTO.add(dto);
        }

        return listaTiendasDTO;
    }

    @Override
    public StoreDTO findStoreByID(Long id) {
        log.info(this.getClass().getSimpleName()
                + " - findStoreByID() - Obtenemos datos de BBDD y transformamos en DTO para enviar a la vista");

        Store store = this.storeRepository.findById(id).get();
        System.out.println(store);
        return StoreDTO.convertToDTO(store);
    }

}
