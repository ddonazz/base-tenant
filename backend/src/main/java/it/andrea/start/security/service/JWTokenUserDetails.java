package it.andrea.start.security.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JWTokenUserDetails implements UserDetails {
    private static final long serialVersionUID = 7189533439229082332L;

    private final String username;
    private final String password;
    private final Long agency;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    private JWTokenUserDetails(Builder builder) {
	this.username = Objects.requireNonNull(builder.username);
	this.password = Objects.requireNonNull(builder.password);
	this.agency = builder.agency;
	this.authorities = Collections.unmodifiableCollection(builder.authorities);
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    public static class Builder {
	private String username;
	private String password;
	private long agency;
	private Collection<? extends GrantedAuthority> authorities;

	public Builder username(String username) {
	    this.username = username;
	    return this;
	}

	public Builder password(String password) {
	    this.password = password;
	    return this;
	}
	
	public Builder agency(Long agency) {
	    this.agency = agency;
	    return this;
	}
	
	public Builder authorities(Collection<? extends GrantedAuthority> authorities) {
	    this.authorities = authorities;
	    return this;
	}

	public JWTokenUserDetails build() {
	    return new JWTokenUserDetails(this);
	}
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
	return authorities;
    }

    @Override
    public String getPassword() {
	return password;
    }

    @Override
    public String getUsername() {
	return username;
    }
    
    public Long getAgency() {
        return agency;
    }

    @Override
    public boolean isAccountNonExpired() {
	return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
	return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
	return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
	return enabled;
    }
}