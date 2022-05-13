package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.11.03	이원식	최초작성
 **********************************************************/

@Alias("sendNote")
public class SendNote extends Base implements Jsonable {
	
	private String send_user_id;
	private String send_user_nm;
	private String send_dt;
	private String note_type_cd;
	private String note_type_nm;
	private String note_title;
	private String note_contents;
	private String del_yn;
	private String send_cnt;
	private String read_yn;
	private String read_text;
	private String read_dt;
	
	private String recv_user_id;
	private String recv_user_nm;
	private String recvUserArry;
	private String check_user_id;
	private String send_yn;
	
	public String getSend_user_id() {
		return send_user_id;
	}
	public void setSend_user_id(String send_user_id) {
		this.send_user_id = send_user_id;
	}
	public String getSend_user_nm() {
		return send_user_nm;
	}
	public void setSend_user_nm(String send_user_nm) {
		this.send_user_nm = send_user_nm;
	}
	public String getSend_dt() {
		return send_dt;
	}
	public void setSend_dt(String send_dt) {
		this.send_dt = send_dt;
	}
	public String getNote_type_cd() {
		return note_type_cd;
	}
	public void setNote_type_cd(String note_type_cd) {
		this.note_type_cd = note_type_cd;
	}
	public String getNote_type_nm() {
		return note_type_nm;
	}
	public void setNote_type_nm(String note_type_nm) {
		this.note_type_nm = note_type_nm;
	}
	public String getNote_title() {
		return note_title;
	}
	public void setNote_title(String note_title) {
		this.note_title = note_title;
	}
	public String getNote_contents() {
		return note_contents;
	}
	public void setNote_contents(String note_contents) {
		this.note_contents = note_contents;
	}
	public String getDel_yn() {
		return del_yn;
	}
	public void setDel_yn(String del_yn) {
		this.del_yn = del_yn;
	}
	public String getSend_cnt() {
		return send_cnt;
	}
	public void setSend_cnt(String send_cnt) {
		this.send_cnt = send_cnt;
	}	
	public String getRead_yn() {
		return read_yn;
	}
	public void setRead_yn(String read_yn) {
		this.read_yn = read_yn;
	}
	public String getRead_text() {
		return read_text;
	}
	public void setRead_text(String read_text) {
		this.read_text = read_text;
	}
	public String getRead_dt() {
		return read_dt;
	}
	public void setRead_dt(String read_dt) {
		this.read_dt = read_dt;
	}
	public String getRecv_user_id() {
		return recv_user_id;
	}
	public void setRecv_user_id(String recv_user_id) {
		this.recv_user_id = recv_user_id;
	}
	public String getRecv_user_nm() {
		return recv_user_nm;
	}
	public void setRecv_user_nm(String recv_user_nm) {
		this.recv_user_nm = recv_user_nm;
	}
	public String getRecvUserArry() {
		return recvUserArry;
	}
	public void setRecvUserArry(String recvUserArry) {
		this.recvUserArry = recvUserArry;
	}	
	public String getCheck_user_id() {
		return check_user_id;
	}
	public void setCheck_user_id(String check_user_id) {
		this.check_user_id = check_user_id;
	}
	public String getSend_yn() {
		return send_yn;
	}
	public void setSend_yn(String send_yn) {
		this.send_yn = send_yn;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		objJson.put("rnum",StringUtil.parseInt(this.getRnum(),0));
		objJson.put("send_user_id",this.getSend_user_id());
		objJson.put("send_user_nm",this.getSend_user_nm());
		objJson.put("send_dt",this.getSend_dt());
		objJson.put("note_type_cd",this.getNote_type_cd());
		objJson.put("note_type_nm",this.getNote_type_nm());
		objJson.put("note_title",this.getNote_title());
		objJson.put("note_contents",this.getNote_contents());
		objJson.put("recv_user_id",this.getRecv_user_id());
		objJson.put("recv_user_nm",this.getRecv_user_nm());		
		objJson.put("del_yn",this.getDel_yn());
		objJson.put("send_cnt",this.getSend_cnt());
		objJson.put("read_yn",this.getRead_yn());
		objJson.put("read_text",this.getRead_text());
		objJson.put("read_dt",this.getRead_dt());

		return objJson;
	}		
}