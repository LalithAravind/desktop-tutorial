function registerJS(){
}

	function loadimageCreatePopUp() {
		 showPopup('create_image', '945');
		
	}
	
	function viewImagesOnLoad(){
		document.getElementById("addImageFormId:resourceManagRadio:0").checked = true;
		document.getElementById("addImageFormId:resourceManagRadio:0").disabled=true;
		
	}
	function validateCreateImages(){
		
		LoadingButtonUtils.changeToLoading(jQuery("#"	+ JSUtils.prototype.escapeColon("saveAs")));
		
			
			document.getElementById('addImagesFormId:actionAddImageId').onclick();	
			
	}
	
	function preDeleteCheck(selRow,selVal,formId,deleteLinkId,errorCode,instanceId) {
	      if(selVal=='false'){
	           var paramArray = new Array();
	           showPopUpUsingMsgId(MessageTypeEnum.ERROR,'0xee0i0ww',paramArray,'');
	           return false;
	      } else if(errorCode != null && errorCode != '') {
	            var paramArray = new Array();
	            var instances = new String();
	            var blankSpace = '  '; 
	            instances = instances.concat('<div style="padding-left:10px;">');
				instances = instances.concat('<span class="bulletpoint"></span>'+blankSpace);
				instances = instances.concat(instanceId);
	    		instances = instances.concat('</div>');		
	            paramArray[0]=instances.toString();;
	            validateDataUsingMsgId(MessageTypeEnum.ERROR, errorCode, paramArray, '');
	      } else {
	            confirmDeleteOperation(selRow,selVal,formId,deleteLinkId,'','Image');
	      }
	}
	
	function showImageMessage(errCode,msgCode) {
		var paramArray = new Array();
		if (null != errCode && errCode != '') {
			validateDataUsingMsgId(MessageTypeEnum.ERROR, errCode, paramArray, '');
		} else {
			showResponseInline(msgCode,paramArray,"");
		}
		return false;
	}
