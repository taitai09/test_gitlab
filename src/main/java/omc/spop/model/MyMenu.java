package omc.spop.model;

import java.util.ArrayList;
import java.util.List;
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
 * 2018.01.31	이원식	최초작성
 **********************************************************/

@Alias("myMenu")
public class MyMenu extends Base implements Jsonable {
	
	private String menu_id;
	private String parent_menu_nm;
	private String parent_menu_id;
	private String menu_nm;
	private String menu_desc;
	private String menu_url_addr;
	private String menu_image_nm;
	private String menu_ordering;
	private String use_yn;
	
	private String top_menu_id;
	private String menu_level;
	private String error_msg;
	
	private List<MyMenu> childMenu = new ArrayList<MyMenu>();
	
    private String id;
    private String parent_id;    
    private String state;
    private String text;
    private String checked;
    private List<MyMenu> children = new ArrayList<MyMenu>();	
    
	public String getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}
	public String getParent_menu_nm() {
		return parent_menu_nm;
	}
	public void setParent_menu_nm(String parent_menu_nm) {
		this.parent_menu_nm = parent_menu_nm;
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
	public List<MyMenu> getChildMenu() {
		return childMenu;
	}
	public void setChildMenu(List<MyMenu> childMenu) {
		this.childMenu = childMenu;
	}
	public String getTop_menu_id() {
		return top_menu_id;
	}
	public void setTop_menu_id(String top_menu_id) {
		this.top_menu_id = top_menu_id;
	}		
	public String getMenu_level() {
		return menu_level;
	}
	public void setMenu_level(String menu_level) {
		this.menu_level = menu_level;
	}
	
	public String getError_msg() {
		return error_msg;
	}
	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	public List<MyMenu> getChildren() {
		return children;
	}
	public void setChildren(List<MyMenu> children) {
		this.children = children;
	}
//	@SuppressWarnings("unchecked")
//	public JSONObject toJSONObject() {
//		JSONObject objJson = new JSONObject();
//		
//		objJson.put("menu_id",this.getMenu_id());
//		objJson.put("parent_menu_id",this.getParent_menu_id());
//		objJson.put("menu_nm",this.getMenu_nm());
//		objJson.put("menu_desc",this.getMenu_desc());
//		objJson.put("menu_url_addr",this.getMenu_url_addr());
//		objJson.put("menu_image_nm",this.getMenu_image_nm());
//		objJson.put("menu_ordering",this.getMenu_ordering());
//		objJson.put("use_yn",this.getUse_yn());
//		objJson.put("user_id",this.getUser_id());
//		objJson.put("top_menu_id",this.getTop_menu_id());
//		objJson.put("menu_level",this.getMenu_level());
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
