package omc.spop.model;

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
 * 2018.08.30	임호경	최초작성
 **********************************************************/

@Alias("trCd")
public class TrCd extends Base implements Jsonable {
	
	private String wrkjob_cd;
	private String wrkjob_cd_nm;
	private String tr_cd;
	private String tr_cd_nm;
	private String mgr_id;
	private String reg_dt;
	private String elapsed_time_threshold;        
	private String buffer_gets_threshold;        
	private String chk_tr_cd;
	private String chk_wrkjob_cd;
	
	
	public String getChk_tr_cd() {
		return chk_tr_cd;
	}

	public void setChk_tr_cd(String chk_tr_cd) {
		this.chk_tr_cd = chk_tr_cd;
	}

	public String getChk_wrkjob_cd() {
		return chk_wrkjob_cd;
	}

	public void setChk_wrkjob_cd(String chk_wrkjob_cd) {
		this.chk_wrkjob_cd = chk_wrkjob_cd;
	}

	public String getWrkjob_cd_nm() {
		return wrkjob_cd_nm;
	}

	public void setWrkjob_cd_nm(String wrkjob_cd_nm) {
		this.wrkjob_cd_nm = wrkjob_cd_nm;
	}

	public String getWrkjob_cd() {
		return wrkjob_cd;
	}

	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}

	public String getTr_cd() {
		return tr_cd;
	}

	public void setTr_cd(String tr_cd) {
		this.tr_cd = tr_cd;
	}

	public String getTr_cd_nm() {
		return tr_cd_nm;
	}

	public void setTr_cd_nm(String tr_cd_nm) {
		this.tr_cd_nm = tr_cd_nm;
	}

	public String getMgr_id() {
		return mgr_id;
	}

	public void setMgr_id(String mgr_id) {
		this.mgr_id = mgr_id;
	}

	public String getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}


	public String getElapsed_time_threshold() {
		return elapsed_time_threshold;
	}

	public void setElapsed_time_threshold(String elapsed_time_threshold) {
		this.elapsed_time_threshold = elapsed_time_threshold;
	}

	public String getBuffer_gets_threshold() {
		return buffer_gets_threshold;
	}

	public void setBuffer_gets_threshold(String buffer_gets_threshold) {
		this.buffer_gets_threshold = buffer_gets_threshold;
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
