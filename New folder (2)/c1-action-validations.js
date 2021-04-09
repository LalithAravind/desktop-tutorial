jQuery.noConflict();

function determinePath(){
	var pathname = window.location.pathname;
	var temp1 =pathname.substr(pathname.indexOf('/')+1);
	return (temp1.substr(0,temp1.indexOf('/')));
}

//********************************************************************************************************************
//Case "isNoInstanceSelected" -> Should be called if user doesn't select any instances
//Case "isAllInvalidUserSelection" -> Should be called if user doesn't select any valid instances specific to the operation
//Case "isAllValidUserSelection" -> Should be called if user selects all the valid entries. No need for a prompt here
//Case "isMixtureOfValidInvalidSelection" -> Should be called if there's a mixture of correct & incorrect entries in user's selection
//*********************************************************************************************************************

//Creating java script HashMap for Instance & Hypervisor action
var operationMap = new Hashtable();
operationMap.put("start", "Start");
operationMap.put("stop", "Stop");
operationMap.put("reboot", "Reboot");
operationMap.put("terminate", "Terminate");
operationMap.put("poweron", "Power On");
operationMap.put("poweroff", "Power Off");
operationMap.put("reset", "Reset");
operationMap.put("suspend", "Suspend");
operationMap.put("shutdown", "Shutdown");
operationMap.put("delete", "Delete");
operationMap.put("reboot", "Reboot");
operationMap.put("restart", "Restart");
operationMap.put("park", "Park");
operationMap.put("unpark", "Unpark");
operationMap.put("save", "Save");
operationMap.put("pause", "Pause");
operationMap.put("resume", "Resume");
operationMap.put("discardsavedstate", "Discard Saved State");
operationMap.put("edit_savvis_instance_resources", "Modify Compute");
operationMap.put("edit_savvis_instance_storage", "Modify Storage");
operationMap.put("clone_savvis_vm", "Clone Instance(s)");
operationMap.put("capture_vapp", "Capture vApp");
operationMap.put("installmonitoragent", "Install Monitoring Agent");
operationMap.put("clone", "Clone VM");
operationMap.put("changeassignee", "Change Assignee");
operationMap.put("disablemonitoring", "Disable Monitoring");
operationMap.put("convertToTemplate", "Convert to Template");
operationMap.put("cloneToTemplate", "Clone to Template");
operationMap.put("enablemonitoring", "Enable Monitoring");
operationMap.put("runDiagnostics", "Run Diagnostics");
operationMap.put("login", "Login");
operationMap.put("applyTemplate", "Apply Template");
operationMap.put("updateinstancedetails", "Update Instance Details");
operationMap.put("converttotemplate", "Convert to Template");
operationMap.put("clonetotemplate", "Clone to Template");
operationMap.put("applytemplate", "Apply Template");
operationMap.put("editresources", "Edit Resources");
operationMap.put("editinstancestorage", "Edit Instance Storage");
operationMap.put("edit_aws_instance_type", "Modify AWS Instance Type");
operationMap.put("edit_aws_instance_storage", "Modify AWS Instance Storage");
operationMap.put("edit_azure_instance_storage", "Modify AZURE Instance Storage");
operationMap.put("modify_monitoring_settings", "Modify Monitoring Settings");
operationMap.put("remove_monitoring_profile", "Remove Monitoring Profile");
operationMap.put("converttoimage", "Clone to Image");
operationMap.put("create_image_template", "Create Image Template");
operationMap.put("create_flex_image", "Create Flex Image");
operationMap.put("manage_private_ip_addresses_of_an_instance", "Manage Private IP Addresses");
operationMap.put("attach_network_interface", "Attach Network Interface");
operationMap.put("detach_network_interface", "Detach Network Interface");
operationMap.put("add_edit_tags", "Add/Edit Tags");
operationMap.put("execute_script", "Execute Script");
operationMap.put("export", "Export");
operationMap.put("migrate_to_aws", "Migrate to AWS");
operationMap.put("update_package", "Update Package");
operationMap.put("security_scan", "Security Scan");
operationMap.put("unpause", "Unpause");
operationMap.put("snmp_update_details", "Update SNMP Details");
operationMap.put("wmi_update_details", "Update WMI Details");
operationMap.put("remove", "Remove");
operationMap.put("apply_patch", "Apply Patch");
var operationType;
   
function confirmOperationOnInstances(validInstancesArray, invalidInstancesArray, operationToPerform, form, operationId, providerType, multiOSNameSize, accessPermission, validInstancesPowerOnTimeArray, validInstancesTotalChargeArray,invalidInstancesBasedonAutoStateArray,notPoweredOnInstances,ipAddressNotSetInstances,agentNotInstalled,agentNotRunningInstances,mpAlreadyAppliedOrSuspended,mpNotApplied,loginDetailsNotUpdatedInstances,validSelectedInstanceCount){
           //Setting global variables: need to re-visit their usage and essence
	operationType = form+''+operationToPerform;
	   var operType;
	   if(accessPermission == 'true'){
	   
		   //Extracting instances array from strings:[xxx, yyy]
		   invalidInstancesBasedonAutoStateArray = invalidInstancesBasedonAutoStateArray != '[]' ? ((((invalidInstancesBasedonAutoStateArray.split('['))[1]).split(']'))[0]).split(',') : new Array();
		   validInstancesArray = validInstancesArray != '[]' ? ((((validInstancesArray.split('['))[1]).split(']'))[0]).split(',') : new Array();
		   invalidInstancesArray = invalidInstancesArray != '[]' ? ((((invalidInstancesArray.split('['))[1]).split(']'))[0]).split(',') : new Array();
		   //Everything user selected is valid: if invalid instances list is empty and valid instances list has some data
		   var isAllValidUserSelection = (validInstancesArray.length > 0 && invalidInstancesArray.length == 0 && invalidInstancesBasedonAutoStateArray.length == 0);
		   //Everything user selected is invalid: if valid instances list is emtpy and invalid instances list has some data
		   var isAllInvalidUserSelection = ((invalidInstancesArray.length > 0 || invalidInstancesBasedonAutoStateArray.length >0) && validInstancesArray.length == 0);
		   //User selected mix of valid and invalid instances: both valid & invalid lists have some data
		   var isMixtureOfValidInvalidSelection = ((invalidInstancesArray.length > 0 || invalidInstancesBasedonAutoStateArray.length > 0) && validInstancesArray.length > 0);
		   //User did not select anything: Ideally the button shouldn't be enabled for no selection but this workaround for the time being.
		   var isNoInstanceSelected = (invalidInstancesArray.length == 0 && validInstancesArray.length == 0 && invalidInstancesBasedonAutoStateArray.length == 0);
                   //Extracting power on times array from strings:[xxx, yyy]
		   validInstancesPowerOnTimeArray = validInstancesPowerOnTimeArray != '[]' ? ((((validInstancesPowerOnTimeArray.split('['))[1]).split(']'))[0]).split(',') : new Array();
		   validInstancesTotalChargeArray = validInstancesTotalChargeArray != '[]' ? ((((validInstancesTotalChargeArray.split('['))[1]).split(']'))[0]).split(',') : new Array();
		   
		   // In case of Enable/Disable Monitoring action exact reason for Invalidity of instances is required to be displayed
		   notPoweredOnUserSelectedInstancesArray = notPoweredOnInstances != '[]' ? ((((notPoweredOnInstances.split('['))[1]).split(']'))[0]).split(',') : new Array();
		   ipAddressNotSetUserSelectedInstancesArray = ipAddressNotSetInstances != '[]' ? ((((ipAddressNotSetInstances.split('['))[1]).split(']'))[0]).split(',') : new Array();
		   agentNotInstalledUserSelectedInstancesArray = agentNotInstalled != '[]' ? ((((agentNotInstalled.split('['))[1]).split(']'))[0]).split(',') : new Array();
		   agentNotRunningUserSelectedInstancesArray = agentNotRunningInstances != '[]' ? ((((agentNotRunningInstances.split('['))[1]).split(']'))[0]).split(',') : new Array();
		   mpAlreadyAppliedOrSuspendedUserSelectedInstancesArray = mpAlreadyAppliedOrSuspended != '[]' ? ((((mpAlreadyAppliedOrSuspended.split('['))[1]).split(']'))[0]).split(',') : new Array();
		   mpNotAppliedUserSelectedInstancesArray = mpNotApplied != '[]' ? ((((mpNotApplied.split('['))[1]).split(']'))[0]).split(',') : new Array();
		   loginDetailsNotUpdatedInstanceArray = loginDetailsNotUpdatedInstances != '[]' ? ((((loginDetailsNotUpdatedInstances.split('['))[1]).split(']'))[0]).split(',') : new Array();
		   //User selected invalid hypervisors
		   //TBD
		   var isInvalidHypervisorSelections = false;
		   	   
	       operType = operationToPerform;
	       var toBePassedArgumentsArray = new Array (validInstancesArray, invalidInstancesArray, operType, validInstancesPowerOnTimeArray, validInstancesTotalChargeArray,invalidInstancesBasedonAutoStateArray,notPoweredOnUserSelectedInstancesArray, ipAddressNotSetUserSelectedInstancesArray, agentNotInstalledUserSelectedInstancesArray, agentNotRunningUserSelectedInstancesArray, mpAlreadyAppliedOrSuspendedUserSelectedInstancesArray, mpNotAppliedUserSelectedInstancesArray,loginDetailsNotUpdatedInstanceArray);
		   var providerType = providerType; 
		   
		   var errorCode = document.getElementById('actionFormId:errorCoderHiddenId');
		   if(errorCode != null && errorCode != undefined){
			   errorCode = document.getElementById('actionFormId:errorCoderHiddenId').value;
		   }
		   
		   if(errorCode == '0xbe0g0aq'){
			   var paramArray = new Array();
			   paramArray[0]='Instances.';
			   validateDataUsingMsgId(MessageTypeEnum.ERROR,errorCode,paramArray,'');	
			   document.getElementById('actionFormId:errorCoderHiddenId').value = '';
			   return false;
		   }
		   
		   //If selected items more than twenty
		   var selectedTwentyItems = document.getElementById('actionFormId:errorCoderHiddenId');
		   if(selectedTwentyItems != null && selectedTwentyItems != undefined){
			   selectedTwentyItems = document.getElementById('actionFormId:errorCoderHiddenId').value;
		   }
		   
		   if(selectedTwentyItems == '0xcb0g0zz'){
			   var paramArray = new Array();
			   paramArray[0]='Instances.';
			   validateDataUsingMsgId(MessageTypeEnum.ERROR,selectedTwentyItems,paramArray,'');	
			   document.getElementById('actionFormId:errorCoderHiddenId').value = '';
			   return false;
		   }else{	   
			   switch(true){
			   	case isNoInstanceSelected:
				   return validateIfNoInstanceSelected(toBePassedArgumentsArray);
			
			   	case isAllInvalidUserSelection:
				    return validateForAllInvalidUserSelection(toBePassedArgumentsArray);
				   
			   	case isAllValidUserSelection:
			   		if(operType == 'poweron' || operType == 'poweroff'|| operType == 'shutdown' || operType == 'enablemonitoring' 
			   			|| operType == 'disablemonitoring' || operType == 'execute_custom_script' || operType == 'update_package' || operType == 'edit_aws_instance_type' || operType == 'editresources' || operType == 'edit_savvis_instance_resources' 
			   			|| operType == 'security_scan' || operType == 'converttoimage' || operType == 'create_image_template' || operType == 'create_flex_image' || operType == 'openstackclonetoimage' || operType == 'azureclonetoimage' || operType == 'openstackeditresources' 
			   			|| operType == 'remove_monitoring_profile' || operType == 'apply_patch'){
			   			return showActionConfirmPopupForInstance(operType,toBePassedArgumentsArray);
			   		}else{
			   			return validateForAllValidUserSelection(toBePassedArgumentsArray,providerType,multiOSNameSize,validSelectedInstanceCount);
			   		}
				   
			   	case isMixtureOfValidInvalidSelection:
			   		if(operType == 'poweron' || operType == 'poweroff'|| operType == 'shutdown'){
			   			return document.getElementById('actionFormId:setDefaultNotifications').click();
			   		}else{
			   			return validateForMixtureOfValidInvalidUserSelection(toBePassedArgumentsArray,multiOSNameSize);
			   		}
				  
			   	case isInvalidHypervisorSelections:
			   		return validateForInvalidHypervisorSelections(toBePassedArgumentsArray);
			   }
		   }
	   }else{
		   hideFadeLoadingImg();
		   var paramArray = new Array();
			validateDataUsingMsgId(MessageTypeEnum.ERROR,"ce0i0aa",paramArray,'');
			return false;
	   }
  }
