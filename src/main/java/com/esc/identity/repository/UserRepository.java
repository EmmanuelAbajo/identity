package com.esc.identity.repository;

import org.springframework.data.repository.CrudRepository;

import com.esc.identity.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}
