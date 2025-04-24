package it.andrea.start.service.user;

import org.springframework.data.domain.Pageable;

import it.andrea.start.controller.types.ChangePassword;
import it.andrea.start.dto.user.UserDTO;
import it.andrea.start.searchcriteria.user.UserSearchCriteria;
import it.andrea.start.security.service.JWTokenUserDetails;
import it.andrea.start.utils.PagedResult;

public interface UserService {

    UserDTO getUser(String username);

    UserDTO getUser(Long id);

    UserDTO whoami(JWTokenUserDetails jWTokenUserDetails);

    UserDTO createUser(UserDTO userDTO, JWTokenUserDetails userDetails);

    UserDTO updateUser(UserDTO userDTO, JWTokenUserDetails userDetails);

    void deleteUser(Long id, JWTokenUserDetails userDetails);

    PagedResult<UserDTO> listUser(UserSearchCriteria criteria, Pageable pageable, JWTokenUserDetails userDetails);

    void changeMyPassword(ChangePassword changePassword, JWTokenUserDetails userDetails);

    void changePasswordForAdmin(Long userId, ChangePassword changePassword, JWTokenUserDetails userDetails);

}
