package com.cognizant.cloudone.ic.instance.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.cognizant.cloudone.biz.instance.bo.ClusterBO;
import com.cognizant.cloudone.biz.instance.bo.DataCenterBO;
import com.cognizant.cloudone.biz.instance.bo.HostBO;
import com.cognizant.cloudone.biz.instance.bo.InstanceBO;
import com.cognizant.cloudone.biz.instance.bo.InventoryBO;
import com.cognizant.cloudone.biz.usersettings.UserGroupDetailsBO;
import com.cognizant.cloudone.dal.dao.DataCenterDetailsDAO;
import com.cognizant.cloudone.dal.dao.InstanceDetailsDAO;
import com.cognizant.cloudone.dal.dbo.CloudMasterDO;
import com.cognizant.cloudone.dal.dbo.CloudUserCredentialsDO;
import com.cognizant.cloudone.dal.dbo.ClusterMasterDO;
import com.cognizant.cloudone.dal.dbo.DataCenterDetailsDO;
import com.cognizant.cloudone.dal.dbo.DeviceLayoutDO;
import com.cognizant.cloudone.dal.dbo.InstanceDetailsDO;
import com.cognizant.cloudone.dal.dbo.PaaSDetailsDO;
import com.cognizant.cloudone.dal.dbo.ProviderMasterDO;
import com.cognizant.cloudone.dal.dbo.ResourcePoolDO;
import com.cognizant.cloudone.dal.dbo.ServerMasterDO;
import com.cognizant.cloudone.dal.dbo.UserGroupMasterDO;
import com.cognizant.cloudone.ic.base.impl.CloudOneDAOFactoryBuilder;
import com.cognizant.cloudone.ic.domain.factory.DomainFactory;
import com.cognizant.cloudone.ic.domain.factory.ICObjectEnum;
import com.cognizant.cloudone.ic.instance.provider.InventoryProvider;
import com.cognizant.cloudone.ic.usersettings.IUserGroupDomain;
import com.cognizant.cloudone.ic.utils.ICUtils;
import com.cognizant.cloudone.kernel.constants.GlobalConstant;
import com.cognizant.cloudone.kernel.constants.InstanceStateEnum;
import com.cognizant.cloudone.kernel.constants.ProviderTypeEnum;
import com.cognizant.cloudone.kernel.exception.DALException;
import com.cognizant.cloudone.kernel.exception.ICException;
import com.cognizant.cloudone.kernel.logger.CloudOneLogger;
import com.cognizant.cloudone.to.usersettings.UserDetailsTO;

/*
 * Helper class to handle refresh instances part.
 * This is a generic class which will be extended with other provider support as well.
 * TODO: Will merge existing providers support as well into this class.
 */
public class DefaultRefreshInstancesHelper extends BaseRefreshInstancesHelper {

	private static final CloudOneLogger logger = CloudOneLogger.getLogger(DefaultRefreshInstancesHelper.class.getName());
	private static final String INSTANCE_STATE_UNKNOWN="UnderCreation";
		public static String getVdcIdFromClusterUniqueId(String clusterMorId) {
                    return clusterMorId.substring(clusterMorId.indexOf(GlobalConstant.TILDE_SEPARATOR)+1);
                }
	
	@Transactional
	public List<InstanceDetailsDO> refreshHyperVInstances(String cmdb, CloudMasterDO cloudMasterDO, InventoryBO inventoryBO, DataCenterDetailsDAO dcDAO, List<ResourcePoolDO> savedResourcePoolsDOs,List<DataCenterDetailsDO> savedDCDOs, List<ClusterMasterDO> savedClusterDOs, List<ServerMasterDO> savedServerMasterDOs) throws DALException, 
                ICException {
        // Instances from cloud
            List<InstanceBO> clInstBOs = new ArrayList<>();
        if (inventoryBO.getDataCenters() != null && !inventoryBO.getDataCenters().isEmpty()) {
        	for(DataCenterBO dataCenterBO : inventoryBO.getDataCenters()) {
        		if(dataCenterBO.getHostBOs() != null && !dataCenterBO.getHostBOs().isEmpty()) {
        			for (HostBO hostBO : dataCenterBO.getHostBOs()) {
                        if (hostBO.getVms() != null) {
                            clInstBOs.addAll(hostBO.getVms());
                        }
                    }
        		}
        	}
            
        }
        // Instances from DB
        List<InstanceDetailsDO> instanceDOs = new ArrayList<>();
        ProviderMasterDO providerMasterDO = dcDAO.get(ProviderMasterDO.class,
                inventoryBO.getProviderID());
        Set<InstanceDetailsDO> instDOs = providerMasterDO.getInstanceDetailsDOs();
        if (instDOs != null) {
            instanceDOs.addAll(instDOs);
        }
        return getInstancesToSave(cmdb, dcDAO, cloudMasterDO, savedDCDOs, instanceDOs,
        		savedClusterDOs, null, clInstBOs, inventoryBO, savedResourcePoolsDOs, null,savedServerMasterDOs);
	}
	
