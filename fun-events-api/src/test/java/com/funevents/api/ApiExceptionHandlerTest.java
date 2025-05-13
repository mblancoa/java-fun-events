package com.funevents.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;

import com.funevents.api.ApiExceptionHandler;
import com.funevents.api.model.Error;
import com.funevents.api.model.EventResponse;

public class ApiExceptionHandlerTest {

	private final ApiExceptionHandler handler = new ApiExceptionHandler();

	@Test
	public void testBadRequestException() {
		final String parameterName = "testParam";
		final String parameterType = "String";

		final MissingServletRequestParameterException ex = new MissingServletRequestParameterException(parameterName,
				parameterType);

		final ResponseEntity<EventResponse> response = this.handler.badRequestException(ex);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.hasBody());
		final EventResponse body = response.getBody();
		assertNotNull(body);
		assertNull(body.getData());
		assertNotNull(body.getError());
		final Error error = body.getError();
		assertEquals(String.valueOf(400), error.getCode());
		assertTrue(error.getMessage().contains(parameterName));
	}

	@Test
	public void testGenericException() {
		final Exception ex = new RuntimeException("Unexpected error");
		final ResponseEntity<EventResponse> response = this.handler.genericException(ex);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		final EventResponse body = response.getBody();
		assertTrue(response.hasBody());
		assertNotNull(body);
		assertNull(body.getData());
		assertNotNull(body.getError());
		final Error error = body.getError();
		assertEquals(String.valueOf(500), error.getCode());
		assertEquals("An unexpected error occurred", error.getMessage());
	}
}