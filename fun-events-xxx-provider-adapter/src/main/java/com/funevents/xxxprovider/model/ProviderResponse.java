package com.funevents.xxxprovider.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@XmlRootElement(name = "planList")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@ToString
public class ProviderResponse {
	@XmlElement(name = "output")
	private Output output;
}