	@Transactional
	public List<InstanceDetailsDO> refreshInstances(String cmdb, CloudMasterDO cloudMasterDO, InventoryBO inventoryBO, 
			List<ClusterMasterDO> savedClusterDOs, DataCenterDetailsDAO dcDAO, List<DataCenterDetailsDO> savedDCDOs, 
			List<PaaSDetailsDO> paasDetailsDOs, List<ResourcePoolDO> savedResourcePoolsDOs, 
			List<DeviceLayoutDO> savedDeviceLayoutDOs, List<ServerMasterDO> serverDOs) throws DALException, ICException {
        //Instances from cloud
            List<InstanceBO> clInstBOs = new ArrayList<>();
        if(inventoryBO.getDataCenters() != null) {
            for(DataCenterBO dcBO : inventoryBO.getDataCenters()) {
                if(dcBO.getInstances() != null) {
                    clInstBOs.addAll(dcBO.getInstances());
                }
                if(dcBO.getClusterBOs() != null) {
                    for(ClusterBO clusterBO : dcBO.getClusterBOs()) {
                        if(clusterBO.getVms() != null) {
                            clInstBOs.addAll(clusterBO.getVms());
                        }
                        if(clusterBO.getHostBOs()!=null){
                        	for(HostBO hostBO : clusterBO.getHostBOs()){
                        		if(hostBO.getVms()!=null){
                        			clInstBOs.addAll(hostBO.getVms());
                        		}
                        	}
                        }
                    }
                }
            }
        }
        //Instances from DB
        List<InstanceDetailsDO> instanceDOs = new ArrayList<>();
        Set<String> instanceMorIds=new HashSet<>();
        for (DataCenterDetailsDO dcDO : savedDCDOs) {
            //To avoid LazyInitializationException..
            DataCenterDetailsDO dataCenterDetailsDO = dcDAO.get(DataCenterDetailsDO.class,dcDO.getDataCenterId());
            Set<InstanceDetailsDO> instDOs = dataCenterDetailsDO.getInstanceDetailsDO();
            if(instDOs != null && !instDOs.isEmpty()) {
                for(InstanceDetailsDO instanceDetailsDO:instDOs){
                    if (instanceDetailsDO != null ){
                        instanceDOs.add(instanceDetailsDO);
                        instanceMorIds.add(instanceDetailsDO.getMorId());                        
                    }
                }
            }
        }

        if(inventoryBO.getProviderType()==ProviderTypeEnum.IBMSL){
        	InstanceDetailsDO instanceDetailsDO = new InstanceDetailsDO ();
			instanceDetailsDO.setCloudMasterDO(dcDAO.get(CloudMasterDO.class, inventoryBO.getCloudId()));
			instanceDetailsDO.setProviderMasterDO(dcDAO.get(ProviderMasterDO.class,inventoryBO.getProviderID()));
			InstanceDetailsDAO instancedetailsDAO=CloudOneDAOFactoryBuilder.getFactory().getInstanceDetailsDAO();
			List <InstanceDetailsDO> instanceListDO =instancedetailsDAO.fetchByCloudIdProviderId(instanceDetailsDO);
             if(instanceListDO != null && !instanceListDO.isEmpty()) {
                 for(InstanceDetailsDO instance:instanceListDO){
                     if (instance != null && !(instance.getState().equalsIgnoreCase(InstanceStateEnum.DELETED.getValue())) && !(instanceMorIds.contains(instance.getMorId()))){
                         instanceDOs.add(instance);
                         instanceMorIds.add(instance.getMorId());
                     }
                 }
             }
        }

        //Using savvis method here, since it satisfies Azure conditions as well.
        //Or else, will change it later on
        logger.info("clInstBOs size for saving in db :: "+clInstBOs.size());
         return getInstancesToSave(cmdb, dcDAO, cloudMasterDO, savedDCDOs, instanceDOs, savedClusterDOs, paasDetailsDOs, 
        		clInstBOs, inventoryBO,savedResourcePoolsDOs,savedDeviceLayoutDOs,serverDOs);
	}
	
