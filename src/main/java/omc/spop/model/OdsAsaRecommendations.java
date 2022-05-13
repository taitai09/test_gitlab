package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.09.22	이원식	최초작성
 **********************************************************/

@Alias("odsAsaRecommendations")
public class OdsAsaRecommendations extends Base implements Jsonable {
    private String base_day;
    private String recommend_seq;
    private String tablespace_name;
    private String segment_owner;
    private String segment_name;
    private String segment_type;
    private String partition_name;
    private String allocated_space;
    private String used_space;
    private String reclaimable_space;
    private String chain_rowexcess;
    
    private String recommendations;
    private String run_first;
    private String run_second;
    private String run_third;
    
	public String getBase_day() {
		return base_day;
	}
	public void setBase_day(String base_day) {
		this.base_day = base_day;
	}
	public String getRecommend_seq() {
		return recommend_seq;
	}
	public void setRecommend_seq(String recommend_seq) {
		this.recommend_seq = recommend_seq;
	}
	public String getTablespace_name() {
		return tablespace_name;
	}
	public void setTablespace_name(String tablespace_name) {
		this.tablespace_name = tablespace_name;
	}
	public String getSegment_owner() {
		return segment_owner;
	}
	public void setSegment_owner(String segment_owner) {
		this.segment_owner = segment_owner;
	}
	public String getSegment_name() {
		return segment_name;
	}
	public void setSegment_name(String segment_name) {
		this.segment_name = segment_name;
	}
	public String getSegment_type() {
		return segment_type;
	}
	public void setSegment_type(String segment_type) {
		this.segment_type = segment_type;
	}
	public String getPartition_name() {
		return partition_name;
	}
	public void setPartition_name(String partition_name) {
		this.partition_name = partition_name;
	}
	public String getAllocated_space() {
		return allocated_space;
	}
	public void setAllocated_space(String allocated_space) {
		this.allocated_space = allocated_space;
	}
	public String getUsed_space() {
		return used_space;
	}
	public void setUsed_space(String used_space) {
		this.used_space = used_space;
	}
	public String getReclaimable_space() {
		return reclaimable_space;
	}
	public void setReclaimable_space(String reclaimable_space) {
		this.reclaimable_space = reclaimable_space;
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
	public String getRun_first() {
		return run_first;
	}
	public void setRun_first(String run_first) {
		this.run_first = run_first;
	}
	public String getRun_second() {
		return run_second;
	}
	public void setRun_second(String run_second) {
		this.run_second = run_second;
	}
	public String getRun_third() {
		return run_third;
	}
	public void setRun_third(String run_third) {
		this.run_third = run_third;
	}
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();

		objJson.put("dbid",this.getDbid());
		objJson.put("base_day",this.getBase_day());
		objJson.put("recommend_seq",StringUtil.parseFloat(this.getRecommend_seq(),0));
		objJson.put("tablespace_name",this.getTablespace_name());
		objJson.put("segment_owner",this.getSegment_owner());
		objJson.put("segment_name",this.getSegment_name());
		objJson.put("segment_type",this.getSegment_type());
		objJson.put("partition_name",this.getPartition_name());
		objJson.put("allocated_space",StringUtil.parseFloat(this.getAllocated_space(),0));
		objJson.put("used_space",StringUtil.parseFloat(this.getUsed_space(),0));
		objJson.put("reclaimable_space",StringUtil.parseFloat(this.getReclaimable_space(),0));
		objJson.put("chain_rowexcess",StringUtil.parseFloat(this.getChain_rowexcess(),0));
		
		objJson.put("recommendations",this.getRecommendations());
		objJson.put("run_first",this.getRun_first());
		objJson.put("run_second",this.getRun_second());
		objJson.put("run_third",this.getRun_third());

		return objJson;
	}		
}