var userSelectedOperType="";
var validInstArray=new Array();
function showActionConfirmPopupForInstance(operType,toBePassedArgumentsArray){
	var validInstancesArray = toBePassedArgumentsArray[0];
	jQuery("#instanceActionPopUp").find("#operationType").html("'"+operationMap.get(operType)+"'");
	userSelectedOperType = operType;
	 if(operType == 'update_package'){
		if (validInstancesArray.length == 1) {
			return updatePackageForInstance();
		} else {
			hideFadeLoadingImg();
			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0al', paramArray,'');
		}
	}else if(operType == 'edit_aws_instance_type'){
		if (validInstancesArray.length == 1) {
			return document.getElementById('actionFormId:actionOnAWSEditInstTypeId').onclick();
			} else {
				hideFadeLoadingImg();
				return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0ae', paramArray,'');
			}
		
	}else if(operType == 'editresources'){
		hideFadeLoadingImg();
		if (validInstancesArray.length == 1) {
				return cloudOnePopup('instanceActionPopUp',650); 
			} else {
				return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0ae', paramArray,'');
			}
	}else if(operType == 'edit_savvis_instance_resources'){
		if (validInstancesArray.length) {
			return document.getElementById('actionFormId:actionOnSavvisEditResourceId').onclick();
			} else {
				hideFadeLoadingImg();
				return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0ae', paramArray,'');
			}

	}else if(operType == 'security_scan'){
		showSecurityScanPopup();
	}else if(operType == 'converttoimage' || operType == 'azureclonetoimage' || operType == 'create_image_template'){
		if(validInstancesArray.length == 1){
			return document.getElementById('actionFormId:actionOnConvertToImage').onclick();
		 }else{
			 hideFadeLoadingImg();
			 return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0df',paramArray,'');
		 }
	}else if(operType == 'create_flex_image'){
		if(validInstancesArray.length == 1){
				return document.getElementById('actionFormId:actionOnConvertToImage').onclick();
		 }else if(validInstancesArray.length > 1){
			 hideFadeLoadingImg();
			 return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0df',paramArray,'');
		 }else {
			 hideFadeLoadingImg();
			 return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xgg0i0aj',paramArray,'');
		 }
	}
	else if(operType == 'openstackeditresources'){
		if(validInstancesArray.length == 1){
			return document.getElementById('actionFormId:actionOpenstackEditResource').onclick();
		 }else{
			 hideFadeLoadingImg();
			 return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0df',paramArray,'');
		 }
	}else if(operType == 'openstackclonetoimage'){
		if(validInstancesArray.length == 1){
			return document.getElementById('actionFormId:actionOpenstackCloneToImage').onclick();
		 }else{
			 hideFadeLoadingImg();
			 return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0df',paramArray,'');
		 }
	}else if(operType == 'apply_patch'){
		if(validInstancesArray.length > 1){
			jQuery(".patchApplyPreference").removeClass("hide");
		}
		hideFadeLoadingImg();
		return cloudOnePopup('instanceActionPopUp',650); 
	}
	else{
		document.getElementById('actionFormId:setDefaultNotifications').click();
	}
}

function showInstanceActionPopUp(){
	jQuery("#instanceActionPopUp").find("#operationType").html("'"+operationMap.get(userSelectedOperType)+"'");
	cloudOnePopup('instanceActionPopUp',650);
}

function checkDeletedInstance(deletedInstanceArray,operation) {
	deletedInstanceArray = deletedInstanceArray != '[]' ? ((((deletedInstanceArray.split('['))[1]).split(']'))[0]).split(',') : new Array();
	operationType = operationMap.get(operation);
	var blankSpace = '  ';
	var requiredMsgFormat = new String();
	paramArray = new Array();
	if(deletedInstanceArray.length > 0){
		hideFadeLoadingImg();
		for ( var p = 0; p < deletedInstanceArray.length; p++) {
			if (deletedInstanceArray[p] != '') {
				requiredMsgFormat = requiredMsgFormat.concat('<div  style="padding-left:10px;"><span class=\"bulletpoint\"></span>' + blankSpace);
				requiredMsgFormat = requiredMsgFormat.concat(deletedInstanceArray[p]);
				requiredMsgFormat = requiredMsgFormat.concat('</div>');
			}
		}
	} else {
		return true;
	}
	paramArray.push(requiredMsgFormat);
	paramArray.push(operationType);
	validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0i0dw',paramArray,'');
	return false;
}

function showSecurityScanPopup(){
	jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:securityScan")).click();
}
function instanceActionPerform(){
	jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:instActionConfirm")).attr("disabled",true);
	document.getElementById('actionFormId:actionOnInstId').onclick();
}

function checkOperation(errorcode){
	if(errorcode!=''){
		 return validateDataUsingMsgId(MessageTypeEnum.ERROR,errorcode,paramArray,'');
	}
}
   
function validateSchedulingOfInstances(validInstancesArray,invalidInstancesArray,actionName,mixedValid,errorCode,actionNotScheduled){
	var paramArray = new Array();
	var blankSpace = '  '; 
	if(errorCode == ""){
		if(actionNotScheduled == 'true'){
			if(actionName == "EDITRESOURCES" || actionName == "EDIT_AWS_INSTANCE_TYPE"){
				closeAllPopUps();
			}else{
				triggerAction();	
			}
		}else{
			closePopUpUsingId('instanceActionPopUp');
			if(null!=actionName && (actionName=="CONVERTTOIMAGE" || actionName=="CREATE_IMAGE_TEMPLATE" || actionName=="CREATE_FLEX_IMAGE")){
				showScheduledMessage("true", actionName);
			}else if(null!=operationType && operationType!="actionFormId:clone" && operationType!="actionFormId:clonetotemplate"){
				showResponseInline('0xee0i0ac',paramArray,'refreshAndClosePoUp');
			}
		}
	}
	else if(errorCode == "0xcb0g0a2" || errorCode=="0xbe0g0a3"){
		if(errorCode == "0xcb0g0a2"){
			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0a2',paramArray,'');
		}else{
				validInstancesArray = validInstancesArray != '[]' ? ((((validInstancesArray.split('['))[1]).split(']'))[0]).split(',') : new Array();
			    invalidInstancesArray = invalidInstancesArray != '[]' ? ((((invalidInstancesArray.split('['))[1]).split(']'))[0]).split(',') : new Array();
				hideFadeLoadingImg();
				var instances = new String();
				var invalidInstances = new String();
				
				instances = instances.concat('<div style="height:5px;">'+blankSpace+'</div>');
				invalidInstances = invalidInstances.concat('<div style="height:5px;">'+blankSpace+'</div>');
				for(var i=0;i<validInstancesArray.length; i++){
					
					if(i == 0){
					   
					   instances = instances.concat('<div style="padding-left:10px;">');
					   instances = instances.concat('<span class="bulletpoint"></span>'+blankSpace);
					}else{
						
					   instances = instances.concat('<div style="padding-left:10px;">');	
					   instances = instances.concat('<span class="bulletpoint"></span>'+blankSpace);
					}
					
					instances = instances.concat(validInstancesArray[i]);
					instances = instances.concat('</div>');
				}
				
				for(var i=0;i<invalidInstancesArray.length; i++){
					
					if(i == 0){
						
		   				invalidInstances = invalidInstances.concat('<div style="padding-left:10px;">');
		   				invalidInstances = invalidInstances.concat('<span class="bulletpoint"></span>'+blankSpace);
		   			}else{
		   				
		   				invalidInstances = invalidInstances.concat('<div style="padding-left:10px;">');	
		   				invalidInstances = invalidInstances.concat('<span class="bulletpoint"></span>'+blankSpace);
		   			}
					
		   			invalidInstances = invalidInstances.concat(invalidInstancesArray[i]);
		   			invalidInstances = invalidInstances.concat('</div>');
		   		}
				
				
			    if(invalidInstancesArray.length > 0){
			    	paramArray[0] = invalidInstances.toString();
				}
			    paramArray[1] = instances.toString();
        		if(invalidInstancesArray.length > 0 ){
        			return validateDataUsingMsgId(MessageTypeEnum.CONFIRMATION,'0xbe0g0a3',paramArray,'continueAction');
        		}
		}
	}
}

function continueAction(){
	document.getElementById('actionFormId:actionOnInstContinueId').onclick();
	closePopUpUsingId('instanceActionPopUp');
	if(null!=operationType && operationType!="actionFormId:clone" && operationType!="actionFormId:clonetotemplate"){
		showResponseInline('0xee0i0ac',paramArray,'refreshAndClosePoUp');
	}
}

function triggerAction(){
	document.getElementById('actionFormId:triggerActionOnInstId').onclick();
	if(null!=operationType && operationType!="actionFormId:clone" && operationType!="actionFormId:clonetotemplate"){
		showResponseInline('0xee0i0ac',paramArray,'refreshAndClosePoUp');
	}
}
function applyMonitoringTemplate() {
	document.getElementById('applyTemplateActionFormId:actionOnApplyTempId').onclick();
	cloudOnePopup('apply_monitoring_template',500);
}

function reSetInstanceGroupDeployment(){
	document.getElementById('actionFormId:newInstGroupId').selectedIndex=0;
	document.getElementById('actionFormId:newDeploymentId').selectedIndex=0;
}

//Function called if no instance is selected
function validateIfNoInstanceSelected(toBePassedArgumentsArray){
	hideFadeLoadingImg();
	paramArray = new Array();
	paramArray[0]='instance';
	return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0aw',paramArray,'');
}

