package omc.spop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import omc.spop.base.InterfaceController;
import omc.spop.model.Result;
import omc.spop.service.TransactionTestService;

/***********************************************************
 * 2017.09.20 이원식 최초작성
 **********************************************************/

@Controller
public class TransactionTestController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(TransactionTestController.class);

	@Autowired
	private TransactionTestService transactionTestService;
	
	/* Transaction Test */
	@RequestMapping(value = "/auth/TransactionTest", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/json; charset=utf8")
	@ResponseBody
	public Result TransactionTest() {
		Result result = new Result();
		try {
			result = transactionTestService.transactionTest();
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage("error");
		}
		return  result;
	}
}