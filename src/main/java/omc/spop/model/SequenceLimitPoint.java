package omc.spop.model;

import java.math.BigDecimal;
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
 * 2018.06.14 반광수 최초작성 (Sequence 한계점예측)
 **********************************************************/

@Alias("sequenceLimitPoint")
public class SequenceLimitPoint extends Base implements Jsonable {

	private String inst_id;
	private String inst_nm;
	private double baseline_cpu_usage = 0;
	private double current_cpu_usage = 0;
	private double cpu_increase_ratio = 0;
	private String snap_dt;
	private String snap_time;
	private double baseline_user_time = 0;
	private double current_user_time = 0;
	private double baseline_sys_time = 0;
	private double current_sys_time = 0;
	private String stat_name;
	private double baseline_time_model = 0;
	private double current_time_model = 0;
	private String sql_id;
	private double cnt = 0;
	private double activity = 0;
	private String sql_text;
	
	private String predict_basic_day;
	private String sequence_owner;
	private String sequence_name;
	private String sequence_max_value;
	private BigDecimal before_3_month_sequence_ratio;
	private BigDecimal before_2_month_sequence_ratio;
	private BigDecimal before_1_month_sequence_ratio;
	private BigDecimal current_sequence_ratio;
	private BigDecimal after_1_month_sequence_ratio;
	private BigDecimal after_2_month_sequence_ratio;
	private BigDecimal after_3_month_sequence_ratio;
	private BigDecimal after_6_month_sequence_ratio;
	private BigDecimal after_12_month_sequence_ratio;
	
	private String prediction_dt;
	private BigDecimal sequence_threshold_value;
	private BigDecimal sequence_last_number;
	private BigDecimal past_sequence_value_trend;
	private BigDecimal future_sequence_value_trend;
	private BigDecimal sequence_threshold_usage;
	private BigDecimal sequence_last_number_usage;
	private BigDecimal past_sequence_usage_trend;
	private BigDecimal future_sequence_usage_trend;

	private String month_sequence_ratio;
	private String period;
	private String predict_standard;
	private String current_sequence_value;
	
