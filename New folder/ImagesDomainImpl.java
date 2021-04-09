package com.cognizant.cloudone.ic.images.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.cognizant.cloudone.biz.base.bo.BaseBO;
import com.cognizant.cloudone.biz.base.bo.CloudCredentialsBO;
import com.cognizant.cloudone.biz.base.bo.LocationBO;
import com.cognizant.cloudone.biz.instance.bo.DataCenterBO;
import com.cognizant.cloudone.biz.instance.bo.ImageInstanceBO;
import com.cognizant.cloudone.biz.instance.bo.ImageListContainerBO;
import com.cognizant.cloudone.biz.instance.bo.OSDescriptorBO;
import com.cognizant.cloudone.biz.instance.bo.PrerequisiteInfoInstanceBO;
import com.cognizant.cloudone.biz.platforms.PlatformBO;
import com.cognizant.cloudone.dal.constant.DatabaseSQLConstant;
import com.cognizant.cloudone.dal.dao.CloudMasterDAO;
import com.cognizant.cloudone.dal.dao.DataCenterDetailsDAO;
import com.cognizant.cloudone.dal.dao.ImageDetailsDAO;
import com.cognizant.cloudone.dal.dao.OsDetailsDAO;
import com.cognizant.cloudone.dal.dao.ProviderMasterDAO;
import com.cognizant.cloudone.dal.dao.factory.CloudOneDAOFactory;
import com.cognizant.cloudone.dal.dbo.CloudMasterDO;
import com.cognizant.cloudone.dal.dbo.DataCenterDetailsDO;
import com.cognizant.cloudone.dal.dbo.ImageDetailsDO;
import com.cognizant.cloudone.dal.dbo.OsDetailsDO;
import com.cognizant.cloudone.dal.dbo.ProviderMasterDO;
import com.cognizant.cloudone.dal.dbo.VPCDetailsDO;
import com.cognizant.cloudone.dal.wrapper.CloudOneResultSet;
import com.cognizant.cloudone.ic.base.impl.BaseDomainImpl;
import com.cognizant.cloudone.ic.base.impl.CloudOneDAOFactoryBuilder;
import com.cognizant.cloudone.ic.images.IImagesDomain;
import com.cognizant.cloudone.ic.instance.provider.ImageDetailsProvider;
import com.cognizant.cloudone.ic.instance.provider.OSResourcesDetailsProvider;
import com.cognizant.cloudone.ic.platforms.IPlatformsDomain;
import com.cognizant.cloudone.ic.platforms.impl.PlatformsDomainImpl;
import com.cognizant.cloudone.ic.platforms.provider.PlatformsProvider;
import com.cognizant.cloudone.ic.utils.ICUtils;
import com.cognizant.cloudone.kernel.constants.GlobalConstant;
import com.cognizant.cloudone.kernel.constants.ProviderTypeEnum;
import com.cognizant.cloudone.kernel.exception.DALException;
import com.cognizant.cloudone.kernel.exception.ICException;
import com.cognizant.cloudone.kernel.exception.SLException;
import com.cognizant.cloudone.kernel.filter.enums.InstanceImageFilterEnum;
import com.cognizant.cloudone.kernel.logger.CloudOneLogger;
import com.cognizant.cloudone.kernel.util.StopWatch;
import com.cognizant.cloudone.sl.instances.facade.SLInstancesFacade;

public class ImagesDomainImpl extends BaseDomainImpl implements IImagesDomain {

	private static final CloudOneLogger logger = CloudOneLogger.getLogger(ImagesDomainImpl.class.getName());

	private final String GET_PUBLIC_IMAGES = "public";
	private final String GET_PRIVATE_IMAGES = "private";


	/**
	 * Refreshes the standard AMIs
	 */
	public List<ImageInstanceBO> refreshStandardAMIs(int providerId) throws ICException {

		try{
			//Clouds to be queried for images
			String[] providerTypes = {ProviderTypeEnum.EUCALYPTUS.getType(), ProviderTypeEnum.AMAZON.getType()};
			return getAllImagesFromProviders(providerTypes,providerId);
		}catch ( Exception e) {
			throw new ICException(e);
		}
	}

	/**
	 * Refreshes the standard AMIs
	 */
	public List<ImageInstanceBO> refreshPublicAMIs(int providerId, List<String> completedRegionList) throws ICException {
		try{
			//Clouds to be queried for images
			PlatformBO platformBO   = getPlatformBO(providerId);
			List<ImageInstanceBO> imageList = getPublicImagesFromCloud(platformBO, completedRegionList,providerId);
			logger.info("public imageList size : {}",imageList.size());
			return imageList;

		}catch ( DALException | SLException e) {
			throw new ICException(e);
		}
	}


	@Transactional
	private CloudMasterDO getCloudMasterDO(int providerId) {
		CloudOneDAOFactory daoFactory = CloudOneDAOFactoryBuilder.getFactory();
		ProviderMasterDAO providerMasterDAO = daoFactory.getProviderMasterDAO();
		ProviderMasterDO providerMasterDO = providerMasterDAO.get(ProviderMasterDO.class, providerId);
		return providerMasterDO.getCloudMasterDO();

	}

	private List<ImageInstanceBO> getPublicImagesFromCloud(PlatformBO platformBO,List<String> completedRegionList, int providerId) {
		List<ImageInstanceBO> imageList = new ArrayList<>();
		try {
			CloudCredentialsBO credBO = new CloudCredentialsBO();
			credBO.setAccessID(platformBO.getAccessID());
			credBO.setSecretKey(platformBO.getSecretKey());
			credBO.setUrl(platformBO.getCloudURL());						
			if(credBO.getAccessID() != null && !credBO.getAccessID().isEmpty()){
				//Get private images from all datacenters.
				imageList = getImagesForAllDataCenters(platformBO, credBO, this.GET_PUBLIC_IMAGES,completedRegionList);
				logger.info("public imageList size in getAllImagesFromProviders for cloudid,providerid {} is : {}",platformBO.getCloudId() + "----" + providerId,imageList.size());
			}							
		} catch (DALException | SLException e) {
			logger.error("Could not fetch public images for providerMasterDO {}",platformBO.getProviderName());
			logger.error(e);//Do not remove this logger. This is to trace image fetch failure reason, if any.
			//Do not throw back errors here. Let the flow continue for other providers.
		}
		return imageList;
	}

	/**
	 * Refreshes the custom AMIs
	 */
	@Transactional
	public List<ImageInstanceBO> getCustomAMIs(ImageInstanceBO imageInstanceBO) throws ICException {
		CloudOneDAOFactory daoFactory = CloudOneDAOFactoryBuilder.getFactory();
		try{
			//Clouds to be queried for images
			String[] providerTypes = {ProviderTypeEnum.EUCALYPTUS.getType(), ProviderTypeEnum.AMAZON.getType()};

			List<ImageInstanceBO> imageList = new ArrayList<>();

			imageList = this.getPrivateImagesFromProviders(imageInstanceBO, daoFactory, providerTypes, imageList);
			logger.info("private imageList size : {}",imageList.size());

			return imageList;

		}catch ( DALException | SLException e) {
			throw new ICException(e);
		}
	}

