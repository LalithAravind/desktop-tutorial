package com.cognizant.cloudone.ic.images.facade;

import com.cognizant.cloudone.biz.base.bo.CloudCredentialsBO;
import com.cognizant.cloudone.biz.instance.bo.DataCenterBO;
import com.cognizant.cloudone.biz.instance.bo.ImageInstanceBO;
import com.cognizant.cloudone.biz.instance.bo.ImageListContainerBO;
import com.cognizant.cloudone.biz.instance.bo.OSDescriptorBO;
import com.cognizant.cloudone.dal.dbo.DataCenterDetailsDO;
import com.cognizant.cloudone.ic.constants.ActionEnum;
import com.cognizant.cloudone.ic.domain.factory.DomainFactory;
import com.cognizant.cloudone.ic.domain.factory.ICObjectEnum;
import com.cognizant.cloudone.ic.images.IImagesDomain;
import com.cognizant.cloudone.ic.instance.provider.ImageDetailsProvider;
import com.cognizant.cloudone.ic.instance.provider.PrerequisiteInfoProvider;
import com.cognizant.cloudone.ic.utils.ErrorUtils;
import com.cognizant.cloudone.kernel.constants.GlobalConstant;
import com.cognizant.cloudone.kernel.exception.ICException;
import com.cognizant.cloudone.kernel.exception.SLException;
import com.cognizant.cloudone.kernel.filter.enums.InstanceImageFilterEnum;
import com.cognizant.cloudone.kernel.logger.CloudOneLogger;
import com.cognizant.cloudone.sl.instances.facade.SLInstancesFacade;
import com.cognizant.cloudone.to.instance.DataCenterTO;
import com.cognizant.cloudone.to.instance.ImageInstanceTO;
import com.cognizant.cloudone.to.instance.ImageListContainerTO;
import com.cognizant.cloudone.to.instance.OSDescriptorTO;
import com.cognizant.cloudone.to.result.ResultTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;

public enum ImagesFacade{
	SINGLETON_INSTANCE;
	
	private static final CloudOneLogger logger = CloudOneLogger.getLogger(ImagesFacade.class.getName());
	private ImagesFacade(){
		//Keep this constructor empty.No shared variables	
	}
	
