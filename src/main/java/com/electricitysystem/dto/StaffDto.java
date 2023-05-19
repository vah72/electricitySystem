package com.electricitysystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffDto {
    String name;
    String address;
    String phoneNumber;
    String email;
    int gender;
}
