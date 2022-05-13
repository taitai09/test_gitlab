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
 * 2020.06.24 	명성태 	최초작성
 **********************************************************/

@Alias("sqlPerformanceTraceStatusChart")
public class SqlPerformanceTraceStatusChart extends Base implements Jsonable {
	private String deploy_complete_dt;
	private String begin_dt;
	private String end_dt;
	private int base_period_value;
	private int isLargerThanBeginDate;
	
	private String wrkjob_cd;
	private String wrkjob_cd_nm;
	
	// chart
	private String day;
	private String buffer_gets_fail;
	private String buffer_gets_pass;
	private String buffer_gets_fail_ratio;
	private String elapsed_time_fail;
	private String elapsed_time_pass;
	private String elapsed_time_fail_ratio;
	private String plan_change_fail;
	private String plan_change_pass;
	private String plan_change_fail_ratio;
	private String buffer_gets_improve;
	private String buffer_gets_regress;
	private String elapsed_time_improve;
	private String elapsed_time_regress;
	private String plan_change_improve;
	private String plan_change_regress;
	
	// chartPerformance01, chartException01
	private String fail;
	private String pass;
	private String fail_ratio;
	
	// chartPerformance02
	private String pass_fail_type;	// chartException02
	private String less_than_2;
	private String less_than_5;
	private String less_than_10;	// chartException02
	private String less_than_30;
	private String less_than_50;
	private String less_than_100;
	private String more_than_100;
	
	// chartException02
	private String less_than_0_dot_1;
	private String less_than_0_dot_3;
	private String less_than_1;		// chartPerformance02
	private String less_than_3;
	private String less_than_60;
	private String more_than_60;
	
	private String bar_label;
	private String pass_value;
	private String fail_value;
	
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

	public String getDeploy_complete_dt() {
		return deploy_complete_dt;
	}

	public void setDeploy_complete_dt(String deploy_complete_dt) {
		this.deploy_complete_dt = deploy_complete_dt;
	}

	public String getBegin_dt() {
		return begin_dt;
	}

	public void setBegin_dt(String begin_dt) {
		this.begin_dt = begin_dt;
	}

	public String getEnd_dt() {
		return end_dt;
	}

	public void setEnd_dt(String end_dt) {
		this.end_dt = end_dt;
	}

	public int getBase_period_value() {
		return base_period_value;
	}

	public void setBase_period_value(int base_period_value) {
		this.base_period_value = base_period_value;
	}

	public int getIsLargerThanBeginDate() {
		return isLargerThanBeginDate;
	}

