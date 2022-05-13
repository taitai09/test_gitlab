package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2018.05.17	이원식	최초작성
 **********************************************************/

@Alias("dbIndexHistory")
public class DbIndexHistory extends Base implements Jsonable {
	
	private String owner;
	private String index_name;	
	private String base_day;
	private String index_type;
	private String index_column;	
	private String table_owner;
	private String table_name;
	private String table_type;
	private String uniqueness;
	private String compression;
	private String prefix_length;
	private String tablespace_name;
	private String ini_trans;
	private String max_trans;
	private String initial_extent;
	private String next_extent;
	private String min_extents;
	private String max_extents;
	private String pct_increase;
	private String pct_threshold;
	private String include_column;
	private String freelists;
	private String freelist_groups;
	private String pct_free;
	private String logging;
	private String blevel;
	private String leaf_blocks;
	private String distinct_key;
	private String avg_leaf_blocks_per_key;
	private String avg_data_blocks_per_key;
	private String clustering_factor;
	private String status;
	private String num_rows;
	private String sample_size;
	private String last_analyzed;
	private String degree;
	private String instances;
	private String partitioned;
	private String temporary;
	private String generated;
	private String secondary;
	private String buffer_pool;
	private String flash_cache;
	private String cell_flash_cache;
	private String user_stats;
	private String duration;
	private String pct_direct_access;
	private String ityp_owner;
	private String ityp_name;
	private String parameters;
	private String global_stats;
	private String domidx_status;
	private String domidx_opstatus;
	private String funcidx_status;
	private String join_index;
	private String iot_redundant_pkey_elim;
	private String dropped;
	private String visibility;
	private String domidx_management;
	private String segment_created;
	private String object_change_type_cd;
	
