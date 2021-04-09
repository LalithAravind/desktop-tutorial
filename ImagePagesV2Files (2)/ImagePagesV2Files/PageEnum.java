package com.cognizant.cloudone.ui.pages.enums;


/**
 * This class is a part of Common Dynamic Data Grid Implementation.
 * A enum class that list downs all the UI pages along with the type of Object 
 * used in Data Grid. 
 * This class will also be used for doing the page tracking.
 *
 */
public enum PageEnum {
	Dashboard("Dashboard", "","dashboard"),
	
    ListInstance("ListInstance", "Instance","compute"),
    VMWareCreateInstance("VMWareCreateInstance", "VMWareCreateInstance","compute instances - create new"),
    VMWareCreateInstanceProfile("VMWareCreateInstanceProfile", "VMWareCreateInstanceProfile","compute profiles - create new"),
    AWSCreateInsance("AWSCreateInsance", "AWSCreateInsance","compute instances - create new"),
    AWSCreateInstanceProfile("AWSCreateInstanceProfile", "AWSCreateInstanceProfile","compute profiles - create new"),
    AWSStorageProfileConfiguration("AWSStorageProfileConfiguration", "AWSStorageProfileConfiguration",""),
    AixCreateInsance("AixCreateInsance", "AixCreateInsance","compute instances - create new"),
    AixCreateInsanceProfile("AixCreateInsanceProfile", "AixCreateInsanceProfile","compute profiles - create new"),
    AzureCreateInsance("AzureCreateInsance", "AzureCreateInsance","compute instances - create new"),
    AzureCreateInstanceProfile("AzureCreateInstanceProfile", "AzureCreateInstanceProfile","compute profiles - create new"),
    Snapshot("Snapshot", "Snapshot","compute snapshots"),
    ListDatastores("ListDatastores", "DataStore","storage"),
    ListEBSVolumesDatastores("ListEBSVolumesDatastores", "DataStore",""),
    ListVMWareDatastores("ListVMWareDatastores", "DataStore",""),
    ListPowerVMDatastores("ListPowerVMDatastores", "DataStore",""),
    ListEBSVolumes("ListEBSVolumes", "DataStore",""),
    ListEBSSnapshot("ListEBSSnapshot", "DataStore","storage snapshots"),
    ListDiskLunForDatastore("ListDiskLunForDatastore", "DataStore","storage - create new"),
    CreateEBSVolumes("CreateEBSVolumes", "EBSVolume","storage - create volume"),
    ObjectStorage("ObjectStorage", "ObjectStorage","object storage"),
    Network("Network", "Network","ip addresses"),
    ChangeAssignee("ChangeAssignee", "Instance","change assignee"),
    CreateNetAppStorage("CreateNetAppStorage", "Storage","storage - create new"),
    OpenStackCreateInsance("OpenStackCreateInsance", "OpenStackCreateInsance","compute instances - create new"),
    OpenStackCreateInsanceProfile("OpenStackCreateInsanceProfile", "OpenStackCreateInsanceProfile","compute profiles - create new"),
    PhysicalServerCreateInstance("PhysicalServerCreateInstance", "PhysicalServerCreateInstance","compute instances - create new"),
    SoftLayerCreateInsance("SoftLayerCreateInsance","SoftLayerCreateInsance","compute instances - create new"),
    SoftLayerCreateInstanceProfile("SoftLayerCreateInstanceProfile","SoftLayerCreateInsance","compute profiles - create new"),
    
    ServiceProfile("ServiceProfile","ServiceProfile","compute profiles"),
    CreateApplicationProfile("CreateApplicationProfile","CreateApplicationProfile","application profiles - create"),
    EditApplicationProfile("EditApplicationProfile","EditApplicationProfile","application profiles - edit"),
    ProvisioningWorkflow("ProvisioningWorkflow","ProvisioningWorkflow","application profiles"),
    ListApplicationProfile("ListApplicationProfile","ListApplicationProfile","application profiles"),
    ListUsagePolicy("ListUsagePolicy","ListUsagePolicy","provisioning policies"),
    ListAutomationPolicy("ListAutomationPolicy","ListAutomationPolicy","automation policies"),
    ListMonitoringPolicy("ListMonitoringPolicy","ListMonitoringPolicy","monitoring policies"),
    ListCompliancePolicy("ListCompliancePolicy","ListCcomplianceompliancePolicy","compliance"),
    ListIntelligentPolicy("ListIntelligentPolicy","ListIntelligentPolicy","placement policies"),
    CreateUsagePolicy("CreateUsagePolicy","CreateUsagePolicy",""),
    CreateAutomationPolicy("CreateAutomationPolicy","CreateAutomationPolicy",""),
    CreateMonitoringPolicy("CreateMonitoringPolicy","CreateMonitoringPolicy",""),
    CreateCompliancePolicy("CreateCompliancePolicy","CreateCompliancePolicy",""),    
    
