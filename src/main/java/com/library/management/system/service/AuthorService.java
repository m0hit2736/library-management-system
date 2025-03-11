package com.library.management.system.service;

import java.util.List;
import java.util.Optional;

import com.library.management.system.dto.AuthorDTO;
import com.library.management.system.entities.AuthorEntity;

public interface AuthorService {
	List<AuthorEntity> getAllAuthors();

	Optional<AuthorEntity> getAuthorById(Long id);

	AuthorEntity createAuthor(AuthorDTO authorDTO);

	AuthorEntity updateAuthor(Long id, AuthorDTO authorDTO);

	void deleteAuthor(Long id);
}
