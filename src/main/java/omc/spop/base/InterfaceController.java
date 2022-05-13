package omc.spop.base;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import omc.spop.utils.JSONResult;
import omc.spop.utils.JSONResult.Jsonable;

public abstract class InterfaceController {

	public static final String RESULT_OK = "00000";
	public static final String RESULT_MSG = "성공적으로 실행되었습니다.";

	protected static JSONResult success(List<? extends Jsonable> data) {
		return new JSONResult(data, RESULT_OK, RESULT_MSG);
	}

	protected JSONResult success(Jsonable data) {
		return new JSONResult(data, RESULT_OK, RESULT_MSG);
	}

	protected static JSONResult success(List<? extends Jsonable> data, String resultCode, String resultMsg) {
		return new JSONResult(data, resultCode, resultMsg);
	}

	protected JSONResult getJSONResult(List<Map<String, Object>> data) {
		return new JSONResult(data, RESULT_OK, RESULT_MSG, 1);
	}
	
	protected JSONResult getJSONResult(List<LinkedHashMap<String, Object>> data, boolean flag) {
		return new JSONResult(data, RESULT_OK, RESULT_MSG, 1, flag);
	}

	protected String getErrorJsonString(Exception ex) {
		String msg = ex.getMessage();
		
		if (msg != null && msg.contains("ORA-")) {
			msg = msg.substring(msg.lastIndexOf("ORA-"));
		}
		
		if (msg == null) {
			msg = "처리중 오류가 발생하였습니다.";
		}	
		return "{\"result\":false,\"message\":\"" + msg + "\"}";
	}

	protected String getSuccessJsonString(String data) {
		return "{\"result\":\"true\",\"message\":\"success\",\"data\":\"" + data + "\"}";
	}

	protected String getErrorJsonString(String data, String msg) {
		return "{\"result\":\"false\",\"message\":" + msg + ",\"data\":\"" + data + "\"}";
	}

}