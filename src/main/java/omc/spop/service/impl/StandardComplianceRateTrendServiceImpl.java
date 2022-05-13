package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.SQLStandardsDao;
import omc.spop.dao.StandardComplianceRateTrendDao;
import omc.spop.model.Result;
import omc.spop.model.SQLStandards;
import omc.spop.model.StandardComplianceRateTrend;
import omc.spop.service.StandardComplianceRateTrendService;
import omc.spop.utils.CryptoUtil;

/***********************************************************
 * 2019.06.11	명성태		OPENPOP V2 최초작업
 * 2022.01.05	이재우		스케줄러 load 추가.
 **********************************************************/

@Service("StandardComplianceRateTrendService")
public class StandardComplianceRateTrendServiceImpl implements StandardComplianceRateTrendService {
	private static final Logger logger = LoggerFactory.getLogger(StandardComplianceRateTrendServiceImpl.class);
	
	@Autowired
	private StandardComplianceRateTrendDao scrtDao;
	
	@Autowired
	private SQLStandardsDao sqlStandardsDao;
	
	private String key = "openmade";
	
	private SQLStandards commonModule(String qty_chk_idt_cd, String parameter_list) throws Exception {
		SQLStandards sqlStandards = new SQLStandards();
		sqlStandards.setQty_chk_idt_cd(qty_chk_idt_cd);
		List<SQLStandards> qtyChkSqlList = sqlStandardsDao.getQtyChkSQL(sqlStandards);
		logger.debug("[" + qty_chk_idt_cd + "] qtyChkSqlList*****\n" + qtyChkSqlList);
		SQLStandards data = qtyChkSqlList.get(0);
		String qty_chk_sql = CryptoUtil.decryptAES128(data.getQty_chk_sql(),key);
		logger.debug("[" + qty_chk_idt_cd + "] decrypt qty_chk_sql*****\n" + qty_chk_sql);
		data.setQty_chk_sql(parseAndCondition(parameter_list, qty_chk_sql));
		
		return data;
	}
	
	private String parseAndCondition(String parameter_list, String qty_chk_sql) throws Exception {
		logger.debug("...parameter_list\n");
		logger.debug(parameter_list);
		logger.debug("\nqty_chk_sql\n");
		logger.debug(qty_chk_sql);
		logger.debug("..............");
		
		try {
			String[] parameterArray = parameter_list.split(",");
			Map<String, Object> parameterMap = new LinkedHashMap<String, Object>();
			
			for(int parameterIndex = 0; parameterIndex < parameterArray.length; parameterIndex++) {
				String parameter = parameterArray[parameterIndex];
				int separateIndex = parameter.indexOf(":");
				String key = parameter.substring(0, separateIndex);
				String value = parameter.substring(separateIndex + 1);
				
				parameterMap.put(key, value);
			}
			
			String[] numberSignArray = qty_chk_sql.split("#");
			List removeSyntax = new ArrayList();
			
			for(int numberSignIndex = 1; numberSignIndex < numberSignArray.length; numberSignIndex++) {
				String syntax =  numberSignArray[numberSignIndex];
				int firstIndex = syntax.indexOf("}");
				String numberSignName = syntax.substring(1, firstIndex);
				String numberSign = parameterMap.get(numberSignName) == null ? "NULL" : parameterMap.get(numberSignName) + "";
				
				if(numberSign.equals("NULL")) {
					removeSyntax.add(numberSignName);
					numberSign = numberSignName;
				}
				
				syntax = "'" + numberSign + "'" + syntax.substring(firstIndex + 1);
				numberSignArray[numberSignIndex] = syntax;
			}
			
			qty_chk_sql = "";
			
			for(int numberSignIndex = 0; numberSignIndex < numberSignArray.length; numberSignIndex++) {
				qty_chk_sql = qty_chk_sql + numberSignArray[numberSignIndex];
			}
			
			if(removeSyntax.size() > 0) {
				logger.debug("before removeSyntax sql\n" + qty_chk_sql);
				qty_chk_sql = proofreadingQty_chk_sql(qty_chk_sql, removeSyntax);
				logger.debug("after removeSyntax sql\n" + qty_chk_sql);
			}
		} catch(Exception ex) {
			logger.error("parseAndCondition error:" + ex);
			throw ex;
		}
		
		logger.debug("final query...\n");
		logger.debug(qty_chk_sql);
		logger.debug("..............");
		
		return qty_chk_sql;
	}
	
