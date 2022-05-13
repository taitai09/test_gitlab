package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.12.11	이원식	최초작성
 **********************************************************/

@Alias("rgbColor")
public class RgbColor extends Base implements Jsonable {
	
    private String rgb_color_id;
    private String rgb_color_value;
    private String inst_id;
    private String inst_nm;
    
	public String getRgb_color_id() {
		return rgb_color_id;
	}
	public void setRgb_color_id(String rgb_color_id) {
		this.rgb_color_id = rgb_color_id;
	}
	public String getRgb_color_value() {
		return rgb_color_value;
	}
	public void setRgb_color_value(String rgb_color_value) {
		this.rgb_color_value = rgb_color_value;
	}
	public String getInst_id() {
		return inst_id;
	}
	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}
	public String getInst_nm() {
		return inst_nm;
	}
	public void setInst_nm(String inst_nm) {
		this.inst_nm = inst_nm;
	}
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();

		objJson.put("rgb_color_id",StringUtil.parseInt(this.getRgb_color_id(),0));
		objJson.put("rgb_color_value",this.getRgb_color_value());
		
		return objJson;
	}
}