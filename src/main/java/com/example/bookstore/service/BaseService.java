package com.example.bookstore.service;

import com.example.bookstore.exception.RecordNotFoundException;
import com.example.bookstore.i18n.LocaleMessageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Supplier;

public abstract class BaseService {

    protected final String RECORD_NOT_FOUND_MSG_KEY="api.response.NOT_FOUND.message";

    @Autowired
    protected LocaleMessageHelper messageHelper;

    protected Supplier<RecordNotFoundException> supplyRecordNotFoundException(String messageKey){
        return this.supplyException(
                new RecordNotFoundException(
                        messageHelper.getLocalMessage(messageKey)
                )
        );
    }

    private  <E extends RuntimeException> Supplier<E> supplyException(E exception){
        return () -> exception;
    }
}