	public boolean refreshStandardAMIs(int providerId) {
		List<ImageInstanceBO> images = null;
		try {
			logger.info("Fetching standard images...");
			IImagesDomain iImagesDomain = (IImagesDomain) DomainFactory.getInstance(ICObjectEnum.IMAGES);
			images = iImagesDomain.refreshStandardAMIs(providerId);
			logger.info("Standard images fetch complete");
			if(images == null || images.isEmpty()) {
				return false;
			}else{
				return true;
			}
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}
	
	public boolean refreshPublicAMIs(int providerId, List<String> completedRegionList) {
		List<ImageInstanceBO> images = null;
		try {
			logger.info("Fetching public images...");
			IImagesDomain iImagesDomain = (IImagesDomain) DomainFactory.getInstance(ICObjectEnum.IMAGES);
			images = iImagesDomain.refreshPublicAMIs(providerId,completedRegionList);
			logger.info("Public images fetch complete");
			if(images == null || images.isEmpty()) {
				return false;
			}else{
				return true;
			}
		} catch (ICException e) {
			logger.error(e);
			return false;
		}
	}
	
	/*
	 * May be this method does not require authorization
	 */
	@SuppressWarnings("unchecked")
	public ResultTO<ImageInstanceTO> getCustomAMIs(ImageInstanceTO imageInstanceTO) {
		ResultTO<ImageInstanceTO> resultTO = new ResultTO<>();
		List<ImageInstanceTO> images = null;
		List<ImageInstanceBO> imagesBO =  null;
		try {
			logger.info("Fetching custom images...");
			IImagesDomain iImagesDomain = (IImagesDomain) DomainFactory.getInstance(ICObjectEnum.IMAGES);
			imagesBO = iImagesDomain.getCustomAMIs(PrerequisiteInfoProvider.getImageInstanceBOFromTO(imageInstanceTO));
			images =ImageDetailsProvider.getTOFromBO(imagesBO);
			logger.info("Custom images fetch complete");
			if(images == null || images.isEmpty()) {
				resultTO.setResult(Collections.EMPTY_LIST);
			}else{
				resultTO.setResult(images);
			}
		} catch (ICException e) {
			logger.error(e);
			ErrorUtils.populateErrors(e, resultTO, imagesBO, ActionEnum.FETCH_PUBLIC_AMIS);
		}
		return resultTO;
	}
	
	
	/*
	 * May be this method does not require authorization
	 */
	public ImageListContainerTO getAMIList(ImageInstanceTO imageInstanceTO,List<InstanceImageFilterEnum> amiFilterEnums) {
		ImageListContainerTO imageListTOContainer = new ImageListContainerTO();
		try {
			IImagesDomain iImagesDomain = (IImagesDomain) DomainFactory.getInstance(ICObjectEnum.IMAGES);
			ImageListContainerBO imageListBOContainer = iImagesDomain.getAmiListForRegionAndFilters((PrerequisiteInfoProvider.getImageInstanceBOFromTO(imageInstanceTO)), amiFilterEnums);
			imageListTOContainer = ImageDetailsProvider.getTOFromBO(imageListBOContainer);
		} catch (ICException e) {
			logger.error(e);
		}
		return imageListTOContainer;
	}
	
	/*
	 * May be this method does not require authorization
	 */
	@SuppressWarnings("unchecked")
	public ResultTO<ImageInstanceTO> getAmiByIdFromCloud(ImageInstanceTO imageInstanceTO) {
		ResultTO<ImageInstanceTO> resultTO = new ResultTO<>();
		ImageInstanceTO imageTO = null;
		ImageInstanceBO imagesBO =  null;
		try {
			IImagesDomain iImagesDomain = (IImagesDomain) DomainFactory.getInstance(ICObjectEnum.IMAGES);
			imagesBO = iImagesDomain.getAmiByIdFromCloud(PrerequisiteInfoProvider.getImageInstanceBOFromTO(imageInstanceTO));
			imageTO = ImageDetailsProvider.getTOFromBO(imagesBO);
			if(imageTO == null) {
				resultTO.setResult(Collections.EMPTY_LIST);
			}else{
				List<ImageInstanceTO> images = new ArrayList<>();
				images.add(imageTO);
				resultTO.setResult(images);
			}
		} catch (ICException e) {
			logger.error(e);
			ErrorUtils.populateErrors(e, resultTO, imagesBO, ActionEnum.FETCH_PUBLIC_AMIS);
		}
		return resultTO;
	}

	/*
	 *  getImageInfoByAmiImageId 
	 */
	@SuppressWarnings("unchecked")
	public ResultTO<ImageInstanceTO> getImageInfoByAmiId(ImageInstanceTO imageInstanceTO) {
		ResultTO<ImageInstanceTO> resultTO = new ResultTO<>();
		ImageInstanceTO imageTO = null;
		ImageInstanceBO imagesBO =  null;
		try {
			IImagesDomain iImagesDomain = (IImagesDomain) DomainFactory.getInstance(ICObjectEnum.IMAGES);
			imagesBO = iImagesDomain.getImageInfoByAmiId(PrerequisiteInfoProvider.getImageInstanceBOFromTO(imageInstanceTO));
			imageTO = ImageDetailsProvider.getTOFromBO(imagesBO);
			if(imageTO == null) {
				resultTO.setResult(Collections.EMPTY_LIST);
			}else{
				List<ImageInstanceTO> images = new ArrayList<>();
				images.add(imageTO);
				resultTO.setResult(images);
			}
		} catch (ICException e) {
			logger.error(e);
			ErrorUtils.populateErrors(e, resultTO, imagesBO, ActionEnum.FETCH_PUBLIC_AMIS);
		}
		return resultTO;
	}
	
	public ResultTO<ImageInstanceTO> getOpenStackImagesByDCID(
			ImageInstanceTO image) {
		IImagesDomain iImagesDomain = (IImagesDomain) DomainFactory.getInstance(ICObjectEnum.IMAGES);
		ResultTO<ImageInstanceTO> resultTO = new ResultTO<>();
		try {
			List<ImageInstanceBO> images = iImagesDomain
					.fetchOpenstackImagesbyDCID(PrerequisiteInfoProvider
							.getImageInstanceBOFromTO(image));
			if (images != null && !images.isEmpty()) {
				List<ImageInstanceTO> imagesTO = new ArrayList<>();
				for (ImageInstanceBO imgBO : images) {
					imagesTO.add(ImageDetailsProvider.getTOFromBO(imgBO));
				}
				resultTO.setResult(imagesTO);
			}
		} catch (ICException e) {
			logger.error(e);
		}
		return resultTO;

	}
	
	public ResultTO<DataCenterTO> getDatacenterDetails(OSDescriptorTO osDescriptorTO){
		IImagesDomain iImagesDomain = (IImagesDomain) DomainFactory.getInstance(ICObjectEnum.IMAGES);
		ResultTO<DataCenterTO> resultTO = new ResultTO<>();
		try {
			List<DataCenterBO> datacenters = iImagesDomain.fetchDataCenters(osDescriptorTO.getCloudId(), osDescriptorTO.getProviderID());
			if (datacenters != null && !datacenters.isEmpty()) {
				List<DataCenterTO> dataCenterList = new ArrayList<>();
				for (DataCenterBO datacenter : datacenters) {
					dataCenterList.add(getDCTOFromBO(datacenter));
				}
				resultTO.setResult(dataCenterList);
			}
		} catch (ICException e) {
			logger.error(e);
		}
		return resultTO;
	}
	
	private DataCenterTO getDCTOFromBO(DataCenterBO dataCenterBO) {
		DataCenterTO dcTO = new DataCenterTO();
		BeanUtils.copyProperties(dataCenterBO, dcTO);
		dcTO.setId(dataCenterBO.getDcId());
		dcTO.setName(dataCenterBO.getDcName());
		dcTO.setLocation(dataCenterBO.getLocation());
		dcTO.setProfile(dataCenterBO.getProfile());
		dcTO.setParent(dataCenterBO.getParent());
		return dcTO;
	}
	
	private static DataCenterBO getDCBOFromTO(DataCenterTO dataCenterTO) {
		DataCenterBO dcBO = new DataCenterBO();
		BeanUtils.copyProperties(dataCenterTO, dcBO);
		dcBO.setDcId(dataCenterTO.getId());
		dcBO.setDcName(dataCenterTO.getName());
		dcBO.setLocation(dataCenterTO.getLocation());
		dcBO.setProfile(dataCenterTO.getProfile());
		dcBO.setParent(dataCenterTO.getParent());
		return dcBO;
	}
	
	public List<OSDescriptorTO> populatePublisherListForSelectedRegion(OSDescriptorTO osDescriptorTO,DataCenterTO dataCenterTO){
		IImagesDomain iImagesDomain = (IImagesDomain) DomainFactory.getInstance(ICObjectEnum.IMAGES);
		List<OSDescriptorTO> publisherNames = null;
			try {
			DataCenterBO dataCenterBO=getDCBOFromTO(dataCenterTO);
			OSDescriptorBO osDescriptorBO=new OSDescriptorBO();
			osDescriptorBO.setCloudId(osDescriptorTO.getCloudId());
			osDescriptorBO.setProviderID(osDescriptorTO.getProviderID());
			osDescriptorBO.setProviderType(osDescriptorTO.getProviderType());
			osDescriptorBO.setProviderName(osDescriptorTO.getProviderName());
			osDescriptorBO.setDatacenter(dataCenterBO.getLocation());
			osDescriptorBO.setOsFamily(GlobalConstant.PUBLISHER);
			List<OSDescriptorBO> publisherBOs = iImagesDomain.getImageListForSelectedRegion(osDescriptorBO, dataCenterBO);
			if(publisherBOs!=null){
				publisherNames=new ArrayList<>();
				for(OSDescriptorBO osDescBO:publisherBOs){
					OSDescriptorTO osDescTO=new OSDescriptorTO();
					BeanUtils.copyProperties(osDescBO, osDescTO);
					publisherNames.add(osDescTO);
				}
			}
		} catch (ICException e) {
			logger.error("Could not fetch publisher details for the region {}", dataCenterTO.getName());			
		}
		return publisherNames;
	}
	
	public List<OSDescriptorTO> populateOfferListForSelectedPublisher(OSDescriptorTO osDescriptorTO,DataCenterTO dataCenterTO){
		IImagesDomain iImagesDomain = (IImagesDomain) DomainFactory.getInstance(ICObjectEnum.IMAGES);
		List<OSDescriptorTO> offerNames = null;
			try {
			DataCenterBO dataCenterBO=getDCBOFromTO(dataCenterTO);
			OSDescriptorBO osDescriptorBO=new OSDescriptorBO();
			osDescriptorBO.setCloudId(osDescriptorTO.getCloudId());
			osDescriptorBO.setProviderID(osDescriptorTO.getProviderID());
			osDescriptorBO.setProviderType(osDescriptorTO.getProviderType());
			osDescriptorBO.setProviderName(osDescriptorTO.getProviderName());
			osDescriptorBO.setDatacenter(dataCenterBO.getLocation());
			osDescriptorBO.setOsFamily(GlobalConstant.OFFER);
			osDescriptorBO.setOsFullName(osDescriptorTO.getOsFullName());
			List<OSDescriptorBO> offers = iImagesDomain.getImageListForSelectedRegion(osDescriptorBO, dataCenterBO);			
			if(offers!=null){
				offerNames=new ArrayList<>();
				for(OSDescriptorBO osDescBO:offers){
					OSDescriptorTO osDescTO=new OSDescriptorTO();
					BeanUtils.copyProperties(osDescBO, osDescTO);
					offerNames.add(osDescTO);
				}
			}
		} catch (ICException e) {
			logger.error("Could not fetch offer details for the region {}", dataCenterTO.getName());			
		}
		return offerNames;
	}
	
	public List<OSDescriptorTO> populateSkus(OSDescriptorTO osDescriptorTO,DataCenterTO dataCenterTO){
		IImagesDomain iImagesDomain = (IImagesDomain) DomainFactory.getInstance(ICObjectEnum.IMAGES);
		List<OSDescriptorTO> imageList = null;
			try {
			DataCenterBO dataCenterBO=getDCBOFromTO(dataCenterTO);
			OSDescriptorBO osDescriptorBO=new OSDescriptorBO();
			osDescriptorBO.setCloudId(osDescriptorTO.getCloudId());
			osDescriptorBO.setProviderID(osDescriptorTO.getProviderID());
			osDescriptorBO.setProviderType(osDescriptorTO.getProviderType());
			osDescriptorBO.setProviderName(osDescriptorTO.getProviderName());
			osDescriptorBO.setDatacenter(dataCenterBO.getLocation());
			osDescriptorBO.setOsFamily(GlobalConstant.SKUS);
			osDescriptorBO.setOsFullName(osDescriptorTO.getOsFullName());
			List<OSDescriptorBO> images = iImagesDomain.getImageListForSelectedRegion(osDescriptorBO, dataCenterBO);
			if(images!=null){
				imageList=new ArrayList<>();
				for(OSDescriptorBO osDescBO:images){
					OSDescriptorTO osDescTO=new OSDescriptorTO();
					BeanUtils.copyProperties(osDescBO, osDescTO);
					imageList.add(osDescTO);
				}
			}
		} catch (ICException e) {
			logger.error("Could not fetch skus details for the region {}", dataCenterTO.getName());			
		}
		return imageList;
	}
}
