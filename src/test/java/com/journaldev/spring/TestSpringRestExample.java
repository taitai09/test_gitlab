package com.journaldev.spring;

import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import omc.spop.model.IqmsStatus;
import omc.spop.model.IqmsURIConstants;
import omc.spop.model.Status;
import omc.spop.model.server.Iqms;

public class TestSpringRestExample {

//	 public static final String SERVER_URI = "http://115.68.102.154:80";
	public static final String SERVER_URI = "http://127.0.0.1:8080";
	// public static final String SERVER_URI = "http://127.0.0.1:8081";
	// public static final String SERVER_URI = "http://115.68.102.154:8082";
	public static final String IQMS_SERVER_URI = "http://115.68.102.154:8082";
	private static final Logger logger = LoggerFactory.getLogger(TestSpringRestExample.class);

	public static void main(String args[]) throws InterruptedException {

		testGetDummyOpenPop();
		Thread.sleep(1000);

		logger.debug("*****");
		testCmConfirmOpenPop();
		Thread.sleep(1000);

		logger.debug("*****");
		testCmCompleteOpenPop();
		Thread.sleep(1000);

		logger.debug("*****");
		testCmCancelOpenPop();
		Thread.sleep(1000);

		logger.debug("*****");
		testCmDeleteOpenPop();
		Thread.sleep(1000);

		logger.debug("*****");
		testCmResultIQMS1();
		Thread.sleep(1000);

		logger.debug("*****");
		testCmResultIQMS2();
		Thread.sleep(1000);

		logger.debug("*****");
		testCmBatchIQMS();
		Thread.sleep(1000);

		// testGetAllEmployee();

	}

	// {"cmpknm":"org","cmusrnum":"2048","cmdepday":"20181010","telegram_code":"CMID_SEND","cmcreateday":"20181010101010","jobcd":"SKI","OPTION":"1","perfCheckId":"4202","cmid":"SK1180015","compcd":"rF+Kpw650Lg12ccmqaliMFhGdSW54iH9"}
	// test dumy
	private static void testGetDummyOpenPop() {
		RestTemplate restTemplate = new RestTemplate();
		// we can't get List<Employee> because JSON convertor doesn't know the
		// type of
		// object in the list and hence convert it to default JSON object type
		// LinkedHashMap
		Iqms iqms = restTemplate.getForObject(SERVER_URI + IqmsURIConstants.Test, Iqms.class);
		logger.debug("==========test read info==========");
		logger.debug("Compcd=" + iqms.getCompcd());
		logger.debug("Cmid=" + iqms.getCmid());
		logger.debug("Cmpknm=" + iqms.getCmpknm());
		logger.debug("Cmdepday=" + iqms.getCmdepday());
		logger.debug("Cmusrnum=" + iqms.getCmusrnum());
		logger.debug("Jobcd=" + iqms.getJobcd());
		logger.debug("Cmcreateday=" + iqms.getCmcreateday());

		// List<LinkedHashMap> emps =
		// restTemplate.getForObject(SERVER_URI+IqmsURIConstants.Test,
		// List.class);
		// logger.debug(emps.size());
		// for(LinkedHashMap map : emps){
		// logger.debug("ID="+map.get("id")+",Name="+map.get("name")+",CreatedDate="+map.get("createdDate"));;
		// }
	}

	// 확정
	private static void testCmConfirmOpenPop() {
		logger.debug("==========test CmConform==========");

		RestTemplate restTemplate = new RestTemplate();
		Iqms iqms = new Iqms();
		iqms.setCompcd("rF+Kpw650Lg12ccmqaliMFhGdSW54iH9");
		iqms.setCmid("2");
		iqms.setCmpknm("org");
		iqms.setCmdepday("20181010");
		iqms.setCmusrnum("2048");
		iqms.setJobcd("SKI");
		iqms.setCmcreateday("20181010101010");

		logger.debug("SERVER_URI + IqmsURIConstants.Confirm:" + SERVER_URI + IqmsURIConstants.Confirm);
		try {
			Iqms response = restTemplate.postForObject(SERVER_URI + IqmsURIConstants.Confirm, iqms, Iqms.class);
			logger.debug("response:", response);
		} catch (Exception e) {
			logger.debug("e.msg:" + e.getMessage());
		}
	}