//Function call if all user selections are valid
function validateForAllValidUserSelection(toBePassedArgumentsArray, provider,multiOSNameSize,validSelectedInstancesCount){
	var blankSpace = '  '; 
	var validInstancesArray = toBePassedArgumentsArray[0];
	var operType = toBePassedArgumentsArray[2];
	var providerType = provider;
        var validInstancesPowerOnTimeArray = toBePassedArgumentsArray[3];
        var validInstancesTotalChargeArray = toBePassedArgumentsArray[4];
	paramArray = new Array();
	paramArray[0]=operationMap.get(operType);
	if(validInstancesArray.length > 0 && paramArray[0] == 'Add/Edit Tags'){
		hideFadeLoadingImg();
		if(validInstancesArray.length > 1){
	 		return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xbe0g0apa',paramArray,'');
		}else{
			cloudOnePopup('tags_popup',550);
			C1Tags.renderTags();
			return true;
		}
	}else if(validInstancesArray.length > 0 && paramArray[0] == 'Login'){
		hideFadeLoadingImg();
		if(multiOSNameSize > 1){
	 		return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xbe0g0ap',paramArray,'');
		}else{
			return selectLoginType();
		}
	}else if(validInstancesArray.length > 0 &&  paramArray[0] == 'Execute Script'){
		hideFadeLoadingImg();
		if(multiOSNameSize > 1){
	 		return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xae0i0ae',paramArray,'');
		}else{
			return showEcsPopup();
		}
	}
	else if(validInstancesArray.length > 0 && paramArray[0] == 'Update Instance Details'){
		hideFadeLoadingImg();
		if(document.getElementById('vmInstancesFormId')!=null){
		
		  show_hide_NickName('vmInstancesFormId');
		  cloudOnePopup('vmware_instance_details',500);
		}
		if(document.getElementById('formId')!=null){
		  show_hide_NickName('formId');
		  cloudOnePopup('instance_details', 500);
		}
		if(document.getElementById('actionFormId')!=null){
		  hideFadeLoadingImg();
		  var newNickNameDivId = document.getElementById("newNickNameDivId");
		  document.getElementById('actionFormId:selInstCount').value = validInstancesArray.length;
		  if(validSelectedInstancesCount == 1){
			  var nickNameTextbox =document.getElementById("actionFormId:newNickNameId");
			  nickNameTextbox.value = '';
			  nickNameTextbox.value = validInstancesArray[0].toString();
			  newNickNameDivId.style.display = 'block';
		  }else{
			  newNickNameDivId.style.display = 'none';
			  reSetInstanceGroupDeployment();
		  }
		   cloudOnePopup('vmware_instance_details',500);
		}
		return;
	}else if(validInstancesArray.length > 0 && paramArray[0] == 'Modify Monitoring Settings'){
		hideFadeLoadingImg();
		if (validSelectedInstancesCount == 1) {
			return cloudOnePopup('modify_monitoring_status_popupId',500);
		} else {
			paramArray[0] = 'Modify Monitoring Settings';
			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0a0ae',paramArray,'');
		}
	}else if(validInstancesArray.length > 0 && paramArray[0] == 'Clone VM'){
		hideFadeLoadingImg();
		var instancesForClone = new String();
		 for(var i=0;i<validInstancesArray.length; i++){
			 instancesForClone = instancesForClone.concat('<tr><td class="columnWidth">');	
			 instancesForClone = instancesForClone.concat('<span class="bulletpoint"></span>'+blankSpace);
			 instancesForClone = instancesForClone.concat(validInstancesArray[i]);
			 instancesForClone = instancesForClone.concat('</td></tr>');
			}
		instancesForClone = instancesForClone.concat('<tr class="blankRowHeight"/>');
	    document.getElementById("cloneVMContent_id").innerHTML =instancesForClone.toString();
		return cloudOnePopup('clone_vm_confirm_popupId',500);
	}else if(validInstancesArray.length > 0 && paramArray[0] == 'Delete'){
		var instancesForDeletion = new String();
		instancesForDeletion = instancesForDeletion.concat('<div id="confirmMessage" style="padding-left: 16px;">Confirm Deletion of:</div>');
		instancesForDeletion = instancesForDeletion.concat('<table class="popupContentLayout2">');
                instancesForDeletion = instancesForDeletion.concat('<tr class="blankRowHeight"/>');
                instancesForDeletion = instancesForDeletion.concat('<tr><td class="columnWidth"><span class="bold">Instance name</span></td>');
                instancesForDeletion = instancesForDeletion.concat('<td class="columnWidth"><span class="bold">Total Powered-On time</span></td>');
                instancesForDeletion = instancesForDeletion.concat('<td class="columnWidth"><span class="bold">Total Show-Back Cost</span></td></tr>');
                for(var i=0;i<validInstancesArray.length; i++){
			instancesForDeletion = instancesForDeletion.concat('<tr><td class="columnWidth">');	
			instancesForDeletion = instancesForDeletion.concat('<span class="bulletpoint"></span>'+blankSpace);
			instancesForDeletion = instancesForDeletion.concat(validInstancesArray[i]);
                        instancesForDeletion = instancesForDeletion.concat('</td><td class="columnWidth">');
                        instancesForDeletion = instancesForDeletion.concat(validInstancesPowerOnTimeArray[i]);
                        instancesForDeletion = instancesForDeletion.concat('</td><td class="columnWidth">');
                        instancesForDeletion = instancesForDeletion.concat(validInstancesTotalChargeArray[i]);
                        instancesForDeletion = instancesForDeletion.concat('</td></tr>');
		}
                        instancesForDeletion = instancesForDeletion.concat('<tr class="blankRowHeight"/>');
                        instancesForDeletion = instancesForDeletion.concat('<tr class="blankRowHeight"/>');
			instancesForDeletion = instancesForDeletion.concat('</table>');
    		document.getElementById("messageContentForDelete").innerHTML =instancesForDeletion.toString();
    		document.getElementById("actionFormId:invokegetNotifications").onclick();
    		return;
	} else if(paramArray[0] == 'Migrate to AWS'){
		hideFadeLoadingImg();
			return showPopup("migrateAWSPopup", 635);
   	}else if(validInstancesArray.length > 0 && paramArray[0] == 'Update SNMP Details'){
		hideFadeLoadingImg();
		if (validSelectedInstancesCount == 1) {
			enableVersions();
			return cloudOnePopup('SNMP_Update_Status_popupId',500);
		} else {
			paramArray[0] = 'Update SNMP Details';
			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0a0ae',paramArray,'');
		}
	}else if(validInstancesArray.length > 0 && paramArray[0] == 'Update WMI Details'){
		hideFadeLoadingImg();
		if (validSelectedInstancesCount == 1) {
			validatePwdText();
			 cloudOnePopup('WMI_Update_status',500);
			 setfocus('actionFormId:wmiUserId');
			 return true;
		} else {
			paramArray[0] = 'Update WMI Details';
			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0a0ae',paramArray,'');
		}
	}
	   
	if(validInstancesArray.length > 0 && paramArray[0] == 'Shutdown' && providerType == 'POWERVM'){
		hideFadeLoadingImg();
		return validateDataUsingMsgId(MessageTypeEnum.CONFIRMATION,'0xbe0g0am',paramArray,'instanceActionPerform');
	}
	else if(validInstancesArray.length > 0 &&  paramArray[0] == 'Export'){
		hideFadeLoadingImg();
		LoadingButtonUtils.changeBackToOrginalValue();
		document.getElementById('actionFormId:storageLocationId:0').checked = true;
		document.getElementById('actionFormId:storageLocationId:1').checked = false;
		 if(providerType == 'AMAZON'){
				document.getElementById('actionFormId:vmPortabilityforAWSProvider').onclick();
				return;
			 }
		 return showPopup("exportVM", 600);
	}
	   
	if(validInstancesArray.length > 0 && paramArray[0] != 'Delete'){
		var instances = new String();
		instances = instances.concat('<div style="height:5px;">'+blankSpace+'</div>');		
		for(var i=0;i<validInstancesArray.length; i++){
			if(i == 0){
				instances = instances.concat('<div style="padding-left:10px;">');
    			instances = instances.concat('<span class="bulletpoint"></span>'+blankSpace);
			}else{
				instances = instances.concat('<div style="padding-left:10px;">');
				instances = instances.concat('<span class="bulletpoint"></span>'+blankSpace);
			}
			instances = instances.concat(validInstancesArray[i]);
			instances = instances.concat('</div>');
		}
		   
		paramArray[1] = instances.toString();
		if(paramArray[0] == 'Apply Template'){
			hideFadeLoadingImg();
			return validateDataUsingMsgId(MessageTypeEnum.CONFIRMATION,'0xbe0g0ab',paramArray,'applyMonitoringTemplate');
		}else if(paramArray[0] == 'Change Assignee'){
			hideFadeLoadingImg();
			return validateDataUsingMsgId(MessageTypeEnum.CONFIRMATION,'0xbe0g0ab',paramArray,'goToChangeAssigneePage');
		}else if(paramArray[0] == 'Edit Resources'){
			hideFadeLoadingImg();
			if (validSelectedInstancesCount == 1) {
					return cloudOnePopup('vmware_edit_instance_resources', 650);
				} else {
					return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0ae', paramArray,'');
				}
		}else if(paramArray[0] == 'Edit Instance Storage'){
			 if(validSelectedInstancesCount == 1){
				return document.getElementById('actionFormId:actionOnEditInstStorId').onclick();
			 }else{
				 hideFadeLoadingImg();
				 return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0af',paramArray,'');
			 }
		}else if(paramArray[0] == 'Modify AWS Instance Type'){
			if (validSelectedInstancesCount == 1) {
				return document.getElementById('actionFormId:actionOnAWSEditInstTypeId').onclick();
				} else {
					hideFadeLoadingImg();
					return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0ae', paramArray,'');
				}
		} else if(paramArray[0] == 'Modify AWS Instance Storage'){
			 if(validSelectedInstancesCount == 1){
					return document.getElementById('actionFormId:actionOnEditAWSStorageId').onclick();
			 }else{
				hideFadeLoadingImg();
				 return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0af',paramArray,'');
			 }
		}else if(paramArray[0] == 'Modify AZURE Instance Storage'){
			 if(validSelectedInstancesCount == 1){
					return document.getElementById('actionFormId:actionOnEditAZUREStorageId').onclick();
			 }else{
				hideFadeLoadingImg();
				 return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0af',paramArray,'');
			 }
		}else if(paramArray[0] == 'Modify Compute'){
			if (validSelectedInstancesCount == 1) {
				return document.getElementById('actionFormId:actionOnSavvisEditResourceId').onclick();
				} else {
					hideFadeLoadingImg();
					return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0ae', paramArray,'');
				}
		}else if(paramArray[0] == 'Modify Storage'){
			if(validSelectedInstancesCount == 1){
				return document.getElementById('actionFormId:actionOnSavvisEditStorageId').onclick();
				 }else{
					hideFadeLoadingImg();
					 return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0af',paramArray,'');
				 }
		}else if(paramArray[0] == 'Clone Instance(s)'){
			hideFadeLoadingImg();
			if(validSelectedInstancesCount == 1){
				return cloudOnePopup('savvis_clone_vm', 450);
				 }else{
					return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xbe0g0ae',paramArray,'');
				 }
		} else if(paramArray[0] == 'Capture vApp'){
			hideFadeLoadingImg();
			if(validSelectedInstancesCount != 1){
				return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xbe0g0af',paramArray,'');
				 }
		}  else if(paramArray[0] == 'Manage Private IP Addresses'){
			hideFadeLoadingImg();
			 if(validSelectedInstancesCount == 1){
				 return document.getElementById('actionFormId:actionOnManagePrivateIPAddress').onclick();
			 }else{
				 return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0i0eu',paramArray,'');
			 }
		}  else if(paramArray[0] == 'Attach Network Interface' || paramArray[0] == 'Detach Network Interface' ){
			hideFadeLoadingImg();
			 if(validSelectedInstancesCount == 1){
				 var errorCode = jQuery(	"#"	+ JSUtils.prototype
						.escapeColon("actionFormId:detachError")).val();
				jQuery(	"#"	+ JSUtils.prototype
						.escapeColon("actionFormId:eni_attach_detach_hdr")).text(paramArray[0]);
				jQuery(	"#"	+ JSUtils.prototype
						.escapeColon("actionFormId:instanceNamEni")).text(validInstancesArray[0].toString());

				if(errorCode != null && errorCode != '') {
					return validateDataUsingMsgId(MessageTypeEnum.ERROR,errorCode,paramArray,'');
				} else {
					return cloudOnePopup('attach_detach_ENI',500);
				}
			 }else{
				 return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0i0eu',paramArray,'');
			 }
		}else if(paramArray[0] == 'Security Scan'){
			hideFadeLoadingImg();
			return validateDataUsingMsgId(MessageTypeEnum.CONFIRMATION,'0xbe0g0ab',paramArray,'showSecurityScanPopup');
		}
		if(paramArray[0] == 'Capture vApp') {
			paramArray[0] = 'Clone to Template';
		}
		hideFadeLoadingImg();
		return validateDataUsingMsgId(MessageTypeEnum.CONFIRMATION,'0xbe0g0ab',paramArray,'instanceActionPerform');
	}else{
		hideFadeLoadingImg();
		return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0ag',paramArray,'instanceActionPerform');
	}
}

function loadEditInstanceStorage(){
	cleanupDataInEditStorage();
	return cloudOnePopup('vmware_edit_instance_storage',600);  
}

function loadConvertToImagePopup() {
	return cloudOnePopup('instanceActionPopUp',650);  
}

//Function called if all user selections are invalid
function validateForAllInvalidUserSelection(toBePassedArgumentsArray){
	hideFadeLoadingImg();
	var blankSpace = '  '; 
	var invalidInstancesArray = toBePassedArgumentsArray[1];
	var invalidInstancesBasedonAutoStateArray = toBePassedArgumentsArray[5];
	var operType = toBePassedArgumentsArray[2];
		   
	paramArray = new Array();
	
	if(invalidInstancesArray.length > 0 || invalidInstancesBasedonAutoStateArray.length > 0){
		var instances = new String();
		instances = instances.concat('<div style="height:5px;">'+blankSpace+'</div>');
		
		for(var i=0;i<invalidInstancesArray.length; i++){
			if(i == 0){
				instances = instances.concat('<div style="padding-left:10px;">');
				instances = instances.concat('<span class="bulletpoint"></span>'+blankSpace);
			}else{
				instances = instances.concat('<div style="padding-left:10px;">');
				instances = instances.concat('<span class="bulletpoint"></span>'+blankSpace);
			}
			instances = instances.concat(invalidInstancesArray[i]);
			instances = instances.concat('</div>');
		}
		
		if(invalidInstancesBasedonAutoStateArray.length > 0){
			var instancesWithAutomationState = new String();
			instancesWithAutomationState = instancesWithAutomationState.concat('<div style="height:5px;">'+blankSpace+'</div>');
			
			for(var j=0;j<invalidInstancesBasedonAutoStateArray.length; j++){
				if(j == 0){
					instancesWithAutomationState = instancesWithAutomationState.concat('<div style="padding-left:10px;">');
					instancesWithAutomationState = instancesWithAutomationState.concat('<span class="bulletpoint"></span>'+blankSpace);
				}else{
					instancesWithAutomationState = instancesWithAutomationState.concat('<div style="padding-left:10px;">');
					instancesWithAutomationState = instancesWithAutomationState.concat('<span class="bulletpoint"></span>'+blankSpace);
				}
				instancesWithAutomationState = instancesWithAutomationState.concat(invalidInstancesBasedonAutoStateArray[j]);
				instancesWithAutomationState = instancesWithAutomationState.concat('</div>');
			}
		}
		
	}
	if(invalidInstancesArray.length > 0 && invalidInstancesBasedonAutoStateArray.length > 0){
	    paramArray[0] = instances.toString();
	    paramArray[1] = instancesWithAutomationState.toString();
	}else if(invalidInstancesArray.length > 0){
		paramArray[0] = instances.toString();
	}else if(invalidInstancesBasedonAutoStateArray.length > 0){
		paramArray[0] = instancesWithAutomationState.toString();
	}
	
    if(operationMap.get(operType) == 'Edit Resources'){
    	return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0aj',paramArray,'');
    }else if(operationMap.get(operType) == 'Edit Instance Storage'){
    	return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0ah',paramArray,'');
    }else if(operationMap.get(operType) == 'Modify AWS Instance Type'){
    	return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0aj',paramArray,'');
    }else if(operationMap.get(operType) == 'Modify AWS Instance Storage'){
    	return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0ah',paramArray,'');
    }else if(operationMap.get(operType) == 'Clone to Image'){
    	return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0i0pv',paramArray,'');
    }else if(operationMap.get(operType) == 'Clone Instance(s)'){
    	if(invalidInstancesArray.length == 1){
 			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xbe0g0ac',paramArray,'');
  		 }else{
  			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xbe0g0ae',paramArray,'');
         }
    }else if(operationMap.get(operType) == 'Modify Monitoring Settings'){
  			 if (invalidInstancesArray.length == 1) {
  			checkModifyMonSettingsError(paramArray);
 		 }else{
 			paramArray[0] = 'Modify Monitoring Settings';
 			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0a0ae',paramArray,'');
 		}
    }else if(operationMap.get(operType) == 'Enable Monitoring'){
    	displayInstallAgentEnableDisableMonitoringErrors(toBePassedArgumentsArray,'Enable Monitoring','0xcb0a0az');
    }else if(operationMap.get(operType) == 'Disable Monitoring'){
    	displayInstallAgentEnableDisableMonitoringErrors(toBePassedArgumentsArray,'Disable Monitoring','0xcb0a0az');
    }else if(operationMap.get(operType) == 'Remove Monitoring Profile'){
    	displayInstallAgentEnableDisableMonitoringErrors(toBePassedArgumentsArray,'Remove Monitoring Profile','0xcb0a0az');
    }else if(operationMap.get(operType) == 'Capture vApp'){
    	if(invalidInstancesArray.length != 1){
 			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xbe0g0af',paramArray,'');
  		 }
    }else if(operationMap.get(operType) == 'Update Package'){
    	return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0ap',paramArray,'');
    }else if(operationMap.get(operType) == 'Update SNMP Details'){
			 if (invalidInstancesArray.length == 1) {
		  			checkModifyMonSettingsError(paramArray);
		 		 }else{
		 			paramArray[0] = 'Update SNMP Details';
		 			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0a0ae',paramArray,'');
		 		}
		    }
    else if(operationMap.get(operType) == 'Update WMI Details'){
		 if (invalidInstancesArray.length == 1) {
			 checkModifyMonSettingsError(paramArray);
	 		 }else{
	 			paramArray[0] = 'Update WMI Details';
	 			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0a0ae',paramArray,'');
	 		}
	    }
    
    else if(operType == 'installmonitoragent'){
    	displayInstallAgentEnableDisableMonitoringErrors(toBePassedArgumentsArray,'Install Agent','0xcb0a0az');	
    }
	else if(operType == 'export'){
	 		  paramArray[0] = 'Export VM';
	 		 paramArray[1] = instances.toString();
	 			 if(invalidInstancesArray.length > 1){
	 			 		return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xae0i0ai',paramArray,'');
	 				}
	 			 else{
	 				 return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xae0i0aj',paramArray,'');
	 			 }
	 		 }
   
    else{
    	if(operType == 'poweron' || operType == 'shutdown' || operType == 'restart' 
    		|| operType == 'poweroff' || operType == 'stop' || operType == 'reboot' || operType == 'start'){
    		if(invalidInstancesArray.length > 0 && invalidInstancesBasedonAutoStateArray.length == 0){
    			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0ah',paramArray,'');
    		}else if(invalidInstancesBasedonAutoStateArray.length > 0 && invalidInstancesArray.length == 0){
    			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0ar',paramArray,'');
    		}else if(invalidInstancesBasedonAutoStateArray.length > 0 && invalidInstancesArray.length > 0){
				return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0aq',paramArray,'');
    		}
    	}else if(operType == 'create_flex_image'){
    		if(invalidInstancesArray.length > 0 && invalidInstancesBasedonAutoStateArray.length == 0){
    			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xgg0i0aj',paramArray,'');
    		}
    		else{
    			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0ah',paramArray,'');
    		}
    	}else{
    		return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0ah',paramArray,'');
    	}
    		
    	
    }
}

