package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.10.19	이원식	최초작성
 **********************************************************/

@Alias("idxAdRecommendIndex")
public class IdxAdRecommendIndex extends Base implements Jsonable {
    private String idx_ad_no;
    private String access_path_type;
    private String table_owner;
    private String table_name;
    private String seq;
    private String recommend_type;
    private String access_path_column_list;
    private String source_index_name;
    private String source_index_column_list;
    private String modify_column;
    
	public String getIdx_ad_no() {
		return idx_ad_no;
	}
	public void setIdx_ad_no(String idx_ad_no) {
		this.idx_ad_no = idx_ad_no;
	}
	public String getAccess_path_type() {
		return access_path_type;
	}
	public void setAccess_path_type(String access_path_type) {
		this.access_path_type = access_path_type;
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
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getRecommend_type() {
		return recommend_type;
	}
	public void setRecommend_type(String recommend_type) {
		this.recommend_type = recommend_type;
	}
	public String getAccess_path_column_list() {
		return access_path_column_list;
	}
	public void setAccess_path_column_list(String access_path_column_list) {
		this.access_path_column_list = access_path_column_list;
	}
	public String getSource_index_name() {
		return source_index_name;
	}
	public void setSource_index_name(String source_index_name) {
		this.source_index_name = source_index_name;
	}
	public String getSource_index_column_list() {
		return source_index_column_list;
	}
	public void setSource_index_column_list(String source_index_column_list) {
		this.source_index_column_list = source_index_column_list;
	}	
	public String getModify_column() {
		return modify_column;
	}
	public void setModify_column(String modify_column) {
		this.modify_column = modify_column;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("rnum",StringUtil.parseInt(this.getRnum(),0));
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("idx_ad_no",this.getIdx_ad_no());
		objJson.put("access_path_type",this.getAccess_path_type());
		objJson.put("table_owner",this.getTable_owner());
		objJson.put("table_name",this.getTable_name());
		objJson.put("seq",StringUtil.parseInt(this.getSeq(),0));
		objJson.put("recommend_type",this.getRecommend_type());
		objJson.put("access_path_column_list",this.getAccess_path_column_list());
		objJson.put("source_index_name",this.getSource_index_name());
		objJson.put("source_index_column_list",this.getSource_index_column_list());
		objJson.put("modify_column",this.getModify_column());

		return objJson;
	}		
}
