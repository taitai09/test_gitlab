package omc.spop.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.11.03	이원식	최초작성
 **********************************************************/

@Alias("board")
public class Board extends Base implements Jsonable {
	
    private String board_mgmt_no;
    private String board_no;
    private String top_notice_yn;
    private String title;
    private String board_contents;
    private String contents;
    private String password;
    private String secret_yn;
    private String ref_board_no;
    private String top_board_no;
    private String depth;
    private String lvl;
    private String hit_cnt;
    private String file_nm;
    private List<Board> temp;
    private String org_file_nm;
    private String file_size;
    private String file_ext_nm;
    private String del_yn;
    private String reg_dt; 
    private String reg_id;
    private String reg_nm;
    private String upd_dt;
    private String upd_id;
    private String upd_nm;
    private String comment_cnt;
    private String file_seq;
    private List<MultipartFile> uploadFile;
    private String files;

	public String getFiles() {
		return files;
	}
	public void setFiles(String files) {
		this.files = files;
	}
	public List<Board> getTemp() {
		return temp;
	}
	public void setTemp(List<Board> temp) {
		this.temp = temp;
	}
	public List<MultipartFile> getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(List<MultipartFile> uploadFile) {
		this.uploadFile = uploadFile;
	}
	public String getFile_seq() {
		return file_seq;
	}
	public void setFile_seq(String file_seq) {
		this.file_seq = file_seq;
	}
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
	public String getTop_notice_yn() {
		return top_notice_yn;
	}
	public void setTop_notice_yn(String top_notice_yn) {
		this.top_notice_yn = top_notice_yn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBoard_contents() {
		return board_contents;
	}
	public void setBoard_contents(String board_contents) {
		this.board_contents = board_contents;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSecret_yn() {
		return secret_yn;
	}
	public void setSecret_yn(String secret_yn) {
		this.secret_yn = secret_yn;
	}
	public String getRef_board_no() {
		return ref_board_no;
	}
	public void setRef_board_no(String ref_board_no) {
		this.ref_board_no = ref_board_no;
	}
	public String getTop_board_no() {
		return top_board_no;
	}
	public void setTop_board_no(String top_board_no) {
		this.top_board_no = top_board_no;
	}
	public String getDepth() {
		return depth;
	}
	public void setDepth(String depth) {
		this.depth = depth;
	}
	public String getLvl() {
		return lvl;
	}
	public void setLvl(String lvl) {
		this.lvl = lvl;
	}
	public String getHit_cnt() {
		return hit_cnt;
	}
	public void setHit_cnt(String hit_cnt) {
		this.hit_cnt = hit_cnt;
	}
	public String getFile_nm() {
		return file_nm;
	}
	public void setFile_nm(String file_nm) {
		this.file_nm = file_nm;
	}
	public String getOrg_file_nm() {
		return org_file_nm;
	}
	public void setOrg_file_nm(String org_file_nm) {
		this.org_file_nm = org_file_nm;
	}
	public String getFile_size() {
		return file_size;
	}
	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}
	public String getFile_ext_nm() {
		return file_ext_nm;
	}
	public void setFile_ext_nm(String file_ext_nm) {
		this.file_ext_nm = file_ext_nm;
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
	public String getComment_cnt() {
		return comment_cnt;
	}
	public void setComment_cnt(String comment_cnt) {
		this.comment_cnt = comment_cnt;
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
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
