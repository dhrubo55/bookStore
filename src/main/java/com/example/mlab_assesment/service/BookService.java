package com.example.mlab_assesment.service;

import com.example.mlab_assesment.model.dto.book.*;
import com.example.mlab_assesment.model.dto.user.UserResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookService {

    List<BookResponse> findAllBooks();
    BookResponse findBookById(long id);
    List<BookResponse> searchBook(BookSearchRequest dto);

    @Transactional(rollbackFor = Exception.class)
    BookResponse createBook(BookCreateRequest dto);
    @Transactional(rollbackFor = Exception.class)
    BookResponse updateBook(BookUpdateRequest dto);
    @Transactional(rollbackFor = Exception.class)
    BookResponse deleteBook(long id);

    @Transactional(rollbackFor = Exception.class)
    UserResponse issueBook(IssueBookRequest dto);
    @Transactional(rollbackFor = Exception.class)
    UserResponse submitBooks(BookSubmitRequest dto);
    @Transactional(rollbackFor = Exception.class)
    UserResponse submitAllBooks(long userId);
}
