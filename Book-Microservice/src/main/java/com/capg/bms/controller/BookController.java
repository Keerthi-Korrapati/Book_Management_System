package com.capg.bms.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capg.bms.entity.BookData;
import com.capg.bms.repository.BookRepository;
import com.capg.bms.service.BookService;
import com.capg.bms.service.SequenceGeneratorService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/Book")
public class BookController {
	
	@Autowired
	private BookService bs;
	
	@Autowired
	private BookRepository br;

	@Autowired
	private SequenceGeneratorService service;
	
	@PostMapping("/addBook")
	public ResponseEntity<String> addBooks(@Valid @RequestBody BookData bd) {
		bd.setId(service.getSequenceNumber(bd.SEQUENCE_NAME));
		return ResponseEntity.ok().body(bs.addBook(bd));
	}
	
	@GetMapping("/get-all-Books")
	public List<BookData> getAllBooks(){
		return bs.getAllBookDetails();
	}
	
	@GetMapping("/getByAuthor")
	public List<BookData> getByAuthor(@PathVariable String author){
		return bs.findByAuthor(author);	
	}
	
	@GetMapping("/search")
    public List<BookData> searchBooks(@RequestParam(required = false) String title, @RequestParam(required = false) String author) {
        
        if (title != null && author != null) {
            return br.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(title, author);
        } else if (title != null) {
            return br.findByTitleContainingIgnoreCase(title);
        } else if (author != null) {
            return br.findByAuthorContainingIgnoreCase(author);
        } else {
            return br.findAll();
        }
    }
	
	@GetMapping("/featured")
	public List<BookData> getFeaturedBooks() {
        return bs.getFeaturedBooks();
    }
	
	@GetMapping("/getbyId/{id}")
	public Optional<BookData> getbyid(@PathVariable("id") int id) {
		return bs.getBookById(id);
	}
	
	@PutMapping("/edit/{id}")
	public BookData editbook(@PathVariable("id") int id,@RequestBody BookData bd) {
		return bs.editBooking(id,bd);
	}
	
	@PutMapping("/update/{id}")
    public ResponseEntity<BookData> updatess(@PathVariable int id,@RequestBody BookData bd){
        return ResponseEntity.ok().body(this.bs.update(id,bd));

    }
	
	@PutMapping("/{id}")
    public void markFeatured(@PathVariable("id") int id, @RequestBody boolean isFeatured) {
        BookData book = br.findById(id).orElse(null);
        if (book != null) {
            book.setFeatured(isFeatured);
            br.save(book);
        }
    }
	
	@DeleteMapping("/deleteBook/{id}")
	public ResponseEntity<String> deleteById(@PathVariable("id") int id) {
        String response = bs.deleteId(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	@DeleteMapping("/deleteall")
	public String deleteall() {
		return bs.deleteAll();
	}

}
