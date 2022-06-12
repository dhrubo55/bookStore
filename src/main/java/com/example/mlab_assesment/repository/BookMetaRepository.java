package com.example.mlab_assesment.repository;

import com.example.mlab_assesment.entity.Book;
import com.example.mlab_assesment.entity.BookMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;

public interface BookMetaRepository extends JpaRepository<BookMeta, Long>, JpaSpecificationExecutor<BookMeta> {

    List<BookMeta> findByIdIn(List<Long> ids);
}
