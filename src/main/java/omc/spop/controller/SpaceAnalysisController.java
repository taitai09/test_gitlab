package omc.spop.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.InterfaceController;
import omc.spop.model.OdsDataFiles;
import omc.spop.model.OdsSegments;
import omc.spop.model.OdsTabModifications;
import omc.spop.model.OdsTables;
import omc.spop.model.OdsTablespaces;
import omc.spop.model.Result;
import omc.spop.service.SpaceAnalysisService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2018.03.14	이원식	OPENPOP V2 최초작업
 * 2018.04.27	이원식	오브젝트 분석 => SPACE 분석으로 변경
 **********************************************************/

@Controller
public class SpaceAnalysisController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(SpaceAnalysisController.class);
	
	@Autowired
	private SpaceAnalysisService spaceAnalysisService;

	/* Tablespace 분석  */
	@RequestMapping(value="/TablespaceAnalysis", method=RequestMethod.GET)
	public String TablespaceAnalysis(@ModelAttribute("odsTablespaces") OdsTablespaces odsTablespaces, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", odsTablespaces.getMenu_id() );
		model.addAttribute("menu_nm", odsTablespaces.getMenu_nm() );
		
		return "spaceAnalysis/tablespaceAnalysis";
	}
	
	/* Tablespace 분석 - list action */
	@RequestMapping(value="/TablespaceAnalysis", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String TablespaceAnalysisAction(@ModelAttribute("odsTablespaces") OdsTablespaces odsTablespaces,  Model model) {
		List<OdsTablespaces> resultList = new ArrayList<OdsTablespaces>();

		try{
			resultList = spaceAnalysisService.tablespaceAnalysisList(odsTablespaces);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* Tablespace 분석 - DB Size Growth action */
	@RequestMapping(value="/TablespaceAnalysis/DBSizeChart", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String DBSizeChartAction(@ModelAttribute("odsTablespaces") OdsTablespaces odsTablespaces,  Model model) {
		List<OdsTablespaces> resultList = new ArrayList<OdsTablespaces>();

		try{
			resultList = spaceAnalysisService.dbSizeChartList(odsTablespaces);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* Tablespace 분석 - TOP Tablespace Growth action */
	@RequestMapping(value="/TablespaceAnalysis/TOPTablespaceChart", method=RequestMethod.POST)
	@ResponseBody
	public Result TOPTablespaceChartAction(@ModelAttribute("odsTablespaces") OdsTablespaces odsTablespaces,  Model model) {
		Result result = new Result();

		try{
			result = spaceAnalysisService.topTablespaceChartList(odsTablespaces);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}	
	
	/* Tablespace 분석 - 상세 */
	@RequestMapping(value="/TablespaceAnalysis/Detail", method=RequestMethod.GET)
	public String TablespaceAnalysisDetail(@ModelAttribute("odsTablespaces") OdsTablespaces odsTablespaces, Model model) {
		model.addAttribute("menu_id", odsTablespaces.getMenu_id());
		model.addAttribute("menu_nm", odsTablespaces.getMenu_nm());
		return "spaceAnalysis/tablespaceAnalysisDetail";
	}
	
	/* Tablespace 분석 상세 - segment list action */
	@RequestMapping(value="/TablespaceAnalysis/Detail/Segment", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String SegmentAction(@ModelAttribute("odsSegments") OdsSegments odsSegments,  Model model) {
		List<OdsSegments> resultList = new ArrayList<OdsSegments>();

		try{
			resultList = spaceAnalysisService.segmentList(odsSegments);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* Tablespace 분석 상세 - segment statistics chart action */
	@RequestMapping(value="/TablespaceAnalysis/Detail/SegmentStatistics", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String SegmentStatisticsAction(@ModelAttribute("odsSegments") OdsSegments odsSegments,  Model model) {
		List<OdsSegments> resultList = new ArrayList<OdsSegments>();

		try{
			resultList = spaceAnalysisService.segmentStatisticsList(odsSegments);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* Tablespace 분석 상세 - segment size history chart action */
	@RequestMapping(value="/TablespaceAnalysis/Detail/SegmentSizeHistory", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String SegmentSizeHistoryAction(@ModelAttribute("odsSegments") OdsSegments odsSegments,  Model model) {
		List<OdsSegments> resultList = new ArrayList<OdsSegments>();

		try{
			resultList = spaceAnalysisService.segmentSizeHistoryList(odsSegments);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* Tablespace 분석 상세 - datafile list action */
	@RequestMapping(value="/TablespaceAnalysis/Detail/DataFile", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String DatafileAction(@ModelAttribute("odsDataFiles") OdsDataFiles odsDataFiles,  Model model) {
		List<OdsDataFiles> resultList = new ArrayList<OdsDataFiles>();

		try{
			resultList = spaceAnalysisService.dataFileList(odsDataFiles);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* Tablespace 분석 상세 - datafile Statistics chart list action */
	@RequestMapping(value="/TablespaceAnalysis/Detail/DatafileStatistics", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String DatafileStatisticsAction(@ModelAttribute("odsDataFiles") OdsDataFiles odsDataFiles,  Model model) {
		List<OdsDataFiles> resultList = new ArrayList<OdsDataFiles>();

		try{
			resultList = spaceAnalysisService.datafileStatisticsList(odsDataFiles);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* Reorg 대상 분석  */
	@RequestMapping(value="/ReorgTargetAnalysis", method=RequestMethod.GET)
	public String ReorgTargetAnalysis(@ModelAttribute("odsTables") OdsTables odsTables, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", odsTables.getMenu_id() );
		model.addAttribute("menu_nm", odsTables.getMenu_nm() );
		
		return "spaceAnalysis/reorgTargetAnalysis";
	}
	
	/* Reorg 대상 분석 - list action */
	@RequestMapping(value="/ReorgTargetAnalysis", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ReorgTargetAnalysisAction(@ModelAttribute("odsTables") OdsTables odsTables,  Model model) {
		List<OdsTables> resultList = new ArrayList<OdsTables>();

		try{
			resultList = spaceAnalysisService.reorgTargetAnalysisList(odsTables);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	

	/* Reorg 대상 분석 - top dml table chart action */
	@RequestMapping(value="/ReorgTargetAnalysis/TOPDMLTableChart", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String TOPDMLTableChartAction(@ModelAttribute("odsTables") OdsTables odsTables,  Model model) {
		List<OdsTables> resultList = new ArrayList<OdsTables>();

		try{
			resultList = spaceAnalysisService.topDMLTableChartList(odsTables);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}

	/* Reorg 대상 분석 - top dml table history chart action */
	@RequestMapping(value="/ReorgTargetAnalysis/TOPDMLTableHistoryChart")
	@ResponseBody
	public Result TOPDMLTableHistoryChartAction(@ModelAttribute("odsTables") OdsTables odsTables,  Model model) {
		Result result = new Result();

		try{
			result = spaceAnalysisService.topDMLTableHistoryChartList(odsTables);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* Reorg 대상 분석 - 상세 */
	@RequestMapping(value="/ReorgTargetAnalysis/Detail", method=RequestMethod.GET)
	public String ReorgTargetAnalysisDetail(@ModelAttribute("odsTables") OdsTables odsTables, Model model) {
		model.addAttribute("menu_id", odsTables.getMenu_id());
		model.addAttribute("menu_nm", odsTables.getMenu_nm());
		return "spaceAnalysis/reorgAnalysisDetail";
	}
	
	/* Reorg 대상 분석 - 상세 - dml history chart action */
	@RequestMapping(value="/ReorgTargetAnalysis/Detail/DMLHistoryChart", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String DMLHistoryChartAction(@ModelAttribute("odsTables") OdsTables odsTables,  Model model) {
		List<OdsTables> resultList = new ArrayList<OdsTables>();

		try{
			resultList = spaceAnalysisService.dmlHistoryChartList(odsTables);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 파티셔닝 대상 분석  */
	@RequestMapping(value="/PartitionTargetAnalysis", method=RequestMethod.GET)
	public String PartitionTargetAnalysis(@ModelAttribute("odsTables") OdsTables odsTables, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", odsTables.getMenu_id() );
		model.addAttribute("menu_nm", odsTables.getMenu_nm() );
		
		return "spaceAnalysis/partitionTargetAnalysis";
	}
	
	/* 파티셔닝 대상 분석 - list action */
	@RequestMapping(value="/PartitionTargetAnalysis", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String PartitionTargetAnalysisAction(@ModelAttribute("odsTables") OdsTables odsTables,  Model model) {
		List<OdsTables> resultList = new ArrayList<OdsTables>();

		try{
			resultList = spaceAnalysisService.partitionTargetAnalysisList(odsTables);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 파티셔닝 대상 분석 - top size table chart action */
	@RequestMapping(value="/PartitionTargetAnalysis/TOPSizeTableChart", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String TOPSizeTableChartAction(@ModelAttribute("odsSegments") OdsSegments odsSegments,  Model model) {
		List<OdsSegments> resultList = new ArrayList<OdsSegments>();

		try{
			resultList = spaceAnalysisService.topSizeTableChartList(odsSegments);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}

	/* 파티셔닝 대상 분석 - top size table history chart action */
	@RequestMapping(value="/PartitionTargetAnalysis/TOPSizeTableHistoryChart")
	@ResponseBody
	public Result TOPSizeTableHistoryChartAction(@ModelAttribute("odsSegments") OdsSegments odsSegments,  Model model) {
		Result result = new Result();

		try{
			result = spaceAnalysisService.topSizeTableHistoryChartList(odsSegments);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}		
	
	/* 파티셔닝 대상 분석  - 상세 */
	@RequestMapping(value="/PartitionTargetAnalysis/Detail", method=RequestMethod.GET)
	public String PartitionTargetAnalysisDetail(@ModelAttribute("odsTables") OdsTables odsTables, Model model) {
		model.addAttribute("menu_id", odsTables.getMenu_id());
		model.addAttribute("menu_nm", odsTables.getMenu_nm());
		return "spaceAnalysis/partitionTargetAnalysisDetail";
	}
	
	/* 파티셔닝 대상 분석 - 상세 - table size history action */
	@RequestMapping(value="/PartitionTargetAnalysis/Detail/TableSizeHistoryChart", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String TableSizeHistoryChartAction(@ModelAttribute("odsTables") OdsTables odsTables,  Model model) {
		List<OdsTables> resultList = new ArrayList<OdsTables>();

		try{
			resultList = spaceAnalysisService.tableSizeHistoryChartList(odsTables);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 파티셔닝 대상 분석 - 상세 - access path list action */
	@RequestMapping(value="/PartitionTargetAnalysis/Detail/AccessPath", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String AccessPathAction(@ModelAttribute("odsTables") OdsTables odsTables,  Model model) {
		List<OdsTables> resultList = new ArrayList<OdsTables>();

		try{
			resultList = spaceAnalysisService.accessPathList(odsTables);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 파티셔닝 대상 분석 - 상세 - partition Recommendation list action */
	@RequestMapping(value="/PartitionTargetAnalysis/Detail/PartitionRecommendation", method=RequestMethod.POST)
	@ResponseBody
	public Result PartitionRecommendationAction(@ModelAttribute("odsTables") OdsTables odsTables,  Model model) {
		Result result = new Result();
		List<OdsTables> resultList = new ArrayList<OdsTables>();
		OdsTables resultObject = new OdsTables();
		
		try{
			resultObject = spaceAnalysisService.getPartitionRecommendation(odsTables);
			
			resultList = spaceAnalysisService.partitionRecommendationList(odsTables);
			
			if(resultList.size() > 0){
				result.setResult(true);
			}else{
				result.setResult(false);
				result.setMessage("");
			}
			result.setObject(resultObject);
			result.setTxtValue(success(resultList).toJSONObject().toString());
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 데이터파일 I/O 분석  */
	@RequestMapping(value="/DataIOAnalysis", method=RequestMethod.GET)
	public String DataIOAnalysis(@ModelAttribute("odsDataFiles") OdsDataFiles odsDataFiles, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String nowTime = DateUtil.getNowFulltime();
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		String startTime = DateUtil.getNextTime("M","10");
		
		model.addAttribute("startDate", startDate );
		model.addAttribute("startTime", startTime );
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("nowTime", nowTime );
		model.addAttribute("menu_id", odsDataFiles.getMenu_id() );
		model.addAttribute("menu_nm", odsDataFiles.getMenu_nm() );
		
		return "spaceAnalysis/dataIOAnalysis";
	}
	
	/* 데이터파일 I/O 분석 - list action */
	@RequestMapping(value="/DataIOAnalysis", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String DataIOAnalysisAction(@ModelAttribute("odsDataFiles") OdsDataFiles odsDataFiles,  Model model) {
		List<OdsDataFiles> resultList = new ArrayList<OdsDataFiles>();

		try{
			resultList = spaceAnalysisService.dataIOAnalysisList(odsDataFiles);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 데이터파일 I/O 분석 - TOP I/O DataFIle chart action */
	@RequestMapping(value="/DataIOAnalysis/DataFileIOChart", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String DataFileIOChartAction(@ModelAttribute("odsDataFiles") OdsDataFiles odsDataFiles,  Model model) {
		List<OdsDataFiles> resultList = new ArrayList<OdsDataFiles>();

		try{
			resultList = spaceAnalysisService.datafileIOChartList(odsDataFiles);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 데이터파일 I/O 분석  - 상세 */
	@RequestMapping(value="/DataIOAnalysis/Detail", method=RequestMethod.GET)
	public String DataIOAnalysisDetail(@ModelAttribute("odsDataFiles") OdsDataFiles odsDataFiles, Model model) {
		model.addAttribute("menu_id", odsDataFiles.getMenu_id());
		model.addAttribute("menu_nm", odsDataFiles.getMenu_nm());
		return "spaceAnalysis/dataIOAnalysisDetail";
	}
	
	/* 데이터파일 I/O 분석  - 상세 - DataFIle I/O History chart action */
	@RequestMapping(value="/DataIOAnalysis/Detail/DataFileIOHistoryChart", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String DataFileIOHistoryChartAction(@ModelAttribute("odsDataFiles") OdsDataFiles odsDataFiles,  Model model) {
		List<OdsDataFiles> resultList = new ArrayList<OdsDataFiles>();

		try{
			resultList = spaceAnalysisService.datafileIOHistoryChartList(odsDataFiles);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* Table별 DML 변경 내역  */
	@RequestMapping(value="/DMLChangeTable", method=RequestMethod.GET)
	public String DMLChangeTable(@ModelAttribute("odsTables") OdsTables odsTables, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", odsTables.getMenu_id() );
		model.addAttribute("menu_nm", odsTables.getMenu_nm() );
		
		return "spaceAnalysis/dmlChangeTable";
	}
	
	/* Table별 DML 변경 현황 - 테이블통계정보 action */
	@RequestMapping(value="/DMLChangeTable", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String TableUseSQLAction(@ModelAttribute("odsTables") OdsTables odsTables,  Model model) {
		List<OdsTables> resultList = new ArrayList<OdsTables>();

		try{
			resultList = spaceAnalysisService.dmlChangeTableList(odsTables);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* Table별 DML 변경 현황 - DML발생현황 action */
	@RequestMapping(value="/DMLChangeTable/DMLOccurrence", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String DMLOccurrenceAction(@ModelAttribute("odsTabModifications") OdsTabModifications odsTabModifications,  Model model) {
		List<OdsTabModifications> resultList = new ArrayList<OdsTabModifications>();

		try{
			resultList = spaceAnalysisService.dmlOccurrenceList(odsTabModifications);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 일자별 DML 변경 현황  */
	@RequestMapping(value="/DMLChangeDaily", method=RequestMethod.GET)
	public String DMLChangeDaily(@ModelAttribute("odsTabModifications") OdsTabModifications odsTabModifications, Model model) {
		//String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.KOREA);
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		sdf.format(ts.getTime()-(24*24*60*1000)); //현재 날짜에서 하루를 뺀다.
		
		model.addAttribute("nowDate", sdf.format(ts.getTime()-(60*60*24*1000)));
		model.addAttribute("menu_id", odsTabModifications.getMenu_id() );
		model.addAttribute("menu_nm", odsTabModifications.getMenu_nm() );
		
		return "spaceAnalysis/dmlChangeDaily";
	}
	
	/* 일자별 DML 변경 현황 - DML발생현황 action */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/DMLChangeDaily", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String DMLChangeDailyAction(@ModelAttribute("odsTabModifications") OdsTabModifications odsTabModifications,  Model model) {
		List<OdsTabModifications> resultList = new ArrayList<OdsTabModifications>();
		int dataCount4NextBtn = 0;

		try{
			resultList = spaceAnalysisService.dmlChangeDailyList(odsTabModifications);
			
			if(resultList != null && resultList.size() > odsTabModifications.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(odsTabModifications.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

//		return success(resultList).toJSONObject().toString();	
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();	
	}
	
	
	/* 성능리포트 - 프로그램유형별 성능개선현황 엑셀 다운 */
	@RequestMapping(value = "/DMLChangeDaily/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getByProgramTypeReportByExcelDown(HttpServletRequest req, HttpServletResponse res, 
			@ModelAttribute("perfList") OdsTabModifications odsTabModifications, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = spaceAnalysisService.dmlChangeDailyListByExcelDown(odsTabModifications);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "일별_DML_변경_분석");
		model.addAttribute("sheetName", "일별_DML_변경_분석");
		model.addAttribute("excelId", "DML_CHANGED_AILYLIST");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	
}