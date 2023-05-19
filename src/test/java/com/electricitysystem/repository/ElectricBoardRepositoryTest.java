package com.electricitysystem.repository;

import com.electricitysystem.entity.ElectricBoardEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("ElectricBoardRepository Tests")
@Transactional
public class ElectricBoardRepositoryTest {

    @Autowired
    ElectricBoardRepository electricBoardRepository;

    @Test
    public void addNewElectricBoardWithNotNullAttributes(){

        Long list_size = electricBoardRepository.count();
        ElectricBoardEntity electricBoard = new ElectricBoardEntity();
        electricBoard.setMeterCode("metercode");
        electricBoard.setOldNumber(0);
        electricBoard.setNewNumber(100);
        electricBoard.setTotalNumber(100);
        electricBoard.setTimeReadMeter("time");
        electricBoard.setUsername("HD11300001");
        electricBoard.setTotalPayment(10000);
        electricBoard.setPeriod("period");
        electricBoardRepository.save(electricBoard);

        Assertions.assertThat(electricBoard.getId()).isNotNull();
        Assertions.assertThat(electricBoardRepository.findElectricBoardById(electricBoard.getId())).isNotNull();
        Assertions.assertThat(electricBoard.getTimeUpdate()).isNotNull();
        Assertions.assertThat(electricBoard.getMeterCode()).isEqualTo("metercode");
        Assertions.assertThat(electricBoard.getOldNumber()).isEqualTo(0);
        Assertions.assertThat(electricBoard.getNewNumber()).isEqualTo(100);
        Assertions.assertThat(electricBoard.getTotalNumber()).isEqualTo(100);
        Assertions.assertThat(electricBoard.getTimeReadMeter()).isEqualTo("time");
        Assertions.assertThat(electricBoard.getUsername()).isEqualTo("HD11300001");
        Assertions.assertThat(electricBoard.getTotalPayment()).isEqualTo(10000.00);
        Assertions.assertThat(electricBoard.getPeriod()).isEqualTo("period");
    }

    @Test
    public void testUpdateElectricBoard(){
        ElectricBoardEntity electricBoard = electricBoardRepository.findElectricBoardById(1);
        electricBoard.setMeterCode("changmetercode");
        electricBoardRepository.save(electricBoard);

        Assertions.assertThat(electricBoard.getMeterCode()).isEqualTo("changmetercode");
    }

    @Test
    public void testDeleteElectricBoard(){
        ElectricBoardEntity electricBoard = electricBoardRepository.findElectricBoardById(1);
        electricBoardRepository.delete(electricBoard);
        Assertions.assertThat(electricBoardRepository.findElectricBoardById(1)).isNull();
    }


    @Test
    void testFindAllByUsernameWithNotNullExpectedResult_ReturnCorrectListSize() {
        String username = "HD11300001";
        List<ElectricBoardEntity> list = electricBoardRepository.findAllByUsername(username);
        Assertions.assertThat(list.size()).isNotNull();
        Assertions.assertThat(list.size()).isEqualTo(1);
        for (ElectricBoardEntity e : list)
            Assertions.assertThat(e.getUsername()).isEqualTo(username);
    }

    @Test
    void testFindAllByUsernameWithNullExpectedResult_ReturnNull() {
        String username = "HD11310000";
        List<ElectricBoardEntity> list = electricBoardRepository.findAllByUsername(username);
        Assertions.assertThat(list.size()).isEqualTo(0);
    }

    @Test
    void testFindAllByUsernameWithIncorrectInput_ReturnNull() {
        String username = "Incorrect";
        List<ElectricBoardEntity> list = electricBoardRepository.findAllByUsername(username);
        Assertions.assertThat(list.size()).isEqualTo(0);
    }


    @Test
    void testFindElectricBoardByIdWithExistedId_ReturnCorrect() {
        int id = 1;
        ElectricBoardEntity electricBoard = electricBoardRepository.findElectricBoardById(id);
        Assertions.assertThat(electricBoard).isNotNull();
        Assertions.assertThat(electricBoard.getId()).isEqualTo(id);
    }

    @Test
    void testFindElectricBoardByIdWithNotExistedId_ReturnNull() {
        int id = 100;
        ElectricBoardEntity electricBoard = electricBoardRepository.findElectricBoardById(id);
        Assertions.assertThat(electricBoard).isNull();
    }
}