package com.cognizant.cloudone.ic.images;

import com.cognizant.cloudone.biz.instance.bo.DataCenterBO;
import com.cognizant.cloudone.biz.instance.bo.ImageInstanceBO;
import com.cognizant.cloudone.biz.instance.bo.ImageListContainerBO;
import com.cognizant.cloudone.biz.instance.bo.OSDescriptorBO;
import com.cognizant.cloudone.dal.dbo.DataCenterDetailsDO;
import com.cognizant.cloudone.dal.dbo.OsDetailsDO;
import com.cognizant.cloudone.kernel.exception.DALException;
import com.cognizant.cloudone.kernel.exception.ICException;
import com.cognizant.cloudone.kernel.filter.enums.InstanceImageFilterEnum;
import java.util.List;

public interface IImagesDomain {

	public List<ImageInstanceBO> refreshStandardAMIs(int providerId) throws ICException;
	
	public List<ImageInstanceBO> getCustomAMIs(ImageInstanceBO ImageInstanceBO) throws ICException;
	
	public ImageListContainerBO getAmiListForRegionAndFilters(ImageInstanceBO ImageInstanceBO,List<InstanceImageFilterEnum> amiFilterEnums) throws ICException;
	
	public ImageInstanceBO getAmiByIdFromCloud(ImageInstanceBO ImageInstanceBO) throws ICException;

	public ImageInstanceBO getImageInfoByAmiId(
			ImageInstanceBO imageInstanceBO) throws ICException;
	
	public List<ImageInstanceBO> refreshPublicAMIs(int providerId, List<String> completedRegionList) throws ICException;
	
	public List<ImageInstanceBO> fetchOpenstackImagesbyDCID(ImageInstanceBO imageInstanceBO) throws ICException;

	public List<OSDescriptorBO> getImageListForSelectedRegion(OSDescriptorBO osDescriptorBO, DataCenterBO dataCenterBO) throws ICException;
	
	List<OsDetailsDO> saveListOfOSDetails(List<OSDescriptorBO> listOfOSDescriptorBO, DataCenterBO dataCenterBO)
			throws ICException;

	public List<OSDescriptorBO> fetchOSDetailsList(OSDescriptorBO osDescriptorBO) throws ICException;

	List<DataCenterBO> fetchDataCenters(int cloudId, int providerID) throws ICException;


}
