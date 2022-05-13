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
 * 2018.06.14 반광수 최초작성 (Tablespace 한계점예측)
 **********************************************************/

@Alias("tablespaceLimitPoint")
public class TablespaceLimitPoint extends Base implements Jsonable {

	private String inst_id;
	private String inst_nm;
	private String sql_id;
	private String cnt;
	private String activity;
	private String sql_text;
	private String snap_dt;

	private String predictBaseDay;
	private String tablespace_name;
	private String tablespace_size;
	private String used_space;
	private String prev3MonthTsUsageRate;
	private String prev2MonthTsUsageRate;
	private String prev1MonthTsUsageRate;
	private String curMonthTsUsageRate;
	private String next1MonthTsUsageRate;
	private String next2MonthTsUsageRate;;
	private String next3MonthTsUsageRate;;
	private String next6MonthTsUsageRate;;
	private String next12MonthTsUsageRate;;
	private String prediction_dt;
	private String prediction_date;
	private String current_ts_used_space;

	private String before_3_month_ts_used_percent;
	private String before_2_month_ts_used_percent;
	private String before_1_month_ts_used_percent;
	private String current_ts_used_percent;
	private String after_1_month_ts_used_percent;
	private String after_2_month_ts_used_percent;
	private String after_3_month_ts_used_percent;
	private String after_6_month_ts_used_percent;
	private String after_12_month_ts_used_percent;

	// Tablespace 한계점 예측 - 상세 - 사용량(MB)
	private String tablespace_threshold_size;
	private String past_ts_used_space_trend;
	private String future_ts_used_space_trend;

	// Tablespace 한계점 예측 - 상세 - 사용율(%)
	private String used_percent;
	private String past_ts_used_percent_trend;
	private String future_ts_used_percent_trend;
	private String tablespace_threshold_usage;

	private String legend;
	private String month_ts_used_percent;
	private String period;
	private String predict_standard;
	
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

