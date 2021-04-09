package com.cognizant.cloudone.ui.constants;


/**
 * General String constants
 * 
 */
public interface StringConstants {
	String RETURN_URL = "ru";
	String HEADER_AJAXREQ_KEY = "X-Requested-With";
	String HEADER_AJAXREQ_VALUE = "XMLHttpRequest";
	String INSTANCES_COLUMN_NICKNAME = "nickName";
	String INSTANCES_COLUMN_UPDATEDATE = "updateDate";
	String INSTANCES_COLUMN_CREATEDATE = "createDT";
	String URL_SOURCE_PARAM = ".src";
	String STANDARD_VOLUME_COLUMN_NAME = "name";
	String STORAGE_PROFILES_PG_NAME = "StorageProfiles";
	String DATASTORE_PG_NAME = "ListDatastores";
	String CREATENETAPPSTORAGE="CreateNetAppStorage";
	String SHOW_LOGIN_PAGE = "SHOW_LOGIN_PAGE";
	
	//JIRA:CO-2658
	String PDF_SORTING_WITH_NAME = "name";
	String PDF_SORTING_WITH_GROUPNAME_APPGROUPS = "groupName";
	String PDF_SORTING_WITH_ROLE = "role";
	String PDF_SORTING_WITH_DEPLOYMENTNAME = "deploymentName";
	String PDF_SORTING_WITH_SNAPSHOTID = "snapshotId";
	String PDF_SORTING_WITH_VOLUMEID = "volumeID";
	String PDF_SORTING_WITH_TASKNAME = "taskName";
	String PDF_SORTING_WITH_GROUPNAME_USRGROUPS = "groupName";
	String PDF_SORTING_WITH_POLICYNAME = "policyName";
	String PDF_SORTING_WITH_BUCKETNAME = "bucketName";
	String PDF_SORTING_WITH_FIRSTNAME = "firstName";
	String PDF_SORTING_WITH_KEYPAIRNAME = "keyPairName";
	String PDF_SORTING_WITH_CREATEDDATE = "createdDate";
	String SORT_IN_DATE_FORMAT = "sortDate1";
	
	String MESSAGES_BUNDLE = "message.messages";
	String ERRORS_BUNDLE = "error.errors";
	String LABELS_BUNDLE = "label.labels";
	String ENV_PROPERTIES = "environment";
	String LOGIN_INFO = "login_userInfo";
	String LOGIN_ID = "login_userID";
	String DO_NOT_NOTIFY="-- Do Not Notify --";
	String PAGE_NAME = "page_name";
	String FAILED_USER_NAME = "failedUserName";
	String FLOW_NAME = "flowName";
	String SESSION_TIMEOUT = "sessionTimeOut";
	
	// Constants for ListInstanceController
	// ----------------------------------------------------------------
	String DEFAULT_SORT_FIELD = "instanceName";
	String DEFAULT_TRUE_STRING = "true";
	String DEFAULT_ON_STRING = "on";
	boolean DEFAULT_SORT_ASCENDING = true;
	boolean DEFAULT_SELECT_MULTIPLE = true;
	boolean DEFAULT_SELECT_ALL = false;
	int DEFAULT_ADD_COUNT = 1;
	int DEFAULT_TABLE_ROWS = 10;
	int SELECTED_ITEM_COUNT = 20;

	String SEVERITY_LEVEL_OK = "Ok";
	String SEVERITY_LEVEL_CRITICAL = "Critical";
	String SEVERITY_LEVEL_WARNING = "Warning";
	String SEVERITY_LEVEL_IDLE = "Idle";
	String SEVERITY_LEVEL_INFORMATION = "Information";
	String SEVERITY_LEVEL_NA = "N/A";
	String SEVERITY_LEVEL_MAP_NA = "NA";
	String INSTANCES_COUNT = "InstanceCount";
	String CREATEINSTANCETO = "CREATEINSTANCETO";
	String SELECTEDINSTANCE = "SELECTEDINSTANCE";
	String PREREQUISITEINFOTO = "PREREQUISITEINFOTO";
	String OPERATION = "OPERATION";
	String IPADDRESS_NA="N/A";
	String NULL = "null";
	
	String IP_POLICY_ACTION = "IP_POLICY_ACTION";
	String IPP_ACTION_CREATE = "Create";
	String IPP_ACTION_UPDATE = "Update";
	String AWS_PLACEMENT_POLICY_PAGE = "gotoAWSPlacementPolicy";
	String PLACEMENT_POLICY_PAGE = "gotoPlacementPolicy";
	String SELECTED_PROVIDER_ID = "providerID";
	String SELECTED_PROVIDER_TYPE = "providerType";
	
	String DEPLOYMENT_FILTER_OPTIONS ="DEPLOYMENT_FILTER_OPTIONS";
	String GROUP_FILTER_OPTIONS ="GROUP_FILTER_OPTIONS";
	String STATE_FILTER_OPTIONS ="STATE_FILTER_OPTIONS";
	String STATUS_FILTER_OPTIONS ="STATUS_FILTER_OPTIONS";
	String REGION_FILTER_OPTIONS ="REGION_FILTER_OPTIONS";
	String ZONE_FILTER_OPTIONS ="ZONE_FILTER_OPTIONS";
	
	String PARAMETER_FILTER_OPTIONS ="PARAMETER_FILTER_OPTIONS";
	String AKNOWLEDGED_FILTER_OPTIONS ="AKNOWLEDGED_FILTER_OPTIONS";
	
	String SELECTED_DEPLOYMENT_FILTER ="SELECTED_DEPLOYMENT_FILTER";
	String SELECTED_GROUP_FILTER ="SELECTED_GROUP_FILTER";
	String SELECTED_STATE_FILTER ="SELECTED_STATE_FILTER";
	String SELECTED_STATUS_FILTER ="SELECTED_STATUS_FILTER";
	String SELECTED_REGION_FILTER ="SELECTED_REGION_FILTER";
	String SELECTED_ZONE_FILTER ="SELECTED_ZONE_FILTER";
	
