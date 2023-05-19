package com.electricitysystem.controller;

import com.electricitysystem.entity.CustomerEntity;
import com.electricitysystem.service.CustomerService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    private CustomerEntity customer;

    @BeforeEach
    public void setUp() throws Exception {
        customer = new CustomerEntity("HD11300001", "Hoàng Vân Anh", "10 Trần Phú, Mộ Lao, Hà Đông", "0961082342",
                "hoangvananh7201@gmail.com", 1, "PAC001", "ACTIVE", false);
    }

    @Test
    public void testAllCustomerWithEmptyList_ReturnMessage() {
        when(customerService.findAll()).thenReturn(new ArrayList<>());
        ResponseEntity<?> responseEntity = customerController.allCustomer();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Không có khách hàng nào", responseEntity.getBody());
    }

    @Test
    public void testAllCustomerWithNotEmptyList_ReturnList() {
        List<CustomerEntity> list = new ArrayList<>();
        list.add(customer);
        when(customerService.findAll()).thenReturn(list);
        ResponseEntity<?> responseEntity = customerController.allCustomer();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(list, responseEntity.getBody());
    }
    @Test
    public void testFindOneByUsernameWithExistCustomer_ReturnCorrectCustomer() {
        String username = "HD11300001";
        when(customerService.getCustomerByUsername(username))
                .thenReturn(customer);
        ResponseEntity<?> response = customerController.findOneByUsername(username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customer, response.getBody());
    }

    @Test
    public void testFindOneByUsernameWithNotExistCustomer_ReturnMessage() {
        String username = "HD11310000";
        when(customerService.getCustomerByUsername(username))
                .thenReturn(null);
        ResponseEntity<?> response = customerController.findOneByUsername(username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Không tìm thấy khách hàng", response.getBody());
    }
}