	private String proofreadingQty_chk_sql(String qty_chk_sql, List removeSyntax) {
		for(int removeIndex = 0; removeIndex < removeSyntax.size(); removeIndex++) {
			String syntax = removeSyntax.get(removeIndex) + "";
			
			while(qty_chk_sql.contains(syntax)) {
				int index = qty_chk_sql.indexOf(syntax);
				String preTemp = qty_chk_sql.substring(0, index);
				String postTemp = qty_chk_sql.substring(index);
				int preIndex = preTemp.lastIndexOf("AND");
				int postIndex = postTemp.indexOf("AND");
				
				String preSql = preTemp.substring(0, preIndex);
				String postSql = postTemp.substring(postIndex);
				
				qty_chk_sql = preSql + postSql;
			}
		}
		
		return qty_chk_sql;
	}
	
	@Override
	public List<StandardComplianceRateTrend> loadChartStandardComplianceRateTrendTotal(StandardComplianceRateTrend scrt) throws Exception {
		return scrtDao.loadSQL(commonModule(scrt.getQty_chk_idt_cd(), scrt.getParameter_list()));
	}

	@Override
	public List<StandardComplianceRateTrend> loadStatusByWork(StandardComplianceRateTrend scrt) throws Exception {
		return scrtDao.loadSQL(commonModule(scrt.getQty_chk_idt_cd(), scrt.getParameter_list()));
	}

	@Override
	public List<StandardComplianceRateTrend> loadChartStatusByWork(StandardComplianceRateTrend scrt) throws Exception {
		return scrtDao.loadSQL(commonModule(scrt.getQty_chk_idt_cd(), scrt.getParameter_list()));
	}
	
	@Override
	public List<StandardComplianceRateTrend> loadChartNonComplianceStatus(StandardComplianceRateTrend scrt) throws Exception {
		return scrtDao.loadSQL(commonModule(scrt.getQty_chk_idt_cd(), scrt.getParameter_list()));
	}
	
	@Override
	public Result loadChartStandardComplianceRateTrend(StandardComplianceRateTrend scrt) throws Exception {
		Result result = new Result();
		JSONObject jsonResult = new JSONObject();
		JSONArray list = new JSONArray();
		JSONObject data = null;
		List<StandardComplianceRateTrend> legendList = new ArrayList<StandardComplianceRateTrend>();
		List<StandardComplianceRateTrend> dataList = new  ArrayList<StandardComplianceRateTrend>();
		String tempDate = "";
		String tempLegend = "";
		
		try {
			dataList = scrtDao.loadSQL(commonModule(scrt.getQty_chk_idt_cd(), scrt.getParameter_list()));
			
			for(int index = 0; index < dataList.size(); index++) {
				StandardComplianceRateTrend temp = dataList.get(index);
				StandardComplianceRateTrend tempScrt = new StandardComplianceRateTrend();
				
				if(tempDate.equals("") && !tempDate.equals(temp.getSql_std_gather_day())) {
					data = new JSONObject();
					data.put("sql_std_gather_day", temp.getSql_std_gather_day());
					data.put(temp.getWrkjob_cd_nm(), temp.getCpla_rate());
				} else if(tempDate.equals(temp.getSql_std_gather_day())) {
					data.put(temp.getWrkjob_cd_nm(), temp.getCpla_rate());
				} else if(!tempDate.equals("") && !tempDate.equals(temp.getSql_std_gather_day())) {
					list.add(data);
					data = new JSONObject();
					data.put("sql_std_gather_day", temp.getSql_std_gather_day());
					data.put(temp.getWrkjob_cd_nm(), temp.getCpla_rate());
				}
				
				tempDate = temp.getSql_std_gather_day();
				tempDate = tempDate == null ? "" : tempDate;
				
				if(tempLegend.equals("") && !tempLegend.equals(temp.getWrkjob_cd_nm())) {
					tempScrt.setWrkjob_cd_nm(temp.getWrkjob_cd_nm());
					legendList.add(tempScrt);
				} else if(!tempLegend.equals("") && !tempLegend.equals(temp.getWrkjob_cd_nm())) {
					tempScrt.setWrkjob_cd_nm(temp.getWrkjob_cd_nm());
					legendList.add(tempScrt);
				}
				
				tempLegend = temp.getWrkjob_cd_nm();
			}
			
			list.add(data);
			jsonResult.put("rows", list);
			
			result.setResult(dataList.size() > 0 ? true : false);
			result.setObject(legendList);
			result.setTxtValue(jsonResult.toString());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> ==> " + ex.getMessage());
			throw ex;
		}
		
		return result;
	}
	
