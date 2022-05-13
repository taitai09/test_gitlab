package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("outstandingAlerts")
public class OutstandingAlerts extends Base implements Jsonable {
	
    private String check_day;
    private String check_seq;
    private String sequence_id;
    private String reason_id;
    private String owner;
    private String object_name;
    private String subobject_name;
    private String object_type;
    private String reason;
    private String time_suggested;
    private String advisor_name;
    private String metric_value;
    private String message_type;
    private String message_group;
    private String message_level;
    private String hosting_client_id;
    private String module_id;
    private String process_id;
    private String host_id;
    private String host_nw_addr;
    private String instance_name;
    private String instance_number;
    private String execution_context_id;
    private String error_instance_id;
    
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
	public String getSequence_id() {
		return sequence_id;
	}
	public void setSequence_id(String sequence_id) {
		this.sequence_id = sequence_id;
	}
	public String getReason_id() {
		return reason_id;
	}
	public void setReason_id(String reason_id) {
		this.reason_id = reason_id;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getObject_name() {
		return object_name;
	}
	public void setObject_name(String object_name) {
		this.object_name = object_name;
	}
	public String getSubobject_name() {
		return subobject_name;
	}
	public void setSubobject_name(String subobject_name) {
		this.subobject_name = subobject_name;
	}
	public String getObject_type() {
		return object_type;
	}
	public void setObject_type(String object_type) {
		this.object_type = object_type;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getTime_suggested() {
		return time_suggested;
	}
	public void setTime_suggested(String time_suggested) {
		this.time_suggested = time_suggested;
	}
	public String getAdvisor_name() {
		return advisor_name;
	}
	public void setAdvisor_name(String advisor_name) {
		this.advisor_name = advisor_name;
	}
	public String getMetric_value() {
		return metric_value;
	}
	public void setMetric_value(String metric_value) {
		this.metric_value = metric_value;
	}
	public String getMessage_type() {
		return message_type;
	}
	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}
	public String getMessage_group() {
		return message_group;
	}
	public void setMessage_group(String message_group) {
		this.message_group = message_group;
	}
	public String getMessage_level() {
		return message_level;
	}
	public void setMessage_level(String message_level) {
		this.message_level = message_level;
	}
	public String getHosting_client_id() {
		return hosting_client_id;
	}
	public void setHosting_client_id(String hosting_client_id) {
		this.hosting_client_id = hosting_client_id;
	}
	public String getModule_id() {
		return module_id;
	}
	public void setModule_id(String module_id) {
		this.module_id = module_id;
	}
	public String getProcess_id() {
		return process_id;
	}
	public void setProcess_id(String process_id) {
		this.process_id = process_id;
	}
	public String getHost_id() {
		return host_id;
	}
	public void setHost_id(String host_id) {
		this.host_id = host_id;
	}
	public String getHost_nw_addr() {
		return host_nw_addr;
	}
	public void setHost_nw_addr(String host_nw_addr) {
		this.host_nw_addr = host_nw_addr;
	}
	public String getInstance_name() {
		return instance_name;
	}
	public void setInstance_name(String instance_name) {
		this.instance_name = instance_name;
	}
	public String getInstance_number() {
		return instance_number;
	}
	public void setInstance_number(String instance_number) {
		this.instance_number = instance_number;
	}
	public String getExecution_context_id() {
		return execution_context_id;
	}
	public void setExecution_context_id(String execution_context_id) {
		this.execution_context_id = execution_context_id;
	}
	public String getError_instance_id() {
		return error_instance_id;
	}
	public void setError_instance_id(String error_instance_id) {
		this.error_instance_id = error_instance_id;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("sequence_id",this.getSequence_id());
		objJson.put("reason_id",this.getReason_id());
		objJson.put("owner",this.getOwner());
		objJson.put("object_name",this.getObject_name());
		objJson.put("subobject_name",this.getSubobject_name());
		objJson.put("object_type",this.getObject_type());
		objJson.put("reason",this.getReason());
		objJson.put("time_suggested",this.getTime_suggested());
		objJson.put("advisor_name",this.getAdvisor_name());
		objJson.put("metric_value",this.getMetric_value());
		objJson.put("message_type",this.getMessage_type());
		objJson.put("message_group",this.getMessage_group());
		objJson.put("message_level",this.getMessage_level());
		objJson.put("hosting_client_id",this.getHosting_client_id());
		objJson.put("module_id",this.getModule_id());
		objJson.put("process_id",this.getProcess_id());
		objJson.put("host_id",this.getHost_id());
		objJson.put("host_nw_addr",this.getHost_nw_addr());
		objJson.put("instance_name",this.getInstance_name());
		objJson.put("instance_number",this.getInstance_number());
		objJson.put("user_id",this.getUser_id());
		objJson.put("execution_context_id",this.getExecution_context_id());
		objJson.put("error_instance_id",this.getError_instance_id());

		return objJson; 
	}		
}
