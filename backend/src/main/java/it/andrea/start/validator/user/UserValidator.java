
package it.andrea.start.validator.user;

import static it.andrea.start.exception.ExceptionCodeError.CODE_USER_REPEAT_PASSWORD_NOT_EQUAL;
import static it.andrea.start.exception.ExceptionCodeError.CODE_USER_ROLE_ADMIN_NOT_CHANGE_PASSWORD;
import static it.andrea.start.exception.ExceptionCodeError.CODE_USER_ROLE_ADMIN_NOT_USABLE;
import static it.andrea.start.exception.ExceptionCodeError.CODE_USER_ROLE_MANAGER_NOT_CHANGE_PASSWORD;
import static it.andrea.start.exception.ExceptionCodeError.CODE_USER_ROLE_MANAGER_NOT_USABLE;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.andrea.start.constants.RoleType;
import it.andrea.start.dto.user.UserDTO;
import it.andrea.start.exception.BusinessException;
import it.andrea.start.exception.user.UserAlreadyExistsException;
import it.andrea.start.models.user.User;
import it.andrea.start.repository.user.UserRepository;

@Component
public class UserValidator {

    private static final Logger LOG = LoggerFactory.getLogger(UserValidator.class);

    private static final String ENTITY = "User";

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
	super();
	this.userRepository = userRepository;
    }

    public void validateUser(UserDTO dto, boolean haveAdminRole, boolean checkAdmin) throws BusinessException, UserAlreadyExistsException {
	String username = dto.getUsername();
	if (userRepository.findByUsername(username.toUpperCase()).isPresent()) {
	    throw new UserAlreadyExistsException(username);
	}

	String email = dto.getEmail();
	if (userRepository.findByEmail(email).isPresent()) {
	    throw new UserAlreadyExistsException(username);
	}

	Collection<String> roles = dto.getRoles();
	if (checkAdmin && roles.stream().anyMatch(role -> role.equals(RoleType.ROLE_ADMIN.name()))) {
	    throw new BusinessException(ENTITY, "Role admin is not usable", CODE_USER_ROLE_ADMIN_NOT_USABLE);
	}

	String roleManager = roles.stream().filter(role -> role.equals(RoleType.ROLE_MANAGER.name())).findFirst().orElse(null);
	if (roleManager != null && !haveAdminRole) {
	    throw new BusinessException(ENTITY, "Role manager is not usable", CODE_USER_ROLE_MANAGER_NOT_USABLE);
	}
    }

    public void validateUserUpdate(UserDTO dto, User entity, boolean haveAdminRole, boolean isMyProfile) throws BusinessException, UserAlreadyExistsException {
	String email = dto.getEmail();
	Optional<User> optionalUserOther = userRepository.findByEmail(email);
	if (optionalUserOther.isPresent() && !optionalUserOther.get().getId().equals(entity.getId())) {
	    throw new UserAlreadyExistsException(email);
	}

	Collection<String> roles = dto.getRoles();
	if (!isMyProfile && roles.stream().anyMatch(role -> role.equals(RoleType.ROLE_ADMIN.name()))) {
	    throw new BusinessException(ENTITY, "Role admin is not usable", CODE_USER_ROLE_ADMIN_NOT_USABLE);
	}

	if (!isMyProfile) {
	    String roleManager = roles.stream().filter(role -> role.equals(RoleType.ROLE_MANAGER.name())).findFirst().orElse(null);
	    if (roleManager != null && !haveAdminRole) {
		throw new BusinessException(ENTITY, "Role manager is not usable", CODE_USER_ROLE_MANAGER_NOT_USABLE);
	    }
	}
    }

    public static void checkPassword(User entity, String newPassword, String repeatPassword, boolean haveAdminRole, boolean haveManagerRole) throws BusinessException {
	if (haveAdminRole) {
	    throw new BusinessException(ENTITY, "Is not possible to change password to admin role user", CODE_USER_ROLE_ADMIN_NOT_CHANGE_PASSWORD, "" + entity.getId());
	}
	if (haveManagerRole) {
	    throw new BusinessException(ENTITY, "Is not possible to change password to manager role user if you not are admin user", CODE_USER_ROLE_MANAGER_NOT_CHANGE_PASSWORD, "" + entity.getId());
	}

	if (!newPassword.equals(repeatPassword)) {
	    throw new BusinessException(ENTITY, "New password and repeat password is not equal", CODE_USER_REPEAT_PASSWORD_NOT_EQUAL);
	}
    }

}