        @Transactional
	public List<InstanceDetailsDO> getInstancesToSave(String cmdb, DataCenterDetailsDAO dcDAO, CloudMasterDO cloudMasterDO, 
			List<DataCenterDetailsDO> dcDOs, List<InstanceDetailsDO> instDOs, List<ClusterMasterDO> savedClusterDOs, 
			List<PaaSDetailsDO> paasDetailsDOs, List<InstanceBO> clInstBOs, InventoryBO inventoryBO, 
			List<ResourcePoolDO> savedResourcePoolsDOs, List<DeviceLayoutDO> savedDeviceLayoutDOs, 
			List<ServerMasterDO> serverDOs) throws DALException, ICException {
            List<InstanceDetailsDO> finalDOs = new ArrayList<>();
        Map<String, InstanceBO> instBOMap = new HashMap<>();
        Map<String, InstanceBO> instNameMap = new HashMap<>();
        Map<String, InstanceBO> templateNameMap = new HashMap<>();
        ServerMasterDO serverMasterDO = null;
        IUserGroupDomain ugDomain = (IUserGroupDomain) DomainFactory.getInstance(ICObjectEnum.USERSETTINGS);
        UserGroupDetailsBO ugBO = ugDomain.fetchSystemUserGroupDetailsForCloud(cloudMasterDO.getCloudID());
        UserGroupMasterDO sysUserGroupDO = dcDAO.get(UserGroupMasterDO.class, ugBO.getGroupId());
        cloudMasterDO = dcDAO.get(CloudMasterDO.class, cloudMasterDO.getCloudID());
        UserDetailsTO userDetailsTO = ICUtils.fetchOrgSystemUser(inventoryBO);
        CloudUserCredentialsDO cloudUserCredentialsDO = dcDAO.get(CloudUserCredentialsDO.class, userDetailsTO.getUserID());
        for (InstanceBO instanceBO : clInstBOs) {
            
            instBOMap.put(instanceBO.getMorId(), instanceBO);
            if(instanceBO.isTemplate()) {
                templateNameMap.put(instanceBO.getInstanceName(), instanceBO);
            }else{
                instNameMap.put(instanceBO.getInstanceName(), instanceBO);
            }
            
            if(inventoryBO.getProviderType()==ProviderTypeEnum.SCVMM && instanceBO.getState().equals(INSTANCE_STATE_UNKNOWN)){
            	instanceBO.setState(InstanceStateEnum.PENDING.getQueryString());
            }
            
        }
        logger.info("instBOMap from cloud :: "+instBOMap.toString());
        Map<String, InstanceDetailsDO> instDOMap = new HashMap<>();
        Set<String> deletedVMs = new HashSet<>();
        List<String> obsoleteIPVMs = new ArrayList<>();
        List<String> runningVMIPs = new ArrayList<>();
        ProviderMasterDO providerMasterDO = dcDAO.get(ProviderMasterDO.class, inventoryBO.getProviderID());
        for (InstanceDetailsDO instDO : instDOs) {
        	logger.info("mor id in db :: "+instDO.getMorId());
            if(instDO.getMorId() == null || instDO.getMorId().isEmpty()) {
                instDO = InventoryProvider.deActivateInstance(instDO, inventoryBO);
            }else if(instDO.getMorId() != null && !instBOMap.containsKey(instDO.getMorId())) { //(to be deleted)
//				if(vcenterExists) {
                if(instDO.isActive() && (null!=instDO.getInstanceType() 
                		&& !instDO.getInstanceType().equalsIgnoreCase(GlobalConstant.AZURE_V2))) {
                    //Instances eligible for this block is either already deleted or will be deleted in this transaction.
                   logger.info("deleted VM and processing for deleting cfg file :: "+instDO.getInstanceID());
                   getAllDeletedVMs().add(instDO.getInstanceID());
                    if (isVMMonitored(instDO)) {
                        getMonitoredDeletedVMs().add(instDO.getInstanceID());
                    }
                    // Already deleted instance not need to update
                    if(InstanceStateEnum.DELETED.getValue().equalsIgnoreCase(instDO.getState())) {
                        deletedVMs.add(instDO.getMorId());
                        
                        prepareVMsAndIPsLists(obsoleteIPVMs, runningVMIPs, instDO.getState(),
                                instDO.isActive(), instDO.getMorId(), instDO.getIpAddress(), instDO.getPrivate_IP());
                        continue;
                    } else {
                        InventoryProvider.markAsDeletedInstance(instDO, inventoryBO);
                        getCurrentTransDeletedVMs().add(instDO.getInstanceID());
                    }
                }
            }else{
                InstanceBO instBO = instBOMap.get(instDO.getMorId());
                validateInstanceForCMDBUpdate(instBO, instDO, cmdb);
                if(instBO != null && inventoryBO.getProviderType().getId()!= ProviderTypeEnum .TERREMARK_ECLOUD.getId()) {
                    
                    String clusterMorId = instBO.getHostMorId();
                    DataCenterDetailsDO dcDO = null;
                    ClusterMasterDO clusterMasterDO = null;
                    PaaSDetailsDO paasDetailsDO = null;
                    if(inventoryBO.getProviderType() == ProviderTypeEnum.SAVVIS
                            || inventoryBO.getProviderType() == ProviderTypeEnum.SAVVISVPDC) {
                        dcDO = InventoryProvider
                                .getDataCenterDO(instBO.getDatacenterName(),
                                        inventoryBO.getCloudId(),
                                        inventoryBO.getProviderID(), dcDOs);
                        String vdcId = getVdcIdFromClusterUniqueId(clusterMorId);
                        clusterMasterDO = InventoryProvider
                                .getClusterDOWithMatchingUniqueId(dcDO, savedClusterDOs, clusterMorId, vdcId);
                    } else if (inventoryBO.getProviderType() == ProviderTypeEnum.AZURE) {
                        dcDO = getDataCenterDetailsByDatacenterName(dcDAO, inventoryBO.getCloudId(), inventoryBO.getProviderID(), instBO.getDatacenterName());
                        clusterMasterDO = InventoryProvider.getClusterDOByUniqueId(clusterMorId, inventoryBO.getProviderID(), savedClusterDOs);
                        paasDetailsDO = InventoryProvider.getMatchingPaasDetailsDO(paasDetailsDOs, dcDO, clusterMasterDO, instBO, inventoryBO);
                    } else if (inventoryBO.getProviderType() == ProviderTypeEnum.OPENSTACK) {
	                   	 serverMasterDO = InventoryProvider.getServerMasterDO(instBO.getServerName(),inventoryBO.getCloudId(), inventoryBO.getProviderID(), serverDOs);
	                     dcDO = InventoryProvider.getDataCenterDO(instBO.getDatacenterName(), inventoryBO.getCloudId(), inventoryBO.getProviderID(), dcDOs);
	                     clusterMasterDO = InventoryProvider.getClusterDOByUniqueId(instBO.getClusterName(), inventoryBO.getProviderID(), savedClusterDOs);
                    } else if (inventoryBO.getProviderType() == ProviderTypeEnum.SCVMM) {
	                   	 serverMasterDO = InventoryProvider.getServerMasterDO(instBO.getServerName(),inventoryBO.getCloudId(), inventoryBO.getProviderID(), serverDOs);
	                     dcDO = InventoryProvider.getDataCenterDO(instBO.getDatacenterName(), inventoryBO.getCloudId(), inventoryBO.getProviderID(), dcDOs);
	                     clusterMasterDO = InventoryProvider.getClusterDO(instBO.getClusterName(), inventoryBO.getCloudId(), inventoryBO.getProviderID(), savedClusterDOs);
                   } else if (inventoryBO.getProviderType() == ProviderTypeEnum.IBMSL) {
                	   dcDO = InventoryProvider.getDataCenterDO(instBO.getDatacenterName(), inventoryBO.getCloudId(), inventoryBO.getProviderID(), dcDOs);
                   }
                    ResourcePoolDO resourcePoolDO = null;
                    if(savedResourcePoolsDOs != null) {
                        resourcePoolDO = InventoryProvider.getResourcePoolDOByMorId(instBO.getResourcePoolId(), providerMasterDO,savedResourcePoolsDOs );
                    }
                    instDO = InventoryProvider.getInstanceDOToUpdate(
                            sysUserGroupDO, cloudUserCredentialsDO, instDO,
                            instBO, dcDO, clusterMasterDO, serverMasterDO, null, paasDetailsDO, inventoryBO,
                            true, null, resourcePoolDO);
                } else {
                    
                    DeviceLayoutDO deviceLayoutDO = null;
                    DataCenterDetailsDO dcDO = instDO.getDataCenterDetailsDO();
                    ClusterMasterDO clusterMasterDO = instDO.getClusterMasterDO();
                    //13305: handle VM update for TMRK, such as moving to another group
                    deviceLayoutDO = InventoryProvider.getDeviceLayoutDOByDeviceLayoutId(instBO.getDeviceLayoutId(),instDO.getResourcePoolDO(),savedDeviceLayoutDOs);
                    logger.info("deviceLayoutDO-to be updated--->" + deviceLayoutDO.getDeviceLayoutId() +"----" + instDO.getInstanceName());
                    instDO = InventoryProvider.getInstanceDOToUpdate(
                            sysUserGroupDO, cloudUserCredentialsDO, instDO,
                            instBO, dcDO, clusterMasterDO, null, null, null, inventoryBO,
                            true,deviceLayoutDO, null);
                    
                }
            }
            finalDOs.add(instDO);
            
            instDOMap.put(instDO.getMorId(), instDO);
            if(InstanceStateEnum.DELETED.getValue().equalsIgnoreCase(instDO.getState())
                    || InstanceStateEnum.TERMINATE.getValue().equalsIgnoreCase(instDO.getState())) {
                deletedVMs.add(instDO.getMorId());
            }
            prepareVMsAndIPsLists(obsoleteIPVMs, runningVMIPs, instDO.getState(),
                    instDO.isActive(), instDO.getMorId(), instDO.getIpAddress(), instDO.getPrivate_IP());
            
        }
        //Set<String> uniqueInstNames = new HashSet<String>();
        //Set<String> uniqueTemplateNames = new HashSet<String>();
        for (InstanceBO instBO : clInstBOs) {
            if(deletedVMs.contains(instBO.getMorId())) {
                //If any template or VM has been already added by the same name, then do not add any more.
                //If any DELETED instance is already present in DB, then bypass the inserts as DB would not support it.
                //This is to ensure refresh functionality as such does not fail in this case.
                
                continue;
            }
            InstanceBO instanceBO = instBO;
            
            if(instanceBO != null) {
                //Add new instances not yet in DB
                if(!instDOMap.keySet().contains(instanceBO.getMorId())) {
                    
                    String clusterMorId = instBO.getHostMorId();
                    if (inventoryBO.getProviderType() == ProviderTypeEnum.TERREMARK_ECLOUD){
                        clusterMorId = ""+instBO.getClusterID();
                    }
                    
                    DataCenterDetailsDO dcDO = null;
                    ClusterMasterDO clusterMasterDO = null;
                    PaaSDetailsDO paasDetailsDO = null;
                    
                    ///133053 : for CO-12301
                    DeviceLayoutDO deviceLayoutDO = null;
                    ResourcePoolDO resourcePoolDO = null;
                    
                    if(inventoryBO.getProviderType() == ProviderTypeEnum.SAVVIS || inventoryBO.getProviderType() == ProviderTypeEnum.SAVVISVPDC) {
                        dcDO = InventoryProvider
                                .getDataCenterDO(instBO.getDatacenterName(),
                                        inventoryBO.getCloudId(),
                                        inventoryBO.getProviderID(), dcDOs);
                        String vdcId = getVdcIdFromClusterUniqueId(clusterMorId);
                        clusterMasterDO = InventoryProvider
                                .getClusterDOWithMatchingUniqueId(dcDO, savedClusterDOs, clusterMorId, vdcId);
                    } else if (inventoryBO.getProviderType() == ProviderTypeEnum.AZURE) {
                        dcDO = InventoryProvider.getDataCenterDO(instBO.getDatacenterName(), inventoryBO.getCloudId(), inventoryBO.getProviderID(), dcDOs);
                        clusterMasterDO = InventoryProvider.getClusterDOByUniqueId(clusterMorId, inventoryBO.getProviderID(), savedClusterDOs);
                        paasDetailsDO = InventoryProvider.getMatchingPaasDetailsDO(paasDetailsDOs, dcDO, clusterMasterDO, instanceBO, inventoryBO);
					} else if (inventoryBO.getProviderType() == ProviderTypeEnum.OPENSTACK) {
						serverMasterDO = InventoryProvider.getServerMasterDO(instBO.getServerName(),inventoryBO.getCloudId(),inventoryBO.getProviderID(), serverDOs);
						dcDO = InventoryProvider.getDataCenterDO(instBO.getDatacenterName(),inventoryBO.getCloudId(),
								inventoryBO.getProviderID(), dcDOs);
						clusterMasterDO = InventoryProvider.getClusterDOByUniqueId(instBO.getClusterName(),inventoryBO.getProviderID(),savedClusterDOs);
					}
                    else if (inventoryBO.getProviderType() == ProviderTypeEnum.TERREMARK_ECLOUD
                            || inventoryBO.getProviderType() == ProviderTypeEnum.SCVMM) {
                        dcDO = InventoryProvider.getDataCenterDOByDCMorID( inventoryBO.getCloudId(), inventoryBO.getProviderID(),
                                ""+instBO.getDatacenterID(), dcDOs);
                        clusterMasterDO = InventoryProvider.getClusterDOByUniqueId(clusterMorId, inventoryBO.getProviderID(), savedClusterDOs);
                        logger.info("resourcePool id---->" + instanceBO.getResourcePoolId());
                        resourcePoolDO = InventoryProvider.getResourcePoolDOByMorId(instanceBO.getResourcePoolId(), providerMasterDO,savedResourcePoolsDOs );
                        logger.info("resourcePool DO---->" + resourcePoolDO);
                        if(resourcePoolDO != null) {
                            deviceLayoutDO = InventoryProvider.getDeviceLayoutDOByDeviceLayoutId(instanceBO.getDeviceLayoutId(),resourcePoolDO,savedDeviceLayoutDOs);
                        }
                        //prepare deviceLayoutDO - iterate through saved layouts and match group ID in table to vmBO.getDeviceLayoutId
                        
                        // prepare resourcePoolDO - iterate through saved resource pools and match vmBO.setResourcePoolId to
                        // computePoolProviderID in the resource pool table
                        
                    }
                    else if (inventoryBO.getProviderType() == ProviderTypeEnum.IBMSL) {
                 	   dcDO = InventoryProvider.getDataCenterDO(instBO.getDatacenterName(), inventoryBO.getCloudId(), inventoryBO.getProviderID(), dcDOs);
                    }
                    
                    InstanceDetailsDO instanceDetailsDO = InventoryProvider
                            .getInstanceDOToInsert(sysUserGroupDO,
                                    cloudUserCredentialsDO, cloudMasterDO,
                                    instanceBO, inventoryBO, clusterMasterDO,
                                    dcDO, serverMasterDO, paasDetailsDO, providerMasterDO, deviceLayoutDO ,resourcePoolDO);
                    // Commenting out below line as part of CO-15574. We'll
                    // treat new instances separately, because for CMDB
                    // registration, we need the generated ids after save. This
                    // will be done as part of the calling class.
//					finalDOs.add(instanceDetailsDO);
                    getCurrentTransNewVMs().add(instanceDetailsDO);
                    
                    prepareVMsAndIPsLists(obsoleteIPVMs, runningVMIPs, instanceDetailsDO.getState(),
                            instanceDetailsDO.isActive(), instanceDetailsDO.getMorId(), instanceDetailsDO.getIpAddress(), instanceDetailsDO.getPrivate_IP());
                }
            }
        }
        nullifyMorIDBasedIPs(obsoleteIPVMs, runningVMIPs, instDOs);
        return finalDOs;
	}
	
