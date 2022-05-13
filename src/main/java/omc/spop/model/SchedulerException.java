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

@Alias("schedulerException")
public class SchedulerException extends Base implements Jsonable {

	private String sched_type_cd;
	private String sched_except_seq;
	private String except_sbst;
	private String except_start_dt;
	private String except_end_dt;
	private String except_rpt_yn;
	private String except_rpt_freq_div_cd;
	private String except_rpt_std_div_cd;
	private String except_rpt_cycle;
	private String except_rpt_dayofweek;
	private String except_rpt_term_start_day;
	private String except_rpt_term_end_day;
	private String except_reg_dt;
	private String except_reg_id;
	private int except_rpt_start_time;
	private int except_rpt_end_time;

	public String getSched_type_cd() {
		return sched_type_cd;
	}

	public void setSched_type_cd(String sched_type_cd) {
		this.sched_type_cd = sched_type_cd;
	}

	public String getSched_except_seq() {
		return sched_except_seq;
	}

	public void setSched_except_seq(String sched_except_seq) {
		this.sched_except_seq = sched_except_seq;
	}

	public String getExcept_sbst() {
		return except_sbst;
	}

	public void setExcept_sbst(String except_sbst) {
		this.except_sbst = except_sbst;
	}

	public String getExcept_start_dt() {
		return except_start_dt;
	}

	public void setExcept_start_dt(String except_start_dt) {
		this.except_start_dt = except_start_dt;
	}

	public String getExcept_end_dt() {
		return except_end_dt;
	}

	public void setExcept_end_dt(String except_end_dt) {
		this.except_end_dt = except_end_dt;
	}

	public String getExcept_rpt_yn() {
		return except_rpt_yn;
	}

	public void setExcept_rpt_yn(String except_rpt_yn) {
		this.except_rpt_yn = except_rpt_yn;
	}

	public String getExcept_rpt_freq_div_cd() {
		return except_rpt_freq_div_cd;
	}

	public void setExcept_rpt_freq_div_cd(String except_rpt_freq_div_cd) {
		this.except_rpt_freq_div_cd = except_rpt_freq_div_cd;
	}

	public String getExcept_rpt_std_div_cd() {
		return except_rpt_std_div_cd;
	}

	public void setExcept_rpt_std_div_cd(String except_rpt_std_div_cd) {
		this.except_rpt_std_div_cd = except_rpt_std_div_cd;
	}

	public String getExcept_rpt_cycle() {
		return except_rpt_cycle;
	}

	public void setExcept_rpt_cycle(String except_rpt_cycle) {
		this.except_rpt_cycle = except_rpt_cycle;
	}

	public String getExcept_rpt_dayofweek() {
		return except_rpt_dayofweek;
	}

	public void setExcept_rpt_dayofweek(String except_rpt_dayofweek) {
		this.except_rpt_dayofweek = except_rpt_dayofweek;
	}

	public String getExcept_rpt_term_start_day() {
		return except_rpt_term_start_day;
	}

	public void setExcept_rpt_term_start_day(String except_rpt_term_start_day) {
		this.except_rpt_term_start_day = except_rpt_term_start_day;
	}

	public String getExcept_rpt_term_end_day() {
		return except_rpt_term_end_day;
	}

	public void setExcept_rpt_term_end_day(String except_rpt_term_end_day) {
		this.except_rpt_term_end_day = except_rpt_term_end_day;
	}

	public String getExcept_reg_dt() {
		return except_reg_dt;
	}

	public void setExcept_reg_dt(String except_reg_dt) {
		this.except_reg_dt = except_reg_dt;
	}

	public String getExcept_reg_id() {
		return except_reg_id;
	}

	public void setExcept_reg_id(String except_reg_id) {
		this.except_reg_id = except_reg_id;
	}

	public int getExcept_rpt_start_time() {
		return except_rpt_start_time;
	}

	public void setExcept_rpt_start_time(int except_rpt_start_time) {
		this.except_rpt_start_time = except_rpt_start_time;
	}

	public int getExcept_rpt_end_time() {
		return except_rpt_end_time;
	}

	public void setExcept_rpt_end_time(int except_rpt_end_time) {
		this.except_rpt_end_time = except_rpt_end_time;
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
