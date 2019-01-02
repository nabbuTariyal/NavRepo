package com.adminportal.repository;

import org.springframework.data.repository.CrudRepository;

import com.adminportal.domain.Book;

public interface BookRepository extends CrudRepository<Book, Long>{

	Book save(Book book);
	
	
	Book findOne(Long id);
}
