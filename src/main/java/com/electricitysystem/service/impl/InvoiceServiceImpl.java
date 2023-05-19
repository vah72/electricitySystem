package com.electricitysystem.service.impl;

import com.electricitysystem.entity.InvoiceEntity;
import com.electricitysystem.repository.InvoiceRepository;
import com.electricitysystem.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired(required = false)
    private InvoiceRepository invoiceRepository;
    @Override
    public List<InvoiceEntity> getAllByUsername(String username) {
        return invoiceRepository.findAllByUsername(username);
    }

    @Override
    public InvoiceEntity getByToken(String token) {
        return invoiceRepository.findByToken(token);
    }

    @Override
    public InvoiceEntity getById(int id) {
        return invoiceRepository.getById(id);
    }

    @Override
    public List<InvoiceEntity> getAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public List<InvoiceEntity> getAllByStatus(String status) {
        return invoiceRepository.getAllByStatus(status);
    }

    @Override
    public InvoiceEntity updateStatus(int id, String status) {
        InvoiceEntity invoice = invoiceRepository.getById(id);
        invoice.setStatus(status);
        return invoiceRepository.save(invoice);
    }
}
