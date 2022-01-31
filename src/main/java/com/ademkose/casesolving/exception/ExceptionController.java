package com.ademkose.casesolving.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice(basePackages = "com.ademkose.casesolving.exception")
public class ExceptionController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    @SuppressWarnings({ "unchecked", "rawtypes" })
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex) {
        LOGGER.error("[BadRequestException] Exception: {}", ex.getMessage());
        return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}