    ListEvent("ListEvent", "Event","events"),
    SystemConfigureTemplate("SystemConfigureTemplate", "SystemConfigureTemplate",""),
    SystemApplyTemplate("SystemApplyTemplate", "SystemApplyTemplate",""),
    ApplicationMonitoringApplyTemplate("ApplicationMonitoringApplyTemplate", "ApplicationMonitoringApplyTemplate",""),
    JMXAppDiscovery("JMXAppDiscovery", "JMXAppDiscovery",""),
    JMXApplicationDiscovery("JMXApplicationDiscovery", "JMXApplicationDiscovery",""),
    JMXConfMonitoringTemp("JMXConfMonitoringTemp", "JMXConfMonitoringTemp",""),
    JMXMbeansSelection("JMXMbeansSelection", "JMXMbeansSelection",""),
    ApplyMonitoringProfile("ApplyMonitoringProfile","ApplyMonitoringProfile",""),
    ApplyAzureMonitoringProfile("ApplyAzureMonitoringProfile","ApplyAzureMonitoringProfile",""),
    ApplyObjStorageMonitoringProfile("ApplyObjStorageMonitoringProfile","ApplyObjStorageMonitoringProfile",""),
    ApplyLBMonitoringProfile("ApplyLBMonitoringProfile","ApplyLBMonitoringProfile",""),
    ApplyStorageMonitoringProfile("ApplyStorageMonitoringProfile", "ApplyStorageMonitoringProfile", ""),
    
    Trending("Trending", "Trending","trends"),
    ListJobManagement("ListJobManagement", "ListJobManagement","tasks"),
    CompareInstances("CompareInstances", "CompareInstances",""),
    ConsumptionMetering("ConsumptionMetering", "ConsumptionMetering","metering"),
    LogScraping("LogScraping", "LogScraping",""),
    Billing("Billing", "Billing","provider billing"),
    
    SecurityGroup("SecurityGroup", "SecurityGroup","firewalls"),
    SavvisFireWall("SavvisFireWall", "SavvisFireWall",""),
    SavvisFireWallRules("SavvisFireWallRules", "SavvisFireWallRules",""),
    SecurityGroupRules("SecurityGroupRules", "Rules",""),
    ListKeyPair("ListKeyPair", "ListKeyPair","key pairs"),
    RolesPermission("RolesPermission", "RolesPermission","roles & permissions"),
    ManageEndPoints("ManageEndPoints", "ManageEndPoints","firewalls"),
    ManageACLs("ManageACLs", "ManageACLs","firewalls"),
    
    CloudSettings("CloudSettings", "CloudSettings",""),
    ManageAppGroups("ManageAppGroups", "ManageAppGroups",""),
    ManageDeployments("ManageDeployments", "ManageDeployments",""),
    ManageUsers("ManageUsers", "ManageUsers",""),
    ManageUserGroups("ManageUserGroups", "ManageUserGroups",""),
    ManageUserRoles("ManageUserRoles","ManageUserRoles",""),
    Notifications("Notifications", "Notifications",""),
    Diagnostics("Diagnostics","Diagnostics","Diagnostics"),
    ListDiagnostics("ListDiagnostics", "ListDiagnostics",""),
    ImportUsers("ImportUsers","ImportUsers",""),
    BasicDiagnostic("BasicDiagnostic","Basic Diagnostics","Basic Diagnostics"),
    DeepDiagnostic("DeepDiagnostic","Deep Diagnostics","Deep Diagnostics "),
    
