package omc.spop.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import omc.spop.dao.TransactionTestDao;
import omc.spop.model.Result;
import omc.spop.service.TransactionTestService;

/***********************************************************
 * 2019.04.30 홍길동 최초작성
 **********************************************************/
@Service("TransactionTestService")
@Transactional
public class TransactionTestServiceImpl implements TransactionTestService {

	private static final Logger logger = LoggerFactory.getLogger(TransactionTestServiceImpl.class);

	@Autowired
	private TransactionTestDao transactionTestDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Result transactionTest() throws Exception {
		Result result = new Result();
		String menuId = transactionTestDao.getNextMenuId();
		logger.debug("menuId :" + menuId);
		int insert1 = transactionTestDao.insertMenu();
		logger.debug("insert1 :" + insert1);
		int insert2 = transactionTestDao.insertMyMenu();
		logger.debug("insert2 :" + insert2);
		result.setResult(true);
		result.setMessage("success");
		return result;
	}

}