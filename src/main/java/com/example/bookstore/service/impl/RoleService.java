package com.example.bookstore.service.impl;

import com.example.bookstore.entity.RoleEntity;
import com.example.bookstore.exception.RecordNotFoundException;
import com.example.bookstore.service.crud.RoleEntityService;
import com.example.bookstore.util.UserRole;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleEntityService roleEntityService;

    public List<RoleEntity> findAllRoles(){
        return roleEntityService.findAll();
    }

    public List<RoleEntity> createRoles(List<UserRole> roles) {
        Function<UserRole, RoleEntity> mapFunction = r -> new RoleEntity(r.getCode(), r.getRole());

        List<RoleEntity> roleEntities = roles.stream()
                .map(mapFunction)
                .collect(Collectors.toList());
        return roleEntityService.save(roleEntities);
    }

    public RoleEntity findRoleById(long id) {
        UserRole.getValueByCode(id);
        return roleEntityService
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException("No role found for this id associated"));
    }

    public RoleEntity findRoleByType(UserRole role) {
        return findRoleById(role.getCode());
    }

    public List<RoleEntity> findRolesIn(List<Long> ids){
        List<RoleEntity> roles = roleEntityService.findRolesIn(ids);
        if(CollectionUtils.isEmpty(roles))
            throw new RecordNotFoundException("No Role found");
        return roles;
    }
}
