package com.example.bookstore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class ApiResponse<T> {
    @JsonProperty("response_code")
    private int responseCode;
    @JsonProperty("response_message")
    private String responseMessage;
    private T data;
}