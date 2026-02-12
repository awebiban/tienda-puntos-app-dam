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
        return StoreDTO.convertToDTO(store);
    }

    @Override
    public List<StoreDTO> findAllByCompanyId(Long companyId) {
        return this.storeRepository.findAllByCompanyId(companyId)
                .stream()
                .map(StoreDTO::convertToDTO)
                .toList();
    }

    @Override
    public void disable(Long storeId) {
        this.storeRepository.disable(storeId);
    }

    @Override
    public void activate(Long storeId) {
        this.storeRepository.activate(storeId);
    }

    @Override
    public StoreDTO save(StoreDTO storeDTO) {
        Store s = StoreDTO.convertToEntity(storeDTO);
        s.setVisible(true);
        s.setImageUrl(createImageFileName(s.getId(), s.getName(), s.getImageUrl()));

        Store savedStore = this.storeRepository.save(s);
        return StoreDTO.convertToDTO(savedStore);
    }

    @Override
    public StoreDTO update(Long storeId, StoreDTO storeDTO) {
        Store existingStore = this.storeRepository.findById(storeId).orElse(null);
        if (existingStore == null) {
            throw new IllegalArgumentException("Store not found with ID: " + storeId);
        }

        Store updatedStore = StoreDTO.convertToEntity(storeDTO);
        updatedStore.setId(storeId);
        updatedStore.setName(storeDTO.getName() != null ? storeDTO.getName() : existingStore.getName());
        updatedStore.setCategory(storeDTO.getCategory() != null ? storeDTO.getCategory() : existingStore.getCategory());
        updatedStore.setAddress(storeDTO.getAddress() != null ? storeDTO.getAddress() : existingStore.getAddress());
        updatedStore.setPointsRatio(
                storeDTO.getPointsRatio() != 0 ? storeDTO.getPointsRatio() : existingStore.getPointsRatio());
        updatedStore.setVisible(existingStore.isVisible());
        updatedStore.setImageUrl(createImageFileName(storeId, updatedStore.getName(), updatedStore.getImageUrl()));

        Store savedUpdatedStore = this.storeRepository.save(updatedStore);
        return StoreDTO.convertToDTO(savedUpdatedStore);
    }

    private String createImageFileName(Long storeId, String storeName, String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return "store_" + storeId + "_" + storeName.replaceAll("\\s+", "_") + extension;
    }

}
