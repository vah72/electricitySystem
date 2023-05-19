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
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @InjectMocks
    PaymentController paymentController;

    @Mock
    ElectricBoardService electricBoardService;

    @Mock
    PayWithCashService payWithCashService;

    @Mock
    InvoiceRepository invoiceRepository;
    @Mock
    InvoiceService invoiceService;
    @Mock
    PayWithPaypalService payWithPaypalService;

    @Mock
    Payment mockPayment;
    @Autowired
    MockMvc mockMvc;
    public static final String PAYPAL_SUCCESS_URL = "pay/success";
    public static final String PAYPAL_CANCEL_URL = "pay/cancel";

    @Test
    @Rollback
    public void testPaymentSuccess_ReturnHref() throws PayPalRESTException {
        ElectricBoardEntity electricBoard = new ElectricBoardEntity();
        electricBoard.setId(1);
        electricBoard.setTotalPayment(1000000);
        when(electricBoardService.getOneById(1)).thenReturn(electricBoard);

        Payment payment = new Payment();
        List<Links> links = new ArrayList<>();
        Links approvalLink = new Links("https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=EC-80840661GV282352V","approval_url");
        links.add(approvalLink);
        payment.setLinks(links);
        when(payWithPaypalService.createPayment(Double.valueOf(electricBoard.getTotalPayment()) / 23447, "USD", "paypal",
                "sale", "thanh toan tien dien"
                , "http://localhost:9090/" + PAYPAL_CANCEL_URL,
                "http://localhost:9090/" + PAYPAL_SUCCESS_URL))
                .thenReturn(payment);

        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setId((1));
        invoice.setUsername("HD11300001");
        invoice.setStatus("PAYMENT PENDING");
        when(invoiceService.getById(1)).thenReturn(invoice);

        ResponseEntity<?> response = paymentController.payment(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        for(Links link:payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                assertEquals(link.getHref(), response.getBody());
            }
        }

        verify(invoiceRepository, times(1)).save(invoice);
        assertEquals("EC-80840661GV282352V", invoice.getToken());
    }

    @Test
    public void testPaymentFailWithNotExistId_ReturnMessage(){
        when(electricBoardService.getOneById(1))
                .thenReturn(null);
        ResponseEntity<?> response = paymentController.payment(1);
        assertEquals("Không tìm thấy mã hóa đơn", response.getBody());
    }

    @Test
    public void testPaymentFailWithPayPalRestException() throws PayPalRESTException {
        ElectricBoardEntity electricBoard = new ElectricBoardEntity();
        electricBoard.setId(1);
        electricBoard.setTotalPayment(1000000);
        when(electricBoardService.getOneById(1)).thenReturn(electricBoard);

        Payment payment = new Payment();
        List<Links> links = new ArrayList<>();
        Links approvalLink = new Links("https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=EC-80840661GV282352V","approval_url");
        links.add(approvalLink);
        payment.setLinks(links);
        when(payWithPaypalService.createPayment(Double.valueOf(electricBoard.getTotalPayment()) / 23447, "USD", "paypal",
                "sale", "thanh toan tien dien"
                , "http://localhost:9090/" + PAYPAL_CANCEL_URL,
                "http://localhost:9090/" + PAYPAL_SUCCESS_URL))
                .thenThrow(new PayPalRESTException("Paypal API error"));

        ResponseEntity<?> response = paymentController.payment(1);
        assertEquals("payment pending", response.getBody());
    }

    @Test
    public void testPaymentFailWithEmptyLinks_ReturnHref() throws PayPalRESTException {
        ElectricBoardEntity electricBoard = new ElectricBoardEntity();
        electricBoard.setId(1);
        electricBoard.setTotalPayment(1000000);
        when(electricBoardService.getOneById(1)).thenReturn(electricBoard);

        List<Links> links = new ArrayList<>();
        when(payWithPaypalService.createPayment(Double.valueOf(electricBoard.getTotalPayment()) / 23447, "USD", "paypal",
                "sale", "thanh toan tien dien"
                , "http://localhost:9090/" + PAYPAL_CANCEL_URL,
                "http://localhost:9090/" + PAYPAL_SUCCESS_URL))
                .thenReturn(mockPayment);
        when(mockPayment.getLinks()).thenReturn(links);

        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setId((1));
        invoice.setUsername("HD11300001");
        invoice.setStatus("PAYMENT PENDING");
        when(invoiceService.getById(1)).thenReturn(invoice);

        ResponseEntity<?> response = paymentController.payment(1);
        assertEquals("payment pending", response.getBody());
    }

    @Test
    public void testPaymentFailWithNotFoundRel_ReturnHref() throws PayPalRESTException {
        ElectricBoardEntity electricBoard = new ElectricBoardEntity();
        electricBoard.setId(1);
        electricBoard.setTotalPayment(1000000);
        when(electricBoardService.getOneById(1)).thenReturn(electricBoard);

        List<Links> links = new ArrayList<>();
        Links approvalLink = new Links("https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=EC-80840661GV282352V","approval");
        links.add(approvalLink);
        mockPayment.setLinks(links);
        when(payWithPaypalService.createPayment(Double.valueOf(electricBoard.getTotalPayment()) / 23447, "USD", "paypal",
                "sale", "thanh toan tien dien"
                , "http://localhost:9090/" + PAYPAL_CANCEL_URL,
                "http://localhost:9090/" + PAYPAL_SUCCESS_URL))
                .thenReturn(mockPayment);
        when(mockPayment.getLinks()).thenReturn(links);


        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setId((1));
        invoice.setUsername("HD11300001");
        invoice.setStatus("PAYMENT PENDING");
        when(invoiceService.getById(1)).thenReturn(invoice);

        ResponseEntity<?> response = paymentController.payment(1);
        assertEquals("payment pending", response.getBody());
    }
    @Test
    public void testCancelPaySuccess_ReturnMessage() {
        String token = "EC-80840661GV282352V";
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setId((1));
        invoice.setUsername("HD11300001");
        invoice.setToken("EC-80840661GV282352V");
        invoice.setStatus("PAYMENT PENDING");

        when(invoiceService.getByToken("EC-80840661GV282352V"))
                .thenReturn(invoice);
        ResponseEntity<?> response = paymentController.cancelPay(token);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals( "payment failed", response.getBody());


        verify(invoiceRepository, times(1)).save(invoice);
        assertNotNull(invoice.getPaymentDate());
        assertEquals("UNPAID", invoice.getStatus());
    }


    @Test
    public void testCancelFailWithNotExistToken_ReturnMessage() {
        String token = "EC-00000000000";
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setId((1));
        invoice.setUsername("HD11300001");
        invoice.setToken("EC-80840661GV282352V");
        invoice.setStatus("PAYMENT PENDING");

        when(invoiceService.getByToken(token))
                .thenReturn(null);
        ResponseEntity<?> response = paymentController.cancelPay(token);
        assertEquals( "Not found payment", response.getBody());
    }

    @Test
    public void testSuccessPaySuccess_ReturnMessage() throws PayPalRESTException {
        String paymentId = "PAYID-MRLCXCI4VB85826W14638726";
        String token = "EC-1H441396HC3924138";
        String PayerID = "8SVHKPHUUZL7E";
        Payment payment = new Payment();
        payment.setState("approved");
        when(payWithPaypalService.executePayment(paymentId, PayerID))
                .thenReturn(payment);

        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setId((1));
        invoice.setUsername("HD11300001");
        invoice.setToken("EC-1H441396HC3924138");
        invoice.setStatus("PAYMENT PENDING");
        when(invoiceService.getByToken(token))
                .thenReturn(invoice);

        ResponseEntity<?> response = paymentController.successPay(paymentId, token, PayerID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals( "payment success", response.getBody());


        verify(invoiceRepository, times(1)).save(invoice);
        assertNotNull(invoice.getPaymentDate());
        assertEquals("PAID", invoice.getStatus());

    }

    @Test
    public void testSuccessPayFailWithPayPalRESTException_ReturnMessage() throws PayPalRESTException {
        String paymentId = "PAYID-MRLCXCI4VB85826W14638726";
        String token = "EC-1H441396HC3924138";
        String PayerID = "8SVHKPHUUZL7E";
        Payment payment = new Payment();
        payment.setState("approved");
        when(payWithPaypalService.executePayment(paymentId, PayerID))
                .thenThrow(new PayPalRESTException("Paypal API error"));

        ResponseEntity<?> response = paymentController.successPay(paymentId, token, PayerID);
        assertEquals( "payment fail", response.getBody());

    }

    @Test
    public void testSuccessPayFailWithStateDifferentApproved_ReturnMessage() throws PayPalRESTException {
        String paymentId = "PAYID-MRLCXCI4VB85826W14638726";
        String token = "EC-1H441396HC3924138";
        String PayerID = "8SVHKPHUUZL7E";
        mockPayment.setState("disapproved");
        when(mockPayment.getState()).thenReturn("disapproved");
        when(payWithPaypalService.executePayment(paymentId, PayerID))
                .thenReturn(mockPayment);

        ResponseEntity<?> response = paymentController.successPay(paymentId, token, PayerID);
        assertEquals( "payment fail", response.getBody());

    }
    @Test
    public void testPayWithCashWithExistId_ReturnMessage() throws Exception {
        when(payWithCashService.payWithCash(1)).thenReturn("Vui long den diem thanh toan gan nhat de dong tien dien.");
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
        mockMvc.perform(post("/payWithCash").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Vui long den diem thanh toan gan nhat de dong tien dien."));

    }
}