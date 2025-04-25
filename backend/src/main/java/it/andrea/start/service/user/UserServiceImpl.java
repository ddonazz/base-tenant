package it.andrea.start.service.user;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.andrea.start.constants.EntityType;
import it.andrea.start.constants.RoleType;
import it.andrea.start.constants.UserStatus;
import it.andrea.start.controller.types.ChangePassword;
import it.andrea.start.dto.user.UserDTO;
import it.andrea.start.error.exception.BusinessException;
import it.andrea.start.error.exception.ErrorCode;
import it.andrea.start.error.exception.user.UserNotFoundException;
import it.andrea.start.mappers.user.UserMapper;
import it.andrea.start.models.user.User;
import it.andrea.start.repository.user.UserRepository;
import it.andrea.start.searchcriteria.user.UserSearchCriteria;
import it.andrea.start.searchcriteria.user.UserSearchSpecification;
import it.andrea.start.security.EncrypterManager;
import it.andrea.start.security.service.JWTokenUserDetails;
import it.andrea.start.utils.HelperAuthorization;
import it.andrea.start.utils.PagedResult;
import it.andrea.start.validator.user.UserValidator;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final EncrypterManager encrypterManager;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUser(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(username);
        }
        User user = optionalUser.get();

        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        User user = optionalUser.get();

        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO whoami(JWTokenUserDetails jWTokenUserDetails) {
        String username = jWTokenUserDetails.getUsername();
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(username);
        }
        User user = optionalUser.get();

        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResult<UserDTO> listUser(UserSearchCriteria criteria, Pageable pageable, JWTokenUserDetails userDetails) {
        final Page<User> userPage = userRepository.findAll(new UserSearchSpecification(criteria), pageable);
        final Page<UserDTO> dtoPage = userPage.map(userMapper::toDto);
    
        final PagedResult<UserDTO> result = new PagedResult<>();
        result.setItems(dtoPage.getContent());
        result.setPageNumber(dtoPage.getNumber() + 1);
        result.setPageSize(dtoPage.getSize());
        result.setTotalElements((int) dtoPage.getTotalElements());
    
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO createUser(UserDTO userDTO, JWTokenUserDetails userDetails) {
        userValidator.validateUser(userDTO, HelperAuthorization.hasRole(userDetails.getAuthorities(), RoleType.ROLE_ADMIN), true);

        User user = new User();
        userMapper.toEntity(userDTO, user);
        String passwordCrypt = encrypterManager.encode(userDTO.getPassword());
        user.setPassword(passwordCrypt);

        String creator = userDetails.getUsername();
        if (StringUtils.isBlank(creator)) {
            creator = EntityType.SYSTEM.getValue();
        }

        user.setCreator(creator);
        user.setLastModifiedBy(creator);

        userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO updateUser(UserDTO userDTO, JWTokenUserDetails userDetails) {
        boolean haveAdminRole = HelperAuthorization.hasRole(userDetails.getAuthorities(), RoleType.ROLE_ADMIN);

        String username = userDTO.getUsername();
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(username);
        }
        User user = optionalUser.get();

        boolean isMyProfile = user.getUsername().compareTo(userDetails.getUsername()) == 0;

        userValidator.validateUserUpdate(userDTO, user, haveAdminRole, isMyProfile);

        userMapper.toEntity(userDTO, user);
        userRepository.save(user);

        userDTO = this.userMapper.toDto(user);
        return userDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id, JWTokenUserDetails userDetails) {
        boolean haveAdminRole = HelperAuthorization.hasRole(userDetails.getAuthorities(), RoleType.ROLE_ADMIN);
        boolean haveManagerRole = HelperAuthorization.hasRole(userDetails.getAuthorities(), RoleType.ROLE_MANAGER);

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(id.toString());
        }
        User user = optionalUser.get();

        if (haveAdminRole) {
            throw new BusinessException(ErrorCode.USER_ROLE_ADMIN_NOT_DELETE, "");
        }
        if (haveManagerRole) {
            throw new BusinessException(ErrorCode.USER_ROLE_MANAGER_NOT_DELETE, "");
        }

        user.setUserStatus(UserStatus.DEACTIVATE);
        user.setLastModifiedBy(userDetails.getUsername());

        userRepository.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePasswordForAdmin(Long userId, ChangePassword changePassword, JWTokenUserDetails userDetails) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(userId.toString());
        }
        User user = optionalUser.get();

        UserValidator.checkPassword(changePassword);

        String passwordCrypt = encrypterManager.encode(changePassword.getNewPassword());
        user.setPassword(passwordCrypt);
        user.setLastModifiedBy(userDetails.getUsername());

        userRepository.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeMyPassword(ChangePassword changePassword, JWTokenUserDetails userDetails) {
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(userDetails.getUsername());
        }
        User user = optionalUser.get();

        UserValidator.checkPassword(changePassword);

        String passwordCrypt = encrypterManager.encode(changePassword.getNewPassword());
        user.setPassword(passwordCrypt);
        user.setLastModifiedBy(userDetails.getUsername());

        userRepository.save(user);
    }

}
