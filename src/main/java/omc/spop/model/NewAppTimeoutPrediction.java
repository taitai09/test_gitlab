package omc.spop.model;

import java.math.BigDecimal;
import java.util.HashMap;
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
 * 2019.03.14 명성태 최초작성 (신규 APP 타임아웃 예측)
 **********************************************************/

@Alias("newAppTimeoutPrediction")
public class NewAppTimeoutPrediction extends Base implements Jsonable {

	private String inst_id;
	private String inst_nm;
	private String sql_id;
	private String cnt;
	private String activity;
	private String sql_text;
	private String snap_dt;
	
	// 조건 값
	private String wrkjob_cd;
	private String wrkjob_cd_nm;
	private String start_first_exec_day;
	private String end_first_exec_day;
	private String timeout_condition;	// 타임아웃 조건
	private String except_yn;			// 예외 조건
	
	// 컬럼 정보
	private String tr_cd;						// 애플리케이션
	private String first_exec_day;				// 최초수행일
	private String last_exec_day;				// 최종수행일
	private int exec_day_cnt;				// 수행 일수
	private int exec_cnt;					// 수행횟수
	private String last_prediction_day;			// 예측일
	private BigDecimal after_1_week_elapsed_time;	// 1주일후
	private BigDecimal after_2_week_elapsed_time;	// 2주일후
	private BigDecimal after_3_week_elapsed_time;	// 3주일후
	private BigDecimal after_1_month_elapsed_time;	// 1개월후
	private BigDecimal after_2_month_elapsed_time;	// 2개월후
	private BigDecimal after_3_month_elapsed_time;	// 3개월후
	private BigDecimal after_6_month_elapsed_time;	// 6개월후
	private BigDecimal after_12_month_elapsed_time;	// 12개월후
	
	// 챠트 정보
	private String base_day;
	private String app_timeout_limit;
	private String avg_elapsed_time;
	private String past_elapsed_time_trend;
	private String future_elapsed_time_trend;
	
	private String rgb_color_value;
	private String total_cnt;	
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

	public String getSnap_dt() {
		return snap_dt;
	}

	public void setSnap_dt(String snap_dt) {
		this.snap_dt = snap_dt;
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

	public String getWrkjob_cd() {
		return wrkjob_cd;
	}

	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}

	public String getWrkjob_cd_nm() {
		return wrkjob_cd_nm;
	}

	public void setWrkjob_cd_nm(String wrkjob_cd_nm) {
		this.wrkjob_cd_nm = wrkjob_cd_nm;
	}

	public String getStart_first_exec_day() {
		return start_first_exec_day;
	}

	public void setStart_first_exec_day(String start_first_exec_day) {
		this.start_first_exec_day = start_first_exec_day;
	}

	public String getEnd_first_exec_day() {
		return end_first_exec_day;
	}

	public void setEnd_first_exec_day(String end_first_exec_day) {
		this.end_first_exec_day = end_first_exec_day;
	}

	public String getTimeout_condition() {
		return timeout_condition;
	}

	public void setTimeout_condition(String timeout_condition) {
		this.timeout_condition = timeout_condition;
	}

	public String getExcept_yn() {
		return except_yn;
	}

	public void setExcept_yn(String except_yn) {
		this.except_yn = except_yn;
	}

	public String getTr_cd() {
		return tr_cd;
	}

	public void setTr_cd(String tr_cd) {
		this.tr_cd = tr_cd;
	}

	public String getFirst_exec_day() {
		return first_exec_day;
	}

	public void setFirst_exec_day(String first_exec_day) {
		this.first_exec_day = first_exec_day;
	}

	public String getLast_exec_day() {
		return last_exec_day;
	}

	public void setLast_exec_day(String last_exec_day) {
		this.last_exec_day = last_exec_day;
	}

	public int getExec_day_cnt() {
		return exec_day_cnt;
	}

	public void setExec_day_cnt(int exec_day_cnt) {
		this.exec_day_cnt = exec_day_cnt;
	}

	public int getExec_cnt() {
		return exec_cnt;
	}

	public void setExec_cnt(int exec_cnt) {
		this.exec_cnt = exec_cnt;
	}

