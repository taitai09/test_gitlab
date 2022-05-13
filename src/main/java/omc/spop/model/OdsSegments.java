package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.05.02	이원식	최초작성
 **********************************************************/

@Alias("odsSegments")
public class OdsSegments extends Base implements Jsonable {
	
	private String owner;
	private String segment_name;
	private String base_day;
	private String partition_name;
	private String segment_type;
	private String blocks;
	private String segment_subtype;
	private String tablespace_name;	
	private String header_file;	
	private String header_block;
	private String bytes;
	private String extents;
	private String initial_extent;
	private String next_extent;
	private String min_extents;
	private String max_extents;
	private String max_size;
	private String retention;
	private String minretention;
	private String pct_increase;
	private String freelists;
	private String freelist_groups;
	private String relative_fno;
	private String buffer_pool;
	private String flash_cache;
	private String cell_flash_cache;
	private String allocated_space_mb;
	private String used_space_mb;
	private String reclaimable_space_mb;
	private String chain_rowexcess;
	private String recommendations;
	private String c1;
	private String c2;
	private String c3;
	
	private String snap_dt;
	private String phyrds;
	private String phywrts;
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getSegment_name() {
		return segment_name;
	}
	public void setSegment_name(String segment_name) {
		this.segment_name = segment_name;
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
	public String getSegment_type() {
		return segment_type;
	}
	public void setSegment_type(String segment_type) {
		this.segment_type = segment_type;
	}
	public String getSegment_subtype() {
		return segment_subtype;
	}
	public void setSegment_subtype(String segment_subtype) {
		this.segment_subtype = segment_subtype;
	}
	public String getTablespace_name() {
		return tablespace_name;
	}
	public void setTablespace_name(String tablespace_name) {
		this.tablespace_name = tablespace_name;
	}
	public String getHeader_file() {
		return header_file;
	}
	public void setHeader_file(String header_file) {
		this.header_file = header_file;
	}
	public String getHeader_block() {
		return header_block;
	}
	public void setHeader_block(String header_block) {
		this.header_block = header_block;
	}
	public String getBytes() {
		return bytes;
	}
	public void setBytes(String bytes) {
		this.bytes = bytes;
	}
	public String getBlocks() {
		return blocks;
	}
	public void setBlocks(String blocks) {
		this.blocks = blocks;
	}
	public String getExtents() {
		return extents;
	}
	public void setExtents(String extents) {
		this.extents = extents;
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
	public String getRetention() {
		return retention;
	}
	public void setRetention(String retention) {
		this.retention = retention;
	}
	public String getMinretention() {
		return minretention;
	}
	public void setMinretention(String minretention) {
		this.minretention = minretention;
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
	public String getRelative_fno() {
		return relative_fno;
	}
	public void setRelative_fno(String relative_fno) {
		this.relative_fno = relative_fno;
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
	public String getChain_rowexcess() {
		return chain_rowexcess;
	}
	public void setChain_rowexcess(String chain_rowexcess) {
		this.chain_rowexcess = chain_rowexcess;
	}
	public String getRecommendations() {
		return recommendations;
	}
	public void setRecommendations(String recommendations) {
		this.recommendations = recommendations;
	}
	public String getC1() {
		return c1;
	}
	public void setC1(String c1) {
		this.c1 = c1;
	}
	public String getC2() {
		return c2;
	}
	public void setC2(String c2) {
		this.c2 = c2;
	}
	public String getC3() {
		return c3;
	}
	public void setC3(String c3) {
		this.c3 = c3;
	}	
	public String getSnap_dt() {
		return snap_dt;
	}
	public void setSnap_dt(String snap_dt) {
		this.snap_dt = snap_dt;
	}
	public String getPhyrds() {
		return phyrds;
	}
	public void setPhyrds(String phyrds) {
		this.phyrds = phyrds;
	}
	public String getPhywrts() {
		return phywrts;
	}
	public void setPhywrts(String phywrts) {
		this.phywrts = phywrts;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("dbid", this.getDbid());            
		objJson.put("owner", this.getOwner());           
		objJson.put("segment_name", this.getSegment_name());    
		objJson.put("base_day", this.getBase_day());        
		objJson.put("partition_name", this.getPartition_name());  
		objJson.put("segment_type", this.getSegment_type());    
		objJson.put("segment_subtype", this.getSegment_subtype()); 
		objJson.put("tablespace_name", this.getTablespace_name());	
		objJson.put("header_file", StringUtil.parseDouble(this.getHeader_file(),0));	    
		objJson.put("header_block", StringUtil.parseDouble(this.getHeader_block(),0));    
		objJson.put("bytes", StringUtil.parseDouble(this.getBytes(),0));           
		objJson.put("blocks", StringUtil.parseDouble(this.getBlocks(),0));          
		objJson.put("extents", StringUtil.parseDouble(this.getExtents(),0));         
		objJson.put("initial_extent", StringUtil.parseDouble(this.getInitial_extent(),0));  
		objJson.put("next_extent", StringUtil.parseDouble(this.getNext_extent(),0));     
		objJson.put("min_extents", StringUtil.parseDouble(this.getMin_extents(),0));     
		objJson.put("max_extents", StringUtil.parseDouble(this.getMax_extents(),0));     
		objJson.put("max_size", StringUtil.parseDouble(this.getMax_size(),0));        
		objJson.put("retention", this.getRetention());       
		objJson.put("minretention", StringUtil.parseDouble(this.getMinretention(),0));    
		objJson.put("pct_increase", StringUtil.parseDouble(this.getPct_increase(),0));    
		objJson.put("freelists", StringUtil.parseDouble(this.getFreelists(),0));       
		objJson.put("freelist_groups", StringUtil.parseDouble(this.getFreelist_groups(),0)); 
		objJson.put("relative_fno", StringUtil.parseDouble(this.getRelative_fno(),0));    
		objJson.put("buffer_pool", this.getBuffer_pool());     
		objJson.put("flash_cache", this.getFlash_cache());     
		objJson.put("cell_flash_cache", this.getCell_flash_cache());
		objJson.put("allocated_space_mb", StringUtil.parseDouble(this.getAllocated_space_mb(),0));
		objJson.put("used_space_mb", StringUtil.parseDouble(this.getUsed_space_mb(),0));
		objJson.put("reclaimable_space_mb", StringUtil.parseDouble(this.getReclaimable_space_mb(),0));
		objJson.put("chain_rowexcess", StringUtil.parseDouble(this.getChain_rowexcess(),0));
		objJson.put("recommendations", this.getRecommendations());
		objJson.put("c1", this.getC1());
		objJson.put("c2", this.getC2());
		objJson.put("c3", this.getC3());
		
		objJson.put("snap_dt", this.getSnap_dt());
		objJson.put("phyrds", StringUtil.parseDouble(this.getPhyrds(),0));
		objJson.put("phywrts", StringUtil.parseDouble(this.getPhywrts(),0));

		return objJson;
	}		
}