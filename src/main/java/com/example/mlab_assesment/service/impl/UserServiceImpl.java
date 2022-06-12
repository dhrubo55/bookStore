package com.example.mlab_assesment.service.impl;

import com.example.mlab_assesment.entity.BookMeta;
import com.example.mlab_assesment.entity.User;
import com.example.mlab_assesment.exception.NotUniqueException;
import com.example.mlab_assesment.exception.RecordNotFoundException;
import com.example.mlab_assesment.model.dto.user.UserCreateRequest;
import com.example.mlab_assesment.model.dto.user.UserResponse;
import com.example.mlab_assesment.model.dto.user.UserUpdateRequest;
import com.example.mlab_assesment.model.mapper.UserMapper;
import com.example.mlab_assesment.service.BaseService;
import com.example.mlab_assesment.service.BookService;
import com.example.mlab_assesment.service.UserService;
import com.example.mlab_assesment.service.crud.BookMetaEntityService;
import com.example.mlab_assesment.service.crud.UserEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends BaseService implements UserService {

    private final UserEntityService userEntityService;
    private final BookMetaEntityService metaEntityService;
    private final BookService bookService;
    private final UserMapper mapper;


    @Override
    public List<UserResponse> findAllUser() {
        List<User> userEntities = userEntityService.findAll();
        List<BookMeta> metaEntities = metaEntityService.findAll();
        return mapper.mapToDto(userEntities, metaEntities);
    }

    @Override
    public UserResponse findUserById(long id) {
        User userEntity = userEntityService
                .findById(id)
                .orElseThrow(supplyRecordNotFoundException("validation.constraints.userId.NotFound.message"));

        List<BookMeta> metaEntityList = metaEntityService.findMetaInBooks(userEntity.getBooks());
        return mapper.mapToDto(userEntity, metaEntityList);

    }

    @Override
    public UserResponse findUserByUsername(String userName) {
        User userEntity = userEntityService
                .findUserByUsername(userName)
                .orElseThrow(supplyRecordNotFoundException("validation.constraints.username.NotFound.message"));

        List<BookMeta> metaEntityList = metaEntityService.findMetaInBooks(userEntity.getBooks());
        return mapper.mapToDto(userEntity, metaEntityList);
    }

    @Override
    public UserResponse createUser(UserCreateRequest dto) {
        userEntityService.findUserByUsername(dto.getUserName())
                .ifPresent(u -> {
                    throw new NotUniqueException(
                            messageHelper.getLocalMessage(
                                    "validation.constraints.username.exists.message"));
                });
        User entity = mapper.mapToNewUserEntity(dto);
        userEntityService.save(entity);
        return getUserResponseDTO(entity);
    }

    @Override
    public UserResponse updateUser(UserUpdateRequest dto) {
        User updatableEntity = userEntityService
                .findById(dto.getId())
                .orElseThrow(supplyRecordNotFoundException("validation.constraints.userId.NotFound.message"));

        mapper.fillUpdatableEntity(updatableEntity, dto);
        userEntityService.save(updatableEntity);
        return getUserResponseDTO(updatableEntity);
    }

    @Override
    public UserResponse deleteUser(long id) {
        bookService.submitAllBooks(id);
        try{
            return mapper.mapToDto(userEntityService.delete(id));
        }catch (RecordNotFoundException e) {
            throw new RecordNotFoundException(messageHelper.getLocalMessage(e.getMessage()));
        }
    }

    private UserResponse getUserResponseDTO(User entity) {
        if(CollectionUtils.isEmpty(entity.getBooks()))
            return mapper.mapToDto(entity);

        List<BookMeta> metaEntities = metaEntityService.findMetaInBooks(entity.getBooks());
        return mapper.mapToDto(entity, metaEntities);
    }
}