	public String getSql_id() {
		return sql_id;
	}

	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}

	public String getCnt() {
		return cnt;
	}

	public void setCnt(String cnt) {
		this.cnt = cnt;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getSql_text() {
		return sql_text;
	}

	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
	}

	public String getTablespace_name() {
		return tablespace_name;
	}

	public void setTablespace_name(String tablespace_name) {
		this.tablespace_name = tablespace_name;
	}

	public String getPrediction_dt() {
		return prediction_dt;
	}

	public void setPrediction_dt(String prediction_dt) {
		this.prediction_dt = prediction_dt;
	}

	public String getPredictBaseDay() {
		return predictBaseDay;
	}

	public void setPredictBaseDay(String predictBaseDay) {
		this.predictBaseDay = predictBaseDay;
	}

	public String getTablespace_size() {
		return tablespace_size;
	}

	public void setTablespace_size(String tablespace_size) {
		this.tablespace_size = tablespace_size;
	}

	public String getUsed_space() {
		return used_space;
	}

	public void setUsed_space(String used_space) {
		this.used_space = used_space;
	}

	public String getPrev3MonthTsUsageRate() {
		return prev3MonthTsUsageRate;
	}

	public void setPrev3MonthTsUsageRate(String prev3MonthTsUsageRate) {
		this.prev3MonthTsUsageRate = prev3MonthTsUsageRate;
	}

	public String getPrev2MonthTsUsageRate() {
		return prev2MonthTsUsageRate;
	}

	public void setPrev2MonthTsUsageRate(String prev2MonthTsUsageRate) {
		this.prev2MonthTsUsageRate = prev2MonthTsUsageRate;
	}

	public String getPrev1MonthTsUsageRate() {
		return prev1MonthTsUsageRate;
	}

	public void setPrev1MonthTsUsageRate(String prev1MonthTsUsageRate) {
		this.prev1MonthTsUsageRate = prev1MonthTsUsageRate;
	}

	public String getCurMonthTsUsageRate() {
		return curMonthTsUsageRate;
	}

	public void setCurMonthTsUsageRate(String curMonthTsUsageRate) {
		this.curMonthTsUsageRate = curMonthTsUsageRate;
	}

	public String getNext1MonthTsUsageRate() {
		return next1MonthTsUsageRate;
	}

	public void setNext1MonthTsUsageRate(String next1MonthTsUsageRate) {
		this.next1MonthTsUsageRate = next1MonthTsUsageRate;
	}

	public String getNext2MonthTsUsageRate() {
		return next2MonthTsUsageRate;
	}

	public void setNext2MonthTsUsageRate(String next2MonthTsUsageRate) {
		this.next2MonthTsUsageRate = next2MonthTsUsageRate;
	}

	public String getNext3MonthTsUsageRate() {
		return next3MonthTsUsageRate;
	}

	public void setNext3MonthTsUsageRate(String next3MonthTsUsageRate) {
		this.next3MonthTsUsageRate = next3MonthTsUsageRate;
	}

	public String getNext6MonthTsUsageRate() {
		return next6MonthTsUsageRate;
	}

	public void setNext6MonthTsUsageRate(String next6MonthTsUsageRate) {
		this.next6MonthTsUsageRate = next6MonthTsUsageRate;
	}

	public String getNext12MonthTsUsageRate() {
		return next12MonthTsUsageRate;
	}

	public void setNext12MonthTsUsageRate(String next12MonthTsUsageRate) {
		this.next12MonthTsUsageRate = next12MonthTsUsageRate;
	}

	public String getPrediction_date() {
		return prediction_date;
	}

	public void setPrediction_date(String prediction_date) {
		this.prediction_date = prediction_date;
	}

	public String getCurrent_ts_used_space() {
		return current_ts_used_space;
	}

	public void setCurrent_ts_used_space(String current_ts_used_space) {
		this.current_ts_used_space = current_ts_used_space;
	}

	public String getMonth_ts_used_percent() {
		return month_ts_used_percent;
	}

	public void setMonth_ts_used_percent(String month_ts_used_percent) {
		this.month_ts_used_percent = month_ts_used_percent;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getBefore_3_month_ts_used_percent() {
		return before_3_month_ts_used_percent;
	}

	public void setBefore_3_month_ts_used_percent(String before_3_month_ts_used_percent) {
		this.before_3_month_ts_used_percent = before_3_month_ts_used_percent;
	}

	public String getBefore_2_month_ts_used_percent() {
		return before_2_month_ts_used_percent;
	}

	public void setBefore_2_month_ts_used_percent(String before_2_month_ts_used_percent) {
		this.before_2_month_ts_used_percent = before_2_month_ts_used_percent;
	}

	public String getBefore_1_month_ts_used_percent() {
		return before_1_month_ts_used_percent;
	}

	public void setBefore_1_month_ts_used_percent(String before_1_month_ts_used_percent) {
		this.before_1_month_ts_used_percent = before_1_month_ts_used_percent;
	}

	public String getCurrent_ts_used_percent() {
		return current_ts_used_percent;
	}

	public void setCurrent_ts_used_percent(String current_ts_used_percent) {
		this.current_ts_used_percent = current_ts_used_percent;
	}

	public String getAfter_1_month_ts_used_percent() {
		return after_1_month_ts_used_percent;
	}

	public void setAfter_1_month_ts_used_percent(String after_1_month_ts_used_percent) {
		this.after_1_month_ts_used_percent = after_1_month_ts_used_percent;
	}

	public String getAfter_2_month_ts_used_percent() {
		return after_2_month_ts_used_percent;
	}

	public void setAfter_2_month_ts_used_percent(String after_2_month_ts_used_percent) {
		this.after_2_month_ts_used_percent = after_2_month_ts_used_percent;
	}

	public String getAfter_3_month_ts_used_percent() {
		return after_3_month_ts_used_percent;
	}

	public void setAfter_3_month_ts_used_percent(String after_3_month_ts_used_percent) {
		this.after_3_month_ts_used_percent = after_3_month_ts_used_percent;
	}

	public String getAfter_6_month_ts_used_percent() {
		return after_6_month_ts_used_percent;
	}

	public void setAfter_6_month_ts_used_percent(String after_6_month_ts_used_percent) {
		this.after_6_month_ts_used_percent = after_6_month_ts_used_percent;
	}

	public String getAfter_12_month_ts_used_percent() {
		return after_12_month_ts_used_percent;
	}

	public void setAfter_12_month_ts_used_percent(String after_12_month_ts_used_percent) {
		this.after_12_month_ts_used_percent = after_12_month_ts_used_percent;
	}

	public String getSnap_dt() {
		return snap_dt;
	}

	public void setSnap_dt(String snap_dt) {
		this.snap_dt = snap_dt;
	}

	public String getTablespace_threshold_size() {
		return tablespace_threshold_size;
	}

	public void setTablespace_threshold_size(String tablespace_threshold_size) {
		this.tablespace_threshold_size = tablespace_threshold_size;
	}

	public String getPast_ts_used_space_trend() {
		return past_ts_used_space_trend;
	}

	public void setPast_ts_used_space_trend(String past_ts_used_space_trend) {
		this.past_ts_used_space_trend = past_ts_used_space_trend;
	}

	public String getFuture_ts_used_space_trend() {
		return future_ts_used_space_trend;
	}

	public void setFuture_ts_used_space_trend(String future_ts_used_space_trend) {
		this.future_ts_used_space_trend = future_ts_used_space_trend;
	}

	public String getUsed_percent() {
		return used_percent;
	}

	public void setUsed_percent(String used_percent) {
		this.used_percent = used_percent;
	}

	public String getPast_ts_used_percent_trend() {
		return past_ts_used_percent_trend;
	}

	public void setPast_ts_used_percent_trend(String past_ts_used_percent_trend) {
		this.past_ts_used_percent_trend = past_ts_used_percent_trend;
	}

	public String getFuture_ts_used_percent_trend() {
		return future_ts_used_percent_trend;
	}

	public void setFuture_ts_used_percent_trend(String future_ts_used_percent_trend) {
		this.future_ts_used_percent_trend = future_ts_used_percent_trend;
	}

	public String getTablespace_threshold_usage() {
		return tablespace_threshold_usage;
	}

	public void setTablespace_threshold_usage(String tablespace_threshold_usage) {
		this.tablespace_threshold_usage = tablespace_threshold_usage;
	}

	public String getLegend() {
		return legend;
	}

	public void setLegend(String legend) {
		this.legend = legend;
	}

	public String getPredict_standard() {
		return predict_standard;
	}

	public void setPredict_standard(String predict_standard) {
		this.predict_standard = predict_standard;
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
