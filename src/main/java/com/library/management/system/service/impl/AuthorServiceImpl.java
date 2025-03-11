package com.library.management.system.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.library.management.system.dto.AuthorDTO;
import com.library.management.system.entities.AuthorEntity;
import com.library.management.system.repository.AuthorRepository;
import com.library.management.system.service.AuthorService;

@Service
public class AuthorServiceImpl implements AuthorService {

	@Autowired
	private AuthorRepository authorRepository;

	@Override
	public List<AuthorEntity> getAllAuthors() {
		return authorRepository.findAll();
	}

	@Override
	public Optional<AuthorEntity> getAuthorById(Long id) {
		return authorRepository.findById(id);
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
	public AuthorEntity createAuthor(AuthorDTO author) {
		AuthorEntity authore = new AuthorEntity(author);
		return authorRepository.save(authore);
	}

	@Override
	@PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
	public AuthorEntity updateAuthor(Long id, AuthorDTO authorDetails) {
		return authorRepository.findById(id).map(author -> {
			author.setFirstName(authorDetails.getFirstName());
			author.setLastName(authorDetails.getLastName());
			author.setEmail(authorDetails.getEmail());
			author.setBirthDate(authorDetails.getBirthDate());
			return authorRepository.save(author);
		}).orElseThrow(() -> new RuntimeException("Author not found"));
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteAuthor(Long id) {
		authorRepository.deleteById(id);
	}
}
