package com.cognizant.cloudone.ui.constants;


public interface NavigationRuleConstants {
	String CLOUD_ONE_BASE_URL = "login";
	String LOGIN_URL = CLOUD_ONE_BASE_URL + "/login.cl";
	String SESSION_TIMEOUT_URL = CLOUD_ONE_BASE_URL + "/loginError.cl";
	String LICENSE_EXPIRED_URL = "template/license-expired.cl";
	String C360_ACS_URL = "sso/cacs.cl";
	String SSO_AUTH_ERROR_URL = "template/sso-auth-error.cl";
	
	
	String DASHBOARD_URL = "dashboard/dashboard-overview.cl";
	String RETURN_URL = "template/auth-error.cl";
	String PASSWORD_ACTION_NOTIFY_URL = "login/confirmation.cl?isfirst=true";
	String REFRESH_INSTANCE_NAVIGATION_RULE_STR = "inventoryRefreshed";
	String UPDATE_INSTANCE_NAVIGATION_RULE_STR = "instanceUpdated";
	String UPDATE_LOGIN_INSTANCE_NAVIGATION_RULE_STR = "instanceLoginUpdated";
	String UPDATE_LOGIN_INSTANCE_NAVIGATION_RECLAMATION_RULE_STR = "reclamationinstanceLoginUpdated";
	String EXECUTE_SCRIPT_INSTANCE_NAVIGATION_RULE_STR = "executeScript";

	String LOI_URL_WITH_PARAM = "provisioning/list-instances.cl";
	String DATASTORES_URL_WITH_PARAM = "provisioning/list-datastores.cl";
	String STORAGE_PROFILE_URL_WITH_PARAM = "policies/list-storage-profiles.cl?isfirst=true";
	String JMX_URL_WITH_PARAM = "monitoring/list-jmx-instance-discovery.cl?isfirst=true";
	String LIST_S3BUCKETS = "provisioning/list-all-s3buckets.cl?isfirst=true" ;
	String OBJECT_STORAGE = "provisioning/list-all-s3buckets.cl";

	String CHANGEASSIGNEE_URL = "/provisioning/changeAssignee.cl?isfirst=true";
	String LIST_EVENT_URL = "monitoring/list-events.cl?isfirst=true";
	String CLONE_VMWARE_VM = "provisioning/vmware-clone-instance.cl?isfirst=true";

	String PROV_WORKFLOW_URL = "policies/list-provisioning-workflow.cl?isfirst=true"; //Changed as per CO-18952
	String CREATE_PROV_WORKFLOW_URL = "policies/create-scaling-policy.cl?isfirst=true";

	String KEYPAIR_URL = "/security/list-keyPair.cl?isfirst=true";
	String KEY_PAIR= "/security/list-keyPair.cl";

	String LIST_PROFILING_URL = "analytics/list-profile.cl?isfirst=true";
	String LIST_EBS_SNAPSHOTS_URL = "provisioning/list-all-ebs-snapshot.cl?isfirst=true";
	String LIST_EBS_SNAPSHOTS = "provisioning/list-all-ebs-snapshot.cl";
	String LIST_DATABASE_SNAPSHOTS = "provisioning/list-database-snapshots.cl";
	String COMPLIANCE_POLICIES_URL = "/analytics/list-compliance-policies.cl?isfirst=true";
	String AUTOMATION_POLICIES_URL = "/policies/list-automation-policies.cl?isfirst=true";
	
