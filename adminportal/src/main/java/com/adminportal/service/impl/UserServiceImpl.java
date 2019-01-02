package com.adminportal.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminportal.domain.User;
import com.adminportal.domain.security.UserRole;
import com.adminportal.repository.RoleRepository;
import com.adminportal.repository.UserRepository;
import com.adminportal.service.UserService;


@Service
public class UserServiceImpl implements UserService{

	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	
	

	@Override
	public User createUser(User user, Set<UserRole> userRoles) throws Exception{
		
		User localUser = userRepository.findByUsername(user.getUsername());
		if(localUser!=null){
			System.out.println("user already exits, nothing will be done.");
			//throw new Exception ("user already exits, nothing will be done.");
			
		}else{
			for(UserRole ur: userRoles){
				roleRepository.save(ur.getRole());
			}
			user.getUserRoles().addAll(userRoles);
			
			localUser = userRepository.save(user);
		}
		
		return localUser;
	}
	
	
	@Override
	public User save(User user) {
		return userRepository.save(user);
	}
}
