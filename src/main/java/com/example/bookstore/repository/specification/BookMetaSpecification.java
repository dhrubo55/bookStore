package com.example.bookstore.repository.specification;

import com.example.bookstore.entity.BookMeta;
import com.example.bookstore.model.dto.book.BookSearchRequest;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


@UtilityClass
public class BookMetaSpecification {

    private final String FIELD_BOOK_NAME = "name";
    private final String FIELD_AUTHOR_NAME = "authorName";


    public static Specification<BookMeta> getSearchSpecification(BookSearchRequest request){
        return Specification.where(generateSearchSpecification(request));
    }

    private static Specification<BookMeta> generateSearchSpecification(BookSearchRequest request){
        return ( (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(StringUtils.isNotBlank(request.getBookName()))
                predicates.add(criteriaBuilder.like(root.get(FIELD_BOOK_NAME),"%"+request.getBookName()+"%"));

            if(StringUtils.isNotBlank(request.getAuthorName()))
                predicates.add(criteriaBuilder.like(root.get(FIELD_AUTHOR_NAME), "%"+request.getAuthorName()+"%"));

            return criteriaBuilder.and(
                    predicates.stream()
                            .toArray(Predicate[]::new));
        });
    }
}
