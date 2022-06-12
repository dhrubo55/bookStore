package com.example.mlab_assesment.model.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserUpdateRequest {
    @Min(value = 1, message = "validation.constraints.userId.NotNull.message")
    private long id;
    @NotBlank(message = "validation.constraints.user.fullName.NotNull.message")
    @JsonProperty("full_name")
    private String fullName;

    @NotBlank(message = "validation.constraints.user.email.empty.message")
    @Email(message = "validation.constraints.user.email.Invalid.message")
    private String email;
}

