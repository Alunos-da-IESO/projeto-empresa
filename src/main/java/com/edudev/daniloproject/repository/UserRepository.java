package com.edudev.daniloproject.repository;

import org.springframework.data.repository.CrudRepository;

import com.edudev.daniloproject.entity.Cellphone;
import com.edudev.daniloproject.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
	Iterable<User> findByCellphone(Cellphone cellphone);
}
