package omc.spop.model;

import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
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
 * 2018.04.13	이원식	최초작성
 * 2018.05.16	이원식	개선유형건수(impr_type_cnt) 추가
 **********************************************************/

@Alias("sqlImprovementType")
public class SqlImprovementType extends Base implements Jsonable {
	
    private String impr_type_cd;
    private String impr_type_cd_nm;
    private String impr_detail_type_cd;
    private String impr_detail_type_cd_nm;
    private String impr_type_cnt;
    
	public String getImpr_type_cd() {
		return impr_type_cd;
	}
	public void setImpr_type_cd(String impr_type_cd) {
		this.impr_type_cd = impr_type_cd;
	}
	public String getImpr_type_cd_nm() {
		return impr_type_cd_nm;
	}
	public void setImpr_type_cd_nm(String impr_type_cd_nm) {
		this.impr_type_cd_nm = impr_type_cd_nm;
	}
	public String getImpr_detail_type_cd() {
		return impr_detail_type_cd;
	}
	public void setImpr_detail_type_cd(String impr_detail_type_cd) {
		this.impr_detail_type_cd = impr_detail_type_cd;
	}
	public String getImpr_detail_type_cd_nm() {
		return impr_detail_type_cd_nm;
	}
	public void setImpr_detail_type_cd_nm(String impr_detail_type_cd_nm) {
		this.impr_detail_type_cd_nm = impr_detail_type_cd_nm;
	}	
	public String getImpr_type_cnt() {
		return impr_type_cnt;
	}
	public void setImpr_type_cnt(String impr_type_cnt) {
		this.impr_type_cnt = impr_type_cnt;
	}
	
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
//	@SuppressWarnings("unchecked")
//	public JSONObject toJSONObject() {
//		JSONObject objJson = new JSONObject();
//		
//		objJson.put("tuning_no",this.getTuning_no());
//		objJson.put("impr_type_cd",this.getImpr_type_cd());
//		objJson.put("impr_type_cd_nm",this.getImpr_type_cd_nm());
//		objJson.put("impr_detail_type_cd",this.getImpr_detail_type_cd());
//		objJson.put("impr_detail_type_cd_nm",this.getImpr_detail_type_cd_nm());
//		objJson.put("impr_type_cnt",StringUtil.parseDouble(this.getImpr_type_cnt(),0));
//		
//		return objJson;
//	}
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