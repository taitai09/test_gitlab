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

import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2020.08.10 명성태 최초작성
 **********************************************************/
@Alias("sqlStandardOperationPlugInResponse")
public class SqlStandardOperationPlugInResponse implements Jsonable {
	private String sql_std_qty_chkt_id;
	private String sql_std_qty_program_seq;
	private String program_nm;
	private String dir_nm;
	private String file_nm;
	private String err_msg;
	private String dir_err_cnt;
	private String program_err_cnt;
	private String qty_chk_idt_nm;
	private String file_nm_table;
	private String program_nm_table;
	private String dir_nm_table;
	private String qty_chk_idt_cd;
	private String abs_dir_nm;
	
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

	public String getSql_std_qty_chkt_id() {
		return sql_std_qty_chkt_id;
	}

	public void setSql_std_qty_chkt_id(String sql_std_qty_chkt_id) {
		this.sql_std_qty_chkt_id = sql_std_qty_chkt_id;
	}

	public String getSql_std_qty_program_seq() {
		return sql_std_qty_program_seq;
	}

	public void setSql_std_qty_program_seq(String sql_std_qty_program_seq) {
		this.sql_std_qty_program_seq = sql_std_qty_program_seq;
	}

	public String getProgram_nm() {
		return program_nm;
	}

	public void setProgram_nm(String program_nm) {
		this.program_nm = program_nm;
	}

	public String getDir_nm() {
		return dir_nm;
	}

	public void setDir_nm(String dir_nm) {
		this.dir_nm = dir_nm;
	}

	public String getFile_nm() {
		return file_nm;
	}

	public void setFile_nm(String file_nm) {
		this.file_nm = file_nm;
	}

	public String getErr_msg() {
		return err_msg;
	}

	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}

	public String getDir_err_cnt() {
		return dir_err_cnt;
	}

	public void setDir_err_cnt(String dir_err_cnt) {
		this.dir_err_cnt = dir_err_cnt;
	}

	public String getProgram_err_cnt() {
		return program_err_cnt;
	}

	public void setProgram_err_cnt(String program_err_cnt) {
		this.program_err_cnt = program_err_cnt;
	}

	public String getQty_chk_idt_nm() {
		return qty_chk_idt_nm;
	}

	public void setQty_chk_idt_nm(String qty_chk_idt_nm) {
		this.qty_chk_idt_nm = qty_chk_idt_nm;
	}

	public String getFile_nm_table() {
		return file_nm_table;
	}

	public void setFile_nm_table(String file_nm_table) {
		this.file_nm_table = file_nm_table;
	}

	public String getProgram_nm_table() {
		return program_nm_table;
	}

	public void setProgram_nm_table(String program_nm_table) {
		this.program_nm_table = program_nm_table;
	}

	public String getDir_nm_table() {
		return dir_nm_table;
	}

	public void setDir_nm_table(String dir_nm_table) {
		this.dir_nm_table = dir_nm_table;
	}

	public String getQty_chk_idt_cd() {
		return qty_chk_idt_cd;
	}

	public void setQty_chk_idt_cd(String qty_chk_idt_cd) {
		this.qty_chk_idt_cd = qty_chk_idt_cd;
	}

	public String getAbs_dir_nm() {
		return abs_dir_nm;
	}

	public void setAbs_dir_nm(String abs_dir_nm) {
		this.abs_dir_nm = abs_dir_nm;
	}

}
