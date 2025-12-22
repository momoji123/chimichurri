package com.picanha.chimichurri.api.pub.controller.configs;

import org.springframework.http.HttpStatus;

public class CustomRequestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8129120395935558235L;
	private HttpStatus status = HttpStatus.BAD_REQUEST;

	public CustomRequestException(String message) {
		super(message);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public CustomRequestException setStatus(HttpStatus status) {
		this.status = status;
		return this;
	}

}
