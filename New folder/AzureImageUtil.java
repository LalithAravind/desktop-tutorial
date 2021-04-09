package com.cognizant.cloudone.cl.azure.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cognizant.cloudone.biz.base.bo.CloudCredentialsBO;
import com.cognizant.cloudone.biz.instance.bo.ResourceGroupDetailsBO;
import com.cognizant.cloudone.cl.azure.constants.AzureConstants;
import com.cognizant.cloudone.cl.azure.entity.CaptureRoleAsVMImageOperation;
import com.cognizant.cloudone.cl.azure.entity.VMImage;
import com.cognizant.cloudone.cl.azure.entity.VMImages;
import com.cognizant.cloudone.cl.azure.entity.v2.ImageRefrence;
import com.cognizant.cloudone.cl.azure.xml.AzureMarshaller;
import com.cognizant.cloudone.common.bean.systemspec.ConnectionSystemSpecBean;
import com.cognizant.cloudone.connect.AzureConnection;
import com.cognizant.cloudone.kernel.constants.GlobalConstant;
import com.cognizant.cloudone.kernel.exception.CLException;
import com.cognizant.cloudone.kernel.logger.CloudOneLogger;
import com.cognizant.cloudone.kernel.util.CloudOneXMLUtil;
import com.cognizant.cloudone.kernel.util.HttpRequestProcessor;
import com.google.gson.Gson;

/**
 * @author 468003
 * List of APIs used for Azure Image operations
 */
public class AzureImageUtil {

	private static final CloudOneLogger logger = CloudOneLogger
			.getLogger(AzureImageUtil.class.getName());	

	/**
	 * Used to clone a VM image
	 * @param captureRoleAsVMImageOperation
	 * @param credentialsBO
	 * @return
	 * @throws CLException
	 */
	public String captureVMImage(CaptureRoleAsVMImageOperation captureRoleAsVMImageOperation, CloudCredentialsBO credentialsBO) throws CLException {		
		logger.info("Enter into captureVMImage with hostedServiceName :: "+captureRoleAsVMImageOperation.getHostedServiceName()+" / deploymentName :: "+captureRoleAsVMImageOperation.getDeploymentName() +" / roleName :: "+captureRoleAsVMImageOperation.getRoleName());
		String result = null;
		try {
			String requestURL = AzureCommonUtil.getVMOperationsUrl(credentialsBO.getSubscriptionId(),captureRoleAsVMImageOperation.getHostedServiceName(), captureRoleAsVMImageOperation.getDeploymentName(), captureRoleAsVMImageOperation.getRoleName());
			InputStream payloadXMLStream = new ByteArrayInputStream(AzureMarshaller.marshall(captureRoleAsVMImageOperation, true).getBytes());
			Map<String, String> responseMap = AzureConnection.sendAPIRequest(GlobalConstant.POST_REQUEST, requestURL,AzureCommonUtil.getHeaderMap(), payloadXMLStream, AzureUtil.getNewCertStream(credentialsBO.getPkcsFile()),credentialsBO.getPkcsPwd(), credentialsBO.getKeyPwd());
		    result = AzureUtil.pollForRequestCompletion(credentialsBO,responseMap, AzureConstants.NUMBER_OF_POLLS, AzureConstants.POLLING_INTERVAL);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CLException(e);
		} 
		logger.info("Exit to captureVMImage with result :: "+result);
		return (null != result && AzureConstants.SUCCESS.equals(result)) ? result : AzureConstants.FAILURE;
	}
	
	/**
	 * Used to delete a VM Image
	 * @param credentialsBO
	 * @param imageName
	 * @param deleteAssociatedVHD
	 * @return
	 * @throws CLException
	 */
	public String deleteVMImage(CloudCredentialsBO credentialsBO,String imageName,boolean deleteAssociatedVHD) throws CLException {
		logger.info("Enter into deleteVMImage with imageName :: "+imageName);
		String result = null;
		try {
			String requestURL =AzureCommonUtil.getVMImagesOperationUrl(credentialsBO.getSubscriptionId(),imageName,deleteAssociatedVHD);
			Map<String, String> responseMap = AzureConnection.sendAPIRequest(GlobalConstant.DELETE_REQUEST,requestURL,AzureCommonUtil.getHeaderMap(), null, AzureUtil.getNewCertStream(credentialsBO.getPkcsFile()),credentialsBO.getPkcsPwd(), credentialsBO.getKeyPwd());
			result = AzureUtil.pollForRequestCompletion(credentialsBO,responseMap, AzureConstants.NUMBER_OF_POLLS, AzureConstants.POLLING_INTERVAL);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CLException(e);
		} 
		logger.info("Exit to deleteVMImage with result :: "+result);
		return (null != result && AzureConstants.SUCCESS.equals(result)) ? result : AzureConstants.FAILURE;
	}
	
