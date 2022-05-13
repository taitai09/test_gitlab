package com.journaldev.spring;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.fasterxml.jackson.databind.ObjectMapper;

import omc.spop.model.IqmsStatus;
import omc.spop.model.IqmsURIConstants;
import omc.spop.model.ReqIqms;
import omc.spop.model.RtnMsg;
import omc.spop.model.Status;
import omc.spop.model.server.Iqms;

public class TestSpringTcpExample {

	public static final String SERVER_URI = "http://localhost:8080";
	// public static final String IQMS_SERVER_URI = "http://localhost:8080";
	// public static final String SERVER_URI = "http://115.68.102.154:8082";
	public static final String IQMS_SERVER_URI = "http://115.68.102.154:8082";

	public static void main(String args[]) throws Exception {
//		testGetDummyOpenPop();
//		testCmConfirmOpenPop();
//		testCmCompleteOpenPop();
//		testCmCancelOpenPop();
//		testCmDeleteOpenPop();
//		testCmResultIQMS();
		testCmBatchIQMS();
	}

	public static void testGetDummyOpenPop() throws Exception {
		String result = "";
		result = httpJsonSend(getHost(SERVER_URI), getIp(SERVER_URI), getPort(SERVER_URI), IqmsURIConstants.Test, null);
		System.out.println("result:\n["+result+"]");
		System.out.println("=========response end==========");
		System.out.println("");

	}

	public static void testCmConfirmOpenPop() throws Exception {
		System.out.println("==========test CmConform==========");
		String result = "";
		Iqms iqms = new Iqms();
		iqms.setCompcd("rF+Kpw650Lg12ccmqaliMFhGdSW54iH9");
		iqms.setCmid("2");
		iqms.setCmpknm("org");
		iqms.setCmdepday("20181010");
		iqms.setCmusrnum("2048");
		iqms.setJobcd("1111");
		iqms.setCmcreateday("20181010101010");

		String jsondata = new ObjectMapper().writeValueAsString(iqms);
		System.out.println(jsondata);
		JSONObject json = (JSONObject) JSONValue.parse(jsondata);
		// 확정
		result = httpJsonSend(getHost(SERVER_URI), getIp(SERVER_URI), getPort(SERVER_URI), IqmsURIConstants.Confirm,
				json);
		System.out.println("result:\n["+result+"]");
		System.out.println("=========response end==========");
		System.out.println("");

	}

	// cm완료
	public static void testCmCompleteOpenPop() throws Exception {
		System.out.println("==========test CmComplete==========");
		String result = "";

		IqmsStatus iqmss = new IqmsStatus();
		iqmss.setCompcd("rF+Kpw650Lg12ccmqaliMFhGdSW54iH9");
		iqmss.setCmid("2");
		iqmss.setStatus("3");

		String jsondata = new ObjectMapper().writeValueAsString(iqmss);
		System.out.println(jsondata);
		JSONObject json = (JSONObject) JSONValue.parse(jsondata);
		// 완료
		result = httpJsonSend(getHost(SERVER_URI), getIp(SERVER_URI), getPort(SERVER_URI), IqmsURIConstants.Complete,
				json);
		System.out.println("result:\n["+result+"]");
		System.out.println("=========response end==========");
		System.out.println("");

	}

	// cm취소
	public static void testCmCancelOpenPop() throws Exception {
		System.out.println("==========test CmCancel==========");
		String result = "";

		IqmsStatus iqmss = new IqmsStatus();
		iqmss.setCompcd("rF+Kpw650Lg12ccmqaliMFhGdSW54iH9");
		iqmss.setCmid("2");
		iqmss.setStatus("4");

		String jsondata = new ObjectMapper().writeValueAsString(iqmss);
		System.out.println(jsondata);
		JSONObject json = (JSONObject) JSONValue.parse(jsondata);
		// 취소
		result = httpJsonSend(getHost(SERVER_URI), getIp(SERVER_URI), getPort(SERVER_URI), IqmsURIConstants.Cancel,
				json);
		System.out.println("result:\n["+result+"]");
		System.out.println("=========response end==========");
		System.out.println("");

	}

