package omc.spop.model;

import java.sql.PreparedStatement;
import java.util.LinkedHashMap;
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
 * 2019.12.03 명성태 최초작성
 **********************************************************/

@Alias("sqlEditor")
public class SQLEditor extends Base implements Jsonable {
	
	private String sql_text;
	private LinkedHashMap<String, Object> columnMetaData;
	private int base_commnad_type;
	private int rownum;
	private String conn_info;
	
	private PreparedStatement ps;
	
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

	public String getSql_text() {
		return sql_text;
	}

	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
	}

	public LinkedHashMap<String, Object> getColumnMetaData() {
		return columnMetaData;
	}

	public void setColumnMetaData(LinkedHashMap<String, Object> columnMetaData) {
		this.columnMetaData = columnMetaData;
	}

	public int getBase_commnad_type() {
		return base_commnad_type;
	}

	public void setBase_commnad_type(int base_commnad_type) {
		this.base_commnad_type = base_commnad_type;
	}

	public int getRownum() {
		return rownum;
	}

	public void setRownum(int rownum) {
		this.rownum = rownum;
	}

	public String getConn_info() {
		return conn_info;
	}

	public void setConn_info(String conn_info) {
		this.conn_info = conn_info;
	}

	public PreparedStatement getPs() {
		return ps;
	}

	public void setPs(PreparedStatement ps) {
		this.ps = ps;
	}

}