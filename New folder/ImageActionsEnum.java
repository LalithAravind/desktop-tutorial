package com.cognizant.cloudone.kernel.image.enums;

public enum ImageActionsEnum {
	ADD_IMAGE(1, "Add Image",1),
	DELETE_IMAGE(5, "Delete IMAGES",2);
	
	private final int id;
	private final String desc;
	private int index;
	
	private ImageActionsEnum(int id, String desc, int index) {
		this.id = id;
		this.desc = desc;
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getId() {
		return id;
	}

	public String getDesc() {
		return desc;
	}
	

}
