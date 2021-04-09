/**
 * 
 */
package com.cognizant.cloudone.to.instance;

import com.cognizant.cloudone.kernel.filter.enums.InstanceImageFilterEnum;
import com.cognizant.cloudone.to.base.LocationTO;

public class OSDescriptorTO extends LocationTO {

	private static final long serialVersionUID = 1L;

	private int supportedMaxCPUs;
	private int recommendedDiskSize;
	private int recommendedMem;
	private int supportedMaxMem;
	private int supportedMinMem;
	private String osFullName;
	private String osFamily;
	private String osId;
	private String osState;
	private int osDetailsId;
	private InstanceImageFilterEnum imageFilterEnum; 
	private String[] ethernetAdapter ;
	private String description;
	private String datacenter;
	
	public int getSupportedMaxCPUs() {
		return supportedMaxCPUs;
	}
	public void setSupportedMaxCPUs(int supportedMaxCPUs) {
		this.supportedMaxCPUs = supportedMaxCPUs;
	}
	public String getOsFullName() {
		return osFullName;
	}
	public void setOsFullName(String osFullName) {
		this.osFullName = osFullName;
	}
	public String getOsFamily() {
		return osFamily;
	}
	public void setOsFamily(String osFamily) {
		this.osFamily = osFamily;
	}
	public int getRecommendedDiskSize() {
		return recommendedDiskSize;
	}
	public void setRecommendedDiskSize(int recommendedDiskSize) {
		this.recommendedDiskSize = recommendedDiskSize;
	}
	public int getRecommendedMem() {
		return recommendedMem;
	}
	public void setRecommendedMem(int recommendedMem) {
		this.recommendedMem = recommendedMem;
	}
	public int getSupportedMaxMem() {
		return supportedMaxMem;
	}
	public void setSupportedMaxMem(int supportedMaxMem) {
		this.supportedMaxMem = supportedMaxMem;
	}
	public int getSupportedMinMem() {
		return supportedMinMem;
	}
	public void setSupportedMinMem(int supportedMinMem) {
		this.supportedMinMem = supportedMinMem;
	}
	public void setOsId(String osId) {
		this.osId = osId;
	}
	public String getOsId() {
		return osId;
	}
	public int getOsDetailsId() {
		return osDetailsId;
	}
	public void setOsDetailsId(int osDetailsId) {
		this.osDetailsId = osDetailsId;
	}
	/**
	 * @param imageFilterEnum the imageFilterEnum to set
	 */
	public void setImageFilterEnum(InstanceImageFilterEnum imageFilterEnum) {
		this.imageFilterEnum = imageFilterEnum;
	}
	/**
	 * @return the imageFilterEnum
	 */
	public InstanceImageFilterEnum getImageFilterEnum() {
		return imageFilterEnum;
	}
	public String[] getEthernetAdapter() {
		return ethernetAdapter;
	}
	public void setEthernetAdapter(String[] ethernetAdapter) {
		this.ethernetAdapter = ethernetAdapter;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOsState() {
		return osState;
	}
	public void setOsState(String osState) {
		this.osState = osState;
	}
	/**
	 * @return the datacenter
	 */
	public String getDatacenter() {
		return datacenter;
	}
	/**
	 * @param datacenter the datacenter to set
	 */
	public void setDatacenter(String datacenter) {
		this.datacenter = datacenter;
	}

	
}
