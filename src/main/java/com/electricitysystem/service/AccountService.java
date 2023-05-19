package com.electricitysystem.service;

import com.electricitysystem.dto.AccountDto;
import com.electricitysystem.entity.AccountEntity;
import com.electricitysystem.entity.CustomerEntity;
import com.electricitysystem.entity.StaffEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AccountService extends UserDetailsService {
    AccountEntity getAccountEntityByUserName (String username);

    AccountEntity login(AccountDto accountDto);

}
