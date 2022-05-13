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
 * 2018.04.19 이원식 최초작성
 **********************************************************/

@Alias("tablespaceCheck")
public class TablespaceCheck extends Base implements Jsonable {

	private String check_day;
	private String check_seq;
	private String tablespace_name;
	private BigDecimal space_used;
	private BigDecimal tablespace_size;
	private BigDecimal space_used_percent;
	private BigDecimal threshold_percent;

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

	public String getTablespace_name() {
		return tablespace_name;
	}

	public void setTablespace_name(String tablespace_name) {
		this.tablespace_name = tablespace_name;
	}

	public BigDecimal getSpace_used() {
		return space_used;
	}

	public void setSpace_used(BigDecimal space_used) {
		this.space_used = space_used;
	}

	public BigDecimal getTablespace_size() {
		return tablespace_size;
	}

	public void setTablespace_size(BigDecimal tablespace_size) {
		this.tablespace_size = tablespace_size;
	}

	public BigDecimal getSpace_used_percent() {
		return space_used_percent;
	}

	public void setSpace_used_percent(BigDecimal space_used_percent) {
		this.space_used_percent = space_used_percent;
	}

	public BigDecimal getThreshold_percent() {
		return threshold_percent;
	}

	public void setThreshold_percent(BigDecimal threshold_percent) {
		this.threshold_percent = threshold_percent;
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
