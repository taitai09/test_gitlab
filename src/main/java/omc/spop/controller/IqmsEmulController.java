package omc.spop.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import omc.spop.model.IqmsURIConstants;
import omc.spop.model.PerformanceCheckMng;
import omc.spop.model.ReqIqms;
import omc.spop.model.RtnMsg;
import omc.spop.model.Status;
import omc.spop.model.server.Iqms;
import omc.spop.service.IqmsService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.Util;

/**
 * Handles requests for the Employee service.
 */
@Controller
public class IqmsEmulController {

	private static final Logger logger = LoggerFactory.getLogger(IqmsEmulController.class);

	@Autowired
	private IqmsService iqmsService;

	@Value("#{defaultConfig['iqmsip']}")
	private String iqmsip;

	@Value("#{defaultConfig['iqmsport']}")
	private String iqmsport;

	@Value("#{defaultConfig['iqms_server_uri']}")
	private String IQMS_SERVER_URI;

	@Value("#{defaultConfig['customer']}")
	private String customer;

	@RequestMapping(value = IqmsURIConstants.ConfirmFromWeb, method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String cmConfirmFromWeb(@ModelAttribute("iqms") Iqms iqms, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("cmConfirmFromWeb====================");
		logger.info("Start Confirm.");
		logger.info("Compcd=" + iqms.getCompcd());
		logger.info("Cmid=" + iqms.getCmid());
		logger.info("Cmpknm=" + iqms.getCmpknm());
		logger.info("Cmdepday=" + iqms.getCmdepday());
		logger.info("Cmusrnum=" + iqms.getCmusrnum());
		logger.info("Jobcd=" + iqms.getJobcd());
		logger.info("Cmcreateday=" + iqms.getCmcreateday());
		
		String responseContent = "";
		String cmStatus = "1";     /* 1: CM확정, 3: CM완료, 4: CM취소, 5: CM삭제 */
		String openpopStatus = ""; /* 200: 정상, 1010:업체코드없음, 1011:업체코드 검증 실패, 1012:업무코드 검증 실패, 1013:성능점검ID 조회실패, 1020:Connection Refused, 1021:XML File Process Fail */
		Iqms resultIqms = null;
		String compCd = "";        // 업체코드
		
		try {
			/* 정상처리 */
			openpopStatus = "200";
			response.setStatus(HttpServletResponse.SC_OK);
			
			iqms.setCmworkday(DateUtil.getNowDate("yyyy-MM-dd hh:mm:ss"));
			iqms.setPerfcheckid("");
			resultIqms = iqms;
			
			logger.info("Compcd=" + iqms.getCompcd());
			
			/* 업체코드 Null 검증 */
			if (iqms.getCompcd().equals("")) {
				logger.info("CompanyCD=Company Code Null");
				openpopStatus = "1010";
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			/* 업체코드 Validation 검증 */
			} else {
				StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
				pbeEnc.setPassword("madeopen");
				compCd = StringUtils.defaultString(pbeEnc.decrypt(iqms.getCompcd()));
				
				if (!Util.chkVerification(compCd)) {
					logger.info("CompanyCD=Company Code Verification Fail");
					openpopStatus = "1011";
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
			
			/* 업무코드 검증 */
			if (openpopStatus.equals("200")) {
				int wrkjobDivCdCnt = iqmsService.getValidateWrkjobDivCd(iqms.getJobcd());
				if (wrkjobDivCdCnt == 0) {
					logger.info("WrkjobDivCD=WorkJob Code Not Found");
					openpopStatus = "1012";
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
			
			/* CM 확정 */
			if (openpopStatus.equals("200")) {
				resultIqms = iqmsService.cmConfirm(iqms);
			}
			
		} catch (java.net.SocketTimeoutException e1) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + e1.getMessage());
			openpopStatus = "1022";
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (java.net.ConnectException e2) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + e2.getMessage());
			openpopStatus = "1020";
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (org.jasypt.exceptions.EncryptionOperationNotPossibleException e3) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + e3.getMessage());
			openpopStatus = "1011";
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} catch (Exception e) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + e.getMessage());
			if(e.getMessage().contains("master agent")) // Master 로 부터 throw 된 오류일경우
			{
				openpopStatus = "1021";
			} else {// 기타 오류 일경우 
				openpopStatus = "1099";
			}
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);	
		}
		responseContent = iqmsService.generateIqmsResult(cmStatus, openpopStatus, resultIqms);
		logger.info("responseContent=" + responseContent);
		return responseContent;
	}

	@RequestMapping(value = IqmsURIConstants.CompleteFromWeb, method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String cmCompleteFromWeb(@ModelAttribute("iqms") Iqms iqms, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("CompleteFromWeb====================");
		logger.info("Start Complete.");
		logger.info("Compcd=" + iqms.getCompcd());
		logger.info("Cmid=" + iqms.getCmid());
		logger.info("status=" + iqms.getStatus());

		String responseContent = "";
		String cmStatus = "3";     /* 1: CM확정, 3: CM완료, 4: CM취소, 5: CM삭제 */
		String openpopStatus = ""; /* 200: 정상, 1010:업체코드없음, 1011:업체코드 검증 실패, 1012:업무코드 검증 실패, 1013:성능점검ID 조회실패, 1020:Connection Refused, 1021:XML File Process Fail */
		Iqms resultIqms = null;
		String compCd = "";                 // 업체코드
		
		try {
			
			openpopStatus = "200";
			response.setStatus(HttpServletResponse.SC_OK);
			
			iqms.setCmworkday(DateUtil.getNowDate("yyyy-MM-dd hh:mm:ss"));
			iqms.setResult(0);
			iqms.setCmusrnum("");
			iqms.setJobcd("");
			iqms.setCmcreateday("");
			iqms.setPerfcheckid("");
			resultIqms = iqms;
			
			logger.info("Compcd=" + iqms.getCompcd());
			
			
			if (iqms.getCompcd().equals("")) {  /* 업체코드 Null 검증 */
				logger.info("CompanyCD=Company Code Null");
				openpopStatus = "1010";
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			
			} else {  /* 업체코드 Validation 검증 */
				StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
				pbeEnc.setPassword("madeopen");
				compCd = StringUtils.defaultString(pbeEnc.decrypt(iqms.getCompcd()));
				
				if (!Util.chkVerification(compCd)) {
					logger.info("CompanyCD=Company Code Verification Fail");
					openpopStatus = "1011";
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
			
			if (openpopStatus.equals("200")) { /* 성능점검ID 검증 */
				String perfCheckId = StringUtils.defaultString(iqmsService.getValidateDeployID(iqms.getCmid()));
				logger.info("PerfCheckID="+perfCheckId);
				iqms.setPerfcheckid(perfCheckId);
				if (perfCheckId.equals("")) {
					logger.info("PerfCheckID=PerfCheckID not found");
					openpopStatus = "1013";
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
			
			if (openpopStatus.equals("200")) { /* CM 완료 */
				resultIqms = iqmsService.cmComplete(iqms);
				
				if (resultIqms.getResult() == 0) { /* error occured */
					openpopStatus = "1099";
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			}
		} catch (org.jasypt.exceptions.EncryptionOperationNotPossibleException e1) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + e1.getMessage());
			openpopStatus = "1011";
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} catch (Exception e) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + e.getMessage());
			openpopStatus = "1099";
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		responseContent = iqmsService.generateIqmsResult(cmStatus, openpopStatus, resultIqms);
		logger.info("responseContent=" + responseContent);
		return responseContent;
	}
	/**
	 * CM 취소
	 * @param iqms
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = IqmsURIConstants.CancelFromWeb, method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String cmCancelFromWeb(@ModelAttribute("iqms") Iqms iqms, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("CM 취소====================");
		logger.info("Start Cancel.");
		logger.info("Compcd=" + iqms.getCompcd());
		logger.info("Cmid=" + iqms.getCmid());
		logger.info("status=" + iqms.getStatus());

		String responseContent = "";
		String cmStatus = "4"; /* 1: CM확정, 3: CM완료, 4: CM취소, 5: CM삭제 */
		String openpopStatus = ""; /* 200: 정상, 1010:업체코드없음, 1011:업체코드 검증 실패, 1012:업무코드 검증 실패, 1013:성능점검ID 조회실패, 1020:Connection Refused, 1021:XML File Process Fail */
		Iqms resultIqms = null;
		String compCd = "";                 // 업체코드

		try {
			/* 정상처리 */
			openpopStatus = "200";
			response.setStatus(HttpServletResponse.SC_OK);
			
			iqms.setCmworkday(DateUtil.getNowDate("yyyy-MM-dd hh:mm:ss"));
			iqms.setResult(0);
			iqms.setCmusrnum("");
			iqms.setJobcd("");
			iqms.setCmcreateday("");
			iqms.setPerfcheckid("");
			resultIqms = iqms;
			
			logger.info("Compcd=" + iqms.getCompcd());
			
			/* 업체코드 Null 검증 */
			if (iqms.getCompcd().equals("")) {
				logger.info("CompanyCD=Company Code Null");
				openpopStatus = "1010";
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			/* 업체코드 Validation 검증 */
			} else {
				StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
				pbeEnc.setPassword("madeopen");
				compCd = StringUtils.defaultString(pbeEnc.decrypt(iqms.getCompcd()));
				
				if (!Util.chkVerification(compCd)) {
					logger.info("CompanyCD=Company Code Verification Fail");
					openpopStatus = "1011";
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
			
			/* 성능점검ID 검증 */
			if (openpopStatus.equals("200")) {
				String perfCheckId = StringUtils.defaultString(iqmsService.getValidateDeployID(iqms.getCmid()));
				logger.info("PerfCheckID="+perfCheckId);
				iqms.setPerfcheckid(perfCheckId);
				if (perfCheckId.equals("")) {
					logger.info("PerfCheckID=PerfCheckID not found");
					openpopStatus = "1013";
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
			
			/* CM 취소 */
			if (openpopStatus.equals("200")) {
				resultIqms = iqmsService.cmCancel(iqms);
				
				/* error occured */
				if (resultIqms.getResult() == 0) {
					openpopStatus = "1099";
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			}
			
		} catch (org.jasypt.exceptions.EncryptionOperationNotPossibleException e1) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + e1.getMessage());
			openpopStatus = "1011";
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} catch (Exception e) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + e.getMessage());
			openpopStatus = "1099";
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		responseContent = iqmsService.generateIqmsResult(cmStatus, openpopStatus, resultIqms);
		logger.info("responseContent=" + responseContent);
		return responseContent;
	}

	@RequestMapping(value = IqmsURIConstants.DeleteFromWeb, method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String cmDeleteFromWeb(@ModelAttribute("iqms") Iqms iqms, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("cmDeleteFromWeb====================");
		logger.info("Start Delete.");
		logger.info("Compcd=" + iqms.getCompcd());
		logger.info("Cmid=" + iqms.getCmid());
		logger.info("status=" + iqms.getStatus());

		String responseContent = "";
		String cmStatus = "5"; /* 1: CM확정, 3: CM완료, 4: CM취소, 5: CM삭제 */
		String openpopStatus = ""; /* 200: 정상, 1010:업체코드없음, 1011:업체코드 검증 실패, 1012:업무코드 검증 실패, 1013:성능점검ID 조회실패, 1020:Connection Refused, 1021:XML File Process Fail */
		Iqms resultIqms = null;
		String compCd = "";                 // 업체코드

		try {
			/* 정상처리 */
			openpopStatus = "200";
			response.setStatus(HttpServletResponse.SC_OK);
			
			iqms.setCmworkday(DateUtil.getNowDate("yyyy-MM-dd hh:mm:ss"));
			iqms.setResult(0);
			iqms.setCmusrnum("");
			iqms.setJobcd("");
			iqms.setCmcreateday("");
			iqms.setPerfcheckid("");
			resultIqms = iqms;
			
			logger.info("Compcd=" + iqms.getCompcd());
			
			/* 업체코드 Null 검증 */
			if (iqms.getCompcd().equals("")) {
				logger.info("CompanyCD=Company Code Null");
				openpopStatus = "1010";
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			/* 업체코드 Validation 검증 */
			} else {
				StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
				pbeEnc.setPassword("madeopen");
				compCd = StringUtils.defaultString(pbeEnc.decrypt(iqms.getCompcd()));
				
				if (!Util.chkVerification(compCd)) {
					logger.info("CompanyCD=Company Code Verification Fail");
					openpopStatus = "1011";
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
			
			/* 성능점검ID 검증 */
			if (openpopStatus.equals("200")) {
				String perfCheckId = StringUtils.defaultString(iqmsService.getValidateDeployID(iqms.getCmid()));
				logger.info("PerfCheckID="+perfCheckId);
				iqms.setPerfcheckid(perfCheckId);
				if (perfCheckId.equals("")) {
					logger.info("PerfCheckID=PerfCheckID not found");
					openpopStatus = "1013";
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
			
			/* CM 삭제 */
			if (openpopStatus.equals("200")) {
				resultIqms = iqmsService.cmDelete(iqms);
				
				/* error occured */
				if (resultIqms.getResult() == 0) {
					openpopStatus = "1099";
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			}
			
		} catch (org.jasypt.exceptions.EncryptionOperationNotPossibleException e1) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + e1.getMessage());
			openpopStatus = "1011";
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} catch (Exception e) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + e.getMessage());
			openpopStatus = "1099";
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		responseContent = iqmsService.generateIqmsResult(cmStatus, openpopStatus, resultIqms);
		logger.info("responseContent=" + responseContent);
		return responseContent;
	}

	/**
	 * 성능점검 결과 통보
	 * @param rIqms
	 * @return
	 */
	@RequestMapping(value = IqmsURIConstants.IqmsFromWeb, method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody RtnMsg doIqms(@ModelAttribute("rIqms") ReqIqms rIqms) {
		// public @ResponseBody RtnMsg doIqms(@RequestBody ReqIqms rIqms,
		// HttpServletRequest request, HttpServletResponse response) {
		logger.info("==========resultCm==========");
		logger.info("====================");
		logger.info("Start Result.");
//		rIqms.setP_cmid("SKI180023");
//		rIqms.setP_servicegb("1001"); // 서비스 구분 성능점검결과요청
//		rIqms.setP_systemgb("WBN0041007"); // 시스템 구분 ,고정값
//		rIqms.setP_resultyn("Y"); // 패스 논패스 "N"
		String cmid = rIqms.getP_cmid();
		String systemgb = rIqms.getP_systemgb();
		String servicegb = rIqms.getP_servicegb();
		String resultyn = rIqms.getP_resultyn();

		logger.info("====================");
		logger.info("Start Result.");
		logger.info("Cmid=" + cmid);
		logger.info("systemgb=" + systemgb);
		logger.info("servicegb=" + servicegb);
		logger.info("resultyn=" + resultyn);
		
		String jsondata = null;
		String result = null;
		RtnMsg rtnMsg = null;

		try {
			jsondata = new ObjectMapper().writeValueAsString(rIqms);
			logger.debug(jsondata);
			JSONObject json = (JSONObject) JSONValue.parse(jsondata);
			if(customer != null && customer.equals("kbcd")) {
				result = Util.httpJsonSendKbCd1000(Util.getHost(IQMS_SERVER_URI), Util.getIp(IQMS_SERVER_URI), Util.getPort(IQMS_SERVER_URI), IqmsURIConstants.Iqms, json);
			}else {
				result = Util.httpJsonSend(Util.getHost(IQMS_SERVER_URI), Util.getIp(IQMS_SERVER_URI), Util.getPort(IQMS_SERVER_URI), IqmsURIConstants.Iqms, json);
			}
			logger.info("result=" + result);
			JSONObject jso = (JSONObject) JSONValue.parse(result);
			logger.debug("jso:" + jso);
			if (jso != null) {
				try {
					rtnMsg = new ObjectMapper().readValue(jso.toString(), RtnMsg.class);
					// 정상 데이타시
					String ret_code = StringUtils.defaultString(rtnMsg.getRet_code());
					logger.debug("ret_code :"+ret_code);
					if (ret_code.equals("SUCCESS") || ret_code.equals("SUCESS")) {
						List ls = (List) rtnMsg.getRet_data();
						for (int i = 0; i < ls.size(); i++) {
							Status status = (Status) ls.get(i);
//							String strCmid = StringUtils.defaultString(status.getCmid());
							String strCmid = StringUtils.defaultString(status.getCmid());
							String strStatus = StringUtils.defaultString(status.getStatus());
							System.out.println("Cmid ="+strCmid);
							System.out.println("Status =" + strStatus);
							PerformanceCheckMng performanceCheckMng = new PerformanceCheckMng();
							performanceCheckMng.setDeploy_id(cmid);
//							String selectNextPerfCheckId = iqmsService.selectNextPerfCheckId();
//							performanceCheckMng.setPerf_check_id(selectNextPerfCheckId);
							String beforePerfCheckId = StringUtils.defaultString(iqmsService.selectBeforePerfCheckId(performanceCheckMng));
							logger.debug("beforePerfCheckId :"+beforePerfCheckId);
							if(beforePerfCheckId.equals("")) { continue;};
							
							performanceCheckMng.setPerf_check_id(beforePerfCheckId);

							if(strStatus.equals("OK")) {
								performanceCheckMng.setCheck_result_anc_yn("Y");
							}else {
								performanceCheckMng.setCheck_result_anc_yn("N");
							}
							
							try {
								int updateResult = iqmsService.updateCheckResultAncYn(performanceCheckMng);
								logger.info("updateResult=" + updateResult);
							} catch (Exception e) {
								logger.error("e.msg:" + e.getMessage());
								e.printStackTrace();
							}							
						}
					} else {
						// 오류시
						logger.debug(rtnMsg.getErr_msg());
						logger.debug(rtnMsg.getRet_code());
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
		return rtnMsg;
	}
	/**
	 * 동기화batch
	 * @param rIqms
	 * @return
	 */
	@RequestMapping(value = IqmsURIConstants.IqmsSyncFromWeb, method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody RtnMsg IqmsSyncFromWeb(@ModelAttribute("rIqms") ReqIqms rIqms) {
		logger.info(this.getClass().getName()+"========batchCm============");
		logger.info(this.getClass().getName()+" Start Batch.");

		Map<String,String> map = new HashMap<String,String>();
		map.put("p_servicegb","1002");//서비스 구분 성능점검결과요청
		map.put("p_systemgb","WBN0041007");//시스템 구분
		
		String jsondata = null;
		String result = null;
		RtnMsg rtnMsg = null;

		try {
//			jsondata = new ObjectMapper().writeValueAsString(riqms);
			
			Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
			jsondata = gson.toJson(map);
			
			logger.debug(jsondata);
			JSONObject json = (JSONObject) JSONValue.parse(jsondata);
			result = Util.httpJsonSendKbCd1000(Util.getHost(IQMS_SERVER_URI), Util.getIp(IQMS_SERVER_URI),
					Util.getPort(IQMS_SERVER_URI), IqmsURIConstants.Iqms, json);
			logger.info("result=" + result);
			JSONObject jso = (JSONObject) JSONValue.parse(result);
			logger.debug("jso:" + jso);
			if (jso != null) {
				try {
					rtnMsg = new ObjectMapper().readValue(jso.toString(), RtnMsg.class);
					// 정상 데이타시
					String ret_code = StringUtils.defaultString(rtnMsg.getRet_code());
					logger.debug("ret_code :"+ret_code);
					if (ret_code.equals("SUCCESS") || ret_code.equals("SUCESS")) {
						List ls = (List) rtnMsg.getRet_data();
						for (int i = 0; i < ls.size(); i++) {
							Status status = (Status) ls.get(i);
							logger.debug(status.getCmid());
							logger.debug("Status=" + status.getStatus());
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
						logger.debug(rtnMsg.getErr_msg());
						logger.debug(rtnMsg.getRet_code());
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
		logger.info("End Batch.");
		logger.info("httpstatusresult=" + HttpServletResponse.SC_OK);
		return rtnMsg;
	}
	
}