	String SELECTED_PARAMETER_FILTER ="SELECTED_PARAMETER_FILTER";
	String SELECTED_AKNOWLEDGED_FILTER ="SELECTED_AKNOWLEDGED_FILTER";
	String GET_SEARCH_CONDITION = "getCondition";
	String GET_SEARCH_KEYWORDS = "getkeyWords";
	String GET_SEARCH_VALUES = "getValues";
	
	//Added for Job Management Use case
	String AUDITLOG = "AuditLog";
	String SCHEDULEDTASKS = "ScheduledTasks";
	String TASK_FILTER_OPTIONS ="TASK_FILTER_OPTIONS";
	String TARGET_FILTER_OPTIONS ="TARGET_FILTER_OPTIONS";
	String TARGET_DEPLOYMENT_FILTER_OPTIONS ="TARGET_DEPLOYMENT_FILTER_OPTIONS";
	String TARGET_GROUP_FILTER_OPTIONS ="TARGET_GROUP_FILTER_OPTIONS";
	String TASK_STATUS_FILTER_OPTIONS ="TASK_STATUS_FILTER_OPTIONS";
	String SELECTED_JOB_MGMNT_DATA="SELECTED_JOB_MGMNT_DATA";	
	
	String SELECTED_TASK_FILTER ="SELECTED_TASK_FILTER";
	String SELECTED_TARGET_FILTER ="SELECTED_TARGET_FILTER";
	String SELECTED_TARGET_DEPLOYMENT_FILTER ="SELECTED_TARGET_DEPLOYMENT_FILTER";
	String SELECTED_TARGET_GROUP_FILTER ="SELECTED_TARGET_GROUP_FILTER";
	String SELECTED_TASK_STATUS_FILTER ="SELECTED_TASK_STATUS_FILTER";
	
	String SELECTED_VOLUME = "selected_volume";
	String SELECTED_ZONE = "selected_Zone";
	String SELECTED_OS = "selected_OS";
	String SELECTED_INSTANCE = "selected_instance";
	String SELECTED_SERVICE_TYPE = "selected_service_type";
	
	String IMPORT_USER_SEARCH_PARAM = "import_user_search_Param";
	String SELECT_AZ_STORAGE_ACCOUNT = "selectedAzureStorageAccount";
	String SELECT_AZ_STORAGE_CONTAINER = "selectedAzureStorageContainer";
	String APP_TIERS = "appTiers";
	String APP_TIERS_DYNA_DATA = "appTiersDynaData";
	String APP_PROFILE = "applicationProfileTO";
	String APPLICATIONS = "applications";
	String APP_TYPE = "app_type";
	String AZURE_CLOUD_DETAILS ="azure_cloud_dtails";
	
	 // Constants for EventController
	 
	 int HOUR_CONSTANT = 24;
	 String ONE_HOUR = "1";
	 String FOUR_HOURS= "4";
	 String TWENTY_FOUR_HOURS= "24";
	 String SEVEN_DAYS= "7";
	 String THIRTY_DAYS= "30";
	 String ONE_HOUR_LABEL = "1h";
	 String FOUR_HOURS_LABEL= "4h";
	 String EIGHT_HOURS_LABEL= "8h";
	 String TWENTY_FOUR_HOURS_LABEL= "1d";
	 String SEVEN_DAYS_LABEL= "7d";
	 String THIRTY_DAYS_LABEL= "1m";
	 String CONSTANT_TO_MATCH_DATE_FORMAT= ".0";
	 String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	 String DATE_FORMAT_FOR_TIME_ZONE = "MMM d,yyyy HH:mm:ss zzz";
	 String OPEN_STATE= "open";
	 String DAY = "d";
	 String MONTH = "m";
	 String TRUE = "true";
	 String FALSE = "false";
	 //Date Time properties
	 String TIME_ZONE_GMT = "GMT";
	 String DATE_FORMAT_FOR_GMT = "MMM d HH:mm z";
	 String SOURCE_DATE_FORMAT_FOR_GMT = "yyyy-mm-dd HH:mm:ss";
	 //for Recent Task Controller
	 int INDEX_TO_FIRST_ELEMENT_OF_LIST = 0;
	 
	 //Cookie Names
	 String USER_SESSION_COOKIE = "COU";
	 
	//constants for Header and Top menu display
	 String COMMA = ", ";
	 String AMPERSAND = " & ";
	 String THREEDOTS = "...)";
	 String SERVICEDBY = "Serviced by ";
	 String OPENBRACKET = "(";
	 String CLOSEBRACKET = ")";
	 String HIDE = "HIDE";
	 String SHOW = "SHOW";
	 String AMAZON = "Amazon";
	 String CHARGESDUE = " | Charges Due: $";
	 
	 //Constants for VMWareCreateInstanceController
	 String PREREQUISITEINFO = "PREREQUISITEINFO";
	String CREATEINSTANCEINFO = "CREATEINSTANCEINFO";
	 String SELECTEDDATASTOREID = "SELECTEDDATASTOREID";
	 String DATASTORE_NAME_FOR_SORT = "dsName";	
	 String INSTANCESLIST = "INSTANCESLIST";
	 String DEFAULT_OS = "winNetStandardGuest";
	 int PARAM_1024 = 1024;
	 String UNIT_MB = "MB";
	 String UNIT_GB = "GB";
	 String UNIT_TB = "TB";
	 String SUPPORTED = "Supported";
	 String NOTSUPPORTED = "Not Supported";
	 String MULTIPLEHOSTS = "Multiple hosts";
	 String SINGLEHOST = "Single host";
	 String GROUP = "group";
	 String DEPLOYMENT = "deployment";
	 int MAX_INSTANCE_SIZE=20;
	 
