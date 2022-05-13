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
 * 2019.04.30 홍길동 최초작성
 **********************************************************/
@Alias("projectSqlIdfyCondition")
public class ProjectSqlIdfyCondition extends Base implements Jsonable {
	
	// Table Columns
	private int    project_id;
	private int    check_target_seq;
	private String sql_idfy_cond_type_cd;
	private String owner;
	private String table_name;
	private String module;
	
	// Add Variable 
	private String project_nm;
	private String project_check_target_type_cd;
	private String sql_idfy_cond_type_nm;
	
	// Popup Batch Insert
	private String tableOwnerArry;
	private String tableNameArry;
	
	// Popup Batch Check
	private String owner_yn;
	private String owner_table_yn;
	private String table_yn;
	private String module_yn;
	
	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	public int getCheck_target_seq() {
		return check_target_seq;
	}

	public void setCheck_target_seq(int check_target_seq) {
		this.check_target_seq = check_target_seq;
	}

	public String getSql_idfy_cond_type_cd() {
		return sql_idfy_cond_type_cd;
	}

	public void setSql_idfy_cond_type_cd(String sql_idfy_cond_type_cd) {
		this.sql_idfy_cond_type_cd = sql_idfy_cond_type_cd;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getProject_nm() {
		return project_nm;
	}

	public void setProject_nm(String project_nm) {
		this.project_nm = project_nm;
	}

	public String getProject_check_target_type_cd() {
		return project_check_target_type_cd;
	}

	public void setProject_check_target_type_cd(String project_check_target_type_cd) {
		this.project_check_target_type_cd = project_check_target_type_cd;
	}

	public String getSql_idfy_cond_type_nm() {
		return sql_idfy_cond_type_nm;
	}

	public void setSql_idfy_cond_type_nm(String sql_idfy_cond_type_nm) {
		this.sql_idfy_cond_type_nm = sql_idfy_cond_type_nm;
	}

	public String getTableOwnerArry() {
		return tableOwnerArry;
	}

	public void setTableOwnerArry(String tableOwnerArry) {
		this.tableOwnerArry = tableOwnerArry;
	}

	public String getTableNameArry() {
		return tableNameArry;
	}

	public void setTableNameArry(String tableNameArry) {
		this.tableNameArry = tableNameArry;
	}

	public String getOwner_yn() {
		return owner_yn;
	}

	public void setOwner_yn(String owner_yn) {
		this.owner_yn = owner_yn;
	}

	public String getOwner_table_yn() {
		return owner_table_yn;
	}

	public void setOwner_table_yn(String owner_table_yn) {
		this.owner_table_yn = owner_table_yn;
	}

	public String getTable_yn() {
		return table_yn;
	}

	public void setTable_yn(String table_yn) {
		this.table_yn = table_yn;
	}

	public String getModule_yn() {
		return module_yn;
	}

	public void setModule_yn(String module_yn) {
		this.module_yn = module_yn;
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

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
