package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.ExceptionHandlingSql;

/***********************************************************
 * 2020.05.12 	명성태 	최초작성
 **********************************************************/

public interface ExceptionHandlingSqlService {
	List<ExceptionHandlingSql> exceptionHandlingMethod(ExceptionHandlingSql exceptionHandlingSql);
	
	List<ExceptionHandlingSql> loadExceptionHandlingSql(ExceptionHandlingSql exceptionHandlingSql);
	
	/* @throws Exception */
	List<LinkedHashMap<String, Object>> loadExceptionHandlingSqlExcelDown(ExceptionHandlingSql exceptionHandlingSql) throws Exception;
}
