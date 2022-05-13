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
 * 2021.09.29	황예지	필드 추가
 **********************************************************/

@Alias("standardComplianceRateTrend")
public class StandardComplianceRateTrend extends Base implements Jsonable {
	private String project_id;
	private String start_day;
	private String end_day;
	private String qty_chk_idt_cd;
	private String sql_std_gather_day;			// SQL표준수집일자
	private String parameter_list;
	
	private String program_cnt;
	private String tot_err_cnt;
	private String cpla_rate;
	
	private String last_program_cnt;				// SQL 본수, 금번회차 총본수
	private String last_tot_err_cnt;				// 미준수 본수, 금번회차 미준수본수
	private String last_cpla_rate;					// 표준 준수율, 금번회차 준수율
	private String incre_sql_cnt;					// SQL 증가건수
	private String incre_cnt_sign;					// SQL 증감기호
	private String incre_err_cnt;					// 미준수 증감
	private String incre_err_sign;					// 미준수 증감기호, 1:파란, 0: 동일, -1:빨간
	private String incre_rate;						// 준수율 증감
	private String incre_rate_sign;					// 준수율 증감기호, 1:파란, 0: 동일, -1:빨간
	private String last_sql_std_gather_day;			// 금번회차
	private String pre_sql_std_gather_day;			// 이전회차
	
	private String wrkjob_cd;				// 업무 ID
	private String wrkjob_cd_nm;			// 업무
	private String pre_cpla_rate;			// 이전회차 준수율
	private String sum_base1;				// 업무코드
	
	private String qty_chk_idt_nm;			// 품질점검지표명
	private String err_cnt;					// 미준수건수
	private String sql_std_qty_div_cd;		// SQL표준점검구분코드(1087), 1: SQL표준셀프점검, 2: 형상기반SQL표준일괄점검, 3: SQL품질진단, 4 :실행기반SQL표준일괄점검
	private String job_scheduler_nm;		// 스케쥴러 명
	private String sql_std_qty_scheduler_no;// 스케줄러 번호
	
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

	public String getJob_scheduler_nm() {
		return job_scheduler_nm;
	}

	public void setJob_scheduler_nm(String job_scheduler_nm) {
		this.job_scheduler_nm = job_scheduler_nm;
	}

	public String getSql_std_qty_scheduler_no() {
		return sql_std_qty_scheduler_no;
	}

