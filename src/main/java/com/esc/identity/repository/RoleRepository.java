package com.esc.identity.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.esc.identity.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
	
	Optional<Role> findByName(String name);

}