	 //constants for CreateInstanceController
	 String CREATE_INSTANCE ="createinstance";
	 String CREATEINSTANCE_EDITED="CreateInstanceEdited";
	 String INSTANCE_INFO="instance";
	 String STORAGE_PROFILE_CONF ="storageProfileConf";
	 String NETAPP_STORAGE_PROFILE_CATALOG ="netappstorageProfileCatalog";
	 String NETAPP_STORAGE_CONTINUE ="netappstoragecontinue";
	 String INSTANCE_SELECTED_VALUE ="instanceSelectedValue";
	 String ATTACH_INSTANCE_LIST ="attachInstanceList";
	 String STORAGE_TYPE="storage_type";
	 String PROTOCOL="protocol";
	 String ROOTDISKTO_FROM_IMAGE="rootdiskfromimage";
	 String GATEWAYSUBNET ="GatewaySubnet";

	 String POWERON = "POWERON";
	 String SHUTDOWN = "SHUTDOWN";
	 String RESTART = "RESTART";
	 String DELETE = "DELETE";
	 String START = "START";
	 String STOP = "STOP";
	 String REBOOT = "REBOOT";
	 String TERMINATE = "TERMINATE";
	 String POWEROFF = "POWEROFF";
	 String REMOVE = "REMOVE";
	 String INSTALLMONITORAGENT= "INSTALLMONITORAGENT";
	 String RUNNING= "RUNNING";
	 String DELETED="DELETED";
	 
	 //constants for Worker Queue - Task Description
	 String START_INSTANCE = "Start Instance";
	 String POWER_ON_INSTANCE = "Power On Instance";
	 String STOP_INSTANCE = "Stop Instance";
	 String SHUTDOWN_INSTANCE = "Shutdown Instance";
	 String REBOOT_INSTANCE = "Reboot Instance";
	 String RESTART_INSTANCE = "Restart Instance";
	 String TERMINATE_INSTANCE = "Terminate Instance";
	 String DELETE_INSTANCE = "Delete Instance";
	 
	 String NETAPPIDFORCOMP = "netAppIdForComp";
	 String NETAPPNAMEFORCOMP = "netAppNameForComp";
	 String CREATEINSTANCETOPP = "CREATEINSTANCETOPP";
	 String PLACEMENTPOLICYLIST	= "placementPolicyList";
	 
	 String AZURE_SELECTED_STORAGE_LIST = "AZURE_SELECTED_STORAGE_LIST";
	 String SELECTED_COMP_PROF_DETAILS = "Compute_Profile_Details";
	 String SERVICE_PROFILE_LIST = "serviceProfileList";
	 String LIST_OF_INSTANCE_BASED_ON_SEL_APP = "listOfInstanceBasedOnApplicaiton";
	 
	 String APPROVE = "approve";
	 String REJECT = "reject";
	 
	 //Constants for Monitoring Template
	 public static final String CREATE_NEW_TEMPLATE="NEW_TMPL";
	 public static final String UPDATE_EXISTING_TEMPLATE="UPDATE_TMPL";
	 public static final String DELETE_EXISTING_TEMPLATE="DELETE_TMPL";
	 public static final String MONITORING_PARAM_TREE_NODES = "monitorParamTreeNodes";
	 public static final String MONITORING_CUSTOM_PARAMS = "monitoring_custom_parameters";
	 public static final String DEVICE_TYPE_ATTRIBUTE_DELIMITER = "-";
	 public static final String DEVICE_TYPE_JMX = "JMX";
	 public static final String MONITORING_PROFILE_ROOT_NODE = "MONITORING_PROFILE_ROOT_NODE";
	 public static final String MONITORING_PROFILE_ROOT_NODE_NAME = "Monitoring Parameters";
	 public static final String MONITORING_TYPE = "monitoring_type";
	 public static final int DEFAULT_NOTIFICATION_ID = -1;
	 public static final String APPLY_MP_VALID_INSTANCES = "APPLY_MP_VALID_INSTANCES";
	 public static final String ALREADY_APPLIED_INSTANCES = "alreadyAppliedInstances";
	 public static final String BLANK_STRING = "";
	 public static final String MONITORING_PROFILE_TO = "monitorTemplate";
	 public static final String MONITORING_PROFILE_TO_EDIT = "clonedTemplate";//Fix for CO-12227
	 public static final String MONITORING_DEVICE_TYPE = "MONITORING_DEVICE_TYPE";
	 public static final String DEVICE_TYPE = "deviceType";
	 public static final String OPTIONAL_ARGUMENTS = "optionalArguments";
	 public static final String TEMPLATEPARAMVALTO_CRITICAL = "tempCrictical";
	 public static final String TEMPLATEPARAMVALTO_WARNING = "tempWarning";
	 public static final String TEMPLATEPARAMVALTO_IDLE = "tempIdle";
	 public static final String CATALINA = "Catalina";
	 public static final String INTEGER = "int";
	 public static final String SERVICE_PARAMETER_NAME = "serviceParameterName";
	 public static final String MONITORING_PROFILE_DESCRIPTION = "templateDescription";
	 public static final String MONITORING_PROFILE_NOTIFICATION_ID = "templateNotificationID"; 
	 public static final String MONITORING_CUSTOM_PARAMETER_DEVICEATTRIBUTE = "script";
	 public static final String MONITORING_CUSTOM_PARAMETERS = "Custom Parameters";
	 public static final String CLONED_MONITOR_PARAM_LIST = "CLONED_MONITOR_PARAM_LIST";
	 public static final String APPLICATION_MONITORING = "AppMonitoring";
	 public static final String CREATE = "create";
	 public static final String EDIT = "edit";
	 public static final String EDIT_AWS_INSTANCE_STORAGE = "EDIT_AWS_INSTANCE_STORAGE";
	 public static final String DISPLAY = "display";
	 public static final String UPDATE = "update";
	 public static final String CANONICAL_NAME = "canonicalName";
	 public static final String DISPLAY_APP_NAME = "disAppName";
	 public static final String MONITOR_TYPE = "monitorType";
	 public static final String ENABLE_JMX_AGENT_ACTION = "ENABLEJMXAGENTID";
	 public static final String THRESHOLD_TXT_DURATION = "exceeds %value %unit for %duration sec";
	 public static final String THRESHOLD_TXT_DURATION_FOR_SERVICE_STATE = "%value for %duration second(s)";
	 public static final String THRESHOLD_TXT_ATTEMPTS = "exceeds %value %unit for consecutive %duration attempts";
	 public static final String THRESHOLD_TXT_STATUS_CODE = "is %value for consecutive %duration attempts";
	 public static final String STATUS_CODE = "Status Code";
	 public static final String RESPONSE_TIME = "Response Time";//Fix for 10663.
	 public static final String MONITORING_INSTANCES = "Monitoring Instances";
	 public static final String APPLICATION_PROFILES = "Application Profile(s)";
	 public static final String OLD_DEVICE_NAME = "oldDeviceName";
	 public static final String NOT_APPLICABLE = "-NA-";
	 public static final String AzureCloudServices = "AzureCloudServices";
	 public static final String AWSRDS = "RDS";	 
	 public static final String AWSELB = "ELB";	 
	 public static final String CloudWatch = "CloudWatch";	 
	 public static final String SERVICE="Service";//Fix for CO-10802
	 public static final String STATE="State";//Fix for CO-10292 - UI for service monitoring
	 public static final String PORT="Port";
	 public static final String STATUS="Status";
	 public static final String LOG="Log";
	 public static final String IIS_LOG="IIS Log";
	 public static final String Security="Security";
	 public static final String Login="Login";
	 public static final String Failure="Failure";
	 public static final String Memcached="Memcached";
	 public static final String LOGFILE="LogFile";
	 public static final String EVENTLOG="EventLog";
	 public static final String COMPUTE_INSTANCES="ComputeInstances";
	 
	 
	 //Constants for instanceAnalyticsController
	 String INSTANCE_ANALYTICSTO ="instanceAnalyticsToinfo";
	 
