package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import omc.spop.model.Result;
import omc.spop.model.TrCd;

/***********************************************************
 * 2018.11.13 임호경 최초작성 
 **********************************************************/

public interface ApplicationMngService {

	List<TrCd> applicationCodeList(TrCd trCd);

	int SaveApplicationCode(TrCd trCd);

	void deleteApplicationCode(TrCd trCd);

	Result uploadApplicationCodeExcelFile(MultipartFile file) throws Exception;

	List<LinkedHashMap<String, Object>> applicationCodeListByExcelDown(TrCd trCd);
	

	
}
