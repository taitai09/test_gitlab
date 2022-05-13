package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.10.17	이원식	최초작성
 **********************************************************/

@Alias("dbioByResourceUsage")
public class DbioByResourceUsage extends Base implements Jsonable {
	
	private String base_day;
	private String wrkjob_cd;
	private String tr_cd;
	private String dbio_nm;
	private String cpu_time;
	private String mem_use_qnt;
	private String exec_cnt;
	private String avg_elap_time;
	
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
	public String getTr_cd() {
		return tr_cd;
	}
	public void setTr_cd(String tr_cd) {
		this.tr_cd = tr_cd;
	}
	public String getDbio_nm() {
		return dbio_nm;
	}
	public void setDbio_nm(String dbio_nm) {
		this.dbio_nm = dbio_nm;
	}
	public String getCpu_time() {
		return cpu_time;
	}
	public void setCpu_time(String cpu_time) {
		this.cpu_time = cpu_time;
	}
	public String getMem_use_qnt() {
		return mem_use_qnt;
	}
	public void setMem_use_qnt(String mem_use_qnt) {
		this.mem_use_qnt = mem_use_qnt;
	}
	public String getExec_cnt() {
		return exec_cnt;
	}
	public void setExec_cnt(String exec_cnt) {
		this.exec_cnt = exec_cnt;
	}
	public String getAvg_elap_time() {
		return avg_elap_time;
	}
	public void setAvg_elap_time(String avg_elap_time) {
		this.avg_elap_time = avg_elap_time;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("base_day",this.getBase_day());
		objJson.put("wrkjob_cd",this.getWrkjob_cd());
		objJson.put("tr_cd",this.getTr_cd());
		objJson.put("dbio_nm",this.getDbio_nm());
		objJson.put("cpu_time",this.getCpu_time());
		objJson.put("mem_use_qnt",this.getMem_use_qnt());
		objJson.put("exec_cnt",this.getExec_cnt());		
		objJson.put("avg_elap_time",StringUtil.parseFloat(this.getAvg_elap_time(),0));

		return objJson;
	}		
}