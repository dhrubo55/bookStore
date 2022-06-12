package com.example.mlab_assesment.model.dto.book;

import com.example.mlab_assesment.entity.Book;
import com.example.mlab_assesment.entity.BookMeta;
import com.example.mlab_assesment.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.util.List;

@Getter
@Setter

@Builder
public class IssueBookValidation {
    private User userEntity;
    @Singular
    private List<Book> bookEntities;
    @Singular
    private List<BookMeta> metaEntities;
}
