package omc.spop.model;

import java.math.BigDecimal;
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
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("filesystemSpaceCheck")
public class FilesystemSpaceCheck extends Base implements Jsonable {
    private String check_day;
    private String check_seq;
    private String inst_id;
    private String filesystem_type_cd;
    private BigDecimal space_used;
    private BigDecimal total_space_size;
    private BigDecimal space_used_percent;
    private BigDecimal threshold_percent;
    
	public String getCheck_day() {
		return check_day;
	}
	public void setCheck_day(String check_day) {
		this.check_day = check_day;
	}
	public String getCheck_seq() {
		return check_seq;
	}
	public void setCheck_seq(String check_seq) {
		this.check_seq = check_seq;
	}
	public String getInst_id() {
		return inst_id;
	}
	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}
	public String getFilesystem_type_cd() {
		return filesystem_type_cd;
	}
	public void setFilesystem_type_cd(String filesystem_type_cd) {
		this.filesystem_type_cd = filesystem_type_cd;
	}
	public BigDecimal getSpace_used() {
		return space_used;
	}
	public void setSpace_used(BigDecimal space_used) {
		this.space_used = space_used;
	}
	public BigDecimal getTotal_space_size() {
		return total_space_size;
	}
	public void setTotal_space_size(BigDecimal total_space_size) {
		this.total_space_size = total_space_size;
	}
	public BigDecimal getSpace_used_percent() {
		return space_used_percent;
	}
	public void setSpace_used_percent(BigDecimal space_used_percent) {
		this.space_used_percent = space_used_percent;
	}
	
	public BigDecimal getThreshold_percent() {
		return threshold_percent;
	}
	public void setThreshold_percent(BigDecimal threshold_percent) {
		this.threshold_percent = threshold_percent;
	}
	/*
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("inst_id",this.getInst_id());
		objJson.put("filesystem_type_cd",this.getFilesystem_type_cd());
		objJson.put("space_used",this.getSpace_used());
		objJson.put("total_space_size",this.getTotal_space_size());
		objJson.put("space_used_percent",this.getSpace_used_percent());

		return objJson; 
	}
	*/
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
