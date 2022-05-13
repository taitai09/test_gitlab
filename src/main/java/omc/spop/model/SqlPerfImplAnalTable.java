package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.01.10	이원식	최초작성
 * 2021.02.17	황예지	필드추가
 **********************************************************/

@Alias("sqlPerfImplAnalTable")
public class SqlPerfImplAnalTable extends Base implements Jsonable {
	
	private String sql_perf_impl_anal_no;
	private String table_owner;
	private String table_name;
	private String plan_change_cnt;
	private String perf_regress_cnt;
	private String sql_cnt;
	private String perf_impact_type_cd_cnt;
	private String rnum;
	private String plan_change_yn_cnt;

	public String getSql_perf_impl_anal_no() {
		return sql_perf_impl_anal_no;
	}
	public void setSql_perf_impl_anal_no(String sql_perf_impl_anal_no) {
		this.sql_perf_impl_anal_no = sql_perf_impl_anal_no;
	}
	public String getTable_owner() {
		return table_owner;
	}
	public void setTable_owner(String table_owner) {
		this.table_owner = table_owner;
	}
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	public String getPlan_change_cnt() {
		return plan_change_cnt;
	}
	public void setPlan_change_cnt(String plan_change_cnt) {
		this.plan_change_cnt = plan_change_cnt;
	}
	public String getPerf_regress_cnt() {
		return perf_regress_cnt;
	}
	public void setPerf_regress_cnt(String perf_regress_cnt) {
		this.perf_regress_cnt = perf_regress_cnt;
	}
	public String getSql_cnt() {
		return sql_cnt;
	}
	public void setSql_cnt(String sql_cnt) {
		this.sql_cnt = sql_cnt;
	}
	
	public String getPerf_impact_type_cd_cnt() {
		return perf_impact_type_cd_cnt;
	}
	public void setPerf_impact_type_cd_cnt(String perf_impact_type_cd_cnt) {
		this.perf_impact_type_cd_cnt = perf_impact_type_cd_cnt;
	}
	public String getRnum() {
		return rnum;
	}
	public void setRnum(String rnum) {
		this.rnum = rnum;
	}
	public String getPlan_change_yn_cnt() {
		return plan_change_yn_cnt;
	}
	public void setPlan_change_yn_cnt(String plan_change_yn_cnt) {
		this.plan_change_yn_cnt = plan_change_yn_cnt;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("rnum", StringUtil.parseInt(this.getRnum(),0));
		objJson.put("dbid", this.getDbid());
		objJson.put("db_name", this.getDb_name());
		objJson.put("sql_perf_impl_anal_no", StringUtil.parseInt(this.getSql_perf_impl_anal_no(),0));
		objJson.put("table_owner", this.getTable_owner());
		objJson.put("table_name", this.getTable_name());
		objJson.put("plan_change_cnt", StringUtil.parseInt(this.getPlan_change_cnt(),0));
		objJson.put("perf_regress_cnt", StringUtil.parseInt(this.getPerf_regress_cnt(),0));
		objJson.put("sql_cnt", StringUtil.parseInt(this.getSql_cnt(),0));
		objJson.put("plan_change_yn_cnt", StringUtil.parseInt(this.getPlan_change_yn_cnt(),0));
		objJson.put("perf_impact_type_cd_cnt", StringUtil.parseInt(this.getPerf_impact_type_cd_cnt(),0));

		return objJson;
	}		
}