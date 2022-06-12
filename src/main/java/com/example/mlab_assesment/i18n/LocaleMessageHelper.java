package com.example.mlab_assesment.i18n;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LocaleMessageHelper {

    private final HttpServletRequest request;
    private final MessageSource messageSource;


    public Locale getLocale() {
        return request.getLocale();
    }

    public String getLocalMessage(String key, Object... objects) {
        return messageSource.getMessage(key, objects, getLocale());
    }
}

