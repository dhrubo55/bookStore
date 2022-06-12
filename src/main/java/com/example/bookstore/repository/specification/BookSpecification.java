package com.example.bookstore.repository.specification;

import com.example.bookstore.entity.Book;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@UtilityClass
public class BookSpecification {

    private final String FIELD_ID = "id";
    private final String FIELD_NOC = "noOfCopy";


    public static Specification<Book> whereBookIdIn(Set<Long> ids){
        return Specification.where(generateInQuerySpecification(ids));
    }

    private static Specification<Book> generateInQuerySpecification(Set<Long> ids){
        return ( (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.and(root.get(FIELD_ID).in(ids)));
//            predicates.add(criteriaBuilder.gt(root.get(FIELD_NOC), 0));

            return criteriaBuilder.and(
                    predicates.stream()
                            .toArray(Predicate[]::new));

        });
    }
}