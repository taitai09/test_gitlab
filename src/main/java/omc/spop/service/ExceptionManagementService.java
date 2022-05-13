package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.DbCheckConfig;
import omc.spop.model.DbCheckException;
import omc.spop.model.DbCheckExceptionHeadTitle;

/***********************************************************
 * 2019.04.30 홍길동 최초작성
 **********************************************************/
public interface ExceptionManagementService {

	List<DbCheckConfig> getCheckItemList(DbCheckConfig dbCheckConfig) throws Exception;

	DbCheckExceptionHeadTitle getDbCheckExceptionHeadTitleList(DbCheckExceptionHeadTitle dbCheckExceptionHeadTitle)
			throws Exception;

	public List<DbCheckException> getDbCheckExceptionList(DbCheckException dbCheckException) throws Exception;

	public int updateExceptDelYn(DbCheckException dbCheckException) throws Exception;

	List<LinkedHashMap<String, Object>> getDbCheckExceptionList4Excel(DbCheckException dbCheckException)
			throws Exception;

	int registException(DbCheckException dbCheckException) throws Exception;

}