	public void setSql_std_qty_scheduler_no(String sql_std_qty_scheduler_no) {
		this.sql_std_qty_scheduler_no = sql_std_qty_scheduler_no;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public String getProject_id() {
		return project_id;
	}

	public String getSql_std_qty_div_cd() {
		return sql_std_qty_div_cd;
	}

	public void setSql_std_qty_div_cd(String sql_std_qty_div_cd) {
		this.sql_std_qty_div_cd = sql_std_qty_div_cd;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getStart_day() {
		return start_day;
	}

	public String getEnd_day() {
		return end_day;
	}

	public void setStart_day(String start_day) {
		this.start_day = start_day;
	}

	public void setEnd_day(String end_day) {
		this.end_day = end_day;
	}

	public String getQty_chk_idt_cd() {
		return qty_chk_idt_cd;
	}

	public void setQty_chk_idt_cd(String qty_chk_idt_cd) {
		this.qty_chk_idt_cd = qty_chk_idt_cd;
	}

	public String getSql_std_gather_day() {
		return sql_std_gather_day;
	}

	public void setSql_std_gather_day(String sql_std_gather_day) {
		this.sql_std_gather_day = sql_std_gather_day;
	}

	public String getParameter_list() {
		return parameter_list;
	}

	public void setParameter_list(String parameter_list) {
		this.parameter_list = parameter_list;
	}

	public String getProgram_cnt() {
		return program_cnt;
	}

	public String getTot_err_cnt() {
		return tot_err_cnt;
	}

	public String getCpla_rate() {
		return cpla_rate;
	}

	public void setTot_err_cnt(String tot_err_cnt) {
		this.tot_err_cnt = tot_err_cnt;
	}

	public void setCpla_rate(String cpla_rate) {
		this.cpla_rate = cpla_rate;
	}

	public void setProgram_cnt(String program_cnt) {
		this.program_cnt = program_cnt;
	}

	public String getLast_program_cnt() {
		return last_program_cnt;
	}

	public String getLast_tot_err_cnt() {
		return last_tot_err_cnt;
	}

	public String getLast_cpla_rate() {
		return last_cpla_rate;
	}

	public String getIncre_sql_cnt() {
		return incre_sql_cnt;
	}

	public String getIncre_cnt_sign() {
		return incre_cnt_sign;
	}

	public String getIncre_err_cnt() {
		return incre_err_cnt;
	}

	public String getIncre_err_sign() {
		return incre_err_sign;
	}

	public String getIncre_rate() {
		return incre_rate;
	}

	public String getIncre_rate_sign() {
		return incre_rate_sign;
	}

	public String getLast_sql_std_gather_day() {
		return last_sql_std_gather_day;
	}

	public String getPre_sql_std_gather_day() {
		return pre_sql_std_gather_day;
	}

	public void setLast_program_cnt(String last_program_cnt) {
		this.last_program_cnt = last_program_cnt;
	}

	public void setLast_tot_err_cnt(String last_tot_err_cnt) {
		this.last_tot_err_cnt = last_tot_err_cnt;
	}

	public void setLast_cpla_rate(String last_cpla_rate) {
		this.last_cpla_rate = last_cpla_rate;
	}

	public void setIncre_sql_cnt(String incre_sql_cnt) {
		this.incre_sql_cnt = incre_sql_cnt;
	}

	public void setIncre_cnt_sign(String incre_cnt_sign) {
		this.incre_cnt_sign = incre_cnt_sign;
	}

	public void setIncre_err_cnt(String incre_err_cnt) {
		this.incre_err_cnt = incre_err_cnt;
	}

	public void setIncre_err_sign(String incre_err_sign) {
		this.incre_err_sign = incre_err_sign;
	}

	public void setIncre_rate(String incre_rate) {
		this.incre_rate = incre_rate;
	}

	public void setIncre_rate_sign(String incre_rate_sign) {
		this.incre_rate_sign = incre_rate_sign;
	}

	public void setLast_sql_std_gather_day(String last_sql_std_gather_day) {
		this.last_sql_std_gather_day = last_sql_std_gather_day;
	}

	public void setPre_sql_std_gather_day(String pre_sql_std_gather_day) {
		this.pre_sql_std_gather_day = pre_sql_std_gather_day;
	}

	public String getWrkjob_cd() {
		return wrkjob_cd;
	}

	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}

	public String getWrkjob_cd_nm() {
		return wrkjob_cd_nm;
	}

	public String getPre_cpla_rate() {
		return pre_cpla_rate;
	}

	public String getSum_base1() {
		return sum_base1;
	}

	public void setWrkjob_cd_nm(String wrkjob_cd_nm) {
		this.wrkjob_cd_nm = wrkjob_cd_nm;
	}

	public void setPre_cpla_rate(String pre_cpla_rate) {
		this.pre_cpla_rate = pre_cpla_rate;
	}

	public void setSum_base1(String sum_base1) {
		this.sum_base1 = sum_base1;
	}

	public String getQty_chk_idt_nm() {
		return qty_chk_idt_nm;
	}

	public String getErr_cnt() {
		return err_cnt;
	}

	public void setQty_chk_idt_nm(String qty_chk_idt_nm) {
		this.qty_chk_idt_nm = qty_chk_idt_nm;
	}

	public void setErr_cnt(String err_cnt) {
		this.err_cnt = err_cnt;
	}
}
