package com.example.bookstore.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(name = "mlab_assessment_user")
@Data
@NoArgsConstructor
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_name")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    @JoinTable(name = "mlab_user_book",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "book_id")}
    )
    List<Book> books = new ArrayList<>();

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(name = "User_Role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    Collection<RoleEntity> roles = new HashSet<>();
    public void addRole(RoleEntity bookEntity){
        roles.add(bookEntity);
    }
    public void removeRole(RoleEntity bookEntity){
        roles.remove(bookEntity);
    }

    public void addBook(Book bookEntity){
        books.add(bookEntity);
    }
    public void removeBook(Book bookEntity){
        books.remove(bookEntity);
    }

    public UserEntity(String username, String fullName, String email, Collection<RoleEntity> roles) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.roles.addAll(roles);
    }
}
