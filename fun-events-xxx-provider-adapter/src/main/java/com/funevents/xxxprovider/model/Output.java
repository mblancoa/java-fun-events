package com.funevents.xxxprovider.model;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@ToString
public class Output {
	@XmlElement(name = "base_plan")
	private List<BasePlan> basePlans;
}