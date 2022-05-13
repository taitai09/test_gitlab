package omc.mqm.model;

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
 * 2019.05.08	임승률	최초작성
 **********************************************************/

@Alias("openqErrCnt")
public class OpenqErrCnt extends Base implements Jsonable {
	
	private String project_id;
	private String extrac_dt;
	private String lib_nm;
	private String lib_cd;
	private String model_nm;
	private String model_cd;
	private String err_type_cd;
	private String ent_cnt;
	private String att_cnt;
	private String err_cnt;
		
	public String getLib_cd() {
		return lib_cd;
	}

	public void setLib_cd(String lib_cd) {
		this.lib_cd = lib_cd;
	}

	public String getModel_cd() {
		return model_cd;
	}

	public void setModel_cd(String model_cd) {
		this.model_cd = model_cd;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getExtrac_dt() {
		return extrac_dt;
	}

	public void setExtrac_dt(String extrac_dt) {
		this.extrac_dt = extrac_dt;
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

	public String getErr_type_cd() {
		return err_type_cd;
	}

	public void setErr_type_cd(String err_type_cd) {
		this.err_type_cd = err_type_cd;
	}

	public String getEnt_cnt() {
		return ent_cnt;
	}

	public void setEnt_cnt(String ent_cnt) {
		this.ent_cnt = ent_cnt;
	}

	public String getAtt_cnt() {
		return att_cnt;
	}

	public void setAtt_cnt(String att_cnt) {
		this.att_cnt = att_cnt;
	}

	public String getErr_cnt() {
		return err_cnt;
	}

	public void setErr_cnt(String err_cnt) {
		this.err_cnt = err_cnt;
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