//Function called if user selects a mixture of valid and invalid instances
function validateForMixtureOfValidInvalidUserSelection(toBePassedArgumentsArray,multiOSNameSize){
	var blankSpace = '  ';
	var validInstancesArray = toBePassedArgumentsArray[0];
	var invalidInstancesArray = toBePassedArgumentsArray[1];
	var operType = toBePassedArgumentsArray[2];
	var validInstancesPowerOnTimeArray = toBePassedArgumentsArray[3];
    var validInstancesTotalChargeArray = toBePassedArgumentsArray[4];
    var invalidInstancesMonPolicyArray = toBePassedArgumentsArray[5];
	var operType = toBePassedArgumentsArray[2];
	paramArray = new Array();
	var operationType = operationMap.get(operType);
	if(validInstancesArray.length > 0 && operationType == 'Login'){
		hideFadeLoadingImg();
		if(multiOSNameSize > 1){
	 		return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xbe0g0ap',paramArray,'');
		}else{
			return selectLoginType();
		}
	}
	else if(validInstancesArray.length > 0 && operationType == 'Execute Script'){
		hideFadeLoadingImg();
		if(multiOSNameSize > 1){
	 		return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xae0i0ae',paramArray,'');
		}else{
			return showEcsPopup();
		}
	}
	else if(validInstancesArray.length > 0 && operationType == 'Update Instance Details'){
		hideFadeLoadingImg();
		if(document.getElementById('vmInstancesFormId')!=null){
		  show_hide_NickName('vmInstancesFormId');
		  cloudOnePopup('vmware_instance_details',500);
		}else{
		  show_hide_NickName('formId');
		  cloudOnePopup('instance_details', 500);
		}
		return;
	}else if(validInstancesArray.length > 0 && invalidInstancesArray.length > 0  && operationType == 'Delete'){
		var instancesForDeletion = new String();
		var invalidInstancesForDeletion = new String();
		invalidInstancesForDeletion =invalidInstancesForDeletion.concat('<div id="confirmMessage" style="float: left;padding-left: 16px;">This action Delete cannot be performed on the following instance(s):</div>');
		invalidInstancesForDeletion =invalidInstancesForDeletion.concat('<div id="instancesDiv" style="width:61.5%;padding-left: 16px;">');

			var message = new String();
			
			for(var i=0;i<invalidInstancesArray.length; i++){
				invalidInstancesForDeletion = invalidInstancesForDeletion.concat('<div style="padding-left:10px;">');	
				invalidInstancesForDeletion = invalidInstancesForDeletion.concat('<span class="bulletpoint"></span>'+blankSpace);
				invalidInstancesForDeletion = invalidInstancesForDeletion.concat(invalidInstancesArray[i]);
				invalidInstancesForDeletion = invalidInstancesForDeletion.concat('</div>');
	   		}
                        message= message.concat(invalidInstancesForDeletion);
			message = message.concat('<br>Continue with other instance(s)?<br></div>');
                        instancesForDeletion = instancesForDeletion.concat('<table class="popupContentLayout2">');
                        instancesForDeletion = instancesForDeletion.concat('<tr class="blankRowHeight"/>');
                        instancesForDeletion = instancesForDeletion.concat('<tr><td class="columnWidth"><span class="bold">Instance name</span></td>');
                        instancesForDeletion = instancesForDeletion.concat('<td class="columnWidth"><span class="bold">Total Powered-On time</span></td>');
                        instancesForDeletion = instancesForDeletion.concat('<td class="columnWidth"><span class="bold">Total Cost</span></td></tr>');
			for(var i=0;i<validInstancesArray.length; i++){
				instancesForDeletion = instancesForDeletion.concat('<tr><td class="columnWidth">');	
                                instancesForDeletion = instancesForDeletion.concat('<span class="bulletpoint"></span>'+blankSpace);
                                instancesForDeletion = instancesForDeletion.concat(validInstancesArray[i]);
                                instancesForDeletion = instancesForDeletion.concat('</td><td class="columnWidth">');
                                instancesForDeletion = instancesForDeletion.concat(validInstancesPowerOnTimeArray[i]);
                                instancesForDeletion = instancesForDeletion.concat('</td><td class="columnWidth">');
                                instancesForDeletion = instancesForDeletion.concat(validInstancesTotalChargeArray[i]);
                                instancesForDeletion = instancesForDeletion.concat('</td></tr>');
                        }
                        instancesForDeletion = instancesForDeletion.concat('<tr class="blankRowHeight"/>');
                        instancesForDeletion = instancesForDeletion.concat('<tr class="blankRowHeight"/>');
			instancesForDeletion = instancesForDeletion.concat('</table>');

			message= message.concat(instancesForDeletion);
	    		document.getElementById("messageContentForDelete").innerHTML =message.toString();
	    		document.getElementById("actionFormId:invokegetNotifications").onclick();
	    		return;
		}
	
	if(validInstancesArray.length > 0 && invalidInstancesArray.length > 0 && operationType != 'Delete'){
		hideFadeLoadingImg();
		var instances = new String();
		var invalidInstances = new String();
		var invalidMonPolicyInstances = new String();
		
		instances = instances.concat('<div style="height:5px;">'+blankSpace+'</div>');
		invalidInstances = invalidInstances.concat('<div style="height:5px;">'+blankSpace+'</div>');
		invalidMonPolicyInstances = invalidMonPolicyInstances.concat('<div style="height:5px;">'+blankSpace+'</div>');
		
		for(var i=0;i<validInstancesArray.length; i++){
			
			if(i == 0){
			   
			   instances = instances.concat('<div style="padding-left:10px;">');
			   instances = instances.concat('<span class="bulletpoint"></span>'+blankSpace);
			}else{
				
			   instances = instances.concat('<div style="padding-left:10px;">');	
			   instances = instances.concat('<span class="bulletpoint"></span>'+blankSpace);
			}
			
			instances = instances.concat(validInstancesArray[i]);
			instances = instances.concat('</div>');
		}
		
		for(var i=0;i<invalidInstancesArray.length; i++){
			
			if(i == 0){
				
   				invalidInstances = invalidInstances.concat('<div style="padding-left:10px;">');
   				invalidInstances = invalidInstances.concat('<span class="bulletpoint"></span>'+blankSpace);
   			}else{
   				
   				invalidInstances = invalidInstances.concat('<div style="padding-left:10px;">');	
   				invalidInstances = invalidInstances.concat('<span class="bulletpoint"></span>'+blankSpace);
   			}
			
   			invalidInstances = invalidInstances.concat(invalidInstancesArray[i]);
   			invalidInstances = invalidInstances.concat('</div>');
   		}
		for(var i=0;i<invalidInstancesMonPolicyArray.length; i++){
			
			if(i == 0){
				
				invalidMonPolicyInstances = invalidMonPolicyInstances.concat('<div style="padding-left:10px;">');
				invalidMonPolicyInstances = invalidMonPolicyInstances.concat('<span class="bulletpoint"></span>'+blankSpace);
   			}else{
   				
   				invalidMonPolicyInstances = invalidMonPolicyInstances.concat('<div style="padding-left:10px;">');	
   				invalidMonPolicyInstances = invalidMonPolicyInstances.concat('<span class="bulletpoint"></span>'+blankSpace);
   			}
			
			invalidMonPolicyInstances = invalidMonPolicyInstances.concat(invalidInstancesMonPolicyArray[i]);
			invalidMonPolicyInstances = invalidMonPolicyInstances.concat('</div>');
   		}
		
	    if(invalidInstancesArray.length > 0 && invalidInstancesMonPolicyArray.length > 0){
	    	paramArray[0] = invalidInstances.toString();
		    paramArray[2] = invalidMonPolicyInstances.toString();
		}else if(invalidInstancesArray.length > 0){
			paramArray[0] = invalidInstances.toString();
		}else if(invalidInstancesMonPolicyArray.length > 0){
			paramArray[0] = invalidMonPolicyInstances.toString();
		}
	    paramArray[1] = instances.toString();
	    
	    if(operationType == 'Edit Resources'){
	    	return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0aj',paramArray,'');
	    }else if(operationType == 'Edit Instance Storage'){
	    	return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0ah',paramArray,'');
	    }else if(operationType == 'Modify AWS Instance Type'){
	    	return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0aj',paramArray,'');
	    }else if(operationType == 'Modify AWS Instance Storage'){
	    	return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0ah',paramArray,'');
	    }else if(operationType == 'Update Package'){
	    	return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0ap',paramArray,'');
	    }else if(operationType == 'Clone to Image'){
	    	return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0i0pv',paramArray,'');
	    }else if(operationType == 'Clone Instance(s)'){
	    	return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xbe0g0ad',paramArray,'');	    
 	    }else if(operationType == 'Modify Monitoring Settings'){
 	    	paramArray[0] = 'Modify Monitoring Settings';
 			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0a0ae',paramArray,'');
 	    }else if(operationMap.get(operType) == 'Enable Monitoring'){		
 	    	displayInstallAgentEnableDisableMonitoringErrors(toBePassedArgumentsArray,'Enable Monitoring','0xcb0a0av');
 	    }else if(operationMap.get(operType) == 'Disable Monitoring'){		
 	    	displayInstallAgentEnableDisableMonitoringErrors(toBePassedArgumentsArray,'Disable Monitoring','0xcb0a0av');
 		}else if(operationMap.get(operType) == 'Remove Monitoring Profile'){
 	    	displayInstallAgentEnableDisableMonitoringErrors(toBePassedArgumentsArray,'Remove Monitoring Profile','0xcb0a0av');
 	    }else if(operationType == 'Modify Storage' &&  validInstancesArray.length > 0 && invalidInstancesArray.length > 0){
     			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0af',paramArray,'');
     	}else if(operationType == 'Modify Compute' &&  validInstancesArray.length > 0 && invalidInstancesArray.length > 0){
     			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xee0g0ae',paramArray,'');
     	}else if (operationType == 'Security Scan'){
     		return validateDataUsingMsgId(MessageTypeEnum.CONFIRMATION,'0xbe0g0aa',paramArray,'showSecurityScanPopup');
    	}else if(operationType == 'Update SNMP Details'){
 	    	paramArray[0] = 'Update SNMP Details';
 			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0a0ae',paramArray,'');
 	    }else if(operationType == 'Update WMI Details'){
 	    	paramArray[0] = 'Update WMI Details';
 			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0a0ae',paramArray,'');
 	    }else if(operationType == 'Update WMI Details'){
 	    	paramArray[0] = 'Update WMI Details';
 			return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0a0ae',paramArray,'');
 	    }else if(operType == 'installmonitoragent'){
    		displayInstallAgentEnableDisableMonitoringErrors(toBePassedArgumentsArray,'Install Agent','0xcb0a0av');	
		}
     	else{
        	if(operType == 'poweron' || operType == 'shutdown' || operType == 'restart' 
        		|| operType == 'poweroff' || operType == 'stop' || operType == 'reboot' || operType == 'start'){
        		if(invalidInstancesArray.length > 0 && invalidInstancesMonPolicyArray.length == 0){
        			return validateDataUsingMsgId(MessageTypeEnum.CONFIRMATION,'0xbe0g0aa',paramArray,'instanceActionPerform');
        		}else if(invalidInstancesMonPolicyArray.length > 0 && invalidInstancesArray.length == 0){
        			return validateDataUsingMsgId(MessageTypeEnum.CONFIRMATION,'0xbe0g0ax',paramArray,'instanceActionPerform');
        		}else if(invalidInstancesMonPolicyArray.length > 0 && invalidInstancesArray.length > 0){
        			return validateDataUsingMsgId(MessageTypeEnum.CONFIRMATION,'0xbe0g0aw',paramArray,'instanceActionPerform');
        		}
        	}else{
        		if(operType == 'apply_patch'){
        			validInstArray  = validInstancesArray;
        			validateDataUsingMsgId(MessageTypeEnum.CONFIRMATION,'0xbe0g0aa',paramArray,'showPatchApplyPopUp');
        		}else{
        			return validateDataUsingMsgId(MessageTypeEnum.CONFIRMATION,'0xbe0g0aa',paramArray,'instanceActionPerform');
        		}
        	}
	    }
	}
}
function showPatchApplyPopUp(){
	if(validInstArray.length > 1){
		jQuery(".patchApplyPreference").removeClass("hide");
	}	
	cloudOnePopup('instanceActionPopUp',650);
}
//Function called for invalid hypervisor selection
function validateForInvalidHypervisorSelections(toBePassedArgumentsArray){
	paramArray = new Array();
	return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0ak',paramArray,'');
}

