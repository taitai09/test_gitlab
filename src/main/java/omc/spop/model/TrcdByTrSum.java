package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.11.03	이원식	최초작성
 **********************************************************/

@Alias("trcdByTrSum")
public class TrcdByTrSum extends Base implements Jsonable {
	
	private String base_day;
	private String wrkjob_cd;
	private String wrkjob_cd_nm;
	private String tr_cd;
	private String tot_tr_cnt;
    private String avg_elap_time;
    private String gap_avg_elap_time;
    private String tmout_cnt;
    private String err_cd;
    private String err_nm;
    private String err_cnt;
    
	public String getBase_day() {
		return base_day;
	}
	public void setBase_day(String base_day) {
		this.base_day = base_day;
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
	public String getTr_cd() {
		return tr_cd;
	}
	public void setTr_cd(String tr_cd) {
		this.tr_cd = tr_cd;
	}
	public String getTot_tr_cnt() {
		return tot_tr_cnt;
	}
	public void setTot_tr_cnt(String tot_tr_cnt) {
		this.tot_tr_cnt = tot_tr_cnt;
	}
	public String getAvg_elap_time() {
		return avg_elap_time;
	}
	public void setAvg_elap_time(String avg_elap_time) {
		this.avg_elap_time = avg_elap_time;
	}
	public String getGap_avg_elap_time() {
		return gap_avg_elap_time;
	}
	public void setGap_avg_elap_time(String gap_avg_elap_time) {
		this.gap_avg_elap_time = gap_avg_elap_time;
	}
	public String getTmout_cnt() {
		return tmout_cnt;
	}
	public void setTmout_cnt(String tmout_cnt) {
		this.tmout_cnt = tmout_cnt;
	}	
	public String getErr_cd() {
		return err_cd;
	}
	public void setErr_cd(String err_cd) {
		this.err_cd = err_cd;
	}
	public String getErr_nm() {
		return err_nm;
	}
	public void setErr_nm(String err_nm) {
		this.err_nm = err_nm;
	}
	public String getErr_cnt() {
		return err_cnt;
	}
	public void setErr_cnt(String err_cnt) {
		this.err_cnt = err_cnt;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("base_day",this.getBase_day());
		objJson.put("wrkjob_cd",this.getWrkjob_cd());
		objJson.put("wrkjob_cd_nm",this.getWrkjob_cd_nm());
		objJson.put("tr_cd",this.getTr_cd());
		objJson.put("tot_tr_cnt",this.getTot_tr_cnt());
		objJson.put("avg_elap_time",this.getAvg_elap_time());
		objJson.put("tmout_cnt",this.getTmout_cnt());

		return objJson;
	}		
}