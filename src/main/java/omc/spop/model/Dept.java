package omc.spop.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.09.20	이원식	최초작성
 **********************************************************/

@Alias("dept")
public class Dept extends Base implements Jsonable {
	
    private String dept_cd;
    private String dept_nm;
    private String dept_desc;
    private String upper_dept_cd;
    private String upper_dept_nm;
    private String use_yn;
    
    private String id;
    private String parent_id;    
    private String text;
    private List<Dept> children = new ArrayList<Dept>();
    
	public String getDept_cd() {
		return dept_cd;
	}
	public void setDept_cd(String dept_cd) {
		this.dept_cd = dept_cd;
	}
	public String getDept_nm() {
		return dept_nm;
	}
	public void setDept_nm(String dept_nm) {
		this.dept_nm = dept_nm;
	}
	public String getDept_desc() {
		return dept_desc;
	}
	public void setDept_desc(String dept_desc) {
		this.dept_desc = dept_desc;
	}
	public String getUpper_dept_cd() {
		return upper_dept_cd;
	}
	public void setUpper_dept_cd(String upper_dept_cd) {
		this.upper_dept_cd = upper_dept_cd;
	}
	public String getUpper_dept_nm() {
		return upper_dept_nm;
	}
	public void setUpper_dept_nm(String upper_dept_nm) {
		this.upper_dept_nm = upper_dept_nm;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public List<Dept> getChildren() {
		return children;
	}
	public void setChildren(List<Dept> children) {
		this.children = children;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("dept_cd",this.getDept_cd());
		objJson.put("dept_nm",this.getDept_nm());
		objJson.put("dept_desc",this.getDept_desc());
		objJson.put("upper_dept_cd",this.getUpper_dept_cd());
		objJson.put("upper_dept_nm",this.getUpper_dept_nm());
		objJson.put("use_yn",this.getUse_yn());

		return objJson;
	}		
}
