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
    UserRepository userDAO;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    JwtFilter jwtFiler;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userDAO.findByUsername(requestMap.get("username"));
                if (Objects.isNull(user)) {
                    userDAO.save(getUserFromMap(requestMap));
                    return EnsUtils.getResponseEntity("Successfully registered", HttpStatus.OK);
                } else {
                    return EnsUtils.getResponseEntity("User already exist.", HttpStatus.BAD_REQUEST);
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

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setUsername(requestMap.get("username"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setAddress(requestMap.get("address"));
        user.setPassword(getEncodedPassword(requestMap.get("password")));
        user.setStatus("false");
        return user;

    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login ");
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("username"), requestMap.get("password")));
            if (auth.isAuthenticated()) {
                if (customerUserDetailsService.getUserDetails().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<String>(
                            "{\"token\":\""
                                    + jwtUtil.generateToken(customerUserDetailsService.getUserDetails().getUsername())
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

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }
    @Override
    public ResponseEntity<String> checkToken() {
        return EnsUtils.getResponseEntity("true", HttpStatus.OK);
    }
}
