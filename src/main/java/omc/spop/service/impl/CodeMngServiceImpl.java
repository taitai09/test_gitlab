package omc.spop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.CodeMngDao;
import omc.spop.model.Cd;
import omc.spop.model.GrpCd;
import omc.spop.service.CodeMngService;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.11.10	이원식	최초작성
 * 2018.04.09	이원식	OPENPOP V2 최초작업  (오픈메이드 관리자 메뉴로 분리) 
 **********************************************************/

@Service("CodeMngService")
public class CodeMngServiceImpl implements CodeMngService {
	@Autowired
	private CodeMngDao codeMngDao;

	@Override
	public List<GrpCd> codeGroupList(GrpCd grpCd) throws Exception {
		return codeMngDao.codeGroupList(grpCd);
	}
	
	@Override
	public int saveCodeGroup(GrpCd grpCd) throws Exception {
		return codeMngDao.saveCodeGroup(grpCd);
	}
	
	@Override
	public List<Cd> codeList(Cd cd) throws Exception {
		return codeMngDao.codeList(cd);
	}
	
	@Override
	public void saveCode(Cd cd) throws Exception {
		String[] cdArry = StringUtil.split(cd.getCdArry(), "|");
		String[] cdNmArry = StringUtil.split(cd.getCdNmArry(), "|");
		String[] cdDescArry = StringUtil.split(cd.getCdDescArry(), "|");
		String[] cdRefVl1Arry = StringUtil.split(cd.getCdRefVl1Arry(), "|");
		String[] cdRefVl2Arry = StringUtil.split(cd.getCdRefVl2Arry(), "|");
		String[] cdRefVl3Arry = StringUtil.split(cd.getCdRefVl3Arry(), "|");
		String[] orderedArry = StringUtil.split(cd.getOrderedArry(), "|");
		String[] useYnArry = StringUtil.split(cd.getUseYnArry(), "|");
		
		if(cd.getCdArry().trim().length() > 1){				
			for(int i = 0 ; i < cdArry.length ; i++){
				Cd temp = new Cd();
				temp.setGrp_cd_id(cd.getGrp_cd_id());
				temp.setCd(cdArry[i]);
				temp.setCd_nm(cdNmArry[i]);
				temp.setCd_desc(cdDescArry[i]);
				temp.setRef_vl_1(cdRefVl1Arry[i]);
				temp.setRef_vl_2(cdRefVl2Arry[i]);
				temp.setRef_vl_3(cdRefVl3Arry[i]);
				temp.setOrdered(orderedArry[i]);
				temp.setUse_yn(useYnArry[i]);
				
				codeMngDao.saveCode(temp);
			}
		}
	}
}