package com.funevents.api;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.funevents.EventService;
import com.funevents.api.model.EventResponse;
import com.funevents.model.Event;

import lombok.RequiredArgsConstructor;

@RestController()
@Validated
@RequiredArgsConstructor(onConstructor = @__({ @org.springframework.beans.factory.annotation.Autowired }))
public class EventController {

	private final EventService eventService;
	private final ApiEventMapper mapper = ApiEventMapper.INSTANCE;

	@GetMapping(path = "/search", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<EventResponse> search(
			@RequestParam(name = "starts_at", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime startsAt,
			@RequestParam(name = "ends_at", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime endsAt) {
		final List<Event> list = this.eventService.getEvents(startsAt, endsAt);

		return ResponseEntity.ok(this.mapper.mapToEventResponse(list));
	}
}
