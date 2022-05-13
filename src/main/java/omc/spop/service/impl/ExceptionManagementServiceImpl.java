package omc.spop.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.ExceptionManagementDao;
import omc.spop.model.DbCheckConfig;
import omc.spop.model.DbCheckException;
import omc.spop.model.DbCheckExceptionHeadTitle;
import omc.spop.service.ExceptionManagementService;

@Service("ExceptionManagementService")
public class ExceptionManagementServiceImpl implements ExceptionManagementService {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionManagementServiceImpl.class);

	@Autowired
	private ExceptionManagementDao exceptionManagementDao;

	@Override
	public List<DbCheckConfig> getCheckItemList(DbCheckConfig dbCheckConfig) throws Exception {
		return exceptionManagementDao.getCheckItemList(dbCheckConfig);
	}

	@Override
	public DbCheckExceptionHeadTitle getDbCheckExceptionHeadTitleList(
			DbCheckExceptionHeadTitle dbCheckExceptionHeadTitle) throws Exception {
		return exceptionManagementDao.getDbCheckExceptionHeadTitleList(dbCheckExceptionHeadTitle);
	}

	@Override
	public List<DbCheckException> getDbCheckExceptionList(DbCheckException dbCheckException) throws Exception {
		return exceptionManagementDao.getDbCheckExceptionList(dbCheckException);
	}

	@Override
	public int updateExceptDelYn(DbCheckException dbCheckException) throws Exception {
		String db_check_exception_no= dbCheckException.getDb_check_exception_no();
		logger.debug("db_check_exception_no :"+db_check_exception_no);
		String db_check_exception_no_array[] = db_check_exception_no.split("\\|");
		int cnt = 0;
		for(String s : db_check_exception_no_array) {
			logger.debug("db_check_exception_no :"+s);
			dbCheckException.setDb_check_exception_no(s);
			cnt += exceptionManagementDao.updateExceptDelYn(dbCheckException);
		}
		return cnt;
	}

	@Override
	public List<LinkedHashMap<String, Object>> getDbCheckExceptionList4Excel(DbCheckException dbCheckException) {
		return exceptionManagementDao.getDbCheckExceptionList4Excel(dbCheckException);
	}

	@Override
	public int registException(DbCheckException dbCheckException) throws Exception {
		String check_except_object= dbCheckException.getCheck_except_object();
		logger.debug("check_except_object :"+check_except_object);
		// bar | means rows
		String check_except_object_array[] = check_except_object.split("\\|");
		
		logger.debug("user_id :"+dbCheckException.getUser_id());
		logger.debug("check_pref_id :"+dbCheckException.getCheck_pref_id());
		logger.debug("dbid :"+dbCheckException.getDbid());


		StringBuilder insertColumnSb = null;
		StringBuilder insertValueSb = null;
		StringBuilder searchConditionSb = null;
		int cnt=0;
		for (int i = 0; i < check_except_object_array.length; i++) {
			insertColumnSb = new StringBuilder();
			insertValueSb = new StringBuilder();
			searchConditionSb = new StringBuilder();

			logger.debug("check_except_object_array["+i+"] :"+check_except_object_array[i]);
			String check_except_object_value_array[] = check_except_object_array[i].split("\\^");
			logger.debug("check_except_object_value_array.length :"+check_except_object_value_array.length);
			
			for(int j=0;j<check_except_object_value_array.length;j++) {
				if(j != 0) {
					insertColumnSb.append(",");
					insertValueSb.append(",");
				}
				insertColumnSb.append("CHECK_EXCEPT_OBJECT_NAME_"+(j+1));
				insertValueSb.append("'"+StringUtils.defaultString(check_except_object_value_array[j])+"'");
				searchConditionSb.append(" AND CHECK_EXCEPT_OBJECT_NAME_"+(j+1)+"="+ "'"+StringUtils.defaultString(check_except_object_value_array[j])+"'");
			}
			logger.debug("insertColumnSb.toString() :"+insertColumnSb.toString());
			logger.debug("insertValueSb.toString() :"+insertValueSb.toString());
			logger.debug("searchConditionSb.toString() :"+searchConditionSb.toString());
			dbCheckException.setCheck_except_object_name(insertColumnSb.toString());
			dbCheckException.setCheck_except_object_value(insertValueSb.toString());
			//중복체크
			dbCheckException.setSearch_condition(searchConditionSb.toString());
			int checkResult = 0;
			checkResult = exceptionManagementDao.checkExceptionDup(dbCheckException);
			logger.debug("checkResult ===>"+checkResult);
			if(checkResult <= 0) {
				cnt += exceptionManagementDao.registException(dbCheckException);
			}
		}

		return cnt;
	}

}