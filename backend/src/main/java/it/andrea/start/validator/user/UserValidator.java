package it.andrea.start.validator.user;

import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Component;

import it.andrea.start.constants.RoleType;
import it.andrea.start.controller.types.ChangePassword;
import it.andrea.start.dto.user.UserDTO;
import it.andrea.start.error.exception.BusinessException;
import it.andrea.start.error.exception.ErrorCode;
import it.andrea.start.error.exception.user.UserAlreadyExistsException;
import it.andrea.start.models.user.User;
import it.andrea.start.repository.user.UserRepository;

@Component
public class UserValidator {

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    public static void checkPassword(ChangePassword changePassword)
            throws BusinessException {
        if (!changePassword.getNewPassword().equals(changePassword.getRepeatPassword())) {
            throw new BusinessException(ErrorCode.USER_REPEAT_PASSWORD_NOT_EQUAL, "Passwords entered do not match.");
        }
    }

    public void validateUser(UserDTO dto, boolean haveAdminRole, boolean checkAdmin) {
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
            throw new BusinessException(ErrorCode.USER_ROLE_ADMIN_NOT_USABLE, "");
        }

        String roleManager = roles.stream().filter(role -> role.equals(RoleType.ROLE_MANAGER.name())).findFirst().orElse(null);
        if (roleManager != null && !haveAdminRole) {
            throw new BusinessException(ErrorCode.USER_ROLE_MANAGER_NOT_USABLE, "");
        }
    }

    public void validateUserUpdate(UserDTO dto, User entity, boolean haveAdminRole, boolean isMyProfile) {
        String email = dto.getEmail();
        Optional<User> optionalUserOther = userRepository.findByEmail(email);
        if (optionalUserOther.isPresent() && !optionalUserOther.get().getId().equals(entity.getId())) {
            throw new UserAlreadyExistsException(email);
        }

        Collection<String> roles = dto.getRoles();
        if (!isMyProfile && roles.stream().anyMatch(role -> role.equals(RoleType.ROLE_ADMIN.name()))) {
            throw new BusinessException(ErrorCode.USER_ROLE_ADMIN_NOT_USABLE, "");
        }

        if (!isMyProfile) {
            String roleManager = roles.stream().filter(role -> role.equals(RoleType.ROLE_MANAGER.name())).findFirst().orElse(null);
            if (roleManager != null && !haveAdminRole) {
                throw new BusinessException(ErrorCode.USER_ROLE_MANAGER_NOT_USABLE, "");
            }
        }
    }

}
