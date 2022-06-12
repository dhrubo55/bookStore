package com.example.mlab_assesment.model.dto.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IssuedBook {
    private long id;
    private String name;

    @JsonProperty("author_name")
    private String authorName;

    private String description;

    @JsonProperty("no_of_copy")
    private int noOfCopy;

    @JsonProperty("release_date")
    private String releaseDate;
}
