package com.electricitysystem.service;

import com.electricitysystem.entity.CustomerEntity;

import java.util.List;

public interface CustomerService {

    CustomerEntity getCustomerByUsername(String username);


    List<CustomerEntity> findAll();

    CustomerEntity updateStatus(String username, String status);
}
