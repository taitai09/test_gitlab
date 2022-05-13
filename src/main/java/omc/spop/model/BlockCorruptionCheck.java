package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("blockCorruptionCheck")
public class BlockCorruptionCheck extends Base implements Jsonable {
    private String check_day;
    private String check_seq;
    private String inst_id;
    private String files;
    private String block;
    private String blocks;
    private String corruption_change;
    private String corruption_type;
    private String file_name;
    
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
	public String getFiles() {
		return files;
	}
	public void setFiles(String files) {
		this.files = files;
	}
	public String getBlock() {
		return block;
	}
	public void setBlock(String block) {
		this.block = block;
	}
	public String getBlocks() {
		return blocks;
	}
	public void setBlocks(String blocks) {
		this.blocks = blocks;
	}
	public String getCorruption_change() {
		return corruption_change;
	}
	public void setCorruption_change(String corruption_change) {
		this.corruption_change = corruption_change;
	}
	public String getCorruption_type() {
		return corruption_type;
	}
	public void setCorruption_type(String corruption_type) {
		this.corruption_type = corruption_type;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	
	public String getInst_id() {
		return inst_id;
	}
	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("inst_id",this.getInst_id());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("files",this.getFiles());
		objJson.put("block",StringUtil.parseDouble(this.getBlock(),0));
		objJson.put("blocks",StringUtil.parseDouble(this.getBlocks(),0));
		objJson.put("corruption_change",StringUtil.parseDouble(this.getCorruption_change(),0));
		objJson.put("corruption_type",this.getCorruption_type());
		objJson.put("file_name",this.getFile_name());

		return objJson; 
	}		
}
