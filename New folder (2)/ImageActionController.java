package com.cognizant.cloudone.ui.controller.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import com.cognizant.cloudone.auth.exception.AuthException;
import com.cognizant.cloudone.biz.instance.bo.OSDescriptorBO;
import com.cognizant.cloudone.ui.constants.NavigationRuleConstants;
import com.cognizant.cloudone.ui.constants.StringConstants;
import com.cognizant.cloudone.ui.controller.base.BaseDataGridActionController;
import com.cognizant.cloudone.ui.controller.bean.ImageBean;
import com.cognizant.cloudone.to.instance.DataCenterTO;
import com.cognizant.cloudone.to.instance.ImageInstanceTO;
import com.cognizant.cloudone.to.instance.ImageTO;
import com.cognizant.cloudone.to.instance.OSDescriptorTO;
import com.cognizant.cloudone.to.result.ResultTO;
import com.cognizant.cloudone.ic.images.facade.ImagesFacade;
import com.cognizant.cloudone.kernel.error.constants.ErrorConstants;
import com.cognizant.cloudone.kernel.image.enums.ImageActionsEnum;
import com.cognizant.cloudone.kernel.image.enums.ImageStatusEnum;
import com.cognizant.cloudone.kernel.logger.CloudOneLogger;
import com.cognizant.cloudone.kernel.logger.CloudOneLogger.CloudOneLoggerFactory;
import com.cognizant.cloudone.kernel.exception.ICException;
import static com.cognizant.cloudone.kernel.constants.GlobalConstant.IS_FIRST; 

public class ImageActionController extends BaseDataGridActionController {
	
	private final CloudOneLogger logger = CloudOneLoggerFactory.createLogger(ImageActionController.class.getName());
	
	private ImageBean imageBean = new ImageBean();
	private final String IMAGE_BEAN="IMAGE_BEAN";
	private String location;
	private String publisher;
	private String offer;
	private String instanceID;
	private List<SelectItem> datacenterList;
	private List<SelectItem> publisherList;
	private List<SelectItem> offerList;
	private List<OSDescriptorTO> listOfImages;
	private HashMap<String, DataCenterTO> dataCenterTOMap=new HashMap<>();
	public ImageActionController()
	{
		if (getRequestParam(IS_FIRST) != null){
			cleanup();
		}
		imageBean =   (ImageBean) getSessionParamAsObject(IMAGE_BEAN);
		if (imageBean == null) {
			imageBean = new ImageBean();
			setSessionParam(IMAGE_BEAN, imageBean);
		}
		if(getSessionParamAsObject(StringConstants.DATACENTER_LIST)!=null){
    		setDatacenterList((List<SelectItem>)getSessionParamAsObject(StringConstants.DATACENTER_LIST));
    		setDataCenterTOMap((HashMap<String, DataCenterTO>)getSessionParamAsObject(StringConstants.DATACENTER_MAP));
    	}
    	else{
    		datacenterList = new ArrayList<>();
    		dataCenterTOMap=new HashMap<>();
    		publisherList=new ArrayList<>();
    		offerList=new ArrayList<>();
    		listOfImages= new ArrayList<>();
    		populateDatacenterDetailsList();
    	}
		logger.debug("eniBean == >> {}",imageBean);
	}
	private void cleanup() {
		// TODO Auto-generated method stub
		removeSession(StringConstants.DATACENTER_LIST); 
		removeSession(StringConstants.DATACENTER_MAP);
	}
	public void launchCreate() {			
		redirectToPage(NavigationRuleConstants.GO_TO_CREATE_IMAGE_PAGE);			
	    }
	
