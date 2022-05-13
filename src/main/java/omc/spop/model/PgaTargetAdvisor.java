package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("pgaTargetAdvisor")
public class PgaTargetAdvisor extends Base implements Jsonable {
	
    private String check_day;
    private String check_seq;
    private String inst_id;
    private String pga_target_for_estimate;
    private String pga_target_factor;
    private String bytes_processed;
    private String estd_time;
    private String estd_extra_bytes_rw;
    private String estd_pga_cache_hit_percentage;
    private String estd_overalloc_count;
    
	public String getCheck_day() {
		return check_day;
	}
	public void setCheck_day(String check_day) {
		this.check_day = check_day;
	}
	public String getCheck_seq() {
		return check_seq;
	}
	public void setCheck_seq(String check_seq) {
		this.check_seq = check_seq;
	}
	public String getInst_id() {
		return inst_id;
	}
	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}
	public String getPga_target_for_estimate() {
		return pga_target_for_estimate;
	}
	public void setPga_target_for_estimate(String pga_target_for_estimate) {
		this.pga_target_for_estimate = pga_target_for_estimate;
	}
	public String getPga_target_factor() {
		return pga_target_factor;
	}
	public void setPga_target_factor(String pga_target_factor) {
		this.pga_target_factor = pga_target_factor;
	}
	public String getBytes_processed() {
		return bytes_processed;
	}
	public void setBytes_processed(String bytes_processed) {
		this.bytes_processed = bytes_processed;
	}
	public String getEstd_time() {
		return estd_time;
	}
	public void setEstd_time(String estd_time) {
		this.estd_time = estd_time;
	}
	public String getEstd_extra_bytes_rw() {
		return estd_extra_bytes_rw;
	}
	public void setEstd_extra_bytes_rw(String estd_extra_bytes_rw) {
		this.estd_extra_bytes_rw = estd_extra_bytes_rw;
	}
	public String getEstd_pga_cache_hit_percentage() {
		return estd_pga_cache_hit_percentage;
	}
	public void setEstd_pga_cache_hit_percentage(String estd_pga_cache_hit_percentage) {
		this.estd_pga_cache_hit_percentage = estd_pga_cache_hit_percentage;
	}
	public String getEstd_overalloc_count() {
		return estd_overalloc_count;
	}
	public void setEstd_overalloc_count(String estd_overalloc_count) {
		this.estd_overalloc_count = estd_overalloc_count;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("inst_id",this.getInst_id());
		objJson.put("pga_target_for_estimate",StringUtil.parseDouble(this.getPga_target_for_estimate(),0));
		objJson.put("pga_target_factor",StringUtil.parseFloat(this.getPga_target_factor(),0));
		objJson.put("bytes_processed",StringUtil.parseDouble(this.getBytes_processed(),0));
		objJson.put("estd_time",StringUtil.parseDouble(this.getEstd_time(),0));
		objJson.put("estd_extra_bytes_rw",StringUtil.parseDouble(this.getEstd_extra_bytes_rw(),0));
		objJson.put("estd_pga_cache_hit_percentage",StringUtil.parseDouble(this.getEstd_pga_cache_hit_percentage(),0));
		objJson.put("estd_overalloc_count",StringUtil.parseDouble(this.getEstd_overalloc_count(),0));

		return objJson; 
	}		
}
