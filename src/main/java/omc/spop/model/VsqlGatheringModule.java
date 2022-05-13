package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.12.15	이원식	최초작성
 **********************************************************/

@Alias("vsqlGatheringModule")
public class VsqlGatheringModule extends Base implements Jsonable {
	
	private String gathering_module_no;
	private String module_name;
	private String reg_dt;
	
	public String getGathering_module_no() {
		return gathering_module_no;
	}
	public void setGathering_module_no(String gathering_module_no) {
		this.gathering_module_no = gathering_module_no;
	}
	public String getModule_name() {
		return module_name;
	}
	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("gathering_module_no",this.getGathering_module_no());
		objJson.put("module_name",this.getModule_name());
		objJson.put("reg_dt",this.getReg_dt());

		return objJson;
	}		
}