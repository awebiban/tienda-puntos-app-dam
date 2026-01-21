package tienda.puntos.app.repository.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tienda.puntos.app.repository.entity.Store;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreDTO {

    private Long id;
    private CompanyDTO companyDTO;
    private String name; // Nombre comercial
    private String category; // Que tipo negocio es
    private String address; // Direccion fisica del local
    private String imageUrl; // Nombre de la imagen de la tarjeta - fondo de tienda y logo PWA
    private int pointsRatio; // Cantidad de puntos otorgados por 1 euro euro gastado (Ej. 1 EUR = 10 puntos)
    private boolean isVisible; // Para ocultar tienda temporalmente

    public static StoreDTO convertToDTO(Store s1) {

        StoreDTO s2 = new StoreDTO();
        s2.setId(s1.getId());
        s2.setCompanyDTO(CompanyDTO.convertToDTO(s1.getCompany()));
        s2.setName(s1.getName());
        s2.setCategory(s1.getCategory());
        s2.setAddress(s1.getAddress());
        s2.setImageUrl(s1.getImageUrl());
        s2.setPointsRatio(s1.getPointsRatio());
        s2.setVisible(s1.isVisible());

        return s2;
    }

    public static Store convertToEntity(StoreDTO s1) {

        Store s2 = new Store();
        s2.setId(s1.getId());
        s2.setCompany(CompanyDTO.convertToEntity(s1.getCompanyDTO()));
        s2.setName(s1.getName());
        s2.setCategory(s1.getCategory());
        s2.setAddress(s1.getAddress());
        s2.setImageUrl(s1.getImageUrl());
        s2.setPointsRatio(s1.getPointsRatio());
        s2.setVisible(s1.isVisible());

        return s2;
    }
}
