package com.funevents.api;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;

import com.funevents.api.model.EventResponse;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@OpenAPIDefinition(info = @Info(title = "User API", version = "1.0"))
public interface EventApiDefinition {
	@Operation(description = "Returns a list of events between two dates", responses = {
			@ApiResponse(responseCode = "200", description = "List of events", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventResponse.class, example = "{\n"
					+ "  \"error\": null,\n" + "  \"data\": {\n" + "    \"events\": [\n" + "      {\n"
					+ "        \"id\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\n" + "        \"title\": \"string\",\n"
					+ "        \"start_date\": \"2025-05-16\",\n" + "        \"start_time\": \"string\",\n"
					+ "        \"end_date\": \"2025-05-16\",\n" + "        \"end_time\": \"string\",\n"
					+ "        \"min_price\": 0.1,\n" + "        \"max_price\": 0.1\n" + "      }\n" + "    ]\n" + "  }\n"
					+ "}"))),
			@ApiResponse(responseCode = "400", description = "Bad request error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventResponse.class, example = "{\n"
					+ "  \"error\": {\n" + "    \"code\": \"400\",\n" + "    \"message\": \"string\"\n" + "  },\n"
					+ "  \"data\": null\n" + "}"))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventResponse.class, example = "{\n"
					+ "  \"error\": {\n" + "    \"code\": \"500\",\n" + "    \"message\": \"string\"\n" + "  },\n"
					+ "  \"data\": null\n" + "}"))) })
	ResponseEntity<EventResponse> search(LocalDateTime startsAt, LocalDateTime endsAt);
}
