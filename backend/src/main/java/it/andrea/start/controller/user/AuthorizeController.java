package it.andrea.start.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import it.andrea.start.controller.response.TokenResponse;
import it.andrea.start.controller.types.ChangePassword;
import it.andrea.start.controller.types.LoginRequest;
import it.andrea.start.controller.types.RecoveryPassword;
import it.andrea.start.controller.types.ResetPasswordContent;
import it.andrea.start.dto.user.UserDTO;
import it.andrea.start.exception.MappingToDtoException;
import it.andrea.start.exception.user.UserNotFoundException;
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

    @Operation(
	    method = "POST", 
	    description = "Login user", 
	    summary = "Login user", 
	    responses = { 
		    @ApiResponse(responseCode = "200", description = "Credentials is valid"), 
		    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(hidden = true))), 
		    @ApiResponse(responseCode = "403", description = "Credentials not valid"), 
		    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(hidden = true))) 
		    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authorize(
	    HttpServletRequest httpServletRequest, 
	    @RequestBody @Validated LoginRequest userAndPassword) {
	Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAndPassword.getUsername(), userAndPassword.getPassword()));
	SecurityContextHolder.getContext().setAuthentication(authentication);
	
	String jwt = jwtUtils.generateToken(authentication);

	TokenResponse tokenResponse = new TokenResponse(jwt);

	return ResponseEntity.ok(tokenResponse);
    }

    @Operation(method = "POST", description = "Recovery password sending email", summary = "Recovery password sending email", responses = { @ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "Bad request"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(hidden = true))) })
    @PostMapping("/recoveryPassword")
    public ResponseEntity<Void> recoveryPassword(@RequestHeader(name = "accept-language", defaultValue = "it", required = false) String language, @RequestBody RecoveryPassword recoveryPassword) throws Exception {

	return ResponseEntity.ok().build();
    }

    @Operation(method = "GET", description = "Validation token reset password user", summary = "Validation token reset password user", responses = { @ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "Bad request"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(hidden = true))) })
    @GetMapping("/validitationTokenResetPasword/{token}")
    public ResponseEntity<Void> validitationTokenResetPasword(@RequestHeader(name = "accept-language", defaultValue = "it", required = false) String language, @PathVariable String token) throws Exception {

	return ResponseEntity.ok().build();
    }

    @Operation(method = "POST", description = "User reset password", summary = "User reset password", responses = { @ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "Bad request"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(hidden = true))) })
    @PostMapping("/resetPassword")
    public ResponseEntity<Void> resetPassword(@RequestHeader(name = "accept-language", defaultValue = "it", required = false) String language, @RequestBody(required = true) ResetPasswordContent resetPasswordContent) throws Exception {

	return ResponseEntity.ok().build();
    }

    @Operation(
	    method = "GET", 
	    description = "Information current user", 
	    summary = "Information current user", 
	    responses = { 
		    @ApiResponse(responseCode = "200", description = "OK"), 
		    @ApiResponse(responseCode = "400", description = "Bad request"),
		    @ApiResponse(responseCode = "401", description = "Not authorized", content = @Content(schema = @Schema(hidden = true))), 
		    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(hidden = true))) 
		    }
	    )
    @GetMapping("/whoami")
    public ResponseEntity<UserDTO> whoami(
	    @RequestHeader(name = "accept-language", defaultValue = "it", required = false) String language) throws UserNotFoundException, MappingToDtoException  {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	JWTokenUserDetails userDetails = (JWTokenUserDetails) authentication.getPrincipal();

	UserDTO dto = userService.whoami(userDetails);

	return ResponseEntity.ok(dto);
    }

    @Operation(method = "PUT", description = "Update information current user", summary = "Update information current user", responses = { @ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "Bad request"), @ApiResponse(responseCode = "401", description = "Not authorized", content = @Content(schema = @Schema(hidden = true))), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(hidden = true))) })
    @PutMapping("/updateProfile")
    public ResponseEntity<UserDTO> updateProfile(@RequestBody(required = true) UserDTO userDTO) throws Exception {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	JWTokenUserDetails userDetails = (JWTokenUserDetails) authentication.getPrincipal();

	userService.updateUser(userDTO, userDetails);

	return ResponseEntity.ok(userDTO);
    }

    @Operation(
	    method = "POST", 
	    description = "User self change password", 
	    summary = "User self change password", 
	    responses = { 
		    @ApiResponse(responseCode = "200", description = "OK"), 
		    @ApiResponse(responseCode = "400", description = "Bad request"),
		    @ApiResponse(responseCode = "401", description = "Not authorized", content = @Content(schema = @Schema(hidden = true))),
		    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(hidden = true))) 
		    }
	    )
    @PostMapping("/changePassword")
    public ResponseEntity<Void> changePassword(
	    @RequestBody ChangePassword changePassword) throws Exception {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	JWTokenUserDetails userDetails = (JWTokenUserDetails) authentication.getPrincipal();
	String username = userDetails.getUsername();

	userService.changePassword(username, changePassword.getNewPassword(), changePassword.getRepeatPassword(), null);

	return ResponseEntity.ok().build();
    }

}
