package com.example.mlab_assesment.service.crud;

import com.example.mlab_assesment.entity.User;
import com.example.mlab_assesment.exception.RecordNotFoundException;
import com.example.mlab_assesment.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserEntityService extends BaseCRUDService<User, UserRepository> {
    public UserEntityService(UserRepository repository) {
        super(repository);
    }

    public Optional<User> findUserByUsername(String userName){
        return repository.findDistinctByUsername(userName);
    }

    @Override
    public User delete(long id){
        return repository.findById(id)
                .map(userEntity -> {
                    userEntity.getBooks().forEach( book -> book.setUsers(null));
                    userEntity.getBooks().clear();
                    repository.delete(userEntity);
                    return userEntity;
                }).orElseThrow(() -> new RecordNotFoundException("validation.constraints.userId.NotFound.message"));
    }
}
