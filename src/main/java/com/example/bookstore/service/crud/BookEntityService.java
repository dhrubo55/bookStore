package com.example.bookstore.service.crud;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.BookMeta;
import com.example.bookstore.exception.RecordNotFoundException;
import com.example.bookstore.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookEntityService extends BaseCRUDService<Book, BookRepository> {
    public BookEntityService(BookRepository repository) {
        super(repository);
    }

    public List<Book> findBooksByIdIn(Set<Long> ids){
        return repository.findByIdIn(ids);
    }

    public List<Book> findBooksInMeta(Collection<BookMeta> metaEntityList){
        return repository.findByIdIn(getBookIds(metaEntityList));
    }

    @Override
    public Book delete(long id){
        return repository.findById(id)
                .map(bookEntity -> {
                    bookEntity.getUserEntities().forEach(user -> user.removeBook(bookEntity));
                    bookEntity.getUserEntities().clear();
                    repository.delete(bookEntity);
                    return bookEntity;
                }).orElseThrow(() -> new RecordNotFoundException("api.response.NOT_FOUND.message"));
    }

    private Set<Long> getBookIds(Collection<BookMeta> metaEntityList){
        return metaEntityList
                .stream()
                .mapToLong(BookMeta::getBookId)
                .boxed()
                .collect(Collectors.toSet());
    }
}