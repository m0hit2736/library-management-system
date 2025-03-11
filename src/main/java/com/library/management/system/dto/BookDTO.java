package com.library.management.system.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.library.management.system.entities.AuthorEntity;
import com.library.management.system.entities.BookEntity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class BookDTO {

	@NotBlank(message = "Title is required")
	private String title;

	@NotBlank(message = "Author is required")
	private AuthorEntity author;

	@NotBlank(message = "ISBN is required")
	private String isbn;

	private LocalDate publishedDate;

	private BigDecimal price;

	private String createdBy;

	private String lastModifiedBy;

	private String message;

	public BookDTO(BookEntity book) {
		this.title = book.getTitle();
		this.author = book.getAuthor();
		this.isbn = book.getIsbn();
		this.price = book.getPrice();
	}
}
