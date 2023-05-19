package com.electricitysystem.repository;

import com.electricitysystem.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, String> {
    CustomerEntity getByUsername(String username);

}