	/*************************************************************************************************************************
	 * This method fetches the private images from the providers of the respective clouds.
	 * Details fetched for every user registered with a provider with a unique owner-id across all available datacenters.
	 * 
	 * @param list->      Contains list of all the providers available across the clouds. This list will be filtered to find details corresponding to
	 * 				      the provider types listed in String[] providers array.
	 * @param providers->    Providers for which images have to be fetched.
	 * @param imageList-> Holds the final result.
	 * @return
	 * @throws Exception
	 **************************************************************************************************************************/
	@Transactional
	public List<ImageInstanceBO> getPrivateImagesFromProviders(ImageInstanceBO imageInstanceBO, CloudOneDAOFactory factory, 
			String[] providerTypes, List<ImageInstanceBO> imageList) throws DALException, SLException{
		CloudMasterDAO cloudMasterDAO = factory.getCloudMasterDAO();
		CloudMasterDO cloudMasterDO = cloudMasterDAO.get(CloudMasterDO.class, imageInstanceBO.getCloudId());	
		ProviderMasterDO providerMasterDO = cloudMasterDAO.get(ProviderMasterDO.class, imageInstanceBO.getProviderID());
		DataCenterDetailsDAO dataCenterDetailsDAO = factory.getDataCenterDetailsDAO();
		DataCenterDetailsDO dataCenterDO = new DataCenterDetailsDO();
		dataCenterDO.setCloudMasterDO(cloudMasterDO);
		dataCenterDO.setProviderMasterDO(providerMasterDO);
		dataCenterDO.setDataCenterName(imageInstanceBO.getDatacenterName());
		DataCenterDetailsDO dataCenterDetailsDO = dataCenterDetailsDAO.fetchByCloudIDProviderIDDataCenterName(dataCenterDO);
		try {
			//tx = imageDetailsDAO.getTransaction();
			try {
				CloudCredentialsBO credBO = new CloudCredentialsBO();
				credBO.setAccessID(providerMasterDO.getAccessID());
				credBO.setSecretKey(providerMasterDO.getSecretKey());
				credBO.setUrl(providerMasterDO.getCloudURL());

				if(credBO.getAccessID() != null && !credBO.getAccessID().isEmpty()){
					//Get private images from all datacenters.
					imageList = getCustomImagesForSpecificDataCenter(cloudMasterDO,providerMasterDO, dataCenterDetailsDO, imageList, credBO, this.GET_PRIVATE_IMAGES);
					logger.info("imageList size in getPrivateImagesFromClouds for cloudid {}",cloudMasterDO.getCloudID()+" is : "+imageList.size());
				}	
			} catch (    DALException | SLException e) {
				logger.error("Could not fetch private images for providerMasterDO {}",providerMasterDO.getProviderName());
				logger.error(e);//Do not remove this logger. This is to trace image fetch failure reason, if any.
				//Do not throw back errors here. Let the flow continue for other providers.
			}
		} catch (Exception e) {
			//ICUtils.rollbackTransaction(tx);
		}
		return imageList;
	}


	/*************************************************************************************************************************
	 * This method fetches ALL {Private/public/Self} images from providers of the respective clouds. Since public images will be common across all the clouds, result-set for 
	 * one cloud is used for all the clouds of same type.
	 * @param providerId 
	 * 
	 * @param list->      Contains list of all the providers available. This list will be filtered to find details corresponding to
	 * 				      the provider types listed in String[] providerTypes array.
	 * @param providers->    Providers for which images have to be fetched.
	 * @param imageList-> Holds the final result.
	 * @return
	 * @throws Exception
	 **************************************************************************************************************************/
	public List<ImageInstanceBO> getAllImagesFromProviders(String[] providerTypes, int providerId) throws DALException, SLException{
		List<ImageInstanceBO> imageList = new ArrayList<>();
		PlatformBO platformBO = getPlatformBO(providerId);

		try {
			CloudCredentialsBO credBO = new CloudCredentialsBO();
			credBO.setAccessID(platformBO.getAccessID());
			credBO.setSecretKey(platformBO.getSecretKey());
			credBO.setUrl(platformBO.getCloudURL());						
			if(credBO.getAccessID() != null && !credBO.getAccessID().isEmpty()){
				//Get private images from all datacenters.
				imageList = getImagesForAllDataCenters(platformBO, credBO, this.GET_PRIVATE_IMAGES);
				logger.info("private imageList size in getAllImagesFromProviders for cloudid,providerid {} is : {}",platformBO.getCloudId() + "---" + providerId,imageList.size());
			}							
		} catch (    DALException | SLException e) {
			logger.error("Could not fetch public images for providerMasterDO {}",platformBO.getProviderName());
			logger.error(e);//Do not remove this logger. This is to trace image fetch failure reason, if any.
			//Do not throw back errors here. Let the flow continue for other providers.
		}

		return imageList;
	}

	@Transactional
	public PlatformBO getPlatformBO(int providerId) throws DALException, SLException{
		ProviderMasterDAO providerMasterDAO = CloudOneDAOFactoryBuilder.getFactory().getProviderMasterDAO();
		PlatformBO platformBO = null;
		try {
			ProviderMasterDO providerMasterDO = providerMasterDAO.get(ProviderMasterDO.class, providerId);
			platformBO = getProviderBOFromDO(providerMasterDO);
		} catch(Exception e){
			throw (new DALException(e));
		}
		return platformBO;
	}

	/***********************************************************************************************************************
	 * 
	 * @param cloudId->      Cloud-Id to fetch all the corresponding data-centers.
	 * @param imageList->    Hold the final result.
	 * @param credBO->       User-credentials to access the cloud.
	 * @param typeOfImages-> Which type of images to be fetched? Public: Private
	 * @return
	 * @throws Exception
	 ***********************************************************************************************************************/
	public List<ImageInstanceBO> getImagesForAllDataCenters(PlatformBO platformBO,
			CloudCredentialsBO credBO, String typeOfImages) throws DALException, SLException{
		List<ImageInstanceBO> imageList = new ArrayList<>();
		List<DataCenterDetailsDO> dcs = fetchDataCenters(platformBO);
		StopWatch sWatch = new StopWatch();		
		if(dcs != null) {
			for(DataCenterDetailsDO dataCenterDetailsDO : dcs) {		
				logger.info("Fetching standard images for region : {} in cloud id {}",dataCenterDetailsDO.getDataCenterName(),platformBO.getCloudId());
				LocationBO locationBO = new LocationBO();
				locationBO.setProviderType(platformBO.getProviderType());
				locationBO.setDatacenterName(dataCenterDetailsDO.getDataCenterName());
				locationBO.setDatacenterID(dataCenterDetailsDO.getDataCenterId());
				List<ImageInstanceBO> imageInstanceBOs = new ArrayList<>();				
				if(typeOfImages != null && typeOfImages.equalsIgnoreCase(this.GET_PRIVATE_IMAGES)) {
					imageInstanceBOs = SLInstancesFacade.SINGLETON_INSTANCE.getPrivateImagesFromCloud(locationBO, credBO);
				} else if (typeOfImages != null && typeOfImages.equalsIgnoreCase(this.GET_PUBLIC_IMAGES)) {	
					imageInstanceBOs = SLInstancesFacade.SINGLETON_INSTANCE.getPublicImagesFromCloud(locationBO, credBO);
				} else {
					imageInstanceBOs = SLInstancesFacade.SINGLETON_INSTANCE.getPrivateImagesFromCloud(locationBO, credBO);
				}				
				sWatch.start();
				if(typeOfImages != null && typeOfImages.equalsIgnoreCase(this.GET_PUBLIC_IMAGES)) {
					refreshPublicImagesOnDB(imageInstanceBOs,dataCenterDetailsDO);
				} else {
					refreshPrivateImagesOnDB(imageInstanceBOs,dataCenterDetailsDO);
				}
				sWatch.stop();
				logger.debug("Time taken to refreshImagesOnDB {} ms",sWatch.getElapsedTimeInMilliSeconds());
				imageList.addAll(imageInstanceBOs);
			}
		}
		return imageList;
	}


