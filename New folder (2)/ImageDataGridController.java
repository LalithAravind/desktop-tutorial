package com.cognizant.cloudone.ui.controller.datagrid;

import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

import com.cognizant.cloudone.kernel.filter.enums.FilterConditionEnum;
import com.cognizant.cloudone.kernel.logger.CloudOneLogger;
import com.cognizant.cloudone.to.instance.ImageTO;
import com.cognizant.cloudone.to.networking.NetworkingTO;
import com.cognizant.cloudone.ui.controller.base.BaseDataGridController;
import com.cognizant.cloudone.ui.controller.bean.DataGridMetaInfoBean;
import com.cognizant.cloudone.ui.constants.DataGridEntityEnum;
import com.cognizant.cloudone.ui.util.WebUtilHelper;
import com.cognizant.cloudone.ui.controller.bean.ProviderDetailsBean;
import com.cognizant.cloudone.ui.controller.proxy.GridDataProviderFactory;
import static com.cognizant.cloudone.kernel.constants.GlobalConstant.IS_FIRST;
import static com.cognizant.cloudone.kernel.constants.GlobalConstant.FILTERED_LIST;
import static com.cognizant.cloudone.kernel.constants.GlobalConstant.PDF_XL_SESSION_DATA;
import static com.cognizant.cloudone.kernel.constants.GlobalConstant.PDF_XL_SESSION_PARAM;
import com.cognizant.cloudone.kernel.constants.GlobalConstant;
import com.cognizant.cloudone.ui.pages.enums.PageEnum;
import com.cognizant.cloudone.ui.controller.bean.ImageBean;
import com.cognizant.cloudone.kernel.constants.ProviderTypeEnum;
import com.cognizant.cloudone.ui.constants.StringConstants;

public class ImageDataGridController extends BaseDataGridController<ImageTO>{
	
	private final CloudOneLogger logger = CloudOneLogger.getLogger(this.getClass().getName());
	private List<ImageTO> imageList = null;
	private String entityType = null;
	 private String providerType = null;
	private ImageBean imageBean=new ImageBean();
	
	
	@PostConstruct
	public void init() {
		if (getRequestParam(IS_FIRST) != null) {
			cleanUpData();
		}
        buildPageMetaData();
		setAutoCompleteOptions("");
		getItems();	
		ProviderDetailsBean providerDetailsBean = getProvidersDetails();
		}
	
	public void getItems() {
		setAuthExpMsg("");				
		//if this it first time, clean up the session basket.
		DataGridMetaInfoBean metaInfo = new DataGridMetaInfoBean(); 
		if ("true".equals(getRequestParam(IS_FIRST))){
			cleanUpData();
		}
		buildDataGridMetaInfo(metaInfo, false);
		
			
	}
	
	private void buildDataGridMetaInfo(DataGridMetaInfoBean metaInfo, boolean isPageSwitched){		
		metaInfo.setCloudTO(createBaseCloudTo());
		metaInfo.setEntityType(DataGridEntityEnum.IMAGES);				
		metaInfo.setPageMetaData(getPageMetaData());
		metaInfo.setProviderSwitched(isPageSwitched);
		metaInfo.setSearchParam(getSearchParam());
		metaInfo.setProviderBasedFilter(false);		
		setDataGridMetaInfo(DataGridEntityEnum.IMAGES.name(), metaInfo);
		this.setProviderType(metaInfo.getCloudTO().getProviderType().name());
		if(this.getProviderType().equalsIgnoreCase(ProviderTypeEnum.AZURE.name()))
		{
		this.setEntityType(DataGridEntityEnum.IMAGES.name());
		}
		else
		{
			this.setEntityType("OTHER_IMAGES");
		}
	}
	
	private void buildDataGridMetaInfo(boolean isPageSwitched){
		buildDataGridMetaInfo(new DataGridMetaInfoBean(), isPageSwitched);
	}
	
	/****
	 * Method that fetches the data from back-end 
	 * based on meta data provided.
	 * 
	 * @param req
	 * @param metaInfoBean
	 * @return Map of data.
	 */
	private LinkedHashMap<Long, ImageTO> getMasterData(DataGridMetaInfoBean metaInfoBean) {
		LinkedHashMap<Long, ImageTO> data = null;
		List<ImageTO> rowData = null;
		if(metaInfoBean.isFromDataBase()) {
			rowData = GridDataProviderFactory.get(metaInfoBean.getEntityType(),metaInfoBean.getCloudTO());
		}else {
			if( getDataforDataGrid(DataGridEntityEnum.IMAGES.name()) == null) {
				rowData = GridDataProviderFactory.get(metaInfoBean.getEntityType(),metaInfoBean.getCloudTO());
				if(rowData != null){	
					data = WebUtilHelper.convertListToMap(rowData);
					setDataforDataGrid(DataGridEntityEnum.IMAGES.name(),data);
				}
			}else {
				data = getDataforDataGrid(DataGridEntityEnum.IMAGES.name());
			}
		}
		return data;
	}
	/***
	 * Method that cleans up the data kept in memory.
	 * This will be only called when there is flag supplied with URL.
	 */
	private void cleanUpData() {
		removeSession(PDF_XL_SESSION_DATA);
		removeSession(PDF_XL_SESSION_PARAM);
		removeSession(GlobalConstant.USER_SELECTED_ITEMS);
        removeSession(FILTERED_LIST);
        removeSession(GlobalConstant.MASTER_DATA);
	}
	
	protected PageEnum getPageDetails() {
		return PageEnum.ListImages;
	}

	public void filterBySmartView() {
		// TODO Auto-generated method stub
		
	}

	public void filterByTreeNode() {
		// TODO Auto-generated method stub
		
	}

	public void filterBySummary(ActionEvent evt) {
		// TODO Auto-generated method stub
		
	}

	public void showAll() {
		// TODO Auto-generated method stub
		
	}

	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	protected void filterBySearch(String columnName,
			FilterConditionEnum filterConditionEnum, String searchString) {
		// TODO Auto-generated method stub
		
	}

	public List<ImageTO> getImageList() {
		return imageList;
	}

	public void setImageList(List<ImageTO> imageList) {
		this.imageList = imageList;
	}

	public ImageBean getImageBean() {
		return imageBean;
	}

	public void setImageBean(ImageBean imageBean) {
		this.imageBean = imageBean;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getProviderType() {
		return providerType;
	}

	public void setProviderType(String providerType) {
		this.providerType = providerType;
	}

}
