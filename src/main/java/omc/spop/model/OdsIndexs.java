package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.09.22	이원식	최초작성
 **********************************************************/

@Alias("odsIndexs")
public class OdsIndexs extends Base implements Jsonable {
	
    private String owner;
    private String table_name;
    private String table_hname;
    private String index_name;
    private String index_column;
    private String uniqueness;
    private String partitioned;
    private String last_analyzed;
    private String pk_yn;
    private String usage_count;
    private String usage_yn;
    
    private String start_snap_no;
    private String end_snap_no;
    private String file_no;
    private String explain_exec_seq;
    
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
	public String getTable_hname() {
		return table_hname;
	}
	public void setTable_hname(String table_hname) {
		this.table_hname = table_hname;
	}
	public String getIndex_name() {
		return index_name;
	}
	public void setIndex_name(String index_name) {
		this.index_name = index_name;
	}
	public String getIndex_column() {
		return index_column;
	}
	public void setIndex_column(String index_column) {
		this.index_column = index_column;
	}
	public String getUniqueness() {
		return uniqueness;
	}
	public void setUniqueness(String uniqueness) {
		this.uniqueness = uniqueness;
	}
	public String getPartitioned() {
		return partitioned;
	}
	public void setPartitioned(String partitioned) {
		this.partitioned = partitioned;
	}
	public String getLast_analyzed() {
		return last_analyzed;
	}
	public void setLast_analyzed(String last_analyzed) {
		this.last_analyzed = last_analyzed;
	}	
	public String getPk_yn() {
		return pk_yn;
	}
	public void setPk_yn(String pk_yn) {
		this.pk_yn = pk_yn;
	}
	public String getUsage_count() {
		return usage_count;
	}
	public void setUsage_count(String usage_count) {
		this.usage_count = usage_count;
	}
	public String getUsage_yn() {
		return usage_yn;
	}
	public void setUsage_yn(String usage_yn) {
		this.usage_yn = usage_yn;
	}	
	public String getStart_snap_no() {
		return start_snap_no;
	}
	public void setStart_snap_no(String start_snap_no) {
		this.start_snap_no = start_snap_no;
	}
	public String getEnd_snap_no() {
		return end_snap_no;
	}
	public void setEnd_snap_no(String end_snap_no) {
		this.end_snap_no = end_snap_no;
	}	
	public String getFile_no() {
		return file_no;
	}
	public void setFile_no(String file_no) {
		this.file_no = file_no;
	}
	public String getExplain_exec_seq() {
		return explain_exec_seq;
	}
	public void setExplain_exec_seq(String explain_exec_seq) {
		this.explain_exec_seq = explain_exec_seq;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("rnum",StringUtil.parseInt(this.getRnum(),0));
		objJson.put("dbid",this.getDbid());
		objJson.put("owner",this.getOwner());
		objJson.put("table_name",this.getTable_name());
		objJson.put("table_hname",this.getTable_hname());
		objJson.put("index_name",this.getIndex_name());
		objJson.put("index_column",this.getIndex_column());
		objJson.put("uniqueness",this.getUniqueness());
		objJson.put("partitioned",this.getPartitioned());
		objJson.put("last_analyzed",this.getLast_analyzed());
		objJson.put("pk_yn",this.getPk_yn());
		objJson.put("usage_count",StringUtil.parseInt(this.getUsage_count(),0));

		return objJson;
	}		
}