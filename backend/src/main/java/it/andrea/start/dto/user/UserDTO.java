package it.andrea.start.dto.user;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

import it.andrea.start.constants.UserStatus;
import it.andrea.start.validator.OnCreate;
import it.andrea.start.validator.OnUpdate;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(
    callSuper = false,
    of = { "name", "username", "email" }
)
@NoArgsConstructor
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6705812365714677548L;

    private Long id;

    @NotBlank(
        message = "{error.user.username.null}",
        groups = { OnCreate.class }
    )
    @Size(
        min = 4,
        max = 30,
        message = "{error.user.username.wrong.length}",
        groups = { OnCreate.class }
    )
    private String username;

    @NotBlank(
        message = "{error.user.name.null}",
        groups = { OnCreate.class, OnUpdate.class }
    )
    @Size(
        min = 5,
        max = 255,
        message = "{error.user.name.wrong.length}",
        groups = { OnCreate.class, OnUpdate.class }
    )
    private String name;

    @NotBlank(
        message = "{error.user.email.null}",
        groups = { OnCreate.class, OnUpdate.class }
    )
    @Email(
        message = "{error.user.email.not.valid}",
        groups = { OnCreate.class, OnUpdate.class }
    )
    private String email;

    @NotNull(
        message = "{error.user.status.null}",
        groups = { OnCreate.class, OnUpdate.class }
    )
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @NotEmpty(
        message = "{error.user.roles.empty}",
        groups = { OnCreate.class, OnUpdate.class }
    )
    private Set<String> roles;

    private String languageDefault;

    @NotBlank(
        message = "{error.user.password.null}",
        groups = OnCreate.class
    )
    @Size(
        min = 5,
        max = 30,
        message = "{error.user.password.wrong.length}",
        groups = OnCreate.class
    )
    private transient String password;

}