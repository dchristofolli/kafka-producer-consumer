package br.com.dchristofolli.kafka.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class Handler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ApiException.class)
    public ErrorModel apiExceptionValidator(ApiException e) {
        return ErrorModel.builder()
                .message(e.getMessage())
                .error(e.getClass().getName())
                .status(HttpStatus.NOT_FOUND)
                .build();
    }
}
