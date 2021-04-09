package com.cognizant.cloudone.ui.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cognizant.cloudone.kernel.constants.AMIDetailsEnum;
import com.cognizant.cloudone.kernel.constants.InstanceTypesEnum;
import com.cognizant.cloudone.kernel.filter.enums.FilterTypeEnum;
import com.cognizant.cloudone.to.instance.ImageInstanceTO;

public class CreateInstanceControllerHelper {
	
	/**
	 *  
	 * 
	 * @param newImageList
	 * @return
	 */
	public  static List<ImageInstanceTO> copyList(List<ImageInstanceTO> newImageList){
		List<ImageInstanceTO> tempList = new ArrayList<>(newImageList);
		return tempList;
		
	}
	
	
	/**
	 * Filter the events by Group
	 * 
	 * @param eventList
	 * @param groupValues
	 * @return
	 */
	public static List<ImageInstanceTO> filterValue( List<ImageInstanceTO> imageList,  List<String> values,int size, FilterTypeEnum filterEnum){
		
		List<ImageInstanceTO> newList = new ArrayList<>();
		boolean shouldFilter = true;
		
		if(values == null ){
			shouldFilter = false;
		}
		if(values != null && (values.isEmpty() || (values.size() == size))){
			shouldFilter = false;
		}
		
		if (shouldFilter){
			for(ImageInstanceTO imageInstanceTO : imageList){
				for (String value : values ){
					if(value != null && checkValue(filterEnum,imageInstanceTO, value)){
						newList.add(imageInstanceTO);
						break;
					}
				}
			}
		}else{
			newList = copyList(imageList);
		}
		return newList;
		
	}//end of filterValue
	

	/**
	 * 
	 * 
	 * @param filterEnum
	 * @param eventTO
	 * @param value
	 * @return
	 */
	private static boolean checkValue(FilterTypeEnum filterEnum, ImageInstanceTO imageInstanceTO, String value){
		
		switch (filterEnum.getId()){
		
		case 8:
			if(value.equalsIgnoreCase(imageInstanceTO.getRootDevice())){
				return true;
			}
			break;
			
		case 9:
			if (value.equalsIgnoreCase(imageInstanceTO.getOperatingSystem())){
				return true;
			}
			break;
		}
		return false;
	}
	
