package com.tasktracker.userservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tasktracker.userservice.Entity.Role;

public interface RoleRepository  extends JpaRepository<Role, Integer>{
	
	Role findByRole(String role);

}
