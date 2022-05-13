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
import oracle.sql.CLOB;

/***********************************************************
 * 2021.06.14	황예지	필드추가
 **********************************************************/
@Alias("analyzeDbChangeSqlImpact")
public class AnalyzeDbChangeSqlImpact extends Base implements Jsonable {
	
	private String sql_id;
	private String deprecated_yn;
	private String ora_01797_yn;
	private String ora_00979_yn;
	private String order_by_yn;
	private String perf_impact_type_nm;
	private String buffer_increase_ratio;
	private String elapsed_time_increase_ratio;
	private String plan_change_yn;
	private String asis_executions;
	private String asis_elapsed_time;
	private String tobe_elapsed_time;
	private String asis_buffer_gets;
	private String tobe_buffer_gets;
	private String sql_text_web;
	private CLOB sql_text_excel;
	
	private String sql_cnt;
	private String change_sql_cnt;
	private String deprecated_sql_cnt;
	private String ora_01719_cnt;
	private String ora_00979_cnt;
	private String order_by_cnt;
	
	private String ora_30563_yn;
	private String wm_concat_yn;
	private String ora_30563_cnt;
	private String wm_concat_cnt;
	
	private String project_id;
	private String sql_auto_perf_check_id;
	private String asis_oracle_version_cd;
	private String tobe_oracle_version_cd;
	
	private String bypass_ujvc_cnt;
	private String and_equal_cnt;
	private String merge_aj_cnt;
	private String hash_aj_cnt;
	private String nl_aj_cnt;
	private String hash_sj_cnt;
	private String merge_sj_cnt;
	private String nl_sj_cnt;
	private String ordered_predicates_cnt;
	private String rowid_cnt;
	private String star_cnt;
	private String noparallel_cnt;
	private String noparallel_index_cnt;
	private String norewrite_cnt;
	
	@Override
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

	public String getBypass_ujvc_cnt() {
		return bypass_ujvc_cnt;
	}

	public void setBypass_ujvc_cnt(String bypass_ujvc_cnt) {
		this.bypass_ujvc_cnt = bypass_ujvc_cnt;
	}

	public String getAnd_equal_cnt() {
		return and_equal_cnt;
	}

	public void setAnd_equal_cnt(String and_equal_cnt) {
		this.and_equal_cnt = and_equal_cnt;
	}

	public String getMerge_aj_cnt() {
		return merge_aj_cnt;
	}

	public void setMerge_aj_cnt(String merge_aj_cnt) {
		this.merge_aj_cnt = merge_aj_cnt;
	}

	public String getHash_aj_cnt() {
		return hash_aj_cnt;
	}

	public void setHash_aj_cnt(String hash_aj_cnt) {
		this.hash_aj_cnt = hash_aj_cnt;
	}

	public String getNl_aj_cnt() {
		return nl_aj_cnt;
	}

	public void setNl_aj_cnt(String nl_aj_cnt) {
		this.nl_aj_cnt = nl_aj_cnt;
	}

	public String getHash_sj_cnt() {
		return hash_sj_cnt;
	}

	public void setHash_sj_cnt(String hash_sj_cnt) {
		this.hash_sj_cnt = hash_sj_cnt;
	}

	public String getMerge_sj_cnt() {
		return merge_sj_cnt;
	}

	public void setMerge_sj_cnt(String merge_sj_cnt) {
		this.merge_sj_cnt = merge_sj_cnt;
	}

	public String getNl_sj_cnt() {
		return nl_sj_cnt;
	}

	public void setNl_sj_cnt(String nl_sj_cnt) {
		this.nl_sj_cnt = nl_sj_cnt;
	}

	public String getOrdered_predicates_cnt() {
		return ordered_predicates_cnt;
	}

	public void setOrdered_predicates_cnt(String ordered_predicates_cnt) {
		this.ordered_predicates_cnt = ordered_predicates_cnt;
	}

	public String getRowid_cnt() {
		return rowid_cnt;
	}

	public void setRowid_cnt(String rowid_cnt) {
		this.rowid_cnt = rowid_cnt;
	}

	public String getStar_cnt() {
		return star_cnt;
	}

	public void setStar_cnt(String star_cnt) {
		this.star_cnt = star_cnt;
	}

	public String getNoparallel_cnt() {
		return noparallel_cnt;
	}

	public void setNoparallel_cnt(String noparallel_cnt) {
		this.noparallel_cnt = noparallel_cnt;
	}

	public String getNoparallel_index_cnt() {
		return noparallel_index_cnt;
	}

	public void setNoparallel_index_cnt(String noparallel_index_cnt) {
		this.noparallel_index_cnt = noparallel_index_cnt;
	}

	public String getNorewrite_cnt() {
		return norewrite_cnt;
	}

	public void setNorewrite_cnt(String norewrite_cnt) {
		this.norewrite_cnt = norewrite_cnt;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getSql_auto_perf_check_id() {
		return sql_auto_perf_check_id;
	}

	public void setSql_auto_perf_check_id(String sql_auto_perf_check_id) {
		this.sql_auto_perf_check_id = sql_auto_perf_check_id;
	}

	public String getAsis_oracle_version_cd() {
		return asis_oracle_version_cd;
	}

	public void setAsis_oracle_version_cd(String asis_oracle_version_cd) {
		this.asis_oracle_version_cd = asis_oracle_version_cd;
	}

	public String getTobe_oracle_version_cd() {
		return tobe_oracle_version_cd;
	}

	public void setTobe_oracle_version_cd(String tobe_oracle_version_cd) {
		this.tobe_oracle_version_cd = tobe_oracle_version_cd;
	}

	public String getSql_id() {
		return sql_id;
	}

	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}

