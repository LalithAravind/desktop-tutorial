/**
 * 
 */
package com.cognizant.cloudone.kernel.constants;

/**
 * @author 313021
 * 
 */
public interface GlobalConstant {
	String APP_NAME = "app-name";
	String COMMA = ", ";
	String AMPERSAND = " & ";
	String THREEDOTS = "...)";
	String SERVICEDBY = "Serviced by ";
	String OPENBRACKET = "(";
	String CLOSEBRACKET = ")";
	String HIDE = "HIDE";
	String SHOW = "SHOW";
	String REQUEST_PARAM = "p";
	String MQ_BROKER_URL = "mq_broker_url";
	String WF_CALLBACK_EXEC_ASYNC = "wf_callback_exec_async";

	String UNCLASSIFIED_DEPLOYMENT_OR_APPGROUP = "Unclassified";
	int DEFAULT_VM_COUNT = 10;
	String SELF = "Notify Me";
	String CLOUDONE_HOME = "CLOUDONE_HOME";
	// this constant will be used for the place-holder for pdf/excel doc builder
	String PDF_XL_SESSION_DATA = "DATA_LIST";
	String PDF_XL_SESSION_PARAM = "PARAM_MAP";
	String PDF_XL_SESSION_ENTITY = "DATAGRID_ENTITY_TYPE";
	String PDF_XL_SESSION_PAGENAME = "PDF_XL_SESSION_PAGENAME";
	String PDF_XL_SESSION_PAGE_ENUM = "PDF_XL_SESSION_PAGE_ENUM";
	String XL_SESSION_DATA = "XL_DATA_LIST";
	// Search related
	String SESSION_SEARCH_TEXT = "SearchString";
	String WEBSERVICE_URL = "WebService_URL";
	String DEFAULT_NAGIOS_CONTACT = "admins";
	String AUTH_MECHANISM_LABEL = "Authentication Mechanism";
	String USER_DN_TEMPLATE = "User DN Template";
	String CONTEXT_FACTORY_URL = "Context Factory URL";
	String DOMAIN_PREFIX = "domain=";
	String USERID_PREFIX = "userid=";
	String USERNAME_PREFIX = "username=";
	String REQURL_PREFIX = "requrl=";
	String CREATED_AT = "Created At";
	String CREATED_BY = "Created By";
	String CHANGE_SETTINGS = "Change Settings";
	String CHANGE_TIME_ZONE = "Change Time Zone";
	// Modified for including additional fields in cloud settings page

	final String ZERO = "0";
	String APP_PROFILES_LIST = "APP_PROFILES_LIST";
	String YES = "Yes";
	String NO = "No";
	String AND = "AND";

	String REFERAL = "Referal";
	String SERVICE_USER_NAME = "Service Username";
	String SERVICE_PASSWORD = "Service Password";
	String SECURITY_PRINCIPAL = "Security Principal";
	String SESSION_LISTINSTANCE = "LISTINSTANCESESSION";
	String MONITORING_PROFILES_SELECTITEM = "MONITORING_PROFILES_SELECTITEM";
	String SESSION_LIST_EBS_VOLUMES = "LIST_EBS_VOLUMES";
	String SESSION_DATASTORES_LIST_EBS_VOLUMES = "LIST_DATASTORE_EBS_VOLUMES";
	String SESSION_DATASTORES_LIST_POWERVM_STORAGEPOOLS = "LIST_DATASTORE_POWERVM_STORAGEPOOLS";
	String SESSION_DATASTORES_LIST_VMWARE_DATASTORES = "LIST_DATASTORE_VMWARE_DATASTORES";
	String SESSION_CREATE_DATASTORES_PREREQUISITE_TO = "CREATE_DATASTORES_PREREQUISITE_DETAILS";
	String SESSION_DATASTORES_SELECTED_PROVIDER_TYPE = "DATASTORES_SELECTED_PROVIDER_TYPE";
	String SESSION_DATASTORES_SELECTED_PROVIDER_ID = "DATASTORES_SELECTED_PROVIDER_ID";
	String SESSION_DATASTORES_LAST_SELECTED_PROVIDER_TYPE = "DATASTORES_LAST_SELECTED_PROVIDER_TYPE";
	String SESSION_DATASTORES_LAST_SELECTED_PROVIDER_ID = "DATASTORES_LAST_SELECTED_PROVIDER_ID";
	String SESSION_LIST_EBS_SNAPSHOTS = "LIST_EBS_SNAPSHOTS";

	String SESSION_DS_SELECTED_PROVIDER_ID = "SESSION_DS_SELECTED_PROVIDER_ID";
	String SESSION_DS_SELECTED_PROVIDER_TYPE = "SESSION_DS_SELECTED_PROVIDER_TYPE";
	String SESSION_DS_LAST_SELECTED_PROVIDER_TYPE = "SESSION_DS_LAST_SELECTED_PROVIDER_TYPE";
	String SESSION_DS_LAST_SELECTED_PROVIDER_ID = "SESSION_DS_LAST_SELECTED_PROVIDER_ID";
	String SESSION_DS_PAAS_DETAILS_ID = "SESSION_DS_PAAS_DETAILS_ID";

	String SESSION_OBJ_STORAGE_SELECTED_PROVIDER_TYPE = "OBJ_STORAGE_SELECTED_PROVIDER_TYPE";
	String SESSION_OBJ_STORAGE_SELECTED_PROVIDER_ID = "OBJ_STORAGE_SELECTED_PROVIDER_ID";
	String SESSION_OBJ_STORAGE_LAST_SELECTED_PROVIDER_TYPE = "OBJ_STORAGE_LAST_SELECTED_PROVIDER_TYPE";
	String SESSION_OBJ_STORAGE_LAST_SELECTED_PROVIDER_ID = "OBJ_STORAGE_LAST_SELECTED_PROVIDER_ID";
	String SESSION_LIST_S3BUCKET = "LIST_S3BUCKET";
	String SESSION_LIST_AZURE_STORAGE = "LIST_AZURE_STORAGE";

