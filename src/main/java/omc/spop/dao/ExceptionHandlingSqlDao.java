package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.ExceptionHandlingSql;

/***********************************************************
 * 2020.05.12 	명성태 	최초작성
 **********************************************************/

public interface ExceptionHandlingSqlDao {
	public List<ExceptionHandlingSql> exceptionHandlingMethod(ExceptionHandlingSql exceptionHandlingSql);
	
	public List<ExceptionHandlingSql> loadExceptionHandlingSql(ExceptionHandlingSql exceptionHandlingSql);
	
	public List<LinkedHashMap<String, Object>> loadExceptionHandlingSqlExcelDown(ExceptionHandlingSql exceptionHandlingSql);
}
