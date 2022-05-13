package omc.spop.model;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
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
 * 2018.08.30 이원식 최초작성
 **********************************************************/

@Alias("dbCheckExceptionHeadTitle")
public class DbCheckExceptionHeadTitle extends Base implements Jsonable {
	private String check_pref_id;
	private String check_except_head_title_cd;
	private String check_except_head_title_nm;
	private String check_except_head_title_nm_1;
	private String check_except_head_title_nm_2;
	private String check_except_head_title_nm_3;
	private String check_except_head_title_nm_4;
	private String check_except_head_title_nm_5;
	private String check_except_head_title_nm_6;
	private String check_except_head_title_nm_7;
	private String check_except_head_title_nm_8;
	private String check_except_head_title_nm_9;
	private String check_except_head_title_nm_10;
	private String check_except_key_cnt;
	
	public String getCheck_pref_id() {
		return check_pref_id;
	}

	public void setCheck_pref_id(String check_pref_id) {
		this.check_pref_id = check_pref_id;
	}

	public String getCheck_except_head_title_cd() {
		return check_except_head_title_cd;
	}

	public void setCheck_except_head_title_cd(String check_except_head_title_cd) {
		this.check_except_head_title_cd = check_except_head_title_cd;
	}

	public String getCheck_except_head_title_nm() {
		return check_except_head_title_nm;
	}

	public void setCheck_except_head_title_nm(String check_except_head_title_nm) {
		this.check_except_head_title_nm = check_except_head_title_nm;
	}

	public String getCheck_except_head_title_nm_1() {
		return check_except_head_title_nm_1;
	}

	public void setCheck_except_head_title_nm_1(String check_except_head_title_nm_1) {
		this.check_except_head_title_nm_1 = check_except_head_title_nm_1;
	}

	public String getCheck_except_head_title_nm_2() {
		return check_except_head_title_nm_2;
	}

	public void setCheck_except_head_title_nm_2(String check_except_head_title_nm_2) {
		this.check_except_head_title_nm_2 = check_except_head_title_nm_2;
	}

	public String getCheck_except_head_title_nm_3() {
		return check_except_head_title_nm_3;
	}

	public void setCheck_except_head_title_nm_3(String check_except_head_title_nm_3) {
		this.check_except_head_title_nm_3 = check_except_head_title_nm_3;
	}

	public String getCheck_except_head_title_nm_4() {
		return check_except_head_title_nm_4;
	}

	public void setCheck_except_head_title_nm_4(String check_except_head_title_nm_4) {
		this.check_except_head_title_nm_4 = check_except_head_title_nm_4;
	}

	public String getCheck_except_head_title_nm_5() {
		return check_except_head_title_nm_5;
	}

	public void setCheck_except_head_title_nm_5(String check_except_head_title_nm_5) {
		this.check_except_head_title_nm_5 = check_except_head_title_nm_5;
	}

	public String getCheck_except_head_title_nm_6() {
		return check_except_head_title_nm_6;
	}

	public void setCheck_except_head_title_nm_6(String check_except_head_title_nm_6) {
		this.check_except_head_title_nm_6 = check_except_head_title_nm_6;
	}

	public String getCheck_except_head_title_nm_7() {
		return check_except_head_title_nm_7;
	}

	public void setCheck_except_head_title_nm_7(String check_except_head_title_nm_7) {
		this.check_except_head_title_nm_7 = check_except_head_title_nm_7;
	}

	public String getCheck_except_head_title_nm_8() {
		return check_except_head_title_nm_8;
	}

	public void setCheck_except_head_title_nm_8(String check_except_head_title_nm_8) {
		this.check_except_head_title_nm_8 = check_except_head_title_nm_8;
	}

	public String getCheck_except_head_title_nm_9() {
		return check_except_head_title_nm_9;
	}

	public void setCheck_except_head_title_nm_9(String check_except_head_title_nm_9) {
		this.check_except_head_title_nm_9 = check_except_head_title_nm_9;
	}

	public String getCheck_except_head_title_nm_10() {
		return check_except_head_title_nm_10;
	}

	public void setCheck_except_head_title_nm_10(String check_except_head_title_nm_10) {
		this.check_except_head_title_nm_10 = check_except_head_title_nm_10;
	}

	public String getCheck_except_key_cnt() {
		return check_except_key_cnt;
	}

	public void setCheck_except_key_cnt(String check_except_key_cnt) {
		this.check_except_key_cnt = check_except_key_cnt;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
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
