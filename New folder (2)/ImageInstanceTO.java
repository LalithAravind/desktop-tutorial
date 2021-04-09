package com.cognizant.cloudone.to.instance;

import com.cognizant.cloudone.to.base.LocationTO;



public class ImageInstanceTO extends LocationTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3998770797461059468L;
	private String amiID;
	private String ramDiskID;
	private String kernelID;
	private String manifest;
	private String rootDevice;
	private String operatingSystem;
	private String osState;
	private String name;
	private String description;
	private String architecture;
	private String virtualizationType;
	private String imageType;
	/*private String imagePublic;
	private String imagePrivate;*/
	private String owner;
	private boolean visibility;
	private boolean subnetsConfigured=false;
	private Integer rootVolumeSize;
	private String volumeType;
	private String snapshotId;
	private String rootDeviceName;
	private boolean deleteOnTermination;
	private String publisher;
	private String offer;
	private String sku;
	private String version;
	
	public String getVolumeType() {
		return volumeType;
	}

	public void setVolumeType(String volumeType) {
		this.volumeType = volumeType;
	}

	public String getSnapshotId() {
		return snapshotId;
	}

	public void setSnapshotId(String snapshotId) {
		this.snapshotId = snapshotId;
	}

	public String getRootDeviceName() {
		return rootDeviceName;
	}

	public void setRootDeviceName(String rootDeviceName) {
		this.rootDeviceName = rootDeviceName;
	}

	public boolean isDeleteOnTermination() {
		return deleteOnTermination;
	}

	public void setDeleteOnTermination(boolean deleteOnTermination) {
		this.deleteOnTermination = deleteOnTermination;
	}

	public Integer getRootVolumeSize() {
		return rootVolumeSize;
	}

	public void setRootVolumeSize(Integer rootVolumeSize) {
		this.rootVolumeSize = rootVolumeSize;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	/*public String getImagePublic() {
		return imagePublic;
	}

	public void setImagePublic(String imagePublic) {
		this.imagePublic = imagePublic;
	}

	public String getImagePrivate() {
		return imagePrivate;
	}

	public void setImagePrivate(String imagePrivate) {
		this.imagePrivate = imagePrivate;
	}*/

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	

	public String getArchitecture() {
		return architecture;
	}

	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}

	public String getAmiID() {
		return amiID;
	}

	public void setAmiID(String amiID) {
		this.amiID = amiID;
	}

	public void setRamDiskID(String ramDiskID) {
		this.ramDiskID = ramDiskID;
	}

	public void setKernelID(String kernelID) {
		this.kernelID = kernelID;
	}

	public String getKernelID() {
		return kernelID;
	}

	public String getRamDiskID() {
		return ramDiskID;
	}

	public String getManifest() {
		return manifest;
	}

	public void setManifest(String manifest) {
		this.manifest = manifest;
	}

	public String getRootDevice() {
		return rootDevice;
	}

	public void setRootDevice(String rootDevice) {
		this.rootDevice = rootDevice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}

	public boolean isVisibility() {
		return visibility;
	}

	/**
	 * @param subnetsConfigured the subnetsConfigured to set
	 */
	public void setSubnetsConfigured(boolean subnetsConfigured) {
		this.subnetsConfigured = subnetsConfigured;
	}

	/**
	 * @return the subnetsConfigured
	 */
	public boolean isSubnetsConfigured() {
		return subnetsConfigured;
	}

	public String getVirtualizationType() {
		return virtualizationType;
	}

	public void setVirtualizationType(String virtualizationType) {
		this.virtualizationType = virtualizationType;
	}
	public String getOsState() {
		return osState;
	}

	public void setOsState(String osState) {
		this.osState = osState;
	}

	/**
	 * @return the publisher
	 */
	public String getPublisher() {
		return publisher;
	}

	/**
	 * @param publisher the publisher to set
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	/**
	 * @return the offer
	 */
	public String getOffer() {
		return offer;
	}

	/**
	 * @param offer the offer to set
	 */
	public void setOffer(String offer) {
		this.offer = offer;
	}

	/**
	 * @return the sku
	 */
	public String getSku() {
		return sku;
	}

	/**
	 * @param sku the sku to set
	 */
	public void setSku(String sku) {
		this.sku = sku;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}


}
