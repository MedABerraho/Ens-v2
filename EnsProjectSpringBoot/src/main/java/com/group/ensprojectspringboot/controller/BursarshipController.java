package com.group.ensprojectspringboot.controller;

import com.group.ensprojectspringboot.consts.EnsConsts;
import com.group.ensprojectspringboot.model.Bursarship;
import com.group.ensprojectspringboot.model.Source;
import com.group.ensprojectspringboot.service.BursarshipService;
import com.group.ensprojectspringboot.utils.EnsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/bursary")
@CrossOrigin
public class BursarshipController {

    @Autowired
    BursarshipService bursarshipService;


    @PostMapping("/add")
    public ResponseEntity<String> createBursary(@RequestBody Map<String, String> request) {
        try {
            return bursarshipService.createBursary(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EnsUtils.getResponseEntity(EnsConsts.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping
    public ResponseEntity<List<Bursarship>> getBursary() {
        try {
            return bursarshipService.getBursary();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<List<Bursarship>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteBursary(@PathVariable Long id) {
        try {
            return bursarshipService.deleteBursary(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EnsUtils.getResponseEntity(EnsConsts.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path = "/update")
    public ResponseEntity<String> updateBursary(@RequestBody Map<String, String> request) {
        try {
            return bursarshipService.updateBursary(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EnsUtils.getResponseEntity(EnsConsts.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
