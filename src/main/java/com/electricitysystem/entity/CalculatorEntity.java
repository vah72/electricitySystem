package com.electricitysystem.entity;

import lombok.Data;

@Data
public class CalculatorEntity {
    private int totalNumber;
    private int numLevel1 = 0;
    private int numLevel2 = 0;
    private int numLevel3 = 0;
    private int numLevel4 = 0;
    private int numLevel5 = 0;
    private int numLevel6 = 0;
    private double vat = 0.1;
    private double priceLv1;
    private double priceLv2;
    private double priceLv3;
    private double priceLv4;
    private double priceLv5;
    private double priceLv6;
}
