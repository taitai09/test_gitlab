package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("chainedRowCheck")
public class ChainedRowCheck extends Base implements Jsonable {
    private String check_day;
    private String check_seq;
    private String owner;
    private String table_name;
    private String tablespace_name;
    private String num_rows;
    private String chain_cnt;
    private String chain_percent;
    
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
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	public String getTablespace_name() {
		return tablespace_name;
	}
	public void setTablespace_name(String tablespace_name) {
		this.tablespace_name = tablespace_name;
	}
	public String getNum_rows() {
		return num_rows;
	}
	public void setNum_rows(String num_rows) {
		this.num_rows = num_rows;
	}
	public String getChain_cnt() {
		return chain_cnt;
	}
	public void setChain_cnt(String chain_cnt) {
		this.chain_cnt = chain_cnt;
	}
	public String getChain_percent() {
		return chain_percent;
	}
	public void setChain_percent(String chain_percent) {
		this.chain_percent = chain_percent;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("owner",this.getOwner());
		objJson.put("table_name",this.getTable_name());
		objJson.put("tablespace_name",this.getTablespace_name());
		objJson.put("num_rows",StringUtil.parseDouble(this.getNum_rows(),0));
		objJson.put("chain_cnt",StringUtil.parseDouble(this.getChain_cnt(),0));
		objJson.put("chain_percent",StringUtil.parseDouble(this.getChain_percent(),0));

		return objJson; 
	}		
}
