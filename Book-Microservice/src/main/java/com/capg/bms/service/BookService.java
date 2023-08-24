package com.capg.bms.service;

import java.util.List; 
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.capg.bms.entity.BookData;
import com.capg.bms.exception.ResourceNotFoundException;
import com.capg.bms.repository.BookRepository;

@Service
public class BookService {
	
	@Autowired
	private BookRepository br;
	
	public String addBook(BookData bd) {
		br.save(bd);
		return "Book is added";
	}
	
	public List<BookData> getAllBookDetails(){
		return br.findAll();
	}
	
	public List<BookData> getFeaturedBooks(){
		return br.findByFeaturedTrue();
	}
	
	public Optional<BookData> getBookById(int id) {
		try {
			return br.findById(id);
		} catch (Exception e) {
			throw new ResourceNotFoundException("NO SUCH BookId");
		}
	}

	public BookData editBooking(int id, BookData bd) {
		return br.save(bd);
	}
	
	
    public BookData update(int id, BookData bd) {
        Optional<BookData> book=this.br.findById(id);
        if(book.isPresent()) {
        	BookData u=book.get();
            u.setTitle(bd.getTitle());
            u.setPrice(bd.getPrice());
            u.setAuthor(bd.getAuthor());
            u.setImageUrl(bd.getImageUrl());
            u.setFeatured(bd.isFeatured());
            br.save(u);
            return u;
        }
            else {    
                return null;
            }
        }
	
	public String deleteId(int id) {
		br.deleteById(id);
		return "deleted";
	}
	
	public String deleteAll() {
		br.deleteAll();
		return "All Books deleted";
	}

	public List<BookData> findByAuthor(String author) {
		// TODO Auto-generated method stub
		return br.findByAuthor(author);
	}


}
