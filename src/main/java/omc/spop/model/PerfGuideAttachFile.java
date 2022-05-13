package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.10.20	이원식	최초작성
 **********************************************************/

@Alias("perfGuideAttachFile")
public class PerfGuideAttachFile extends Base implements Jsonable {
	
    private String guide_no;
    private String file_seq;
    private String file_nm;
    private String org_file_nm;
    private String file_size;
    private String file_ext_nm;
    private String download_cnt;
    
    private String use_seq;

	public String getGuide_no() {
		return guide_no;
	}
	public void setGuide_no(String guide_no) {
		this.guide_no = guide_no;
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
	public String getDownload_cnt() {
		return download_cnt;
	}
	public void setDownload_cnt(String download_cnt) {
		this.download_cnt = download_cnt;
	}	
	public String getUse_seq() {
		return use_seq;
	}
	public void setUse_seq(String use_seq) {
		this.use_seq = use_seq;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("guide_no",this.getGuide_no());
		objJson.put("file_seq",this.getFile_seq());
		objJson.put("file_nm",this.getFile_nm());
		objJson.put("org_file_nm",this.getOrg_file_nm());
		objJson.put("file_size",this.getFile_size());
		objJson.put("file_ext_nm",this.getFile_ext_nm());
		objJson.put("download_cnt",this.getDownload_cnt());

		return objJson;
	}		
}
