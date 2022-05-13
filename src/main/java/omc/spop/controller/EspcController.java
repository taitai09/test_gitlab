package omc.spop.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import omc.spop.model.EspcURIConstants;
import omc.spop.model.server.Iqms;
import omc.spop.service.EspcService;

/**
 * Handles requests for the Employee service.
 */
@Controller
public class EspcController {

	private static final Logger logger = LoggerFactory.getLogger(EspcController.class);

	@Autowired
	private EspcService expcService;

	@RequestMapping(value = "/espcTest")
	public @ResponseBody ModelAndView espcTest(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("execSqlPerfCheck/espcLinkTest");
	}
	
	@RequestMapping(value = EspcURIConstants.Test, method = RequestMethod.POST)
	public @ResponseBody Iqms getDummyTestPost(HttpServletRequest request, HttpServletResponse response) {
		logger.info("getDummyTestPost=======POST=============");
		logger.info("Start getDummyTest");
		Iqms iqms = new Iqms();
		iqms.setCompcd("rF+Kpw650Lg12ccmqaliMFhGdSW54iH9");
		iqms.setCmid("1");
		iqms.setCmpknm("org");
		iqms.setCmdepday("20181010");
		iqms.setCmusrnum("2048");
		iqms.setJobcd("1111");
		iqms.setCmcreateday("20181010101010");
		// HttpStatus.BAD_REQUEST

		logger.info("httpstatusresult=" + HttpServletResponse.SC_OK);
		return iqms;
	}

	// @RequestMapping(value = IqmsURIConstants.Test, method = //
	// RequestMethod.GET)
	@RequestMapping(value = EspcURIConstants.Test, method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Iqms getDummyTest(HttpServletRequest request, HttpServletResponse response) {
		logger.info("getDummyTest=======GET=============");
		logger.info("Start getDummyTest");
		Iqms iqms = new Iqms();
		iqms.setCompcd("rF+Kpw650Lg12ccmqaliMFhGdSW54iH9");
		iqms.setCmid("1");
		iqms.setCmpknm("org");
		iqms.setCmdepday("20181010");
		iqms.setCmusrnum("2048");
		iqms.setJobcd("1111");
		iqms.setCmcreateday("20181010101010");

		logger.info("httpstatusresult=" + HttpServletResponse.SC_OK);
		return iqms;
	}
	
	@RequestMapping(value = EspcURIConstants.Confirm, method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String cmConfirm(@RequestBody Iqms iqms, HttpServletRequest request, HttpServletResponse response) {
		logger.info("cmConfirm====================");
		logger.info("Start Confirm.");
		logger.info("Compcd=" + iqms.getCompcd());
		logger.info("Cmid=" + iqms.getCmid());
		logger.info("Cmpknm=" + iqms.getCmpknm());
		logger.info("Cmdepday=" + iqms.getCmdepday());
		logger.info("Cmusrnum=" + iqms.getCmusrnum());
		logger.info("Jobcd=" + iqms.getJobcd());
		logger.info("Cmcreateday=" + iqms.getCmcreateday());
		System.out.println("============================================");
		System.out.println(iqms.getClass().getName());
		
		Map<String,Object> responseMap = expcService.setResponseData(iqms, "1");
		
		response.setStatus((Integer) responseMap.get("responseStatus"));
		
		return (String) responseMap.get("responseContent");
	}

	@RequestMapping(value = EspcURIConstants.Complete, method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String cmComplete(@RequestBody Iqms iqms, HttpServletRequest request, HttpServletResponse response) {
		logger.info("cmComplete====================");
		logger.info("Start Complete.");
		logger.info("Compcd=" + iqms.getCompcd());
		logger.info("Cmid=" + iqms.getCmid());
		logger.info("status=" + iqms.getStatus());
		
		Map<String,Object> responseMap = expcService.setResponseData(iqms, "3");
		
		response.setStatus( (Integer)responseMap.get("responseStatus") );
		
		return (String)responseMap.get("responseContent");
	}
	/**
	 * CM 취소
	 * @param iqms
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = EspcURIConstants.Cancel, method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String cmCancel(@RequestBody Iqms iqms, HttpServletRequest request, HttpServletResponse response) {
		logger.info("CM 취소====================");
		logger.info("Start Cancel.");
		logger.info("Compcd=" + iqms.getCompcd());
		logger.info("Cmid=" + iqms.getCmid());
		logger.info("status=" + iqms.getStatus());
		
		Map<String,Object> responseMap = expcService.setResponseData(iqms, "4");
				
		response.setStatus( (Integer)responseMap.get("responseStatus") );
		
		return (String)responseMap.get("responseContent");
	}

	@RequestMapping(value = EspcURIConstants.Delete, method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String cmDelete(@RequestBody Iqms iqms, HttpServletRequest request, HttpServletResponse response) {
		logger.info("cmDelete====================");
		logger.info("Start Delete.");
		logger.info("Compcd=" + iqms.getCompcd());
		logger.info("Cmid=" + iqms.getCmid());
		logger.info("status=" + iqms.getStatus());

		Map<String,Object> responseMap = expcService.setResponseData(iqms, "5");
		
		response.setStatus( (Integer)responseMap.get("responseStatus") );
		
		return (String)responseMap.get("responseContent");
	}
	
	@RequestMapping(value = "/espc/ConfirmTestLink", method = RequestMethod.POST)
	@ResponseBody 
	public String testLink(@RequestBody String jData, HttpServletRequest request, HttpServletResponse response) {
		
		Map<String,Object> responseMap = new LinkedHashMap<String, Object>();
		
		try {
			Iqms iqms = new Iqms();
			
			JSONParser jParser = new JSONParser();
			JSONObject jObject = (JSONObject)jParser.parse(jData);
			
			iqms.setCompcd( (String)jObject.get("compcd") );
			iqms.setCmid( (String)jObject.get("cmid") );
			iqms.setCmpknm( (String)jObject.get("cmpknm") );
			iqms.setCmdepday( (String)jObject.get("cmdepday") );
			iqms.setCmusrnum( (String)jObject.get("cmusrnum") );
			iqms.setJobcd( (String)jObject.get("jobcd") );
			iqms.setCmcreateday( (String)jObject.get("cmcreateday") );
			
			logger.info("testLink====================");
			logger.info("Start Test.");
			logger.info("Compcd=" + iqms.getCompcd());
			logger.info("Cmid=" + iqms.getCmid());
			logger.info("Cmpknm=" + iqms.getCmpknm());
			logger.info("Cmdepday=" + iqms.getCmdepday());
			logger.info("Cmusrnum=" + iqms.getCmusrnum());
			logger.info("Jobcd=" + iqms.getJobcd());
			logger.info("Cmcreateday=" + iqms.getCmcreateday());
			
			responseMap = expcService.setResponseData(iqms, "1");
			response.setStatus((Integer) responseMap.get("responseStatus"));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return (String) responseMap.get("responseContent");
	}

}
