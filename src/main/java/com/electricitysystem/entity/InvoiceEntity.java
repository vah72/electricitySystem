package com.electricitysystem.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.time.LocalDateTime;
import java.util.Date;
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invoice")
public class InvoiceEntity {
    @Id
    private Integer id;
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    @Column(name = "total_payment")
    private long totalPayment;
    @Column(name = "electric_number")
    private int electricNumber;
    @Column(name = "status")
    private String status;
    @Column(name = "username")
    private String username;
    @Column(name = "customer_name")
    private String customerName;
    @Column(name = "electric_board_id")
    private Integer electricBoardId;
    private String token="";
    private String address;
    @Column(name = "last_time_pay")
    private String lastTimePay;
}
