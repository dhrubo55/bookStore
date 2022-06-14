package com.example.bookstore.service.impl;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.BookMeta;
import com.example.bookstore.model.dto.book.BookCreateRequest;
import com.example.bookstore.model.dto.book.BookSearchRequest;
import com.example.bookstore.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @Test
    @DisplayName("Test for getting all books")
    void listAllBooks() {
        assertNotNull(bookService.findAllBooks());
    }

    @Test
    @DisplayName("Test for find a book by id")
    void findBookById() {
        long id = 1;
        assertNotNull(bookService.findBookById(id));
    }

    @Test
    @DisplayName("Test for searching a book by book name")
    void searchBookByBookName() {
        String bookName = "Foundation";
        BookSearchRequest bookSearchRequest = new BookSearchRequest();
        bookSearchRequest.setBookName(bookName);
        assertNotNull(bookService.searchBook(bookSearchRequest));
    }

    @Test
    @DisplayName("Test for searching a book by author name")
    void searchBookAuthorName() {
        String authorName = "Issac Osimov";
        BookSearchRequest bookSearchRequest = new BookSearchRequest();
        bookSearchRequest.setAuthorName(authorName);
        assertNotNull(bookService.searchBook(bookSearchRequest));
    }

    @Test
    @DisplayName("Test for creating a book")
    void createBook() {
        BookCreateRequest bookSearchRequest = new BookCreateRequest("War and Peace","Leo Tolstoy","Novel",10,"23-02-2022");
        assertNotNull(bookService.createBook(bookSearchRequest));
    }
}