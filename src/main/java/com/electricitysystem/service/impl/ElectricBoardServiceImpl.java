package com.electricitysystem.service.impl;

import com.electricitysystem.entity.CustomerEntity;
import com.electricitysystem.entity.ElectricBoardEntity;
import com.electricitysystem.entity.InvoiceEntity;
import com.electricitysystem.repository.CustomerRepository;
import com.electricitysystem.repository.ElectricBoardRepository;
import com.electricitysystem.repository.InvoiceRepository;
import com.electricitysystem.service.CalculatorService;
import com.electricitysystem.service.ElectricBoardService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ElectricBoardServiceImpl implements ElectricBoardService {

    @Autowired(required = false)
    private ElectricBoardRepository electricBoardRepository;
    @Autowired(required = false)
    private InvoiceRepository invoiceRepository;
    @Autowired(required = false)
    private CustomerRepository customerRepository;
    @Autowired(required = false)
    protected CalculatorService calculatorService;

    @Override
    public List<ElectricBoardEntity> create(@RequestParam("file") MultipartFile file) throws IOException {

        int thisMonth = DateTime.now().getMonthOfYear() - 1;
        int thisYear = DateTime.now().getYear();
        String cPeriod;
        if (thisMonth < 10) {
            cPeriod = "0" + thisMonth + "-" + thisYear;
        } else {
            cPeriod = thisMonth + "-" + thisYear;
        }
        List<ElectricBoardEntity> entities;
        List<ElectricBoardEntity> eEntities = electricBoardRepository.findAllByPeriod(cPeriod);
        if (eEntities.size() > 0) {
            entities = eEntities;
        } else {
            entities = new ArrayList<>();
        }
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());

        // Read data form excel file sheet1.
        XSSFSheet worksheet = workbook.getSheetAt(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        if (eEntities.size() > 0) {
            for (int index = 1; index < worksheet.getPhysicalNumberOfRows(); index++) {
                XSSFRow row = worksheet.getRow(index);
                int newNumber = (int) (row.getCell(8).getNumericCellValue());
                String username = row.getCell(2).getStringCellValue();
                ElectricBoardEntity cEntity = electricBoardRepository.findByPeriodAndUsername(cPeriod, username);
                updateNewNumber(row, newNumber, cEntity);
                InvoiceEntity uInvoice = invoiceRepository.getById(cEntity.getId());
                uInvoice.setElectricNumber(cEntity.getTotalNumber());
                uInvoice.setTotalPayment(cEntity.getTotalPayment());
                invoiceRepository.save(uInvoice);
                if (cEntity.getTotalNumber() == 0) {
                    continue;
                }
                CustomerEntity customer = customerRepository.getByUsername(cEntity.getUsername());
                customer.setCheckUpdate(Objects.equals(cEntity.getPeriod(), cPeriod));
                customerRepository.save(customer);

            }
        } else {
            for (int index = 1; index < worksheet.getPhysicalNumberOfRows(); index++) {
                XSSFRow row = worksheet.getRow(index);

                ElectricBoardEntity electricBoard = new ElectricBoardEntity();
                String period = row.getCell(1).getStringCellValue();
                String username = row.getCell(2).getStringCellValue();
                electricBoard.setPeriod(period);
                electricBoard.setUsername(username);
                electricBoard.setMeterCode(row.getCell(3).getStringCellValue());
                electricBoard.setAddress(row.getCell(4).getStringCellValue());
                electricBoard.setCustomerName(row.getCell(6).getStringCellValue());
                electricBoard.setOldNumber((int) (row.getCell(7).getNumericCellValue()));
                int newNumber = (int) (row.getCell(8).getNumericCellValue());
                electricBoard.setNewNumber(newNumber);
                electricBoard.setTimeUpdate(LocalDateTime.now());
                electricBoard.setTimeReadMeter(sdf.format(new LocalDate().toDate()));

                updateNewNumber(row, newNumber, electricBoard);

                entities.add(electricBoard);
            }
            electricBoardRepository.saveAll(entities);

            for (ElectricBoardEntity i : entities) {
                InvoiceEntity invoice = new InvoiceEntity();
                invoice.setId(i.getId());
                invoice.setElectricNumber(i.getTotalNumber());
                invoice.setUsername(i.getUsername());
                invoice.setCustomerName(i.getCustomerName());
                invoice.setTotalPayment(i.getTotalPayment());
                invoice.setStatus("UNPAID");
                invoice.setAddress(i.getAddress());
                LocalDate nextWeek = new LocalDate().plusDays(7);
                Date date = nextWeek.toDate();
                invoice.setLastTimePay(sdf.format(date));
                invoice.setElectricNumber(i.getTotalNumber());
                invoice.setElectricBoardId(i.getId());
                invoiceRepository.save(invoice);

                if (i.getTotalNumber() == 0) {
                    continue;
                }
                CustomerEntity customer = customerRepository.getByUsername(i.getUsername());
                customer.setCheckUpdate(Objects.equals(i.getPeriod(), cPeriod));
                customerRepository.save(customer);
            }

        }
        return electricBoardRepository.saveAll(entities);
    }

    @Override
    public ElectricBoardEntity update(Integer id, int newNumber) {
        ElectricBoardEntity electricBoard = electricBoardRepository.findElectricBoardById(id);
        electricBoard.setNewNumber(newNumber);
        electricBoardRepository.save(electricBoard);
        int totalNumber = electricBoard.getNewNumber() - electricBoard.getOldNumber();
        long totalPayment = calculatorService.calculator(totalNumber);
        electricBoard.setTotalNumber(totalNumber);
        electricBoard.setTotalPayment(totalPayment);
        InvoiceEntity invoice = invoiceRepository.getById(id);
        invoice.setElectricNumber(totalNumber);
        invoice.setTotalPayment(totalPayment);
        invoiceRepository.save(invoice);
        return electricBoardRepository.save(electricBoard);
    }

    private void updateNewNumber(XSSFRow row, int newNumber, ElectricBoardEntity e) {
        if (newNumber == 0) {
            e.setTotalNumber(0);
            e.setTotalPayment(0L);
        } else {
            int totalNumber = (int) (row.getCell(8).getNumericCellValue()) -
                    (int) (row.getCell(7).getNumericCellValue());
            e.setNewNumber(newNumber);
            e.setTotalNumber(totalNumber);
            e.setTotalPayment(calculatorService.calculator(totalNumber));
        }
        electricBoardRepository.save(e);
    }


    @Override
    public List<ElectricBoardEntity> getAllByCustomerUserName(String username) {
        return electricBoardRepository.findAllByUsername(username);
    }

    @Override
    public ElectricBoardEntity getOneById(Integer electricBoardId) {
        return electricBoardRepository.findElectricBoardById(electricBoardId);
    }

    @Override
    public List<ElectricBoardEntity> getAllByPeriod(String period) {
        return electricBoardRepository.findAllByPeriod(period);
    }
}
