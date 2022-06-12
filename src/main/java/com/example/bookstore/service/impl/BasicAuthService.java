package com.example.bookstore.service.impl;

import com.example.bookstore.i18n.LocaleMessageHelper;
import com.example.bookstore.model.dto.AuthenticationRequest;
import com.example.bookstore.model.dto.TokenResponse;
import com.example.bookstore.service.AuthService;
import com.example.bookstore.service.BaseService;
import com.example.bookstore.util.JWTUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;

@Service
public class BasicAuthService extends BaseService implements AuthService {

    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    private final LocaleMessageHelper localeMessageHelper;


    public BasicAuthService(@Qualifier("customUserDetailsService") UserDetailsService userDetailsService,
                                AuthenticationManager authenticationManager, LocaleMessageHelper localeMessageHelper) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.localeMessageHelper  = localeMessageHelper;
    }


    @Override
    public TokenResponse authenticate(AuthenticationRequest request) {
        try{
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            );
            authenticationManager.authenticate(auth);
        }catch (BadCredentialsException e){
            throw new BadCredentialsException(
                    localeMessageHelper.getLocalMessage("validation.authentication.failed.message"), e);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = JWTUtils.generateToken(userDetails.getUsername());
        return new TokenResponse(token);
    }

    @Override
    public UserDetails validateToken(String token) {
        String userName = JWTUtils.extractUserName(token);

        if(JWTUtils.isTokenInvalidOrExpired(token))
            try {
                throw new AuthException(localeMessageHelper.getLocalMessage("validation.token.expired.or.invalid.message"));
            } catch (AuthException e) {
                throw new RuntimeException(e);
            }

        return userDetailsService.loadUserByUsername(userName);
    }
}

