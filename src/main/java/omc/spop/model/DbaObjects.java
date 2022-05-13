package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.01.16	이원식	최초작성
 **********************************************************/

@Alias("dbaObjects")
public class DbaObjects extends Base implements Jsonable {
	
	private String owner;
	private String table_name;
	private String savtime;
	private String rowcnt;
	private String blkcnt;
	private String avgrln;
	private String samplesize;
	private String analyzetime;
	
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
	public String getSavtime() {
		return savtime;
	}
	public void setSavtime(String savtime) {
		this.savtime = savtime;
	}
	public String getRowcnt() {
		return rowcnt;
	}
	public void setRowcnt(String rowcnt) {
		this.rowcnt = rowcnt;
	}
	public String getBlkcnt() {
		return blkcnt;
	}
	public void setBlkcnt(String blkcnt) {
		this.blkcnt = blkcnt;
	}
	public String getAvgrln() {
		return avgrln;
	}
	public void setAvgrln(String avgrln) {
		this.avgrln = avgrln;
	}
	public String getSamplesize() {
		return samplesize;
	}
	public void setSamplesize(String samplesize) {
		this.samplesize = samplesize;
	}
	public String getAnalyzetime() {
		return analyzetime;
	}
	public void setAnalyzetime(String analyzetime) {
		this.analyzetime = analyzetime;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("dbid", this.getDbid());
		objJson.put("owner", this.getOwner());
		objJson.put("table_name", this.getTable_name());		
		objJson.put("savtime", this.getSavtime());
		objJson.put("rowcnt", StringUtil.parseDouble(this.getRowcnt(),0));
		objJson.put("blkcnt", StringUtil.parseDouble(this.getBlkcnt(),0));
		objJson.put("avgrln", StringUtil.parseDouble(this.getAvgrln(),0));
		objJson.put("samplesize", StringUtil.parseDouble(this.getSamplesize(),0));
		objJson.put("analyzetime", this.getAnalyzetime());

		return objJson;
	}
}