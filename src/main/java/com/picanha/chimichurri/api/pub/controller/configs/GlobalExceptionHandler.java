package com.picanha.chimichurri.api.pub.controller.configs;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomRequestException.class)
	public ResponseEntity<Map<String, Object>> handleInvalidRequest(CustomRequestException ex) {

		Map<String, Object> body = new HashMap<>();
		body.put("error", ex.getStatus().getReasonPhrase());
		body.put("message", ex.getMessage());
		body.put("status", ex.getStatus().value());
		body.put("timestamp", System.currentTimeMillis());

		return ResponseEntity.badRequest().body(body);
	}
}
