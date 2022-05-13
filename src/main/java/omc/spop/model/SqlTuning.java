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
 * 2017.10.23 이원식 최초작성
 **********************************************************/

@Alias("sqlTuning")
public class SqlTuning extends Base implements Jsonable {

	private String except_target_yn;
	private String tuning_complete_why_cd;
	private String tuning_complete_why;
	private String tuning_complete_dt;
	private String tuning_completer_id;
	private String tuning_apply_rcess_why;
	private String tuning_apply_rcesser_id;
	private String tuning_apply_rcess_dt;
	private String tuning_apply_dt;
	private String tuning_applyer_id;
	private String tuning_end_why_cd;
	private String tuning_end_why;
	private String tuning_ender_id;
	private String tuning_end_dt;
	private String tuning_case_posting_yn;
	private String tuning_case_posting_title;
	private String all_tuning_end_dt;
	private String imprb_elap_time;
	private String imprb_buffer_cnt;
	private String imprb_pga_usage;
	private String impra_elap_time;
	private String impra_buffer_cnt;
	private String impra_pga_usage;
	private String elap_time_impr_ratio;
	private String buffer_impr_ratio;
	private String pga_impr_ratio;
	private String controversialist;
	private String impr_sbst;
	private String impr_sql_text;
	private String imprb_exec_plan;
	private String impra_exec_plan;

	private String choice_div_cd;
	private String tuning_status_cd;
	private String tuning_status_nm;
	private String perfr_id;
	private String wrkjob_mgr_nm;
	private String wrkjob_mgr_wrkjob_cd;
	private String wrkjob_mgr_wrkjob_nm;
	private String wrkjob_mgr_tel_num;
	private String tuning_rcess_why;
	private String tuning_rcess_dt;

	private String tuningNoArry;

	private String tuning_requester_id;

	private String choiceTmsArray;
	private String choice_tms;
	private String autoChoiceCondNoArray;
	private String auto_choice_cond_no;

	public String getExcept_target_yn() {
		return except_target_yn;
	}

	public void setExcept_target_yn(String except_target_yn) {
		this.except_target_yn = except_target_yn;
	}

	public String getTuning_complete_why_cd() {
		return tuning_complete_why_cd;
	}

	public void setTuning_complete_why_cd(String tuning_complete_why_cd) {
		this.tuning_complete_why_cd = tuning_complete_why_cd;
	}

	public String getTuning_complete_why() {
		return tuning_complete_why;
	}

	public void setTuning_complete_why(String tuning_complete_why) {
		this.tuning_complete_why = tuning_complete_why;
	}

	public String getTuning_complete_dt() {
		return tuning_complete_dt;
	}

	public void setTuning_complete_dt(String tuning_complete_dt) {
		this.tuning_complete_dt = tuning_complete_dt;
	}

	public String getTuning_completer_id() {
		return tuning_completer_id;
	}

	public void setTuning_completer_id(String tuning_completer_id) {
		this.tuning_completer_id = tuning_completer_id;
	}

	public String getTuning_apply_rcess_why() {
		return tuning_apply_rcess_why;
	}

	public void setTuning_apply_rcess_why(String tuning_apply_rcess_why) {
		this.tuning_apply_rcess_why = tuning_apply_rcess_why;
	}

	public String getTuning_apply_rcesser_id() {
		return tuning_apply_rcesser_id;
	}

	public void setTuning_apply_rcesser_id(String tuning_apply_rcesser_id) {
		this.tuning_apply_rcesser_id = tuning_apply_rcesser_id;
	}

	public String getTuning_apply_rcess_dt() {
		return tuning_apply_rcess_dt;
	}

	public void setTuning_apply_rcess_dt(String tuning_apply_rcess_dt) {
		this.tuning_apply_rcess_dt = tuning_apply_rcess_dt;
	}

	public String getTuning_apply_dt() {
		return tuning_apply_dt;
	}

	public void setTuning_apply_dt(String tuning_apply_dt) {
		this.tuning_apply_dt = tuning_apply_dt;
	}

	public String getTuning_applyer_id() {
		return tuning_applyer_id;
	}

