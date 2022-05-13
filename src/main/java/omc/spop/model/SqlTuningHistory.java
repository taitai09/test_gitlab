package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.04.16	이원식	최초작성
 **********************************************************/

@Alias("sqlTuningHistory")
public class SqlTuningHistory extends Base implements Jsonable {
	
    private String update_dt;
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
    
	public String getUpdate_dt() {
		return update_dt;
	}
	public void setUpdate_dt(String update_dt) {
		this.update_dt = update_dt;
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
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();

		objJson.put("tuning_no",this.getTuning_no());
		objJson.put("update_dt",this.getUpdate_dt());
		objJson.put("imprb_elap_time",StringUtil.parseFloat(this.getImprb_elap_time(),0));
		objJson.put("imprb_buffer_cnt",this.getImprb_buffer_cnt());
		objJson.put("imprb_pga_usage",this.getImprb_pga_usage());
		objJson.put("impra_elap_time",StringUtil.parseFloat(this.getImpra_elap_time(),0));
		objJson.put("impra_buffer_cnt",this.getImpra_buffer_cnt());
		objJson.put("impra_pga_usage",this.getImpra_pga_usage());
		objJson.put("elap_time_impr_ratio",StringUtil.parseFloat(this.getElap_time_impr_ratio(),0));
		objJson.put("buffer_impr_ratio",StringUtil.parseFloat(this.getBuffer_impr_ratio(),0));
		objJson.put("pga_impr_ratio",StringUtil.parseFloat(this.getPga_impr_ratio(),0));
		objJson.put("controversialist",this.getControversialist());
		objJson.put("impr_sbst",this.getImpr_sbst());
		objJson.put("impr_sql_text",this.getImpr_sql_text());
		objJson.put("imprb_exec_plan",this.getImprb_exec_plan());
		objJson.put("impra_exec_plan",this.getImpra_exec_plan()); 
		
		return objJson;
	}    
}