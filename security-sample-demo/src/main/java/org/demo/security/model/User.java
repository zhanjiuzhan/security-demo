package org.demo.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author Administrator
 */
public class User implements UserDetails {

    private String username;
    private String role;
    private String password;
    private boolean isEnabled;
    Collection<? extends GrantedAuthority> grantedAuthorities;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isEnabled = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
        return isEnabled;
    }

    public void setGrantedAuthorities(Collection<? extends GrantedAuthority> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAuthorities() {

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return username.equals( ((User) obj).getUsername() );
        }
        return false;
    }

    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isEnabled=" + isEnabled +
                '}';
    }
}
