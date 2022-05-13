package omc.spop.service;

import java.util.List;

import omc.spop.model.Cd;
import omc.spop.model.GrpCd;

/***********************************************************
 * 2017.11.10	이원식	최초작성
 * 2018.04.09	이원식	OPENPOP V2 최초작업  (오픈메이드 관리자 메뉴로 분리) 
 **********************************************************/

public interface CodeMngService {
	/** Code Group 리스트 */
	List<GrpCd> codeGroupList(GrpCd grpCd) throws Exception;
	
	/** Code Group save */
	int saveCodeGroup(GrpCd grpCd) throws Exception;
	
	/** Code 리스트 */
	List<Cd> codeList(Cd cd) throws Exception;
	
	/** Code save */
	void saveCode(Cd cd) throws Exception;	
}
