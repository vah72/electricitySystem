package com.electricitysystem.controller;

import com.electricitysystem.entity.StaffEntity;
import com.electricitysystem.service.StaffService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(StaffController.class)
public class StaffControllerTest {

    @InjectMocks
    StaffController staffController;

    @Mock
    StaffService staffService;

    @Test
    public void testGetStaffByUsernameWithExistUsername_ReturnCustomer() {
        String username = "admin123";
        StaffEntity staff = new StaffEntity();
        staff.setUsername("admin123");
        when(staffService.getStaffByUsername(username))
                .thenReturn(staff);
        ResponseEntity<?> response = staffController.getStaffByUsername(username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(staff, response.getBody());
    }

    @Test
    public void testGetStaffByUsernameWithNotExistUsername_ReturnMessage() {
        String username = "admin";
        when(staffService.getStaffByUsername(username))
                .thenReturn(null);
        ResponseEntity<?> response = staffController.getStaffByUsername(username);
        assertEquals("Không tìm thấy nhân viên", response.getBody());
    }
}