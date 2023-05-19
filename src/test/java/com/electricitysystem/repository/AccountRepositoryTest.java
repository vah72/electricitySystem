package com.electricitysystem.repository;

import com.electricitysystem.entity.AccountEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@DisplayName("AccountRepository Tests")
//@Transactional
public class AccountRepositoryTest {


    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Test
    public void testGetAccountByUserWithExistedAccount_ReturnNotNull(){
        String username = "HD11300001";
        AccountEntity account = accountRepository.getAccountEntityByUsername(username);
        Assertions.assertThat(account).isNotNull();
        Assertions.assertThat(account.getUsername()).isEqualTo(username);
    }

    @Test
    public void testGetAccountByUserWithNotExistedAccount_ReturnNull(){
        String username = "nonexisted";
        AccountEntity account = accountRepository.getAccountEntityByUsername(username);
        Assertions.assertThat(account).isNull();
    }

    @Test
    public void updateAccount_changePassword(){
        String newPassword= "Password1";
        AccountEntity account = accountRepository.getAccountEntityByUsername("HD11300001");
        account.setPassword(account.hashPassword(newPassword));
        accountRepository.save(account);
        Assertions.assertThat(account).isNotNull();
        Assertions.assertThat(bCryptPasswordEncoder.matches(newPassword, account.getPassword())).isTrue();
    }




}