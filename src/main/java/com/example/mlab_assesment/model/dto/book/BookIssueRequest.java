package com.example.mlab_assesment.model.dto.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Getter
@Setter
public class BookIssueRequest {
    @Min(value = 1, message = "validation.constraints.userId.NotNull.message")
    @JsonProperty("user_id")
    private long userId;
    @JsonProperty("book_ids")
    @Singular
    private Set<Long> bookIds;
}
