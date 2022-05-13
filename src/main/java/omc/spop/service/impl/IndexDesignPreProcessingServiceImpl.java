package omc.spop.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import omc.spop.base.Config;
import omc.spop.dao.IndexDesignPreProcessingDao;
import omc.spop.model.AccPathExec;
import omc.spop.model.DbioExplainExec;
import omc.spop.model.DbioLoadFile;
import omc.spop.model.DbioLoadSql;
import omc.spop.model.ProjectUnitLoadFile;
import omc.spop.model.ProjectUnitLoadSql;
import omc.spop.model.SolutionProgramMng;
import omc.spop.model.VsqlGatheringModule;
import omc.spop.model.VsqlParsingSchema;
import omc.spop.model.VsqlSnapshot;
import omc.spop.server.tune.DbioAccPathPlan;
import omc.spop.service.IndexDesignPreProcessingService;
import omc.spop.utils.CryptoUtil;
import omc.spop.utils.ExcelDownHandler;
import omc.spop.utils.FileMngUtil;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.09.27	이원식	최초작성
 * 2018.02.21	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Service("IndexDesignPreProcessingService")
public class IndexDesignPreProcessingServiceImpl implements IndexDesignPreProcessingService {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexDesignPreProcessingServiceImpl.class);
	
	private String key = "openmade";
	
	@Autowired
	private IndexDesignPreProcessingDao indexDesignPreProcessingDao;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Override
	public List<VsqlParsingSchema> collectionTargetList(VsqlParsingSchema vsqlParsingSchema) throws Exception {
		return indexDesignPreProcessingDao.collectionTargetList(vsqlParsingSchema);
	}
	
	@Override
	public List<VsqlParsingSchema> applyTargetList(VsqlParsingSchema vsqlParsingSchema) throws Exception {
		return indexDesignPreProcessingDao.applyTargetList(vsqlParsingSchema);
	}
	
	@Override
	public VsqlParsingSchema glovalViewInfo(VsqlParsingSchema vsqlParsingSchema) throws Exception {
		return indexDesignPreProcessingDao.glovalViewInfo(vsqlParsingSchema);
	}
	
	@Override
	public List<VsqlGatheringModule> collectionModuleList(VsqlGatheringModule vsqlGatheringModule) throws Exception {
		return indexDesignPreProcessingDao.collectionModuleList(vsqlGatheringModule);
	}	
	
	@Override
	public void saveSetCollectionCondition(VsqlParsingSchema vsqlParsingSchema) throws Exception {
		String parsingSchemaNo = "";
		String gatheringModuleNo = "";
		String[] parsingSchemaArry = null;
		String[] moduleNameArry = null;
		
		// 1. VSQL_PARSING_SCHEMA MAX PARSING_SCHEMA_NO 조회
		parsingSchemaNo = indexDesignPreProcessingDao.getMaxParsingSchemaNo(vsqlParsingSchema);
		parsingSchemaArry = StringUtil.split(vsqlParsingSchema.getParsingSchemaArry(), "|");
		
		// 2. VSQL_PARSING_SCHEMA INSERT
		for (int i = 0; i < parsingSchemaArry.length; i++) {
			VsqlParsingSchema tempSchema = new VsqlParsingSchema();
			tempSchema.setDbid(vsqlParsingSchema.getDbid());
			tempSchema.setParsing_schema_no(parsingSchemaNo);
			tempSchema.setParsing_username(parsingSchemaArry[i]);
			tempSchema.setGlobal_view_yn(vsqlParsingSchema.getGlobal_view_yn());
			tempSchema.setInstance_number(vsqlParsingSchema.getInstance_number());
			
			indexDesignPreProcessingDao.insertParsingSchema(tempSchema);
		}
		
		// 3. VSQL_GATHERING_MODULE MAX GATHERING_MODULE_NO 조회
		gatheringModuleNo = indexDesignPreProcessingDao.getMaxGatheringModlueNo(vsqlParsingSchema);
		
		moduleNameArry = StringUtil.split(vsqlParsingSchema.getModuleNameArry(), "|");		
		// 4. VSQL_GATHERING_MODULE INSERT
		for (int i = 0; i < moduleNameArry.length; i++) {
			VsqlGatheringModule tempModule = new VsqlGatheringModule();
			tempModule.setDbid(vsqlParsingSchema.getDbid());
			tempModule.setGathering_module_no(gatheringModuleNo);
			tempModule.setModule_name(moduleNameArry[i]);
			
			indexDesignPreProcessingDao.insertGatheringModule(tempModule);
		}		
	}
	
	@Override
	public List<AccPathExec> parsingCollectionTermsList(AccPathExec accPathExec) throws Exception {
		return indexDesignPreProcessingDao.parsingCollectionTermsList(accPathExec);
	}
	
	@Override
	public List<VsqlSnapshot> snapShotList(VsqlSnapshot vsqlSnapshot) throws Exception {
		return indexDesignPreProcessingDao.snapShotList(vsqlSnapshot);
	}	
	
	@Override
	public void insertParsingCollectionTerms(VsqlSnapshot vsqlSnapshot) throws Exception {
		VsqlSnapshot vsql_temp = new VsqlSnapshot();
		vsql_temp = indexDesignPreProcessingDao.getSnapNoList(vsqlSnapshot);

		if(vsql_temp != null && vsql_temp.getSnap_s_no() != null){
			vsqlSnapshot.setSnap_s_no(vsql_temp.getSnap_s_no());
			vsqlSnapshot.setSnap_e_no(vsql_temp.getSnap_e_no());
			indexDesignPreProcessingDao.insertParsingCollectionTerms(vsqlSnapshot);
		}else{
			throw new Exception("DB에 데이터가 없거나 수집일시를 정확히 선택해 주세요.");
		}
		
	}	
	
	@Override
	public List<DbioLoadFile> loadSQLList(DbioLoadFile dbioLoadFile) throws Exception {
		return indexDesignPreProcessingDao.loadSQLList(dbioLoadFile);
	}
	
	@Override
	public void loadSQLFile(MultipartFile file, DbioLoadFile dbioLoadFile) throws Exception {
		FileMngUtil fileMng = new FileMngUtil();
		DbioLoadFile tempFile = new DbioLoadFile();
		List<DbioLoadSql> tempSqlList = new ArrayList<DbioLoadSql>();		
		String uploadPath = "";
		String orgFileName = file.getOriginalFilename();
		String fileName = "";
		String sqlText = "";
		int rowCnt = 0;
		
		// 1. 파일 NO 조회
		String fileNo = indexDesignPreProcessingDao.getDbioFileNo();
		
		try {
			fileName = fileMng.upload(file, fileNo);			
		}catch (Exception ex) {
			logger.error("error => " + ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}			
		
		// 2. DBIO_LOAD_FILE INSERT
		tempFile.setDbid(dbioLoadFile.getDbid());
		tempFile.setFile_no(fileNo);
		tempFile.setFile_nm(orgFileName);
		
		indexDesignPreProcessingDao.insertDbioLoadFile(tempFile);
		
		uploadPath = Config.getString("upload.file.dir") + fileName;
		
		List<String> rowData = FileMngUtil.fileLineRead(uploadPath);	

		for(int i = 0 ; i < rowData.size() ; i++){
			sqlText += rowData.get(i) + "\n";
			
			if(rowData.get(i).indexOf(";") > -1){
				rowCnt++;
				
				DbioLoadSql tempSql = new DbioLoadSql();
				tempSql.setFile_no(fileNo);
				tempSql.setQuery_seq(String.valueOf(rowCnt));
				tempSql.setSql_text(StringUtil.replace(sqlText,";",""));
				
				tempSqlList.add(tempSql);
				
				sqlText = "";				
			}
		}
		
		for(int i = 0 ; i < tempSqlList.size() ; i++){
			DbioLoadSql insDbioLoadSql = tempSqlList.get(i);
			// 3. DBID_LOAD_SQL INSERT
			indexDesignPreProcessingDao.insertDbioLoadSql(insDbioLoadSql);			
		}
		
		tempFile.setQuery_load_cnt(String.valueOf(rowCnt));

		// 4. DBIO_LOAD_FILE query_load_cnt UPDATE
		indexDesignPreProcessingDao.updateDbioLoadFile(tempFile);
	}
	
	@Override
	public int projectUnitLoadSQLFile(MultipartFile file, ProjectUnitLoadFile projectUnitLoadFile) throws Exception {
		FileMngUtil fileMng = new FileMngUtil();
		ProjectUnitLoadFile tempFile = new ProjectUnitLoadFile();
		List<ProjectUnitLoadSql> tempSqlList = new ArrayList<ProjectUnitLoadSql>();
		String uploadPath = "";
		String fileName = "";
		String sqlText = "";
		int updateCount = -1;
		
		String fileNo = indexDesignPreProcessingDao.getDbioFileNo();
		
		try {
			fileName = fileMng.upload(file, fileNo);
		}catch (Exception ex) {
			logger.error("error => " + ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}			
		
		// 2. DELETE PROJECT_UNIT_LOAD_FILE
		tempFile.setProject_id(projectUnitLoadFile.getProject_id());
		String project_id = projectUnitLoadFile.getProject_id();
		ProjectUnitLoadSql tempSql = new ProjectUnitLoadSql();
		tempSql.setProject_id(project_id);
		
		int delCount = indexDesignPreProcessingDao.deleteProjectUnitLoadSql(tempSql);
		
		logger.debug("delCount[" + delCount + "]");
		
		// 3. INSERT PROJECT_UNIT_LOAD_FILE
		uploadPath = Config.getString("upload.file.dir") + fileName;
		
		List<String> rowData = FileMngUtil.fileLineRead(uploadPath);
		
		for(int i = 0 ; i < rowData.size() ; i++){
			sqlText += rowData.get(i) + "\n";
			
			if(rowData.get(i).indexOf(";") > -1){
				tempSql = new ProjectUnitLoadSql();
				tempSql.setProject_id(project_id);
				tempSql.setProgram_source_desc(StringUtil.replace(sqlText,";",""));
				tempSql.setSql_hash(getHash(StringUtil.replace(sqlText,";","")));
				tempSql.setProgram_source_desc(sqlText);
				tempSql.setSql_hash(getHash(sqlText));
				
				tempSqlList.add(tempSql);
				
				sqlText = "";
			}
		}
		
		for(int i = 0 ; i < tempSqlList.size() ; i++){
			ProjectUnitLoadSql insDbioLoadSql = tempSqlList.get(i);
			// 3. DBID_LOAD_SQL INSERT
			indexDesignPreProcessingDao.insertProjectUnitLoadSql(insDbioLoadSql);
		}
		
		// 4. UPDATE 10235
		List<SolutionProgramMng> solutionProgramMngList = indexDesignPreProcessingDao.selectSolution10235();
		
		for (SolutionProgramMng programRule : solutionProgramMngList) {
			// String qty_chk_sql =
			// CryptoUtil.decryptAES256(sltProgramChkSqlRow.getQty_chk_sql(),key);
			String slt_program_chk_sql = null;
			try {
				slt_program_chk_sql = CryptoUtil.decryptAES128(programRule.getSlt_program_chk_sql(), key);
			} catch (Exception e) {
				slt_program_chk_sql = "복호화하는 과정에서 에러가 발생하였습니다. 해당 RULE을 수정해 주세요.";
			}
			
			tempSql = new ProjectUnitLoadSql();
			tempSql.setProject_id(project_id);
			tempSql.setUpdate10235(slt_program_chk_sql);
			updateCount = indexDesignPreProcessingDao.update10235(tempSql);
			logger.debug("updateCount:" + updateCount);
		}
		
		return updateCount;
	}
	
	private static String getMD5(String str){
		String MD5 = ""; 
		try{
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(str.getBytes()); 
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			MD5 = sb.toString();
		}catch(NoSuchAlgorithmException e){
			logger.error("Err Message : ", e);
			MD5 = null; 
		}
		
		return MD5;
	}
	
	private static String getHash(String sql) {
		String rtn="";
		String hashsrc="";
		int sqlLen=sql.length();
		if(sqlLen<4000) {
			hashsrc=sql;
		} else if(sqlLen>=4000 && sqlLen < 8000 ){
			hashsrc=sql.substring(0, 2000) + sql.substring(sqlLen-2000);
		} else {
			//log.debug("["+sql.substring(1000,1400)+"]");
			hashsrc=sql.substring(0, 1500) + sql.substring(4000,5000) + sql.substring(sqlLen-1500);
		}
		rtn=getMD5(hashsrc);
		
		return rtn;
	}
	
	@Override
	public List<DbioLoadSql> loadActionPlanList(DbioLoadSql dbioLoadSql) throws Exception {
		dbioLoadSql.setQuery_seq("0");
		
		return indexDesignPreProcessingDao.loadActionPlanList(dbioLoadSql);
	}

	@Override
	public List<LinkedHashMap<String, Object>> loadActionPlanList4Excel(DbioLoadSql dbioLoadSql) throws Exception {
		return indexDesignPreProcessingDao.loadActionPlanList4Excel(dbioLoadSql);
	}
	
	@Override
	public DbioLoadSql actionPlanInfo(DbioLoadSql dbioLoadSql) throws Exception {
		return indexDesignPreProcessingDao.actionPlanInfo(dbioLoadSql);
	}	
	
	@Override
	public String getMaxExplainExecSeq(DbioLoadSql dbioLoadSql) throws Exception {
		return indexDesignPreProcessingDao.getMaxExplainExecSeq(dbioLoadSql);
	}
	
	@Override
	public List<DbioLoadSql> isTaskLoadActionPlan(DbioLoadSql dbioLoadSql) throws Exception {
		return indexDesignPreProcessingDao.isTaskLoadActionPlan(dbioLoadSql);
	}
	
	@Override
	public int insertLoadActionPlan(DbioLoadSql dbioLoadSql) throws Exception {
		int returnFlag = 0;
		
		try{
			returnFlag = DbioAccPathPlan.CreatePlan(StringUtil.parseLong(dbioLoadSql.getDbid(),0), StringUtil.parseLong(dbioLoadSql.getFile_no(),0), StringUtil.parseLong(dbioLoadSql.getExplain_exec_seq(),0), dbioLoadSql.getCurrent_schema());
		}catch(Exception ex){
			logger.error("CreatePlan error ==> " + ex.getMessage());
			throw ex;
		}	

		return returnFlag;
	}
	
	@Override
	public DbioExplainExec planExecCnt(DbioExplainExec dbioExplainExec) throws Exception {
		return indexDesignPreProcessingDao.planExecCnt(dbioExplainExec);
	}
	
	@Override
	public DbioExplainExec selectActionPlanLog(DbioExplainExec dbioExplainExec) throws Exception {
		return indexDesignPreProcessingDao.selectActionPlanLog(dbioExplainExec);
	}
	
	@Override
	public int updateForceComplete(DbioExplainExec dbioExplainExec) throws Exception {
		return indexDesignPreProcessingDao.updateForceComplete(dbioExplainExec);
	}
	
	@Override
	public List<DbioLoadFile> explainList(DbioLoadFile dbioLoadFile) throws Exception {
		return indexDesignPreProcessingDao.explainList(dbioLoadFile);
	}
	
	@Override
	public List<AccPathExec> accessPathList(AccPathExec accPathExec) throws Exception {
		return indexDesignPreProcessingDao.accessPathList(accPathExec);
	}
	
	@Override
	public void insertParseLoadingCondition(AccPathExec accPathExec) throws Exception {
		indexDesignPreProcessingDao.insertParseLoadingCondition(accPathExec);
	}

	@Override
	public boolean downloadLargeExcel(DbioLoadSql dbioLoadSql, Model model, HttpServletRequest request, HttpServletResponse response) {
		boolean resultSuccess = true;
		ExcelDownHandler handler = new ExcelDownHandler(model, request, response);
		String sqlId = "omc.spop.dao.IndexDesignPreProcessingDao.downloadLargeExcel";
		String sql = "";
		
		try {
			handler.open();
			
			handler.buildInit("적재SQL_실행_계획_생성", "LOAD_ACTION_PLAN");
			
			dbioLoadSql.setQuery_seq("0");
			sql = handler.getSql(sqlSessionFactory, sqlId, dbioLoadSql);
			handler.buildDocument(sqlSessionFactory, sql);
			
			handler.writeDoc();
			
		}catch(Exception e) {
			e.printStackTrace();
			resultSuccess = false;
			
		}finally {
			handler.close();
		}
		
		return resultSuccess;
	}
}
