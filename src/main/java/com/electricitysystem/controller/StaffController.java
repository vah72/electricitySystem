package com.electricitysystem.controller;

import com.electricitysystem.entity.StaffEntity;
import com.electricitysystem.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StaffController {
    @Autowired
    private StaffService staffService;
    @GetMapping("getStaffInfo/{username}")
    public ResponseEntity<?> getStaffByUsername(@PathVariable String username){
        StaffEntity staff = staffService.getStaffByUsername(username);
        if (staff==null)
            return ResponseEntity.ok("Không tìm thấy nhân viên");
        return ResponseEntity.ok(staff);
    }
}
