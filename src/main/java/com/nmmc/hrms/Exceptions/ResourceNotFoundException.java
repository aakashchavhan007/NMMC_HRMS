package com.nmmc.hrms.Exceptions;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
   
	private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String messageKey, MessageSource messageSource, Locale locale, Object...args) {
    	super(messageSource.getMessage(messageKey, args, locale));
    }
    
    public ResourceNotFoundException(String message) {
		super(message);
	}

}
