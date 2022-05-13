package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.10.16	이원식	최초작성
 **********************************************************/

@Alias("vsqlBindCapture")
public class VsqlBindCapture extends Base implements Jsonable {
	
    private String sql_id;
    private String position;
    private String name;
    private String value_string;
    private String datatype_string;
    private String last_captured;
    
	public String getSql_id() {
		return sql_id;
	}
	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue_string() {
		return value_string;
	}
	public void setValue_string(String value_string) {
		this.value_string = value_string;
	}
	public String getDatatype_string() {
		return datatype_string;
	}
	public void setDatatype_string(String datatype_string) {
		this.datatype_string = datatype_string;
	}
	public String getLast_captured() {
		return last_captured;
	}
	public void setLast_captured(String last_captured) {
		this.last_captured = last_captured;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("dbid",this.getDbid());
		objJson.put("sql_id",this.getSql_id());
		objJson.put("position",this.getPosition());
		objJson.put("name",this.getName());
		objJson.put("value_string",this.getValue_string());
		objJson.put("datatype_string",this.getDatatype_string());
		objJson.put("last_captured",this.getLast_captured());
		
		return objJson;
	}		
}