	// cm삭제
	public static void testCmDeleteOpenPop() throws Exception {
		System.out.println("==========test CmDelete==========");
		String result = "";

		IqmsStatus iqmss = new IqmsStatus();
		iqmss.setCompcd("rF+Kpw650Lg12ccmqaliMFhGdSW54iH9");
		iqmss.setCmid("2");
		iqmss.setStatus("5");

		String jsondata = new ObjectMapper().writeValueAsString(iqmss);
		System.out.println(jsondata);
		JSONObject json = (JSONObject) JSONValue.parse(jsondata);
		// 삭제
		result = httpJsonSend(getHost(SERVER_URI), getIp(SERVER_URI), getPort(SERVER_URI), IqmsURIConstants.Delete,
				json);
		System.out.println("result:\n["+result+"]");
		System.out.println("=========response end==========");
		System.out.println("");

	}

	// 성능점검결과요청
	public static void testCmResultIQMS() throws Exception {
		System.out.println("==========test CmResult==========");
		String result = "";

		// Status status = new Status();
		// status.setCmid("2");
		// status.setStatus("Y");
		ReqIqms riqms = new ReqIqms();
		riqms.setP_cmid("1");
		riqms.setP_servicegb("1001"); // 서비스 구분 성능점검결과요청
		riqms.setP_systemgb("WBN0041007"); // 고정
		riqms.setP_resultyn("Y"); // 패스 논패스 "N"

		String jsondata = new ObjectMapper().writeValueAsString(riqms);
		System.out.println(jsondata);
		JSONObject json = (JSONObject) JSONValue.parse(jsondata);

		result = httpJsonSend(getHost(IQMS_SERVER_URI), getIp(IQMS_SERVER_URI), getPort(IQMS_SERVER_URI),
				IqmsURIConstants.Iqms, json);
		System.out.println("result:\n["+result+"]");
		System.out.println("=========response end==========");
		System.out.println("");
	}

	// 동기화batch
	public static void testCmBatchIQMS() throws Exception {

		System.out.println("==========test CmBatch==========");
		String result = "";
		JSONObject tmp = null;

		// 배치 업무상 하위 값 사용
		String jsondata = "{\"p_systemgb\":\"WBN0041007\",\"p_servicegb\":\"1002\"}";
		System.out.println(jsondata);
		JSONObject json = (JSONObject) JSONValue.parse(jsondata);

		result = httpJsonSend(getHost(IQMS_SERVER_URI), getIp(IQMS_SERVER_URI), getPort(IQMS_SERVER_URI),
				IqmsURIConstants.Iqms, json);

		System.out.println("result:\n["+result+"]");
		JSONObject jso = (JSONObject) JSONValue.parse(result);
		System.out.println("jso:" + jso);
		RtnMsg rm = null;
		if (jso != null) {
			rm = new ObjectMapper().readValue(jso.toString(), RtnMsg.class);

			// 정상 데이타시
			if (rm.getRet_code().equals("SUCCESS")) {
				List ls = (List) rm.getRet_data();
				for (int i = 0; i < ls.size(); i++) {
					Status status = (Status) ls.get(i);
					System.out.println(status.getCmid());
					System.out.println("Status=" + status.getStatus());
				}
			} else {
				// 오류시
				System.out.println(rm.getErr_msg());
				System.out.println(rm.getRet_code());
			}
		}
	}

