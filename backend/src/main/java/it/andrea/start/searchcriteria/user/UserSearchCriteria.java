package it.andrea.start.searchcriteria.user;

import java.util.Collection;

import it.andrea.start.constants.RoleType;
import it.andrea.start.constants.UserStatus;

public class UserSearchCriteria {

    private Long id;
    private String username;
    private String textSearch;
    private UserStatus userStatus;
    private Collection<RoleType> roles;
    private Collection<RoleType> rolesNotValid;

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

    public String getTextSearch() {
	return textSearch;
    }

    public void setTextSearch(String textSearch) {
	this.textSearch = textSearch;
    }

    public UserStatus getUserStatus() {
	return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
	this.userStatus = userStatus;
    }

    public Collection<RoleType> getRoles() {
        return roles;
    }

    public void setRoles(Collection<RoleType> roles) {
        this.roles = roles;
    }

    public Collection<RoleType> getRolesNotValid() {
        return rolesNotValid;
    }

    public void setRolesNotValid(Collection<RoleType> rolesNotValid) {
        this.rolesNotValid = rolesNotValid;
    }

}
