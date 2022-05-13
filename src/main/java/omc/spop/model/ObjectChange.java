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
 * 2018.05.16 이원식 최초작성 [dashboard 오브젝트변경현황 사용]
 **********************************************************/

@Alias("objectChange")
public class ObjectChange extends Base implements Jsonable {

	private String type;
	private String table_create;
	private String table_drop;
	private String table_modify;
	private String column_create;
	private String column_add;
	private String column_drop;
	private String column_modify;
	private String index_create;
	private String index_drop;
	private String index_modify;
	private String base_day;

	/* 테이블 변경 */
	private String change_type;
	private String owner;
	private String table_name;
	private String tablespace_name;
	private String comments;

	/* 컬럼 변경 */
	private String column_name;
	private String data_type;
	private String data_length;
	private String data_precision;
	private String data_scale;
	private String nullable;
	private String column_id;
	private String before_column_info;

	/* 인덱스 변경 */
	private String index_name;
	private String index_type;
	private String uniqueness;
	private String columns;
	private String base_day_gubun;
	private String object_change_type;
	private String before_columns;

	private int yesterday;
	private int recent_one_week;
	private int recent_one_month;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTable_create() {
		return table_create;
	}

	public void setTable_create(String table_create) {
		this.table_create = table_create;
	}

	public String getTable_drop() {
		return table_drop;
	}

	public void setTable_drop(String table_drop) {
		this.table_drop = table_drop;
	}

	public String getTable_modify() {
		return table_modify;
	}

	public void setTable_modify(String table_modify) {
		this.table_modify = table_modify;
	}

	public String getColumn_create() {
		return column_create;
	}

	public void setColumn_create(String column_create) {
		this.column_create = column_create;
	}

	public String getColumn_add() {
		return column_add;
	}

	public void setColumn_add(String column_add) {
		this.column_add = column_add;
	}

	public String getColumn_drop() {
		return column_drop;
	}

	public void setColumn_drop(String column_drop) {
		this.column_drop = column_drop;
	}

	public String getColumn_modify() {
		return column_modify;
	}

	public void setColumn_modify(String column_modify) {
		this.column_modify = column_modify;
	}

	public String getIndex_create() {
		return index_create;
	}

	public void setIndex_create(String index_create) {
		this.index_create = index_create;
	}

	public String getIndex_drop() {
		return index_drop;
	}

	public void setIndex_drop(String index_drop) {
		this.index_drop = index_drop;
	}

	public String getIndex_modify() {
		return index_modify;
	}

	public void setIndex_modify(String index_modify) {
		this.index_modify = index_modify;
	}

	public String getBase_day() {
		return base_day;
	}

	public void setBase_day(String base_day) {
		this.base_day = base_day;
	}

	public String getChange_type() {
		return change_type;
	}

	public void setChange_type(String change_type) {
		this.change_type = change_type;
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

	public String getTablespace_name() {
		return tablespace_name;
	}

	public void setTablespace_name(String tablespace_name) {
		this.tablespace_name = tablespace_name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	// @SuppressWarnings("unchecked")
	// public JSONObject toJSONObject() {
	// JSONObject objJson = new JSONObject();
	//
	// objJson.put("dbid",this.getDbid());
	// objJson.put("db_name",this.getDb_name());
	// objJson.put("table_create",StringUtil.parseDouble(this.getTable_create(),0));
	// objJson.put("table_drop",StringUtil.parseDouble(this.getTable_drop(),0));
	// objJson.put("table_modify",StringUtil.parseDouble(this.getTable_modify(),0));
	// objJson.put("column_add",StringUtil.parseDouble(this.getColumn_add(),0));
	// objJson.put("column_drop",StringUtil.parseDouble(this.getColumn_drop(),0));
	// objJson.put("column_modify",StringUtil.parseDouble(this.getColumn_modify(),0));
	// objJson.put("index_create",StringUtil.parseDouble(this.getIndex_create(),0));
	// objJson.put("index_drop",StringUtil.parseDouble(this.getIndex_drop(),0));
	// objJson.put("index_modify",StringUtil.parseDouble(this.getIndex_modify(),0));
	//
	// return objJson;
	// }

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public String getData_length() {
		return data_length;
	}

	public void setData_length(String data_length) {
		this.data_length = data_length;
	}

	public String getData_precision() {
		return data_precision;
	}

	public void setData_precision(String data_precision) {
		this.data_precision = data_precision;
	}

	public String getData_scale() {
		return data_scale;
	}

	public void setData_scale(String data_scale) {
		this.data_scale = data_scale;
	}

	public String getNullable() {
		return nullable;
	}

	public void setNullable(String nullable) {
		this.nullable = nullable;
	}

	public String getColumn_id() {
		return column_id;
	}

	public void setColumn_id(String column_id) {
		this.column_id = column_id;
	}

	public String getBefore_column_info() {
		return before_column_info;
	}

	public void setBefore_column_info(String before_column_info) {
		this.before_column_info = before_column_info;
	}

	public String getIndex_name() {
		return index_name;
	}

	public void setIndex_name(String index_name) {
		this.index_name = index_name;
	}

	public String getIndex_type() {
		return index_type;
	}

	public void setIndex_type(String index_type) {
		this.index_type = index_type;
	}

	public String getUniqueness() {
		return uniqueness;
	}

	public void setUniqueness(String uniqueness) {
		this.uniqueness = uniqueness;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public String getBase_day_gubun() {
		return base_day_gubun;
	}

	public void setBase_day_gubun(String base_day_gubun) {
		this.base_day_gubun = base_day_gubun;
	}

	public String getObject_change_type() {
		return object_change_type;
	}

	public void setObject_change_type(String object_change_type) {
		this.object_change_type = object_change_type;
	}

	public String getBefore_columns() {
		return before_columns;
	}

	public void setBefore_columns(String before_columns) {
		this.before_columns = before_columns;
	}

	public int getYesterday() {
		return yesterday;
	}

	public void setYesterday(int yesterday) {
		this.yesterday = yesterday;
	}

	public int getRecent_one_week() {
		return recent_one_week;
	}

	public void setRecent_one_week(int recent_one_week) {
		this.recent_one_week = recent_one_week;
	}

	public int getRecent_one_month() {
		return recent_one_month;
	}

	public void setRecent_one_month(int recent_one_month) {
		this.recent_one_month = recent_one_month;
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