	/***********************************************************************************************************************
	 * 
	 * @param cloudId->      Cloud-Id to fetch all the corresponding data-centers.
	 * @param imageList->    Hold the final result.
	 * @param credBO->       User-credentials to access the cloud.
	 * @param typeOfImages-> Which type of images to be fetched? Public: Private
	 * @param completedRegionList-> List to avoid duplicate cloud calls
	 * @return
	 * @throws Exception
	 ***********************************************************************************************************************/
	public List<ImageInstanceBO> getImagesForAllDataCenters(PlatformBO platformBO,CloudCredentialsBO credBO, String typeOfImages,List<String> completedRegionList) throws DALException, SLException{
		List<DataCenterDetailsDO> dcs = this.fetchDataCenters(platformBO);
		List<ImageInstanceBO> imageList = new ArrayList<>();
		if(dcs != null) {

			for(DataCenterDetailsDO dataCenterDetailsDO : dcs) {				
				logger.info("Fetching standard images for region : {} in cloud id {}",dataCenterDetailsDO.getDataCenterName(),platformBO.getCloudId());
				LocationBO locationBO = new LocationBO();
				locationBO.setProviderType(platformBO.getProviderType());
				locationBO.setDatacenterName(dataCenterDetailsDO.getDataCenterName());
				locationBO.setDatacenterID(dataCenterDetailsDO.getDataCenterId());
				List<ImageInstanceBO> imageInstanceBOs = new ArrayList<>();				
				if(typeOfImages != null && typeOfImages.equalsIgnoreCase(this.GET_PRIVATE_IMAGES)) {
					imageInstanceBOs = SLInstancesFacade.SINGLETON_INSTANCE.getPrivateImagesFromCloud(locationBO, credBO);
					
					refreshPrivateImagesOnDB(imageInstanceBOs,dataCenterDetailsDO);
				} else if (typeOfImages != null && typeOfImages.equalsIgnoreCase(this.GET_PUBLIC_IMAGES)) {
					if(!completedRegionList.contains(dataCenterDetailsDO.getDataCenterName())) {
						imageInstanceBOs = SLInstancesFacade.SINGLETON_INSTANCE.getPublicImagesFromCloud(locationBO, credBO);						
						completedRegionList.add(dataCenterDetailsDO.getDataCenterName());
						refreshPublicImagesOnDB(imageInstanceBOs,dataCenterDetailsDO);
					} 
				} else {
					imageInstanceBOs = SLInstancesFacade.SINGLETON_INSTANCE.getPrivateImagesFromCloud(locationBO, credBO);
					refreshPrivateImagesOnDB(imageInstanceBOs,dataCenterDetailsDO);
				}
				logger.info("Number of Images in region : {} ---- {}" , dataCenterDetailsDO.getDataCenterName(),imageInstanceBOs.size());

				imageList.addAll(imageInstanceBOs);
			}
		}
		return imageList;
	}

	public List<ImageInstanceBO> getCustomImagesForSpecificDataCenter(
			CloudMasterDO cloudMasterDO, ProviderMasterDO providerMasterDO, DataCenterDetailsDO dcDO,
			List<ImageInstanceBO> imageList, CloudCredentialsBO credBO,
			String typeOfImages) throws DALException, SLException {

		LocationBO locationBO = new LocationBO();
		locationBO.setProviderType(ProviderTypeEnum.getEnum(providerMasterDO.getProviderType()));
		locationBO.setDatacenterName(dcDO.getDataCenterName());
		locationBO.setDatacenterID(dcDO.getDataCenterId());
		logger.info("Fetching custom images for region : {} in cloud id {}",dcDO.getDataCenterName(),cloudMasterDO.getCloudID());
		imageList.addAll(SLInstancesFacade.SINGLETON_INSTANCE.getPrivateImagesFromCloud(locationBO, credBO));
		return imageList;
	}


	/***
	 * This method fetches the list of AMIs based on the filters supplied.
	 */
	@Transactional
	public ImageListContainerBO getAmiListForRegionAndFilters(
			ImageInstanceBO imageInstanceBO,List<InstanceImageFilterEnum> amiFilterEnums ) throws ICException{
		ImageListContainerBO imageListContainer = new ImageListContainerBO();
		//Clouds to be queried for images
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();

		try {
			CloudMasterDAO cloudMasterDAO = factory.getCloudMasterDAO();
			CloudMasterDO cloudMasterDO = cloudMasterDAO.get(CloudMasterDO.class, imageInstanceBO.getCloudId());	

			ProviderMasterDO providerMasterDO = cloudMasterDAO.get(ProviderMasterDO.class, imageInstanceBO.getProviderID());

			DataCenterDetailsDAO dataCenterDetailsDAO = factory.getDataCenterDetailsDAO();
			DataCenterDetailsDO dataCenterDO = new DataCenterDetailsDO();
			dataCenterDO.setCloudMasterDO(cloudMasterDO);
			dataCenterDO.setProviderMasterDO(providerMasterDO);
			dataCenterDO.setDataCenterName(imageInstanceBO.getDatacenterName());
			dataCenterDO = dataCenterDetailsDAO.fetchByCloudIDProviderIDDataCenterName(dataCenterDO);

			LocationBO locationBO = new LocationBO();
			locationBO.setProviderType(ProviderTypeEnum.getEnum(providerMasterDO.getProviderType()));
			locationBO.setDatacenterName(dataCenterDO.getDataCenterName());
			locationBO.setDatacenterID(dataCenterDO.getDataCenterId());

			CloudCredentialsBO credBO = new CloudCredentialsBO();
			credBO.setAccessID(providerMasterDO.getAccessID());
			credBO.setSecretKey(providerMasterDO.getSecretKey());
			credBO.setUrl(providerMasterDO.getCloudURL());

			StopWatch sWatch = new StopWatch();
			sWatch.start();
			logger.debug("INSTANCE_BASED_ON_SCOPE_SQL is: {}", DatabaseSQLConstant.IMAGES_FOR_DATACENTER);	 
			try {
				for (InstanceImageFilterEnum amiFilterEnum : amiFilterEnums) {
					logger.debug("amiFilterEnum --? {}",amiFilterEnum.getValue());
					CloudOneResultSet resultSet = factory.getReportingDAO()
							.executeQuery(
									DatabaseSQLConstant.IMAGES_FOR_DATACENTER,
									false, dataCenterDO.getDataCenterId(),
									amiFilterEnum.getQueryValue(),dataCenterDO.getDataCenterName());					
					if (resultSet != null) {
						List<ImageInstanceBO> imagesBOs = new ArrayList<>();
						imagesBOs = ICUtils.getFeildValue(resultSet, imageInstanceBO);
						imageListContainer.setImageList(imagesBOs);
					}
				}
			} catch (DALException e) {
				logger.error(e);
			}
			sWatch.stop();
			logger.debug("Time Taken for IMAGE QRY {} ms",sWatch.getElapsedTimeInMilliSeconds());

			//Fetching Kernel image type ..

			sWatch.reset();
			sWatch.start();
			CloudOneResultSet kernelResultSet = factory.getReportingDAO()
					.executeQuery(
							DatabaseSQLConstant.IMAGES_FOR_DATACENTER,
							false, dataCenterDO.getDataCenterId(),
							"ImageType = 'kernel'",dataCenterDO.getDataCenterName());

			if (kernelResultSet != null) {
				List<ImageInstanceBO> kernelImagesBOs = new ArrayList<>();
				kernelImagesBOs = ICUtils.getFeildValue(kernelResultSet, imageInstanceBO);
				imageListContainer.setAkiIdList(kernelImagesBOs);
			}


			//Fetching RAM image type ..

			CloudOneResultSet ramDiskResultSet = factory.getReportingDAO()
					.executeQuery(
							DatabaseSQLConstant.IMAGES_FOR_DATACENTER,
							false, dataCenterDO.getDataCenterId(),
							"ImageType = 'ramdisk'",dataCenterDO.getDataCenterName());

			if (ramDiskResultSet != null) {
				List<ImageInstanceBO> ramImageBOs = new ArrayList<>();
				ramImageBOs = ICUtils.getFeildValue(ramDiskResultSet, imageInstanceBO);
				imageListContainer.setAriIdList(ramImageBOs);
			}
			sWatch.stop();
			logger.debug("Time taken for KERNEL + RAM QRY --> {} ms",sWatch.getElapsedTimeInMilliSeconds());

			//The below condition will come into effect only when there is no data available with in DB
			if ((imageListContainer == null
					|| imageListContainer.getImageList() == null || imageListContainer
					.getImageList().isEmpty())
					&& (credBO.getAccessID() != null && !credBO.getAccessID()
					.isEmpty())) {
				logger.debug("Data not avilable inside DB ..Going to cloud to fetch data !");
				imageListContainer = SLInstancesFacade.SINGLETON_INSTANCE
						.getAmiListFromCloudAndRegion(locationBO, credBO,
								amiFilterEnums);
			}

		} catch (DALException | SLException | IOException | SQLException e) {
			logger.error("Could not fetch private images" ,e);
		}

		return imageListContainer;
	}

