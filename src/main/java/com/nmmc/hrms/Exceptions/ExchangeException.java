package com.nmmc.hrms.Exceptions;

import java.util.Locale;

import org.springframework.context.MessageSource;

public class ExchangeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ExchangeException(String message) {
		super(message);
	}
	
    public ExchangeException(String messageKey, MessageSource messageSource, Locale locale, Object...args) {
    	super(messageSource.getMessage(messageKey, args, locale));
    }
}
