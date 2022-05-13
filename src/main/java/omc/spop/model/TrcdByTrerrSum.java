package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.10.17	이원식	최초작성
 **********************************************************/

@Alias("trcdByTrerrSum")
public class TrcdByTrerrSum extends Base implements Jsonable {
	
	private String wrkjob_cd;
	private String tr_cd;
	private String tr_cd_nm;
	private String err_cd;
	private String err_nm;
	private String err_cnt;
    private String base_day;
    
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
	public String getBase_day() {
		return base_day;
	}
	public void setBase_day(String base_day) {
		this.base_day = base_day;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("wrkjob_cd",this.getWrkjob_cd());
		objJson.put("tr_cd",this.getTr_cd());
		objJson.put("tr_cd_nm",this.getTr_cd_nm());
		objJson.put("err_cd",this.getErr_cd());
		objJson.put("err_nm",this.getErr_nm());
		objJson.put("err_cnt",StringUtil.parseDouble(this.getErr_cnt(),0));
		objJson.put("base_day",this.getBase_day());
		objJson.put("user_nm",this.getUser_nm());

		return objJson;
	}		
}