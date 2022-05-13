package omc.spop.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import omc.spop.base.InterfaceController;
import omc.spop.model.OdsAsaRecommendations;
import omc.spop.model.PartitionRecommendation;
import omc.spop.model.ReorgRecommendation;
import omc.spop.model.Result;
import omc.spop.service.SpaceDiagnosticsService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2018.03.08	이원식	OPENPOP V2 최초작업
 * 2018.04.27	이원식	오브젝트 진단 => SPACE 진단으로 변경
 * 2018.05.04	이원식	파티셔닝 대상 진단 추가
 **********************************************************/

@Controller
public class SpaceDiagnosticsController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(SpaceDiagnosticsController.class);
	
	@Autowired
	private SpaceDiagnosticsService spaceDiagnosticsService;

	
	/* Reorg 대상 테이블  */
	@RequestMapping(value="/ReorgTargetTable", method=RequestMethod.GET)
	public String ReorgTargetTable(@ModelAttribute("odsAsaRecommendations") OdsAsaRecommendations odsAsaRecommendations, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", odsAsaRecommendations.getMenu_id() );
		model.addAttribute("menu_nm", odsAsaRecommendations.getMenu_nm() );
		model.addAttribute("dbid", odsAsaRecommendations.getDbid());
		model.addAttribute("call_from_parent", odsAsaRecommendations.getCall_from_parent());
		
		return "spaceDiagnostics/reorgTargetTable";
	}	
	
	/* Oracle Reorg 대상 테이블 action */
	@RequestMapping(value="/ReorgTargetTable/Oracle", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String OracleReorgTargetTableAction(@ModelAttribute("odsAsaRecommendations") OdsAsaRecommendations odsAsaRecommendations,  Model model) {
		List<OdsAsaRecommendations> resultList = new ArrayList<OdsAsaRecommendations>();

		try{
			resultList = spaceDiagnosticsService.oracleReorgTargetTableList(odsAsaRecommendations);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* OpenPOP Reorg 대상 테이블 action */
	@RequestMapping(value="/ReorgTargetTable/OpenPOP", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String OpenPOPReorgTargetTableAction(@ModelAttribute("reorgRecommendation") ReorgRecommendation reorgRecommendation,  Model model) {
		List<ReorgRecommendation> resultList = new ArrayList<ReorgRecommendation>();

		try{
			resultList = spaceDiagnosticsService.openPOPReorgTargetTableList(reorgRecommendation);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* Oracle Reorg 대상 테이블 Recommendation action */
	@RequestMapping(value="/ReorgTargetTable/OracleRecommendation", method=RequestMethod.POST)
	@ResponseBody
	public Result OracleRecommendationAction(@ModelAttribute("odsAsaRecommendations") OdsAsaRecommendations odsAsaRecommendations,  Model model) {
		Result result = new Result();
		OdsAsaRecommendations resultOdsAsaRecommendations = new OdsAsaRecommendations();

		try{
			resultOdsAsaRecommendations = spaceDiagnosticsService.getOracleRecommendation(odsAsaRecommendations);
			result.setResult(true);
			result.setObject(resultOdsAsaRecommendations);			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* OpenPOP Reorg 대상 테이블 Recommendation action */
	@RequestMapping(value="/ReorgTargetTable/OpenPOPRecommendation", method=RequestMethod.POST)
	@ResponseBody
	public Result OpenPOPRecommendationAction(@ModelAttribute("reorgRecommendation") ReorgRecommendation reorgRecommendation,  Model model) {
		Result result = new Result();
		ReorgRecommendation resultReorgRecommendation = new ReorgRecommendation();

		try{
			resultReorgRecommendation = spaceDiagnosticsService.getOpenPOPRecommendation(reorgRecommendation);
			result.setResult(true);
			result.setObject(resultReorgRecommendation);			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 파티셔닝 대상 테이블  */
	@RequestMapping(value="/PartitionTargetTable", method=RequestMethod.GET)
	public String PartitionTargetTable(@ModelAttribute("partitionRecommendation") PartitionRecommendation partitionRecommendation, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", partitionRecommendation.getMenu_id() );
		model.addAttribute("menu_nm", partitionRecommendation.getMenu_nm() );
		
		return "spaceDiagnostics/partitionTargetTable";
	}
	
	/* 파티셔닝 대상 테이블  - partition recommendation list action */
	@RequestMapping(value="/PartitionTargetTable/PartitionRecommendation", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String PartitionRecommendationAction(@ModelAttribute("partitionRecommendation") PartitionRecommendation partitionRecommendation, Model model) {
		List<PartitionRecommendation> resultList = new ArrayList<PartitionRecommendation>();

		try{
			resultList = spaceDiagnosticsService.partitionRecommendationList(partitionRecommendation);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 파티셔닝 대상 테이블  - access path list action */
	@RequestMapping(value="/PartitionTargetTable/AccessPath", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String AccessPathAction(@ModelAttribute("partitionRecommendation") PartitionRecommendation partitionRecommendation, Model model) {
		List<PartitionRecommendation> resultList = new ArrayList<PartitionRecommendation>();

		try{
			resultList = spaceDiagnosticsService.accessPathList(partitionRecommendation);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 파티셔닝 대상 테이블 - partition Key Recommendation list action */
	@RequestMapping(value="/PartitionTargetTable/PartitionKeyRecommendation", method=RequestMethod.POST)
	@ResponseBody
	public Result PartitionKeyRecommendationAction(@ModelAttribute("partitionRecommendation") PartitionRecommendation partitionRecommendation,  Model model) {
		Result result = new Result();
		List<PartitionRecommendation> resultList = new ArrayList<PartitionRecommendation>();
		PartitionRecommendation resultObject = new PartitionRecommendation();
		
		try{
			resultObject = spaceDiagnosticsService.getPartitionKeyRecommendation(partitionRecommendation);
			
			resultList = spaceDiagnosticsService.partitionKeyRecommendationList(partitionRecommendation);
			
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultObject);
			result.setTxtValue(success(resultList).toJSONObject().toString());
			result.setMessage("Partition Recommendation 데이터가 없습니다.");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
}