package com.cognizant.cloudone.to.instance;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import com.cognizant.cloudone.to.base.LocationTO;

@XmlAccessorType(XmlAccessType.FIELD)
public class ImageTO extends LocationTO{
	
	private String imageName;
	private String imageType;
	private String imageDetails;
	private String location;
	private String osName;
	private String imageID;
	private String status;
	
	private List<ImageInstanceTO> listAmiImages;
	
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	public String getImageDetails() {
		return imageDetails;
	}
	public void setImageDetails(String imageDetails) {
		this.imageDetails = imageDetails;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getOsName() {
		return osName;
	}
	public void setOsName(String osName) {
		this.osName = osName;
	}
	public String getImageID() {
		return imageID;
	}
	public void setImageID(String imageID) {
		this.imageID = imageID;
	}
	public List<ImageInstanceTO> getListAmiImages() {
		return listAmiImages;
	}
	public void setListAmiImages(List<ImageInstanceTO> listAmiImages) {
		this.listAmiImages = listAmiImages;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

}