	 //constants for Cloud Setting DiagnosticsDetails
	 public static final String YYYYMMDDHHMMSS_FORMAT = "yyyy-MM-dd HH:mm:ss";
	 public static final String MMMDHHMM_FORMAT = "MMM d HH:mm:ss";
	 public static final String MANIFEST = "/META-INF/MANIFEST.MF";
	 public static final String HUDSON = "HudsonSvnRevision";
	 public static final String SETTINGS = "Settings";
	  
	 String SNAPSHOT_DETAILS = "snapshotDetails";
	 String INSTANCE_DETAILS_LIST = "instanceDetailsList";
	 
	 //Adde for Job Management use case
	 public static final String MESSAGE_ONE = "1";
	 public static final String MESSAGE_TWO = "2";
	 public static final String MESSAGE_THREE = "3";
	 
	 //Constants for notification controller 
	 String DEFAULT_NOTIFICATION =" (default)";
	 
	 //Constants for list user group controller
	 String GROUP_LIST="GROUP_LIST";
	 
	 String USER_GROUP_ID="GROUP_ID";
	 
	 String USR_ROLE_GRP_MASTER_LIST="USR_ROLE_GRP_MASTER_LIST";
	 
	 String USR_GRP_NAME="USR_GRP_NAME";
	 String USR_GRP_DESC="USR_GRP_DESC";
	 String USR_GRP_QUOTA="USR_GRP_QUOTA";
	 String USR_GRP_VISIBILITY="USR__GRP_VISIBILITY";
	 String USR_GRP_APPROVER_GROUP="USR_GRP_APPROVER_GROUP";
	 String USR_GRP_OPERATION="USR_GRP_OPERATION";
	 String USER_GROUP_READ_ONLY="USER_GROUP_READ_ONLY";
	 
	 public static final String DEFAULT_SELECTITEM = "-- Select --";
	 public static final String NO_SNAPSHOT_SELECTITEM = "-- No Snapshot --";
	 public static final String DEFAULT_SELECTITEM_VALUE = "-";
	 public static final String DEFAULT_SELECTITEM_NUMVALUE = "-999";
	 public static final String DO_NOT_MONITOR = "No profile - Do not monitor";
	 public static final String DEFAULT_ALLITEM_TEXT = "-- All --";
	 public static final String DEFAULT_ALLITEM_VALUE = "-";
	 public static final String DEFAULT_ANY_VALUE = "Any";
	 String POWERED_OFF="Powered Off";
	 public static final String poweredOn = "poweredOn";
	 
	 public static final String POLICYTO_DETAILS="policyto_details";
	 public static final String APPLICATIONTO_DETAILS = "APPLICATIONTO_DETAILS";
	 public static final String POLICY_NAME="Policy";
	 public static final String COMPLIANCE="COMPLIANCE";
	 public static final String POLICY_TYPE_SELECTED="policyTypeSelected";
	 
	 String DEFAULT_SELECTITEM_EBS_SS = "None";
	 String SELECTED_TREE_ELEMENT_ID = "id";
	 String SELECTED_TREE_ELEMENT_TYPE = "type";
	 String SELECTED_TREE_PROVIDER_ID = "providerId";
	 String SELECTED_TREE_PROVIDER_TYPE = "providerType";
	 String SELECTED_TREE_CLOUD_ID= "cloudId";
	 String SELECTED_TREE_NODE_NAME= "nodeName";
	 String SELECTED_TREE_PROFILE_BY_CLOUD = "profileByCloud";
	 String SELECTED_TREE_HIERARCHICAL_INFO = "hierarchicalInfo";
	 
	 String COMPUTE_INSTANCE_TEXT_KEY = "compareInstances_vmware_text_header";
	 String DEPLOYMENTS_TEXT_KEY = "deployements_text_header";
	 String JMX_APP_DISC_TEXT_KEY = "JMX_text_header";
	 String USER_GRPS_TEXT_KEY = "usergrps_text_header";
	 String APPGROUPS_TEXT_KEY = "appgroups_text_header";
	 String SERVICE_PROFILE_TEXT_KEY = "serviceProfiles_text_header";
	 String S3BUCKETS_TEXT_KEY = "s3buckets_text_header";
	 String S3BUCKETS_RULES_TEXT_KEY = "s3buckets_rules_text_header";
	 String KEYPAIR_TEXT_KEY = "keyPair_text_header";
	 String CONSUMPTION_METERING_TEXT_KEY = "consumption_metering_text_header";
	 String ROL_PERM_CRTD_USR="ROL_PERM_CRTD	_USR";
	 String SELECT_AWS_KEYPAIR_VALUE = "existingkeypair";
	 String UNSELECT_AWS_KEYPAIR_VALUE = "nokeypair";
	 String NETWORKS_TEXT_KEY = "elasticip_text_header";
	 String ENI_TEXT_KEY = "list_instances_eni_network_interface_label";
	 String STORAGE_PROFILE_TEXT_KEY = "storage_Profile_dd_label_lb";
	 String DATABASE_SERVICES_TEXT_KEY = "database_services_text_header";
	 String STORAGE_TEXT_KEY = "storage_dd_label_lb";
	 
