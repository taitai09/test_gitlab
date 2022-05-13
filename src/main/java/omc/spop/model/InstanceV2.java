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

/***********************************************************
 * 2019.04.30 홍길동 최초작성
 **********************************************************/
@Alias("instanceV2")
public class InstanceV2 extends Instance {
	private String dplx_agent_ips;
	private String dplx_agent_ports;
	private String dplx_gather_agent_ports;

	public String getDplx_agent_ips() {
		return dplx_agent_ips;
	}

	public void setDplx_agent_ips(String dplx_agent_ips) {
		this.dplx_agent_ips = dplx_agent_ips;
	}

	public String getDplx_agent_ports() {
		return dplx_agent_ports;
	}

	public void setDplx_agent_ports(String dplx_agent_ports) {
		this.dplx_agent_ports = dplx_agent_ports;
	}

	public String getDplx_gather_agent_ports() {
		return dplx_gather_agent_ports;
	}

	public void setDplx_gather_agent_ports(String dplx_gather_agent_ports) {
		this.dplx_gather_agent_ports = dplx_gather_agent_ports;
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
