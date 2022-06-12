package com.example.bookstore.model.dto.book;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.BookMeta;
import com.example.bookstore.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.util.List;

@Getter
@Setter

@Builder
public class IssueBookValidation {
    private UserEntity userEntity;
    @Singular
    private List<Book> bookEntities;
    @Singular
    private List<BookMeta> metaEntities;
}
