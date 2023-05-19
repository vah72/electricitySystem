package com.electricitysystem.jwt;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer ";
    private Integer id;
    private String username;
    private String roles;

    private String code;
    private String status;

    public JwtResponse(String token, Integer id, String username,
                       String roles, String code, String status) {
        super();
        this.token = token;
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.code=code;
        this.status=status;
    }


}