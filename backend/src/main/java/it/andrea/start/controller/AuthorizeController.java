package it.andrea.start.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import it.andrea.start.annotation.Audit;
import it.andrea.start.constants.AuditActivity;
import it.andrea.start.constants.AuditTypeOperation;
import it.andrea.start.controller.response.TokenResponse;
import it.andrea.start.controller.types.ChangePassword;
import it.andrea.start.controller.types.LoginRequest;
import it.andrea.start.dto.user.UserDTO;
import it.andrea.start.error.exception.BusinessException;
import it.andrea.start.error.exception.user.UserException;
import it.andrea.start.security.jwt.JwtUtils;
import it.andrea.start.security.service.JWTokenUserDetails;
import it.andrea.start.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/authorize")
public class AuthorizeController {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    public AuthorizeController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        super();
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Operation( //
            method = "POST", //
            description = "Login user", //
            summary = "Login user" //
    ) //
    @Audit(activity = AuditActivity.USER_OPERATION, type = AuditTypeOperation.LOGIN)
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authorize( //
            HttpServletRequest httpServletRequest, //
            @RequestBody @Validated LoginRequest userAndPassword) { //

        Authentication auth = new UsernamePasswordAuthenticationToken(userAndPassword.getUsername(), userAndPassword.getPassword());
        auth = authenticationManager.authenticate(auth);
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = jwtUtils.generateToken(auth);

        return ResponseEntity.ok(new TokenResponse(jwt));
    }

    @Operation( //
            method = "GET", //
            description = "Information current user", //
            summary = "Information current user" //
    )
    @GetMapping("/whoami")
    public ResponseEntity<UserDTO> whoami() throws UserException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTokenUserDetails userDetails = (JWTokenUserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(userService.whoami(userDetails));
    }

    @Operation( //
            method = "PUT", //
            description = "Update information current user", //
            summary = "Update information current user" //
    )
    @PutMapping("/update-profile")
    public ResponseEntity<UserDTO> updateProfile(@RequestBody(required = true) UserDTO userDTO)
            throws UserException, BusinessException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTokenUserDetails userDetails = (JWTokenUserDetails) authentication.getPrincipal();

        userService.updateUser(userDTO, userDetails);

        return ResponseEntity.ok(userDTO);
    }

    @Operation( //
            method = "POST", //
            description = "User self change password", //
            summary = "User self change password" //
    ) //
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePassword changePassword) throws UserException, BusinessException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTokenUserDetails userDetails = (JWTokenUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        userService.changePassword(username, changePassword.getNewPassword(), changePassword.getRepeatPassword(), null);

        return ResponseEntity.ok().build();
    }

}
