package com.funevents.api.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventSummary {
	private UUID id;
	private String title;
	@JsonProperty("start_date")
	private LocalDate startDate;
	@JsonProperty("start_time")
	private LocalTime startTime;
	@JsonProperty("end_date")
	private LocalDate endDate;
	@JsonProperty("end_time")
	private LocalTime endTime;
	@JsonProperty("min_price")
	private double minPrice;
	@JsonProperty("max_price")
	private double maxPrice;
}
