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
 * 2017.08.10 이원식 최초작성
 **********************************************************/

@Alias("projectTuningPrgrsStep")
public class ProjectTuningPrgrsStep extends Base implements Jsonable {

	private String project_id;
	private String project_nm;
	private String tuning_prgrs_step_seq;
	private String tuning_prgrs_step_nm;
	private String tuning_prgrs_step_desc;
	private String del_yn;
	private String isAll;
	private String isChoice;
	private String isNotApplicable;

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getProject_nm() {
		return project_nm;
	}

	public void setProject_nm(String project_nm) {
		this.project_nm = project_nm;
	}

	public String getTuning_prgrs_step_seq() {
		return tuning_prgrs_step_seq;
	}

	public String getTuning_prgrs_step_nm() {
		return tuning_prgrs_step_nm;
	}

	public String getTuning_prgrs_step_desc() {
		return tuning_prgrs_step_desc;
	}

	public void setTuning_prgrs_step_seq(String tuning_prgrs_step_seq) {
		this.tuning_prgrs_step_seq = tuning_prgrs_step_seq;
	}

	public void setTuning_prgrs_step_nm(String tuning_prgrs_step_nm) {
		this.tuning_prgrs_step_nm = tuning_prgrs_step_nm;
	}

	public void setTuning_prgrs_step_desc(String tuning_prgrs_step_desc) {
		this.tuning_prgrs_step_desc = tuning_prgrs_step_desc;
	}

	public String getDel_yn() {
		return del_yn;
	}

	public void setDel_yn(String del_yn) {
		this.del_yn = del_yn;
	}

	public String getIsAll() {
		return isAll;
	}

	public String getIsChoice() {
		return isChoice;
	}

	public void setIsAll(String isAll) {
		this.isAll = isAll;
	}

	public void setIsChoice(String isChoice) {
		this.isChoice = isChoice;
	}

	public String getIsNotApplicable() {
		return isNotApplicable;
	}

	public void setIsNotApplicable(String isNotApplicable) {
		this.isNotApplicable = isNotApplicable;
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
