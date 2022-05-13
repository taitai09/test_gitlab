package omc.spop.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.maven.artifact.ant.shaded.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import omc.spop.dao.ApplicationMngDao;
import omc.spop.model.Result;
import omc.spop.model.TrCd;
import omc.spop.service.ApplicationMngService;
import omc.spop.utils.ExcelRead;
import omc.spop.utils.ExcelReadOption;
import omc.spop.utils.FileMngUtil;

/***********************************************************
 * 2018.11.13	임호경	최초작성
 **********************************************************/

@Service("ApplicationMngService")
public class ApplicationMngServiceImpl implements ApplicationMngService {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationMngServiceImpl.class);

	@Autowired
	private ApplicationMngDao applicationMngDao;

	@Override
	public List<TrCd> applicationCodeList(TrCd trCd) {
		return applicationMngDao.applicationCodeList(trCd);
	}

	@Override
	public int SaveApplicationCode(TrCd trCd) {
		int check = 0;

		if(trCd.getCrud_flag().equals("C")){
			check = applicationMngDao.checkApplicationCode(trCd);
			if(check > 0) return -1; //중복
			check = 0;
			trCd.setMgr_id(trCd.getUser_id());
			check += applicationMngDao.insertApplicationCode(trCd);		
		}else{
			check = 0;
			check += applicationMngDao.updateApplicationCode(trCd);		
		}
			return check;
	}

	@Override
	public void deleteApplicationCode(TrCd trCd) {
		applicationMngDao.deleteApplicationCode(trCd);		
	}

	@Override
	public Result uploadApplicationCodeExcelFile(MultipartFile file) throws Exception {
		FileMngUtil fileMng = new FileMngUtil();
		ExcelReadOption option = new ExcelReadOption();
		Result result = new Result();
		int resultCount = 0;
		String filePath = "";
		
		// 1. 엑셀 파일 업로드
		try {
			filePath = fileMng.uploadExcel(file);			
		}catch (Exception ex) {
			logger.error("엑셀 파일 업로드 error => " + ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}
		
		option.setFilePath(filePath);
		option.setStartRow(2);
		
		int errCnt = 0;
		int totalCnt = 0;
		Map<String, Object> errObj = new HashMap<String, Object>();  //err 정보를 담을 obj
		Vector<String> errTrcd = new Vector<String>(); //err 유저를 담을 obj
		Vector<Integer> errIndexs = new Vector<Integer>(); //err 로우를 담을 obj
		int errIndex = 1; //엑셀의 몇번째 로우에서 에러나는지 알려주기위한 용도.
		

		try{
			List<Map<String, String>> excelContent = ExcelRead.read(option);
			totalCnt = excelContent.size();

			TrCd trCd = null;
			String tr_cd, wrkjob_cd, mgr_id = "";
			int trcdCheck, wrkjobcdCheck, useridCheck = 0;
			for(Map<String, String> article : excelContent){
				errIndex += 1;
				System.out.println("############## 엑셀 업로드 데이터 ##############");
		        System.out.print(article.get("A")+"\t");
		        System.out.print(article.get("B")+"\t");
		        System.out.print(article.get("C")+"\t");
		        System.out.println(article.get("D"));	
//		        System.out.print(article.get("D")+"\t");
//		        System.out.print(article.get("E")+"\t");
//		        System.out.println(article.get("F"));	

				trCd = new TrCd();
				wrkjob_cd = StringUtils.defaultString(article.get("A"));
				tr_cd = StringUtils.defaultString(article.get("B"));
				mgr_id = StringUtils.defaultString(article.get("D"));
				
				trCd.setWrkjob_cd(wrkjob_cd);
				trCd.setTr_cd(tr_cd);
				trCd.setUser_id(mgr_id);
				trcdCheck = applicationMngDao.checkApplicationCode(trCd);
				wrkjobcdCheck = applicationMngDao.checkWrkjobCd(trCd);
				useridCheck = applicationMngDao.checkMgrId(trCd);
				
				//중복시 아래로직을 실행안하고 에러 객체에 에러를 추가함.
				if(trcdCheck > 0 || wrkjobcdCheck == 0 || useridCheck == 0 ){
					errCnt += 1;
					errTrcd.add(trCd.getTr_cd());
					errIndexs.add(errIndex);
					System.out.println("실패 행 ::"+errIndex);
					continue;
				}
				
		        trCd.setWrkjob_cd(article.get("A"));
		        trCd.setTr_cd(article.get("B"));
		        trCd.setTr_cd_nm(article.get("C"));
		        trCd.setMgr_id(article.get("D"));
				
				resultCount += applicationMngDao.saveApplicationCodeByExcelUpload(trCd);
			}			
			
			errObj.put("totalCnt", totalCnt);
			errObj.put("isErr", errCnt > 0 ? true : false);
			errObj.put("errCnt", errCnt);
			errObj.put("errTrcd", errTrcd.toString());
			errObj.put("errIndex", errIndexs.toString());
			result.setObject(errObj);			
			result.setMessage("success");
			result.setResult(true);
			
		}catch (Exception ex) {
			logger.error("엑셀 파일 조회 error => " + ex.getMessage());
			ex.printStackTrace();
			errObj.put("totalCnt", totalCnt);
			errObj.put("isErr", true);
			errObj.put("errIndex",errIndexs.toString());
			errObj.put("errTrcd", errTrcd.toString());
			result.setObject(errObj);			
			result.setMessage("엑셀 업로드가 실패하였습니다. <BR/>총 [ "+totalCnt+" ] 건 중 [ "+errIndex+ " ] 번째 행에서 에러가 발생하였습니다.<BR/>"+ex.getMessage());
			result.setResult(false);
			throw ex;
		}

		return result;
	}

	@Override
	public List<LinkedHashMap<String, Object>> applicationCodeListByExcelDown(TrCd trCd) {
		return applicationMngDao.applicationCodeListByExcelDown(trCd);		
	}

}