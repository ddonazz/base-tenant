package it.andrea.start.service.user;

import org.springframework.data.domain.Pageable;

import it.andrea.start.dto.user.UserDTO;
import it.andrea.start.error.exception.BusinessException;
import it.andrea.start.error.exception.user.UserException;
import it.andrea.start.searchcriteria.user.UserSearchCriteria;
import it.andrea.start.security.service.JWTokenUserDetails;
import it.andrea.start.utils.PagedResult;

public interface UserService {

    UserDTO getUser(String username) throws UserException;

    UserDTO getUser(Long id) throws UserException;

    UserDTO whoami(JWTokenUserDetails jWTokenUserDetails) throws UserException;

    UserDTO createUser(UserDTO userDTO, JWTokenUserDetails userDetails) throws BusinessException, UserException;

    UserDTO updateUser(UserDTO userDTO, JWTokenUserDetails userDetails) throws UserException, BusinessException;

    void deleteUser(Long id, JWTokenUserDetails userDetails) throws UserException, BusinessException;

    PagedResult<UserDTO> listUser(UserSearchCriteria criteria, Pageable pageable, JWTokenUserDetails userDetails);

    void changePassword(Long userId, String newPassword, String repeatPassword, JWTokenUserDetails userDetails) throws UserException, BusinessException;

    void changePassword(String newPassword, String repeatPassword, JWTokenUserDetails userDetails) throws UserException, BusinessException;

}
