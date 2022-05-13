package omc.mqm.model;

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

@Alias("modelEntityType")
public class ModelEntityType extends Base implements Jsonable {

	private String project_id;
	private String cd;
	private String cd_nm;
	private String lib_nm;
	private String model_nm;
	private String sub_nm;
	private String ent_type_cd;
	private String ref_ent_type_nm;
	private String ent_type_nm;
	private String tbl_type_nm;
	private String tbl_type_cd;
	private String ent_type_desc;

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getLib_nm() {
		return lib_nm;
	}

	public void setLib_nm(String lib_nm) {
		this.lib_nm = lib_nm;
	}

	public String getModel_nm() {
		return model_nm;
	}

	public void setModel_nm(String model_nm) {
		this.model_nm = model_nm;
	}

	public String getSub_nm() {
		return sub_nm;
	}

	public void setSub_nm(String sub_nm) {
		this.sub_nm = sub_nm;
	}

	public String getCd() {
		return cd;
	}

	public void setCd(String cd) {
		this.cd = cd;
	}

	public String getCd_nm() {
		return cd_nm;
	}

	public void setCd_nm(String cd_nm) {
		this.cd_nm = cd_nm;
	}

	public String getEnt_type_cd() {
		return ent_type_cd;
	}

	public void setEnt_type_cd(String ent_type_cd) {
		this.ent_type_cd = ent_type_cd;
	}

	public String getRef_ent_type_nm() {
		return ref_ent_type_nm;
	}

	public void setRef_ent_type_nm(String ref_ent_type_nm) {
		this.ref_ent_type_nm = ref_ent_type_nm;
	}

	public String getEnt_type_nm() {
		return ent_type_nm;
	}

	public void setEnt_type_nm(String ent_type_nm) {
		this.ent_type_nm = ent_type_nm;
	}

	public String getTbl_type_nm() {
		return tbl_type_nm;
	}

	public void setTbl_type_nm(String tbl_type_nm) {
		this.tbl_type_nm = tbl_type_nm;
	}

	public String getTbl_type_cd() {
		return tbl_type_cd;
	}

	public void setTbl_type_cd(String tbl_type_cd) {
		this.tbl_type_cd = tbl_type_cd;
	}

	public String getEnt_type_desc() {
		return ent_type_desc;
	}

	public void setEnt_type_desc(String ent_type_desc) {
		this.ent_type_desc = ent_type_desc;
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
