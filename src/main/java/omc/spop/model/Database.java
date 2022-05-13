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
 * 2017.09.20	이원식	최초작성
 * 2021.10.27	이재우	DATABASE_KINDS_CD(DB종류 코드) 추가.
 **********************************************************/

@Alias("database")
public class Database extends Base implements Jsonable {
	private String db_abbr_nm;
	private String ordering;
	private String use_yn;
	private String snapshot_agent_inst_id;
	private String d_1_agent_inst_id;
	private String oracle_user_id;
	private String oracle_user_password;
	private String db_operate_type_cd;
	private String db_operate_type_nm;
	private String collect_inst_id;
	private String gather_inst_id;
	private int cnt;
	private String characterset;
	private String rgb_color_value;
	private String rgb_color_id;
	private String isBase;
	private String old_db_name;
	private String exadata_yn;
	
	public String getExadata_yn() {
		return exadata_yn;
	}
	public void setExadata_yn(String exadata_yn) {
		this.exadata_yn = exadata_yn;
	}
	public String getOld_db_name() {
		return old_db_name;
	}
	public void setOld_db_name(String old_db_name) {
		this.old_db_name = old_db_name;
	}
	public String getDb_abbr_nm() {
		return db_abbr_nm;
	}
	public void setDb_abbr_nm(String db_abbr_nm) {
		this.db_abbr_nm = db_abbr_nm;
	}
	public String getOrdering() {
		return ordering;
	}
	public void setOrdering(String ordering) {
		this.ordering = ordering;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	public String getSnapshot_agent_inst_id() {
		return snapshot_agent_inst_id;
	}
	public void setSnapshot_agent_inst_id(String snapshot_agent_inst_id) {
		this.snapshot_agent_inst_id = snapshot_agent_inst_id;
	}
	public String getD_1_agent_inst_id() {
		return d_1_agent_inst_id;
	}
	public void setD_1_agent_inst_id(String d_1_agent_inst_id) {
		this.d_1_agent_inst_id = d_1_agent_inst_id;
	}
	public String getOracle_user_id() {
		return oracle_user_id;
	}
	public void setOracle_user_id(String oracle_user_id) {
		this.oracle_user_id = oracle_user_id;
	}
	public String getOracle_user_password() {
		return oracle_user_password;
	}
	public void setOracle_user_password(String oracle_user_password) {
		this.oracle_user_password = oracle_user_password;
	}	
	public String getDb_operate_type_cd() {
		return db_operate_type_cd;
	}
	public void setDb_operate_type_cd(String db_operate_type_cd) {
		this.db_operate_type_cd = db_operate_type_cd;
	}	
	public String getDb_operate_type_nm() {
		return db_operate_type_nm;
	}
	public void setDb_operate_type_nm(String db_operate_type_nm) {
		this.db_operate_type_nm = db_operate_type_nm;
	}	
	
	public String getCollect_inst_id() {
		return collect_inst_id;
	}
	public void setCollect_inst_id(String collect_inst_id) {
		this.collect_inst_id = collect_inst_id;
	}
	public String getGather_inst_id() {
		return gather_inst_id;
	}
	public void setGather_inst_id(String gather_inst_id) {
		this.gather_inst_id = gather_inst_id;
	}
	
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public String getCharacterset() {
		return characterset;
	}
	public void setCharacterset(String characterset) {
		this.characterset = characterset;
	}
	public String getRgb_color_value() {
		return rgb_color_value;
	}
	public void setRgb_color_value(String rgb_color_value) {
		this.rgb_color_value = rgb_color_value;
	}
	public String getRgb_color_id() {
		return rgb_color_id;
	}
	public void setRgb_color_id(String rgb_color_id) {
		this.rgb_color_id = rgb_color_id;
	}
	public String getIsBase() {
		return isBase;
	}
	public void setIsBase(String isBase) {
		this.isBase = isBase;
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
