package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import omc.spop.base.SessionManager;
import omc.spop.dao.PreDeploymentCheckDao;
import omc.spop.model.BeforeAccidentCheck;
import omc.spop.model.DeployPerfCheck;
import omc.spop.server.tune.DeployPerfCheckAppl;
import omc.spop.server.tune.DeployPerfCheckDbio;
import omc.spop.service.PreDeploymentCheckService;
import omc.spop.utils.ExcelRead;
import omc.spop.utils.ExcelReadOption;
import omc.spop.utils.FileMngUtil;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.03.14	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Service("PreDeploymentCheckService")
public class PreDeploymentCheckServiceImpl implements PreDeploymentCheckService {
	
	private static final Logger logger = LoggerFactory.getLogger(PreDeploymentCheckServiceImpl.class);
	
	@Autowired
	private PreDeploymentCheckDao preDeploymentCheckDao;

	
	@Override
	public List<BeforeAccidentCheck> sourceCheckList(BeforeAccidentCheck beforeAccidentCheck) throws Exception {
		return preDeploymentCheckDao.sourceCheckList(beforeAccidentCheck);
	}
	
	@Override
	public BeforeAccidentCheck sourceCheckView(BeforeAccidentCheck beforeAccidentCheck) throws Exception {
		return preDeploymentCheckDao.sourceCheckView(beforeAccidentCheck);
	}
	
	@Override
	public void updateSourceCheck(BeforeAccidentCheck beforeAccidentCheck) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		beforeAccidentCheck.setBfac_chkr_id(user_id);
		
		preDeploymentCheckDao.updateSourceCheck(beforeAccidentCheck);
	}	
	
	@Override
	public List<DeployPerfCheck> applicationCheckList(DeployPerfCheck deployPerfCheck) throws Exception {
		return preDeploymentCheckDao.applicationCheckList(deployPerfCheck);
	}
	/**
	 * 성능개선 > 배포전점검 > 애플리케이션 성능점검
	 */	
	public void applicationRequestCheck_20180710(DeployPerfCheck deployPerfCheck) throws Exception {
		try{
			DeployPerfCheckAppl.start_perfCheck_APPL(StringUtil.parseLong(deployPerfCheck.getDeploy_perf_check_no(),0), deployPerfCheck.getWrkjob_cd(), "1");
		}catch(Exception ex){
			logger.error("SERVER(start_perfCheck_APPL) ERROR => " + ex.getMessage());
			throw ex;
		}
	}
	/**
	 * 성능개선 > 배포전점검 > 애플리케이션 성능점검
	 */
	@Override
	public void applicationRequestCheck(DeployPerfCheck deployPerfCheck) throws Exception {
		preDeploymentCheckDao.applicationRequestCheck(deployPerfCheck);
	}
	
	@Override
	public List<DeployPerfCheck> applicationDetailCheckList(DeployPerfCheck deployPerfCheck) throws Exception {
		return preDeploymentCheckDao.applicationDetailCheckList(deployPerfCheck);
	}
	
	@Override
	public List<DeployPerfCheck> applicationDBIOCheckList(DeployPerfCheck deployPerfCheck) throws Exception {
		return preDeploymentCheckDao.applicationDBIOCheckList(deployPerfCheck);
	}

	@Override
	public List<DeployPerfCheck> dbioCheckList(DeployPerfCheck deployPerfCheck) throws Exception {
		return preDeploymentCheckDao.dbioCheckList(deployPerfCheck);
	}
	/**
	 * 성능개선 > 배포전점검 > DBIO 성능점검
	 */	
	public void dbioRequestCheck_20180710(DeployPerfCheck deployPerfCheck) throws Exception {
		try{
			DeployPerfCheckDbio.start_perfCheck_DBIO(StringUtil.parseLong(deployPerfCheck.getDeploy_perf_check_no(),0), deployPerfCheck.getWrkjob_cd(), "2");
		}catch(Exception ex){
			logger.error("SERVER(start_perfCheck_DBIO) ERROR => " + ex.getMessage());
			throw ex;
		}
	}
	/**
	 * 성능개선 > 배포전점검 > DBIO 성능점검
	 */	
	@Override
	public void dbioRequestCheck(DeployPerfCheck deployPerfCheck) throws Exception {
		preDeploymentCheckDao.dbioRequestCheck(deployPerfCheck);
	}
	
	@Override
	public List<DeployPerfCheck> dbioDetailCheckList(DeployPerfCheck deployPerfCheck) throws Exception {
		return preDeploymentCheckDao.dbioDetailCheckList(deployPerfCheck);
	}
	
	@Override
	public List<DeployPerfCheck> uploadExcelPerformanceFile(MultipartFile file) throws Exception {
		FileMngUtil fileMng = new FileMngUtil();
		ExcelReadOption option = new ExcelReadOption();
		List<DeployPerfCheck> resultList = new ArrayList<DeployPerfCheck>();
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
		
		try{
			List<Map<String, String>> excelContent = ExcelRead.read(option);
			
			DeployPerfCheck temp = null;
			for(Map<String, String> article : excelContent){
				temp = new DeployPerfCheck();
				temp.setTr_cd(article.get("A"));

				resultList.add(temp);
			}			
		}catch (Exception ex) {
			logger.error("엑셀 파일 조회 error => " + ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}

		return resultList;
	}
	
	@Override
	public int insertExcelPerformanceCheck(HttpServletRequest req) throws Exception {
		DeployPerfCheck deployPerfCheck = new DeployPerfCheck();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		int insertRow = 0;
		String wrkjob_cd = StringUtil.nvl(req.getParameter("wrkjob_cd"));
		String deploy_perf_check_type_cd = StringUtil.nvl(req.getParameter("deploy_perf_check_type_cd"));
		String deploy_day = StringUtil.nvl(req.getParameter("deploy_day"));
		String[] uploadFlagArr = req.getParameterValues("upload_flag");
		String[] trCdArr = req.getParameterValues("tr_cd");

		
		if(uploadFlagArr != null){
			deployPerfCheck.setWrkjob_cd(wrkjob_cd);
			deployPerfCheck.setDeploy_day(deploy_day);
			deployPerfCheck.setDeploy_perf_check_type_cd(deploy_perf_check_type_cd);
			deployPerfCheck.setReg_id(user_id);
			
			// 1. DEPLOY_PERF_CHECK INSERT
			insertRow = preDeploymentCheckDao.insertDeployPerfCheck(deployPerfCheck);
			
			for(int i = 0; i < uploadFlagArr.length ; i++){
				DeployPerfCheck temp = new DeployPerfCheck();
				
				temp.setWrkjob_cd(wrkjob_cd);
				temp.setTr_cd(trCdArr[i]);
				
				if(deploy_perf_check_type_cd.equals("1")){ // 성능점검유형이 "애플리케이션 = '1'"
					// 2. DEPLOY_APP_PERF_STAT INSERT
					preDeploymentCheckDao.insertDeployAppPerfStat(temp);					
				}else{ // 성능점검유형이 "DBIO = '2'"
					// 2. DEPLOY_APP_PERF_STAT INSERT
					preDeploymentCheckDao.insertDeployDBIOPerfStat(temp);
				}
			}
		}
		
		return insertRow;
	}
}