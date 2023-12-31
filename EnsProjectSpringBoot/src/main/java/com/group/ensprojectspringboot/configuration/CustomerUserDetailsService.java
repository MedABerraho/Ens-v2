package com.group.ensprojectspringboot.configuration;

import com.group.ensprojectspringboot.model.User;
import com.group.ensprojectspringboot.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class CustomerUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userDAO;

	private User userDetails;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("Inside loadUserByUsername {}", username);
		userDetails = userDAO.findByUsername(username);
		if (!Objects.isNull(userDetails)) {
			return new org.springframework.security.core.userdetails.User(userDetails.getUsername(),
					userDetails.getPassword(), new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found!");
		}

	}

	public User getUserDetails() {
		return userDetails;
	}

}
