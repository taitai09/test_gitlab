package omc.spop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.PerformanceCheckDao;
import omc.spop.model.BasicCheckConfig;
import omc.spop.service.PerformanceCheckService;

/***********************************************************
 * 2018.09.11 	임호경	 최초작성
 **********************************************************/

@Service("performanceCheckService")
public class PerformanceCheckImpl implements PerformanceCheckService {

	
	@Autowired
	private PerformanceCheckDao performanceCheckDao;
	
	@Override
	public List<BasicCheckConfig> getPerformanceBasicCheckList(BasicCheckConfig basicCheckConfig) {
		return performanceCheckDao.getPerformanceBasicCheckList(basicCheckConfig);
	}

	@Override
	public List<BasicCheckConfig> getPerformanceDBCheckList(BasicCheckConfig basicCheckConfig) {
		return performanceCheckDao.getPerformanceDBCheckList(basicCheckConfig);
	}

	@Override
	public List<BasicCheckConfig> getGradeCd(BasicCheckConfig basicCheckConfig) {
		return performanceCheckDao.getGradeCd(basicCheckConfig);
	}

	@Override
	public List<BasicCheckConfig> getClassDivCd(BasicCheckConfig basicCheckConfig) {
		return performanceCheckDao.getClassDivCd(basicCheckConfig);
	}

	@Override
	public int SaveBasicCheckConfig(BasicCheckConfig basicCheckConfig) {
		return performanceCheckDao.SaveBasicCheckConfig(basicCheckConfig);
	}

	@Override
	public int SaveDBCheckConfig(BasicCheckConfig basicCheckConfig) {
		return performanceCheckDao.SaveDBCheckConfig(basicCheckConfig);
	}

	@Override
	public List<BasicCheckConfig> getCheckPrefValue(BasicCheckConfig basicCheckConfig) {
		return performanceCheckDao.getCheckPrefValue(basicCheckConfig);
	}

	@Override
	public int deleteDBCheckConfig(BasicCheckConfig basicCheckConfig) {
		return performanceCheckDao.deleteDBCheckConfig(basicCheckConfig);
	}

	


}
