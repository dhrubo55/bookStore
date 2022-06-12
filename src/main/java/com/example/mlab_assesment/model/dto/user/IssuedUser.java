package com.example.mlab_assesment.model.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IssuedUser {
    private long id;
    private String name;
}