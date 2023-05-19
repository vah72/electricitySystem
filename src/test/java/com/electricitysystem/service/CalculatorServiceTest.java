package com.electricitysystem.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorServiceTest {


    private CalculatorService calculatorService = new CalculatorService();

    @Test
    @DisplayName("total=0")
    void testCalculatorWithTotalEqual0() {
        long result = calculatorService.calculator(0);
        assertEquals(0, result);
    }

    @Test
    @DisplayName("total<=50")
    void testCalculatorWithTotalNoBiggerThan50() {
        long result = calculatorService.calculator(50);
        assertEquals(86400+86400*0.1, result);
    }

    @Test
    @DisplayName("total>50")
    void testCalculatorWithTotalBiggerThan50() {
        long result = calculatorService.calculator(51);
        assertEquals(Math.round(88186 + 88186*0.1), result);
    }

    @Test
    @DisplayName("total<=100")
    void testCalculatorWithTotalNoBiggerThan100() {
        long result = calculatorService.calculator(100);
        assertEquals(175700 + 175700*0.1, result);
    }

    @Test
    @DisplayName("total>100")
    void testCalculatorWithTotalBiggerThan100() {
        long result = calculatorService.calculator(101);
        assertEquals(Math.round(177774 + 177774*0.1), result);
    }

    @Test
    @DisplayName("total<=200")
    void testCalculatorWithTotalNoBiggerThan200() {
        long result = calculatorService.calculator(200);
        assertEquals(383100+383100*0.1, result);
    }

    @Test
    @DisplayName("total>200")
    void testCalculatorWithTotalBiggerThan200() {
        long result = calculatorService.calculator(201);
        assertEquals(Math.round(385712+385712*0.1), result);
    }

    @Test
    @DisplayName("total<=300")
    void testCalculatorWithTotalNoBiggerThan300() {
        long result = calculatorService.calculator(300);
        assertEquals(644300 + 644300*0.1, result);
    }

    @Test
    @DisplayName("total>300")
    void testCalculatorWithTotalBiggerThan300() {
        long result = calculatorService.calculator(301);
        assertEquals(Math.round(647219+647219*0.1), result);
    }

    @Test
    @DisplayName("total<=400")
    void testCalculatorWithTotalNoBiggerThan400() {
        long result = calculatorService.calculator(400);
        assertEquals(936200+936200*0.1, result);
    }

    @Test
    @DisplayName("total>400")
    void testCalculatorWithTotalBiggerThan400() {
        long result = calculatorService.calculator(401);
        assertEquals(Math.round(939215+939215*0.1), result);
    }
}