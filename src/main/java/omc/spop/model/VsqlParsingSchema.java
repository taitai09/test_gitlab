package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.12.15	이원식	최초작성
 **********************************************************/

@Alias("vsqlParsingSchema")
public class VsqlParsingSchema extends Base implements Jsonable {
	
	private String parsing_schema_no;
	private String parsing_username;
	private String global_view_yn;
	private String instance_number;
	private String reg_dt;
	private String username;
	
	private String parsingSchemaArry;
	private String moduleNameArry;
	
	public String getParsing_schema_no() {
		return parsing_schema_no;
	}
	public void setParsing_schema_no(String parsing_schema_no) {
		this.parsing_schema_no = parsing_schema_no;
	}
	public String getParsing_username() {
		return parsing_username;
	}
	public void setParsing_username(String parsing_username) {
		this.parsing_username = parsing_username;
	}
	public String getGlobal_view_yn() {
		return global_view_yn;
	}
	public void setGlobal_view_yn(String global_view_yn) {
		this.global_view_yn = global_view_yn;
	}
	public String getInstance_number() {
		return instance_number;
	}
	public void setInstance_number(String instance_number) {
		this.instance_number = instance_number;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}	
	public String getParsingSchemaArry() {
		return parsingSchemaArry;
	}
	public void setParsingSchemaArry(String parsingSchemaArry) {
		this.parsingSchemaArry = parsingSchemaArry;
	}
	public String getModuleNameArry() {
		return moduleNameArry;
	}
	public void setModuleNameArry(String moduleNameArry) {
		this.moduleNameArry = moduleNameArry;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("parsing_schema_no",this.getParsing_schema_no());
		objJson.put("parsing_username",this.getParsing_username());
		objJson.put("global_view_yn",this.getGlobal_view_yn());
		objJson.put("instance_number",this.getInstance_number());
		objJson.put("reg_dt",this.getReg_dt());
		objJson.put("username",this.getUsername());

		return objJson;
	}		
}