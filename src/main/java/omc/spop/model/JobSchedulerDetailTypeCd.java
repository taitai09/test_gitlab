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

@Alias("jobSchedulerDetailTypeCd")  //(필요없음)
public class JobSchedulerDetailTypeCd extends Base implements Jsonable {
	
	private String job_scheduler_type_cd;
	private String job_scheduler_type_cd_nm;
	private String job_scheduler_detail_type_cd;
	private String job_scheduler_detail_type_nm;
	private String job_scheduler_detail_type_desc;

	
	public String getJob_scheduler_type_cd() {
		return job_scheduler_type_cd;
	}




	public void setJob_scheduler_type_cd(String job_scheduler_type_cd) {
		this.job_scheduler_type_cd = job_scheduler_type_cd;
	}




	public String getJob_scheduler_type_cd_nm() {
		return job_scheduler_type_cd_nm;
	}




	public void setJob_scheduler_type_cd_nm(String job_scheduler_type_cd_nm) {
		this.job_scheduler_type_cd_nm = job_scheduler_type_cd_nm;
	}




	public String getJob_scheduler_detail_type_cd() {
		return job_scheduler_detail_type_cd;
	}




	public void setJob_scheduler_detail_type_cd(String job_scheduler_detail_type_cd) {
		this.job_scheduler_detail_type_cd = job_scheduler_detail_type_cd;
	}




	public String getJob_scheduler_detail_type_nm() {
		return job_scheduler_detail_type_nm;
	}




	public void setJob_scheduler_detail_type_nm(String job_scheduler_detail_type_nm) {
		this.job_scheduler_detail_type_nm = job_scheduler_detail_type_nm;
	}




	public String getJob_scheduler_detail_type_desc() {
		return job_scheduler_detail_type_desc;
	}




	public void setJob_scheduler_detail_type_desc(String job_scheduler_detail_type_desc) {
		this.job_scheduler_detail_type_desc = job_scheduler_detail_type_desc;
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
