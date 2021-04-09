package com.cognizant.cloudone.dal.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.cognizant.cloudone.dal.dao.DataCenterDetailsDAO;
import com.cognizant.cloudone.dal.dbo.DataCenterDetailsDO;
import com.cognizant.cloudone.kernel.error.constants.ErrorConstants;
import com.cognizant.cloudone.kernel.exception.DALException;
import com.cognizant.cloudone.kernel.logger.CloudOneLogger;

public class DataCenterDetailsDaoImpl extends BatchSupportDaoImpl implements
		DataCenterDetailsDAO {

	private final CloudOneLogger logger = CloudOneLogger
			.getLogger(DataCenterDetailsDaoImpl.class.getName());

	@Override
	public DataCenterDetailsDO create(DataCenterDetailsDO dataCenterDO)
			throws DALException {
		DataCenterDetailsDO thisDataCenterDO = null;
		try {
			thisDataCenterDO = excute(dataCenterDO,
					DataBaseOperationType.create);
		} catch (Exception e) {
			logger.debug("Error in Creating Data Center : {}", e);
			throw new DALException(e);
		}
		return thisDataCenterDO;
	}

	@Override
	public int delete(DataCenterDetailsDO dataCenterDO) throws DALException {
		@SuppressWarnings("unused")
		DataCenterDetailsDO thisDataCenterDO = null;
		try {
			thisDataCenterDO = excute(dataCenterDO,
					DataBaseOperationType.delete);
		} catch (Exception e) {
			logger.debug("Error in Deleting Data Center : {}", e);
			throw new DALException(e);
		}
		return 1;
	}

	@Override
	public DataCenterDetailsDO fetchByDataCenterId(
			DataCenterDetailsDO dataCenterDO) throws DALException {
		DataCenterDetailsDO thisDataCenterDO = null;
		try {
			thisDataCenterDO = (DataCenterDetailsDO) getCriteria(
					DataCenterDetailsDO.class).add(
					Restrictions.eq("dataCenterId", dataCenterDO
							.getDataCenterId())).uniqueResult();
		} catch (HibernateException e) {
			logger
					.debug("Error class DataCenterDaoImpl in fetchByDataCenterId : {}",e);
			throw new DALException(e.getMessage());
		}
		if (thisDataCenterDO == null) {
			logger
					.debug("Error class DataCenterDaoImpl in fetchByDataCenterId : {}",
							ErrorConstants.CO_DAL_ERR_015.getCode());
			throw new DALException(ErrorConstants.CO_DAL_ERR_015.getCode());
		} else {
			return thisDataCenterDO;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataCenterDetailsDO> fetchByCloudId(
			DataCenterDetailsDO dataCenterDO) throws DALException {
		List<DataCenterDetailsDO> lstDataCenterDO = null;
		try {
			lstDataCenterDO = (List<DataCenterDetailsDO>) getCriteria(
					DataCenterDetailsDO.class).add(
					Restrictions.eq("cloudMasterDO", dataCenterDO
							.getCloudMasterDO())).list();
		} catch (HibernateException e) {
			logger.debug("Error class DataCenterDaoImpl in fetchByCloudId : {}",e);
			throw new DALException(e.getMessage());
		}
		if (lstDataCenterDO == null) {
			logger.debug("Error class DataCenterDaoImpl in fetchByCloudId : {}",
					 ErrorConstants.CO_DAL_ERR_015.getCode());
			throw new DALException(ErrorConstants.CO_DAL_ERR_015.getCode());
		} else {
			return lstDataCenterDO;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DataCenterDetailsDO> fetchByCloudIdProviderId(
			DataCenterDetailsDO dataCenterDO) throws DALException {
		List<DataCenterDetailsDO> lstDataCenterDO = null;
		try {
			lstDataCenterDO = (List<DataCenterDetailsDO>) getCriteria(
					DataCenterDetailsDO.class).add(
					Restrictions.eq("cloudMasterDO", dataCenterDO
							.getCloudMasterDO())).add(
					Restrictions.eq("providerMasterDO", dataCenterDO.getProviderMasterDO())).addOrder(Order.asc("location")).list();
		} catch (HibernateException e) {
			logger.debug("Error class DataCenterDaoImpl in fetchByCloudIdProviderId : {}",e);
			throw new DALException(e.getMessage());
		}
		if (lstDataCenterDO == null) {
			logger.debug("Error class DataCenterDaoImpl in fetchByCloudIdProviderId : {}",
					 ErrorConstants.CO_DAL_ERR_015.getCode());
			throw new DALException(ErrorConstants.CO_DAL_ERR_015.getCode());
		} else {
			return lstDataCenterDO;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DataCenterDetailsDO> fetchActiveDcsByCloudIdProviderId(
			DataCenterDetailsDO dataCenterDO) throws DALException {
		List<DataCenterDetailsDO> lstDataCenterDO = null;
		try {
			lstDataCenterDO = (List<DataCenterDetailsDO>) getCriteria(
					DataCenterDetailsDO.class)
					.add(Restrictions.eq("cloudMasterDO",
							dataCenterDO.getCloudMasterDO()))
					.add(Restrictions.eq("providerMasterDO",
							dataCenterDO.getProviderMasterDO()))
					.addOrder(Order.asc("location"))
					.add(Restrictions.eq("active", true)).list();
		} catch (HibernateException e) {
			logger.debug(
					"Error class DataCenterDaoImpl in fetchActiveDcsByCloudIdProviderId : {}",
					e);
			throw new DALException(e.getMessage());
		}
		if (lstDataCenterDO == null) {
			logger.debug(
					"Error class DataCenterDaoImpl in fetchActiveDcsByCloudIdProviderId : {}",
					ErrorConstants.CO_DAL_ERR_015.getCode());
			throw new DALException(ErrorConstants.CO_DAL_ERR_015.getCode());
		} else {
			return lstDataCenterDO;
		}
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<DataCenterDetailsDO> fetchByCloudIdProviderIdNotEqualsProfile(
			DataCenterDetailsDO dataCenterDO) throws DALException {
		List<DataCenterDetailsDO> lstDataCenterDO = null;
		try {
			lstDataCenterDO = (List<DataCenterDetailsDO>) getCriteria(
					DataCenterDetailsDO.class).add(
					Restrictions.eq("cloudMasterDO", dataCenterDO
							.getCloudMasterDO())).add(
					Restrictions.eq("providerMasterDO", dataCenterDO.getProviderMasterDO())).add(
							Restrictions.ne("profile", dataCenterDO
									.getProfile())).list();
		} catch (HibernateException e) {
			logger.debug("Error class DataCenterDaoImpl in fetchByCloudIdProviderId : {}",e);
			throw new DALException(e.getMessage());
		}
		if (lstDataCenterDO == null) {
			logger.debug("Error class DataCenterDaoImpl in fetchByCloudIdProviderId : {}",
					 ErrorConstants.CO_DAL_ERR_015.getCode());
			throw new DALException(ErrorConstants.CO_DAL_ERR_015.getCode());
		} else {
			return lstDataCenterDO;
		}
	}

	

	@Override
	public DataCenterDetailsDO update(DataCenterDetailsDO dataCenterDO)
			throws DALException {
		DataCenterDetailsDO thisDataCenterDO = null;
		try {
			thisDataCenterDO = excute(dataCenterDO,
					DataBaseOperationType.update);
		} catch (Exception e) {
			logger.debug("Error in updating Data Center : {}", e);
			throw new DALException(e);
		}
		return thisDataCenterDO;
	}

	@Override
	public DataCenterDetailsDO fetchByCloudIDDataCenterName(
			DataCenterDetailsDO dataCenterDO) throws DALException {
		DataCenterDetailsDO thisDataCenterDO = null;
		try {
			thisDataCenterDO = (DataCenterDetailsDO) getCriteria(
					DataCenterDetailsDO.class).add(
					Restrictions.eq("cloudMasterDO", dataCenterDO
							.getCloudMasterDO())).add(
					Restrictions.eq("dataCenterName", dataCenterDO
							.getDataCenterName())).uniqueResult();
		} catch (HibernateException e) {
			logger
					.debug("Error class DataCenterDaoImpl in fetchByCloudIDDataCenterName : {}",e);
			throw new DALException(e.getMessage());
		}
		if (thisDataCenterDO == null) {
			logger
					.debug("Error class DataCenterDaoImpl in fetchByCloudIDDataCenterName : {}",
							 ErrorConstants.CO_DAL_ERR_015.getCode());
			throw new DALException(ErrorConstants.CO_DAL_ERR_015.getCode());
		} else {
			logger.debug("In DataCenterDaoImpl class fetchByCloudIDDataCenterName : {}",thisDataCenterDO);
			return thisDataCenterDO;
		}
	}
	
	
	
	@Override
	public DataCenterDetailsDO fetchByCloudIDProviderIDDataCenterName(
			DataCenterDetailsDO dataCenterDO) throws DALException {
		DataCenterDetailsDO thisDataCenterDO = null;
		try {
			thisDataCenterDO = (DataCenterDetailsDO) getCriteria(
					DataCenterDetailsDO.class).add(
					Restrictions.eq("cloudMasterDO", dataCenterDO
							.getCloudMasterDO())).add(
					Restrictions.eq("providerMasterDO", dataCenterDO
							.getProviderMasterDO())).add(
					Restrictions.eq("dataCenterName", dataCenterDO
							.getDataCenterName())).add(
									Restrictions.eq("active", true)).uniqueResult();
		} catch (HibernateException e) {
			logger
					.debug("Error class DataCenterDaoImpl in fetchByCloudIDProviderIDDataCenterName : {}", e);
			throw new DALException(e.getMessage());
		}
		if (thisDataCenterDO == null) {
			logger
					.debug("Error class DataCenterDaoImpl in fetchByCloudIDProviderIDDataCenterName : {}",
							 ErrorConstants.CO_DAL_ERR_015.getCode());
			throw new DALException(ErrorConstants.CO_DAL_ERR_015.getCode());
		} else {
			return thisDataCenterDO;
		}
	}

	/**
	 * This method is used for fetching DataCenterDO based on ProviderID MorID
	 * and DataCenterName
	 * 
	 * @param dataCenterDO
	 * @return DataCenterDO
	 * @throws DALException
	 */
	public DataCenterDetailsDO fetchByCloudIDProviderIDMorId(
			DataCenterDetailsDO dataCenterDO) throws DALException
	{
		DataCenterDetailsDO thisDataCenterDO = null;
		try {
			thisDataCenterDO = (DataCenterDetailsDO) getCriteria(
					DataCenterDetailsDO.class).add(
					Restrictions.eq("cloudMasterDO", dataCenterDO
							.getCloudMasterDO())).add(
					Restrictions.eq("providerMasterDO", dataCenterDO
							.getProviderMasterDO())).add(
					Restrictions.eq("morId", dataCenterDO
							.getMorId())).add(
									Restrictions.eq("active", true)).uniqueResult();
		} catch (HibernateException e) {
			logger
					.debug("Error class DataCenterDaoImpl in fetchByCloudIDProviderIDMorId : {}", e);
			throw new DALException(e.getMessage());
		}
		if (thisDataCenterDO == null) {
			logger
					.debug("Error class DataCenterDaoImpl in fetchByCloudIDProviderIDMorId : {}",
							 ErrorConstants.CO_DAL_ERR_015.getCode());
			throw new DALException(ErrorConstants.CO_DAL_ERR_015.getCode());
		} else {
			logger.debug("In DataCenterDaoImpl class fetchByCloudIDProviderIDMorId : {}",thisDataCenterDO);
			return thisDataCenterDO;
		}
	}
	
	@Override
    public DataCenterDetailsDO fetchByCIDPIDLocationName(
                  DataCenterDetailsDO dataCenterDO) throws DALException {
           DataCenterDetailsDO thisDataCenterDO = null;
           try {
                  thisDataCenterDO = (DataCenterDetailsDO) getCriteria(
                               DataCenterDetailsDO.class).add(
                               Restrictions.eq("cloudMasterDO", dataCenterDO
                                             .getCloudMasterDO())).add(
                               Restrictions.eq("providerMasterDO", dataCenterDO
                                             .getProviderMasterDO())).add(
                               Restrictions.eq("location", dataCenterDO
                                             .getLocation())).add(
                                                           Restrictions.eq("active", true)).uniqueResult();
           } catch (HibernateException e) {
                  logger
                               .debug("Error class DataCenterDaoImpl in fetchByCIDPIDLocationName : {}", e);
                  throw new DALException(e.getMessage());
           }
           if (thisDataCenterDO == null) {
                  logger
                               .debug("Error class DataCenterDaoImpl in fetchByCIDPIDLocationName : {}",
                                             ErrorConstants.CO_DAL_ERR_015.getCode());
                  throw new DALException(ErrorConstants.CO_DAL_ERR_015.getCode());
           } else {
                  return thisDataCenterDO;
           }
    }

}
