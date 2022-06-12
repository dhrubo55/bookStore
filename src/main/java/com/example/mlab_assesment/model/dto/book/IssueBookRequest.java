package com.example.mlab_assesment.model.dto.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import javax.validation.constraints.Min;
import java.util.Set;

@Getter
@Setter
public class IssueBookRequest {
    @Min(value = 1, message = "validation.constraints.userId.NotNull.message")
    @JsonProperty("user_id")
    private long userId;
    @JsonProperty("book_ids")
    @Singular
    private Set<Long> bookIds;
}