	 String EVENT_TEXT_KEY = "evnts_hdr_txt";
	 String MARKASACKGED = "evnts_mrk_ack";
	 String MARKASNEW = "evnts_mrk_new";
	 String DELETE_EVENT = "evnts_del";
	 String EVENT_MARK_AS_RESPONDED = "responded";
	 String EVENT_MARK_AS_RESOLVED = "evnts_mrk_resolved";
	 String DETAILS ="details";
	 
	 String JOB_MANAGEMENT_TEXT_KEY = "job_management_jobList_header";
	 String SECURITYGROUP_TEXT_KEY = "securityGroups_secGrpList_text_header";
	 String CLOUDSERVICES_TEXT_KEY = "cloud_services_cloudSrvList_text_header";
	 
	 String APPLICATION_POWER_ON ="application_power_on";
	 String APPLICATION_POWER_OFF ="application_power_off";
	 String APPLICATION_DELETED ="application_de_provision";
	 String APPLICATION_ENABLE_MONITROING ="application_enable_monitoring";
	 String APPLICATION_DISABLE_MONITROING ="application_disable_monitoring";
	 
	 //Define constant for Browser Support utility.
	 String USER_AGENT = "user-agent";
	 String ACCEPT = "Accept";
	 String MSIE = "msie";
	 String FIREFOX = "firefox";
	 String SAFARI = "safari";
	 String CHROME = "chrome";
	 String NETSCAPE = "netscape";
	 String PRISM = "prism";
	 public static final String COMMA_SEPARATOR = ",";
	 String CLOUD_PROV_LIST="CLOUD_PROV_LIST";
	 String CLOUD_SEC_RESOURCE_LIST="CLOUD_SEC_RESOURCE_LIST";
	 String SECONDARY_RESOURCE_LIST="SECONDARY_RESOURCE_LIST";
	 String CLOUD_PROV_TO_LIST = "CLOUD_PROV_TO_LIST";
	 String LIST_ADDED_CLOUD_PROVIDERS="LIST_ADDED_CLOUD_PROVIDERS";
	 String LIST_ADDED_CLOUD_SEC_PROVIDERS="LIST_ADDED_CLOUD_SEC_PROVIDERS";
	 String BILLINGSITE_VPDC_TO_LIST = "BILLINGSITE_VPDC_TO_LIST";
	 String EBS_VOLUMES = "ebsvolume_pdf_xls_text_header";
	 String AZURE_DATASTORES = "azure_pdf_xls_text_header";
     String VMWARE_DATASTORES = "datastore_pdf_xls_text_header";
     String POWERVM_STORAGEPOOLS = "powervm_pdf_xls_text_header";
	 String UNSUPPORTED_PAGE_URL = "site/unsupported-browser.cl";
	 
	 //Added for JIRA:3615
	 String AWS = "AWS";
	 String VCENTER = "vCenter";
	 String EUCA = "Euca";
	 String POWERVM = "PowerVM";
	 String AZURE = "Azure";
	 String AZURE_CLOUD_SERVICES = "Azure Cloud Services";
	 String INSTANCES_SELECTED="Instance_Selected";
	 
	 String MENU_PROVIDER_MAPPING = "MENU_PROVIDER_MAPPING" ; 
	 String DEFAULT_POWERVM_OS = "IBM Power - AIXLINUX";
	 String PROVIDER_LIST_FOR_CLOUD = "PROVIDER_LIST_FOR_CLOUD" ;
	 String SECONDARY_RESOURCE_LIST_FOR_CLOUD = "SECONDARY_RESOURCE_LIST_FOR_CLOUD" ;
	 
	 String FROM_AUTH_EXCEPTION_CONTROLLER = "authExceptionController" ;
	 
	 String SESSION_UNDEPLOY_PACKAGETO = "SESSION_UNDEPLOY_PACKAGETO";
	 
	 //Added for JIRA CO-3948
	 String SELECTED_IMAGE_OBJECT = "Image_Selected";
	 String AMI_PANEL_SAVED = "AMI_PANEL_SAVED";
	 String AMI_PANEL_WITH_NO_TMPL_ID = "AMI_PANEL_WITH_NO_TMPL_ID";
	 String SELECTED_AMI_ID = "ami_id";
	 String SELECTED_AMI_ID_PREV="ami_id_prev";
	 String SSH_INFO ="ssh_key";

	 	 // Added for JIRA CO-3121
	 String INTELLIPOLICY_TEXT_KEY = "Intellipolicy_text_header";
	 
	 //Constants for Service Profile
	 String SESSION_SERVICEPROFILETO = "serviceProfileTO";
	 String SESSION_FLOWINDICATOR = "flowIndicator";
	 
	 String SESSION_PROFILE_BEAN_COMP_PROF = "profilesBeanCompProfile";
	 
	 //Constants for chef
	 String SESSION_CHEFCHECK = "chefcheck";
	 String CHEFINSTALLED = "ChefInstalled";
	 
	 String LAST_NAME = "Last Name";
	 String FIRST_NAME = "First Name";
	 String USER_NAME = "Username";
	 String NAME = "Name";
	 String USER_GROUPS = "User Groups";
	 
	 
	 /** Constants for help information **/
	 String HELP_PATH = "help_location";
	 String HELP_FLAG = "help_flag";
	 String BUSINESS_POL_TYP="BUSINESS_POL_TYP";        
        String SELECTED_TREE_POLICY_TYPE = "policyType";
        String DIAGNOSTIC_TYPE="diagnostictype";
        
