package com.example.mlab_assesment.controller;

import com.example.mlab_assesment.model.ApiResponse;
import com.example.mlab_assesment.model.dto.user.UserCreateRequest;
import com.example.mlab_assesment.model.dto.user.UserResponse;
import com.example.mlab_assesment.model.dto.user.UserUpdateRequest;
import com.example.mlab_assesment.service.UserService;
import com.example.mlab_assesment.util.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;

    @GetMapping("/user/get-all")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(
                ResponseBuilder
                        .buildOkResponse(userService.findAllUser())
        );
    }

    @GetMapping("/user/get-by-id/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable long id) {
        return ResponseEntity.ok(
                ResponseBuilder
                        .buildOkResponse(userService.findUserById(id))
        );
    }

    @PostMapping("/user/create")
    public ResponseEntity<ApiResponse<UserResponse>> createNewUser(@RequestBody @Valid UserCreateRequest dto, BindingResult result) {
        super.throwIfHasError(result);

        return ResponseEntity.ok(
                ResponseBuilder
                        .buildOkResponse(userService.createUser(dto))
        );
    }

    @PostMapping("/user/update")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@RequestBody @Valid UserUpdateRequest dto, BindingResult result) {
        super.throwIfHasError(result);

        return ResponseEntity.ok(
                ResponseBuilder
                        .buildOkResponse(userService.updateUser(dto))
        );
    }

    @DeleteMapping("/user/delete-by-id/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> deleteUserById(@PathVariable long id){
        return ResponseEntity.ok(
                ResponseBuilder
                        .buildOkResponse(
                                userService.deleteUser(id)));
    }
}
