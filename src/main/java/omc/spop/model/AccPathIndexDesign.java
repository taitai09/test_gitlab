package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.09.22	이원식	최초작성
 **********************************************************/

@Alias("accPathIndexDesign")
public class AccPathIndexDesign extends Base implements Jsonable {
    private String exec_seq;
    private String table_owner;
    private String table_name;
    private String index_seq;
    private String index_column_list;
    private String reg_dt;
    
    private String indexColumnArry;
    
	public String getExec_seq() {
		return exec_seq;
	}
	public void setExec_seq(String exec_seq) {
		this.exec_seq = exec_seq;
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
	public String getIndex_seq() {
		return index_seq;
	}
	public void setIndex_seq(String index_seq) {
		this.index_seq = index_seq;
	}
	public String getIndex_column_list() {
		return index_column_list;
	}
	public void setIndex_column_list(String index_column_list) {
		this.index_column_list = index_column_list;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}	
	public String getIndexColumnArry() {
		return indexColumnArry;
	}
	public void setIndexColumnArry(String indexColumnArry) {
		this.indexColumnArry = indexColumnArry;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();

		objJson.put("dbid",this.getDbid());
		objJson.put("exec_seq",StringUtil.parseInt(this.getExec_seq(),0));
		objJson.put("table_owner",this.getTable_owner());		
		objJson.put("table_name",this.getTable_name());
		objJson.put("index_seq",this.getIndex_seq());
		objJson.put("index_column_list",this.getIndex_column_list());
		objJson.put("reg_dt",this.getReg_dt());
		
		return objJson;
	}		
}