	String SESSION_SP_LAST_SELECTED_PROVIDER_ID = "SESSION_SP_LAST_SELECTED_PROVIDER_ID";
	String SESSION_SP_LAST_SELECTED_PROVIDER_TYPE = "SESSION_SP_LAST_SELECTED_PROVIDER_TYPE";
	String SESSION_SP_SELECTED_PROVIDER_ID = "SESSION_SP_SELECTED_PROVIDER_ID";
	String SESSION_SP_SELECTED_PROVIDER_TYPE = "SESSION_SP_SELECTED_PROVIDER_TYPE";

	String SECURITY_GROUP_ID = "security_group_id";
	String CUSTOM_SERVICE = "customService";
	String TEMPLATE_SERVICE = "templateService";
	String NATIVE_CLOUDONE = "co";
	String IS_FIRST = "isfirst";
	String MASTER_DATA = "MASTER_DATA";
	String FILTERED_DATA = "FILTERED_DATA";
	String ORG_MASTER_DATA = "ORG_MASTER_DATA";
	String SCHEDULEDTASKS_MASTER_DATA = "SCHEDULEDTASKS_MASTER_DATA";
	String FILTERED_LIST = "FILTERED_LIST";
	String DATAGRID_META_INFO = "DATAGRID_META_INFO";
	String SMART_VIEW_OPTIONS = "SMART_VIEW_OPTIONS";
	String DATAGRID_FILTER_PARAM = "filter";
	String OR_ANNOTATION_DELIMITOR = "|";
	String AND_ANNOTATION_DELIMITOR = "&";
	String USER_SELECTED_ITEMS = "USER_SELECTED_ITEMS";
	String SELECTED_ROW_IDS = "selectedRowIds";
	String DATAGRID_ID_PREFIX = "DataGrid";
	String JMX_APP_DISCOVERY_LIST = "JMX_APP_DISCOVERY_LIST";
	String JMX_DISCAPPTO = "JMX_DISCAPPTO";
	String JMX_MBEANS_LIST = "JMX_MBEANS_LIST";
	String JMX_ATTRIBUTE_LIST = "JMX_ATTRIBUTE_LIST";
	String JMX_ATTRIBUTE_MAP_LIST = "JMX_ATTRIBUTE_MAP_LIST";
	String JMX_ATTRIBUTE_KEY = "JMX_ATTRIBUTE_KEY";
	String SESSION_ATTR_LIST = "SESSION_ATTR_LIST";
	String SESSION_INSTANCE_LIST = "SESSION_INSTANCE_LIST";
	String USER_GROUP_LIST = "USER_GROUP_LIST";
	String USER_DETAILS_LIST = "USER_DETAILS_LIST";
	String AD_USER_DETAILS_LIST = "AD_USER_DETAILS_LIST";
	String AWS_IMAGES_LIST = "AWS_IMAGES_LIST";
	String AWS_STORAGE_PROFILE_LIST = "AWS_STORAGE_PROFILE_LIST";
	String AWS_STORAGE_PROFILE_DB_LIST = "AWS_STORAGE_PROFILE_DB_LIST";
	String VALID_SELECTED_ITEMS = "VALID_SELECTED_ITEMS";
	String VALID_SELECTED_INSTANCES = "VALID_SELECTED_INSTANCES";
	String VCENTER_SERVER_TEXT = "vCenter Server";
	String SESSION_LIST_EVENTS = "LISTEVENTSESSION";
	String SESSION_LIST_JOBS = "LISTJOBSESSION";
	String SESSION_LIST_SCHEDULEDTASKS = "LISTSCHEDULEDTASKSSESSION";
	String SESSION_RULE_LIST = "LISTRULESSESSION";
	String SESSION_TYPE_LIST = "LISTTYPESESSION";
	String SESSION_LIST_PERMISSIONS = "LISTPERMISSIONSESSION";
	String MQ_HOST = "cloudone.mq.host";
	String MQ_PORT = "cloudone.mq.port";
	String MQ_ADMIN = "admin";
	String MQ_PROP_FILE_NAME = "mq.properties";
	String CONFIG_FOLDER = "config";
	String MISC_PROP_FILE_NAME = "miscellaneous.properties";
	String RAMS_RESPONSE_URL = "rams.response.url";
	String SYS_PROP_FILE_NAME = "sysprep.properties";
	String OVERRIDE_FOLDER = "overrideprop";
	String RULES_PROP_FILE_NAME = "rules.properties";
	String OVERRIDER_ENV_PROP_FILE_NAME = "override-environment.properties";
	String POWERVM_JSON_BO_MAPPER_PROP = "jason-bo-mapper.properties";
	String STORAGE_PROFILE_CONF_LIST = "STORAGE_PROFILE_CONF_LIST";
	String STORAGE_PROFILE_CREATE_UPDATE_INDICATOR = "saveIndicator";
	String VIRTUAL_DISK_METHOD = "getVirtualDisks";
	String URL_MAPPINGS = "URL_MAPPINGS";
	String CONTEXT_PATH = "CONTEXT_PATH";
	String AZURE_OBJ_STORAGE_LIST = "AZURE_OBJ_STORAGE_LIST";

	// Variable used for Snapshot Operation page
	String SNAPSHOT_OBJECT_LIST = "Snapshot Object List";
	String SELECTED_SNAPSHOT_OBJECT = "Selected Snapshot Object";
	String SELECTED_SNAPSHOT_ID = "selectedSnapShot";
	String N_A = "N/A";
	String NA = "NA";
	String CREATED_DATE = "createdDtTime";
	// Added for JIRA:3716
	String ERROR_MSG = "errormessage";
	String WINDOWS = "Windows";
	String LINUX = "Linux";

