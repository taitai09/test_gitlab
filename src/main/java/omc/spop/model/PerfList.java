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
 * 2017.10.30	이원식	최초작성
 * 2018.02.06	이원식	처리단계 - 분석완료 (오류 추가)
 **********************************************************/

@Alias("perfList")
public class PerfList extends Base implements Jsonable {
	
    private String db_abbr_nm;
    private String req_before;
    private String req_today;
    private String sel_before;
    private String sel_today;
    private String improve_tot;
    private String mng_analyzing;
    private String mng_complete;
    private String mng_not_improve;
    private String mng_error;
    private String dev_analyzing;
    private String dev_cancel;
    private String dev_complete;
    private String dev_apply_cancel;
    
    private String chk_except;
    private String chk_dbAuth;
    private String search_startDate;
    private String search_endDate;
    
    private String base_day;
    private String online_cnt;
    private String batch_cnt;
    private String etc_cnt;
    private String all_cnt;
    
    private String search_wrkjob_cd;
    
    private String wrkjob_cd_nm;
    private String choice_div_nm;
    private String cnt;
    private String wrkjob_cd;
    private String choice_div_cd;
    private String ordered;
    private String wrkjob_count;
    
    private String project_nm;
    private String project_id;
    private String tuning_prgrs_step_seq;
    private String tuning_prgrs_step_nm;
    
    
    
	public String getProject_nm() {
		return project_nm;
	}
	public void setProject_nm(String project_nm) {
		this.project_nm = project_nm;
	}
	public String getProject_id() {
		return project_id;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public String getTuning_prgrs_step_seq() {
		return tuning_prgrs_step_seq;
	}
	public void setTuning_prgrs_step_seq(String tuning_prgrs_step_seq) {
		this.tuning_prgrs_step_seq = tuning_prgrs_step_seq;
	}
	public String getTuning_prgrs_step_nm() {
		return tuning_prgrs_step_nm;
	}
	public void setTuning_prgrs_step_nm(String tuning_prgrs_step_nm) {
		this.tuning_prgrs_step_nm = tuning_prgrs_step_nm;
	}
	public String getWrkjob_count() {
		return wrkjob_count;
	}
	public void setWrkjob_count(String wrkjob_count) {
		this.wrkjob_count = wrkjob_count;
	}
	public String getWrkjob_cd_nm() {
		return wrkjob_cd_nm;
	}
	public void setWrkjob_cd_nm(String wrkjob_cd_nm) {
		this.wrkjob_cd_nm = wrkjob_cd_nm;
	}
	public String getChoice_div_nm() {
		return choice_div_nm;
	}
	public void setChoice_div_nm(String choice_div_nm) {
		this.choice_div_nm = choice_div_nm;
	}
	public String getCnt() {
		return cnt;
	}
	public void setCnt(String cnt) {
		this.cnt = cnt;
	}
	public String getWrkjob_cd() {
		return wrkjob_cd;
	}
	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}
	public String getChoice_div_cd() {
		return choice_div_cd;
	}
	public void setChoice_div_cd(String choice_div_cd) {
		this.choice_div_cd = choice_div_cd;
	}
	public String getOrdered() {
		return ordered;
	}
	public void setOrdered(String ordered) {
		this.ordered = ordered;
	}
	public String getSearch_wrkjob_cd() {
		return search_wrkjob_cd;
	}
	public void setSearch_wrkjob_cd(String search_wrkjob_cd) {
		this.search_wrkjob_cd = search_wrkjob_cd;
	}
	public String getBase_day() {
		return base_day;
	}
	public void setBase_day(String base_day) {
		this.base_day = base_day;
	}
	public String getOnline_cnt() {
		return online_cnt;
	}
	public void setOnline_cnt(String online_cnt) {
		this.online_cnt = online_cnt;
	}
	public String getBatch_cnt() {
		return batch_cnt;
	}
	public void setBatch_cnt(String batch_cnt) {
		this.batch_cnt = batch_cnt;
	}
	public String getChk_except() {
		return chk_except;
	}
	public String getEtc_cnt() {
		return etc_cnt;
	}
	public void setEtc_cnt(String etc_cnt) {
		this.etc_cnt = etc_cnt;
	}
	public String getAll_cnt() {
		return all_cnt;
	}
	public void setAll_cnt(String all_cnt) {
		this.all_cnt = all_cnt;
	}
	public void setChk_except(String chk_except) {
		this.chk_except = chk_except;
	}
	public String getChk_dbAuth() {
		return chk_dbAuth;
	}
	public void setChk_dbAuth(String chk_dbAuth) {
		this.chk_dbAuth = chk_dbAuth;
	}
	public String getSearch_startDate() {
		return search_startDate;
	}
	public void setSearch_startDate(String search_startDate) {
		this.search_startDate = search_startDate;
	}
	public String getSearch_endDate() {
		return search_endDate;
	}
	public void setSearch_endDate(String search_endDate) {
		this.search_endDate = search_endDate;
	}
	public String getDb_abbr_nm() {
		return db_abbr_nm;
	}
	public void setDb_abbr_nm(String db_abbr_nm) {
		this.db_abbr_nm = db_abbr_nm;
	}
	public String getReq_before() {
		return req_before;
	}
	public void setReq_before(String req_before) {
		this.req_before = req_before;
	}
	public String getReq_today() {
		return req_today;
	}
	public void setReq_today(String req_today) {
		this.req_today = req_today;
	}
	public String getSel_before() {
		return sel_before;
	}
	public void setSel_before(String sel_before) {
		this.sel_before = sel_before;
	}
	public String getSel_today() {
		return sel_today;
	}
	public void setSel_today(String sel_today) {
		this.sel_today = sel_today;
	}
	public String getImprove_tot() {
		return improve_tot;
	}
	public void setImprove_tot(String improve_tot) {
		this.improve_tot = improve_tot;
	}
	public String getMng_analyzing() {
		return mng_analyzing;
	}
	public void setMng_analyzing(String mng_analyzing) {
		this.mng_analyzing = mng_analyzing;
	}
	public String getMng_complete() {
		return mng_complete;
	}
	public void setMng_complete(String mng_complete) {
		this.mng_complete = mng_complete;
	}
	public String getMng_not_improve() {
		return mng_not_improve;
	}
	public void setMng_not_improve(String mng_not_improve) {
		this.mng_not_improve = mng_not_improve;
	}
	public String getMng_error() {
		return mng_error;
	}
	public void setMng_error(String mng_error) {
		this.mng_error = mng_error;
	}
	public String getDev_analyzing() {
		return dev_analyzing;
	}
	public void setDev_analyzing(String dev_analyzing) {
		this.dev_analyzing = dev_analyzing;
	}
	public String getDev_cancel() {
		return dev_cancel;
	}
	public void setDev_cancel(String dev_cancel) {
		this.dev_cancel = dev_cancel;
	}
	public String getDev_complete() {
		return dev_complete;
	}
	public void setDev_complete(String dev_complete) {
		this.dev_complete = dev_complete;
	}
	public String getDev_apply_cancel() {
		return dev_apply_cancel;
	}
	public void setDev_apply_cancel(String dev_apply_cancel) {
		this.dev_apply_cancel = dev_apply_cancel;
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
