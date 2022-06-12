package com.example.bookstore.service.crud;

import com.example.bookstore.entity.UserEntity;
import com.example.bookstore.exception.RecordNotFoundException;
import com.example.bookstore.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserEntityService extends BaseCRUDService<UserEntity, UserRepository> {
    public UserEntityService(UserRepository repository) {
        super(repository);
    }

    public Optional<UserEntity> findUserByUsername(String userName){
        return repository.findDistinctByUsername(userName);
    }

    @Override
    public UserEntity delete(long id){
        return repository.findById(id)
                .map(userEntity -> {
                    userEntity.getBooks().forEach( book -> book.setUserEntities(null));
                    userEntity.getBooks().clear();
                    repository.delete(userEntity);
                    return userEntity;
                }).orElseThrow(() -> new RecordNotFoundException("validation.constraints.userId.NotFound.message"));
    }
}
