package com.electricitysystem.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.Serializable;
import java.security.SecureRandom;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "account")
public class AccountEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true, length = 10)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private int role;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @OneToOne
    @JoinColumn(name = "staff_id")
    @JsonBackReference
    private StaffEntity staff;

    public String hashPassword(String password){
        int salt=10;
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(salt, new SecureRandom());
        return bCryptPasswordEncoder.encode(password);
    }

}

