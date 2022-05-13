package omc.spop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.TestDao;
import omc.spop.service.TestService;

/***********************************************************
 * 2018.03.23 이원식 OPENPOP V2 최초작업
 **********************************************************/

@Service("testService")
public class TestServiceImpl implements TestService {
	@Autowired
	private TestDao testDao;

	@Override
	public int urlTest() throws Exception {
		return testDao.urlTest();
	}
}