	@Override
	public Result loadChartNonStandardComplianceRateTrend(StandardComplianceRateTrend scrt) throws Exception {
		Result result = new Result();
		JSONObject jsonResult = new JSONObject();
		JSONArray list = new JSONArray();
		JSONObject data = null;
		List<StandardComplianceRateTrend> legendList = new ArrayList<StandardComplianceRateTrend>();
		List<StandardComplianceRateTrend> dataList = new  ArrayList<StandardComplianceRateTrend>();
		String tempDate = "";
		String tempLegend = "";
		
		try {
			dataList = scrtDao.loadSQL(commonModule(scrt.getQty_chk_idt_cd(), scrt.getParameter_list()));
			
			for(int index = 0; index < dataList.size(); index++) {
				StandardComplianceRateTrend temp = dataList.get(index);
				StandardComplianceRateTrend tempScrt = new StandardComplianceRateTrend();
				
				if(tempDate.equals("") && !tempDate.equals(temp.getSql_std_gather_day())) {
					data = new JSONObject();
					data.put("sql_std_gather_day", temp.getSql_std_gather_day());
					data.put(temp.getQty_chk_idt_nm(), temp.getErr_cnt());
				} else if(tempDate.equals(temp.getSql_std_gather_day())) {
					data.put(temp.getQty_chk_idt_nm(), temp.getErr_cnt());
				} else if(!tempDate.equals("") && !tempDate.equals(temp.getSql_std_gather_day())) {
					list.add(data);
					data = new JSONObject();
					data.put("sql_std_gather_day", temp.getSql_std_gather_day());
					data.put(temp.getQty_chk_idt_nm(), temp.getErr_cnt());
				}
				
				tempDate = temp.getSql_std_gather_day();
				tempDate = tempDate == null ? "" : tempDate;
				
				if(tempLegend.equals("") && !tempLegend.equals(temp.getQty_chk_idt_nm())) {
					tempScrt.setQty_chk_idt_nm(temp.getQty_chk_idt_nm());
					legendList.add(tempScrt);
				} else if(!tempLegend.equals("") && !tempLegend.equals(temp.getQty_chk_idt_nm())) {
					tempScrt.setQty_chk_idt_nm(temp.getQty_chk_idt_nm());
					legendList.add(tempScrt);
				}
				
				tempLegend = temp.getQty_chk_idt_nm();
			}
			
			list.add(data);
			jsonResult.put("rows", list);
			
			result.setResult(dataList.size() > 0 ? true : false);
			result.setObject(legendList);
			result.setTxtValue(jsonResult.toString());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> ==> " + ex.getMessage());
			throw ex;
		}
		
		return result;
	}
	
