package com.example.bookstore.service.impl;

import com.example.bookstore.entity.UserEntity;
import com.example.bookstore.model.mapper.UserMapper;
import com.example.bookstore.service.BaseService;
import com.example.bookstore.service.crud.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService extends BaseService implements UserDetailsService {

    private final UserEntityService userEntityService;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = userEntityService
                .findUserByUsername(username)
                .orElseThrow(supplyRecordNotFoundException("validation.constraints.username.NotFound.message"));
        return userMapper.mapToUserDetails(entity);
    }

}
