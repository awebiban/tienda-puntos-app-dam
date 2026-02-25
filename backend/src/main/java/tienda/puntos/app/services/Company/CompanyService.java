package tienda.puntos.app.services.Company;

import tienda.puntos.app.model.dto.CompanyDTO;

public interface CompanyService {

    CompanyDTO findCompanyByID(Long id);

    CompanyDTO findCompanyFromUserID(Long userId);

    CompanyDTO findCompanyByCIF(String string);

    CompanyDTO save(CompanyDTO company);

    CompanyDTO update(Long companyId, CompanyDTO companyDTO);

}
