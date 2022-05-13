package omc.spop.model;

import java.util.ArrayList;
import java.util.List;
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
 * 2018.01.30 이원식 최초작성
 **********************************************************/

@Alias("menu")
public class Menu extends Base implements Jsonable {

	private String rownum;
	private String menu_id;
	private String parent_menu_id;
	private String menu_nm;
	private String menu_desc;
	private String menu_url_addr;
	private String menu_image_nm;
	private String menu_ordering;
	private String use_yn;
	private String crud_flag;

	private String auth_cd;
	private String auth_id;

	private String auth_nm;
	private List<String> array_auth_id;
	private String id;
	private String parent_id;
	private String text;

	private String menu_ordering_list;
	private String menu_id_list;
	
	private List<Menu> children = new ArrayList<Menu>();
	private List<Menu> childMenu = new ArrayList<Menu>();

	
	public String getMenu_ordering_list() {
		return menu_ordering_list;
	}

	public void setMenu_ordering_list(String menu_ordering_list) {
		this.menu_ordering_list = menu_ordering_list;
	}

	public String getMenu_id_list() {
		return menu_id_list;
	}

	public void setMenu_id_list(String menu_id_list) {
		this.menu_id_list = menu_id_list;
	}

	public String getRownum() {
		return rownum;
	}

	public void setRownum(String rownum) {
		this.rownum = rownum;
	}

	public String getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}

	public String getParent_menu_id() {
		return parent_menu_id;
	}

	public void setParent_menu_id(String parent_menu_id) {
		this.parent_menu_id = parent_menu_id;
	}

	public String getMenu_nm() {
		return menu_nm;
	}

	public void setMenu_nm(String menu_nm) {
		this.menu_nm = menu_nm;
	}

	public String getMenu_desc() {
		return menu_desc;
	}

	public void setMenu_desc(String menu_desc) {
		this.menu_desc = menu_desc;
	}

	public String getMenu_url_addr() {
		return menu_url_addr;
	}

	public void setMenu_url_addr(String menu_url_addr) {
		this.menu_url_addr = menu_url_addr;
	}

	public String getMenu_image_nm() {
		return menu_image_nm;
	}

	public void setMenu_image_nm(String menu_image_nm) {
		this.menu_image_nm = menu_image_nm;
	}

	public String getMenu_ordering() {
		return menu_ordering;
	}

	public void setMenu_ordering(String menu_ordering) {
		this.menu_ordering = menu_ordering;
	}

	public String getUse_yn() {
		return use_yn;
	}

	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}

	public String getCrud_flag() {
		return crud_flag;
	}

	public void setCrud_flag(String crud_flag) {
		this.crud_flag = crud_flag;
	}

	public String getAuth_cd() {
		return auth_cd;
	}

	public void setAuth_cd(String auth_cd) {
		this.auth_cd = auth_cd;
	}

	public List<Menu> getChildMenu() {
		return childMenu;
	}

	public void setChildMenu(List<Menu> childMenu) {
		this.childMenu = childMenu;
	}

	public String getAuth_id() {
		return auth_id;
	}

	public void setAuth_id(String auth_id) {
		this.auth_id = auth_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	
	
	public String getAuth_nm() {
		return auth_nm;
	}

	public void setAuth_nm(String auth_nm) {
		this.auth_nm = auth_nm;
	}
	
	public List<String> getArray_auth_id() {
		return array_auth_id;
	}
	
	public void setArray_auth_id(List<String> array_auth_id) {
		this.array_auth_id = array_auth_id;
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
