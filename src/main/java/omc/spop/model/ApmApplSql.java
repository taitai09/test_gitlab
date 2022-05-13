package omc.spop.model;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.ibatis.type.Alias;
import org.codehaus.plexus.util.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.10.30	이원식	최초작성
 **********************************************************/

@Alias("apmApplSql")
public class ApmApplSql extends Base implements Jsonable {
	private String dbio;
	private String tr_cd;
	private String wrkjob_cd;
	private String appl_name;
	private BigDecimal appl_hash;
	private BigDecimal sql_hash;
	private BigDecimal elapsed_time;
	private BigDecimal exec_cnt;
	private String sql_text;
	
	private String ndv_ratio;
	private String col_null;
	private String parsing_schema_name;
	
	private String db_operate_type_nm;
	private String default_yn;
	private String access_path_type;
	private String selectivity_calc_method;
	private String table_owner;
	


	public String getTable_owner() {
		return table_owner;
	}


	public void setTable_owner(String table_owner) {
		this.table_owner = table_owner;
	}


	public String getAccess_path_type() {
		return access_path_type;
	}


	public void setAccess_path_type(String access_path_type) {
		this.access_path_type = access_path_type;
	}


	public String getSelectivity_calc_method() {
		return selectivity_calc_method;
	}


	public void setSelectivity_calc_method(String selectivity_calc_method) {
		this.selectivity_calc_method = selectivity_calc_method;
	}


	public String getDbio() {
		return dbio;
	}


	public void setDbio(String dbio) {
		this.dbio = dbio;
	}


	public String getTr_cd() {
		return tr_cd;
	}


	public void setTr_cd(String tr_cd) {
		this.tr_cd = tr_cd;
	}


	public String getWrkjob_cd() {
		return wrkjob_cd;
	}


	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}


	public String getAppl_name() {
		return appl_name;
	}


	public void setAppl_name(String appl_name) {
		this.appl_name = appl_name;
	}


	public BigDecimal getAppl_hash() {
		return appl_hash;
	}


	public void setAppl_hash(BigDecimal appl_hash) {
		this.appl_hash = appl_hash;
	}


	public BigDecimal getSql_hash() {
		return sql_hash;
	}


	public void setSql_hash(BigDecimal sql_hash) {
		this.sql_hash = sql_hash;
	}


	public BigDecimal getElapsed_time() {
		return elapsed_time;
	}


	public void setElapsed_time(BigDecimal elapsed_time) {
		this.elapsed_time = elapsed_time;
	}


	public BigDecimal getExec_cnt() {
		return exec_cnt;
	}


	public void setExec_cnt(BigDecimal exec_cnt) {
		this.exec_cnt = exec_cnt;
	}


	public String getSql_text() {
		return sql_text;
	}


	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
	}


	public String getNdv_ratio() {
		return ndv_ratio;
	}


	public void setNdv_ratio(String ndv_ratio) {
		this.ndv_ratio = ndv_ratio;
	}


	public String getCol_null() {
		return col_null;
	}


	public void setCol_null(String col_null) {
		this.col_null = col_null;
	}


	public String getParsing_schema_name() {
		return parsing_schema_name;
	}


	public void setParsing_schema_name(String parsing_schema_name) {
		this.parsing_schema_name = parsing_schema_name;
	}


	public String getDb_operate_type_nm() {
		return db_operate_type_nm;
	}


	public void setDb_operate_type_nm(String db_operate_type_nm) {
		this.db_operate_type_nm = db_operate_type_nm;
	}


	public String getDefault_yn() {
		return default_yn;
	}


	public void setDefault_yn(String default_yn) {
		this.default_yn = default_yn;
	}
	
	/*@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();

		objJson.put("dbid",this.getDbid());
		objJson.put("dbio",this.getDbio());
		objJson.put("tr_cd",this.getTr_cd());
		objJson.put("wrkjob_cd",this.getWrkjob_cd());
		objJson.put("appl_name",this.getAppl_name());
		objJson.put("appl_hash",this.getAppl_hash() == null || this.getAppl_hash().equals("") ? 0 : Integer.parseInt(this.getAppl_hash()));
		objJson.put("sql_hash",this.getSql_hash() == null || this.getSql_hash().equals("") ? 0 : Integer.parseInt(this.getSql_hash()));
		objJson.put("elapsed_time",this.getElapsed_time() == null || this.getElapsed_time().equals("") ? 0 : Float.parseFloat(this.getElapsed_time()));
		objJson.put("exec_cnt",this.getExec_cnt() == null || this.getExec_cnt().equals("") ? 0 : Integer.parseInt(this.getExec_cnt()));
		objJson.put("sql_text",this.getSql_text());
		objJson.put("ndv_ratio",this.getNdv_ratio());
		objJson.put("col_null",this.getCol_null());
		objJson.put("parsing_schema_name",this.getParsing_schema_name());
		objJson.put("db_operate_type_nm",this.getDb_operate_type_nm());
		objJson.put("default_yn",this.getDefault_yn());

		return objJson;
	}		*/


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

	
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
}