	private String change_type;
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getIndex_name() {
		return index_name;
	}
	public void setIndex_name(String index_name) {
		this.index_name = index_name;
	}
	public String getBase_day() {
		return base_day;
	}
	public void setBase_day(String base_day) {
		this.base_day = base_day;
	}
	public String getIndex_type() {
		return index_type;
	}
	public void setIndex_type(String index_type) {
		this.index_type = index_type;
	}
	public String getIndex_column() {
		return index_column;
	}
	public void setIndex_column(String index_column) {
		this.index_column = index_column;
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
	public String getTable_type() {
		return table_type;
	}
	public void setTable_type(String table_type) {
		this.table_type = table_type;
	}
	public String getUniqueness() {
		return uniqueness;
	}
	public void setUniqueness(String uniqueness) {
		this.uniqueness = uniqueness;
	}
	public String getCompression() {
		return compression;
	}
	public void setCompression(String compression) {
		this.compression = compression;
	}
	public String getPrefix_length() {
		return prefix_length;
	}
	public void setPrefix_length(String prefix_length) {
		this.prefix_length = prefix_length;
	}
	public String getTablespace_name() {
		return tablespace_name;
	}
	public void setTablespace_name(String tablespace_name) {
		this.tablespace_name = tablespace_name;
	}
	public String getIni_trans() {
		return ini_trans;
	}
	public void setIni_trans(String ini_trans) {
		this.ini_trans = ini_trans;
	}
	public String getMax_trans() {
		return max_trans;
	}
	public void setMax_trans(String max_trans) {
		this.max_trans = max_trans;
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
	public String getPct_increase() {
		return pct_increase;
	}
	public void setPct_increase(String pct_increase) {
		this.pct_increase = pct_increase;
	}
	public String getPct_threshold() {
		return pct_threshold;
	}
	public void setPct_threshold(String pct_threshold) {
		this.pct_threshold = pct_threshold;
	}
	public String getInclude_column() {
		return include_column;
	}
	public void setInclude_column(String include_column) {
		this.include_column = include_column;
	}
	public String getFreelists() {
		return freelists;
	}
	public void setFreelists(String freelists) {
		this.freelists = freelists;
	}
	public String getFreelist_groups() {
		return freelist_groups;
	}
	public void setFreelist_groups(String freelist_groups) {
		this.freelist_groups = freelist_groups;
	}
	public String getPct_free() {
		return pct_free;
	}
	public void setPct_free(String pct_free) {
		this.pct_free = pct_free;
	}
	public String getLogging() {
		return logging;
	}
	public void setLogging(String logging) {
		this.logging = logging;
	}
	public String getBlevel() {
		return blevel;
	}
	public void setBlevel(String blevel) {
		this.blevel = blevel;
	}
	public String getLeaf_blocks() {
		return leaf_blocks;
	}
	public void setLeaf_blocks(String leaf_blocks) {
		this.leaf_blocks = leaf_blocks;
	}
	public String getDistinct_key() {
		return distinct_key;
	}
	public void setDistinct_key(String distinct_key) {
		this.distinct_key = distinct_key;
	}
	public String getAvg_leaf_blocks_per_key() {
		return avg_leaf_blocks_per_key;
	}
	public void setAvg_leaf_blocks_per_key(String avg_leaf_blocks_per_key) {
		this.avg_leaf_blocks_per_key = avg_leaf_blocks_per_key;
	}
	public String getAvg_data_blocks_per_key() {
		return avg_data_blocks_per_key;
	}
	public void setAvg_data_blocks_per_key(String avg_data_blocks_per_key) {
		this.avg_data_blocks_per_key = avg_data_blocks_per_key;
	}
	public String getClustering_factor() {
		return clustering_factor;
	}
	public void setClustering_factor(String clustering_factor) {
		this.clustering_factor = clustering_factor;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNum_rows() {
		return num_rows;
	}
	public void setNum_rows(String num_rows) {
		this.num_rows = num_rows;
	}
	public String getSample_size() {
		return sample_size;
	}
	public void setSample_size(String sample_size) {
		this.sample_size = sample_size;
	}
	public String getLast_analyzed() {
		return last_analyzed;
	}
	public void setLast_analyzed(String last_analyzed) {
		this.last_analyzed = last_analyzed;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getInstances() {
		return instances;
	}
	public void setInstances(String instances) {
		this.instances = instances;
	}
	public String getPartitioned() {
		return partitioned;
	}
	public void setPartitioned(String partitioned) {
		this.partitioned = partitioned;
	}
	public String getTemporary() {
		return temporary;
	}
	public void setTemporary(String temporary) {
		this.temporary = temporary;
	}
	public String getGenerated() {
		return generated;
	}
	public void setGenerated(String generated) {
		this.generated = generated;
	}
	public String getSecondary() {
		return secondary;
	}
	public void setSecondary(String secondary) {
		this.secondary = secondary;
	}
	public String getBuffer_pool() {
		return buffer_pool;
	}
	public void setBuffer_pool(String buffer_pool) {
		this.buffer_pool = buffer_pool;
	}
	public String getFlash_cache() {
		return flash_cache;
	}
	public void setFlash_cache(String flash_cache) {
		this.flash_cache = flash_cache;
	}
	public String getCell_flash_cache() {
		return cell_flash_cache;
	}
	public void setCell_flash_cache(String cell_flash_cache) {
		this.cell_flash_cache = cell_flash_cache;
	}
	public String getUser_stats() {
		return user_stats;
	}
	public void setUser_stats(String user_stats) {
		this.user_stats = user_stats;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getPct_direct_access() {
		return pct_direct_access;
	}
	public void setPct_direct_access(String pct_direct_access) {
		this.pct_direct_access = pct_direct_access;
	}
	public String getItyp_owner() {
		return ityp_owner;
	}
	public void setItyp_owner(String ityp_owner) {
		this.ityp_owner = ityp_owner;
	}
	public String getItyp_name() {
		return ityp_name;
	}
	public void setItyp_name(String ityp_name) {
		this.ityp_name = ityp_name;
	}
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	public String getGlobal_stats() {
		return global_stats;
	}
	public void setGlobal_stats(String global_stats) {
		this.global_stats = global_stats;
	}
	public String getDomidx_status() {
		return domidx_status;
	}
	public void setDomidx_status(String domidx_status) {
		this.domidx_status = domidx_status;
	}
	public String getDomidx_opstatus() {
		return domidx_opstatus;
	}
	public void setDomidx_opstatus(String domidx_opstatus) {
		this.domidx_opstatus = domidx_opstatus;
	}
	public String getFuncidx_status() {
		return funcidx_status;
	}
	public void setFuncidx_status(String funcidx_status) {
		this.funcidx_status = funcidx_status;
	}
	public String getJoin_index() {
		return join_index;
	}
	public void setJoin_index(String join_index) {
		this.join_index = join_index;
	}
	public String getIot_redundant_pkey_elim() {
		return iot_redundant_pkey_elim;
	}
	public void setIot_redundant_pkey_elim(String iot_redundant_pkey_elim) {
		this.iot_redundant_pkey_elim = iot_redundant_pkey_elim;
	}
	public String getDropped() {
		return dropped;
	}
	public void setDropped(String dropped) {
		this.dropped = dropped;
	}
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	public String getDomidx_management() {
		return domidx_management;
	}
	public void setDomidx_management(String domidx_management) {
		this.domidx_management = domidx_management;
	}
	public String getSegment_created() {
		return segment_created;
	}
	public void setSegment_created(String segment_created) {
		this.segment_created = segment_created;
	}
	public String getObject_change_type_cd() {
		return object_change_type_cd;
	}
	public void setObject_change_type_cd(String object_change_type_cd) {
		this.object_change_type_cd = object_change_type_cd;
	}	
	public String getChange_type() {
		return change_type;
	}
	public void setChange_type(String change_type) {
		this.change_type = change_type;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("dbid", this.getDbid());
		objJson.put("owner", this.getOwner());
		objJson.put("index_name", this.getIndex_name());	
		objJson.put("base_day", this.getBase_day());
		objJson.put("index_type", this.getIndex_type());
		objJson.put("index_column", this.getIndex_column());	
		objJson.put("table_owner", this.getTable_owner());
		objJson.put("table_name", this.getTable_name());
		objJson.put("table_type", this.getTable_type());
		objJson.put("uniqueness", this.getUniqueness());
		objJson.put("compression", this.getCompression());
		objJson.put("prefix_length", this.getPrefix_length());
		objJson.put("tablespace_name", this.getTablespace_name());
		objJson.put("ini_trans", this.getIni_trans());
		objJson.put("max_trans", this.getMax_trans());
		objJson.put("initial_extent", this.getInitial_extent());
		objJson.put("next_extent", this.getNext_extent());
		objJson.put("min_extents", this.getMin_extents());
		objJson.put("max_extents", this.getMax_extents());
		objJson.put("pct_increase", this.getPct_increase());
		objJson.put("pct_threshold", this.getPct_threshold());
		objJson.put("include_column", this.getInclude_column());
		objJson.put("freelists", this.getFreelists());
		objJson.put("freelist_groups", this.getFreelist_groups());
		objJson.put("pct_free", this.getPct_free());
		objJson.put("logging", this.getLogging());
		objJson.put("blevel", this.getBlevel());
		objJson.put("leaf_blocks", this.getLeaf_blocks());
		objJson.put("distinct_key", this.getDistinct_key());
		objJson.put("avg_leaf_blocks_per_key", this.getAvg_leaf_blocks_per_key());
		objJson.put("avg_data_blocks_per_key", this.getAvg_data_blocks_per_key());
		objJson.put("clustering_factor", this.getClustering_factor());
		objJson.put("status", this.getStatus());
		objJson.put("num_rows", this.getNum_rows());
		objJson.put("sample_size", this.getSample_size());
		objJson.put("last_analyzed", this.getLast_analyzed());
		objJson.put("degree", this.getDegree());
		objJson.put("instances", this.getInstances());
		objJson.put("partitioned", this.getPartitioned());
		objJson.put("temporary", this.getTemporary());
		objJson.put("generated", this.getGenerated());
		objJson.put("secondary", this.getSecondary());
		objJson.put("buffer_pool", this.getBuffer_pool());
		objJson.put("flash_cache", this.getFlash_cache());
		objJson.put("cell_flash_cache", this.getCell_flash_cache());
		objJson.put("user_stats", this.getUser_stats());
		objJson.put("duration", this.getDuration());
		objJson.put("pct_direct_access", this.getPct_direct_access());
		objJson.put("ityp_owner", this.getItyp_owner());
		objJson.put("ityp_name", this.getItyp_name());
		objJson.put("parameters", this.getParameters());
		objJson.put("global_stats", this.getGlobal_stats());
		objJson.put("domidx_status", this.getDomidx_status());
		objJson.put("domidx_opstatus", this.getDomidx_opstatus());
		objJson.put("funcidx_status", this.getFuncidx_status());
		objJson.put("join_index", this.getJoin_index());
		objJson.put("iot_redundant_pkey_elim", this.getIot_redundant_pkey_elim());
		objJson.put("dropped", this.getDropped());
		objJson.put("visibility", this.getVisibility());
		objJson.put("domidx_management", this.getDomidx_management());
		objJson.put("segment_created", this.getSegment_created());
		objJson.put("object_change_type_cd", this.getObject_change_type_cd());
		objJson.put("change_type", this.getChange_type());

		return objJson;
	}
}