	public void populateDatacenterDetailsList() {
		ResultTO<DataCenterTO> resultTO=null;
		 List<SelectItem> locationSelectItem = new ArrayList<>();
			HashMap<String, DataCenterTO> dataCenterMap=new HashMap<>();
		try {
			ImagesFacade imageFacade = ImagesFacade.SINGLETON_INSTANCE;
			OSDescriptorTO osDescriptorTO=new OSDescriptorTO();
			setCloudUserDetails(osDescriptorTO);
			resultTO= imageFacade.getDatacenterDetails(osDescriptorTO);
			if(resultTO.getResult()!=null){				
					for(DataCenterTO dataCenter:resultTO.getResult()){
					locationSelectItem.add(new SelectItem(dataCenter.getLocation(), dataCenter.getName()));			
					dataCenterMap.put(dataCenter.getLocation(), dataCenter);
				}				
			}			
		} catch (Exception e) {
			if (e instanceof AuthException) {
				logger.error(" Not authorized to create Image ");
				resultTO = new ResultTO<>();
				setErrorCode(ErrorConstants.CO_UI_ERR_0005.getCode());
				setAuthExpMsg(ErrorConstants.CO_UI_ERR_0005.getCode());
			}
			logger.error(e);
		}
		if (resultTO.getErrors() != null && resultTO.getErrors().hasErrors()) {
			processErrors(resultTO.getErrors());
			logger.info("Unable to create image.");		
		}
		
		this.setDatacenterList(locationSelectItem);
		this.setDataCenterTOMap(dataCenterMap);
		setSessionParam(StringConstants.DATACENTER_LIST, this.datacenterList);
		setSessionParam(StringConstants.DATACENTER_MAP, this.dataCenterTOMap);
	}
	
	public void populatePublisherList() {
		 List<SelectItem> publisherSelectItem = new ArrayList<>();
		try {
			ImagesFacade imageFacade = ImagesFacade.SINGLETON_INSTANCE;
			OSDescriptorTO osDescriptorTO=new OSDescriptorTO();
			DataCenterTO datacenter=new DataCenterTO();
			setCloudUserDetails(osDescriptorTO);
			String selectedLocation=this.location;
			if(dataCenterTOMap.get(selectedLocation)!=null){
				datacenter=dataCenterTOMap.get(selectedLocation);
			}
			List<OSDescriptorTO> publishernames= imageFacade.populatePublisherListForSelectedRegion(osDescriptorTO, datacenter);
			if(publishernames!=null){
						for(OSDescriptorTO osDescriptorBO:publishernames){
						publisherSelectItem.add(new SelectItem(osDescriptorBO.getDescription()));
				}				
			}			
		} catch (Exception e) {
			if (e instanceof AuthException) {
				logger.error(" Not authorized to create Image ");			
				setErrorCode(ErrorConstants.CO_UI_ERR_0005.getCode());
				setAuthExpMsg(ErrorConstants.CO_UI_ERR_0005.getCode());
			}
			logger.error(e);
		}		
		this.setPublisherList(publisherSelectItem);
		this.publisher="SUSE";
		populateOfferList();
	}
	
	public void populateOfferList() {
		 List<SelectItem> offerSelectItem = new ArrayList<>();
		try {
			ImagesFacade imageFacade = ImagesFacade.SINGLETON_INSTANCE;
			OSDescriptorTO osDescriptorTO=new OSDescriptorTO();
			DataCenterTO datacenter=new DataCenterTO();
			setCloudUserDetails(osDescriptorTO);
			String selectedLocation=this.location;
			osDescriptorTO.setOsFullName(this.publisher);
			if(dataCenterTOMap.get(selectedLocation)!=null){
				datacenter=dataCenterTOMap.get(selectedLocation);
			}
			List<OSDescriptorTO> offerItems= imageFacade.populateOfferListForSelectedPublisher(osDescriptorTO, datacenter);
			if(offerItems!=null){
						for(OSDescriptorTO osDescriptorBO:offerItems){
							offerSelectItem.add(new SelectItem(osDescriptorBO.getDescription()));
				}				
			}			
		} catch (Exception e) {
			if (e instanceof AuthException) {
				logger.error(" Not authorized to create Image ");			
				setErrorCode(ErrorConstants.CO_UI_ERR_0005.getCode());
				setAuthExpMsg(ErrorConstants.CO_UI_ERR_0005.getCode());
			}
			logger.error(e);
		}		
		this.setOfferList(offerSelectItem);
		this.offer="openSUSE";
		populateSkus();
	}
	
