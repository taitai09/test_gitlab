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
 * 2017.10.23	이원식	최초작성
 **********************************************************/

@Alias("cd")
public class Cd extends Base implements Jsonable {
	
    private String cd;
    private String cd_nm;
    private String cd_desc;
    private String ref_vl_1;
    private String ref_vl_2;
    private String ref_vl_3;
    private String ordered;
    private String use_yn;
    
    private String cdArry;
    private String cdNmArry;
    private String cdDescArry;
    private String cdRefVl1Arry;
    private String cdRefVl2Arry;
    private String cdRefVl3Arry;
    private String orderedArry;
    private String useYnArry;

	public String getCd() {
		return cd;
	}
	public void setCd(String cd) {
		this.cd = cd;
	}
	public String getCd_nm() {
		return cd_nm;
	}
	public void setCd_nm(String cd_nm) {
		this.cd_nm = cd_nm;
	}
	public String getCd_desc() {
		return cd_desc;
	}
	public void setCd_desc(String cd_desc) {
		this.cd_desc = cd_desc;
	}
	public String getRef_vl_1() {
		return ref_vl_1;
	}
	public void setRef_vl_1(String ref_vl_1) {
		this.ref_vl_1 = ref_vl_1;
	}
	public String getRef_vl_2() {
		return ref_vl_2;
	}
	public void setRef_vl_2(String ref_vl_2) {
		this.ref_vl_2 = ref_vl_2;
	}
	public String getRef_vl_3() {
		return ref_vl_3;
	}
	public void setRef_vl_3(String ref_vl_3) {
		this.ref_vl_3 = ref_vl_3;
	}
	public String getOrdered() {
		return ordered;
	}
	public void setOrdered(String ordered) {
		this.ordered = ordered;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}	
	public String getCdArry() {
		return cdArry;
	}
	public void setCdArry(String cdArry) {
		this.cdArry = cdArry;
	}
	public String getCdNmArry() {
		return cdNmArry;
	}
	public void setCdNmArry(String cdNmArry) {
		this.cdNmArry = cdNmArry;
	}
	public String getCdDescArry() {
		return cdDescArry;
	}
	public void setCdDescArry(String cdDescArry) {
		this.cdDescArry = cdDescArry;
	}
	public String getCdRefVl1Arry() {
		return cdRefVl1Arry;
	}
	public void setCdRefVl1Arry(String cdRefVl1Arry) {
		this.cdRefVl1Arry = cdRefVl1Arry;
	}
	public String getCdRefVl2Arry() {
		return cdRefVl2Arry;
	}
	public void setCdRefVl2Arry(String cdRefVl2Arry) {
		this.cdRefVl2Arry = cdRefVl2Arry;
	}
	public String getCdRefVl3Arry() {
		return cdRefVl3Arry;
	}
	public void setCdRefVl3Arry(String cdRefVl3Arry) {
		this.cdRefVl3Arry = cdRefVl3Arry;
	}
	public String getOrderedArry() {
		return orderedArry;
	}
	public void setOrderedArry(String orderedArry) {
		this.orderedArry = orderedArry;
	}
	public String getUseYnArry() {
		return useYnArry;
	}
	public void setUseYnArry(String useYnArry) {
		this.useYnArry = useYnArry;
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
