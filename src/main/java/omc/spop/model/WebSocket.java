package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import com.google.gson.Gson;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2018.04.23	이원식	최초작성
 **********************************************************/

@Alias("webSocket")
public class WebSocket extends Base implements Jsonable {
    private String session_id;
    private String user_id;
    private String auth_cd;
    private String wrkjob_cd;
    private String send_type; /* ALL : 전체, USER : 사용자, AUTH : 권한, WRKJOB : 업무 */
    private String send_gubun;
    private String title;
    private String message;
    
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getAuth_cd() {
		return auth_cd;
	}
	public void setAuth_cd(String auth_cd) {
		this.auth_cd = auth_cd;
	}
	public String getWrkjob_cd() {
		return wrkjob_cd;
	}
	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}
	public String getSend_type() {
		return send_type;
	}
	public void setSend_type(String send_type) {
		this.send_type = send_type;
	}
	public String getSend_gubun() {
		return send_gubun;
	}
	public void setSend_gubun(String send_gubun) {
		this.send_gubun = send_gubun;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public static WebSocket converWebSocket(String source) {
		WebSocket webSocket = new WebSocket();
	    Gson gson = new Gson();
	    webSocket = gson.fromJson(source, WebSocket.class);

	    return webSocket;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();

		objJson.put("session_id",this.getSession_id());
		objJson.put("user_id",this.getUser_id());
		objJson.put("auth_cd",this.getAuth_cd());
		objJson.put("wrkjob_cd",this.getWrkjob_cd());
		objJson.put("send_type",this.getSend_type());
		objJson.put("send_gubun",this.getSend_gubun());
		objJson.put("title",this.getTitle());
		objJson.put("message",this.getMessage());

		return objJson;
	}    
}