	public void populateSkus() {
		 List<OSDescriptorTO> imageList = new ArrayList<>();
		try {
			ImagesFacade imageFacade = ImagesFacade.SINGLETON_INSTANCE;
			OSDescriptorTO osDescriptorTO=new OSDescriptorTO();
			DataCenterTO datacenter=new DataCenterTO();
			setCloudUserDetails(osDescriptorTO);
			String selectedLocation=this.location;
			osDescriptorTO.setOsFullName(this.publisher+"~"+this.offer);
			if(dataCenterTOMap.get(selectedLocation)!=null){
				datacenter=dataCenterTOMap.get(selectedLocation);
			}
			List<OSDescriptorTO> osDescriptorTOs= imageFacade.populateSkus(osDescriptorTO, datacenter);
			if(osDescriptorTOs!=null){
					
			}			
		} catch (Exception e) {
			if (e instanceof AuthException) {
				logger.error(" Not authorized to create Image ");			
				setErrorCode(ErrorConstants.CO_UI_ERR_0005.getCode());
				setAuthExpMsg(ErrorConstants.CO_UI_ERR_0005.getCode());
			}
			logger.error(e);
		}		
		this.setListOfImages(imageList);
	}
	 
	public void createImage(){
		
		ResultTO resultTO = null;
		try {
			
			ImagesFacade imageFacade=ImagesFacade.SINGLETON_INSTANCE;
			//resultTO=imageFacade.createImage(getPrerequisiteDataForImageCreate());
			 System.out.println("Inside Method CreateImage");
		}
		catch (Exception e) {
			if (e instanceof AuthException) {
				logger.error(" Not authorized to create Image ");
				resultTO = new ResultTO<>();
				setErrorCode(ErrorConstants.CO_UI_ERR_0005.getCode());
				setAuthExpMsg(ErrorConstants.CO_UI_ERR_0005.getCode());
			}
			logger.error(e);
		}
		if (resultTO.getErrors() != null && resultTO.getErrors().hasErrors()) {
			processErrors(resultTO.getErrors());
			logger.info("Unable to create image.");
		} else {
			logger.debug("Setting result object ");
			if (resultTO.getMessages() != null && resultTO.getMessages().hasMessages()) {
				processMessages(resultTO.getMessages());
				setAuthExpMsg("0xee0i0ao");
				setErrorCode("0xee0i0ao");
				logger.info("Displaying messages for user group update Quota.");
			}
			
		}
		 
	 }
	
	private ImageTO getPrerequisiteDataForImageCreate() {
		ImageTO imageTO= new ImageTO();
		setCloudUserDetails(imageTO);
		populateDatacenterDetailsList();
		List<ImageInstanceTO> imageList = (List<ImageInstanceTO>)getSessionParamAsObject(StringConstants.SELECTED_IMAGE_OBJECT);
		imageTO.setLocation(this.getLocation());
		imageTO.setImageDetails(this.getPublisher());
		imageTO.setOsName(this.getOffer());
		imageTO.setListAmiImages(imageList);
		return 	imageTO;
	}
	
    public void navigateToListImages(){
	    	redirectToPage(NavigationRuleConstants.LOAD_PR_IMG);
	    }
	public void selectRow() {
		List<ImageInstanceTO> imageList = getSelectedItems();
		if(imageList != null && imageList.size() == 1 && imageList.get(0) != null){ // if a row is selected
			setSessionParam(StringConstants.SELECTED_IMAGE_OBJECT, imageList);
		}
	
	}	
	public void create() {
		// TODO Auto-generated method stub

	}

	
	public void update() {
		// TODO Auto-generated method stub

	}

	
	public void delete() {
		logger.debug("In delete method -- >>>> ");
		List<ImageTO> ImageTOs = getSelectedItems();
		ImagesFacade imageFacade=ImagesFacade.SINGLETON_INSTANCE;
		ImageTO imageTO=new ImageTO();
		setCloudUserDetails(imageTO);
		if (ImageTOs != null && !ImageTOs.isEmpty()) {
			logger.debug("In delete method ImageID-- >>>> {}",ImageTOs.get(0).getImageID());
			imageTO.setImageID(ImageTOs.get(0).getImageID());			
		}
		imageTO.setImageActionEnum(ImageActionsEnum.DELETE_IMAGE);
		try {
			ResultTO<ImageTO> resultTO=new ResultTO<>();
			//ResultTO<ImageTO> resultTO = networkMngmtFacade.actionOnENI(networkingTO);
			if (resultTO != null && resultTO.getErrors() != null && resultTO.getErrors().hasErrors()) {
				// Process Error.
				processErrors(resultTO.getErrors());
				setErrorCode("0xee0i0wh");

			} else {
				setMessageCode("0xee0i0ac");
			}
		} catch (Exception e) {
			logger.debug("Error in Deleting Image -- > {}",e);
		}
		reloadDataGrid();

	}

	
	public void validateAction() {
		// TODO Auto-generated method stub

	}

