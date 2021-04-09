/**
 * 
 */
package com.cognizant.cloudone.ic.instance.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.cognizant.cloudone.biz.base.bo.CloudCredentialsBO;
import com.cognizant.cloudone.biz.elasticip.ElasticIPBO;
import com.cognizant.cloudone.biz.instance.bo.ClusterBO;
import com.cognizant.cloudone.biz.instance.bo.CreateInstanceInfoBO;
import com.cognizant.cloudone.biz.instance.bo.DataCenterBO;
import com.cognizant.cloudone.biz.instance.bo.HostBO;
import com.cognizant.cloudone.biz.instance.bo.ImageInstanceBO;
import com.cognizant.cloudone.biz.instance.bo.InstanceBO;
import com.cognizant.cloudone.biz.instance.bo.InstanceTypeBO;
import com.cognizant.cloudone.biz.instance.bo.OSDescriptorBO;
import com.cognizant.cloudone.biz.instance.bo.PrerequisiteInfoInstanceBO;
import com.cognizant.cloudone.biz.instance.bo.ResourceGroupDetailsBO;
import com.cognizant.cloudone.biz.instance.bo.ResourcePoolBO;
import com.cognizant.cloudone.biz.instance.bo.SecurityGroupBO;
import com.cognizant.cloudone.biz.instance.bo.ServiceBO;
import com.cognizant.cloudone.biz.instance.bo.VirtualDiskBO;
import com.cognizant.cloudone.biz.keypairs.KeyPairsBO;
import com.cognizant.cloudone.biz.networking.NetworkingBO;
import com.cognizant.cloudone.biz.notification.NotificationBO;
import com.cognizant.cloudone.biz.platforms.PlatformBO;
import com.cognizant.cloudone.dal.dao.ClusterMasterDAO;
import com.cognizant.cloudone.dal.dao.ComputeProfileMasterDAO;
import com.cognizant.cloudone.dal.dao.DataCenterDetailsDAO;
import com.cognizant.cloudone.dal.dao.DeviceLayoutDAO;
import com.cognizant.cloudone.dal.dao.ElasticIPDetailsDAO;
import com.cognizant.cloudone.dal.dao.EniDetailsDAO;
import com.cognizant.cloudone.dal.dao.ImageDetailsDAO;
import com.cognizant.cloudone.dal.dao.InstanceTypeDAO;
import com.cognizant.cloudone.dal.dao.KeypairDetailsDAO;
import com.cognizant.cloudone.dal.dao.OsDetailsDAO;
import com.cognizant.cloudone.dal.dao.ResourceGroupDetailsDAO;
import com.cognizant.cloudone.dal.dao.ResourcePoolDAO;
import com.cognizant.cloudone.dal.dao.S3FileDetailsDAO;
import com.cognizant.cloudone.dal.dao.SecurityGroupDAO;
import com.cognizant.cloudone.dal.dao.ServerMasterDAO;
import com.cognizant.cloudone.dal.dao.VmDiskDetialsDAO;
import com.cognizant.cloudone.dal.dao.factory.CloudOneDAOFactory;
import com.cognizant.cloudone.dal.dbo.CloudMasterDO;
import com.cognizant.cloudone.dal.dbo.ClusterMasterDO;
import com.cognizant.cloudone.dal.dbo.ComputeProfileMasterDO;
import com.cognizant.cloudone.dal.dbo.DataCenterDetailsDO;
import com.cognizant.cloudone.dal.dbo.DeviceLayoutDO;
import com.cognizant.cloudone.dal.dbo.ElasticIPDetailsDO;
import com.cognizant.cloudone.dal.dbo.EniDetailsDO;
import com.cognizant.cloudone.dal.dbo.ImageDetailsDO;
import com.cognizant.cloudone.dal.dbo.InstanceDetailsDO;
import com.cognizant.cloudone.dal.dbo.InstanceTypeDO;
import com.cognizant.cloudone.dal.dbo.KeyPairDetailsDO;
import com.cognizant.cloudone.dal.dbo.NotificationDO;
import com.cognizant.cloudone.dal.dbo.OsDetailsDO;
import com.cognizant.cloudone.dal.dbo.ProviderMasterDO;
import com.cognizant.cloudone.dal.dbo.ResourceGroupDetailsDO;
import com.cognizant.cloudone.dal.dbo.ResourcePoolDO;
import com.cognizant.cloudone.dal.dbo.S3FileDetailsDO;
import com.cognizant.cloudone.dal.dbo.SecurityGroupDO;
import com.cognizant.cloudone.dal.dbo.ServerMasterDO;
import com.cognizant.cloudone.dal.dbo.VmDiskDetialsDO;
import com.cognizant.cloudone.ic.base.impl.CloudOneDAOFactoryBuilder;
import com.cognizant.cloudone.ic.elasticip.provider.ElasticIPProvider;
import com.cognizant.cloudone.ic.instance.IPrereqInstanceDomain;
import com.cognizant.cloudone.ic.instance.provider.ImageDetailsProvider;
import com.cognizant.cloudone.ic.instance.provider.InstanceProvider;
import com.cognizant.cloudone.ic.instance.provider.InstanceTypeProvider;
import com.cognizant.cloudone.ic.instance.provider.OSResourcesDetailsProvider;
import com.cognizant.cloudone.ic.instance.provider.PrerequisiteInfoProvider;
import com.cognizant.cloudone.ic.instance.provider.ResourceGroupProvider;
import com.cognizant.cloudone.ic.keypairs.provider.KeyPairsProvider;
import com.cognizant.cloudone.ic.networking.provider.NetworkMngmtProvider;
import com.cognizant.cloudone.ic.notification.provider.NotificationProvider;
import com.cognizant.cloudone.ic.platforms.IPlatformsDomain;
import com.cognizant.cloudone.ic.platforms.impl.PlatformsDomainImpl;
import com.cognizant.cloudone.ic.platforms.provider.PlatformsProvider;
import com.cognizant.cloudone.ic.s3buckets.impl.ObjectStorageMgmtImpl;
import com.cognizant.cloudone.ic.security.provider.SecurityProvider;
import com.cognizant.cloudone.kernel.constants.GlobalConstant;
import com.cognizant.cloudone.kernel.constants.PrerequisiteInformationParamaterEnum;
import com.cognizant.cloudone.kernel.constants.ProviderTypeEnum;
import com.cognizant.cloudone.kernel.exception.DALException;
import com.cognizant.cloudone.kernel.exception.ICException;
import com.cognizant.cloudone.kernel.exception.SLException;
import com.cognizant.cloudone.kernel.logger.CloudOneLogger;
import com.cognizant.cloudone.sl.instances.facade.SLInstancesFacade;

/**
 * @author 322478
 * 
 */
