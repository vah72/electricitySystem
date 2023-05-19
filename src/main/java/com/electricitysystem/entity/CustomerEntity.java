package com.electricitysystem.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class CustomerEntity {
    @Id
    @Column(name = "id", nullable = false, unique = true, length = 10)
    private String username;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private int gender;
    @Column(name = "meter_code", unique = true)
    private String meterCode;
    private String status = "ACTIVE";
    private boolean checkUpdate = false;
}
