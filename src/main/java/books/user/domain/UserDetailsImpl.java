package books.user.domain;

import books.user.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.transaction.Transactional;
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
        Collection<GrantedAuthority> collection = new HashSet<>();
        authorities.forEach(authority -> {
                    collection.add((GrantedAuthority) authority::getAuthority);
                });
        return collection;
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