	String TREELEAF_DATA_SEPARATOR = "-";
	String TREELEAF_DATA_DELEMETER = "@@@";
	String OPTARGS_DATA_DELIMITER = "@@@";
	String APP_VIEW = "APP_VIEW";
	String INFRA_VIEW = "INFRA_VIEW";
	String USER_GROUP_VIEW = "USER_GROUP_VIEW";
	String SHOWBACK_SUMMARY_VIEW = "SUMMARY_VIEW";
	String SHOWBACK_INSTANCE_SUMMARY_VIEW = "INSTANCE_SUMMARY_VIEW";
	String TAG_VIEW = "TAG_VIEW";
	String LIST_OF_REGIONS = "LIST_OF_REGIONS";
	String LIST_OF_S3_REGIONS = "LIST_OF_S3_REGIONS";
	String IS_IMPORT_USER = "isImportUser";
	String VALID_SELECTED_INSTANCES_APPLY_TEMP = "VALID_SELECTED_INSTANCES_APPLY_TEMP";
	String NO_PREFERENCE = "No Preference";
	String LINE_SEPERATOR = "<br/>";

	// For JIRA CO-4874
	String PORTGROUP_KERNEL = "kernel";
	String PORTGROUP_SC = "Console";
	String PORTGROUP_MGMT = "Management";
	String PORTGROUP_UPLINKS = "DVUplinks";
	String PORTGROUP_VMotion = "VMotion";
	String SWITCH_VXLAN = "VXLAN";
	String STATIC_CONFIG_PROP = "powervm_staticconfig.properties";
	String WAIT_TIME_PROP = "waittime.properties";
	String AZURE_EA_COST_PROP = "azureeacost.properties";
	String CPU_MODE = "cpumode";
	String DEPLOY_DELAY = "deploydelay";
	String DELETE_STORAGE = "deleteStorage";

	int THIRTY = 30;
	int SIXTY = 60;
	int NEGATIVE_INDEX = -1;

	String SUCCESS_MSG = "successMsg";
	String SET = "set";
	String NATIVE = "native";
	String COM_DOT = "com.";
	String FILTERED_VALUE_INSTANCES = "filteredValueInstances";

	String SELECTED_REGION_NAME = "SELECTED_REGION_NAMES";
	String SELECTED_IPADDRESS = "SELECTED_IPADDRESS";

	// For Business Policies Page
	String SELECTED_POLICY_TYPE = "SELECTED_POLICY_TYPE";

	String CPU = "CPU";
	String MEMORY = "MEMORY";
	String STORAGE = "STORAGE";
	String OVERHEAD = "OVERHEAD";
	String ESTIMATEDCOSTPERHOUR = "ESTIMATEDCOSTPERHOUR";
	String TOTALCHARGEPERHOUR = "TOTALCHARGEPERHOUR";

	// CO-5835 Service Profile
	String CUSTOM_CONFIG = "Custom Configuration";

	int PARAM_1024 = 1024;
	int PARAM_512 = 512;
	String UNIT_KB = "KB";
	String UNIT_MB = "MB";
	String UNIT_GB = "GB";
	String UNIT_TB = "TB";

	// UserGroup Quota Updation
	String UNLIMITED = "Unlimited";
	int UNLIMITED_USER_GROUP_QUOTA = -999;
	int META_PARENT_TASK_ID = -999;

	String DASH_VALUE = "-";
	String HIPHEN_WITH_SPACE_VALUE = " - ";
	String UNDERSCORE = "_";
	String COMMA_SEPARATOR = ",";
	String SEMICOLON_SEPARATOR = ";";
	String COMMA_SEPARATOR_WITH_SPACE = ", ";
	String TILDE_SEPARATOR = "~";
	String TAGS_SEPARATOR = "@@@";
	String NEW_VOLUME = "New Volume";
	String WIN = "win";
	String FILE_EXT_SCRIPT = "misc.properties";
	String OVERRIDE_FILE_EXT_SCRIPT = "override_misc.properties";
	String DISK_EXT_FILE_WRAPPER = "disk_ext_wrapper";
	String DISK_EXT_ADD_SCRIPT = "disk_ext_add_script";
	String DISK_EXT_MOD_SCRIPT = "disk_ext_edit_script";
	String FILE_EXT_WRAPPER_SCRIPT = "file_ext_script_wrapper";
	String FILE_EXT_REQUIRE = "file_ext_require";
	// these are the ad-hoc entries to fix the compilation error.
	String FILE_EXT_ADD_SCRIPT = "file_ext_add_script";
	String FILE_EXT_EDIT_SCRIPT = "file_ext_edit_script";

	String DISK_LUN_LIST = "disk_lun_list";
	String DISK_STORAGE_POOL = "disk_storage_pool";
	String THIN_DS = "Thin";
	String THICK_DS = "Thick";
	String SINGLE_HOST = "Single host";
	String MUTI_HOST = "Multi host";
	String TimeFormat1 = "%s hr %s min";
	String TimeFormat2 = "%s::%s";
	String TimeFormat3 = "%s days";
	String TimeFormat4 = "%s hr %s min %s sec";
	// Entries to decide whether the provider is placed @ Local (or) Remote
	String LOCAL_PROVIDER = "local";
	String REMOTE_PROVIDER = "remote";
	// CO-15886 - Changed the display values
	String EVENT_TICKET_NONE = "None";
	String EVENT_TICKET_FORWARDED = "Ticket Forwarded";
	String EVENT_TICKET_FORWARDED_FAILED = "Ticket Forward Failed";
	String EVENT_TICKET_CREATED = "Ticket Created";

	String INTELLIGENT_POLICY_DC_USERDEFINE_SELECTGROUPNAME = "Specific Datacenter";
	String INTELLIGENT_POLICY_DC_USERDEFINE_SELECTGROUPTEXT = "UserDefinedCondition";

