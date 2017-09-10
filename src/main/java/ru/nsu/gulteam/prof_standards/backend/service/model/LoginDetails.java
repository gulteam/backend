package ru.nsu.gulteam.prof_standards.backend.service.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

public class LoginDetails implements UserDetails {
    private String username;
    private String password;
    private Set<GrantedAuthority> roles;

    public LoginDetails(String username, String password, Set<GrantedAuthority> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return roles;
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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
