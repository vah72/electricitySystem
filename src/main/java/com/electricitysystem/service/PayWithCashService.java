package com.electricitysystem.service;

import com.electricitysystem.entity.ElectricBoardEntity;
import com.electricitysystem.entity.InvoiceEntity;
import com.electricitysystem.repository.CustomerRepository;
import com.electricitysystem.repository.ElectricBoardRepository;
import com.electricitysystem.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.SecondaryTable;
import java.sql.Time;
import java.time.LocalDateTime;

@Service
public class PayWithCashService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public String payWithCash(int id) {
        InvoiceEntity invoice = invoiceRepository.getById(id);
        return "Vui lòng đến điểm thanh toán gần nhất để đóng tiền điện.";
    }
}
