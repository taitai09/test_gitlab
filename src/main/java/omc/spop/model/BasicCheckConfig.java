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
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.04.18	이원식	최초작성
 * 2018.05.16	이원식	dashboard 일예방점검현황을 위한 필드 추가
 **********************************************************/

@Alias("basicCheckConfig")
public class BasicCheckConfig extends Base implements Jsonable {
    private String check_pref_id;
    private String check_pref_nm;
    private String check_enable_yn;
    private String check_value_unit;
    private String default_threshold_value;
    private String check_grade_cd;
    private String check_class_div_cd;
    private String emergency_action_yn;
    
    private String check_day;
    private String database;
    private String instance;
    private String space;
    private String object;
    private String statistics;
    private String select_dbid;
    private String use_yn;
    
    
    
    public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}

	private String check_grade_cd_nm;
    private String check_class_div_cd_nm;
    private String threshold_value;
    
    
	public String getSelect_dbid() {
		return select_dbid;
	}
	public void setSelect_dbid(String select_dbid) {
		this.select_dbid = select_dbid;
	}
	public String getThreshold_value() {
		return threshold_value;
	}
	public void setThreshold_value(String threshold_value) {
		this.threshold_value = threshold_value;
	}
	public String getCheck_class_div_cd_nm() {
		return check_class_div_cd_nm;
	}
	public void setCheck_class_div_cd_nm(String check_class_div_cd_nm) {
		this.check_class_div_cd_nm = check_class_div_cd_nm;
	}
	public String getCheck_grade_cd_nm() {
		return check_grade_cd_nm;
	}
	public void setCheck_grade_cd_nm(String check_grade_cd_nm) {
		this.check_grade_cd_nm = check_grade_cd_nm;
	}
	public String getCheck_pref_id() {
		return check_pref_id;
	}
	public void setCheck_pref_id(String check_pref_id) {
		this.check_pref_id = check_pref_id;
	}
	public String getCheck_pref_nm() {
		return check_pref_nm;
	}
	public void setCheck_pref_nm(String check_pref_nm) {
		this.check_pref_nm = check_pref_nm;
	}
	public String getCheck_enable_yn() {
		return check_enable_yn;
	}
	public void setCheck_enable_yn(String check_enable_yn) {
		this.check_enable_yn = check_enable_yn;
	}
	public String getCheck_value_unit() {
		return check_value_unit;
	}
	public void setCheck_value_unit(String check_value_unit) {
		this.check_value_unit = check_value_unit;
	}
	public String getDefault_threshold_value() {
		return default_threshold_value;
	}
	public void setDefault_threshold_value(String default_threshold_value) {
		this.default_threshold_value = default_threshold_value;
	}
	public String getCheck_grade_cd() {
		return check_grade_cd;
	}
	public void setCheck_grade_cd(String check_grade_cd) {
		this.check_grade_cd = check_grade_cd;
	}	
	public String getCheck_class_div_cd() {
		return check_class_div_cd;
	}
	public void setCheck_class_div_cd(String check_class_div_cd) {
		this.check_class_div_cd = check_class_div_cd;
	}
	public String getEmergency_action_yn() {
		return emergency_action_yn;
	}
	public void setEmergency_action_yn(String emergency_action_yn) {
		this.emergency_action_yn = emergency_action_yn;
	}	
	public String getCheck_day() {
		return check_day;
	}
	public void setCheck_day(String check_day) {
		this.check_day = check_day;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getInstance() {
		return instance;
	}
	public void setInstance(String instance) {
		this.instance = instance;
	}
	public String getSpace() {
		return space;
	}
	public void setSpace(String space) {
		this.space = space;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getStatistics() {
		return statistics;
	}
	public void setStatistics(String statistics) {
		this.statistics = statistics;
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
	
	
	
/*	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_pref_id",this.getCheck_pref_id());
		objJson.put("check_pref_nm",this.getCheck_pref_nm());
		objJson.put("check_enable_yn",this.getCheck_enable_yn());
		objJson.put("check_value_unit",this.getCheck_value_unit());
		objJson.put("default_threshold_value",this.getDefault_threshold_value());
		objJson.put("check_grade_cd",this.getCheck_grade_cd());
		objJson.put("check_class_div_cd",this.getCheck_class_div_cd());
		objJson.put("emergency_action_yn",this.getEmergency_action_yn());
		
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("database",StringUtil.parseDouble(this.getDatabase(),0));
		objJson.put("instance",StringUtil.parseDouble(this.getInstance(),0));
		objJson.put("space",StringUtil.parseDouble(this.getSpace(),0));
		objJson.put("object",StringUtil.parseDouble(this.getObject(),0));
		objJson.put("statistics",StringUtil.parseDouble(this.getStatistics(),0));

		return objJson;
	}		*/
}
