package com.edudev.daniloproject.repository;

import org.springframework.data.repository.CrudRepository;

import com.edudev.daniloproject.entity.Cellphone;
import com.edudev.daniloproject.entity.Factory;

public interface CellphoneRepository extends CrudRepository<Cellphone, Long> {
	Iterable<Cellphone> findByFactory(Factory factory);
}
