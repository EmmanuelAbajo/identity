package com.esc.identity.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.esc.identity.entity.Privilege;

public interface PrivilegeRepository extends CrudRepository<Privilege, Integer> {
	
	Optional<Privilege> findByName(String name);

}
