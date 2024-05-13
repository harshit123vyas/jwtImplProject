package com.tasktracker.userservice.ServiceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tasktracker.userservice.Entity.AuthUser;
import com.tasktracker.userservice.Entity.Role;
import com.tasktracker.userservice.Repository.AuthUserRepository;
import com.tasktracker.userservice.Repository.RoleRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthUserService {

	@Autowired
	private AuthUserRepository repository;

	@Autowired
	private RoleRepository roleRepository;

	public AuthUser saveUser(AuthUser user) {
		Optional<AuthUser> authuser = repository.findAuthUserByUserNameAndEmail(user.getUsername(), user.getEmail());
		log.info("autheruser : {}", authuser);
		if (!authuser.isPresent()) {
			AuthUser newAuthUser = new AuthUser();
			String encoded = new BCryptPasswordEncoder().encode(user.getPassword());
			newAuthUser.setPassword(encoded);
			newAuthUser.setEmail(user.getEmail());
			newAuthUser.setUserName(user.getUsername());
			Role role = user.getRole();
			Role existingRole = roleRepository.findByRoleName(role.getRoleName());
			if (existingRole == null) {
				role = roleRepository.save(role);
			} else {
				role = existingRole; // Use existing role if found
			}

			newAuthUser.setRole(role);
			return repository.save(newAuthUser);
		}
		return authuser.get();
	}

}