	String INTELLIGENT_POLICY_DC_SPECIFIC_SELECTGROUPNAME = "Datacenter based on a rule";
	String INTELLIGENT_POLICY_DC_SPECIFIC_SELECTGROUPTEXT = "PreDefinedCondition";
	String JMX_SEPERATOR = "__";
	String AUTOMATION_POLICY_APP_PROFILE_SEPERATOR = "\\.";
	String APPLICATIONCOPY_TIER_SPLIITER = "@";
	String PLUS = "+";
	String MINUS = "-";
	String DATACENTER = "Datacenter: ";
	String HOST = "Host: ";
	String NETWORK = "Network: ";
	String CLUSTER = "Cluster: ";
	String REGION = "Region:";
	String VPC = "VPC:";
	String ZONE = "Availability Zone(s): ";
	String SUBNET = "Subnet(s): ";
	String RULE = "Rule: ";
	String SECURITY_GROUP = "Security Group(s): ";

	String CHANGE_IN_STORAGE = "Storage Changed";
	String ALL = "all";
	String MY = "my";
	String ACTIVE = "active";
	String NEW = "new";
	String ALLEVENTS = "All Events";
	String OPENALERTS = "Open Alerts";
	String ACTIVE_STORAGE = "Active";
	String INACTIVE = "inactive";

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
	String INSTALLMONITORAGENT = "INSTALLMONITORAGENT";
	String RUNNING = "RUNNING";
	String DELETED = "DELETED";

	String SNAPSHOT_TYPE_OWNED_BY_ME = "snapshotType__Owned";
	String SNAPSHOT_TYPE_ALL_SNAPSHOTS = "snapshotType__All";
	String SNAPSHOT_TYPE_MY_AMI_SNAPSHOTS = "snapshotType__My";
	String SNAPSHOT_TYPE_OWNED = "snapshotType__Owned By Me";

	String REST_PLUGIN_RESOURCE_FILE = "plugin.resource.file";
	String REST_PLUGIN_JAR_PATH = "plugin.jar.path";
	String REST_PLUGIN_RESPONSE_DELIMITOR = "#@#@";
	String REST_PLUGIN_HOST = "plugin.remote.host";
	String REST_PLUGIN_PORT = "plugin.remote.port";
	String REST_PLUGIN_SECURE_FILE = "plugin.secure.file";
	String REST_PLUGIN_PROPERTY_FILE = "plugin.properties";
	String REST_PLUGIN_REMOTE_URL = "plugin.remote.url";
	String REST_PLUGIN_REMOTE_VCENTER = "plugin.remote.vcenter";
	String REST_PLUGIN_TRUSTSTORE_KEY = "javax.net.ssl.trustStore";
	String REST_PLUGIN_TRUSTSTORE_PWD_KEY = "javax.net.ssl.trustStorePassword";
	String REST_PLUGIN_TRUSTSTORE_VALUE = "plugin.truststore";
	String REST_PLUGIN_TRUSTSTORE_PWD_VALUE = "plugin.truststore.pwd";
	String REST_PLUGIN_EXPORT_CLASSES = "plugin.export.classes";
	String REST_PLUGIN_CATALOG_REFRESH_REMOTE = "plugin.remote.catalog.refresh";
	String XIATTRIBUTES_PROPERTY_FILE = "xiAttributes.properties";

	String OVERRIDE_QUARTZ_PROPERTIES = "override-quartz-config.properties";

	String OPEN_PARANTHESIS = "(";
	String CLOSED_PARANTHESIS = ")";

	String NETWORK_PAGE = "ip addresses";

	String SUMMARYVALUE_SEPERATOR = "__";
	String POWERED_OFF = "Powered Off";
	String POWERED_ON = "Powered On";

	String ATTACHING = "attaching";
	String INUSE = "in-use";
	String DETACHING = "detaching";
	String AVAILABLE = "available";
	String BLANK_SPACE_STRING = " ";
	String BLANK_STRING = "";

	String SIGNATURE_DOES_NOT_MATCH = "SignatureDoesNotMatch";
	String AUTH_FAILURE = "AuthFailure";

	String LIST_OF_ENIS = "ListOfENIs";
	String LIST_OF_ENIS_FROM_CLOUD = "ListOfENIFromCloud";
	String LIST_OF_EBS_SNAPSHOT = "ListOfEBSSnapshot";
	String OLD_LIST_OF_EXISTING_ENI="oldListOfExistingEni";

	String LIST_OF_ENIS_FROM_CLOUD_SELECT_ITEM = "ListOfENIFromCloudSelectItem";
	String NUMBER_OF_ENI_SELECT_ITEM = "NumberOfENISelectItem";
	String NUMBER_OF_ALLOWED_SECONDARY_IP = "NumberOfSecondaryIPAllowed";
	String COLON_OPERATOR = ":";
	String ENI_DEVICE_PREFIX = "eth";

	String DEFAULT_SELECTITEM = "-- Select --";
	String DEFAULT_SELECTITEM_NONE = "-- None --";
	String DEFAULT_SELECTITEM_NUMVALUE = "-999";
	String OU = "OU";
	String UID = "UID";
	String EBS_SNAPSHOT_AMI_PREFIX = "Created by CreateImage";
	String SESSION_LIST_AZURE_DATASTORES = "SESSION_LIST_AZURE_DATASTORES";
	String AZURE_IMAGES_LIST = "AZURE_IMAGES_LIST";
	String OVERRIDE_MISC_MC_DIAGNOSTICS_SCRIPT = "diag_script_ext_wrapper";
	String OVERRIDE_DIAGNOSTICS_EMAIL_LIST = "diagnostics_email_list";

