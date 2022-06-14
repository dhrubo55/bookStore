package com.example.bookstore.model.mapper;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.BookMeta;
import com.example.bookstore.entity.RoleEntity;
import com.example.bookstore.entity.UserEntity;
import com.example.bookstore.model.CurrentUser;
import com.example.bookstore.model.dto.book.IssuedBook;
import com.example.bookstore.model.dto.user.UserCreateRequest;
import com.example.bookstore.model.dto.user.UserResponse;
import com.example.bookstore.model.dto.user.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.bookstore.util.DateTimeUtil.toAPIDateFormat;

@Slf4j
@Component
public class UserMapper {
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;
    private Map<Long, BookMeta> getAsMap(List<BookMeta> metaEntityList){
        return metaEntityList
                .stream()
                .collect(Collectors.toMap(BookMeta::getId, Function.identity()));
    }

    public UserResponse mapToDto(UserEntity entity) {
        return UserResponse
                .builder()
                .id(entity.getId())
                .name(entity.getFullName())
                .userName(entity.getUsername())
                .email(entity.getEmail())
                .build();
    }

    public UserResponse mapToDto(UserEntity entity, List<BookMeta> metaEntity){
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

    public List<UserResponse> mapToDto(List<UserEntity> entityList, List<BookMeta> metaEntityList){

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
    public UserEntity mapToNewUserEntity(UserCreateRequest dto, Collection<RoleEntity> roleEntities){
        return new UserEntity(dto.getUserName(), dto.getFullName(), dto.getEmail(), roleEntities);
    }

    public void fillUpdatableEntity(UserEntity entity, UserUpdateRequest dto){
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
    }

    private static List<GrantedAuthority> getGrantedAuthorities(Collection<RoleEntity> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    public  User mapToUserDetails(UserEntity entity) {
        return new CurrentUser(
                entity.getId(),
                entity.getUsername(),
                //password encoder
                entity.getPassword(),
                getGrantedAuthorities(entity.getRoles())
        );
    }

}