	String SERVICE_PROFILES_URL = "/policies/list-service-profiles.cl";
	String CREATEVIEWEDIT_SERVICE_PROFILE_URL = "policies/common-create-instance.cl?isfirst=true";
	String SELECT_SERVICE_PROFILE_URL = "/provisioning/select-service-profiles.cl?isfirst=true";
	String CREATE_INSTANCE_PAGE = "provisioning/vmware-create-instance.cl?isfirst=true";
	String AIX_REVERT_SNAPSHOTS = "provisioning/aix-revertsnap-datagrid.cl?isfirst=true";
	String LIST_INTELLEGENT_POLICY_URL = "policies/list-intelligent-policy.cl";
	String VMWARE_CREATE_INSTANCE_COUNT_PAGE = "provisioning/vmware-instance-count.cl";
	String VMWARE_CREATE_INST_COUNT_PAGE = "provisioning/vmware-create-instance-count.cl";
	String AWS_CREATE_INSTANCE_COUNT_PAGE = "provisioning/create-instance-count.cl";
	String DATABASE_SERVICES = "provisioning/list-database-services.cl";
	String IP_ADDRESS ="provisioning/ElasticIp.cl";
    String LIST_BIZ_POLICIES_PAGE = "policies/list-biz-policies.cl?isfirst=true";
    String CREATE_MONITORING_PROFILE = "monitoring/create-monitoring-profile.cl?isfirst=true";
    String CREATE_MONITORING_PROFILE_WITHOUT_CACHE_PARAM = "monitoring/create-monitoring-profile.cl";
    String LIST_MONITORING_PROFILE = "monitoring/list-all-monitoring-profiles.cl?isfirst=true";
    String START_DIAGNOSTIC_RULE = "settings/list-diagnostics.cl";
    String FIREWALLS_PAGE = "security/list-security-groups.cl?isfirst=true";
    String REDIRECT_TO_ERROR_PAGE="redirectErrorPage";
    String CLOUD_SETTINGS_WITH_PARAM = "settings/cloud-settings.cl?isfirst=true";
    String SECURITY_PERMISSION_PAGE="security/security-permission.cl";
    String USER_GROUP_PAGE = "settings/user-grp.cl";
    String POLICY_MANAGEMENT_PAGE="policies/policy-management.cl";
    String CREATE_POLICY_PAGE = "policies/create-policy.cl";
    String S3_BUCKET_PAGE= "provisioning/s3-buckets.cl";
    String SECURITY_GROUP_PAGE="security-instances.cl";
    String FORGOT_PWD = CLOUD_ONE_BASE_URL + "/forgot-password.cl";
	String CONFIRMATION = CLOUD_ONE_BASE_URL + "/confirmation.cl";
	String RESET_PASSWORD = CLOUD_ONE_BASE_URL + "/reset-password.cl";
	String LOAD_AUP_POLICY = "policies/list-automation-policies.cl";
	String LOAD_PRP_POLICY = "policies/list-usage-policies.cl";
	String LOAD_PLP_POLICY = "policies/list-intelligent-policy.cl";
	String LOAD_MP_POLICY = "monitoring/list-monitoring-policies.cl";
	String LOAD_CM_POLICY = "analytics/list-compliance-policies.cl";
	String LOAD_STF_PROFILE = "policies/list-storage-profiles.cl";
	String LOAD_AT_CPF="policies/list-service-profiles.cl?isfirst=true";
   
