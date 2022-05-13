package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.09.22	이원식	최초작성
 **********************************************************/

@Alias("odsTabColumns")
public class OdsTabColumns extends Base implements Jsonable {
	
	private String owner;
	private String table_name;
	private String column_id;
    private String column_name;
    private String comments;
    private String datatype;
    private String notnull;
    private String num_distinct;
    private String num_nulls;
    
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
	public String getColumn_id() {
		return column_id;
	}
	public void setColumn_id(String column_id) {
		this.column_id = column_id;
	}
	public String getColumn_name() {
		return column_name;
	}
	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getNotnull() {
		return notnull;
	}
	public void setNotnull(String notnull) {
		this.notnull = notnull;
	}
	public String getNum_distinct() {
		return num_distinct;
	}
	public void setNum_distinct(String num_distinct) {
		this.num_distinct = num_distinct;
	}
	public String getNum_nulls() {
		return num_nulls;
	}
	public void setNum_nulls(String num_nulls) {
		this.num_nulls = num_nulls;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("owner", this.getOwner());
		objJson.put("table_name", this.getTable_name());
		objJson.put("column_id", StringUtil.parseInt(this.getColumn_id(),0));
		objJson.put("column_name", this.getColumn_name());
		objJson.put("comments", this.getComments()); 
		objJson.put("datatype", this.getDatatype());
		objJson.put("notnull", this.getNotnull());
		objJson.put("num_distinct", StringUtil.parseInt(this.getNum_distinct(),0));
		objJson.put("num_nulls", StringUtil.parseInt(this.getNum_nulls(),0));

		return objJson;
	}		
}