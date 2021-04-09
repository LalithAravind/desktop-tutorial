package com.cognizant.cloudone.ui.controller.bean;

import java.util.List;

import com.cognizant.cloudone.kernel.constants.GlobalConstant;

public class ImageBean extends BaseDataGridBean{
	
	private String lastUpdateTime = GlobalConstant.N_A;
	private String location;
	private List<String> datacenterList;
	private String ImageID;
	
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public List<String> getDatacenterList() {
		return datacenterList;
	}
	public void setDatacenterList(List<String> datacenterList) {
		this.datacenterList = datacenterList;
	}
	public String getImageID() {
		return ImageID;
	}
	public void setImageID(String imageID) {
		ImageID = imageID;
	}

}
