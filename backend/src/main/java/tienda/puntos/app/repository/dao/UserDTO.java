package tienda.puntos.app.repository.dao;

import java.sql.Date;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tienda.puntos.app.repository.entity.User;
import tienda.puntos.app.utils.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String email;

    @JsonIgnore
    private String password;
    private String nickname;
    private Role role;
    private LocalDateTime createdAt;
    private CompanyDTO companyDTO;

    public static UserDTO convertoToDTO(User u1) {
        UserDTO u2 = new UserDTO();
        u2.setId(u1.getId());
        u2.setEmail(u1.getEmail());
        u2.setPassword(u1.getPassword());
        u2.setNickname(u1.getNickname());
        u2.setRole(u1.getRole());
        u2.setCreatedAt(u1.getCreatedAt());
        u2.setCompanyDTO(CompanyDTO.convertToDTO(u1.getCompany()));

        return u2;
    }

    public static User convertoToEntity(UserDTO u1) {
        User u2 = new User();
        u2.setId(u1.getId());
        u2.setEmail(u1.getEmail());
        u2.setPassword(u1.getPassword());
        u2.setNickname(u1.getNickname());
        u2.setRole(u1.getRole());
        u2.setCreatedAt(u1.getCreatedAt());
        u2.setCompany(CompanyDTO.convertToEntity(u1.getCompanyDTO()));

        return u2;
    }

}