	public void setIsLargerThanBeginDate(int isLargerThanBeginDate) {
		this.isLargerThanBeginDate = isLargerThanBeginDate;
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

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getBuffer_gets_fail() {
		return buffer_gets_fail;
	}

	public void setBuffer_gets_fail(String buffer_gets_fail) {
		this.buffer_gets_fail = buffer_gets_fail;
	}

	public String getBuffer_gets_pass() {
		return buffer_gets_pass;
	}

	public void setBuffer_gets_pass(String buffer_gets_pass) {
		this.buffer_gets_pass = buffer_gets_pass;
	}

	public String getBuffer_gets_fail_ratio() {
		return buffer_gets_fail_ratio;
	}

	public void setBuffer_gets_fail_ratio(String buffer_gets_fail_ratio) {
		this.buffer_gets_fail_ratio = buffer_gets_fail_ratio;
	}

	public String getElapsed_time_fail() {
		return elapsed_time_fail;
	}

	public void setElapsed_time_fail(String elapsed_time_fail) {
		this.elapsed_time_fail = elapsed_time_fail;
	}

	public String getElapsed_time_pass() {
		return elapsed_time_pass;
	}

	public void setElapsed_time_pass(String elapsed_time_pass) {
		this.elapsed_time_pass = elapsed_time_pass;
	}

	public String getElapsed_time_fail_ratio() {
		return elapsed_time_fail_ratio;
	}

	public void setElapsed_time_fail_ratio(String elapsed_time_fail_ratio) {
		this.elapsed_time_fail_ratio = elapsed_time_fail_ratio;
	}

	public String getPlan_change_fail() {
		return plan_change_fail;
	}

	public void setPlan_change_fail(String plan_change_fail) {
		this.plan_change_fail = plan_change_fail;
	}

	public String getPlan_change_pass() {
		return plan_change_pass;
	}

	public void setPlan_change_pass(String plan_change_pass) {
		this.plan_change_pass = plan_change_pass;
	}

	public String getPlan_change_fail_ratio() {
		return plan_change_fail_ratio;
	}

	public void setPlan_change_fail_ratio(String plan_change_fail_ratio) {
		this.plan_change_fail_ratio = plan_change_fail_ratio;
	}

	public String getBuffer_gets_improve() {
		return buffer_gets_improve;
	}

	public void setBuffer_gets_improve(String buffer_gets_improve) {
		this.buffer_gets_improve = buffer_gets_improve;
	}

	public String getBuffer_gets_regress() {
		return buffer_gets_regress;
	}

	public void setBuffer_gets_regress(String buffer_gets_regress) {
		this.buffer_gets_regress = buffer_gets_regress;
	}

	public String getElapsed_time_improve() {
		return elapsed_time_improve;
	}

	public void setElapsed_time_improve(String elapsed_time_improve) {
		this.elapsed_time_improve = elapsed_time_improve;
	}

	public String getElapsed_time_regress() {
		return elapsed_time_regress;
	}

	public void setElapsed_time_regress(String elapsed_time_regress) {
		this.elapsed_time_regress = elapsed_time_regress;
	}

	public String getPlan_change_improve() {
		return plan_change_improve;
	}

	public void setPlan_change_improve(String plan_change_improve) {
		this.plan_change_improve = plan_change_improve;
	}

	public String getPlan_change_regress() {
		return plan_change_regress;
	}

	public void setPlan_change_regress(String plan_change_regress) {
		this.plan_change_regress = plan_change_regress;
	}

	public String getFail() {
		return fail;
	}

	public void setFail(String fail) {
		this.fail = fail;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getFail_ratio() {
		return fail_ratio;
	}

	public void setFail_ratio(String fail_ratio) {
		this.fail_ratio = fail_ratio;
	}

	public String getPass_fail_type() {
		return pass_fail_type;
	}

	public void setPass_fail_type(String pass_fail_type) {
		this.pass_fail_type = pass_fail_type;
	}

	public String getLess_than_2() {
		return less_than_2;
	}

	public void setLess_than_2(String less_than_2) {
		this.less_than_2 = less_than_2;
	}

	public String getLess_than_5() {
		return less_than_5;
	}

	public void setLess_than_5(String less_than_5) {
		this.less_than_5 = less_than_5;
	}

	public String getLess_than_10() {
		return less_than_10;
	}

	public void setLess_than_10(String less_than_10) {
		this.less_than_10 = less_than_10;
	}

	public String getLess_than_30() {
		return less_than_30;
	}

	public void setLess_than_30(String less_than_30) {
		this.less_than_30 = less_than_30;
	}

	public String getLess_than_50() {
		return less_than_50;
	}

	public void setLess_than_50(String less_than_50) {
		this.less_than_50 = less_than_50;
	}

	public String getLess_than_100() {
		return less_than_100;
	}

	public void setLess_than_100(String less_than_100) {
		this.less_than_100 = less_than_100;
	}

	public String getMore_than_100() {
		return more_than_100;
	}

	public void setMore_than_100(String more_than_100) {
		this.more_than_100 = more_than_100;
	}

	public String getLess_than_0_dot_1() {
		return less_than_0_dot_1;
	}

	public void setLess_than_0_dot_1(String less_than_0_dot_1) {
		this.less_than_0_dot_1 = less_than_0_dot_1;
	}

	public String getLess_than_0_dot_3() {
		return less_than_0_dot_3;
	}

	public void setLess_than_0_dot_3(String less_than_0_dot_3) {
		this.less_than_0_dot_3 = less_than_0_dot_3;
	}

	public String getLess_than_1() {
		return less_than_1;
	}

	public void setLess_than_1(String less_than_1) {
		this.less_than_1 = less_than_1;
	}

	public String getLess_than_3() {
		return less_than_3;
	}

	public void setLess_than_3(String less_than_3) {
		this.less_than_3 = less_than_3;
	}

	public String getLess_than_60() {
		return less_than_60;
	}

	public void setLess_than_60(String less_than_60) {
		this.less_than_60 = less_than_60;
	}

	public String getMore_than_60() {
		return more_than_60;
	}

	public void setMore_than_60(String more_than_60) {
		this.more_than_60 = more_than_60;
	}

	public String getBar_label() {
		return bar_label;
	}

	public void setBar_label(String bar_label) {
		this.bar_label = bar_label;
	}

	public String getPass_value() {
		return pass_value;
	}

	public void setPass_value(String pass_value) {
		this.pass_value = pass_value;
	}

	public String getFail_value() {
		return fail_value;
	}

	public void setFail_value(String fail_value) {
		this.fail_value = fail_value;
	}
}
