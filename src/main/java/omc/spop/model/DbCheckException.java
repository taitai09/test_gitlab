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
 * 2018.08.30 임호경 최초작성
 **********************************************************/

@Alias("dbCheckException")
public class DbCheckException extends Base implements Jsonable {

	private String check_pref_id;
	private String check_pref_nm;
	private int check_object_seq;
	private int inst_id;
	private String owner;
	private String check_except_object_index;
	private String check_except_object;
	private String check_except_object_name;
	private String check_except_object_value;
	private String check_except_object_name_1;
	private String check_except_object_name_2;
	private String check_except_object_name_3;
	private String check_except_object_name_4;
	private String check_except_object_name_5;
	private String check_except_object_name_6;
	private String check_except_object_name_7;
	private String check_except_object_name_8;
	private String check_except_object_name_9;
	private String check_except_object_name_10;
	private String except_processor_id;
	private String except_process_dt;
	private String search_condition;

	private String db_check_exception_no;
	private String db_check_exception_no_array;
	
	DbCheckException(){
		super.setPagePerCount(20);
	}

	public String getCheck_pref_id() {
		return check_pref_id;
	}

	public void setCheck_pref_id(String check_pref_id) {
		this.check_pref_id = check_pref_id;
	}

	public int getCheck_object_seq() {
		return check_object_seq;
	}

	public void setCheck_object_seq(int check_object_seq) {
		this.check_object_seq = check_object_seq;
	}

	public int getInst_id() {
		return inst_id;
	}

	public void setInst_id(int inst_id) {
		this.inst_id = inst_id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getCheck_except_object_index() {
		return check_except_object_index;
	}

	public void setCheck_except_object_index(String check_except_object_index) {
		this.check_except_object_index = check_except_object_index;
	}

	public String getCheck_except_object_name() {
		return check_except_object_name;
	}

	public void setCheck_except_object_name(String check_except_object_name) {
		this.check_except_object_name = check_except_object_name;
	}

	public String getCheck_except_object_name_1() {
		return check_except_object_name_1;
	}

	public void setCheck_except_object_name_1(String check_except_object_name_1) {
		this.check_except_object_name_1 = check_except_object_name_1;
	}

	public String getCheck_except_object_name_2() {
		return check_except_object_name_2;
	}

	public void setCheck_except_object_name_2(String check_except_object_name_2) {
		this.check_except_object_name_2 = check_except_object_name_2;
	}

	public String getCheck_except_object_name_3() {
		return check_except_object_name_3;
	}

	public void setCheck_except_object_name_3(String check_except_object_name_3) {
		this.check_except_object_name_3 = check_except_object_name_3;
	}

	public String getExcept_processor_id() {
		return except_processor_id;
	}

	public void setExcept_processor_id(String except_processor_id) {
		this.except_processor_id = except_processor_id;
	}

	public String getExcept_process_dt() {
		return except_process_dt;
	}

	public void setExcept_process_dt(String except_process_dt) {
		this.except_process_dt = except_process_dt;
	}

	public String getCheck_pref_nm() {
		return check_pref_nm;
	}

	public void setCheck_pref_nm(String check_pref_nm) {
		this.check_pref_nm = check_pref_nm;
	}

	public String getCheck_except_object_name_4() {
		return check_except_object_name_4;
	}

	public void setCheck_except_object_name_4(String check_except_object_name_4) {
		this.check_except_object_name_4 = check_except_object_name_4;
	}

	public String getCheck_except_object_name_5() {
		return check_except_object_name_5;
	}

	public void setCheck_except_object_name_5(String check_except_object_name_5) {
		this.check_except_object_name_5 = check_except_object_name_5;
	}

	public String getCheck_except_object_name_6() {
		return check_except_object_name_6;
	}

	public void setCheck_except_object_name_6(String check_except_object_name_6) {
		this.check_except_object_name_6 = check_except_object_name_6;
	}

	public String getCheck_except_object_name_7() {
		return check_except_object_name_7;
	}

	public void setCheck_except_object_name_7(String check_except_object_name_7) {
		this.check_except_object_name_7 = check_except_object_name_7;
	}

	public String getCheck_except_object_name_8() {
		return check_except_object_name_8;
	}

	public void setCheck_except_object_name_8(String check_except_object_name_8) {
		this.check_except_object_name_8 = check_except_object_name_8;
	}

	public String getCheck_except_object_name_9() {
		return check_except_object_name_9;
	}

	public void setCheck_except_object_name_9(String check_except_object_name_9) {
		this.check_except_object_name_9 = check_except_object_name_9;
	}

	public String getCheck_except_object_name_10() {
		return check_except_object_name_10;
	}

	public void setCheck_except_object_name_10(String check_except_object_name_10) {
		this.check_except_object_name_10 = check_except_object_name_10;
	}

	public String getDb_check_exception_no() {
		return db_check_exception_no;
	}

	public void setDb_check_exception_no(String db_check_exception_no) {
		this.db_check_exception_no = db_check_exception_no;
	}

	public String getDb_check_exception_no_array() {
		return db_check_exception_no_array;
	}

	public void setDb_check_exception_no_array(String db_check_exception_no_array) {
		this.db_check_exception_no_array = db_check_exception_no_array;
	}

	public String getCheck_except_object_value() {
		return check_except_object_value;
	}

	public void setCheck_except_object_value(String check_except_object_value) {
		this.check_except_object_value = check_except_object_value;
	}

	public String getCheck_except_object() {
		return check_except_object;
	}

	public void setCheck_except_object(String check_except_object) {
		this.check_except_object = check_except_object;
	}

	public String getSearch_condition() {
		return search_condition;
	}

	public void setSearch_condition(String search_condition) {
		this.search_condition = search_condition;
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