	public String getInst_id() {
		return inst_id;
	}

	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}

	public String getInst_nm() {
		return inst_nm;
	}

	public void setInst_nm(String inst_nm) {
		this.inst_nm = inst_nm;
	}

	public double getBaseline_cpu_usage() {
		return baseline_cpu_usage;
	}

	public void setBaseline_cpu_usage(double baseline_cpu_usage) {
		this.baseline_cpu_usage = baseline_cpu_usage;
	}

	public double getCurrent_cpu_usage() {
		return current_cpu_usage;
	}

	public void setCurrent_cpu_usage(double current_cpu_usage) {
		this.current_cpu_usage = current_cpu_usage;
	}

	public double getCpu_increase_ratio() {
		return cpu_increase_ratio;
	}

	public void setCpu_increase_ratio(double cpu_increase_ratio) {
		this.cpu_increase_ratio = cpu_increase_ratio;
	}

	public String getSnap_dt() {
		return snap_dt;
	}

	public void setSnap_dt(String snap_dt) {
		this.snap_dt = snap_dt;
	}

	public String getSnap_time() {
		return snap_time;
	}

	public void setSnap_time(String snap_time) {
		this.snap_time = snap_time;
	}

	public double getBaseline_user_time() {
		return baseline_user_time;
	}

	public void setBaseline_user_time(double baseline_user_time) {
		this.baseline_user_time = baseline_user_time;
	}

	public double getCurrent_user_time() {
		return current_user_time;
	}

	public void setCurrent_user_time(double current_user_time) {
		this.current_user_time = current_user_time;
	}

	public double getBaseline_sys_time() {
		return baseline_sys_time;
	}

	public void setBaseline_sys_time(double baseline_sys_time) {
		this.baseline_sys_time = baseline_sys_time;
	}

	public double getCurrent_sys_time() {
		return current_sys_time;
	}

	public void setCurrent_sys_time(double current_sys_time) {
		this.current_sys_time = current_sys_time;
	}

	public String getStat_name() {
		return stat_name;
	}

	public void setStat_name(String stat_name) {
		this.stat_name = stat_name;
	}

	public double getBaseline_time_model() {
		return baseline_time_model;
	}

	public void setBaseline_time_model(double baseline_time_model) {
		this.baseline_time_model = baseline_time_model;
	}

	public double getCurrent_time_model() {
		return current_time_model;
	}

	public void setCurrent_time_model(double current_time_model) {
		this.current_time_model = current_time_model;
	}

	public String getSql_id() {
		return sql_id;
	}

	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}

	public double getCnt() {
		return cnt;
	}

	public void setCnt(double cnt) {
		this.cnt = cnt;
	}

	public double getActivity() {
		return activity;
	}

	public void setActivity(double activity) {
		this.activity = activity;
	}

	public String getSql_text() {
		return sql_text;
	}

	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
	}

	public String getPredict_basic_day() {
		return predict_basic_day;
	}

	public void setPredict_basic_day(String predict_basic_day) {
		this.predict_basic_day = predict_basic_day;
	}

	public String getSequence_owner() {
		return sequence_owner;
	}

	public void setSequence_owner(String sequence_owner) {
		this.sequence_owner = sequence_owner;
	}

	public String getSequence_name() {
		return sequence_name;
	}

	public void setSequence_name(String sequence_name) {
		this.sequence_name = sequence_name;
	}

	public String getSequence_max_value() {
		return sequence_max_value;
	}

	public void setSequence_max_value(String sequence_max_value) {
		this.sequence_max_value = sequence_max_value;
	}

	public BigDecimal getBefore_3_month_sequence_ratio() {
		return before_3_month_sequence_ratio;
	}

	public void setBefore_3_month_sequence_ratio(BigDecimal before_3_month_sequence_ratio) {
		this.before_3_month_sequence_ratio = before_3_month_sequence_ratio;
	}

	public BigDecimal getBefore_2_month_sequence_ratio() {
		return before_2_month_sequence_ratio;
	}

	public void setBefore_2_month_sequence_ratio(BigDecimal before_2_month_sequence_ratio) {
		this.before_2_month_sequence_ratio = before_2_month_sequence_ratio;
	}

	public BigDecimal getBefore_1_month_sequence_ratio() {
		return before_1_month_sequence_ratio;
	}

	public void setBefore_1_month_sequence_ratio(BigDecimal before_1_month_sequence_ratio) {
		this.before_1_month_sequence_ratio = before_1_month_sequence_ratio;
	}

	public BigDecimal getCurrent_sequence_ratio() {
		return current_sequence_ratio;
	}

	public void setCurrent_sequence_ratio(BigDecimal current_sequence_ratio) {
		this.current_sequence_ratio = current_sequence_ratio;
	}

	public BigDecimal getAfter_1_month_sequence_ratio() {
		return after_1_month_sequence_ratio;
	}

	public void setAfter_1_month_sequence_ratio(BigDecimal after_1_month_sequence_ratio) {
		this.after_1_month_sequence_ratio = after_1_month_sequence_ratio;
	}

	public BigDecimal getAfter_2_month_sequence_ratio() {
		return after_2_month_sequence_ratio;
	}

	public void setAfter_2_month_sequence_ratio(BigDecimal after_2_month_sequence_ratio) {
		this.after_2_month_sequence_ratio = after_2_month_sequence_ratio;
	}

	public BigDecimal getAfter_3_month_sequence_ratio() {
		return after_3_month_sequence_ratio;
	}

	public void setAfter_3_month_sequence_ratio(BigDecimal after_3_month_sequence_ratio) {
		this.after_3_month_sequence_ratio = after_3_month_sequence_ratio;
	}

	public BigDecimal getAfter_6_month_sequence_ratio() {
		return after_6_month_sequence_ratio;
	}

	public void setAfter_6_month_sequence_ratio(BigDecimal after_6_month_sequence_ratio) {
		this.after_6_month_sequence_ratio = after_6_month_sequence_ratio;
	}

	public BigDecimal getAfter_12_month_sequence_ratio() {
		return after_12_month_sequence_ratio;
	}

	public void setAfter_12_month_sequence_ratio(BigDecimal after_12_month_sequence_ratio) {
		this.after_12_month_sequence_ratio = after_12_month_sequence_ratio;
	}

	public String getPrediction_dt() {
		return prediction_dt;
	}

	public void setPrediction_dt(String prediction_dt) {
		this.prediction_dt = prediction_dt;
	}

	public BigDecimal getSequence_threshold_value() {
		return sequence_threshold_value;
	}

	public void setSequence_threshold_value(BigDecimal sequence_threshold_value) {
		this.sequence_threshold_value = sequence_threshold_value;
	}

	public BigDecimal getSequence_last_number() {
		return sequence_last_number;
	}

	public void setSequence_last_number(BigDecimal sequence_last_number) {
		this.sequence_last_number = sequence_last_number;
	}

	public BigDecimal getPast_sequence_value_trend() {
		return past_sequence_value_trend;
	}

	public void setPast_sequence_value_trend(BigDecimal past_sequence_value_trend) {
		this.past_sequence_value_trend = past_sequence_value_trend;
	}

	public BigDecimal getFuture_sequence_value_trend() {
		return future_sequence_value_trend;
	}

	public void setFuture_sequence_value_trend(BigDecimal future_sequence_value_trend) {
		this.future_sequence_value_trend = future_sequence_value_trend;
	}

	public BigDecimal getSequence_threshold_usage() {
		return sequence_threshold_usage;
	}

	public void setSequence_threshold_usage(BigDecimal sequence_threshold_usage) {
		this.sequence_threshold_usage = sequence_threshold_usage;
	}

	public BigDecimal getSequence_last_number_usage() {
		return sequence_last_number_usage;
	}

	public void setSequence_last_number_usage(BigDecimal sequence_last_number_usage) {
		this.sequence_last_number_usage = sequence_last_number_usage;
	}

	public BigDecimal getPast_sequence_usage_trend() {
		return past_sequence_usage_trend;
	}

	public void setPast_sequence_usage_trend(BigDecimal past_sequence_usage_trend) {
		this.past_sequence_usage_trend = past_sequence_usage_trend;
	}

	public BigDecimal getFuture_sequence_usage_trend() {
		return future_sequence_usage_trend;
	}

	public void setFuture_sequence_usage_trend(BigDecimal future_sequence_usage_trend) {
		this.future_sequence_usage_trend = future_sequence_usage_trend;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getMonth_sequence_ratio() {
		return month_sequence_ratio;
	}

	public void setMonth_sequence_ratio(String month_sequence_ratio) {
		this.month_sequence_ratio = month_sequence_ratio;
	}

	public String getPredict_standard() {
		return predict_standard;
	}

	public void setPredict_standard(String predict_standard) {
		this.predict_standard = predict_standard;
	}

	public String getCurrent_sequence_value() {
		return current_sequence_value;
	}

	public void setCurrent_sequence_value(String current_sequence_value) {
		this.current_sequence_value = current_sequence_value;
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