	@Override
	public void performAction() {
		
		StringBuffer bufferSelectedRows = new StringBuffer();
		String operationType = getRequestParam("operation");
		List<ImageTO> userSelectedImages = getSelectedItems();
		if (imageBean == null) {
			imageBean = new ImageBean();
		}
		if (userSelectedImages != null && !userSelectedImages.isEmpty()) {
			for (ImageTO imageTO : userSelectedImages) {
				if(imageTO != null) {
					setItemSelected(true);
					bufferSelectedRows.append(imageTO.getImageID());
					bufferSelectedRows.append(",");
					if (operationType.equalsIgnoreCase(ImageActionsEnum.DELETE_IMAGE.toString())){
						if(imageTO.getStatus()!=null){
						if(imageTO.getStatus().equalsIgnoreCase(ImageStatusEnum.IN_USE.getDesc())) {
							imageBean.setInstanceID(imageTO.getInstanceID());
							setErrorCode("0xee0i0mw");
						}
					}
					}
					imageBean.setImageID(imageTO.getImageID());
					this.setInstanceID(imageTO.getImageID());
				}
				
			}
			if(bufferSelectedRows != null) {
				setSelectedGridRows(bufferSelectedRows.toString());
			}
		}

	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getOffer() {
		return offer;
	}
	public void setOffer(String offer) {
		this.offer = offer;
	}
	public ImageBean getImageBean() {
		return imageBean;
	}
	public void setImageBean(ImageBean imageBean) {
		this.imageBean = imageBean;
	}
	public String getInstanceID() {
		return instanceID;
	}
	public void setInstanceID(String instanceID) {
		this.instanceID = instanceID;
	}
	/**
	 * @return the datacenterList
	 */
	public List<SelectItem> getDatacenterList() {
		return datacenterList;
	}
	/**
	 * @param datacenterList the datacenterList to set
	 */
	public void setDatacenterList(List<SelectItem> datacenterList) {
		this.datacenterList = datacenterList;
	}
	/**
	 * @return the dataCenterTOMap
	 */
	public HashMap<String, DataCenterTO> getDataCenterTOMap() {
		return dataCenterTOMap;
	}
	/**
	 * @param dataCenterTOMap the dataCenterTOMap to set
	 */
	public void setDataCenterTOMap(HashMap<String, DataCenterTO> dataCenterTOMap) {
		this.dataCenterTOMap = dataCenterTOMap;
	}
	/**
	 * @return the publisherList
	 */
	public List<SelectItem> getPublisherList() {
		return publisherList;
	}
	/**
	 * @param publisherList the publisherList to set
	 */
	public void setPublisherList(List<SelectItem> publisherList) {
		this.publisherList = publisherList;
	}
	/**
	 * @return the offerList
	 */
	public List<SelectItem> getOfferList() {
		return offerList;
	}
	/**
	 * @param offerList the offerList to set
	 */
	public void setOfferList(List<SelectItem> offerList) {
		this.offerList = offerList;
	}
	/**
	 * @return the listOfImages
	 */
	public List<OSDescriptorTO> getListOfImages() {
		return listOfImages;
	}
	/**
	 * @param listOfImages the listOfImages to set
	 */
	public void setListOfImages(List<OSDescriptorTO> listOfImages) {
		this.listOfImages = listOfImages;
	}

}
