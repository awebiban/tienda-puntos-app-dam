package tienda.puntos.app.model.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tienda.puntos.app.repository.entity.User;
import tienda.puntos.app.utils.Role;
import tienda.puntos.app.utils.Views;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @JsonView(Views.Detalle.class)
    private Long id;

    @JsonView(Views.Detalle.class)
    private String email;

    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonView(Views.Detalle.class)
    private String nickname;

    @JsonView(Views.Detalle.class)
    private Role role;

    @JsonView(Views.Detalle.class)
    private LocalDateTime createdAt;
    // private CompanyDTO companyDTO;

    public static UserDTO convertToDTO(User u1) {
        UserDTO u2 = new UserDTO();
        u2.setId(u1.getId());
        u2.setEmail(u1.getEmail());
        u2.setPassword(u1.getPassword());
        u2.setNickname(u1.getNickname());
        u2.setRole(u1.getRole());
        u2.setCreatedAt(u1.getCreatedAt());
        // u2.setCompanyDTO(CompanyDTO.convertToDTO(u1.getCompany()));

        return u2;
    }

    public static User convertToEntity(UserDTO u1) {
        User u2 = new User();
        u2.setId(u1.getId());
        u2.setEmail(u1.getEmail());
        u2.setPassword(u1.getPassword());
        u2.setNickname(u1.getNickname());
        u2.setRole(u1.getRole());
        u2.setCreatedAt(u1.getCreatedAt());
        // u2.setCompany(CompanyDTO.convertToEntity(u1.getCompanyDTO()));

        return u2;
    }

}