	public void setTuning_applyer_id(String tuning_applyer_id) {
		this.tuning_applyer_id = tuning_applyer_id;
	}

	public String getTuning_end_why_cd() {
		return tuning_end_why_cd;
	}

	public void setTuning_end_why_cd(String tuning_end_why_cd) {
		this.tuning_end_why_cd = tuning_end_why_cd;
	}

	public String getTuning_end_why() {
		return tuning_end_why;
	}

	public void setTuning_end_why(String tuning_end_why) {
		this.tuning_end_why = tuning_end_why;
	}

	public String getTuning_ender_id() {
		return tuning_ender_id;
	}

	public void setTuning_ender_id(String tuning_ender_id) {
		this.tuning_ender_id = tuning_ender_id;
	}

	public String getTuning_end_dt() {
		return tuning_end_dt;
	}

	public void setTuning_end_dt(String tuning_end_dt) {
		this.tuning_end_dt = tuning_end_dt;
	}

	public String getTuning_case_posting_yn() {
		return tuning_case_posting_yn;
	}

	public void setTuning_case_posting_yn(String tuning_case_posting_yn) {
		this.tuning_case_posting_yn = tuning_case_posting_yn;
	}

	public String getTuning_case_posting_title() {
		return tuning_case_posting_title;
	}

	public void setTuning_case_posting_title(String tuning_case_posting_title) {
		this.tuning_case_posting_title = tuning_case_posting_title;
	}

	public String getAll_tuning_end_dt() {
		return all_tuning_end_dt;
	}

	public void setAll_tuning_end_dt(String all_tuning_end_dt) {
		this.all_tuning_end_dt = all_tuning_end_dt;
	}

	public String getImprb_elap_time() {
		return imprb_elap_time;
	}

	public void setImprb_elap_time(String imprb_elap_time) {
		this.imprb_elap_time = imprb_elap_time;
	}

	public String getImprb_buffer_cnt() {
		return imprb_buffer_cnt;
	}

	public void setImprb_buffer_cnt(String imprb_buffer_cnt) {
		this.imprb_buffer_cnt = imprb_buffer_cnt;
	}

	public String getImprb_pga_usage() {
		return imprb_pga_usage;
	}

	public void setImprb_pga_usage(String imprb_pga_usage) {
		this.imprb_pga_usage = imprb_pga_usage;
	}

	public String getImpra_elap_time() {
		return impra_elap_time;
	}

	public void setImpra_elap_time(String impra_elap_time) {
		this.impra_elap_time = impra_elap_time;
	}

	public String getImpra_buffer_cnt() {
		return impra_buffer_cnt;
	}

	public void setImpra_buffer_cnt(String impra_buffer_cnt) {
		this.impra_buffer_cnt = impra_buffer_cnt;
	}

	public String getImpra_pga_usage() {
		return impra_pga_usage;
	}

	public void setImpra_pga_usage(String impra_pga_usage) {
		this.impra_pga_usage = impra_pga_usage;
	}

	public String getElap_time_impr_ratio() {
		return elap_time_impr_ratio;
	}

	public void setElap_time_impr_ratio(String elap_time_impr_ratio) {
		this.elap_time_impr_ratio = elap_time_impr_ratio;
	}

	public String getBuffer_impr_ratio() {
		return buffer_impr_ratio;
	}

	public void setBuffer_impr_ratio(String buffer_impr_ratio) {
		this.buffer_impr_ratio = buffer_impr_ratio;
	}

	public String getPga_impr_ratio() {
		return pga_impr_ratio;
	}

	public void setPga_impr_ratio(String pga_impr_ratio) {
		this.pga_impr_ratio = pga_impr_ratio;
	}

	public String getControversialist() {
		return controversialist;
	}

	public void setControversialist(String controversialist) {
		this.controversialist = controversialist;
	}

	public String getImpr_sbst() {
		return impr_sbst;
	}

	public void setImpr_sbst(String impr_sbst) {
		this.impr_sbst = impr_sbst;
	}

	public String getImpr_sql_text() {
		return impr_sql_text;
	}

	public void setImpr_sql_text(String impr_sql_text) {
		this.impr_sql_text = impr_sql_text;
	}

