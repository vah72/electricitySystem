package com.electricitysystem.service.impl;


import com.electricitysystem.entity.AccountEntity;
import com.electricitysystem.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountDetailServiceImpl implements UserDetailsService {

    @Autowired(required = false)
    AccountRepository accountRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)  {
        AccountEntity account = accountRepository.getAccountEntityByUsername(username);
        if(account==null){
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }
        return AccountDetails.build(account);
    }
}