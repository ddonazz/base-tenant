package it.andrea.start.service.user;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Pageable;

import it.andrea.start.dto.audit.AuditTraceDTO;
import it.andrea.start.dto.user.UserDTO;
import it.andrea.start.exception.BusinessException;
import it.andrea.start.exception.MappingToDtoException;
import it.andrea.start.exception.MappingToEntityException;
import it.andrea.start.exception.user.UserAlreadyExistsException;
import it.andrea.start.exception.user.UserNotFoundException;
import it.andrea.start.searchcriteria.user.UserSearchCriteria;
import it.andrea.start.security.service.JWTokenUserDetails;
import it.andrea.start.utils.PagedResult;

public interface UserService {

    void requestPasswordReset(String email) throws UserNotFoundException;

    UserDTO getUser(String username) throws UserNotFoundException, MappingToDtoException;

    UserDTO getUser(Long id) throws UserNotFoundException, MappingToDtoException;

    UserDTO whoami(JWTokenUserDetails jWTokenUserDetails) throws UserNotFoundException, MappingToDtoException;

    UserDTO createUser(UserDTO userDTO, JWTokenUserDetails userDetails) throws BusinessException, MappingToEntityException, MappingToDtoException, UserAlreadyExistsException;

    Pair<UserDTO, AuditTraceDTO> updateUser(UserDTO userDTO, JWTokenUserDetails userDetails) throws UserNotFoundException, BusinessException, MappingToEntityException, MappingToDtoException, UserAlreadyExistsException;

    void deleteUser(Long id, JWTokenUserDetails userDetails) throws UserNotFoundException, BusinessException;

    PagedResult<UserDTO> listUser(UserSearchCriteria criteria, Pageable pageable, JWTokenUserDetails userDetails) throws MappingToDtoException;

    void changePassword(Long userId, String newPassword, String repeatPassword, JWTokenUserDetails userDetails) throws UserNotFoundException, BusinessException;

    void changePassword(String username, String newPassword, String repeatPassword, JWTokenUserDetails userDetails) throws UserNotFoundException, BusinessException;

}
