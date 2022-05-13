package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.TrCd;

/***********************************************************
 * 2018.11.13	임호경	최초작성
 **********************************************************/

public interface ApplicationMngDao {

	List<TrCd> applicationCodeList(TrCd trCd);

	int updateApplicationCode(TrCd trCd);

	int insertApplicationCode(TrCd trCd);

	void deleteApplicationCode(TrCd trCd);

	int checkApplicationCode(TrCd trCd);

	int saveApplicationCodeByExcelUpload(TrCd trCd);

	List<LinkedHashMap<String, Object>> applicationCodeListByExcelDown(TrCd trCd);

	int checkWrkjobCd(TrCd trCd);

	int checkMgrId(TrCd trCd);

}
