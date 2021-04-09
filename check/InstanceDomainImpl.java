package com.cognizant.cloudone.ic.instance.impl;

import static com.cognizant.cloudone.kernel.constants.MessageQueueConstants.ENABLE_MESSAGING;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cognizant.cloudone.assetmgmnt.bean.AssetBean;
import com.cognizant.cloudone.biz.base.bo.BaseBO;
import com.cognizant.cloudone.biz.base.bo.BaseCredentialsBO;
import com.cognizant.cloudone.biz.base.bo.CloudCredentialsBO;
import com.cognizant.cloudone.biz.changecloud.CloudDetailsBO;
import com.cognizant.cloudone.biz.elasticip.ElasticIPBO;
import com.cognizant.cloudone.biz.instance.bo.CreateInstanceInfoBO;
import com.cognizant.cloudone.biz.instance.bo.DataCenterBO;
import com.cognizant.cloudone.biz.instance.bo.DiskUsageBO;
import com.cognizant.cloudone.biz.instance.bo.ImageInstanceBO;
import com.cognizant.cloudone.biz.instance.bo.InstanceBO;
import com.cognizant.cloudone.biz.instance.bo.InstanceDeploymentBO;
import com.cognizant.cloudone.biz.instance.bo.InstanceGroupBO;
import com.cognizant.cloudone.biz.instance.bo.InstanceTypeBO;
import com.cognizant.cloudone.biz.instance.bo.InventoryBO;
import com.cognizant.cloudone.biz.instance.bo.OSDescriptorBO;
import com.cognizant.cloudone.biz.instance.bo.PaaSDetailsBO;
import com.cognizant.cloudone.biz.instance.bo.PrerequisiteInfoInstanceBO;
import com.cognizant.cloudone.biz.instance.bo.ProcessInstanceBO;
import com.cognizant.cloudone.biz.instance.bo.ServiceBO;
import com.cognizant.cloudone.biz.instance.bo.ServiceScalingBO;
import com.cognizant.cloudone.biz.instance.bo.VMCustomizationSpecInfoBO;
import com.cognizant.cloudone.biz.instance.bo.VirtualDiskBO;
import com.cognizant.cloudone.biz.instance.bo.VpcBO;
import com.cognizant.cloudone.biz.instance.bo.WSAuditBO;
import com.cognizant.cloudone.biz.integration.IntegrationSystemBO;
import com.cognizant.cloudone.biz.jobmgmt.JobMgmtBO;
import com.cognizant.cloudone.biz.keypairs.KeyPairsBO;
import com.cognizant.cloudone.biz.monitor.MonitorableBO;
import com.cognizant.cloudone.biz.monitor.bo.MonitorTemplateBO;
import com.cognizant.cloudone.biz.networking.NetworkingBO;
import com.cognizant.cloudone.biz.organization.OrganizationBO;
import com.cognizant.cloudone.biz.platforms.PlatformBO;
import com.cognizant.cloudone.biz.recenttasks.bo.WorkerQueueBO;
import com.cognizant.cloudone.biz.scheduling.RecurrenceBO;
import com.cognizant.cloudone.bizrules.constants.PolicyConstants;
import com.cognizant.cloudone.bizrules.policy.bean.ProvisioningPolicyResult;
import com.cognizant.cloudone.bizrules.policy.exception.PolicyException;
import com.cognizant.cloudone.bizrules.policy.rules.entities.Instance;
import com.cognizant.cloudone.co.assetmgmnt.AssetCO;
import com.cognizant.cloudone.common.bean.notification.EditVMResourceBean;
import com.cognizant.cloudone.common.bean.notification.ProvisionBean;
import com.cognizant.cloudone.common.bean.scheduler.JobDataBean;
import com.cognizant.cloudone.common.bean.scheduler.JobDetailsBean;
import com.cognizant.cloudone.common.bean.scheduler.ScheduleDetailsBean;
import com.cognizant.cloudone.common.bean.scheduler.TriggerDetailsBean;
import com.cognizant.cloudone.dal.constant.DatabaseSQLConstant;
import com.cognizant.cloudone.dal.dao.ApplicationCopyDAO;
import com.cognizant.cloudone.dal.dao.CloudUserCredentialDAO;
import com.cognizant.cloudone.dal.dao.ClusterMasterDAO;
import com.cognizant.cloudone.dal.dao.DataCenterDetailsDAO;
import com.cognizant.cloudone.dal.dao.DataStoresDAO;
import com.cognizant.cloudone.dal.dao.DeploymentMasterDAO;
import com.cognizant.cloudone.dal.dao.EBSSnapshotDetailsDAO;
import com.cognizant.cloudone.dal.dao.EBSVolumeDetailsDAO;
import com.cognizant.cloudone.dal.dao.ImageDetailsDAO;
import com.cognizant.cloudone.dal.dao.InstanceDetailsDAO;
import com.cognizant.cloudone.dal.dao.InstanceGroupMasterDAO;
import com.cognizant.cloudone.dal.dao.InstanceTypeDAO;
import com.cognizant.cloudone.dal.dao.OsDetailsDAO;
import com.cognizant.cloudone.dal.dao.PaaSDetailsDAO;
import com.cognizant.cloudone.dal.dao.ServerMasterDAO;
import com.cognizant.cloudone.dal.dao.TagMappingDAO;
import com.cognizant.cloudone.dal.dao.TaskDetailsDAO;
import com.cognizant.cloudone.dal.dao.TaskStatusDAO;
import com.cognizant.cloudone.dal.dao.VmDiskDetialsDAO;
import com.cognizant.cloudone.dal.dao.WSAuditDetailsDAO;
import com.cognizant.cloudone.dal.dao.factory.CloudOneDAOFactory;
import com.cognizant.cloudone.dal.dbo.ApplicationCopyDO;
import com.cognizant.cloudone.dal.dbo.CloudMasterDO;
import com.cognizant.cloudone.dal.dbo.CloudUserCredentialsDO;
import com.cognizant.cloudone.dal.dbo.ClusterMasterDO;
import com.cognizant.cloudone.dal.dbo.DataCenterDetailsDO;
import com.cognizant.cloudone.dal.dbo.DataStoresDO;
import com.cognizant.cloudone.dal.dbo.DeploymentMasterDO;
import com.cognizant.cloudone.dal.dbo.EBSSnapshotDetailsDO;
import com.cognizant.cloudone.dal.dbo.EBSVolumeDetailsDO;
import com.cognizant.cloudone.dal.dbo.ElasticIPDetailsDO;
import com.cognizant.cloudone.dal.dbo.ImageDetailsDO;
import com.cognizant.cloudone.dal.dbo.InstanceDetailsDO;
import com.cognizant.cloudone.dal.dbo.InstanceGroupMasterDO;
import com.cognizant.cloudone.dal.dbo.InstanceTypeDO;
import com.cognizant.cloudone.dal.dbo.OsDetailsDO;
import com.cognizant.cloudone.dal.dbo.PaaSDetailsDO;
import com.cognizant.cloudone.dal.dbo.ProviderMasterDO;
import com.cognizant.cloudone.dal.dbo.ResourcePoolDO;
import com.cognizant.cloudone.dal.dbo.ServerMasterDO;
import com.cognizant.cloudone.dal.dbo.TagMappingDO;
import com.cognizant.cloudone.dal.dbo.TagMasterDO;
import com.cognizant.cloudone.dal.dbo.TaskDetailsDO;
import com.cognizant.cloudone.dal.dbo.TaskStatusDO;
import com.cognizant.cloudone.dal.dbo.TemplateDetailsDO;
import com.cognizant.cloudone.dal.dbo.VmDiskDetialsDO;
import com.cognizant.cloudone.dal.dbo.WSAuditDetailsDO;
import com.cognizant.cloudone.dal.wrapper.CloudOneResultSet;
import com.cognizant.cloudone.ic.appgroups.impl.AppGroupsDomainImpl;
import com.cognizant.cloudone.ic.assetmngmt.facade.AssetMgmntFacade;
import com.cognizant.cloudone.ic.assetmngmt.provider.AssetMgmntProvider;
import com.cognizant.cloudone.ic.assetmngmt.util.AssetManagementUtil;
import com.cognizant.cloudone.ic.async.messaging.impl.ParallelProcessingService;
import com.cognizant.cloudone.ic.autoscale.helper.TagsHelper;
import com.cognizant.cloudone.ic.autoscale.impl.LocalScriptCommandImpl;
import com.cognizant.cloudone.ic.base.impl.BaseDomainImpl;
import com.cognizant.cloudone.ic.base.impl.CloudOneDAOFactoryBuilder;
import com.cognizant.cloudone.ic.constants.ActionEnum;
import com.cognizant.cloudone.ic.constants.DashBoardConstants;
import com.cognizant.cloudone.ic.deployments.impl.DeploymentsDomainImpl;
import com.cognizant.cloudone.ic.domain.factory.DomainFactory;
import com.cognizant.cloudone.ic.domain.factory.ICObjectEnum;
import com.cognizant.cloudone.ic.instance.ICreateInstanceDomain;
import com.cognizant.cloudone.ic.instance.IInstanceDomain;
import com.cognizant.cloudone.ic.instance.IPrereqInstanceDomain;
import com.cognizant.cloudone.ic.instance.IRefreshInventoryDomain;
import com.cognizant.cloudone.ic.instance.factory.CreateInstanceFactory;
import com.cognizant.cloudone.ic.instance.factory.PrerequisiteInstanceFactory;
import com.cognizant.cloudone.ic.instance.factory.RefreshInventoryFactory;
import com.cognizant.cloudone.ic.instance.helper.InstanceHelper;
import com.cognizant.cloudone.ic.instance.provider.CreateInstanceInfoProvider;
import com.cognizant.cloudone.ic.instance.provider.HostProvider;
import com.cognizant.cloudone.ic.instance.provider.ImageDetailsProvider;
import com.cognizant.cloudone.ic.instance.provider.InstanceProvider;
import com.cognizant.cloudone.ic.instance.provider.InstanceTypeProvider;
import com.cognizant.cloudone.ic.instance.provider.OSResourcesDetailsProvider;
import com.cognizant.cloudone.ic.instance.provider.PaaSProvider;
import com.cognizant.cloudone.ic.jobmgmt.IJobMgmtDomain;
import com.cognizant.cloudone.ic.jobmgmt.util.JobMgmtUtil;
import com.cognizant.cloudone.ic.license.validity.aspect.ValidateLicense;
import com.cognizant.cloudone.ic.loadbalancer.impl.LoadBalancerDomainImpl;
import com.cognizant.cloudone.ic.monitor.IMonitorDomain;
import com.cognizant.cloudone.ic.platforms.IPlatformsDomain;
import com.cognizant.cloudone.ic.platforms.impl.PlatformsDomainImpl;
import com.cognizant.cloudone.ic.platforms.provider.PlatformsProvider;
import com.cognizant.cloudone.ic.quotamgmt.IQuotaValidationDomain;
import com.cognizant.cloudone.ic.quotamgmt.factory.QuotaObjectFactory;
import com.cognizant.cloudone.ic.reservation.helper.ReservationHelper;
import com.cognizant.cloudone.ic.servicecatalog.IServiceCatalogDomain;
import com.cognizant.cloudone.ic.serviceconsole.IServiceConsoleDomain;
import com.cognizant.cloudone.ic.utils.ICUtils;
import com.cognizant.cloudone.ic.utils.InstanceActionUtil;
import com.cognizant.cloudone.ic.utils.InstanceFilter;
import com.cognizant.cloudone.ic.workerQueue.WorkerQueueHelper;
import com.cognizant.cloudone.ic.workerQueue.WorkerQueueManager;
import com.cognizant.cloudone.kernel.cache.CloudOneCache;
import com.cognizant.cloudone.kernel.cache.CloudOneCacheDataWrapper;
import com.cognizant.cloudone.kernel.cache.ICache;
import com.cognizant.cloudone.kernel.constants.AMIDetailsEnum;
import com.cognizant.cloudone.kernel.constants.AzureDiskSizeEnum;
import com.cognizant.cloudone.kernel.constants.ConfigParamsEnum;
import com.cognizant.cloudone.kernel.constants.DateTimeFormatEnum;
import com.cognizant.cloudone.kernel.constants.EntityTypeEnum;
import com.cognizant.cloudone.kernel.constants.GlobalConstant;
import com.cognizant.cloudone.kernel.constants.InstanceMonitoringStatusEnum;
import com.cognizant.cloudone.kernel.constants.InstanceStateEnum;
import com.cognizant.cloudone.kernel.constants.InstanceStorageActionEnum;
import com.cognizant.cloudone.kernel.constants.InstanceTypesEnum;
import com.cognizant.cloudone.kernel.constants.InstancesProvisioningConstants;
import com.cognizant.cloudone.kernel.constants.MessageQueueConstants;
import com.cognizant.cloudone.kernel.constants.OSTypesEnum;
import com.cognizant.cloudone.kernel.constants.ObjectStateEnum;
import com.cognizant.cloudone.kernel.constants.PermissionEnum;
import com.cognizant.cloudone.kernel.constants.PhysicalServerOSEnum;
import com.cognizant.cloudone.kernel.constants.PrerequisiteInformationParamaterEnum;
import com.cognizant.cloudone.kernel.constants.ProviderTypeEnum;
import com.cognizant.cloudone.kernel.constants.RefreshTypeEnum;
import com.cognizant.cloudone.kernel.constants.SavvisOSEnum;
import com.cognizant.cloudone.kernel.constants.StateEnum;
import com.cognizant.cloudone.kernel.constants.TagsConstants;
import com.cognizant.cloudone.kernel.constants.TagsEnum;
import com.cognizant.cloudone.kernel.constants.TaskDescriptionEnum;
import com.cognizant.cloudone.kernel.constants.TerremarkCategoryEnum;
import com.cognizant.cloudone.kernel.constants.UserTypeEnum;
import com.cognizant.cloudone.kernel.constants.bizrules.UserActionEnum;
import com.cognizant.cloudone.kernel.error.constants.ErrorConstants;
import com.cognizant.cloudone.kernel.exception.DALException;
import com.cognizant.cloudone.kernel.exception.ICException;
import com.cognizant.cloudone.kernel.exception.InvalidLicenseException;
import com.cognizant.cloudone.kernel.exception.NotificationException;
import com.cognizant.cloudone.kernel.exception.SLException;
import com.cognizant.cloudone.kernel.exception.SchedulerException;
import com.cognizant.cloudone.kernel.exception.ServiceException;
import com.cognizant.cloudone.kernel.filter.Filter;
import com.cognizant.cloudone.kernel.filter.enums.FilterTypeEnum;
import com.cognizant.cloudone.kernel.filter.enums.InstanceImageFilterEnum;
import com.cognizant.cloudone.kernel.instance.enums.InstancesActionsEnum;
import com.cognizant.cloudone.kernel.logger.CloudOneLogger;
import com.cognizant.cloudone.kernel.message.constants.MessageConstants;
import com.cognizant.cloudone.kernel.message.constants.MessageStringConstants;
import com.cognizant.cloudone.kernel.scheduler.bean.InstanceDataBean;
import com.cognizant.cloudone.kernel.scheduler.bean.TagsBean;
import com.cognizant.cloudone.kernel.scheduler.constants.JobTypeEnum;
import com.cognizant.cloudone.kernel.scheduler.constants.SchedulerEntityTypeEnum;
import com.cognizant.cloudone.kernel.scheduler.constants.SchedulingEnvTypeEnum;
import com.cognizant.cloudone.kernel.tags.constants.TagSourceEnum;
import com.cognizant.cloudone.kernel.tags.constants.TagTypeEnum;
import com.cognizant.cloudone.kernel.util.AsyncNotifiable;
import com.cognizant.cloudone.kernel.util.DateUtils;
import com.cognizant.cloudone.kernel.util.StopWatch;
import com.cognizant.cloudone.kernel.util.StringUtils;
import com.cognizant.cloudone.kernel.util.TimeZoneConverter;
import com.cognizant.cloudone.kernel.validation.errors.CloudOneError;
import com.cognizant.cloudone.kernel.validation.errors.CloudOneErrors;
import com.cognizant.cloudone.kernel.validation.messages.CloudOneMessage;
import com.cognizant.cloudone.kernel.validation.messages.CloudOneMessages;
import com.cognizant.cloudone.messaging.adapter.MessageAdapter;
import com.cognizant.cloudone.messaging.exception.MessagingException;
import com.cognizant.cloudone.messaging.message.Cloud360MessageObject;
import com.cognizant.cloudone.messaging.util.MessageUtil;
import com.cognizant.cloudone.notifications.INotification;
import com.cognizant.cloudone.notifications.impl.NotificationImplFactory;
import com.cognizant.cloudone.scheduler.ISchedulerService;
import com.cognizant.cloudone.scheduler.impl.SchedulerServiceImpl;
import com.cognizant.cloudone.scheduler.util.BeanBuilder;
import com.cognizant.cloudone.scheduler.util.ScheduleBuilder;
import com.cognizant.cloudone.scheduling.jobs.helper.JobAuthHelper;
import com.cognizant.cloudone.sl.assetmgmnt.ISLAssetMgmntDomain;
import com.cognizant.cloudone.sl.autoscale.facade.SLWorkflowFacade;
import com.cognizant.cloudone.sl.domain.factory.SLDomainFactory;
import com.cognizant.cloudone.sl.domain.factory.SLObjectEnum;
import com.cognizant.cloudone.sl.instances.facade.SLInstancesFacade;
import com.cognizant.cloudone.to.instance.CreateInstanceInfoTO;
import com.cognizant.cloudone.to.instance.InstanceTO;
import com.cognizant.cloudone.to.login.UserTO;
import com.cognizant.cloudone.to.recenttasks.RecentTaskDetailsTo;


public class InstanceDomainImpl extends BaseDomainImpl implements IInstanceDomain   {

	private static final CloudOneLogger logger = CloudOneLogger.getLogger(InstanceDomainImpl.class.getName());

	public static OSDescriptorBO getOSObject(SavvisOSEnum os) {
		OSDescriptorBO osDescriptorBO = new OSDescriptorBO();
		osDescriptorBO.setOsFullName(os.getOsDescription());
		osDescriptorBO.setOsFamily(os.getOsFamily());
		osDescriptorBO.setOsId(os.getTemplateId());
		osDescriptorBO.setSupportedMaxCPUs(1);
		osDescriptorBO.setRecommendedDiskSize(1);
		osDescriptorBO.setSupportedMaxMem(1);
		osDescriptorBO.setSupportedMinMem(1);
		osDescriptorBO.setRecommendedMem(1);
		if (os.isManagedImage()) {
			osDescriptorBO.setImageFilterEnum(InstanceImageFilterEnum.SavvisManagedImages);
		} else {
			osDescriptorBO.setImageFilterEnum(InstanceImageFilterEnum.SavvisUnmanagedImages);
		}
		return osDescriptorBO;
	}

	public static OSDescriptorBO getPhysOSObject(PhysicalServerOSEnum os) {
		OSDescriptorBO osDescriptorBO = new OSDescriptorBO();
		osDescriptorBO.setOsFullName(os.getOsName());
		osDescriptorBO.setOsFamily(os.getOsFamily());
		osDescriptorBO.setOsId(os.getOsId());
		return osDescriptorBO;
	}

	CloudOneDAOFactory cdf = null;
	InstanceProvider instanceProvider = new InstanceProvider();
	public SLInstancesFacade slfacade = null;

	public <T extends BaseBO> boolean isOperationAllowed(T instanceBO,
			PermissionEnum permissionEnum) {

		return isValidOperation(instanceBO,permissionEnum);
	}

	// 133053, added as a temporary measure for CO-4572
	@Transactional
	public List<InstanceBO> getInstancesForCloudAndProvider(InstanceBO instBO)
			throws ICException {

		List<InstanceBO> instanceList = null;

		try {
			CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
			InstanceDetailsDAO instanceDetailsDAO = factory
					.getInstanceDetailsDAO();

			InstanceDetailsDO instanceDetailsDO = new InstanceDetailsDO();
			instanceDetailsDO.setCloudMasterDO(instanceDetailsDAO.get(
					CloudMasterDO.class, instBO.getCloudId()));
			instanceDetailsDO.setProviderMasterDO(instanceDetailsDAO.get(
					ProviderMasterDO.class, instBO.getProviderID()));

			List<InstanceDetailsDO> instanceListDO = instanceDetailsDAO
					.fetchByCloudIdProviderId(instanceDetailsDO);
			if (null != instanceListDO && !instanceListDO.isEmpty()) {
				instanceList = instanceProvider.getBOFromDO(instanceListDO);
			}
		} catch (DALException ex) {
			logger.error("getInstancesForCloudAndProvider Instancedomain ", ex);
			throw new ICException(ex);
		}

		return instanceList;

	}

	public List<InstanceBO> getInstancesForAutomationFromCloud(InstanceBO instBO)
			throws ICException {
		//lets get the instances for given CloudId
		List<InstanceBO> instanceList = getInstanceListByCloudId(instBO);
		
		//lets populate the application copy data
		populateApplicationProfileData(instanceList);
		
		return instanceList;
	}
	
	@Transactional
	public List<InstanceBO> getInstanceListByCloudId(InstanceBO instBO)
			throws ICException {
		List<InstanceBO> instanceList = new ArrayList<>();
		InstanceProvider instanceProvider = new InstanceProvider();

		try {
			InstanceDetailsDAO instanceDetailsDAO = CloudOneDAOFactoryBuilder.getFactory().getInstanceDetailsDAO();

			InstanceDetailsDO instanceDetailsDO = new InstanceDetailsDO();
			instanceDetailsDO.setCloudMasterDO(instanceDetailsDAO.get(CloudMasterDO.class, instBO.getCloudId()));

			List<InstanceDetailsDO> instanceListDO = 
					instanceDetailsDAO.fetchActiveNotDeletedByCloudId(instanceDetailsDO);

			if (null != instanceListDO && !instanceListDO.isEmpty()) {
				for(InstanceDetailsDO inDO : instanceListDO) {
					instanceList.add(instanceProvider.getBOFromDOForMonitoringPolicy(inDO));
				}
			}
		} catch (DALException ex) {
			logger.error("getInstanceListForAutomationFromCloud Instancedomain ", ex);
			throw new ICException(ex);
		}
		return instanceList;
	}
	
	

	/****
	 * Method which returns the Instance List of LOI, Monitoring Profile pages.
	 */
	public List<InstanceBO> getInstances(InstanceBO instBO) throws ICException {
		String sql = DatabaseSQLConstant.INSTANCE_BASED_ON_SCOPE_SQL;
		List<InstanceBO> finalInstance = getInstances(instBO, sql);
				
		return finalInstance;
	}

	public List<InstanceBO> getInstancesForVMSnapshot(InstanceBO instBO) throws ICException {
		String sql = DatabaseSQLConstant.INSTANCE_WITH_SNAPSHOTBASED_ON_SCOPE_SQL;
		return getInstances(instBO, sql);
	}

	/**
	 * Basic operations to be done on clouds.
	 */
	public List<InstanceBO> runOperation(List<InstanceBO> instanceBOs)
			throws ICException {

		CloudOneErrors errors = null;									// on the SL_Layer
		boolean approvalPending = false;
		WSAuditBO wsAuditBO = null;
		if (instanceBOs != null && instanceBOs.get(0).isWebServices()) {
			wsAuditBO = getWSAuditBO(instanceBOs.get(0));			
			wsAuditBO.setInstanceName(instanceBOs.get(0).getInstanceName());
			wsAuditBO.setCloudId(instanceBOs.get(0).getCloudId());
			logger.debug("Inside InstanceDomainImpl.runOperation()"+instanceBOs.get(0).getCloudId());
			wsAuditBO=updateWSAudit(wsAuditBO);
			instanceBOs.get(0).setWsAuditID(wsAuditBO.getWsAuditID());
		}
		List<WorkerQueueBO> workerQueueObjects = new ArrayList<>();
		InstanceBO theInstanceBO = instanceBOs.get(0);
		// Filter class introduced to populate values based on "iswebservices"
		// is true - CO-5253
		InstanceFilter instanceFilter = new InstanceFilter();
		for (InstanceBO instBo : instanceBOs) {
			instBo = instanceFilter.filterFromDObyID(instBo);
		}
		String operation = theInstanceBO.getOperation();

		if("CLONE_SAVVIS_VM".equalsIgnoreCase(operation)) {
			//Operation being set to 'clone' primarily for WQ purpose.
			operation = InstancesActionsEnum.CLONE_VM.getValue();
			theInstanceBO.setOperation(operation);
		} else if ("CAPTURE_VAPP".equalsIgnoreCase(operation)) {
			operation = InstancesActionsEnum.CLONE_TO_TEMPLATE.getValue();
			theInstanceBO.setOperation(operation);
		} else if (InstancesActionsEnum.CONVERTTOIMAGEEXPIRE.getValue().equalsIgnoreCase(operation)) {
			theInstanceBO.setWqTarget(theInstanceBO.getAmiImageId());
		}

		try {
			instanceBOs = preRunOperation(instanceBOs);

		} catch (DALException e3) {
			logger.error("abort the delete operation", e3);

			// Populate any exceptions to update final WQ status
			instanceBOs = this.populateErrorMessages(instanceBOs, e3);			
		}
		//Check for in-progress operation on the instances
		InstanceActionUtil actionUtil = new InstanceActionUtil();
		Iterator<InstanceBO> itr = instanceBOs.iterator();
		List<InstanceBO> discardedInstanceBOs = new ArrayList<>();

		while(itr.hasNext()) {
			InstanceBO instanceBO = itr.next();
			if(actionUtil.isInstanceOperationInProgress(instanceBO)) {
				instanceBO.setErrors(buildErrors(null, "Operation is already in progress.", null));
				discardedInstanceBOs.add(instanceBO);
				//remove this instance from the list which is sent for further processing
				itr.remove();
			}
		}

		if(!discardedInstanceBOs.isEmpty()) {
			workerQueueObjects = WorkerQueueHelper
					.buildInitialWorkerQueueObjectList(discardedInstanceBOs,
							InstanceProvider.getTaskDesc(theInstanceBO));
			workerQueueObjects = WorkerQueueManager
					.updateInitialWorkerQueueStatus(workerQueueObjects);
			for(InstanceBO instanceBO : discardedInstanceBOs) {
				for(WorkerQueueBO workerQueueBO : workerQueueObjects) {
					if(instanceBO.getWqTargetId() == workerQueueBO.getWqTargetId()) {
						if(workerQueueBO.getCurrentTaskKey() > 0)
							instanceBO.setWqTaskID(workerQueueBO.getCurrentTaskKey());
					}
				}
			}
			// Update WQ final status for discarded instance BOs
			workerQueueObjects = WorkerQueueHelper
					.buildFinalWorkerQueueObjectList(ICUtils.setWQTaskID(
							discardedInstanceBOs, workerQueueObjects),
							InstanceProvider.getTaskDesc(theInstanceBO));
			WorkerQueueManager
			.updateFinalWorkerQueueStatus(workerQueueObjects);
			if (theInstanceBO.isWebServices()){
				try {
					updateTaskIdInWSAuditDetails(theInstanceBO,
							workerQueueObjects);
				} catch (DALException e) {
					logger.error(e.getMessage());
				}
			}
		}

		/*
		 * CO-13308-if the operation requires disabling monitoring then the worker queue initialization
		 * is done after  disabling monitoring for the operation
		 */

		if(instanceBOs != null && !instanceBOs.isEmpty()&&!isSuspendBoundMonitoringRequired(operation)) {
			workerQueueObjects = WorkerQueueHelper
					.buildInitialWorkerQueueObjectList(instanceBOs,
							InstanceProvider.getTaskDesc(theInstanceBO));
			workerQueueObjects = WorkerQueueManager
					.updateInitialWorkerQueueStatus(workerQueueObjects);
			for(InstanceBO instanceBO : instanceBOs) {
				for(WorkerQueueBO workerQueueBO : workerQueueObjects) {
					if(instanceBO.getWqTargetId() == workerQueueBO.getWqTargetId()) {
						if(workerQueueBO.getCurrentTaskKey() > 0)
							instanceBO.setWqTaskID(workerQueueBO.getCurrentTaskKey());
					}
				}
			}
			logger.debug("Populated WQ from BO");
			if (theInstanceBO.isWebServices()){
				logger.info("Going to trigger updateWSAuditDetails......");
				try {
					updateTaskIdInWSAuditDetails(instanceBOs.get(0),
							workerQueueObjects);
				} catch (DALException e) {
					logger.error(e.getMessage());
				}
			}

		}
		if ("START".equalsIgnoreCase(operation)
				|| (InstancesActionsEnum.POWERON.getValue()
						.equalsIgnoreCase(operation))) {
			if (null != instanceBOs && !instanceBOs.isEmpty()) {
				for (InstanceBO instance : instanceBOs) {
					if (instance.getAssigneeGroupID() <= 0) {
						instance.setAssigneeGroupID(instance.getActiveUserGroupID());
					}

				}
				IQuotaValidationDomain quotaValidationDomain = (IQuotaValidationDomain) QuotaObjectFactory
						.getInstance(theInstanceBO);
				errors = quotaValidationDomain.validateQuota(
						instanceBOs, TaskDescriptionEnum.POWER_ON_INSTANCE);
				if (null != errors && errors.hasErrors()) {
					logger.error("Quota Validation failed during creation of instances");
					for (InstanceBO instance : instanceBOs) {
						instance.setErrors(errors);
						updateFinalWQStatusForRun(workerQueueObjects, instance);
					}
					return instanceBOs;
				}
				
				if (null != errors && errors.hasErrors()) {					
					for (InstanceBO instance : instanceBOs) {
						instance.setErrors(errors);
						updateFinalWQStatusForRun(workerQueueObjects, instance);
					}
					return instanceBOs;
				}
			}
		}

		if (null != instanceBOs && !instanceBOs.isEmpty()) {
			IPlatformsDomain domain = new PlatformsDomainImpl();
			PlatformBO platformBO = domain.fetchProvider(PlatformsProvider
					.getInputPlatformBO(theInstanceBO));

			// Get the InstanceDetailsDAO
			logger.debug("Operating on instances returned to SL: "+operation);
				if(!theInstanceBO.isApproved())
				 approvalPending = executePolicies(instanceBOs, workerQueueObjects,operation);
			
			if(!approvalPending){ //check if approval is pending and skip the below code execution
				if ((InstancesActionsEnum.CONVERTTOIMAGE.getValue().equalsIgnoreCase(operation)) || (InstancesActionsEnum.AZURECLONETOIMAGE.getValue().equalsIgnoreCase(operation)) || InstancesActionsEnum.CREATE_IMAGE_TEMPLATE.getValue().equalsIgnoreCase(operation)
						|| InstancesActionsEnum.CREATE_FLEX_IMAGE.getValue().equalsIgnoreCase(operation)) {
					if (instanceBOs.get(0).getNewNickName() == null
							|| "".equalsIgnoreCase(instanceBOs.get(0).getNewNickName())) {
						instanceBOs.get(0).setNewNickName(
								getImageNickName(instanceBOs.get(0)));
					}
				}
	
	
				IMonitorDomain monDom = (IMonitorDomain) DomainFactory.getInstance(ICObjectEnum.MONITORTEMPLATE);
				try {
					monDom.handlePreActionOnInstances(instanceBOs, InstancesActionsEnum.getInstanceActionEnum(operation));
					//CO-13308-Worker Queue initialization done after disabling monitoring for operation
					if(isSuspendBoundMonitoringRequired(operation) && !theInstanceBO.isApproved() && !operation.equalsIgnoreCase(InstancesActionsEnum.TERMINATE_INSTANCES.getValue()) && !operation.equalsIgnoreCase(InstancesActionsEnum.DELETE.getValue())) {
						workerQueueObjects = WorkerQueueHelper
								.buildInitialWorkerQueueObjectList(instanceBOs,
										InstanceProvider.getTaskDesc(theInstanceBO));
						workerQueueObjects = WorkerQueueManager
								.updateInitialWorkerQueueStatus(workerQueueObjects);
						for(InstanceBO instanceBO : instanceBOs) {
							for(WorkerQueueBO workerQueueBO : workerQueueObjects) {
								if(instanceBO.getWqTargetId() == workerQueueBO.getWqTargetId()) {
									if(workerQueueBO.getCurrentTaskKey() > 0)
										instanceBO.setWqTaskID(workerQueueBO.getCurrentTaskKey());
								}
							}
						}
						if (instanceBOs.get(0).isWebServices()){
							logger.info("Going to trigger updateWSAuditDetails......");
							try {
								updateTaskIdInWSAuditDetails(instanceBOs.get(0),
										workerQueueObjects);
							} catch (DALException e) {
								logger.error(e.getMessage());
							}
						}
					}
					else if(theInstanceBO.isApproved()){
        				logger.debug("approvedTaskIds = " + theInstanceBO.getWqTaskID());
        				WorkerQueueBO workerQueueBO = WorkerQueueHelper.buildIntermediateWQObject(theInstanceBO.getUserID(),
        						theInstanceBO.getCloudId(),
																				MessageStringConstants.INPROGRESS, null);
        				WorkerQueueManager.updateAnyWorkerQueueStatus(workerQueueBO,theInstanceBO.getWqTaskID());
        				workerQueueObjects.add(workerQueueBO);
					
					}
				} catch (ICException e) {
					// as of now don't block the actual action just because monitoring action failed
					logger.error("Error occured while notifying monitoring domain - {}", e.getMessage());
				}
				try {
					instanceBOs = updateAssetsForDeRegistration(instanceBOs,theInstanceBO.getOperation(),workerQueueObjects);
					// Run Operation
					logger.debug("Operating on instances forwarded to SL. ");
					// AsyncNotifiable is used to update the DB after performing the
					// run operation asynchronously.
					LoadBalancerDomainImpl loadBalancerDomainImpl = (LoadBalancerDomainImpl) DomainFactory.getInstance(ICObjectEnum.LB);
					AsyncNotifiable asyncNotifiable = new AsyncInstanceRunNotifiableImpl<>(
							this, loadBalancerDomainImpl, operation, workerQueueObjects);
	
					for(InstanceBO instanceBO: instanceBOs)
					{
						if (InstancesActionsEnum.DELETE.getValue().equalsIgnoreCase(
								instanceBO.getOperation())
								|| InstancesActionsEnum.TERMINATE_INSTANCES.getValue()
								.equalsIgnoreCase(instanceBO.getOperation())) {
							performPostDeleteCleanup(instanceBO);
						}
					}
	
					//Verify state and filter instances accordingly.
					excludeInvalidInstancesBasedOnState(instanceBOs,workerQueueObjects, operation);
					
					populateAzureInstanceDetails(instanceBOs,operation);
					String enableMessagingValue="";
					ResourceBundle resource = ResourceBundle.getBundle("deployment-config");
					enableMessagingValue = resource.getString(ENABLE_MESSAGING);
					if ((InstancesActionsEnum.DELETE.getValue().equalsIgnoreCase(
							operation)
							|| InstancesActionsEnum.TERMINATE_INSTANCES.getValue()
							.equalsIgnoreCase(operation))&& enableMessagingValue.equalsIgnoreCase("Y")) {
						buildAsyncDeleteTask(instanceBOs,asyncNotifiable,workerQueueObjects);//processParallelDeleteRequest();
					} else {
						SLInstancesFacade slInstancesFacade = SLInstancesFacade.SINGLETON_INSTANCE;
						slInstancesFacade.runOperation(instanceBOs,
								PlatformsProvider.getProviderCredentials(platformBO),
								asyncNotifiable);
					}
				} catch (SLException | SOAPFaultException sle) {
					logger.error("run operation erreur: ",sle);
					// Populate any exceptions to update final WQ status)
					instanceBOs = this.populateErrorMessages(instanceBOs, sle);
	
					// Update WQ final status
					workerQueueObjects = WorkerQueueHelper
							.buildFinalWorkerQueueObjectList(ICUtils.setWQTaskID(
									instanceBOs, workerQueueObjects),
									InstanceProvider.getTaskDesc(theInstanceBO));
					WorkerQueueManager
					.updateFinalWorkerQueueStatus(workerQueueObjects);
					for(InstanceBO  instBO :instanceBOs) {
						rollbackDergisterAsset(instBO);
					}
					throw new ICException(sle);
				}
	
			}
		} //approval pending end
		return instanceBOs;
	}

	/**
	 * Method to validate the action against on instances based on state and exclude invalid instances with proper final WQ msg
	 * @param instanceBOs
	 * @param instanceDetailsDAO
	 * @param workerQueueObjects
	 * @param operation
	 * @param instanceList
	 * @throws ICException
	 */
	@Transactional
	public void excludeInvalidInstancesBasedOnState(
			List<InstanceBO> instanceBOs,
			List<WorkerQueueBO> workerQueueObjects, String operation) throws ICException {

		InstanceDetailsDAO instanceDetailsDAO = null;
		ServerMasterDAO serverMasterDAO = null;
		try{
			String stateFromBO=verifyState(operation);
			String stateFromDB=null;
			if(stateFromBO!=null){
				//Going with an assumption , set of instances coming as part of an individual request belongs to a single provider.
				if (instanceBOs.get(0).getProviderType().equals(ProviderTypeEnum.IBMSLBAREMETAL)){
					serverMasterDAO = getFactory().getServerMasterDAO();
				}else{
					instanceDetailsDAO = getFactory().getInstanceDetailsDAO();
				}

				Iterator<InstanceBO> iterator = instanceBOs.iterator();
				while(iterator.hasNext()){
					InstanceBO instBo=(InstanceBO)iterator.next();
					if(!instBo.getProviderType().equals(ProviderTypeEnum.IBMSLBAREMETAL)){
						try{
							InstanceDetailsDO instanceDetailsDO = instanceDetailsDAO.get(InstanceDetailsDO.class, instBo.getInstanceId());
							stateFromDB = verifyState(instanceDetailsDO.getState());
						} catch(Exception e){
							logger.error(e.getMessage());
						}
					} else {
						try{
							ServerMasterDO smDO = serverMasterDAO.get(ServerMasterDO.class, instBo.getInstanceId());
							stateFromDB=verifyState(smDO.getConnectedState());
						} catch(Exception e){
							logger.error(e.getMessage());
						}
					}
					if(stateFromBO.equalsIgnoreCase(stateFromDB)){
						CloudOneErrors errors = new CloudOneErrors();
						CloudOneError error = new CloudOneError();
						error.setErrorMessage("This Operation cannot be performed. Since the instance "+instBo.getInstanceName()+" is already "+stateFromBO.toLowerCase()+". ");						  
						errors.addError(error);
						instBo.setErrors(errors);
						updateFinalWQStatusForRun(workerQueueObjects, instBo);
						iterator.remove();
						continue;
					}
				}
			}
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}

	
	
	private void buildAsyncDeleteTask(List<InstanceBO> instanceBOs,
			AsyncNotifiable asyncNotifiable, List<WorkerQueueBO> workerQueueObjects) {
		try {
			
			ParallelProcessingService<InstanceBO> parallelService = new ParallelProcessingService<>();
			parallelService.saveMessagesToQueue(instanceBOs, workerQueueObjects,instanceBOs.get(0),ActionEnum.DELETE_INSTANCES.getName(),"processParallelDeleteRequest");
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**/
		
	}
	
	public void processParallelDeleteRequest(InstanceBO instanceBO,
			List<WorkerQueueBO> workerQueueObjects) throws ICException {
		logger.info("Inside process Parallel Delete request");
		List<InstanceBO> resultList = null;
		InstanceBO instanceToBePassed = new InstanceBO();

		List<InstanceBO> listToBePassed = new ArrayList<>();
		IPlatformsDomain domain = new PlatformsDomainImpl();
		PlatformBO platformBO = domain.fetchProvider(PlatformsProvider
				.getInputPlatformBO(instanceBO));
		LoadBalancerDomainImpl loadBalancerDomainImpl = (LoadBalancerDomainImpl) DomainFactory
				.getInstance(ICObjectEnum.LB);
		AsyncNotifiable asyncNotifiable = new AsyncInstanceRunNotifiableImpl<>(
				this, loadBalancerDomainImpl, instanceBO.getOperation(),
				workerQueueObjects);
		try {
			BeanUtils.copyProperties(instanceToBePassed, instanceBO);
			listToBePassed.add(instanceToBePassed);
			SLInstancesFacade slInstancesFacade = SLInstancesFacade.SINGLETON_INSTANCE;
			resultList = slInstancesFacade.runOperation(listToBePassed,
					PlatformsProvider.getProviderCredentials(platformBO),
					asyncNotifiable);
		} catch (IllegalAccessException | InvocationTargetException
				| SLException e) {
			logger.error("run operation erreur: ", e);
			// Populate any exceptions to update final WQ status)
			listToBePassed = this.populateErrorMessages(listToBePassed, e);

			// Update WQ final status
			workerQueueObjects = WorkerQueueHelper
					.buildFinalWorkerQueueObjectList(ICUtils.setWQTaskID(
							listToBePassed, workerQueueObjects),
							InstanceProvider.getTaskDesc(instanceBO));
			WorkerQueueManager.updateFinalWorkerQueueStatus(workerQueueObjects);
			for (InstanceBO instBO : listToBePassed) {
				rollbackDergisterAsset(instBO);
			}
			throw new ICException(e);
		}

	}

	private String verifyState(String operation){

		List<String> shutdownOptions=Arrays.asList(new String[]{"SHUTDOWN","STOP","STOPPED","POWEROFF","POWEREDOFF"});
		List<String> poweronOptions=Arrays.asList(new String[]{"POWERON","POWEREDON","START","RUNNING"});
		List<String> deleteOptions=Arrays.asList(new String[]{"DELETE","DELETED","TERMINATE","TERMINATED"});
		
		String flag=null;
		operation=operation.toUpperCase();
		if(deleteOptions.contains(operation)){
			flag="DELETED";
		}
		else if(shutdownOptions.contains(operation)){
			flag="SHUTDOWN";
		}
		else if(poweronOptions.contains(operation)){
			flag="POWEREDON";
		}
		return flag;
	}

	private List<InstanceBO> updateAssetsForDeRegistration(
			List<InstanceBO> instanceBOs, String operation, List<WorkerQueueBO> workerQueueObjects) {
		if (InstancesActionsEnum.DELETE.getValue().equalsIgnoreCase(operation)
				|| InstancesActionsEnum.POWEROFF.getValue().equalsIgnoreCase(
						operation)
						|| InstancesActionsEnum.TERMINATE_INSTANCES.getValue()
						.equalsIgnoreCase(operation)
						|| InstancesActionsEnum.STOP_INSTANCES.getValue()
						.equalsIgnoreCase(operation)
						|| InstancesActionsEnum.SHUTDOWN.getValue().equalsIgnoreCase(
								operation)) {
			logger.debug("About to de-register asset due to {} action", operation);
			IntegrationSystemBO integrationSystemBO;
			try {
				integrationSystemBO = AssetManagementUtil
						.getEnabledCMDBIntegrationSystemBO(instanceBOs.get(0)
								.getCloudId());
				List<AssetCO> assetCos = new ArrayList<>();
				if (integrationSystemBO != null) {
					logger.debug("CMDB configured");
					List<AssetBean> assetBeanList = new ArrayList<>();
					int index = 0;
					for (InstanceBO instanceBO : instanceBOs) {
						// Build asset bean
						if(workerQueueObjects!=null && workerQueueObjects.get(index)!=null){
							instanceBO.setWqTaskID( workerQueueObjects.get(index).getCurrentTaskKey());
						}
						AssetBean assetBean = AssetMgmntProvider
								.getInstanceBean(instanceBO);
						String systemIdentifier = null;
						// Check if SI is present
						if (assetBean != null) {
							systemIdentifier = AssetManagementUtil
									.getAssetSI(assetBean);
						}
						logger.debug("systemIdentifier : {}", systemIdentifier);
						if (StringUtils.isNotEmpty(systemIdentifier)
								&& !GlobalConstant.N_A.equals(systemIdentifier)) {
							// Here, asset is registered and we have proper SI.
							// So, de-register the asset
							assetBeanList.add(assetBean);
							AssetCO assetCo = AssetMgmntProvider.getAssetCOFromInstanceBO(instanceBO);
							assetCo.setSystemId(systemIdentifier);
							assetCos.add(assetCo);
						} else {
							if (!GlobalConstant.N_A.equals(systemIdentifier)) {
								// Here, asset is registered, but still waiting
								// for the SI. So, just update the pending
								// status
								AssetManagementUtil
								.updateSyncPendingStatus(assetBean);
							} else {
								// Asset is not yet registered. So, we can
								// neither retire it nor register it!  
								logger.info(
										"Asset {} is not yet registered. So, we can neither retire it nor register it!",
										assetBean.getName());
							}
						}
						index++;
					}
					if (assetCos != null && !assetCos.isEmpty()) {
						// Call webservice for deregistration
						ISLAssetMgmntDomain islAssetMgmntDomain = (ISLAssetMgmntDomain) SLDomainFactory
								.getInstance(SLObjectEnum.ASSETMGMNT);
						AssetMgmntFacade assetFacade = AssetMgmntFacade.SINGLETON_INSTANCE;
						List<AssetCO> resultAssetcos = islAssetMgmntDomain
								.deregisterAsset(assetCos);
						updateDatabaseForDeregistration(assetFacade,
								resultAssetcos);
					}
				} else {
					logger.debug("CMDB not configured");
				}

			} catch (    ICException | SLException e) {
				logger.error(e);
			}
		}
		return instanceBOs;
	}

	/**
	 * @param assetFacade
	 * @param resultAssetcos
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	protected void updateDatabaseForDeregistration(
			AssetMgmntFacade assetFacade, List<AssetCO> resultAssetcos) {
		if (resultAssetcos != null && !resultAssetcos.isEmpty()) {
			for (AssetCO assetCOforDB : resultAssetcos) {
				logger.debug(
						"updating asset mapping for {} {}",
						assetCOforDB.getName(),
						assetCOforDB.getTaskId());
				assetFacade.updateAssetMapping(assetCOforDB);
			}
		}
	}

	public void registerAsset(InstanceBO boObj) throws ICException {
		IntegrationSystemBO integrationSystemBO = AssetManagementUtil
				.getEnabledCMDBIntegrationSystemBO(boObj.getCloudId());
		if (integrationSystemBO != null) {
			sendNewAssetsToQueue(integrationSystemBO, boObj);
		} else {
			IntegrationSystemBO serviceRequestIntegrationSystemBO = AssetManagementUtil
					.getEnabledServiceRequestIntegrationSystemBO(boObj.getCloudId());
			logger.info("serviceRequestIntegrationSystemBO---->" + serviceRequestIntegrationSystemBO);
			if(serviceRequestIntegrationSystemBO != null) {
				sendNewAssetsToQueue(serviceRequestIntegrationSystemBO, boObj);
			}
		}
	}
	
	public void createServiceRequest(CreateInstanceInfoBO boObj) throws ICException {
		IntegrationSystemBO integrationSystemBO = AssetManagementUtil
				.getEnabledCMDBIntegrationSystemBO(boObj.getCloudId());
		if (integrationSystemBO != null) {
			createServiceRequestinSync(integrationSystemBO, boObj);
		} else {
			IntegrationSystemBO serviceRequestIntegrationSystemBO = AssetManagementUtil
					.getEnabledServiceRequestIntegrationSystemBO(boObj.getCloudId());
			logger.info("serviceRequestIntegrationSystemBO---->" + serviceRequestIntegrationSystemBO);
			if(serviceRequestIntegrationSystemBO != null) {
				createServiceRequestinSync(serviceRequestIntegrationSystemBO, boObj);
			}
		}
	}

	private void sendNewAssetsToQueue(
			IntegrationSystemBO integrationSystemBO, InstanceBO boObj) throws ICException {
		AssetBean assetBean = AssetManagementUtil.validateNewAsset(boObj,
				integrationSystemBO.getIntegrationSystemName());

		if (assetBean != null) {
			logger.info("Valid Asset : " + assetBean.getName());
			// send to queue
			AssetCO assetCO = AssetMgmntProvider
					.getAssetCOFromInstanceBO(boObj);
			List<AssetCO> assetCOs = new ArrayList<>();
			assetCOs.add(assetCO);
			Class<?>[] argTypes = { List.class };
			Object[] args = new Object[] { assetCOs };
			Cloud360MessageObject c360MsgObject = MessageUtil
					.buildMessageObject(AssetMgmntFacade.class.getName(), null,
							"registerAsset", argTypes, args);
			try {
				MessageAdapter.sendQueueObjMessage(
						MessageQueueConstants.ASSETMGMT_QUEUE, c360MsgObject);
			} catch (MessagingException e) {
				logger.error(e);
			}
		} else {
			logger.info("Invalid Asset : " + boObj.getInstanceName());
		}
	}
	
	private void sendNewAssetsToQueueForCreateServiceRequest(
			IntegrationSystemBO integrationSystemBO, CreateInstanceInfoBO boObj) throws ICException {
		

		
			logger.info("Sending Asset for SR Creation : " + boObj.getInstanceName());
			// send to queue
			AssetCO assetCO = AssetMgmntProvider
					.getAssetCOFromCreateInstanceInfoBO(boObj);
			List<AssetCO> assetCOs = new ArrayList<>();
			assetCOs.add(assetCO);
			Class<?>[] argTypes = { List.class };
			Object[] args = new Object[] { assetCOs };
			Cloud360MessageObject c360MsgObject = MessageUtil
					.buildMessageObject(AssetMgmntFacade.class.getName(), null,
							"createSR", argTypes, args);
			try {
				MessageAdapter.sendQueueObjMessage(
						MessageQueueConstants.ASSETMGMT_QUEUE, c360MsgObject);
			} catch (MessagingException e) {
				logger.error(e);
			}
		
	}
	
	
	private void createServiceRequestinSync(IntegrationSystemBO integrationSystemBO, CreateInstanceInfoBO boObj) throws ICException {
		logger.info("Sending Asset for SR Creation Sync method : " + boObj.getInstanceName());
		// send to queue
		AssetCO assetCO = AssetMgmntProvider
				.getAssetCOFromCreateInstanceInfoBO(boObj);
		List<AssetCO> assetCOs = new ArrayList<>();
		assetCOs.add(assetCO);
		AssetMgmntFacade assetFacade = AssetMgmntFacade.SINGLETON_INSTANCE;
		AssetCO assetCOResult = assetFacade.createSR(assetCOs);
		if(assetCOResult != null) {
			if(assetCOResult.getServiceRequestCO() != null ) {
				boObj.setWfRequestId(assetCOResult.getServiceRequestCO().getSrNo() + "~~" + assetCOResult.getServiceRequestCO().getSrId());
			}
		}
		
		logger.info("Service Request id created--->" + boObj.getWfRequestId());
	}

	public void updateAsset(InstanceBO existingBoObj, InstanceBO newBoObj)
			throws ICException {
		IntegrationSystemBO integrationSystemBO = AssetManagementUtil
				.getEnabledCMDBIntegrationSystemBO(newBoObj.getCloudId());
		if (integrationSystemBO != null) {
			AssetBean tmpAssetBean = AssetMgmntProvider
					.getInstanceBean(newBoObj);

			if (tmpAssetBean != null) {
				String systemIdentifier = null;
				try {
					systemIdentifier = AssetManagementUtil
							.getAssetSI(tmpAssetBean);
				} catch (ICException e) {
					logger.error(e);
				}
				logger.info("StringUtils.isEmpty(systemIdentifier)--->"
						+ StringUtils.isEmpty(systemIdentifier));
				if (StringUtils.isEmpty(systemIdentifier)) {
					// Asset is registered but SI is not yet received. So,
					// update the status accordingly
					try {
						tmpAssetBean.setTaskId(newBoObj.getWqTaskID());
						AssetManagementUtil
						.updateSyncPendingStatus(tmpAssetBean);
					} catch (ICException e) {
						logger.error(e);
					}
				} else {
					if (GlobalConstant.N_A.equals(systemIdentifier)) {
						// Asset is not yet registered. So, instead of updating,
						// register the asset if validation criteria is met. 
						logger.info("Asset {} not yet registered to CMDB",
								tmpAssetBean.getName());
						sendNewAssetsToQueue(integrationSystemBO, newBoObj);
					} else {
						// Asset is registered and got the SI too!
						// So, just update to CMDB if validation criteria is met
						AssetBean assetBeanToUpdate = AssetManagementUtil
								.validateUpdateAsset(existingBoObj, newBoObj,
										integrationSystemBO
										.getIntegrationSystemName());
						if (assetBeanToUpdate != null) {
							logger.info("Valid Asset to update CMDB : "
									+ assetBeanToUpdate.getName());
							// send to queue
							sendAssetUpdatesToQueue(newBoObj, systemIdentifier);
						} else {
							logger.info("Invalid Asset for CMDB update: "
									+ tmpAssetBean.getName());
						}
					}
				}
			} else {
				logger.info("Invalid Asset : " + newBoObj.getInstanceName());
			}
		}
	}

	public void rollbackDergisterAsset(InstanceBO instanceBO) {
		if (InstancesActionsEnum.DELETE.getValue().equalsIgnoreCase(instanceBO.getOperation())
				|| InstancesActionsEnum.POWEROFF.getValue().equalsIgnoreCase(
						instanceBO.getOperation())
						|| InstancesActionsEnum.TERMINATE_INSTANCES.getValue()
						.equalsIgnoreCase(instanceBO.getOperation())
						|| InstancesActionsEnum.STOP_INSTANCES.getValue()
						.equalsIgnoreCase(instanceBO.getOperation())
						|| InstancesActionsEnum.SHUTDOWN.getValue().equalsIgnoreCase(
								instanceBO.getOperation())) {
			try {
				logger.debug("Entering rollbackDergisterAsset as cloud operation failed");
				IntegrationSystemBO integrationSystemBO = AssetManagementUtil
						.getEnabledCMDBIntegrationSystemBO(instanceBO
								.getCloudId());
				if (integrationSystemBO != null) {
					logger.debug("CMDB configured");
					AssetBean assetBean = AssetMgmntProvider
							.getInstanceBean(instanceBO);
					String systemIdentifier = null;
					if (assetBean != null) {
						systemIdentifier = AssetManagementUtil
								.getAssetSI(assetBean);
					}
					logger.debug("systemIdentifier : {}", systemIdentifier);
					if (StringUtils.isNotEmpty(systemIdentifier)
							&& !GlobalConstant.N_A.equals(systemIdentifier)) {
						// send to queue
						sendAssetUpdatesToQueue(instanceBO, systemIdentifier);
					} else {
						logger.debug("Invalid asset found when rollbackDergisterAsset");
					}
				} else {
					logger.debug("CMDB not configured");
				}
			} catch (ICException e) {
				logger.error(e);
			}
		}
	}

	private void sendAssetUpdatesToQueue(InstanceBO instanceBO,
			String systemIdentifier) {
		AssetCO assetCO = AssetMgmntProvider
				.getAssetCOFromInstanceBO(instanceBO);
		assetCO.setSystemId(systemIdentifier);
		List<AssetCO> assetCOs = new ArrayList<>();
		assetCOs.add(assetCO);
		Class<?>[] argTypes = { List.class };
		Object[] args = new Object[] { assetCOs };
		Cloud360MessageObject c360MsgObject = MessageUtil.buildMessageObject(
				AssetMgmntFacade.class.getName(), null, "updateAsset",
				argTypes, args);
		try {
			MessageAdapter.sendQueueObjMessage(
					MessageQueueConstants.ASSETMGMT_QUEUE, c360MsgObject);
		} catch (MessagingException e) {
			logger.error(e);
		}
	}

	/**
	 * Method to check if suspension of monitoring is required for the given operation.
	 * 
	 * @param String
	 * @return boolean
	 *
	 */
	private boolean isSuspendBoundMonitoringRequired(String operation) 	{
		boolean status=false;
		if(operation.equalsIgnoreCase(InstancesActionsEnum.POWEROFF.getValue())||operation.equalsIgnoreCase(InstancesActionsEnum.SHUTDOWN.getValue())
				||operation.equalsIgnoreCase(InstancesActionsEnum.STOP_INSTANCES.getValue())||operation.equalsIgnoreCase(InstancesActionsEnum.SUSPEND.getValue())
				||operation.equalsIgnoreCase(InstancesActionsEnum.RESET.getValue())||operation.equalsIgnoreCase(InstancesActionsEnum.STANDBY.getValue())
				||operation.equalsIgnoreCase(InstancesActionsEnum.CONVERT_TO_TEMPLATE.getValue())||operation.equalsIgnoreCase(InstancesActionsEnum.RESTART.getValue())
				||operation.equalsIgnoreCase(InstancesActionsEnum.PARK.getValue())||operation.equalsIgnoreCase(InstancesActionsEnum.TERMINATE_INSTANCES.getValue())
				||operation.equalsIgnoreCase(InstancesActionsEnum.DELETE.getValue()))
		{
			status=true;
		}
		return status;
	}


	/**
	 * Method to create an image entry into our database as a result of
	 * CONVERTTOIMAGE action for AWS . Visibility of this image will be "self".
	 * 
	 * @param resultList
	 * @param imageDetailsDAO
	 * @throws SLException
	 */
	@Transactional
	public void saveImageDetailsInDB(List<InstanceBO> resultList)
			throws DALException {

		ImageDetailsDAO imageDetailsDAO = getFactory().getImageDetailsDAO();
		for (InstanceBO instanceBO : resultList) {
			if (instanceBO.getImageInstanceBO() != null) {
				ImageInstanceBO imageInstanceBO = instanceBO
						.getImageInstanceBO();
				if (imageInstanceBO.getOperatingSystem() == null
						|| (imageInstanceBO.getOperatingSystem().equals(
								OSTypesEnum.OTHER.getDesc())
								&& instanceBO.getOsName() != null && !instanceBO
								.getOsName()
								.equals(OSTypesEnum.OTHER.getDesc()))) {
					imageInstanceBO.setOperatingSystem(instanceBO.getOsName());
				}

				// Change it to true so that images will be listed
				// for 'self'.
				imageInstanceBO.setOwnedByMe(true);				

				List<ImageInstanceBO> imageList = new ArrayList<>();
				imageList.add(imageInstanceBO);
				try {
					List<ImageDetailsDO> imageDetails = ImageDetailsProvider
							.getDOFromBO(imageList);
					imageDetails.get(0).setOperation(instanceBO.getOperation());
					if (imageDetails != null && !imageDetails.isEmpty()) {
						imageDetailsDAO.saveOrUpdate(imageDetails.get(0));
					}
				} catch (DALException e) {
					logger.error(e);
					throw e;
				}
			}
		}
	}

	/**
	 * 
	 * @param resultList
	 * @throws DALException
	 */

	@Transactional
	public void deleteImageDetails(List<InstanceBO> resultList)
			throws DALException {
		logger.debug("inside deleteImageDetails ");
		ImageDetailsDAO imageDetailsDAO = getFactory().getImageDetailsDAO();
		for (InstanceBO instanceBO : resultList) {
			try {
				ImageDetailsDO imageDetailsDO = new ImageDetailsDO();
				DataCenterDetailsDAO dataCenterDetailsDAO = getFactory().getDataCenterDetailsDAO();
				DataCenterDetailsDO dataCenterDetailsDO = new DataCenterDetailsDO();
				logger.debug("deleteImageDetails- datacenterName,cloudId,Providerid,amiImageid:"
						+instanceBO.getDatacenterName()+"  "+instanceBO.getCloudId()+"  "+instanceBO.getCloudId()+instanceBO.getAmiImageId());
				dataCenterDetailsDO.setDataCenterName(instanceBO.getDatacenterName());
				dataCenterDetailsDO.setCloudMasterDO(getFactory().getCloudMasterDAO().get(CloudMasterDO.class, instanceBO.getCloudId()));
				dataCenterDetailsDO.setProviderMasterDO(getFactory().getProviderMasterDAO().get(ProviderMasterDO.class, instanceBO.getProviderID()));
				dataCenterDetailsDO = dataCenterDetailsDAO.fetchByCloudIDProviderIDDataCenterName(dataCenterDetailsDO);
				imageDetailsDO.setAmiId(instanceBO.getAmiImageId());
				imageDetailsDO.setDataCenterDetailsDO(dataCenterDetailsDO);
				imageDetailsDO = imageDetailsDAO.fetchByAmiIdAndDatacenterId(imageDetailsDO);
				logger.debug("imageDetailsDO object : "+imageDetailsDO);
				imageDetailsDAO.delete(imageDetailsDO);
			} catch (DALException e) {
				logger.error(e);
				throw e;
			}

		}
	}

	private boolean executePolicies(List<InstanceBO> instanceBOs,
			List<WorkerQueueBO> workerQueueObjects, String operation) throws ICException {
		boolean flag = false;
		List<WorkerQueueBO> workerQueueObject = new ArrayList<>();
		List<InstanceBO> theinstanceBOs = new ArrayList<>();
		try {
			// Execute Policies
			
			if (workerQueueObjects.isEmpty() &&(operation.equalsIgnoreCase(InstancesActionsEnum.TERMINATE_INSTANCES.getValue()) || operation.equalsIgnoreCase(InstancesActionsEnum.DELETE.getValue()))){
				workerQueueObject = WorkerQueueHelper
						.buildInitialWorkerQueueObjectList(instanceBOs,
								InstanceProvider.getTaskDesc(instanceBOs.get(0)));
				workerQueueObject = WorkerQueueManager
						.updateInitialWorkerQueueStatus(workerQueueObject);
				for(InstanceBO instanceBOWQ : instanceBOs) {
					for(WorkerQueueBO workerQueueBO : workerQueueObject) {
						if(instanceBOWQ.getWqTargetId() == workerQueueBO.getWqTargetId()) {
							instanceBOWQ.setWqTaskID(workerQueueBO.getCurrentTaskKey());
						}
					}
				}
			}
			workerQueueObjects.addAll(workerQueueObject);

			Map<InstanceBO, PolicyException> blockedList = BusinessPoliciesUtil
					.executeInstanceActionProvisioningPolicies(instanceBOs);			


			if (blockedList != null && !blockedList.isEmpty()) {
				// Removed blocked instance from list
				instanceBOs.removeAll(blockedList.keySet());
				logger.debug("BlockedList{} not null");

				// Update worker queue status of blocked instances
				InstanceBO instanceBO = null;
				theinstanceBOs.addAll(blockedList.keySet());
				InstanceBO theInstanceBO = theinstanceBOs.get(0);

				for (Map.Entry<InstanceBO, PolicyException> entry : blockedList
						.entrySet()) {
					instanceBO = entry.getKey();
					if(!entry.getValue().getMessage().equals(PolicyConstants.APPROVAL_ACTION)){					
					
					CloudOneErrors extractPolicyErrors = BusinessPoliciesUtil
							.extractPolicyErrors(entry.getValue());
					if (workerQueueObjects.isEmpty()){
						workerQueueObject = WorkerQueueHelper
							.buildInitialWorkerQueueObjectList(theinstanceBOs,
									InstanceProvider.getTaskDesc(theInstanceBO));
						workerQueueObject = WorkerQueueManager
								.updateInitialWorkerQueueStatus(workerQueueObject);
						for(InstanceBO instanceBOWQ : theinstanceBOs) {
							for(WorkerQueueBO workerQueueBO : workerQueueObject) {
								if(instanceBOWQ.getWqTargetId() == workerQueueBO.getWqTargetId()) {
									instanceBOWQ.setWqTaskID(workerQueueBO.getCurrentTaskKey());
								}
							}
						}
					}
					workerQueueObjects.addAll(workerQueueObject);					
					instanceBO.setErrors(extractPolicyErrors);
					updateFinalWQStatusForRun(workerQueueObjects, instanceBO);
					} else {
						return true;
					}
				}
			}
		} catch (ServiceException se) {
			flag = true;
			logger.error(se);
			CloudOneErrors extractPolicyErrors = BusinessPoliciesUtil
					.extractPolicyErrors(se);
			for (InstanceBO tempBO : instanceBOs) {
				tempBO.setErrors(extractPolicyErrors);
				updateFinalWQStatusForRun(workerQueueObjects, tempBO);
			}
			throw new ICException(se);
		}
		return false;
	}

	@Transactional
	public List<InstanceBO> preRunOperation(List<InstanceBO> instanceBOs) throws DALException {
		String operation = instanceBOs.get(0).getOperation();
		String newInstanceNamePrefix = instanceBOs.get(0).getNickName()
				+ InstancesProvisioningConstants.NAME_DELIMITER
				+ "clone";
		int maxFromWorkerQueue = 0;

		if ((ProviderTypeEnum.SAVVIS == instanceBOs.get(0).getProviderType() || ProviderTypeEnum.SAVVISVPDC == instanceBOs
				.get(0).getProviderType())
				&& InstancesActionsEnum.CLONE_VM.getValue().equalsIgnoreCase(
						operation)) {
			int cloneCount = instanceBOs.get(0).getNoOfClones();
			if(cloneCount > 1) {
				List<InstanceBO> finalInstanceBOs = new ArrayList<>();
				for(int i = 0; i < cloneCount; i++) {
					InstanceBO instBO = new InstanceBO();
					try {
						BeanUtils.copyProperties(instBO, instanceBOs.get(0));
						finalInstanceBOs.add(instBO);
					} catch (            IllegalAccessException | InvocationTargetException e) {
						logger.error(e);
					}
				}
				instanceBOs = finalInstanceBOs;
			}				
			maxFromWorkerQueue = ICUtils.getMaxCountFromWorkerQueue(instanceBOs.get(0).getCloudId(), newInstanceNamePrefix);
		}
		int ctr = 1;
		InstanceDetailsDAO instDAO = getFactory().getInstanceDetailsDAO();
		for (InstanceBO instanceBO : instanceBOs) {
			if (GlobalConstant.N_A.equalsIgnoreCase(instanceBO
					.getSystemAddress())) {
				instanceBO.setSystemAddress(null);
			}
			if (GlobalConstant.N_A.equalsIgnoreCase(instanceBO
					.getPrivateIPAddress())) {
				instanceBO.setPrivateIPAddress(null);
			}
			if (ProviderTypeEnum.SAVVIS == instanceBO.getProviderType()
					|| ProviderTypeEnum.SAVVISVPDC == instanceBO
					.getProviderType()) {
				populateOrgId(instanceBO);
			} else if (ProviderTypeEnum.SCVMM == instanceBO.getProviderType()) {
				InstanceDetailsDO instDO = instDAO.get(InstanceDetailsDO.class, instanceBO.getInstanceId());
				ResourcePoolDO resourcePoolDO = instDO.getResourcePoolDO();
				if(resourcePoolDO != null) {
					instanceBO.setResourcePoolId(resourcePoolDO.getResourcePoolIdInProvider());
					instanceBO.setResourcePoolName(resourcePoolDO.getName());
				} else {
					instanceBO.setResourcePoolName(GlobalConstant.N_A);
				}
				instanceBO.setWsUrl(ICUtils.getServiceConsoleUrl(instanceBO.getCloudId()));
			}
			if (InstancesActionsEnum.CLONE_VM.getValue().equalsIgnoreCase(
					operation)
					|| InstancesActionsEnum.CLONE_TO_TEMPLATE.getValue()
					.equalsIgnoreCase(operation)
					|| InstancesActionsEnum.CONVERT_TO_TEMPLATE.getValue()
					.equalsIgnoreCase(operation)) {

				if ((ProviderTypeEnum.SAVVIS == instanceBOs.get(0)
						.getProviderType() || ProviderTypeEnum.SAVVISVPDC == instanceBOs
						.get(0).getProviderType())
						&& InstancesActionsEnum.CLONE_VM.getValue()
						.equalsIgnoreCase(operation)) {

					instanceBO.setNewNickName(newInstanceNamePrefix
							+ InstancesProvisioningConstants.NAME_DELIMITER
							+ (maxFromWorkerQueue + ctr));
					ctr++;
					if(instanceBO.getUpdatedByUser() != 0) {
						// CO-11555: Setting user id to the user performing the
						// action so that new VM is assigned accordingly
						instanceBO.setUserID(instanceBO.getUpdatedByUser());
					}
				} else {
					InstanceHelper instanceHelper = new InstanceHelper();
					instanceBO = instanceHelper.getNewNickName(instanceBO,
							operation);
				}
				try {
					String templateId = ICUtils.getInstanceInfoFromDB(
							instanceBO).getMonitoringTemplateId();
					if (templateId != null) {
						instanceBO.setMonitoringTemplateId("" + templateId);
					}

				} catch (Exception excepp) {
					logger.error("Unable to get original template id: ", excepp);
				}
				if (!InstancesActionsEnum.CONVERT_TO_TEMPLATE.getValue()
						.equalsIgnoreCase(operation)) {
					instanceBO.setWqTarget(instanceBO.getNewNickName());
					instanceBO.setWqTargetId(instanceBO.getInstanceId());
					instanceBO.setWqTargetType(EntityTypeEnum.INSTANCE.getId());					
				}
			}
			if(ProviderTypeEnum.OPENSTACK == instanceBO.getProviderType() 
					&& InstancesActionsEnum.CLONE_TO_TEMPLATE.getValue()
					.equalsIgnoreCase(operation)){
				instanceBO.setWqTarget(StringUtils
						.getDisplayInstanceAndNickName(
								instanceBO.getInstanceName(),
								instanceBO.getNickName()));
				instanceBO.setWqTargetId(instanceBO.getInstanceId());
				instanceBO.setWqTargetType(EntityTypeEnum.INSTANCE.getId());
			}

			if ("START".equalsIgnoreCase(operation)
					|| (InstancesActionsEnum.POWERON.getValue()
							.equalsIgnoreCase(operation))) {
				//CO-11303: Using concatenated name for WQ to avoid confusion with same name VMs
				instanceBO.setWqTarget(StringUtils
						.getDisplayInstanceAndNickName(
								instanceBO.getInstanceName(),
								instanceBO.getNickName()));
				instanceBO.setWqTargetId(instanceBO.getInstanceId());
				instanceBO.setWqTargetType(EntityTypeEnum.INSTANCE.getId());

				// set the future state for all instances
				instanceBO.setFutureState(InstanceStateEnum.POWERON);
			}
			// Added for JIRA CO-5354 to set worker queue target for
			// shutdown/poweroff properly
			if ("STOP".equalsIgnoreCase(operation)
					|| (InstancesActionsEnum.POWEROFF.getValue()
							.equalsIgnoreCase(operation))) {
				//CO-11303: Using concatenated name for WQ to avoid confusion with same name VMs
				instanceBO.setWqTarget(StringUtils
						.getDisplayInstanceAndNickName(
								instanceBO.getInstanceName(),
								instanceBO.getNickName()));
				// set the future state for all instances
				instanceBO.setFutureState(InstanceStateEnum.POWEROFF);
			}
			if ("DELETE".equalsIgnoreCase(operation)
					|| (InstancesActionsEnum.TERMINATE_INSTANCES.getValue()
							.equalsIgnoreCase(operation))) {
				// Using concatenated name for WQ to avoid confusion with same name VMs
				instanceBO.setWqTarget(StringUtils
						.getDisplayInstanceAndNickName(
								instanceBO.getInstanceName(),
								instanceBO.getNickName()));
				// set the future state for all instances
				//	instanceBO.setFutureState(InstanceStateEnum.DELETED);
			}
			if (ProviderTypeEnum.AMAZON == instanceBO.getProviderType()) {				
				if (operation.equalsIgnoreCase(InstancesActionsEnum.DELETE.getValue())
						|| operation
						.equalsIgnoreCase(InstancesActionsEnum.TERMINATE_INSTANCES.getValue())) {
					EBSVolumeDetailsDAO ebsVolumeDetailsDAO = getFactory().getEBSVolumeDetailsDAO();
					EBSVolumeDetailsDO ebsVolumeDetailsDO = new EBSVolumeDetailsDO();
					ebsVolumeDetailsDO.setInstanceDO(ebsVolumeDetailsDAO.get(
							InstanceDetailsDO.class, instanceBO.getInstanceId()));
					List<EBSVolumeDetailsDO> volumesList = ebsVolumeDetailsDAO.fetchEBSVolumesDetailsByInstanceID(ebsVolumeDetailsDO);
					boolean isFirst = true;
					StringBuilder volumeBuilder = new StringBuilder("");
					if(volumesList != null && !volumesList.isEmpty()) {
						for (EBSVolumeDetailsDO eBSVolumeDetailsDO : volumesList) {
							if(eBSVolumeDetailsDO.isDeleteOnTermination()){
							if (!isFirst) {
								volumeBuilder.append(", ");
							} else {
								isFirst = false;
							}
							volumeBuilder.append(eBSVolumeDetailsDO.getEBSVolumeID());
							volumeBuilder.append(" (");
							volumeBuilder.append(eBSVolumeDetailsDO.getSize());
							volumeBuilder.append(")");
							}
						}
						instanceBO.setVolumesAttached(volumeBuilder.toString());
					}                     
				}
			}
		}
		return instanceBOs;
	}

	public void updateFinalWQStatusForRun(
			List<WorkerQueueBO> workerQueueObjects, InstanceBO instanceBO)
					throws ICException {

		List<InstanceBO> instBOs = Arrays.asList(instanceBO);
		// Update WQ final status
		workerQueueObjects = WorkerQueueHelper.buildFinalWorkerQueueObjectList(
				ICUtils.setWQTaskID(instBOs, workerQueueObjects), InstanceProvider.getTaskDesc(instanceBO));
		WorkerQueueManager.updateFinalWorkerQueueStatus(workerQueueObjects);

	}

	public void updateInstanceState(InstanceBO instanceBO,
			List<InstanceBO> resultInstBOs) throws DALException {

		String operation = instanceBO.getOperation();
		logger.debug("operaton is" + operation);

		boolean isStateUpdatedAfterPowerOperations = updateMontoringProfileStateAfterPowerOperations(
				instanceBO, operation);

		List<InstanceBO> instBOs = Arrays.asList(instanceBO);
		// Fix for CO-681: update the instance state
		// Fix for 2533 : convert to Template
		if (InstancesActionsEnum.CONVERTTOIMAGE.getValue().equalsIgnoreCase(operation) || InstancesActionsEnum.AZURECLONETOIMAGE.getValue().equalsIgnoreCase(operation)) {
			saveImageDetailsInDB(instBOs);
			if(instanceBO.getImageExpirationDate() != null && !instanceBO.getImageExpirationDate().isEmpty()){
				try {
					InstanceBO expireImageInstanceBO = new InstanceBO();
					BeanUtils.copyProperties(expireImageInstanceBO, instanceBO);
					List<InstanceBO> expireImageinstBOs = Arrays.asList(expireImageInstanceBO);
					Calendar calendar = Calendar.getInstance();
					calendar.add(Calendar.MONTH, Integer.parseInt(instanceBO.getImageExpirationDate()));
					DateFormat dateFormat = new SimpleDateFormat(DateTimeFormatEnum.DTF_20.getPattern());
					String expireDate = dateFormat.format(calendar.getTime());
					ImageInstanceBO imageInstanceBO = instanceBO.getImageInstanceBO();
					RecurrenceBO recurrenceBO = new RecurrenceBO();
					recurrenceBO.setTimeZone(ICUtils.getUserTimeZone(instanceBO.getUserID(), instanceBO.getCloudId()));
					recurrenceBO.setCloudId(instanceBO.getCloudId());
					recurrenceBO.setProviderID(instanceBO.getProviderID());
					recurrenceBO.setProviderType(instanceBO.getProviderType());
					recurrenceBO.setActiveUserGroupID(instanceBO.getActiveUserGroupID());
					recurrenceBO.setUserID(instanceBO.getUserID());
					recurrenceBO.setSchedulingStartDate(expireDate);
					recurrenceBO.setSchedulingTime("00:00");
					expireImageInstanceBO.setOperation(InstancesActionsEnum.CONVERTTOIMAGEEXPIRE.getValue());
					expireImageInstanceBO.setImageDescription(imageInstanceBO.getAmiID());
					expireImageinstBOs = Arrays.asList(expireImageInstanceBO);
					expireImageinstBOs = scheduleAction(expireImageinstBOs, recurrenceBO);
				} catch (ICException | IllegalAccessException | InvocationTargetException e) {	
					logger.error(e);
				}
			}

		}else if (InstancesActionsEnum.CONVERTTOIMAGEEXPIRE.getValue().equalsIgnoreCase(
				operation)) {
			deleteImageDetails(instBOs);
		}else if ((!InstancesActionsEnum.CLONE_VM.getValue().equalsIgnoreCase(
				operation)
				&& !InstancesActionsEnum.CLONE_TO_TEMPLATE.getValue()
				.equalsIgnoreCase(operation)
				&& !"START".equalsIgnoreCase(operation.toString())
				&& !"STOP".equalsIgnoreCase(operation.toString())
				&& !"REBOOT".equalsIgnoreCase(operation.toString()) && !"TERMINATE"
				.equalsIgnoreCase(operation.toString()))
				|| InstancesActionsEnum.CONVERT_TO_TEMPLATE.getValue()
				.equalsIgnoreCase(operation)
				|| isStateUpdatedAfterPowerOperations) {

			if(!MessageStringConstants.FAILURE.equalsIgnoreCase(instanceBO.getTaskStatus())) {
				this.updateInstanceStateInDB(instanceBO);
			}
		} else {
			// Prakhar - not a clean way to do this, but no options at the
			// moment.
			CreateInstanceInfoBO inputBO = new CreateInstanceInfoBO();
			inputBO.setProviderID(instanceBO.getProviderID());
			inputBO.setProviderType(instanceBO.getProviderType());
			inputBO.setCloudId(instanceBO.getCloudId());
			if(instanceBO.getUpdatedByUser() > 0) {
				inputBO.setUserID(instanceBO.getUpdatedByUser());
			} else {
				inputBO.setUserID(instanceBO.getUserID());
			}
			inputBO.setActiveUserGroupID(instanceBO.getActiveUserGroupID());
			if (InstancesActionsEnum.CLONE_VM.getValue().equalsIgnoreCase(
					operation)
					|| InstancesActionsEnum.CLONE_TO_TEMPLATE.getValue()
					.equalsIgnoreCase(operation)
					|| InstancesActionsEnum.CONVERT_TO_TEMPLATE.getValue()
					.equalsIgnoreCase(operation)) {
				// CO-2896, status will be same as source VM
				logger.error("Source instance monitoring status is: "
						+ instanceBO.getMonitoringStatus());
				// CO-6014 BEGIN
				inputBO.setOperatingSystem(instanceBO.getOsName());
				inputBO.setOperatingSystemName(instanceBO.getOsName());
				inputBO.setNoOfVirtualProcessors(instanceBO.getNoOfCPUs());
				inputBO.setMemorySize(instanceBO.getMemorySize());
				if (StringUtils.doTrim(instanceBO.getMemorySizeUnit())
						.isEmpty()) {
					inputBO.setMemorySizeUnit(GlobalConstant.UNIT_MB);
				} else {
					inputBO.setMemorySizeUnit(instanceBO.getMemorySizeUnit());
				}
				inputBO.setDiskSize(new Long(instanceBO.getTotalDiskSize())
				.intValue());
				if (StringUtils.doTrim(instanceBO.getTotalDiskSizeUnit())
						.isEmpty()) {
					inputBO.setDiskSizeUnit(GlobalConstant.UNIT_GB);
				} else {
					inputBO.setDiskSizeUnit(instanceBO.getTotalDiskSizeUnit());
				}
				inputBO.setDiskRequired(true);
				inputBO.setTemplate(instanceBO.getAmiImageId());
				// CO-6014 END
				inputBO.setVirtualDisks(instanceBO.getDisks());
				inputBO.setNatOneToOneFlag(instanceBO.isNatOnetoOneFlag());
				inputBO.setNotificationID(instanceBO.getNotificationID());
				inputBO.setUserName(instanceBO.getUserName()); 

			} else {
				// set status to NA, as was being done previous to CO-2896

				// this else block is now redundant and should be removed in X9

				instanceBO.setMonitoringStatus(InstanceMonitoringStatusEnum.NA
						.getValue());
			}

			ICreateInstanceDomain obj = CreateInstanceFactory
					.getInstance(instanceBO.getProviderType());

			resultInstBOs.addAll(obj.updateInstanceDetailsInDAL(instBOs,
					inputBO));
		}
		ReservationHelper reservationHelper = new ReservationHelper();
		// Release Datastores space if VM deletion is successful.
		if (InstancesActionsEnum.DELETE.getValue().equalsIgnoreCase(
				instanceBO.getOperation())
				&& MessageStringConstants.SUCCESS.equalsIgnoreCase(instanceBO
						.getTaskStatus())) {
			this.updateDataStoresAfterDeleteVM(instanceBO);
			if(instanceBO.getExpirationDate() != null)
				reservationHelper.updateScheduleQuotaStatus(instanceBO);
		}

		if(InstancesActionsEnum.TERMINATE_INSTANCES.getValue().equalsIgnoreCase(instanceBO.getOperation()) || InstancesActionsEnum.DELETE.getValue().equalsIgnoreCase(instanceBO.getOperation()) && (instanceBO.getProviderType() == ProviderTypeEnum.AMAZON || instanceBO.getProviderType() == ProviderTypeEnum.AZURE)) {
			this.deleteEBSVolumeinDB(instanceBO);
		}
	}

	@Transactional
	public void deleteEBSVolumeinDB(InstanceBO instanceBO) throws DALException {
		try {
			EBSVolumeDetailsDAO ebsVolumeDetailsDAO = getFactory().getEBSVolumeDetailsDAO();
			InstanceDetailsDO instanceDetailsDO = ebsVolumeDetailsDAO.get(InstanceDetailsDO.class, instanceBO.getInstanceId());
			List<EBSVolumeDetailsDO> ebsVolumeStatusDOList=getEBSVolumeStatusDOListFromInstanceBO(ebsVolumeDetailsDAO, instanceBO,instanceDetailsDO);
			if(instanceBO.getProviderType() == ProviderTypeEnum.AMAZON){
				performEBSVolumeDetailsUpdateAmazon(ebsVolumeDetailsDAO, ebsVolumeStatusDOList);
			}
			else if (instanceBO.getProviderType() == ProviderTypeEnum.AZURE){
				performVMDiskDetailsUpdate(instanceDetailsDO, instanceBO.isDeleteStorage());
				if(null != ebsVolumeStatusDOList && ebsVolumeStatusDOList.size() > 0)
					performEBSVolumeDetailsUpdate(ebsVolumeDetailsDAO, instanceBO.isDeleteStorage(), ebsVolumeStatusDOList);
			}

		} catch (DALException e) {
			logger.error(e.getMessage());
			throw new DALException(e);
		}					
	}
	
	@Transactional
	private List<EBSVolumeDetailsDO> getEBSVolumeStatusDOListFromInstanceBO(EBSVolumeDetailsDAO ebsVolumeDetailsDAO,InstanceBO instanceBO,InstanceDetailsDO instanceDetailsDO)throws DALException{
		EBSVolumeDetailsDO eBSVolumeDetailsDO = new EBSVolumeDetailsDO();
		eBSVolumeDetailsDO.setInstanceDO(instanceDetailsDO);
		return ebsVolumeDetailsDAO.fetchEBSVolumesDetailsByInstanceID(eBSVolumeDetailsDO);
	}

	@Transactional
	private void performEBSVolumeDetailsUpdateAmazon(EBSVolumeDetailsDAO ebsVolumeDetailsDAO,List<EBSVolumeDetailsDO> ebsVolumeStatusDOList)throws DALException{
		EBSSnapshotDetailsDAO ebsSnapshotDetailsDAO = getFactory().getEBSSnapshotDetailsDAO();
		List<EBSVolumeDetailsDO> ebsVolumeStatusDODeleteList = new ArrayList<EBSVolumeDetailsDO>();	
		EBSSnapshotDetailsDO ebsSnapshotDetailsDO=new EBSSnapshotDetailsDO(); 
		for(EBSVolumeDetailsDO eBSVolDetDO : ebsVolumeStatusDOList) {
			if(eBSVolDetDO.isDeleteOnTermination()){
				ebsVolumeStatusDODeleteList.add(eBSVolDetDO);
				ebsSnapshotDetailsDO.setEBSVolumeDetailsDO(eBSVolDetDO);
				List<EBSSnapshotDetailsDO> ebsSnapshotDOList=ebsSnapshotDetailsDAO.fetchByEBSVolumeId(ebsSnapshotDetailsDO);
				if(!ebsSnapshotDOList.isEmpty()){
					for(EBSSnapshotDetailsDO ebsshot:ebsSnapshotDOList){
						ebsshot.setEBSVolumeDetailsDO(null);
					}
					ebsSnapshotDetailsDAO.updatetBatch(ebsSnapshotDOList);
				}
			}
		}
		if(!ebsVolumeStatusDODeleteList.isEmpty()) {
			ebsVolumeDetailsDAO.deleteBatch(ebsVolumeStatusDODeleteList);
		}
	}

	@Transactional
	private void performEBSVolumeDetailsUpdate(EBSVolumeDetailsDAO ebsVolumeDetailsDAO,boolean isDeleteStorage,List<EBSVolumeDetailsDO> ebsVolumeStatusDOList)throws DALException{
		if(isDeleteStorage){
			ebsVolumeDetailsDAO.deleteBatch(ebsVolumeStatusDOList);
		}else{
			ebsVolumeDetailsDAO.updatetBatch(populateVMDetailsDOsWithDiskDeleteValues(ebsVolumeStatusDOList));		
		}
	}

	@Transactional
	private void performVMDiskDetailsUpdate(InstanceDetailsDO instanceDetailsDO,boolean isDeleteStorage)throws DALException{
		VmDiskDetialsDAO vmDiskDetialsDAO=getFactory().getVmDiskDetialsDAO();
		List<VmDiskDetialsDO> vmDiskDetialsDOs=getVMDiskDetailsFromInstanceID(vmDiskDetialsDAO, instanceDetailsDO);
		if(null != vmDiskDetialsDOs && vmDiskDetialsDOs.size() > 0)
			vmDiskDetialsDAO.updatetBatch(populateVMDetailsDOsWithDiskDeleteValues(vmDiskDetialsDOs, isDeleteStorage));
	}

	@Transactional
	private List<VmDiskDetialsDO> getVMDiskDetailsFromInstanceID(VmDiskDetialsDAO vmDiskDetialsDAO,InstanceDetailsDO instanceDetailsDO)throws DALException{
		VmDiskDetialsDO vmDiskDetialsDO=new VmDiskDetialsDO();
		vmDiskDetialsDO.setInstanceDetailsDO(instanceDetailsDO);
		return vmDiskDetialsDAO.fetchByInstanceId(vmDiskDetialsDO);
	}

	private List<VmDiskDetialsDO> populateVMDetailsDOsWithDiskDeleteValues(List<VmDiskDetialsDO> vmDiskDetialsDOs,boolean isDeleteStorage)throws DALException{
		for (VmDiskDetialsDO vmDiskDetialsDO : vmDiskDetialsDOs) {
			vmDiskDetialsDO.setInstanceDetailsDO(null);
			if(isDeleteStorage)
				vmDiskDetialsDO.setActive(false);
		}
		return vmDiskDetialsDOs;
	}

	private List<EBSVolumeDetailsDO> populateVMDetailsDOsWithDiskDeleteValues(List<EBSVolumeDetailsDO> ebsVolumeStatusDOList)throws DALException{
		for (EBSVolumeDetailsDO ebsVolumeDetailsDO : ebsVolumeStatusDOList) {
			ebsVolumeDetailsDO.setInstanceDO(null);
			ebsVolumeDetailsDO.setStatus(GlobalConstant.AVAILABLE);
		}
		return ebsVolumeStatusDOList;
	}

	private boolean updateMontoringProfileStateAfterPowerOperations( 
			InstanceBO instanceBO, String operation) {

		boolean isPowerOperation = false;

		boolean isSuccesful = MessageStringConstants.SUCCESS.equalsIgnoreCase(instanceBO.getTaskStatus());

		if (! isSuccesful) {
			logger.debug( "operation failed: {} on vm {}",operation,instanceBO.getInstanceId());
		} else {
			logger.debug( "operation succeded: {} on vm {}",operation,instanceBO.getInstanceId() );
		}

		if (InstancesActionsEnum.POWEROFF.getValue()
				.equalsIgnoreCase(operation)
				|| InstancesActionsEnum.SHUTDOWN.getValue().equalsIgnoreCase(
						operation)
						|| InstancesActionsEnum.STOP_INSTANCES.getValue()
						.equalsIgnoreCase(operation)
						|| operation.equalsIgnoreCase(InstancesActionsEnum.POWERON
								.getValue())
								|| operation
								.equalsIgnoreCase(InstancesActionsEnum.START_INSTANCES
										.getValue())
										|| operation
										.equalsIgnoreCase(InstancesActionsEnum.REBOOT_INSTANCES
												.getValue())
												|| operation.equalsIgnoreCase(InstancesActionsEnum.PAUSE
														.getValue())
														|| operation.equalsIgnoreCase(InstancesActionsEnum.SAVE
																.getValue())
																|| operation.equalsIgnoreCase(InstancesActionsEnum.SUSPEND
																		.getValue())
																		|| operation.equalsIgnoreCase(InstancesActionsEnum.RESET
																				.getValue())) {
			isPowerOperation = true;
		}
		return isPowerOperation;
	}

	/**
	 * Method to notify sms/email while performing action on instances
	 * 
	 * @param T
	 *            BaseBO
	 * @throws ICException
	 */
	@Transactional
	public <T extends BaseBO> void notifyOnActions(T instanceInfo)
			throws ICException {
		// JIRA CO-1687
		cdf = CloudOneDAOFactoryBuilder.getFactory();
		if (instanceInfo != null) {
			String messageBeanType = instanceInfo.getClass().getName();

			String cloudName = "";
			String orgName = "";
			OrganizationBO organizationBO = ICUtils
					.getCloudOrganisationDetails(instanceInfo);
			if (organizationBO != null) {
				cloudName = organizationBO.getCloudName();
				if (organizationBO.getOrganisationName() != null) {
					orgName = organizationBO.getOrganisationName();
				}
			}
			instanceInfo.setCloudName(cloudName);
			instanceInfo.setOrganisationName(orgName);

			CloudOneErrors errors = instanceInfo.getErrors();
			String status = (null != errors && errors.hasErrors() ? MessageStringConstants.FAILED
					: MessageStringConstants.COMPELTE);

			try {
				ProvisionBean provisionBean = null;
				if (messageBeanType
						.equalsIgnoreCase(InstanceBO.class.getName())) {
					InstanceBO instanceBO = (InstanceBO) instanceInfo;
					if(ProviderTypeEnum.VMWARE.equals(instanceBO.getProviderType())){
						instanceBO.setServerMasterName(instanceBO.getServerName());
					}
					logger.info("instanceBO  NotificationID  is: {}",
							instanceBO.getNotificationID());

					provisionBean = InstanceProvider
							.populateProvisionBeanFromBO(instanceBO, status);
					provisionBean.setName(nameInNotification(instanceBO));
					provisionBean.setProviderType(instanceBO.getProviderType());
					
				} else if (messageBeanType
						.equalsIgnoreCase(ProcessInstanceBO.class.getName())) {
					ProcessInstanceBO instanceBO = (ProcessInstanceBO) instanceInfo;
					InstanceDetailsDO instanceDetailsDO = ICUtils.getMatchingInstanceDOByInstanceName(instanceBO.getCloudId(), instanceBO.getProviderID(), instanceBO.getInstanceName());
					if (instanceDetailsDO != null && instanceDetailsDO.getNotificationDO() != null) {
						logger.info("notification for selected instance {}",
								instanceDetailsDO.getNotificationDO()
								.getNotificationID());
						instanceBO.setNotificationID(instanceDetailsDO
								.getNotificationDO().getNotificationID());
					}
					provisionBean = InstanceProvider
							.populateProvisionBeanFromBO(instanceBO, status);
					provisionBean.setName(nameInNotification(instanceBO));
					provisionBean.setProviderType(instanceBO.getProviderType());
				}
			
				if (provisionBean != null) {
					if (provisionBean.getNotificationId() > 0) {
						// 133053: JIRA 2712, notification ID is -1 if template
						// is "do not notify"
						
						sendNotification(provisionBean);
					}
				}
			} catch ( Exception e) {
				List<T> instanceBOs = new ArrayList<>();
				instanceBOs.add(instanceInfo);
				populateErrorMessages(instanceBOs, e);
			}

		}
		// JIRA CO-1687
	}

	@Transactional
	public void updateInstanceStateInDB(InstanceBO instanceBO) throws DALException {
		try {
			if (instanceBO.getInstanceName() != null) {
				InstanceDetailsDAO instanceDetailsDAO = getFactory().getInstanceDetailsDAO();
				InstanceDetailsDO instanceDO = instanceDetailsDAO.get(
						InstanceDetailsDO.class, instanceBO.getInstanceId());
				instanceDO.setOperation(instanceBO.getOperation());
				logger.info("Instance id to be updated after AOI : "
						+ instanceDO.getInstanceID() + " = "
						+ instanceDO.getInstanceName());

				if (InstanceStateEnum.POWERON.getValue().equalsIgnoreCase(
						instanceBO.getState()) && StringUtils.isNotEmpty(instanceBO.getLastBootTime())) {
					if (instanceBO.getLastBootTime().matches(DateTimeFormatEnum.DTF_26.getRegex())){
						instanceDO.setLastBootTime(instanceBO.getLastBootTime());
					}else{
						instanceDO.setLastBootTime(DateUtils
								.getFormatedDateWithTimezone(DateUtils
										.getFormatedDate(
												instanceBO.getLastBootTime(),
												DateTimeFormatEnum.DTF_10)));						
					}
				}
				int serverId = getServerId(instanceBO);
				if((instanceBO.getProviderType()
						.equals(ProviderTypeEnum.VMWARE)||instanceBO.getProviderType()
						.equals(ProviderTypeEnum.POWERVM)) && serverId>0){
					instanceDO.setServerMasterDO(instanceDetailsDAO.get(ServerMasterDO.class, serverId));
				}
				if (instanceBO.getProviderType().equals(
						ProviderTypeEnum.AMAZON)
						&& instanceDO.getVpcAmazon() == null) {
					for(ElasticIPDetailsDO elasticIPDetailsDO : instanceDO.getElasticIPDetailsDO()) {
						elasticIPDetailsDO.setInstanceDetailsDO(null);
					}
				}


				instanceDO.setState(instanceBO.getState());

				instanceDO
				.setUpdatedByUser((instanceBO.getUpdatedByUser() > 0) ? instanceBO
						.getUpdatedByUser() : instanceBO.getUserID());
				// Expiration date Updated Here JIRA:CO-812
				instanceDO.setExpirationDate(
						instanceBO.getExpirationDate());
				instanceDO.setReservationID(instanceBO.getReservationID());
				// Set the template information
				instanceDO.setTemplateVM(instanceBO.isTemplate());
				instanceDO.setNickName(instanceBO.getNickName());// Fix for 2533 : convert to Template

				instanceDO.setPrivate_IP(StringUtils.isNotEmpty(instanceBO.getPrivateIPAddress()) ? instanceBO.getPrivateIPAddress() : instanceDO.getPrivate_IP());
				instanceDO.setIpAddress(StringUtils.isNotEmpty(instanceBO.getSystemAddress()) ? instanceBO.getSystemAddress() : instanceDO.getIpAddress());

				logger.info("updateInstanceStateInDB MonitoringTemplateId: {}",
						instanceBO.getMonitoringTemplateId() == null ? "NULL"
								: instanceBO.getMonitoringTemplateId());
				try {
					if (instanceBO.getMonitoringTemplateId() != null
							&& !"null".equals(instanceBO
									.getMonitoringTemplateId())
									&& 0 < Integer.parseInt(instanceBO
											.getMonitoringTemplateId())) {
						TemplateDetailsDO tdDO = instanceDetailsDAO.get(
								TemplateDetailsDO.class, Integer
								.parseInt(instanceBO
										.getMonitoringTemplateId()));
						instanceDO.setTemplateDetailsDO(tdDO);
					}
				} catch (NumberFormatException nex) { // NumberFormatException
					logger.error("updateInstanceStateInDB error: ", nex);
				}
				// CO-15032: Using merge instead of update to avoid stale object
				// exception caused by concurrent tx updates
				instanceDetailsDAO.merge(instanceDO);
				logger.info("Committed state after AOI : {}",
						instanceBO.getState());
			}
		} catch (Exception e) {
			reportInstanceUpdateSpecificError(e);
			throw e;
		}
	}
	
	
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public int getServerId(InstanceBO instanceBO)
			throws DALException {
		ServerMasterDAO dao = getFactory().getServerMasterDAO();
		ServerMasterDO serverMasterDo = new ServerMasterDO();
		serverMasterDo.setCloudMasterDO(dao.get(CloudMasterDO.class, instanceBO.getCloudId()));
		serverMasterDo.setProviderMasterDO(dao.get(ProviderMasterDO.class, instanceBO.getProviderID()));

		List<ServerMasterDO> serverMasterDOs = dao
				.fetchByCloudIdProviderId(serverMasterDo);
		if (instanceBO.getProviderType()
				.equals(ProviderTypeEnum.VMWARE)) {
			for (ServerMasterDO serverMasterDO : serverMasterDOs) {
				if (serverMasterDO.isActive()
						&& serverMasterDO.getServerName()
						.equalsIgnoreCase(
								instanceBO.getServerName())) {
					return serverMasterDO.getServerId();

				}
			}
		} else if (instanceBO.getProviderType().equals(
				ProviderTypeEnum.POWERVM)) {
			for (ServerMasterDO serverMasterDO : serverMasterDOs) {
				if (serverMasterDO.isActive()
						&& serverMasterDO.getHostOid()
						.equalsIgnoreCase(
								String.valueOf(instanceBO
										.getHostOid()))) {
					return serverMasterDO.getServerId();				}
			}
		} 

		return 0;
	}

	@Transactional
	public void updateDataStoresAfterDeleteVM(InstanceBO instanceBO) throws DALException {
		DataStoresDAO dsDAO = getFactory().getDataStoresDAO();	
		CloudMasterDO cloudMasterDO = dsDAO.get(CloudMasterDO.class, instanceBO.getCloudId());
		try {			
			InstanceDetailsDO instanceDetailsDO = dsDAO.get(InstanceDetailsDO.class, instanceBO.getInstanceId());
			if (instanceDetailsDO != null && instanceDetailsDO.getDataStoresDO() != null) {
				DataStoresDO dataStoresDO = dsDAO.get(DataStoresDO.class,
						instanceDetailsDO.getDataStoresDO()
						.getDataStoreId());
				if (dataStoresDO != null) {
					double freeSpaceInGB = Double.parseDouble(dataStoresDO
							.getFreeSpaceAvailable());
					long releasedSpaceInKB = instanceBO.getDiskSizeInKB();
					// Adding cast as part of findbugs error removal - int
					// division result cast to double or float
					double releasedSpaceInGB = releasedSpaceInKB
							/ (double) (1024 * 1024);
					double adjustedFreeSpace = freeSpaceInGB
							+ releasedSpaceInGB;
					// Update Datastore here with the adjustedFreeSpace
					String freeSpaceAvailable = String
							.valueOf(adjustedFreeSpace);
					dataStoresDO.setFreeSpaceAvailable(freeSpaceAvailable);
					if(dataStoresDO.getCloudMasterDO()==null)
					{
						dataStoresDO.setCloudMasterDO(cloudMasterDO);
					}
					dsDAO.saveOrUpdate(dataStoresDO);
				}
			}			
		} catch (NumberFormatException | DALException e) {
			throw new DALException(e);
		}
	}

	@Transactional
	public void updateDataStoresAfterAddingDisk(InstanceBO instanceBO,
			InstanceDetailsDAO instDAO) throws DALException {
		InstanceDetailsDO instDO = null;
		logger.debug("INSIDE.....");
		try {
			logger.info("DS NAME is ... {}",instanceBO.getDatastoreName());
			instDO = instDAO.get(InstanceDetailsDO.class,
					instanceBO.getInstanceId());
			DataStoresDAO dsDAO = getFactory().getDataStoresDAO();
			List<DataStoresDO> dsListforUpdate = new ArrayList<>();
			if (instDO.getDataCenterDetailsDO() != null) {
				DataStoresDO dataStoresDO = new DataStoresDO();
				dataStoresDO.setDataCenterDetailsDO(instDO
						.getDataCenterDetailsDO());
				dataStoresDO.setDataStoreName(instanceBO.getDatastoreName());
				List<DataStoresDO> dsLost = dsDAO
						.fetchByDataCenterIDDataStoreName(dataStoresDO);
				if (dsLost != null) {
					for (DataStoresDO dsDO : dsLost) {
						logger.info("Updating the DataStore Id - {}",
								dsDO.getDataStoreId());
						double totalFreeSpace = Double.parseDouble(dsDO
								.getFreeSpaceAvailable());
						// Adding cast as part of findbugs error removal - int
						// division result cast to double or float
						double diskRequested = (instanceBO.getDiskSizeInKB() / (double) (1024 * 1024));
						totalFreeSpace -= diskRequested;
						dsDO.setFreeSpaceAvailable(Double
								.toString(totalFreeSpace));
						logger.info("New Freespace is {}",
								dsDO.getFreeSpaceAvailable());
						dsListforUpdate.add(dsDO);
					}
				} else {
					// Unlikely scenarion that there's no Data Store associated
					// with the Instance
					logger.error("No Datastore found to update the Disk details - need to throw the error");
					throw new DALException(
							ErrorConstants.CO_DAL_ERR_GENERIC.getDescription());
				}
			}
			dsDAO.updatetBatch(dsListforUpdate);
		} catch (DALException | NumberFormatException e) {
			throw new DALException(e);
		}
	}

	
	public void refreshOsDetails(OSDescriptorBO descriptorBO)
			throws ICException {
		List<OSDescriptorBO> osDetailsList = new ArrayList<>();
		try {
			IPlatformsDomain domain = new PlatformsDomainImpl();
			PlatformBO platformBO = domain.fetchProvider(PlatformsProvider
					.getInputPlatformBO(descriptorBO));
			descriptorBO.setPlatformBO(platformBO);
			if (platformBO.getProviderType() == ProviderTypeEnum.PHYSICAL_SERVERS) {
				List<OSDescriptorBO> providerOSDetails = new ArrayList<>();
				for (PhysicalServerOSEnum os : PhysicalServerOSEnum.values()) {
					providerOSDetails.add(getPhysOSObject(os));
				}
				osDetailsList.addAll(providerOSDetails);
			} else {
				slfacade = SLInstancesFacade.SINGLETON_INSTANCE;
				osDetailsList = slfacade.retrieveOsDetails(descriptorBO,
						PlatformsProvider.getProviderCredentials(platformBO));
			}
			// leaving the osdetails as it is if the values are not retrieved
			if (null != osDetailsList) {
				syncOSDetailsInDB(osDetailsList,platformBO,descriptorBO);
				
			}
		} catch (SLException e) {
			throw new ICException(e);
		} catch (DALException e) {
			logger.error("Error in fetching the details from InstanceDetails Using ExpirationDate {}",e.getMessage());
			throw new ICException(e);
		}

	}

	@Transactional
	public void syncOSDetailsInDB(List<OSDescriptorBO> osDetailsList, PlatformBO platformBO, OSDescriptorBO descriptorBO) throws DALException {
		OsDetailsDAO osDetailsDAO = getFactory().getOsDetailsDAO();
		OsDetailsDO osDetailsDO = new OsDetailsDO();
		CloudMasterDO cloudMasterDO = osDetailsDAO.get(CloudMasterDO.class, descriptorBO.getCloudId());
		ProviderMasterDO providerMasterDO = osDetailsDAO.get(ProviderMasterDO.class, descriptorBO.getProviderID());
		osDetailsDO.setCloudMasterDO(cloudMasterDO);
		osDetailsDO.setProviderMasterDO(providerMasterDO);

		List<OsDetailsDO> osDetailsDOList = osDetailsDAO
				.fetchAllOsDetails(osDetailsDO);
		if(platformBO.getProviderType() == ProviderTypeEnum.SAVVIS
				|| platformBO.getProviderType() == ProviderTypeEnum.SAVVISVPDC
				|| platformBO.getProviderType() == ProviderTypeEnum.VMWARE
				|| platformBO.getProviderType() == ProviderTypeEnum.PHYSICAL_SERVERS
				|| platformBO.getProviderType() == ProviderTypeEnum.IBMSL) {

			if(platformBO.getProviderType() == ProviderTypeEnum.SAVVIS
					|| platformBO.getProviderType() == ProviderTypeEnum.SAVVISVPDC) {
				List<OSDescriptorBO> providerOSDetails = new ArrayList<>();
				for (SavvisOSEnum os : SavvisOSEnum.values()) {
					providerOSDetails.add(getOSObject(os));
				}
				osDetailsList.addAll(providerOSDetails);
			}
			List<OsDetailsDO> finalList = getMergedOSDetails(
					osDetailsDOList, osDetailsList, descriptorBO,
					cloudMasterDO, providerMasterDO);
			osDetailsDAO.saveOrUpdateBatch(finalList);
		}
	}

	public List<OsDetailsDO> getMergedOSDetails(List<OsDetailsDO> osDetailsDOList, List<OSDescriptorBO> cloudOSDetailsList, OSDescriptorBO osDescBO, CloudMasterDO cloudMasterDO, ProviderMasterDO providerMasterDO) {
		List<OsDetailsDO> finalDOs = new ArrayList<>();
		Map<String, OSDescriptorBO> osDescriptorBOMap = new HashMap<>();
		Set<String> dbOSIds = new HashSet<>();
		if (cloudOSDetailsList != null) {
			for (OSDescriptorBO osDescriptorBO : cloudOSDetailsList) {
				osDescriptorBOMap.put(osDescriptorBO.getOsId(), osDescriptorBO);
			}
		}
		if (osDetailsDOList != null) {
			for (OsDetailsDO osDetailsDO : osDetailsDOList) {
				if (osDescriptorBOMap.containsKey(osDetailsDO.getOsId())) {
					// update and activate
					OSResourcesDetailsProvider.getOsDoToUpdateFromBO(
							osDescriptorBOMap.get(osDetailsDO.getOsId()),
							osDetailsDO, cloudMasterDO, providerMasterDO);
				} else {
					// de-activate
					osDetailsDO.setActive(false);
					osDetailsDO.setUpdatedByUser(osDescBO.getUserID());
				}
				dbOSIds.add(osDetailsDO.getOsId());
				finalDOs.add(osDetailsDO);
			}
		}
		if (cloudOSDetailsList != null) {
			for (OSDescriptorBO osDescriptorBO : cloudOSDetailsList) {
				if (!dbOSIds.contains(osDescriptorBO.getOsId())) {
					// add new entry
					OsDetailsDO osDetailsDO = OSResourcesDetailsProvider
							.getOsDoToInsertFromBO(osDescriptorBO,
									cloudMasterDO, providerMasterDO);
					finalDOs.add(osDetailsDO);
				}
			}
		}
		return finalDOs;
	}


	public List<CreateInstanceInfoBO> createInstances(
			CreateInstanceInfoBO createInstanceInfo) throws ICException {
		try{
			return validateLicenseAndProceed(createInstanceInfo);
		} catch (Exception e) {
			logger.error("Error -- > " + e.getMessage());
			if (e instanceof InvalidLicenseException) {
				List<WorkerQueueBO> workerQueueObjects = new ArrayList<>();
				TaskDescriptionEnum taskDescEnum = TaskDescriptionEnum.CREATE_INSTANCE;
				createInstanceInfo.setWqTarget(GlobalConstant.N_A);
				List<CreateInstanceInfoBO> createInstanceInfoBOs = new ArrayList<CreateInstanceInfoBO>();
				createInstanceInfoBOs.add(createInstanceInfo);
				workerQueueObjects = WorkerQueueHelper.buildInitialWorkerQueueObjectList(createInstanceInfoBOs, taskDescEnum);
				workerQueueObjects = WorkerQueueManager.updateInitialWorkerQueueStatus(workerQueueObjects);

				CloudOneErrors errors = new CloudOneErrors();
				CloudOneError error = new CloudOneError();
				error.setErrorMessage(e.getMessage());
				errors.addError(error);
				createInstanceInfoBOs.get(0).setErrors(errors);
				workerQueueObjects = WorkerQueueHelper.buildFinalWorkerQueueObjectList(ICUtils.setWQTaskID(createInstanceInfoBOs, workerQueueObjects), taskDescEnum);
				WorkerQueueManager.updateFinalWorkerQueueStatus(workerQueueObjects);
			}
			throw new ICException(e);
		}

	}

	/**
	 * @param createInstanceInfo
	 * @return
	 * @throws ICException
	 */
	@ValidateLicense(entity="instance")
	public List<CreateInstanceInfoBO> validateLicenseAndProceed(
			CreateInstanceInfoBO createInstanceInfo) throws ICException {
		ICreateInstanceDomain obj = CreateInstanceFactory
				.getInstance(createInstanceInfo.getProviderType());
		logger.debug("**validateLicenseAndProceed**");
		return obj.createInstances(createInstanceInfo);
	}

	@Transactional
	public LinkedHashMap<Integer, String> getZonesForRegeion(
			PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) {
		LinkedHashMap<Integer, String> zoneDetails = new LinkedHashMap<>();
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		ClusterMasterDAO dao = factory.getClusterMasterDAO();
		ClusterMasterDO cDO = new ClusterMasterDO();
		cDO.setDataCenterDetailsDO(dao.get(DataCenterDetailsDO.class,
				prerequisiteInfoInstanceBO.getDatacenterID()));
		try {
			List<ClusterMasterDO> zonesForRegions = dao
					.fetchByDataCenterId(cDO);
			if (zoneDetails != null) {
				for (ClusterMasterDO clusterDO : zonesForRegions) {
					if (clusterDO.isActive()){
						zoneDetails.put(clusterDO.getClusterID(),
								clusterDO.getClusterName());
					}
				}
			}

		} catch (DALException e) {
			logger.error("Error while getting Zone Details for Region {}",
					prerequisiteInfoInstanceBO.getDatacenterName(), e);
		}
		return zoneDetails;
	}

	public List<InstanceGroupBO> fetchGroups(
			PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) throws ICException {
		List<InstanceGroupBO> groupsList = null;
		InstanceGroupBO instanceGroupBO = new InstanceGroupBO();
		logger.debug("Value for the input fields {} - {}",
				prerequisiteInfoInstanceBO.getUserID(),
				prerequisiteInfoInstanceBO.getRoleID());
		try {
			AppGroupsDomainImpl appGroupsDomainImpl = new AppGroupsDomainImpl();
			instanceGroupBO.setCloudId(prerequisiteInfoInstanceBO.getCloudId());
			instanceGroupBO.setRoleID(prerequisiteInfoInstanceBO.getRoleID());
			instanceGroupBO.setUserID(prerequisiteInfoInstanceBO.getUserID());
			instanceGroupBO.setActiveUserGroupID(prerequisiteInfoInstanceBO
					.getActiveUserGroupID());
			instanceGroupBO.setActive(true);
			// List doesn't include ALL & UNCLASSIFIED
			groupsList = appGroupsDomainImpl
					.listMappedAppGroups(instanceGroupBO);
		} catch (ICException e) {
			logger.error(
					"Error while getting Instance Group ;Error is: {}",
					e.getMessage(), e);
			throw new ICException(ErrorConstants.CO_IC_ERR_0093.getCode(), e);
		}
		return groupsList;
	}

	@Transactional
	public List<String> fetchNickNames(
			int cloudId, int userId, int providerID) {
		if (cloudId == 0 || userId == 0) {
			return new ArrayList<>();
		}
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		InstanceDetailsDAO instanceDetailsDAO = factory.getInstanceDetailsDAO();
		CloudMasterDO cloudMasterDO = new CloudMasterDO();
		cloudMasterDO = instanceDetailsDAO.get(CloudMasterDO.class, cloudId);
		ProviderMasterDO providerMasterDO = new ProviderMasterDO();
		providerMasterDO = instanceDetailsDAO.get(ProviderMasterDO.class, providerID);
		InstanceDetailsDO instanceDetailsDO = new InstanceDetailsDO();
		instanceDetailsDO.setCloudMasterDO(cloudMasterDO);
		instanceDetailsDO.setProviderMasterDO(providerMasterDO);
		List<InstanceDetailsDO> instancedetailsList = null;
		List<String> nickNameList = new ArrayList<>();
		try {
			instancedetailsList = instanceDetailsDAO
					.fetchByCloudIdProviderId(instanceDetailsDO);
			if (instancedetailsList != null) {
				for (InstanceDetailsDO theInstanceDetailsDO : instancedetailsList) {
					nickNameList.add(theInstanceDetailsDO.getNickName());
				}
			}
		} catch (DALException e) {
			logger.error(e);
		}
		return nickNameList;
	}

	@Transactional
	public List<String> fetchNickNamesFromWQ(int cloudId, int providerID) {
		if (cloudId == 0) {
			return new ArrayList<>();
		}
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		TaskDetailsDAO taskDetailsDAO = factory.getTaskDetailsDAO();
		CloudMasterDO cloudMasterDO = taskDetailsDAO.get(CloudMasterDO.class,
				cloudId);
		ProviderMasterDO providerMasterDO = taskDetailsDAO.get(
				ProviderMasterDO.class, providerID);
		TaskDetailsDO taskDetailsDO = new TaskDetailsDO();
		taskDetailsDO.setCloudMasterDO(cloudMasterDO);
		taskDetailsDO.setProviderMasterDO(providerMasterDO);
		taskDetailsDO.setTaskDescriptionId(TaskDescriptionEnum.CREATE_INSTANCE.getId());
		List<TaskDetailsDO> taskdetailsList = null;
		List<String> nickNameList = new ArrayList<>();
		try {
			taskdetailsList = taskDetailsDAO
					.fetchByCloudIdProviderIdTaskDescriptionId(taskDetailsDO);
			boolean flage = false;
			if (taskdetailsList != null) {
				for (TaskDetailsDO tasDetailsDO : taskdetailsList) {
					flage = false;
					for (TaskStatusDO taskStatusDO : tasDetailsDO
							.getTaskStatusDO()) {
						if (taskStatusDO.getStatus().equalsIgnoreCase(
								MessageStringConstants.FAILED)) {
							flage = true;
						}
					}
					if (!flage) {
						nickNameList.add(tasDetailsDO.getTarget());
					}
				}
			}
		}catch (DALException e) {
			logger.error("Error in accessing data from WQ for duplicate check : {}",e.getMessage());
		}
		return nickNameList;
	}

	@Transactional
	public String fetchNickName(int cloudId, String nickName, int providerID, int instanceId) throws ICException {
		if (cloudId == 0 || providerID == 0) {
			return new String();
		}
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		InstanceDetailsDAO instanceDetailsDAO = factory.getInstanceDetailsDAO();
		CloudMasterDO cloudMasterDO = new CloudMasterDO();
		cloudMasterDO = instanceDetailsDAO.get(CloudMasterDO.class, cloudId);
		ProviderMasterDO providerMasterDO = new ProviderMasterDO();
		providerMasterDO = instanceDetailsDAO.get(ProviderMasterDO.class, providerID);
		InstanceDetailsDO instanceDetailsDO = new InstanceDetailsDO();
		instanceDetailsDO.setCloudMasterDO(cloudMasterDO);
		instanceDetailsDO.setNickName(nickName);
		instanceDetailsDO.setProviderMasterDO(providerMasterDO);
		InstanceDetailsDO instancedetails = null;
		String dupNickName = null;
		try {
			try {
				instancedetails = instanceDetailsDAO
						.fetchByCloudIdProviderIdNickName(instanceDetailsDO);
				dupNickName = new String();
			} catch (DALException e) {
				logger.info("No instance with this criteria : fetchByCloudIdProviderIdNickName");
			}
			if (instancedetails != null) {
				if (instancedetails.getInstanceID() == instanceId) {
					dupNickName = null;
				} else {
					dupNickName = instancedetails.getNickName();
				}
			} else {
				dupNickName = null;
			}
		}catch (Exception e) {
			logger.info("No instance with this criteria : fetchByCloudIdProviderIdNickName");
		}
		return dupNickName;
	}

	private List<InstanceDeploymentBO> fetchDeployments(PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) throws ICException {
		List<InstanceDeploymentBO> groupsList = null;
		InstanceDeploymentBO instanceDeployBO = new InstanceDeploymentBO();
		logger.debug("Value for the input fields {} - {}",
				prerequisiteInfoInstanceBO.getCloudId(),
				prerequisiteInfoInstanceBO.getRoleID());
		try {
			instanceDeployBO.setUserID(prerequisiteInfoInstanceBO.getUserID());
			instanceDeployBO.setRoleID(prerequisiteInfoInstanceBO.getRoleID());
			instanceDeployBO
			.setCloudId(prerequisiteInfoInstanceBO.getCloudId());
			instanceDeployBO.setActiveUserGroupID(prerequisiteInfoInstanceBO
					.getActiveUserGroupID());
			DeploymentsDomainImpl deploymentsDomainImpl = new DeploymentsDomainImpl();

			// List doesn't include ALL & UNCLASSIFIED
			groupsList = deploymentsDomainImpl
					.listMappedDeployments(instanceDeployBO);
		} catch (ICException e) {
			logger.error("Error while getting Deployment Master ;Error is: {}",
					e.getMessage(), e);
			throw new ICException(ErrorConstants.CO_IC_ERR_0094.getCode(), e);
		}
		return groupsList;
	}

	/**
	 * This method returns the List of Instances based on CloudId and Monitoring
	 * Template Id;
	 */
	@Transactional
	public List<InstanceBO> getInstanceListByTemplate(
			MonitorTemplateBO monTemplateBO) {
		List<InstanceBO> instanceList = new ArrayList<>();
		InstanceDetailsDAO dao = getFactory().getInstanceDetailsDAO();
		try {
			// get CloudMaster details
			CloudMasterDO cloudMasterDO = dao.get(CloudMasterDO.class,
					monTemplateBO.getCloudId());
			TemplateDetailsDO templateDO = dao.get(TemplateDetailsDO.class,
					monTemplateBO.getTemplateId());
			// create a innstance DO
			InstanceDetailsDO instanceDetailsDO = new InstanceDetailsDO();
			instanceDetailsDO.setCloudMasterDO(cloudMasterDO);
			instanceDetailsDO.setTemplateDetailsDO(templateDO);
			//CO-10951 - Changes - Ensure state is not delete
			instanceDetailsDO.setState(MessageStringConstants.DELETED);
			List<InstanceDetailsDO> doList = dao
					.fetchByCloudIdTemplateIdNotState(instanceDetailsDO);
			//CO-10951 - Changes end
			instanceList = instanceProvider.getBOFromDO(doList);
		} catch (DALException e) {
			logger.error("Exception encountered while getting Instance List by Template Id : {}",
					e.getMessage());
		}
		return instanceList;

	}

	public CreateInstanceInfoBO saveProvisioningDetailsAndSchedule(
			CreateInstanceInfoBO createInstanceInfoBO) throws ICException {
		List<WorkerQueueBO> wqBOs = null;
		List<CreateInstanceInfoBO> createInstanceInfoBOs = new ArrayList<>();
		createInstanceInfoBO.setWqTarget("Create Instance");
		createInstanceInfoBO.setWqTargetType(EntityTypeEnum.INSTANCE.getId());
		createInstanceInfoBO.setParentWQTaskId(GlobalConstant.META_PARENT_TASK_ID);
		createInstanceInfoBOs.add(createInstanceInfoBO);
		RecurrenceBO recurrenceBO = createInstanceInfoBO.getRecurrenceBO();
		CloudOneMessage cloudOneMessage = new CloudOneMessage();
		String command = InstancesProvisioningConstants.PROVISIONING;
		String policyCode = null;
		try {
			wqBOs = WorkerQueueHelper.buildInitialWorkerQueueObjectList(createInstanceInfoBOs,  TaskDescriptionEnum.SCHEDULE_CREATE_VM);
			WorkerQueueManager.updateInitialWorkerQueueStatus(wqBOs);
			InstanceDataBean instanceDataBean = InstanceProvider.getInstanceDataBeanFromCreateInstanceInfoBO(createInstanceInfoBO);
			ScheduleDetailsBean scheduleDetailsBean = buildScheduleBeanFromRecurrenceDetails(recurrenceBO);
			//			List<Integer> includedEntities = new ArrayList<Integer>();
			//			includedEntities.add(instBO.getInstanceId());
			String schedulingInfo = BeanBuilder
					.buildScheduleInfo(scheduleDetailsBean);
			schedulingInfo=addDeprovisionDetails(scheduleDetailsBean,schedulingInfo);
			List<TagsBean> tags = null;
			Map.Entry<String, String> mapEntry = null;
			if (instanceDataBean.getTags() != null && instanceDataBean.getTags().size() > 0 )  {
				tags = new ArrayList<>();
				Iterator<Entry<String, String>> it = instanceDataBean.getTags() .entrySet().iterator();
				while (it.hasNext()) {
					mapEntry = (Map.Entry<String, String>) it.next();
					TagsBean tagBean = new TagsBean();
					tagBean.setTagKeyName(mapEntry.getKey());
					tagBean.setValue(mapEntry.getValue());
					tagBean.setTagSource(TagSourceEnum.CLOUD360.name());
					tagBean.setTagType(TagTypeEnum.USER.getDescription());
					tagBean.setState(ObjectStateEnum.NEW.name());
					tags.add(tagBean);
				}
			}
			JobDataBean jobDataBean = BeanBuilder.buildJobDataBean(
					recurrenceBO.getCloudId(), JobTypeEnum.CREATE_VM,
					recurrenceBO.getProviderID(), recurrenceBO.getProviderType(),
					recurrenceBO.getActiveUserGroupID(),
					null,
					JobTypeEnum.CREATE_VM.name(),
					recurrenceBO.getUserID(), SchedulerEntityTypeEnum.INSTANCE,
					null, null, schedulingInfo, createInstanceInfoBO.getNotificationID(), instanceDataBean, tags,wqBOs.get(0).getCurrentTaskKey());
			JobDetailsBean jobDetailsBean = BeanBuilder
					.buildJobDetailsBean(
							JobTypeEnum.CREATE_VM.name(),
							JobTypeEnum.CREATE_VM.toString(),
							JobTypeEnum.CREATE_VM.getDescription(),
							null,
							"com.cognizant.cloudone.scheduling.jobs.InstancesProvisioningJob",
							null, false, false, SchedulingEnvTypeEnum.LOCAL_MC,
							jobDataBean);
			List<TriggerDetailsBean> triggerDetailsBeans = null;
			if (scheduleDetailsBean.isReccurence()) {
				triggerDetailsBeans = ScheduleBuilder
						.buildRecurrence(jobDetailsBean,
								JobTypeEnum.CREATE_VM.toString(),
								JobTypeEnum.CREATE_VM.toString(), scheduleDetailsBean);
			} else {
				String startDt = scheduleDetailsBean.getRecurStartDate();
				String startTime = scheduleDetailsBean.getRecurStartTime();
				Date startDtTime = TimeZoneConverter.getDate(startDt+" "+startTime, scheduleDetailsBean.getTimeZone(), DateTimeFormatEnum.DTF_19);
				triggerDetailsBeans = new ArrayList<>();
				TriggerDetailsBean triggerDetailsBean = BeanBuilder.buildSimpleTriggerBean(JobTypeEnum.CREATE_VM.toString(),
						JobTypeEnum.CREATE_VM.toString(), null, 0,
						null, startDtTime, 0,
						false, scheduleDetailsBean.getTimeZone());
				triggerDetailsBeans.add(triggerDetailsBean);
			}
			List<Instance> entityList = BusinessPoliciesUtil.getEntityList(createInstanceInfoBO, wqBOs);
			ProvisioningPolicyResult policyResult = BusinessPoliciesUtil.executeInstanceProvisioningPolicies(createInstanceInfoBO, entityList);
			Map<String, String> policyResultMap = policyResult.getPolicyResult();
			if(null != policyResultMap && !policyResultMap.isEmpty() && policyResultMap.size() > 0){
				if(null != policyResultMap.get(PolicyConstants.STATUS_CODE)){
					policyCode = policyResultMap.get(PolicyConstants.STATUS_CODE);
				}
			}	
			if(!PolicyConstants.APPROVAL_ACTION.equals(policyCode)){
				ISchedulerService schedulerService = new SchedulerServiceImpl();
				schedulerService.scheduleJob(jobDetailsBean, triggerDetailsBeans, recurrenceBO.getCloudId());
			}
			createInstanceInfoBO.setScheduled(MessageStringConstants.SUCCESS);
			createInstanceInfoBO.setJobId(jobDataBean.getJobId());
			createInstanceInfoBO.setWqTaskID(wqBOs.get(0).getCurrentTaskKey());
			cloudOneMessage
			.setMessageCode(MessageConstants.CO_IC_SCHDJOB_MSG_001
					.getCode());
			cloudOneMessage
			.setMessageType(MessageStringConstants.MSGTYPE_SUCCESS);
			cloudOneMessage
			.setMessageDesc(MessageConstants.CO_IC_SCHDJOB_MSG_001
					.getDescription());
			CloudOneMessages cloudOneMessages = new CloudOneMessages();
			cloudOneMessages.addMessage(cloudOneMessage);
			createInstanceInfoBO.setMessages(cloudOneMessages);
		} catch (ICException | SchedulerException | ParseException | PolicyException | ServiceException e) {
			populateErrorMessages(createInstanceInfoBOs, e);
			createInstanceInfoBO.setScheduled(MessageStringConstants.FAILURE);
			throw new ICException("Exception while saving "
					+ command.toLowerCase() + " details.");
		} finally {
			if(!PolicyConstants.APPROVAL_ACTION.equals(policyCode)){
				wqBOs = WorkerQueueHelper.buildFinalWorkerQueueObjectList(ICUtils.setWQTaskID(createInstanceInfoBOs, wqBOs),  TaskDescriptionEnum.SCHEDULE_CREATE_VM);
				WorkerQueueManager.updateFinalWorkerQueueStatus(wqBOs);
			}
		}
		return createInstanceInfoBO;
	}

	@Transactional
	public List<InstanceBO> updateInstanceLoginDetails(
			List<InstanceBO> instanceBOs) throws ICException {
		InstanceDetailsDAO instanceDetailsDAO = getFactory()
				.getInstanceDetailsDAO();
		List<InstanceBO> instanceList = new ArrayList<>();
		List<WorkerQueueBO> workerQueueObjects = null;
		List<InstanceDetailsDO> instDOs = new ArrayList<>();
		try {
			for (InstanceBO instanceBO : instanceBOs) {
				// Fetch the InstanceDetailsDO object from DB using the
				// InstanceId				
				InstanceDetailsDO instDO = instanceDetailsDAO.get(
						InstanceDetailsDO.class, instanceBO.getInstanceId());
				/*
				 * Adding Null Check to updated the details accordingly as we
				 * will update only one detail at a time Added check for
				 * Password as we will not update the password if its null
				 */
				instDO.setOperation(instanceBO.getOperation());
				
				if (instDO != null) {
					if (instanceBO.getInstanceUserName() != null) {
						instDO.setInstanceUserName(instanceBO
								.getInstanceUserName());
					}
					if (instanceBO.getKeyPairFile() != null) {
						instDO.setKeyPairFilePath(instanceBO.getKeyPairFile());
					}
					/*
					 *  KeyPairFilePath will be updated as null if user tried to clear the Key Pair 
					 */
					else{
						instDO.setKeyPairFilePath(null);
					}
					if(null != instanceBO.getPassphrase()){
						instDO.setPassphrase(instanceBO.getPassphrase());
					}
					/*
					 *  PassPhrase will be updated as null if user tried to clear the PassPhrase 
					 */
					else{
						instDO.setPassphrase(null);
					} 
					if (null != instanceBO.getInstancePassword()) {
						instDO.setInstancePassword(instanceBO
								.getInstancePassword());
					}
					/*
					 *  Password will be updated as null if user tried to clear the password 
					 */
					else{
						instDO.setInstancePassword(null);
					} 					
					//CO-11303: Using concatenated name for WQ to avoid confusion with same name VMs
					instanceBO.setWqTarget(StringUtils
							.getDisplayInstanceAndNickName(
									instDO.getInstanceName(),
									instDO.getNickName()));
					instanceBO.setWqTargetId(instanceBO.getInstanceId());
					instanceBO.setWqTargetType(EntityTypeEnum.INSTANCE.getId());
					instDO.setUpdatedByUser(instanceBO.getUpdatedByUser());
					instDOs.add(instDO);
				}			 
			}

			// Adding Initial Worker Queue Entries
			workerQueueObjects = WorkerQueueHelper
					.buildInitialWorkerQueueObjectList(instanceBOs,
							TaskDescriptionEnum.UPDATE_INS_LOGIN_DETAILS);
			workerQueueObjects = WorkerQueueManager
					.updateInitialWorkerQueueStatus(workerQueueObjects);

			
				List<InstanceDetailsDO> instanceDOList = instanceDetailsDAO
						.updatetBatch(instDOs);
				instanceList = instanceProvider.getBOFromDO(instanceDOList);
				//CO-12459- Changes start here
				IServiceConsoleDomain serviceConsoleDomain = (IServiceConsoleDomain) DomainFactory.getInstance(ICObjectEnum.SERVICECONSOLE);
				serviceConsoleDomain.updateVirtualMachines(instanceList,false);
				//CO-12459- Changes end here
			
		} catch (DALException e) {
			CloudOneErrors errors = new CloudOneErrors();
			CloudOneError error = new CloudOneError();
			if (ErrorConstants.CO_DAL_ERR_004.getCode().equals(e.getMessage())) {
				error.setErrorMessage(ErrorConstants.CO_DAL_ERR_004
						.getDescription());
				error.setErrorCode(ErrorConstants.CO_DAL_ERR_004.getCode());
			} else {
				error.setErrorMessage(e.getMessage());
				error.setErrorCode(ErrorConstants.CO_DAL_ERR_GENERIC.getCode());
			}
			errors.addError(error);
			if (!instanceList.isEmpty()) {
				instanceList.get(0).setErrors(errors);
			}
			throw new ICException(e);
		} finally {
			workerQueueObjects = WorkerQueueHelper
					.buildFinalWorkerQueueObjectList(ICUtils.setWQTaskID(
							instanceBOs, workerQueueObjects),
							TaskDescriptionEnum.UPDATE_INS_LOGIN_DETAILS);
			WorkerQueueManager.updateFinalWorkerQueueStatus(workerQueueObjects);
		}
		return instanceList;
	}

	public List<InstanceBO> updateInstanceDetails(List<InstanceBO> instBOs)
			throws ICException {
		List<InstanceBO> returnBOs = new ArrayList<>();
		InstanceBO returnBO = null;
		for (InstanceBO instanceBO : instBOs) {
			try {
				returnBO = saveInstanceDetails(instanceBO);
				if (ProviderTypeEnum.AMAZON == instanceBO.getProviderType()
						&& (instanceBO.getNewNickName() != null && !instanceBO.getNewNickName().trim().isEmpty())) {
					returnBO.setUpdatedByUser(instanceBO.getUpdatedByUser());
					InstanceHelper instanceHelper = new InstanceHelper();
					instanceHelper.updateTags(returnBO, TagsConstants.TAGKEY_NAME, returnBO.getNickName());
				}
				returnBOs.add(returnBO);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return returnBOs;

	}

	@Transactional
	public InstanceBO saveInstanceDetails(InstanceBO instanceBO)
			throws ICException, DALException {
		InstanceBO returnBO = null;
		InstanceDetailsDAO instDAO = getFactory().getInstanceDetailsDAO();
		List<WorkerQueueBO> workerQueueObjects = null;
		List<InstanceBO> instanceBOs = new ArrayList<>();
		try {
			InstanceDetailsDO instDO = instDAO.get(InstanceDetailsDO.class,
					instanceBO.getInstanceId());
			// Added for JIRA:CO-5810
			// Modified for JIRA:CO-8965
			if (instanceBO.getNewNickName() != null
					&& !instanceBO.getNewNickName().trim().isEmpty()) {
				instanceBO.setWqTarget(StringUtils
						.getDisplayInstanceAndNickName(
								instanceBO.getInstanceName(),
								instanceBO.getNewNickName()));
			} else {
				// CO-11303: Using concatenated name for WQ to avoid
				// confusion with same name VMs
				instanceBO.setWqTarget(StringUtils
						.getDisplayInstanceAndNickName(
								instanceBO.getInstanceName(),
								instanceBO.getNickName()));
			}
			instanceBO.setWqTargetId(instanceBO.getInstanceId());
			instanceBO.setWqTargetType(EntityTypeEnum.INSTANCE.getId());
			if (instanceBO.getNewNickName() != null
					&& !instanceBO.getNewNickName().isEmpty()) {
				instDO.setNickName(instanceBO.getNewNickName());
			}
			if (instanceBO.getNewDeploymentId() != 0) {
				DeploymentMasterDO deploymentMasterDO = instDAO.get(
						DeploymentMasterDO.class,
						instanceBO.getNewDeploymentId());
				instDO.setDeploymentMasterDO(deploymentMasterDO);
				String deploymentName = deploymentMasterDO.getDeploymentName();
				instanceBO.setWqTargetDeployment(deploymentName);

			}
			if (instanceBO.getNewInstGroupId() != 0) {
				InstanceGroupMasterDO instanceGroupMasterDO = instDAO.get(
						InstanceGroupMasterDO.class,
						instanceBO.getNewInstGroupId());
				instDO.setInstanceGroupMasterDO(instanceGroupMasterDO);
				String groupName = instanceGroupMasterDO.getGroupName();
				instanceBO.setWqTargetGroup(groupName);
			}
			instanceBOs.add(instanceBO);

			workerQueueObjects = WorkerQueueHelper
					.buildInitialWorkerQueueObjectList(instanceBOs,
							TaskDescriptionEnum.UPDATE_INSTANCE_DETAILS);
			workerQueueObjects = WorkerQueueManager
					.updateInitialWorkerQueueStatus(workerQueueObjects);
			instDO.setUpdatedByUser(instanceBO.getUpdatedByUser());
			instDO.setOperation(instanceBO.getOperation());
			instDAO.merge(instDO);
			returnBO = instanceProvider.getBOFromDO(instDO);
			returnBO.setParentWQTaskId(instanceBO.getParentWQTaskId());

		} catch(Exception e) {

		} finally {
			workerQueueObjects = WorkerQueueHelper
					.buildFinalWorkerQueueObjectList(ICUtils.setWQTaskID(
							instanceBOs, workerQueueObjects),
							TaskDescriptionEnum.UPDATE_INSTANCE_DETAILS);
			WorkerQueueManager.updateFinalWorkerQueueStatus(workerQueueObjects);
		}
		return returnBO;
	}

	/*
	 * Query the Instances with expiration date = todayTerminate the expired
	 * instances
	 */
	@Transactional
	public List<InstanceBO> terminateExpiredInstances(InstanceBO instanceBO)
			throws ICException {

		List<InstanceBO> instanceList = null;
		List<InstanceDetailsDO> instanceListDO = null;
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();

		InstanceDetailsDAO instanceDetailsDAO = factory.getInstanceDetailsDAO();
		try {
			InstanceDetailsDO instanceDetailsDO = InstanceProvider
					.getDOFromBO(instanceBO);
			instanceDetailsDO.setProviderMasterDO(instanceDetailsDAO.get(ProviderMasterDO.class, instanceBO.getProviderID()));
			instanceDetailsDO.setCloudMasterDO(instanceDetailsDAO.get(CloudMasterDO.class, instanceBO.getCloudId()));
			instanceListDO = instanceDetailsDAO
					.fetchByCloudIdExpirationDate(instanceDetailsDO);
		} catch (DALException e) {
			CloudOneErrors errors = new CloudOneErrors();
			CloudOneError error = new CloudOneError();
			error.setErrorMessage(e.getMessage());
			errors.addError(error);
			instanceBO.setErrors(errors);
			logger.error("Error in fetching the details from InstanceDetails Using ExpirationDate {}",e.getMessage());
			throw new ICException(e);
		} catch (ParseException e) {
			CloudOneErrors errors = new CloudOneErrors();
			CloudOneError error = new CloudOneError();
			error.setErrorMessage(e.getMessage());
			errors.addError(error);
			instanceBO.setErrors(errors);
			logger.error("Unable to fetch the details from InstanceDetails Using ExpirationDate {}",e.getMessage());
			throw new ICException(e);
		}

		if (instanceListDO != null) {
			instanceList = instanceProvider.getBOFromDO(instanceListDO);
		}
		/* Triggering the terminate request for those expired instances */
		if (instanceList != null) {
			for (int i = 0; i < instanceList.size(); i++) {
				instanceList.get(i).setOperation(
						InstancesActionsEnum.DELETE.getValue());
				instanceList.get(i).setProviderType(ProviderTypeEnum.VMWARE);
			}

			if (instanceList.size() > 0) {
				instanceList = runOperation(instanceList);
			}
		}
		return instanceList;
	}

	/**
	 * getAllHostedServices
	 *
	 * @param cloudDetails
	 * @return list of Hosted Services -Azure
	 * @throws ICException
	 */
	public List<ServiceBO> getAllHostedServices(CloudDetailsBO cloudDetails)
			throws ICException {

		List<ServiceBO> hostedServiceList = null;
		try {
			IPlatformsDomain domain = new PlatformsDomainImpl();
			PlatformBO platformBO = domain.fetchProvider(PlatformsProvider.getInputPlatformBO(cloudDetails));
			CloudCredentialsBO providerCredentials = PlatformsProvider.getProviderCredentials(platformBO);

			SLInstancesFacade slfacade = SLInstancesFacade.SINGLETON_INSTANCE;

			hostedServiceList = slfacade.getAllHostedServices(cloudDetails,
					providerCredentials);
		} catch (SLException e) {
			throw new ICException(e);
		}
		return hostedServiceList;
	}

	/**
	 * This method checks if there is a refresh task already in progress. If
	 * yes, then abort refresh with an error message in WQ. If no, proceed with
	 * refresh. This is applicable for both UI and background job executions.
	 */
	public List<InstanceBO> refreshInventory(InventoryBO inventoryBO,
			RefreshTypeEnum refreshType) throws ICException {
		logger.info("Start refreshing instances at : {}",
				DateUtils.getCurrentDate());
		// This switch here sets appropriate enum based on the execution action
		// origin.
		TaskDescriptionEnum currentTask = TaskDescriptionEnum.REFRESH_INSTANCES;
		switch (refreshType) {
		case INSTANCE:
			currentTask = TaskDescriptionEnum.REFRESH_INSTANCES;
			break;
		case STORAGES:
			currentTask = TaskDescriptionEnum.REFRESH_STORAGES;
			break;
		case EBSVOLUMES:
			currentTask = TaskDescriptionEnum.REFRESH_STORAGES;
			inventoryBO.setProviderCheck(true);
			break;
		case CLOUDSERVICE:
			currentTask = TaskDescriptionEnum.REFRESH_CLOUD_SERVICES;
			break;
		case SECURITYGROUP:
			currentTask = TaskDescriptionEnum.REFRESH_SECURITY_GROUPS;
			inventoryBO.setProviderCheck(true);
			break;

		default:
			break;
		}
		/*
		 * FIXME: There is a possible race condition here. If 2 threads start in
		 * parallel then before 1st thread makes an entry in WQ tables, 2nd
		 * thread might not see the task from isRefreshTaskExist() call. Now,
		 * both threads will proceed for refresh since both threads saw that
		 * there are no refresh tasks in progress.
		 */
		List<InstanceBO> instBOs = null;
		String[] taskInProgress = checkTaskInProgress(inventoryBO, refreshType,
				currentTask);
		boolean proceed = true;
		if (taskInProgress[0] != null && !taskInProgress[0].isEmpty() && !taskInProgress[0].equalsIgnoreCase(TaskDescriptionEnum.UNKNOWN.getDescription())) {
			if (!inventoryBO.isAsynchronous()) {
				String errMsg=null;
				InstanceBO instBO = new InstanceBO();
				logger.info(" {} refreshed failed at : {} ",
						refreshType.getValue(), DateUtils.getCurrentDate());
				
				if(inventoryBO.getProviderType()!=ProviderTypeEnum.VMWARE){
					 errMsg = (currentTask == TaskDescriptionEnum.REFRESH_STORAGES) ? ErrorConstants.CO_IC_ERR_0117
                        .getDescription().replace("refresh job", 	 
                                        taskInProgress[1]) 	 
                        + GlobalConstant.BLANK_SPACE_STRING + taskInProgress[0] 	 
                        : currentTask.getDescription() + "," 	 
                                         + taskInProgress[0];
				}
				else{
					errMsg = currentTask.getDescription() + ","
							+ taskInProgress[0];
				}

				instBO.setErrors(buildErrors(
						ErrorConstants.CO_IC_ERR_0117.getCode(), errMsg, null));
				logger.info("after error set");
				instBOs = new ArrayList<>();
				instBOs.add(instBO);
			} else {
				updateWQForInProgressRefreshTask(inventoryBO, currentTask,
						refreshType, taskInProgress[1], taskInProgress[0],
						EntityTypeEnum.INSTANCE);
			}
			proceed = false;


		}
		if (proceed) {
			IPlatformsDomain domain = new PlatformsDomainImpl();
			PlatformBO platformBO = domain.fetchProvider(PlatformsProvider
					.getInputPlatformBO(inventoryBO));
			if (platformBO != null) {
				inventoryBO = processRefresh(platformBO, inventoryBO,
						currentTask, refreshType);
			} else {
				throw new ICException(
						ErrorConstants.CO_IC_ERR_0110.getDescription());
			}
			if (inventoryBO != null && inventoryBO.getErrors() != null
					&& inventoryBO.getErrors().hasErrors()) {
				instBOs = new ArrayList<>();
				InstanceBO instanceBO = new InstanceBO();
				instanceBO.setErrors(inventoryBO.getErrors());
				instBOs.add(instanceBO);
			}else if(refreshType.equals(RefreshTypeEnum.SECURITYGROUP) && inventoryBO.getProviderType().equals(ProviderTypeEnum.AZURE)){
				if (inventoryBO.getDataCenters() != null) {
					instBOs = new ArrayList<>();
					for (DataCenterBO dcBO : inventoryBO.getDataCenters()) {
						if (dcBO.getInstances() != null) {
							instBOs.addAll(dcBO.getInstances());
						}

					}
				}
			}
		}
		return instBOs;
	}

	public InventoryBO processRefresh(PlatformBO platformBO,
			InventoryBO inventoryBO, TaskDescriptionEnum currentTask,
			RefreshTypeEnum refreshType) throws ICException {
		logger.debug("Refresh for provider {}" , platformBO.getProviderType());
		String propValue = ICUtils
				.getDefaultValue(ConfigParamsEnum.SHOW_WQ_FOR_BG_TASKS
						.getPropKey());
		List<WorkerQueueBO> wqBOs = null;
		List<InventoryBO> inventoryBOs = null;
		if(platformBO.getProviderType()  == ProviderTypeEnum.OPENSTACK){
			IPlatformsDomain platformsDomain = (IPlatformsDomain) DomainFactory.getInstance(ICObjectEnum.PLATFORMS);
			List<DataCenterBO> dataCenterBOs = new ArrayList<>();
			dataCenterBOs = platformsDomain.fetchDataCenters(platformBO.getCloudId(), platformBO.getProviderID());
			inventoryBO.setDataCenters(dataCenterBOs);
		}


		// By default generate the WQ entries. By-pass for background jobs when
		// configured as 'false'.
		// IsAsynchronous flag considered as true for UI force refresh.
		// Do not generate WQ entries when refresh instances is a part
		// EBSVOLUMES refresh.
		if (checkWQCondition(propValue, inventoryBO)
				&& refreshType != RefreshTypeEnum.EBSVOLUMES && refreshType != RefreshTypeEnum.SECURITYGROUP) {
			inventoryBO.setWqTarget(platformBO.getProviderName());
			inventoryBO.setWqTargetType(EntityTypeEnum.INSTANCE.getId());
			inventoryBOs = new ArrayList<>();
			inventoryBOs.add(inventoryBO);
			wqBOs = WorkerQueueHelper.buildInitialWorkerQueueObjectList(
					inventoryBOs, currentTask);
			WorkerQueueManager.updateInitialWorkerQueueStatus(wqBOs);
			inventoryBO.setWqTaskID(wqBOs.get(0).getCurrentTaskKey());
		}
		IRefreshInventoryDomain domainObj = RefreshInventoryFactory
				.getInstance(platformBO.getProviderType());
		try {
			List<VpcBO> vpcBOs = null;
			if (platformBO.getProviderType() == ProviderTypeEnum.AMAZON) {
				IPlatformsDomain platformsDomain = (IPlatformsDomain) DomainFactory
						.getInstance(ICObjectEnum.PLATFORMS);
				vpcBOs = platformsDomain.getConfiguredVPCList(platformBO
						.getProviderID());
			}
			return domainObj.refreshInventory(
					setInventoryBO(inventoryBO, platformBO, vpcBOs),
					PlatformsProvider.getProviderCredentials(platformBO));
		} catch (Exception e) {
			logger.error("Exception in processRefresh {}" , e);
			if (inventoryBOs == null) {
				inventoryBOs = new ArrayList<>();
				inventoryBOs.add(inventoryBO);
			}
			boolean isOptimisticLockingException = false;
			try {
				reportRefreshSpecificError(e);
			} catch (DALException e1) {
				inventoryBOs = populateErrorMessages(inventoryBOs, e1);
				isOptimisticLockingException = true;
			}
			if(!isOptimisticLockingException) {
				inventoryBOs = populateErrorMessages(inventoryBOs, e);
			}
		} finally {
			if (checkWQCondition(propValue, inventoryBO)
					&& refreshType != RefreshTypeEnum.EBSVOLUMES && refreshType != RefreshTypeEnum.SECURITYGROUP) {
				wqBOs = WorkerQueueHelper.buildFinalWorkerQueueObjectList(
						ICUtils.setWQTaskID(inventoryBOs, wqBOs), currentTask);
				WorkerQueueManager.updateFinalWorkerQueueStatus(wqBOs);
				String key = inventoryBO.getCloudId()
						+ GlobalConstant.TAGS_SEPARATOR + currentTask.getId();
				ICache<String, CloudOneCacheDataWrapper> cache = CloudOneCache.CACHEINSTANCE;
				cache.removeFromCache(key);
			} else if (refreshType.equals(RefreshTypeEnum.EBSVOLUMES) || refreshType.equals(RefreshTypeEnum.SECURITYGROUP)) {
				String key = inventoryBO.getCloudId()
						+ GlobalConstant.TAGS_SEPARATOR
						+ TaskDescriptionEnum.REFRESH_INSTANCES.getId();
				ICache<String, CloudOneCacheDataWrapper> cache = CloudOneCache.CACHEINSTANCE;
				cache.removeFromCache(key);
			}
		}
		return inventoryBOs.get(0);
	}


	private boolean checkWQCondition(String propValue, InventoryBO inventoryBO) {
		if (propValue == null || propValue.isEmpty()
				|| !inventoryBO.isAsynchronous()
				|| !propValue.equalsIgnoreCase(Boolean.FALSE.toString())) {
			return true;
		} else {
			return false;
		}
	}

	private InventoryBO setInventoryBO(InventoryBO invBO, PlatformBO platformBO, List<VpcBO> vpcBOs) {
		InventoryBO inventoryBO = new InventoryBO();
		inventoryBO.setProviderID(platformBO.getProviderID());
		inventoryBO.setUuid(platformBO.getVCenterUUID());
		inventoryBO.setProviderType(platformBO.getProviderType());
		inventoryBO.setCloudId(invBO.getCloudId());
		inventoryBO.setUserID(invBO.getUserID());
		inventoryBO.setWqTaskID(invBO.getWqTaskID());
		inventoryBO.setActiveUserGroupID(invBO.getActiveUserGroupID());
		inventoryBO.setServerName(platformBO.getVCenterUUID());//Used for SCVMM
		inventoryBO.setDataCenters(invBO.getDataCenters()); //Used for Openstack
		if (vpcBOs != null && !vpcBOs.isEmpty()) {
			Set<String> vpcIDs = new HashSet<>();
			for (VpcBO vpcBO : vpcBOs) {
				vpcIDs.add(vpcBO.getVpcId().trim());
			}
			inventoryBO.setVpcIDs(vpcIDs);
		}
		inventoryBO.setPlatformBO(platformBO);
		return inventoryBO;
	}

	/***********************************************************************************************************************
	 * Function to retrieve the information for the unacknowledge request for
	 * the web service received by the customer VIZ - NSS or RAMS
	 *
	 * @param WSAuditBO
	 *            -> WSAuditBO with CloudID & Role Id
	 * @return List<InstanceBO> List of Instance BO
	 * @throws ICException
	 ***********************************************************************************************************************/
	@SuppressWarnings("unchecked")
	@Transactional
	public List<WSAuditBO> processWSAudit(WSAuditBO wsAudit) throws ICException {
		List<WSAuditDetailsDO> auditDetailsDOList = null;
		List<WSAuditBO> auditBOList = new ArrayList<>();
		WSAuditDetailsDAO auditDetailsDAO = getFactory().getWSAuditDetailsDAO();
		WSAuditDetailsDO wSAuditDetailsDO = new WSAuditDetailsDO();
		InstanceBO instanceBO;
		CloudMasterDO cloudMasterDO = auditDetailsDAO.get(CloudMasterDO.class,
				wsAudit.getCloudId());
		CloudUserCredentialsDO cloudUserCredentialsDO = auditDetailsDAO.get(
				CloudUserCredentialsDO.class, wsAudit.getUserID());
		wSAuditDetailsDO.setCloudUserCredentialsDO(cloudUserCredentialsDO);
		wSAuditDetailsDO.setCloudMasterDO(cloudMasterDO);
		try {
			auditDetailsDOList = auditDetailsDAO
					.fetchByCreateDateUpdate(
							wSAuditDetailsDO,
							Integer.parseInt(MessageStringConstants.ACK_ID_CHK_INTERVAL));// /
							// To
			// be
			// changed
			// according
			// to
			// requirement
		} catch (DALException e) {
			populateErrorMessages(auditBOList, e);
			logger.error("Unable to fetch the entries from TASK DETAILS & TASK STATUS");
			throw new ICException(e.getMessage());
		} catch (ParseException e) {
			logger.error("Unable to fetch from WSAuditDetails within prescribed time limit");
			throw new ICException(e.getMessage());
		}
		if (auditDetailsDOList != null) {
			logger.info("Number of Audit records to be processed {}",
					auditDetailsDOList.size());
			for (int i = 0; i < auditDetailsDOList.size(); i++) {
				List<TaskStatusDO> taskStatusList = null;
				WSAuditDetailsDO auditDetailsDO = auditDetailsDOList.get(i);
				if ((auditDetailsDO != null)
						&& (auditDetailsDO.getTaskID() > 0)) {
					logger.info("Task Id being processed now {}",
							auditDetailsDO.getTaskID());
					WSAuditBO wsAuditBO = InstanceProvider
							.getWSAuditBOfromDO(auditDetailsDO);
					TaskStatusDAO taskStatusDAO = getFactory()
							.getTaskStatusDAO();
					TaskStatusDO taskStatusDO = new TaskStatusDO();
					TaskDetailsDO taskDetailsDO = taskStatusDAO.get(
							TaskDetailsDO.class, auditDetailsDO.getTaskID());
					taskStatusDO.setTaskDetailsDO(taskDetailsDO);
					try {
						taskStatusList = taskStatusDAO
								.fetchByTaskId(taskStatusDO);
					} catch (DALException e) {
						logger.error("Unable to fetch the entries from TASK DETAILS & TASK STATUS");
						populateErrorMessages(auditBOList, e);
						throw new ICException(e.getMessage());
					}
					/*
					 * Completed tasks (Success or failed) will have three
					 * entries against the same task id inside Task Status ---
					 * Pending,InProgress,Completed/Failed.
					 */
					if (taskStatusList.size() >= 2) {
						logger.info("Task Id has 3 status {}",
								auditDetailsDO.getTaskID());
						for (int j = 0; j < taskStatusList.size(); j++) {
							TaskStatusDO statusDO = taskStatusList.get(j);
							if (statusDO.getStatus().equalsIgnoreCase(
									MessageStringConstants.COMPELTE)) {
								logger.info("Task Id has 3 status {}",
										auditDetailsDO.getTaskID());
								try {
									InstanceDetailsDAO instanceDetailsDAO = getFactory()
											.getInstanceDetailsDAO();
									List<InstanceBO> instanceList = new ArrayList<>();
									InstanceDetailsDO instanceDetailsDO = instanceDetailsDAO
											.get(InstanceDetailsDO.class,
													auditDetailsDO
													.getInstanceId());
									if (instanceDetailsDO != null
											&& instanceDetailsDO.getIpAddress() != null) {
										instanceBO = InstanceProvider
												.getBOFromDO(instanceDetailsDO);
										if (instanceBO != null) {
											logger.info("Instance details for the Instance {}",
													instanceBO
													.getInstanceName());
											instanceList.add(instanceBO);
											wsAuditBO
											.setResponseObject(instanceList);
										}
										wsAuditBO
										.setComments(MessageStringConstants.SUCCESS);
										logger.info("Set the status for the WS Audit as Success - {}",
												wsAuditBO.getWsAuditID());
										auditBOList.add(wsAuditBO);
									} else {
										logger.info("Ignoring the instance due to either entire null object or IP not updated yet...");
									}
								}catch (Exception e) {
									populateErrorMessages(auditBOList, e);
									logger.error("Unable to Update an entry into WSAuditDetails");
									throw new ICException(e.getMessage());
								}
							} else if (statusDO.getStatus().equalsIgnoreCase(
									MessageStringConstants.FAILED)) {
								wsAuditBO.setComments(statusDO.getComments());
								logger.info("Set the status for the WS Audit as Failure - {}",
										wsAuditBO.getWsAuditID());
								auditBOList.add(wsAuditBO);
							} else {
								/*
								 * PENDING & INPROGRESS ENTRIES WILL IGNORED
								 * OVER HEREASSUMING EXECUTION OF THOSE TASKS
								 * ARE NOT YET COMPLETED/FAILED
								 */
							}
						}
					}
				}
			}
		} // End braces for the if Null condition
		return auditBOList;
	}

	/***********************************************************************************************************************
	 * Method to update the WSAUDIT table after sending the successful
	 * acknowledgment to client system.
	 *
	 * @param wsAuditBO
	 * @return WSAuditBO
	 * @throws ICException
	 **********************************************************************************************************************/
	@Transactional
	public WSAuditBO WSAuditAckUpdate(WSAuditBO wsAuditBO) throws ICException {
		WSAuditDetailsDAO auditDetailsDAO = getFactory().getWSAuditDetailsDAO();
		WSAuditDetailsDO auditDetailsDO;
		try {
			auditDetailsDO = InstanceProvider.getWSAuditDOfromBO(wsAuditBO);
		} catch (DALException e1) {
			CloudOneErrors errors = new CloudOneErrors();
			CloudOneError error = new CloudOneError();
			if (ErrorConstants.CO_DAL_ERR_082.getCode().equals(e1.getMessage())) {
				error.setErrorMessage(ErrorConstants.CO_DAL_ERR_082
						.getDescription());
				error.setErrorCode(ErrorConstants.CO_DAL_ERR_082.getCode());
			} else {
				error.setErrorMessage(e1.getMessage());
				error.setErrorCode(ErrorConstants.CO_DAL_ERR_GENERIC.getCode());
			}
			wsAuditBO.setErrors(errors);
		}

		auditDetailsDO = auditDetailsDAO.get(WSAuditDetailsDO.class,
				wsAuditBO.getWsAuditID());
		auditDetailsDO.setAckStatus(wsAuditBO.isAuditAckStatus());

		try {
			auditDetailsDO = auditDetailsDAO.update(auditDetailsDO);
		} catch (DALException e) {
			CloudOneErrors errors = new CloudOneErrors();
			CloudOneError error = new CloudOneError();
			if (ErrorConstants.CO_DAL_ERR_082.getCode().equals(e.getMessage())) {
				error.setErrorMessage(ErrorConstants.CO_DAL_ERR_082
						.getDescription());
				error.setErrorCode(ErrorConstants.CO_DAL_ERR_082.getCode());
			} else {
				error.setErrorMessage(e.getMessage());
				error.setErrorCode(ErrorConstants.CO_DAL_ERR_GENERIC.getCode());
			}
			wsAuditBO.setErrors(errors);
		}
		return wsAuditBO;

	}

	/***********************************************************************************************************************
	 * Method to fetch the List of Instances based on the Authorization
	 * permission for the logged in user.
	 *
	 * @param InstanceBO
	 * @return List<InstanceBO>
	 * @throws ICException
	 **********************************************************************************************************************/
	@Transactional
	private List<InstanceBO> getListInstances(InstanceBO instBO, String sql) throws ICException {
		CloudOneResultSet resultSet = null;
		List<InstanceBO> instanceBOList = null;
		try {
			if(sql == null || sql.isEmpty()) {//fallback to default one.
				sql = DatabaseSQLConstant.INSTANCE_BASED_ON_SCOPE_SQL;
			}
			//If we need to fetch all instances (including DELETED) then,
			//change the SQL. Basically replace the 'Deleted' clause by space.
			//yes, its a crazy way but did not want to introduce a new SQL just with
			//minor change (without deleted clause) and also couldn't think of
			//any smart way of string formation logic, so thought let me go this way.
			if(instBO.isFetchAllInstances()) {
				sql = DatabaseSQLConstant.INSTANCE_BASED_ON_SCOPE_SQL.replace("INS.State !='Deleted' AND ","");
			}
			int providerId = 0;
			int dataCenterId = 0;
			int clusterId = 0;
			//AND INS.PROVIDERID = (%5$s)
			//AND INS.DataCenterID = (%7$s) 
		    //AND INS.ClusterID = (%8$s)
			String appendString = "";
			if(instBO.getFilter() != null && !instBO.getFilter().isEmpty()) {
				Map<FilterTypeEnum, Filter> filters = buildFilterMap(instBO.getFilter());
				if(filters.get(FilterTypeEnum.PROVIDER_ID) != null) {
					providerId = NumberUtils.toInt(org.apache.commons.lang.StringUtils.join(filters.get(FilterTypeEnum.PROVIDER_ID).getValues(), ","));
					appendString = appendString + " AND INS.PROVIDERID = (%6$s) ";
				}
				
				if(filters.get(FilterTypeEnum.DATACENTER_ID) != null) {
					dataCenterId = NumberUtils.toInt(org.apache.commons.lang.StringUtils.join(filters.get(FilterTypeEnum.DATACENTER_ID).getValues(), ","));
					appendString = appendString + " AND INS.DataCenterID = (%7$s) ";
				} 
				
				if(filters.get(FilterTypeEnum.CLUSTER_ID) != null) {
					clusterId = NumberUtils.toInt(org.apache.commons.lang.StringUtils.join(filters.get(FilterTypeEnum.CLUSTER_ID).getValues(), ","));
					appendString = appendString + " AND INS.ClusterID = (%8$s) ";
				} 
			} 
			if(appendString != null && !appendString.isEmpty()) {
				sql = sql.replace("INS.NestedHypervisor = 0", "INS.NestedHypervisor = 0" + appendString);
			}
			resultSet = getFactory().getReportingDAO().executeQuery(
					sql, false,
					instBO.getUserID(), instBO.getActiveUserGroupID(),
					PermissionEnum.COMPUTE_INSTANCE_READ.getId(),
					instBO.getCloudId(), "", providerId,dataCenterId,clusterId);
			instanceBOList = new ArrayList<>();
			instanceBOList = ICUtils.getFeildValue(resultSet, instBO);
		} catch (DALException | IOException | SQLException e) {
			logger.error(
					"Error while getting User Query ;Error is: {}",
					e.getMessage(), e);
			throw new ICException(ErrorConstants.CO_IC_ERR_0093.getCode(), e);
		}
		return instanceBOList;

	}

	@Transactional
	public InstanceBO updateAdditionalDiskSpace(InstanceBO instanceBO)
			throws ICException {
		CloudOneDAOFactory factory = null;
		boolean dskAvailability = false;
		slfacade = SLInstancesFacade.SINGLETON_INSTANCE;
		try {
			IPlatformsDomain domain = new PlatformsDomainImpl();
			dskAvailability = checkDataStoresBeforeAddingDisk(instanceBO);
			logger.info("dskAvailability {}",dskAvailability);
			if (!dskAvailability) {
				logger.info("Insufficent disk place as the 10% threshold limit reached for the Datastore - returning failure message");
				throw new ICException(
						ErrorConstants.CO_IC_ERR_0118.getDescription());
			}
			PlatformBO platformBO = domain.fetchProvider(PlatformsProvider
					.getInputPlatformBO(instanceBO));
			instanceBO = slfacade.updateAdditionalDiskSpace(instanceBO,
					PlatformsProvider.getProviderCredentials(platformBO));

			factory = CloudOneDAOFactoryBuilder.getFactory();
			InstanceDetailsDAO instanceDetailsDAO = factory
					.getInstanceDetailsDAO();

			this.updateDataStoresAfterAddingDisk(instanceBO, instanceDetailsDAO);
		} catch (DALException e) {

			CloudOneErrors errors = new CloudOneErrors();
			CloudOneError error = new CloudOneError();
			if (ErrorConstants.CO_DAL_ERR_004.getCode().equals(e.getMessage())) {
				error.setErrorMessage(ErrorConstants.CO_DAL_ERR_004
						.getDescription());
				error.setErrorCode(ErrorConstants.CO_DAL_ERR_004.getCode());
			} else {
				error.setErrorMessage(e.getMessage());
				error.setErrorCode(ErrorConstants.CO_DAL_ERR_GENERIC.getCode());
			}
			errors.addError(error);
			logger.error(e.getMessage());
			throw new ICException(e);
		} catch (SLException e) {
			throw new ICException(e);
		}
		return instanceBO;
	}

	// Deprecating this method since the way fetch works in this method does not
	// really apply to any use case
	@Deprecated
	public InstanceBO getInstanceDetailsByVMID(InstanceBO instanceBO)
			throws ICException {
		InstanceBO instBO = null;
		try {
			InstanceDetailsDO idDO = ICUtils.getMatchingInstanceDOByInstanceName(instanceBO.getCloudId(), instanceBO.getProviderID(), instanceBO.getInstanceName());
			if (idDO != null) {
				instBO = InstanceProvider.getBOFromDO(idDO);
			}else{
				logger.debug("Unable to find Matching Instance from DB for InstanceName {}",
						StringUtils.doTrim(instanceBO.getInstanceName()));				
			}
		} catch (Exception e) {
			CloudOneErrors errors = new CloudOneErrors();
			CloudOneError error = new CloudOneError();
			if (ErrorConstants.CO_DAL_ERR_004.getCode().equals(e.getMessage())) {
				error.setErrorMessage(ErrorConstants.CO_DAL_ERR_004
						.getDescription());
				error.setErrorCode(ErrorConstants.CO_DAL_ERR_004.getCode());
			} else {
				error.setErrorMessage(e.getMessage());
				error.setErrorCode(ErrorConstants.CO_DAL_ERR_GENERIC.getCode());
			}
			errors.addError(error);
			logger.error(e.getMessage());
			throw new ICException(e);
		}
		return instBO;
	}

	@Transactional
	public InstanceBO getInstanceDetailsByVMID(int instanceId)
			throws ICException {
		InstanceBO instBO = null;
		try {
			InstanceDetailsDAO idDAO = getFactory().getInstanceDetailsDAO();
			InstanceDetailsDO idDO = new InstanceDetailsDO();
			List<InstanceDetailsDO> resultDO = null;

			idDO.setInstanceID(instanceId);

			resultDO = idDAO.fetchByInstanceId(idDO);
			if (resultDO != null && !resultDO.isEmpty()) {
				instBO = InstanceProvider.getBOFromDO(resultDO.get(0));
			}
		} catch (DALException e) {
			CloudOneErrors errors = new CloudOneErrors();
			CloudOneError error = new CloudOneError();
			if (ErrorConstants.CO_DAL_ERR_004.getCode().equals(e.getMessage())) {
				error.setErrorMessage(ErrorConstants.CO_DAL_ERR_004
						.getDescription());
				error.setErrorCode(ErrorConstants.CO_DAL_ERR_004.getCode());
			} else {
				error.setErrorMessage(e.getMessage());
				error.setErrorCode(ErrorConstants.CO_DAL_ERR_GENERIC.getCode());
			}
			errors.addError(error);
			logger.error(e.getMessage());
			throw new ICException(e);
		}
		return instBO;
	}

	@Transactional
	public String getNewInstanceName(String instanceName, int cloudId) {
		// for JIRA 3354
		String newInstanceName = "";
		TaskDetailsDO taskDetailsDO = new TaskDetailsDO();
		CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		TaskDetailsDAO taskDetailsDAO = factory.getTaskDetailsDAO();
		taskDetailsDO.setCloudMasterDO(taskDetailsDAO.get(CloudMasterDO.class,
				cloudId));
		// taskDetailsDO.setTarget(instanceName);
		List<TaskDetailsDO> taskList = new ArrayList<>();
		try {
			taskList = taskDetailsDAO.fetchByCloudId(taskDetailsDO);
		} catch (DALException e1) {
			logger.error(e1.getMessage());
		}
		if (taskList != null && !taskList.isEmpty()) {
			for (TaskDetailsDO detailsDO : taskList) {
				if (detailsDO.getTarget() != null && !"".equals(detailsDO.getTarget()) && detailsDO.getTarget().equalsIgnoreCase(instanceName)) {
					newInstanceName = instanceName;
				}
			}
		}
		return newInstanceName;

		// }
		// for JIRA 3354

	}

	// 133053, for CO-3772
	public List<VMCustomizationSpecInfoBO> getAllCustomizationSpecInfo(BaseBO inputBO) throws ICException {
		// Get Create Instance class object reference
		ICreateInstanceDomain obj = CreateInstanceFactory.getInstance(inputBO
				.getProviderType());

		return obj.getAllCustomizationSpecInfo(inputBO);
	}

	// 133053, for CO-3772
	public String getCustomizableOperatingSystems(
			BaseBO inputBO, String vmTemplateName) throws ICException {
		// Get Create Instance class object reference
		ICreateInstanceDomain obj = CreateInstanceFactory.getInstance(inputBO
				.getProviderType());

		return obj.getCustomizableOperatingSystems(inputBO, vmTemplateName);
	}

	/***
	 * Method to fetch list of instance based on Instance Name starting with
	 * nickName.
	 *
	 * @param instBO
	 * @return
	 */@Transactional
	 public List<InstanceBO> getInstancesByNickNamePrefix(InstanceBO instBO) {
		 CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		 InstanceDetailsDAO instanceDetailsDAO = factory.getInstanceDetailsDAO();
		 List<InstanceDetailsDO> instDOList = null;
		 List<InstanceBO> instBOList = null;
		 try {
			 CloudMasterDO cloudMasterDO = instanceDetailsDAO.get(
					 CloudMasterDO.class, instBO.getCloudId());
			 InstanceDetailsDO instanceDetailsDO = new InstanceDetailsDO();
			 instanceDetailsDO.setCloudMasterDO(cloudMasterDO);
			 instanceDetailsDO.setNickName(instBO.getNickName());

			 instDOList = instanceDetailsDAO
					 .fetchByCloudIdNickNamePrefix(instanceDetailsDO);// CO-8821
			 // -
			 // Change

			 instBOList = instanceProvider.getBOFromDO(instDOList);
		 } catch (DALException e) {
			 e.printStackTrace();
		 }
		 return instBOList;
	 }

	 /***
	  * Method to get maximum index value .
	  *
	  * @param instBO
	  * @return
	  */
	 public int getMaxIndexofInstanceName(InstanceBO instBO) {
		 logger.debug(" instBO  Nick Name -- > {}",instBO.getNickName());
		 int maxIndex = 1;
		 List<Integer> indices = new ArrayList<>();
		 Set<String> instanceNames = getListOfNickNamesByPrefix(instBO);
		 String postFix = "";
		 synchronized(this) {
			 RecentTaskDetailsTo rtTO = new RecentTaskDetailsTo();
			 rtTO.setCloudId(instBO.getCloudId());
			 rtTO.setTarget(instBO.getNickName());
			 rtTO.setProviderID(instBO.getProviderID());
			 instanceNames.addAll(ICUtils
					 .getListOfTargetsByPrefix(rtTO));

			 for (String name : instanceNames) {
				 try {
					 postFix = org.apache.commons.lang.StringUtils.substringAfter(
							 name, instBO.getNickName());
					 int index = NumberUtils.toInt(postFix);
					 if (index > 0) {
						 indices.add(index);
					 }
				 } catch (Exception e) {
					 logger.error("Exception while finding index - ignoring ", e.getMessage());
				 }
			 }
			 if (indices.isEmpty()) {
				 return 1;
			 }
			 maxIndex = Collections.max(indices);

		 }
		 logger.debug(" instanceCount -- > {}",maxIndex);
		 return maxIndex + 1;
	 }

	 /**
	  * Method to fetch the list of existing instance names from INSTANCEDETAILS
	  * Will be used while naming new instance process. 
	  * @param instBO
	  * @return
	  */
	 @Transactional
	 public Set<String> getListOfNickNamesByPrefix(InstanceBO instBO) {
		 Set<String> instanceNames = new HashSet<String>();
		 try {

			 CloudOneResultSet resultSet = getFactory().getReportingDAO().executeQuery(
					 DatabaseSQLConstant.GET_LIST_OF_INSTANCE_NAMES_BY_PREFIX, false,
					 instBO.getCloudId(), instBO.getNickName());
			 while (resultSet.next()) {
				 instanceNames.add(resultSet.getString(0));
			 }

		 } catch (Exception e) {
			 logger.error(e);
		 }
		 return instanceNames;
	 }

	 
	 @Transactional
	 public boolean checkDataStoresBeforeAddingDisk(InstanceBO instanceBO) throws DALException {
		 InstanceDetailsDO instDO = null;
		 logger.debug("Inside the method - checkDataStoresBeforeAddingDisk..");
		 try {
			 cdf = CloudOneDAOFactoryBuilder.getFactory();
			 InstanceDetailsDAO instDAO = cdf.getInstanceDetailsDAO();
			 instDO = instDAO.get(InstanceDetailsDO.class,
					 instanceBO.getInstanceId());
			 DataStoresDAO dsDAO = getFactory().getDataStoresDAO();
			 if (instDO.getDataStoresDO() != null) {
				 logger.debug("Getting the datastore..");
				 DataStoresDO dataStoresDO = dsDAO.get(DataStoresDO.class,
						 instDO.getDataStoresDO().getDataStoreId());
				 if (dataStoresDO != null) {
					 double freeSpace = Double.parseDouble(dataStoresDO
							 .getFreeSpaceAvailable());
					 logger.debug("FreeSpace {}",freeSpace);
					 // Adding cast as part of findbugs error removal - int
					 // division result cast to double or float
					 double diskRequested = (instanceBO.getDiskSizeInKB() / (double) (1024 * 1024));
					 logger.debug("diskRequested {}",diskRequested);
					 double totalCapacity = Double.parseDouble(dataStoresDO
							 .getCapacity());
					 logger.debug("totalCapacity {}",totalCapacity);

					 if ((freeSpace - diskRequested) < (0.1 * totalCapacity)) {
						 logger.debug("");
						 return false;
					 }
				 }
			 }
		 } catch (NumberFormatException e) {
			 logger.error("Error while determining the Disk Capacity for Additional Hard Disk {}",e);
			 return false;
		 }
		 return true;
	 }

	 @Transactional
	 public Map<String, List<CreateInstanceInfoTO>> getServiceCatalogForOneClickProv(CreateInstanceInfoBO createInstanceInfoBO)
			 throws ICException {
		 List<CreateInstanceInfoBO> createInstanceInfoBOList = null;
		 List<CreateInstanceInfoTO> serviceCatalogList = null;
		 String providerName = null;
		 ProviderMasterDO providerMasterDO = null;
		 Map<String, List<CreateInstanceInfoTO>> providerServiceCatalogMap = new HashMap<>();
		 IServiceCatalogDomain serviceCatalogDomain = (IServiceCatalogDomain) DomainFactory
				 .getInstance(ICObjectEnum.SERVICECATALOG);
		 if (null != serviceCatalogDomain) {
			 createInstanceInfoBO.setActiveFlag(true);
			 //CO-9844 - Changes start here
			 if (createInstanceInfoBO.getDeploymentName()== null || createInstanceInfoBO.getDeploymentName().isEmpty()) {
				 createInstanceInfoBOList = serviceCatalogDomain
						 .getServiceCatalogsForCloud(createInstanceInfoBO);
			 } else {
				 createInstanceInfoBOList = serviceCatalogDomain
						 .getServiceCatalogsByCloudDeployment(createInstanceInfoBO);
			 }
			 //CO-9844 - Changes end here
			 if (null != createInstanceInfoBOList
					 && !createInstanceInfoBOList.isEmpty()) {
				 for (CreateInstanceInfoBO createInstanceInfoBOItem : createInstanceInfoBOList) {
					 providerMasterDO = getFactory().getProviderMasterDAO().get(
							 ProviderMasterDO.class,
							 createInstanceInfoBOItem.getProviderID());
					 if (null != providerMasterDO) {
						 providerName = providerMasterDO.getProviderName();
					 }
					 if (providerServiceCatalogMap.containsKey(providerName)) {
						 serviceCatalogList = providerServiceCatalogMap
								 .get(providerName);
					 } else {
						 serviceCatalogList = new ArrayList<>();
					 }
					 if (null != serviceCatalogList
							 && serviceCatalogList.size() < 10) { // Max 10
						 // service
						 // catalog
						 // is
						 // allowed
						 // per
						 // provider
						 serviceCatalogList.add(CreateInstanceInfoProvider
								 .getTOFromBO(createInstanceInfoBOItem));
						 providerServiceCatalogMap.put(providerName,
								 serviceCatalogList);
					 }
				 }
			 }
		 }
		 return providerServiceCatalogMap;
	 }

	 @Transactional
	 public Map<String, List<CreateInstanceInfoTO>> getServiceCatalog(
			 CreateInstanceInfoBO createInstanceInfoBO) throws ICException {
		 List<CreateInstanceInfoBO> createInstanceInfoBOList = null;
		 List<CreateInstanceInfoTO> serviceCatalogList = null;
		 String providerName = null;
		 ProviderMasterDO providerMasterDO = null;
		 Map<String, List<CreateInstanceInfoTO>> providerServiceCatalogMap = new HashMap<>();
		 IServiceCatalogDomain serviceCatalogDomain = (IServiceCatalogDomain) DomainFactory
				 .getInstance(ICObjectEnum.SERVICECATALOG);
		 if (null != serviceCatalogDomain) {
			 createInstanceInfoBO.setActiveFlag(true);
			 if (createInstanceInfoBO.getDeploymentName()== null || createInstanceInfoBO.getDeploymentName().isEmpty()) {
				 createInstanceInfoBOList = serviceCatalogDomain
						 .getServiceCatalogsForCloud(createInstanceInfoBO);
			 } else {
				 createInstanceInfoBOList = serviceCatalogDomain
						 .getServiceCatalogsByCloudDeployment(createInstanceInfoBO);
			 }
			 if (null != createInstanceInfoBOList
					 && !createInstanceInfoBOList.isEmpty()) {
				 for (CreateInstanceInfoBO createInstanceInfoBOItem : createInstanceInfoBOList) {
					 providerMasterDO = getFactory().getProviderMasterDAO().get(
							 ProviderMasterDO.class,
							 createInstanceInfoBOItem.getProviderID());
					 if (null != providerMasterDO) {
						 providerName = providerMasterDO.getProviderName();
					 }
					 if (providerServiceCatalogMap.containsKey(providerName)) {
						 serviceCatalogList = providerServiceCatalogMap
								 .get(providerName);
					 } else {
						 serviceCatalogList = new ArrayList<>();
					 }
					 if (null != serviceCatalogList) {
						 serviceCatalogList.add(CreateInstanceInfoProvider
								 .getTOFromBO(createInstanceInfoBOItem));
						 providerServiceCatalogMap.put(providerName,
								 serviceCatalogList);
					 }
				 }
			 }
		 }
		 return providerServiceCatalogMap;
	 }

	 /*
	  * This method needs to be revisited later on to map individual tracking ids
	  * to every instance asked for. Primarily required for bulk provisioning
	  * request. As of now, single WS audit record is getting created for
	  * multiple instances requests, which in turn, is not enabling tracking for
	  * individual requests.
	  * 
	  * (non-Javadoc)
	  * 
	  * @see com.cognizant.cloudone.ic.instance.IInstanceDomain#
	  * createInstanceUsingWebservice
	  * (com.cognizant.cloudone.biz.instance.bo.CreateInstanceInfoBO)
	  */
	 public List<CreateInstanceInfoBO> createInstanceUsingWebservice(
			 CreateInstanceInfoBO createInstanceInfoBO) throws ICException {
		 List<CreateInstanceInfoBO> instances = null;
		 List<CreateInstanceInfoBO> instancesToCreate = null;
		 CreateInstanceInfoBO instanceInfo=null;
		 InstanceBO instanceBO = null;
		 String instanceName = null;
		 String additionalComments = createInstanceInfoBO.getAdditionalComments();
		 List<String> instanceNickNameList = new ArrayList<>();
		 IServiceCatalogDomain serviceCatalogDomain = (IServiceCatalogDomain) DomainFactory
				 .getInstance(ICObjectEnum.SERVICECATALOG);
		 IInstanceDomain instanceDomain = (IInstanceDomain) DomainFactory
				 .getInstance(ICObjectEnum.INSTANCE);
		 // Added for CO-12486
		 WSAuditBO wsAuditBO = getWSAuditBO(createInstanceInfoBO);
		 wsAuditBO.setInstanceName(createInstanceInfoBO.getInstanceName());
		 wsAuditBO=updateWSAudit(wsAuditBO);
		 int wsAuditID=wsAuditBO.getWsAuditID();
		 logger.info("the wsAudit id is --->"+wsAuditID);
		 logger.debug("wsAudit..."+wsAuditID);
		 int userId = createInstanceInfoBO.getUserID();
		 int activeUserGroupId = createInstanceInfoBO.getActiveUserGroupID();
		 int diskSize = createInstanceInfoBO.getDiskSize();
		 String diskUnit = createInstanceInfoBO.getDiskSizeUnit();
		 String memUnit = createInstanceInfoBO.getMemorySizeUnit();
		 int memorySize = createInstanceInfoBO.getMemorySize();
		 int noOfCPU = createInstanceInfoBO.getNoOfVirtualProcessors();
		 boolean isWebService=createInstanceInfoBO.isWebServices();
		 boolean isAsync=createInstanceInfoBO.isAsynchronous();
		 int noOfInstancesToCreate = createInstanceInfoBO.getNoOfInstancesToCreate();
		 String prefix = createInstanceInfoBO.getPrefix();
		 List<String> instanceNames = createInstanceInfoBO.getListOfInstanceNames();
		 if((instanceNames == null || instanceNames.isEmpty()) && createInstanceInfoBO.getNickNames() != null && !createInstanceInfoBO.getNickNames().isEmpty()) {
			 instanceNames = createInstanceInfoBO.getNickNames();
		 }
		 String wfRequestId = createInstanceInfoBO.getWfRequestId();

		 String instanceType = createInstanceInfoBO.getSelectedInstanceType();
		 String userName = createInstanceInfoBO.getUserName();
		 String password = createInstanceInfoBO.getPassword();
		 String network = createInstanceInfoBO.getPortGroupName();
		 String subnetID = createInstanceInfoBO.getSubnetId();
		 String host = createInstanceInfoBO.getHost();
		 String dnsName = createInstanceInfoBO.getDnsName();
		 String storagePoolName = createInstanceInfoBO.getStoragePoolName();
		 String portConfig = createInstanceInfoBO.getPortDetails();
		 List<VirtualDiskBO> virtualDiskBOs = createInstanceInfoBO.getVirtualDisks();
		 createInstanceInfoBO = serviceCatalogDomain
				 .getServiceCatalogByNameCloudIdProviderId(createInstanceInfoBO);
		 createInstanceInfoBO.setAdditionalComments(additionalComments);
		 createInstanceInfoBO.setUpdatedByUser(userId);
		 createInstanceInfoBO.setUserID(userId);
		 createInstanceInfoBO.setActiveUserGroupID(activeUserGroupId);
		 createInstanceInfoBO.setWebServices(isWebService);
		 createInstanceInfoBO.setAsynchronous(isAsync);
		 createInstanceInfoBO.setWsAuditID(wsAuditID);
		 createInstanceInfoBO.setWfRequestId(wfRequestId);
		 if(instanceType != null && !instanceType.isEmpty()) {
			 createInstanceInfoBO.setSelectedInstanceType(instanceType);
		 }
		 if(userName != null && !userName.isEmpty()) {
			 createInstanceInfoBO.setUserName(userName);
		 }
		 if(password != null && !password.isEmpty()) {
			 createInstanceInfoBO.setPassword(password);
		 }
		 if(network != null && !network.isEmpty()) {
			 createInstanceInfoBO.setPortGroupName(network);
		 }
		 if(subnetID != null && !subnetID.isEmpty()) {
			 createInstanceInfoBO.setSubnetId(subnetID);
		 }
		 if(host != null && !host.isEmpty()) {
			 createInstanceInfoBO.setHost(host);
		 }
		 if(dnsName != null && !dnsName.isEmpty()) {
			 createInstanceInfoBO.setDnsName(dnsName);
		 }
		 if(storagePoolName != null && !storagePoolName.isEmpty()) {
			 createInstanceInfoBO.setStoragePoolName(storagePoolName);
			 createInstanceInfoBO.setCustomizationSpecName(storagePoolName);
		 }
		 if(virtualDiskBOs != null && !virtualDiskBOs.isEmpty() && virtualDiskBOs.size() > 0)  {
			 createInstanceInfoBO.setVirtualDisks(virtualDiskBOs);
		 }
		 if(createInstanceInfoBO.getSSHKeyFile() != null && createInstanceInfoBO.getSSHKeyFile().length == 0) {
			 createInstanceInfoBO.setSSHKeyFile(null);
		 }
		 
		 if(portConfig != null && !portConfig.isEmpty()) {
			 createInstanceInfoBO.setPortDetails(portConfig);
			 createInstanceInfoBO.setDefaultEndPoint(false);
		 }
		 
		 if (noOfCPU > 0) {
			 createInstanceInfoBO.setNoOfVirtualProcessors(noOfCPU);
		 }
		 if (diskSize > 0) {
			 createInstanceInfoBO.setDiskSize(diskSize);
			 createInstanceInfoBO.setDiskSizeUnit(diskUnit);
			 createInstanceInfoBO.setDiskRequired(true);
		 }
		 if (memorySize > 0) {
			 createInstanceInfoBO.setMemorySize(memorySize);
			 createInstanceInfoBO.setMemorySizeUnit(memUnit);
		 }
		 if(instanceNames != null && !instanceNames.isEmpty()) {
			 createInstanceInfoBO.setListOfInstanceNames(instanceNames);
		 } else if (prefix != null && !prefix.isEmpty()) {
			 createInstanceInfoBO.setListOfInstanceNames(null);
		 }
		 if (createInstanceInfoBO.getListOfInstanceNames() != null
				 && !createInstanceInfoBO.getListOfInstanceNames().isEmpty()) {
			 createInstanceInfoBO.setNickNames(createInstanceInfoBO
					 .getListOfInstanceNames());
			 createInstanceInfoBO.setNoOfInstancesToCreate(createInstanceInfoBO
					 .getListOfInstanceNames().size());
		 } else if(noOfInstancesToCreate > 1) {
			if (createInstanceInfoBO.getProviderType().equals(
					ProviderTypeEnum.IBMSL)) {
				instancesToCreate = new ArrayList<>();
				if (createInstanceInfoBO.getDnsName() != null
						&& !createInstanceInfoBO.getDnsName().isEmpty()) {
					createInstanceInfoBO.setDnsName(createInstanceInfoBO
							.getDnsName() + ".com");
				}
			}
			 for(int i = 0 ; i < noOfInstancesToCreate ; i++) {
				 instanceBO = InstanceProvider.getInstanceBO(createInstanceInfoBO);
				 instanceName = ICUtils.generatedInstanceNameWithPrefix(
						 instanceBO.getNickName(),
						 (getMaxIndexofInstanceName(instanceBO) + i));
				 instanceNickNameList.add(instanceName);
				 if(createInstanceInfoBO.getProviderType().equals(ProviderTypeEnum.IBMSL)){
					 instanceInfo=new CreateInstanceInfoBO();
					 instanceInfo.setInstanceName(instanceName);
					 instancesToCreate.add(instanceInfo);
				 }
			 }
			 if(instancesToCreate!=null && !instancesToCreate.isEmpty()){
				 createInstanceInfoBO.setInstances(instancesToCreate);
			 }
			 createInstanceInfoBO.setListOfInstanceNames(instanceNickNameList);
			 createInstanceInfoBO.setNickNames(instanceNickNameList);
			 createInstanceInfoBO.setNoOfInstancesToCreate(noOfInstancesToCreate);
		 } else {
			 instanceBO = InstanceProvider.getInstanceBO(createInstanceInfoBO);
			 if(prefix != null) {
				 instanceBO.setNickName(prefix);
			 }
			 instanceName = ICUtils.generatedInstanceNameWithPrefix(
					 instanceBO.getNickName(),
					 getMaxIndexofInstanceName(instanceBO));
			 instanceNickNameList.add(instanceName);
			 createInstanceInfoBO.setListOfInstanceNames(instanceNickNameList);
			 createInstanceInfoBO.setNickNames(instanceNickNameList);
			 createInstanceInfoBO.setNoOfInstancesToCreate(1);
			if (createInstanceInfoBO.getProviderType().equals(
					ProviderTypeEnum.IBMSL)) {
				instancesToCreate = new ArrayList<>();
				instanceInfo = new CreateInstanceInfoBO();
				instanceInfo.setInstanceName(instanceName);
				instancesToCreate.add(instanceInfo);
				createInstanceInfoBO.setInstances(instancesToCreate);
				if (createInstanceInfoBO.getDnsName() != null
						&& !createInstanceInfoBO.getDnsName().isEmpty()) {
					createInstanceInfoBO.setDnsName(createInstanceInfoBO
							.getDnsName() + ".com");
				}
			}
		 }
		 createInstanceInfoBO.setAsynchronous(true);
		 instances = instanceDomain.createInstances(createInstanceInfoBO);
		 return instances;
	 }
	 // To set the instance state to powered on
	 @Transactional
	 public void setInstanceStatePoweredOn(
			 InstanceBO instanceBO) throws ICException {
		 try {
			 if (instanceBO != null) {
				 logger.info("InstanceDomainImpl::setInstanceState");
				 InstanceDetailsDAO instanceDetailsDAO = getFactory()
						 .getInstanceDetailsDAO();
				 InstanceDetailsDO instanceDetailsDO = instanceDetailsDAO.get(
						 InstanceDetailsDO.class, instanceBO.getInstanceId());
				 instanceDetailsDO.setState(DashBoardConstants.POWEREDON);
				 instanceDetailsDAO.update(instanceDetailsDO);
			 }
		 } catch (DALException e) {
			 logger.error(e);
			 throw new ICException(e);
		 }
	 }


	 /**
	  * Method to notify sms/email while Cloning VM's
	  *
	  * @param T
	  *            BaseBO
	  * @throws ICException
	  */	@Transactional
	  public <T extends BaseBO> void notifyOnCloneVM(T instanceInfo)
			  throws ICException {
		  cdf = CloudOneDAOFactoryBuilder.getFactory();
		  if (instanceInfo != null) {
			  String messageBeanType = instanceInfo.getClass().getName();
			  String cloudName = "";
			  String orgName = "";
			  OrganizationBO organizationBO = ICUtils
					  .getCloudOrganisationDetails(instanceInfo);
			  if (organizationBO != null) {
				  cloudName = organizationBO.getCloudName();
				  if (organizationBO.getOrganisationName() != null) {
					  orgName = organizationBO.getOrganisationName();
				  }
			  }
			  instanceInfo.setCloudName(cloudName);
			  instanceInfo.setOrganisationName(orgName);
			  CloudOneErrors errors = instanceInfo.getErrors();
			  String status = (null != errors && errors.hasErrors() ? MessageStringConstants.FAILED
					  : MessageStringConstants.COMPELTE);
			  try {
				  ProvisionBean provisionBean = null;
				  if (messageBeanType
						  .equalsIgnoreCase(InstanceBO.class.getName())) {
					  InstanceBO instanceBO = (InstanceBO) instanceInfo;
					  logger.info("instanceBO  NotificationID  is: "
							  + instanceBO.getNotificationID());
					  instanceBO
					  .setOperation(InstancesProvisioningConstants.EMAIL_CLONE_VM);
					  provisionBean = InstanceProvider
							  .populateProvisionBeanFromInstanceBO(instanceBO,
									  status);
				  } else if (messageBeanType
						  .equalsIgnoreCase(ProcessInstanceBO.class.getName())) {
					  ProcessInstanceBO instanceBO = (ProcessInstanceBO) instanceInfo;
					  InstanceDetailsDO instanceDetailsDO = ICUtils.getMatchingInstanceDOByInstanceName(instanceBO.getCloudId(), instanceBO.getProviderID(), instanceBO.getInstanceName());
					  if (instanceDetailsDO != null && instanceDetailsDO.getNotificationDO() != null) {
						  logger.info("notification for selected instance"
								  + instanceDetailsDO.getNotificationDO()
								  .getNotificationID());
						  instanceBO.setNotificationID(instanceDetailsDO
								  .getNotificationDO().getNotificationID());
					  }
					  instanceBO
					  .setCloudAction(InstancesProvisioningConstants.EMAIL_CLONE_VM);
					  provisionBean = InstanceProvider
							  .populateProvisionBeanFromInstanceBO(instanceBO,
									  status);
				  }
				  if (provisionBean != null) {
					  if (provisionBean.getNotificationId() > 0) {
						  sendNotification(provisionBean);
					  }
				  }
			  } catch (Exception ex) {
				  List<T> instanceBOs = new ArrayList<>();
				  instanceBOs.add(instanceInfo);
				  populateErrorMessages(instanceBOs, ex);
			  }
		  }
	  }

	  public InstanceBO reconfigureVMSettings(InstanceBO instanceBO, boolean isScaleUp)
			  throws ICException {

		  List<InstanceBO> instanceBOList = new ArrayList<>();		
		  List<WorkerQueueBO> workerQueueObjects = null;		
		  TaskDescriptionEnum taskDescription = null;

		  try {
			  if(isScaleUp) {
				  taskDescription = TaskDescriptionEnum.AUTOSCALEUP;
			  } else {
				  taskDescription = TaskDescriptionEnum.AUTOSCALEDOWN;
			  }

			  instanceBOList.add(instanceBO);

			  instanceBO.setWqTarget(StringUtils.getDisplayInstanceAndNickName(
					  instanceBO.getInstanceName(), instanceBO.getNickName()));
			  instanceBO.setWqTargetId(instanceBO.getInstanceId());
			  instanceBO.setWqTargetType(EntityTypeEnum.INSTANCE.getId());
			  instanceBO.setParentWQTaskId(GlobalConstant.META_PARENT_TASK_ID);

			  workerQueueObjects = WorkerQueueHelper
					  .buildInitialWorkerQueueObjectList(instanceBOList,
							  taskDescription);
			  workerQueueObjects = WorkerQueueManager
					  .updateInitialWorkerQueueStatus(workerQueueObjects);

			  for(WorkerQueueBO workerQueueBO : workerQueueObjects) {
				  if(instanceBO.getWqTargetId() == workerQueueBO.getWqTargetId()) {
					  if(workerQueueBO.getCurrentTaskKey() > 0)
						  instanceBO.setWqTaskID(workerQueueBO.getCurrentTaskKey());
				  }
			  }
			  Thread instanceScalingImplThread = new Thread(new InstanceScalingImpl(this, workerQueueObjects, instanceBO, isScaleUp));
			  instanceScalingImplThread.start();
		  } catch (ICException e) {
			  logger.error(e);
			  instanceBOList = this.populateErrorMessages(instanceBOList, e);
			  e.printStackTrace();
		  }

		  return instanceBO;
	  }


	  /**
	   * Method to reconfigure the VM Settings on CPU and Memory
	   *
	   * @param T
	   *            BaseBO
	   * @throws ICException
	   */
	  public  List<InstanceBO> reconfigureVMSettings(List<InstanceBO> instanceBOs, boolean isPolicyExecuted)
			  throws ICException {
		  // TODO Auto-generated method stub
		  // CloudOneDAOFactory factory = null;
		  CloudOneError error = null;
		  CloudOneErrors cloudOneErrors = null;
		  UserActionEnum userAction = null;
		  List<InstanceBO> instanceBOList = new ArrayList<>();
		  //		List<InstanceBO> instanceBOs = new ArrayList<InstanceBO>();
		  List<WorkerQueueBO> workerQueueObjects = null;
		   boolean isBlockedListNotEmpty=  false;
		  slfacade = SLInstancesFacade.SINGLETON_INSTANCE;

		  IPlatformsDomain domain = new PlatformsDomainImpl();
		  PlatformBO platformBO = domain
				  .fetchProvider(PlatformsProvider
						  .getInputPlatformBO(instanceBOs.get(0)));
		  CloudCredentialsBO cloudCredentialsBO = PlatformsProvider.getProviderCredentials(platformBO);

		  for(InstanceBO instanceBO : instanceBOs) {
			  //CO-11303: Using concatenated name for WQ to avoid confusion with same name VMs
			  instanceBO.setWqTarget(StringUtils.getDisplayInstanceAndNickName(
					  instanceBO.getInstanceName(), instanceBO.getNickName()));
			  instanceBO.setWqTargetId(instanceBO.getInstanceId());
			  instanceBO.setWqTargetType(EntityTypeEnum.INSTANCE.getId());
			  instanceBOList.add(instanceBO);

			  try {
				  if (!isPolicyExecuted){
					  if(isModifiedResourceActionScaleUp(instanceBO)){
						  userAction = UserActionEnum.SCALE_UP;
					  }else{
						  userAction = UserActionEnum.SCALE_DOWN;
					  }
					  Map<InstanceBO, PolicyException>  blockedList = BusinessPoliciesUtil
								.executeReconfigureVMSettingsProvisioningPolicies(instanceBOs, userAction);	
					  if (blockedList != null && !blockedList.isEmpty()) {			
							for (Map.Entry<InstanceBO, PolicyException> entry : blockedList
									.entrySet()) {
								CloudOneErrors extractPolicyErrors = BusinessPoliciesUtil
										.extractPolicyErrors(entry.getValue());				
								instanceBO.setErrors(extractPolicyErrors);
								isBlockedListNotEmpty = true;
								workerQueueObjects = WorkerQueueHelper
										  .buildInitialWorkerQueueObjectList(instanceBOList,
												  TaskDescriptionEnum.EDIT_INSTANCE_RESOURCES);
								workerQueueObjects = WorkerQueueManager
										  .updateInitialWorkerQueueStatus(workerQueueObjects);
							}
						}
				  } 
				  
				  
				  if (!isBlockedListNotEmpty) {
					  logger.debug("InstancedomainImpl providertype :{}", instanceBO.getProviderType() );
						  if (instanceBO.getProviderType() != ProviderTypeEnum.SAVVIS
								  && instanceBO.getProviderType() != ProviderTypeEnum.SAVVISVPDC) {
							  if(instanceBO.isScheduledExecution()){
								  workerQueueObjects = WorkerQueueHelper
										  .buildInitialWorkerQueueObjectList(instanceBOs,
												  TaskDescriptionEnum.SCHEDULED_MODIFY_RESOURCE);
			
							  }else{
								  workerQueueObjects = WorkerQueueHelper
										  .buildInitialWorkerQueueObjectList(instanceBOs,
												  TaskDescriptionEnum.EDIT_INSTANCE_RESOURCES);
							  }
							  workerQueueObjects = WorkerQueueManager
									  .updateInitialWorkerQueueStatus(workerQueueObjects);
							  logger.debug("Populated WQ from BO - reconfigureVMSettings");
							  
							  logger.debug("setting current task key of Edit instance resources");
							  //CO-19408, Here setting the taskId for task status tracking in the calling methods
							  instanceBO.setWqTaskID(workerQueueObjects.get(0).getCurrentTaskKey());
						  }
						  //				instanceBOs = new ArrayList<InstanceBO>();
						  if (instanceBO.getProviderType().equals(ProviderTypeEnum.VMWARE)) {
							  if (instanceBO.getNoOfCPUs() != 0
									  || instanceBO.getMemorySize() != 0) {
								  AsyncNotifiable<InstanceDomainImpl> asyncNotifiable = new AsyncInstanceResourcesNotifiableImpl(this, workerQueueObjects);
								  instanceBO = slfacade.reconfigureVMSettings(instanceBO, cloudCredentialsBO, asyncNotifiable);
								  //						logger.debug("Successfully completed - next step to update the DB");
								  //						this.updateInstanceResources(instanceBO);
								  //						instanceBOs.add(instanceBO);
							  } else {
								  error = new CloudOneError();
								  cloudOneErrors = new CloudOneErrors();
								  error.setErrorCode(ErrorConstants.CO_IC_ERR_0135.getCode());
								  error.setErrorMessage(String
										  .format(ErrorConstants.CO_IC_ERR_0135
												  .getDescription()));
								  cloudOneErrors.addError(error);
								  instanceBO.setErrors(cloudOneErrors);
								  //						instanceBOs.add(instanceBO);
							  }
						  } else if (instanceBO.getProviderType().equals(
								  ProviderTypeEnum.AMAZON)) {
							  if (instanceBO.isSystemTypeChanged()) {
								  AsyncNotifiable<InstanceDomainImpl> asyncNotifiable = new AsyncInstanceResourcesNotifiableImpl(this, workerQueueObjects);
								  instanceBO = slfacade.reconfigureVMSettings(instanceBO, cloudCredentialsBO, asyncNotifiable);
								  //						logger.debug("Successfully completed - next step to update the DB");
								  //						this.updateInstanceResources(instanceBO);
								  //						instanceBOs.add(instanceBO);
							  } else {
								  error = new CloudOneError();
								  cloudOneErrors = new CloudOneErrors();
								  error.setErrorCode(ErrorConstants.CO_IC_ERR_0135.getCode());
								  error.setErrorMessage(String
										  .format(ErrorConstants.CO_IC_ERR_0135
												  .getDescription()));
								  cloudOneErrors.addError(error);
								  instanceBO.setErrors(cloudOneErrors);
								  //						instanceBOs.add(instanceBO);
							  }
						  } else if (instanceBO.getProviderType().equals(
								  ProviderTypeEnum.SAVVIS)
								  || instanceBO.getProviderType().equals(
										  ProviderTypeEnum.SAVVISVPDC)) {
			
							  workerQueueObjects = populateInitialWQEntriesForSavvisModifyResources(
									  instanceBO, instanceBOs);
			
							  if(!instanceBO.isCPUChanged() && !instanceBO.isMemoryChanged()) {
								  error = new CloudOneError();
								  cloudOneErrors = new CloudOneErrors();
								  error.setErrorCode(ErrorConstants.CO_IC_ERR_0166.getCode());
								  error.setErrorMessage(String
										  .format(ErrorConstants.CO_IC_ERR_0166
												  .getDescription()));
								  cloudOneErrors.addError(error);
								  instanceBO.setErrors(cloudOneErrors);
							  } else {
								  populateOrgId(instanceBO);
								  AsyncNotifiable<InstanceDomainImpl> asyncNotifiable = new AsyncInstanceResourcesNotifiableImpl(this,
										  workerQueueObjects);
								  instanceBO = slfacade.reconfigureVMSettings(instanceBO,
										  cloudCredentialsBO, asyncNotifiable);
							  }
						  }
						  else if (instanceBO.getProviderType().equals(
								  ProviderTypeEnum.OPENSTACK)) {
			
							  if (instanceBO.getIdentifier() != null) {
								  AsyncNotifiable<InstanceDomainImpl> asyncNotifiable = new AsyncInstanceResourcesNotifiableImpl(this, workerQueueObjects);
								  instanceBO = slfacade.reconfigureVMSettings(instanceBO, cloudCredentialsBO, asyncNotifiable);
							  } else {
								  error = new CloudOneError();
								  cloudOneErrors = new CloudOneErrors();
								  error.setErrorCode(ErrorConstants.CO_IC_ERR_0135.getCode());
								  error.setErrorMessage(String
										  .format(ErrorConstants.CO_IC_ERR_0135
												  .getDescription()));
								  cloudOneErrors.addError(error);
								  instanceBO.setErrors(cloudOneErrors);
							  }
						  }
					}
			  } catch (SLException e) {
				  logger.error(e.getMessage());
				  error = new CloudOneError();
				  cloudOneErrors = new CloudOneErrors();
				  error.setErrorMessage(e.getMessage());
				  cloudOneErrors.addError(error);
				  instanceBO.setErrors(cloudOneErrors);
			  } finally {
				  try{
					  if (isBlockedListNotEmpty) {
						  workerQueueObjects = WorkerQueueHelper.buildFinalWorkerQueueObjectList(ICUtils.setWQTaskID(instanceBOList, workerQueueObjects),TaskDescriptionEnum.EDIT_INSTANCE_RESOURCES);
						  WorkerQueueManager.updateFinalWorkerQueueStatus(workerQueueObjects);
					  }
				  } catch (Exception ice) {
						logger.error(ice);				
					}
			  }
		  }
		  return instanceBOs;
	  }

	  private List<WorkerQueueBO> populateInitialWQEntriesForSavvisModifyResources(InstanceBO instanceBO, List<InstanceBO> instanceBOs)
			  throws ICException {
		  List<WorkerQueueBO> finalList = new ArrayList<>();
		  List<WorkerQueueBO> wqObjects = null;
		  if(instanceBO.isCPUChanged()) {
			  if(instanceBO.isScheduledExecution()){
				  wqObjects = WorkerQueueHelper
						  .buildInitialWorkerQueueObjectList(instanceBOs,
								  TaskDescriptionEnum.SCHEDULED_MODIFY_CPU);
			  }else {
				  wqObjects = WorkerQueueHelper
						  .buildInitialWorkerQueueObjectList(instanceBOs,
								  TaskDescriptionEnum.EDIT_SAVVIS_VM_CPU);
			  }
			  wqObjects = WorkerQueueManager
					  .updateInitialWorkerQueueStatus(wqObjects);
			  if(wqObjects != null) {
				  finalList.addAll(wqObjects);
			  }
		  }
		  if(instanceBO.isMemoryChanged()) {
			  if(instanceBO.isScheduledExecution()){
				  wqObjects = WorkerQueueHelper
						  .buildInitialWorkerQueueObjectList(instanceBOs,
								  TaskDescriptionEnum.SCHEDULED_MODIFY_MEMORY);
			  }else {
				  wqObjects = WorkerQueueHelper
						  .buildInitialWorkerQueueObjectList(instanceBOs,
								  TaskDescriptionEnum.EDIT_SAVVIS_VM_MEMORY);
			  }
			  wqObjects = WorkerQueueManager
					  .updateInitialWorkerQueueStatus(wqObjects);
			  if(wqObjects != null) {
				  finalList.addAll(wqObjects);
			  }
		  }
		  return finalList;
	  }

	  public void updateDetailsForModifyResources(
			  List<WorkerQueueBO> workerQueueObjects, List<InstanceBO> instanceBOs, InstanceBO instanceBO) throws ICException {
		  try {
			  if (instanceBO.getProviderType() == ProviderTypeEnum.SAVVIS
					  || instanceBO.getProviderType() == ProviderTypeEnum.SAVVISVPDC) {
				  if (instanceBO.isCPUChanged()) {
					  List<WorkerQueueBO> modifyCpuWQBOs = new ArrayList<>();
					  List<WorkerQueueBO> wqObjects = null;
					  for(WorkerQueueBO wqBO : workerQueueObjects) {
						  if(wqBO.getTaskId() == TaskDescriptionEnum.EDIT_SAVVIS_VM_CPU.getId()) {
							  modifyCpuWQBOs.add(wqBO);
							  wqObjects = WorkerQueueHelper
									  .buildFinalWorkerQueueObjectList(ICUtils.setWQTaskID(
											  instanceBOs, modifyCpuWQBOs),
											  TaskDescriptionEnum.EDIT_SAVVIS_VM_CPU);
						  }else  if(wqBO.getTaskId() == TaskDescriptionEnum.SCHEDULED_MODIFY_CPU.getId()) {
							  modifyCpuWQBOs.add(wqBO);
							  wqObjects = WorkerQueueHelper
									  .buildFinalWorkerQueueObjectList(ICUtils.setWQTaskID(
											  instanceBOs, modifyCpuWQBOs),
											  TaskDescriptionEnum.SCHEDULED_MODIFY_CPU);

						  }
						  WorkerQueueManager.updateFinalWorkerQueueStatus(wqObjects);
						  break;

					  }
				  }
				  if (instanceBO.isMemoryChanged()) {
					  List<WorkerQueueBO> modifyMemoryWQBOs = new ArrayList<>();
					  List<WorkerQueueBO> wqObjects = null;
					  for(WorkerQueueBO wqBO : workerQueueObjects) {
						  if(wqBO.getTaskId() == TaskDescriptionEnum.EDIT_SAVVIS_VM_MEMORY.getId()) {
							  modifyMemoryWQBOs.add(wqBO);
							  wqObjects = WorkerQueueHelper
									  .buildFinalWorkerQueueObjectList(ICUtils.setWQTaskID(
											  instanceBOs, modifyMemoryWQBOs),
											  TaskDescriptionEnum.EDIT_SAVVIS_VM_MEMORY);
						  }else if (wqBO.getTaskId() == TaskDescriptionEnum.SCHEDULED_MODIFY_MEMORY.getId()) {
							  modifyMemoryWQBOs.add(wqBO);
							  wqObjects = WorkerQueueHelper
									  .buildFinalWorkerQueueObjectList(ICUtils.setWQTaskID(
											  instanceBOs, modifyMemoryWQBOs),
											  TaskDescriptionEnum.SCHEDULED_MODIFY_MEMORY);
						  }
						  WorkerQueueManager.updateFinalWorkerQueueStatus(wqObjects);
						  break;

					  }
				  }
			  } else {
				  if(instanceBO.isScheduledExecution()){
					  workerQueueObjects = WorkerQueueHelper
							  .buildFinalWorkerQueueObjectList(ICUtils.setWQTaskID(
									  instanceBOs, workerQueueObjects),
									  TaskDescriptionEnum.SCHEDULED_MODIFY_RESOURCE);
				  }else {
					  workerQueueObjects = WorkerQueueHelper
							  .buildFinalWorkerQueueObjectList(ICUtils.setWQTaskID(
									  instanceBOs, workerQueueObjects),
									  TaskDescriptionEnum.EDIT_INSTANCE_RESOURCES);
				  }
				  WorkerQueueManager.updateFinalWorkerQueueStatus(workerQueueObjects);
			  }
			  notifyOnEditResources(instanceBO);
		  } catch (ICException ex) {
			  logger.error("Error while updating the WQ Status");
			  throw new ICException(ex.getMessage());
		  }
	  }

	  /**
	   * Method to store the VM's CPU and Memory
	   * 
	   * @param T
	   *            BaseBO
	   * @throws ICException
	   */
	  @Transactional
	  public void updateInstanceResources(InstanceBO instanceBO)
			  throws DALException {
		  InstanceDetailsDO instDO = null;
		  InstanceDetailsDAO instanceDetailsDAO = getFactory()
				  .getInstanceDetailsDAO();
		  instDO = instanceDetailsDAO.get(InstanceDetailsDO.class,
				  instanceBO.getInstanceId());
		  if (null != instDO) {
			  if (instanceBO.getProviderType().equals(ProviderTypeEnum.VMWARE)) {
				  instDO.setAllocatedCPU(instanceBO.getNoOfCPUs());
				  instDO.setAllocatedMemory((double) instanceBO.getMemorySize());
				  instDO.setAllocatedDiskSize(ICUtils.getSizeInGB(
						  instanceBO.getTotalDiskSize(),
						  instanceBO.getTotalDiskSizeUnit()));
			  } else if (instanceBO.getProviderType().equals(
					  ProviderTypeEnum.AMAZON)) {
				  instDO.setSystemType(instanceBO.getModifiedSystemType());

				  InstanceTypesEnum instanceType = InstanceTypesEnum
						  .getEnum(instDO.getSystemType());
				  if (instanceType != null) {
					  instDO.setAllocatedCPU(instanceType.getNumberOfVCPU()
							  .intValue());
					  instDO.setAllocatedMemory(instanceType.getMemoryInGB()
							  .doubleValue());
				  }
			  } else if (ProviderTypeEnum.SAVVIS == instanceBO.getProviderType()
					  || ProviderTypeEnum.SAVVISVPDC == instanceBO
					  .getProviderType()) {
				  instDO.setAllocatedCPU(instanceBO.getNoOfCPUs());
				  instDO.setAllocatedMemory((double) instanceBO.getMemorySize());
			  } else if (instanceBO.getProviderType().equals(
					  ProviderTypeEnum.AZURE)) {
				  AzureDiskSizeEnum azureInstanceType = AzureDiskSizeEnum
						  .getEnumById(instDO.getSystemType());
				  if (azureInstanceType != null) {
					  instDO.setAllocatedCPU(azureInstanceType.getNumberOfVCPU()
							  .intValue());
					  instDO.setAllocatedMemory(azureInstanceType.getMemoryInGB()
							  .doubleValue());
				  }
			  } else if (instanceBO.getProviderType().equals(
					  ProviderTypeEnum.OPENSTACK)) {
				  instDO.setInstanceOID(Integer.parseInt(instanceBO.getIdentifier()));
			  }
			  instDO.setOperation(instanceBO.getOperation());
			  instanceDetailsDAO.merge(instDO);
			  logger.debug("Update of Instance Resource is successful");
		  } else {
			  logger.warn("Unable to find the instance -- "+ instanceBO.getInstanceName());
		  }
	  }


	  public List<InstanceBO> executeScript(List<InstanceBO> instanceBOs)
			  throws ICException {
		  SLWorkflowFacade slfacade = SLWorkflowFacade.SINGLETON_INSTANCE;
		  LocalScriptCommandImpl cmdImpl = new LocalScriptCommandImpl();    		
		  List<? extends BaseBO> returnList = null;
		  List<InstanceBO> wqinstBOs = null;
		  List<WorkerQueueBO> workerQueueBOs = new ArrayList<>();
		  TaskDescriptionEnum taskEnum = TaskDescriptionEnum.RUN_CUSTOM_SCRIPT_SC;

		  for (InstanceBO instanceBO : instanceBOs) {    				
			  try {			
				  wqinstBOs = new ArrayList<>();
				  if(instanceBO.isScheduledExecution() && "serviceConsole".equalsIgnoreCase(instanceBO.getExecutionService())){
					  taskEnum=TaskDescriptionEnum.SCHEDULED_CUSTOM_SCRIPT_EXECUTION_SC;
				  }else if(instanceBO.isScheduledExecution() && "computeInstance".equalsIgnoreCase(instanceBO.getExecutionService())){
					  taskEnum=TaskDescriptionEnum.SCHEDULED_CUSTOM_SCRIPT_EXECUTION_VM;
				  }
				  if (chkAndPowerOn(instanceBO)) {
					  break;
				  }
				  if (instanceBO.getIdentifier() != null
						  && !instanceBO.getIdentifier().isEmpty()) {
					  instanceBO.setWqTarget(StringUtils
							  .getDisplayInstanceAndNickName(
									  instanceBO.getInstanceName(),
									  instanceBO.getNickName())
									  +GlobalConstant.OPEN_PARANTHESIS+instanceBO.getIdentifier()+GlobalConstant.CLOSED_PARANTHESIS);					
				  } else {
					  instanceBO.setWqTarget(StringUtils
							  .getDisplayInstanceAndNickName(
									  instanceBO.getInstanceName(),
									  instanceBO.getNickName()));
					  instanceBO.setWqTargetId(instanceBO.getInstanceId());					

				  }
				  instanceBO.setWqTargetType(EntityTypeEnum.INSTANCE.getId()); 
				  instanceBO.setOperation(InstancesActionsEnum.EXECUTE_SCRIPT.getValue());
				  wqinstBOs.add(instanceBO);
				  List<? extends BaseBO> inputList = cmdImpl
						  .extractInstanceDetails(wqinstBOs, instanceBO.getScriptActionBO());    				
				  if (instanceBO.getExecutionService().equalsIgnoreCase(
						  "serviceConsole")) {
					  workerQueueBOs = WorkerQueueHelper
							  .buildInitialWorkerQueueObjectList(wqinstBOs,
									  taskEnum);
					  workerQueueBOs = WorkerQueueManager
							  .updateInitialWorkerQueueStatus(workerQueueBOs);
					  returnList = slfacade.executeCustomizationOnSC(inputList);
				  } else if (instanceBO.getExecutionService().equalsIgnoreCase(
						  "computeInstance")) {  
					  if(!instanceBO.isScheduledExecution()){
						  taskEnum = TaskDescriptionEnum.RUN_CUSTOM_SCRIPT_VM;}
					  workerQueueBOs = WorkerQueueHelper
							  .buildInitialWorkerQueueObjectList(wqinstBOs,
									  taskEnum);
					  workerQueueBOs = WorkerQueueManager
							  .updateInitialWorkerQueueStatus(workerQueueBOs);
					  returnList = slfacade.executeCustomizationOnVM(inputList);
				  }
				  cmdImpl.processForErrors(returnList); 
				  if(returnList.size() > 0 && StringUtils.isNotEmpty(returnList.get(0).getExitText())){
					  instanceBO.setContent(returnList.get(0).getExitText());}    				
				  updateFinalWQStatusForRun(workerQueueBOs, instanceBO);
				  notifyOnActions(instanceBO);
			  }
			  catch (Exception ex) {
				  logger.error("Exception while invoking script for instance. : {}", ex);
				  cmdImpl.populateErrors(wqinstBOs, ex);
				  updateFinalWQStatusForRun(workerQueueBOs, wqinstBOs.get(0));							
			  } 
		  }	

		  return instanceBOs;
	  } 


	  public boolean chkAndPowerOn(InstanceBO instanceBO) throws ICException{
		  List<InstanceBO> instBOs = new ArrayList<>();
		  List<InstanceBO> resultList = null; 
		  if (instanceBO.isChkPowerState()
				  && (instanceBO.getState().equalsIgnoreCase(
						  InstanceStateEnum.POWEROFF.getValue())
						  || instanceBO.getState().equalsIgnoreCase(
								  InstanceStateEnum.TERMINATE.getValue())
								  || instanceBO.getState().equalsIgnoreCase(
										  InstanceStateEnum.SHUTDOWN.getValue()) || instanceBO
										  .getState().equalsIgnoreCase(
												  InstanceStateEnum.STOPPED.getValue()))) {
			  if (instanceBO.getProviderType().equals(ProviderTypeEnum.AMAZON)) {
				  instanceBO.setOperation(InstancesActionsEnum.START_INSTANCES.getValue());
			  } else {
				  instanceBO.setOperation(InstancesActionsEnum.POWERON.getValue());
			  }
			  instBOs.add(instanceBO);
			  resultList = runOperation(instBOs);
			  if (resultList.get(0).getErrors() != null
					  && resultList.get(0).getErrors().hasErrors()) {
				  return true;
			  }
		  }
		  return false;

	  }    

	  /**
	   * Method to reconfigure the VM Settings for storage
	   *
	   * @param T
	   *            BaseBO
	   * @throws ICException
	   */
	  public List<InstanceBO> reconfigureVMStorage(List<InstanceBO> instanceBOs)
			  throws ICException {
		  //		List<InstanceBO> instanceBOs = new ArrayList<InstanceBO>();
		  slfacade = SLInstancesFacade.SINGLETON_INSTANCE;
		  List<WorkerQueueBO> workerQueueObjects = null;
		  try {
			  logger.debug("Populated WQ from BO - reconfigureVMStorage");
			  if (ProviderTypeEnum.VMWARE == instanceBOs.get(0).getProviderType()) {
				  for(InstanceBO instanceBO : instanceBOs) {
					  workerQueueObjects = populateDiskBOforWQ(instanceBO,
							  instanceBO.getDiskUsage());
					  //			instanceBOs.add(instanceBO);
					  // Calling the Asynchronous function to create the WQ object and
					  // proceed further for processing
					  proceedForReconfigureVMStorage(instanceBOs, workerQueueObjects,
							  instanceBO);
				  }
			  } else if (ProviderTypeEnum.SAVVIS == instanceBOs.get(0).getProviderType()
					  || ProviderTypeEnum.SAVVISVPDC == instanceBOs.get(0).getProviderType()) {
				  for (InstanceBO instanceBO : instanceBOs) {
					  //CO-11303: Using concatenated name for WQ to avoid confusion with same name VMs
					  instanceBO.setWqTarget(StringUtils.getDisplayInstanceAndNickName(
							  instanceBO.getInstanceName(), instanceBO.getNickName()));
					  instanceBO.setWqTargetId(instanceBO.getInstanceId());
					  instanceBO.setWqTargetType(EntityTypeEnum.INSTANCE.getId());
					  List<WorkerQueueBO> workerQueueBOs = new ArrayList<>();
					  workerQueueBOs = WorkerQueueHelper
							  .buildInitialWorkerQueueObjectList(instanceBOs, TaskDescriptionEnum.UPDATE_STORAGE);
					  workerQueueBOs = WorkerQueueManager
							  .updateInitialWorkerQueueStatus(workerQueueBOs);
					  populateOrgId(instanceBO);
					  IPlatformsDomain domain = new PlatformsDomainImpl();
					  PlatformBO platformBO = domain.fetchProvider(PlatformsProvider
							  .getInputPlatformBO(instanceBO));
					  CloudCredentialsBO cloudCredentialsBO = PlatformsProvider
							  .getProviderCredentials(platformBO);
					  AsyncNotifiable<InstanceDomainImpl> asyncNotifiable = new AsyncInstanceStorageNotifyImpl<>(this, instanceBO, workerQueueBOs);
					  proceedReconfigureVMStorage(instanceBO, cloudCredentialsBO, asyncNotifiable);
				  }
			  }
			  logger.debug("Successfully completed - next step to update the DB");

		  } finally {
			  logger.info("Lets see the status...");
		  }
		  return instanceBOs;

	  }

	  /**
	   * Method to retrieve VM Storage / disk
	   *
	   * @param T
	   *            BaseBO
	   * @throws ICException
	   */
	  public InstanceBO getAllVMDisks(InstanceBO instanceBO)
			  throws ICException {
		  StringBuilder result = new StringBuilder("Error ");
		  slfacade = SLInstancesFacade.SINGLETON_INSTANCE;
		  try {
			  IPlatformsDomain domain = new PlatformsDomainImpl();
			  PlatformBO platformBO = domain.fetchProvider(PlatformsProvider
					  .getInputPlatformBO(instanceBO));
			  if(ProviderTypeEnum.SAVVIS == instanceBO.getProviderType()
					  || ProviderTypeEnum.SAVVISVPDC == instanceBO.getProviderType()) {
				  populateOrgId(instanceBO);
			  }
			  instanceBO = slfacade.getAllVMDisks(instanceBO,
					  PlatformsProvider.getProviderCredentials(platformBO));
			  logger.debug("Successfully completed - next step to update the DB");

		  } catch (SLException e) {
			  result.append(e.getMessage());
			  throw new ICException(result.toString());
		  }
		  return instanceBO;
	  }

	  /**
	   * Method to store the VM's CPU and Memory
	   *
	   * @param T
	   *            BaseBO
	   * @throws ICException
	   */
	  @Transactional
	  public void updateDiskDetails(InstanceBO instanceBO) throws ICException {
		  InstanceDetailsDO instDO = null;
		  try {
			  InstanceDetailsDAO instanceDetailsDAO = getFactory()
					  .getInstanceDetailsDAO();
			  instDO = instanceDetailsDAO.get(InstanceDetailsDO.class,
					  instanceBO.getInstanceId());

			  if (null != instDO && !MessageStringConstants.FAILURE.equalsIgnoreCase(instanceBO.getTaskStatus())) {
				  if(instanceBO.getProviderType() == ProviderTypeEnum.VMWARE) {
					  updateDiskUsageBOs(instDO, instanceBO, instanceDetailsDAO);
				  } else if (instanceBO.getProviderType() == ProviderTypeEnum.SAVVIS
						  || instanceBO.getProviderType() == ProviderTypeEnum.SAVVISVPDC) {
					  updateVirtualDiskBOs(instDO, instanceBO, instanceDetailsDAO);
				  }
			  } else {
				  logger.warn("Unable to find the instance -- "
						  + instanceBO.getInstanceName());
			  }
		  }catch (Exception e) {
			  try {
				  reportInstanceUpdateSpecificError(e);
			  } catch (DALException e1) {
				  throw new ICException(e1.getMessage());
			  }
			  throw new ICException(e.getMessage());
		  }
	  }

	  @Transactional
	  public InstanceDetailsDO updateVirtualDiskBOs(InstanceDetailsDO instDO, InstanceBO instanceBO, InstanceDetailsDAO instanceDetailsDAO) throws DALException {
		  InstanceDetailsDO finalDO = null;
		  List<VirtualDiskBO> virtualDiskBOs = instanceBO.getDisks();
		  Set<VmDiskDetialsDO> vmDiskDOs = instDO.getVmDiskDetialsDO();
		  if(virtualDiskBOs != null) {
			  //Update disk details here.
			  //TODO: Will update the details here once we have the refresh set the data.
			  double totalDS = 0;
			  for (VirtualDiskBO virtualDiskBO : virtualDiskBOs) {
				  if(InstanceStorageActionEnum.ADD == virtualDiskBO.getStorageActionEnum()
						  || InstanceStorageActionEnum.UPDATE == virtualDiskBO.getStorageActionEnum()) {
					  totalDS += virtualDiskBO.getVdSize();
				  }
			  }
			  if (totalDS > 0) {
				  instDO.setAllocatedDiskSize(totalDS);
				  instDO.setUpdatedByUser(instanceBO.getUserID());
				  instDO.setOperation(instanceBO.getOperation());
				  finalDO = instanceDetailsDAO.saveOrUpdate(instDO);
				  logger.debug("Update of Instance Resource is successful");
			  }
		  } else {
			  logger.warn("No disks found");
		  }
		  return finalDO;
	  }

	  @Transactional
	  public InstanceDetailsDO updateDiskUsageBOs(InstanceDetailsDO instDO, InstanceBO instanceBO, InstanceDetailsDAO instanceDetailsDAO) throws DALException {
		  InstanceDetailsDO finalDO = null;
		  double totalDS = 0;
		  List<DiskUsageBO> diskBOList = instanceBO.getDiskUsage();
		  if(diskBOList != null) {
			  for (DiskUsageBO diskUsageBO : diskBOList) {
				  if (diskUsageBO.getErrors() == null) {
					  if (diskUsageBO
							  .getStorageActionEnum()
							  .getTypeDesc()
							  .equalsIgnoreCase(
									  InstanceStorageActionEnum.ADD
									  .getTypeDesc())) {
						  totalDS = instDO.getAllocatedDiskSize()
								  + diskUsageBO.getSize();
					  } else if (diskUsageBO
							  .getStorageActionEnum()
							  .getTypeDesc()
							  .equalsIgnoreCase(
									  InstanceStorageActionEnum.UPDATE
									  .getTypeDesc())) {
						  totalDS = instDO.getAllocatedDiskSize()
								  + diskUsageBO.getSize()
								  - diskUsageBO.getOldDiskSize();
					  } else if (diskUsageBO
							  .getStorageActionEnum()
							  .getTypeDesc()
							  .equalsIgnoreCase(
									  InstanceStorageActionEnum.DETACH
									  .getTypeDesc())) {
						  totalDS = instDO.getAllocatedDiskSize()
								  - diskUsageBO.getSize();
					  }
					  // Updating the Power ON State for the VM
					  if ((null != instanceBO.getFutureState())
							  && (!instanceBO.getFutureState().getValue()
									  .equalsIgnoreCase(instDO.getState()))) {
						  logger.debug("New state for the VM is {}",
								  instanceBO.getFutureState().getValue());
						  instDO.setState(instanceBO.getFutureState()
								  .getValue());
					  }
					  if (totalDS > 0) {
						  instDO.setAllocatedDiskSize(totalDS);
						  instDO.setUpdatedByUser(instanceBO.getUserID());
						  instDO.setOperation(instanceBO.getOperation());
						  finalDO = instanceDetailsDAO.saveOrUpdate(instDO);
						  logger.debug("Update of Instance Resource is successful");
					  }
				  }
			  }
		  } else {
			  logger.warn("No disks found");
		  }
		  return finalDO;
	  }

	  /**
	   * Method to notify sms/email while terminating instances
	   *
	   * @param T
	   *            BaseBO
	   * @throws ICException
	   */
	  @Transactional
	  public <T extends BaseBO> void notifyOnEditResources(T instanceInfo) throws ICException {
		  // JIRA CO-5603
		  cdf = CloudOneDAOFactoryBuilder.getFactory();
		  if (instanceInfo != null) {
			  String messageBeanType = instanceInfo.getClass().getName();
			  OrganizationBO organizationBO = ICUtils
					  .getCloudOrganisationDetails(instanceInfo);
			  if (organizationBO != null) {
				  instanceInfo.setCloudName(organizationBO.getCloudName());
				  if (organizationBO.getOrganisationName() != null) {
					  instanceInfo.setOrganisationName(organizationBO
							  .getOrganisationName());
				  }
			  }
			  CloudOneErrors errors = instanceInfo.getErrors();
			  String status = (null != errors && errors.hasErrors() ? MessageStringConstants.FAILED
					  : MessageStringConstants.COMPELTE);
			  try {
				  EditVMResourceBean editVMBean = null;
				  if (messageBeanType
						  .equalsIgnoreCase(InstanceBO.class.getName())) {
					  InstanceBO instanceBO = (InstanceBO) instanceInfo;
					  CloudUserCredentialDAO dao = cdf
							  .getCloudUserCredentialDAO();
					  int instAssigneeId = instanceBO.getUserID();
					  CloudUserCredentialsDO instOwnerDO = dao.get(CloudUserCredentialsDO.class, instAssigneeId);
					  List<String> emailList = null;
					  if (instOwnerDO == null) {
						  //If user is not found, then at least notify Managed Service users
						  emailList = getMSUserEmailIds(instanceBO.getProviderID());
					  } else {
						  String userType = instOwnerDO.getUserType();
						  if (UserTypeEnum.SYSUSER.name().equals(userType)) {
							  //For orphan VMs, send the notifications to all Managed Service users for the provider
							  emailList = getMSUserEmailIds(instanceBO.getProviderID());
						  } else {
							  //For an other VM owner, send the notification to the owner
							  emailList = new ArrayList<>();
							  emailList.add(instOwnerDO.getEmail());
							  logger.debug("instOwnerDO.getEmail {}",instOwnerDO.getEmail());
							  logger.debug("userID {}",instOwnerDO.getUserId());
						  }
					  }
					  instanceBO
					  .setOperation(InstancesProvisioningConstants.EMAIL_EDIT_RESOURCES);
					  editVMBean = InstanceProvider.populateEditVMBeanFromBO(
							  instanceBO, status);
					  editVMBean.setName(nameInNotification(instanceBO));
					  editVMBean.setEmailAddressList(emailList);
				  }
				  if (editVMBean != null) {
					  if (editVMBean.getEmailAddressList() != null
							  && editVMBean.getEmailAddressList().size() > 0) {
						  sendNotification(editVMBean);
					  }
				  }
			  } catch (ICException icException) {
				  List<T> instanceBOs = new ArrayList<>();
				  instanceBOs.add(instanceInfo);
				  populateErrorMessages(instanceBOs, icException);
			  }
		  }
		  // JIRA CO-1687
	  }

	  private  void sendNotification(EditVMResourceBean editVMBean)
			  throws ICException {
		  // have a factory here
		  INotification<EditVMResourceBean> notificationDomain = NotificationImplFactory
				  .createNotificationImpl(editVMBean);
		  try {
			  notificationDomain.sendNotifications(editVMBean);
		  } catch (NotificationException e) {
			  throw new ICException(ErrorConstants.CO_IC_ERR_0065.getCode());
		  }
	  }

	  /*************************************************************************************************
	   * Only meant for Reconfigure VM Disk Storage.
	   *
	   * Arun K - Different jobs to show up in WQ based on nature of Disk
	   * Operation, VIZ, Add, Update or Detach. Hence the workaround below.
	   *
	   * @param InstanceBO
	   * @param List
	   *            <DiskUsageBO>
	   * @return
	   **************************************************************************************************/
	  private List<WorkerQueueBO> populateDiskBOforWQ(InstanceBO instanceBO, List<DiskUsageBO> diskUsageBOs) {
		  List<WorkerQueueBO> workerQueueBOs = new ArrayList<>();
		  List<WorkerQueueBO> finalQueueBOs = new ArrayList<>();
		  List<DiskUsageBO> tempList = new ArrayList<>();
		  int counter = 0;
		  try {
			  for (DiskUsageBO diskUsageBO : diskUsageBOs) {
				  String diskName = "";
				  if ((diskUsageBO.getDiskName() == null)
						  || ("".equals(diskUsageBO.getDiskName()))) {
					  diskName = "New Disk";
				  } else {
					  diskName = diskUsageBO.getDiskName();
				  }
				  diskUsageBO.setCloudId(instanceBO.getCloudId());
				  diskUsageBO.setWqTarget(diskName + ","
						  + instanceBO.getNickName());
				  instanceBO.setWqTargetId(diskUsageBO.getDiskID());
				  instanceBO.setWqTargetType(EntityTypeEnum.DISK.getId());
				  diskUsageBO.setWqTargetDeployment(instanceBO.getDeploymentName());
				  diskUsageBO.setWqTargetGroup(instanceBO.getInstGroupName());

				  diskUsageBO.setUserID(instanceBO.getUserID());
				  tempList.add(diskUsageBO);
				  workerQueueBOs = WorkerQueueHelper
						  .buildInitialWorkerQueueObjectList(tempList,
								  InstanceProvider
								  .getTaskDescForStorage(diskUsageBO));
				  finalQueueBOs.addAll(workerQueueBOs);
				  workerQueueBOs.clear();
				  tempList.clear();
			  }

			  finalQueueBOs = WorkerQueueManager
					  .updateInitialWorkerQueueStatus(finalQueueBOs);

			  for (WorkerQueueBO workerQueueBO : finalQueueBOs) {
				  DiskUsageBO diskUsageBO = diskUsageBOs.get(counter);
				  diskUsageBO.setWqTaskID(workerQueueBO.getCurrentTaskKey());
				  diskUsageBOs.set(counter, diskUsageBO);
				  counter++;
			  }
		  } catch (ICException e) {
			  logger.error(e);
		  }
		  return finalQueueBOs;
	  }

	  protected List<InstanceBO> proceedForReconfigureVMStorage(List<InstanceBO> instanceBOs, List<WorkerQueueBO> workerQueueObjects, InstanceBO instanceBO) throws ICException {
		  CloudCredentialsBO providerCredentials = null;
		  CloudOneError error;
		  CloudOneErrors cloudOneErrors;
		  IPlatformsDomain domain = new PlatformsDomainImpl();
		  List<DiskUsageBO> diskUsageBOs = new ArrayList<>();
		  DiskUsageBO diskUsageBO = new DiskUsageBO();
		  try {
			  PlatformBO platformBO = domain.fetchProvider(PlatformsProvider
					  .getInputPlatformBO(instanceBO));
			  providerCredentials = PlatformsProvider
					  .getProviderCredentials(platformBO);
			  AsyncNotifiable<InstanceDomainImpl> asyncNotifiable = new AsyncInstanceStorageNotifyImpl<>(this, instanceBO, workerQueueObjects);
			  instanceBOs = proceedReconfigureVMStorage(instanceBO,
					  providerCredentials, asyncNotifiable);
		  } catch (ICException ex) {
			  logger.error(ex.getMessage());
			  error = new CloudOneError();
			  cloudOneErrors = new CloudOneErrors();
			  error.setErrorCode(ErrorConstants.CO_IC_ERR_0145.getCode());
			  error.setErrorMessage(ex.getMessage());
			  cloudOneErrors.addError(error);
			  diskUsageBO = instanceBO.getDiskUsage().get(0);
			  if (null != diskUsageBO) {
				  diskUsageBOs.add(diskUsageBO);
				  populateErrorMessages(diskUsageBOs, ex);
				  // updateWorkerQueueDetailsforVMStorage(diskUsageBOs,workerQueueObjects,instanceBO);
				  throw new ICException(ex.getMessage());
			  }
		  }
		  return instanceBOs;
	  }

	  public List<InstanceBO> proceedReconfigureVMStorage(
			  InstanceBO instanceBO, BaseCredentialsBO credentialsBO, AsyncNotifiable asyncNotifiable)
					  throws ICException {
		  List<InstanceBO> instanceBOList = new ArrayList<>();
		  try {
			  instanceBOList.add(SLInstancesFacade.SINGLETON_INSTANCE
					  .reconfigureVMStorage(instanceBO, credentialsBO,
							  asyncNotifiable));
		  } catch (SLException e) {
			  logger.error("Exception while Reconfiguring the Virtual Machne - "
					  + instanceBO.getNickName());
			  throw new ICException(e.getMessage());
		  }
		  return instanceBOList;
	  }

	  public void updateWorkerQueueDetailsforVMStorage(List<WorkerQueueBO> workerQueueObjects, InstanceBO instanceBO)
			  throws ICException {
		  logger.debug(" In the updateWorkerQueueDetails --- >>> ");
		  InstanceBO tempInstanceBO = instanceBO;
		  try {
			  if (ProviderTypeEnum.SAVVIS == instanceBO.getProviderType()
					  || ProviderTypeEnum.SAVVISVPDC == instanceBO.getProviderType()) {
				  List<InstanceBO> instanceBOs = new ArrayList<>();
				  instanceBOs.add(instanceBO);
				  workerQueueObjects = WorkerQueueHelper
						  .buildFinalWorkerQueueObjectList(ICUtils.setWQTaskID(
								  instanceBOs, workerQueueObjects),
								  TaskDescriptionEnum.UPDATE_STORAGE);
				  WorkerQueueManager.updateFinalWorkerQueueStatus(workerQueueObjects);
			  } else {
				  workerQueueObjects = WorkerQueueHelper
						  .buildFinalWorkerQueueObjectList(instanceBO.getDiskUsage(),
								  InstanceProvider
								  .getTaskDescForStorage(instanceBO.getDiskUsage()
										  .get(0)));
				  WorkerQueueManager.updateFinalWorkerQueueStatus(workerQueueObjects);
				  tempInstanceBO.setDiskUsage(instanceBO.getDiskUsage());
				  tempInstanceBO.setErrors(instanceBO.getDiskUsage().get(0).getErrors());
			  }
			  logger.debug("About to send the notification for the Instance - {}",
					  tempInstanceBO.getNickName());
			  notifyOnEditStorage(tempInstanceBO);
		  } catch (ICException ex) {
			  logger.error("Error while updating the WQ Status");
			  throw new ICException(ex.getMessage());
		  }
	  }

	  /**
	   * Method to notify sms/email while terminating instances
	   *
	   * @param T
	   *            BaseBO
	   * @throws ICException
	   */
	  @Transactional
	  public <T extends BaseBO> void notifyOnEditStorage(
			  T instanceInfo)
					  throws ICException {
		  // JIRA CO-5603
		  cdf = CloudOneDAOFactoryBuilder.getFactory();
		  if (instanceInfo != null) {
			  String messageBeanType = instanceInfo.getClass().getName();
			  OrganizationBO organizationBO = ICUtils
					  .getCloudOrganisationDetails(instanceInfo);
			  if (organizationBO != null) {
				  instanceInfo.setCloudName(organizationBO.getCloudName());
				  if (organizationBO.getOrganisationName() != null) {
					  instanceInfo.setOrganisationName(organizationBO
							  .getOrganisationName());
				  }
			  }
			  CloudOneErrors errors = instanceInfo.getErrors();
			  String status = (null != errors && errors.hasErrors() ? MessageStringConstants.FAILED
					  : MessageStringConstants.COMPELTE);
			  try {
				  EditVMResourceBean editVMBean = null;
				  if (messageBeanType
						  .equalsIgnoreCase(InstanceBO.class.getName())) {
					  InstanceBO instanceBO = (InstanceBO) instanceInfo;
					  CloudUserCredentialDAO dao = cdf
							  .getCloudUserCredentialDAO();
					  int userID = instanceBO.getUserID();
					  CloudUserCredentialsDO instOwnerDO = dao.get(CloudUserCredentialsDO.class, userID);
					  List<String> emailList = null;
					  if (instOwnerDO == null) {
						  //If user is not found, then at least notify Managed Service users
						  emailList = getMSUserEmailIds(instanceBO.getProviderID());
					  } else {
						  String userType = instOwnerDO.getUserType();
						  if (UserTypeEnum.SYSUSER.name().equals(userType)) {
							  //For orphan VMs, send the notifications to all Managed Service users for the provider
							  emailList = getMSUserEmailIds(instanceBO.getProviderID());
						  } else {
							  //For an other VM owner, send the notification to the owner
							  emailList = new ArrayList<>();
							  emailList.add(instOwnerDO.getEmail());
							  logger.debug("instOwnerDO.getEmail {}",instOwnerDO.getEmail());
							  logger.debug("userID {}",instOwnerDO.getUserId());
						  }
					  }
					  instanceBO
					  .setOperation(InstancesProvisioningConstants.EMAIL_EDIT_STORAGE);
					  editVMBean = InstanceProvider.populateEditVMBeanFromBO(
							  instanceBO, status);
					  editVMBean.setName(nameInNotification(instanceBO));
					  editVMBean.setEmailAddressList(emailList);
				  }
				  if (editVMBean != null) {
					  if (editVMBean.getEmailAddressList() != null
							  && editVMBean.getEmailAddressList().size() > 0) {
						  sendNotification(editVMBean);
					  }
				  }
			  } catch (ICException icException) {
				  List<T> instanceBOs = new ArrayList<>();
				  instanceBOs.add(instanceInfo);
				  populateErrorMessages(instanceBOs, icException);
			  }
		  }
		  // JIRA CO-1687
	  }

	  // JIRA CO-6415

	  @Transactional
	  public  String fetchGroupNamefromGroupId(CreateInstanceInfoBO createInstanceInfoBO) {
		  InstanceGroupMasterDO instanceGroupMasterDO = new InstanceGroupMasterDO();
		  InstanceGroupMasterDAO instanceGroupMasterDAO = getFactory()
				  .getInstanceGroupMasterDAO();
		  instanceGroupMasterDO = instanceGroupMasterDAO.get(
				  InstanceGroupMasterDO.class,
				  Integer.parseInt(createInstanceInfoBO.getGroup()));
		  return instanceGroupMasterDO.getGroupName();
	  }
	  @Transactional
	  public String fetchDeploymentNamefromDeploymentId(
			  CreateInstanceInfoBO createInstanceInfoBO) {
		  DeploymentMasterDAO dao = getFactory().getDeploymentMasterDAO();
		  DeploymentMasterDO deploymentMasterDO = dao.get(
				  DeploymentMasterDO.class,
				  Integer.parseInt(createInstanceInfoBO.getDeployment()));
		  return deploymentMasterDO.getDeploymentName();
	  }

	  @Transactional
	  public String getImageNickName(
			  InstanceBO instanceBO) throws ICException {
		  // Boolean variable to be used for iterating
		  CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		  ImageDetailsDAO imageDetailsDAO = factory.getImageDetailsDAO();
		  boolean flag = true;
		  // Counter: to be appended to the instance name, if need be
		  int ctr = 1;
		  String instanceName = instanceBO.getNickName();
		  SimpleDateFormat format = new SimpleDateFormat("MMMd-yyyy");
		  String dateString = format.format(new Date());
		  String newInstanceName = instanceName + InstancesProvisioningConstants.NAME_DELIMITER + dateString;
		  List<ImageDetailsDO> imageDetailsDOList = null;
		  try {
			  imageDetailsDOList = imageDetailsDAO
					  .getImageDetailsByImageName(newInstanceName);
		  } catch (DALException e) {
			  throw new ICException(e.getMessage());
		  }
		  Map<String, Object> nameLookupMap = new HashMap<>();
		  if (null != imageDetailsDOList && !imageDetailsDOList.isEmpty()) {
			  for (ImageDetailsDO imageDetailsDO : imageDetailsDOList) {
				  nameLookupMap.put(imageDetailsDO.getName(), imageDetailsDO);
			  }
			  while (flag) {
				  if (nameLookupMap.containsKey(newInstanceName)) {
					  newInstanceName = instanceName + InstancesProvisioningConstants.NAME_DELIMITER
							  + dateString + InstancesProvisioningConstants.NAME_DELIMITER + ctr;
				  } else {
					  instanceBO.setNewNickName(newInstanceName);
					  flag = false;
				  }
				  ctr++;
			  }
		  }
		  return newInstanceName;
	  }

	  @Transactional
	  public InstanceBO fetchUpdatedInstance(InstanceBO instanceBO) throws ICException {
		  CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		  InstanceDetailsDAO instanceDetailsDAO = factory.getInstanceDetailsDAO();
		  InstanceDetailsDO instanceDetailsDO = new InstanceDetailsDO();
		  instanceDetailsDO.setInstanceID(instanceBO.getInstanceId());
		  List<InstanceDetailsDO> instancedetails = null;
		  InstanceBO instBO = null;
		  try {
			  try {
				  instancedetails = instanceDetailsDAO
						  .fetchByInstanceId(instanceDetailsDO);
			  } catch (DALException e) {
				  logger.info("No instance with this criteria : fetchUpdatedInstance");
			  }
			  if (null != instancedetails && !instancedetails.isEmpty()) {
				  InstanceDetailsDO instDetailsDO = instancedetails.get(0); // Only
				  // one
				  // record
				  // exists
				  // for
				  // given
				  // instance
				  // id.
				  instBO = InstanceProvider.getBOFromDO(instDetailsDO);
			  }
		  } catch (Exception e) {
			  logger.info("No instance with this criteria : fetchUpdatedInstance");
		  }
		  return instBO;
	  }

	  /**
	   * Method to get the instance details from cloud and update the info in
	   * database for the specific instances.
	   */
	  @Transactional
	  public List<InstanceBO> updateInstanceDetailsFromCloud(List<InstanceBO> instanceBOs)
			  throws ICException {
		  List<InstanceBO> instBOs = null;
		  try {
			  if (instanceBOs != null && !instanceBOs.isEmpty()){
				  instBOs = new ArrayList<InstanceBO>();
				  IPlatformsDomain domain = new PlatformsDomainImpl();
				  for (InstanceBO instanceBO:instanceBOs){
					  List<InstanceBO> listForCloud = new ArrayList<>();
					  listForCloud.add(instanceBO);
					  PlatformBO platformBO = domain.fetchProvider(PlatformsProvider
							  .getInputPlatformBO(instanceBO));
					  slfacade = SLInstancesFacade.SINGLETON_INSTANCE;
					  instBOs.addAll(slfacade.getInstanceDetailsFromCloud(listForCloud,
							  PlatformsProvider.getProviderCredentials(platformBO)));
				  }
				  if (null != instBOs && !instBOs.isEmpty()) {
					  InstanceDetailsDAO instDAO = getFactory()
							  .getInstanceDetailsDAO();
					  List<InstanceDetailsDO> instanceDOs = new ArrayList<>(instBOs.size());
					  for (InstanceBO instBO : instBOs) {
						  InstanceDetailsDO instanceDO = instDAO.get(
								  InstanceDetailsDO.class, instBO.getInstanceId());
						  instanceDO.setIpAddress(instBO.getSystemAddress());
						  instanceDO.setPrivate_IP(instBO.getPrivateIPAddress());
						  instanceDO.setUpdatedByUser(instBO.getUserID());
						  instanceDO.setState(instBO.getState());
						  instanceDOs.add(instanceDO);
					  }
					  instDAO.saveOrUpdateBatch(instanceDOs);
				  }
			  }
		  } catch (SLException | DALException e) {
			  throw new ICException(e);
		  }
		  return instBOs;
	  }

	  public List<InstanceBO> getInstanceDetailsFromCloud(List<InstanceBO> instanceBOs)
			  throws ICException {
		  List<InstanceBO> instBOs = null;
		  try {
			  IPlatformsDomain domain = new PlatformsDomainImpl();
			  PlatformBO platformBO = domain.fetchProvider(PlatformsProvider
					  .getInputPlatformBO(instanceBOs.get(0)));
			  slfacade = SLInstancesFacade.SINGLETON_INSTANCE;
			  instBOs = slfacade.getInstanceDetailsFromCloud(instanceBOs,
					  PlatformsProvider.getProviderCredentials(platformBO));
		  } catch (SLException e) {
			  throw new ICException(e);
		  }
		  return instBOs;
	  }

	  public List<String> buildInstanceTypeList(String architecture, String rootDevice, String virtualizationType, boolean buildStaticList) {

		  List<String> instanceTypes = new ArrayList<>();
		  Set<Integer> instanceTypesOrder = new HashSet<>();

		  if (architecture != null && rootDevice != null && virtualizationType != null && !buildStaticList) {
			  if (AMIDetailsEnum.AMI_ARCH_I386.getID().equals(
					  architecture)) {
				  if (AMIDetailsEnum.AMI_ROOTDEVICE_EBS.getID().equalsIgnoreCase(
						  rootDevice)) {
					  instanceTypesOrder.add(InstanceTypesEnum.T1_MICRO.getOrderId());
				  }
				  instanceTypesOrder.add(InstanceTypesEnum.M1_SMALL.getOrderId());
				  instanceTypesOrder.add(InstanceTypesEnum.M1_MEDIUM.getOrderId());
				  instanceTypesOrder.add(InstanceTypesEnum.C1_MEDIUM.getOrderId());
			  } else if (AMIDetailsEnum.AMI_ARCH_X86_64.getID().equals(
					  architecture)) {
				  if (AMIDetailsEnum.AMI_ROOTDEVICE_EBS.getID().equalsIgnoreCase(
						  rootDevice)) {
					  instanceTypesOrder.add(InstanceTypesEnum.T1_MICRO.getOrderId());
					  instanceTypesOrder.add(InstanceTypesEnum.M3_XLARGE.getOrderId());
					  instanceTypesOrder.add(InstanceTypesEnum.M3_2XLARGE.getOrderId());
				  }
				  instanceTypesOrder.add(InstanceTypesEnum.M3_MEDIUM.getOrderId());
				  instanceTypesOrder.add(InstanceTypesEnum.M3_LARGE.getOrderId());
				  instanceTypesOrder.add(InstanceTypesEnum.M1_SMALL.getOrderId());
				  instanceTypesOrder.add(InstanceTypesEnum.M1_MEDIUM.getOrderId());
				  instanceTypesOrder.add(InstanceTypesEnum.M1_LARGE.getOrderId());
				  instanceTypesOrder.add(InstanceTypesEnum.M1_XLARGE.getOrderId());
				  instanceTypesOrder.add(InstanceTypesEnum.M2_XLARGE.getOrderId());
				  instanceTypesOrder.add(InstanceTypesEnum.M2_2XLARGE.getOrderId());
				  instanceTypesOrder.add(InstanceTypesEnum.M2_4XLARGE.getOrderId());
				  instanceTypesOrder.add(InstanceTypesEnum.C1_MEDIUM.getOrderId());
				  instanceTypesOrder.add(InstanceTypesEnum.C1_XLARGE.getOrderId());
				  instanceTypesOrder.add(InstanceTypesEnum.C3_LARGE.getOrderId());
				  instanceTypesOrder.add(InstanceTypesEnum.C3_XLARGE.getOrderId());
				  instanceTypesOrder.add(InstanceTypesEnum.C3_2XLARGE.getOrderId());
				  instanceTypesOrder.add(InstanceTypesEnum.C3_4XLARGE.getOrderId());
				  instanceTypesOrder.add(InstanceTypesEnum.C3_8XLARGE.getOrderId());
				  if (AMIDetailsEnum.VIRTUALIZATIONTYPE_HVM
						  .getID()
						  .equalsIgnoreCase(virtualizationType)) {
					  instanceTypesOrder.add(InstanceTypesEnum.HI1_4XLARGE.getOrderId());
					  instanceTypesOrder.add(InstanceTypesEnum.G2_2XLARGE.getOrderId());
					  instanceTypesOrder.add(InstanceTypesEnum.I2_XLARGE.getOrderId());
					  instanceTypesOrder.add(InstanceTypesEnum.I2_2XLARGE.getOrderId());
					  instanceTypesOrder.add(InstanceTypesEnum.I2_4XLARGE.getOrderId());
					  instanceTypesOrder.add(InstanceTypesEnum.I2_8XLARGE.getOrderId());

				  } else if (AMIDetailsEnum.VIRTUALIZATIONTYPE_PARAVIRTUAL
						  .getID().equalsIgnoreCase(virtualizationType)) {
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


	  @Transactional
	  public List<String> fetchNickNamesFromWQByCloudId(
			  int cloudId) {
		  if (cloudId == 0) {
			  return new ArrayList<>();
		  }
		  CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
		  TaskDetailsDAO taskDetailsDAO = factory.getTaskDetailsDAO();
		  CloudMasterDO cloudMasterDO = taskDetailsDAO.get(CloudMasterDO.class,
				  cloudId);
		  TaskDetailsDO taskDetailsDO = new TaskDetailsDO();
		  taskDetailsDO.setCloudMasterDO(cloudMasterDO);
		  taskDetailsDO.setTaskDescriptionId(TaskDescriptionEnum.CREATE_INSTANCE.getId());
		  List<TaskDetailsDO> taskdetailsList = null;
		  List<String> nickNameList = new ArrayList<>();
		  try {
			  taskdetailsList = taskDetailsDAO.fetchByCloudIdTaskDescriptionIdExcludingStatus(taskDetailsDO,MessageStringConstants.FAILED);
			  if(taskdetailsList != null && !taskdetailsList.isEmpty()) {
				  for(TaskDetailsDO task : taskdetailsList) {
					  nickNameList.add(task.getTarget());
				  }
			  }
		  } catch (DALException e) {
			  logger.error("Error in accessing data from WQ for duplicate check Based on cloud: {}",e.getMessage());
		  }
		  return nickNameList;
	  }

	  public List<InstanceBO> scheduleAction(List<InstanceBO> instanceBOs, RecurrenceBO recurrenceBO) throws ICException {
		  List<WorkerQueueBO> wqBOs = null;
		  InstanceBO instanceBO = instanceBOs.get(0);
		  JobTypeEnum jobType = null;
		  TaskDescriptionEnum taskEnum = null;
		  String operation = instanceBO.getOperation();
		  logger.debug("scheduleAction ----> >>>> operation:"+operation);

		  if(InstanceStateEnum.POWERON.name().equalsIgnoreCase(operation)
				  || "START".equalsIgnoreCase(operation)) {
			  jobType = JobTypeEnum.POWERON_VM;
			  taskEnum = TaskDescriptionEnum.SCHEDULE_POWERON_VM;
		  } else if (InstanceStateEnum.POWEROFF.name().equalsIgnoreCase(operation)) {
			  jobType = JobTypeEnum.POWEROFF_VM;
			  taskEnum = TaskDescriptionEnum.SCHEDULE_POWEROFF_VM;
		  }else if ("STOP".equalsIgnoreCase(operation)) {
			  jobType = JobTypeEnum.SHUTDOWN_VM;
			  taskEnum = TaskDescriptionEnum.SCHEDULE_SHUTDOWN_VM;
		  } else if (InstanceStateEnum.SHUTDOWN.name().equalsIgnoreCase(operation)) {
			  if(ProviderTypeEnum.POWERVM == instanceBO.getProviderType()) {
				  jobType = JobTypeEnum.POWEROFF_VM;
				  taskEnum = TaskDescriptionEnum.SCHEDULE_POWEROFF_VM;
			  } else {
				  jobType = JobTypeEnum.SHUTDOWN_VM;
				  taskEnum = TaskDescriptionEnum.SCHEDULE_SHUTDOWN_VM;
			  }
		  } else if ("DELETE".equalsIgnoreCase(operation)
				  || "TERMINATE".equalsIgnoreCase(operation)) {
			  jobType = JobTypeEnum.DELETE_VM;
			  taskEnum = TaskDescriptionEnum.SCHEDULE_DELETE_VM;
		  } else if("ENABLEMONITORING".equalsIgnoreCase(operation)){
			  jobType = JobTypeEnum.ENABLE_VM_MONITORING;
			  taskEnum = TaskDescriptionEnum.SCHEDULE_ENABLE_MONITORING;
		  } else if("DISABLEMONITORING".equalsIgnoreCase(operation)){
			  jobType = JobTypeEnum.DISABLE_VM_MONITORING;
			  taskEnum = TaskDescriptionEnum.SCHEDULE_DISABLE_MONITORING;
		  }else if("REMOVE_MONITORING_PROFILE".equalsIgnoreCase(operation)){
			  jobType = JobTypeEnum.REMOVE_VM_MONITORING;
			  taskEnum = TaskDescriptionEnum.SCHEDULE_REMOVE_MONITORING;
		  }
		  else if("EDITRESOURCES".equalsIgnoreCase(operation) || "EDIT_AWS_INSTANCE_TYPE".equalsIgnoreCase(operation)
				  || "EDIT_SAVVIS_INSTANCE_RESOURCES".equalsIgnoreCase(operation)){
			  jobType = JobTypeEnum.MODIFY_RESOURCE;
			  taskEnum = TaskDescriptionEnum.SCHEDULE_MODIFY_RESOURCE;
		  }else if("EXECUTE_SCRIPT".equalsIgnoreCase(operation)){
			  jobType = JobTypeEnum.EXECUTE_SCRIPT;           
		  }else if("CONVERTTOIMAGE".equalsIgnoreCase(operation)){
			  jobType = JobTypeEnum.CONVERT_TO_IMAGE;
			  taskEnum = TaskDescriptionEnum.SCHEDULE_CONVERT_TO_IMAGE;
		  }else if("AZURECLONETOIMAGE".equalsIgnoreCase(operation)) {
			  jobType = JobTypeEnum.CONVERT_TO_IMAGE;
			  taskEnum = TaskDescriptionEnum.SCHEDULED_AZURE_CLONE_TO_IMAGE;			  
		  }else if("CONVERTTOIMAGEEXPIRE".equalsIgnoreCase(operation)){
			  jobType = JobTypeEnum.CONVERT_TO_IMAGE_EXPIRE;
		      taskEnum = TaskDescriptionEnum.SCHEDULE_CONVERTTOIMAGEEXPIRE;  
		  }else if("CREATE_IMAGE_TEMPLATE".equalsIgnoreCase(operation)){
			  jobType = JobTypeEnum.CREATE_IMAGE_TEMPLATE;
			  taskEnum = TaskDescriptionEnum.SCHEDULE_CREATE_IMAGE_TEMPLATE;
		  }else if("CREATE_FLEX_IMAGE".equalsIgnoreCase(operation)){
			  jobType = JobTypeEnum.CREATE_FLEX_IMAGE;
			  taskEnum = TaskDescriptionEnum.SCHEDULE_CREATE_FLEX_IMAGE;
		  }
		  else {
			  //Add other types here
		  }
		  for(InstanceBO instBO : instanceBOs) {
			  //CO-11303: Using concatenated name for WQ to avoid confusion with same name VMs
			  instBO.setWqTarget(StringUtils
					  .getDisplayInstanceAndNickName(
							  instBO.getInstanceName(),
							  instBO.getNickName()));
			  instBO.setWqTargetId(instBO.getInstanceId());
			  instBO.setWqTargetType(EntityTypeEnum.INSTANCE.getId());
			  if(jobType.equals(JobTypeEnum.EXECUTE_SCRIPT) && "serviceConsole".equalsIgnoreCase(instBO.getExecutionService())){
				  taskEnum=TaskDescriptionEnum.SCHEDULE_CUSTOM_SCRIPT_EXECUTION_SC;
			  }else if(jobType.equals(JobTypeEnum.EXECUTE_SCRIPT) && "computeInstance".equalsIgnoreCase(instBO.getExecutionService())){
				  taskEnum=TaskDescriptionEnum.SCHEDULE_CUSTOM_SCRIPT_EXECUTION_VM;
			  }else if(jobType.equals(JobTypeEnum.CONVERT_TO_IMAGE_EXPIRE)){
				  instBO.setWqTarget(instBO.getImageDescription());
			  }

		  }
		  wqBOs = WorkerQueueHelper.buildInitialWorkerQueueObjectList(instanceBOs,  taskEnum);
		  WorkerQueueManager.updateInitialWorkerQueueStatus(wqBOs);
		  try {
			  ScheduleDetailsBean scheduleDetailsBean = buildScheduleBeanFromRecurrenceDetails(recurrenceBO);
			  for (InstanceBO instBO : instanceBOs) {
				  List<Integer> includedEntities = new ArrayList<>();
				  includedEntities.add(instBO.getInstanceId());
				  String schedulingInfo = BeanBuilder
						  .buildScheduleInfo(scheduleDetailsBean);
				  InstanceDataBean instDataBean = InstanceProvider.getInstanceDataBeanFromInstanceBO(instBO);
				  // Added for CO-20043 - Scheduled De-Provisioning of Instances
				  if(taskEnum.getName().equalsIgnoreCase("SCHEDULE_DELETE_VM")){
					  int notificationJobID = scheduleDeProvNotifyJob(scheduleDetailsBean,instDataBean,recurrenceBO,instBO,includedEntities,wqBOs,instanceBOs);
					  instDataBean.setNotificationJobID(notificationJobID);
				  }	
				  JobDataBean jobDataBean = BeanBuilder.buildJobDataBean(
						  recurrenceBO.getCloudId(), jobType,
						  recurrenceBO.getProviderID(), recurrenceBO.getProviderType(),
						  recurrenceBO.getActiveUserGroupID(),
						  null,
						  jobType.name(),
						  recurrenceBO.getUserID(), SchedulerEntityTypeEnum.INSTANCE,
						  includedEntities, null, schedulingInfo, instBO.getNotificationID(), instDataBean,wqBOs.get(0).getCurrentTaskKey());
				  String[] jobExecDetails = JobMgmtUtil.getJobClassNameForType(jobType);
				  JobDetailsBean jobDetailsBean = BeanBuilder
						  .buildJobDetailsBean(
								  jobType.name(),
								  jobType.toString(),
								  jobType.getDescription(),
								  null,
								  jobExecDetails[1],
								  null, false, false, SchedulingEnvTypeEnum.valueOf(jobExecDetails[0]),
								  jobDataBean);
				  List<TriggerDetailsBean> triggerDetailsBeans = null;
				  if (scheduleDetailsBean.isReccurence()) {
					  triggerDetailsBeans = ScheduleBuilder
							  .buildRecurrence(jobDetailsBean,
									  jobType.toString(),
									  jobType.toString(), scheduleDetailsBean);
				  } else {
					  String startDt = scheduleDetailsBean.getRecurStartDate();
					  String startTime = scheduleDetailsBean.getRecurStartTime();
					  Date startDtTime = TimeZoneConverter.getDate(startDt+" "+startTime, scheduleDetailsBean.getTimeZone(), DateTimeFormatEnum.DTF_19);
					  triggerDetailsBeans = new ArrayList<>();
					  TriggerDetailsBean triggerDetailsBean = BeanBuilder.buildSimpleTriggerBean(jobType.toString(),
							  jobType.toString(), null, 0,
							  null, startDtTime, 0,
							  false, scheduleDetailsBean.getTimeZone());
					  triggerDetailsBeans.add(triggerDetailsBean);
				  }
				  ISchedulerService schedulerService = new SchedulerServiceImpl();
				  schedulerService.scheduleJob(jobDetailsBean, triggerDetailsBeans, recurrenceBO.getCloudId());			  
			  }
		  } catch (SchedulerException | ParseException e) {
			  instanceBOs = populateErrorMessages(instanceBOs, e);
		  } finally {
			  wqBOs = WorkerQueueHelper.buildFinalWorkerQueueObjectList(ICUtils.setWQTaskID(instanceBOs, wqBOs),  taskEnum);
			  WorkerQueueManager.updateFinalWorkerQueueStatus(wqBOs);
		  }
		  return instanceBOs;
	  }

	  //133053, for  CO-10542
	  @Transactional
	  public MonitorableBO changeInstanceIPAddress(MonitorableBO instanceBO, String ipChangeDetails) {
		  InstanceBO originalInstanceBO = null;
		  InstanceDetailsDAO instanceDetailsDAO = getFactory().getInstanceDetailsDAO();
		  try {
			  String newIp = getAddressFromIPChangeDetails(ipChangeDetails, false);
			  String oldIp = getAddressFromIPChangeDetails(ipChangeDetails, true);

			  originalInstanceBO = (InstanceBO) instanceBO;

			  //http://10.236.194.130/browse/CO-8898
			  if (oldIp != null && oldIp.equalsIgnoreCase( originalInstanceBO.getSystemAddress())) { //compare public IP
				  originalInstanceBO.setSystemAddress(newIp);
				  logger.error("updateInstanceIPAddress updated publicIP for {} to {}",originalInstanceBO.getInstanceId(),
						  originalInstanceBO.getSystemAddress());
			  } else {
				  //replace the old IP in the private IP list
				  String privateIPList = originalInstanceBO.getPrivateIPAddress();
				  privateIPList = replacePrivateIP (privateIPList, oldIp,  newIp );
				  originalInstanceBO.setPrivateIPAddress(privateIPList);
				  logger.error("updateInstanceIPAddress updated privateip for {} to {}",originalInstanceBO.getInstanceId(),
						  originalInstanceBO.getPrivateIPAddress());
			  }
			  InstanceDetailsDO instanceDO =  instanceDetailsDAO.get(InstanceDetailsDO.class, originalInstanceBO.getInstanceId());
			  instanceDO.setIpAddress( originalInstanceBO.getSystemAddress());
			  instanceDO.setPrivate_IP( originalInstanceBO.getPrivateIPAddress() );
			  instanceDetailsDAO.update(instanceDO);
		  } catch (Exception ex){
			  logger.error("updateInstanceIPAddress error", ex);
		  }
		  return originalInstanceBO ;
	  }

	  //133053, for  CO-10542
	  // look up instance details by key, which can be one of ID, name, or IP ADDRESS
	  @Transactional
	  public InstanceBO getInstanceDetailsByKey(InstanceBO instanceBO) {
		  InstanceBO resultBO = null;
		  InstanceDetailsDAO instDAO = getFactory().getInstanceDetailsDAO();
		  logger.debug("getting getInstanceDetailsBy Key {}", instanceBO==null? "null": instanceBO.getInstanceId()) ;
		  try {
			  CloudMasterDO cloudMasterDO = instDAO.get(CloudMasterDO.class, instanceBO.getCloudId());
			  if (instanceBO.getInstanceId()>0) {
				  //look up by ID
				  logger.info("Trying to find instance with instance ID and cloud id {} {}",instanceBO.getInstanceId(),instanceBO.getCloudId());
				  InstanceDetailsDO instDO = instDAO.get(InstanceDetailsDO.class, instanceBO.getInstanceId());
				  resultBO = instDO!=null && instDO.isActive() ? InstanceProvider.getBOFromDO(instDO) : null;
			  } else if (instanceBO.getSystemAddress()!=null && instanceBO.getSystemAddress().trim().length()>0) {
				  //look up by IP ADDRESS
				  logger.info("Trying to find instance with address and cloud id {} {}",instanceBO.getSystemAddress(),instanceBO.getCloudId());

				  InstanceDetailsDO  searchInstanceDetailsDO = new InstanceDetailsDO();
				  searchInstanceDetailsDO.setIpAddress(instanceBO.getSystemAddress()); //target
				  searchInstanceDetailsDO.setCloudMasterDO(cloudMasterDO);

				  InstanceDetailsDO instDO = instDAO.fetchByCloudIdIpAddress(searchInstanceDetailsDO);
				  resultBO = instDO!=null && instDO.isActive() ? InstanceProvider.getBOFromDO(instDO) : null;
			  } else {
				  //look up by name
				  logger.info("Trying to find instance with instance name and cloud id {} {}",instanceBO.getInstanceName(),instanceBO.getCloudId());
				  logger.info("provider id is : {}",instanceBO.getProviderID() ) ;
				  ProviderMasterDO providerMasterDO  =instDAO.get(ProviderMasterDO.class, instanceBO.getProviderID());
				  InstanceDetailsDO  searchInstanceDetailsDO = new InstanceDetailsDO();
				  searchInstanceDetailsDO.setProviderMasterDO(providerMasterDO) ;
				  searchInstanceDetailsDO.setInstanceName(instanceBO.getInstanceName() );
				  searchInstanceDetailsDO.setCloudMasterDO(cloudMasterDO);
				  List<InstanceDetailsDO> resultList = instDAO.fetchByCloudIdProviderIdInstanceName(searchInstanceDetailsDO);
				  if (resultList == null || resultList.isEmpty() || resultList.size()>1) {
					  // if there is more than 1 match, should throw APP Exception
					  String errorMessage = (resultList == null || resultList.isEmpty()) ? " no match found " : " multiple matches found";
					  logger.info("InstanceDetails getInstanceDetailsByKey by name error: {}",errorMessage);
				  } else {
					  InstanceDetailsDO instDO = resultList.get(0);
					  resultBO = instDO.isActive() ? InstanceProvider.getBOFromDO(instDO) : null;
				  }
			  }
		  } catch (DALException ex) {
			  logger.error("getInstanceDetailsByKey error ", ex);
		  }
		  if (resultBO != null) {
			  logger.info("getVMDetailsByKey found : {}",resultBO.getInstanceId());
			  TagMasterDO tagMasterDO = TagsHelper.getApplicationCopyTag(resultBO.getInstanceId());
			  if (tagMasterDO != null) {
				  try {
					  logger.info("tagMasterDO key : {}",tagMasterDO.getKey());
					  ApplicationCopyDAO applicationCopyDAO = CloudOneDAOFactoryBuilder.getFactory().getApplicationCopyDAO();
					  ApplicationCopyDO applicationCopyDO = new ApplicationCopyDO();
					  applicationCopyDO.setApplicationCopyID(Integer.parseInt(tagMasterDO.getKey()));

					  applicationCopyDO = applicationCopyDAO.fetchApplicationCopyByID(applicationCopyDO);
					  String applicationInstanceName = applicationCopyDO.getApplicationCopyName();
					  logger.info("applicationInstanceName  : {}",applicationInstanceName);
					  if(applicationInstanceName!=null && !applicationInstanceName.isEmpty()){
						  resultBO.setApplicationInstanceName(applicationInstanceName);
					  }
				  }
				  catch(NumberFormatException | DALException ex) {
					  logger.info("In getInstancesForAutomationFromCloud, error finding application details", ex.getMessage());
				  }
			  }
		  } else {
			  logger.info("getVMDetailsByKey no found : " );
		  }
		  return resultBO;
	  }	
	  	 

	  //133053, for  CO-10542
	  @Transactional
	  public List<String> getRecepeintsForUnexpectedShutdown(MonitorableBO monitorableBO) {
		  List<String> emailList = new ArrayList<>();
		  try {
			  InstanceBO instanceBO = (InstanceBO) monitorableBO;

			  //managed service user
			  UserTO managedUserTO = JobAuthHelper.fetchManagedServiceUsers(instanceBO.getProviderID());

			  if (managedUserTO != null){
				  emailList.add(managedUserTO.getEmail());
			  }

			  //VM assignee
			  logger.debug("Going to fetch assignee for the VM {}",instanceBO.getInstanceId());
			  InstanceDetailsDAO instDAO = getFactory().getInstanceDetailsDAO();
			  InstanceDetailsDO resultInstanceDetailsDO = instDAO.get(InstanceDetailsDO.class, instanceBO.getInstanceId());
			  if (resultInstanceDetailsDO.getCloudUserCredentialsDO() != null){
				  logger.debug("Assignee is {}",resultInstanceDetailsDO.getCloudUserCredentialsDO().getEmail());
				  emailList.add(resultInstanceDetailsDO.getCloudUserCredentialsDO().getEmail());
			  }else{
				  logger.debug("Assignee not found for the VM {}",instanceBO.getInstanceId());
			  }

			  for (String thisEmail :  emailList ){
				  logger.info("getRecepeintsForUnexpectedShutdown list : {}",thisEmail) ;
			  }

		  } catch (Exception ex) {
			  logger.error("erro in getRecepeintsForUnexpectedShutdown" , ex) ;
		  }
		  return emailList;
	  }	
	  private String replacePrivateIP(String privateIPList, String oldIp, String newIp) throws Exception {
		  // replace old IP with new IP, look for exact match
		  // privateIPList is a ';' separated list of IPs

		  String result = privateIPList ;

		  if (privateIPList!= null && privateIPList.trim().length()>0){
			  String[] IParray = privateIPList.split(GlobalConstant.SEMICOLON_SEPARATOR);
			  int index =0;
			  boolean isFound = false;
			  while (index < IParray.length) {
				  if (IParray[index].trim().equalsIgnoreCase(oldIp)){
					  IParray[index] = newIp;
					  isFound = true;
					  break;
				  }
				  index ++;
			  }

			  if (isFound){
				  //assemble the ';' separated list to be returned
				  index =0;
				  result ="";
				  while (index < IParray.length){
					  if (index >0) {
						  result = result.concat(GlobalConstant.SEMICOLON_SEPARATOR);
					  }
					  result = result.concat(IParray[index]);
					  index ++;
				  }
			  }
		  }

		  return result;
	  }

	  private String getAddressFromIPChangeDetails (String details, boolean isOld) {

		  //details look like "OldIP NewIP"

		  final String DELIMITER =" ";
		  final int NEW_POSITION =1;
		  final int OLD_POSITION =0;

		  String newIp = null;
		  String oldIp = null;
		  try {
			  newIp = details.split(DELIMITER)[NEW_POSITION].trim();
			  oldIp = details.split(DELIMITER)[OLD_POSITION].trim();
		  }catch (Exception ex){
			  logger.error("getNewAndOldIP error", ex);
		  }
		  return isOld ? oldIp : newIp;
	  }

	  /**
	   * This method is used for fetching and populating application profile
	   * data.
	   *
	   */
	  @Transactional
	  public void populateApplicationProfileData (List<InstanceBO> instanceList) {
		  StopWatch sWatch = new StopWatch();
		  logger.debug("Going to populate App Prof Data for Instances with count : "+ instanceList.size());
		  sWatch.start();
		  int cloudId = 0;
		  if(instanceList!=null && !instanceList.isEmpty() && instanceList.get(0)!=null) {
			  cloudId = instanceList.get(0).getCloudId();
		  }
		  Map<Integer,TagMasterDO> tagMasterDOMap = TagsHelper.getApplicationCopyTags(cloudId);		  		  
		  //Populate application profile data into the list.
		  for (InstanceBO instanceDetails : instanceList) {
			  String applicatinProfileName = "";
			  String applicationInstanceName = "";
			  if (tagMasterDOMap != null && !tagMasterDOMap.isEmpty() && tagMasterDOMap.containsKey(instanceDetails.getInstanceId())) {
				  try {
					  ApplicationCopyDAO applicationCopyDAO = CloudOneDAOFactoryBuilder.getFactory().getApplicationCopyDAO();
					  ApplicationCopyDO applicationCopyDO = new ApplicationCopyDO();
					  applicationCopyDO.setApplicationCopyID(Integer.parseInt(tagMasterDOMap.get(instanceDetails.getInstanceId()).getKey()));
					  
					  applicationCopyDO = applicationCopyDAO.fetchApplicationCopyByID(applicationCopyDO);
					  applicationInstanceName = applicationCopyDO.getApplicationCopyName();
					  applicatinProfileName = applicationCopyDO.getScalingPolicyDO().getScalingPolicyName();

					  instanceDetails.setApplicationProfileId(applicationCopyDO.getScalingPolicyDO().getScalingPolicyId());
					  instanceDetails.setApplicationInstanceId(applicationCopyDO.getApplicationCopyID());
					  instanceDetails.setApplicationId(applicationCopyDO.getScalingPolicyDO().getApplicationDO().getApplicationID());
				  } catch (NumberFormatException | DALException ex) {
					  logger.error("In getInstancesForAutomationFromCloud, error finding application details", ex.getMessage());
				  }
			  }
			  instanceDetails.setApplicationProfileName(applicatinProfileName);
			  instanceDetails.setApplicationInstanceName(applicationInstanceName);
		  }
		  sWatch.stop();
		  logger.debug("Time taken to populate the list of instances with " +
				  "App Prof details is: {}milli secs.",sWatch.getElapsedTimeInMilliSeconds());
	  }

	  /**
	   * This method scales the Hosted Service Roles
	   *
	   * @param serviceScalingBO
	   *
	   * @return
	   *
	   * @throws SLException
	   */
	  public ServiceScalingBO scaleHostedService(ServiceScalingBO serviceScalingBO) throws ICException {
		  List<WorkerQueueBO> wqBOs = null;
		  List<ServiceScalingBO> wqKeyPairList = null;
		  try {
			  wqBOs = new ArrayList<>();
			  wqKeyPairList = new ArrayList<>();
			  serviceScalingBO.setWqTarget(serviceScalingBO.getHostedServiceName());
			  serviceScalingBO.setWqTargetType(EntityTypeEnum.INSTANCE.getId());
			  wqKeyPairList.add(serviceScalingBO);
			  wqBOs = WorkerQueueHelper.buildInitialWorkerQueueObjectList(
					  wqKeyPairList, TaskDescriptionEnum.SCALE_CLOUD_SERVICE);
			  IPlatformsDomain domain = new PlatformsDomainImpl();
			  PlatformBO platformBO = domain.fetchProvider(PlatformsProvider
					  .getInputPlatformBO(serviceScalingBO));
			  CloudCredentialsBO providerCredentials = PlatformsProvider
					  .getProviderCredentials(platformBO);
			  WorkerQueueManager.updateInitialWorkerQueueStatus(wqBOs);
			  SLInstancesFacade slfacade = SLInstancesFacade.SINGLETON_INSTANCE;
			  serviceScalingBO = slfacade.scaleHostedService(serviceScalingBO,
					  providerCredentials);
			  ServiceBO serviceBOFromCloud = slfacade.getHostedServiceDetailsByName(providerCredentials, serviceScalingBO.getHostedServiceName());
			  ICreateInstanceDomain obj = CreateInstanceFactory
					  .getInstance(serviceScalingBO.getProviderType());
			  serviceScalingBO = obj.saveScaledInstanceInDAL(serviceScalingBO,serviceBOFromCloud);
		  } catch (SLException | ICException | DALException e) {
			  wqKeyPairList = populateErrorMessages(wqKeyPairList, e);
			  throw new ICException(e);
		  } finally {
			  wqBOs = WorkerQueueHelper.buildFinalWorkerQueueObjectList(ICUtils
					  .setWQTaskID(wqKeyPairList, wqBOs),
					  TaskDescriptionEnum.SCALE_CLOUD_SERVICE);
			  WorkerQueueManager.updateFinalWorkerQueueStatus(wqBOs);
		  }
		  return serviceScalingBO;

	  }

	  /**
	   * Method to get the Number of Roles in a Hosted Service
	   *
	   * @param scalingServiceBO
	   *
	   * @return
	   *
	   * @throws ICException
	   */
	  public ServiceScalingBO fetchScalingDetails(ServiceScalingBO scalingServiceBO)
			  throws ICException {
		  try {
			  IPlatformsDomain domain = new PlatformsDomainImpl();
			  PlatformBO platformBO = domain.fetchProvider(PlatformsProvider.getInputPlatformBO(scalingServiceBO));
			  CloudCredentialsBO providerCredentials = PlatformsProvider.getProviderCredentials(platformBO);

			  SLInstancesFacade slfacade = SLInstancesFacade.SINGLETON_INSTANCE;

			  scalingServiceBO = slfacade.fetchScalingDetails(scalingServiceBO, providerCredentials);
		  } catch (SLException | ICException e) {
			  throw new ICException(e);
		  }
		  return scalingServiceBO;
	  }

	  /**
	   * Method to get the catalogs for Terremark
	   *
	   * @param scalingServiceBO
	   *
	   * @return
	   *
	   * @throws ICException
	   */
	  public List<VirtualDiskBO> fetchCatalogs(PrerequisiteInfoInstanceBO prereqInfoInstanceBO) throws ICException {
		  List<VirtualDiskBO> catalogs = new ArrayList<>();
		  try {
			  IPlatformsDomain domain = new PlatformsDomainImpl();
			  PlatformBO platformBO = domain.fetchProvider(PlatformsProvider.getInputPlatformBO(prereqInfoInstanceBO));
			  CloudCredentialsBO providerCredentials = PlatformsProvider.getProviderCredentials(platformBO);
			  SLInstancesFacade slfacade = SLInstancesFacade.SINGLETON_INSTANCE;
			  catalogs = slfacade.fetchCatalogsFrmCloud(providerCredentials);
		  } catch (SLException | ICException e) {
			  throw new ICException(e);
		  }
		  return catalogs;
	  }

	  public List<NetworkingBO> getNetworksFromCloud(PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) throws ICException {
		  List<NetworkingBO> networks = new ArrayList<>();
		  try {
			  IPlatformsDomain domain = new PlatformsDomainImpl();
			  PlatformBO platformBO = domain.fetchProvider(PlatformsProvider.getInputPlatformBO(prerequisiteInfoInstanceBO));
			  CloudCredentialsBO providerCredentials = PlatformsProvider.getProviderCredentials(platformBO);
			  SLInstancesFacade slfacade = SLInstancesFacade.SINGLETON_INSTANCE;
			  networks = slfacade.getNetworksFrmCloud(providerCredentials);
		  } catch (SLException | ICException e) {
			  throw new ICException(e);
		  }
		  return networks;
	  }

	  public List<KeyPairsBO> getKeyPairsFromCloud(
			  PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) throws ICException {
		  List<KeyPairsBO> keypairs = new ArrayList<>();
		  try {
			  IPlatformsDomain domain = new PlatformsDomainImpl();
			  PlatformBO platformBO = domain.fetchProvider(PlatformsProvider.getInputPlatformBO(prerequisiteInfoInstanceBO));
			  CloudCredentialsBO providerCredentials = PlatformsProvider.getProviderCredentials(platformBO);
			  SLInstancesFacade slfacade = SLInstancesFacade.SINGLETON_INSTANCE;
			  keypairs = slfacade.getKeyPairsFrmCloud(providerCredentials);
		  } catch (SLException | ICException e) {
			  throw new ICException(e);
		  }
		  return keypairs;
	  }

	  public List<ElasticIPBO> getIPAddressForNtwrk(
			  PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO, String ntwrkName) throws ICException {
		  List<ElasticIPBO> ipaddrs = new ArrayList<>();
		  try {
			  IPlatformsDomain domain = new PlatformsDomainImpl();
			  PlatformBO platformBO = domain.fetchProvider(PlatformsProvider.getInputPlatformBO(prerequisiteInfoInstanceBO));
			  CloudCredentialsBO providerCredentials = PlatformsProvider.getProviderCredentials(platformBO);
			  SLInstancesFacade slfacade = SLInstancesFacade.SINGLETON_INSTANCE;
			  ipaddrs = slfacade.getIPAddrsForNtwrk(providerCredentials,ntwrkName);
		  } catch (SLException | ICException e) {
			  throw new ICException(e);
		  }
		  return ipaddrs;
	  }


	  public List<InstanceBO> getTemplatesFrmDatabase(
			  PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO, String operatingSystem, String category, String resourcePoolMorId) throws ICException {
		  List<InstanceBO> templates = new ArrayList<>();
		  List<InstanceBO> templatesFiltered = new ArrayList<>();
		  try {
			  IPlatformsDomain domain = new PlatformsDomainImpl();
			  PlatformBO platformBO = domain.fetchProvider(PlatformsProvider.getInputPlatformBO(prerequisiteInfoInstanceBO));
			  templates = fetchTerremarkTemplates(platformBO);
			  //Filter based on operating system
			  if(templates!=null && !templates.isEmpty()){
				  for(InstanceBO instBO : templates){
					  instBO.setTemplateHref(GlobalConstant.TERREMARK_TEMPLATE_PREFIX_1 + instBO.getMorId() + GlobalConstant.TERREMARK_TEMPLATE_PREFIX_2 + resourcePoolMorId);
					  instBO.setDescription(instBO.getNotes());
					  if (instBO.getOsName().contains(operatingSystem) &&
							  instBO.getResourcePoolMorID()!=null &&
							  instBO.getResourcePoolMorID().equals(resourcePoolMorId)){
						  if(category.equalsIgnoreCase(TerremarkCategoryEnum.OSOnlyStandardTemplate.getID()) &&
								  instBO.getDescription().equalsIgnoreCase(TerremarkCategoryEnum.OSOnlyStandardTemplate.getDescription())){
							  templatesFiltered.add(instBO);
						  }else if(category.equalsIgnoreCase(TerremarkCategoryEnum.OSDatabase.getID()) &&
								  instBO.getDescription().equalsIgnoreCase(TerremarkCategoryEnum.OSDatabase.getDescription())){
							  templatesFiltered.add(instBO);
						  }
					  }
				  }
			  }
		  } catch (ICException e) {
			  throw new ICException(e);
		  }
		  return templatesFiltered;
	  }

	  @Transactional
	  public List<InstanceBO> fetchTerremarkTemplates(
			  PlatformBO platformBO) throws ICException {
		  List<InstanceBO> templates = new ArrayList<>();
		  InstanceDetailsDAO instanceDetailsDAO = getFactory().getInstanceDetailsDAO();
		  InstanceDetailsDO inputInstanceDetailsDO = new InstanceDetailsDO();
		  ProviderMasterDO providerMasterDO = instanceDetailsDAO.get(ProviderMasterDO.class, platformBO.getProviderID());
		  inputInstanceDetailsDO.setProviderMasterDO(providerMasterDO);
		  inputInstanceDetailsDO.setState(InstanceStateEnum.DELETED.getValue());
		  try {
			  List<InstanceDetailsDO> activeTemplates = instanceDetailsDAO.fetchActiveTemplatesByProvideridNotState(inputInstanceDetailsDO);
			  if(activeTemplates != null) {
				  InstanceProvider instProvider = new InstanceProvider();
				  for(InstanceDetailsDO templateDO : activeTemplates) {
					  if(GlobalConstant.TERREMARK_TEMPLATE.equalsIgnoreCase(templateDO.getRootDeviceType())) {
						  templates.add(instProvider.populateBOFromDO(templateDO));
					  }
				  }
			  }
		  } catch (DALException e) {
			  throw new ICException(e);
		  }
		  return templates;
	  }

	  public List<InstanceBO> runOperationByInstanceID(List<InstanceBO> instanceBOs) throws ICException {
		  List<InstanceBO> resultList = null;
		  String operation=null;
		  List<InstanceBO> instanceBOList = new ArrayList<>();
		  InstanceBO theInstanceBO = instanceBOs.get(0);
		  operation = theInstanceBO.getOperation();
		  for (InstanceBO instBo : instanceBOs) {
			  InstanceBO instanceBO= getInstanceDetailsByVMID(instBo.getInstanceId());
			  if(instBo.isApproved()){
				  instanceBO.setApproved(instBo.isApproved());
				  instanceBO.setWqTaskID(instBo.getWqTaskID());
			  }
			 
			  if (operation.equalsIgnoreCase(InstancesActionsEnum.START_INSTANCES
					  .getValue())
					  && !(ProviderTypeEnum.AMAZON.equals(instanceBO
							  .getProviderType())
							  || ProviderTypeEnum.AZURE.equals(instanceBO
									  .getProviderType()) || ProviderTypeEnum.EUCALYPTUS
									  .equals(instanceBO.getProviderType())))
			  {
				  instanceBO.setOperation(InstancesActionsEnum.POWERON.getValue());
			  }
			  else if(operation.equalsIgnoreCase(InstancesActionsEnum.STOP_INSTANCES
					  .getValue())
					  && !(ProviderTypeEnum.AMAZON.equals(instanceBO
							  .getProviderType())		
							  || ProviderTypeEnum.AZURE.equals(instanceBO
									  .getProviderType()) || ProviderTypeEnum.EUCALYPTUS
									  .equals(instanceBO.getProviderType())))
			  {
				  instanceBO.setOperation(InstancesActionsEnum.POWEROFF.getValue());
			  }
			  else if(operation.equalsIgnoreCase(InstancesActionsEnum.SHUTDOWN
					  .getValue())
					  && (ProviderTypeEnum.AMAZON.equals(instanceBO
							  .getProviderType())		
							  || ProviderTypeEnum.AZURE.equals(instanceBO
									  .getProviderType()) || ProviderTypeEnum.EUCALYPTUS
									  .equals(instanceBO.getProviderType())))
			  {
				  instanceBO.setOperation(InstancesActionsEnum.STOP_INSTANCES.getValue());
			  }
			  else
			  {
				  instanceBO.setOperation(operation) ;
			  }
			  instanceBO.setWebServices(theInstanceBO.isWebServices());
			  instanceBO.setUpdatedByUser(theInstanceBO.getUpdatedByUser());
			  instanceBOList.add(instanceBO);
		  }
		  resultList=runOperation(instanceBOList);
		  return resultList;
	  }

	  /***********************************************************************************************************************
	   * Function to get the status of the Instances provisioned through Webservices
	   *
	   * @param int
	   * @return WSAuditBO
	   * @throws ICException
	   ***********************************************************************************************************************/
	  @SuppressWarnings({ "rawtypes" })
	  @Transactional
	  public WSAuditBO getInstanceCreationStatus(
			  int wsAuditID) throws ICException {
		  WSAuditBO wsAuditBO=null;
		  WSAuditDetailsDAO auditDetailsDAO = getFactory().getWSAuditDetailsDAO();
		  TaskStatusDAO taskStatusDAO = getFactory().getTaskStatusDAO();
		  TaskDetailsDAO taskDetailsDAO=getFactory().getTaskDetailsDAO();
		  try {
			  logger.debug("wsAuditID {}",wsAuditID);
			  /* Get the Audit details based on the tracking ID */
			  WSAuditDetailsDO auditDetailsDO = auditDetailsDAO.get(WSAuditDetailsDO.class,wsAuditID);
			  auditDetailsDO = auditDetailsDAO.fetchByWsAuditID(auditDetailsDO);
			  wsAuditBO=InstanceProvider.getWSAuditBOfromDO(auditDetailsDO);
			  wsAuditBO.setWqTargetId(auditDetailsDO.getInstanceId());
			  /*Get the task details based on the task id */
			  int taskID=auditDetailsDO.getTaskID();
			  TaskDetailsDO taskDetailsDO = new TaskDetailsDO();
			  taskDetailsDO.setTaskId(taskID);
			  TaskDetailsDO taskDetails=taskDetailsDAO.fetchByTaskId(taskDetailsDO);
			  /*Get the task status based on taskdetails */
			  TaskStatusDO taskStatusDO = new TaskStatusDO();
			  taskStatusDO.setTaskDetailsDO(taskDetails);
			  List<TaskStatusDO> lsttaskStatusDO = taskStatusDAO.fetchByTaskId(taskStatusDO);
			  /*Get the last updated status */
			  TaskStatusDO taskStatus=lsttaskStatusDO.get(0);
			  logger.debug("taskStatus...{}",lsttaskStatusDO.get(0).getStatus());
			  String comments=taskStatus.getStatus();
			  if(comments.equalsIgnoreCase(MessageStringConstants.FAILED))
			  {
				  String failureMessage=comments+".["+taskStatus.getComments()+"]";
				  wsAuditBO.setComments(failureMessage);
			  }
			  else if(comments.equalsIgnoreCase(MessageStringConstants.COMPELTE))
			  {
				  auditDetailsDO = auditDetailsDAO.fetchByWsAuditID(auditDetailsDO);
				  wsAuditBO.setWqTargetId(auditDetailsDO.getInstanceId());
				  wsAuditBO.setComments(comments);
			  }
			  else
			  {
				  wsAuditBO.setComments(comments);
			  }
		  } catch (DALException e) {
			  logger.error("Error in getInstanceCreationStatus {}",e);
			  throw new ICException(e);
		  }
		  return wsAuditBO;
	  }

	  
	  public  WSAuditBO getInstanceCreationStatusByTaskId(int taskId) throws ICException {
		  WSAuditBO wsAuditBO=null;
		  
		  TaskStatusDAO taskStatusDAO = getFactory().getTaskStatusDAO();
		  TaskDetailsDAO taskDetailsDAO=getFactory().getTaskDetailsDAO();
		  InstanceDetailsDAO instanceDetailsDAO = getFactory().getInstanceDetailsDAO();
		try {
			
			TaskDetailsDO taskDetailsDO = new TaskDetailsDO();
			taskDetailsDO.setTaskId(taskId);
			TaskDetailsDO taskDetails = taskDetailsDAO
					.fetchByTaskId(taskDetailsDO);
			/* Get the task status based on taskdetails */
			TaskStatusDO taskStatusDO = new TaskStatusDO();
			taskStatusDO.setTaskDetailsDO(taskDetails);
			List<TaskStatusDO> lsttaskStatusDO = taskStatusDAO
					.fetchByTaskId(taskStatusDO);
			/* Get the last updated status */
			TaskStatusDO taskStatus = lsttaskStatusDO.get(0);
			logger.debug("taskStatus...{}", lsttaskStatusDO.get(0).getStatus());
			String comments = taskStatus.getStatus();
			if (comments.equalsIgnoreCase(MessageStringConstants.FAILED)) {
				wsAuditBO = new WSAuditBO();
				String failureMessage = comments + ".["
						+ taskStatus.getComments() + "]";
				wsAuditBO.setComments(failureMessage);
			} else if (comments
					.equalsIgnoreCase(MessageStringConstants.COMPELTE)) {
				/*
				 * auditDetailsDO =
				 * auditDetailsDAO.fetchByWsAuditID(auditDetailsDO);
				 * wsAuditBO.setWqTargetId(auditDetailsDO.getInstanceId());
				 */
				wsAuditBO = new WSAuditBO();
				InstanceDetailsDO instanceDetails = new InstanceDetailsDO();
				instanceDetails
						.setCloudMasterDO(taskDetails.getCloudMasterDO());
				instanceDetails.setProviderMasterDO(taskDetails
						.getProviderMasterDO());
				instanceDetails.setNickName(taskDetails.getTarget());
				InstanceDetailsDO instanceDetailsDO = instanceDetailsDAO
						.fetchByCloudIdProviderIdNickName(instanceDetails);
				wsAuditBO.setWqTargetId(instanceDetailsDO.getInstanceID());
				wsAuditBO.setComments(comments);
			} else {
				wsAuditBO = new WSAuditBO();
				wsAuditBO.setComments(comments);
			}
		} catch (DALException e) {
		  logger.error("Error in getInstanceCreationStatus {}",e);
		  throw new ICException(e);
	  }
	  return wsAuditBO;
  }

	  
	  @Transactional
	  public InstanceBO updateInstancePolicyState(InstanceBO instanceBO)throws ICException
	  {
		  logger.debug("InstanceID {}", instanceBO.getInstanceId());
		  try {
			  InstanceDetailsDAO instDAO = getFactory().getInstanceDetailsDAO();
			  InstanceDetailsDO instDO = instDAO.get(InstanceDetailsDO.class,
					  instanceBO.getInstanceId());
			  instDO.setAutomationState(instanceBO.getAutomationState().getType());
			  instDAO.update(instDO);
			  InstanceProvider instProvider = new InstanceProvider();
			  instanceBO = instProvider.populateBOFromDO(instDO);
			  logger.info("Instance details updated");
		  } catch (DALException e) {
			  CloudOneErrors errors = new CloudOneErrors();
			  CloudOneError error = new CloudOneError();
			  InstanceBO instBO = new InstanceBO();
			  if (ErrorConstants.CO_DAL_ERR_004.getCode().equals(e.getMessage())) {
				  error.setErrorMessage(ErrorConstants.CO_DAL_ERR_004
						  .getDescription());
				  error.setErrorCode(ErrorConstants.CO_DAL_ERR_004.getCode());
			  } else {
				  error.setErrorMessage(e.getMessage());
				  error.setErrorCode(ErrorConstants.CO_DAL_ERR_GENERIC.getCode());
			  }
			  errors.addError(error);
			  logger.error(e.getMessage());
			  instBO.setErrors(errors);
		  }
		  return instanceBO;
	  }

	  public PrerequisiteInfoInstanceBO getPrerequisiteInformation(PrerequisiteInformationParamaterEnum fetchParameter, CreateInstanceInfoBO createBO, PrerequisiteInfoInstanceBO prerequisiteInfoInstanceBO) throws ICException {
		  try {
			  IPrereqInstanceDomain obj = (IPrereqInstanceDomain) PrerequisiteInstanceFactory
					  .getInstance(prerequisiteInfoInstanceBO.getProviderType());

			  prerequisiteInfoInstanceBO = obj
					  .getPrerequisiteInformation(fetchParameter,createBO,prerequisiteInfoInstanceBO);
			  logger.debug("Fetching complete prerequisite info inside InstanceDomainImpl.");

			  List<InstanceGroupBO> groupBOs = fetchGroups(prerequisiteInfoInstanceBO);
			  List<InstanceDeploymentBO> deploymentBOs = fetchDeployments(prerequisiteInfoInstanceBO);

			  prerequisiteInfoInstanceBO.setGroupsList(groupBOs);
			  prerequisiteInfoInstanceBO.setDeploymentsList(deploymentBOs);
			  if(ProviderTypeEnum.AZURE != prerequisiteInfoInstanceBO.getProviderType() && ProviderTypeEnum.SCVMM != prerequisiteInfoInstanceBO.getProviderType()
					  && ProviderTypeEnum.OPENSTACK != prerequisiteInfoInstanceBO.getProviderType()) {
				  prerequisiteInfoInstanceBO
				  .setClusters(getZonesForRegeion(prerequisiteInfoInstanceBO));
			  }
		  } catch (ICException e) {
			  logger.error("Error occurred during fetching the prerequisite information {}",e.getMessage());
			  logger.error(e);
			  CloudOneErrors errors = new CloudOneErrors();
			  CloudOneError error = new CloudOneError();
			  error.setErrorCode(StringUtils.getFilteredExceptionText(e));
			  errors.addError(error);
			  prerequisiteInfoInstanceBO.setErrors(errors);
		  }
		  return prerequisiteInfoInstanceBO;
	  }

	  @SuppressWarnings("unchecked")
	  @Override
	  public List<String> getCustomSpecsForTemplate(BaseBO inputBO)
			  throws ICException {
		  List<String> customSpecs = null;
		  if (CreateInstanceInfoBO.class.isAssignableFrom(inputBO.getClass())) {
			  CreateInstanceInfoBO createInstanceInfoBO = (CreateInstanceInfoBO) inputBO;
			  int templateId = createInstanceInfoBO.getTemplateID();
			  InstanceDetailsDO templateDO = getTemplateDODetails(templateId);

			  if(templateDO != null) {
				  byte[] bytes = templateDO.getTag();
				  if (bytes != null) {
					  Map<String, String> jsonTagsMap = null;
					  try {
						  jsonTagsMap = StringUtils.getMapFromJsonString(StringUtils
								  .getUTF8String(bytes));
					  } catch (UnsupportedEncodingException e) {
						  logger.error(e);
					  } catch (Exception e) {
						  logger.error(e);
					  }
					  if (jsonTagsMap != null
							  && jsonTagsMap.get(TagsConstants.CUSTOMSPECS) != null) {
						  String specs = jsonTagsMap.get(TagsConstants.CUSTOMSPECS);
						  String[] specNames = specs
								  .split(GlobalConstant.COMMA_SEPARATOR);
						  customSpecs = Arrays.asList(specNames);
					  }
				  }
			  }
		  }
		  return customSpecs;
	  }

	  @Transactional
	  private InstanceDetailsDO getTemplateDODetails(int templateId) {
		  InstanceDetailsDAO instanceDetailsDAO = CloudOneDAOFactoryBuilder
				  .getFactory().getInstanceDetailsDAO();
		  InstanceDetailsDO instanceDetailsDO = instanceDetailsDAO.get(
				  InstanceDetailsDO.class, templateId);
		  return instanceDetailsDO;
	  }

	  @Transactional(propagation = Propagation.REQUIRES_NEW)
	  public List<InstanceBO> updateSNMPDetails(
			  List<InstanceBO> instanceBOs) throws ICException {
		  logger.debug("In updateSNMPDetails method ");
		  InstanceDetailsDAO instanceDetailsDAO = getFactory()
				  .getInstanceDetailsDAO();
		  List<InstanceBO> instanceList = new ArrayList<>();
		  List<WorkerQueueBO> workerQueueObjects = null;
		  List<InstanceDetailsDO> instDOs = new ArrayList<>();
		  try {
			  for (InstanceBO instanceBO : instanceBOs) {
				  // Fetch the InstanceDetailsDO object from DB using the
				  // InstanceId
				  InstanceDetailsDO instDO = instanceDetailsDAO.get(
						  InstanceDetailsDO.class, instanceBO.getInstanceId());
				  /*
				   * Adding Null Check to updated the details accordingly as we
				   * will update only one detail at a time Added check for
				   */
				  if (instDO != null) {
					  if (instanceBO.getMetaData() != null) {
						  try {
							  instDO.setMetaData(instanceBO.getMetaData().getBytes("UTF-8"));
						  } catch (UnsupportedEncodingException e) {
							  logger.error(e);
						  }                   
					  }

					  //CO-11303: Using concatenated name for WQ to avoid confusion with same name VMs
					  instanceBO.setWqTarget(StringUtils
							  .getDisplayInstanceAndNickName(
									  instDO.getInstanceName(),
									  instDO.getNickName()));
					  instanceBO.setWqTargetId(instanceBO.getInstanceId());
					  instanceBO.setWqTargetType(EntityTypeEnum.INSTANCE.getId());
					  instDO.setOperation(instanceBO.getOperation());
					  instDOs.add(instDO);
				  }
			  }

			  // Adding Initial Worker Queue Entries
			  workerQueueObjects = WorkerQueueHelper
					  .buildInitialWorkerQueueObjectList(instanceBOs,
							  TaskDescriptionEnum.UPDATE_SNMP_DETAILS);
			  workerQueueObjects = WorkerQueueManager
					  .updateInitialWorkerQueueStatus(workerQueueObjects);

			  logger.debug("Callin Update Batch in updateSNMPDetails method ");
			  List<InstanceDetailsDO> instanceDOList = instanceDetailsDAO
					  .updatetBatch(instDOs);
			  instanceList = instanceProvider.getBOFromDO(instanceDOList);
			  logger.info("SNMP Details details updated");

			  logger.info("Instance details refreshed");
		  } catch (DALException e) {
			  CloudOneErrors errors = new CloudOneErrors();
			  CloudOneError error = new CloudOneError();
			  if (ErrorConstants.CO_DAL_ERR_004.getCode().equals(e.getMessage())) {
				  error.setErrorMessage(ErrorConstants.CO_DAL_ERR_004
						  .getDescription());
				  error.setErrorCode(ErrorConstants.CO_DAL_ERR_004.getCode());
			  } else {
				  error.setErrorMessage(e.getMessage());
				  error.setErrorCode(ErrorConstants.CO_DAL_ERR_GENERIC.getCode());
			  }
			  errors.addError(error);
			  if (!instanceList.isEmpty()) {
				  instanceList.get(0).setErrors(errors);
			  }
			  throw new ICException(e);
		  }
		  finally {
			  workerQueueObjects = WorkerQueueHelper
					  .buildFinalWorkerQueueObjectList(ICUtils.setWQTaskID(
							  instanceBOs, workerQueueObjects),
							  TaskDescriptionEnum.UPDATE_SNMP_DETAILS);
			  WorkerQueueManager.updateFinalWorkerQueueStatus(workerQueueObjects);
		  }
		  return instanceList;
	  }

	  @Transactional(propagation = Propagation.REQUIRES_NEW)
	  public List<InstanceBO> updateWMIDetails(
			  List<InstanceBO> instanceBOs) throws ICException {
		  logger.debug("In updateWMIDetails method ");
		  InstanceDetailsDAO instanceDetailsDAO = getFactory()
				  .getInstanceDetailsDAO();
		  List<InstanceBO> instanceList = new ArrayList<>();
		  List<WorkerQueueBO> workerQueueObjects = null;
		  List<InstanceDetailsDO> instDOs = new ArrayList<>();
		  try {
			  for (InstanceBO instanceBO : instanceBOs) {
				  // Fetch the InstanceDetailsDO object from DB using the
				  // InstanceId
				  InstanceDetailsDO instDO = instanceDetailsDAO.get(
						  InstanceDetailsDO.class, instanceBO.getInstanceId());
				  /*
				   * Adding Null Check to updated the details accordingly as we
				   * will update only one detail at a time Added check for
				   */
				  if (instDO != null) {
					  if (instanceBO.getMetaData() != null) {
						  try {
							  instDO.setMetaData(instanceBO.getMetaData().getBytes("UTF-8"));
							  instDO.setOperation(instanceBO.getOperation());
						  } catch (UnsupportedEncodingException e) {
							  logger.error(e);
						  }                   
					  }

					  instanceBO.setWqTarget(StringUtils
							  .getDisplayInstanceAndNickName(
									  instDO.getInstanceName(),
									  instDO.getNickName()));
					  instanceBO.setWqTargetId(instanceBO.getInstanceId());
					  instanceBO.setWqTargetType(EntityTypeEnum.INSTANCE.getId());

					  instDOs.add(instDO);
				  }
			  }

			  // Adding Initial Worker Queue Entries
			  workerQueueObjects = WorkerQueueHelper
					  .buildInitialWorkerQueueObjectList(instanceBOs,
							  TaskDescriptionEnum.UPDATE_WMI_DETAILS);
			  workerQueueObjects = WorkerQueueManager
					  .updateInitialWorkerQueueStatus(workerQueueObjects);

			  logger.debug("Callin Update Batch in updateWMIDetails method ");
			  List<InstanceDetailsDO> instanceDOList = instanceDetailsDAO
					  .updatetBatch(instDOs);
			  instanceList = instanceProvider.getBOFromDO(instanceDOList);
			  logger.info("WMI Details details updated");

			  logger.info("Instance details refreshed");
		  } catch (DALException e) {
			  CloudOneErrors errors = new CloudOneErrors();
			  CloudOneError error = new CloudOneError();
			  if (ErrorConstants.CO_DAL_ERR_004.getCode().equals(e.getMessage())) {
				  error.setErrorMessage(ErrorConstants.CO_DAL_ERR_004
						  .getDescription());
				  error.setErrorCode(ErrorConstants.CO_DAL_ERR_004.getCode());
			  } else {
				  error.setErrorMessage(e.getMessage());
				  error.setErrorCode(ErrorConstants.CO_DAL_ERR_GENERIC.getCode());
			  }
			  errors.addError(error);
			  if (!instanceList.isEmpty()) {
				  instanceList.get(0).setErrors(errors);
			  }
			  throw new ICException(e);
		  }
		  finally {
			  workerQueueObjects = WorkerQueueHelper
					  .buildFinalWorkerQueueObjectList(ICUtils.setWQTaskID(
							  instanceBOs, workerQueueObjects),
							  TaskDescriptionEnum.UPDATE_WMI_DETAILS);
			  WorkerQueueManager.updateFinalWorkerQueueStatus(workerQueueObjects);
		  }
		  return instanceList;
	  }

	  public InstanceBO getFlavorsFromDB(InstanceBO instanceBO) throws ICException {
		  try {
			  CloudOneDAOFactory factory = CloudOneDAOFactoryBuilder.getFactory();
			  InstanceTypeDAO instTypeDAO = factory.getInstanceTypeDAO();
			  List<InstanceTypeBO> flavorsBO = new ArrayList<>();
			  List<InstanceTypeDO> flavorsDO = new ArrayList<>();
			  InstanceTypeDO flavorDO = new InstanceTypeDO();
			  flavorDO.setDataCenterDetailsDO(instTypeDAO.get(DataCenterDetailsDO.class,instanceBO.getDatacenterID()));
			  flavorsDO = instTypeDAO.fetchByDataCenterId(flavorDO);
			  if(flavorsDO!=null && !flavorsDO.isEmpty()){
				  for(InstanceTypeDO thisFlavorDO: flavorsDO){
					  flavorsBO.add(InstanceTypeProvider.getInstanceTypeBOfromDO(thisFlavorDO));
				  }
			  }
			  instanceBO.setInstanceTypeBO(flavorsBO);
		  } catch (DALException e) {
			  CloudOneErrors errors = new CloudOneErrors();
			  CloudOneError error = new CloudOneError();
			  error.setErrorMessage(e.getMessage());
			  error.setErrorCode(ErrorConstants.CO_DAL_ERR_GENERIC.getCode());
			  errors.addError(error);
			  throw new ICException(e);
		  }
		  return instanceBO;
	  }

	  @Transactional
	  public void performPostDeleteCleanup(InstanceBO instanceBO) {
		  try{
			  TagMappingDAO tagMappingDAO = CloudOneDAOFactoryBuilder.getFactory().getTagMappingDAO();
			  List<TagMappingDO> tagMappingDOs = null;
			  try {
				  TagMappingDO tagMappingDO = new TagMappingDO();
				  tagMappingDO.setEntityID(instanceBO.getInstanceId());
				  tagMappingDO.setEntityType(TagsEnum.ATTRIBUTE_INSTANCE.name());
				  tagMappingDOs = tagMappingDAO.fetchAllTagsByEntityIdType(tagMappingDO);
				  for (TagMappingDO tagMappingDo : tagMappingDOs) {
					  tagMappingDo.setState(StateEnum.DELETED.name());
				  }
				  tagMappingDAO.updatetBatch(tagMappingDOs);
				  logger.info("Tag mappings deleted");
			  } catch (DALException e) {
				  // Not throwing any exceptions from here and allowing to proceed
				  // with further execution
				  logger.error(e);
			  }
			  cleanUpScheduledJobs(instanceBO);

		  } catch (Exception e) {
			  logger.error(e);
		  }
	  }

	  private void cleanUpScheduledJobs(InstanceBO instanceBO) {
		  IJobMgmtDomain jobMgmtDomain = (IJobMgmtDomain) DomainFactory
				  .getInstance(ICObjectEnum.JOBMGMT);
		  try {
			  JobMgmtBO jobMgmtBO = new JobMgmtBO();
			  jobMgmtBO.setCloudId(instanceBO.getCloudId());
			  jobMgmtBO.setProviderID(instanceBO.getProviderID());
			  jobMgmtBO.setProviderType(instanceBO.getProviderType());
			  jobMgmtBO
			  .setUserID((instanceBO.getUpdatedByUser() == 0) ? instanceBO
					  .getUserID() : instanceBO.getUpdatedByUser());
			  jobMgmtDomain.deleteJobsForEntity(jobMgmtBO,
					  SchedulerEntityTypeEnum.INSTANCE,
					  instanceBO.getInstanceId());
		  } catch (ICException e) {
			  logger.error(e);
		  }
	  }

	  private List<InstanceBO> getInstances(InstanceBO instBO, String sql) throws ICException {
		  List<InstanceBO> instanceList = null;
		  StopWatch sWatch = new StopWatch();
		  sWatch.start();
		  try {
			  if(instBO.getProviderType()==ProviderTypeEnum.IBMSLBAREMETAL){
				  instanceList = getIBMBareMetalsList(instBO);
			  } else {
				  instanceList = getListInstances(instBO,sql);  
			  }
		  } catch (ICException e) {
			  CloudOneErrors errors = new CloudOneErrors();
			  CloudOneError error = new CloudOneError();
			  if (ErrorConstants.CO_DAL_ERR_004.getCode().equals(e.getMessage())) {
				  error.setErrorMessage(ErrorConstants.CO_DAL_ERR_004
						  .getDescription());
				  error.setErrorCode(ErrorConstants.CO_DAL_ERR_004.getCode());
			  } else {
				  error.setErrorMessage(e.getMessage());
				  error.setErrorCode(ErrorConstants.CO_DAL_ERR_GENERIC.getCode());
			  }
			  errors.addError(error);
			  logger.error(e.getMessage());
			  throw new ICException(e);
		  }
		  sWatch.stop();
		  logger.debug("Time taken to get the list of instances is: {}milli secs.",sWatch.getElapsedTimeInMilliSeconds());

		  return instanceList;
	  }

	  /**
	   * Used to get List of instances based on DatacenterID(Region),UserID,UserGroupID,CloudID,ProviderID
	   * @param instBO
	   * @param sql
	   * @return
	   * @throws DALException 
	   * @throws ICException
	   */
	  @Transactional
	  private CloudOneResultSet getInstanceListBasedOnDataCenterIDFromDB(InstanceBO instBO) throws DALException{
		  return getFactory().getReportingDAO().executeQuery(DatabaseSQLConstant.GET_All_INSTANCES_BASED_ON_DATACENTER,false,instBO.getUserID(),instBO.getActiveUserGroupID(),PermissionEnum.COMPUTE_INSTANCE_READ.getId(),instBO.getCloudId(),instBO.getDatacenterID(),instBO.getProviderID());
	  }

	  /**
	   * Used to get InstanceBos form resultset
	   * @param resultSet
	   * @return
	   */
	  private List<InstanceBO> getInstanceBosFromResultSet(CloudOneResultSet resultSet){
		  List<InstanceBO> instanceBOs=null;
		  if(null != resultSet){
			  InstanceBO instanceBO=null;
			  int instanceID=0;
			  int datacenterID=0;
			  instanceBOs=new ArrayList<InstanceBO>();
			  while (resultSet.next()) {
				  if(null != resultSet.getString("InstanceID")){
					  instanceID=NumberUtils.toInt(resultSet.getString("InstanceID"));
				  }
				  if(null != resultSet.getString("DataCenterID")){
					  datacenterID=NumberUtils.toInt(resultSet.getString("DataCenterID"));
				  }
				  instanceBO=new InstanceBO(resultSet.getString("InstanceName"),resultSet.getString("Nickname"),instanceID,datacenterID,resultSet.getString("State")); 
				  instanceBOs.add(instanceBO);
			  }
		  }
		  return instanceBOs;
	  }

	  /**
	   * get instance list based on datacenter id
	   * @param instBO
	   * @return
	   */
	  public List<InstanceBO> getInstanceListBasedOnDataCenterID(InstanceBO instBO){
		  List<InstanceBO> instanceBOs=null;
		  InstanceBO instanceBO=null;
		  try {
			  instanceBOs=getInstanceBosFromResultSet(getInstanceListBasedOnDataCenterIDFromDB(instBO));
		  } catch (DALException e) {
			  //creating new instanceBOs list to set error message for display in UI
			  instanceBOs=new ArrayList<InstanceBO>();
			  instanceBO=new InstanceBO();
			  instanceBOs.add(instanceBO);
			  populateErrorMessages(instanceBOs, e);
		  }
		  return instanceBOs;
	  }
	  
	  /**
	   * used to fetch azure instance details for cloud api call
	   * @param instanceBOs
	   * @param instanceDetailsDAO
	   * @param operation
	   * @throws SLException
	   */
	@SuppressWarnings("unchecked")
	@Transactional
	public void populateAzureInstanceDetails(List<InstanceBO> instanceBOs, String operation) throws SLException{
		  try {
			  
			  if(null != instanceBOs && null != instanceBOs.get(0) && ProviderTypeEnum.AZURE.equals(instanceBOs.get(0).getProviderType()) && InstancesActionsEnum.AZURECLONETOIMAGE.getValue().equalsIgnoreCase(operation)){
				  InstanceBO instBo=instanceBOs.get(0);
				  InstanceDetailsDAO instanceDetailsDAO = getFactory().getInstanceDetailsDAO();
				  InstanceDetailsDO instanceDetailsDO=instanceDetailsDAO.get(InstanceDetailsDO.class, instBo.getInstanceId());
				  if(null !=instanceDetailsDO) {
					  byte[] bytes = instanceDetailsDO.getTag();
					  if (bytes != null) {
						  Map<String, String> jsonTagsMap = null;

						  jsonTagsMap = StringUtils
								  .getMapFromJsonString(StringUtils
										  .getUTF8String(bytes));
						  instBo.setTags(jsonTagsMap);
					  }
					  if(instBo.getTags()!=null && !instBo.getTags().isEmpty()) {									
						  instBo.setDeploymentName(instBo.getTags().get(TagsConstants.AZURE_INST_TAGKEY_DEPLOYMENT));
						  instBo.setInstGroupName(instBo.getTags().get(TagsConstants.AZURE_INST_TAGKEY_HOSTEDSERVICE));
					  }									
				  }
			  }
		  } catch (UnsupportedEncodingException e) {
			  throw new SLException(e);
		  }
	  }
	  
	  /**
	   * Added for CO-20043 - Mail to notify Scheduled De-Provisioning of Instances 3 hours prior to actual De-Prov task completion
	   * @param scheduleDetailsBean,instDataBean
	   * @param recurrenceBO,instBO
	   * @param includedEntities,wqBOs,instanceBOs
	   * @throws ParseException,SchedulerException,ICException
	   */
	  public int scheduleDeProvNotifyJob(ScheduleDetailsBean scheduleDetailsBean,InstanceDataBean instDataBean,RecurrenceBO recurrenceBO,InstanceBO instBO,
			  List<Integer> includedEntities,List<WorkerQueueBO> wqBOs,List<InstanceBO> instanceBOs) throws ParseException,SchedulerException,ICException{
		  List<WorkerQueueBO> notifyWqBOs = null;
		  
		  int notificationJobID = 0;
		  Date newDtTime = null;
			  			  
		  String startDt = scheduleDetailsBean.getRecurStartDate();
		  String startTime = scheduleDetailsBean.getRecurStartTime();
		  
		  String scheduledNotificationTime = DateUtils.getHoursSubtracted(startDt+" "+startTime, 3, DateTimeFormatEnum.DTF_19);
		  
		  newDtTime = TimeZoneConverter.getDate(scheduledNotificationTime, scheduleDetailsBean.getTimeZone(), DateTimeFormatEnum.DTF_19);
		  
		  // Create the job only if the scheduled Deprov is 3 or more hours ahead of current time
		  if(newDtTime.compareTo(new Date(System.currentTimeMillis())) == 1){
			  try {		  
				  notifyWqBOs = WorkerQueueHelper.buildInitialWorkerQueueObjectList(instanceBOs,  TaskDescriptionEnum.SCHEDULE_NOTIFY_DELETE_VM);
				  WorkerQueueManager.updateInitialWorkerQueueStatus(notifyWqBOs);
				  
				  scheduleDetailsBean.setRecurStartDate(scheduledNotificationTime.split(" ")[0]);
				  scheduleDetailsBean.setRecurStartTime(scheduledNotificationTime.split(" ")[1]);
				  String schedulingInfoForNotif = BeanBuilder.buildScheduleInfo(scheduleDetailsBean);
				  
				  JobTypeEnum jobTypeForNotif = JobTypeEnum.NOTIFY_DELETE_VM;
				  List<TriggerDetailsBean> triggerDetailsBeans1 = new ArrayList<>();
				  
				  JobDataBean jobDataBean1 = BeanBuilder.buildJobDataBean(
						  recurrenceBO.getCloudId(), jobTypeForNotif,
						  recurrenceBO.getProviderID(), recurrenceBO.getProviderType(),
						  recurrenceBO.getActiveUserGroupID(),
						  null,
						  jobTypeForNotif.name(),
						  recurrenceBO.getUserID(), SchedulerEntityTypeEnum.INSTANCE,
						  includedEntities, null, schedulingInfoForNotif, instBO.getNotificationID(), instDataBean, wqBOs.get(0).getCurrentTaskKey()); 
				 
				  TriggerDetailsBean triggerDetailsBeanForNotif = BeanBuilder.buildSimpleTriggerBean(jobTypeForNotif.toString(),
						  jobTypeForNotif.toString(), null, 0,
						  null, newDtTime, 0,
						  false, scheduleDetailsBean.getTimeZone());
				  
				  triggerDetailsBeans1.add(triggerDetailsBeanForNotif);	
				  
				  String[] jobExecDetails1 = JobMgmtUtil.getJobClassNameForType(jobTypeForNotif);
				  
				  JobDetailsBean jobDetailsBean1 = BeanBuilder
						  .buildJobDetailsBean(
								  jobTypeForNotif.name(),
								  jobTypeForNotif.toString(),
								  jobTypeForNotif.getDescription(),
								  null,
								  jobExecDetails1[1],
								  null, false, false, SchedulingEnvTypeEnum.valueOf(jobExecDetails1[0]),
								  jobDataBean1);
				  
				  ISchedulerService schedulerService = new SchedulerServiceImpl();
				  schedulerService.scheduleJob(jobDetailsBean1, triggerDetailsBeans1, recurrenceBO.getCloudId());
				  notificationJobID = jobDataBean1.getJobId();
				  scheduleDetailsBean.setRecurStartDate(startDt);
				  scheduleDetailsBean.setRecurStartTime(startTime);
			  }
			  catch (SchedulerException e) {
				  instanceBOs = populateErrorMessages(instanceBOs, e);
			  } finally {
				  notifyWqBOs = WorkerQueueHelper.buildFinalWorkerQueueObjectList(ICUtils.setWQTaskID(instanceBOs, notifyWqBOs),  TaskDescriptionEnum.SCHEDULE_NOTIFY_DELETE_VM);
				  WorkerQueueManager.updateFinalWorkerQueueStatus(notifyWqBOs);
			  }
		  }
		  return notificationJobID;
	  }
	  
	  @Transactional
	  public List<PaaSDetailsBO> validateDNSName(List<InstanceTO> instanceBOs) throws ICException{
		  PaaSDetailsDAO paaSDetailsDAO = getFactory().getPaaSDetailsDAO();
		  PaaSDetailsDO paasDetailsDO = new PaaSDetailsDO();
		  List<PaaSDetailsBO> paaSDetailsBOs = new ArrayList<PaaSDetailsBO>();
		  List<PaaSDetailsDO> paaSDetailsDOs = new ArrayList<>();
		  if(instanceBOs != null && !instanceBOs.isEmpty()){
			  InstanceTO instanceBO = instanceBOs.get(0);
			  paasDetailsDO.setCloudMasterDO(getFactory().getCloudMasterDAO().get(CloudMasterDO.class, instanceBO.getCloudId()));
			  paasDetailsDO.setProviderMasterDO(getFactory().getProviderMasterDAO().get(ProviderMasterDO.class, instanceBO.getProviderID()));
			  try {
				  paaSDetailsDOs = paaSDetailsDAO.fetchByCloudIdProviderIDServiceType(paasDetailsDO, GlobalConstant.HOSTED_SERVICE, true);
				  if(paaSDetailsDOs != null && !paaSDetailsDOs.isEmpty()){
					  paaSDetailsBOs = PaaSProvider.getPaaSDetailsBOFromDO(paaSDetailsDOs);
				  }
			} catch (DALException e) {
				throw new ICException(e);
			}
		  }
		  return paaSDetailsBOs;
	  }
	  
	  /***********************************************************************************************************************
		 * Method to fetch the List of Instances based on the Authorization
		 * permission for the logged in user.
		 *
		 * @param InstanceBO
		 * @return List<InstanceBO>
		 * @throws ICException
		 **********************************************************************************************************************/
		@Transactional
		public List<InstanceBO> getIBMBareMetalsList(InstanceBO instBO) throws ICException {
			List<InstanceBO> instanceBOList = null;
			List<ServerMasterDO> serverMasterDOs = new ArrayList<>();
			try {
				
				instanceBOList = new ArrayList<>();
				ServerMasterDAO serverMasterDAO = getFactory().getServerMasterDAO();
				ServerMasterDO smDO = new ServerMasterDO();				
				smDO.setCloudMasterDO(getFactory().getCloudMasterDAO().get(CloudMasterDO.class, instBO.getCloudId()));
				smDO.setProviderMasterDO(getFactory().getProviderMasterDAO().get(ProviderMasterDO.class, instBO.getProviderID()));
				serverMasterDOs = serverMasterDAO.fetchActiveServersByCloudIdProviderId(smDO);
				instanceBOList = HostProvider.populateInstanceBOsFromServerMasterDOs(instBO,serverMasterDOs);
			} catch (DALException e) {
				logger.error("Error while getting User Query ;Error is: {}", e.getMessage(), e);
				throw new ICException(ErrorConstants.CO_IC_ERR_0093.getCode(), e);
			}
			return instanceBOList;

		}
		
		private  String calculateDeprovisioningDate(String provDate,int daysToBeAdded){
			SimpleDateFormat sdf=new SimpleDateFormat(DateTimeFormatEnum.DTF_20.getPattern());
			try {
				Date provisioningDate=sdf.parse(provDate);
				long timeInMilliSec=((long)daysToBeAdded)*24L*60L*60L*1000L;
				provisioningDate.setTime(provisioningDate.getTime()+(timeInMilliSec));
				
				String deprovisioningDate=sdf.format(provisioningDate);
				return deprovisioningDate;
			} catch (ParseException e) {
			}
			return null;
		}
		
		private  String getDeprovisioningDetails(String startDate,String startTime){
			String deprovisioningDetails=String.format(GlobalConstant.DEPROVISIONING_DETAILS, startDate,startTime);
			return deprovisioningDetails;
		}
		
		private String addDeprovisionDetails(ScheduleDetailsBean scheduleDetailsBean,String schedulingInfo){
			if((!StringUtils.isEmpty(scheduleDetailsBean.getDeprovisiongStartDate())) && (!StringUtils.isEmpty(scheduleDetailsBean.getDeprovisiongStartTime()))){
				schedulingInfo=schedulingInfo+";"+getDeprovisioningDetails(scheduleDetailsBean.getDeprovisiongStartDate(),scheduleDetailsBean.getDeprovisiongStartTime());
			}
			return schedulingInfo;
		}
				
	  private boolean isModifiedResourceActionScaleUp(InstanceBO instanceBO) throws ICException{
		  boolean isScaleUp = false;
		  int currentSystemType = 0;
		  int arrivedSystemType = 0;
		  int oldMemoryValue = 0;
		  int newMemoryValue = 0;
		  int oldCPUValue = 0;
		  int newCPUValue = 0;
		  List<InstanceBO> instanceBOList = new ArrayList<>();
		  List<String> systemTypes = new ArrayList<>();
		  InstanceBO instanceDetailsFromCloud = null;

		  
			if(instanceBO.getProviderType() == ProviderTypeEnum.AMAZON && instanceBO.isSystemTypeChanged()){
				instanceBOList.add(instanceBO);
				instanceBOList = getInstanceDetailsFromCloud(instanceBOList);
				if(instanceBOList == null || instanceBOList.isEmpty()) return false;
				
				//Use the Architecture, RootDevice and VirtualizationType details to get possible instance types
				//Only one instance will be available
				instanceDetailsFromCloud = instanceBOList.get(0);
				logger.debug("InstanceDomainImpl:architecture  {} ", instanceDetailsFromCloud.getArchitecture());
				logger.debug("InstanceDomainImpl:rootDeviceType  {}",  instanceDetailsFromCloud.getRootDeviceType());
				logger.debug("InstanceDomainImpl:virtualizationType {} ", instanceDetailsFromCloud.getVirtualizationType());
				systemTypes = buildInstanceTypeList(instanceDetailsFromCloud.getArchitecture(), 
						instanceDetailsFromCloud.getRootDeviceType(), instanceDetailsFromCloud.getVirtualizationType(), false);
				logger.debug("systemTypes size {} ", systemTypes.size());

				currentSystemType = systemTypes.indexOf(instanceBO.getSystemType());
				arrivedSystemType = systemTypes.indexOf(instanceBO.getModifiedSystemType());
				
				if(arrivedSystemType > currentSystemType){
					isScaleUp = true;
				}
				else {
					isScaleUp = false;
				}
			} else if(instanceBO.getProviderType() == ProviderTypeEnum.VMWARE){
				if(instanceBO.isCPUChanged()) {
					oldCPUValue = instanceBO.getOldCpuValue();
					newCPUValue = instanceBO.getNoOfCPUs();
					if(oldCPUValue < newCPUValue){
						isScaleUp =  true;
					}else {
						isScaleUp = false;
					}
				}
				if(instanceBO.isMemoryChanged()){
					oldMemoryValue = instanceBO.getOldMemoryValue();
					newMemoryValue = instanceBO.getMemorySize();
					if(oldMemoryValue < newMemoryValue){
						isScaleUp =  true;
					}else{
						isScaleUp = false;
					}
				}
			}

		  
		  return isScaleUp;
	  }

	@Override
	public void updateServiceRequest(CreateInstanceInfoBO boObj) throws ICException {

		IntegrationSystemBO serviceRequestIntegrationSystemBO = AssetManagementUtil
				.getEnabledServiceRequestIntegrationSystemBO(boObj.getCloudId());
		logger.info("serviceRequestIntegrationSystemBO---->"
				+ serviceRequestIntegrationSystemBO);
		if (serviceRequestIntegrationSystemBO != null) {
			sendServiceRequestToQueueForUpdate(
					serviceRequestIntegrationSystemBO, boObj);
		}

	}
	
	private void sendServiceRequestToQueueForUpdate(
			IntegrationSystemBO integrationSystemBO, CreateInstanceInfoBO boObj) throws ICException {
		

		
			logger.info("Sending Asset for SR Creation : " + boObj.getInstanceName());
			// send to queue
			AssetCO assetCO = AssetMgmntProvider
					.getAssetCOFromCreateInstanceInfoBO(boObj);
			List<AssetCO> assetCOs = new ArrayList<>();
			assetCOs.add(assetCO);
			Class<?>[] argTypes = { List.class };
			Object[] args = new Object[] { assetCOs };
			Cloud360MessageObject c360MsgObject = MessageUtil
					.buildMessageObject(AssetMgmntFacade.class.getName(), null,
							"updateSR", argTypes, args);
			try {
				MessageAdapter.sendQueueObjMessage(
						MessageQueueConstants.ASSETMGMT_QUEUE, c360MsgObject);
			} catch (MessagingException e) {
				logger.error(e);
			}
		
	}
}