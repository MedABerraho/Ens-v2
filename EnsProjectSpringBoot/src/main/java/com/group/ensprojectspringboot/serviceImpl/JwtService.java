package com.group.ensprojectspringboot.serviceImpl;


import com.group.ensprojectspringboot.model.JwtRequest;
import com.group.ensprojectspringboot.model.JwtResponse;
import com.group.ensprojectspringboot.model.User;
import com.group.ensprojectspringboot.repository.UserRepository;
import com.group.ensprojectspringboot.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

	
	@Autowired
	private UserRepository userDAO;

	@Autowired
	private JwtUtil jwtUtil;

	
	@Autowired
	private AuthenticationManager authenticationManager;

	public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
		String userName = jwtRequest.getUserName();
		String userPassword = jwtRequest.getUserPassword();
		authenticate(userName, userPassword);
		
		final UserDetails userDetails = loadUserByUsername(userName);
		
		String newGeneratedToken = jwtUtil.generateToken(userDetails);
		
		User user = userDAO.findByUsername(userName);
		
		return new JwtResponse(user, newGeneratedToken);

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDAO.findByUsername(username);

		if (user != null) {
			return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
					getAuthority(user));
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}

	private Set getAuthority(User user) {
		Set authoritites = new HashSet();
		user.getRole().forEach(role -> {
			authoritites.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
		});

		return authoritites;
	}

	private void authenticate(String userName, String userPassword) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));

		} catch (DisabledException e) {
			throw new Exception("User is disabled");
		} catch (BadCredentialsException e) {
			throw new Exception("Bad credentials from user");
		}
	}

}
