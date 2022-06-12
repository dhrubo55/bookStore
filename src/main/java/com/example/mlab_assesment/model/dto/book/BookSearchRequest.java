package com.example.mlab_assesment.model.dto.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Min;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookSearchRequest {
    @JsonProperty("book_name")
    private String bookName;
    @JsonProperty("author_name")
    private String authorName;
}