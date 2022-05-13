package omc.spop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
 * 2018.05.14	이원식	최초작성
 **********************************************************/

@Alias("dbParameterHistory")
public class DbParameterHistory extends Base implements Jsonable {

	private String id;
	private String p_id;	
	private String inst_id;
	private String inst_nm;
	private String instance_number;	
	private String parameter_hash;
	private String parameter_chg_dt;
	private String parameter_name;
	private String value;
	private String db_value;
	private String standard_value;
	private String cnt;
	
	private List<DbParameterHistory> children = new ArrayList<DbParameterHistory>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getP_id() {
		return p_id;
	}
	public void setP_id(String p_id) {
		this.p_id = p_id;
	}
	public String getInst_id() {
		return inst_id;
	}
	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}
	public String getInst_nm() {
		return inst_nm;
	}
	public void setInst_nm(String inst_nm) {
		this.inst_nm = inst_nm;
	}
	public String getInstance_number() {
		return instance_number;
	}
	public void setInstance_number(String instance_number) {
		this.instance_number = instance_number;
	}
	public String getParameter_hash() {
		return parameter_hash;
	}
	public void setParameter_hash(String parameter_hash) {
		this.parameter_hash = parameter_hash;
	}
	public String getParameter_chg_dt() {
		return parameter_chg_dt;
	}
	public void setParameter_chg_dt(String parameter_chg_dt) {
		this.parameter_chg_dt = parameter_chg_dt;
	}
	public String getParameter_name() {
		return parameter_name;
	}
	public void setParameter_name(String parameter_name) {
		this.parameter_name = parameter_name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDb_value() {
		return db_value;
	}
	public void setDb_value(String db_value) {
		this.db_value = db_value;
	}
	public String getStandard_value() {
		return standard_value;
	}
	public void setStandard_value(String standard_value) {
		this.standard_value = standard_value;
	}
	public String getCnt() {
		return cnt;
	}
	public void setCnt(String cnt) {
		this.cnt = cnt;
	}
	
	public List<DbParameterHistory> getChildren() {
		return children;
	}
	public void setChildren(List<DbParameterHistory> children) {
		this.children = children;
	}
	
//	@SuppressWarnings("unchecked")
//	public JSONObject toJSONObject() {
//		JSONObject objJson = new JSONObject();
//		
//		objJson.put("dbid", this.getDbid());
//		objJson.put("db_name", this.getDb_name());
//		objJson.put("inst_id", StringUtil.parseDouble(this.getInst_id(),0));
//		objJson.put("inst_nm", this.getInst_nm());
//		objJson.put("instance_number", StringUtil.parseDouble(this.getInstance_number(),0));
//		objJson.put("parameter_hash", StringUtil.parseDouble(this.getParameter_hash(),0));
//		objJson.put("parameter_chg_dt", this.getParameter_chg_dt());
//		objJson.put("parameter_name", this.getParameter_name());
//		objJson.put("value", this.getValue());
//		objJson.put("db_value", this.getDb_value());
//		objJson.put("standard_value", this.getStandard_value());
//		objJson.put("cnt", StringUtil.parseDouble(this.getCnt(),0));
//
//		return objJson;
//	}
	
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
	
}