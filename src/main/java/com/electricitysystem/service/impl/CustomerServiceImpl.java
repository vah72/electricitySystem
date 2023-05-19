package com.electricitysystem.service.impl;

import com.electricitysystem.entity.CustomerEntity;
import com.electricitysystem.entity.InvoiceEntity;
import com.electricitysystem.repository.CustomerRepository;
import com.electricitysystem.repository.InvoiceRepository;
import com.electricitysystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired(required = false)
    CustomerRepository customerRepository;

    @Autowired(required = false)
    InvoiceRepository invoiceRepository;
    @Override
    public CustomerEntity getCustomerByUsername(String username) {
        return customerRepository.getByUsername(username);
    }

    @Override
    public List<CustomerEntity> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public CustomerEntity updateStatus(String username, String status) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateFm = sdf.format(date);
        CustomerEntity customer = customerRepository.getByUsername(username);

        InvoiceEntity invoice = invoiceRepository.findByUsernameAndStatus(username, status);
        if (invoice != null) {
            String lastDayPay = invoice.getLastTimePay();

            if (dateFm.compareTo(lastDayPay) > 0) {
                customer.setStatus("INACTIVE");
            }
        }
        return customerRepository.save(customer);
    }
}
