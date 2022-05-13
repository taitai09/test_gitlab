package omc.spop.model;

import java.math.BigDecimal;
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
 * 2017.10.30	이원식	최초작성
 **********************************************************/

@Alias("odsApmSAppl")
public class OdsApmSAppl extends Base implements Jsonable {
	private String wrkjob_cd;
	private String appl_hash;
	private String log_dt;
	private String agent_id;
	private String cnt;
	private BigDecimal fail_cnt;
	private String elapsed_sum;
	private String elapsed2_sum;
	private String elapsed_min;
	private String elapsed_max;
	private String cpu_sum;
	private String tpmc_sum;
	private BigDecimal executions;
	private BigDecimal avg_elapsed_time;
	private BigDecimal min_elapsed_time;
	private BigDecimal max_elapsed_time;
	

	public String getWrkjob_cd() {
		return wrkjob_cd;
	}


	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}


	public String getAppl_hash() {
		return appl_hash;
	}


	public void setAppl_hash(String appl_hash) {
		this.appl_hash = appl_hash;
	}


	public String getLog_dt() {
		return log_dt;
	}


	public void setLog_dt(String log_dt) {
		this.log_dt = log_dt;
	}


	public String getAgent_id() {
		return agent_id;
	}


	public void setAgent_id(String agent_id) {
		this.agent_id = agent_id;
	}


	public String getCnt() {
		return cnt;
	}


	public void setCnt(String cnt) {
		this.cnt = cnt;
	}


	public BigDecimal getFail_cnt() {
		return fail_cnt;
	}


	public void setFail_cnt(BigDecimal fail_cnt) {
		this.fail_cnt = fail_cnt;
	}


	public String getElapsed_sum() {
		return elapsed_sum;
	}


	public void setElapsed_sum(String elapsed_sum) {
		this.elapsed_sum = elapsed_sum;
	}


	public String getElapsed2_sum() {
		return elapsed2_sum;
	}


	public void setElapsed2_sum(String elapsed2_sum) {
		this.elapsed2_sum = elapsed2_sum;
	}


	public String getElapsed_min() {
		return elapsed_min;
	}


	public void setElapsed_min(String elapsed_min) {
		this.elapsed_min = elapsed_min;
	}


	public String getElapsed_max() {
		return elapsed_max;
	}


	public void setElapsed_max(String elapsed_max) {
		this.elapsed_max = elapsed_max;
	}


	public String getCpu_sum() {
		return cpu_sum;
	}


	public void setCpu_sum(String cpu_sum) {
		this.cpu_sum = cpu_sum;
	}


	public String getTpmc_sum() {
		return tpmc_sum;
	}


	public void setTpmc_sum(String tpmc_sum) {
		this.tpmc_sum = tpmc_sum;
	}


	public BigDecimal getExecutions() {
		return executions;
	}


	public void setExecutions(BigDecimal executions) {
		this.executions = executions;
	}


	public BigDecimal getAvg_elapsed_time() {
		return avg_elapsed_time;
	}


	public void setAvg_elapsed_time(BigDecimal avg_elapsed_time) {
		this.avg_elapsed_time = avg_elapsed_time;
	}


	public BigDecimal getMin_elapsed_time() {
		return min_elapsed_time;
	}


	public void setMin_elapsed_time(BigDecimal min_elapsed_time) {
		this.min_elapsed_time = min_elapsed_time;
	}


	public BigDecimal getMax_elapsed_time() {
		return max_elapsed_time;
	}


	public void setMax_elapsed_time(BigDecimal max_elapsed_time) {
		this.max_elapsed_time = max_elapsed_time;
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