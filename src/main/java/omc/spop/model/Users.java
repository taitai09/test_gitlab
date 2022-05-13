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
 * 2017.08.10 이원식 최초작성
 **********************************************************/

@Alias("users")
public class Users extends Base implements Jsonable {

	private String chk_user_id; // 사용여부 승인여부를 일괄처리하기위해 추가.
	private String password;
	private String ext_no;
	private String extNo1;
	private String extNo2;
	private String extNo3;
	private String hp_no;
	private String hpNo1;
	private String hpNo2;
	private String hpNo3;
	private String email;
	private String emailId;
	private String emailCp;
	private String auth_id;
	// private String auth_cd;
	// private String auth_nm;
	private String wrkjob_cd;
	private String wrkjob_cd_nm;
	private String wrkjob_nm;
	private String default_password_yn;
	private String password_chg_dt;
	private String reg_dt;
	private String approve_yn;
	private String approve_dt;
	private String approve_id;
	private String approve_nm;
	private String use_yn;
	private String search_use_yn;
	private String user_auth_id;

	private String new_password;
	private String new_user_id;
	private String is_new;
	private String leader_yn;

	private String popSearchKey;
	private String popSearchValue;
	private String popAuth_cd;
	private String popWrkjob_cd;

	private String auth_start_day;
	private String auth_end_day;
	private String auth_grp_id;

	private String default_wrkjob_cd;
	private String default_auth_grp_cd;
	private String default_auth_grp_id;
	private String default_wrkjob_cd_nm;
	private String default_auth_grp_id_nm;

	private String chk_dbAuth;
	private String search_dbid;
	private String search_approve_yn;
	private String wrkjob_div_cd;
	private String applied_dt;
	private String salt_value;
	private String pass_decrypt;
	/*****감사로그*****/
	private String audit_log_date;
	private String audit_log_div_cd;
	private String audit_log_user_id;
	private String audit_log_ip;
	private String audit_log_desc;
	private String audit_log_result_value;
	private String user_locked_yn;
	

	public String getUser_locked_yn() {
		return user_locked_yn;
	}

	public void setUser_locked_yn(String user_locked_yn) {
		this.user_locked_yn = user_locked_yn;
	}

	public String getPass_decrypt() {
		return pass_decrypt;
	}

	public void setPass_decrypt(String pass_decrypt) {
		this.pass_decrypt = pass_decrypt;
	}

	public String getUser_auth_id() {
		return user_auth_id;
	}

	public void setUser_auth_id(String user_auth_id) {
		this.user_auth_id = user_auth_id;
	}

	public String getAudit_log_date() {
		return audit_log_date;
	}

	public void setAudit_log_date(String audit_log_date) {
		this.audit_log_date = audit_log_date;
	}

	public String getAudit_log_div_cd() {
		return audit_log_div_cd;
	}

	public void setAudit_log_div_cd(String audit_log_div_cd) {
		this.audit_log_div_cd = audit_log_div_cd;
	}

	public String getAudit_log_user_id() {
		return audit_log_user_id;
	}

	public void setAudit_log_user_id(String audit_log_user_id) {
		this.audit_log_user_id = audit_log_user_id;
	}

	public String getAudit_log_ip() {
		return audit_log_ip;
	}

	public void setAudit_log_ip(String audit_log_ip) {
		this.audit_log_ip = audit_log_ip;
	}

	public String getAudit_log_desc() {
		return audit_log_desc;
	}

	public void setAudit_log_desc(String audit_log_desc) {
		this.audit_log_desc = audit_log_desc;
	}

	public String getAudit_log_result_value() {
		return audit_log_result_value;
	}

	public void setAudit_log_result_value(String audit_log_result_value) {
		this.audit_log_result_value = audit_log_result_value;
	}

	/**한글부점명*/
	private String branch_nm;

	/**직원번호*/
//	private String user_id;

	/**한글직원명*/
//	private String user_nm;

	/**한글인사직위구분명*/
	private String grade_div_nm;

	/**팀장직원번호*/
	private String senior_user_id;

	/**한글팀장명*/
	private String senior_user_nm;

	/**it부서전입년월일*/
	private String in_ymd;

