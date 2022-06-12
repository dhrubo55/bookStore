package com.example.mlab_assesment.controller;

import com.example.mlab_assesment.model.ApiResponse;
import com.example.mlab_assesment.model.dto.book.*;
import com.example.mlab_assesment.model.dto.user.UserResponse;
import com.example.mlab_assesment.service.BookService;
import com.example.mlab_assesment.util.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookController extends BaseController {

    private final BookService bookService;

    @GetMapping("/book/get-all")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getBooks(){
        return ResponseEntity.ok(
                ResponseBuilder.buildOkResponse(
                        bookService.findAllBooks()));
    }

    @GetMapping("/book/get-by-id/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> getBookById(@PathVariable long id){
        return ResponseEntity.ok(
                ResponseBuilder.buildOkResponse(
                        bookService.findBookById(id)));
    }

    @GetMapping("/book/search-by-book-name")
    public ResponseEntity<ApiResponse<List<BookResponse>>> searchBookByBookName(@RequestParam String bookName){
        BookSearchRequest dto = new BookSearchRequest();
        dto.setBookName(bookName);
        return ResponseEntity.ok(
                ResponseBuilder.buildOkResponse(
                        bookService.searchBook(dto)));
    }

    @GetMapping("/book/search-by-author-name")
    public ResponseEntity<ApiResponse<List<BookResponse>>> searchBookByAuthorName(@RequestParam String authorName){
        BookSearchRequest dto = new BookSearchRequest();
        dto.setAuthorName(authorName);
        return ResponseEntity.ok(
                ResponseBuilder.buildOkResponse(
                        bookService.searchBook(dto)));
    }

    @PostMapping("/book/create")
    public ResponseEntity<ApiResponse<BookResponse>> create(@RequestBody @Valid BookCreateRequest dto,
                                                            BindingResult result){
        super.throwIfHasError(result);
        return ResponseEntity.ok(
                ResponseBuilder.buildOkResponse(
                        bookService.createBook(dto)));
    }

    @PostMapping("/book/update")
    public ResponseEntity<ApiResponse<BookResponse>> update(@RequestBody @Valid BookUpdateRequest dto,
                                                            BindingResult result){
        super.throwIfHasError(result);
        return ResponseEntity.ok(
                ResponseBuilder.buildOkResponse(
                        bookService.updateBook(dto)));
    }

    @DeleteMapping("/book/delete-by-id/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> deleteBookById(@PathVariable long id){
        return ResponseEntity.ok(
                ResponseBuilder.buildOkResponse(
                        bookService.deleteBook(id)));
    }

    @PostMapping("/book/issue")
    public ResponseEntity<ApiResponse<UserResponse>> issueBook(@RequestBody @Valid IssueBookRequest dto,
                                                               BindingResult result){
        super.throwIfHasError(result);
        return ResponseEntity.ok(
                ResponseBuilder.buildOkResponse(
                        bookService.issueBook(dto)));
    }

    @PostMapping("/book/submit")
    public ResponseEntity<ApiResponse<UserResponse>> deleteUserById(@RequestBody @Valid BookSubmitRequest dto){
        return ResponseEntity.ok(
                ResponseBuilder
                        .buildOkResponse(
                                bookService.submitBooks(dto)));
    }

}
