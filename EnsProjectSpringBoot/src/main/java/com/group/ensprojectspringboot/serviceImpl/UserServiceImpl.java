package com.group.ensprojectspringboot.serviceImpl;

import com.group.ensprojectspringboot.configuration.CustomerUserDetailsService;
import com.group.ensprojectspringboot.consts.EnsConsts;
import com.group.ensprojectspringboot.model.JwtRequest;
import com.group.ensprojectspringboot.model.Role;
import com.group.ensprojectspringboot.model.User;
import com.group.ensprojectspringboot.repository.RoleRepository;
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

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userDAO;

    @Autowired
    private RoleRepository roleDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Autowired
    CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtRequest jwtRequest;

    @Autowired
    JwtService jwtService;

    @Override
    public ResponseEntity<String> registerNewUser(Map<String, String> request) {
        log.info("Inside signup {}", request);
        try {
            if (validateSignUpMap(request)) {
                User user = userDAO.findByUsername(request.get("username"));
                if (Objects.isNull(user)) {
                    userDAO.save(getUserFromMap(request));
                    return EnsUtils.getResponseEntity("Successfully registered", HttpStatus.OK);
                } else {
                    return EnsUtils.getResponseEntity("Email already exist.", HttpStatus.BAD_REQUEST);
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
        Role role = roleDAO.findByRoleName("User");
        user.setUsername(request.get("username"));
        user.setPassword(getEncodedPassword(request.get("password")));
        user.setAddress(request.get("address"));
        user.setContactNumber(request.get("contactNumber"));
        user.setStatus("true"); // to activate a user ATM is true
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRole(roles);
        return user;

    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public void initRoles() {
        Role adminRole = new Role();
        adminRole.setRoleName("Admin");
        adminRole.setRoleDescription("Admin role");
        roleDAO.save(adminRole);
        Role userRole = new Role();
        userRole.setRoleName("User");
        userRole.setRoleDescription("Default role for newly created record");
        roleDAO.save(userRole);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> request) {
        log.info("Inside login ");
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.get("username"), request.get("password")));
            if (auth.isAuthenticated()) {
                if (customerUserDetailsService.getUserDetails().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<String>(
                            "{\"token\":\""
                                    + jwtService.createJwtToken(jwtRequest)
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
