package com.example.bookstore.service.impl;

import com.example.bookstore.entity.BookMeta;
import com.example.bookstore.entity.RoleEntity;
import com.example.bookstore.entity.UserEntity;
import com.example.bookstore.exception.NotUniqueException;
import com.example.bookstore.exception.RecordNotFoundException;
import com.example.bookstore.model.dto.user.UserCreateRequest;
import com.example.bookstore.model.dto.user.UserResponse;
import com.example.bookstore.model.dto.user.UserUpdateRequest;
import com.example.bookstore.model.mapper.UserMapper;
import com.example.bookstore.service.BaseService;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.UserService;
import com.example.bookstore.service.crud.BookMetaEntityService;
import com.example.bookstore.service.crud.UserEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends BaseService implements UserService {

    private final UserEntityService userEntityService;
    private final BookMetaEntityService metaEntityService;
    private final BookService bookService;
    private final UserMapper mapper;

    private final RoleService roleService;


    @Override
    public List<UserResponse> findAllUser() {
        List<UserEntity> userEntityEntities = userEntityService.findAll();
        List<BookMeta> metaEntities = metaEntityService.findAll();
        return mapper.mapToDto(userEntityEntities, metaEntities);
    }

    @Override
    public UserResponse findUserById(long id) {
        UserEntity userEntity = userEntityService
                .findById(id)
                .orElseThrow(supplyRecordNotFoundException("validation.constraints.userId.NotFound.message"));

        List<BookMeta> metaEntityList = metaEntityService.findMetaInBooks(userEntity.getBooks());
        return mapper.mapToDto(userEntity, metaEntityList);

    }

    @Override
    public UserResponse findUserByUsername(String userName) {
        UserEntity userEntity = userEntityService
                .findUserByUsername(userName)
                .orElseThrow(supplyRecordNotFoundException("validation.constraints.username.NotFound.message"));

        List<BookMeta> metaEntityList = metaEntityService.findMetaInBooks(userEntity.getBooks());
        return mapper.mapToDto(userEntity, metaEntityList);
    }

    @Override
    public UserResponse createUser(UserCreateRequest dto) {
        List<RoleEntity> roles = roleService.findRolesIn(dto.getRoles());
        userEntityService.findUserByUsername(dto.getUserName())
                .ifPresent(u -> {
                    throw new NotUniqueException(
                            messageHelper.getLocalMessage(
                                    "validation.constraints.username.exists.message"));
                });
        UserEntity entity = mapper.mapToNewUserEntity(dto, roles);
        userEntityService.save(entity);
        return getUserResponseDTO(entity);
    }

    @Override
    public UserResponse updateUser(UserUpdateRequest dto) {
        UserEntity updatableEntity = userEntityService
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

    private UserResponse getUserResponseDTO(UserEntity entity) {
        if(CollectionUtils.isEmpty(entity.getBooks()))
            return mapper.mapToDto(entity);

        List<BookMeta> metaEntities = metaEntityService.findMetaInBooks(entity.getBooks());
        return mapper.mapToDto(entity, metaEntities);
    }

    private List<String> getRoleNames(Collection<RoleEntity> roleEntities){
        return roleEntities
                .stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toList());
    }
}
