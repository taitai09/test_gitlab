package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("sgaTargetAdvisor")
public class SgaTargetAdvisor extends Base implements Jsonable {
	
    private String check_day;
    private String check_seq;
    private String inst_id;
    private String sga_size;
    private String sga_size_factor;
    private String estd_db_time;
    private String estd_db_time_factor;
    private String estd_physical_reads;
    
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
	public String getSga_size() {
		return sga_size;
	}
	public void setSga_size(String sga_size) {
		this.sga_size = sga_size;
	}
	public String getSga_size_factor() {
		return sga_size_factor;
	}
	public void setSga_size_factor(String sga_size_factor) {
		this.sga_size_factor = sga_size_factor;
	}
	public String getEstd_db_time() {
		return estd_db_time;
	}
	public void setEstd_db_time(String estd_db_time) {
		this.estd_db_time = estd_db_time;
	}
	public String getEstd_db_time_factor() {
		return estd_db_time_factor;
	}
	public void setEstd_db_time_factor(String estd_db_time_factor) {
		this.estd_db_time_factor = estd_db_time_factor;
	}
	public String getEstd_physical_reads() {
		return estd_physical_reads;
	}
	public void setEstd_physical_reads(String estd_physical_reads) {
		this.estd_physical_reads = estd_physical_reads;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("inst_id",this.getInst_id());
		objJson.put("sga_size",StringUtil.parseDouble(this.getSga_size(),0));
		objJson.put("sga_size_factor",StringUtil.parseFloat(this.getSga_size_factor(),0));
		objJson.put("estd_db_time",StringUtil.parseDouble(this.getEstd_db_time(),0));
		objJson.put("estd_db_time_factor",StringUtil.parseFloat(this.getEstd_db_time_factor(),0));
		objJson.put("estd_physical_reads",StringUtil.parseDouble(this.getEstd_physical_reads(),0));
		
		return objJson; 
	}		
}
