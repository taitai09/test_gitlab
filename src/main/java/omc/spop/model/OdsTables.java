package omc.spop.model;

import java.util.Map;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.09.22	이원식	최초작성
 **********************************************************/

@Alias("odsTables")
public class OdsTables extends Base implements Jsonable {
	
    private String owner;
    private String table_name;
    private String ods_table_name;
    private String base_day;
    private String tablespace_name;
    private String cluster_name;
    private String iot_name;
    private String status;
    private String pct_free;
    private String pct_used;
    private String ini_trans;
    private String max_trans;
    private String initial_extent;
    private String next_extent;
    private String min_extents;
    private String max_extentx;
    private String pct_increase;
    private String freelists;
    private String freelist_groups;
    private String logging;
    private String backed_up;
    private String num_rows;
//    private int num_rows;
    private int blocks;
    private String partitioned;
    private String empty_blocks;
    private String avg_space;
    private String chain_cnt;
    private String avg_row_len;
    private String avg_space_freelist_blocks;
    private String num_freelist_blocks;
    private String degree;
    private String instances;
    private String last_analyzed;
    
    private String exec_seq;
    private int acc_cnt;
    private String table_h_name;
    private String part_key_column;
    private String subpart_key_column;
    private String partitioning_type;
    
    private String partition_name;
    private String subpartition_name;
    
    private String ndv_ratio;
    private String col_null;    
    
    private String access_path_type;
    private String access_path_value;
    private String file_no;
    
    private String comments;
    private String allocated_space_mb;
    private String used_space_mb;
    private String reclaimable_space_mb;
    private String reclaimable_space;
    private String sample_size;
    
    private String current_size_mb;
    private String one_month_ago_size_mb;
    private String three_month_ago_size_mb;
    private String six_month_ago_size_mb;
    private String one_month_size_increas_ratio;
    
    private String inserts;
    private String updates;
    private String deletes;
    private String dml_cnt;
    
