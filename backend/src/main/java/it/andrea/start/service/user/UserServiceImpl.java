package it.andrea.start.service.user;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.andrea.start.configuration.GlobalConfig;
import it.andrea.start.constants.AuditLevel;
import it.andrea.start.constants.EntityType;
import it.andrea.start.constants.RoleType;
import it.andrea.start.constants.UserStatus;
import it.andrea.start.dto.audit.AuditTraceDTO;
import it.andrea.start.dto.user.UserDTO;
import it.andrea.start.error.exception.BusinessException;
import it.andrea.start.error.exception.ErrorCode;
import it.andrea.start.error.exception.user.UserException;
import it.andrea.start.error.exception.user.UserNotFoundException;
import it.andrea.start.mappers.user.UserMapper;
import it.andrea.start.models.user.User;
import it.andrea.start.repository.user.UserRepository;
import it.andrea.start.searchcriteria.user.UserSearchCriteria;
import it.andrea.start.searchcriteria.user.UserSearchSpecification;
import it.andrea.start.security.EncrypterManager;
import it.andrea.start.security.service.JWTokenUserDetails;
import it.andrea.start.utils.HelperAudit;
import it.andrea.start.utils.HelperAuthorization;
import it.andrea.start.utils.HelperString;
import it.andrea.start.utils.PagedResult;
import it.andrea.start.validator.user.UserValidator;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final String ENTITY = "User";

    private final GlobalConfig globalConfig;
    private final EncrypterManager encrypterManager;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    public UserServiceImpl(GlobalConfig globalConfig, EncrypterManager encrypterManager, UserRepository userRepository, UserMapper userMapper,
            UserValidator userValidator) {
        super();
        this.globalConfig = globalConfig;
        this.encrypterManager = encrypterManager;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userValidator = userValidator;
    }

    @Override
    @Transactional(noRollbackFor = Exception.class, readOnly = true, propagation = Propagation.REQUIRED)
    public UserDTO getUser(String username) throws UserException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(username);
        }
        User user = optionalUser.get();

        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public UserDTO getUser(Long id) throws UserException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(id.toString());
        }
        User user = optionalUser.get();

        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public UserDTO whoami(JWTokenUserDetails jWTokenUserDetails) throws UserException {
        String username = jWTokenUserDetails.getUsername();
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(username);
        }
        User user = optionalUser.get();

        return userMapper.toDto(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public UserDTO createUser(UserDTO userDTO, JWTokenUserDetails userDetails) throws BusinessException, UserException {
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
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Pair<UserDTO, AuditTraceDTO> updateUser(UserDTO userDTO, JWTokenUserDetails userDetails) throws UserException, BusinessException {
        boolean haveAdminRole = HelperAuthorization.hasRole(userDetails.getAuthorities(), RoleType.ROLE_ADMIN);

        String username = userDTO.getUsername();
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(username);
        }
        User user = optionalUser.get();

        AuditTraceDTO audit = new AuditTraceDTO();
        String oldValue = null;
        if (globalConfig.getAuditLevel() == AuditLevel.ALL || globalConfig.getAuditLevel() == AuditLevel.DATABASE) {
            oldValue = HelperString.toJson(user);
        }

        boolean isMyProfile = false;
        if (user.getUsername().compareTo(userDetails.getUsername()) == 0) {
            isMyProfile = true;
        }

        userValidator.validateUserUpdate(userDTO, user, haveAdminRole, isMyProfile);

        userMapper.toEntity(userDTO, user);
        userRepository.save(user);

        HelperAudit.generateAudit(audit, globalConfig, user, ENTITY, oldValue);

        userDTO = this.userMapper.toDto(user);
        return Pair.of(userDTO, audit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void deleteUser(Long id, JWTokenUserDetails userDetails) throws UserException, BusinessException {
        boolean haveAdminRole = HelperAuthorization.hasRole(userDetails.getAuthorities(), RoleType.ROLE_ADMIN);
        boolean haveManagerRole = HelperAuthorization.hasRole(userDetails.getAuthorities(), RoleType.ROLE_MANAGER);

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(id.toString());
        }
        User user = optionalUser.get();

        if (haveAdminRole) {
            throw new BusinessException(ENTITY, "Admin not possible to delete", ErrorCode.USER_ROLE_ADMIN_NOT_DELETE.getCode(), String.valueOf(id));
        }
        if (haveManagerRole) {
            throw new BusinessException(ENTITY, "Only admin can delete a manager", ErrorCode.USER_ROLE_MANAGER_NOT_DELETE.getCode(), String.valueOf(id));
        }

        user.setUserStatus(UserStatus.DEACTIVE);
        user.setLastModifiedBy(userDetails.getUsername());

        userRepository.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true, propagation = Propagation.REQUIRED)
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
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void changePassword(Long userId, String newPassword, String repeatPassword, JWTokenUserDetails userDetails) throws UserException, BusinessException {
        boolean haveAdminRole = HelperAuthorization.hasRole(userDetails.getAuthorities(), RoleType.ROLE_ADMIN);
        boolean haveManagerRole = HelperAuthorization.hasRole(userDetails.getAuthorities(), RoleType.ROLE_MANAGER);

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(userId.toString());
        }
        User user = optionalUser.get();

        UserValidator.checkPassword(user, newPassword, repeatPassword, haveAdminRole, haveManagerRole);

        String passwordCrypt = encrypterManager.encode(newPassword);
        user.setPassword(passwordCrypt);
        user.setLastModifiedBy(userDetails.getUsername());

        userRepository.save(user);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void changePassword(String username, String newPassword, String repeatPassword, JWTokenUserDetails userDetails)
            throws UserException, BusinessException {
        boolean haveAdminRole = HelperAuthorization.hasRole(userDetails.getAuthorities(), RoleType.ROLE_ADMIN);
        boolean haveManagerRole = HelperAuthorization.hasRole(userDetails.getAuthorities(), RoleType.ROLE_MANAGER);

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(username);
        }
        User user = optionalUser.get();

        UserValidator.checkPassword(user, newPassword, repeatPassword, haveAdminRole, haveManagerRole);

        String passwordCrypt = encrypterManager.encode(newPassword);
        user.setPassword(passwordCrypt);
        user.setLastModifiedBy(userDetails.getUsername());

        userRepository.save(user);
    }

}
