package com.polpid.springboottest.service;


import com.polpid.springboottest.domain.Book;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {
    List<Book> getBookList();
}
