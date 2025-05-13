package com.funevents.xxxprovider.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@ToString
public class Zone {
	@XmlAttribute(name = "zone_id")
	private String zoneId;

	@XmlAttribute(name = "capacity")
	private int capacity;

	@XmlAttribute(name = "price")
	private double price;

	@XmlAttribute(name = "name")
	private String name;

	@XmlAttribute(name = "numbered")
	private String numbered;

}
