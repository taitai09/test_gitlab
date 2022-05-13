package omc.spop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import omc.spop.base.InterfaceController;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2017.08.10 이원식 최초작성
 **********************************************************/

@Controller
public class TestController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(TestController.class);

	@RequestMapping(value = "/note", method = RequestMethod.GET)
	public String note(Model model) {
		return "websocket/note";
	}

	@RequestMapping(value = "/chart", method = RequestMethod.GET)
	public String chart(Model model) {
		String nowDate = DateUtil.getNextDay("M", "yyyy-MM-dd", DateUtil.getNowDate("yyyy-MM-dd"), "1");
		model.addAttribute("nowDate", nowDate);

		return "chart";
	}

}