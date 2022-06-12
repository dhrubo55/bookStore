package com.example.bookstore.service.crud;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.BookMeta;
import com.example.bookstore.model.dto.book.BookSearchRequest;
import com.example.bookstore.repository.BookMetaRepository;
import com.example.bookstore.repository.specification.BookMetaSpecification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookMetaEntityService extends BaseCRUDService<BookMeta, BookMetaRepository> {
    public BookMetaEntityService(BookMetaRepository repository) {
        super(repository);
    }

    public List<BookMeta> searchBook(BookSearchRequest searchDTO){
        return repository.findAll(
                BookMetaSpecification.getSearchSpecification(searchDTO));
    }

    public List<BookMeta> findBookMetaByIdIn(List<Long> ids){
        return repository.findByIdIn(ids);
    }

    public List<BookMeta> findMetaInBooks(List<Book> bookEntities){
        return repository.findByIdIn(getMetaIds(bookEntities));
    }

    private List<Long> getMetaIds(List<Book> bookEntities){
        return bookEntities
                .stream()
                .mapToLong(Book::getMetaId)
                .boxed()
                .collect(Collectors.toList());
    }
}
