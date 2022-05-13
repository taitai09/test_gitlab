package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/**********************************************
 * 2020.05.27	이재우	최초작성
 *********************************************/
@Alias("sqlTuningAttachFile")
public class SqlTuningAttachFile extends Base implements Jsonable{
	
	// 튜닝번호
	private String tuning_no;
	
	// 파일순번
	private String file_seq;
	
	// 저장파일명
	private String file_nm;
	
	// 원파일명
	private String org_file_nm;
	
	// 파일크기
	private String file_size;
	
	// 파일확장자명
	private String file_ext_nm;
	
	
	public String getTuning_no() {
		return tuning_no;
	}
	public void setTuning_no(String tuning_no) {
		this.tuning_no = tuning_no;
	}
	public String getFile_seq() {
		return file_seq;
	}
	public void setFile_seq(String file_seq) {
		this.file_seq = file_seq;
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

	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("tuning_no",this.getTuning_no());
		objJson.put("file_seq",this.getFile_seq());
		objJson.put("file_nm",this.getFile_nm());
		objJson.put("org_file_nm",this.getOrg_file_nm());
		objJson.put("file_size",this.getFile_size());
		objJson.put("file_ext_nm",this.getFile_ext_nm());
		
		return objJson;
	}
	
}
