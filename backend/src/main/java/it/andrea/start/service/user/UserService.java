package it.andrea.start.service.user;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Pageable;

import it.andrea.start.dto.audit.AuditTraceDTO;
import it.andrea.start.dto.user.UserDTO;
import it.andrea.start.error.exception.BusinessException;
import it.andrea.start.error.exception.mapping.MappingException;
import it.andrea.start.error.exception.user.UserException;
import it.andrea.start.searchcriteria.user.UserSearchCriteria;
import it.andrea.start.security.service.JWTokenUserDetails;
import it.andrea.start.utils.PagedResult;

public interface UserService {

    void requestPasswordReset(String email) throws UserException;

    UserDTO getUser(String username) throws UserException, MappingException;

    UserDTO getUser(Long id) throws UserException, MappingException;

    UserDTO whoami(JWTokenUserDetails jWTokenUserDetails) throws UserException, MappingException;

    UserDTO createUser(UserDTO userDTO, JWTokenUserDetails userDetails) throws BusinessException, MappingException, UserException;

    Pair<UserDTO, AuditTraceDTO> updateUser(UserDTO userDTO, JWTokenUserDetails userDetails) throws UserException, BusinessException, MappingException;

    void deleteUser(Long id, JWTokenUserDetails userDetails) throws UserException, BusinessException;

    PagedResult<UserDTO> listUser(UserSearchCriteria criteria, Pageable pageable, JWTokenUserDetails userDetails) throws MappingException;

    void changePassword(Long userId, String newPassword, String repeatPassword, JWTokenUserDetails userDetails) throws UserException, BusinessException;

    void changePassword(String username, String newPassword, String repeatPassword, JWTokenUserDetails userDetails) throws UserException, BusinessException;

}