function validateRefreshErrors(id){
	var paramArray = new Array();
	var errormsg = document.getElementById('statusSummaryFormId:'+id+'errorMsgId').value;
	var additionalMsg = document.getElementById('statusSummaryFormId:'+id+'additionalMsg').value;
	if(errormsg!=''){
		if(additionalMsg!=''){
			var additionalMsgArray = additionalMsg.split(',');
			if(additionalMsgArray.length > 0) {
				for (var i = 0; i < additionalMsgArray.length; i++) {
					paramArray[i] = additionalMsgArray[i];
				}
			} else {
				paramArray[0] = additionalMsgArray;
			}
		}
		validateDataUsingMsgId(MessageTypeEnum.ERROR,errormsg,paramArray,'');
		document.getElementById('statusSummaryFormId:errorMsgId').value='';
		document.getElementById("statusSummaryFormId:additionalMsg").value='';
	}
}

// Method to display connectivitiy errors based on formId and element type
function displayConnectionError(formId,output){
	var paramArray = new Array();
	var errormsg='';
	var provider='';
	if(output =='output'){
		if(document.getElementById(formId+':connectError').firstChild != null){
			errormsg = document.getElementById(formId+':connectError').firstChild.nodeValue;
		}else{
			if(document.getElementById(formId+':connectError')) {
				errormsg = document.getElementById(formId+':connectError').value;
			}
		}
		
		if(document.getElementById(formId+':providerValue').firstChild != null){
			provider = document.getElementById(formId+':providerValue').firstChild.nodeValue;
		}else{
			provider = document.getElementById(formId+':providerValue').value;
		}
		
		if(errormsg == undefined){
			errormsg='';
		}
	}else{
		if(document.getElementById(formId+':connectError')) {
			errormsg = document.getElementById(formId+':connectError').value;
		}
		if(document.getElementById(formId+':providerValue')) {
			provider = document.getElementById(formId+':providerValue').value;
		}
	}
	if(errormsg!=''){
		paramArray[0] = provider;
		validateDataUsingMsgId(MessageTypeEnum.ERROR,errormsg,paramArray,'');
		document.getElementById(formId+':connectError').value='';
		document.getElementById(formId+':providerValue').value='';
		if(output=='output'){
			document.getElementById(formId+':connectError').firstChild.nodeValue='';
			document.getElementById(formId+':providerValue').firstChild.nodeValue='';
		}
		return false;
	}
}

function enableAddDisk(){
	document.getElementById("actionFormId:addDiskId").style.visibility = 'hidden';
	document.getElementById("actionFormId:addDiskId").style.display= 'none';
	document.getElementById("actionFormId:addDiskId").style.height = '0px';
	document.getElementById("newDiskDivId").style.visibility = 'visible';
	document.getElementById("newDiskDivId").style.display= 'inline';
	document.getElementById("actionFormId:newVirtualDiskId:0").checked =true;
	document.getElementById("newVirtualDivId").style.visibility = 'visible';
	document.getElementById("newVirtualDivId").style.display= 'inline';
}

function rawDeviceMapping(){
	document.getElementById("rawDeviceMappingDivId").style.visibility = 'visible';
	document.getElementById("rawDeviceMappingDivId").style.display= 'inline';
	document.getElementById("newVirtualDivId").style.visibility = 'hidden';
	document.getElementById("newVirtualDivId").style.display= 'none';
	document.getElementById("actionFormId:newVirtualDiskId:0").checked =false;
	document.getElementById("actionFormId:newDiskId").value = '';
	document.getElementById("actionFormId:checkboxId").checked = false;
	document.getElementById("actionFormId:compatibilityId:0").checked=true;
	document.getElementById("actionFormId:compatibilityId:1").checked=false;
}

function newVirtualDisk(){
	document.getElementById("newVirtualDivId").style.visibility = 'visible';
	document.getElementById("newVirtualDivId").style.display= 'inline';
	document.getElementById("rawDeviceMappingDivId").style.visibility = 'hidden';
	document.getElementById("rawDeviceMappingDivId").style.display= 'none';
	document.getElementById("actionFormId:rawDeviceMappingId:0").checked =false;
	document.getElementById("actionFormId:newTargetLUNId").value='-999';
	document.getElementById("actionFormId:compatibilityId:0").checked=false;
	document.getElementById("actionFormId:compatibilityId:1").checked=false;
}

function cleanupDataInEditStorage(){
	document.getElementById("actionFormId:newDiskId").value = '';
	document.getElementById("actionFormId:checkboxId").checked = false;
	document.getElementById("actionFormId:newTargetLUNId").value='-999';
	document.getElementById("actionFormId:compatibilityId:0").checked=false;
	document.getElementById("actionFormId:compatibilityId:1").checked=false;
	document.getElementById("actionFormId:rawDeviceMappingId:0").checked =false;
	document.getElementById("actionFormId:newVirtualDiskId:0").checked =false;
	jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:costUpdated")).val('');
}

function clearCustomScript(){
	jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:scriptLocId")).val('');
	jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:descriptionId")).val('');
	jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:ecsLoginUsername")).val('');
	jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:ecsPwdTxt")).val('');
}

function onloadSavvisModifyStorageCallback() {
	jQuery(".bootDriveDisabled").attr("disabled", true);
	jQuery(".bootDiskSizeDisabled").attr("disabled", true);
	jQuery(".dataDriveDisabled").attr("disabled", true);
}

function displaySavvisAOIMsgs(error) {
	if(error != null && error != '') {
		var paramArray = new Array();
		if(error.indexOf("0x") == 0) {
			validateDataUsingMsgId(MessageTypeEnum.ERROR,error,paramArray,'');
		} else {
			showPopUpUsingMsgId(MessageTypeEnum.ERROR, error, paramArray, '');
		}
	}
}

function validateEditInstance(){
	var paramArray = new Array();
	var memorySize=0;
	if(document.getElementById('actionFormId:memorySizeId') != null){
		memorySize = document.getElementById('actionFormId:memorySizeId').value;
	}
	if(isNaN(memorySize)){
		validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0i0pj',paramArray,'');
		return false;
	} else {
		return true;
	}
}

function validateCloneSavvisInstance(){
	closeAllPopUps();
	document.getElementById('actionFormId:actionCloneSavvisVM').onclick();
}

function validateModifiedDiskSize(obj) {
	if(obj != null) {
		var objId = obj.id;
		var objVal = obj.value;
		var originalObj = document.getElementById(objId+'_hidden');
		if(originalObj != null) {
			if(parseInt(originalObj.value) > parseInt(objVal)) {
				obj.value = originalObj.value;
				var paramArray = new Array();
				validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0dk',paramArray,'');
				return false;
			}
		}
	}
}

function checkDuplicateSavvisDiskNames() {
	LoadingButtonUtils.changeToLoading(jQuery("#"	+ JSUtils.prototype.escapeColon("create_instance_vmware_save_continue_command_button_label")));
	var osValue= jQuery("#"+ JSUtils.prototype
			.escapeColon("vmWareInstanceFormId:osTypeId")).val();
	var imageType = jQuery("#imageType").val();
	if(osValue != "" && imageType != 'Custom Images') {
		var driveNames = new Array();
		var ctr = 0;
		var duplicateFound = false;
		jQuery(".driveValue").each(function(){
			var currVal = jQuery(this).val();
			currVal = jQuery.trim(currVal);
			if(currVal !=""){
				if(jQuery.inArray(currVal, driveNames) > -1) {
					duplicateFound = true;
					var paramArray = new Array();
					validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0dl',paramArray,'');
					return;
				}
			}
			driveNames[ctr] = currVal;
			ctr++;
		});
		if(duplicateFound) {
			return false;
		} else {
			getItemsToValidateinDiv('main');
			return true;
		}
	} else {
		getItemsToValidateinDiv('main');
		return true;
	}
}

function triggerSavvisStorage() {
	var driveNames = new Array();
	var ctr = 0;
	var duplicateFound = false;
	jQuery(".driveValue").each(function(){
		var currVal = jQuery(this).val();
		currVal = jQuery.trim(currVal);
		if(currVal !=""){
			if(jQuery.inArray(currVal, driveNames) > -1) {
				duplicateFound = true;
				var paramArray = new Array();
				validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0dl',paramArray,'');
				return;
			}
		}
		driveNames[ctr] = currVal;
		ctr++;
	});
	if(!duplicateFound) {
		var paramArray = new Array();
		showAOIResponseMsg('true','0xee0i0ac');
		closeAllPopUps();	
		document.getElementById('actionFormId:actionOnEditInstStorageId').onclick();		
	}
}

function validateSavvisEditStorage() {
	var diskCount = document.getElementById('actionFormId:diskCount').innerHTML;
	var diskString = "";
	var paramArray = new Array();
	var dataMissing = false;
	var missedDataString = "";
	for(var i=2;i<diskCount;i++) {	
		var diskObj = document.getElementById("actionFormId:dataDrive"+i+"_enabled");
		if(diskObj) {
			diskObj = diskObj.value;
		}
		if(diskObj != null && diskObj!=""){
			if(!savvisDiskValidation(diskObj)) {
				diskString = diskString+(i+1)+",";
			}
		}else{
			missedDataString=missedDataString+(i+1)+", ";
			dataMissing = true;
		}
	}
	if(dataMissing){
		missedDataString = "Disk (data) Drive "+(missedDataString.slice(0,-2));
		paramArray[0]=missedDataString;
		return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0bpb',paramArray,'');
	}
	if(diskString != null && diskString != "") {
		paramArray[0]=diskString.slice(0,-1);
		validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0mt',paramArray,'');
	} else {
		triggerSavvisStorage();
	}
}

function savvisDiskValidation(diskNam) {
	var regex = /^[0-9A-Za-z\\\/:\-_]+$/;
	var isproper = true;
	if (!regex.test(diskNam)) {
		diskNam = "";
		isproper =  false;
	}
	return isproper;
}

function removeSavvisDisk(removeIndex, diskNameId){
	var paramArray = new Array();
	paramArray[0] = document.getElementById(diskNameId).value;
	document.getElementById('actionFormId:diskRemoveIndexId').value = removeIndex;
	validateDataUsingMsgId(MessageTypeEnum.CONFIRMATION,'0xcb0g0xz',paramArray,'proceedForSavvisDiskRemoval');
}

function proceedForSavvisDiskRemoval() {
	document.getElementById('actionFormId:hiddenRemoveSavvisDisk').onclick();
}

