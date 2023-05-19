package com.electricitysystem.entity;

import javax.persistence.*;
import javax.servlet.http.PushBuilder;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "electric_board")
public class ElectricBoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "meter_code", nullable = false)
    private String meterCode;
    @Column(name = "old_number", nullable = false)
    @Min(0)
    private int oldNumber;
    @Column(name = "new_number", nullable = false)
    @Min(0)
    private int newNumber;
    @Column(name = "total_number", nullable = false)
    @Min(0)
    private int totalNumber;
    @Column(name = "time_read", nullable = true)
    private String timeReadMeter;
    @Column(name = "time_update", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime timeUpdate;
    @Column(name = "username")
    private String username;
    @Column(name = "total_payment")
    private long totalPayment;
    private String period;
    @Column(name = "customer_name")
    private String customerName;
    private String address;

    public ElectricBoardEntity(int id,String meterCode, int oldNumber, int newNumber, String timeReadMeter, String username, String period) {
        this.id = id;
        this.meterCode = meterCode;
        this.oldNumber = oldNumber;
        this.newNumber = newNumber;
        this.timeReadMeter = timeReadMeter;
        this.username = username;
        this.period = period;
    }
}