	// Added for Job Management Use case
	String MYTASK = "MyTasks";
	String ALLTASK = "AllTasks";
	String MYSCHEDULEDTASK = "MyScheduledTasks";
	String ALLSCHEDULEDTTASK = "AllScheduledTasks";

	String MC_CATALINA_WEBAPPS_FOLDER_NAME = "webapps";
	String MC_WAR_FILE_NAME = "cloudone";
	String MC_WARPATH_EXTENSION = "war";
	String MC_FILE_SEPARATOR = "file.separator";
	String MC_WEBINF_LOCATION = "WEB-INF";
	String MC_LIB_LOCATION = "lib";
	String MC_FILE_TO_SEARCH = "pom.properties";
	String MC_ARTIFACT_NAME = "artifactId";
	String MC_ARTIFACT_VERSION = "version";
	String MC_FILTER_JAR_NAME = "cloudone";
	String MC_JAR_EXTENSION = "jar";
	String CATALINA_HOME = "CATALINA_HOME";
	String PROTOCOL_HTTP = "http";
	String PROTOCOL_HTTPS = "https";
	String EQUALS = "=";
	String DEFAULT_HTTP_PORT = "80";
	String DEFAULT_HTTPS_PORT = "443";
	String NULL = "null";
	String MC_VERIFY_MAP_KEY = "mc";

	String TREE_OPTIONS_SEPERATOR = "___";
	String AZURE_STORAGE_SEPARATOR = "~@@@~";
	String OWNED_BY_ME_CAPTION = "My AWS account Snapshots";
	String RECENT_TASK_STATUS_ID = "RecentTasksTATUSiD";
	String UTF_8 = "UTF-8";

	String PROVIDER_SERVICE_REPORT = "Provider Services Compute Usage Report";
	String STORAGE_ACTIVITY_REPORT = "Storage Volumes Activity Report";
	String DATABASE_SERVICE_REPORT = "Database Services Usage Report";
	String APPLICATION_AVAILABILITY_REPORT = "Application Availability Report";
	String SECURITY_SCAN_REPORT = "Security Scan Report";

	String UNIT = "unit";
	String UPTIME = "uptime";

	String SESSION_LB_LAST_SELECTED_PROVIDER_ID = "SESSION_LB_LAST_SELECTED_PROVIDER_ID";
	String SESSION_LB_LAST_SELECTED_PROVIDER_TYPE = "SESSION_LB_LAST_SELECTED_PROVIDER_TYPE";
	String SESSION_LB_SELECTED_PROVIDER_ID = "SESSION_LB_SELECTED_PROVIDER_ID";
	String SESSION_LB_SELECTED_PROVIDER_TYPE = "SESSION_LB_SELECTED_PROVIDER_TYPE";

	int USERGROUP_VISIBLE_VALUE = 2;
	int USERGROUP_EDITABLE_VALUE = 1;

	String SUCCESS = "success";
	String FAILURE = "failure";
	String TIMEOUT = "TIMEOUT";

	String INSTANCE_STATE = "Instance State";
	String AGENT_STATE = "Agent State";
	String MONITORING_STATE = "Monitoring State";
	String EVENT_ALTERNATE_NAME = "Cloud360 agent";
	String OK = "OK";
	String DEFAULT_IMAGE = "other.png";
	String LINUX_IMAGE = "linux.png";
	String CENTOS_IMAGE = "centos.png";
	String FEDORA_IMAGE = "fedora.png";
	String REDHAT_IMAGE = "redHat.png";
	String SOLARIS_IMAGE = "solaris.png";
	String SUSE_IMAGE = "suse.png";
	String UBUNTU_IMAGE = "ubuntu.png";
	String WINDOWS_IMAGE = "windows.png";
	String AIX_IMAGE = "aix.png";
	String OK1_IMAGE = "ok1.png";
	String OK_IMAGE = "ok.png";
	String CRITICAL_IMAGE = "critical.png";
	String WARNING_IMAGE = "warning.png";
	String IDLE_IMAGE = "idle.png";

	String FIRST = "first";

	String GET_REQUEST = "GET";
	String POST_REQUEST = "POST";
	String PUT_REQUEST = "PUT";
	String DELETE_REQUEST = "DELETE";
	String ESCAPE_DOT = "\\.";

	String TERREMARK_CATALOG_PREFIX = "/cloudapi/ecloud/admin/catalog/";
	String TERREMARK_GROUP_PREFIX = "/cloudapi/ecloud/layoutgroups/";
	String TERREMARK_ROW_PREFIX = "/cloudapi/ecloud/layoutrows/";
	String TERREMARK_CATALOG = "CATALOG";
	String TERREMARK_TEMPLATE = "TEMPLATE";
	String TERREMARK_TEMPLATE_PREFIX_1 = "/cloudapi/ecloud/templates/";
	String TERREMARK_TEMPLATE_PREFIX_2 = "/computepools/";
	String TERREMARK_PRIMARY_DNS = "208.67.222.222";
	String TERREMARK_SECONDARY_DNS = "208.67.220.220";

	String RSA_ALGORITHM = "RSA";
	int RSA_KEYSIZE = 512;
	String ENI_PAGE = "network";
	int DEFAULT_TAG_ID = -987;
	String NONE = "None";

	String SESSION_ACTION_PACKAGETO = "SESSION_ACTION_PACKAGETO";
	String SESSION_RUNBOOK = "SESSION_RUNBOOK";

	String GET_SESSION_ATTRIBUTE = "getSessionAttribute";
	String LIST_SESSION_IDS = "listSessionIds";
	String CONTEXT_2 = ",host=localhost";
	String CONTEXT_1 = "Catalina:type=Manager,context=/";
	String EXPIRE_SESSION = "expireSession";

	String SORT_ASC = "asc";
	String SORT_DESC = "desc";
	String CREATE_DATE = "createDate";

