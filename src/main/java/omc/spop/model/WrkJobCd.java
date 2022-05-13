package omc.spop.model;

import java.util.ArrayList;
import java.util.List;
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

@Alias("wrkJobCd")
public class WrkJobCd extends Base implements Jsonable {

	private int lvl;
	private String base_day;
	private String max_base_day;
	/**단위업무코드*/
	private String wrkjob_cd;
	/**단위업무명*/
	private String wrkjob_cd_nm;
	/**상위단위업무코드*/
	private String old_wrkjob_div_cd;
	private String upper_wrkjob_cd;
	private String perf_check_threshold_type_cd;
	private String perf_check_threshold_type_nm;
	private int elapsed_time_threshold;
	private int buffer_gets_threshold;
	private String wrkjob_div_cd;
	private String tr_type;
	private String day0 = "0";
	private String day1 = "0";
	private String day2 = "0";
	private String day3 = "0";
	private String day4 = "0";
	private String day5 = "0";
	private String cd;
	private String cd_nm;
	private int cnt;
	private int timeout_cnt;
	private int replytimeinc_cnt;

	private String id;
	private String parent_id;
	private String text;

	private String use_yn;
	private String deploy_check_target_yn; // 배포전성능점검
	/**시작년월일 */
	private String start_ymd;
	/**종료년월일*/
	private String end_ymd;
	/**순서*/
	private int line_up;
	/**단위업무표시명*/
	private String wrkjob_cd_desc_nm;
	
	private String db1_id;
	private String db2_id;
	private String db3_id;
	private String db1_name;
	private String db2_name;
	private String db3_name;
	private String wrkjob_cd_target;
	
	private List<WrkJobCd> children = new ArrayList<WrkJobCd>();
	
	public String getOld_wrkjob_div_cd() {
		return old_wrkjob_div_cd;
	}

	public void setOld_wrkjob_div_cd(String old_wrkjob_div_cd) {
		this.old_wrkjob_div_cd = old_wrkjob_div_cd;
	}

	public String getBase_day() {
		return base_day;
	}

	public void setBase_day(String base_day) {
		this.base_day = base_day;
	}

	public String getMax_base_day() {
		return max_base_day;
	}

	public void setMax_base_day(String max_base_day) {
		this.max_base_day = max_base_day;
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

	public String getUpper_wrkjob_cd() {
		return upper_wrkjob_cd;
	}

	public void setUpper_wrkjob_cd(String upper_wrkjob_cd) {
		this.upper_wrkjob_cd = upper_wrkjob_cd;
	}

	public String getPerf_check_threshold_type_cd() {
		return perf_check_threshold_type_cd;
	}

	public void setPerf_check_threshold_type_cd(String perf_check_threshold_type_cd) {
		this.perf_check_threshold_type_cd = perf_check_threshold_type_cd;
	}

	public String getPerf_check_threshold_type_nm() {
		return perf_check_threshold_type_nm;
	}

	public void setPerf_check_threshold_type_nm(String perf_check_threshold_type_nm) {
		this.perf_check_threshold_type_nm = perf_check_threshold_type_nm;
	}

	public int getElapsed_time_threshold() {
		return elapsed_time_threshold;
	}

	public void setElapsed_time_threshold(int elapsed_time_threshold) {
		this.elapsed_time_threshold = elapsed_time_threshold;
	}

	public int getBuffer_gets_threshold() {
		return buffer_gets_threshold;
	}

	public void setBuffer_gets_threshold(int buffer_gets_threshold) {
		this.buffer_gets_threshold = buffer_gets_threshold;
	}

	public String getWrkjob_div_cd() {
		return wrkjob_div_cd;
	}

	public void setWrkjob_div_cd(String wrkjob_div_cd) {
		this.wrkjob_div_cd = wrkjob_div_cd;
	}

	public String getTr_type() {
		return tr_type;
	}

	public void setTr_type(String tr_type) {
		this.tr_type = tr_type;
	}

	public String getDay0() {
		return day0;
	}

	public void setDay0(String day0) {
		this.day0 = day0;
	}

	public String getDay1() {
		return day1;
	}

	public void setDay1(String day1) {
		this.day1 = day1;
	}

	public String getDay2() {
		return day2;
	}

	public void setDay2(String day2) {
		this.day2 = day2;
	}

	public String getDay3() {
		return day3;
	}

	public void setDay3(String day3) {
		this.day3 = day3;
	}

	public String getDay4() {
		return day4;
	}

	public void setDay4(String day4) {
		this.day4 = day4;
	}

	public String getDay5() {
		return day5;
	}

	public void setDay5(String day5) {
		this.day5 = day5;
	}

	public String getCd() {
		return cd;
	}

	public void setCd(String cd) {
		this.cd = cd;
	}

	public String getCd_nm() {
		return cd_nm;
	}

	public void setCd_nm(String cd_nm) {
		this.cd_nm = cd_nm;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public int getTimeout_cnt() {
		return timeout_cnt;
	}

	public void setTimeout_cnt(int timeout_cnt) {
		this.timeout_cnt = timeout_cnt;
	}

	public int getReplytimeinc_cnt() {
		return replytimeinc_cnt;
	}

	public void setReplytimeinc_cnt(int replytimeinc_cnt) {
		this.replytimeinc_cnt = replytimeinc_cnt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<WrkJobCd> getChildren() {
		return children;
	}

	public void setChildren(List<WrkJobCd> children) {
		this.children = children;
	}

	public String getUse_yn() {
		return use_yn;
	}

	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}

	public String getDeploy_check_target_yn() {
		return deploy_check_target_yn;
	}

	public void setDeploy_check_target_yn(String deploy_check_target_yn) {
		this.deploy_check_target_yn = deploy_check_target_yn;
	}

	public int getLvl() {
		return lvl;
	}

	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	public String getStart_ymd() {
		return start_ymd;
	}

	public void setStart_ymd(String start_ymd) {
		this.start_ymd = start_ymd;
	}

	public String getEnd_ymd() {
		return end_ymd;
	}

	public void setEnd_ymd(String end_ymd) {
		this.end_ymd = end_ymd;
	}

	public int getLine_up() {
		return line_up;
	}

	public void setLine_up(int line_up) {
		this.line_up = line_up;
	}

	public String getWrkjob_cd_desc_nm() {
		return wrkjob_cd_desc_nm;
	}

	public void setWrkjob_cd_desc_nm(String wrkjob_cd_desc_nm) {
		this.wrkjob_cd_desc_nm = wrkjob_cd_desc_nm;
	}
	
	public String getDb1_id() {
		return db1_id;
	}

	public void setDb1_id(String db1_id) {
		this.db1_id = db1_id;
	}

	public String getDb2_id() {
		return db2_id;
	}

	public void setDb2_id(String db2_id) {
		this.db2_id = db2_id;
	}

	public String getDb3_id() {
		return db3_id;
	}

	public void setDb3_id(String db3_id) {
		this.db3_id = db3_id;
	}

	public String getDb1_name() {
		return db1_name;
	}

	public void setDb1_name(String db1_name) {
		this.db1_name = db1_name;
	}

	public String getDb2_name() {
		return db2_name;
	}

	public void setDb2_name(String db2_name) {
		this.db2_name = db2_name;
	}

	public String getDb3_name() {
		return db3_name;
	}

	public void setDb3_name(String db3_name) {
		this.db3_name = db3_name;
	}
	
	public String getWrkjob_cd_target() {
		return wrkjob_cd_target;
	}

	public void setWrkjob_cd_target(String wrkjob_cd_target) {
		this.wrkjob_cd_target = wrkjob_cd_target;
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