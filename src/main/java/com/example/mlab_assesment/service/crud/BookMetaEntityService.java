package com.example.mlab_assesment.service.crud;

import com.example.mlab_assesment.entity.Book;
import com.example.mlab_assesment.entity.BookMeta;
import com.example.mlab_assesment.model.dto.book.BookSearchRequest;
import com.example.mlab_assesment.repository.BookMetaRepository;
import com.example.mlab_assesment.repository.specification.BookMetaSpecification;
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
