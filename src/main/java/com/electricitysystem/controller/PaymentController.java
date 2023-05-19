package com.electricitysystem.controller;

import com.electricitysystem.entity.ElectricBoardEntity;
import com.electricitysystem.entity.InvoiceEntity;
import com.electricitysystem.repository.InvoiceRepository;
import com.electricitysystem.service.ElectricBoardService;
import com.electricitysystem.service.InvoiceService;
import com.electricitysystem.service.PayWithCashService;
import com.electricitysystem.service.PayWithPaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/")
public class PaymentController {
    public static final String PAYPAL_SUCCESS_URL = "pay/success";
    public static final String PAYPAL_CANCEL_URL = "pay/cancel";

    @Autowired(required = false)
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceService invoiceService;
    @Autowired(required = false)
    private PayWithPaypalService payWithPaypalService;
    @Autowired
    private ElectricBoardService electricBoardService;
    @Autowired(required = false)
    private PayWithCashService payWithCashService;
    @PostMapping("/pay")
    public ResponseEntity<?> payment(@RequestParam int id) {
        String token = "";
        try {
            ElectricBoardEntity electricBoard = electricBoardService.getOneById(id);
            if(electricBoard!=null) {
                Payment payment = payWithPaypalService.createPayment(Double.valueOf (electricBoard.getTotalPayment())/ 23447, "USD", "paypal",
                        "sale", "thanh toan tien dien"
                        , "http://localhost:9090/" + PAYPAL_CANCEL_URL,
                        "http://localhost:9090/" + PAYPAL_SUCCESS_URL);
                System.out.println(payment);
                InvoiceEntity invoice = invoiceService.getById(id);
                invoice.setStatus("PAYMENT PENDING");
//            invoiceRepository.save(invoice);

                for (Links link : payment.getLinks()) {
                    if (link.getRel().equals("approval_url")) {
                        String[] s = link.getHref().split("=");
                        token = s[2];
                        invoice.setToken(token);
                        invoiceRepository.save(invoice);
                        return ResponseEntity.ok(link.getHref());
                    }
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("payment pending");
    }

    @GetMapping(value = PAYPAL_CANCEL_URL)
    public ResponseEntity<?> cancelPay(
            @RequestParam("token") String token) {
        InvoiceEntity invoice = invoiceService.getByToken(token);
        invoice.setPaymentDate(LocalDateTime.now());
        invoice.setStatus("UNPAID");
        invoiceRepository.save(invoice);
        return ResponseEntity.ok("payment failed");
    }
    @GetMapping(value = PAYPAL_SUCCESS_URL)
    public ResponseEntity<?> successPay(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("token") String token,
            @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = payWithPaypalService.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                InvoiceEntity invoice = invoiceService.getByToken(token);
                invoice.setPaymentDate(LocalDateTime.now());
                invoice.setStatus("PAID");
                invoiceRepository.save(invoice);
                return ResponseEntity.ok("payment success");
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());}
        return ResponseEntity.ok("payment success");}
    @PostMapping("payWithCash")
    public ResponseEntity<?> payWithCash(
            @RequestParam int id
    ) {
        return ResponseEntity.ok(payWithCashService.payWithCash(id));}
}
