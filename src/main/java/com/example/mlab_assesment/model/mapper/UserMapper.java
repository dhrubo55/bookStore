package com.example.mlab_assesment.model.mapper;

import com.example.mlab_assesment.entity.Book;
import com.example.mlab_assesment.entity.BookMeta;
import com.example.mlab_assesment.entity.User;
import com.example.mlab_assesment.model.dto.book.IssuedBook;
import com.example.mlab_assesment.model.dto.user.UserCreateRequest;
import com.example.mlab_assesment.model.dto.user.UserResponse;
import com.example.mlab_assesment.model.dto.user.UserUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.mlab_assesment.util.DateTimeUtil.toAPIDateFormat;

@Slf4j
@Component
public class UserMapper {

    private Map<Long, BookMeta> getAsMap(List<BookMeta> metaEntityList){
        return metaEntityList
                .stream()
                .collect(Collectors.toMap(BookMeta::getId, Function.identity()));
    }

    public UserResponse mapToDto(User entity) {
        return UserResponse
                .builder()
                .id(entity.getId())
                .name(entity.getFullName())
                .userName(entity.getUsername())
                .email(entity.getEmail())
                .build();
    }

    public UserResponse mapToDto(User entity, List<BookMeta> metaEntity){
        Map<Long, BookMeta> metaMap = this.getAsMap(metaEntity);
        return UserResponse
                .builder()
                .id(entity.getId())
                .name(entity.getFullName())
                .userName(entity.getUsername())
                .email(entity.getEmail())
                .issuedBooks(this.mapToIssueBooks(entity.getBooks(), metaMap))
                .build();
    }

    public List<UserResponse> mapToDto(List<User> entityList, List<BookMeta> metaEntityList){

        Map<Long, BookMeta> metaMap = this.getAsMap(metaEntityList);

        return entityList
                .stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .name(user.getFullName())
                        .userName(user.getUsername())
                        .email(user.getEmail())
                        .issuedBooks(mapToIssueBooks(user.getBooks(), metaMap))
                        .build()
                ).collect(Collectors.toList());
    }

    private List<IssuedBook> mapToIssueBooks(List<Book> bookEntities, Map<Long, BookMeta> metaMap){
        return bookEntities
                .stream()
                .map(e ->
                        mapToIssueBook(metaMap.get(e.getMetaId())))
                .collect(Collectors.toList());
    }

    private IssuedBook mapToIssueBook(BookMeta metaEntity){
        return IssuedBook.builder()
                .id(metaEntity.getBookId())
                .name(metaEntity.getName())
                .description(metaEntity.getDescription())
                .authorName(metaEntity.getAuthorName())
                .noOfCopy(metaEntity.getNoOfCopy())
                .releaseDate(toAPIDateFormat(metaEntity.getReleaseDate()))
                .build();
    }

    /** ENTITY MAPPING */
    public User mapToNewUserEntity(UserCreateRequest dto){
        return new User(dto.getUserName(), dto.getFullName(), dto.getEmail());
    }

    public void fillUpdatableEntity(User entity, UserUpdateRequest dto){
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
    }

}