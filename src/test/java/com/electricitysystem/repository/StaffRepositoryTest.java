package com.electricitysystem.repository;

import com.electricitysystem.entity.StaffEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("StaffRepository Tests")
@Transactional
public class StaffRepositoryTest {

    @Autowired
    StaffRepository staffRepository;

    @Test
    void testGetStaffEntityByIdWithExistedId_ReturnCorrectStaff() {
        int id = 1;
        StaffEntity staff = staffRepository.getStaffEntityById(id);
        Assertions.assertNotNull(staff);
        Assertions.assertEquals(staff.getId(), id);
    }

    @Test
    void testGetStaffEntityByIdWithNotExistedId_ReturnNull() {
        int id = 100;
        StaffEntity staff = staffRepository.getStaffEntityById(id);
        Assertions.assertNull(staff);
    }

    @Test
    void testGetStaffEntityByIdWithExistedUsername_ReturnCorrectStaff() {
        String username = "ngochoai";
        StaffEntity staff = staffRepository.getStaffEntityByUsername(username);
        Assertions.assertNotNull(staff);
        Assertions.assertEquals(staff.getUsername(),username);
    }

    @Test
    void testGetStaffEntityByIdWithNotExistedUsername_ReturnNull() {
        String username = "notexisted";
        StaffEntity staff = staffRepository.getStaffEntityByUsername(username);
        Assertions.assertNull(staff);
    }
}