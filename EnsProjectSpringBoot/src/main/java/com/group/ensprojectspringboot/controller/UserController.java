package com.group.ensprojectspringboot.controller;


import com.group.ensprojectspringboot.consts.EnsConsts;
import com.group.ensprojectspringboot.service.UserService;
import com.group.ensprojectspringboot.utils.EnsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void initRoles() {
        try{
            userService.initRoles();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping({"/registerNewUser"})
    public ResponseEntity<String> registerNewUser(@RequestBody Map<String, String> request) {
        try {
            return userService.registerNewUser(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EnsUtils.getResponseEntity(EnsConsts.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping({"/login"})
    public ResponseEntity<String> login(@RequestBody Map<String, String> request){
        try {
            return userService.login(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EnsUtils.getResponseEntity(EnsConsts.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping({"/forAdmin"})
    @PreAuthorize("hasRole('Admin')")
    public String forAdmin() {
        return "This URL is only accessible to admin";
    }

    @GetMapping({"/forUser"})
    @PreAuthorize("hasAnyRole('Admin','User')")
    public String forUser() {
        return "This URL is only accessible to user";
    }
}
