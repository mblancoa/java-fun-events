package com.funevents.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Event {

	private UUID id;
	private String provId;
	private String title;
	private boolean onlineSale;
	private LocalDateTime startsAt;
	private LocalDateTime endsAt;
	private double maxPrice;
	private double minPrice;
}
