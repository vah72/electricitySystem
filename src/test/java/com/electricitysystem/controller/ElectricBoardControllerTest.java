package com.electricitysystem.controller;

import com.electricitysystem.entity.ElectricBoardEntity;
import com.electricitysystem.service.CustomerService;
import com.electricitysystem.service.ElectricBoardService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(ElectricBoardController.class)
public class ElectricBoardControllerTest {

    @InjectMocks
    private ElectricBoardController electricBoardController;

    @Mock
    private ElectricBoardService electricBoardService;

    @Test
    public void testCreateWithValidFile() throws IOException {
        List<ElectricBoardEntity> list = new ArrayList<>();
        ElectricBoardEntity board = new ElectricBoardEntity(1,"PAC001",1530,1686,
                "04-05-2023", "HD11300001", "04-2023");
        list.add(board);
        when(electricBoardService.create(Mockito.any(MultipartFile.class)))
                .thenReturn(list);

        String path = new File("electric.xlsx").getAbsolutePath();
        File file = new File(path);
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), MediaType.MULTIPART_FORM_DATA_VALUE, inputStream);

        ResponseEntity<?> response = electricBoardController.create(multipartFile);
        assertEquals(list, response.getBody());
    }

    @Test
    public void testCreateWithInvalidFile() throws Exception {
        byte[] fileContent = "Test file content".getBytes();
        MultipartFile file = new MockMultipartFile("file", "electric.xlsx", "text/plain", fileContent);
        doThrow(new IOException()).when(electricBoardService).create(file);
        assertThrows(IOException.class, () -> {
            ResponseEntity<?> response = electricBoardController.create(file);
        });
    }

    @Test
    public void testCreateWithNullFile() throws Exception{
        doThrow(new IllegalArgumentException()).when(electricBoardService).create(null);
        assertThrows(IllegalArgumentException.class, () -> {
            ResponseEntity<?> response = electricBoardController.create(null);
        });
    }

    @Test
    public void testUpdateWithValidInput_ReturnInformation() {
        int id = 1;
        int newNumber = 1686;
        ElectricBoardEntity board = new ElectricBoardEntity(1,"PAC001",1530,1686,
                "04-05-2023", "HD11300001", "04-2023");
        when(electricBoardService.update(1, 1686))
                .thenReturn(board);

        ResponseEntity<?> response = electricBoardController.update(id, newNumber);
        verify(electricBoardService, times(1)).update(id, newNumber);
        assertEquals(board, response.getBody());
    }

    @Test
    public void testUpdateWithMissingNewNumber_ReturnMessage(){
        int id = 1, newNumber = 0;
        when(electricBoardService.update(id, newNumber))
                .thenReturn(null);

        ResponseEntity<?> response = electricBoardController.update(id, 0);
        assertEquals("Vui lòng nhập số công tơ điện tháng này", response.getBody());
    }

    @Test
    public void testUpdateWithInvalidInput_OldNumberBiggerThanNewNumber_ReturnMessage(){
        int id = 1, newNumber = 1000;

        when(electricBoardService.update(id, newNumber))
                .thenReturn(null);
        ResponseEntity<?> response = electricBoardController.update(id, newNumber);
        verify(electricBoardService, times(1)).update(id, newNumber);
        assertEquals("Số công tơ điện mới phải lớn hơn số công tơ điện cũ", response.getBody());
    }

    @Test
    public void testGetOneByIdSuccess_ReturnElectricBoard() {
        ElectricBoardEntity board = new ElectricBoardEntity();
        board.setId(1);
        board.setMeterCode("PAC001");
        board.setOldNumber(1686);
        board.setNewNumber(1000);
        board.setTimeReadMeter("04-05-2023");
        board.setUsername("HD11300001");
        board.setPeriod("04-2023");
        when(electricBoardService.getOneById(board.getId()))
                .thenReturn(board);

        ResponseEntity<?> response = electricBoardController.getOneById(board.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(board, response.getBody());
    }

    @Test
    public void testGetOneByIdWithNotExistId_ReturnMessage() {
        ElectricBoardEntity board = new ElectricBoardEntity();
        board.setId(1);
        when(electricBoardService.getOneById(board.getId()))
                .thenReturn(null);

        ResponseEntity<?> response = electricBoardController.getOneById(100);
        assertEquals("Không tìm thấy bảng số điện", response.getBody());
    }

    @Test
    public void testGetAllByUsernameSuccessWithNotNullResult_ReturnListResult() {
        List<ElectricBoardEntity> list = new ArrayList<>();
        list.add(new ElectricBoardEntity(1, "PAC001",1530,1686,
                "04-05-2023", "HD11300001", "04-2023"));

        when(electricBoardService.getAllByCustomerUserName("HD11300001"))
                .thenReturn(list);

        ResponseEntity<?> response = electricBoardController.getAllByUsername("HD11300001");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
    }

    @Test
    public void testGetAllByUsernameSuccessWithNullResult_ReturnMessage() {
        List<ElectricBoardEntity> list = new ArrayList<>();
        list.add(new ElectricBoardEntity(1, "PAC001",1530,1686,
                "04-05-2023", "HD11300001", "04-2023"));

        when(electricBoardService.getAllByCustomerUserName("HD11300002"))
                .thenReturn(null);

        ResponseEntity<?> response = electricBoardController.getAllByUsername("HD11300002");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Khách hàng không có hóa đơn nào", response.getBody());
    }

    @Test
    public void testGetAllByUsernameWithNotExistUsername_ReturnMessage() {
        List<ElectricBoardEntity> list = new ArrayList<>();

        when(electricBoardService.getAllByCustomerUserName("HD11310000"))
                .thenReturn(null);
        ResponseEntity<?> response = electricBoardController.getAllByUsername("HD11310000");
        assertEquals("Không có khách hàng cần tìm", response.getBody());
    }
}