     String RESPONDED = "RESPONDED";
     
     //Storage Details
     String storageName = "Name: ";
     String volType = ", Volume Type: "; 
     String delOnTermination = ", Delete On Termination: ";
     String size = ", Size(GB): ";
     String device = ", Device: ";
     String sourceSnapshot = ", Source Snapshot: ";
     String sourceType = ", Source Type: ";
     String sourceName = ", Source Name: ";
     String OpenStackVolType = "Volume Type: ";
     String volIsEncrypted = ", Volume Encrypted: ";
     String brTag = "<br/>";
     
     String VMWAREFORMNAME = "vmWareInstanceFormId";
     String HYPERVFORMNAME = "hyperVInstanceFormId";
     String TERREMARKFORMNAME = "terremarkInstanceFormId";
     String AZUREFORMNAME = "azureCreateInstanceFormId";
     String OPENSTACKFORMNAME =  "openStackcreateInstanceFormId";
     String SOFTLAYERFORMNAME = "softLayerInstanceFormId";
     String SORTING_PACKAGE_NAME_ASC = "name";
     
     String SAVE = "Save";
     String SAVE_AS = "SaveAs";
     String AWSFORMNAME = "createInstanceFormId";	
     String INSTACTIONFORMNAME = "actionFormId";
     String KEYPAIRNAME = "KeyPair";
     String KEY_PAIR_NAME = "keyPairName";
     String POLICYTO_VALUES = "POLICYTO_VALUES";
     String SEC_GRP_NAME = "name";
     
     String CALL_ACTION_STRING = "calActionString";
     String CALL_ACTION_STRING_CONSTANTS = "\\u00a0";
     String RETURN_PAGE = "returnPage";
	 
     String ACTION = "action";
	 //Constant used for default duration to fetch consumption data 
	 public static final String DEFAULT_PERIOD = "4"; 

		 // User Roles Quota
	 String USR_GRP_QUOTA_UNL = "userQuotaUnlimited";
	 
	String SERVICE_PROFILE = "compute profiles";
	public static final String STORAGE_DISK_LIST="Storage_disk_list";
	public static final String VIRTUAL_DISK_LIST="virtual_disk_list";
	public static final String VIRTUAL_DISK_DETACHED_LIST="detached_disk_list";
	public static final String COMPLIANCE_DTLS="compliance_dtls";
	public static final String VIRTUAL_DISK_COUNT="virtual_disk_count";
	public static final String NEW_VIRTUAL_DISK = "NewVirtualDisk";
	public static final String RAW_DEVICE_MAPPING = "RawDeviceMapping";
	public static final String DISK_NAME="Hard disk ";
	public static final String TOTAL_DISKS="total_disks";
	public static final String TARGET_LUN_LIST="target_lun_list";
	public static final String REMOVED_LIST = "dis_remove_list";
	public static final String VIRTUAL_DISKCOMPAT="virtualmode";
	public static final String PHYSICAL_DISKCOMPAT="physicalmode";
	public static final String MARKED_FOR_DETACH="Marked for Detach";
	
        
        String NA = "N/A";
        String ZERO = "0";
        String ONE = "1";
		
        
    // AWS Storage - Volume
    
	public static final String EXISTING_EBS_VOL_DEVICE_MAPPING = "existingAWSVolMapping";
	public static final String DETACHED_VOLUME_IDS= "detachedVolumeIDS";
	public static final String AVAIL_VOL_LIST = "Avl_vol_list";
 	public static final String AVAIL_SNAP_LIST = "Avl_snap_list";
 	public static final String AVAIL_DISK_LIST = "Avl_disk_list";
 	public static final String ATTACHED_AZURE_DISK_LIST = "ATTACHED_AZURE_DISK_LIST";
    public static final String VOLUME_LIST="Volume_list";
    public static final String NETAPP_STORAGE_LIST="NetApp_Storage_list";
	public static final String COMPATIBLE_INSTANCE_TYPES="instance_types";
	public static final String SNAP_LIST_DATACENTER = "SNAP_LIST_DATACENTER";
    
    public static final String ON_OPEN_POPUP="on_popup_open";
    public static final String CUSTOM_CONFIG="custom";
    public static final String YESTERDAY="Yesterday";
    public static final String MONTHS = "month";
    public static final String WEEK="Week";
    public static final String SELECT="select";
    public static final String NOT_ATTACHED_INSTANCE = "Not Attached to any instance";
    public static final String ATTACHED_INSTANCE = "Attached to instance";
    public static final String INSTANCE_NOT_EXISTS="No instance available in zone";

	//Fix for JIRA CO-6009
	public static final String DATACENTER="Datacenter: ";
    public static final String HOST="Host: ";
    public static final String NETWORK="Network: ";
    public static final String KEY_NOT_MODIFIEFD ="On file. Click here to modify";
    
    //Fix for JIRA CO-6821
    public static final String LABEL="label";
    
    public static final String ALL="all";
    public static final String NEW="new";
    public static final String REFRESH="refresh";
    public static final String ACTIVE="active";
    public static final String ACTIVE_STORAGE="Active";
    public static final String INACTIVE="inactive";
    // Added for Application Profiles
    public static final String SESSION_APP_BEAN = "profilesBean";
    public static final String SESSION_UNMODIFIED_PROFILE_BEAN = "unModifiedProfilesBean";
    public static final String APPLICATION_OPERATION = "appOperation";
    public static final String SELECTED_APPLICATION = "selectedApplication";
    public static final String ENTITY_TYPE = "appEntityType";
    public static final String SELECT_APP_PROF = "selectedAppProf";
    public static final String ASSIGNED_IPs = "assignedIPs";
    public static final String SELECTED_APP_PROF = "scalingPolicyName";
    public static final String ENTERED_NAME = "enteredName";
    public static final String ADDITIONAL_COMMENTS = "additionalComments";
    public static final String APP_PROF = "Application Profile";
    public static final String APP_PROFS = "Application Profiles";
    public static final String VERSION = "Version";
    public static final String H_SIZE = "Size";
    public static final String H_SIZE_GB = "Size(GB)";
    public static final String DATABASE_SNAPSHOTS= "Database Snapshots";
    public static final String DATABASE_SERVICES = "Database Services";
    public static final String SIZE_GB = "GB";
    public static final String STORAGE_PROFILE="Storage Profiles";
    public static final String STORAGE_SNAPSHOT="Storage Snapshots";
    public static final String SNAP_SIZE="snapSize";
    public static final String EBS_SIZE="ebsSize";
    public static final String DS_CAPACITY="dsCapacity";
    
