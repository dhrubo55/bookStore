package com.example.bookstore.service;

import com.example.bookstore.model.dto.user.UserCreateRequest;
import com.example.bookstore.model.dto.user.UserResponse;
import com.example.bookstore.model.dto.user.UserUpdateRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    @Transactional(readOnly = true)
    List<UserResponse> findAllUser();
    @Transactional(readOnly = true)
    UserResponse findUserById(long id);
    @Transactional(readOnly = true)
    UserResponse findUserByUsername(String userName);
    @Transactional
    UserResponse createUser(UserCreateRequest dto);
    @Transactional
    UserResponse updateUser(UserUpdateRequest dto);
    @Transactional
    UserResponse deleteUser(long id);
}
