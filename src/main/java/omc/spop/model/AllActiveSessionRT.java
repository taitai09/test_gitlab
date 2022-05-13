package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.01.02	유동균	최초작성
 **********************************************************/

@Alias("AllActiveSessionRT")
public class AllActiveSessionRT extends Base implements Jsonable {
	
	private String inst_id;
	private String sample_id;
	private String sample_time;
	private String is_awr_sample;
	private String sid;
	private String serial;
	private String session_id;
	private String session_type;
	private String flags;
	private String sql_id;
	private String is_sqlid_current;
	private String sql_child_number;
	private String sql_opcode;
	private String force_matching_signature;
	private String top_level_sql_id;
	private String top_level_sql_opcode;
	private String sql_opname;
	private String sql_plan_hash_value;
	private String sql_plan_line_id;
	private String sql_plan_operation;
	private String sql_plan_options;
	private String sql_exec_id;
	private String sql_exec_start;
	private String plsql_entry_object_id;
	private String plsql_entry_subprogram_id;
	private String plsql_object_id;
	private String plsql_subprogram_id;
	private String qc_instance_id;
	private String qc_session_id;
	private String qc_session_serial;
	private String event;
	private String event_id;
	private String event_sharp;
	private String seq_sharp;
	private String p1text;
	private String p1;
	private String p2text;
	private String p2;
	private String p3text;
	private String p3;
	private String wait_class;
	private String wait_class_id;
	private String wait_time;
	private String session_state;
	private String time_waited;
	private String blocking_session_status;
	private String blocking_session;
	private String blocking_session_serial;
	private String blocking_inst_id;
	private String blocking_hangchain_info;
	private String current_obj;
	private String current_file;
	private String current_block;
	private String current_row;
	private String top_level_call;
	private String top_level_call_name;
	private String consumer_group_id;
	private String xid;
	private String remote_instance;
	private String time_model;
	private String in_connection_mgmt;
	private String in_parse;
	private String in_hard_parse;
	private String in_sql_execution;
	private String in_plsql_execution;
	private String in_plsql_rpc;
	private String in_plsql_compilation;
	private String in_java_execution;
	private String in_bind;
	private String in_cursor_close;
	private String in_sequence_load;
	private String capture_overhead;
	private String replay_overhead;
	private String is_captured;
	private String is_replayed;
	private String service_hash;
	private String program;
	private String module;
	private String action;
	private String client_id;
	private String machine;
	private String port;
	private String ecid;
	private String tm_delta_time;
	private String tm_delta_cpu_time;
	private String tm_delta_db_time;
	private String delta_time;
	private String delta_read_io_requests;
	private String delta_write_io_requests;
	private String delta_read_io_bytes;
	private String delta_write_io_bytes;
	private String delta_interconnect_io_bytes;
	private String pga_allocated;
	private String temp_space_allocated;

	
	public String getInst_id() {
		return inst_id;
	}
	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}
	public String getSample_id() {
		return sample_id;
	}
	public void setSample_id(String sample_id) {
		this.sample_id = sample_id;
	}
	public String getSample_time() {
		return sample_time;
	}
	public void setSample_time(String sample_time) {
		this.sample_time = sample_time;
	}
	public String getIs_awr_sample() {
		return is_awr_sample;
	}
	public void setIs_awr_sample(String is_awr_sample) {
		this.is_awr_sample = is_awr_sample;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	public String getSession_type() {
		return session_type;
	}
	public void setSession_type(String session_type) {
		this.session_type = session_type;
	}
	public String getFlags() {
		return flags;
	}
	public void setFlags(String flags) {
		this.flags = flags;
	}
	public String getSql_id() {
		return sql_id;
	}
	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}
	public String getIs_sqlid_current() {
		return is_sqlid_current;
	}
	public void setIs_sqlid_current(String is_sqlid_current) {
		this.is_sqlid_current = is_sqlid_current;
	}
	public String getSql_child_number() {
		return sql_child_number;
	}
	public void setSql_child_number(String sql_child_number) {
		this.sql_child_number = sql_child_number;
	}
	public String getSql_opcode() {
		return sql_opcode;
	}
	public void setSql_opcode(String sql_opcode) {
		this.sql_opcode = sql_opcode;
	}
	public String getForce_matching_signature() {
		return force_matching_signature;
	}
	public void setForce_matching_signature(String force_matching_signature) {
		this.force_matching_signature = force_matching_signature;
	}
	public String getTop_level_sql_id() {
		return top_level_sql_id;
	}
	public void setTop_level_sql_id(String top_level_sql_id) {
		this.top_level_sql_id = top_level_sql_id;
	}
	public String getTop_level_sql_opcode() {
		return top_level_sql_opcode;
	}
	public void setTop_level_sql_opcode(String top_level_sql_opcode) {
		this.top_level_sql_opcode = top_level_sql_opcode;
	}
	public String getSql_opname() {
		return sql_opname;
	}
	public void setSql_opname(String sql_opname) {
		this.sql_opname = sql_opname;
	}
	public String getSql_plan_hash_value() {
		return sql_plan_hash_value;
	}
	public void setSql_plan_hash_value(String sql_plan_hash_value) {
		this.sql_plan_hash_value = sql_plan_hash_value;
	}
	public String getSql_plan_line_id() {
		return sql_plan_line_id;
	}
	public void setSql_plan_line_id(String sql_plan_line_id) {
		this.sql_plan_line_id = sql_plan_line_id;
	}
	public String getSql_plan_operation() {
		return sql_plan_operation;
	}
	public void setSql_plan_operation(String sql_plan_operation) {
		this.sql_plan_operation = sql_plan_operation;
	}
	public String getSql_plan_options() {
		return sql_plan_options;
	}
	public void setSql_plan_options(String sql_plan_options) {
		this.sql_plan_options = sql_plan_options;
	}
	public String getSql_exec_id() {
		return sql_exec_id;
	}
	public void setSql_exec_id(String sql_exec_id) {
		this.sql_exec_id = sql_exec_id;
	}
	public String getSql_exec_start() {
		return sql_exec_start;
	}
	public void setSql_exec_start(String sql_exec_start) {
		this.sql_exec_start = sql_exec_start;
	}
	public String getPlsql_entry_object_id() {
		return plsql_entry_object_id;
	}
	public void setPlsql_entry_object_id(String plsql_entry_object_id) {
		this.plsql_entry_object_id = plsql_entry_object_id;
	}
	public String getPlsql_entry_subprogram_id() {
		return plsql_entry_subprogram_id;
	}
	public void setPlsql_entry_subprogram_id(String plsql_entry_subprogram_id) {
		this.plsql_entry_subprogram_id = plsql_entry_subprogram_id;
	}
	public String getPlsql_object_id() {
		return plsql_object_id;
	}
	public void setPlsql_object_id(String plsql_object_id) {
		this.plsql_object_id = plsql_object_id;
	}
	public String getPlsql_subprogram_id() {
		return plsql_subprogram_id;
	}
	public void setPlsql_subprogram_id(String plsql_subprogram_id) {
		this.plsql_subprogram_id = plsql_subprogram_id;
	}
	public String getQc_instance_id() {
		return qc_instance_id;
	}
	public void setQc_instance_id(String qc_instance_id) {
		this.qc_instance_id = qc_instance_id;
	}
	public String getQc_session_id() {
		return qc_session_id;
	}
	public void setQc_session_id(String qc_session_id) {
		this.qc_session_id = qc_session_id;
	}
	public String getQc_session_serial() {
		return qc_session_serial;
	}
	public void setQc_session_serial(String qc_session_serial) {
		this.qc_session_serial = qc_session_serial;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}
	public String getEvent_sharp() {
		return event_sharp;
	}
	public void setEvent_sharp(String event_sharp) {
		this.event_sharp = event_sharp;
	}
	public String getSeq_sharp() {
		return seq_sharp;
	}
	public void setSeq_sharp(String seq_sharp) {
		this.seq_sharp = seq_sharp;
	}
	public String getP1text() {
		return p1text;
	}
	public void setP1text(String p1text) {
		this.p1text = p1text;
	}
	public String getP1() {
		return p1;
	}
	public void setP1(String p1) {
		this.p1 = p1;
	}
	public String getP2text() {
		return p2text;
	}
	public void setP2text(String p2text) {
		this.p2text = p2text;
	}
	public String getP2() {
		return p2;
	}
	public void setP2(String p2) {
		this.p2 = p2;
	}
	public String getP3text() {
		return p3text;
	}
	public void setP3text(String p3text) {
		this.p3text = p3text;
	}
	public String getP3() {
		return p3;
	}
	public void setP3(String p3) {
		this.p3 = p3;
	}
	public String getWait_class() {
		return wait_class;
	}
	public void setWait_class(String wait_class) {
		this.wait_class = wait_class;
	}
	public String getWait_class_id() {
		return wait_class_id;
	}
	public void setWait_class_id(String wait_class_id) {
		this.wait_class_id = wait_class_id;
	}
	public String getWait_time() {
		return wait_time;
	}
	public void setWait_time(String wait_time) {
		this.wait_time = wait_time;
	}
	public String getSession_state() {
		return session_state;
	}
	public void setSession_state(String session_state) {
		this.session_state = session_state;
	}
	public String getTime_waited() {
		return time_waited;
	}
	public void setTime_waited(String time_waited) {
		this.time_waited = time_waited;
	}
	public String getBlocking_session_status() {
		return blocking_session_status;
	}
	public void setBlocking_session_status(String blocking_session_status) {
		this.blocking_session_status = blocking_session_status;
	}
	public String getBlocking_session() {
		return blocking_session;
	}
	public void setBlocking_session(String blocking_session) {
		this.blocking_session = blocking_session;
	}
	public String getBlocking_session_serial() {
		return blocking_session_serial;
	}
	public void setBlocking_session_serial(String blocking_session_serial) {
		this.blocking_session_serial = blocking_session_serial;
	}
	public String getBlocking_inst_id() {
		return blocking_inst_id;
	}
	public void setBlocking_inst_id(String blocking_inst_id) {
		this.blocking_inst_id = blocking_inst_id;
	}
	public String getBlocking_hangchain_info() {
		return blocking_hangchain_info;
	}
	public void setBlocking_hangchain_info(String blocking_hangchain_info) {
		this.blocking_hangchain_info = blocking_hangchain_info;
	}
	public String getCurrent_obj() {
		return current_obj;
	}
	public void setCurrent_obj(String current_obj) {
		this.current_obj = current_obj;
	}
	public String getCurrent_file() {
		return current_file;
	}
	public void setCurrent_file(String current_file) {
		this.current_file = current_file;
	}
	public String getCurrent_block() {
		return current_block;
	}
	public void setCurrent_block(String current_block) {
		this.current_block = current_block;
	}
	public String getCurrent_row() {
		return current_row;
	}
	public void setCurrent_row(String current_row) {
		this.current_row = current_row;
	}
	public String getTop_level_call() {
		return top_level_call;
	}
	public void setTop_level_call(String top_level_call) {
		this.top_level_call = top_level_call;
	}
	public String getTop_level_call_name() {
		return top_level_call_name;
	}
	public void setTop_level_call_name(String top_level_call_name) {
		this.top_level_call_name = top_level_call_name;
	}
	public String getConsumer_group_id() {
		return consumer_group_id;
	}
	public void setConsumer_group_id(String consumer_group_id) {
		this.consumer_group_id = consumer_group_id;
	}
	public String getXid() {
		return xid;
	}
	public void setXid(String xid) {
		this.xid = xid;
	}
	public String getRemote_instance() {
		return remote_instance;
	}
	public void setRemote_instance(String remote_instance) {
		this.remote_instance = remote_instance;
	}
	public String getTime_model() {
		return time_model;
	}
	public void setTime_model(String time_model) {
		this.time_model = time_model;
	}
	public String getIn_connection_mgmt() {
		return in_connection_mgmt;
	}
	public void setIn_connection_mgmt(String in_connection_mgmt) {
		this.in_connection_mgmt = in_connection_mgmt;
	}
	public String getIn_parse() {
		return in_parse;
	}
	public void setIn_parse(String in_parse) {
		this.in_parse = in_parse;
	}
	public String getIn_hard_parse() {
		return in_hard_parse;
	}
	public void setIn_hard_parse(String in_hard_parse) {
		this.in_hard_parse = in_hard_parse;
	}
	public String getIn_sql_execution() {
		return in_sql_execution;
	}
	public void setIn_sql_execution(String in_sql_execution) {
		this.in_sql_execution = in_sql_execution;
	}
	public String getIn_plsql_execution() {
		return in_plsql_execution;
	}
	public void setIn_plsql_execution(String in_plsql_execution) {
		this.in_plsql_execution = in_plsql_execution;
	}
	public String getIn_plsql_rpc() {
		return in_plsql_rpc;
	}
	public void setIn_plsql_rpc(String in_plsql_rpc) {
		this.in_plsql_rpc = in_plsql_rpc;
	}
	public String getIn_plsql_compilation() {
		return in_plsql_compilation;
	}
	public void setIn_plsql_compilation(String in_plsql_compilation) {
		this.in_plsql_compilation = in_plsql_compilation;
	}
	public String getIn_java_execution() {
		return in_java_execution;
	}
	public void setIn_java_execution(String in_java_execution) {
		this.in_java_execution = in_java_execution;
	}
	public String getIn_bind() {
		return in_bind;
	}
	public void setIn_bind(String in_bind) {
		this.in_bind = in_bind;
	}
	public String getIn_cursor_close() {
		return in_cursor_close;
	}
	public void setIn_cursor_close(String in_cursor_close) {
		this.in_cursor_close = in_cursor_close;
	}
	public String getIn_sequence_load() {
		return in_sequence_load;
	}
	public void setIn_sequence_load(String in_sequence_load) {
		this.in_sequence_load = in_sequence_load;
	}
	public String getCapture_overhead() {
		return capture_overhead;
	}
	public void setCapture_overhead(String capture_overhead) {
		this.capture_overhead = capture_overhead;
	}
	public String getReplay_overhead() {
		return replay_overhead;
	}
	public void setReplay_overhead(String replay_overhead) {
		this.replay_overhead = replay_overhead;
	}
	public String getIs_captured() {
		return is_captured;
	}
	public void setIs_captured(String is_captured) {
		this.is_captured = is_captured;
	}
	public String getIs_replayed() {
		return is_replayed;
	}
	public void setIs_replayed(String is_replayed) {
		this.is_replayed = is_replayed;
	}
	public String getService_hash() {
		return service_hash;
	}
	public void setService_hash(String service_hash) {
		this.service_hash = service_hash;
	}
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public String getMachine() {
		return machine;
	}
	public void setMachine(String machine) {
		this.machine = machine;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getEcid() {
		return ecid;
	}
	public void setEcid(String ecid) {
		this.ecid = ecid;
	}
	public String getTm_delta_time() {
		return tm_delta_time;
	}
	public void setTm_delta_time(String tm_delta_time) {
		this.tm_delta_time = tm_delta_time;
	}
	public String getTm_delta_cpu_time() {
		return tm_delta_cpu_time;
	}
	public void setTm_delta_cpu_time(String tm_delta_cpu_time) {
		this.tm_delta_cpu_time = tm_delta_cpu_time;
	}
	public String getTm_delta_db_time() {
		return tm_delta_db_time;
	}
	public void setTm_delta_db_time(String tm_delta_db_time) {
		this.tm_delta_db_time = tm_delta_db_time;
	}
	public String getDelta_time() {
		return delta_time;
	}
	public void setDelta_time(String delta_time) {
		this.delta_time = delta_time;
	}
	public String getDelta_read_io_requests() {
		return delta_read_io_requests;
	}
	public void setDelta_read_io_requests(String delta_read_io_requests) {
		this.delta_read_io_requests = delta_read_io_requests;
	}
	public String getDelta_write_io_requests() {
		return delta_write_io_requests;
	}
	public void setDelta_write_io_requests(String delta_write_io_requests) {
		this.delta_write_io_requests = delta_write_io_requests;
	}
	public String getDelta_read_io_bytes() {
		return delta_read_io_bytes;
	}
	public void setDelta_read_io_bytes(String delta_read_io_bytes) {
		this.delta_read_io_bytes = delta_read_io_bytes;
	}
	public String getDelta_write_io_bytes() {
		return delta_write_io_bytes;
	}
	public void setDelta_write_io_bytes(String delta_write_io_bytes) {
		this.delta_write_io_bytes = delta_write_io_bytes;
	}
	public String getDelta_interconnect_io_bytes() {
		return delta_interconnect_io_bytes;
	}
	public void setDelta_interconnect_io_bytes(String delta_interconnect_io_bytes) {
		this.delta_interconnect_io_bytes = delta_interconnect_io_bytes;
	}
	public String getPga_allocated() {
		return pga_allocated;
	}
	public void setPga_allocated(String pga_allocated) {
		this.pga_allocated = pga_allocated;
	}
	public String getTemp_space_allocated() {
		return temp_space_allocated;
	}
	public void setTemp_space_allocated(String temp_space_allocated) {
		this.temp_space_allocated = temp_space_allocated;
	}	
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();

		objJson.put("dbid",this.getDbid());
		objJson.put("inst_id",StringUtil.parseInt(this.getInst_id(),0));
		objJson.put("sample_id",this.getSample_id());
		objJson.put("sample_time",this.getSample_time());
		objJson.put("is_awr_sample",this.getIs_awr_sample());
		objJson.put("sid",this.getSid());
		objJson.put("serial",this.getSerial());
		objJson.put("session_id",this.getSession_id());
		objJson.put("session_type",this.getSession_type());
		objJson.put("flags",this.getFlags());
		objJson.put("user_id",this.getUser_id());
		objJson.put("sql_id",this.getSql_id());
		objJson.put("is_sqlid_current",this.getIs_sqlid_current());
		objJson.put("sql_child_number",this.getSql_child_number());
		objJson.put("sql_opcode",this.getSql_opcode());
		objJson.put("force_matching_signature",this.getForce_matching_signature());
		objJson.put("top_level_sql_id",this.getTop_level_sql_id());
		objJson.put("top_level_sql_opcode",this.getTop_level_sql_opcode());
		objJson.put("sql_opname",this.getSql_opname());
		objJson.put("sql_plan_hash_value",this.getSql_plan_hash_value());
		objJson.put("sql_plan_line_id",this.getSql_plan_line_id());
		objJson.put("sql_plan_operation",this.getSql_plan_operation());
		objJson.put("sql_plan_options",this.getSql_plan_options());
		objJson.put("sql_exec_id",this.getSql_exec_id());
		objJson.put("sql_exec_start",this.getSql_exec_start());
		objJson.put("plsql_entry_object_id",this.getPlsql_entry_object_id());
		objJson.put("plsql_entry_subprogram_id",this.getPlsql_entry_subprogram_id());
		objJson.put("plsql_object_id",this.getPlsql_object_id());
		objJson.put("plsql_subprogram_id",this.getPlsql_subprogram_id());
		objJson.put("qc_instance_id",this.getQc_instance_id());
		objJson.put("qc_session_id",this.getQc_session_id());
		objJson.put("qc_session_serial",this.getQc_session_serial());
		objJson.put("event",this.getEvent());
		objJson.put("event_id",this.getEvent_id());
		objJson.put("event_sharp",this.getEvent_sharp());
		objJson.put("seq_sharp",this.getSeq_sharp());
		objJson.put("p1text",this.getP1text());
		objJson.put("p1",this.getP1());
		objJson.put("p2text",this.getP2text());
		objJson.put("p2",this.getP2());
		objJson.put("p3text",this.getP3text());
		objJson.put("p3",this.getP3());
		objJson.put("wait_class",this.getWait_class());
		objJson.put("wait_class_id",this.getWait_class_id());
		objJson.put("wait_time",this.getWait_time());
		objJson.put("session_state",this.getSession_state());
		objJson.put("time_waited",this.getTime_waited());
		objJson.put("blocking_session_status",this.getBlocking_session_status());
		objJson.put("blocking_session",this.getBlocking_session());
		objJson.put("blocking_session_serial",this.getBlocking_session_serial());
		objJson.put("blocking_inst_id",this.getBlocking_inst_id());
		objJson.put("blocking_hangchain_info",this.getBlocking_hangchain_info());
		objJson.put("current_obj",this.getCurrent_obj());
		objJson.put("current_file",this.getCurrent_file());
		objJson.put("current_block",this.getCurrent_block());
		objJson.put("current_row",this.getCurrent_row());
		objJson.put("top_level_call",this.getTop_level_call());
		objJson.put("top_level_call_name",this.getTop_level_call_name());
		objJson.put("consumer_group_id",this.getConsumer_group_id());
		objJson.put("xid",this.getXid());
		objJson.put("remote_instance",this.getRemote_instance());
		objJson.put("time_model",this.getTime_model());
		objJson.put("in_connection_mgmt",this.getIn_connection_mgmt());
		objJson.put("in_parse",this.getIn_parse());
		objJson.put("in_hard_parse",this.getIn_hard_parse());
		objJson.put("in_sql_execution",this.getIn_sql_execution());
		objJson.put("in_plsql_execution",this.getIn_plsql_execution());
		objJson.put("in_plsql_rpc",this.getIn_plsql_rpc());
		objJson.put("in_plsql_compilation",this.getIn_plsql_compilation());
		objJson.put("in_java_execution",this.getIn_java_execution());
		objJson.put("in_bind",this.getIn_bind());
		objJson.put("in_cursor_close",this.getIn_cursor_close());
		objJson.put("in_sequence_load",this.getIn_sequence_load());
		objJson.put("capture_overhead",this.getCapture_overhead());
		objJson.put("replay_overhead",this.getReplay_overhead());
		objJson.put("is_captured",this.getIs_captured());
		objJson.put("is_replayed",this.getIs_replayed());
		objJson.put("service_hash",this.getService_hash());
		objJson.put("program",this.getProgram());
		objJson.put("module",this.getModule());
		objJson.put("action",this.getAction());
		objJson.put("client_id",this.getClient_id());
		objJson.put("machine",this.getMachine());
		objJson.put("port",this.getPort());
		objJson.put("ecid",this.getEcid());
		objJson.put("tm_delta_time",StringUtil.parseFloat(this.getTm_delta_time(),0));
		objJson.put("tm_delta_cpu_time",StringUtil.parseFloat(this.getTm_delta_cpu_time(),0));
		objJson.put("tm_delta_db_time",StringUtil.parseFloat(this.getTm_delta_db_time(),0));
		objJson.put("delta_time",StringUtil.parseFloat(this.getDelta_time(),0));
		objJson.put("delta_read_io_requests",StringUtil.parseFloat(this.getDelta_read_io_requests(),0));
		objJson.put("delta_write_io_requests",StringUtil.parseFloat(this.getDelta_write_io_requests(),0));
		objJson.put("delta_read_io_bytes",StringUtil.parseFloat(this.getDelta_read_io_bytes(),0));
		objJson.put("delta_write_io_bytes",StringUtil.parseFloat(this.getDelta_write_io_bytes(),0));
		objJson.put("delta_interconnect_io_bytes",StringUtil.parseFloat(this.getDelta_interconnect_io_bytes(),0));
		objJson.put("pga_allocated",StringUtil.parseFloat(this.getPga_allocated(),0));
		objJson.put("temp_space_allocated",StringUtil.parseFloat(this.getTemp_space_allocated(),0));

		return objJson;
	}	
		
}