package com.example.mlab_assesment.advice;

import com.example.mlab_assesment.exception.BadRequestException;
import com.example.mlab_assesment.exception.RecordNotFoundException;
import com.example.mlab_assesment.i18n.LocaleMessageHelper;
import com.example.mlab_assesment.util.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final LocaleMessageHelper messageHelper;

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequestExceptionHandler(BadRequestException ex, WebRequest request) {
        this.logException(ex);
        return this.buildResponseEntity(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<?> recordNotFoundExceptionHandler(RecordNotFoundException ex, WebRequest request) {
        this.logException(ex);

        return this.buildResponseEntity(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseBuilder
                        .buildResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                messageHelper.getLocalMessage("api.response.INTERNAL_SERVER_ERROR.message")));
    }

    private ResponseEntity<?> buildResponseEntity(HttpStatus status, Exception ex){
        return ResponseEntity
                .status(status)
                .body(ResponseBuilder.buildResponse(status, ex.getLocalizedMessage()));
    }

    private void logException(Exception e){
        log.error(e.getMessage());
    }
}