	/***
	 * This method fetches the list of AMIs based on the filters supplied.
	 */
	@Transactional
	public ImageInstanceBO getAmiByIdFromCloud(ImageInstanceBO imageInstanceBO) throws ICException{
		//Clouds to be queried for images
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		try {
			CloudMasterDAO cloudMasterDAO = factory.getCloudMasterDAO();
			CloudMasterDO cloudMasterDO = cloudMasterDAO.get(CloudMasterDO.class, imageInstanceBO.getCloudId());	
			ProviderMasterDO providerMasterDO = cloudMasterDAO.get(ProviderMasterDO.class, imageInstanceBO.getProviderID());
			DataCenterDetailsDAO dataCenterDetailsDAO = factory.getDataCenterDetailsDAO();
			DataCenterDetailsDO dataCenterDO = new DataCenterDetailsDO();
			dataCenterDO.setCloudMasterDO(cloudMasterDO);
			dataCenterDO.setProviderMasterDO(providerMasterDO);
			dataCenterDO.setDataCenterName(imageInstanceBO.getDatacenterName());
			dataCenterDO = dataCenterDetailsDAO.fetchByCloudIDProviderIDDataCenterName(dataCenterDO);

			LocationBO locationBO = new LocationBO();
			locationBO.setProviderType(ProviderTypeEnum.getEnum(providerMasterDO.getProviderType()));
			locationBO.setDatacenterName(dataCenterDO.getDataCenterName());
			locationBO.setDatacenterID(dataCenterDO.getDataCenterId());

			CloudCredentialsBO credBO = new CloudCredentialsBO();
			credBO.setAccessID(providerMasterDO.getAccessID());
			credBO.setSecretKey(providerMasterDO.getSecretKey());
			credBO.setUrl(providerMasterDO.getCloudURL());

			if(credBO.getAccessID() != null && !credBO.getAccessID().isEmpty()){
				imageInstanceBO = SLInstancesFacade.SINGLETON_INSTANCE.getAmiByIdFromCloud(locationBO, credBO, imageInstanceBO.getAmiID());
			}	
		} catch (DALException | SLException e) {
			logger.error("Could not fetch image details for image {}",imageInstanceBO.getAmiID() ,e);
		}

		return imageInstanceBO;
	}

	@Transactional
	public List<DataCenterDetailsDO> fetchDataCenters(PlatformBO platformBO) throws DALException {
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		DataCenterDetailsDAO dcDAO = factory.getDataCenterDetailsDAO();
		List<DataCenterDetailsDO> dcs = null;
		DataCenterDetailsDO dcDO = new DataCenterDetailsDO();
		dcDO.setCloudMasterDO(dcDAO.get(CloudMasterDO.class, platformBO.getCloudId()));
		dcDO.setProviderMasterDO(dcDAO.get(ProviderMasterDO.class, platformBO.getProviderID()));
		dcs = dcDAO.fetchByCloudIdProviderId(dcDO);
		List<DataCenterDetailsDO> activeDCs = new ArrayList<>();
		for (DataCenterDetailsDO dataCenterDetailsDO : dcs) {
			if(dataCenterDetailsDO.isActive()) {
				activeDCs.add(dataCenterDetailsDO);
			}
		}
		return activeDCs;
	}

