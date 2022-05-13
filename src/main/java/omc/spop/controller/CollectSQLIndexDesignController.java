package omc.spop.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import omc.spop.base.InterfaceController;
import omc.spop.model.AccPathExec;
import omc.spop.model.OdsIndexs;
import omc.spop.model.OdsTables;
import omc.spop.service.IndexDesignMaintenanceService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2019.09.16 initiate
 **********************************************************/

@Controller
public class CollectSQLIndexDesignController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(CollectSQLIndexDesignController.class);

	@Autowired
	private IndexDesignMaintenanceService indexDesignMaintenanceService;

	/* 인덱스 설계 > 수집SQL 인덱스 설계 */
	@RequestMapping(value = "/indexDesign/collectSQL/CollectSQLIndexDesign", method = RequestMethod.GET)
	public String CollectSQLIndexDesign(@RequestParam(required = false) Map<String, String> param, Model model) {
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		return "indexDesign/collectSQL/collectSQLIndexDesign";
	}

	/* 수집 SQL 조건절 파싱 */
	@RequestMapping(value = "/indexDesign/collectSQL/ParsingCollectionTerms", method = RequestMethod.GET)
	public String ParsingCollectionTerms(@ModelAttribute("accPathExec") AccPathExec accPathExec, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		
		String nowDate2 = DateUtil.getNowDate("yyyy/MM/dd");
		String startDate2 = DateUtil.getNextDay("M", "yyyy/MM/dd", nowDate, "7");
		
//		String startTime = DateUtil.getNextTime("M", "30");
		String nowTime = DateUtil.getNextTime("M", "30");
//		String nowTime = DateUtil.getNowFulltime();
		
		model.addAttribute("startDate", startDate);
//		model.addAttribute("startDateTime", startDate + " " + startTime);
		model.addAttribute("startTime", "00:00:00");
		
		model.addAttribute("nowDate", nowDate);
//		model.addAttribute("nowDateTime", nowDate+ " "+ nowTime);
		model.addAttribute("nowTime", nowTime);
		
		model.addAttribute("menu_id", "144");
		model.addAttribute("menu_nm", accPathExec.getMenu_nm());

		return "indexDesign/collectSQL/parsingCollectionTerms";
	}

	/* 수집SQL인덱스 자동설계 */
	@RequestMapping(value = "/indexDesign/collectSQL/AutoCollectionIndexDesign", method = RequestMethod.GET)
	public String AutoCollectionIndexDesign(@ModelAttribute("accPathExec") AccPathExec accPathExec, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", "149");
		model.addAttribute("menu_nm", accPathExec.getMenu_nm());

		return "indexDesign/collectSQL/autoCollectionIndexDesign";
	}

	/* 수집 SQL 인덱스 설계 */
	@RequestMapping(value = "/indexDesign/collectSQL/CollectionIndexDesign", method = { RequestMethod.GET, RequestMethod.POST })
	public String CollectionIndexDesign(@ModelAttribute("odsTables") OdsTables odsTables, Model model) {
		String nowDate = DateUtil.getNowDate("yyyyMMdd");
		String accessPathValue = "";
		try {
			if ((odsTables.getDbid() != null && !odsTables.getDbid().equals(""))
					&& (odsTables.getExec_seq() != null && !odsTables.getExec_seq().equals(""))) {
				accessPathValue = indexDesignMaintenanceService.getAccessPathExec(odsTables);
				odsTables.setAccess_path_value(accessPathValue);
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", "151");
		model.addAttribute("menu_nm", odsTables.getMenu_nm());

		return "indexDesign/collectSQL/collectionIndexDesign";
	}

	/* 수집 SQL 인덱스 정비 */
	@RequestMapping(value = "/indexDesign/collectSQL/CollectionIndexUsage", method = RequestMethod.GET)
	public String CollectionIndexUsage(@ModelAttribute("odsIndexs") OdsIndexs odsIndexs, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");

		model.addAttribute("startDate", startDate);
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", "152");
		model.addAttribute("menu_nm", odsIndexs.getMenu_nm());

		return "indexDesign/collectSQL/collectionIndexUsage";
	}

}