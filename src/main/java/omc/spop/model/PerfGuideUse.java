package omc.spop.model;

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
 * 2017.10.20	이원식	최초작성
 **********************************************************/

@Alias("perfGuideUse")
public class PerfGuideUse extends Base implements Jsonable {
	
    private String guide_no;
    private String use_seq;
    private String wrkjob_cd;
    private String retv_user_id;
    private String retv_dt;
    private String download_yn;
    
	public String getGuide_no() {
		return guide_no;
	}
	public void setGuide_no(String guide_no) {
		this.guide_no = guide_no;
	}
	public String getUse_seq() {
		return use_seq;
	}
	public void setUse_seq(String use_seq) {
		this.use_seq = use_seq;
	}
	public String getWrkjob_cd() {
		return wrkjob_cd;
	}
	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}
	public String getRetv_user_id() {
		return retv_user_id;
	}
	public void setRetv_user_id(String retv_user_id) {
		this.retv_user_id = retv_user_id;
	}
	public String getRetv_dt() {
		return retv_dt;
	}
	public void setRetv_dt(String retv_dt) {
		this.retv_dt = retv_dt;
	}
	public String getDownload_yn() {
		return download_yn;
	}
	public void setDownload_yn(String download_yn) {
		this.download_yn = download_yn;
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
