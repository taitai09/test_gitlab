package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("segmentAdvisor")
public class SegmentAdvisor extends Base implements Jsonable {
	
    private String check_day;
    private String check_seq;
    private String task_name;
    private String task_start_dt;
    private String segment_type;
    private String owner;
    private String segment_name;
    private String partition_name;
    private String message;
    private String more_info;
    
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
	public String getTask_name() {
		return task_name;
	}
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	public String getTask_start_dt() {
		return task_start_dt;
	}
	public void setTask_start_dt(String task_start_dt) {
		this.task_start_dt = task_start_dt;
	}
	public String getSegment_type() {
		return segment_type;
	}
	public void setSegment_type(String segment_type) {
		this.segment_type = segment_type;
	}
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
	public String getPartition_name() {
		return partition_name;
	}
	public void setPartition_name(String partition_name) {
		this.partition_name = partition_name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMore_info() {
		return more_info;
	}
	public void setMore_info(String more_info) {
		this.more_info = more_info;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("task_name",this.getTask_name());
		objJson.put("task_start_dt",this.getTask_start_dt());
		objJson.put("segment_type",this.getSegment_type());
		objJson.put("owner",this.getOwner());
		objJson.put("segment_name",this.getSegment_name());
		objJson.put("partition_name",this.getPartition_name());
		objJson.put("message",this.getMessage());
		objJson.put("more_info",this.getMore_info());

		return objJson; 
	}		
}