	/**it부서전출년월일*/
	private String out_ymd;

	/**외주지원팀명*/
	private String support_team_nm;

	/**외주소속회사명*/
	private String belong_com_nm;
	
	
	public String getSalt_value() {
		return salt_value;
	}

	public void setSalt_value(String salt_value) {
		this.salt_value = salt_value;
	}

	public String getWrkjob_div_cd() {
		return wrkjob_div_cd;
	}

	public void setWrkjob_div_cd(String wrkjob_div_cd) {
		this.wrkjob_div_cd = wrkjob_div_cd;
	}

	public String getWrkjob_cd_nm() {
		return wrkjob_cd_nm;
	}

	public void setWrkjob_cd_nm(String wrkjob_cd_nm) {
		this.wrkjob_cd_nm = wrkjob_cd_nm;
	}

	public String getDefault_wrkjob_cd_nm() {
		return default_wrkjob_cd_nm;
	}

	public void setDefault_wrkjob_cd_nm(String default_wrkjob_cd_nm) {
		this.default_wrkjob_cd_nm = default_wrkjob_cd_nm;
	}

	public String getDefault_auth_grp_id_nm() {
		return default_auth_grp_id_nm;
	}

	public void setDefault_auth_grp_id_nm(String default_auth_grp_id_nm) {
		this.default_auth_grp_id_nm = default_auth_grp_id_nm;
	}

	public String getSearch_approve_yn() {
		return search_approve_yn;
	}

	public void setSearch_approve_yn(String search_approve_yn) {
		this.search_approve_yn = search_approve_yn;
	}

	public String getSearch_use_yn() {
		return search_use_yn;
	}

	public void setSearch_use_yn(String search_use_yn) {
		this.search_use_yn = search_use_yn;
	}

	public String getChk_dbAuth() {
		return chk_dbAuth;
	}

	public void setChk_dbAuth(String chk_dbAuth) {
		this.chk_dbAuth = chk_dbAuth;
	}

	public String getSearch_dbid() {
		return search_dbid;
	}

	public void setSearch_dbid(String search_dbid) {
		this.search_dbid = search_dbid;
	}

	public String getChk_user_id() {
		return chk_user_id;
	}

