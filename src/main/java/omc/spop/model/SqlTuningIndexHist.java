package omc.spop.model;

import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
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
 * 2019.04.30 홍길동 최초작성
 **********************************************************/
@Alias("sqlTuningIndexHist")
public class SqlTuningIndexHist extends Base implements Jsonable {

	private String owner;
	private String table_name;
	private String table_hname;
	private String index_name;
	private String index_column;
	private String uniqueness;
	private String partitioned;
	private String last_analyzed;
	private String pk_yn;
	private String usage_count;
	private String usage_yn;

	private String start_snap_no;
	private String end_snap_no;
	private String file_no;
	private String explain_exec_seq;
	
	private String tuning_no;
	private String update_dt;
	private String index_tuning_seq;
	private String index_impr_type_cd;
	private String index_column_name;
	private String before_index_column_name;
	
	public String getTuning_no() {
		return tuning_no;
	}

	public void setTuning_no(String tuning_no) {
		this.tuning_no = tuning_no;
	}

	public String getUpdate_dt() {
		return update_dt;
	}

	public void setUpdate_dt(String update_dt) {
		this.update_dt = update_dt;
	}

	public String getIndex_tuning_seq() {
		return index_tuning_seq;
	}

	public void setIndex_tuning_seq(String index_tuning_seq) {
		this.index_tuning_seq = index_tuning_seq;
	}

	public String getIndex_impr_type_cd() {
		return index_impr_type_cd;
	}

	public void setIndex_impr_type_cd(String index_impr_type_cd) {
		this.index_impr_type_cd = index_impr_type_cd;
	}

	public String getIndex_column_name() {
		return index_column_name;
	}

	public void setIndex_column_name(String index_column_name) {
		this.index_column_name = index_column_name;
	}

	public String getBefore_index_column_name() {
		return before_index_column_name;
	}

	public void setBefore_index_column_name(String before_index_column_name) {
		this.before_index_column_name = before_index_column_name;
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

	public String getTable_hname() {
		return table_hname;
	}

	public void setTable_hname(String table_hname) {
		this.table_hname = table_hname;
	}

	public String getIndex_name() {
		return index_name;
	}

	public void setIndex_name(String index_name) {
		this.index_name = index_name;
	}

	public String getIndex_column() {
		return index_column;
	}

	public void setIndex_column(String index_column) {
		this.index_column = index_column;
	}

	public String getUniqueness() {
		return uniqueness;
	}

	public void setUniqueness(String uniqueness) {
		this.uniqueness = uniqueness;
	}

	public String getPartitioned() {
		return partitioned;
	}

	public void setPartitioned(String partitioned) {
		this.partitioned = partitioned;
	}

	public String getLast_analyzed() {
		return last_analyzed;
	}

	public void setLast_analyzed(String last_analyzed) {
		this.last_analyzed = last_analyzed;
	}

	public String getPk_yn() {
		return pk_yn;
	}

	public void setPk_yn(String pk_yn) {
		this.pk_yn = pk_yn;
	}

	public String getUsage_count() {
		return usage_count;
	}

	public void setUsage_count(String usage_count) {
		this.usage_count = usage_count;
	}

	public String getUsage_yn() {
		return usage_yn;
	}

	public void setUsage_yn(String usage_yn) {
		this.usage_yn = usage_yn;
	}

	public String getStart_snap_no() {
		return start_snap_no;
	}

	public void setStart_snap_no(String start_snap_no) {
		this.start_snap_no = start_snap_no;
	}

	public String getEnd_snap_no() {
		return end_snap_no;
	}

	public void setEnd_snap_no(String end_snap_no) {
		this.end_snap_no = end_snap_no;
	}

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

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}