package com.electricitysystem.service.impl;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.electricitysystem.entity.AccountEntity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AccountDetails implements UserDetails {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String username;

    @JsonIgnore
    private String password;

    List<GrantedAuthority> authorities = null;

    public AccountDetails(Integer id, String username, String password,
                              List<GrantedAuthority> authorities) {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static AccountDetails build(AccountEntity account) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (account.getRole()==0) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return new AccountDetails(
                account.getId(),
                account.getUsername(),
                account.getPassword(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Integer getId() {
        return id;
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