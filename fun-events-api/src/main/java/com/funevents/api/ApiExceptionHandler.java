package com.funevents.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.funevents.api.model.Error;
import com.funevents.api.model.EventResponse;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

	@ExceptionHandler(value = MissingServletRequestParameterException.class)
	public ResponseEntity<EventResponse> badRequestException(final MissingServletRequestParameterException exception) {
		final EventResponse eventResponse = new EventResponse();
		final Error error = new Error();
		error.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
		error.setMessage(exception.getMessage());
		eventResponse.setError(error);

		return ResponseEntity.badRequest().body(eventResponse);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<EventResponse> handle404(final NoHandlerFoundException exception) {
		final EventResponse eventResponse = new EventResponse();
		final Error error = new Error();
		error.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
		error.setMessage("¡Ups! page not found");
		eventResponse.setError(error);

		log.error("Page not found.", exception);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(eventResponse);
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<EventResponse> genericException(final Exception exception) {
		final EventResponse eventResponse = new EventResponse();
		final Error error = new Error();
		error.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
		error.setMessage("An unexpected error occurred");
		eventResponse.setError(error);

		log.error("Unexpected error.", exception);
		return ResponseEntity.internalServerError().body(eventResponse);
	}

}
