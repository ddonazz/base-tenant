package it.andrea.start.controller;

import java.util.Collection;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.andrea.start.annotation.Audit;
import it.andrea.start.constants.AuditActivity;
import it.andrea.start.constants.AuditTypeOperation;
import it.andrea.start.constants.RoleType;
import it.andrea.start.constants.UserStatus;
import it.andrea.start.controller.types.ChangePassword;
import it.andrea.start.dto.user.UserDTO;
import it.andrea.start.error.exception.BusinessException;
import it.andrea.start.searchcriteria.user.UserSearchCriteria;
import it.andrea.start.security.service.JWTokenUserDetails;
import it.andrea.start.service.user.UserService;
import it.andrea.start.utils.PagedResult;
import it.andrea.start.validator.OnCreate;

@Tag(
    name = "User API",
    description = "API for user CRUD operations"
)
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        super();
        this.userService = userService;
    }

    @Operation(
        description = "Aggiungi un utente da ADMIN o MANAGER",
        summary = "Aggiungi un utente da ADMIN o MANAGER"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("")
    @Audit(
        activity = AuditActivity.USER_OPERATION,
        type = AuditTypeOperation.CREATE
    )
    public ResponseEntity<UserDTO> createUser(@RequestBody @Validated(OnCreate.class) UserDTO userDTO)
            throws BusinessException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTokenUserDetails userDetails = (JWTokenUserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(userService.createUser(userDTO, userDetails));
    }

    @Operation(
        method = "PUT",
        description = "Aggiorna un utente da ADMIN o MANAGER",
        summary = "Aggiorna un utente da ADMIN o MANAGER"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{id}")
    @Audit(
        activity = AuditActivity.USER_OPERATION,
        type = AuditTypeOperation.UPDATE
    )
    public ResponseEntity<UserDTO> updateUser(
            @RequestBody UserDTO userDTO,
            @PathVariable Long id)
            throws BusinessException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTokenUserDetails userDetails = (JWTokenUserDetails) authentication.getPrincipal();

        userDTO.setId(id);
        return ResponseEntity.ok(userService.updateUser(userDTO, userDetails));
    }

    @Operation(
        description = "Elimina un utente da ADMIN o MANAGER",
        summary = "Elimina un utente da ADMIN o MANAGER"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    @Audit(
        activity = AuditActivity.USER_OPERATION,
        type = AuditTypeOperation.DELETE
    )
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTokenUserDetails userDetails = (JWTokenUserDetails) authentication.getPrincipal();

        userService.deleteUser(id, userDetails);

        return ResponseEntity.ok().build();
    }

    @Operation(
        description = "Informazioni di un utente",
        summary = "Informazioni di un utente"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{id}")
    @Audit(
        activity = AuditActivity.USER_OPERATION,
        type = AuditTypeOperation.GET_INFO
    )
    public ResponseEntity<UserDTO> infoUser(@PathVariable Long id) {

        return ResponseEntity.ok(userService.getUser(id));
    }

    @Operation(
        description = "Lista degli utenti",
        summary = "Lista degli utenti"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR')")
    @GetMapping("/list")
    public ResponseEntity<PagedResult<UserDTO>> listUser(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) UserStatus userStatus,
            @RequestParam(required = false) Collection<RoleType> roles,
            @RequestParam(required = false) Collection<RoleType> rolesNotValid,
            Pageable pageable) {

        UserSearchCriteria criteria = new UserSearchCriteria();
        criteria.setId(id);
        criteria.setUsername(username);
        criteria.setTextSearch(textSearch);
        criteria.setUserStatus(userStatus);
        criteria.setRoles(roles);
        criteria.setRolesNotValid(rolesNotValid);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTokenUserDetails userDetails = (JWTokenUserDetails) authentication.getPrincipal();

        PagedResult<UserDTO> users = userService.listUser(criteria, pageable, userDetails);

        return ResponseEntity.ok(users);
    }

    @Operation(
        description = "Cambio password da ADMIN",
        summary = "Cambio password da ADMIN"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/change-password/{userId}")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long userId,
            @RequestBody ChangePassword changePassword) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTokenUserDetails userDetails = (JWTokenUserDetails) authentication.getPrincipal();

        userService.changePasswordForAdmin(userId, changePassword, userDetails);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR')")
    @Operation(
        description = "User self change password",
        summary = "User self change password"
    )
    @PostMapping("/change-my-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePassword changePassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTokenUserDetails userDetails = (JWTokenUserDetails) authentication.getPrincipal();

        userService.changeMyPassword(changePassword, userDetails);

        return ResponseEntity.ok().build();
    }

}
