package com.bookstore.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.domain.Book;

public interface BookRepository extends CrudRepository<Book, Long>{

	public List<Book> findAll();
	
	public Book findOne(Long id);

	public List<Book> findByCategory(String category);

	public List<Book> findByTitleContaining(String keyword);
}
