package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.12.11	이원식	최초작성
 **********************************************************/

@Alias("agentFailover")
public class AgentFailover extends Base implements Jsonable {
    private String agent_type_cd;
    private String agent_type_nm;
    private String first_inst_id;
    private String secondary_inst_id;
    
	public String getAgent_type_cd() {
		return agent_type_cd;
	}
	public void setAgent_type_cd(String agent_type_cd) {
		this.agent_type_cd = agent_type_cd;
	}
	public String getAgent_type_nm() {
		return agent_type_nm;
	}
	public void setAgent_type_nm(String agent_type_nm) {
		this.agent_type_nm = agent_type_nm;
	}
	public String getFirst_inst_id() {
		return first_inst_id;
	}
	public void setFirst_inst_id(String first_inst_id) {
		this.first_inst_id = first_inst_id;
	}
	public String getSecondary_inst_id() {
		return secondary_inst_id;
	}
	public void setSecondary_inst_id(String secondary_inst_id) {
		this.secondary_inst_id = secondary_inst_id;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("dbid",StringUtil.parseDouble(this.getDbid(),0));
		objJson.put("db_name",this.getDb_name());
		objJson.put("agent_type_cd",this.getAgent_type_cd());
		objJson.put("agent_type_nm",this.getAgent_type_nm());
		objJson.put("first_inst_id",this.getFirst_inst_id());
		objJson.put("secondary_inst_id",this.getSecondary_inst_id());

		return objJson;
	}		
}