	public String getDeprecated_yn() {
		return deprecated_yn;
	}

	public void setDeprecated_yn(String deprecated_yn) {
		this.deprecated_yn = deprecated_yn;
	}

	public String getOra_01797_yn() {
		return ora_01797_yn;
	}

	public void setOra_01797_yn(String ora_01797_yn) {
		this.ora_01797_yn = ora_01797_yn;
	}

	public String getOra_00979_yn() {
		return ora_00979_yn;
	}

	public void setOra_00979_yn(String ora_00979_yn) {
		this.ora_00979_yn = ora_00979_yn;
	}

	public String getOrder_by_yn() {
		return order_by_yn;
	}

	public void setOrder_by_yn(String order_by_yn) {
		this.order_by_yn = order_by_yn;
	}

	public String getPerf_impact_type_nm() {
		return perf_impact_type_nm;
	}

	public void setPerf_impact_type_nm(String perf_impact_type_nm) {
		this.perf_impact_type_nm = perf_impact_type_nm;
	}

	public String getBuffer_increase_ratio() {
		return buffer_increase_ratio;
	}

	public void setBuffer_increase_ratio(String buffer_increase_ratio) {
		this.buffer_increase_ratio = buffer_increase_ratio;
	}

	public String getElapsed_time_increase_ratio() {
		return elapsed_time_increase_ratio;
	}

	public void setElapsed_time_increase_ratio(String elapsed_time_increase_ratio) {
		this.elapsed_time_increase_ratio = elapsed_time_increase_ratio;
	}

	public String getPlan_change_yn() {
		return plan_change_yn;
	}

	public void setPlan_change_yn(String plan_change_yn) {
		this.plan_change_yn = plan_change_yn;
	}

	public String getAsis_executions() {
		return asis_executions;
	}

	public void setAsis_executions(String asis_executions) {
		this.asis_executions = asis_executions;
	}

	public String getAsis_elapsed_time() {
		return asis_elapsed_time;
	}

	public void setAsis_elapsed_time(String asis_elapsed_time) {
		this.asis_elapsed_time = asis_elapsed_time;
	}

	public String getTobe_elapsed_time() {
		return tobe_elapsed_time;
	}

	public void setTobe_elapsed_time(String tobe_elapsed_time) {
		this.tobe_elapsed_time = tobe_elapsed_time;
	}

	public String getAsis_buffer_gets() {
		return asis_buffer_gets;
	}

	public void setAsis_buffer_gets(String asis_buffer_gets) {
		this.asis_buffer_gets = asis_buffer_gets;
	}

	public String getTobe_buffer_gets() {
		return tobe_buffer_gets;
	}

	public void setTobe_buffer_gets(String tobe_buffer_gets) {
		this.tobe_buffer_gets = tobe_buffer_gets;
	}

	public String getSql_text_web() {
		return sql_text_web;
	}

	public void setSql_text_web(String sql_text_web) {
		this.sql_text_web = sql_text_web;
	}

	public CLOB getSql_text_excel() {
		return sql_text_excel;
	}

	public void setSql_text_excel(CLOB sql_text_excel) {
		this.sql_text_excel = sql_text_excel;
	}

	public String getSql_cnt() {
		return sql_cnt;
	}

	public void setSql_cnt(String sql_cnt) {
		this.sql_cnt = sql_cnt;
	}

	public String getChange_sql_cnt() {
		return change_sql_cnt;
	}

	public void setChange_sql_cnt(String change_sql_cnt) {
		this.change_sql_cnt = change_sql_cnt;
	}

	public String getDeprecated_sql_cnt() {
		return deprecated_sql_cnt;
	}

	public void setDeprecated_sql_cnt(String deprecated_sql_cnt) {
		this.deprecated_sql_cnt = deprecated_sql_cnt;
	}

	public String getOra_01719_cnt() {
		return ora_01719_cnt;
	}

	public void setOra_01719_cnt(String ora_01719_cnt) {
		this.ora_01719_cnt = ora_01719_cnt;
	}

	public String getOra_00979_cnt() {
		return ora_00979_cnt;
	}

	public void setOra_00979_cnt(String ora_00979_cnt) {
		this.ora_00979_cnt = ora_00979_cnt;
	}

	public String getOrder_by_cnt() {
		return order_by_cnt;
	}

	public void setOrder_by_cnt(String order_by_cnt) {
		this.order_by_cnt = order_by_cnt;
	}

	public String getOra_30563_cnt() {
		return ora_30563_cnt;
	}

	public void setOra_30563_cnt(String ora_30563_cnt) {
		this.ora_30563_cnt = ora_30563_cnt;
	}

	public String getWm_concat_cnt() {
		return wm_concat_cnt;
	}

	public void setWm_concat_cnt(String wm_concat_cnt) {
		this.wm_concat_cnt = wm_concat_cnt;
	}

	public String getOra_30563_yn() {
		return ora_30563_yn;
	}

	public void setOra_30563_yn(String ora_30563_yn) {
		this.ora_30563_yn = ora_30563_yn;
	}

	public String getWm_concat_yn() {
		return wm_concat_yn;
	}

	public void setWm_concat_yn(String wm_concat_yn) {
		this.wm_concat_yn = wm_concat_yn;
	}
	
}
