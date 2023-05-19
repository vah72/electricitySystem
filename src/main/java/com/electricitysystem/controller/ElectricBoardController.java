package com.electricitysystem.controller;

import com.electricitysystem.entity.ElectricBoardEntity;
import com.electricitysystem.repository.ElectricBoardRepository;
import com.electricitysystem.service.ElectricBoardService;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/board")
public class ElectricBoardController {

    @Autowired
    private ElectricBoardService electricBoardService;


    @PostMapping(value = "/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> create(
            @RequestParam("file") MultipartFile files) throws IOException {
        return ResponseEntity.ok(electricBoardService.create(files));
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") int id,
            @RequestParam int newNumber
    ) {
        return ResponseEntity.ok(electricBoardService.update(id, newNumber));
    }

    @GetMapping("/getOneById")
    public ResponseEntity<?> getOneById(
            @RequestParam int electricBoardId
    ) {
        return ResponseEntity.ok(electricBoardService.getOneById(electricBoardId));
    }

    @GetMapping("/getAllByUsername")
    public ResponseEntity<?> getAllByUsername(
            @RequestParam String username
    ) {
        return ResponseEntity.ok(electricBoardService.getAllByCustomerUserName(username));
    }

    @GetMapping("/getAllBoard")
    public ResponseEntity<?> getAllBoard(){
        return ResponseEntity.ok(electricBoardService.getAllByPeriod("04-2023"));
    }
}
