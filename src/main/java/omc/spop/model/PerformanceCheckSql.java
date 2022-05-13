package omc.spop.model;

import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2020.05.12 	명성태 	최초작성
 **********************************************************/

@Alias("performanceCheckSql")
public class PerformanceCheckSql extends Base implements Jsonable {
	private String deploy_complete_dt;
	private String begin_dt;
	private String end_dt;
	private int base_period_value;
	private int isLargerThanBeginDate;
	
	private String wrkjob_cd;
	private String wrkjob_cd_nm;
	private String parent_id;
	private String _parentId;
	private int deploy_sql_cnt;
	private int sql_cnt;
	private int improve;
	private int regress;
	private int fail;
	private int less_than_2;
	private int less_than_5;
	private int less_than_10;
	private int less_than_30;
	private int less_than_50;
	private int less_than_100;
	private int more_than_100;
	
	private int searchType;		// 1 : 성능 점검 SLQ 탭, 2 : 예외 처리 SQL 탭
	private int sqlType;		// 1 : 전체, 2 : 성능 향상, 3 : 저하 
	
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		// object -> Map
		ObjectMapper oMapper = new ObjectMapper();
		Map<String, Object> map = oMapper.convertValue(this, Map.class);
		Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
		String strJson = gson.toJson(map);
		try {
			objJson = (JSONObject) new JSONParser().parse(strJson);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return objJson;
	}

	public int getDeploy_sql_cnt() {
		return deploy_sql_cnt;
	}

	public void setDeploy_sql_cnt(int deploy_sql_cnt) {
		this.deploy_sql_cnt = deploy_sql_cnt;
	}

	public String getDeploy_complete_dt() {
		return deploy_complete_dt;
	}

	public void setDeploy_complete_dt(String deploy_complete_dt) {
		this.deploy_complete_dt = deploy_complete_dt;
	}

//	public String getStart_first_analysis_day() {
//		return start_first_analysis_day;
//	}
//
//	public void setStart_first_analysis_day(String start_first_analysis_day) {
//		this.start_first_analysis_day = start_first_analysis_day;
//	}
//
//	public String getEnd_first_analysis_day() {
//		return end_first_analysis_day;
//	}
//
//	public void setEnd_first_analysis_day(String end_first_analysis_day) {
//		this.end_first_analysis_day = end_first_analysis_day;
//	}

	public String getBegin_dt() {
		return begin_dt;
	}

	public void setBegin_dt(String begin_dt) {
		this.begin_dt = begin_dt;
	}

	public String getEnd_dt() {
		return end_dt;
	}

	public void setEnd_dt(String end_dt) {
		this.end_dt = end_dt;
	}

	public int getBase_period_value() {
		return base_period_value;
	}

	public void setBase_period_value(int base_period_value) {
		this.base_period_value = base_period_value;
	}

	public int getIsLargerThanBeginDate() {
		return isLargerThanBeginDate;
	}

	public void setIsLargerThanBeginDate(int isLargerThanBeginDate) {
		this.isLargerThanBeginDate = isLargerThanBeginDate;
	}

	public String getWrkjob_cd() {
		return wrkjob_cd;
	}

	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}

	public String getWrkjob_cd_nm() {
		return wrkjob_cd_nm;
	}

	public void setWrkjob_cd_nm(String wrkjob_cd_nm) {
		this.wrkjob_cd_nm = wrkjob_cd_nm;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String get_parentId() {
		return _parentId;
	}

	public void set_parentId(String _parentId) {
		this._parentId = _parentId;
	}

	public int getSql_cnt() {
		return sql_cnt;
	}

	public void setSql_cnt(int sql_cnt) {
		this.sql_cnt = sql_cnt;
	}

	public int getImprove() {
		return improve;
	}

	public void setImprove(int improve) {
		this.improve = improve;
	}

	public int getRegress() {
		return regress;
	}

	public void setRegress(int regress) {
		this.regress = regress;
	}

	public int getFail() {
		return fail;
	}

	public void setFail(int fail) {
		this.fail = fail;
	}

	public int getLess_than_2() {
		return less_than_2;
	}

	public void setLess_than_2(int less_than_2) {
		this.less_than_2 = less_than_2;
	}

	public int getLess_than_5() {
		return less_than_5;
	}

	public int getLess_than_10() {
		return less_than_10;
	}

	public void setLess_than_10(int less_than_10) {
		this.less_than_10 = less_than_10;
	}

	public void setLess_than_5(int less_than_5) {
		this.less_than_5 = less_than_5;
	}

	public int getLess_than_30() {
		return less_than_30;
	}

	public void setLess_than_30(int less_than_30) {
		this.less_than_30 = less_than_30;
	}

	public int getLess_than_50() {
		return less_than_50;
	}

	public void setLess_than_50(int less_than_50) {
		this.less_than_50 = less_than_50;
	}

	public int getLess_than_100() {
		return less_than_100;
	}

	public void setLess_than_100(int less_than_100) {
		this.less_than_100 = less_than_100;
	}

	public int getMore_than_100() {
		return more_than_100;
	}

	public void setMore_than_100(int more_than_100) {
		this.more_than_100 = more_than_100;
	}

	public int getSearchType() {
		return searchType;
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	public int getSqlType() {
		return sqlType;
	}

	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}
}
