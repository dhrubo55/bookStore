package com.example.bookstore.controller;

import com.example.bookstore.model.ApiResponse;
import com.example.bookstore.model.dto.book.*;
import com.example.bookstore.model.dto.user.UserResponse;
import com.example.bookstore.service.BookService;
import com.example.bookstore.util.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookController extends BaseController {

    private final BookService bookService;
    //TODo: use appropiate verbs

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/books")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getBooks(){
        return ResponseEntity.ok(
                ResponseBuilder.buildOkResponse(
                        bookService.findAllBooks()));
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/books/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> getBookById(@PathVariable long id){
        return ResponseEntity.ok(
                ResponseBuilder.buildOkResponse(
                        bookService.findBookById(id)));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/books/name/search")
    public ResponseEntity<ApiResponse<List<BookResponse>>> searchBookByBookName(@RequestParam String bookName){
        BookSearchRequest dto = new BookSearchRequest();
        dto.setBookName(bookName);
        return ResponseEntity.ok(
                ResponseBuilder.buildOkResponse(
                        bookService.searchBook(dto)));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/books/author/search")
    public ResponseEntity<ApiResponse<List<BookResponse>>> searchBookByAuthorName(@RequestParam String authorName){
        BookSearchRequest dto = new BookSearchRequest();
        dto.setAuthorName(authorName);
        return ResponseEntity.ok(
                ResponseBuilder.buildOkResponse(
                        bookService.searchBook(dto)));
    }

    @PostMapping("/books")
    public ResponseEntity<ApiResponse<BookResponse>> create(@RequestBody @Valid BookCreateRequest dto,
                                                            BindingResult result){
        super.throwIfHasError(result);
        return ResponseEntity.ok(
                ResponseBuilder.buildOkResponse(
                        bookService.createBook(dto)));
    }

    @PutMapping("/books")
    public ResponseEntity<ApiResponse<BookResponse>> update(@RequestBody @Valid BookUpdateRequest dto,
                                                            BindingResult result){
        super.throwIfHasError(result);
        return ResponseEntity.ok(
                ResponseBuilder.buildOkResponse(
                        bookService.updateBook(dto)));
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> deleteBookById(@PathVariable long id){
        return ResponseEntity.ok(
                ResponseBuilder.buildOkResponse(
                        bookService.deleteBook(id)));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/books/issue")
    public ResponseEntity<ApiResponse<UserResponse>> issueBook(@RequestBody @Valid IssueBookRequest dto,
                                                               BindingResult result){
        super.throwIfHasError(result);
        return ResponseEntity.ok(
                ResponseBuilder.buildOkResponse(
                        bookService.issueBook(dto)));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/books/submit")
    public ResponseEntity<ApiResponse<UserResponse>> deleteUserById(@RequestBody @Valid BookSubmitRequest dto){
        return ResponseEntity.ok(
                ResponseBuilder
                        .buildOkResponse(
                                bookService.submitBooks(dto)));
    }

}
