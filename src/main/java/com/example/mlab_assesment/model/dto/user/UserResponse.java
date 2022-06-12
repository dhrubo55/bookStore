package com.example.mlab_assesment.model.dto.user;

import com.example.mlab_assesment.model.dto.book.IssuedBook;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserResponse {
    private long id;
    private String name;
    @JsonProperty("user_name")
    private String userName;
    private String email;
    @JsonProperty("issued_books")
    private List<IssuedBook> issuedBooks;

}