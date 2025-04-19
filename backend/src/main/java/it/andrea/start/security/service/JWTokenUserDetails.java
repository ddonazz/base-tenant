package it.andrea.start.security.service;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JWTokenUserDetails implements UserDetails {

    @Serial
    private static final long serialVersionUID = 7189533439229082332L;

    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    private JWTokenUserDetails(Builder builder) {
        this.username = Objects.requireNonNull(builder.username);
        this.password = Objects.requireNonNull(builder.password);
        this.authorities = Collections.unmodifiableCollection(builder.authorities);
        this.accountNonExpired = builder.accountNonExpired;
        this.accountNonLocked = builder.accountNonLocked;
        this.credentialsNonExpired = true;
        this.enabled = builder.enabled;
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

    public static class Builder {
        private String username;
        private String password;
        private Collection<? extends GrantedAuthority> authorities;
        private boolean accountNonExpired;
        private boolean accountNonLocked;
        private boolean enabled;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public Builder accountNonExpired(boolean isAccountNonExpired) {
            this.accountNonExpired = isAccountNonExpired;
            return this;
        }

        public Builder accountNonLocked(boolean isAccountNonLocked) {
            this.accountNonLocked = isAccountNonLocked;
            return this;
        }

        public Builder enabled(boolean isAccountEnable) {
            this.enabled = isAccountEnable;
            return this;
        }

        public JWTokenUserDetails build() {
            return new JWTokenUserDetails(this);
        }
    }
}