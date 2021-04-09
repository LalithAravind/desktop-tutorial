package com.cognizant.cloudone.dal.dao;

import com.cognizant.cloudone.dal.dbo.OsDetailsDO;
import com.cognizant.cloudone.kernel.exception.DALException;
import java.util.List;

public interface OsDetailsDAO extends BatchSupportDAO {

	/**
	 * This method is used for inserting OsDetailsDO
	 * 
	 * @param osDetailsDO
	 * @return OsDetailsDO
	 * @throws DALException
	 */
	public OsDetailsDO create(OsDetailsDO osDetailsDO) throws DALException;

	/**
	 * This method is used for updating an existing OsDetailsDO
	 * 
	 * @param osDetailsDO
	 * @return OsDetailsDO
	 * @throws DALException
	 */
	public OsDetailsDO update(OsDetailsDO osDetailsDO) throws DALException;

	/**
	 * This method is used for deleting OsDetailsDO
	 * 
	 * @param osDetailsDO
	 * @return
	 * @throws DALException
	 */
	public int delete(OsDetailsDO osDetailsDO) throws DALException;

	/**
	 * This method is used for fetching OsDetailsDO based on OsDetailsId
	 * 
	 * @param osDetailsDO
	 * @return OsDetailsDO
	 * @throws DALException
	 */
	public OsDetailsDO fetchByOsDetailsId(OsDetailsDO osDetailsDO)
			throws DALException;

	/**
	 * This method is used for fetching All OsDetailsDO based on the CloudId
	 * 
	 * @param osDetailsDO
	 * @return OsDetailsDO
	 * @throws DALException
	 */
	public List<OsDetailsDO> fetchByCloudId(OsDetailsDO osDetailsDO)
			throws DALException;

	/**
	 * This method is used for fetching All OsDetailsDO
	 * 
	 * @param
	 * @return OsDetailsDO
	 * @throws DALException
	 */
	public List<OsDetailsDO> fetchAllOsDetails(OsDetailsDO osDetailsDO) throws DALException;

	/**
	 * This method is used for inserting or updating OsDetailsDO
	 * 
	 * @param osDetailsDO
	 * @return OsDetailsDO
	 * @throws DALException
	 */
	public OsDetailsDO saveOrUpdate(OsDetailsDO osDetailsDO)
			throws DALException;
	
	/**
	 * This method is used for fetching All OsDetailsDO based on the CloudId
	 * and providerId
	 * @param osDetailsDO
	 * @return OsDetailsDO
	 * @throws DALException
	 */
	public List<OsDetailsDO> fetchByCloudIdProviderId(OsDetailsDO osDetailsDO)
			throws DALException;
	
	public List<OsDetailsDO> fetchByProviderIdOsId(OsDetailsDO osDetailsDO)
			throws DALException;
	
	/**
	 * This method is used for fetching All OsDetailsDO based on the CloudId
	 * and providerId and DatacenterId
	 * @param osDetailsDO
	 * @return OsDetailsDO
	 * @throws DALException
	 */
	
	public List<OsDetailsDO> fetchByCloudIdProviderIdDatacenterId(OsDetailsDO osDetailsDO)
			throws DALException;
}
