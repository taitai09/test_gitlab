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

@Alias("sqlStdQtyIdtBase")
public class SqlStdQtyIdtBase extends Base implements Jsonable {

	private String qty_chk_idt_cd;
	private String qty_chk_idt_nm;
	private String srt_ord       ;
	private String qty_chk_cont  ;
	private String slv_rsl_cont  ;
	private String qty_chk_idt_yn;

	public String getQty_chk_idt_yn() {
		return qty_chk_idt_yn;
	}

	public void setQty_chk_idt_yn(String qty_chk_idt_yn) {
		this.qty_chk_idt_yn = qty_chk_idt_yn;
	}

	public String getQty_chk_idt_cd() {
		return qty_chk_idt_cd;
	}

	public void setQty_chk_idt_cd(String qty_chk_idt_cd) {
		this.qty_chk_idt_cd = qty_chk_idt_cd;
	}

	public String getQty_chk_idt_nm() {
		return qty_chk_idt_nm;
	}

	public void setQty_chk_idt_nm(String qty_chk_idt_nm) {
		this.qty_chk_idt_nm = qty_chk_idt_nm;
	}

	public String getSrt_ord() {
		return srt_ord;
	}

	public void setSrt_ord(String srt_ord) {
		this.srt_ord = srt_ord;
	}

	public String getQty_chk_cont() {
		return qty_chk_cont;
	}

	public void setQty_chk_cont(String qty_chk_cont) {
		this.qty_chk_cont = qty_chk_cont;
	}

	public String getSlv_rsl_cont() {
		return slv_rsl_cont;
	}

	public void setSlv_rsl_cont(String slv_rsl_cont) {
		this.slv_rsl_cont = slv_rsl_cont;
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
