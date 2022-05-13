package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("fraSpaceUsage")
public class FraSpaceUsage extends Base implements Jsonable {
    private String check_day;
    private String check_seq;
    private String inst_id;
    private String file_type;
    private String percent_space_used;
    private String percent_space_reclaimable;
    private String number_of_files;
    
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
	public String getFile_type() {
		return file_type;
	}
	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}
	public String getPercent_space_used() {
		return percent_space_used;
	}
	public void setPercent_space_used(String percent_space_used) {
		this.percent_space_used = percent_space_used;
	}
	public String getPercent_space_reclaimable() {
		return percent_space_reclaimable;
	}
	public void setPercent_space_reclaimable(String percent_space_reclaimable) {
		this.percent_space_reclaimable = percent_space_reclaimable;
	}
	public String getNumber_of_files() {
		return number_of_files;
	}
	public void setNumber_of_files(String number_of_files) {
		this.number_of_files = number_of_files;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("inst_id",this.getInst_id());
		objJson.put("file_type",this.getFile_type());
		objJson.put("percent_space_used",this.getPercent_space_used());
		objJson.put("percent_space_reclaimable",this.getPercent_space_reclaimable());
		objJson.put("number_of_files",this.getNumber_of_files());

		return objJson; 
	}		
}
