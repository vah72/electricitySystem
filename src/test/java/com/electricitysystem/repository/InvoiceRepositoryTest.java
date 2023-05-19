package com.electricitysystem.repository;

import com.electricitysystem.entity.InvoiceEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@DisplayName("InvoiceRepository Tests")
@Transactional
public class InvoiceRepositoryTest {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Test
    void testFindAllByUsernameWithNotNullExpected_ReturnCorrectSize(){
        String username = "HD11300001";
        List<InvoiceEntity> list = invoiceRepository.findAllByUsername(username);
        Assertions.assertThat(list).isNotNull();
        Assertions.assertThat(list.size()).isEqualTo(1);
        for(InvoiceEntity i : list)
            Assertions.assertThat(i.getUsername()).isEqualTo(username);
    }

    @Test
    void testFindAllByUsernameWithNullExpected_ReturnZero(){
        String username = "HD1131000";
        List<InvoiceEntity> list = invoiceRepository.findAllByUsername(username);
        Assertions.assertThat(list.size()).isEqualTo(0);
    }

    @Test
    void testFindAllByUsernameWithNotExistedUsername_ReturnZero(){
        String username = "notexisted";
        List<InvoiceEntity> list = invoiceRepository.findAllByUsername(username);
        Assertions.assertThat(list.size()).isEqualTo(0);
    }

    @Test
    void testGetAllByStatusWithNotNullExpected_ReturnCorrectSize() {
        String status = "UNPAID";
        List<InvoiceEntity> list = invoiceRepository.getAllByStatus(status);
        Assertions.assertThat(list).isNotNull();
        Assertions.assertThat(list.size()).isEqualTo(3);
        for(InvoiceEntity i : list)
            Assertions.assertThat(i.getStatus()).isEqualTo(status);
    }

    @Test
    void testGetAllByStatusWithNullExpected_ReturnZero() {
        String status = "PAID";
        List<InvoiceEntity> list = invoiceRepository.getAllByStatus(status);
        Assertions.assertThat(list.size()).isEqualTo(0);
    }

    @Test
    void testGetAllByStatusWithNotExistedStatus_ReturnZero() {
        String status = "NOTEXISTED";
        List<InvoiceEntity> list = invoiceRepository.getAllByStatus(status);
        Assertions.assertThat(list.size()).isEqualTo(0);
    }

    @Test
    void testGetByIdWithExistedId_ReturnCorrectObject() {
        int id = 1;
        InvoiceEntity invoice = invoiceRepository.getById(id);
        Assertions.assertThat(invoice).isNotNull();
        Assertions.assertThat(invoice.getId()).isEqualTo(id);
    }

    @Test
    void testGetByIdWithNotExistedId_ReturnNull() {
        int id = 100;
        InvoiceEntity invoice = invoiceRepository.getById(id);
        Assertions.assertThat(invoice).isNull();
    }

    @Test
    void testFindByTokenWithExistedToken_ReturnCorrectObject() {
        //add token
        String token = "EC-0Y889948HL5202537";
        InvoiceEntity invoice = invoiceRepository.findByToken(token);
        Assertions.assertThat(invoice).isNotNull();
        Assertions.assertThat(invoice.getToken()).isEqualTo(token);
    }

    @Test
    void testGetByIdWithNotExistedToken_ReturnNull() {
        String token = "notexistedtoken";
        InvoiceEntity invoice = invoiceRepository.findByToken(token);
        Assertions.assertThat(invoice).isNull();
    }

    @Test
    void testFindByUsernameAndStatusWithCorrectInput_ReturnCorrectObject() {
        String username = "HD11300001";
        String status = "UNPAID";
        InvoiceEntity invoice = invoiceRepository.findByUsernameAndStatus(username, status);
        Assertions.assertThat(invoice).isNotNull();
        Assertions.assertThat(invoice.getUsername()).isEqualTo(username);
        Assertions.assertThat(invoice.getStatus()).isEqualTo(status);
    }

    @Test
    void testFindByUsernameAndStatusWithIncorrectUsername_ReturnNull() {
        String username = "wrongUsername";
        String status = "UNPAID";
        InvoiceEntity invoice = invoiceRepository.findByUsernameAndStatus(username, status);
        Assertions.assertThat(invoice).isNull();
    }

    @Test
    void testFindByUsernameAndStatusWithIncorrectStatus_ReturnNull() {
        String username = "HD11300001";
        String status = "Incorrect";
        InvoiceEntity invoice = invoiceRepository.findByUsernameAndStatus(username, status);
        Assertions.assertThat(invoice).isNull();
    }
}