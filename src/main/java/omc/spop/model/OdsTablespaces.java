package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.05.02	이원식	최초작성
 **********************************************************/

@Alias("odsTablespaces")
public class OdsTablespaces extends Base implements Jsonable {
	
	private String owner;
	private String tablespace_name;	
	private String base_day;
	private String total_size_mb;
	private String used_space_mb;
	private String used_percent;
	private String block_size;
	private String initial_extent;
	private String next_extent;
	private String min_extents;
	private String max_extents;
	private String max_size;
	private String pct_increase;
	private String min_extlen;
	private String status;
	private String contents;
	private String logging;
	private String force_logging;
	private String extent_management;
	private String allocation_type;
	private String plugged_in;
	private String segment_space_management;
	private String def_tab_compression;
	private String retention;
	private String bigfile;
	private String predicate_evaluation;
	private String encrypted;
	private String compress_for;
	private String def_inmemory;
	private String def_inmemory_priority;
	private String def_inmemory_distribute;
	private String def_inmemory_compression;
	private String def_inmemory_duplicate;
	private String shared;
	private String def_index_compression;
	private String index_compress_for;
	private String def_cellmemory;
	private String def_inmemory_service;
	private String def_inmemory_service_name;
	private String lost_write_protect;
	private String chunk_tablespace;
	
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
	public String getUsed_percent() {
		return used_percent;
	}
	public void setUsed_percent(String used_percent) {
		this.used_percent = used_percent;
	}
	public String getBlock_size() {
		return block_size;
	}
	public void setBlock_size(String block_size) {
		this.block_size = block_size;
	}
	public String getInitial_extent() {
		return initial_extent;
	}
	public void setInitial_extent(String initial_extent) {
		this.initial_extent = initial_extent;
	}
	public String getNext_extent() {
		return next_extent;
	}
	public void setNext_extent(String next_extent) {
		this.next_extent = next_extent;
	}
	public String getMin_extents() {
		return min_extents;
	}
	public void setMin_extents(String min_extents) {
		this.min_extents = min_extents;
	}
	public String getMax_extents() {
		return max_extents;
	}
	public void setMax_extents(String max_extents) {
		this.max_extents = max_extents;
	}
	public String getMax_size() {
		return max_size;
	}
	public void setMax_size(String max_size) {
		this.max_size = max_size;
	}
	public String getPct_increase() {
		return pct_increase;
	}
	public void setPct_increase(String pct_increase) {
		this.pct_increase = pct_increase;
	}
	public String getMin_extlen() {
		return min_extlen;
	}
	public void setMin_extlen(String min_extlen) {
		this.min_extlen = min_extlen;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getLogging() {
		return logging;
	}
	public void setLogging(String logging) {
		this.logging = logging;
	}
	public String getForce_logging() {
		return force_logging;
	}
	public void setForce_logging(String force_logging) {
		this.force_logging = force_logging;
	}
	public String getExtent_management() {
		return extent_management;
	}
	public void setExtent_management(String extent_management) {
		this.extent_management = extent_management;
	}
	public String getAllocation_type() {
		return allocation_type;
	}
	public void setAllocation_type(String allocation_type) {
		this.allocation_type = allocation_type;
	}
	public String getPlugged_in() {
		return plugged_in;
	}
	public void setPlugged_in(String plugged_in) {
		this.plugged_in = plugged_in;
	}
	public String getSegment_space_management() {
		return segment_space_management;
	}
	public void setSegment_space_management(String segment_space_management) {
		this.segment_space_management = segment_space_management;
	}
	public String getDef_tab_compression() {
		return def_tab_compression;
	}
	public void setDef_tab_compression(String def_tab_compression) {
		this.def_tab_compression = def_tab_compression;
	}
	public String getRetention() {
		return retention;
	}
	public void setRetention(String retention) {
		this.retention = retention;
	}
	public String getBigfile() {
		return bigfile;
	}
	public void setBigfile(String bigfile) {
		this.bigfile = bigfile;
	}
	public String getPredicate_evaluation() {
		return predicate_evaluation;
	}
	public void setPredicate_evaluation(String predicate_evaluation) {
		this.predicate_evaluation = predicate_evaluation;
	}
	public String getEncrypted() {
		return encrypted;
	}
	public void setEncrypted(String encrypted) {
		this.encrypted = encrypted;
	}
	public String getCompress_for() {
		return compress_for;
	}
	public void setCompress_for(String compress_for) {
		this.compress_for = compress_for;
	}
	public String getDef_inmemory() {
		return def_inmemory;
	}
	public void setDef_inmemory(String def_inmemory) {
		this.def_inmemory = def_inmemory;
	}
	public String getDef_inmemory_priority() {
		return def_inmemory_priority;
	}
	public void setDef_inmemory_priority(String def_inmemory_priority) {
		this.def_inmemory_priority = def_inmemory_priority;
	}
	public String getDef_inmemory_distribute() {
		return def_inmemory_distribute;
	}
	public void setDef_inmemory_distribute(String def_inmemory_distribute) {
		this.def_inmemory_distribute = def_inmemory_distribute;
	}
	public String getDef_inmemory_compression() {
		return def_inmemory_compression;
	}
	public void setDef_inmemory_compression(String def_inmemory_compression) {
		this.def_inmemory_compression = def_inmemory_compression;
	}
	public String getDef_inmemory_duplicate() {
		return def_inmemory_duplicate;
	}
	public void setDef_inmemory_duplicate(String def_inmemory_duplicate) {
		this.def_inmemory_duplicate = def_inmemory_duplicate;
	}
	public String getShared() {
		return shared;
	}
	public void setShared(String shared) {
		this.shared = shared;
	}
	public String getDef_index_compression() {
		return def_index_compression;
	}
	public void setDef_index_compression(String def_index_compression) {
		this.def_index_compression = def_index_compression;
	}
	public String getIndex_compress_for() {
		return index_compress_for;
	}
	public void setIndex_compress_for(String index_compress_for) {
		this.index_compress_for = index_compress_for;
	}
	public String getDef_cellmemory() {
		return def_cellmemory;
	}
	public void setDef_cellmemory(String def_cellmemory) {
		this.def_cellmemory = def_cellmemory;
	}
	public String getDef_inmemory_service() {
		return def_inmemory_service;
	}
	public void setDef_inmemory_service(String def_inmemory_service) {
		this.def_inmemory_service = def_inmemory_service;
	}
	public String getDef_inmemory_service_name() {
		return def_inmemory_service_name;
	}
	public void setDef_inmemory_service_name(String def_inmemory_service_name) {
		this.def_inmemory_service_name = def_inmemory_service_name;
	}
	public String getLost_write_protect() {
		return lost_write_protect;
	}
	public void setLost_write_protect(String lost_write_protect) {
		this.lost_write_protect = lost_write_protect;
	}
	public String getChunk_tablespace() {
		return chunk_tablespace;
	}
	public void setChunk_tablespace(String chunk_tablespace) {
		this.chunk_tablespace = chunk_tablespace;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("dbid", this.getDbid());
		objJson.put("owner", this.getOwner());
		objJson.put("tablespace_name", this.getTablespace_name());
		objJson.put("base_day", this.getBase_day());
		objJson.put("total_size_mb", StringUtil.parseDouble(this.getTotal_size_mb(),0));
		objJson.put("used_space_mb", StringUtil.parseDouble(this.getUsed_space_mb(),0));
		objJson.put("used_percent", StringUtil.parseDouble(this.getUsed_percent(),0));
		objJson.put("block_size", StringUtil.parseDouble(this.getBlock_size(),0));
		objJson.put("initial_extent", StringUtil.parseDouble(this.getInitial_extent(),0));
		objJson.put("next_extent", StringUtil.parseDouble(this.getNext_extent(),0));
		objJson.put("min_extents", StringUtil.parseDouble(this.getMin_extents(),0));
		objJson.put("max_extents", StringUtil.parseDouble(this.getMax_extents(),0));
		objJson.put("max_size", StringUtil.parseDouble(this.getMax_size(),0));
		objJson.put("pct_increase", StringUtil.parseDouble(this.getPct_increase(),0));
		objJson.put("min_extlen", StringUtil.parseDouble(this.getMin_extlen(),0));
		objJson.put("status", this.getStatus());
		objJson.put("contents", this.getContents());
		objJson.put("logging", this.getLogging());
		objJson.put("force_logging", this.getForce_logging());
		objJson.put("extent_management", this.getExtent_management());
		objJson.put("allocation_type", this.getAllocation_type());
		objJson.put("plugged_in", this.getPlugged_in());
		objJson.put("segment_space_management", this.getSegment_space_management());
		objJson.put("def_tab_compression", this.getDef_tab_compression());
		objJson.put("retention", this.getRetention());
		objJson.put("bigfile", this.getBigfile());
		objJson.put("predicate_evaluation", this.getPredicate_evaluation());
		objJson.put("encrypted", this.getEncrypted());
		objJson.put("compress_for", this.getCompress_for());
		objJson.put("def_inmemory", this.getDef_inmemory());
		objJson.put("def_inmemory_priority", this.getDef_inmemory_priority());
		objJson.put("def_inmemory_distribute", this.getDef_inmemory_distribute());
		objJson.put("def_inmemory_compression", this.getDef_inmemory_compression());
		objJson.put("def_inmemory_duplicate", this.getDef_inmemory_duplicate());
		objJson.put("shared", this.getShared());
		objJson.put("def_index_compression", this.getDef_index_compression());
		objJson.put("index_compress_for", this.getIndex_compress_for());
		objJson.put("def_cellmemory", this.getDef_cellmemory());
		objJson.put("def_inmemory_service", this.getDef_inmemory_service());
		objJson.put("def_inmemory_service_name", this.getDef_inmemory_service_name());
		objJson.put("lost_write_protect", this.getLost_write_protect());
		objJson.put("chunk_tablespace", this.getChunk_tablespace());

		return objJson;
	}		
}