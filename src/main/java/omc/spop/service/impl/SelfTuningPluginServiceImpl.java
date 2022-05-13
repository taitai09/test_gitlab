package omc.spop.service.impl;

import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.SelfTuningDao;
import omc.spop.model.ApmApplSql;
import omc.spop.model.ApmApplSqlPlugIn;
import omc.spop.model.Result;
import omc.spop.model.SelftunSql;
import omc.spop.model.SelftunSqlBind;
import omc.spop.model.Users;
import omc.spop.server.tune.SelfTuneTest;
import omc.spop.service.SelfTuningPlugInService;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.03.23	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Service("SelfTuningPluginService")
public class SelfTuningPluginServiceImpl implements SelfTuningPlugInService {
	
	private static final Logger logger = LoggerFactory.getLogger(SelfTuningPluginServiceImpl.class);
	
	@Autowired
	private SelfTuningDao selfTuningDao;
	
	@Override
	public List<ApmApplSql> databaseListOfWrkjobCd(ApmApplSql apmApplSql) throws Exception {
		return selfTuningDao.databaseListOfWrkjobCd(apmApplSql);
	}
	
	@Override
	public String insertSelftunQuery(ApmApplSql apmApplSql) throws Exception {
		SelftunSql selftunSql = new SelftunSql();
		String selftunQuerySeq = "";
		Users users = null;				/* plugin에서 넘겨준 uuid 정보를 Reg_id로 사용한다. */
		
		try{
			// 1. SELFTUN MAX SEQ 조회
			selftunQuerySeq = selfTuningDao.getMaxSelftunQuerySeq(apmApplSql);
			
			// 2. SELFTUN_SQL INSERT
			selftunSql.setDbid(apmApplSql.getDbid());
			selftunSql.setSelftun_query_seq(selftunQuerySeq);
			selftunSql.setUuid(apmApplSql.getUuid());
			selftunSql.setReg_id(apmApplSql.getUser_id());
			logger.debug("uuid[" + apmApplSql.getUuid()+ "]");
			
			selftunSql.setSql_text(apmApplSql.getDefaultText());
			selftunSql.setWrkjob_cd(apmApplSql.getWrkjob_cd());
			
			selfTuningDao.insertSelftunQuery(selftunSql);
		}catch(Exception ex){
			logger.error("SELFTUN_SQL INSERT ERROR ==> " + ex.getMessage());
			throw ex;
		}
		
		return selftunQuerySeq;
	}
	
	@Override
	public Result startSelfTestNew(ApmApplSqlPlugIn apmApplSqlPlugIn, String selftunQuerySeq) throws Exception {
		Result result = new Result();
		SelftunSqlBind bind = null;
		int bindInt = 0;

		String bindSeq = apmApplSqlPlugIn.getBind_seq();
		String bindVarNm = apmApplSqlPlugIn.getBind_var_nm();
		String bindVarType = apmApplSqlPlugIn.getBind_var_type();
		String bindVarValue = apmApplSqlPlugIn.getBind_var_value();
		
		String[] bindSeqArr = bindSeq == null ? null : bindSeq.split(";");
		String[] bindVarNmArr = bindVarNm == null ? null : bindVarNm.split(";");
		String[] bindVarTypeArr = bindVarType == null ? null : bindVarType.split(";");
		String[] bindVarValueArr = bindVarValue == null ? null : bindVarValue.split(";");
		
		String[] bindArry = null;
		
		if(bindVarNmArr != null){
			bindInt = bindVarNmArr.length;
			
			bindArry = new String[bindInt];
			
			for(int i = 0; i < bindInt; i++){
				bindArry[i] = bindVarNmArr[i] + "," + bindVarTypeArr[i] + "," + bindVarValueArr[i].replaceAll(" ", "");
			}
		} else {
			bindArry = new String[0];
		}
		
		// 서버모듈 - ExecutionPlan 조회
		// select * from orders where order_id = :order_id
		String execPlanNew = "";
		
		try{ 
			execPlanNew = SelfTuneTest.getExecPlanNew(StringUtil.parseLong(apmApplSqlPlugIn.getDbid(), 0),
													StringUtil.parseLong(selftunQuerySeq, 0), bindArry,
													apmApplSqlPlugIn.getParsing_schema_name());
			logger.debug("execPlanNew :"+execPlanNew);
			
			if (execPlanNew == null) {
				throw new Exception(execPlanNew);
			}
			
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonResult = (JSONObject) jsonParser.parse(execPlanNew);
			
			String isError = (String) jsonResult.get("is_error");
			String errMsg = (String) jsonResult.get("err_msg");
			if (isError.equals("true")) {
				throw new Exception((String) jsonResult.get("err_msg"));
			}
//			else{
//				if(errMsg.contains("ORA-")) {
//					throw new Exception((String) jsonResult.get("err_msg"));
//				}
//			}

			// SELFTUN_SQL_BIND INSERT
			if(bindInt == 0) {
				return result;
			}
			
			if(bindVarNmArr != null){
				int insertResult = 0;
				for(int i = 0; i < bindVarNmArr.length ; i++){
					bind = new SelftunSqlBind();
					bind.setDbid(apmApplSqlPlugIn.getDbid());
					bind.setSelftun_query_seq(selftunQuerySeq);
					bind.setBind_seq(bindSeqArr[i]);
					bind.setBind_var_nm(bindVarNmArr[i]);
					bind.setBind_var_value(bindVarValueArr[i]);
					bind.setBind_var_type(bindVarTypeArr[i]);
					
					insertResult += selfTuningDao.insertSelftunSqlBind(bind);
				}
				if(insertResult == bindVarNmArr.length) {
					result.setResult(true);
					result.setMessage("성능점검수행을 완료하였습니다.");
				}
			}
		}catch(Exception ex){
			logger.error("SERVER ERROR(Get_ExecPlan) ==> " + ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}		
		return result;
	}
}
