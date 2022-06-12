package com.example.bookstore.service.impl;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.BookMeta;
import com.example.bookstore.entity.UserEntity;
import com.example.bookstore.exception.BadRequestException;
import com.example.bookstore.exception.RecordNotFoundException;
import com.example.bookstore.model.dto.book.*;
import com.example.bookstore.model.dto.user.UserResponse;
import com.example.bookstore.model.mapper.BookMapper;
import com.example.bookstore.model.mapper.UserMapper;
import com.example.bookstore.service.BaseService;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.EmailService;
import com.example.bookstore.service.crud.BookEntityService;
import com.example.bookstore.service.crud.BookMetaEntityService;
import com.example.bookstore.service.crud.UserEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl extends BaseService implements BookService {

    private final BookEntityService bookEntityService;
    private final BookMetaEntityService metaEntityService;
    private final UserEntityService userEntityService;
    private final EmailService emailService;

    private final BookMapper mapper;
    private final UserMapper userMapper;

    @Override
    public List<BookResponse> findAllBooks() {
        List<Book> books = bookEntityService.findAll();
        if(CollectionUtils.isEmpty(books))
            throw new RecordNotFoundException(messageHelper.getLocalMessage(RECORD_NOT_FOUND_MSG_KEY));

        List<BookMeta> metaList = metaEntityService.findAll();
        if(CollectionUtils.isEmpty(metaList))
            throw new RecordNotFoundException(messageHelper.getLocalMessage(RECORD_NOT_FOUND_MSG_KEY));

        return mapper.mapToBookResponseDTO(books, metaList);
    }

    @Override
    public BookResponse findBookById(long id) {
        Book book = bookEntityService
                .findById(id)
                .orElseThrow(super.supplyRecordNotFoundException(RECORD_NOT_FOUND_MSG_KEY));

        BookMeta meta = metaEntityService
                .findById(book.getMetaId())
                .orElseThrow(super.supplyRecordNotFoundException(RECORD_NOT_FOUND_MSG_KEY));

        return mapper.mapToBookResponseDTO(book, meta);
    }

    @Override
    public List<BookResponse> searchBook(BookSearchRequest dto) {
        List<BookMeta> metaEntities = metaEntityService.searchBook(dto);
        if(CollectionUtils.isEmpty(metaEntities))
            throw new RecordNotFoundException(messageHelper.getLocalMessage(RECORD_NOT_FOUND_MSG_KEY));

        List<Book> bookEntities = bookEntityService.findBooksInMeta(metaEntities);
        return mapper.mapToBookResponseDTO(bookEntities, metaEntities);
    }

    @Override
    public BookResponse createBook(BookCreateRequest dto) {
        Book book = new Book();
        BookMeta meta = mapper.mapToNewBookMetaEntity(dto);
        this.saveBook(book, meta);
        return mapper.mapToBookResponseDTO(book, meta);
    }

    @Override
    public BookResponse updateBook(BookUpdateRequest dto) {
        Book bookEntity = bookEntityService
                .findById(dto.getId())
                .orElseThrow(super.supplyRecordNotFoundException(RECORD_NOT_FOUND_MSG_KEY));

        BookMeta metaEntity = metaEntityService
                .findById(bookEntity.getMetaId())
                .orElseThrow(super.supplyRecordNotFoundException(RECORD_NOT_FOUND_MSG_KEY));

        mapper.fillUpdatableEntity(metaEntity, dto);

        saveBook(bookEntity, metaEntity);
        return mapper.mapToBookResponseDTO(bookEntity, metaEntity);
    }

    @Override
    public UserResponse issueBook(IssueBookRequest request) {
        int maxIssueBook = 5;
        if(CollectionUtils.isEmpty(request.getBookIds()))
            throw new BadRequestException("validation.constraints.issueBook.Empty.message");

        if(request.getBookIds().size() > maxIssueBook)
            throw new BadRequestException("validation.constraints.issueBook.Invalid.Max.message");

        IssueBookValidation validationDTO = this.validateIssueBookRequest(request);

        mapper.fillIssuableEntity(validationDTO.getBookEntities(), validationDTO.getMetaEntities(), validationDTO.getUserEntity());
        bookEntityService.save(validationDTO.getBookEntities());
        metaEntityService.save(validationDTO.getMetaEntities());

        emailService.sendEmail(
                validationDTO.getUserEntity().getEmail(),
                "Book issuance",
                String.format("Book issued by: %s", validationDTO.getUserEntity().getFullName()));

        return userMapper.mapToDto(
                validationDTO.getUserEntity(),
                metaEntityService.findMetaInBooks(validationDTO.getUserEntity().getBooks())
        );
    }

    @Override
    public UserResponse submitBooks(BookSubmitRequest dto) {
        if(CollectionUtils.isEmpty(dto.getBookIds()))
            throw new BadRequestException(messageHelper.getLocalMessage("api.response.BAD_REQUEST.message"));

        UserEntity userEntity = userEntityService
                .findById(dto.getUserId())
                .orElseThrow(supplyRecordNotFoundException("validation.constraints.userId.NotFound.message"));

        if(CollectionUtils.isEmpty(userEntity.getBooks()))
            throw new RecordNotFoundException(messageHelper.getLocalMessage("validation.constraints.user.NotIssued.books.message"));

        submitBooks(userEntity, dto.getBookIds());

        emailService.sendEmail(
                userEntity.getEmail(),
                "Book Submitted",
                String.format("Book submitted by: %s", userEntity.getFullName()));

        List<BookMeta> newMetaList = metaEntityService.findMetaInBooks(userEntity.getBooks());
        return userMapper.mapToDto(userEntity, newMetaList);
    }

    @Override
    public UserResponse submitAllBooks(long userId) {
        UserEntity userEntity = userEntityService
                .findById(userId)
                .orElseThrow(supplyRecordNotFoundException("validation.constraints.userId.NotFound.message"));

        if(CollectionUtils.isEmpty(userEntity.getBooks()))
            return userMapper.mapToDto(userEntity);

        List<BookMeta> metaEntities = metaEntityService.findMetaInBooks(userEntity.getBooks());
        if(CollectionUtils.isEmpty(metaEntities))
            throw new RecordNotFoundException(messageHelper.getLocalMessage(RECORD_NOT_FOUND_MSG_KEY));

        mapper.fillBookSubmissionEntity(userEntity, userEntity.getBooks(), metaEntities);
        userEntityService.save(userEntity);
        metaEntityService.save(metaEntities);
        return userMapper.mapToDto(userEntity);
    }

    @Override
    public BookResponse deleteBook(long id) {
        try{
            Book bookEntity = bookEntityService.delete(id);
            BookMeta metaEntity = metaEntityService.delete(bookEntity.getMetaId());
            return mapper.mapToBookResponseDTO(bookEntity, metaEntity);
        }catch (RecordNotFoundException e){
            throw new RecordNotFoundException(messageHelper.getLocalMessage(e.getMessage()));
        }
    }

    private IssueBookValidation validateIssueBookRequest(IssueBookRequest dto){
        UserEntity userEntity = userEntityService
                .findById(dto.getUserId())
                .orElseThrow(super.supplyRecordNotFoundException("validation.constraints.userId.NotFound.message"));

        List<Book> bookEntities = bookEntityService.findBooksByIdIn(dto.getBookIds());
        if(CollectionUtils.isEmpty(bookEntities))
            throw new RecordNotFoundException(messageHelper.getLocalMessage(RECORD_NOT_FOUND_MSG_KEY));

        List<BookMeta> metaEntities = metaEntityService.findMetaInBooks(bookEntities);
        if(CollectionUtils.isEmpty(metaEntities))
            throw new RecordNotFoundException(messageHelper.getLocalMessage(RECORD_NOT_FOUND_MSG_KEY));

        return IssueBookValidation.builder()
                .userEntity(userEntity)
                .bookEntities(bookEntities)
                .metaEntities(metaEntities)
                .build();
    }

    private void submitBooks(UserEntity userEntity, Set<Long> bookIds){
        List<Book> issuedBooks = mapper.extractIssuedBooks(userEntity, bookIds);

        if(CollectionUtils.isEmpty(issuedBooks))
            throw new RecordNotFoundException(messageHelper.getLocalMessage("validation.constraints.issueBook.NotFound.message"));

        List<BookMeta> metaEntities = metaEntityService.findMetaInBooks(issuedBooks);
        if(CollectionUtils.isEmpty(metaEntities))
            throw new RecordNotFoundException(messageHelper.getLocalMessage(RECORD_NOT_FOUND_MSG_KEY));

        mapper.fillBookSubmissionEntity(userEntity, bookIds, issuedBooks, metaEntities);
        userEntityService.save(userEntity);
        metaEntityService.save(metaEntities);
    }

    private void saveBook(Book book, BookMeta metaEntity) {
        bookEntityService.save(book);
        metaEntity.setBookId(book.getId());
        metaEntityService.save(metaEntity);
        book.setMetaId(metaEntity.getId());
        bookEntityService.save(book);
    }


}