	/*
	 * Method to fetch details of image based on image id .
	 * (non-Javadoc)
	 * @see com.cognizant.cloudone.ic.images.IImagesDomain#getImageInfoByAmiId(com.cognizant.cloudone.biz.instance.bo.ImageInstanceBO)
	 */
	@Transactional
	public ImageInstanceBO getImageInfoByAmiId(
			ImageInstanceBO imageInstanceBO) throws ICException {
		ImageInstanceBO resultedImageDetail = null;
		DataCenterDetailsDO dataCenterDetailsDO = null;
		boolean isSubnetConfigured = false;
		try {
			ImageDetailsDAO imageDetailsDAO = getFactory().getImageDetailsDAO();
			logger.debug("IC Image id {}",imageInstanceBO.getAmiID());
			ProviderMasterDO providerMasterDO = imageDetailsDAO.get(ProviderMasterDO.class, imageInstanceBO.getProviderID());
			if (imageInstanceBO.getDatacenterID() == 0
					&& (imageInstanceBO.getDatacenterName() != null && !imageInstanceBO
					.getDatacenterName().isEmpty())) {
				dataCenterDetailsDO = getDataCenterDetailsByDatacenterName(
						imageInstanceBO.getCloudId(),
						imageInstanceBO.getProviderID(),
						imageInstanceBO.getDatacenterName());
			} else {
				dataCenterDetailsDO = imageDetailsDAO.get(
						DataCenterDetailsDO.class,
						imageInstanceBO.getDatacenterID());
			}

			ImageDetailsDO imageDetailsDO = new ImageDetailsDO();
			imageDetailsDO.setAmiId(imageInstanceBO.getAmiID());
			imageDetailsDO.setDataCenterDetailsDO(dataCenterDetailsDO);
			imageDetailsDO = imageDetailsDAO.fetchByAmiIdDatacenterId(imageDetailsDO);
			if(imageDetailsDO == null) {
				imageDetailsDO = new ImageDetailsDO();
				imageDetailsDO.setAmiId(imageInstanceBO.getAmiID());
				imageDetailsDO.setDataCenterName(dataCenterDetailsDO.getDataCenterName());
				imageDetailsDO = imageDetailsDAO.fetchByAmiIdAndDatacenterName(imageDetailsDO);
			}
			if(providerMasterDO != null) {
				Set<VPCDetailsDO> vpcDetailDOs = providerMasterDO.getVpcDetailsDOs();
				if(vpcDetailDOs != null && !vpcDetailDOs.isEmpty()) {
					isSubnetConfigured = true;
				}
			}
			resultedImageDetail = ImageDetailsProvider.getBOFromDO(imageDetailsDO);
		} catch (DALException e) {
			logger.error("Error while feching AMI details");
			logger.error(e);
		}
		if(resultedImageDetail==null){
			//Not there in database, fetch from cloud
			resultedImageDetail = getAmiByIdFromCloud(imageInstanceBO);
		}
		if(resultedImageDetail!=null){
			resultedImageDetail.setSubnetsConfigured(isSubnetConfigured);
		}
		return resultedImageDetail;

	}

	/**
	 * Method to perform DAL transactions based on the inventory info 
	 * retrieved from inventory .
	 * @param imageInstanceBOs
	 * @param dataCenterDetailsDO
	 */
	@Transactional
	public void refreshImagesOnDB(List<ImageInstanceBO> imageInstanceBOs,
			DataCenterDetailsDO dataCenterDetailsDO,String typeOfImages) {
		List<String> amiIDsFromCloud = new ArrayList<>();
		List<ImageDetailsDO> saveOrupdateDOs = new ArrayList<>();
		List<ImageDetailsDO> toBeDeletedDOs = new ArrayList<>();
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		List<String> amiIdsInDB = new ArrayList<>();
		ImageDetailsDAO imageDetailsDAO = factory.getImageDetailsDAO();
		StopWatch sWatch = new StopWatch();		
		sWatch.start();		
		if(typeOfImages != null && typeOfImages.equalsIgnoreCase(this.GET_PUBLIC_IMAGES)) {
			ImageDetailsDO publicImageDetailsDO = new ImageDetailsDO();		
			publicImageDetailsDO.setDataCenterName(dataCenterDetailsDO.getDataCenterName());
			publicImageDetailsDO.setVisibility("Public");

			List<String> publicAmiIdsInDB = new ArrayList<>();
			try {
				publicAmiIdsInDB = imageDetailsDAO.fetchAMIIdByDataCenterNameAndVisibility(publicImageDetailsDO);			
			} catch (DALException e) {
				logger.error("Error while fetching image details for datacenter id --> {}",dataCenterDetailsDO.getDataCenterId());
				logger.error(e);
			}
			if(publicAmiIdsInDB != null && !publicAmiIdsInDB.isEmpty()) {			
				amiIdsInDB.addAll(publicAmiIdsInDB);
			}
		} else if(typeOfImages != null && typeOfImages.equalsIgnoreCase(this.GET_PRIVATE_IMAGES)) {
			ImageDetailsDO otherImageDetailsDO = new ImageDetailsDO();
			otherImageDetailsDO.setDataCenterDetailsDO(dataCenterDetailsDO);
			otherImageDetailsDO.setVisibility("Public");
			List<String> otherAmiIdsInDB = new ArrayList<>();
			try {
				otherAmiIdsInDB = imageDetailsDAO.fetchAMIIdByDataCenterAndVisibilityOtherThan(otherImageDetailsDO);	

			} catch (DALException e) {
				logger.error("Error while fetching image details for datacenter id --> {}",dataCenterDetailsDO.getDataCenterId());
				logger.error(e);
			}

			if(otherAmiIdsInDB != null && !otherAmiIdsInDB.isEmpty()) {			
				amiIdsInDB.addAll(otherAmiIdsInDB);
			}
		}


		if(amiIdsInDB == null || amiIdsInDB.isEmpty()) {			
			saveOrupdateDOs = ImageDetailsProvider
					.getDOFromBO(imageInstanceBOs);
		}
		sWatch.stop();
		logger.debug("Time taken to fetchAMIId from DB  {} ms",sWatch.getElapsedTimeInMilliSeconds());
		sWatch.reset();
		sWatch.start();
		Set<String> amiIdsTobeDeleted = new HashSet<>();
		if (amiIdsInDB != null && !amiIdsInDB.isEmpty()) {					
			for (ImageInstanceBO imageInstanceBO : imageInstanceBOs) {
				if (amiIdsInDB.contains(imageInstanceBO.getAmiID()) || amiIDsFromCloud.contains(imageInstanceBO.getAmiID())) {//To Avoid Dups
					if(amiIdsInDB.contains(imageInstanceBO.getAmiID())) {
						if(typeOfImages != null && !typeOfImages.equalsIgnoreCase(this.GET_PUBLIC_IMAGES)) {
							ImageDetailsDO imageDetailsPassed = new ImageDetailsDO();
							imageDetailsPassed.setAmiId(imageInstanceBO.getAmiID());
							imageDetailsPassed.setDataCenterDetailsDO(dataCenterDetailsDO);
							imageDetailsPassed.setDataCenterName(dataCenterDetailsDO.getDataCenterName());						
							try {
								ImageDetailsDO imageDetailsToBeUpdated = imageDetailsDAO.fetchByAmiIdAndDatacenterId(imageDetailsPassed);
								String visibility = null;
								if (imageInstanceBO.isOwnedByMe()){
									visibility = "Self";
								}else{
									if(!imageInstanceBO.isVisibility()) {
										visibility = "Private";
									}									
								}
								if(imageDetailsToBeUpdated != null && imageDetailsToBeUpdated.getVisibility() != null && !imageDetailsToBeUpdated.getVisibility().equalsIgnoreCase(visibility)) {
									logger.info("visibility change-->" + imageInstanceBO.getAmiID());
									imageDetailsToBeUpdated.setVisibility(visibility);
									saveOrupdateDOs.add(imageDetailsToBeUpdated);
								}
							} catch (DALException e) {
								logger.error("Error while fetching imageDetailsToBeDeleted --> {}",imageInstanceBO.getAmiID());
								logger.error(e);
							}
						}
					}

				} else {
					dataCenterDetailsDO = imageDetailsDAO.get(DataCenterDetailsDO.class, imageInstanceBO.getDatacenterID());
					saveOrupdateDOs.add(ImageDetailsProvider.populateDOfromBO(imageDetailsDAO, imageInstanceBO, dataCenterDetailsDO));
				}
				if (!amiIDsFromCloud.contains(imageInstanceBO.getAmiID())){
					amiIDsFromCloud.add(imageInstanceBO.getAmiID());
				}

			}			
			for (String amiID : amiIdsInDB) { 
				if (amiID != null && !amiIDsFromCloud.contains(amiID)) {
					ImageDetailsDO imageDetailsToBeDeleted = null;
					if(typeOfImages != null && typeOfImages.equalsIgnoreCase(this.GET_PUBLIC_IMAGES)) {
						amiIdsTobeDeleted.add(amiID);
					} else {
						ImageDetailsDO imageDetailsPassed = new ImageDetailsDO();
						imageDetailsPassed.setAmiId(amiID);
						imageDetailsPassed.setDataCenterDetailsDO(dataCenterDetailsDO);
						imageDetailsPassed.setDataCenterName(dataCenterDetailsDO.getDataCenterName());						
						try {
							imageDetailsToBeDeleted = imageDetailsDAO.fetchByAmiIdAndDatacenterId(imageDetailsPassed);						
						} catch (DALException e) {
							logger.error("Error while fetching imageDetailsToBeDeleted --> {}",amiID);
							logger.error(e);
						}
						toBeDeletedDOs.add(imageDetailsToBeDeleted);
					}

				}
			}
		}
		sWatch.stop();
		logger.debug("Time taken to delete private records in DB {} ms",sWatch.getElapsedTimeInMilliSeconds());
		sWatch.reset();
		sWatch.start();
		if (typeOfImages != null
				&& typeOfImages.equalsIgnoreCase(this.GET_PUBLIC_IMAGES)) {
			if (amiIdsTobeDeleted != null && !amiIdsTobeDeleted.isEmpty()) {
				String amiIds = GlobalConstant.BLANK_STRING;
				List<String> toBeDeletedlist = new ArrayList<>(amiIdsTobeDeleted);
				int toBeDeletedlistSize = toBeDeletedlist.size();
				logger.debug("Number of records to be  deleted {}",toBeDeletedlistSize);
				int count = 0;
				while(toBeDeletedlistSize!=0){
					List<String> toBeDeletedSublist;
					//Delete in bulk of 5000's records in a loop
					if(toBeDeletedlistSize <= 5000){
						toBeDeletedSublist = toBeDeletedlist.subList(count, (count + toBeDeletedlistSize) );
						toBeDeletedlistSize = 0; 
						//All deleted, exit loop
					}else{
						toBeDeletedSublist = toBeDeletedlist.subList(count, count + 5000);
						toBeDeletedlistSize = toBeDeletedlistSize - 5000;
						//5000 records marked for deletion, bring in the next batch in the next loop
					}
					for (String amiId : toBeDeletedSublist) {
						amiIds = amiIds + GlobalConstant.QUOTE
								+ amiId + GlobalConstant.QUOTE
								+ GlobalConstant.COMMA_SEPARATOR;
					}
					//Remove the last comma separator
					amiIds = amiIds.substring(0, amiIds.length()-1) ;
					try {
						int result = getFactory().getImageDetailsDAO().executeCustomUpdate(DatabaseSQLConstant.DELETE_IMAGEDETAILS, amiIds);
						logger.debug("Number of records deleted {}",result);
					} catch (Exception e) {
						logger.error(e);
					}
					count = count + 5000;
				}
			}
		}
		sWatch.stop();
		logger.debug("Time taken to delete public records in DB {} ms",sWatch.getElapsedTimeInMilliSeconds());

		try {
			if (saveOrupdateDOs != null && !saveOrupdateDOs.isEmpty()){
				imageDetailsDAO.saveOrUpdateBatch(saveOrupdateDOs);
			}
		} catch (DALException e) {
			logger.error("Error while save or update  transactions ..");
			logger.error(e);
		}

		try{
			if (toBeDeletedDOs != null && !toBeDeletedDOs.isEmpty()){
				imageDetailsDAO.deleteBatch(toBeDeletedDOs);
			}
		} catch (DALException e) {
			logger.error("Error while deleting transactions ..");
			logger.error(e);
		}

	}

