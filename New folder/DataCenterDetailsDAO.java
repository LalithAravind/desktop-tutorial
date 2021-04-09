package com.cognizant.cloudone.dal.dao;

import java.util.List;

import com.cognizant.cloudone.dal.dbo.DataCenterDetailsDO;
import com.cognizant.cloudone.kernel.exception.DALException;

public interface DataCenterDetailsDAO extends BatchSupportDAO {
	/**
	 * This method is used for inserting DataCenterDO
	 * 
	 * @param dataCenterDO
	 * @return DataCenterDO
	 * @throws DALException
	 */
	public DataCenterDetailsDO create(DataCenterDetailsDO dataCenterDO) throws DALException;

	/**
	 * This method is used for updating an existing DataCenterDO
	 * 
	 * @param dataCenterDO
	 * @return DataCenterDO
	 * @throws DALException
	 */
	public DataCenterDetailsDO update(DataCenterDetailsDO dataCenterDO) throws DALException;

	/**
	 * This method is used for deleting DataCenterDO
	 * 
	 * @param dataCenterDO
	 * @return
	 * @throws DALException
	 */
	public int delete(DataCenterDetailsDO dataCenterDO) throws DALException;
	
	/**
	 * This method is used for fetching DataCenterDO based on DataCenterId
	 * 
	 * @param dataCenterDO
	 * @return DataCenterDO
	 * @throws DALException
	 */
	public DataCenterDetailsDO fetchByDataCenterId(DataCenterDetailsDO dataCenterDO)throws DALException;
	
	/**
	 * This method is used for fetching DataCenterDO based on CloudID
	 * and DataCenterName
	 * 
	 * @param dataCenterDO
	 * @return DataCenterDO
	 * @throws DALException
	 */
	public DataCenterDetailsDO fetchByCloudIDDataCenterName(DataCenterDetailsDO dataCenterDO)throws DALException;
	
	/**
	 * This method is used for fetching DataCenterDO based on CloudId
	 * 
	 * @param dataCenterDO
	 * @return DataCenterDO
	 * @throws DALException
	 */
	public List<DataCenterDetailsDO> fetchByCloudId(DataCenterDetailsDO dataCenterDO)throws DALException;
	
	
	/**
	 * This method is used for fetching DataCenterDO based on ProviderID DataCenterName
	 * and DataCenterName
	 * 
	 * @param dataCenterDO
	 * @return DataCenterDO
	 * @throws DALException
	 */
	public DataCenterDetailsDO fetchByCloudIDProviderIDDataCenterName(DataCenterDetailsDO dataCenterDO)throws DALException;
	
	/**
	 * This method is used for fetching DataCenterDO based on ProviderID MorID
	 * and DataCenterName
	 * 
	 * @param dataCenterDO
	 * @return DataCenterDO
	 * @throws DALException
	 */
	public DataCenterDetailsDO fetchByCloudIDProviderIDMorId(DataCenterDetailsDO dataCenterDO)throws DALException;
	
	/**
	 * This method is used for fetching DataCenterDO based on CloudId, ProviderId
	 * 
	 * @param dataCenterDO
	 * @return DataCenterDO
	 * @throws DALException
	 */
	public List<DataCenterDetailsDO> fetchByCloudIdProviderId(DataCenterDetailsDO dataCenterDO)throws DALException;
	
	/**
	 * This method is used for fetching DataCenterDO based on CloudId, ProviderId
	 * 
	 * @param dataCenterDO
	 * @return DataCenterDO
	 * @throws DALException
	 */

	public List<DataCenterDetailsDO> fetchByCloudIdProviderIdNotEqualsProfile(DataCenterDetailsDO dataCenterDO) throws DALException;

	
	/**
	 * This method is used for fetching Active DataCenterDOs based on CloudId, ProviderId
	 * 
	 * @param dataCenterDO
	 * @return DataCenterDO
	 * @throws DALException
	 */
	public List<DataCenterDetailsDO> fetchActiveDcsByCloudIdProviderId(
			DataCenterDetailsDO dataCenterDO) throws DALException;
	/**
	 * This method is used for fetching DataCenterDO based on ProviderID LocationName
	 * and LocationName
	 * 
	 * @param dataCenterDO
	 * @return DataCenterDO
	 * @throws DALException
	 */
	public DataCenterDetailsDO fetchByCIDPIDLocationName(DataCenterDetailsDO dataCenterDO)throws DALException; 
}
