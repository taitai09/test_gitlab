package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.10.16	이원식	최초작성
 **********************************************************/

@Alias("trTop")
public class TrTop extends Base implements Jsonable {
	
	private String wrkjob_cd;
	private String tr_cd;
    private String tr_cd_nm;
    private String mgr_id;
    private String prev_year_last_day;
    private String prev_month_last_day;
    private String base_day;
    private String prev_year_avg_elap_time;
    private String prev_month_avg_elap_time;
    private String avg_elap_time;
    private String tot_tr_cnt;
    private String gap_avg_elap_time;
    
	public String getWrkjob_cd() {
		return wrkjob_cd;
	}
	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}
	public String getTr_cd() {
		return tr_cd;
	}
	public void setTr_cd(String tr_cd) {
		this.tr_cd = tr_cd;
	}
	public String getTr_cd_nm() {
		return tr_cd_nm;
	}
	public void setTr_cd_nm(String tr_cd_nm) {
		this.tr_cd_nm = tr_cd_nm;
	}
	public String getMgr_id() {
		return mgr_id;
	}
	public void setMgr_id(String mgr_id) {
		this.mgr_id = mgr_id;
	}
	public String getPrev_year_last_day() {
		return prev_year_last_day;
	}
	public void setPrev_year_last_day(String prev_year_last_day) {
		this.prev_year_last_day = prev_year_last_day;
	}
	public String getPrev_month_last_day() {
		return prev_month_last_day;
	}
	public void setPrev_month_last_day(String prev_month_last_day) {
		this.prev_month_last_day = prev_month_last_day;
	}
	public String getBase_day() {
		return base_day;
	}
	public void setBase_day(String base_day) {
		this.base_day = base_day;
	}
	public String getPrev_year_avg_elap_time() {
		return prev_year_avg_elap_time;
	}
	public void setPrev_year_avg_elap_time(String prev_year_avg_elap_time) {
		this.prev_year_avg_elap_time = prev_year_avg_elap_time;
	}
	public String getPrev_month_avg_elap_time() {
		return prev_month_avg_elap_time;
	}
	public void setPrev_month_avg_elap_time(String prev_month_avg_elap_time) {
		this.prev_month_avg_elap_time = prev_month_avg_elap_time;
	}
	public String getAvg_elap_time() {
		return avg_elap_time;
	}
	public void setAvg_elap_time(String avg_elap_time) {
		this.avg_elap_time = avg_elap_time;
	}
	public String getTot_tr_cnt() {
		return tot_tr_cnt;
	}
	public void setTot_tr_cnt(String tot_tr_cnt) {
		this.tot_tr_cnt = tot_tr_cnt;
	}	
	public String getGap_avg_elap_time() {
		return gap_avg_elap_time;
	}
	public void setGap_avg_elap_time(String gap_avg_elap_time) {
		this.gap_avg_elap_time = gap_avg_elap_time;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("wrkjob_cd",this.getWrkjob_cd());
		objJson.put("tr_cd",this.getTr_cd());
		objJson.put("tr_cd_nm",this.getTr_cd_nm());
		objJson.put("mgr_id",this.getMgr_id());
		objJson.put("prev_year_last_day",this.getPrev_year_last_day());
		objJson.put("prev_month_last_day",this.getPrev_month_last_day());
		objJson.put("base_day",this.getBase_day());
		objJson.put("prev_year_avg_elap_time",this.getPrev_year_avg_elap_time());
		objJson.put("prev_month_avg_elap_time",this.getPrev_month_avg_elap_time());
		objJson.put("avg_elap_time",this.getAvg_elap_time());
		objJson.put("tot_tr_cnt",this.getTot_tr_cnt());
		objJson.put("gap_avg_elap_time",this.getGap_avg_elap_time());

		return objJson;
	}		
}