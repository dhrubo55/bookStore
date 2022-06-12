package com.example.mlab_assesment.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "mlab_assessment_book_meta")
@Data
@NoArgsConstructor
public class BookMeta extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "author_name")
    private String authorName;

    @Column(name = "description")
    private String description;

    @Column(name = "no_of_copy")
    private int noOfCopy;

    @Column(name = "release_date")
    private String releaseDate;

    @Column(name = "book_id", nullable = false)
    private long bookId;

    @Builder
    public BookMeta(String name, String authorName, String description, int noOfCopy, String releaseDate, long bookId) {
        this.name = name;
        this.authorName = authorName;
        this.description = description;
        this.noOfCopy = noOfCopy;
        this.releaseDate = releaseDate;
        this.bookId = bookId;
    }
}