	int APPROVAL_WORKFLOW_FLAG = 2;
	String PACKAGE_SEPARATOR = "@@";
	String SLASH = "/";

	String ADDED_SECONDARY_IP = "Added Secondary IP";
	String AVAILABLE_PACKAGE = "AvailablePackageData";

	String INVALID_IP_ERROR_MESSAGE = "Could not assign %s private IP address to %s";
	String CREATED_FROM = "Created from ";

	String MANUAL = "Manual";
	String AUTOMATED = "Automated";
	String DBSNAPSHOTTYPE = "dbSnapshotType";
	String SNAPSHOTTYPE = "snapshotType";
	String PATCHSTATE = "patchState";
	String DFM_ADDRESS = "dfm-address";
	String DFM_USERNAME = "dfm-username";
	String DFM_PASSWORD = "dfm-password";
	String ONTAP_ADDRESS = "ontap-address";
	String ONTAP_USERNAME = "ontap-username";
	String ONTAP_PASSWORD = "ontap-password";
	String ISCSI_TARGET_NODENAME = "iSCSI-target-nodename";
	String DATASETPREFIX = "dataset_";
	String CIFS_USER_EVERYONE = "everyone";
	String CIFS_PERMISSION_FULL = "full_control";
	String COLONSLASH = ":/";
	String CIFS_AND_NFS = "CIFS and NFS";
	String NFS_SECURITY_SYS = "sys";
	String NETAPP_PROP_FILE_NAME = "netapp.properties";
	String P83_PREFIX_NETAPP = "p83Prefix-netapp";

	String SELECTED_RUNBOOK_ACTIONTO = "SelectedRunBookActionTO";
	String SELECTED_SERVICETO = "SelectedServiceTO";
	String ACTION_TO_INDEX = "actionTOIndex";
	String SCALING_POLICY_FORMID = "scalingPolicyFormId";
	String RUNBOOK_SERVICE_PARAMETER_ID = "id";
	String RUNBOOK_SERVICE_PARAMETER_VALUE = "value";
	String RUNBOOK_SERVICE_PARAMETER_NAME = "name";
	String RUNBOOK_SERVICE_PARAMETER_TYPE = "type";
	String PARAMETERS = "parameters";
	String RUNBOOK_LIST = "runbookList";

	String PREVIOUS_INST_TYPE = "actionFormId:instTypeHidden";

	String READ_WRITE_PERMISSION = "rw";

	String NEW_LINE = "<br/>";
	String SPLIT_CHAR = "@@@";
	String TREE_NODE = "node";
	String TREE_GROUP = "group";
	String DELIMITER = "---";
	String DELIMITER_START = "---BEGIN TABLE";
	String DELIMITER_END = "---END TABLE";
	String TABLE_START_TAG = "<TABLE border=2 width=100% class=bodyContent;>";
	String TABLE_END_TAG = "</TABLE>";
	String HEADING_START = "<p style='color: #404040;display: block;font-family: Arial;font-size: 18px;' class=bodyContent>";
	String HEADING_END = "</p>";
	String BOLD_START = "<b>";
	String BOLD_END = "</b>";
	String ROW_START_TAG = "<tr style='color: #505050;font-family:Arial;font-size:14px;'>";
	String ROW_END_TAG = "</tr>";
	String CELL_START_TAG = "<td>";
	String CELL_END_TAG = "</td>";
	String BEGIN = "BEGIN TABLE";
	String TABLE_HEADING_START_TAG = "<th style='color: #404040;font-family: Arial;font-size: 14px;text-align: left;'>";
	String TABLE_HEADING_END_TAG = "</th>";

	String resetLinkPath = "/login/reset-password.cl";
	String PASSWORD_EXPIRED = "password.expired";
	String F_PASSWORD_RESET = "force.password.reset";
	String attachUserNameKey = "?username=";
	String CLDSETUP_DV_FILE = "cloudSetUpDefault.properties";

	String SELECTED_REGION = "selectedRegion";
	String computeInstances = "ComputeInstances";

	String Cloud = "Cloud";
	String MSUSER_GRP_NAME = "_Cloud Admin Group_";

	String OPERATION = "Operation : ";

	String START_INSTANCE_COMMAND = "Start Instance Command";
	String STOP_INSTANCE_COMMAND = "Stop Instance Command";
	String TERMINATE_INSTANCE_COMMAND = "Terminate Instance Command";
	String APPLY_TEMPLATE_COMMAND = "Apply Template Command";

	String APPLICATION_PROVISION = "Application Provisioning";
	String APPLICATION_DEPROVISION = "Application Deprovisioning";
	String APPLICATION_START = "Application Start";
	String APPLICATION_STOP = "Application Stop";
	String APPLICATION_SCALEOUT = "Application Scaleout";
	String APPLICATION_SCALEIN = "Application Scalein";

	String APPLY_MONITORING = "Apply Monitoring";
	String UPDATE_AVAILABILITY_POLICY = "Update Availability Policy";
	String UPDATE_APPLICATION_PROFILE = "Update Application Profile";
	String EVENT_ARRIVAL = "Event Arrival";

	String WORKFLOW_COMPLETE = "Workflow Complete";

	String CONFIG = "config";
	String ADAPTER = "adapter";
	String USER_PAGE = "users";

	// Added for JIRA CO-14081
	String COMPUTE_PROFILES = "Compute Profile";
	String MONITORING_PROFILE = "Monitoring Profile";
	String APPLICATION_PROFILE = "Application Profile";
	String MONITORING_POLICIES = "Monitoring Policies";
	String PROVISIONING_POLICIES = "Provisioning Policies";
	String AUTOMATION_POLICIES = "Automation Policies";
	String COMPLIANCE_POLICIES = "Compliance Policies";

