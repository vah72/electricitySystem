package com.electricitysystem.repository;

import com.electricitysystem.entity.AccountEntity;
import com.electricitysystem.entity.CustomerEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("CustomerRepository Tests")
@Transactional
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void testGetCustomerByUserNameWithExitedCustomer_ReturnNotNull(){
        String username = "HD11300001";
        CustomerEntity customer = customerRepository.getByUsername(username);
        Assertions.assertThat(customer).isNotNull();
        Assertions.assertThat(customer.getUsername()).isEqualTo(username);
    }

    @Test
    public void testGetCustomerByUserNameWithNotExitedCustomer_ReturnNull(){
        String username = "notexited";
        CustomerEntity customer = customerRepository.getByUsername(username);
        Assertions.assertThat(customer).isNull();
    }

    @Test
    public void testUpdateStatusByUserNameSuccess_ReturnCustomer(){
        String username = "HD11300001";
        CustomerEntity customer = customerRepository.getByUsername(username);
        customer.setStatus("CHANGE");
        customerRepository.save(customer);
        Assertions.assertThat(customer).isNotNull();
        Assertions.assertThat(customer.getStatus()).isEqualTo("CHANGE");
    }

    @Test
    public void testUpdateCheckByUserNameSuccessWithNot_ReturnCustomer(){
        String username = "HD11300001";
        CustomerEntity customer = customerRepository.getByUsername(username);
        customer.setCheckUpdate(true);
        customerRepository.save(customer);
        Assertions.assertThat(customer).isNotNull();
        Assertions.assertThat(customer.isCheckUpdate()).isTrue();
    }




}