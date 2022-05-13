package omc.spop.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import omc.spop.model.IqmsURIConstants;
import omc.spop.model.Result;
import omc.spop.model.RtnMsg;
import omc.spop.model.Status;
import omc.spop.service.IqmsService;
import omc.spop.service.UserWorkSyncService;
import omc.spop.service.impl.IqmsServiceImpl;
import omc.spop.utils.Util;

/***********************************************************
 * 2021.01.21 황예지 showErrorMsg 추가
 **********************************************************/

public class IqmsBatchController {
	private static final Logger logger = LoggerFactory.getLogger(IqmsBatchController.class);

	@Autowired
	private IqmsService iqmsService;
	
	@Autowired
	private UserWorkSyncService userWorkSyncService;
	
	@Value("#{defaultConfig['iqmsip']}")
	private String iqmsip;

	@Value("#{defaultConfig['iqmsport']}")
	private String iqmsport;

	@Value("#{defaultConfig['iqms_server_uri']}")
	private String IQMS_SERVER_URI;

	@Value("#{defaultConfig['customer']}")
	private String customer;
	
	public IqmsBatchController() {
	}

//	@Scheduled(cron = "*/30 * * * * *")
	public void print() {
		logger.info("call : " + new Date());
	}
/**
 *
+-------------------- second (0 - 59)
|  +----------------- minute (0 - 59)
|  |  +-------------- hour (0 - 23)
|  |  |  +----------- day of month (1 - 31)
|  |  |  |  +-------- month (1 - 12)
|  |  |  |  |  +----- day of week (0 - 6) (Sunday=0 or 7)
|  |  |  |  |  |  +-- year [optional]
|  |  |  |  |  |  |
*  *  *  *  *  *  * command to be executed 
 * @return
 */
	@Scheduled(cron = "0 0 0 * * *")
	public @ResponseBody RtnMsg batchCm() {
		logger.info("========batchCm============");
		logger.info(" Start Batch.");

		Map<String, String> map = new HashMap<String, String>();
		map.put("p_servicegb", "1002");// 서비스 구분 성능점검결과요청
		map.put("p_systemgb", "WBN0041007");// 시스템구분

		String jsondata = null;
		String result = null;
		RtnMsg rtnMsg = null;
		logger.debug("customer:" + customer);
		if (customer.equals("kbcd")) {
			try {
				// jsondata = new ObjectMapper().writeValueAsString(riqms);

				Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
				jsondata = gson.toJson(map);

				System.out.println(jsondata);
				JSONObject json = (JSONObject) JSONValue.parse(jsondata);
				result = Util.httpJsonSendKbCd1000(Util.getHost(IQMS_SERVER_URI), Util.getIp(IQMS_SERVER_URI),
						Util.getPort(IQMS_SERVER_URI), IqmsURIConstants.Iqms, json);
				logger.info("result=" + result);
				JSONObject jso = (JSONObject) JSONValue.parse(result);
				System.out.println("jso:" + jso);
				if (jso != null) {
					try {
						rtnMsg = new ObjectMapper().readValue(jso.toString(), RtnMsg.class);
						// 정상 데이타시
						String ret_code = StringUtils.defaultString(rtnMsg.getRet_code());
						logger.debug("ret_code :"+ret_code);
						if (ret_code.equals("SUCCESS") || ret_code.equals("SUCESS")) {
							List<?> ls = (List<?>) rtnMsg.getRet_data();
							for (int i = 0; i < ls.size(); i++) {
								Status status = (Status) ls.get(i);
								String strCmid = StringUtils.defaultString(status.getCmid());
								String strStatus = StringUtils.defaultString(status.getStatus());
								System.out.println("Cmid ="+strCmid);
								System.out.println("Status =" + strStatus);
								try {
									int statusSyncResult = iqmsService.cmStatusSync(status);
									logger.info("statusSyncReuslt=" + statusSyncResult);
								} catch (Exception e) {
									logger.error("e.msg:" + e.getMessage());
									e.printStackTrace();
								}
							}
						} else {
							// 오류시
							System.out.println(rtnMsg.getErr_msg());
							System.out.println(rtnMsg.getRet_code());
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (JsonProcessingException e2) {
				e2.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		logger.info("End Batch.");
		logger.info("httpstatusresult=" + HttpServletResponse.SC_OK);
		return rtnMsg;
	}
	
	public @ResponseBody Result scheduledSyncWrkRelatedTable() {
		Result result = new Result();
		logger.debug("customer:" + customer);
		if (customer.equals("kbcd")) {
			try {
				int syncResult = 0;
				//jdbc
				syncResult = userWorkSyncService.scheduledSyncWrkRelatedTable();
				if (syncResult > 0) {
					result.setResult(true);
					result.setMessage("업무, 사용자, 사용자업무 동기화 완료");
				} else {
					result.setResult(false);
					result.setMessage("업무, 사용자, 사용자업무 동기화 실패");
				}
			} catch (ClassNotFoundException e) {
				result = showErrorMsg(e, result);
				
			} catch (SQLException e) {
				result = showErrorMsg(e, result);
				
			} catch (Exception ex) {
				result = showErrorMsg(ex, result);
			}
		}
		return result;
	}

	@Scheduled(cron = "${wrkjob.sync.time}")	
	public @ResponseBody Result scheduledSyncWrkjob() {
		Result result = new Result();
		logger.debug("time:" + new Date());
		logger.debug("customer:" +customer);
		if (customer.equals("kbcd")) {
			try {
				int syncResult = 0;
				//jdbc
				syncResult = userWorkSyncService.scheduledSyncWrkjob();
				if (syncResult > 0) {
					result.setResult(true);
					result.setMessage("업무 동기화 완료");
				} else {
					result.setResult(false);
					result.setMessage("업무 동기화 실패");
				}
			} catch (ClassNotFoundException e) {
				result = showErrorMsg(e, result);
				
			} catch (SQLException e) {
				result = showErrorMsg(e, result);
				
			} catch (Exception ex) {
				result = showErrorMsg(ex, result);
			}
		}
		return result;
	}

	@Scheduled(cron = "${user.sync.time}")	
	public @ResponseBody Result scheduledSyncUser() {
		Result result = new Result();
		logger.debug("time:" + new Date());
		logger.debug("customer:" +customer);
		if (customer.equals("kbcd")) {
			try {
				int syncResult = 0;
				//jdbc
				syncResult = userWorkSyncService.scheduledSyncUser();
				if (syncResult > 0) {
					result.setResult(true);
					result.setMessage("사용자  동기화 완료");
				} else {
					result.setResult(false);
					result.setMessage("사용자  동기화 실패");
				}
			} catch (ClassNotFoundException e) {
				result = showErrorMsg(e, result);
				
			} catch (SQLException e) {
				result = showErrorMsg(e, result);
				
			} catch (Exception ex) {
				result = showErrorMsg(ex, result);
			}
		}
		return result;
	}

	@Scheduled(cron = "${userwrkjob.sync.time}")	
	public @ResponseBody Result scheduledSyncUserWrkjob() {
		Result result = new Result();
		logger.debug("time:" + new Date());
		logger.debug("customer:" +customer);
		if (customer.equals("kbcd")) {
			try {
				int syncResult = 0;
				//jdbc
				syncResult = userWorkSyncService.scheduledSyncUserWrkjob();
				if (syncResult > 0) {
					result.setResult(true);
					result.setMessage("사용자업무 동기화 완료");
				} else {
					result.setResult(false);
					result.setMessage("사용자업무 동기화 실패");
				}
			} catch (ClassNotFoundException e) {
				result = showErrorMsg(e, result);
				
			} catch (SQLException e) {
				result = showErrorMsg(e, result);
				
			} catch (Exception ex) {
				result = showErrorMsg(ex, result);
			}
		}
		return result;
	}
	
	/**
	* 오류 발생 시 printStackTrace를 console에 출력,
	* 오류 메세지 로그로 작성 및 출력,
	* Result객체에 result, message 값 세팅
	* 
	* @param Exception
	* @param Result
	* @return result
	*/
	private Result showErrorMsg(Exception ex, Result result) {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		
		ex.printStackTrace();
		
		result.setResult(false);
		
		if(ex instanceof ClassNotFoundException || ex instanceof SQLException) {
			result.setMessage("사용자업무 동기화 실패");
		}else {
			result.setMessage("업무, 사용자, 사용자업무 동기화 실패");
		}
		
		return result;
	}
	
}
