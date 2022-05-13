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

@Alias("jobSchedulerDependency")
public class JobSchedulerDependency extends Base implements Jsonable {

	private String job_scheduler_type_cd;
	private String dpnd_job_sched_type_cd;
	private String dpnd_job_sched_detail_type_cd;

	public String getJob_scheduler_type_cd() {
		return job_scheduler_type_cd;
	}

	public void setJob_scheduler_type_cd(String job_scheduler_type_cd) {
		this.job_scheduler_type_cd = job_scheduler_type_cd;
	}

	public String getDpnd_job_sched_type_cd() {
		return dpnd_job_sched_type_cd;
	}

	public void setDpnd_job_sched_type_cd(String dpnd_job_sched_type_cd) {
		this.dpnd_job_sched_type_cd = dpnd_job_sched_type_cd;
	}

	public String getDpnd_job_sched_detail_type_cd() {
		return dpnd_job_sched_detail_type_cd;
	}

	public void setDpnd_job_sched_detail_type_cd(String dpnd_job_sched_detail_type_cd) {
		this.dpnd_job_sched_detail_type_cd = dpnd_job_sched_detail_type_cd;
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
