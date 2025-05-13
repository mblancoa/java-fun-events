package com.funevents.mongodbrepository.model;

import static com.funevents.mongodbrepository.model.Constants.EVENT_COLLECTION;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = EVENT_COLLECTION)
public class EventDB {
	@Id
	private UUID id;
	@Field("prov_id")
	private String provId;
	@Field("title")
	private String title;
	@Field("starts_at")
	private LocalDateTime startsAt;
	@Field("ends_at")
	private LocalDateTime endsAt;
	@Field("max_price")
	private double maxPrice;
	@Field("min_price")
	private double minPrice;
}