function validateEditInstanceStorage(diskList){	
	var storageModified = document.getElementById('actionFormId:costUpdated').value;
	var newDiskSize= jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:newDiskId")).val();
	var targetLun = document.getElementById("actionFormId:newTargetLUNId").value;
	var modified=false;
	for(var i=0;i<diskList;i++) {
		var diskSize= jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:diskSize"+i)).val();	
		var previousdiskSize = jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:hiddenDiskSizeId"+i)).val();
		if(document.getElementById("actionFormId:diskSize"+i) && (diskSize == null || diskSize == '')) {
			var paramArray = new Array();
			validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0i0qg',paramArray,'');
			return false;
		}
		else if(document.getElementById("actionFormId:diskSize"+i) && (parseInt(diskSize) < 1 || parseInt(diskSize) < parseInt(previousdiskSize))) {
			var paramArray = new Array();
			validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0dj',paramArray,'');
			return false;
		}	
		if(document.getElementById("actionFormId:detachLabel"+i) || parseInt(diskSize) > parseInt(previousdiskSize)){			
            modified=true;
        }	
	}
	if(document.getElementById("actionFormId:newVirtualDiskId:0").checked == true || document.getElementById("actionFormId:rawDeviceMappingId:0").checked == true){
		modified=true;
	}
	if(document.getElementById("actionFormId:newVirtualDiskId:0").checked == true && (newDiskSize < 1 || newDiskSize == '')){
		var paramArray = new Array();
		validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0ds',paramArray,'');
		return false;
	}
	if(document.getElementById("actionFormId:rawDeviceMappingId:0").checked == true && targetLun=='-999'){
	  var paramArray = new Array();
		validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0dq',paramArray,'');
		return false;
	}

	if(modified==false) {
		var paramArray = new Array();
		validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0de',paramArray,'');
		return false;
	}	
	var paramArray = new Array();
    showAOIResponseMsg('true','0xee0i0ac');
	closeAllPopUps();	
	document.getElementById('actionFormId:actionOnEditInstStorageId').onclick();	
}

function showAOIResponseMsg(showInline, errCode) {
	var paramArray = new Array();
	if(showInline != null && showInline == 'true') {
		showResponseInline(errCode, paramArray);		
		return false;
	} else {
		showPopUpUsingMsgId(MessageTypeEnum.ERROR, errCode, paramArray, '');
		return false;
	}
}

function createImage(){
	if (!validateElements('instanceActionPopUp')) {
		return false;
	}else{
		closeAllPopUps();
		document.getElementById('actionFormId:actionOnInstId').onclick();
		setTimeout("showAOIResponseMsg('true','0xee0i0ac')",4000);
	}
}
function showScheduledMessage(isScheduled, operation){
	if(operation == "CONVERTTOIMAGE" || operation == "AZURECLONETOIMAGE" || operation == "CREATE_IMAGE_TEMPLATE" || operation == "CREATE_FLEX_IMAGE"){
		if(isScheduled == "true"){
			var paramArray = new Array();
			showPopUpUsingMsgId(MessageTypeEnum.INFORMATION,'0xae0i0ah',paramArray,'');
		}else{
			triggerAction();
		}
	}
}
function roundOffMemoryInMB(){
	var memorySize = document.getElementById('actionFormId:memorySizeId').value;
	var changedMemory = memorySize % 4;
	if(changedMemory < 2 && memorySize > 1)
		document.getElementById('actionFormId:memorySizeId').value = parseInt(memorySize - changedMemory);
	else
		document.getElementById('actionFormId:memorySizeId').value = parseInt(memorySize - changedMemory + 4);
}

function setMaxCapacityValue(maxCapValue){
	document.getElementById("vmWareDSFormId:datastoreCapacityTxtId").value = maxCapValue;
	document.getElementById("vmWareDSFormId:datastoreCapacityTxtId").disabled= false;
	document.getElementById("vmWareDSFormId:maxCapacityHiddTxtId").value = maxCapValue;
	document.getElementById("vmWareDSFormId:datastoreCapacityCheckboxId").checked = true
}

function displayMsgForCreateDS(errCode, dsName) {
	var paramArray = new Array();
	if (errCode == '0xcb0i0co') {
		paramArray[0] = dsName;
		validateDataUsingMsgId(MessageTypeEnum.ERROR, errCode, paramArray,'');
	} else if(errCode == '0xee0i0ab' || errCode == '') {
		displaySuccessMsgForCreateDS('0xee0i0ab');
	} 
}
function displayMsgForCreateImage(errCode) {
	var paramArray = new Array();
	if (errCode == '0xcb0i0ck') {
		paramArray[0] = "Image";
		validateDataUsingMsgId(MessageTypeEnum.ERROR, errCode, paramArray,'');
	} else if(errCode == '0xee0i0ao' || errCode == '') {
		displaySuccessMsgForCreateIMG('0xee0i0ao');
	} 
}

function displaySuccessMsgForCreateIMG(msg){
	var paramArray = new Array();
	showResponseInline(msg,paramArray,"goToListOfImages");
	return true;
}
function displaySuccessMsgForCreateDS(msg){
	var paramArray = new Array();
	showResponseInline(msg,paramArray,"goToListOfDatastores");
	return true;
}

function goToListOfDatastores() {
	document.getElementById('vmWareCreateDSFormId:navigListDatastorePageId').onclick();
}
function goToListOfImages() {
	document.getElementById('addImagesFormId:navigListimagesPageId').onclick();
}

function enableAddAWSDisk(){
    document.getElementById("actionFormId:addVolumesId").style.visibility = 'hidden';
    document.getElementById("actionFormId:addVolumesId").style.display= 'none';
    document.getElementById("actionFormId:addVolumesId").style.height = '0px';
    document.getElementById("newVolumeDivId").style.display= 'inline';
    document.getElementById("actionFormId:newVolumeId:0").checked = true;
    newVolumeAWSDisk();
}

function enableAddAzureDisk(AzureInstanceType){
	
	if(AzureInstanceType=='V2')
		{
		document.getElementById("actionFormId:existingStorageId").style.visibility = 'hidden';
		document.getElementById("actionFormId:existingStorageId").style.display= 'none';
	    document.getElementById("actionFormId:existingStorageId").style.height = '0px';
		document.getElementById("DiskDetailsForV2").style.visibility = 'visible';
		document.getElementById("DiskDetailsForV2").style.display= 'inline';
		}
	else{
		document.getElementById("actionFormId:existingStorageId").style.visibility = 'visible';
		document.getElementById("actionFormId:existingStorageId").style.display= 'inline';
		document.getElementById("DiskDetailsForV2").style.visibility = 'hidden';
		document.getElementById("DiskDetailsForV2").style.display= 'none';
		document.getElementById("DiskDetailsForV2").style.height = '0px';
		   
	}
	document.getElementById("actionFormId:addStorageId").style.visibility = 'hidden';
    document.getElementById("actionFormId:addStorageId").style.display= 'none';
    document.getElementById("actionFormId:addStorageId").style.height = '0px';
	document.getElementById("newStorageDiskDivId").style.display= 'inline';
	document.getElementById("actionFormId:newStorageId:0").checked = true;
	document.getElementById("actionFormId:existingStorageId:0").checked = false;
	document.getElementById("addnewDiskDivId").style.visibility = 'visible';
	document.getElementById("addnewDiskDivId").style.display= 'inline';
	document.getElementById("actionFormId:hostCacheId:0").checked = true;
}


function existingAWSVolMapping(){
    document.getElementById("existingVolumeDivId").style.visibility = 'visible';
    document.getElementById("existingVolumeDivId").style.display= 'inline';
    document.getElementById("addVolumeDivId").style.visibility = 'hidden';
    document.getElementById("addVolumeDivId").style.display= 'none';
    document.getElementById("actionFormId:newVolumeId:0").checked =false;
    document.getElementById("actionFormId:snapshotId").value='-999';
	document.getElementById("actionFormId:deviceType").setAttribute("mandatory",false);  
    document.getElementById("actionFormId:newVolumeSize").setAttribute("mandatory",false);
    document.getElementById("actionFormId:exisDeviceType").setAttribute("mandatory",true);  
	document.getElementById('actionFormId:exisDeviceType').setAttribute("validationType","required");
}

function newVolumeAWSDisk(){
	var volSize = 1;
    document.getElementById("addVolumeDivId").style.visibility = 'visible';
    document.getElementById("addVolumeDivId").style.display= 'inline';
    document.getElementById("existingVolumeDivId").style.visibility = 'hidden';
    document.getElementById("existingVolumeDivId").style.display= 'none';
    document.getElementById("actionFormId:existingVolId:0").checked =false;
    document.getElementById("actionFormId:newTargetVolId").value='-999';
    document.getElementById("actionFormId:newVolumeSize").value = parseInt(volSize);
    document.getElementById("actionFormId:deviceType").setAttribute("mandatory",true);	
	document.getElementById('actionFormId:deviceType').setAttribute("eventType","blur");
	document.getElementById('actionFormId:deviceType').setAttribute("associatedFieldName","Device");
	document.getElementById('actionFormId:deviceType').setAttribute("validationType","required");
	document.getElementById("actionFormId:exisDeviceType").setAttribute("mandatory",false);  
	document.getElementById('actionFormId:exisDeviceType').setAttribute("validationType","optional");
    document.getElementById("actionFormId:newVolumeSize").setAttribute("mandatory",true);
	document.getElementById('actionFormId:newVolumeSize').setAttribute("eventType","blur");
	document.getElementById('actionFormId:newVolumeSize').setAttribute("associatedFieldName","Size");
	document.getElementById('actionFormId:newVolumeSize').setAttribute("validationType","required");
}

function newStorageDiskAzure(){
    document.getElementById("addnewDiskDivId").style.visibility = 'visible';
    document.getElementById("addnewDiskDivId").style.display= 'inline';
	document.getElementById("actionFormId:hostCacheId:0").checked = true;
	document.getElementById("existingDiskDivId").style.visibility = 'hidden';
    document.getElementById("existingDiskDivId").style.display= 'none'; 
	document.getElementById("actionFormId:existingVolId:0").checked =false;
	document.getElementById("actionFormId:existingStorageId:0").checked =false;
}

function existingStorageDiskAzure(){
    document.getElementById("existingDiskDivId").style.visibility = 'visible';
	document.getElementById("existingDiskDivId").style.display = 'inline';
    document.getElementById("addnewDiskDivId").style.visibility = 'hidden';
    document.getElementById("addnewDiskDivId").style.visibility = 'hidden';
    document.getElementById("addnewDiskDivId").style.display= 'none';
    document.getElementById("actionFormId:hostCacheId1:0").checked = true;
	document.getElementById("actionFormId:newStorageId:0").checked =false;
}

function validateEditAWSInstanceStorage(volumeList){	
	for(var i=0;i<volumeList;i++) {
		var volSize= jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:ebsVolSize"+i)).val();		
		if(volSize == null || volSize == '') {
			var paramArray = new Array();
			validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0i0qg',paramArray,'');
			return false;
		}
		else if(volSize < 1) {
			var paramArray = new Array();
			validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0g0dj',paramArray,'');
			return false;
		}	
	}
	getItemsToValidateinDiv("aws_edit_instance_storage");
	if(!validateElements('aws_edit_instance_storage')){
		return false;
	}else if(validateModifyExistingStorage()){
		if(validateEbsVolumeSize()){
			var paramArray = new Array();
			showAOIResponseMsg('true','0xee0i0ac');
			closeAllPopUps();
			document.getElementById('actionFormId:actionOnUpdateAWSStorage').onclick();
		}
	}
}

function validateEditAzureInstanceStorage(){

	var newDiskSize= jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:newDiskSize")).val();	
	getVisibleItemsToValidateinDiv("azure_edit_instance_storage");
	if(!validateElements('azure_edit_instance_storage')){
		return false;
	}
	if (document.getElementById('actionFormId:newStorageId:0').checked == true) {
		document.getElementById('actionFormId:existingStorageId:0').checked = false;
		if(newDiskSize <=0){
			var paramArray = new Array();
			return validateDataUsingMsgId(MessageTypeEnum.ERROR, "0xcb0g0dn", paramArray,'');
		}	
		if(newDiskSize >1000){
			var paramArray = new Array();
			return validateDataUsingMsgId(MessageTypeEnum.ERROR, "0xcb0g0el", paramArray,'');
	}
}
	var storageAddVisibility = jQuery(".addStorageId").css("visibility");
	var notificationChanged = jQuery(".notificationChangedId").val();
	if(storageAddVisibility == "hidden" || notificationChanged == "changed"){
		showAOIResponseMsg('true','0xee0i0ac');
		closeAllPopUps();
		document.getElementById('actionFormId:actionOnUpdateAzureStorage').onclick();
	}else{
		closeAllPopUps();
	}
}
		
function validateEBSSize() {
    var size = parseInt(document.getElementById('actionFormId:size').value);
    var volume = document.getElementById('actionFormId:esbVolumeComboId').value;
    if(size > 1024 && volume=="GB")
            return false;
    else if (size > 1 && volume=="TB")	
            return false;
    else if (size == 0)
            return false;
    else
            return true;
}
function validateEbsVolumeSize(){
	var volumeSize = jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:newVolumeSize")).val();
	if(volumeSize <= 0){
		var paramArray = new Array();
		return validateDataUsingMsgId(MessageTypeEnum.ERROR, "0xcb0g0dn", paramArray,'');
	}
	return true;
}

