package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("parameterChangeCheck")
public class ParameterChangeCheck extends Base implements Jsonable {
	
    private String check_day;
    private String check_seq;
    private String inst_id;
    private String num;
    private String name;
    private String before_value;
    private String value;
    private String update_date;
    
    public String getCheck_day() {
		return check_day;
	}
	public void setCheck_day(String check_day) {
		this.check_day = check_day;
	}
	public String getCheck_seq() {
		return check_seq;
	}
	public void setCheck_seq(String check_seq) {
		this.check_seq = check_seq;
	}
	public String getInst_id() {
		return inst_id;
	}
	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBefore_value() {
		return before_value;
	}
	public void setBefore_value(String before_value) {
		this.before_value = before_value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the update_date
	 */
	public String getUpdate_date() {
		return update_date;
	}
	/**
	 * @param update_date the update_date to set
	 */
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("inst_id",this.getInst_id());
		objJson.put("num",StringUtil.parseDouble(this.getNum(),0));
		objJson.put("name",this.getName());
		objJson.put("value",this.getValue());
		objJson.put("before_value",this.getBefore_value());
		objJson.put("update_date",this.getUpdate_date());
		
		return objJson;
	}		
}
