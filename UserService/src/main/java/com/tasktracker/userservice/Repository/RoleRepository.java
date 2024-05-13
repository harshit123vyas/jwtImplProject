package com.tasktracker.userservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasktracker.userservice.Entity.Role;

@Repository
public interface RoleRepository  extends JpaRepository<Role, Integer>{
	
	Role findByRoleName(String role);

}
