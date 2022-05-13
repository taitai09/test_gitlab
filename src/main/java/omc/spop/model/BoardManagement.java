package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.11.13	이원식	최초작성
 **********************************************************/

@Alias("boardManagement")
public class BoardManagement extends Base implements Jsonable {
	
    private String board_mgmt_no;
    private String board_type_cd;
    private String board_type_nm;
    private String board_nm;
    private String file_add_yn;
    private String comment_use_yn;
    private String board_use_yn;
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
	public String getBoard_type_cd() {
		return board_type_cd;
	}
	public void setBoard_type_cd(String board_type_cd) {
		this.board_type_cd = board_type_cd;
	}
	public String getBoard_type_nm() {
		return board_type_nm;
	}
	public void setBoard_type_nm(String board_type_nm) {
		this.board_type_nm = board_type_nm;
	}
	public String getBoard_nm() {
		return board_nm;
	}
	public void setBoard_nm(String board_nm) {
		this.board_nm = board_nm;
	}
	public String getFile_add_yn() {
		return file_add_yn;
	}
	public void setFile_add_yn(String file_add_yn) {
		this.file_add_yn = file_add_yn;
	}
	public String getComment_use_yn() {
		return comment_use_yn;
	}
	public void setComment_use_yn(String comment_use_yn) {
		this.comment_use_yn = comment_use_yn;
	}
	public String getBoard_use_yn() {
		return board_use_yn;
	}
	public void setBoard_use_yn(String board_use_yn) {
		this.board_use_yn = board_use_yn;
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
		objJson.put("board_type_cd",this.getBoard_type_cd());
		objJson.put("board_type_nm",this.getBoard_type_nm());
		objJson.put("board_nm",this.getBoard_nm());
		objJson.put("file_add_yn",this.getFile_add_yn());
		objJson.put("comment_use_yn",this.getComment_use_yn());
		objJson.put("board_use_yn",this.getBoard_use_yn());
		objJson.put("reg_dt",this.getReg_dt());
		objJson.put("reg_id",this.getReg_id());
		objJson.put("reg_nm",this.getReg_nm());
		objJson.put("upd_dt",this.getUpd_dt());
		objJson.put("upd_id",this.getUpd_id());
		objJson.put("upd_nm",this.getUpd_nm());

		return objJson;
	}		
}
