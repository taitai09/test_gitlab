package omc.mqm.model;

import java.util.Map;

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
 * 2019.05.08	임승률	최초작성
 **********************************************************/

@Alias("openmBizCls")
public class OpenmBizCls extends Base implements Jsonable {
	
	private String project_id;
	private String lib_nm;
	private String lib_cd;
	private String model_nm;
	private String model_cd;
	private String sub_nm;
	private String sys_nm;
	private String sys_cd;
	private String main_biz_cls_nm;
	private String main_biz_cls_cd;
	private String mid_biz_cls_nm;
	private String mid_biz_cls_cd;
	private String biz_desc;
	private String remark;
		
	
	public String getLib_cd() {
		return lib_cd;
	}

	public void setLib_cd(String lib_cd) {
		this.lib_cd = lib_cd;
	}

	public String getModel_cd() {
		return model_cd;
	}

	public void setModel_cd(String model_cd) {
		this.model_cd = model_cd;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getLib_nm() {
		return lib_nm;
	}

	public void setLib_nm(String lib_nm) {
		this.lib_nm = lib_nm;
	}

	public String getModel_nm() {
		return model_nm;
	}

	public void setModel_nm(String model_nm) {
		this.model_nm = model_nm;
	}

	public String getSub_nm() {
		return sub_nm;
	}

	public void setSub_nm(String sub_nm) {
		this.sub_nm = sub_nm;
	}

	public String getSys_nm() {
		return sys_nm;
	}

	public void setSys_nm(String sys_nm) {
		this.sys_nm = sys_nm;
	}

	public String getSys_cd() {
		return sys_cd;
	}

	public void setSys_cd(String sys_cd) {
		this.sys_cd = sys_cd;
	}

	public String getMain_biz_cls_nm() {
		return main_biz_cls_nm;
	}

	public void setMain_biz_cls_nm(String main_biz_cls_nm) {
		this.main_biz_cls_nm = main_biz_cls_nm;
	}

	public String getMain_biz_cls_cd() {
		return main_biz_cls_cd;
	}

	public void setMain_biz_cls_cd(String main_biz_cls_cd) {
		this.main_biz_cls_cd = main_biz_cls_cd;
	}

	public String getMid_biz_cls_nm() {
		return mid_biz_cls_nm;
	}

	public void setMid_biz_cls_nm(String mid_biz_cls_nm) {
		this.mid_biz_cls_nm = mid_biz_cls_nm;
	}

	public String getMid_biz_cls_cd() {
		return mid_biz_cls_cd;
	}

	public void setMid_biz_cls_cd(String mid_biz_cls_cd) {
		this.mid_biz_cls_cd = mid_biz_cls_cd;
	}

	public String getBiz_desc() {
		return biz_desc;
	}

	public void setBiz_desc(String biz_desc) {
		this.biz_desc = biz_desc;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
