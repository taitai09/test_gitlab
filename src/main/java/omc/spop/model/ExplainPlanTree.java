package omc.spop.model;

import java.util.ArrayList;
import java.util.List;
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
@Alias("explainPlanTree")
public class ExplainPlanTree extends Base implements Jsonable {
	private String project_id;
	private String sql_auto_perf_check_id;
	private String sql_id;
	private String plan_hash_value;
	private String id;
	private String operation;
	private String options;
	private String object_owner;
	private String object_name;
	private String object_type;
	private String optimizer;
	private String parent_id;
	private String cost;
	private String bytes;
	private String imid;
	private String text;
	private List<ExplainPlanTree> children = new ArrayList<ExplainPlanTree>();

	public String getProject_id() {
		return project_id;
	}

	public String getSql_auto_perf_check_id() {
		return sql_auto_perf_check_id;
	}

	public String getSql_id() {
		return sql_id;
	}

	public String getId() {
		return id;
	}

	public String getOperation() {
		return operation;
	}

	public String getOptions() {
		return options;
	}

	public String getObject_owner() {
		return object_owner;
	}

	public String getObject_name() {
		return object_name;
	}

	public String getObject_type() {
		return object_type;
	}

	public String getOptimizer() {
		return optimizer;
	}

	public String getParent_id() {
		return parent_id;
	}

	public String getCost() {
		return cost;
	}

	public String getBytes() {
		return bytes;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public void setSql_auto_perf_check_id(String sql_auto_perf_check_id) {
		this.sql_auto_perf_check_id = sql_auto_perf_check_id;
	}

	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public void setObject_owner(String object_owner) {
		this.object_owner = object_owner;
	}

	public void setObject_name(String object_name) {
		this.object_name = object_name;
	}

	public void setObject_type(String object_type) {
		this.object_type = object_type;
	}

	public void setOptimizer(String optimizer) {
		this.optimizer = optimizer;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public void setBytes(String bytes) {
		this.bytes = bytes;
	}

	public String getImid() {
		return imid;
	}

	public String getText() {
		return text;
	}

	public void setImid(String imid) {
		this.imid = imid;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<ExplainPlanTree> getChildren() {
		return children;
	}

	public void setChildren(List<ExplainPlanTree> children) {
		this.children = children;
	}

	public String getPlan_hash_value() {
		return plan_hash_value;
	}

	public void setPlan_hash_value(String plan_hash_value) {
		this.plan_hash_value = plan_hash_value;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
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