	public void setChk_user_id(String chk_user_id) {
		this.chk_user_id = chk_user_id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getExt_no() {
		return ext_no;
	}

	public void setExt_no(String ext_no) {
		this.ext_no = ext_no;

		if (ext_no != null && !ext_no.equals("") && ext_no.split("-").length > 0) {
			for (int i = 0; i < ext_no.split("-").length; i++) {
				if (i == 0)
					this.extNo1 = ext_no.split("-")[0];
				if (i == 1)
					this.extNo2 = ext_no.split("-")[1];
				if (i == 2)
					this.extNo3 = ext_no.split("-")[2];
			}
		}

		// if (ext_no != null && ext_no.length() > 11) { 로직 위에걸로 변경 차후에 문제있을시 수정
		// ext_no = ext_no.replaceAll("-", "");
		// if (ext_no.length() == 9) {
		// this.extNo1 = ext_no.substring(0,2);
		// this.extNo2 = ext_no.substring(2,5);
		// this.extNo3 = ext_no.substring(5);
		// } else if (ext_no.length() == 10) {
		// if (ext_no.substring(0,2).equals("02")) {
		// this.extNo1 = ext_no.substring(0,2);
		// this.extNo2 = ext_no.substring(2,6);
		// this.extNo3 = ext_no.substring(6);
		// } else {
		// this.extNo1 = ext_no.substring(0,3);
		// this.extNo2 = ext_no.substring(3,6);
		// this.extNo3 = ext_no.substring(6);
		// }
		// } else if (ext_no.length() == 11) {
		// this.extNo1 = ext_no.substring(0,3);
		// this.extNo2 = ext_no.substring(3,7);
		// this.extNo3 = ext_no.substring(7);
		// }
		// }
	}

	public String getExtNo1() {
		return extNo1;
	}

	public void setExtNo1(String extNo1) {
		this.extNo1 = extNo1;
	}

	public String getExtNo2() {
		return extNo2;
	}

	public void setExtNo2(String extNo2) {
		this.extNo2 = extNo2;
	}

	public String getExtNo3() {
		return extNo3;
	}

	public void setExtNo3(String extNo3) {
		this.extNo3 = extNo3;
	}

	public String getHp_no() {
		return hp_no;
	}

	public void setHp_no(String hp_no) {
		this.hp_no = hp_no;

		if (hp_no != null && !hp_no.equals("") && hp_no.split("-").length > 0) {
			for (int i = 0; i < hp_no.split("-").length; i++) {
				if (i == 0)
					this.hpNo1 = hp_no.split("-")[0];
				if (i == 1)
					this.hpNo2 = hp_no.split("-")[1];
				if (i == 2)
					this.hpNo3 = hp_no.split("-")[2];
			}
		}

		// if (hp_no != null && hp_no.length() > 9) { 10.29. 위에 로직으로 변경 차후 문제있을
		// 시 수정
		// hp_no = hp_no.replaceAll("-", "");
		// if (hp_no.length() == 10) {
		// this.hpNo1 = hp_no.substring(0,3);
		// this.hpNo2 = hp_no.substring(3,6);
		// this.hpNo3 = hp_no.substring(6,10);
		// } else if (hp_no.length() == 11) {
		// this.hpNo1 = hp_no.substring(0,3);
		// this.hpNo2 = hp_no.substring(3,7);
		// this.hpNo3 = hp_no.substring(7,11);
		// }
		// }
	}

	public String getHpNo1() {
		return hpNo1;
	}

	public void setHpNo1(String hpNo1) {
		this.hpNo1 = hpNo1;
	}

	public String getHpNo2() {
		return hpNo2;
	}

	public void setHpNo2(String hpNo2) {
		this.hpNo2 = hpNo2;
	}

	public String getHpNo3() {
		return hpNo3;
	}

	public void setHpNo3(String hpNo3) {
		this.hpNo3 = hpNo3;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;

		if (email != null && email.indexOf("@") > 0) {
			this.emailId = email.split("@")[0];
			this.emailCp = email.split("@")[1];
		}
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getEmailCp() {
		return emailCp;
	}

	public void setEmailCp(String emailCp) {
		this.emailCp = emailCp;
	}

	public String getAuth_id() {
		return auth_id;
	}

	public void setAuth_id(String auth_id) {
		this.auth_id = auth_id;
	}

	// public String getAuth_cd() {
	// return auth_cd;
	// }
	// public void setAuth_cd(String auth_cd) {
	// this.auth_cd = auth_cd;
	// }
	// public String getAuth_nm() {
	// return auth_nm;
	// }
	// public void setAuth_nm(String auth_nm) {
	// this.auth_nm = auth_nm;
	// }
	public String getWrkjob_cd() {
		return wrkjob_cd;
	}

	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}

	public String getWrkjob_nm() {
		return wrkjob_nm;
	}

	public void setWrkjob_nm(String wrkjob_nm) {
		this.wrkjob_nm = wrkjob_nm;
	}

	public String getDefault_password_yn() {
		return default_password_yn;
	}

	public void setDefault_password_yn(String default_password_yn) {
		this.default_password_yn = default_password_yn;
	}

	public String getPassword_chg_dt() {
		return password_chg_dt;
	}

	public void setPassword_chg_dt(String password_chg_dt) {
		this.password_chg_dt = password_chg_dt;
	}

	public String getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}

	public String getApprove_yn() {
		return approve_yn;
	}

	public void setApprove_yn(String approve_yn) {
		this.approve_yn = approve_yn;
	}

	public String getApprove_dt() {
		return approve_dt;
	}

	public void setApprove_dt(String approve_dt) {
		this.approve_dt = approve_dt;
	}

	public String getApprove_id() {
		return approve_id;
	}

	public void setApprove_id(String approve_id) {
		this.approve_id = approve_id;
	}

	public String getApprove_nm() {
		return approve_nm;
	}

	public void setApprove_nm(String approve_nm) {
		this.approve_nm = approve_nm;
	}

