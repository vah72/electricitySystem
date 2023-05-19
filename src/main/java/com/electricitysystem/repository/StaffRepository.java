package com.electricitysystem.repository;

import com.electricitysystem.entity.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<StaffEntity, Integer> {
    StaffEntity getStaffEntityById(int id);

    StaffEntity getStaffEntityByUsername(String username);
}