public class DefaultPrerequisiteInfoFetchImpl extends BasePrerequisiteImpl
		implements IPrereqInstanceDomain {
	
	private final CloudOneLogger logger = CloudOneLogger.getLogger(this.getClass()
			.getName());
	
	private final String CATALOG = "CATALOG";
	
	@Transactional
	public PrerequisiteInfoInstanceBO getPrerequisiteInformation(
			PrerequisiteInformationParamaterEnum fetchParameter,
			CreateInstanceInfoBO createBO,
			PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO)
			throws ICException {
		try{
			if (prerequisiteInfoInstanceBO.getProviderType().equals(ProviderTypeEnum.SCVMM)){
				prerequisiteInfoInstanceBO.setResourcePools(fetchResourcePools(prerequisiteInfoInstanceBO));
				prerequisiteInfoInstanceBO.setTemplates(getTemplates(prerequisiteInfoInstanceBO));
				prerequisiteInfoInstanceBO.setVirtualDiskBOs(getVHDsFromDB(prerequisiteInfoInstanceBO));
				List<ClusterBO> clusterBOList = fetchClustersForSCVMM(prerequisiteInfoInstanceBO);
				Map<Integer, Map<Integer,String>> clusterAndHostsMap = new HashMap<>();
				Map<Integer, Map<Integer, String>> hostGrpAndHostsMap = new HashMap<>();
				Map<Integer,String> hostMapForCluster = null;
				Map<Integer,String> hostMapForHostGrp = null;
				LinkedHashMap<Integer, String> clustersMap = new LinkedHashMap<>();
				LinkedHashMap<Integer, String> hostGrpsMap = new LinkedHashMap<>();
				List<HostBO> hostBOs;
				if(clusterBOList != null && !clusterBOList.isEmpty()){
					for(ClusterBO clusterBO : clusterBOList){
						hostMapForCluster = new HashMap<>();
						hostMapForHostGrp = new HashMap<>();
						
						if(clusterBO.getMorId().contains(GlobalConstant.HYPERV_CLUSTER)){
							hostBOs = clusterBO.getHostBOs();
							if(hostBOs != null && !hostBOs.isEmpty()){
								for(HostBO hostBO : hostBOs){
									hostMapForCluster.put(hostBO.getHostId(), hostBO.getHostName());
								}
							}
							clusterAndHostsMap.put(clusterBO.getClusterId(), hostMapForCluster);
							clustersMap.put(clusterBO.getClusterId(), clusterBO.getClusterName());
						}else if(clusterBO.getMorId().contains(GlobalConstant.HYPERV_HOST_GROUP)){
							hostBOs = clusterBO.getHostBOs();
							if(hostBOs != null && !hostBOs.isEmpty()){
								for(HostBO hostBO : hostBOs){
									hostMapForHostGrp.put(hostBO.getHostId(), hostBO.getHostName());
								}
							}
							hostGrpAndHostsMap.put(clusterBO.getClusterId(), hostMapForHostGrp);
							hostGrpsMap.put(clusterBO.getClusterId(), clusterBO.getClusterName());
						}
					}
					
				}
				prerequisiteInfoInstanceBO.setClusterAndHostsMap(clusterAndHostsMap);
				prerequisiteInfoInstanceBO.setHostGrpAndHostsMap(hostGrpAndHostsMap);
				prerequisiteInfoInstanceBO.setHosts(fetchAllHostsForSCVMM(prerequisiteInfoInstanceBO));
				prerequisiteInfoInstanceBO.setClusters(clustersMap);
				prerequisiteInfoInstanceBO.setHostGroups(hostGrpsMap);
			}else{
				List<ClusterBO> clusterBOs = null;
				List<DataCenterDetailsDO> dataCenterDOList = fetchDataCenters(prerequisiteInfoInstanceBO.getCloudId(), prerequisiteInfoInstanceBO.getProviderID());
				
				List<String> dcNames = new ArrayList<>();
				Map<Integer, LinkedHashMap<Integer,String>> dataCenterAndClustersMap = new LinkedHashMap<>();
				Map<Integer, Map<Integer,String>> clusterAndHostsMap = new LinkedHashMap<>();
				LinkedHashMap<Integer, DataCenterBO> dataCenters = new LinkedHashMap<>();
				LinkedHashMap<Integer, String> clusters = new LinkedHashMap<>();
				LinkedHashMap<Integer, String> hosts = new LinkedHashMap<>();
				DataCenterBO dcBO = null;
				for (DataCenterDetailsDO dcDO : dataCenterDOList) {
					clusterBOs = new ArrayList<>();
					/*
					 * CO-20089 clusters/affinity groups must not be cleared
					 * As we support cloning of Azure VM's in affinity group.
					 */
					if(!prerequisiteInfoInstanceBO.getProviderType().equals(ProviderTypeEnum.AZURE)){
						clusters.clear();
						hosts.clear();
					}
					if (dcDO.getClusterMasterDO() != null && !dcDO.getClusterMasterDO().isEmpty()){
						for(ClusterMasterDO clusterMasterDO:dcDO.getClusterMasterDO()){
							if (clusterMasterDO != null && clusterMasterDO.isActive()){
								clusters.put(clusterMasterDO.getClusterID(), clusterMasterDO.getClusterName());
								ClusterBO clusterBO = new ClusterBO();
								clusterBO.setClusterName(clusterMasterDO.getClusterName());
								//Add hosts based on cluster for openstack
								if(clusterMasterDO.getServerMasterDO()!=null && !clusterMasterDO.getServerMasterDO().isEmpty()){
									List<HostBO> hostBOs = new ArrayList<>();
									for(ServerMasterDO serverMasterDO: clusterMasterDO.getServerMasterDO()){
										HostBO host = new HostBO();
										host.setHostId(serverMasterDO.getServerId());
										host.setHostName(serverMasterDO.getServerName());
										hosts.put(serverMasterDO.getServerId(), serverMasterDO.getServerName());
										hostBOs.add(host);
									}
									clusterAndHostsMap.put(clusterMasterDO.getClusterID(), hosts);
									clusterBO.setHostBOs(hostBOs);
								}
								clusterBOs.add(clusterBO);
							}
						}
						dataCenterAndClustersMap.put(dcDO.getDataCenterId(), clusters);
					}    
					dcBO = new DataCenterBO();
					dcBO.setClusterBOs(clusterBOs);
					dcBO.setDcId(dcDO.getDataCenterId());
					dcBO.setDcName(dcDO.getDataCenterName());
					dcBO.setLocation(dcDO.getLocation());
					dcBO.setProfile(dcDO.getProfile());
	
					dcNames.add(dcDO.getDataCenterName());
					dataCenters.put(dcDO.getDataCenterId(), dcBO);
				}
				prerequisiteInfoInstanceBO.setDataCenterAndClustersMap(dataCenterAndClustersMap);
				prerequisiteInfoInstanceBO.setClusterAndHostsMap(clusterAndHostsMap);
				prerequisiteInfoInstanceBO.setDataCentersList(dcNames);
				prerequisiteInfoInstanceBO.setDataCenters(dataCenters);
				prerequisiteInfoInstanceBO.setClusters(clusters);
				prerequisiteInfoInstanceBO.setHosts(hosts);
				List<OSDescriptorBO> osDescriptorBOs = fetchOSDetailsList(prerequisiteInfoInstanceBO);
				prerequisiteInfoInstanceBO.setOsSpecificResourcesList(osDescriptorBOs);
				
				List<VirtualDiskBO> virtualDiskBOs = fetchDiskDetails(prerequisiteInfoInstanceBO);
				prerequisiteInfoInstanceBO.setVirtualDiskBOs(virtualDiskBOs);
				
				//Temporarily added for now ...will be replaced only PASS details is done...
				if (prerequisiteInfoInstanceBO.getProviderType().equals(ProviderTypeEnum.AZURE)){
					ObjectStorageMgmtImpl objectStorageMgmtImpl = new ObjectStorageMgmtImpl();
					ServiceBO serviceBO = new ServiceBO();
					serviceBO.setCloudId(prerequisiteInfoInstanceBO.getCloudId());
					serviceBO.setProviderID(prerequisiteInfoInstanceBO.getProviderID());
					List<ServiceBO> serviceBOs = null;
					//Get objects storages from DB
					serviceBOs =  objectStorageMgmtImpl.getObjectStoragefromDB(serviceBO);
					//else fetch from cloud
					if(serviceBOs==null || serviceBOs.isEmpty()){
						serviceBOs =  objectStorageMgmtImpl.getObjectStorageFromCloud(serviceBO);
					}
					Map<String, List<InstanceTypeBO>> dataCenterInstanceTypeMap =  new HashMap<>();
					for(DataCenterDetailsDO dcDO :dataCenterDOList){
						prerequisiteInfoInstanceBO.setDatacenterID(dcDO.getDataCenterId());
						List<InstanceTypeBO> flavors = getFlavorsFromDB(prerequisiteInfoInstanceBO);
						prerequisiteInfoInstanceBO.setInstanceTypes(flavors);
						dataCenterInstanceTypeMap.put(dcDO.getDataCenterName(), flavors);
					}
					prerequisiteInfoInstanceBO.setDataCenterInstanceTypeMapAzure(dataCenterInstanceTypeMap);
					prerequisiteInfoInstanceBO.setStorageAccounts(serviceBOs);
					prerequisiteInfoInstanceBO.setNtwrkBOListAsNtwrkObj(getVirtualNetworkDetails(prerequisiteInfoInstanceBO));
					List<ResourceGroupDetailsBO> resourceGroupDetailsBOList=getResourceGroupDetails(prerequisiteInfoInstanceBO);
					if(resourceGroupDetailsBOList!=null && !resourceGroupDetailsBOList.isEmpty()){
						prerequisiteInfoInstanceBO.setResourceGroupDetailsBOList(resourceGroupDetailsBOList);	
					}
					List<SecurityGroupBO> securityGroupDetailsList= getAzureSecurityGroupDetails(prerequisiteInfoInstanceBO);
					if(securityGroupDetailsList!=null && !securityGroupDetailsList.isEmpty()){
						prerequisiteInfoInstanceBO.setSecurityGroup(securityGroupDetailsList);
					}
					List<ElasticIPBO> elasticIPDetailsList = getElasticIPDetails(prerequisiteInfoInstanceBO);
					if(elasticIPDetailsList!=null && !elasticIPDetailsList.isEmpty()){
						prerequisiteInfoInstanceBO.setElasticIPBOList(elasticIPDetailsList);
					}
				}
				if (prerequisiteInfoInstanceBO.getProviderType().equals(ProviderTypeEnum.TERREMARK_ECLOUD)){
					List<VirtualDiskBO> catalogs =getCatalogsFromDB(prerequisiteInfoInstanceBO) ;
					//If not available in database. fetch from cloud
					if(catalogs!=null){
						prerequisiteInfoInstanceBO.setVirtualDiskBOs(catalogs);
					}else{
						prerequisiteInfoInstanceBO.setVirtualDiskBOs(getCatalogsFromCloud(prerequisiteInfoInstanceBO));						
					}
					prerequisiteInfoInstanceBO.setClusterAndHostsMap(createEnvsCompPoolMap(prerequisiteInfoInstanceBO));
					prerequisiteInfoInstanceBO.setEnvNetworkMap(createEnvNtwrkMap(prerequisiteInfoInstanceBO,getNetworksFromCloud(prerequisiteInfoInstanceBO)));
					List<DeviceLayoutDO> deviceLayoutDOs = getDeviceLayouts(prerequisiteInfoInstanceBO);
					if(deviceLayoutDOs!=null && !deviceLayoutDOs.isEmpty()){
						prerequisiteInfoInstanceBO.setDevLayoutRowList(createDeviceLayoutRowList(deviceLayoutDOs));
					}
					else{
						prerequisiteInfoInstanceBO.setDevLayoutRowList(new ArrayList<String>());
					}					
					prerequisiteInfoInstanceBO.setDevLayoutGroupMap(createDeviceLayoutGroupMap(deviceLayoutDOs));
					prerequisiteInfoInstanceBO.setTrmrkKeyPairs(getKeyPairsFromCloud(prerequisiteInfoInstanceBO));
					if(dataCenterDOList.get(0).getProviderMasterDO() != null){
						prerequisiteInfoInstanceBO.setProviderName(dataCenterDOList.get(0).getProviderMasterDO().getProviderName());
					}
				}
				if (prerequisiteInfoInstanceBO.getProviderType().equals(ProviderTypeEnum.OPENSTACK)){
					Map<Integer, List<InstanceTypeBO>> dataCenterInstanceTypeMap =  new HashMap<>();
					Map<Integer, List<ImageInstanceBO>> dataCenterImageMap =  new HashMap<>();
					Map<Integer, List<KeyPairsBO>> dataCenterKeyPairMap =  new HashMap<>();
					Map<Integer, List<SecurityGroupBO>> dataCenterSecGrpMap =  new HashMap<>();
					
					for(DataCenterDetailsDO dcDO :dataCenterDOList){
						prerequisiteInfoInstanceBO.setDatacenterID(dcDO.getDataCenterId());
						List<InstanceTypeBO> flavors = getFlavorsFromDB(prerequisiteInfoInstanceBO);
						List<ImageInstanceBO> images = getOpenStackImagesFromDB(prerequisiteInfoInstanceBO);
						List<KeyPairsBO> keypairs = getKeyPairsFromDB(prerequisiteInfoInstanceBO);
						List<SecurityGroupBO> secGrps = getSecGrpsFromDB(prerequisiteInfoInstanceBO);
						prerequisiteInfoInstanceBO.setListamiImages(images);
						prerequisiteInfoInstanceBO.setInstanceTypes(flavors);
						dataCenterInstanceTypeMap.put(dcDO.getDataCenterId(), flavors);
						dataCenterImageMap.put(dcDO.getDataCenterId(), images);
						dataCenterKeyPairMap.put(dcDO.getDataCenterId(), keypairs);
						dataCenterSecGrpMap.put(dcDO.getDataCenterId(), secGrps);
					}
					prerequisiteInfoInstanceBO.setDataCenterInstanceTypeMap(dataCenterInstanceTypeMap);
					prerequisiteInfoInstanceBO.setDataCenterImageMap(dataCenterImageMap);
					prerequisiteInfoInstanceBO.setDataCenterKeyPairMap(dataCenterKeyPairMap);
					prerequisiteInfoInstanceBO.setDataCenterSecGrpMap(dataCenterSecGrpMap);
					setAvailableQuota(createBO,prerequisiteInfoInstanceBO);
				}
				if (prerequisiteInfoInstanceBO.getProviderType().equals(ProviderTypeEnum.IBMSL)){
					LinkedHashMap<Integer, DataCenterBO> dataCenterMap = new LinkedHashMap<>();
					Map<String, List<OSDescriptorBO>> osFlavorMap = null;
					Map<Integer, List<NetworkingBO>> datacenterVLANMap = null;
					List<KeyPairsBO> keypairsList = null;
					List<ImageInstanceBO> images = null;
					
					
					for(DataCenterDetailsDO dcDO :dataCenterDOList){
						DataCenterBO dataCenterBO = PlatformsProvider.getDataCenterBOFromDO(dcDO);
						dataCenterMap.put(dcDO.getDataCenterId(), dataCenterBO);
					}
					osFlavorMap = fetchOSFlavorsMapFromDB(prerequisiteInfoInstanceBO);
					datacenterVLANMap = fetchDatacenterVLANMapFromDB(prerequisiteInfoInstanceBO, dataCenterDOList);
					keypairsList = fetchKeypairsFromDB(prerequisiteInfoInstanceBO);
					images = fetchImages(prerequisiteInfoInstanceBO);
					prerequisiteInfoInstanceBO.setOsFlavorMap(osFlavorMap);
					prerequisiteInfoInstanceBO.setDatacenterVLANMap(datacenterVLANMap);
					prerequisiteInfoInstanceBO.setDataCenters(dataCenterMap);
					prerequisiteInfoInstanceBO.setTrmrkKeyPairs(keypairsList);
					prerequisiteInfoInstanceBO.setListamiImages(images);
				}
			}
			
			if(!prerequisiteInfoInstanceBO.getProviderType().equals(ProviderTypeEnum.PHYSICAL_SERVERS)){
			Map<Integer,String> computeProfileMasterMap=getComputeProfileMaster(prerequisiteInfoInstanceBO);
			prerequisiteInfoInstanceBO.setComputeProfileMasterMap(computeProfileMasterMap);
			}
			List<NotificationBO> notificationTemplates = new ArrayList<>();			
			
			List<NotificationDO>  notifications  = fetchNotifications(prerequisiteInfoInstanceBO.getCloudId());
					
			for (NotificationDO notificationDO : notifications) {
				NotificationBO notificationBO = NotificationProvider
						.getNotificationBOFromDO(notificationDO);
				notificationTemplates.add(notificationBO);	
			}
			prerequisiteInfoInstanceBO.setNotificationsList(notificationTemplates);			
		
                } catch(DALException | ICException | IllegalAccessException | InvocationTargetException e){
			throw new ICException(e);
		}
		return prerequisiteInfoInstanceBO;
	}
	
	private List<ImageInstanceBO> fetchImages(
			PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) throws ICException {
		List<ImageInstanceBO> imageBOs = null;
		ImageDetailsDAO imageDAO = getFactory().getImageDetailsDAO();
		ProviderMasterDO providerMasterDO = imageDAO.get(ProviderMasterDO.class, prerequisiteInfoInstanceBO.getProviderID());
		CloudMasterDO cloudMasterDO = imageDAO.get(CloudMasterDO.class, prerequisiteInfoInstanceBO.getCloudId());
		ImageDetailsDO imageDetailsDO = new ImageDetailsDO();
		imageDetailsDO.setProviderMasterDO(providerMasterDO);
		imageDetailsDO.setCloudMasterDO(cloudMasterDO);
		try {
			List<ImageDetailsDO> imageDetailsDOs = imageDAO.fetchByCloudIDProviderID(imageDetailsDO);
			if(imageDetailsDOs != null && !imageDetailsDOs.isEmpty()) {
				imageBOs = ImageDetailsProvider.getBOFromDO(imageDetailsDOs, null);
			}
		} catch (DALException e) {
			throw new ICException(e);
		}
		return imageBOs;
	}

	private List<NetworkingBO> getVirtualNetworkDetails(PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) 
			throws ICException {
		 List<NetworkingBO> networkingBOList = null;
		 NetworkingBO networkingBO = new NetworkingBO();
		 EniDetailsDAO  eniDetailsDAO = getFactory().getENIDetailsDAO();
		 EniDetailsDO eniDetailsDO = new EniDetailsDO();
		 eniDetailsDO.setProviderMasterDO(getFactory().getProviderMasterDAO()
				 .get(ProviderMasterDO.class, prerequisiteInfoInstanceBO.getProviderID())); 
		 eniDetailsDO.setCloudMasterDO(getFactory().getCloudMasterDAO()
				 .get(CloudMasterDO.class, prerequisiteInfoInstanceBO.getCloudId()));
		 try {
			 List<EniDetailsDO> eniDetailsDOList = eniDetailsDAO.fetchEnisByProviderIdCloudId(eniDetailsDO);
			 networkingBOList = NetworkMngmtProvider.getNetworkingBOFromDO(eniDetailsDOList, networkingBO);
		} catch (DALException | IllegalAccessException | InvocationTargetException e) {
			logger.error("DAL Error while fetching virtual network details");
			logger.error(e);
			throw new ICException(e);
		}
		 
		return networkingBOList;
	}

	private List<ResourceGroupDetailsBO> getResourceGroupDetails(PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) 
			throws ICException {
		 List<ResourceGroupDetailsBO> resourceGroupDetailsList = null;
		 ResourceGroupDetailsBO resourceGroupDetailsBO = new ResourceGroupDetailsBO();
		 ResourceGroupDetailsDAO  resourceGroupDetailsDAO = getFactory().getResourceGroupDetailsDAO();
		 ResourceGroupDetailsDO resourceGroupDetailsDO = new ResourceGroupDetailsDO();
		 resourceGroupDetailsDO.setProviderMasterDO(getFactory().getProviderMasterDAO()
				 .get(ProviderMasterDO.class, prerequisiteInfoInstanceBO.getProviderID())); 
		 resourceGroupDetailsDO.setCloudMasterDO(getFactory().getCloudMasterDAO()
				 .get(CloudMasterDO.class, prerequisiteInfoInstanceBO.getCloudId()));		
		 try {
			 List<ResourceGroupDetailsDO> resourceGroupDetailsDOList = resourceGroupDetailsDAO.fetchByCloudIdProviderId(resourceGroupDetailsDO);
			 resourceGroupDetailsList = ResourceGroupProvider.getResourceGroupDetailsBOFromDO(resourceGroupDetailsDOList, resourceGroupDetailsBO);
		} catch (DALException e) {
			logger.error("DAL Error while fetching Resource group details");
			logger.error(e);
			throw new ICException(e);
		}
		 
		return resourceGroupDetailsList;
	}
	private List<SecurityGroupBO> getAzureSecurityGroupDetails(PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) 
			throws ICException {
		 List<SecurityGroupBO> securityGroupDetailsList = null;
		 SecurityGroupBO securityGroupBO = new SecurityGroupBO();
		 SecurityGroupDAO  securityGroupDAO = getFactory().getSecurityGroupDAO();
		 SecurityGroupDO securityGroupDO = new SecurityGroupDO();
		 securityGroupDO.setProviderMasterDO(getFactory().getProviderMasterDAO()
				 .get(ProviderMasterDO.class, prerequisiteInfoInstanceBO.getProviderID())); 
		 securityGroupDO.setCloudMasterDO(getFactory().getCloudMasterDAO()
				 .get(CloudMasterDO.class, prerequisiteInfoInstanceBO.getCloudId()));
		 try {
			 List<SecurityGroupDO> securityGroupDOList = securityGroupDAO.fetchByCloudIdProviderId(securityGroupDO);
			 securityGroupDetailsList = SecurityProvider.getBOs(securityGroupDOList);
		} catch (DALException e) {
			logger.error("DAL Error while fetching security group details");
			logger.error(e);
			throw new ICException(e);
		}
		 
		return securityGroupDetailsList;
	}
	
	private List<ElasticIPBO> getElasticIPDetails(PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) 
			throws ICException {
		 List<ElasticIPBO> elasticIPDetailsList = null;
		 ElasticIPBO elasticIPBO = new ElasticIPBO();
		 ElasticIPDetailsDAO  elasticIPDetailsDAO = getFactory().getElasticIPDetailsDAO();
		 ElasticIPDetailsDO elasticIPDetailsDO = new ElasticIPDetailsDO();
		 elasticIPDetailsDO.setProviderMasterDO(getFactory().getProviderMasterDAO()
				 .get(ProviderMasterDO.class, prerequisiteInfoInstanceBO.getProviderID())); 
		 elasticIPDetailsDO.setCloudMasterDO(getFactory().getCloudMasterDAO()
				 .get(CloudMasterDO.class, prerequisiteInfoInstanceBO.getCloudId()));
		 try {
			 List<ElasticIPDetailsDO> elasticIPDetailsDOList = elasticIPDetailsDAO.fetchElasticIPsByCloudIdProviderId(elasticIPDetailsDO);
			 elasticIPDetailsList = ElasticIPProvider.getElasticIPBOsFromDOs(elasticIPDetailsDOList);
		} catch (DALException e) {
			logger.error("DAL Error while fetching ElasticIP details");
			logger.error(e);
			throw new ICException(e);
		}
		 
		return elasticIPDetailsList;
	}
	private List<ClusterBO> fetchClustersForSCVMM(PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) throws DALException{
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		ClusterMasterDAO clusterMasterDAO = factory.getClusterMasterDAO();
		List<ClusterMasterDO> clusterMasterDOs;
		List<ClusterBO> clusterBOs = new ArrayList<>();
		ClusterMasterDO clusterMasterDO = new ClusterMasterDO();
		CloudMasterDO cloudMasterDO = clusterMasterDAO.get(CloudMasterDO.class, prerequisiteInfoInstanceBO.getCloudId());		
		clusterMasterDO.setProviderMasterDO(clusterMasterDAO.get(ProviderMasterDO.class, prerequisiteInfoInstanceBO.getProviderID()));
		clusterMasterDOs = clusterMasterDAO.fetchByCloudIdProviderID(clusterMasterDO, cloudMasterDO);
		clusterBOs = PrerequisiteInfoProvider.populateClusterBOsFromDOs(clusterMasterDOs);
		return clusterBOs;
	}

	@Transactional
	private Map<Integer, String> fetchAllHostsForSCVMM(PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) throws DALException{
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		ServerMasterDAO serverMasterDAO = factory.getServerMasterDAO();
		List<ServerMasterDO> serverMasterDOs;
		Map<Integer, String> hostsMap = new HashMap<>();
		ServerMasterDO serverMasterDO = new ServerMasterDO();
		serverMasterDO.setCloudMasterDO(serverMasterDAO.get(CloudMasterDO.class, prerequisiteInfoInstanceBO.getCloudId()));
		serverMasterDO.setProviderMasterDO(serverMasterDAO.get(ProviderMasterDO.class, prerequisiteInfoInstanceBO.getProviderID()));
		serverMasterDOs = serverMasterDAO.fetchByCloudIdProviderId(serverMasterDO);
		if(serverMasterDOs != null && !serverMasterDOs.isEmpty()){
			for(ServerMasterDO smDO : serverMasterDOs){
				hostsMap.put(smDO.getServerId(), smDO.getServerName());
			}
		}
		return hostsMap;
	}

	@Transactional
	private List<SecurityGroupBO> getSecGrpsFromDB (
			PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) throws DALException{
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		SecurityGroupDAO securityGroupDAO = factory.getSecurityGroupDAO();
		List<SecurityGroupBO> secGrps = new ArrayList<>();
		List<SecurityGroupDO> secGrpsDO = new ArrayList<>();
		SecurityGroupDO securityGroupDO = new SecurityGroupDO();
		securityGroupDO.setDataCenterDetailsDO(securityGroupDAO.get(DataCenterDetailsDO.class,prerequisiteInfoInstanceBO.getDatacenterID()));
		securityGroupDO.setCloudMasterDO(securityGroupDAO.get(CloudMasterDO.class,prerequisiteInfoInstanceBO.getCloudId()));
		securityGroupDO.setProviderMasterDO(securityGroupDAO.get(ProviderMasterDO.class,prerequisiteInfoInstanceBO.getProviderID()));
		secGrpsDO = securityGroupDAO.fetchByCloudIdProviderIdDatacenterId(securityGroupDO);
		if(secGrpsDO!=null && !secGrpsDO.isEmpty()){
			secGrps= SecurityProvider.getBOs(secGrpsDO);
		}
		return secGrps;
	}

	@Transactional
	private List<KeyPairsBO> getKeyPairsFromDB(
			PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) throws DALException{
		KeypairDetailsDAO keyPairGroupDAO = getFactory().getKeypairDetailsDAO();
		List<KeyPairsBO> keyPairsBOs = new ArrayList<>();
		List<KeyPairDetailsDO> keyPairDOs = new ArrayList<>();
		KeyPairDetailsDO keyPairDetailsDO = new KeyPairDetailsDO();
		keyPairDetailsDO.setDataCenterDetailsDO(keyPairGroupDAO.get(DataCenterDetailsDO.class,prerequisiteInfoInstanceBO.getDatacenterID()));
		keyPairDetailsDO.setCloudMasterDO(keyPairGroupDAO.get(CloudMasterDO.class,prerequisiteInfoInstanceBO.getCloudId()));
		keyPairDetailsDO.setProviderMasterDO(keyPairGroupDAO.get(ProviderMasterDO.class,prerequisiteInfoInstanceBO.getProviderID()));
		keyPairDOs = keyPairGroupDAO.fetchByCloudIdProviderIdDatacenterId(keyPairDetailsDO);
		if(keyPairDOs!=null && !keyPairDOs.isEmpty()){
			for(KeyPairDetailsDO thisKeyPairDetailsDO :keyPairDOs){
				keyPairsBOs.add(KeyPairsProvider.getBOFromDO(thisKeyPairDetailsDO));
			}
		}
		return keyPairsBOs;
	}

	@Transactional
	private List<ImageInstanceBO> getOpenStackImagesFromDB(
			PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) throws DALException{
		ImageDetailsDAO imageDetailsDAO = getFactory().getImageDetailsDAO(); 
		List<ImageInstanceBO> imageBOs = new ArrayList<>();
		List<ImageDetailsDO> imageDOs = new ArrayList<>();
		ImageDetailsDO imageDetailsDO = new ImageDetailsDO();
		imageDetailsDO.setDataCenterDetailsDO(imageDetailsDAO.get(DataCenterDetailsDO.class,prerequisiteInfoInstanceBO.getDatacenterID()));
		imageDOs = imageDetailsDAO.fetchByDataCenterId(imageDetailsDO);
		if(imageDOs!=null && !imageDOs.isEmpty()){
			for(ImageDetailsDO imageDO :imageDOs){
				imageBOs.add(ImageDetailsProvider.getBOFromDO(imageDO));
			}
		}
		return imageBOs;
	}

	@Transactional
	private List<InstanceTypeBO> getFlavorsFromDB (
			PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) throws DALException{
		InstanceTypeDAO instTypeDAO = getFactory().getInstanceTypeDAO();
		List<InstanceTypeBO> flavorsBO = new ArrayList<>();
		List<InstanceTypeDO> flavorsDO = new ArrayList<>();
		InstanceTypeDO flavorDO = new InstanceTypeDO();
		flavorDO.setDataCenterDetailsDO(instTypeDAO.get(DataCenterDetailsDO.class,prerequisiteInfoInstanceBO.getDatacenterID()));
		flavorsDO = instTypeDAO.fetchByDataCenterId(flavorDO);
		if(flavorsDO!=null && !flavorsDO.isEmpty()){
			for(InstanceTypeDO thisFlavorDO: flavorsDO){
				flavorsBO.add(InstanceTypeProvider.getInstanceTypeBOfromDO(thisFlavorDO));
			}
		}
		return flavorsBO;
	}
	
	@Transactional
	private Map<String, List<OSDescriptorBO>> fetchOSFlavorsMapFromDB(PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) throws DALException{
		Map<String, List<OSDescriptorBO>> osFlavorsMap = new LinkedHashMap<>();
		List<OSDescriptorBO> osDescriptorBOs = null;
		List<OSDescriptorBO> osdescList = null;
		OsDetailsDAO osDetailsDAO = getFactory().getOsDetailsDAO();
		OsDetailsDO osDetailsDO = new OsDetailsDO();
		osDetailsDO.setProviderMasterDO(getFactory().getProviderMasterDAO()
				 .get(ProviderMasterDO.class, prerequisiteInfoInstanceBO.getProviderID())); 
		osDetailsDO.setCloudMasterDO(getFactory().getCloudMasterDAO()
				 .get(CloudMasterDO.class, prerequisiteInfoInstanceBO.getCloudId()));
		List<OsDetailsDO> osDetailsDOs = osDetailsDAO.fetchByCloudIdProviderId(osDetailsDO);
		osDescriptorBOs = OSResourcesDetailsProvider.getOSDescriptorBOsFromDOs(osDetailsDOs);
		for(OSDescriptorBO osDescriptorBO : osDescriptorBOs){
			osdescList = new ArrayList<>();
			for(OSDescriptorBO osDescriptorBO2 : osDescriptorBOs){
				if(osDescriptorBO.getOsFamily().equals(osDescriptorBO2.getOsFamily())){
					osdescList.add(osDescriptorBO2);
				}
			}
			osFlavorsMap.put(osDescriptorBO.getOsFamily(), osdescList);
		}
		return osFlavorsMap;
	}
	
	@Transactional
	private Map<Integer, List<NetworkingBO>> fetchDatacenterVLANMapFromDB(PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO,
			List<DataCenterDetailsDO> dataCenterDOList) throws DALException, IllegalAccessException, InvocationTargetException{
		Map<Integer, List<NetworkingBO>> vlanMap = new LinkedHashMap<>();
		List<NetworkingBO> networkingBOs = null;
		List<NetworkingBO> networkingBOSpecificDcIdList = null;
		NetworkingBO networkingBO = new NetworkingBO();
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		EniDetailsDAO eniDetailsDAO = factory.getENIDetailsDAO();
		EniDetailsDO eniDetailsDO = new EniDetailsDO();
		eniDetailsDO.setProviderMasterDO(getFactory().getProviderMasterDAO()
				 .get(ProviderMasterDO.class, prerequisiteInfoInstanceBO.getProviderID())); 
		eniDetailsDO.setCloudMasterDO(getFactory().getCloudMasterDAO()
				 .get(CloudMasterDO.class, prerequisiteInfoInstanceBO.getCloudId()));
		List<EniDetailsDO> eniDetailsDOs = eniDetailsDAO.fetchEnisByProviderIdCloudId(eniDetailsDO);
		networkingBOs = NetworkMngmtProvider.getNetworkingBOFromDO(eniDetailsDOs, networkingBO);
		for(DataCenterDetailsDO dataCenterDetailsDO : dataCenterDOList){
			networkingBOSpecificDcIdList = NetworkMngmtProvider.getNetworkingBOsForDcId(networkingBOs,dataCenterDetailsDO.getDataCenterId());
			vlanMap.put(dataCenterDetailsDO.getDataCenterId(), networkingBOSpecificDcIdList);
		}
		
		
		return vlanMap;
	}
	
	@Transactional
	private List<KeyPairsBO> fetchKeypairsFromDB(PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) throws DALException{

		List<KeyPairsBO> keyPairsBOs = new ArrayList<KeyPairsBO>();
		KeyPairsBO keyPairsBO = null;
		KeypairDetailsDAO keypairDAO = getFactory().getKeypairDetailsDAO();
		KeyPairDetailsDO keyPairDetailsDO = new KeyPairDetailsDO();
		keyPairDetailsDO.setProviderMasterDO(getFactory().getProviderMasterDAO()
				 .get(ProviderMasterDO.class, prerequisiteInfoInstanceBO.getProviderID())); 
		keyPairDetailsDO.setCloudMasterDO(getFactory().getCloudMasterDAO()
				 .get(CloudMasterDO.class, prerequisiteInfoInstanceBO.getCloudId()));
		List<KeyPairDetailsDO> keyPairDetailsDOs = keypairDAO.fetchByCloudIdProviderId(keyPairDetailsDO);
		for(KeyPairDetailsDO detailsDO : keyPairDetailsDOs){
			keyPairsBO = KeyPairsProvider.getBOFromDO(detailsDO);
			keyPairsBOs.add(keyPairsBO);
		}
		return keyPairsBOs;
	}
	
	@Transactional
	public Map<Integer, Map<Integer,String>> fetchAllHostsForDC(int dcId) {
		DataCenterDetailsDAO dcDAO = getFactory().getDataCenterDetailsDAO();
		DataCenterDetailsDO dcDO = dcDAO.get(DataCenterDetailsDO.class, dcId);
		Map<Integer, Map<Integer,String>> dcAndHosts = new HashMap<>();
		if(null!=dcDO){
			Set<ServerMasterDO> hosts = dcDO.getServerMasterDO();
			Map<Integer,String> allHosts = new HashMap<>();
			if(hosts != null) {
				for (ServerMasterDO serverMasterDO : hosts) {
					if(serverMasterDO.isActive()){
						allHosts.put(serverMasterDO.getServerId(),serverMasterDO.getServerName());
					}
				}
			}
			dcAndHosts.put(dcDO.getDataCenterId(), allHosts);
		}
		return dcAndHosts;
	}


	/**
	 * Fetches disk details to populate the drop down for OS while creating instances.
	 * @param prerequisiteInfoInstanceBO
	 * @return
	 * @throws DALException
	 */
	@Transactional
	public List<VirtualDiskBO> fetchDiskDetails(PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) throws DALException {
		VmDiskDetialsDAO vmDiskDetialsDAO = getFactory().getVmDiskDetialsDAO();
		
		List<VirtualDiskBO> virtualDiskBOs = new ArrayList<>();
		VmDiskDetialsDO vmDiskDetialsDO = new VmDiskDetialsDO();
		vmDiskDetialsDO.setCloudMasterDO(vmDiskDetialsDAO.get(CloudMasterDO.class, prerequisiteInfoInstanceBO.getCloudId()));
		vmDiskDetialsDO.setProviderMasterDO(vmDiskDetialsDAO.get(ProviderMasterDO.class, prerequisiteInfoInstanceBO.getProviderID()));
		List<VmDiskDetialsDO> vmDiskDetialsDOs = vmDiskDetialsDAO.fetchByCloudIdProviderId(vmDiskDetialsDO);
		virtualDiskBOs = PrerequisiteInfoProvider.getDiskBOFromDisksDO(vmDiskDetialsDOs, true);
		return virtualDiskBOs;
	}
	
	/**
	 * Method to  
	 * @param prerequisiteInfoInstanceBO
	 * @return
	 * @throws DALException
	 */ 
	@Transactional
	public List<ResourcePoolBO> fetchResourcePools(PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) {
		List<ResourcePoolBO> resourcePoolBOs;
		resourcePoolBOs = new ArrayList<>();
		try {
			ResourcePoolDAO resourcePoolDAO = getFactory().getResourcePoolDAO();
			ResourcePoolDO resourcePoolDO = new ResourcePoolDO();
			resourcePoolDO.setCloudMasterDO(resourcePoolDAO.get(
					CloudMasterDO.class,
					prerequisiteInfoInstanceBO.getCloudId()));
			resourcePoolDO.setProviderMasterDO(resourcePoolDAO.get(
					ProviderMasterDO.class,
					prerequisiteInfoInstanceBO.getProviderID()));
			List<ResourcePoolDO> resourcePoolDOs = resourcePoolDAO
					.fetchByCloudIdProviderId(resourcePoolDO);
			resourcePoolBOs = PrerequisiteInfoProvider
					.populateResourceBOsFromDOs(resourcePoolDOs);
		} catch (DALException e) {
			logger.error("Error while fetching resource pools");
			logger.error(e);
		}
		return resourcePoolBOs;
	}
	
	/**
	 * Method to fetch templates from InstanceDetails
	 * @param prerequisiteInfoInstanceBO
	 * @return
	 */
	private Map<Integer, InstanceBO> getTemplates(
			PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) {
		Map<Integer, InstanceBO> templateMap = null;
		List<InstanceDetailsDO> instanceDetailsDOs = fetchActiveTemplates(prerequisiteInfoInstanceBO);
		
		if (instanceDetailsDOs != null && !instanceDetailsDOs.isEmpty()){
			templateMap = new HashMap<>();
			for (InstanceDetailsDO instanceDetailsDO:instanceDetailsDOs){
				templateMap.put(instanceDetailsDO.getInstanceID(), InstanceProvider.getBOFromDO(instanceDetailsDO));
			}
		}
		return templateMap;
	}
	
	/**
	 * Method to fetch VHDs from OBJECTSTORAGECONTENT.
	 * @param prerequisiteInfoInstanceBO
	 * @return
	 */
	@Transactional
	public List<VirtualDiskBO> getVHDsFromDB(PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO){
		List<VirtualDiskBO> virtualDiskBOs = null;
		S3FileDetailsDAO s3FileDetailsDAO = getFactory().getS3FileDetailsDAO();
		S3FileDetailsDO s3FileDetailsDO = new S3FileDetailsDO();
		s3FileDetailsDO.setCloudMasterDO(s3FileDetailsDAO.get(CloudMasterDO.class, prerequisiteInfoInstanceBO.getCloudId()));
		s3FileDetailsDO.setProviderMasterDO(s3FileDetailsDAO.get(ProviderMasterDO.class, prerequisiteInfoInstanceBO.getProviderID()));
		
		try {
			List<S3FileDetailsDO> fileDetailsDOs = s3FileDetailsDAO.fetchByCloudIdProviderID(s3FileDetailsDO);
			
			if (fileDetailsDOs != null && !fileDetailsDOs.isEmpty()){
				virtualDiskBOs = new ArrayList<>();
				for (S3FileDetailsDO fileDetailsDO:fileDetailsDOs){
					VirtualDiskBO virtualDiskBO = new VirtualDiskBO();
					virtualDiskBO.setVdId(fileDetailsDO.getFileID());
					virtualDiskBO.setVdName(fileDetailsDO.getFileName());
					if (fileDetailsDO.getDataCenterDetailsDO() != null){
						virtualDiskBO.setDatacenterID(fileDetailsDO.getDataCenterDetailsDO().getDataCenterId());
						virtualDiskBO.setDatacenterName(fileDetailsDO.getDataCenterDetailsDO().getDataCenterName());
					}
					virtualDiskBOs.add(virtualDiskBO);
				}
			}
		} catch (DALException e) {
			logger.error("Error while fetching vhds");
			logger.error(e);
		}		
		return virtualDiskBOs;
	}
	
	
	/**
	 * Method to fetch Catalogs for Terremark from Cloud
	 * @param prerequisiteInfoInstanceBO
	 * @return
	 * To do : Will be modified to fetch from database later
	 * @throws ICException 
	 */
	private List<VirtualDiskBO> getCatalogsFromCloud(PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) throws ICException{
		List<VirtualDiskBO> virtualDiskBOs = null;
		try {
			InstanceDomainImpl instDomainImpl = new InstanceDomainImpl();
			virtualDiskBOs = instDomainImpl.fetchCatalogs(prerequisiteInfoInstanceBO);
		} catch (ICException e) {
			throw new ICException(e);
		}		
		return virtualDiskBOs;
	}
	
	/**
	 * Method to fetch Networks for Terremark from Cloud
	 * @param prerequisiteInfoInstanceBO
	 * @return
	 * To do : Will be modified to fetch from database later
	 * @throws ICException 
	 */
	private List<NetworkingBO> getNetworksFromCloud(PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) throws ICException{
		List<NetworkingBO> networkingBOs = null;
		try {
			InstanceDomainImpl instDomainImpl = new InstanceDomainImpl();
			networkingBOs = instDomainImpl.getNetworksFromCloud(prerequisiteInfoInstanceBO);
		} catch (ICException e) {
			throw new ICException(e);
		}		
		return networkingBOs;
	}
	
	/**
	 * @param prereqBO
	 * @return Environment and resource pool map
	 * @throws ICException 
	 * Method to fetch environments and respective compute pool 
	 */
	@Transactional
	public Map<Integer, Map<Integer,String>> createEnvsCompPoolMap(PrerequisiteInfoInstanceBO prereqBO) throws ICException {
		
		Map<Integer, Map<Integer,String>> clusterAndHostsMap = new HashMap<>();
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		ClusterMasterDAO cmDAO = factory.getClusterMasterDAO();
		ResourcePoolDAO resourcePoolDAO = factory.getResourcePoolDAO();
		ClusterMasterDO clusterDO = new ClusterMasterDO();
		CloudMasterDO cloudMasterDO = new CloudMasterDO();
		ProviderMasterDO provMasterDO = new ProviderMasterDO();
		cloudMasterDO = cmDAO.get(CloudMasterDO.class, prereqBO.getCloudId());
		provMasterDO = cmDAO.get(ProviderMasterDO.class, prereqBO.getProviderID());
		clusterDO.setProviderMasterDO(provMasterDO);
		List<ClusterMasterDO> clusterDOs;
		try {
			clusterDOs = cmDAO.fetchByCloudIdProviderID(clusterDO, cloudMasterDO);
			if(clusterDOs != null) {
				for (ClusterMasterDO clusterMasterDO : clusterDOs) {
						Map<Integer,String> resPools = new HashMap<>();
						ResourcePoolDO resPoolDO = new ResourcePoolDO();
						resPoolDO.setCloudMasterDO(cloudMasterDO);
						resPoolDO.setProviderMasterDO(provMasterDO);
						resPoolDO.setClusterMasterDO(clusterMasterDO);
						List<ResourcePoolDO> envResPools = resourcePoolDAO.fetchByCloudIdProviderIdClusterId(resPoolDO);
						if(envResPools != null) {
							for (ResourcePoolDO resourcePoolDO : envResPools) {
								resPools.put(Integer.valueOf(resourcePoolDO.getResourcePoolIdInProvider()),resourcePoolDO.getName());
							}						
						}
						clusterAndHostsMap.put(clusterMasterDO.getClusterID(), resPools);
				}			
			}
		} catch(DALException | NumberFormatException e){
			logger.error("Error while creating env and res pool map");
			logger.error(e);
			 throw new ICException(e);
		}
		return clusterAndHostsMap;
	}

	
	/**
	 * @param prereqBO
	 * @return Environment and network map
	 * @throws ICException 
	 * Method to fetch networks and respective env map 
	 */
	@Transactional
	public Map<Integer,List<NetworkingBO>> createEnvNtwrkMap(PrerequisiteInfoInstanceBO prereqBO,List<NetworkingBO> ntwrksBO) throws ICException {
		Map<Integer,List<NetworkingBO>> envNetworkMap = new HashMap<>();
		try {
			CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
			ProviderMasterDO provMasterDO = new ProviderMasterDO();
			ClusterMasterDAO cmDAO = factory.getClusterMasterDAO();
			ClusterMasterDO clusterMasterDO = new ClusterMasterDO();
			provMasterDO = cmDAO.get(ProviderMasterDO.class, prereqBO.getProviderID());
			CloudMasterDO cloudMasterDO = new CloudMasterDO();
			clusterMasterDO.setProviderMasterDO(provMasterDO);
			cloudMasterDO = cmDAO.get(CloudMasterDO.class, prereqBO.getCloudId());
			List<ClusterMasterDO> clusterDOs;
			clusterDOs = cmDAO.fetchByCloudIdProviderID(clusterMasterDO, cloudMasterDO);
			if(clusterDOs!=null){
				for (ClusterMasterDO cmDO : clusterDOs) {
					List<NetworkingBO> thisListNwBOs = new ArrayList<>();
					for(NetworkingBO ntwBO :ntwrksBO){
						if(cmDO.getMorId().equals(ntwBO.getEnvID())){
							ntwBO.setName(ntwBO.getName() + GlobalConstant.UNDERSCORE + ntwBO.getNetworkManagementType().toString());
							thisListNwBOs.add(ntwBO);
						}
					}
					envNetworkMap.put(cmDO.getClusterID(), thisListNwBOs);
				}
			}
		} catch (DALException e) {
			logger.error("DAL Error while creating env and network map");
			logger.error(e);
			throw new ICException(e);
		}
		return envNetworkMap;
	}
	
	/**
	 * @param prereqBO
	 * @return list of catalogs
	 * @throws ICException 
	 * Method to fetch catalogs from instance details table 
	 */
	private List<VirtualDiskBO> getCatalogsFromDB(PrerequisiteInfoInstanceBO prereqBO) throws ICException {
		List<VirtualDiskBO> catalogs = new ArrayList<>();
		try {
			//Gets list of catalogs
			List<InstanceDetailsDO> instanceDetailsDOs = fetchActiveTemplates(prereqBO);
			if(instanceDetailsDOs!=null && !instanceDetailsDOs.isEmpty()){
				for(InstanceDetailsDO instDO :instanceDetailsDOs){
					if(!GlobalConstant.TERREMARK_TEMPLATE.equalsIgnoreCase(instDO.getRootDeviceType())) {
						VirtualDiskBO catalog = new VirtualDiskBO();
						catalog.setVdName(instDO.getNickName());
						catalog.setVdId(instDO.getMorId().replace(CATALOG, GlobalConstant.BLANK_STRING));
						catalog.setNtwkName(instDO.getSubnet());
						catalog.setMemSize((int)instDO.getAllocatedMemory());
						catalog.setMemUnit(instDO.getNotes());
						catalog.setVdSize(instDO.getAllocatedCPU());
						catalogs.add(catalog);
					}
				}
			}
			logger.info("catalog list-->" + catalogs);
		}catch(Exception e){
			logger.error("Error while fetching catalogs from db");
			logger.error(e);
			 throw new ICException(e);
		}
		return catalogs;
	}


	private List<String> createDeviceLayoutRowList(
			List<DeviceLayoutDO> deviceLayoutDOs) {
		List<String> deviceLayoutRowList = new ArrayList<>();
		if(deviceLayoutDOs != null && !deviceLayoutDOs.isEmpty()) {
			for(DeviceLayoutDO deviceLayoutDO : deviceLayoutDOs) {
				if(!deviceLayoutRowList.contains(deviceLayoutDO.getRowName())) {
					deviceLayoutRowList.add(deviceLayoutDO.getRowName());
				}
			}
		}
		logger.info("deviceLayoutRowList--->" + deviceLayoutRowList);
		return deviceLayoutRowList;
	}
	
	private Map<String,List<String>> createDeviceLayoutGroupMap(
			List<DeviceLayoutDO> deviceLayoutDOs) {
		Map<String,List<String>> deviceLayoutGroupMap = new HashMap<>();
		if(deviceLayoutDOs != null && !deviceLayoutDOs.isEmpty()) {
			for(DeviceLayoutDO deviceLayoutDO : deviceLayoutDOs) {
				List<String> groupList = null;
				if(deviceLayoutGroupMap.containsKey(deviceLayoutDO.getRowName())) {
					groupList = deviceLayoutGroupMap.get(deviceLayoutDO.getRowName());
					groupList.add(deviceLayoutDO.getGroupName());
				} else {
					groupList = new ArrayList<>();
					groupList.add(deviceLayoutDO.getGroupName());					
				}
				deviceLayoutGroupMap.put(deviceLayoutDO.getRowName(), groupList);
			}
		}
		logger.info("deviceLayoutGroupList--->" + deviceLayoutGroupMap);
		return deviceLayoutGroupMap;
	}
	
	@Transactional
	public List<DeviceLayoutDO> getDeviceLayouts(
			PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) throws ICException {
		List<DeviceLayoutDO> deviceLayoutDOs = null;
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		DeviceLayoutDAO deviceLayoutDAO = factory.getDeviceLayoutDAO();
		ProviderMasterDO providerMasterDO = deviceLayoutDAO.get(ProviderMasterDO.class, prerequisiteInfoInstanceBO.getProviderID());
		CloudMasterDO cloudMasterDO = deviceLayoutDAO.get(CloudMasterDO.class, prerequisiteInfoInstanceBO.getCloudId());
		Set<ResourcePoolDO> resourcePoolDOs= providerMasterDO.getResourcePoolDOs();
		DeviceLayoutDO deviceLayoutDO = new DeviceLayoutDO();
		deviceLayoutDO.setCloudMasterDO(cloudMasterDO);
		deviceLayoutDO.setProviderMasterDO(providerMasterDO);
		if(resourcePoolDOs!=null && resourcePoolDOs.size()>0){
			for(ResourcePoolDO resourcePool:resourcePoolDOs){
				if(resourcePool.getClusterMasterDO().getClusterID()==prerequisiteInfoInstanceBO.getClusterID()){
					deviceLayoutDO.setResourcePoolDO(resourcePool);
				}
			}
		}
		try {
			deviceLayoutDOs = deviceLayoutDAO.fetchByCloudIdProviderIdResourcePoolId(deviceLayoutDO);

		} catch (DALException e) {
			logger.error("Error while fetching catalogs from db");
			logger.error(e);
			throw new ICException(e);
		}
		return deviceLayoutDOs;
	}
	
	/**
	 * Method to fetch keypairs for Terremark from Cloud
	 * @param prerequisiteInfoInstanceBO
	 * @return
	 * To do : Will be modified to fetch from database later
	 * @throws ICException 
	 */
	private List<KeyPairsBO> getKeyPairsFromCloud(PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) throws ICException{
		List<KeyPairsBO> keyPairs = null;
		try {
			InstanceDomainImpl instDomainImpl = new InstanceDomainImpl();
			keyPairs = instDomainImpl.getKeyPairsFromCloud(prerequisiteInfoInstanceBO);
		} catch (ICException e) {
			throw new ICException(e);
		}		
		return keyPairs;
	}
	private void setAvailableQuota(CreateInstanceInfoBO createInstanceInfoBO,PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO){
		
		HashMap<String,String> dataCenters=new HashMap<String,String>();
		Map<Integer,Map<String,Integer>> quotaValues;
		
		SLInstancesFacade slInstancesFacade = SLInstancesFacade.SINGLETON_INSTANCE;	
		IPlatformsDomain domain = new PlatformsDomainImpl();
		try{
			Map<Integer,DataCenterBO> dataCenterBOs=prerequisiteInfoInstanceBO.getDataCenters();
			PlatformBO platformBO = domain.fetchProvider(PlatformsProvider
				.getInputPlatformBO(prerequisiteInfoInstanceBO));
			CloudCredentialsBO cloudCredentialBO=PlatformsProvider.getProviderCredentials(platformBO);
			if(dataCenterBOs!=null && dataCenterBOs.size()>0){
				for(Map.Entry<Integer, DataCenterBO> entry:dataCenterBOs.entrySet()){
						DataCenterBO dataCenterBO=entry.getValue();
						dataCenters.put(String.valueOf(dataCenterBO.getDcId()),dataCenterBO.getDcName());
				}	
			cloudCredentialBO.setLstAccessbleDataCenters(dataCenters);
			quotaValues=slInstancesFacade.getAvailableQuota(createInstanceInfoBO,cloudCredentialBO);
			prerequisiteInfoInstanceBO.setAvailableQuota(quotaValues);
			}
		}
	catch(ICException|SLException e)
	{
		 logger.info("Error in retrieving Remaining Qutoa");
	}
  }
	
	/**
	 * Fetches the list of ComputeProfileMaster Associated with the cloud
	 * @param prerequisiteInfoInstanceBO
	 * @return
	 * @throws DALException
	 */
	@Transactional
	private Map<Integer,String> getComputeProfileMaster (
			PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) throws DALException{
		LinkedHashMap<Integer,String> computeProfileMap=new LinkedHashMap<>();
		ComputeProfileMasterDAO computeProfileMasterDAO = getFactory().getComputeProfileMasterDAO();
		List<ComputeProfileMasterDO> ComputeProfileMasterDOList ;
		ComputeProfileMasterDO computeProfileMasterDO = new ComputeProfileMasterDO();
		computeProfileMasterDO.setCloudMasterDO(computeProfileMasterDAO.get(CloudMasterDO.class,prerequisiteInfoInstanceBO.getCloudId()));
		computeProfileMasterDO.setActive(true);
		ComputeProfileMasterDOList = computeProfileMasterDAO.fetchByCloudID(computeProfileMasterDO);
		if(ComputeProfileMasterDOList!=null && !ComputeProfileMasterDOList.isEmpty()){
			for(ComputeProfileMasterDO profileMasterDO:ComputeProfileMasterDOList) {
				computeProfileMap.put(profileMasterDO.getComputeProfileMasterId(), profileMasterDO.getComputeProfileMasterName());
			}
		}
		return computeProfileMap;
	}
	
}
