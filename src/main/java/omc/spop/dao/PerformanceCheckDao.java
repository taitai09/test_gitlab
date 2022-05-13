package omc.spop.dao;

import java.util.List;

import omc.spop.model.BasicCheckConfig;

/***********************************************************
 * 2018.09.11 	임호경	 최초작성
 **********************************************************/

public interface PerformanceCheckDao {

	List<BasicCheckConfig> getPerformanceBasicCheckList(BasicCheckConfig basicCheckConfig);

	List<BasicCheckConfig> getPerformanceDBCheckList(BasicCheckConfig basicCheckConfig);

	List<BasicCheckConfig> getGradeCd(BasicCheckConfig basicCheckConfig);

	List<BasicCheckConfig> getClassDivCd(BasicCheckConfig basicCheckConfig);

	int SaveBasicCheckConfig(BasicCheckConfig basicCheckConfig);

	int SaveDBCheckConfig(BasicCheckConfig basicCheckConfig);

	List<BasicCheckConfig> getCheckPrefValue(BasicCheckConfig basicCheckConfig);

	int deleteDBCheckConfig(BasicCheckConfig basicCheckConfig);


}
