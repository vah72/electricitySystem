package com.electricitysystem.service.impl;

import com.electricitysystem.entity.StaffEntity;
import com.electricitysystem.repository.StaffRepository;
import com.electricitysystem.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffServiceImpl implements StaffService {
    @Autowired(required = false)
    private StaffRepository staffRepository;

    @Override
    public StaffEntity getStaffByUsername(String username) {
        return staffRepository.getStaffEntityByUsername(username);
    }
}
