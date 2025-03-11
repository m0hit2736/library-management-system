package com.library.management.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.management.system.entities.BookEntity;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

}
