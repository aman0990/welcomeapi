package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String rsourcName, String fieldName, String fieldValue) {
        super(String.format("%s not found with %s : '%s'", rsourcName, fieldName, fieldValue));
    }
}