	public String getUse_yn() {
		return use_yn;
	}

	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}

	public String getNew_password() {
		return new_password;
	}

	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}

	public String getIs_new() {
		return is_new;
	}

	public void setIs_new(String is_new) {
		this.is_new = is_new;
	}

	public String getLeader_yn() {
		return leader_yn;
	}

	public void setLeader_yn(String leader_yn) {
		this.leader_yn = leader_yn;
	}

	public String getPopSearchKey() {
		return popSearchKey;
	}

	public void setPopSearchKey(String popSearchKey) {
		this.popSearchKey = popSearchKey;
	}

	public String getPopSearchValue() {
		return popSearchValue;
	}

	public void setPopSearchValue(String popSearchValue) {
		this.popSearchValue = popSearchValue;
	}

	public String getPopAuth_cd() {
		return popAuth_cd;
	}

	public void setPopAuth_cd(String popAuth_cd) {
		this.popAuth_cd = popAuth_cd;
	}

	public String getPopWrkjob_cd() {
		return popWrkjob_cd;
	}

	public void setPopWrkjob_cd(String popWrkjob_cd) {
		this.popWrkjob_cd = popWrkjob_cd;
	}

	public String getAuth_start_day() {
		return auth_start_day;
	}

	public void setAuth_start_day(String auth_start_day) {
		this.auth_start_day = auth_start_day;
	}

	public String getAuth_end_day() {
		return auth_end_day;
	}

	public void setAuth_end_day(String auth_end_day) {
		this.auth_end_day = auth_end_day;
	}

	public String getAuth_grp_id() {
		return auth_grp_id;
	}

	public void setAuth_grp_id(String auth_grp_id) {
		this.auth_grp_id = auth_grp_id;
	}

	public String getDefault_wrkjob_cd() {
		return default_wrkjob_cd;
	}

	public void setDefault_wrkjob_cd(String default_wrkjob_cd) {
		this.default_wrkjob_cd = default_wrkjob_cd;
	}

	public String getDefault_auth_grp_cd() {
		return default_auth_grp_cd;
	}

	public void setDefault_auth_grp_cd(String default_auth_grp_cd) {
		this.default_auth_grp_cd = default_auth_grp_cd;
	}

	public String getDefault_auth_grp_id() {
		return default_auth_grp_id;
	}

	public void setDefault_auth_grp_id(String default_auth_grp_id) {
		this.default_auth_grp_id = default_auth_grp_id;
	}

	public String getNew_user_id() {
		return new_user_id;
	}

	public void setNew_user_id(String new_user_id) {
		this.new_user_id = new_user_id;
	}

	public String getBranch_nm() {
		return branch_nm;
	}

	public void setBranch_nm(String branch_nm) {
		this.branch_nm = branch_nm;
	}

	public String getGrade_div_nm() {
		return grade_div_nm;
	}

	public void setGrade_div_nm(String grade_div_nm) {
		this.grade_div_nm = grade_div_nm;
	}

	public String getSenior_user_id() {
		return senior_user_id;
	}

	public void setSenior_user_id(String senior_user_id) {
		this.senior_user_id = senior_user_id;
	}

	public String getSenior_user_nm() {
		return senior_user_nm;
	}

	public void setSenior_user_nm(String senior_user_nm) {
		this.senior_user_nm = senior_user_nm;
	}

	public String getIn_ymd() {
		return in_ymd;
	}

	public void setIn_ymd(String in_ymd) {
		this.in_ymd = in_ymd;
	}

	public String getOut_ymd() {
		return out_ymd;
	}

	public void setOut_ymd(String out_ymd) {
		this.out_ymd = out_ymd;
	}

	public String getSupport_team_nm() {
		return support_team_nm;
	}

	public void setSupport_team_nm(String support_team_nm) {
		this.support_team_nm = support_team_nm;
	}

	public String getBelong_com_nm() {
		return belong_com_nm;
	}

	public void setBelong_com_nm(String belong_com_nm) {
		this.belong_com_nm = belong_com_nm;
	}

	
	public String getApplied_dt() {
		return applied_dt;
	}

	public void setApplied_dt(String applied_dt) {
		this.applied_dt = applied_dt;
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
