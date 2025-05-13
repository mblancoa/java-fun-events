package com.funevents.xxxprovider.model;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@ToString
public class BasePlan {
	@XmlAttribute(name = "base_plan_id")
	private String basePlanId;

	@XmlAttribute(name = "sell_mode")
	private String sellMode;

	@XmlAttribute(name = "title")
	private String title;

	@XmlAttribute(name = "organizer_company_id")
	private String organizerCompanyId;

	@XmlElement(name = "plan")
	private List<Plan> plans;
}