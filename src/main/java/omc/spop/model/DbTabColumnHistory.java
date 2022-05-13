package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2018.05.17	이원식	최초작성
 **********************************************************/

@Alias("dbTabColumnHistory")
public class DbTabColumnHistory extends Base implements Jsonable {
	
	private String owner;
	private String table_name;
    private String column_name;
    private String base_day;
    private String data_type;
    private String data_type_mod;
    private String data_type_owner;
    private String data_length;
    private String data_precision;
    private String data_scale;
    private String nullable;
    private String column_id;
    private String default_length;
    private String data_default;
    private String num_distinct;
    private String low_value;
    private String high_value;
    private String density;
    private String num_nulls;
    private String num_buckets;
    private String last_analyzed;
    private String sample_size;
    private String character_set_name;
    private String char_col_decl_length;
    private String global_stats;
    private String user_stats;
    private String avg_col_len;
    private String char_length;
    private String char_used;
    private String v80_fmt_image;
    private String data_upgraded;
    private String histogram;
    private String object_change_type_cd;
    
    private String change_type;
    private String change_day;
    
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
	public String getColumn_name() {
		return column_name;
	}
	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}
	public String getBase_day() {
		return base_day;
	}
	public void setBase_day(String base_day) {
		this.base_day = base_day;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public String getData_type_mod() {
		return data_type_mod;
	}
	public void setData_type_mod(String data_type_mod) {
		this.data_type_mod = data_type_mod;
	}
	public String getData_type_owner() {
		return data_type_owner;
	}
	public void setData_type_owner(String data_type_owner) {
		this.data_type_owner = data_type_owner;
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
	public String getDefault_length() {
		return default_length;
	}
	public void setDefault_length(String default_length) {
		this.default_length = default_length;
	}
	public String getData_default() {
		return data_default;
	}
	public void setData_default(String data_default) {
		this.data_default = data_default;
	}
	public String getNum_distinct() {
		return num_distinct;
	}
	public void setNum_distinct(String num_distinct) {
		this.num_distinct = num_distinct;
	}
	public String getLow_value() {
		return low_value;
	}
	public void setLow_value(String low_value) {
		this.low_value = low_value;
	}
	public String getHigh_value() {
		return high_value;
	}
	public void setHigh_value(String high_value) {
		this.high_value = high_value;
	}
	public String getDensity() {
		return density;
	}
	public void setDensity(String density) {
		this.density = density;
	}
	public String getNum_nulls() {
		return num_nulls;
	}
	public void setNum_nulls(String num_nulls) {
		this.num_nulls = num_nulls;
	}
	public String getNum_buckets() {
		return num_buckets;
	}
	public void setNum_buckets(String num_buckets) {
		this.num_buckets = num_buckets;
	}
	public String getLast_analyzed() {
		return last_analyzed;
	}
	public void setLast_analyzed(String last_analyzed) {
		this.last_analyzed = last_analyzed;
	}
	public String getSample_size() {
		return sample_size;
	}
	public void setSample_size(String sample_size) {
		this.sample_size = sample_size;
	}
	public String getCharacter_set_name() {
		return character_set_name;
	}
	public void setCharacter_set_name(String character_set_name) {
		this.character_set_name = character_set_name;
	}
	public String getChar_col_decl_length() {
		return char_col_decl_length;
	}
	public void setChar_col_decl_length(String char_col_decl_length) {
		this.char_col_decl_length = char_col_decl_length;
	}
	public String getGlobal_stats() {
		return global_stats;
	}
	public void setGlobal_stats(String global_stats) {
		this.global_stats = global_stats;
	}
	public String getUser_stats() {
		return user_stats;
	}
	public void setUser_stats(String user_stats) {
		this.user_stats = user_stats;
	}
	public String getAvg_col_len() {
		return avg_col_len;
	}
	public void setAvg_col_len(String avg_col_len) {
		this.avg_col_len = avg_col_len;
	}
	public String getChar_length() {
		return char_length;
	}
	public void setChar_length(String char_length) {
		this.char_length = char_length;
	}
	public String getChar_used() {
		return char_used;
	}
	public void setChar_used(String char_used) {
		this.char_used = char_used;
	}
	public String getV80_fmt_image() {
		return v80_fmt_image;
	}
	public void setV80_fmt_image(String v80_fmt_image) {
		this.v80_fmt_image = v80_fmt_image;
	}
	public String getData_upgraded() {
		return data_upgraded;
	}
	public void setData_upgraded(String data_upgraded) {
		this.data_upgraded = data_upgraded;
	}
	public String getHistogram() {
		return histogram;
	}
	public void setHistogram(String histogram) {
		this.histogram = histogram;
	}
	public String getObject_change_type_cd() {
		return object_change_type_cd;
	}
	public void setObject_change_type_cd(String object_change_type_cd) {
		this.object_change_type_cd = object_change_type_cd;
	}	
	public String getChange_type() {
		return change_type;
	}
	public void setChange_type(String change_type) {
		this.change_type = change_type;
	}
	
	public String getChange_day() {
		return change_day;
	}
	public void setChange_day(String change_day) {
		this.change_day = change_day;
	}
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("dbid", this.getDbid());
		objJson.put("owner", this.getOwner());
		objJson.put("table_name", this.getTable_name());
		objJson.put("column_name", this.getColumn_name());
		objJson.put("base_day", this.getBase_day());
		objJson.put("data_type", this.getData_type());
		objJson.put("data_type_mod", this.getData_type_mod());
		objJson.put("data_type_owner", this.getData_type_owner());
		objJson.put("data_length", this.getData_length());
		objJson.put("data_precision", this.getData_precision());
		objJson.put("data_scale", this.getData_scale());
		objJson.put("nullable", this.getNullable());
		objJson.put("column_id", this.getColumn_id());
		objJson.put("default_length", this.getDefault_length());
		objJson.put("data_default", this.getData_default());
		objJson.put("num_distinct", this.getNum_distinct());
		objJson.put("low_value", this.getLow_value());
		objJson.put("high_value", this.getHigh_value());
		objJson.put("density", this.getDensity());
		objJson.put("num_nulls", this.getNum_nulls());
		objJson.put("num_buckets", this.getNum_buckets());
		objJson.put("last_analyzed", this.getLast_analyzed());
		objJson.put("sample_size", this.getSample_size());
		objJson.put("character_set_name", this.getCharacter_set_name());
		objJson.put("char_col_decl_length", this.getChar_col_decl_length());
		objJson.put("global_stats", this.getGlobal_stats());
		objJson.put("user_stats", this.getUser_stats());
		objJson.put("avg_col_len", this.getAvg_col_len());
		objJson.put("char_length", this.getChar_length());
		objJson.put("char_used", this.getChar_used());
		objJson.put("v80_fmt_image", this.getV80_fmt_image());
		objJson.put("data_upgraded", this.getData_upgraded());
		objJson.put("histogram", this.getHistogram());
		objJson.put("object_change_type_cd", this.getObject_change_type_cd());
		objJson.put("change_type", this.getChange_type());
		objJson.put("change_day", this.getChange_day());

		return objJson;
	}		
}