	public static String sendData(String ip, int port, String strMsg) throws Exception {
		String receivedLine = null;
		try {
			Socket socket = new Socket(ip, port);
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			pw.println(strMsg);
			pw.flush();
			InputStream stream = socket.getInputStream();
			// BufferedReader br = new BufferedReader(new
			// InputStreamReader(stream));

			byte[] data = new byte[10240];
			byte[] readByte = null;
			String rtn = "";
			int len = 0;

			for (int i = 0; i < 1000000; i++) {
				if (receivedLine != null) {
					if (stream.available() == 0) {
						// 버그로 인하여 존재하는 로직(원인은 동기화 문제로 보고 있슴)
						Thread.sleep(20);
						if (stream.available() == 0) {
							if (stream.available() == 0) {
								Thread.sleep(200);
								if (stream.available() == 0) {
									rtn = receivedLine;
									rtn = rtn.trim();
									rtn = rtn.replace(" ", "");

									if (rtn.endsWith("\n")) {
										// System.out.println("============\r\n============");
										break;
									} else {
										Thread.sleep(2000);
										// System.out.println("============
										// sleep 20 sec============");
										if (stream.available() == 0) {
											// System.out.println("============
											// sleep 20 sec end============");
											break;
										}
									}
								}
							}
						}
					}
				}
				len = stream.read(data, 0, data.length);

				if (len < 0) { //
					break; // while
				}

				// if(data == null) break;
				readByte = new byte[len];
				System.arraycopy(data, 0, readByte, 0, len);
				if (receivedLine == null)
					receivedLine = new String(readByte);
				else
					receivedLine += new String(readByte);
				receivedLine = receivedLine.trim();
			}

			pw.close();
			// log.debug("[==="+response+"===]");
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return receivedLine;
	}

	public static String httpJsonSend(String hst, String ip, int port, String path, JSONObject myJsonObject)
			throws Exception {
		StringBuffer data = new StringBuffer();
		String method = "POST";
		String urlPath = path;
		String httpVersion = "HTTP/1.0";
		String ContentType = "application/json";
		String charset = "UTF-8";

		String host = hst;

		String hport = String.valueOf(port);
		String lineFeed = "\r\n";
		String blank = " ";
		String tmp = "";

		data.append(method);
		data.append(blank);
		data.append(urlPath);
		data.append(blank);
		data.append(httpVersion);
		data.append(lineFeed);

		// data.append("Accept: application/json, application/*+json");
		// data.append(lineFeed);

		data.append("Content-Type: ");
		data.append(ContentType);
		data.append(";charset=");
		data.append(charset);
		data.append(lineFeed);

		data.append("Host: ");
		data.append(host);
		data.append(":");
		data.append(hport);
		data.append(lineFeed);

		data.append("Connection: keep-alive");
		data.append(lineFeed);
		if (myJsonObject == null)
			;
		else {
			data.append("Content-Length: ");
			data.append(myJsonObject.toString().length());
			data.append(lineFeed);
		}

		data.append(lineFeed);

		if (myJsonObject == null)
			;
		else {
			data.append(myJsonObject.toString());
			data.append(lineFeed);
		}

		data.append(lineFeed);

		System.out.println("=========request==========");
		System.out.println(data.toString());
		String rtn = sendData(ip, port, data.toString());
		System.out.println("###################################");
		System.out.println("rtn:[###\n"+rtn+"\n###]");
		System.out.println("###################################");
		System.out.println("=========response==========");

		if (rtn.indexOf("\r\n") >= 0)
			;
		else
			lineFeed = "\n";

		if (rtn.indexOf(lineFeed + lineFeed) >= 0) {
			rtn = rtn.substring(rtn.indexOf(lineFeed + lineFeed) + (lineFeed + lineFeed).length());
		}
		if (rtn.startsWith("[") && rtn.endsWith("]"))
			return rtn;
		if (rtn.startsWith("{") && rtn.endsWith("}"))
			return rtn;
		tmp = rtn.substring(4, rtn.length() - 1);
		// System.out.println(tmp);
		if (tmp.startsWith("[") && tmp.endsWith("]"))
			return tmp;
		if (tmp.startsWith("{") && tmp.endsWith("}"))
			return tmp;
		return rtn;
	}

	public static String getHost(String SERVER_URI) throws Exception {
		URL url = new URL(SERVER_URI);
		return url.getHost();
	}

	public static String getPath(String SERVER_URI) throws Exception {
		URL url = new URL(SERVER_URI);
		return url.getPath();
	}

	public static int getPort(String SERVER_URI) throws Exception {
		URL url = new URL(SERVER_URI);
		return url.getPort();
	}

	public static String getIp(String SERVER_URI) throws Exception {
		URL url = new URL(SERVER_URI);
		return InetAddress.getByName(url.getHost()).getHostAddress();
	}

}
