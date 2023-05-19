package com.electricitysystem.controller;

import com.electricitysystem.dto.CustomerDto;
import com.electricitysystem.entity.CustomerEntity;
import com.electricitysystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping("getListCustomersInfo")
    public ResponseEntity<?> allCustomer(){
        List<CustomerEntity> list = new ArrayList<>();
        list=customerService.findAll();
        if (list.isEmpty())
            return ResponseEntity.ok("Không có khách hàng nào");
        else return ResponseEntity.ok(list);
    }

    @GetMapping("getCustomerInfo/{username}")
    public ResponseEntity<?> findOneByUsername(@PathVariable String username){
        CustomerEntity customer = customerService.getCustomerByUsername(username);
        if (customer==null)
            return ResponseEntity.ok("Không tìm thấy khách hàng");
        return ResponseEntity.ok(customer);

    }

}
