package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("dbCacheAdvisor")
public class DbCacheAdvisor extends Base implements Jsonable {
    private String check_day;
    private String check_seq;
    private String inst_id;
    private String id;
    private String name;
    private String block_size;
    private String size_for_estimate;
    private String size_factor;
    private String buffers_for_estimate;
    private String estd_physical_read_factor;
    private String estd_physical_reads;
    private String estd_cluster_read_time;
    private String estd_physical_read_time;
    private String estd_pct_of_db_time_for_reads;
    private String estd_cluster_reads;
    
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
	public String getInst_id() {
		return inst_id;
	}
	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBlock_size() {
		return block_size;
	}
	public void setBlock_size(String block_size) {
		this.block_size = block_size;
	}
	public String getSize_for_estimate() {
		return size_for_estimate;
	}
	public void setSize_for_estimate(String size_for_estimate) {
		this.size_for_estimate = size_for_estimate;
	}
	public String getSize_factor() {
		return size_factor;
	}
	public void setSize_factor(String size_factor) {
		this.size_factor = size_factor;
	}
	public String getBuffers_for_estimate() {
		return buffers_for_estimate;
	}
	public void setBuffers_for_estimate(String buffers_for_estimate) {
		this.buffers_for_estimate = buffers_for_estimate;
	}
	public String getEstd_physical_read_factor() {
		return estd_physical_read_factor;
	}
	public void setEstd_physical_read_factor(String estd_physical_read_factor) {
		this.estd_physical_read_factor = estd_physical_read_factor;
	}
	public String getEstd_physical_reads() {
		return estd_physical_reads;
	}
	public void setEstd_physical_reads(String estd_physical_reads) {
		this.estd_physical_reads = estd_physical_reads;
	}
	public String getEstd_cluster_read_time() {
		return estd_cluster_read_time;
	}
	public void setEstd_cluster_read_time(String estd_cluster_read_time) {
		this.estd_cluster_read_time = estd_cluster_read_time;
	}
	public String getEstd_physical_read_time() {
		return estd_physical_read_time;
	}
	public void setEstd_physical_read_time(String estd_physical_read_time) {
		this.estd_physical_read_time = estd_physical_read_time;
	}
	public String getEstd_pct_of_db_time_for_reads() {
		return estd_pct_of_db_time_for_reads;
	}
	public void setEstd_pct_of_db_time_for_reads(String estd_pct_of_db_time_for_reads) {
		this.estd_pct_of_db_time_for_reads = estd_pct_of_db_time_for_reads;
	}
	public String getEstd_cluster_reads() {
		return estd_cluster_reads;
	}
	public void setEstd_cluster_reads(String estd_cluster_reads) {
		this.estd_cluster_reads = estd_cluster_reads;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("inst_id",this.getInst_id());
		objJson.put("id",this.getId());
		objJson.put("name",this.getName());
		objJson.put("block_size",StringUtil.parseDouble(this.getBlock_size(),0));
		objJson.put("size_for_estimate",StringUtil.parseDouble(this.getSize_for_estimate(),0));
		objJson.put("size_factor",StringUtil.parseFloat(this.getSize_factor(),0));
		objJson.put("buffers_for_estimate",StringUtil.parseDouble(this.getBuffers_for_estimate(),0));
		objJson.put("estd_physical_read_factor",StringUtil.parseFloat(this.getEstd_physical_read_factor(),0));
		objJson.put("estd_physical_reads",StringUtil.parseDouble(this.getEstd_physical_reads(),0));
		objJson.put("estd_cluster_read_time",StringUtil.parseDouble(this.getEstd_cluster_read_time(),0));
		objJson.put("estd_physical_read_time",StringUtil.parseDouble(this.getEstd_physical_read_time(),0));
		objJson.put("estd_pct_of_db_time_for_reads",StringUtil.parseFloat(this.getEstd_pct_of_db_time_for_reads(),0));
		objJson.put("estd_cluster_reads",StringUtil.parseDouble(this.getEstd_cluster_reads(),0));
		
		return objJson; 
	}		
}
