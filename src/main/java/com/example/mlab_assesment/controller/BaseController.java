package com.example.mlab_assesment.controller;

import com.example.mlab_assesment.exception.BadRequestException;
import com.example.mlab_assesment.i18n.LocaleMessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.stream.Collectors;

public class BaseController {

    private LocaleMessageHelper messageHelper;

    @Autowired
    public void di(LocaleMessageHelper messageHelper){
        this.messageHelper = messageHelper;
    }

    private String getJoinedErrorMessage(BindingResult bindingResult){
        return bindingResult.getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .map(messageHelper::getLocalMessage)
                .collect(Collectors.joining(", "));
    }

    protected void throwIfHasError(BindingResult result){
        if(result.hasErrors())
            throw new BadRequestException(this.getJoinedErrorMessage(result));
    }
}