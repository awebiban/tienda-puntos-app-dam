package tienda.puntos.app.services.Company;

import org.jspecify.annotations.Nullable;

import tienda.puntos.app.model.dto.CompanyDTO;

public interface CompanyService {

    @Nullable
    CompanyDTO findCompanyByID(Long id);

    @Nullable
    CompanyDTO findCompanyFromUserID(Long userId);

    @Nullable
    CompanyDTO save(CompanyDTO company);

}