	@Override
	@Transactional
	public List<ImageInstanceBO> fetchOpenstackImagesbyDCID(
			ImageInstanceBO imageInstanceBO) {
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		List<ImageInstanceBO> imagesList = new ArrayList<>();
		ImageDetailsDAO imageDetailsDAO = factory.getImageDetailsDAO();
		DataCenterDetailsDO dcDO = imageDetailsDAO.get(DataCenterDetailsDO.class, imageInstanceBO.getDatacenterID());
		ImageDetailsDO imageDetailsDO = new ImageDetailsDO();	
		imageDetailsDO.setDataCenterDetailsDO(dcDO);
		try {
			List<ImageDetailsDO> imageDOs = imageDetailsDAO.fetchByDataCenterId(imageDetailsDO);
			if(imageDOs!=null && !imageDOs.isEmpty()){
				for(ImageDetailsDO imageDO :imageDOs){
					imagesList.add(ImageDetailsProvider.getBOFromDO(imageDO));
				}
			}
		} catch (DALException e) {
			logger.error("Error while fetchOpenstackImagesbyDCID ..");
			logger.error(e);
		}
		return imagesList;

	}

	@Transactional
	public void refreshPublicImagesOnDB(List<ImageInstanceBO> imageInstanceBOs,
			DataCenterDetailsDO dataCenterDetailsDO) {
		Set<String> amiIDsFromCloud = new HashSet<>();
		List<ImageDetailsDO> saveOrupdateDOs = new ArrayList<>();
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		List<String> amiIdsInDB = new ArrayList<>();
		ImageDetailsDAO imageDetailsDAO = factory.getImageDetailsDAO();
		StopWatch sWatch = new StopWatch();		
		sWatch.start();	
		ImageDetailsDO publicImageDetailsDO = new ImageDetailsDO();		
		publicImageDetailsDO.setDataCenterName(dataCenterDetailsDO.getDataCenterName());
		publicImageDetailsDO.setVisibility("Public");
		
		List<String> publicAmiIdsInDB = new ArrayList<>();
		Set<String> publicAmisSet = null;
		try {
			publicAmiIdsInDB = imageDetailsDAO.fetchAMIIdByDataCenterNameAndVisibility(publicImageDetailsDO);			
		} catch (DALException e) {
			logger.error("Error while fetching image details for datacenter id --> {}",dataCenterDetailsDO.getDataCenterId());
			logger.error(e);
		}
		if(publicAmiIdsInDB != null && !publicAmiIdsInDB.isEmpty()) {			
			amiIdsInDB.addAll(publicAmiIdsInDB);
			publicAmisSet = new HashSet<>(amiIdsInDB);
		}
		
		
		
		sWatch.stop();
		logger.debug("Time taken to fetchAMIId from DB  {} ms",sWatch.getElapsedTimeInMilliSeconds());
		sWatch.reset();
		sWatch.start();
		
		if(publicAmisSet == null || publicAmisSet.isEmpty()) {			
			saveOrupdateDOs = ImageDetailsProvider
					.getDOFromBO(imageInstanceBOs);
		}
		Set<String> amiIdsTobeDeleted = new HashSet<>();
		if (publicAmisSet != null && !publicAmisSet.isEmpty()) {					
			for (ImageInstanceBO imageInstanceBO : imageInstanceBOs) {
				if (!publicAmisSet.contains(imageInstanceBO.getAmiID())) {//To Avoid Dups
					dataCenterDetailsDO = imageDetailsDAO.get(DataCenterDetailsDO.class, imageInstanceBO.getDatacenterID());
					saveOrupdateDOs.add(ImageDetailsProvider.populateDOfromBO(
							imageDetailsDAO, imageInstanceBO,dataCenterDetailsDO));
				}
				if (!amiIDsFromCloud.contains(imageInstanceBO.getAmiID())){
					amiIDsFromCloud.add(imageInstanceBO.getAmiID());
				}
				
			}
			
			for (String amiID : publicAmisSet) { 
				if (amiID != null && !amiIDsFromCloud.contains(amiID)) {
					amiIdsTobeDeleted.add(amiID);
				}
			}
		}
		
		if (amiIdsTobeDeleted != null && !amiIdsTobeDeleted.isEmpty()) {
			String amiIds = GlobalConstant.BLANK_STRING;
			List<String> toBeDeletedlist = new ArrayList<>(amiIdsTobeDeleted);
			int toBeDeletedlistSize = toBeDeletedlist.size();
			logger.debug("Number of records to be  deleted {}",toBeDeletedlistSize);
			int count = 0;
			while(toBeDeletedlistSize!=0){
				List<String> toBeDeletedSublist;
				//Delete in bulk of 5000's records in a loop
				if(toBeDeletedlistSize <= 5000){
					toBeDeletedSublist = toBeDeletedlist.subList(count, (count + toBeDeletedlistSize) );
					toBeDeletedlistSize = 0; 
					//All deleted, exit loop
				}else{
					toBeDeletedSublist = toBeDeletedlist.subList(count, count + 5000);
					toBeDeletedlistSize = toBeDeletedlistSize - 5000;
					//5000 records marked for deletion, bring in the next batch in the next loop
				}
				for (String amiId : toBeDeletedSublist) {
					amiIds = amiIds + GlobalConstant.QUOTE
							+ amiId + GlobalConstant.QUOTE
							+ GlobalConstant.COMMA_SEPARATOR;
				}
				//Remove the last comma separator
				amiIds = amiIds.substring(0, amiIds.length()-1) ;
				try {
					int result = getFactory().getImageDetailsDAO().executeCustomUpdate(DatabaseSQLConstant.DELETE_IMAGEDETAILS, amiIds);
					logger.debug("Number of records deleted {}",result);
				} catch (Exception e) {
					logger.error(e);
				}
				count = count + 5000;
			}
		}
		try {
			if (saveOrupdateDOs != null && !saveOrupdateDOs.isEmpty()){
				imageDetailsDAO.saveOrUpdateBatch(saveOrupdateDOs);
			}
		} catch (DALException e) {
			logger.error("Error while save or update  transactions ..");
			logger.error(e);
		}

	
	}
	
	
	@Transactional
	public void refreshPrivateImagesOnDB(List<ImageInstanceBO> imageInstanceBOs,
			DataCenterDetailsDO dataCenterDetailsDO) {
		List<String> amiIDsFromCloud = new ArrayList<>();
		List<ImageDetailsDO> saveOrupdateDOs = new ArrayList<>();
		List<ImageDetailsDO> toBeDeletedDOs = new ArrayList<>();
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		List<ImageDetailsDO> amiIdsInDB = new ArrayList<>();
		ImageDetailsDAO imageDetailsDAO = factory.getImageDetailsDAO();
		StopWatch sWatch = new StopWatch();		
		sWatch.start();
		ImageDetailsDO imageDetailsDO = new ImageDetailsDO();
		imageDetailsDO.setDataCenterDetailsDO(dataCenterDetailsDO);
		try {
			amiIdsInDB = imageDetailsDAO.fetchByDataCenterId(imageDetailsDO);	
		} catch (DALException e) {
			logger.error("Error while fetching image details for datacenter id --> {}",dataCenterDetailsDO.getDataCenterId());
			logger.error(e);
		}
		sWatch.stop();
		logger.debug("Time taken to fetchAMIId from DB  {} ms",sWatch.getElapsedTimeInMilliSeconds());
		if(amiIdsInDB == null || amiIdsInDB.isEmpty()) {			
			saveOrupdateDOs = ImageDetailsProvider
					.getDOFromBO(imageInstanceBOs);
		}
		Map<String,ImageDetailsDO> imagesInDBMap = new HashMap<>();
		if(amiIdsInDB != null && !amiIdsInDB.isEmpty()) {
			for(ImageDetailsDO imagesDetailsDO : amiIdsInDB) {
				imagesInDBMap.put(imagesDetailsDO.getAmiId(), imagesDetailsDO);
			}
		}
		if (imagesInDBMap != null && !imagesInDBMap.isEmpty()) {					
			for (ImageInstanceBO imageInstanceBO : imageInstanceBOs) {
				if (imagesInDBMap.containsKey(imageInstanceBO.getAmiID()) || amiIDsFromCloud.contains(imageInstanceBO.getAmiID())) {//To Avoid Dups
					if(imagesInDBMap.containsKey(imageInstanceBO.getAmiID())) {
						ImageDetailsDO imageDetailsToBeUpdated = imagesInDBMap.get(imageInstanceBO.getAmiID());
						String visibility = null;
						if (imageInstanceBO.isOwnedByMe()){
							visibility = "Self";
						}else{
							if(!imageInstanceBO.isVisibility()) {
								visibility = "Private";
							}									
						}
						if(imageDetailsToBeUpdated != null && imageDetailsToBeUpdated.getVisibility() != null && !imageDetailsToBeUpdated.getVisibility().equalsIgnoreCase(visibility)) {
							logger.info("visibility change-->" + imageInstanceBO.getAmiID());
							imageDetailsToBeUpdated.setVisibility(visibility);
							saveOrupdateDOs.add(imageDetailsToBeUpdated);
						}
					}
				} else {
					dataCenterDetailsDO = imageDetailsDAO.get(DataCenterDetailsDO.class, imageInstanceBO.getDatacenterID());
					saveOrupdateDOs.add(ImageDetailsProvider.populateDOfromBO(
							imageDetailsDAO, imageInstanceBO,dataCenterDetailsDO));
				}
				if (!amiIDsFromCloud.contains(imageInstanceBO.getAmiID())){
					amiIDsFromCloud.add(imageInstanceBO.getAmiID());
				}
				logger.info("imageInstanceBO.getAmiID()--->" + imageInstanceBO.getAmiID() + "---"  +  saveOrupdateDOs.size());
			}			
			for (ImageDetailsDO imageFromDB : amiIdsInDB) { 
				if (imageFromDB != null && !amiIDsFromCloud.contains(imageFromDB.getAmiId())) {
					toBeDeletedDOs.add(imageFromDB);
				}
			}
		}
		try {
			if (saveOrupdateDOs != null && !saveOrupdateDOs.isEmpty()){
				imageDetailsDAO.saveOrUpdateBatch(saveOrupdateDOs);
			}
		} catch (DALException e) {
			logger.error("Error while save or update  transactions ..");
			logger.error(e);
		}

		try{
			if (toBeDeletedDOs != null && !toBeDeletedDOs.isEmpty()){
				imageDetailsDAO.deleteBatch(toBeDeletedDOs);
			}
		} catch (DALException e) {
			logger.error("Error while deleting transactions ..");
			logger.error(e);
		}
		
	}
	
