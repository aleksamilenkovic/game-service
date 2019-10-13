package com.gameservice.exception;

import static com.gameservice.exception.InternalException.extractCause;
import static java.lang.String.format;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = -1919066409346042757L;

	final ApplicationExceptionCode code;

	public ApplicationException(ApplicationExceptionCode code, String pattern, Object... args) {
		super(format("[%s] ", code) + format(pattern, args), extractCause(args));
		this.code = code;
	}

}
