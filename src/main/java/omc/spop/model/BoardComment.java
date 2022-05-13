package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.11.14	이원식	최초작성
 **********************************************************/

@Alias("boardComment")
public class BoardComment extends Base implements Jsonable {
	
    private String board_mgmt_no;
    private String board_no;
    private String comment_seq;
    private String comment_contents;
    private String del_yn;
    private String reg_dt;
    private String reg_id;
    private String reg_nm;
    private String upd_dt;
    private String upd_id;
    private String upd_nm;
    
	public String getBoard_mgmt_no() {
		return board_mgmt_no;
	}
	public void setBoard_mgmt_no(String board_mgmt_no) {
		this.board_mgmt_no = board_mgmt_no;
	}
	public String getBoard_no() {
		return board_no;
	}
	public void setBoard_no(String board_no) {
		this.board_no = board_no;
	}
	public String getComment_seq() {
		return comment_seq;
	}
	public void setComment_seq(String comment_seq) {
		this.comment_seq = comment_seq;
	}
	public String getComment_contents() {
		return comment_contents;
	}
	public void setComment_contents(String comment_contents) {
		this.comment_contents = comment_contents;
	}
	public String getDel_yn() {
		return del_yn;
	}
	public void setDel_yn(String del_yn) {
		this.del_yn = del_yn;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
	public String getReg_id() {
		return reg_id;
	}
	public void setReg_id(String reg_id) {
		this.reg_id = reg_id;
	}
	public String getReg_nm() {
		return reg_nm;
	}
	public void setReg_nm(String reg_nm) {
		this.reg_nm = reg_nm;
	}
	public String getUpd_dt() {
		return upd_dt;
	}
	public void setUpd_dt(String upd_dt) {
		this.upd_dt = upd_dt;
	}
	public String getUpd_id() {
		return upd_id;
	}
	public void setUpd_id(String upd_id) {
		this.upd_id = upd_id;
	}
	public String getUpd_nm() {
		return upd_nm;
	}
	public void setUpd_nm(String upd_nm) {
		this.upd_nm = upd_nm;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("board_mgmt_no",this.getBoard_mgmt_no());
		objJson.put("board_no",this.getBoard_no());
		objJson.put("comment_seq",this.getComment_seq());
		objJson.put("comment_contents",this.getComment_contents());
		objJson.put("del_yn",this.getDel_yn());
		objJson.put("reg_dt",this.getReg_dt());
		objJson.put("reg_id",this.getReg_id());
		objJson.put("reg_nm",this.getReg_nm());
		objJson.put("upd_dt",this.getUpd_dt());
		objJson.put("upd_id",this.getUpd_id());
		objJson.put("upd_nm",this.getUpd_nm());

		return objJson;
	}		
}
