package com.cognizant.cloudone.dal.dao.impl;

import com.cognizant.cloudone.dal.dao.OsDetailsDAO;
import com.cognizant.cloudone.dal.dbo.OsDetailsDO;
import com.cognizant.cloudone.kernel.error.constants.ErrorConstants;
import com.cognizant.cloudone.kernel.exception.DALException;
import com.cognizant.cloudone.kernel.logger.CloudOneLogger;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

public class OsDetailsDaoImpl extends BatchSupportDaoImpl implements
		OsDetailsDAO {

	private final CloudOneLogger logger = CloudOneLogger
			.getLogger(OsDetailsDaoImpl.class.getName());

	@Override
	public OsDetailsDO create(OsDetailsDO osDetailsDO) throws DALException {
		OsDetailsDO thisOsDetailsDO = null;
		try {
			thisOsDetailsDO = excute(osDetailsDO, DataBaseOperationType.create);
		} catch (Exception e) {
			logger.debug("Error in Creating Os Details {}",e);
			throw new DALException(e);
		}
		logger.debug("Created Os Details {}",thisOsDetailsDO);
		return thisOsDetailsDO;
	}

	@Override
	public int delete(OsDetailsDO osDetailsDO) throws DALException {
		@SuppressWarnings("unused")
		OsDetailsDO thisOsDetailsDO = null;
		try {
			thisOsDetailsDO = excute(osDetailsDO, DataBaseOperationType.delete);
		} catch (Exception e) {
			logger.debug("Error in Deleting Os Details {}",e);
			throw new DALException(e);
		}
		return 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OsDetailsDO> fetchAllOsDetails(OsDetailsDO osDetailsDO) throws DALException {
		List<OsDetailsDO> lstOsDetailsDO = null;
		try {
			lstOsDetailsDO = (List<OsDetailsDO>) getCriteria(OsDetailsDO.class)
							.add(Restrictions.eq("cloudMasterDO", osDetailsDO
									.getCloudMasterDO()))
							.add(Restrictions.eq("providerMasterDO", osDetailsDO
									.getProviderMasterDO())).list();
		} catch (HibernateException e) {
			logger.debug("Error in OsDetailsDaoImpl class fetchAllOsDetails : {}",e);
			throw new DALException(e.getMessage());
		}
		if (lstOsDetailsDO == null) {
			logger.debug("Error in OsDetailsDaoImpl class fetchAllOsDetails : {}",
					ErrorConstants.CO_DAL_ERR_079.getCode());
			throw new DALException(ErrorConstants.CO_DAL_ERR_079.getCode());
		} else {
			return lstOsDetailsDO;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OsDetailsDO> fetchByCloudId(OsDetailsDO osDetailsDO)
			throws DALException {
		List<OsDetailsDO> lstOsDetailsDO = null;
		try {
			lstOsDetailsDO = (List<OsDetailsDO>) getCriteria(OsDetailsDO.class)
					.add(
							Restrictions.eq("cloudMasterDO", osDetailsDO
									.getCloudMasterDO())).list();
		} catch (HibernateException e) {
			logger
					.debug("Error in OsDetailsDaoImpl class fetchByCloudId : {}",
							 e);
			throw new DALException(e.getMessage());
		}
		if (lstOsDetailsDO == null) {
			logger.debug("Error in OsDetailsDaoImpl class fetchByCloudId : {}",
					 ErrorConstants.CO_DAL_ERR_079.getCode());
			throw new DALException(ErrorConstants.CO_DAL_ERR_079.getCode());
		} else {
			return lstOsDetailsDO;
		}
	}

	@Override
	public OsDetailsDO fetchByOsDetailsId(OsDetailsDO osDetailsDO)
			throws DALException {
		OsDetailsDO thisOsDetailsDO = null;
		try {
			thisOsDetailsDO = (OsDetailsDO) getCriteria(OsDetailsDO.class).add(
					Restrictions
							.eq("osDetailsId", osDetailsDO.getOsDetailsId()))
					.uniqueResult();
		} catch (HibernateException e) {
			logger.debug("Error in OsDetailsDaoImpl class fetchByOsDetailsId : {}", e);
			throw new DALException(e.getMessage());
		}
		if (thisOsDetailsDO == null) {
			logger.debug("Error in OsDetailsDaoImpl class fetchByOsDetailsId : {}",
					 ErrorConstants.CO_DAL_ERR_079.getCode());
			throw new DALException(ErrorConstants.CO_DAL_ERR_079.getCode());
		} else {
			return thisOsDetailsDO;
		}
	}

	@Override
	public OsDetailsDO saveOrUpdate(OsDetailsDO osDetailsDO)
			throws DALException {
		OsDetailsDO thisOsDetailsDO = null;
		try {
			thisOsDetailsDO = excute(osDetailsDO,
					DataBaseOperationType.saveOrUpdate);
		} catch (Exception e) {
			logger.debug("Error in SaveOrUpdate Os Details {}",e);
			throw new DALException(e);
		}
		logger.debug("SavedOrUpdated Os Details {}",thisOsDetailsDO);
		return thisOsDetailsDO;
	}

	@Override
	public OsDetailsDO update(OsDetailsDO osDetailsDO) throws DALException {
		OsDetailsDO thisOsDetailsDO = null;
		try {
			thisOsDetailsDO = excute(osDetailsDO, DataBaseOperationType.update);
		} catch (Exception e) {
			logger.debug("Error in Updating Os Details {}",e);
			throw new DALException(e);
		}
		logger.debug("Updated Os Details {}",thisOsDetailsDO);
		return thisOsDetailsDO;
	}

	/*
	 * This method is used for fetching Osdetails by CloudId & providerId
	 * (non-Javadoc)
	 * @see com.cognizant.cloudone.dal.dao.OsDetailsDAO#fetchByCloudIdProviderId(com.cognizant.cloudone.dal.dbo.OsDetailsDO)
	 * @param OsDetailsDO
	 * @return List<OsDetailsDO>
	 * @throws DALException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<OsDetailsDO> fetchByCloudIdProviderId(OsDetailsDO osDetailsDO)
			throws DALException {
		List<OsDetailsDO> lstOsDetailsDO = null;
		try {
			lstOsDetailsDO = (List<OsDetailsDO>) getCriteria(OsDetailsDO.class)
					.add(
							Restrictions.eq("cloudMasterDO", osDetailsDO
									.getCloudMasterDO())).add(
							Restrictions.eq("providerMasterDO", osDetailsDO
									.getProviderMasterDO())).add(
							Restrictions.eq("active", true)).list();
		} catch (HibernateException e) {
			logger
					.debug("Error in OsDetailsDaoImpl class fetchByCloudIdProviderId : {}",e);
			throw new DALException(e.getMessage());
		}
		if (lstOsDetailsDO == null) {
			logger
					.debug("Error in OsDetailsDaoImpl class fetchByCloudIdProviderId : {}",
							 ErrorConstants.CO_DAL_ERR_079.getCode());
			throw new DALException(ErrorConstants.CO_DAL_ERR_079.getCode());
		} else {
			return lstOsDetailsDO;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OsDetailsDO> fetchByProviderIdOsId(OsDetailsDO osDetailsDO)
			throws DALException {
		List<OsDetailsDO> lstOsDetailsDO = null;
		try {
			lstOsDetailsDO = (List<OsDetailsDO>) getCriteria(OsDetailsDO.class)
					.add(Restrictions.eq("providerMasterDO",
							osDetailsDO.getProviderMasterDO()))
					.add(Restrictions.eq("osId", osDetailsDO.getOsId()))
					.add(Restrictions.eq("active", true)).list();
		} catch (HibernateException e) {
			logger.debug("Error in OsDetailsDaoImpl class fetchByProviderIdOsId : {}",e);
			throw new DALException(e.getMessage());
		}
		if (lstOsDetailsDO == null) {
			logger.debug("Error in OsDetailsDaoImpl class fetchByProviderIdOsId : {}",
					 ErrorConstants.CO_DAL_ERR_079.getCode());
			throw new DALException(ErrorConstants.CO_DAL_ERR_079.getCode());
		} else {
			return lstOsDetailsDO;
		}
	}

	@Override
	public List<OsDetailsDO> fetchByCloudIdProviderIdDatacenterId(OsDetailsDO osDetailsDO) throws DALException {
		List<OsDetailsDO> lstOsDetailsDO = null;
		try {
			lstOsDetailsDO = (List<OsDetailsDO>) getCriteria(OsDetailsDO.class)
					.add(
							Restrictions.eq("cloudMasterDO", osDetailsDO
									.getCloudMasterDO())).add(
							Restrictions.eq("providerMasterDO", osDetailsDO
									.getProviderMasterDO())).add(
							Restrictions.eq("dataCenterDetailsDO", osDetailsDO
									.getDataCenterDetailsDO())).add(				
							Restrictions.eq("active", true)).list();
		} catch (HibernateException e) {
			logger
					.debug("Error in OsDetailsDaoImpl class fetchByCloudIdProviderIdDatacenterId : {}",e);
			throw new DALException(e.getMessage());
		}
		if (lstOsDetailsDO == null) {
			logger
					.debug("Error in OsDetailsDaoImpl class fetchByCloudIdProviderIdDatacenterId : {}",
							 ErrorConstants.CO_DAL_ERR_079.getCode());
			throw new DALException(ErrorConstants.CO_DAL_ERR_079.getCode());
		} 
		return lstOsDetailsDO;
	}

}
