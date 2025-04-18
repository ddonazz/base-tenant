package it.andrea.start.controller.types;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class LoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 3903555349152727298L;

    @NotBlank(message = "{error.user.username.null}")
    private String username;

    @NotBlank(message = "{error.user.password.null}")
    private String password;

}
