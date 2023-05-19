package com.electricitysystem.service;

import com.electricitysystem.entity.StaffEntity;

public interface StaffService {


    StaffEntity getStaffByUsername(String username);
}

