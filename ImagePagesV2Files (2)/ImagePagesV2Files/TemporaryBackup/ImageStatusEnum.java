package com.cognizant.cloudone.kernel.image.enums;

public enum ImageStatusEnum {
	AVAILABLE(1, "available",1),
	IN_USE(2, "in-use",2),
	OTHER(3, "other",6);
	
	private final int id;
	private final String desc;
	private int index;
	
	private ImageStatusEnum(int id, String desc, int index) {
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
