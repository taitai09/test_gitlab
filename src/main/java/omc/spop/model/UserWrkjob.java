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
 * 2018.01.04 이원식 최초작성
 **********************************************************/

@Alias("userWrkjob")
public class UserWrkjob extends Base implements Jsonable {
	/**담당어플리케이션코드*/
	private String charge_app_cd;
	private String wrkjob_cd;
	private String old_wrkjob_cd;
	private String wrkjob_nm;
	private String workjob_start_day;
	private String workjob_end_day;
	private String leader_yn;
	private String default_wrkjob_yn;
	private String default_wrkjob_cd;
	private String default_wrkjob_cd_nm;
	private String old_default_wrkjob_yn;
	private int cnt;
	
	
	public UserWrkjob() {
		super();
	}

	public UserWrkjob(int cnt) {
		this.cnt = cnt;
	}


	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public String getOld_default_wrkjob_yn() {
		return old_default_wrkjob_yn;
	}

	public void setOld_default_wrkjob_yn(String old_default_wrkjob_yn) {
		this.old_default_wrkjob_yn = old_default_wrkjob_yn;
	}

	public String getOld_wrkjob_cd() {
		return old_wrkjob_cd;
	}

	public void setOld_wrkjob_cd(String old_wrkjob_cd) {
		this.old_wrkjob_cd = old_wrkjob_cd;
	}

	public String getDefault_wrkjob_yn() {
		return default_wrkjob_yn;
	}

	public void setDefault_wrkjob_yn(String default_wrkjob_yn) {
		this.default_wrkjob_yn = default_wrkjob_yn;
	}

	public String getDefault_wrkjob_cd() {
		return default_wrkjob_cd;
	}

	public void setDefault_wrkjob_cd(String default_wrkjob_cd) {
		this.default_wrkjob_cd = default_wrkjob_cd;
	}

	public String getDefault_wrkjob_cd_nm() {
		return default_wrkjob_cd_nm;
	}

	public void setDefault_wrkjob_cd_nm(String default_wrkjob_cd_nm) {
		this.default_wrkjob_cd_nm = default_wrkjob_cd_nm;
	}

	private String work_comp_day;

	public String getWrkjob_cd() {
		return wrkjob_cd;
	}

	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}

	public String getWrkjob_nm() {
		return wrkjob_nm;
	}

	public void setWrkjob_nm(String wrkjob_nm) {
		this.wrkjob_nm = wrkjob_nm;
	}

	public String getWorkjob_start_day() {
		return workjob_start_day;
	}

	public void setWorkjob_start_day(String workjob_start_day) {
		this.workjob_start_day = workjob_start_day;
	}

	public String getWorkjob_end_day() {
		return workjob_end_day;
	}

	public void setWorkjob_end_day(String workjob_end_day) {
		this.workjob_end_day = workjob_end_day;
	}

	public String getLeader_yn() {
		return leader_yn;
	}

	public void setLeader_yn(String leader_yn) {
		this.leader_yn = leader_yn;
	}

	public String getWork_comp_day() {
		return work_comp_day;
	}

	public void setWork_comp_day(String work_comp_day) {
		this.work_comp_day = work_comp_day;
	}

	// @SuppressWarnings("unchecked")
	// public JSONObject toJSONObject() {
	// JSONObject objJson = new JSONObject();
	//
	// objJson.put("user_id", this.getUser_id());
	// objJson.put("user_nm", this.getUser_nm());
	// objJson.put("wrkjob_cd", this.getWrkjob_cd());
	// objJson.put("wrkjob_nm", this.getWrkjob_nm());
	// objJson.put("workjob_start_day", this.getWorkjob_start_day());
	// objJson.put("workjob_end_day", this.getWorkjob_end_day());
	// objJson.put("leader_yn", this.getLeader_yn());
	//
	// return objJson;
	// }

	public String getCharge_app_cd() {
		return charge_app_cd;
	}

	public void setCharge_app_cd(String charge_app_cd) {
		this.charge_app_cd = charge_app_cd;
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
