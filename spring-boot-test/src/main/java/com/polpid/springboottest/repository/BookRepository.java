package com.polpid.springboottest.repository;

import com.polpid.springboottest.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
