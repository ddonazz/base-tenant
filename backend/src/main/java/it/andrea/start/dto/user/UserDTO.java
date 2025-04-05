package it.andrea.start.dto.user;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import it.andrea.start.constants.UserStatus;
import it.andrea.start.validator.OnCreate;
import it.andrea.start.validator.OnUpdate;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDTO implements Serializable {
    private static final long serialVersionUID = -6705812365714677548L;

    private Long id;

    @NotBlank(message = "L'username dell'utente non può essere vuoto", groups = { OnCreate.class })
    @Size(min = 4, max = 30, message = "L'username deve avere almeno 4 e massimo 30 caratteri", groups = { OnCreate.class })
    private String username;

    @NotBlank(message = "Il nome dell'utente non può essere vuoto", groups = { OnCreate.class, OnUpdate.class })
    @Size(min = 5, max = 255, message = "Il nome deve avere almeno 5 e massimo 255 caratteri", groups = { OnCreate.class, OnUpdate.class })
    private String name;

    @Email(message = "La mail non è valida", groups = { OnCreate.class, OnUpdate.class })
    private String email;

    @NotNull(message = "Lo stato dell'utente non può essere vuoto", groups = { OnCreate.class, OnUpdate.class })
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Valid
    @NotEmpty(message = "I ruoli non possono essere vuoti", groups = { OnCreate.class, OnUpdate.class })
    private Set<String> roles;

    private String languageDefault;

    @NotBlank(message = "La password è vuota", groups = OnCreate.class)
    @Size(min = 5, max = 30, message = "La password deve avere almeno 6 e massimo 30 caratteri", groups = OnCreate.class)
    private transient String password;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public UserStatus getUserStatus() {
	return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
	this.userStatus = userStatus;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getLanguageDefault() {
	return languageDefault;
    }

    public void setLanguageDefault(String languageDefault) {
	this.languageDefault = languageDefault;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    @Override
    public int hashCode() {
	return Objects.hash(email, id, name, username);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!(obj instanceof UserDTO))
	    return false;
	UserDTO other = (UserDTO) obj;
	return Objects.equals(email, other.email) && Objects.equals(id, other.id) && Objects.equals(name, other.name) && Objects.equals(username, other.username);
    }

}