function validateResourcePopup(input){
	if(validateEbsVolumeSize()){
		validateNumeric(input);
	}
}


function loadAWSStorageDtls() {
	clearAWSStorageDetails();
	cloudOnePopup('aws_edit_instance_storage', 500);
}

function loadAZUREStorageDtls() {
	cloudOnePopup('azure_edit_instance_storage', 500);
}

function clearAWSStorageDetails(){
	var volSize = 1;
	document.getElementById("actionFormId:snapshotId").value='-999';
	document.getElementById("actionFormId:newTargetVolId").value='-999';
	document.getElementById("actionFormId:newVolumeSize").value = parseInt(volSize);
	document.getElementById("actionFormId:existingVolId:0").checked =false;
	document.getElementById("actionFormId:newVolumeId:0").checked =false;
}

function displayDeviceLoading(typ){
	if(typ == 'NEW'){
		document.getElementById('actionFormId:wait').style.display='block';
	} else {
		document.getElementById('actionFormId:waitExisting').style.display='block';
	}
}

function validateAWSDevice(errCode){
	var paramArray = new Array();
	document.getElementById('actionFormId:wait').style.display='none';
	document.getElementById('actionFormId:waitExisting').style.display='none';
	if(errCode != '') {
		validateDataUsingMsgId(MessageTypeEnum.ERROR,errCode,paramArray,'');
		if(document.getElementById('actionFormId:deviceType')) {
			document.getElementById('actionFormId:deviceType').value='';
		}
		if(document.getElementById('actionFormId:exisDeviceType')) {
			document.getElementById('actionFormId:deviceType').value='';
		}
	}	
}

function showElement(elementId){
        document.getElementById(elementId).style.visibility = 'visible';
}

function hideElement(elementId){
        document.getElementById(elementId).style.visibility = 'hidden';
}

//Validate before submitting modify storage request
function validateModifyExistingStorage(){
	if( document.getElementById("actionFormId:existingVolId:0").checked == true){
    	 	var actionOnStorageSave = new Hashtable();
    	 	actionOnStorageSave.put("actionFormId:newTargetVolId", "Volume,required");
   	  	return validateFormData(MessageTypeEnum.ERROR,actionOnStorageSave);
    }
    return true;
}

function modifyMonitoringSettings(){
	document.getElementById("actionFormId:modifymonitoringId").onclick();
	var paramArray = new Array();
	closeAllPopUps();
}

function modifyMonComplete(monProfileModifiedFlag,failedInst,failedSource,agentNotRunningInstances,agentNotInstalledInstances){
	if(monProfileModifiedFlag != null && monProfileModifiedFlag != 'false'){
	paramArray = new Array();
	if(monProfileModifiedFlag == '0'){
		validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xcb0a0bk',paramArray,'');
	}else{
		var errorCodeHidden = jQuery("#"+ JSUtils.prototype.escapeColon("errorCodeHidden")).val();
		if(errorCodeHidden == '0xcb0a0an' || errorCodeHidden == '0xcb0i0qz' || errorCodeHidden == '0xcb0a0ay'  ||errorCodeHidden == '0xcb0i0rk'
			|| errorCodeHidden == '0xcb0a0az' || errorCodeHidden == '0xcb0i0yc' || errorCodeHidden == '0xcb0i0yb' || errorCodeHidden == '0xcb0i0yk'){
			var blankSpace = '  ';
			var sameMpApplied = new String();
			if(errorCodeHidden == '0xcb0a0az'){
				var inValid = new String();
				var str = new String();
				if(agentNotRunningInstances != '[]'){
				inValid = inValid.concat("Agent Installed but not Running on Instance: ");	
				 str = agentNotRunningInstances != '[]' ? ((((agentNotRunningInstances.split('['))[1]).split(']'))[0]).split(',') : new Array();
				}
				if(agentNotInstalledInstances != '[]'){
					str = agentNotInstalledInstances != '[]' ? ((((agentNotInstalledInstances.split('['))[1]).split(']'))[0]).split(',') : new Array();
				inValid = inValid.concat("Agent not Installed on Instance:");		
				}
				inValid = inValid.concat('<div style="height:5px;">'+blankSpace+'</div>');
				inValid = inValid.concat('<div style="padding-left:10px;">');	
				inValid = inValid.concat('<span class="bulletpoint"></span>'+blankSpace);
				inValid = inValid.concat(str);
				inValid = inValid.concat('</div>');
				paramArray[0] = 'Apply Profile';
				paramArray[1] = inValid.toString();;
			}else if(errorCodeHidden == '0xcb0i0rk' || errorCodeHidden == '0xcb0i0qz'){
				paramArray[0] = failedSource;
				paramArray[1] = failedInst;
			}
			else{
				sameMpApplied = sameMpApplied.concat('<div style="height:5px;">'+blankSpace+'</div>');
				sameMpApplied = sameMpApplied.concat('<div style="padding-left:10px;">');
				sameMpApplied = sameMpApplied.concat('<span class="bulletpoint"></span>'+blankSpace);
				sameMpApplied = sameMpApplied.concat(monProfileModifiedFlag);
				sameMpApplied = sameMpApplied.concat('</div>');
				paramArray[0] = sameMpApplied.toString();
			}
				validateDataUsingMsgId(MessageTypeEnum.ERROR,errorCodeHidden,paramArray,'');	
		}			
	}
}
else{
	showAOIResponseMsg('true', '0xee0i0ac');
}
}

function continueCreateInstance(){
	LoadingButtonUtils.changeToLoading(jQuery("#"	+ JSUtils.prototype.escapeColon("create_instance_save_continue_command_button_text")));
	getItemsToValidateinDiv("main");
	if(!validateElements('main')){
		return false;
	}else{
		var selImageType = document.getElementById('createInstanceFormId:selectListAmiImageId');
		if(selImageType && selImageType.value=='AWS Marketplace'){
			paramArray = new Array();
			validateDataUsingMsgId(MessageTypeEnum.CONFIRMATION,'0xcb0g0ec',paramArray,'confirmAWSMarketSubscription');
		}else{
			document.getElementById("createInstanceFormId:continueId").onclick();
			return true;
		}
	}
}

function confirmAWSMarketSubscription(){
	   document.getElementById("createInstanceFormId:continueId").onclick();
	   return true;
}

function validateInstancePrefix(provider) {	
	var id="createInstanceFormId:namePrefix";
	if(provider=='Vmware') {
		id="vmWareInstanceFormId:namePrefix";
	} else if (provider=='AWS') {
		id="createInstanceFormId:namePrefix";
	} else if (provider=='Savvis') {
		id="vmWareInstanceFormId:namePrefix";
	}else if (provider=='AIX') {
		id="vmWareInstanceFormId:namePrefix";
	} else {
		id="azureCreateInstanceFormId:namePrefix";
	}	
	if(document.getElementById(id) != null){		
		var componentsHashMap = new Hashtable();
		componentsHashMap.put(id, "Name Prefix,required,alphanumericwithspaceunderscore");
		if(!validateFormData(MessageTypeEnum.ERROR,componentsHashMap)){			
			return false;
		}	    				
	}	
	return true;
}

function loadManagePrivateIPAdderssPopup(ipAddressLength) {
	if(ipAddressLength > 0){
		return cloudOnePopup('managePrivateIPAddressPopup',600);
	}else {
		var paramArray = new Array();
		return validateDataUsingMsgId(MessageTypeEnum.ERROR, '0xcb0g0pj', paramArray,'');
	}  
}

function validateErrorMessagesInManagePrivateIP(errorCode,secondaryIPCount,invalidIP) {
	if(errorCode == '0xee0i0ep') {
		var paramArray = new Array();
		paramArray[0]=secondaryIPCount;
		return validateDataUsingMsgId(MessageTypeEnum.ERROR, errorCode, paramArray,'');
	} else if(errorCode == '0xee0i0eq') {
		var paramArray = new Array();
		return validateDataUsingMsgId(MessageTypeEnum.ERROR, errorCode, paramArray,'');
	} else if(errorCode == '0xee0i0fb') {
		var paramArray = new Array();
		paramArray[0]=invalidIP;
		return validateDataUsingMsgId(MessageTypeEnum.ERROR, errorCode, paramArray,'');
	} else if(errorCode == '0xee0i0fg') {
		var paramArray = new Array();
		paramArray[0]=invalidIP;
		return validateDataUsingMsgId(MessageTypeEnum.ERROR, errorCode, paramArray,'');
	}
	closeAllPopUps();
	return true;
}

//This method is a workaround to fix notifications re-rendering issue seen in the delete instance popup
function setSelectionValues(notificationId) {
	if(document.getElementById('actionFormId:notificationId') != null) {
		document.getElementById('actionFormId:notificationId').value = notificationId;
	}
}

function validateEBSOptInstance(){
	var instanceType = document.getElementById('createInstanceFormId:instanceTypeId').value;
	if(instanceType == "m1.large" || instanceType == "m1.xlarge" || instanceType =="m2.4xlarge"){
		jQuery("#ebsOptTRId").removeClass("hide");
		jQuery("#ebsOptTRHideClassId").removeClass("hide");
		jQuery("#ebsOptTRHideClassId").addClass("blankRowHeight");
	}else{
		jQuery("#ebsOptTRId").addClass("hide");
		jQuery("#ebsOptTRHideClassId").addClass("hide");
		jQuery("#ebsOptTRHideClassId").removeClass("blankRowHeight");
	}
}

function validateSelectedPackage(errCode){
	var paramArray = new Array();
	if (errCode != null && errCode != '') 
	{
		validateDataUsingMsgId(MessageTypeEnum.ERROR, errCode, paramArray, '');
	}
}

function checkModifyMonSettingsError(paramArray){
   var errorCodeHidden = jQuery("#"+ JSUtils.prototype.escapeColon("errorCodeHidden")).val();
   if(errorCodeHidden == '0xcb0a0ao' || errorCodeHidden == '0xcb0a0aq' || errorCodeHidden == '0xcb0a0ax' || errorCodeHidden == '0xcb0a0ay' ||errorCodeHidden =='0xcb0i0qz' || errorCodeHidden == '0xee0i0fr'){
	   validateDataUsingMsgId(MessageTypeEnum.ERROR,errorCodeHidden,paramArray,'');
	   return false;
   }
}