	/**
	 * Used to get the list of VMImages
	 * 
	 * @param credentialsBO
	 * @return
	 * @throws CLException
	 */
	public List<VMImage> getVMImages(CloudCredentialsBO credentialsBO) throws CLException {
		logger.info("Enter into getVMImages...");
		List<VMImage> vmImagesList = null;
		try {
			String requestURL = AzureCommonUtil.getAllVMImagesURL(credentialsBO.getSubscriptionId(), null, false, true);
			Map<String, String> responseMap = AzureConnection.sendAPIRequest(GlobalConstant.GET_REQUEST, requestURL,
					AzureCommonUtil.getHeaderMap(), null, AzureUtil.getNewCertStream(credentialsBO.getPkcsFile()),
					credentialsBO.getPkcsPwd(), credentialsBO.getKeyPwd());
			if (null != responseMap.get(AzureConstants.MAP_KEY_RESULT)) {
				VMImages vmImages = new VMImages();
				Object object = CloudOneXMLUtil.parseXMLToJavaClass(responseMap.get(AzureConstants.MAP_KEY_RESULT),
						false, vmImages);
				if (null != object && object instanceof VMImages) {
					vmImages = (VMImages) object;
					vmImagesList = vmImages.getVmImage();
				}
			}
		} catch (CLException e) {
			logger.error(e);
			throw e;
		} finally {
			int noOfDisks = (vmImagesList != null) ? vmImagesList.size() : 0;
			logger.info("Exit to getVMImages...with total vmImagesList size :: " + noOfDisks);
		}
		return vmImagesList;
	}
	
	public List<ImageRefrence> getPublisherDetails(CloudCredentialsBO credentialsBO, String datacenterName) throws CLException {

		String reqURL = AzureCommonUtil.getBaseURL(credentialsBO.getSubscriptionId(), AzureConstants.API_VERSION_V2)
				+ "providers/Microsoft.Compute/locations/" + datacenterName + "/publishers?api-version=2015-06-15";

		return getV2ImageDetails(credentialsBO.getAccessToken(), reqURL);
	}

	public List<ImageRefrence> getOfferDetails(CloudCredentialsBO credentialsBO, String datacenterName,
			String publisherName) throws CLException {

		String reqURL = AzureCommonUtil.getBaseURL(credentialsBO.getSubscriptionId(), AzureConstants.API_VERSION_V2)
				+ "providers/Microsoft.Compute/locations/" + datacenterName + "/publishers/" + publisherName
				+ "/artifacttypes/vmimage/offers?api-version=2015-06-15";

		return getV2ImageDetails(credentialsBO.getAccessToken(), reqURL);
	}

	public List<ImageRefrence> getSkusDetails(CloudCredentialsBO credentialsBO, String datacenterName,
			String publisherName, String offerName) throws CLException {

		String reqURL = AzureCommonUtil.getBaseURL(credentialsBO.getSubscriptionId(), AzureConstants.API_VERSION_V2)
				+ "providers/Microsoft.Compute/locations/" + datacenterName + "/publishers/" + publisherName
				+ "/artifacttypes/vmimage/offers/" + offerName + "/skus?api-version=2015-06-15";

		return getV2ImageDetails(credentialsBO.getAccessToken(), reqURL);
	}

	public List<ImageRefrence> getV2ImageDetails(String accessToken, String reqURL) throws CLException {
		logger.info("Entering into getV2ImageDetails from API");
		List<ImageRefrence> listOfImageReference = null;
		HttpRequestProcessor httpRequestProcessor = null;
		try {
			ConnectionSystemSpecBean connectionSystemSpecBean = new ConnectionSystemSpecBean();
			connectionSystemSpecBean.setCloudId(1);
			httpRequestProcessor = AzureConnection.sendV2APIRequest(reqURL, GlobalConstant.GET_REQUEST, null,
					AzureCommonUtil.getV2HeaderMap(accessToken), 0, connectionSystemSpecBean);
		} catch (CLException e) {
			logger.error(e.getMessage());
			throw new CLException(e);
		} finally {
			if (null != httpRequestProcessor) {
				String response = new String(httpRequestProcessor.getResponseData());
				boolean result = AzureCommonUtil.isRequestSuccess(httpRequestProcessor.getResponseCode());
				if (result) {
					ImageRefrence[] publishersObject = new Gson().fromJson(response, ImageRefrence[].class);
					listOfImageReference = Arrays.asList(publishersObject);
				}

				logger.info("Exit to getV2ImageDetails");
			}
		}
		return listOfImageReference;
	}

}
