package omc.spop.service;

import org.springframework.transaction.annotation.Transactional;

import omc.spop.model.Result;

/***********************************************************
 * 2019.04.30 홍길동 최초작성
 **********************************************************/
@Transactional
public interface TransactionTestService {
	@Transactional
	Result transactionTest() throws Exception;

}