	public static List<String> buildInstanceTypeList(
			ImageInstanceTO imageInstance, boolean buildStaticList) {

		List<String> instanceTypes = new ArrayList<>();
		Set<Integer> instanceTypesOrder = new HashSet<>();

		if (imageInstance != null && !buildStaticList) {
			if (AMIDetailsEnum.AMI_ARCH_I386.getID().equals(
					imageInstance.getArchitecture())) {
				if (AMIDetailsEnum.AMI_ROOTDEVICE_EBS.getID().equalsIgnoreCase(
						imageInstance.getRootDevice())) {
					instanceTypesOrder.add(InstanceTypesEnum.T1_MICRO.getOrderId());
				}
				instanceTypesOrder.add(InstanceTypesEnum.M1_SMALL.getOrderId());
				instanceTypesOrder.add(InstanceTypesEnum.M1_MEDIUM.getOrderId());
				instanceTypesOrder.add(InstanceTypesEnum.C1_MEDIUM.getOrderId());
				instanceTypesOrder.add(InstanceTypesEnum.C3_LARGE.getOrderId());
			} else if (AMIDetailsEnum.AMI_ARCH_X86_64.getID().equals(
					imageInstance.getArchitecture())) {
				if (AMIDetailsEnum.AMI_ROOTDEVICE_EBS.getID().equalsIgnoreCase(
						imageInstance.getRootDevice())) {
					if (AMIDetailsEnum.VIRTUALIZATIONTYPE_PARAVIRTUAL
							.getID()
							.equalsIgnoreCase(imageInstance.getVirtualizationType())){
					instanceTypesOrder.add(InstanceTypesEnum.T1_MICRO.getOrderId());
					}
					if (AMIDetailsEnum.VIRTUALIZATIONTYPE_HVM
							.getID()
							.equalsIgnoreCase(imageInstance.getVirtualizationType())) {
					instanceTypesOrder.add(InstanceTypesEnum.T2_MICRO.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.T2_SMALL.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.T2_MEDIUM.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.T2_LARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.M4_LARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.M4_XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.M4_2XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.M4_4XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.M4_10XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.C4_LARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.C4_XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.C4_2XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.C4_4XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.C4_8XLARGE.getOrderId());
					
					}
				}
				instanceTypesOrder.add(InstanceTypesEnum.M3_MEDIUM.getOrderId());
				instanceTypesOrder.add(InstanceTypesEnum.M3_LARGE.getOrderId());
				instanceTypesOrder.add(InstanceTypesEnum.M3_XLARGE.getOrderId());
				instanceTypesOrder.add(InstanceTypesEnum.M3_2XLARGE.getOrderId());
				instanceTypesOrder.add(InstanceTypesEnum.C3_LARGE.getOrderId());
				instanceTypesOrder.add(InstanceTypesEnum.C3_XLARGE.getOrderId());
				instanceTypesOrder.add(InstanceTypesEnum.C3_2XLARGE.getOrderId());
				instanceTypesOrder.add(InstanceTypesEnum.C3_4XLARGE.getOrderId());
				instanceTypesOrder.add(InstanceTypesEnum.C3_8XLARGE.getOrderId());
				if (AMIDetailsEnum.VIRTUALIZATIONTYPE_HVM
						.getID()
						.equalsIgnoreCase(imageInstance.getVirtualizationType())) {
					instanceTypesOrder.add(InstanceTypesEnum.HI1_4XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.G2_2XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.G2_8XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.I2_XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.I2_2XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.I2_4XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.I2_8XLARGE.getOrderId());
					
					instanceTypesOrder.add(InstanceTypesEnum.D2_XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.D2_2XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.D2_4XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.D2_8XLARGE.getOrderId());
					
					instanceTypesOrder.add(InstanceTypesEnum.R3_LARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.R3_XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.R3_2XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.R3_4XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.R3_8XLARGE.getOrderId());
					
				} else if (AMIDetailsEnum.VIRTUALIZATIONTYPE_PARAVIRTUAL
						.getID().equalsIgnoreCase(
								imageInstance.getVirtualizationType())) {
					instanceTypesOrder.add(InstanceTypesEnum.M1_SMALL.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.M1_MEDIUM.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.M1_LARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.M1_XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.M2_XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.M2_2XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.M2_4XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.C1_MEDIUM.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.C1_XLARGE.getOrderId());
					instanceTypesOrder.add(InstanceTypesEnum.HI1_4XLARGE.getOrderId());
				}
				instanceTypesOrder.add(InstanceTypesEnum.HS1_8XLARGE.getOrderId());
				instanceTypesOrder.add(InstanceTypesEnum.CR1_8XLARGE.getOrderId());
			} else {
				for (InstanceTypesEnum instanceTypesEnum : InstanceTypesEnum.values()) {
					if (!InstanceTypesEnum.UNKNOWN.equals(instanceTypesEnum)) {
                                            instanceTypesOrder.add(instanceTypesEnum.getOrderId());
                    }
				}
			}
		} else {
			for (InstanceTypesEnum instanceTypesEnum : InstanceTypesEnum.values()) {
				if (!InstanceTypesEnum.UNKNOWN.equals(instanceTypesEnum)) {
                                    instanceTypesOrder.add(instanceTypesEnum.getOrderId());
                }
			}
		}
		ArrayList<Integer> instanceTypesOrdersTmp=new ArrayList<>(instanceTypesOrder); 
		Collections.sort(instanceTypesOrdersTmp);
		for (Integer orderId : instanceTypesOrdersTmp) {
			instanceTypes.add(InstanceTypesEnum.getEnumByOrderId(orderId).getId());
		}
		return instanceTypes;
	}
	
	public static boolean buildEncryptionSupportedInstanceTypeList(String instanceType){
		List<String> encryptSupportedInstanceTypes = new ArrayList<>();
		//General Purpose
		encryptSupportedInstanceTypes.add(InstanceTypesEnum.M3_MEDIUM.getId());
		encryptSupportedInstanceTypes.add(InstanceTypesEnum.M3_LARGE.getId());
		encryptSupportedInstanceTypes.add(InstanceTypesEnum.M3_XLARGE.getId());
		encryptSupportedInstanceTypes.add(InstanceTypesEnum.M3_2XLARGE.getId());
		
		//Compute Optimized
		encryptSupportedInstanceTypes.add(InstanceTypesEnum.C3_LARGE.getId());
		encryptSupportedInstanceTypes.add(InstanceTypesEnum.C3_XLARGE.getId());
		encryptSupportedInstanceTypes.add(InstanceTypesEnum.C3_2XLARGE.getId());
		encryptSupportedInstanceTypes.add(InstanceTypesEnum.C3_4XLARGE.getId());
		encryptSupportedInstanceTypes.add(InstanceTypesEnum.C3_8XLARGE.getId());
		
		//Memory Optimized
		encryptSupportedInstanceTypes.add(InstanceTypesEnum.CR1_8XLARGE.getId());
	
		//Storage Optimized
		encryptSupportedInstanceTypes.add(InstanceTypesEnum.I2_XLARGE.getId());
		encryptSupportedInstanceTypes.add(InstanceTypesEnum.I2_2XLARGE.getId());
		encryptSupportedInstanceTypes.add(InstanceTypesEnum.I2_4XLARGE.getId());
		encryptSupportedInstanceTypes.add(InstanceTypesEnum.I2_8XLARGE.getId());
		
		//GPU Instances
		encryptSupportedInstanceTypes.add(InstanceTypesEnum.G2_2XLARGE.getId());
		if(encryptSupportedInstanceTypes.contains(instanceType)){
			return true;
		}else{
			return false;
		}
		
	}
}
