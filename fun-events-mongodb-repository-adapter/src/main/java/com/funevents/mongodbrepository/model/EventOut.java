package com.funevents.mongodbrepository.model;

import static com.funevents.mongodbrepository.model.Constants.EVENT_COLLECTION;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = EVENT_COLLECTION)
public class EventOut {
	@Id
	private UUID id;
	@Field("title")
	private String title;
	@Field("start_date")
	private String startDate;
	@Field("end_date")
	private String endDate;
	@Field("start_time")
	private String startTime;
	@Field("end_time")
	private String endTime;
	@Field("max_price")
	private double maxPrice;
	@Field("min_price")
	private double minPrice;
}
