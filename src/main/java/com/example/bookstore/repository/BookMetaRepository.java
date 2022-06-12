package com.example.bookstore.repository;

import com.example.bookstore.entity.BookMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BookMetaRepository extends JpaRepository<BookMeta, Long>, JpaSpecificationExecutor<BookMeta> {

    List<BookMeta> findByIdIn(List<Long> ids);
}
