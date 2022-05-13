package omc.spop.model;

import org.apache.ibatis.type.Alias;

import omc.spop.base.Base;

/***********************************************************
 * 2017.10.23	이원식	최초작성
 **********************************************************/

@Alias("downLoadFile")
public class DownLoadFile extends Base{
	
	private String file_path;
	private String file_nm;
    private String org_file_nm;
    private String file_size;
    private String file_ext_nm;
    
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
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
}