function displayInstallAgentEnableDisableMonitoringErrors(toBePassedArgumentsArray,operType,errCode){
	    var blankSpace = '  ';
		var valid= new String();
		var validInstance = toBePassedArgumentsArray[0];
		var notPoweredOn = toBePassedArgumentsArray[6];
		var ipAddressNotSet = toBePassedArgumentsArray[7];
		var agentNotInstalled = toBePassedArgumentsArray[8];
		var agentNotRunning = toBePassedArgumentsArray[9];
		var mpAlreadyEnabledOrDisabled = toBePassedArgumentsArray[10];
		var mpNotApplied = toBePassedArgumentsArray[11];
		var loginDetailsNotUpdated = toBePassedArgumentsArray[12];
		var paramArray = new Array();
		var inValid = new String();
		inValid = inValid.concat('<div style="height:5px;">'+blankSpace+'</div>');

		if(notPoweredOn.length > 0){
			inValid = inValid.concat("Instance(s) not Powered On:");
			inValid = inValid.concat('<div style="height:5px;">'+blankSpace+'</div>');
			for(var i=0;i<notPoweredOn.length; i++){				
				inValid = inValid.concat('<div style="padding-left:10px;">');	
				inValid = inValid.concat('<span class="bulletpoint"></span>'+blankSpace);
				inValid = inValid.concat(notPoweredOn[i]);
				inValid = inValid.concat('</div>');
			}
		}
		if(ipAddressNotSet.length > 0){
			inValid = inValid.concat("IP Address not set for Instance(s):");
			inValid = inValid.concat('<div style="height:5px;">'+blankSpace+'</div>');
			for(var i=0;i<ipAddressNotSet.length; i++){				
				inValid = inValid.concat('<div style="padding-left:10px;">');	
				inValid = inValid.concat('<span class="bulletpoint"></span>'+blankSpace);
				inValid = inValid.concat(ipAddressNotSet[i]);
				inValid = inValid.concat('</div>');
			}
		}
		if(operType == 'Install Agent' && loginDetailsNotUpdated.length > 0){
			inValid = inValid.concat("Login Details is not updated for Instance(s):");
			inValid = inValid.concat('<div style="height:5px;">'+blankSpace+'</div>');
			for(var i=0;i<loginDetailsNotUpdated.length; i++){				
				inValid = inValid.concat('<div style="padding-left:10px;">');	
				inValid = inValid.concat('<span class="bulletpoint"></span>'+blankSpace);
				inValid = inValid.concat(loginDetailsNotUpdated[i]);
				inValid = inValid.concat('</div>');
			}	
		}
		if(agentNotInstalled.length > 0){
			inValid = inValid.concat("Agent not Installed on Instance(s):");
			inValid = inValid.concat('<div style="height:5px;">'+blankSpace+'</div>');
			for(var i=0;i<agentNotInstalled.length; i++){				
				inValid = inValid.concat('<div style="padding-left:10px;">');	
				inValid = inValid.concat('<span class="bulletpoint"></span>'+blankSpace);
				inValid = inValid.concat(agentNotInstalled[i]);
				inValid = inValid.concat('</div>');
			}
		}
		if(agentNotRunning.length > 0){
			inValid = inValid.concat("Agent Installed but not Running on Instance(s):");
			inValid = inValid.concat('<div style="height:5px;">'+blankSpace+'</div>');
			for(var i=0;i<agentNotRunning.length; i++){				
				inValid = inValid.concat('<div style="padding-left:10px;">');	
				inValid = inValid.concat('<span class="bulletpoint"></span>'+blankSpace);
				inValid = inValid.concat(agentNotRunning[i]);
				inValid = inValid.concat('</div>');
			}
		}
		if(mpAlreadyEnabledOrDisabled.length > 0 && operType == 'Enable Monitoring'){
			inValid = inValid.concat("Monitoring is already enabled on Instance(s):");
			inValid = inValid.concat('<div style="height:5px;">'+blankSpace+'</div>');
			for(var i=0;i<mpAlreadyEnabledOrDisabled.length; i++){				
				inValid = inValid.concat('<div style="padding-left:10px;">');	
				inValid = inValid.concat('<span class="bulletpoint"></span>'+blankSpace);
				inValid = inValid.concat(mpAlreadyEnabledOrDisabled[i]);
				inValid = inValid.concat('</div>');
			}
		}
		if(mpAlreadyEnabledOrDisabled.length > 0 && operType == 'Disable Monitoring'){
			inValid = inValid.concat("Monitoring is already disabled on Instance(s):");
			inValid = inValid.concat('<div style="height:5px;">'+blankSpace+'</div>');
			for(var i=0;i<mpAlreadyEnabledOrDisabled.length; i++){				
				inValid = inValid.concat('<div style="padding-left:10px;">');	
				inValid = inValid.concat('<span class="bulletpoint"></span>'+blankSpace);
				inValid = inValid.concat(mpAlreadyEnabledOrDisabled[i]);
				inValid = inValid.concat('</div>');
			}
		}
		if(mpNotApplied.length > 0){
			inValid = inValid.concat("Monitoring Profile is not applied on Instance(s):");
			inValid = inValid.concat('<div style="height:5px;">'+blankSpace+'</div>');
			for(var i=0;i<mpNotApplied.length; i++){				
				inValid = inValid.concat('<div style="padding-left:10px;">');	
				inValid = inValid.concat('<span class="bulletpoint"></span>'+blankSpace);
				inValid = inValid.concat(mpNotApplied[i]);
				inValid = inValid.concat('</div>');
			}
		}
		paramArray[0] = operType;
		paramArray[1] = inValid.toString();
		if(errCode == '0xcb0a0av' && validInstance.length >0){
			valid = valid.concat('<div style="height:5px;">'+blankSpace+'</div>');
			for(var i=0;i<validInstance.length; i++){				
				valid = valid.concat('<div style="padding-left:20px;">');	
				valid = valid.concat('<span class="bulletpoint"></span>'+blankSpace);
				valid = valid.concat(validInstance[i]);
				valid = valid.concat('</div>');
			}	
		paramArray[2] = valid.toString();	
		}
		if(errCode == '0xcb0a0av'){
		return validateDataUsingMsgId(MessageTypeEnum.CONFIRMATION,errCode,paramArray,'instanceActionPerform');
		}else if(errCode == '0xcb0a0az'){
		return validateDataUsingMsgId(MessageTypeEnum.ERROR,errCode,paramArray,'');
		}
}

function enableVersions(){	
	document.getElementById("actionFormId:snmpVersion2Id:0").checked =true;	
	validateVersion();
}

function validateVersion1(){
	document.getElementById("newVersion1DivId").style.visibility = 'visible';
	document.getElementById("newVersion1DivId").style.display= 'inline';
	document.getElementById("newVersion2cDivId").style.visibility = 'hidden';
	document.getElementById("newVersion2cDivId").style.display= 'none';
	document.getElementById("newVersion3DivId").style.visibility = 'hidden';
	document.getElementById("newVersion3DivId").style.display= 'none';
	document.getElementById("actionFormId:snmpVersion2Id:0").checked =false;
	document.getElementById("actionFormId:snmpVersion3Id:0").checked =false;		
	document.getElementById('actionFormId:securityLevel').value = 'noAuthNoPriv';
	document.getElementById('actionFormId:authProtocol').value = 'MD5';
	document.getElementById('actionFormId:privacyProtocol').value = 'DES';
}

function validateVersion2c(){
	document.getElementById("newVersion2cDivId").style.visibility = 'visible';
	document.getElementById("newVersion2cDivId").style.display= 'inline';
	document.getElementById("newVersion1DivId").style.visibility = 'hidden';
	document.getElementById("newVersion1DivId").style.display= 'none';	
	document.getElementById("newVersion3DivId").style.visibility = 'hidden';
	document.getElementById("newVersion3DivId").style.display= 'none';
	document.getElementById("actionFormId:snmpVersion1Id:0").checked =false;
	document.getElementById("actionFormId:snmpVersion3Id:0").checked =false;		
	document.getElementById('actionFormId:securityLevel').value = 'noAuthNoPriv';
	document.getElementById('actionFormId:authProtocol').value = 'MD5';
	document.getElementById('actionFormId:privacyProtocol').value = 'DES';
	
	var communitystatus = document.getElementById('actionFormId:communityStatus').value;
	if(communitystatus == 'true'){
		jQuery("#"+ JSUtils.prototype
				.escapeColon("actionFormId:communityPwdTxt")).addClass("hide");
	} else {
		jQuery("#"+ JSUtils.prototype
				.escapeColon("actionFormId:communityDummyPwdText")).val('');
	}			
}

function validateVersion3(){
	document.getElementById("newVersion3DivId").style.visibility = 'visible';
	document.getElementById("newVersion3DivId").style.display= 'inline';
	document.getElementById("newVersion1DivId").style.visibility = 'hidden';
	document.getElementById("newVersion1DivId").style.display= 'none';
	document.getElementById("newVersion2cDivId").style.visibility = 'hidden';
	document.getElementById("newVersion2cDivId").style.display= 'none';
	document.getElementById("actionFormId:snmpVersion1Id:0").checked =false;
	document.getElementById("actionFormId:snmpVersion2Id:0").checked =false;
	validateSecurityLevel();
	}

function validateSecurityLevel(){
	var securityLevelVal=null;
	if(document.getElementById('actionFormId:securityLevel') != null){		
		securityLevelVal = document.getElementById('actionFormId:securityLevel').value;			
		var authstatus = document.getElementById('actionFormId:authStatus').value;
		var privacyStatus = document.getElementById('actionFormId:privacyStatus').value;
		if(securityLevelVal == 'noAuthNoPriv'){
			jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:authProtocolTxtId")).addClass("greyTxtColor");
			jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:authPasswordTxtId")).addClass("greyTxtColor");
			jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:privacyProtocolTxtId")).addClass("greyTxtColor");
			jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:privacyPassphraseTxtId")).addClass("greyTxtColor");			
			document.getElementById("actionFormId:authProtocol").disabled= true;
			document.getElementById("actionFormId:authDummyPwdText").disabled= true;
			document.getElementById("actionFormId:privacyProtocol").disabled= true;
			document.getElementById("actionFormId:privacyDummyPwdText").disabled= true;	
            jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:authPwdTxt")).val('');
			jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:authDummyPwdText")).val('');
			jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:privacyPwdTxt")).val('');
			jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:privacyDummyPwdText")).val('');			
		}else if(securityLevelVal == 'authNoPriv'){
			jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:authProtocolTxtId")).removeClass("greyTxtColor");
			jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:authPasswordTxtId")).removeClass("greyTxtColor");
			jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:privacyProtocolTxtId")).addClass("greyTxtColor");
			jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:privacyPassphraseTxtId")).addClass("greyTxtColor");
			if(document.getElementById('actionFormId:authProtocol').value == null){
			document.getElementById('actionFormId:authProtocol').value = 'MD5';
			}
			document.getElementById("actionFormId:authProtocol").disabled= false;
			document.getElementById("actionFormId:authDummyPwdText").disabled= false;
			document.getElementById("actionFormId:privacyProtocol").disabled= true;
			document.getElementById("actionFormId:privacyDummyPwdText").disabled= true;				
			jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:privacyPwdTxt")).val('');
            jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:privacyDummyPwdText")).val('');
            jQuery("#authPwdEmptyRow").addClass("hide"); 
			jQuery("#privacyPwdEmptyRow").addClass("hide"); 
            jQuery("#authConfirmPwdDivObj").addClass("hide"); 
        	jQuery("#privacyConfirmPwdDivObj").addClass("hide");        	       	
		}else if(securityLevelVal == 'authPriv'){
			jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:authProtocolTxtId")).removeClass("greyTxtColor");
			jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:authPasswordTxtId")).removeClass("greyTxtColor");
			jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:privacyProtocolTxtId")).removeClass("greyTxtColor");
			jQuery("#"+ JSUtils.prototype.escapeColon("actionFormId:privacyPassphraseTxtId")).removeClass("greyTxtColor");
			if(document.getElementById('actionFormId:authProtocol').value == null){
				document.getElementById('actionFormId:authProtocol').value = 'MD5';
			}
			if(document.getElementById('actionFormId:privacyProtocol').value == null){
			document.getElementById('actionFormId:privacyProtocol').value = 'DES';
			}
			document.getElementById("actionFormId:authProtocol").disabled= false;
			document.getElementById("actionFormId:authDummyPwdText").disabled= false;
			document.getElementById("actionFormId:privacyProtocol").disabled= false;
			document.getElementById("actionFormId:privacyDummyPwdText").disabled= false;
			jQuery("#authPwdEmptyRow").addClass("hide"); 
			jQuery("#privacyPwdEmptyRow").addClass("hide"); 
            jQuery("#authConfirmPwdDivObj").addClass("hide"); 
        	jQuery("#privacyConfirmPwdDivObj").addClass("hide");        	
		}	
		if(authstatus == 'true' && privacyStatus == 'false'){
			jQuery("#"+ JSUtils.prototype
					.escapeColon("actionFormId:privacyDummyPwdText")).val('');
		}else if(authstatus == 'false' && privacyStatus == 'true'){
			jQuery("#"+ JSUtils.prototype
					.escapeColon("actionFormId:authDummyPwdText")).val('');
		}else if(authstatus == 'false' && privacyStatus == 'false'){
			jQuery("#"+ JSUtils.prototype
					.escapeColon("actionFormId:authDummyPwdText")).val('');
			jQuery("#"+ JSUtils.prototype
					.escapeColon("actionFormId:privacyDummyPwdText")).val('');
		}
				
	}
}	

function validateVersion(){
	if(document.getElementById("selectedVersionID").value == 'v1'){
		document.getElementById("actionFormId:snmpVersion1Id:0").checked =true;
		document.getElementById("actionFormId:snmpVersion2Id:0").checked =false;
		document.getElementById("actionFormId:snmpVersion3Id:0").checked =false;
		validateVersion1();
	}else if(document.getElementById("selectedVersionID").value == 'v2c'){
		document.getElementById("actionFormId:snmpVersion3Id:0").checked =false;
		document.getElementById("actionFormId:snmpVersion1Id:0").checked =false;
		document.getElementById("actionFormId:snmpVersion2Id:0").checked =true;
		validateVersion2c();
	}
	else if(document.getElementById("selectedVersionID").value == 'v3'){
		document.getElementById("actionFormId:snmpVersion3Id:0").checked =true;
		document.getElementById("actionFormId:snmpVersion1Id:0").checked =false;
		document.getElementById("actionFormId:snmpVersion2Id:0").checked =false;
		validateVersion3();
	}
	else if(document.getElementById('selectedVersionID').value == ''){
		document.getElementById("actionFormId:snmpVersion2Id:0").checked =true;
		document.getElementById("actionFormId:snmpVersion1Id:0").checked =false;
		document.getElementById("actionFormId:snmpVersion3Id:0").checked =false;
		validateVersion2c();
	}			
}

function validatePwdText(){	
	var wmistatus = document.getElementById('actionFormId:wmiStatus').value;
	if(wmistatus == 'true'){
		jQuery("#"+ JSUtils.prototype
				.escapeColon("actionFormId:wmiPwdTxt")).addClass("hide");
	} else {
		jQuery("#"+ JSUtils.prototype
				.escapeColon("actionFormId:wmiDummyPwdText")).val('');
	}		
}

function showexportpopup(errorCode){
	paramArray = new Array();
	 paramArray[0] = 'Export VM';
	if(errorCode != ""){
 		 return validateDataUsingMsgId(MessageTypeEnum.ERROR,'0xae0i0ak',paramArray,'');
	}
	else{
		document.getElementById('actionFormId:moveToVMWARE:0').checked = true;
		document.getElementById('actionFormId:moveToVMWARE:1').checked = false;
		return showPopup("exportVM", 600);
	}
}

function displayDisksDetails(selected){
	if(selected.checked){
		jQuery("#volumerow").removeClass("hide");
	}
	else{
		jQuery("#volumerow").addClass("hide");
	}
}