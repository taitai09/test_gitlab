package omc.spop.model;

import java.util.ArrayList;
import java.util.List;
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
 * 2017.10.17 이원식 최초작성
 **********************************************************/

@Alias("odsHistParameter")
public class OdsHistParameter extends Base implements Jsonable {
	
	private String id;
	private String p_id;
	private String snap_id;
	
	private String inst_id;
	private String instance_number;
	private String parameter_hash;
	private String begin_interval_time;
	private String parameter_name;
	private String value;
	private String isdefault;
	private String ismodified;
	private String modify_time;

	private List<OdsHistParameter> children = new ArrayList<OdsHistParameter>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getP_id() {
		return p_id;
	}

	public void setP_id(String p_id) {
		this.p_id = p_id;
	}

	public String getSnap_id() {
		return snap_id;
	}

	public void setSnap_id(String snap_id) {
		this.snap_id = snap_id;
	}

	public String getInst_id() {
		return inst_id;
	}

	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}

	public String getInstance_number() {
		return instance_number;
	}

	public void setInstance_number(String instance_number) {
		this.instance_number = instance_number;
	}

	public String getParameter_hash() {
		return parameter_hash;
	}

	public void setParameter_hash(String parameter_hash) {
		this.parameter_hash = parameter_hash;
	}

	public String getBegin_interval_time() {
		return begin_interval_time;
	}

	public void setBegin_interval_time(String begin_interval_time) {
		this.begin_interval_time = begin_interval_time;
	}

	public String getParameter_name() {
		return parameter_name;
	}

	public void setParameter_name(String parameter_name) {
		this.parameter_name = parameter_name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}

	public String getIsmodified() {
		return ismodified;
	}

	public void setIsmodified(String ismodified) {
		this.ismodified = ismodified;
	}

	public String getModify_time() {
		return modify_time;
	}

	public void setModify_time(String modify_time) {
		this.modify_time = modify_time;
	}

	public List<OdsHistParameter> getChildren() {
		return children;
	}

	public void setChildren(List<OdsHistParameter> children) {
		this.children = children;
	}

	// @SuppressWarnings("unchecked")
	// public JSONObject toJSONObject() {
	// JSONObject objJson = new JSONObject();
	//
	// objJson.put("id", this.getId());
	// objJson.put("p_id", this.getP_id());
	// objJson.put("snap_id", this.getSnap_id());
	// objJson.put("dbid", this.getDbid());
	// objJson.put("inst_id", this.getInst_id());
	// objJson.put("instance_number", this.getInstance_number());
	// objJson.put("parameter_hash", this.getParameter_hash());
	// objJson.put("begin_interval_time", this.getBegin_interval_time());
	// objJson.put("parameter_name", this.getParameter_name());
	// objJson.put("value", this.getValue());
	// objJson.put("isdefault", this.getIsdefault());
	// objJson.put("ismodified", this.getIsmodified());
	// objJson.put("modify_time", this.getModify_time());
	//
	// return objJson;
	// }
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