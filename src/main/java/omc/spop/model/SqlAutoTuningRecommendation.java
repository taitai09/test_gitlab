package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("sqlAutoTuningRecommendation")
public class SqlAutoTuningRecommendation extends Base implements Jsonable {
	
    private String check_day;
    private String check_seq;
    private String recommendation_report;
    
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
	public String getRecommendation_report() {
		return recommendation_report;
	}
	public void setRecommendation_report(String recommendation_report) {
		this.recommendation_report = recommendation_report;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("rnum",StringUtil.parseInt(this.getRnum(),0));
		objJson.put("recommendation_report",this.getRecommendation_report());
		
		return objJson; 
	}		
}
