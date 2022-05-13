<%@ page import="org.apache.commons.httpclient.HttpClient"%>
<%@ page import="org.apache.commons.httpclient.methods.PostMethod"%>
<%@ page import="org.apache.commons.httpclient.NameValuePair"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="org.json.*"%>
<%@ page import="org.json.simple.parser.JSONParser"%>
<%@ include file="./agentInfo.jsp"%>

<%
	String resultCode = request.getParameter("resultCode") == null ? "" : request.getParameter("resultCode");
	String secureToken = request.getParameter("secureToken") == null ? "" : request.getParameter("secureToken");
	String secureSessionId = request.getParameter("secureSessionId") == null ? ""
			: request.getParameter("seureSessionId");
	System.out.println("secureSessionId : " + secureSessionId);

	String clientIp = request.getRemoteAddr();

	String logOutUrl = AUTHORIZATION_URL + "logout";
	String logOutUrlAgentId = logOutUrl = "?agentId=" + agentId;
	String resultMessage = "";
	String resultData = "";
	String id = "";
	String returnUrl = "";

	session.setAttribute("secureSessionId", secureSessionId);

	Boolean isErrorPage = false;
	session.setAttribute("isErrorPage", isErrorPage);
	if (resultCode == "" || secureToken == "" || secureSessionId == "") {
		isErrorPage = true;
		session.setAttribute("isErrorPage", isErrorPage);
		System.out.println("error!!!");
		response.sendRedirect("error.jsp");
	} else {
		if (resultCode.equals("000000")) {
			System.out.println("checkauth page sessionId : " + session.getId());
			if (AUTHORIZATION_URL == "") {
				System.out.println("AUTHORIZATION_URL is empty");
				return;
			}
			String TOKEN_AUTHORIZATION_URL = AUTHORIZATION_URL + "token/authorization";
			System.out.println("resultCode : " + resultCode);
			System.out.println("secureToken : " + secureToken);
			System.out.println("secureSessionId : " + secureSessionId);
			System.out.println("TOKEN_AUTHORIZATION_URL : " + TOKEN_AUTHORIZATION_URL);
			System.out.println("agendId : " + agentId);
			System.out.println("requestData : " + requestData);
			System.out.println("clientIp" + clientIp);

			resultCode = "";
			resultMessage = "";
			resultData = "";
			returnUrl = "";

			try {
				PostMethod postMethod = new PostMethod(TOKEN_AUTHORIZATION_URL);
				NameValuePair[] nameValuePair = { new NameValuePair("secureToken", secureToken),
						new NameValuePair("secureSessionId", secureSessionId),
						new NameValuePair("requestData", requestData), new NameValuePair("agendId", agentId),
						new NameValuePair("clientIP", clientIp) };
				postMethod.setQueryString(nameValuePair);
				HttpClient httpClient = new HttpClient();
				httpClient.executeMethod(postMethod);

				String httpResponse = postMethod.getResponseBodyAsString();
				postMethod.releaseConnection();

				System.out.println("httpResponse : " + httpResponse);

				JSONParser parser = new JSONParser();
				Object object = parser.parse(httpResponse);
				JSONObject jsonObject = (JSONObject) object;

				object = jsonObject.get("user");
				JSONObject requestDatajsonObject = (JSONObject) object;

				resultCode = (String) jsonObject.get("resultCode");
				resultMessage = (String) jsonObject.get("resultMessage");
				returnUrl = (String) jsonObject.get("returnUrl");

				if (resultCode.equals("000000")) {
					String[] keys = requestData.split(",");

					for (int i = 0; i < keys.length; i++) {
						String temp = (String) requestDatajsonObject.get(keys[i]);
						//temp ê° nullì´ë©´ ldapDataì ìì²­í ë°ì´í°ê° ìëì§ ê²ìíë¤.
						if (temp == null) {
							JSONObject ldapObject = (JSONObject) requestDatajsonObject.get("ldapData");
							if (ldapObject != null) {
								System.out.println(ldapObject);
								temp = (String) ldapObject.get(keys[i]);
							} else {
								continue;
							}

						}
						if (resultData == "") {
							resultData = temp;
							id = temp;
						} else {
							resultData = resultData + "," + temp;
						}
					}
				} else {
					System.out.println("call error.jsp");
					returnUrl = "error.jsp";
				}

				System.out.println("resultCode : " + resultCode);
				System.out.println("resultMessage : " + resultMessage);
				System.out.println("resultData : " + resultData);
				System.out.println("returnUrl : " + returnUrl);

				System.out.println("resultCode : " + resultCode);
				System.out.println("resultMessage : " + resultMessage);
				System.out.println("resultData : " + resultData);
				System.out.println("id : " + id);

				if (returnUrl == null || returnUrl.equals("")) {
					response.sendRedirect("return.jsp");
				} else {
					response.sendRedirect(returnUrl);
				}

			} catch (Exception e) {
				System.out.println(e.toString());
				isErrorPage = true;
				session.setAttribute("isErrorPage", isErrorPage);
				session.setAttribute("resultCode", resultCode);
				session.setAttribute("resultMessage", resultMessage);
				System.out.println(resultCode);
				System.out.println(resultMessage);

				response.sendRedirect("error.jsp");
			}

		}
	}
%>
