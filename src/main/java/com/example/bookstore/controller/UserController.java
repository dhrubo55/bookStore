package com.example.bookstore.controller;

import com.example.bookstore.model.ApiResponse;
import com.example.bookstore.model.dto.user.UserCreateRequest;
import com.example.bookstore.model.dto.user.UserResponse;
import com.example.bookstore.model.dto.user.UserUpdateRequest;
import com.example.bookstore.service.UserService;
import com.example.bookstore.util.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(
                ResponseBuilder
                        .buildOkResponse(userService.findAllUser())
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable long id) {
        return ResponseEntity.ok(
                ResponseBuilder
                        .buildOkResponse(userService.findUserById(id))
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserResponse>> createNewUser(@RequestBody @Valid UserCreateRequest dto, BindingResult result) {
        super.throwIfHasError(result);

        return ResponseEntity.ok(
                ResponseBuilder
                        .buildOkResponse(userService.createUser(dto))
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/users")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@RequestBody @Valid UserUpdateRequest dto, BindingResult result) {
        super.throwIfHasError(result);

        return ResponseEntity.ok(
                ResponseBuilder
                        .buildOkResponse(userService.updateUser(dto))
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> deleteUserById(@PathVariable long id){
        return ResponseEntity.ok(
                ResponseBuilder
                        .buildOkResponse(
                                userService.deleteUser(id)));
    }
}
