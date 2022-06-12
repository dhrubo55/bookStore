package com.example.bookstore.model.dto.book;

import com.example.bookstore.model.dto.user.IssuedUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class BookResponse {
    private long id;
    private String name;

    @JsonProperty("author_name")
    private String authorName;

    private String description;

    @JsonProperty("no_of_copy")
    private int noOfCopy;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("issued_users")
    private List<IssuedUser> issuedUsers;
}