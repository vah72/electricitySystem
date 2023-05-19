package com.electricitysystem.service;

import com.electricitysystem.entity.InvoiceEntity;
import com.electricitysystem.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface InvoiceService {

    List<InvoiceEntity> getAllByUsername(String username);

    InvoiceEntity getByToken(String token);

    InvoiceEntity getById(int id);

    List<InvoiceEntity> getAll();

    List<InvoiceEntity> getAllByStatus(String status);

    InvoiceEntity updateStatus(int id, String status);
}
