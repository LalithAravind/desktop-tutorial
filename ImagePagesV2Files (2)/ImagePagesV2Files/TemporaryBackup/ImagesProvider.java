package com.cognizant.cloudone.ic.instance.provider;

import java.util.ArrayList;
import java.util.List;

import com.cognizant.cloudone.biz.instance.bo.ImageBO;
import com.cognizant.cloudone.to.instance.ImageTO;

public class ImagesProvider {
	
	
	public static List<ImageTO> getTOFromBO(List<ImageBO> listImageBOs) {
		List<ImageTO> listImageTOs = new ArrayList<>();
		if(listImageBOs != null && !listImageBOs.isEmpty()){
			{
				for(ImageBO bo : listImageBOs){
					ImageTO imageTo=new ImageTO();
					imageTo.setImageID(bo.getImageID());
					imageTo.setImageName(bo.getImageName());
					imageTo.setImageType(bo.getImageType());
					imageTo.setImageDetails(bo.getImageDetails());
					imageTo.setLocation(bo.getLocation());
					imageTo.setImageID(bo.getImageID());
					imageTo.setOsName(bo.getOsName());
					imageTo.setStatus(bo.getStatus());
					listImageTOs.add(imageTo);
				}
			}
		}
		return listImageTOs;
	}
	public static ImageBO getBOFromTO(ImageTO imageTO) {
					ImageBO imageBo=new ImageBO();
					imageBo.setImageID(imageTO.getImageID());
					imageBo.setImageName(imageTO.getImageName());
					imageBo.setImageType(imageTO.getImageType());
					imageBo.setImageDetails(imageTO.getImageDetails());
					imageBo.setLocation(imageTO.getLocation());
					imageBo.setImageID(imageTO.getImageID());
					imageBo.setOsName(imageTO.getOsName());
					imageBo.setStatus(imageTO.getStatus());
					return imageBo;
	}

}
