package com.cognizant.cloudone.cl.azure.entity;


import static com.cognizant.cloudone.cl.azure.constants.AzureConstants.XML_NAMESPACE;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="OSImage", namespace=XML_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class OSImage {
	
	@XmlElement(name="AffinityGroup", namespace=XML_NAMESPACE)
	private String affinityGroup;
	@XmlElement(name="Category", namespace=XML_NAMESPACE)
	private String category;
	@XmlElement(name="Location", namespace=XML_NAMESPACE)
	private String location;
	@XmlElement(name="Label", namespace=XML_NAMESPACE)
	private String label;
	@XmlElement(name="LogicalSizeInGB", namespace=XML_NAMESPACE)
	private Integer logicalSizeInGB;
	@XmlElement(name="Name", namespace=XML_NAMESPACE)
	private String name;
	@XmlElement(name="OS", namespace=XML_NAMESPACE)
	private String osType;
	@XmlElement(name="Eula", namespace=XML_NAMESPACE)
	private String eula;
	@XmlElement(name="Description", namespace=XML_NAMESPACE)
	private String description;
	@XmlElement(name="MediaLink", namespace=XML_NAMESPACE)
	private String mediaLink;
	
	public String getMediaLink() {
		return mediaLink;
	}
	public void setMediaLink(String mediaLink) {
		this.mediaLink = mediaLink;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Integer getLogicalSizeInGB() {
		return logicalSizeInGB;
	}
	public void setLogicalSizeInGB(Integer logicalSizeInGB) {
		this.logicalSizeInGB = logicalSizeInGB;
	}
	public String getOsType() {
		return osType;
	}
	public void setOsType(String osType) {
		this.osType = osType;
	}
	public String getEula() {
		return eula;
	}
	public void setEula(String eula) {
		this.eula = eula;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAffinityGroup() {
		return affinityGroup;
	}
	public void setAffinityGroup(String affinityGroup) {
		this.affinityGroup = affinityGroup;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
