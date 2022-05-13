package omc.spop.model;

import java.math.BigDecimal;
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
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("dbFileCheck")
public class DbFileCheck extends Base implements Jsonable {
    private String check_day;
    private String check_seq;
    private String inst_id;
    private String param_db_file_cnt;
    private String create_db_file_cnt;
    private BigDecimal created_percent;
    
    
	public String getCheck_day() {
		return check_day;
	}
	public void setCheck_day(String check_day) {
		this.check_day = check_day;
	}
	public String getCheck_seq() {
		return check_seq;
	}
	public void setCheck_seq(String check_seq) {
		this.check_seq = check_seq;
	}
	public String getInst_id() {
		return inst_id;
	}
	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}
	public String getParam_db_file_cnt() {
		return param_db_file_cnt;
	}
	public void setParam_db_file_cnt(String param_db_file_cnt) {
		this.param_db_file_cnt = param_db_file_cnt;
	}
	public String getCreate_db_file_cnt() {
		return create_db_file_cnt;
	}
	public void setCreate_db_file_cnt(String create_db_file_cnt) {
		this.create_db_file_cnt = create_db_file_cnt;
	}
	
	/**
	 * @return the created_percent
	 */
	public BigDecimal getCreated_percent() {
		return created_percent;
	}
	/**
	 * @param created_percent the created_percent to set
	 */
	public void setCreated_percent(BigDecimal created_percent) {
		this.created_percent = created_percent;
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