	/**
	 * dynamic data에 대한 챠트 데이터 샘플
	 */
	@Override
	public Result loadDynamicChartSample1(StandardComplianceRateTrend scrt) throws Exception {
		Result result = new Result();
		JSONObject jsonResult = new JSONObject();
		JSONArray list = new JSONArray();
		JSONObject data = null;
		List<StandardComplianceRateTrend> legendList = new ArrayList<StandardComplianceRateTrend>();
		List<StandardComplianceRateTrend> dataList = new  ArrayList<StandardComplianceRateTrend>();
		String tempDate = "";
		String tempLegend = "";
		
		try {
			dataList = getDataList();
			
			for(int index = 0; index < dataList.size(); index++) {
				StandardComplianceRateTrend temp = dataList.get(index);
				StandardComplianceRateTrend tempScrt = new StandardComplianceRateTrend();
				
				if(tempDate.equals("") && !tempDate.equals(temp.getSql_std_gather_day())) {
					data = new JSONObject();
					data.put("sql_std_gather_day", temp.getSql_std_gather_day());
					data.put(temp.getQty_chk_idt_nm(), temp.getErr_cnt());
				} else if(tempDate.equals(temp.getSql_std_gather_day())) {
					data.put(temp.getQty_chk_idt_nm(), temp.getErr_cnt());
				} else if(!tempDate.equals("") && !tempDate.equals(temp.getSql_std_gather_day())) {
					list.add(data);
					data = new JSONObject();
					data.put("sql_std_gather_day", temp.getSql_std_gather_day());
					data.put(temp.getQty_chk_idt_nm(), temp.getErr_cnt());
				}
				
				tempDate = temp.getSql_std_gather_day();
				tempDate = tempDate == null ? "" : tempDate;
				
				if(tempLegend.equals("") && !tempLegend.equals(temp.getQty_chk_idt_nm())) {
					tempScrt.setQty_chk_idt_nm(temp.getQty_chk_idt_nm());
					legendList.add(tempScrt);
				} else if(!tempLegend.equals("") && !tempLegend.equals(temp.getQty_chk_idt_nm())) {
					tempScrt.setQty_chk_idt_nm(temp.getQty_chk_idt_nm());
					legendList.add(tempScrt);
				}
				
				tempLegend = temp.getQty_chk_idt_nm();
			}
			
			list.add(data);
			jsonResult.put("rows", list);
			
			result.setResult(dataList.size() > 0 ? true : false);
			result.setObject(legendList);
			result.setTxtValue(jsonResult.toString());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> ==> " + ex.getMessage());
			throw ex;
		}
		
		return result;
	}
	
	private List<StandardComplianceRateTrend> getDataList() {
		List<StandardComplianceRateTrend> data = new ArrayList<StandardComplianceRateTrend>();
		StandardComplianceRateTrend scrt = null;
		String day = "";
		List nmList = new ArrayList();
		String tempNm = "";
		Random random = new Random();
		
		nmList.add("SQL 식별자 표준 미준수");
		nmList.add("SELECT * 사용");
//		nmList.add("NOLOGGING 옵션 사용");
//		nmList.add("금지 HINT 사용");
//		nmList.add("SELECT FOR UPDATE - WAIT 옵션 누락");
//		nmList.add("ANSI SQL 사용");
//		nmList.add("FETCH FIRST 사용 ");
//		nmList.add("UPDATE/DELETE 문에 WHERE 절 누락");
//		nmList.add("(빈공백) 비교");
//		nmList.add("TO_DATE 함수 FORMAT 누락");
		
		for(int index = 0; index < nmList.size(); index++) {
			tempNm = nmList.get(index) + "";
			
			for(int dayIndex = 1; dayIndex < 132; dayIndex++) {
				scrt = new StandardComplianceRateTrend();
				
				if(index == nmList.size() - 1
						&& dayIndex >= 0 && dayIndex < 99) {
					continue;
				}
				
				scrt.setQty_chk_idt_nm(tempNm);
				scrt.setSql_std_gather_day(dayIndex + "");
				scrt.setErr_cnt(random.nextInt(15) + "");
				
				data.add(scrt);
			}
		}
		
		logger.debug("LOG---\n");
		for(int index = 0; index < data.size(); index++) {
			StandardComplianceRateTrend scrt1 = (StandardComplianceRateTrend) data.get(index);
			logger.debug("Qty_chk_idt_nm[" + scrt1.getQty_chk_idt_nm() + "] Sql_std_gather_day[" + scrt1.getSql_std_gather_day() + "] Err_cnt[" + scrt1.getErr_cnt() + "]");
		}
		
		return data;
	}

	@Override
	public List<StandardComplianceRateTrend> loadSchedulerList(StandardComplianceRateTrend scrt) throws Exception {
		return scrtDao.loadSchedulerList(scrt);
	}
}
