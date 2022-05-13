package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.05.02	이원식	최초작성
 **********************************************************/

@Alias("odsTablespaceUsageMetrics")
public class OdsTablespaceUsageMetrics extends Base implements Jsonable {
	
	private String owner;
	private String tablespace_name;
	private String base_day;
	private String used_space;
	private String tablespace_size;
	private String used_percent;
	private String db_block_size;	
	private String total_size_mb;	
	private String used_space_mb;
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getTablespace_name() {
		return tablespace_name;
	}
	public void setTablespace_name(String tablespace_name) {
		this.tablespace_name = tablespace_name;
	}
	public String getBase_day() {
		return base_day;
	}
	public void setBase_day(String base_day) {
		this.base_day = base_day;
	}
	public String getUsed_space() {
		return used_space;
	}
	public void setUsed_space(String used_space) {
		this.used_space = used_space;
	}
	public String getTablespace_size() {
		return tablespace_size;
	}
	public void setTablespace_size(String tablespace_size) {
		this.tablespace_size = tablespace_size;
	}
	public String getUsed_percent() {
		return used_percent;
	}
	public void setUsed_percent(String used_percent) {
		this.used_percent = used_percent;
	}
	public String getDb_block_size() {
		return db_block_size;
	}
	public void setDb_block_size(String db_block_size) {
		this.db_block_size = db_block_size;
	}
	public String getTotal_size_mb() {
		return total_size_mb;
	}
	public void setTotal_size_mb(String total_size_mb) {
		this.total_size_mb = total_size_mb;
	}
	public String getUsed_space_mb() {
		return used_space_mb;
	}
	public void setUsed_space_mb(String used_space_mb) {
		this.used_space_mb = used_space_mb;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("dbid", this.getDbid());
		objJson.put("owner", this.getOwner());
		objJson.put("tablespace_name", this.getTablespace_name());
		objJson.put("base_day", this.getBase_day());
		objJson.put("used_space", this.getUsed_space());
		objJson.put("tablespace_size", StringUtil.parseDouble(this.getTablespace_size(),0));
		objJson.put("used_percent", StringUtil.parseDouble(this.getUsed_percent(),0));
		objJson.put("db_block_size", StringUtil.parseDouble(this.getDb_block_size(),0));
		objJson.put("total_size_mb", StringUtil.parseDouble(this.getTotal_size_mb(),0));
		objJson.put("used_space_mb", StringUtil.parseDouble(this.getUsed_space_mb(),0));

		return objJson;
	}		
}