	public String getImprb_exec_plan() {
		return imprb_exec_plan;
	}

	public void setImprb_exec_plan(String imprb_exec_plan) {
		this.imprb_exec_plan = imprb_exec_plan;
	}

	public String getImpra_exec_plan() {
		return impra_exec_plan;
	}

	public void setImpra_exec_plan(String impra_exec_plan) {
		this.impra_exec_plan = impra_exec_plan;
	}

	public String getChoice_div_cd() {
		return choice_div_cd;
	}

	public void setChoice_div_cd(String choice_div_cd) {
		this.choice_div_cd = choice_div_cd;
	}

	public String getTuning_status_cd() {
		return tuning_status_cd;
	}

	public void setTuning_status_cd(String tuning_status_cd) {
		this.tuning_status_cd = tuning_status_cd;
	}

	public String getTuning_status_nm() {
		return tuning_status_nm;
	}

	public void setTuning_status_nm(String tuning_status_nm) {
		this.tuning_status_nm = tuning_status_nm;
	}

	public String getPerfr_id() {
		return perfr_id;
	}

	public void setPerfr_id(String perfr_id) {
		this.perfr_id = perfr_id;
	}

	public String getWrkjob_mgr_nm() {
		return wrkjob_mgr_nm;
	}

	public void setWrkjob_mgr_nm(String wrkjob_mgr_nm) {
		this.wrkjob_mgr_nm = wrkjob_mgr_nm;
	}

	public String getWrkjob_mgr_wrkjob_cd() {
		return wrkjob_mgr_wrkjob_cd;
	}

	public void setWrkjob_mgr_wrkjob_cd(String wrkjob_mgr_wrkjob_cd) {
		this.wrkjob_mgr_wrkjob_cd = wrkjob_mgr_wrkjob_cd;
	}

	public String getWrkjob_mgr_wrkjob_nm() {
		return wrkjob_mgr_wrkjob_nm;
	}

	public void setWrkjob_mgr_wrkjob_nm(String wrkjob_mgr_wrkjob_nm) {
		this.wrkjob_mgr_wrkjob_nm = wrkjob_mgr_wrkjob_nm;
	}

	public String getWrkjob_mgr_tel_num() {
		return wrkjob_mgr_tel_num;
	}

	public void setWrkjob_mgr_tel_num(String wrkjob_mgr_tel_num) {
		this.wrkjob_mgr_tel_num = wrkjob_mgr_tel_num;
	}

	public String getTuning_rcess_why() {
		return tuning_rcess_why;
	}

	public void setTuning_rcess_why(String tuning_rcess_why) {
		this.tuning_rcess_why = tuning_rcess_why;
	}

	public String getTuning_rcess_dt() {
		return tuning_rcess_dt;
	}

	public void setTuning_rcess_dt(String tuning_rcess_dt) {
		this.tuning_rcess_dt = tuning_rcess_dt;
	}

	public String getTuningNoArry() {
		return tuningNoArry;
	}

	public void setTuningNoArry(String tuningNoArry) {
		this.tuningNoArry = tuningNoArry;
	}

	public String getTuning_requester_id() {
		return tuning_requester_id;
	}

	public void setTuning_requester_id(String tuning_requester_id) {
		this.tuning_requester_id = tuning_requester_id;
	}

	public String getChoiceTmsArray() {
		return choiceTmsArray;
	}

	public void setChoiceTmsArray(String choiceTmsArray) {
		this.choiceTmsArray = choiceTmsArray;
	}

	public String getChoice_tms() {
		return choice_tms;
	}

	public void setChoice_tms(String choice_tms) {
		this.choice_tms = choice_tms;
	}

	public String getAutoChoiceCondNoArray() {
		return autoChoiceCondNoArray;
	}

	public void setAutoChoiceCondNoArray(String autoChoiceCondNoArray) {
		this.autoChoiceCondNoArray = autoChoiceCondNoArray;
	}

	public String getAuto_choice_cond_no() {
		return auto_choice_cond_no;
	}

	public void setAuto_choice_cond_no(String auto_choice_cond_no) {
		this.auto_choice_cond_no = auto_choice_cond_no;
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