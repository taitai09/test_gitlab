package omc.spop.dao;

import java.util.List;

import omc.spop.model.Cd;
import omc.spop.model.GrpCd;

/***********************************************************
 * 2017.11.10	이원식	최초작성
 * 2018.04.09	이원식	OPENPOP V2 최초작업  (오픈메이드 관리자 메뉴로 분리) 
 **********************************************************/

public interface CodeMngDao {	
	public List<GrpCd> codeGroupList(GrpCd grpCd);
	
	public int saveCodeGroup(GrpCd grpCd);
	
	public List<Cd> codeList(Cd cd);
	
	public int saveCode(Cd cd);
}
