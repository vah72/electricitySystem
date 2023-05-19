package com.electricitysystem.controller;

import com.electricitysystem.entity.InvoiceEntity;
import com.electricitysystem.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/getAllByUsername")
    public ResponseEntity<?> getAllByUsername(
            @RequestParam String username) {
        return ResponseEntity.ok(invoiceService.getAllByUsername(username));
    }

    @GetMapping("/getByStatus")
    public ResponseEntity<?>  getByStatus(
            @RequestParam String status
    ) {
        return ResponseEntity.ok(invoiceService.getAllByStatus(status));
    }
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(invoiceService.getAll());
    }

    @GetMapping("/getById")
    public ResponseEntity<?> getById(
            @RequestParam int id
    ) {
        return ResponseEntity.ok(invoiceService.getById(id));
    }
    @PostMapping("/updateStatus")
    public ResponseEntity<?> updateStatus(
            @RequestParam int id,
            @RequestParam String status
    ) {
        return ResponseEntity.ok(invoiceService.updateStatus(id, status));
    }

}