	String PROVISIONING = "Provisioning";
	String SI_MONITORING = "SI Monitoring";
	String MI_MONITORING = "MI Monitoring";
	String COMPLIANCE = "Compliance";

	// Added for JIRAs CO-15541 and CO-15559 - Default Monitoring Profile
	// changes
	String OWNED_BY_ME_MON_PROFILES = "Owned By Me";
	String MON_PROFILES_TYPE = "monitoringProfileType";
	String USERID = "userID";
	String SYSTEM = "System";
	String All = "All";

	// Netscaler Details
	String NETSCALER_USERNAME = "netscaler-username";
	String NETSCALER_PASSWORD = "netscaler-password";
	String NETSCALER_IP = "netscaler-ip";

	String ACTION_FAILED = "Action Failed";

	String TRUE = "true";
	String FALSE = "false";

	String AUTOMATIC_STORAGEACCOUNT = "automatic";
	String ONE = "1";

	// for JIRA CO-16504
	String SCWEBSERVICE_CONSTANTS = "scwebservices-constants.properties";
	String SHORT_RECEIVE_TIMEOUT_VALUE = "SHORT_RECEIVE_TIMEOUT_VALUE";
	String MEDIUM_RECEIVE_TIMEOUT_VALUE = "MEDIUM_RECEIVE_TIMEOUT_VALUE";
	String LONG_RECEIVE_TIMEOUT_VALUE = "LONG_RECEIVE_TIMEOUT_VALUE";
	String AUTO_ASSIGN = "auto-assign";

	// for CO-16476 - Customized Notification Subject
	String CUSTOMIZED_SUBJECT_FOR_NOTIFICATIONS = "notificationsubject_en_US.properties";

	String IP_DELIMITER = ";";

	// for CO-16925 - Load corresponding grid data when cancel action performed
	// in self/Everyone.
	String SELECTED_SCHEDULED_TASK = "selectedScheduledTask";
	String SUMMARY_OPTION = "JOB_MANAGEMENT@@@my";
	String SCHEDULED_TASK = "SCHEDULED_TASK";

	// Added for JIRA 17156
	String SQL_FOR_USERGROUP_QUOTA_UTILIZATION = "SQL_FOR_USERGROUP_QUOTA_UTILIZATION";

	// Added for JIRA 16919 and 16956
	String NEXT_LINE = "\n";
	String LINE_BREAK = "<br>";
	String LESS_THAN = "<";
	String HTML_LESS_THAN = "&lt";
	String GREATER_THAN = ">";
	String HTML_GREATER_THAN = "&gt";

	String QUOTE = "'";

	// ServiceType Constant -- Added for CO-17896
	String MONTYPE_DATABASE_SERVICES = "Database Service(s)";

	String APPLICATION = "APPLICATION";
	String OTHER = "OTHER";

	// deviceType constants added for CO-17910
	String AWS_RDS = "AWS RDS";
	String AWS_Instances = "AWS INSTANCES";
	String AWS_ELB = "AWS ELB";
	String AWS_EBS = "AWS EBS";
	String AMAZON = "AMAZON";
	String AWS_EMR = "AWS EMR";
	String EBS = "EBS";

	String CREATED_INSTANCES = "List of created Compute Instances";
	String DELETED_INSTANCES = "List of deleted Compute Instances";

	// CO-18443
	String MONTYPE_LOADBALANCER = "LoadBalancer";
	String MONTYPE_COMPUTE_INSTANCE = "Compute Instance";
	String MONTYPE_HOSTED_SERVICE = "Hosted Service";
	String MONTYPE_AZURE_SERVICE = "Azure Cloud Service";
	String MONTYPE_DATABASE_SERVICE = "Database Service";
	String MONTYPE_EBSVOLUME = "Elastic Block Storage";
	String MONTYPE_PLATFORM_SERVICE = "Platform Service";

	// For loading first ReportType
	String UTILIZATION = "Utilization";

	// Added for comparing the reportPage with reportPage defined in config.
	String APPLICATION_POPUP = "Application";
	String INSTANCE_POPUP = "Instance";

	String CLIENT = "client";
	String APPLICATION_DETAILS = "applicationDetails";
	String INSTANCE_DETAILS = "instanceDetails";
	String LITE_UI = "liteui";

	// Licensing changes
	String ENTITY = "Entity : ";

	String FIREWALL = "FIREWALL";

	// Added for CO-18800
	String MONTYPE_PLATFORM_SERVICES = "Platform Service(s)";

	// CO-18747
	String COMMUNICATION_TYPE = "Communication_Type";

	String OPEN_SQR_BRACKET = "[";
	String CLOSE_SQR_BRACKET = "]";

	String SNMP = "SNMP";
	String WMI = "WMI";
	String ERR_NOTIFICATION_SUBJECT_VARIABLES_FETCH = "Error while fetching Notification Subject variables";
	String CONNECTION_STATE = "connectionState";
	String REPORTING = "REPORTING";

	/**
	 * Added as part of JIRA CO-19513
	 * 
	 */
	String ENABLE_MONITORING = "ENABLE";
	String DISABLE_MONITORING = "DISABLE";
	String REMOVE_MONITORING_PROFILE = "REMOVE";
	String APPLY_PROFILE = "APPLY";

	// Added for new app profile changes..
	String SAVE_AS = "SaveAs";

	// Added aspart of PatchManagement changes.
	String PATCH_GROUP = "PatchGroup";
	String WSUS = "WSUS";
	String NOTIFICATION_TASK = "Synchronization of Patches from WSUS";
	String ANALYTICS = "analytics";
	String LIST_PATCHES = "list-all-patches.cl";
	String NEW_PATCHES_ADDED = " new patches added";
	String PATCH_FAILURE_REASON = "Synchronization of latest patches on the integrated WSUS system failed";
	String HOST_ADDRESS = "hostAddress";
	String HOST_NAME = "hostName";
	String PATCH_MANAGEMENT = "PatchManagement";
	String SCHEDULE_CREATE_DB_SNAPSHOT = "Schedule Create DBSnapshot";

