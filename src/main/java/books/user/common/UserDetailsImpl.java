package books.user.common;


import books.user.domain.Authority;
import books.user.domain.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class UserDetailsImpl implements UserDetails {
    private User user;
    private List<Authority> authorities;

    public UserDetailsImpl(User user, List<Authority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities
                .stream()
                .map(authority -> (GrantedAuthority) authority::getAuthority)
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return isEnabledAccount();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isEnabledAccount();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isEnabledAccount();
    }

    @Override
    public boolean isEnabled() {
        return isEnabledAccount();
    }

    private boolean isEnabledAccount() {
        return user.isEnabled();
    }
}
