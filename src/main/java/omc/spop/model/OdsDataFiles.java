package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.05.02	이원식	최초작성
 **********************************************************/

@Alias("odsDataFiles")
public class OdsDataFiles extends Base implements Jsonable {
	
	private String owner;
	private String file_name;
	private String base_day;
	private String file_id;
	private String tablespace_name;
	private String bytes;
	private String blocks;
	private String status;
	private String relative_fno;
	private String autoextensible;
	private String maxbytes;
	private String maxblocks;
	private String incrementby;
	private String user_bytes;
	private String user_blocks;
	private String online_status;
	
	private String begin_interval_time;
	private String snap_dt;
	private String phyrds;
	private String phywrts;
	private String wait_count;
	private String time;
	
	private String start_time;
	private String end_time;
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getBase_day() {
		return base_day;
	}
	public void setBase_day(String base_day) {
		this.base_day = base_day;
	}
	public String getFile_id() {
		return file_id;
	}
	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}
	public String getTablespace_name() {
		return tablespace_name;
	}
	public void setTablespace_name(String tablespace_name) {
		this.tablespace_name = tablespace_name;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRelative_fno() {
		return relative_fno;
	}
	public void setRelative_fno(String relative_fno) {
		this.relative_fno = relative_fno;
	}
	public String getAutoextensible() {
		return autoextensible;
	}
	public void setAutoextensible(String autoextensible) {
		this.autoextensible = autoextensible;
	}
	public String getMaxbytes() {
		return maxbytes;
	}
	public void setMaxbytes(String maxbytes) {
		this.maxbytes = maxbytes;
	}
	public String getMaxblocks() {
		return maxblocks;
	}
	public void setMaxblocks(String maxblocks) {
		this.maxblocks = maxblocks;
	}
	public String getIncrementby() {
		return incrementby;
	}
	public void setIncrementby(String incrementby) {
		this.incrementby = incrementby;
	}
	public String getUser_bytes() {
		return user_bytes;
	}
	public void setUser_bytes(String user_bytes) {
		this.user_bytes = user_bytes;
	}
	public String getUser_blocks() {
		return user_blocks;
	}
	public void setUser_blocks(String user_blocks) {
		this.user_blocks = user_blocks;
	}
	public String getOnline_status() {
		return online_status;
	}
	public void setOnline_status(String online_status) {
		this.online_status = online_status;
	}	
	public String getBegin_interval_time() {
		return begin_interval_time;
	}
	public void setBegin_interval_time(String begin_interval_time) {
		this.begin_interval_time = begin_interval_time;
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
	public String getWait_count() {
		return wait_count;
	}
	public void setWait_count(String wait_count) {
		this.wait_count = wait_count;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}	
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("dbid", this.getDbid());            
		objJson.put("db_name", this.getDb_name());
		objJson.put("owner", this.getOwner());           
		objJson.put("file_name", this.getFile_name());    
		objJson.put("base_day", this.getBase_day());        
		objJson.put("file_id", this.getFile_id());  
		objJson.put("tablespace_name", this.getTablespace_name());    
		objJson.put("bytes", StringUtil.parseDouble(this.getBytes(),0));           
		objJson.put("blocks", StringUtil.parseDouble(this.getBlocks(),0));          
		objJson.put("status", this.getStatus());       
		objJson.put("relative_fno", StringUtil.parseDouble(this.getRelative_fno(),0));    
		objJson.put("autoextensible", this.getAutoextensible());    
		objJson.put("maxbytes", StringUtil.parseDouble(this.getMaxbytes(),0));       
		objJson.put("maxblocks", StringUtil.parseDouble(this.getMaxblocks(),0)); 
		objJson.put("incrementby", StringUtil.parseDouble(this.getIncrementby(),0));    
		objJson.put("user_bytes", StringUtil.parseDouble(this.getUser_bytes(),0));     
		objJson.put("user_blocks", StringUtil.parseDouble(this.getUser_blocks(),0));     
		objJson.put("online_status", this.getOnline_status());
		
		objJson.put("begin_interval_time", this.getBegin_interval_time());
		objJson.put("snap_dt", this.getSnap_dt());
		objJson.put("phyrds", StringUtil.parseDouble(this.getPhyrds(),0));
		objJson.put("phywrts", StringUtil.parseDouble(this.getPhywrts(),0));
		objJson.put("wait_count", StringUtil.parseDouble(this.getWait_count(),0));
		objJson.put("time", StringUtil.parseDouble(this.getTime(),0));

		return objJson;
	}		
}