    String GO_TO_VMWARE_CREATE_INSTANCE = "vmwarebacktocreateinstance";
    String GO_TO_SERVICE_PROFILES = "gotoServiceProfiles";
    String GO_TO_COUNT = "continue";
    String GO_TO_AZURE_COUNT = "continueToInstCount";
    String GO_TO_LAUNCH = "toLaunch";
    String GO_TO_TERREMARK_COUNT = "continueToInstCountTerremark";
    String GO_TO_OPENSTACK_COUNT = "continueToInstCountOpenStack";
    String GO_TO_SOFTLAYERCOUNT = "continueToInstCountSoftLayer";
    String GO_TO_COMMON_LAUNCH = "continueToLaunch";
    String GO_TO_TERREMARK_LAUNCH = "continueToLaunchTerremark";
    String GO_TO_OPENSTACK_LAUNCH = "continueToLaunchOpenStack";
    String GO_TO_SOFTLAYER_LAUNCH = "continueToLaunchSoftLayer";
    String GO_TO_PHYSICAL_SERVER_LAUNCH = "continueToLaunchPhysicalServer";
    String GO_TO_HV_LAUNCH = "hypervinstancedetails";
    String GO_TO_CREATE_INSTANCE = "backtocreateinstance";
    String GO_TO_VMWARE_COUNT = "vmwareinstancecount";
    String GO_TO_VMWARE_INST_COUNT = "vmwareinstcount";
    String GO_TO_SAVVIS_INST_COUNT = "savvisinstcount";
    String GO_TO_HV_COUNT = "hypervinstancecount";
    String GO_TO_CREATE_SEC_GROUP = "toCreateSecGroups";
    String GO_TO_USER_ROLES = "tousrroles";
    String GO_TO_CREATE_USER = "toadcreateuser";
    String GO_TO_AIX_CREATE_INSTANCE = "aixcreateinstance";
    String GO_TO_SAVVIS_CREATE_INSTANCE = "savviscreateinstance";
    String GO_TO_HV_CREATE_INSTANCE = "hypervcreateinstance";
    String GO_TO_VMWARE_LANUCH = "vmwareinstancedetails";
    String GO_TO_SAVVIS_CREATE_LAUNCH = "savvisinstdetails";
    String GO_TO_VMWARE_CREATE_LAUNCH = "vmwareinstdetails";
    String GO_TO_CREATE_POLICY = "createpolicy";
    String GO_TO_EDIT_POLICY = "editpolicy";
    String GO_TO_CREATE_INTELL_POLICY = "createintelligentpolicy";
    String GO_TO_CREATE_INTELL_POLICY_AWS = "createintelligentpolicyaws";
    String GO_TO_CREATE_SCALLING_POLICY = "gotoCreateScalingPolicy";
    String GO_TO_CREATE_APP_PROF = "gotoCreateAppProfile";
    String GO_TO_AUTOMATION_POLICY = "automationPolicy";
    String GO_TO_SELECT_SERVICE_PROFILE = "gotoSelectServiceProfile";
    String GO_TO_AZURE_CREATE_INSTANCE = "azurecreateinstance";
    String GO_TO_TERREMARK_CREATE_INSTANCE = "terremarkcreateinstance";
    String GO_TO_OPENSTACK_CREATE_INSTANCE = "openstackcreateinstance";
    String GO_TO_SOFTLAYER_INSTANCE = "softlayercreateinstance";
    String GO_TO_PHYSICAL_SERVER_CREATE_INSTANCE = "physicalservercreateinstance";
    String GO_TO_CREATE_STORAGE_PROFILE = "createStorageProfile";
    String LOAD_PR_AP = "provisioning/list-applications.cl";
    String GO_TO_CREATE_APPLICATION_PAGE = "provisioning/create-application.cl?isfirst=true";
    String LOAD_SY_PR ="security/role-permission.cl";
    String LOAD_SY_FR ="security/list-security-groups.cl";
    String LOAD_ST_UG="settings/list-usergroups.cl";
    String LOAD_ST_CL="settings/cloud-settings.cl";
    String LOAD_PR_DS="provisioning/list-datastores.cl?isfirst=true";
    String LOAD_PR_IMG="provisioning/list-images.cl?isfirst=true";
    String LOAD_ST_CL_IS="settings/cloud-settings.cl?isfirst=true";
    String LOAD_ST_USER="settings/list-users.cl";
    String LOAD_ST_USERGROUP="settings/list-usergroups.cl";
    String LOAD_ST_DP="settings/list-deployments.cl?isfirst=true";
    String LOAD_ST_IG="settings/list-appgroups.cl?isfirst=true";
    String GO_TO_CREATE_APP_LAUNCH="provisioning/create-application-launch.cl";
    
