package it.andrea.start.dto.user;

import java.io.Serializable;
import java.util.Objects;

public class UserRoleDTO implements Serializable {
    private static final long serialVersionUID = 4014378396482783842L;

    private String role;

    public UserRoleDTO() {
	super();
    }

    public UserRoleDTO(String role) {
	super();
	this.role = role;
    }
    
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
	return Objects.hash(role);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!(obj instanceof UserRoleDTO))
	    return false;
	UserRoleDTO other = (UserRoleDTO) obj;
	return Objects.equals(role, other.role);
    }

}
