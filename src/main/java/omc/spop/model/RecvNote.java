package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.11.03	이원식	최초작성
 **********************************************************/

@Alias("recvNote")
public class RecvNote extends Base implements Jsonable {
	
	private String send_user_id;
	private String send_user_nm;
	private String send_dt;
	private String recv_user_id;
	private String recv_user_nm;
	private String read_dt;
	private String read_yn;
	private String del_yn;
	private String recv_cnt;
	
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
	public String getRead_dt() {
		return read_dt;
	}
	public void setRead_dt(String read_dt) {
		this.read_dt = read_dt;
	}
	public String getRead_yn() {
		return read_yn;
	}
	public void setRead_yn(String read_yn) {
		this.read_yn = read_yn;
	}
	public String getDel_yn() {
		return del_yn;
	}
	public void setDel_yn(String del_yn) {
		this.del_yn = del_yn;
	}
	public String getRecv_cnt() {
		return recv_cnt;
	}
	public void setRecv_cnt(String recv_cnt) {
		this.recv_cnt = recv_cnt;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		objJson.put("rum",StringUtil.parseInt(this.getRnum(),0));
		objJson.put("send_user_id",this.getSend_user_id());
		objJson.put("send_user_nm",this.getSend_user_nm());
		objJson.put("send_dt",this.getSend_dt());
		objJson.put("recv_user_id",this.getRecv_user_id());
		objJson.put("recv_user_nm",this.getRecv_user_nm());
		objJson.put("read_dt",this.getRead_dt());
		objJson.put("read_yn",this.getRead_yn());
		objJson.put("del_yn",this.getDel_yn());
		objJson.put("recv_cnt",this.getRecv_cnt());

		return objJson;
	}		
}