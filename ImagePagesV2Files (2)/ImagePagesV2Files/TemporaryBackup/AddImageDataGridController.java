package com.cognizant.cloudone.ui.controller.datagrid;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;

import com.cognizant.cloudone.kernel.constants.GlobalConstant;
import com.cognizant.cloudone.to.instance.VirtualDiskTO;
import com.cognizant.cloudone.ui.controller.base.BaseDataGridController;
import com.cognizant.cloudone.ui.controller.bean.DataGridMetaInfoBean;
import com.cognizant.cloudone.to.instance.ImageTO;
import com.cognizant.cloudone.ui.util.WebUtilHelper;
import com.cognizant.cloudone.ui.pages.enums.PageEnum;
import com.cognizant.cloudone.ui.constants.DataGridEntityEnum;
import com.cognizant.cloudone.kernel.filter.enums.FilterConditionEnum;
import javax.faces.event.ActionEvent;
import javax.annotation.PostConstruct;
import static com.cognizant.cloudone.kernel.constants.GlobalConstant.PDF_XL_SESSION_DATA;
import static com.cognizant.cloudone.kernel.constants.GlobalConstant.PDF_XL_SESSION_PARAM;

public class AddImageDataGridController extends BaseDataGridController<ImageTO> { 
	
	 private List<ImageTO> imageList = null;
	 private String entityType = null;
	

	@PostConstruct
	public void init() {
		cleanUpSession();
        buildPageMetaData();
		setAutoCompleteOptions("");
		getItems();	
		}
	
	private void cleanUpSession() {
	   removeSession(PDF_XL_SESSION_DATA);
	   removeSession(PDF_XL_SESSION_PARAM);
	   removeSession(GlobalConstant.MASTER_DATA);  
	   removeSession(GlobalConstant.USER_SELECTED_ITEMS);
	   }
	  
	  @Override
    public void getItems(){
	   setAuthExpMsg("");		
	   DataGridMetaInfoBean metaInfo = new DataGridMetaInfoBean(); 
	   cleanUpSession();
	   buildDataGridMetaInfo(metaInfo, false);	
		}
	  
	 private void buildDataGridMetaInfo(DataGridMetaInfoBean metaInfo, boolean isPageSwitched){
		metaInfo.setCloudTO(createBaseCloudTo());
		metaInfo.setEntityType(DataGridEntityEnum.ADD_IMAGE);
		metaInfo.setPageMetaData(getPageMetaData());
		metaInfo.setProviderSwitched(isPageSwitched);		
		metaInfo.setSearchParam(getSearchParam());
		metaInfo.setProviderBasedFilter(false);
		setDataGridMetaInfo(DataGridEntityEnum.ADD_IMAGE.name(), metaInfo);
		this.setEntityType(DataGridEntityEnum.ADD_IMAGE.name());
		}
	 protected PageEnum getPageDetails() {
			return PageEnum.AddImages;
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

		public String getEntityType() {
			return entityType;
		}

		public void setEntityType(String entityType) {
			this.entityType = entityType;
		}
}
