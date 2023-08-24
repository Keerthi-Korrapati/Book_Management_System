package com.capg.bms.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.capg.bms.entity.BookData;

@Repository
public interface BookRepository extends MongoRepository<BookData,Integer>{
	
	List<BookData> findByFeaturedTrue();

	List<BookData> findByAuthor(String author);

	List<BookData> findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(String title, String author);

	List<BookData> findByTitleContainingIgnoreCase(String title);

	List<BookData> findByAuthorContainingIgnoreCase(String author);

}
