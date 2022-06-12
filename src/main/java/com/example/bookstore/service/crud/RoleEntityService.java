package com.example.bookstore.service.crud;

import com.example.bookstore.entity.RoleEntity;
import com.example.bookstore.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleEntityService extends BaseCRUDService<RoleEntity, RoleRepository> {

    public RoleEntityService(RoleRepository repository) {
        super(repository);
    }

    public List<RoleEntity> findRolesIn(List<Long> ids){
        return repository.findByIdIn(ids);
    }
}