    public static final String SELECTED_LB = "SELECTED_LB";
    public static final String SELECTED_LB_NAME = "SELECTED_LB_NAME";
    
    
    // Added for Dynamic expiration rule naming
    public static final String DEFAULT_RULE_NAME = "Rule-";
    
    public static final String SEPARATOR = "Separator";
    public static final String BLANK_SPACE = " ";
    public static final String ALL_RECORDS = "All";
    public static final String FILTERED_RECORDS = "Filtered records";
    
	public static final String CHEF = "Chef";
	public static final String SCORCH_LB = "System Center Orchestrator";
	public static final String SCORCH = "SCORCH";

    // Diagnostics Page Param
    public static final String DIAGNOSTICS_REQUEST_PARAM="type";
    
    public static final String errorsMap="errorsMap";
    
    public static final String COMPLIANCE_NORMAL = "icon-no-compliance";
    public static final String COMPLIANCE_WARNING = "icon-compliance-full";
    public static final String COMPLIANCE_SATISFIED = "icon-no-compliance";
    
    //CO-11113 - Change
	public static final String SESSION_MONITORPROFID ="monitorprofileId";
	public static final String ENABLED="Enabled";
	public static final String DISABLED="Disabled";
	
	//Instance names during recurrence
	public static final String AUTO_GENERATED ="[auto-generated]";
	
	public static final String SESSION_NOTIFICATION_LIST = "notificationList";
	public static final String DATASTORES_FOR_HOST = "dataStoresForHost";
	
	public static final String YES = "Yes";
	public static final String NO = "No";
	public static final String IOPS = "IOPS";
	public static final String SESSION_CREATE_INSTANCE_NAMES = "instanceName";
	public static final String DEFAULT_APP_ICON_CLASS = "default_app_icon_class";
	
	//CO-10896 - Change
	public static final String SESSION_MONITORING_TYPE = "session_monitoring_type";
	public static final String SESSION_MONITORING_TYPE_ID = "session_monitoring_type_id";
	
	//CO-17094 - Change
	public static final String SESSION_SEARCH_PARAM = "session_search_param";
	public static final String SESSION_SUMMARY_OPTION = "session_summary_option";
	public static final String Monitoring_Profiles = "Monitoring Profiles";
	
	public static final String Management_Interfaces = "Management Interfaces";
	
	//Integration System Related Constants:
	public static final String  ticketingSystemName = "ticketingSystemName";
	public static final String  ticketForwardMode = "ticketForwardMode";
	public static final String  severityfilter = "severityfilter";
	public static final String  config = "config";
	public static final String  preference = "preference";
	
	public static final String  RECLAMATION_POLICY_LIST = "RECLAMATION_POLICY_LIST";
	public static final String  SELECTED_BIZ_POLICY_ID = "SELECTED_BIZ_POLICY_ID";
	public static final String  SELECTED_FILTER_ID = "SELECTED_FILTER_ID";
	public static final String  LOGIN_STATUS_UPDATED="Login Status Updated";
	public static final String  LOGIN_STATUS_NOT_UPDATED="Login Status Not Updated";
	public static final String  RESET_PASSWORD_URL="CLOUD_ONE_BASE_URL + /reset.cl";
	public static final String NO_PREFERENCE = "No Preference";
	public static final String NOT_IN_VPC = "Not in VPC";
	public static final String IP_ACTION_DELIMITER = "&&&";
	public static final String IP_STEP_DELIMITER = "@@@";
	public static final String MONITORING_BEAN="MONITORING_BEAN";
	public static final String DETAILS_BEAN="DETAILS_BEAN";
	public static final String HYPEN_DELIMITER="---";
	public static final String MTD = "mtd";
	public static final String FROM_PAGE="fromPage";
	public static final String INSTANCE_DETAILS="instanceDetails";
	public static final String INSTANCE_ACTION_PAGE="instanceActionsPage";
	public static final String TAGS="tags";
	public static final String IMPACT_AVAILABILITY = "Impacts Availability";
	public static final String NO_IMPACT_AVAILABILITY = "Does not Impact Availability";
	public static final String IS_CLUSTER_DATASTORE="IS_CLUSTER_DATASTORE";
	
	public static final String APPLICATION_DETAILS="applicationDetails";	
	public static final String PAGE_NAME_DTS="pageNameDetails";
	public static final String SSH_KEY_FILE_UPDATED="sshKeyUpdated";
	public static final String LOAD_BALANCER_PG_NAME="LoadBalancerAWS";
	public static final String LOAD_BALANCER_INSTANCES="LoadBalancerInstanceAWS";
	public static final String NETSCALER = "isNetscaler";
	public static final String SELECTEDTO_LB = "selectedLB";
	
	public static final String PROVISIONING_WORKFLOW="Provisioning-";
	
	//New entries for autocomplete use cases.
	public static final String REQUEST = "req";
	public static final String TYPE = "type";
	public static final String SUGGESTION = "suggestion";
	public static final String ERROR = "error";
	public static final String ENTITYTYPE = "entityType";
	public static final String TEXT_TYPE = "textType";
	public static final String TERM = "term";
	public static final String RULE_TYPE = "ruleType";
	
	public static final String CODE_SELECT_ITEM = "codeselect";
	
	public static final String BASIC_DIAGNOSTICS = "BASIC";
	public static final String DEEP_DIAGNOSTICS = "DEEP";
	