	String CHANGE_MONITORING_SESSION = "stringTOBeSearched";
	String MARK_FOR_APPROVAL_PATCHES = "Mark for Approval Patch(s)";
	String PATCH_APPROVAL = "Patch Approval";
	String DECLINE_PATCH = "Decline Patch(s)";
	String APPLY_PATCH = "Apply Patch(s)";
	String PATCH_UPLOAD_FAILURE_NOTOFICATION = "Patch Upload Failure Notification";
	String APPLY_PATCH_FAILURE_NOTIFICATION = "Apply Patch Failure Notification";
	
	String PERIOD = ".";

	String HYPERV_HOST_GROUP = "HOST_GROUP";
	String HYPERV_CLUSTER = "CLUSTER";
	String CLOUD360 = "Cloud360";
	String LOGIN_URL = "login/login.cl";
	String SEARCH_DETAILS_ID = "searchDetailsId";
	String FROM_DATE = "fromDate";
	String TO_DATE = "toDate";
	String RETURN_PAGE = "returnPage";

	// Adding for jira CO-20047 Event Screen

	String Ticket_State = "ticketState";
	String Ticket_Number = "ticketNumber";

	String LOCALHOST_IP = "127.0.0.1";
	String WEB_ROLE = "WebRole";
	String WORKER_ROLE = "WorkerRole";

	// CO-20167
	String CREATE = "create";
	String EDIT = "edit";
	String HOSTED_SERVICE = "HOSTEDSERVICE";
	
	String EVM_IDENTIFIER = "_EVM";

	public static final String FROM_PAGE="fromPage";
	String PRIMARYKEY="PrimaryKey";
	String SECONDARYKEY="SecondaryKey";
	
	//Azure WebApps Services
	String DIAGNOSTICS_AZURETABLESASURL="DIAGNOSTICS_AZURETABLESASURL";
	String VIRTUAL_NETWORK="virtualNetwork";
	String CONST_SUBNET="subnet";
	String DIAGNOSTICS_AZUREBLOBCONTAINERSASURL="DIAGNOSTICS_AZUREBLOBCONTAINERSASURL";
	String WEBSITE_HTTPLOGGING_CONTAINER_URL="WEBSITE_HTTPLOGGING_CONTAINER_URL";
	
	//Added for JIRA CO-21129
	String OBJECT_STORAGE="Object Storage(s)";
	String WEBAPPSERVICES="WebAppService(s)";
	
	// Added for JIRA CO-21360
	String BILLING_API_VERSION = "billing_api_version";
	String LOCALE = "locale";
	String RESOURCE = "https://management.azure.com/";
	String WORKFLOWSERVICE="/workflowservice";
	String AZURE_BILLING_AUTH_URL="/v1/tokenGeneration/code";
	String IC_SERVICES = "ic_services";
	
	//Added for JIRA CO-21327
	String REMOVE_MONITORING = "REMOVE_MONITORING_PROFILE";
	String AZURE_AUTH_SUCCESS = "template/azure-authentication-success.cl";
	String AZURE_AUTH_FAILURE = "template/azure-authentication-failure.cl";
	
	//Added for CO-21408
	String CLOUD_ICSERVICES_URI = "CLOUD-ICSERVICES-URI";
	String ICSERVICES_URL = "icservices-url";

	//Added for JIRA CO-21069
	String COMPUTE_INSTANCE="computeInstance";
	String SERVICE_CONSOLE=	"serviceConsole";	
	
	String REPORT_FILE_DURATION = "REPORT_FILE_DURATION";
	String ACCESS_TOKEN = "AccessToken";
	String PROVIDERID_PROVIDERTYPE_ACCESSTOKEN = "PROVIDERID-PROVIDERTYPE-ACCESSTOKEN";
	String INSTANCE = "INSTANCE";
	String JSON = "JSON";
	String STORAGECLOUD = "storagecloud";
	String INSTANCECLOUD = "instancecloud";
	String INSTANCETYPES = "instanceTypes";
	String OS_WINDOWS = "windows";
	String OS_LINUX = "linux";
	String MSWIN = "mswin";
	String STORAGETYPES = "storageTypes";
	String PERGBMOPROVSTORAGE = "perGBmoProvStorage";
	String BANDWIDTHTYPE = "BandwidthType";
	String BILLINGCYCLETYPE = "BillingCycleType";
	String HOSTINGTYPE = "HostingType";
	String PORTSPEED = "PortSpeed";
	String PRIMARYIPV6 = "PrimaryIPV6";
	String PRIVATEIPV6 = "PrivateIPV6";
	String PUBLICSECONDARYIP = "PublicSecondaryIP";
	String SSHKEY = "SSHKey";
	String DISKID = "DiskId";
	String MAGNETIC = "Amazon EBS Magnetic volumes";
	String IOPS = "Amazon EBS Provisioned IOPS (SSD) volumes";
	String GENERALPURPOSESSD = "Amazon EBS General Purpose (SSD) volumes";
	String DEPROVISIONING_DETAILS=" Deprovisioning of instance scheduled on %1$s at %2$s";	
	String AZURE = "Azure";
	String INSTANCENAME = "instanceName";
	String AZURE_V1 = "V1";
	String AZURE_V2 = "V2";
	String key="P@ssword!@#123";
	String EAPRICELIST = "eapricelist.csv";
	String EACOSTAPPLICABLE = "EACostApplicable";
	String PRICING = "pricing";
	String OS="OS";
	String DATA = "Data";
	String APPROVER_PASSWORD = "password-1";
	String PUBLISHER="Publisher";
	String OFFER="Offer";
	String SKUS="Skus";
	String TYPE="Type";
}
