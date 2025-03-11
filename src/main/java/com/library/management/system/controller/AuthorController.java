package com.library.management.system.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.management.system.dto.AuthorDTO;
import com.library.management.system.entities.AuthorEntity;
import com.library.management.system.service.AuthorService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

	@Autowired
	private AuthorService authorService;

	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public ResponseEntity<List<AuthorEntity>> getAllAuthors() {
		return ResponseEntity.ok(authorService.getAllAuthors());
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{id}")
	public ResponseEntity<Optional<AuthorEntity>> getAuthorById(@PathVariable Long id) {
		return ResponseEntity.ok(authorService.getAuthorById(id));
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
	@PostMapping
	public ResponseEntity<AuthorEntity> createAuthor(@RequestBody @Valid AuthorDTO authorDTO) {
		return ResponseEntity.ok(authorService.createAuthor(authorDTO));
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
	@PutMapping("/{id}")
	public ResponseEntity<AuthorEntity> updateAuthor(@PathVariable Long id, @RequestBody @Valid AuthorDTO authorDTO) {
		return ResponseEntity.ok(authorService.updateAuthor(id, authorDTO));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
		authorService.deleteAuthor(id);
		return ResponseEntity.ok("Author deleted successfully");
	}
}