	private PlatformBO getProviderBOFromDO(ProviderMasterDO providerMasterDO) {
		
		PlatformBO providerBO = new PlatformBO();
			
		providerBO.setProviderID(providerMasterDO.getProviderID());
        providerBO.setProviderTypeId(providerMasterDO.getProviderTypeId());
        providerBO.setProviderType(ProviderTypeEnum.getEnumById(providerMasterDO.getProviderTypeId()));
        providerBO.setProviderName(providerMasterDO.getProviderName());
        providerBO.setAccessID(providerMasterDO.getAccessID());
        providerBO.setSecretKey(providerMasterDO.getSecretKey());
        providerBO.setPassword(providerMasterDO.getPassword());
        providerBO.setAccountNo(providerMasterDO.getAccountNumber());
        providerBO.setConsolidatedBilling(providerMasterDO.isConsolidatedBilling());
        providerBO.setCloudURL(providerMasterDO.getCloudURL());
        providerBO.setCloudId(providerMasterDO.getCloudMasterDO().getCloudID());
        providerBO.setCloudName(providerMasterDO.getCloudMasterDO().getCloudName());
        providerBO.setPort(providerMasterDO.getPort());
        return providerBO;
	}

	@Override
	public List<OSDescriptorBO> getImageListForSelectedRegion(OSDescriptorBO oSDescriptorBO, DataCenterBO dataCenterBO) throws ICException {
		List<OSDescriptorBO> publisherNames = null;
		CloudCredentialsBO cloudCredentialsBO = getCredentials(dataCenterBO);
		try {
			publisherNames = SLInstancesFacade.SINGLETON_INSTANCE.retrieveOsDetails(oSDescriptorBO, cloudCredentialsBO);
		} catch (SLException e) {
			logger.error("Could not fetch publisher details for the region {}", dataCenterBO.getDcName());
			throw new ICException(e);
		}
		return publisherNames;
	}