    private String bytes;
    private String access_path;
    private String access_path_count;
    private String column_name;
    private String usage_cnt;
    private String partition_key_recommend_rank;
    private int pagePerCount = 20;
    private String selectivity_calc_method;

    
	public String getSelectivity_calc_method() {
		return selectivity_calc_method;
	}
	public void setSelectivity_calc_method(String selectivity_calc_method) {
		this.selectivity_calc_method = selectivity_calc_method;
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
	public String getOds_table_name() {
		return ods_table_name;
	}
	public void setOds_table_name(String ods_table_name) {
		this.ods_table_name = ods_table_name;
	}
	public String getBase_day() {
		return base_day;
	}
	public void setBase_day(String base_day) {
		this.base_day = base_day;
	}
	public String getTablespace_name() {
		return tablespace_name;
	}
	public void setTablespace_name(String tablespace_name) {
		this.tablespace_name = tablespace_name;
	}
	public String getCluster_name() {
		return cluster_name;
	}
	public void setCluster_name(String cluster_name) {
		this.cluster_name = cluster_name;
	}
	public String getIot_name() {
		return iot_name;
	}
	public void setIot_name(String iot_name) {
		this.iot_name = iot_name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPct_free() {
		return pct_free;
	}
	public void setPct_free(String pct_free) {
		this.pct_free = pct_free;
	}
	public String getPct_used() {
		return pct_used;
	}
	public void setPct_used(String pct_used) {
		this.pct_used = pct_used;
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
	public String getMax_extentx() {
		return max_extentx;
	}
	public void setMax_extentx(String max_extentx) {
		this.max_extentx = max_extentx;
	}
	public String getPct_increase() {
		return pct_increase;
	}
	public void setPct_increase(String pct_increase) {
		this.pct_increase = pct_increase;
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
	public String getLogging() {
		return logging;
	}
	public void setLogging(String logging) {
		this.logging = logging;
	}
	public String getBacked_up() {
		return backed_up;
	}
	public void setBacked_up(String backed_up) {
		this.backed_up = backed_up;
	}
	
	public String getNum_rows() {
		return num_rows;
	}
	public void setNum_rows(String num_rows) {
		this.num_rows = num_rows;
	}
	public int getBlocks() {
		return blocks;
	}
	public void setBlocks(int blocks) {
		this.blocks = blocks;
	}
	public String getPartitioned() {
		return partitioned;
	}
	public void setPartitioned(String partitioned) {
		this.partitioned = partitioned;
	}
	public String getEmpty_blocks() {
		return empty_blocks;
	}
	public void setEmpty_blocks(String empty_blocks) {
		this.empty_blocks = empty_blocks;
	}
	public String getAvg_space() {
		return avg_space;
	}
	public void setAvg_space(String avg_space) {
		this.avg_space = avg_space;
	}
	public String getChain_cnt() {
		return chain_cnt;
	}
	public void setChain_cnt(String chain_cnt) {
		this.chain_cnt = chain_cnt;
	}
	public String getAvg_row_len() {
		return avg_row_len;
	}
	public void setAvg_row_len(String avg_row_len) {
		this.avg_row_len = avg_row_len;
	}
	public String getAvg_space_freelist_blocks() {
		return avg_space_freelist_blocks;
	}
	public void setAvg_space_freelist_blocks(String avg_space_freelist_blocks) {
		this.avg_space_freelist_blocks = avg_space_freelist_blocks;
	}
	public String getNum_freelist_blocks() {
		return num_freelist_blocks;
	}
	public void setNum_freelist_blocks(String num_freelist_blocks) {
		this.num_freelist_blocks = num_freelist_blocks;
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
	public String getExec_seq() {
		return exec_seq;
	}
	public void setExec_seq(String exec_seq) {
		this.exec_seq = exec_seq;
	}
	public int getAcc_cnt() {
		return acc_cnt;
	}
	public void setAcc_cnt(int acc_cnt) {
		this.acc_cnt = acc_cnt;
	}
	public String getTable_h_name() {
		return table_h_name;
	}
	public void setTable_h_name(String table_h_name) {
		this.table_h_name = table_h_name;
	}
	public String getLast_analyzed() {
		return last_analyzed;
	}
	public void setLast_analyzed(String last_analyzed) {
		this.last_analyzed = last_analyzed;
	}
	public String getPart_key_column() {
		return part_key_column;
	}
	public void setPart_key_column(String part_key_column) {
		this.part_key_column = part_key_column;
	}
	public String getSubpart_key_column() {
		return subpart_key_column;
	}
	public void setSubpart_key_column(String subpart_key_column) {
		this.subpart_key_column = subpart_key_column;
	}	
	public String getPartitioning_type() {
		return partitioning_type;
	}
	public void setPartitioning_type(String partitioning_type) {
		this.partitioning_type = partitioning_type;
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
	public String getNdv_ratio() {
		return ndv_ratio;
	}
	public void setNdv_ratio(String ndv_ratio) {
		this.ndv_ratio = ndv_ratio;
	}
	public String getCol_null() {
		return col_null;
	}
	public void setCol_null(String col_null) {
		this.col_null = col_null;
	}	
	public String getAccess_path_type() {
		return access_path_type;
	}
	public void setAccess_path_type(String access_path_type) {
		this.access_path_type = access_path_type;
	}
	public String getAccess_path_value() {
		return access_path_value;
	}
	public void setAccess_path_value(String access_path_value) {
		this.access_path_value = access_path_value;
	}	
	public String getFile_no() {
		return file_no;
	}
	public void setFile_no(String file_no) {
		this.file_no = file_no;
	}	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getAllocated_space_mb() {
		return allocated_space_mb;
	}
	public void setAllocated_space_mb(String allocated_space_mb) {
		this.allocated_space_mb = allocated_space_mb;
	}
	public String getUsed_space_mb() {
		return used_space_mb;
	}
	public void setUsed_space_mb(String used_space_mb) {
		this.used_space_mb = used_space_mb;
	}
	public String getReclaimable_space_mb() {
		return reclaimable_space_mb;
	}
	public void setReclaimable_space_mb(String reclaimable_space_mb) {
		this.reclaimable_space_mb = reclaimable_space_mb;
	}
	public String getReclaimable_space() {
		return reclaimable_space;
	}
	public void setReclaimable_space(String reclaimable_space) {
		this.reclaimable_space = reclaimable_space;
	}
	public String getSample_size() {
		return sample_size;
	}
	public void setSample_size(String sample_size) {
		this.sample_size = sample_size;
	}	
	public String getCurrent_size_mb() {
		return current_size_mb;
	}
	public void setCurrent_size_mb(String current_size_mb) {
		this.current_size_mb = current_size_mb;
	}
	public String getOne_month_ago_size_mb() {
		return one_month_ago_size_mb;
	}
	public void setOne_month_ago_size_mb(String one_month_ago_size_mb) {
		this.one_month_ago_size_mb = one_month_ago_size_mb;
	}
	public String getThree_month_ago_size_mb() {
		return three_month_ago_size_mb;
	}
	public void setThree_month_ago_size_mb(String three_month_ago_size_mb) {
		this.three_month_ago_size_mb = three_month_ago_size_mb;
	}
	public String getSix_month_ago_size_mb() {
		return six_month_ago_size_mb;
	}
	public void setSix_month_ago_size_mb(String six_month_ago_size_mb) {
		this.six_month_ago_size_mb = six_month_ago_size_mb;
	}
	public String getOne_month_size_increas_ratio() {
		return one_month_size_increas_ratio;
	}
	public void setOne_month_size_increas_ratio(String one_month_size_increas_ratio) {
		this.one_month_size_increas_ratio = one_month_size_increas_ratio;
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
	public String getDml_cnt() {
		return dml_cnt;
	}
	public void setDml_cnt(String dml_cnt) {
		this.dml_cnt = dml_cnt;
	}	
	public String getBytes() {
		return bytes;
	}
	public void setBytes(String bytes) {
		this.bytes = bytes;
	}
	public String getAccess_path() {
		return access_path;
	}
	public void setAccess_path(String access_path) {
		this.access_path = access_path;
	}
	public String getAccess_path_count() {
		return access_path_count;
	}
	public void setAccess_path_count(String access_path_count) {
		this.access_path_count = access_path_count;
	}
	public String getColumn_name() {
		return column_name;
	}
	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}
	public String getUsage_cnt() {
		return usage_cnt;
	}
	public void setUsage_cnt(String usage_cnt) {
		this.usage_cnt = usage_cnt;
	}
	public String getPartition_key_recommend_rank() {
		return partition_key_recommend_rank;
	}
	public void setPartition_key_recommend_rank(String partition_key_recommend_rank) {
		this.partition_key_recommend_rank = partition_key_recommend_rank;
	}
	
//	@SuppressWarnings("unchecked")
//	public JSONObject toJSONObject() {
//		JSONObject objJson = new JSONObject();
//		
//		objJson.put("dbid", this.getDbid());
//		objJson.put("db_name", this.getDb_name());
//		objJson.put("owner", this.getOwner());
//		objJson.put("table_name", this.getTable_name()); 
//		objJson.put("base_day", this.getBase_day());
//		objJson.put("tablespace_name", this.getTablespace_name());
//		objJson.put("cluster_name", this.getCluster_name());
//		objJson.put("iot_name", this.getIot_name());
//		objJson.put("status", this.getStatus());
//		objJson.put("pct_free", this.getPct_free());
//		objJson.put("pct_used", this.getPct_used());
//		objJson.put("ini_trans", this.getIni_trans());
//		objJson.put("max_trans", this.getMax_trans());
//		objJson.put("initial_extent", this.getInitial_extent());
//		objJson.put("next_extent", this.getNext_extent());
//		objJson.put("min_extents", this.getMin_extents());
//		objJson.put("max_extentx", this.getMax_extentx());
//		objJson.put("pct_increase", this.getPct_increase());
//		objJson.put("freelists", this.getFreelists());
//		objJson.put("freelist_groups", this.getFreelist_groups());
//		objJson.put("logging", this.getLogging());
//		objJson.put("backed_up", this.getBacked_up());
//		objJson.put("num_rows", StringUtil.parseInt(this.getNum_rows(),0));
//		objJson.put("blocks", StringUtil.parseInt(this.getBlocks(),0));
//		objJson.put("partitioned", this.getPartitioned());
//		objJson.put("empty_blocks", this.getEmpty_blocks());
//		objJson.put("avg_space", this.getAvg_space());
//		objJson.put("chain_cnt", this.getChain_cnt());
//		objJson.put("avg_row_len", this.getAvg_row_len());
//		objJson.put("avg_space_freelist_blocks", this.getAvg_space_freelist_blocks());
//		objJson.put("num_freelist_blocks", this.getNum_freelist_blocks());
//		objJson.put("degree", this.getDegree());
//		objJson.put("instances", this.getInstances());
//		objJson.put("last_analyzed", this.getLast_analyzed());
//		objJson.put("acc_cnt", StringUtil.parseInt(this.getAcc_cnt(),0));
//		objJson.put("table_h_name", this.getTable_h_name());
//		objJson.put("part_key_column", this.getPart_key_column());
//		objJson.put("subpart_key_column", this.getSubpart_key_column());
//		objJson.put("partitioning_type", this.getPartitioning_type());
//		objJson.put("partition_name", this.getPartition_name());
//		objJson.put("subpartition_name", this.getSubpartition_name());
//		objJson.put("comments", this.getComments());
//		objJson.put("allocated_space_mb", StringUtil.parseDouble(this.getAllocated_space_mb(),0));
//		objJson.put("used_space_mb", StringUtil.parseDouble(this.getUsed_space_mb(),0));
//		objJson.put("reclaimable_space_mb", StringUtil.parseDouble(this.getReclaimable_space_mb(),0));
//		objJson.put("reclaimable_space", StringUtil.parseDouble(this.getReclaimable_space(),0));
//		objJson.put("sample_size", StringUtil.parseDouble(this.getSample_size(),0));
//		objJson.put("current_size_gb", StringUtil.parseFloat(this.getCurrent_size_gb(),0));
//		objJson.put("one_month_ago_size_gb", StringUtil.parseDouble(this.getOne_month_ago_size_gb(),0));
//		objJson.put("tree_month_ago_size_gb", StringUtil.parseDouble(this.getThree_month_ago_size_gb(),0));
//		objJson.put("six_month_ago_size_gb", StringUtil.parseDouble(this.getSix_month_ago_size_gb(),0));
//		objJson.put("one_month_size_increas_ratio", StringUtil.parseDouble(this.getOne_month_size_increas_ratio(),0));
//		
//		objJson.put("inserts", StringUtil.parseDouble(this.getInserts(),0));
//		objJson.put("updates", StringUtil.parseDouble(this.getUpdates(),0));
//		objJson.put("deletes", StringUtil.parseDouble(this.getDeletes(),0));
//		objJson.put("dml_cnt", StringUtil.parseDouble(this.getDml_cnt(),0));
//		
//		objJson.put("rnum", StringUtil.parseDouble(this.getRnum(),0));
//		objJson.put("bytes", StringUtil.parseDouble(this.getBytes(),0));
//		objJson.put("access_path", this.getAccess_path());
//		objJson.put("access_path_count", StringUtil.parseDouble(this.getAccess_path_count(),0));
//		objJson.put("column_name", this.getColumn_name());
//		objJson.put("usage_cnt", StringUtil.parseDouble(this.getUsage_cnt(),0));
//		objJson.put("partition_key_recommend_rank", StringUtil.parseDouble(this.getPartition_key_recommend_rank(),0));
//		
//		return objJson;
//	}
	
	public int getPagePerCount() {
		return pagePerCount;
	}
	public void setPagePerCount(int pagePerCount) {
		this.pagePerCount = pagePerCount;
	}
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();

		// object -> Map
		ObjectMapper oMapper = new ObjectMapper();
		Map<String, Object> map = oMapper.convertValue(this, Map.class);
		Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
		String strJson = gson.toJson(map);
		try {
			objJson = (JSONObject) new JSONParser().parse(strJson);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return objJson;
	}
	
}