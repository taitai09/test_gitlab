package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.10.19	이원식	최초작성
 **********************************************************/

@Alias("dbioPlanTable")
public class DbioPlanTable extends Base implements Jsonable {
	
    private String file_no;
    private String explain_exec_seq;
    private String query_seq;
    private String id;
    private String pid;
    private String level;
    private String statement_id;
    private String query_output;
    
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
	public String getQuery_seq() {
		return query_seq;
	}
	public void setQuery_seq(String query_seq) {
		this.query_seq = query_seq;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getStatement_id() {
		return statement_id;
	}
	public void setStatement_id(String statement_id) {
		this.statement_id = statement_id;
	}
	public String getQuery_output() {
		return query_output;
	}
	public void setQuery_output(String query_output) {
		this.query_output = query_output;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("file_no",this.getFile_no());
		objJson.put("explain_exec_seq",this.getExplain_exec_seq());
		objJson.put("query_seq",this.getQuery_seq());
		objJson.put("id",this.getId());
		objJson.put("pid",this.getPid());
		objJson.put("level",this.getLevel());
		objJson.put("statement_id",this.getStatement_id());
		objJson.put("query_output",this.getQuery_output());
		
		return objJson;
	}		
}