	private CloudCredentialsBO getCredentials(BaseBO baseBO) throws ICException {
		IPlatformsDomain domain = new PlatformsDomainImpl();
		PlatformBO platformBO = domain.fetchProvider(PlatformsProvider.getInputPlatformBO(baseBO));
		return PlatformsProvider.getProviderCredentials(platformBO);
	}

	@Override
	public List<OsDetailsDO> saveListOfOSDetails(List<OSDescriptorBO> listOfOSDescriptorBO,DataCenterBO dataCenterBO) throws ICException {
		
		List<OsDetailsDO> listOfOsDetailsDOs=null;
		if(listOfOSDescriptorBO!=null && !listOfOSDescriptorBO.isEmpty()){
			CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
			CloudMasterDAO cloudMasterDAO = factory.getCloudMasterDAO();
			CloudMasterDO cloudMasterDO = cloudMasterDAO.get(CloudMasterDO.class, dataCenterBO.getCloudId());	
			ProviderMasterDO providerMasterDO = cloudMasterDAO.get(ProviderMasterDO.class, dataCenterBO.getProviderID());
			DataCenterDetailsDAO dataCenterDetailsDAO = factory.getDataCenterDetailsDAO();
			DataCenterDetailsDO dataCenterDO = new DataCenterDetailsDO();
			listOfOsDetailsDOs = new ArrayList<>();
			try{
				dataCenterDO.setCloudMasterDO(cloudMasterDO);
				dataCenterDO.setProviderMasterDO(providerMasterDO);
				dataCenterDO.setDataCenterName(dataCenterBO.getDcName());
				DataCenterDetailsDO dataCenterDetailsDO = dataCenterDetailsDAO.fetchByCloudIDProviderIDDataCenterName(dataCenterDO);
				for(OSDescriptorBO osDescriptorBO: listOfOSDescriptorBO){
					OsDetailsDO osDetailsDO =OSResourcesDetailsProvider.getOsDoToInsertFromBO(osDescriptorBO, cloudMasterDO, providerMasterDO);
					osDetailsDO.setDataCenterDetailsDO(dataCenterDetailsDO);
					listOfOsDetailsDOs.add(osDetailsDO);
					}	
				
			}catch(DALException e){
				logger.debug("Error while saving OSImageDetails in db {}", e);
				throw new ICException(e);
			}
		}
		return listOfOsDetailsDOs;
	}

	@Override
	@Transactional
	public List<OSDescriptorBO> fetchOSDetailsList(OSDescriptorBO osDescriptorBO) throws ICException {
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		OsDetailsDAO osDAO = factory.getOsDetailsDAO();
		try{
			OsDetailsDO osDetailsDO = new OsDetailsDO();
			osDetailsDO.setCloudMasterDO(osDAO.get(CloudMasterDO.class, osDescriptorBO.getCloudId()));
			osDetailsDO.setProviderMasterDO(osDAO.get(ProviderMasterDO.class, osDescriptorBO.getProviderID()));
			List<OsDetailsDO> osDetailsDOs = osDAO.fetchByCloudIdProviderId(osDetailsDO);
			return OSResourcesDetailsProvider.getOSDescriptorBOsFromDOs(osDetailsDOs);
		}catch(DALException e){
			logger.debug("Error while saving OSImageDetails in db {}", e);
			throw new ICException(e);
		}
	}
	
	@Override
	@Transactional
	public List<DataCenterBO> fetchDataCenters(int cloudId, int providerID) throws ICException {
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		DataCenterDetailsDAO dcDAO = factory.getDataCenterDetailsDAO();
		List<DataCenterBO> finalBOs = null;
		DataCenterBO dcBO = null;
		try {
			List<DataCenterDetailsDO> dcs = null;
			DataCenterDetailsDO dcDO = new DataCenterDetailsDO();
			dcDO.setCloudMasterDO(dcDAO.get(CloudMasterDO.class, cloudId));
			dcDO.setProviderMasterDO(dcDAO.get(ProviderMasterDO.class, providerID));
			dcs = dcDAO.fetchByCloudIdProviderId(dcDO);
			finalBOs = new ArrayList<>();
			for (DataCenterDetailsDO dataCenterDetailsDO : dcs) {
				if (dataCenterDetailsDO.isActive()) {
					dcBO = new DataCenterBO();
					dcBO.setCloudId(dataCenterDetailsDO.getCloudMasterDO().getCloudID());
					dcBO.setProviderID(dataCenterDetailsDO.getProviderMasterDO().getProviderID());					
					dcBO.setProviderType(ProviderTypeEnum.getEnum(dataCenterDetailsDO.getProviderMasterDO().getProviderType()));
					dcBO.setProviderName(dataCenterDetailsDO.getProviderMasterDO().getProviderName());
					dcBO.setDcId(dataCenterDetailsDO.getDataCenterId());
					dcBO.setDcName(dataCenterDetailsDO.getDataCenterName());
					dcBO.setLocation(dataCenterDetailsDO.getLocation());
					dcBO.setProfile(dataCenterDetailsDO.getProfile());
					finalBOs.add(dcBO);
				}
			}
		} catch (DALException e) {
			logger.debug("Error while saving OSImageDetails in db {}", e);
			throw new ICException(e);
		}
		return finalBOs;
	}
}
