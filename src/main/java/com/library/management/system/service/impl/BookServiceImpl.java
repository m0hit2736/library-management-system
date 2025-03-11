package com.library.management.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.management.system.builder.BookBuilder;
import com.library.management.system.dto.BookDTO;
import com.library.management.system.entities.BookEntity;
import com.library.management.system.exception.RecordNotFoundException;
import com.library.management.system.repository.AuthorRepository;
import com.library.management.system.repository.BookRepository;
import com.library.management.system.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	AuthorRepository authorRepository;

	@Autowired
	BookBuilder bookBuilder;

	private static final String BOOK_NOT_FOUND = "Book not found";

	@Override
	public BookDTO createBook(BookDTO bookDTO) {
		authorRepository.findByEmail(bookDTO.getAuthor().getEmail()).orElseGet(() -> {
			// Save new author if not found
			return authorRepository.save(bookDTO.getAuthor());
		});

		BookEntity book = bookBuilder.buildBookEntity(bookDTO);
		bookRepository.save(book);
		return bookBuilder.buildResponse("book created successfully");
	}

	@Override
	public List<BookDTO> getAllBooks() {
		return bookRepository.findAll().stream().map(BookDTO::new).toList();
	}

	@Override
	public BookDTO getBookById(Long id) {
		BookEntity book = bookRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(BOOK_NOT_FOUND));
		return new BookDTO(book);
	}

	@Override
	public void deleteBook(Long id) {
		bookRepository.deleteById(id);
	}

	@Override
	public BookDTO updateBook(Long id, BookDTO bookDTO) {
		BookEntity book = bookRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(BOOK_NOT_FOUND));

		book.setTitle(bookDTO.getTitle());
		book.setAuthor(bookDTO.getAuthor());

		book.setIsbn(bookDTO.getIsbn());

		bookRepository.save(book);
		return bookBuilder.buildResponse("Book updated sucessfully");
	}
}