	public String getLast_prediction_day() {
		return last_prediction_day;
	}

	public void setLast_prediction_day(String last_prediction_day) {
		this.last_prediction_day = last_prediction_day;
	}

	public BigDecimal getAfter_1_week_elapsed_time() {
		return after_1_week_elapsed_time;
	}

	public void setAfter_1_week_elapsed_time(BigDecimal after_1_week_elapsed_time) {
		this.after_1_week_elapsed_time = after_1_week_elapsed_time;
	}

	public BigDecimal getAfter_2_week_elapsed_time() {
		return after_2_week_elapsed_time;
	}

	public void setAfter_2_week_elapsed_time(BigDecimal after_2_week_elapsed_time) {
		this.after_2_week_elapsed_time = after_2_week_elapsed_time;
	}

	public BigDecimal getAfter_3_week_elapsed_time() {
		return after_3_week_elapsed_time;
	}

	public void setAfter_3_week_elapsed_time(BigDecimal after_3_week_elapsed_time) {
		this.after_3_week_elapsed_time = after_3_week_elapsed_time;
	}

	public BigDecimal getAfter_1_month_elapsed_time() {
		return after_1_month_elapsed_time;
	}

	public void setAfter_1_month_elapsed_time(BigDecimal after_1_month_elapsed_time) {
		this.after_1_month_elapsed_time = after_1_month_elapsed_time;
	}

	public BigDecimal getAfter_2_month_elapsed_time() {
		return after_2_month_elapsed_time;
	}

	public void setAfter_2_month_elapsed_time(BigDecimal after_2_month_elapsed_time) {
		this.after_2_month_elapsed_time = after_2_month_elapsed_time;
	}

	public BigDecimal getAfter_3_month_elapsed_time() {
		return after_3_month_elapsed_time;
	}

	public void setAfter_3_month_elapsed_time(BigDecimal after_3_month_elapsed_time) {
		this.after_3_month_elapsed_time = after_3_month_elapsed_time;
	}

	public BigDecimal getAfter_6_month_elapsed_time() {
		return after_6_month_elapsed_time;
	}

	public void setAfter_6_month_elapsed_time(BigDecimal after_6_month_elapsed_time) {
		this.after_6_month_elapsed_time = after_6_month_elapsed_time;
	}

	public BigDecimal getAfter_12_month_elapsed_time() {
		return after_12_month_elapsed_time;
	}

	public void setAfter_12_month_elapsed_time(BigDecimal after_12_month_elapsed_time) {
		this.after_12_month_elapsed_time = after_12_month_elapsed_time;
	}

	public String getBase_day() {
		return base_day;
	}

	public void setBase_day(String base_day) {
		this.base_day = base_day;
	}

	public String getApp_timeout_limit() {
		return app_timeout_limit;
	}

	public void setApp_timeout_limit(String app_timeout_limit) {
		this.app_timeout_limit = app_timeout_limit;
	}

	public String getAvg_elapsed_time() {
		return avg_elapsed_time;
	}

	public void setAvg_elapsed_time(String avg_elapsed_time) {
		this.avg_elapsed_time = avg_elapsed_time;
	}

	public String getPast_elapsed_time_trend() {
		return past_elapsed_time_trend;
	}

	public void setPast_elapsed_time_trend(String past_elapsed_time_trend) {
		this.past_elapsed_time_trend = past_elapsed_time_trend;
	}

	public String getFuture_elapsed_time_trend() {
		return future_elapsed_time_trend;
	}

	public void setFuture_elapsed_time_trend(String future_elapsed_time_trend) {
		this.future_elapsed_time_trend = future_elapsed_time_trend;
	}

	public String getRgb_color_value() {
		return rgb_color_value;
	}

	public void setRgb_color_value(String rgb_color_value) {
		this.rgb_color_value = rgb_color_value;
	}

	public String getTotal_cnt() {
		return total_cnt;
	}

	public void setTotal_cnt(String total_cnt) {
		this.total_cnt = total_cnt;
	}

	public String getPredict_standard() {
		return predict_standard;
	}

	public void setPredict_standard(String predict_standard) {
		this.predict_standard = predict_standard;
	}

}
