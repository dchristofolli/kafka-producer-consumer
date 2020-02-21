package br.com.dchristofolli.kafka.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
@AllArgsConstructor
@lombok.Data
public class ErrorModel {
    String message;
    String error;
    HttpStatus status;
}