    String GO_TO_SNAPSHOT_PAGE="provisioning/instance-snapshot.cl?isfirst=true";
    String GO_TO_LIST_LB_INSTANCE_PAGE="provisioning/list-all-load-balancer-instances.cl?isfirst";
    String GO_TO_APPLY_MON_AZURE_PAGE = "monitoring/apply-azure-monitoring-profile.cl?isfirst=true";
    String GO_TO_APPLY_MON_OBJ_STORAGE_PAGE = "monitoring/apply-azure-object-storage.cl?isfirst=true";
    String GO_TO_APPLY_MON_PAGE = "monitoring/apply-monitoring-profile.cl";
    String GO_TO_APPLY_MON_LB_PAGE = "monitoring/apply-lb-monitoring-profile.cl?isfirst=true";
    String GO_TO_APPLY_MON_DATABASE_SERVICES_PAGE = "monitoring/apply-database-services-monitoring-profile.cl?isfirst=true";
    String GO_TO_S3_ALL_S3_BUCKET_EXP_PAGE="provisioning/list-all-s3bucket-expiration-rules.cl?isfirst=true";
    String GO_TO_CREATE_NETAPP_STORAGE_PAGE = "provisioning/create-netapp-storage.cl?isfirst=true";
    String GO_TO_USER_ROLES_PAGE="settings/user-roles.cl?isfirst=true";
    String GO_TO_CREATE_DATESTORE_PAGE="provisioning/create-datastore.cl?isfirst=true";
    String GO_TO_LIST_LB_PAGE="provisioning/list-load-balancers.cl";
    String GO_TO_CREATE_EBS_VOLUME_PAGE="provisioning/create-ebsvolume.cl?isfirst=true";
    String GO_TO_CREATE_FROM_VHD_PAGE="provisioning/create-from-vhd.cl?isfirst=true";
    String GO_TO_CREATE_IMAGE_PAGE="provisioning/add-image.cl?isfirst=true";
    
    String GO_TO_APPLY_MON_STORAGE_PAGE="monitoring/apply-storage-monitoring-profile.cl?isfirst=true";
    String GO_TO_MANAGE_ENDPOINT_PAGE="security/list-endpoints.cl?isfirst=true";
    String GO_TO_MANAGE_ACL_PAGE="security/list-acl.cl?isfirst=true";
    String GO_TO_APPLY_MON_PLATFORM_SERVICES_PAGE="monitoring/apply-platform-services-monitoring-profile.cl?isfirst=true";
    String GO_TO_AZURE_STORAGE_CONTAINER_PAGE="provisioning/list-storage-containers.cl?isfirst=true";
    String GO_TO_AZURE_STORAGE_BLOB_PAGE="provisioning/list-storage-blobs.cl?isfirst=true";
    String GO_TO_EDIT_APP_PROF_PAGE="policies/create-appprofile.cl";
    
    String GO_TO_BASIC_DIAGNOSTICS = "loadBasicDiagnostics";
    String GO_TO_DEEP_DIAGNOSTICS = "loadDeepDiagnostics";
    String GO_TO_COMPLIANCE_POLICIES = "listCompliancePolicies";
    String GO_TO_LIST_AUTOPOLICIES = "listAutoPolicies";
    String GO_TO_MONITORING_POLICIES = "listMonPolicies";
    String GO_TO_PROV_POLICIES = "listProvPolicies";
    String GO_TO_USERGROUPS = "usergroups";
    String GO_TO_USERGRPS = "userGrps";
    String GO_TO_ALERTS_NOTIFICATION = "alertsnotification";
    String GO_TO_S3 = "S3";
    String GO_TO_APPLICATION_DISCOVERY = "applicationDiscovery";
    String GO_TO_CLOUDSETTINGS = "cloudSettings";
    String HELP_FOLDER_LOC ="#Cloud360 Help"; 
    String CLOUD360_LOGO_PATH = "/images/common/";
    String LIST_PLATFORM_SERVICES = "provisioning/list-platformServices.cl?isfirst=true";
    String Auth_URL = "template/auth-expired.cl";
    //Login
    //Dashboard
    //Provisoning
    //Automation
    //Monitoring
    //Advisor
    //Security
    //Settings
}
