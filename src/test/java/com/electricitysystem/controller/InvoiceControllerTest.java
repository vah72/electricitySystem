package com.electricitysystem.controller;

import com.electricitysystem.entity.InvoiceEntity;
import com.electricitysystem.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(InvoiceController.class)
public class InvoiceControllerTest {

    @InjectMocks
    private InvoiceController invoiceController;

    @Mock
    private InvoiceService invoiceService;

    @Test
    public void testGetAllByUsernameSuccessWithNotNulResult_ReturnList() {
        String username = "HD11300001";
        List<InvoiceEntity> list = new ArrayList<>();
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setUsername("HD11300001");
        list.add(invoice);
        when(invoiceService.getAllByUsername(username))
                .thenReturn(list);
        ResponseEntity<?> response = invoiceController.getAllByUsername(username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
    }

    @Test
    public void testGetAllByUsernameSuccessWithNullResult_ReturnMessage() {
        String username = "HD11300002";
        ResponseEntity<?> response = invoiceController.getAllByUsername(username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Không có hóa đơn nào", response.getBody());
    }


    @Test
    public void testGetAllByUsernameWithNotExistUsername_ReturnMessage() {
        String username = "HD11310000";
        when(invoiceService.getAllByUsername(username))
                .thenReturn(null);
        ResponseEntity<?> response = invoiceController.getAllByUsername(username);
        assertEquals("Thông tin khách hàng không chính xác", response.getBody());
    }

    @Test
    public void testGetByStatusWithNotNullResult_ReturnList() {
        String status = "UNPAID";
        List<InvoiceEntity> list = new ArrayList<>();
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setUsername("HD11300001");
        invoice.setStatus("UNPAID");
        list.add(invoice);
        when(invoiceService.getAllByStatus(status))
                .thenReturn(list);
        ResponseEntity<?> response = invoiceController.getByStatus(status);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
    }

    @Test
    public void testGetByStatusWithNullResult_ReturnMessage() {
        String status = "PAID";
        when(invoiceService.getAllByStatus(status))
                .thenReturn(null);
        ResponseEntity<?> response = invoiceController.getByStatus(status);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Không có hóa đơn nào", response.getBody());
    }

    @Test
    public void testGetAllWithNotNullResult_ReturnList() {
        List<InvoiceEntity> list = new ArrayList<>();
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setUsername("HD11300001");
        list.add(invoice);
        when(invoiceService.getAll())
                .thenReturn(list);
        ResponseEntity<?> response = invoiceController.getAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
    }

    @Test
    public void testGetAllWithNullResult_ReturnMessage() {
        when(invoiceService.getAll())
                .thenReturn(null);
        ResponseEntity<?> response = invoiceController.getAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Không có hóa đơn nào", response.getBody());
    }


    @Test
    public void testGetByIdSuccessWithNotNullResult_ReturnInvoice() {
        int id = 1;
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setId(1);
        invoice.setUsername("HD11300001");
        when(invoiceService.getById(id))
                .thenReturn(invoice);
        ResponseEntity<?> response = invoiceController.getById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(invoice, response.getBody());
    }

    @Test
    public void testGetByIdWithNotExistId_ReturnMessage() {
        int id = 1;
        when(invoiceService.getById(id))
                .thenReturn(null);
        ResponseEntity<?> response = invoiceController.getById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Không tìm thấy hóa đơn", response.getBody());
    }

    @Test
    public void testUpdateStatusSuccess_ReturnInvoice() {
        int id = 1;
        String status = "PAID";
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setId(1);
        invoice.setUsername("HD11300001");
        invoice.setStatus("PAID");

        when(invoiceService.updateStatus(id, status))
                .thenReturn(invoice);

        ResponseEntity<?> response = invoiceController.updateStatus(id, status);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(invoice, response.getBody());
    }

}