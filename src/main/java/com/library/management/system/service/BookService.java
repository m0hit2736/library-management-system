package com.library.management.system.service;

import java.util.List;

import com.library.management.system.dto.BookDTO;

public interface BookService {
	BookDTO createBook(BookDTO bookDTO);

	List<BookDTO> getAllBooks();

	BookDTO getBookById(Long id);

	void deleteBook(Long id);

	BookDTO updateBook(Long id, BookDTO bookDTO);
}