	// cm완료
	private static void testCmCompleteOpenPop() {
		logger.debug("==========test CmComplete==========");

		RestTemplate restTemplate = new RestTemplate();
		IqmsStatus iqmss = new IqmsStatus();
		iqmss.setCompcd("rF+Kpw650Lg12ccmqaliMFhGdSW54iH9");
		iqmss.setCmid("2");
		iqmss.setStatus("3");
		try {
			IqmsStatus response = restTemplate.postForObject(SERVER_URI + IqmsURIConstants.Complete, iqmss,
					IqmsStatus.class);
			logger.debug("response:", response);
		} catch (Exception e) {
			logger.debug("e.msg:" + e.getMessage());
		}

	}

	// cm취소
	private static void testCmCancelOpenPop() {
		logger.debug("==========test CmCancel==========");

		RestTemplate restTemplate = new RestTemplate();
		IqmsStatus iqmss = new IqmsStatus();
		iqmss.setCompcd("rF+Kpw650Lg12ccmqaliMFhGdSW54iH9");
		iqmss.setCmid("2");
		iqmss.setStatus("4");

		try {
			IqmsStatus response = restTemplate.postForObject(SERVER_URI + IqmsURIConstants.Cancel, iqmss,
					IqmsStatus.class);
			logger.debug("response:", response);
		} catch (Exception e) {
			logger.debug("e.msg:" + e.getMessage());
		}
	}

	// cm삭제
	private static void testCmDeleteOpenPop() {
		logger.debug("==========test CmDelete==========");

		RestTemplate restTemplate = new RestTemplate();
		IqmsStatus iqmss = new IqmsStatus();
		iqmss.setCompcd("rF+Kpw650Lg12ccmqaliMFhGdSW54iH9");
		iqmss.setCmid("2");
		iqmss.setStatus("5");
		try {
			IqmsStatus response = restTemplate.postForObject(SERVER_URI + IqmsURIConstants.Delete, iqmss,
					IqmsStatus.class);
			logger.debug("response:", response);
		} catch (Exception e) {
			logger.debug("e.msg:" + e.getMessage());
		}
	}

	// 성능점검결과요청
	private static void testCmResultIQMS1() {
		logger.debug("==========test testCmResultIQMS1==========");

		RestTemplate restTemplate = new RestTemplate();
		Status status = new Status();
		status.setCmid("2");
		status.setStatus("Y");

		// Map<String, String> vars = new HashMap<String, String>();
		// vars.put("cmid", "2");
		// vars.put("status", "Y");

		try {
			Status response = restTemplate.getForObject(IQMS_SERVER_URI + IqmsURIConstants.Result1, Status.class,
					status);
			logger.debug("response:", response);
		} catch (Exception e) {
			logger.debug("e.msg:" + e.getMessage());
		}
	}

	private static void testCmResultIQMS2() {
		logger.debug("==========test testCmResultIQMS2==========");

		RestTemplate restTemplate = new RestTemplate();
		Status status = new Status();
		status.setCmid("2");
		status.setStatus("Y");
		try {
			Status response = restTemplate.postForObject(IQMS_SERVER_URI + IqmsURIConstants.Result2, status,
					Status.class);
			logger.debug("response:", response);
		} catch (Exception e) {
			logger.debug("e.msg:" + e.getMessage());
		}
	}

	// 동기화batch
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void testCmBatchIQMS() {
		logger.debug("==========test testCmBatchIQMS==========");

		LinkedHashMap tmp = null;

		RestTemplate restTemplate = new RestTemplate();
		try {
			List<LinkedHashMap> statuss = restTemplate.postForObject(IQMS_SERVER_URI + IqmsURIConstants.Batch, null,
					List.class);
			logger.debug("갯수=" + statuss.size());

			for (int i = 0; i < statuss.size(); i++) {
				tmp =  statuss.get(i);
				logger.debug("Cmid=" + tmp.get("cmid"));
				logger.debug("Status=" + tmp.get("status"));

			}
		} catch (Exception e) {
			logger.debug("e.msg:" + e.getMessage());
		}

	}

}
