package com.group.ensprojectspringboot.serviceImpl;


import com.group.ensprojectspringboot.configuration.CustomerUserDetailsService;
import com.group.ensprojectspringboot.configuration.JwtFilter;
import com.group.ensprojectspringboot.configuration.JwtUtil;
import com.group.ensprojectspringboot.consts.EnsConsts;
import com.group.ensprojectspringboot.model.User;
import com.group.ensprojectspringboot.repository.UserRepository;
import com.group.ensprojectspringboot.service.UserService;
import com.group.ensprojectspringboot.utils.EnsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<String> signUp(Map<String, String> request) {
        log.info("Inside signup {}", request);
        try {
            if (validateSignUpMap(request)) {
                User user = userRepository.findByUsername(request.get("username"));
                if (Objects.isNull(user)) {
                    userRepository.save(getUserFromMap(request));
                    return EnsUtils.getResponseEntity("Successfully registered", HttpStatus.OK);
                } else {
                    return EnsUtils.getResponseEntity("Username already exist.", HttpStatus.BAD_REQUEST);
                }

            } else {
                return EnsUtils.getResponseEntity(EnsConsts.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return EnsUtils.getResponseEntity(EnsConsts.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("username") && requestMap.containsKey("password")) {
            return true;
        } else {
            return false;
        }
    }

    private User getUserFromMap(Map<String, String> request) {
        User user = new User();
        user.setUsername(request.get("username"));
        user.setPassword(getEncodedPassword(request.get("password")));
        user.setAddress(request.get("address"));
        user.setPhoneNumber(request.get("phoneNumber"));
        user.setRole("user");
        user.setStatus(true);
        return user;

    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public ResponseEntity<String> login(Map<String, String> request) {
        log.info("Inside login ");
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.get("username"), request.get("password")));
            if (auth.isAuthenticated()) {
                if (customerUserDetailsService.getUserDetails().isStatus()) {
                    return new ResponseEntity<String>(
                            "{\"token\":\""
                                    + jwtUtil.generateToken(customerUserDetailsService.getUserDetails().getUsername(),
                                    customerUserDetailsService.getUserDetails().getRole())
                                    + "\"}",
                            HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("{\"message\":\"" + "Wait for admin approval." + "\"}",
                            HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
        return new ResponseEntity<String>("{\"message\":\"" + "Bad Credentials" + "\"}", HttpStatus.BAD_REQUEST);
    }
}
