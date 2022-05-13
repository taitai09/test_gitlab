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
 * 2019.10.23 명성태		최초작성
 **********************************************************/

@Alias("diagnosisReport")
public class DiagnosisReport extends Base implements Jsonable {
	private String contents_id;
	private String parent_contents_id;
	private String contents_name;
	private int contents_ordering;
	private int level;
	private int slt_program_sql_number;
	private String slt_program_chk_sql;
	private String sql;
	private String slt_program_sql_name;
	private String exadata_yn;				// Web에서 받은 exadata 판별 여부
	private String exadata_contents_yn;
	private String sql_std_qty_chkt_id;
	private String sql_std_qty_div_cd;
	private String slt_program_div_cd;
	
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

	
	public String getSlt_program_div_cd() {
		return slt_program_div_cd;
	}


	public void setSlt_program_div_cd(String slt_program_div_cd) {
		this.slt_program_div_cd = slt_program_div_cd;
	}


	public String getSql_std_qty_div_cd() {
		return sql_std_qty_div_cd;
	}


	public void setSql_std_qty_div_cd(String sql_std_qty_div_cd) {
		this.sql_std_qty_div_cd = sql_std_qty_div_cd;
	}


	public String getSql_std_qty_chkt_id() {
		return sql_std_qty_chkt_id;
	}


	public void setSql_std_qty_chkt_id(String sql_std_qty_chkt_id) {
		this.sql_std_qty_chkt_id = sql_std_qty_chkt_id;
	}


	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public String getContents_id() {
		return contents_id;
	}

	public String getParent_contents_id() {
		return parent_contents_id;
	}

	public String getContents_name() {
		return contents_name;
	}

	public int getContents_ordering() {
		return contents_ordering;
	}

	public void setContents_id(String contents_id) {
		this.contents_id = contents_id;
	}

	public void setParent_contents_id(String parent_contents_id) {
		this.parent_contents_id = parent_contents_id;
	}

	public void setContents_name(String contents_name) {
		this.contents_name = contents_name;
	}

	public void setContents_ordering(int contents_ordering) {
		this.contents_ordering = contents_ordering;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSlt_program_sql_number() {
		return slt_program_sql_number;
	}

	public void setSlt_program_sql_number(int slt_program_sql_number) {
		this.slt_program_sql_number = slt_program_sql_number;
	}

	public String getSlt_program_chk_sql() {
		return slt_program_chk_sql;
	}

	public void setSlt_program_chk_sql(String slt_program_chk_sql) {
		this.slt_program_chk_sql = slt_program_chk_sql;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getSlt_program_sql_name() {
		return slt_program_sql_name;
	}

	public void setSlt_program_sql_name(String slt_program_sql_name) {
		this.slt_program_sql_name = slt_program_sql_name;
	}

	public String getExadata_yn() {
		return exadata_yn;
	}

	public void setExadata_yn(String exadata_yn) {
		this.exadata_yn = exadata_yn;
	}

	public String getExadata_contents_yn() {
		return exadata_contents_yn;
	}

	public void setExadata_contents_yn(String exadata_contents_yn) {
		this.exadata_contents_yn = exadata_contents_yn;
	}
}
