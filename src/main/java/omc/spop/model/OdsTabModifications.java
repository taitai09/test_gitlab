package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.09.29	이원식	최초작성
 **********************************************************/

@Alias("odsTabModifications")
public class OdsTabModifications extends Base implements Jsonable {
	
    private String owner;
    private String table_name;
    private String base_day;
    private String partition_name;
    private String subpartition_name;
    private String inserts;
    private String updates;
    private String deletes;
    private String truncated;
    private String timestamp;
    private String drop_segments;
    private String all_change_cnt;
    private String rank;
    
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
	public String getBase_day() {
		return base_day;
	}
	public void setBase_day(String base_day) {
		this.base_day = base_day;
	}
	public String getPartition_name() {
		return partition_name;
	}
	public void setPartition_name(String partition_name) {
		this.partition_name = partition_name;
	}
	public String getSubpartition_name() {
		return subpartition_name;
	}
	public void setSubpartition_name(String subpartition_name) {
		this.subpartition_name = subpartition_name;
	}
	public String getInserts() {
		return inserts;
	}
	public void setInserts(String inserts) {
		this.inserts = inserts;
	}
	public String getUpdates() {
		return updates;
	}
	public void setUpdates(String updates) {
		this.updates = updates;
	}
	public String getDeletes() {
		return deletes;
	}
	public void setDeletes(String deletes) {
		this.deletes = deletes;
	}
	public String getTruncated() {
		return truncated;
	}
	public void setTruncated(String truncated) {
		this.truncated = truncated;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getDrop_segments() {
		return drop_segments;
	}
	public void setDrop_segments(String drop_segments) {
		this.drop_segments = drop_segments;
	}
	public String getAll_change_cnt() {
		return all_change_cnt;
	}
	public void setAll_change_cnt(String all_change_cnt) {
		this.all_change_cnt = all_change_cnt;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("dbid", this.getDbid());
		objJson.put("owner", this.getOwner());
		objJson.put("table_name", this.getTable_name()); 
		objJson.put("base_day", this.getBase_day());
		objJson.put("partition_name", this.getPartition_name());
		objJson.put("subpartition_name", this.getSubpartition_name());
		objJson.put("inserts", StringUtil.parseFloat(this.getInserts(),0));
		objJson.put("updates", StringUtil.parseFloat(this.getUpdates(),0));
		objJson.put("deletes", StringUtil.parseFloat(this.getDeletes(),0));
		objJson.put("truncated", this.getTruncated());
		objJson.put("timestamp", this.getTimestamp());
		objJson.put("drop_segments", this.getDrop_segments());
		objJson.put("all_change_cnt", StringUtil.parseFloat(this.getAll_change_cnt(),0));
		objJson.put("rank", StringUtil.parseInt(this.getRank(),0));

		return objJson;
	}		
}