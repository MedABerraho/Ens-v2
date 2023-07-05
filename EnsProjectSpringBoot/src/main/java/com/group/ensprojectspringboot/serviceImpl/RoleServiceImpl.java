package com.group.ensprojectspringboot.serviceImpl;


import com.group.ensprojectspringboot.model.Role;
import com.group.ensprojectspringboot.repository.RoleRepository;
import com.group.ensprojectspringboot.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleDAO;
	@Override
	public Role createNewRole(Role role) {
		return roleDAO.save(role);
	}
}