	/**
	 * Method to fetch the datacenter details using cloudid , provider id and datacentername
	 * @param dataCenterDetailsDAO 
	 * @param cloudId
	 * @param providerId
	 * @param dcName
	 * @return
	 * @throws DALException
	 */
	@Transactional
	public  DataCenterDetailsDO getDataCenterDetailsByDatacenterName (DataCenterDetailsDAO dataCenterDetailsDAO, int cloudId, int providerId, String dcName) throws  DALException{
		
		DataCenterDetailsDO dataCenterDetailsDO = new DataCenterDetailsDO();
		dataCenterDetailsDO.setDataCenterName(dcName);
		CloudMasterDO cloudMasterDO = dataCenterDetailsDAO.get(CloudMasterDO.class, cloudId);
		ProviderMasterDO providerMasterDO = dataCenterDetailsDAO.get(ProviderMasterDO.class, providerId);
		dataCenterDetailsDO.setCloudMasterDO(cloudMasterDO);
		dataCenterDetailsDO.setProviderMasterDO(providerMasterDO);
		
		try {
			dataCenterDetailsDO = dataCenterDetailsDAO.fetchByCloudIDProviderIDDataCenterName(dataCenterDetailsDO);
			return dataCenterDetailsDO;
		} catch (DALException e) {
			throw new DALException("Cloud id: " + cloudId +" does not have access to this datacenter: " + dcName);
		}
	}
}
