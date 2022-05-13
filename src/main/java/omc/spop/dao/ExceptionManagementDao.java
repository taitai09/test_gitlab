package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.DbCheckConfig;
import omc.spop.model.DbCheckException;
import omc.spop.model.DbCheckExceptionHeadTitle;

/***********************************************************
 * 2017.08.10 이원식 최초작성
 **********************************************************/

public interface ExceptionManagementDao {

	public List<DbCheckConfig> getCheckItemList(DbCheckConfig dbCheckConfig);

	public DbCheckExceptionHeadTitle getDbCheckExceptionHeadTitleList(
			DbCheckExceptionHeadTitle dbCheckExceptionHeadTitle);

	public List<DbCheckException> getDbCheckExceptionList(DbCheckException dbCheckException);

	public int updateExceptDelYn(DbCheckException dbCheckException);

	public List<LinkedHashMap<String, Object>> getDbCheckExceptionList4Excel(DbCheckException dbCheckException);

	public int registException(DbCheckException dbCheckException);

	public int checkExceptionDup(DbCheckException dbCheckException);

}