    UnAssignSnap("ListUnAssignSnap", "ListUnAssignSnap",""),
    ListMonitoringProfile("ListMonitoringProfile", "ListMonitoringProfile","monitoring profiles"),
    CreateMonitoringProfile("CreateMonitoringProfile", "CreateMonitoringProfile",""),
    EditMonitoringProfile("EditMonitoringProfile", "EditMonitoringProfile",""),
    ListShowBackCost("ListShowBackCost", "ListShowBackCost",""),
    S3BucketExpirationRules("S3BucketExpirationRules", "S3BucketExpirationRules",""),
    Reporting("Reporting", "Reporting","reports"),
    NetworkInterfaces("NetworkInterfaces", "NetworkInterfaces","network"),
    ListScheduledTasks("ListScheduledTasks", "ListScheduledTasks",""),
    ListApplication("ListApplication", "ListApplication","applications"),
    CreateApplication("CreateApplication", "CreateApplication","applications"),
    ListApplicationAvailability("ListApplicationAvailability", "ListApplicationAvailability",""),
    ListCloudServices("ListCloudServices", "ListCloudServices",""),
    ListAzureDatastores("ListAzureDatastores", "ListAzureDatastores",""),
    ListDataBaseServices("ListDataBaseServices", "ListDataBaseServices","database services"),
    ListDataBaseSnapshots("ListDataBaseSnapshots", "ListDataBaseSnapshots","database snapshots"),
    LoadBalancerAWS("LoadBalancerAWS", "LoadBalancerAWS","load balancers"),
    LoadBalancerSAVVIS("LoadBalancerSAVVIS", "LoadBalancerSAVVIS",""),
    LoadBalancerInstanceSAVVIS("LoadBalancerInstanceSAVVIS", "LoadBalancerInstanceSAVVIS",""),
    LoadBalancerInstanceNETSCALER("LoadBalancerInstanceNETSCALER", "LoadBalancerInstanceNETSCALER",""),
    LoadBalancerInstanceAWS("LoadBalancerInstanceAWS", "LoadBalancerInstanceAWS",""),
    ListStorageProfiles("ListStorageProfiles", "storage profiles","storage profiles"),
    ListNetAppStorageProfiles("ListNetAppStorageProfiles", "NetAppStorageProfiles","storage profiles"),
    CreateStorageProfiles("CreateStorageProfiles", "CreateStorageProfiles","storage profiles"),
    ModifyStorageProfiles("ModifyStorageProfiles", "ModifyStorageProfiles","storage profiles"),
	IntegrationSystem("IntegrationSystem","IntegrationSystem",""),
	SCVMMCreateInstance("SCVMMCreateInstance","SCVMMCreateInstance",""),
	SCVMMCreateInstanceProfile("SCVMMCreateInstanceProfile","SCVMMCreateInstanceProfile",""),
	ListEntityAdvisories("ListEntityAdvisories","ListEntityAdvisories","reclamation"),
	CreateEntityAdvisories("CreateEntityAdvisories","CreateEntityAdvisories",""),
	TerremarkCreateInstance("TerremarkCreateInstance", "TerremarkCreateInstance","compute instances - create new"),
	TerremarkCreateInstanceProfile("TerremarkCreateInstanceProfile", "TerremarkCreateInstanceProfile","compute profiles - create new"),
	ListPatch("ListPatch", "ListPatch","patch manager"),
	ListPatchInstances("ListPatchInstances", "ListPatchInstances","patch manager"),
	ListPlatformServices("ListPlatformServices", "ListPlatformServices" , "platform services"),
	AzureSelectStorage("AzureSelectStorage", "AzureSelectStorage","Select Storage"),
	AzureStorageContainer("AzureStorageContainer", "AzureStorageContainer","Storage Container"),
	AzureStorageBlobs("AzureStorageBlobs", "AzureStorageBlobs","Storage Blobs"),
	ChangeTracking("ChangeTracking", "ChangeTracking", "Change Tracking"),
	ListImages("ListImages", "Images", "Images"),
	AddImages("AddImages", "AddImages","Image - Add new");
	
	
    private final String pageName;
    private final String dataType;
    private final String jsonPageName;

    private PageEnum(String pageName, String dataType, String jsonPageName) {
        this.pageName = pageName;
        this.dataType = dataType;
        this.jsonPageName = jsonPageName;
    }

	   /****
     * Get enum using page name.
     * @param pageName
     * @return
     */
    public static PageEnum getEnumByPageName(String pageName) {
        for (PageEnum val : PageEnum.values()) {
            if ((val.getPageName().equalsIgnoreCase(pageName))) {
                return val;
            }
        }
        return null;
    }
    

	   /****
  * Get enum using page name.
  * @param pageName
  * @return
  */
 public static PageEnum getEnumByJsonPageName(String pageName) {
     for (PageEnum val : PageEnum.values()) {
         if ((val.getJsonPageName().equalsIgnoreCase(pageName))) {
             return val;
         }
     }
     return null;
 }

    public String getPageName() {
        return this.pageName;
    }

    public String getDataType() {
        return this.dataType;
    }

    @Override
    public String toString() {
        return pageName;
    }

	public String getJsonPageName() {
		return jsonPageName;
	}
}
