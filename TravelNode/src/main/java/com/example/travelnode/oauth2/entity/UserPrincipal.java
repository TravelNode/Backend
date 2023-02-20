package com.example.travelnode.oauth2.entity;

import com.example.travelnode.entity.ProviderType;
import com.example.travelnode.entity.RoleType;
import com.example.travelnode.entity.Token;
import com.example.travelnode.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
public class UserPrincipal implements OAuth2User, UserDetails {
    private final Long uid;
    private final String uniqueId;
    private final String email;
    private final String nickname;
    private final RoleType roleType;
    private final ProviderType providerType;
    //private final String accessToken;
    //private final String refreshToken;
    private final Collection<GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private static Token token;

    public UserPrincipal(Long uid, String uniqueId, String email, String nickname, RoleType roleType,
                         ProviderType providerType, Collection<GrantedAuthority> authorities) {
        this.uid = uid;
        this.uniqueId = uniqueId;
        this.email = email;
        this.nickname = nickname;
        this.roleType = roleType;
        this.providerType = providerType;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(RoleType.USER.getCode()));

        return new UserPrincipal(
            user.getUid(), user.getUniqueId(), user.getEmail(), user.getNickname(),
            RoleType.USER, user.getProviderType(), authorities
        );
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);

        return userPrincipal;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return nickname;
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

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return String.valueOf(uid);
    }

    public String getEmail() {
        return email;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public ProviderType getProviderType() {
        return providerType;
    }
}
