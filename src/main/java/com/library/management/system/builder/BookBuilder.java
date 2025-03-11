package com.library.management.system.builder;

import org.springframework.stereotype.Component;

import com.library.management.system.dto.BookDTO;
import com.library.management.system.entities.BookEntity;

@Component
public class BookBuilder {

	public BookEntity buildBookEntity(BookDTO bookDto) {
		BookEntity book = new BookEntity();
		book.setTitle(bookDto.getTitle());
		book.setAuthor(bookDto.getAuthor());
		book.setIsbn(bookDto.getIsbn());
		book.setPrice(bookDto.getPrice());
		book.setCreatedBy(bookDto.getCreatedBy());
		book.setLastModifiedBy(bookDto.getLastModifiedBy());
		return book;
	}

	public BookDTO buildResponse(String string) {
		return BookDTO.builder().message(string).build();
	}
}
