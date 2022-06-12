package com.example.mlab_assesment.model.mapper;

import com.example.mlab_assesment.entity.Book;
import com.example.mlab_assesment.entity.BookMeta;
import com.example.mlab_assesment.entity.User;
import com.example.mlab_assesment.model.dto.book.BookCreateRequest;
import com.example.mlab_assesment.model.dto.book.BookResponse;
import com.example.mlab_assesment.model.dto.book.BookUpdateRequest;
import com.example.mlab_assesment.model.dto.user.IssuedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.example.mlab_assesment.util.DateTimeUtil.toAPIDateFormat;
import static com.example.mlab_assesment.util.DateTimeUtil.toDBDateFormat;

@Component
@Slf4j
public class BookMapper {

    public BookMeta mapToNewBookMetaEntity(BookCreateRequest dto){
        return new BookMeta(
                dto.getName(),
                dto.getAuthorName(),
                dto.getDescription(),
                dto.getNoOfCopy(),
                toDBDateFormat(dto.getReleaseDate()), 0);
    }

    public void fillUpdatableEntity(BookMeta entityToUpdate, BookUpdateRequest dto){
        entityToUpdate.setAuthorName(dto.getAuthorName());
        entityToUpdate.setDescription(dto.getDescription());
        entityToUpdate.setNoOfCopy(dto.getNoOfCopy());
        entityToUpdate.setReleaseDate(toDBDateFormat(dto.getReleaseDate()));
    }

    public void fillIssuableEntity(List<Book> entityList, List<BookMeta> metaList, User user){

        Map<Long, BookMeta> bookMetaMap = this.getAsMetaMap(metaList);

        Consumer<Book> issueBookMappingConsumer = book -> {
            BookMeta metaEntity = bookMetaMap.get(book.getMetaId());
            if(metaEntity.getNoOfCopy() > 0){
                metaEntity.setNoOfCopy(metaEntity.getNoOfCopy() - 1);
                book.addUser(user);
                user.addBook(book);
            }else{
                log.info("Can't issue {} ", metaEntity.getName());
            }
        };

        entityList.forEach(issueBookMappingConsumer);
    }

    public BookResponse mapToBookResponseDTO(BookMeta meta, IssuedUser issuedUser){

        return BookResponse.builder()
                .id(meta.getBookId())
                .name(meta.getName())
                .authorName(meta.getAuthorName())
                .description(meta.getDescription())
                .noOfCopy(meta.getNoOfCopy())
                .releaseDate(meta.getReleaseDate())
                .issuedUsers(Collections.singletonList(issuedUser))
                .build();
    }

    public BookResponse mapToBookResponseDTO(BookMeta metaEntity, User user){

        IssuedUser issuedUser = IssuedUser.builder()
                .id(user.getId())
                .name(user.getFullName())
                .build();

        return this.mapToBookResponseDTO(metaEntity, issuedUser);
    }

    public List<BookResponse> mapToBookResponseDTO(List<BookMeta> metaEntityList, User user){
        return metaEntityList
                .stream()
                .map(e -> mapToBookResponseDTO(e, user))
                .collect(Collectors.toList());
    }


    public BookResponse mapToBookResponseDTO(Book book, BookMeta meta){
        return BookResponse
                .builder()
                .id(book.getId())
                .name(meta.getName())
                .description(meta.getDescription())
                .authorName(meta.getAuthorName())
                .noOfCopy(meta.getNoOfCopy())
                .releaseDate(toAPIDateFormat(meta.getReleaseDate()))
                .issuedUsers(mapToIssuedUserDTO(book.getUsers()))
                .build();
    }

    public List<BookResponse> mapToBookResponseDTO(List<Book> bookEntities,
                                                   List<BookMeta> bookMetaEntities){

        Map<Long, BookMeta> metaMap = this.getAsMetaMap(bookMetaEntities);

        Function<Book, BookResponse> bookResponseMapper =
                book -> mapToBookResponseDTO(
                        book,
                        metaMap.get(book.getMetaId())
                );

        return bookEntities
                .stream()
                .map(bookResponseMapper)
                .collect(Collectors.toList());
    }

    private Map<Long, BookMeta> getAsMetaMap(List<BookMeta> metaList){
        return metaList
                .stream()
                .collect(Collectors.toMap(BookMeta::getId, Function.identity()));
    }

    private IssuedUser mapToIssuedUserDTO(User entity){
        return IssuedUser
                .builder()
                .name(entity.getFullName())
                .id(entity.getId())
                .build();
    }

    private List<IssuedUser> mapToIssuedUserDTO(List<User> entityList){
        return entityList
                .stream()
                .map(this::mapToIssuedUserDTO)
                .collect(Collectors.toList());
    }

    public List<Book> extractIssuedBooks(User userEntity, Set<Long> bookIds) {
        return userEntity
                .getBooks()
                .stream()
                .filter(book -> bookIds.contains(book.getId()))
                .collect(Collectors.toList());
    }

    public void fillBookSubmissionEntity(User userEntity, Set<Long> requestedBookIds, List<Book> issuedBooks, List<BookMeta> metaEntities) {

        Predicate<Book> isBookInRequestedBookList = book -> requestedBookIds.contains(book.getId()); //O(n)

        userEntity.getBooks().removeIf(isBookInRequestedBookList);
        issuedBooks.forEach(book -> book.getUsers().remove(userEntity));
        metaEntities.forEach(meta -> meta.setNoOfCopy(meta.getNoOfCopy() + issuedBooks.size()));
    }

    public void fillBookSubmissionEntity(User userEntity, List<Book> issuedBooks, List<BookMeta> metaEntities) {
        Map<Long, List<Book>> bookMetaMap = issuedBooks
                .stream()
                .collect(Collectors.groupingBy(Book::getMetaId));

        userEntity.getBooks().removeAll(issuedBooks);
        issuedBooks.forEach(book -> book.getUsers().remove(userEntity));
        metaEntities.forEach(meta -> meta.setNoOfCopy(meta.getNoOfCopy() + bookMetaMap.get(meta.getId()).size())); //O(n)
    }
}