	public static final String STORAGE_PROFILE_NAME="selectedStorageProfileName";
	public static final String SCANNER_APPLIANCE_EXTERNAL="External";
	
	//Monitoring EntityTypes Constants -- Added for CO-17896
	public static final String MONTYPE_LOADBALANCERS = "LoadBalancer(s)";
	public static final String MONTYPE_COMPUTE_INSTANCES = "Compute Instance(s)";
	public static final String MONTYPE_DATABASE_SERVICES = "Database Service(s)";
	public static final String MONTYPE_HOSTED_SERVICES = "Hosted Service(s)"; 
	
	//Added for CO-18443
	public static final String MONTYPE_AZURE_SERVICES = "Azure Cloud Service(s)"; 
	
	//Added for CO-18800
	public static final String MONTYPE_PLATFORM_SERVICES = "Platform Service(s)"; 
	
	
	
	// Added for JIRA CO-18524
	public static final String SELECTED_BILLING_PERIOD = "selectedBillingPeriod";
	public static final String CURRENT_MONTH = "Current Month";
	public static final String USER_ACTIVITY="User Activity";
	public static final String PRODUCT_USAGE="Product Usage";
	
	//Monitoring EntityTypes Constants -- Added for CO-18634
	public static final String STORAGE = "Storage(s)";
	
	//Patch Management
	public static final String DEFAULT_SUMMARY_FILTER = "state__Pending";
	 
	//Azure Endpoint
	public static final String SELECTED_AZURE_INSTANCE = "SELECTED_AZURE_INSTANCE";
	public static final String SELECTED_SECURITY_GROUP_VALUE = "SELECTED_SECURITY_GROUP_VALUE";
	public static final String SELECTED_ENDPOINT = "SELECTED_ENDPOINT";
	public static final String SELECTED_ACL = "SELECTED_ACL";
	public static final String ACL_ACTION = "ACL_ACTION";
	
		//Added for jira CO-12050
	public static final String INSTANCE_TEMPLATE_NAME = "instanceTemplateName";
	
	//Subnet Selection for Policy
	public static final String POLICY_SELECTED_WITH_ANY_SUBNET = "Subnet Will be selected through Policy";
	
	public static final String SCALING_POLICY_NAME = "scalingPolicyName";
	public static final String SELECTED_PATCHES = "SELECTED_PATCHES";
	public static final String SELECTED_PATCH_ACTION = "SELECTED_PATCH_ACTION";
	
	public static final String SNAPSHOT_TYPE_OWNED_BY_ME="snapshotType__Owned";
	public static final String SNAPSHOT_TYPE_ALL_SNAPSHOTS="snapshotType__All";
	public static final String SNAPSHOT_TYPE_MY_AMI_SNAPSHOTS="snapshotType__My";
	public static final String SNAPSHOT_TYPE_OWNED="snapshotType__Owned By Me";
	
	public static final String OS = "OS";
	public static final String PORTABILITY_ARCHITECTURELIST = "architectureList";
	public static final String PORTABILITY_NETWORKLIST = "networkList";
	public static final String NEW_PREREQUISITETO = "newprerequisiteTO";
	public static final String APPLY_PATCH = "Apply Patch";
	public static final String PATCH_NOT_APPLICABLE = "Not Applicable";
	public static final String PATCH_APPLY_PREFERENCE_SEQUENTIAL= "sequential";
	public static final String PATCH_DEFAULT_SUMMARY_FILTER= "patchApprovalStatus_Unapproved__patchStatuses_NoStatus__multi_summary_filter";
	public static final String SUBNETS = "subnets";
	//Changemonitoring constants
	public static final String SERVICE_LIST ="serviceList";
	public static final String NOTIFICATION_LIST = "notificationListForChangeMonitoring";
	public static final String DELPLOYMENT_LIST = "deploymentList";
	public static final String INSTANCEGROUP_LIST = "instanceGroup";
	public static final String CHANGE_MONITORING_LOGINSESSION = "changeMonitoringSession";
	public static final String RESOURCE_POOL = "ResourcePool";
	
	//Added for JIRA CO-21129
	public static final String OBJECT_STORAGE = "Object Storage(s)";
	public static final String WEBAPPSERVICES = "WebAppService(s)";
	public static final String AZURE_FILTER = "Storage,Database,Performance Counter";
	public static final String SOFTLAYERBEANSESSION = "SoftLayerBean";
	
	// Added for Jira CO-20730
	public static final String PROVISIONING_POLICIES_MODIFY = "Provisioning Policies - Modify";
	public static final String AUTOMATION_POLICIES_MODIFY = "Automation Policies - Modify";
	public static final String MONITORING_POLICIES_MODIFY = "Monitoring Policies - Modify";
	public static final String COMPLIANCE_POLICIES_MODIFY = "Compliance Policies - Modify";
	
	public static final String PROVISIONING_POLICIES_CREATE = "Provisioning Policies - Create";
	public static final String AUTOMATION_POLICIES_CREATE = "Automation Policies - Create";
	public static final String MONITORING_POLICIES_CREATE = "Monitoring Policies - Create";
	public static final String COMPLIANCE_POLICIES_CREATE = "Compliance Policies - Create";
	
	public static final String POLICY_ACTION_SELECTED="policyActionSelected";
	//CO-21327 - Changes
	public static final String REMOVE_MONITORING_PROFILE="REMOVE_MONITORING_PROFILE";
	
	public static final String SORT_ORDER_STORAGE="sortOrderStorage";
	public static final String SORT_BY_STORAGE="sortByStorage";
	public static final String SELECTED_ACTION = "selectedAction";
	public static final String VIRTUAL_MACHINE = "Virtual Machine";
	public static final String BARE_METAL = "BareMetal";
	public static final String SYSTEMTYPE_SELECTED = "SystemTypeSelected";
	
	public static final String INTEGRATION_SYSTEM_NAME = "name";
	public static final String DATACENTER_LIST = "DataCenterList";
	public static final String DATACENTER_MAP = "DataCenterMap";
}
