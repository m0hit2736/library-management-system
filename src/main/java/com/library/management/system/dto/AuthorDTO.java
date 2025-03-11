package com.library.management.system.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.library.management.system.entities.BookEntity;

import jakarta.validation.constraints.Email;
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
public class AuthorDTO {

	@NotBlank(message = "firstName name is required")
	private String firstName;

	@NotBlank(message = "firstName name is required")
	private String lastName;

	@Email
	private String email;

	private LocalDate birthDate;

	private List<BookEntity> books;
}
