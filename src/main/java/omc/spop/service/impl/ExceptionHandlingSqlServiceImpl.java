package omc.spop.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.ExceptionHandlingSqlDao;
import omc.spop.model.ExceptionHandlingSql;
import omc.spop.service.ExceptionHandlingSqlService;

/***********************************************************
 * 2020.05.13 	명성태 	최초작성
 **********************************************************/

@Service("exceptionHandlingSqlService")
public class ExceptionHandlingSqlServiceImpl implements ExceptionHandlingSqlService {
	private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlingSqlServiceImpl.class);
	
	@Autowired
	private ExceptionHandlingSqlDao exceptionHandlingSqlDao;

	@Override
	public List<ExceptionHandlingSql> exceptionHandlingMethod(ExceptionHandlingSql exceptionHandlingSql) {
		return exceptionHandlingSqlDao.exceptionHandlingMethod(exceptionHandlingSql);
	}
	
	@Override
	public List<ExceptionHandlingSql> loadExceptionHandlingSql(ExceptionHandlingSql exceptionHandlingSql) {
		return exceptionHandlingSqlDao.loadExceptionHandlingSql(exceptionHandlingSql);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> loadExceptionHandlingSqlExcelDown(ExceptionHandlingSql exceptionHandlingSql) throws Exception {
		return exceptionHandlingSqlDao.loadExceptionHandlingSqlExcelDown(exceptionHandlingSql);
	}
}
