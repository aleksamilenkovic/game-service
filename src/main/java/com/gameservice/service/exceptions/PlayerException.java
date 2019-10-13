package com.gameservice.service.exceptions;

import com.gameservice.exception.ApplicationException;
import com.gameservice.exception.ApplicationExceptionCode;

public class PlayerException extends ApplicationException {

	private static final long serialVersionUID = 3322213216263416742L;

	public PlayerException(PlayerExceptionCode code, String pattern, Object... args) {
		super(code, pattern, args);
	}

	public enum PlayerExceptionCode implements ApplicationExceptionCode {
		DUPLICATED_NAME, DUPLICATED_NUMBER, PLAYER_NOT_IN_TEAM, INVALID_JERSEY_NUMBER;
	}
}
