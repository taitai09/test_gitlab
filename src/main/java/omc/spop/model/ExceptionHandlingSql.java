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

@Alias("exceptionHandlingSql")
public class ExceptionHandlingSql extends Base implements Jsonable {
	private String deploy_complete_dt;
	private String begin_dt;
	private String end_dt;
	private int base_period_value;
	
	private String cd;
	private String cd_nm;
	
	private String wrkjob_cd;
	private String wrkjob_cd_nm;
	private String parent_id;
	private String _parentId;
	private int deploy_sql_cnt;
	private int sql_cnt;
	private int pass;
	private int fail;
	private int less_than_0_dot_1;
	private int less_than_0_dot_3;
	private int less_than_1;
	private int less_than_3;
	private int less_than_10;
	private int less_than_60;
	private int more_than_60;
	
	private int searchType;				// 1 : 성능 점검 SLQ 탭, 2 : 예외 처리 SQL 탭
	
	private String exception_prc_meth_cd;	// 2 : 영구 점검제외, 3 : 한시 점검제외
	
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

	public String getCd() {
		return cd;
	}

	public void setCd(String cd) {
		this.cd = cd;
	}

	public String getCd_nm() {
		return cd_nm;
	}

	public void setCd_nm(String cd_nm) {
		this.cd_nm = cd_nm;
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

	public int getPass() {
		return pass;
	}

	public void setPass(int pass) {
		this.pass = pass;
	}

	public int getFail() {
		return fail;
	}

	public void setFail(int fail) {
		this.fail = fail;
	}

	public int getLess_than_0_dot_1() {
		return less_than_0_dot_1;
	}

	public void setLess_than_0_dot_1(int less_than_0_dot_1) {
		this.less_than_0_dot_1 = less_than_0_dot_1;
	}

	public int getLess_than_0_dot_3() {
		return less_than_0_dot_3;
	}

	public void setLess_than_0_dot_3(int less_than_0_dot_3) {
		this.less_than_0_dot_3 = less_than_0_dot_3;
	}

	public int getLess_than_1() {
		return less_than_1;
	}

	public void setLess_than_1(int less_than_1) {
		this.less_than_1 = less_than_1;
	}

	public int getLess_than_3() {
		return less_than_3;
	}

	public void setLess_than_3(int less_than_3) {
		this.less_than_3 = less_than_3;
	}

	public int getLess_than_10() {
		return less_than_10;
	}

	public void setLess_than_10(int less_than_10) {
		this.less_than_10 = less_than_10;
	}

	public int getLess_than_60() {
		return less_than_60;
	}

	public void setLess_than_60(int less_than_60) {
		this.less_than_60 = less_than_60;
	}

	public int getMore_than_60() {
		return more_than_60;
	}

	public void setMore_than_60(int more_than_60) {
		this.more_than_60 = more_than_60;
	}

	public int getSearchType() {
		return searchType;
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	public String getException_prc_meth_cd() {
		return exception_prc_meth_cd;
	}

	public void setException_prc_meth_cd(String exception_prc_meth_cd) {
		this.exception_prc_meth_cd = exception_prc_meth_cd;
	}

}
