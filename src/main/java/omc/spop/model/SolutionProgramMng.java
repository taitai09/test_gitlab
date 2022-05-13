package omc.spop.model;

import java.util.ArrayList;
import java.util.List;
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
 * 2017.08.10 이원식 최초작성
 **********************************************************/

@Alias("solutionProgramMng")
public class SolutionProgramMng extends Base implements Jsonable {

	private String rnum;
	private String id;
	private String text;
	private String slt_program_div_nm;
	private String contents_id;
	private String path;
	private String contents_desc;
	private String contents_url_addr;
	private String exadata_contents_yn;
	private String contents_ordering;
	private String use_yn;
	private String level;
	private String contents_name;
	private String parent_contents_id;
	private String slt_program_div_cd;
	private String contents_id_list;
	private String contents_ordering_list;
	
	private List<SolutionProgramMng> children = new ArrayList<SolutionProgramMng>();

	
	private String slt_program_sql_number;
	private String slt_program_sql_name;
	private String slt_program_sql_desc;
	private String slt_program_chk_sql;
	private String slt_encrypted_sql;
	private String base_day;
	
	public String getSlt_encrypted_sql() {
		return slt_encrypted_sql;
	}

	public void setSlt_encrypted_sql(String slt_encrypted_sql) {
		this.slt_encrypted_sql = slt_encrypted_sql;
	}

	public String getBase_day() {
		return base_day;
	}

	public void setBase_day(String base_day) {
		this.base_day = base_day;
	}

	public String getSlt_program_sql_number() {
		return slt_program_sql_number;
	}

	public void setSlt_program_sql_number(String slt_program_sql_number) {
		this.slt_program_sql_number = slt_program_sql_number;
	}

	public String getSlt_program_sql_name() {
		return slt_program_sql_name;
	}

	public void setSlt_program_sql_name(String slt_program_sql_name) {
		this.slt_program_sql_name = slt_program_sql_name;
	}

	public String getSlt_program_sql_desc() {
		return slt_program_sql_desc;
	}

	public void setSlt_program_sql_desc(String slt_program_sql_desc) {
		this.slt_program_sql_desc = slt_program_sql_desc;
	}

	public String getSlt_program_chk_sql() {
		return slt_program_chk_sql;
	}

	public void setSlt_program_chk_sql(String slt_program_chk_sql) {
		this.slt_program_chk_sql = slt_program_chk_sql;
	}

	public String getContents_id_list() {
		return contents_id_list;
	}

	public void setContents_id_list(String contents_id_list) {
		this.contents_id_list = contents_id_list;
	}

	public String getContents_ordering_list() {
		return contents_ordering_list;
	}

	public void setContents_ordering_list(String contents_ordering_list) {
		this.contents_ordering_list = contents_ordering_list;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	public List<SolutionProgramMng> getChildren() {
		return children;
	}
	public void setChildren(List<SolutionProgramMng> children) {
		this.children = children;
	}

	public String getRnum() {
		return rnum;
	}

	public void setRnum(String rnum) {
		this.rnum = rnum;
	}

	public String getSlt_program_div_nm() {
		return slt_program_div_nm;
	}

	public void setSlt_program_div_nm(String slt_program_div_nm) {
		this.slt_program_div_nm = slt_program_div_nm;
	}


	public String getContents_id() {
		return contents_id;
	}

	public void setContents_id(String contents_id) {
		this.contents_id = contents_id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getContents_desc() {
		return contents_desc;
	}

	public void setContents_desc(String contents_desc) {
		this.contents_desc = contents_desc;
	}

	public String getContents_url_addr() {
		return contents_url_addr;
	}

	public void setContents_url_addr(String contents_url_addr) {
		this.contents_url_addr = contents_url_addr;
	}

	public String getExadata_contents_yn() {
		return exadata_contents_yn;
	}

	public void setExadata_contents_yn(String exadata_contents_yn) {
		this.exadata_contents_yn = exadata_contents_yn;
	}

	public String getContents_ordering() {
		return contents_ordering;
	}

	public void setContents_ordering(String contents_ordering) {
		this.contents_ordering = contents_ordering;
	}

	public String getUse_yn() {
		return use_yn;
	}

	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getContents_name() {
		return contents_name;
	}

	public void setContents_name(String contents_name) {
		this.contents_name = contents_name;
	}

	public String getParent_contents_id() {
		return parent_contents_id;
	}

	public void setParent_contents_id(String parent_contents_id) {
		this.parent_contents_id = parent_contents_id;
	}

	public String getSlt_program_div_cd() {
		return slt_program_div_cd;
	}

	public void setSlt_program_div_cd(String slt_program_div_cd) {
		this.slt_program_div_cd = slt_program_div_cd;
	}

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

}
