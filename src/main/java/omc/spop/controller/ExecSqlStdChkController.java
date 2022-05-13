package omc.spop.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.JobSchedulerBase;
import omc.spop.model.JobSchedulerConfigDetail;
import omc.spop.model.Result;
import omc.spop.model.SQLStandards;
import omc.spop.service.ExecSqlStdChkService;
import omc.spop.service.SQLStandardsService;

@RequestMapping(value="/execSqlStdChk")
@Controller
public class ExecSqlStdChkController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(ExecSqlStdChkController.class);
	
	@Autowired
	private ExecSqlStdChkService execSqlStdChkService;
	
	@Autowired
	private SQLStandardsService sqlStandarsService;
		
	@Value("#{defaultConfig['maxUploadSize']}")
	private int maxUploadSize;
	
	@Value("#{defaultConfig['maxUploadMegaBytes']}")
	private int maxUploadMegaBytes;
		
	/* 스케줄러 관리 - sql 표준 점검 스케줄러 검색 (new) */
	@RequestMapping(value = "/loadSchedulerList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadSchedulerList(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase) {
		List<JobSchedulerBase> resultList = Collections.emptyList();
		String returnStr = "";
		
		jobSchedulerBase.setJob_scheduler_type_cd("37");
		
		try {
			resultList = execSqlStdChkService.loadSchedulerByManager(jobSchedulerBase);
			returnStr = success(resultList).toJSONObject().get("rows").toString();
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
			
		} finally {
			resultList = null;
		}
		
		return returnStr;
	}
	
	/* 스케줄러 엑셀 다운로드 */
	@RequestMapping(value = "/loadSchedulerList/excelDownload", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView excelDownload(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase,
			Model model) {
		
		List<LinkedHashMap<String, Object>> resultList = Collections.emptyList();
		jobSchedulerBase.setJob_scheduler_type_cd("37");
		
		try {
			resultList = execSqlStdChkService.excelDownload(jobSchedulerBase);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
		}
		model.addAttribute("fileName", "스케줄러_관리");
		model.addAttribute("sheetName", "스케줄러_관리");
		model.addAttribute("excelId", "EXEC_SQL_STD_CHK_SCHEDULER");
		
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* 스케줄러 관리 신규스케줄러명 중복 확인 */
	@RequestMapping(value = "/checkExistScheduler", method = RequestMethod.GET)
	@ResponseBody
	public Result checkExistScheduler(@ModelAttribute("jobSchedulerConfigDetail") JobSchedulerConfigDetail jobSchedulerConfigDetail) {
		Result result = new Result();
		
		try {
			int count = execSqlStdChkService.checkExistScheduler(jobSchedulerConfigDetail);
			
			result.setResultCount(count);
			result.setResult(true);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 스케줄러 관리 신규등록 */
	@RequestMapping(value = "/insertSqlStdQtyScheduler", method = RequestMethod.POST)
	@ResponseBody
	public Result insertSqlStdQtyScheduler(@ModelAttribute("jobSchedulerConfigDetail") JobSchedulerConfigDetail jobSchedulerConfigDetail) {
		Result result = new Result();
		boolean b_isUpdate = false;
		int count = 0;
		
		jobSchedulerConfigDetail.setUpd_id(SessionManager.getLoginSession().getUsers().getUser_id());
		
		try {
			b_isUpdate = sqlStandarsService.isUpdateY(jobSchedulerConfigDetail);
			
			if( b_isUpdate ) {
				jobSchedulerConfigDetail.setUse_yn("Y");
				
			}else {
				jobSchedulerConfigDetail.setUse_yn("N");
			}
			
			count = execSqlStdChkService.insertSqlStdQtyScheduler(jobSchedulerConfigDetail);
			
			if (count == 2) {
				result.setResult(true);
				result.setMessage("저장 되었습니다.");
			} else {
				result.setResult(false);
				result.setMessage("저장에 실패하였습니다.");
			}
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 스케줄러 관리 수정 */
	@RequestMapping(value = "/updateSqlStdQtyScheduler", method = RequestMethod.POST)
	@ResponseBody
	public Result updateSqlStdQtyScheduler(@ModelAttribute("jobSchedulerConfigDetail") JobSchedulerConfigDetail jobSchedulerConfigDetail) {
		Result result = new Result();
		boolean b_isUpdate = false;
		int count = 0;
		
		jobSchedulerConfigDetail.setUpd_id(SessionManager.getLoginSession().getUsers().getUser_id());
		
		try {
			b_isUpdate = sqlStandarsService.isUpdateY(jobSchedulerConfigDetail);
			
			if( b_isUpdate ) {
				jobSchedulerConfigDetail.setUse_yn("Y");
			}else {
				jobSchedulerConfigDetail.setUse_yn("N");
			}
			
			count = execSqlStdChkService.updateSqlStdQtyScheduler(jobSchedulerConfigDetail);
			
			if (count == 2) {
				result.setResult(true);
				result.setMessage("저장 되었습니다.");
			} else {
				result.setResult(false);
				result.setMessage("저장에 실패하였습니다.");
			}
			
		}catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 스케줄러 관리 삭제 */
	@RequestMapping(value = "/deleteScheduler", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteScheduler(@ModelAttribute("jobSchedulerConfigDetail") JobSchedulerConfigDetail jobSchedulerConfigDetail) {
		Result result = new Result();
		int count = 0;
		
		jobSchedulerConfigDetail.setUpd_id(SessionManager.getLoginSession().getUsers().getUser_id());
		
		try {
			count = execSqlStdChkService.deleteScheduler(jobSchedulerConfigDetail);
			
			if (count == 2) {
				result.setResult(true);
				result.setMessage("삭제 되었습니다.");
			} else {
				result.setResult(false);
				result.setMessage("삭제에 실패하였습니다.");
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* sql 표준 점검 결과(new) */
	@RequestMapping(value = "/loadSqlStdChkRslt", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadSqlStdChkRslt(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) {
		List<JobSchedulerBase> resultList = Collections.emptyList();
		String returnStr = "";
		
		try {
			resultList = execSqlStdChkService.loadSqlStdChkRslt(sqlStandards);
			returnStr = success(resultList).toJSONObject().get("rows").toString();
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
			
		}finally {
			resultList = null;
		}
		
		return returnStr;
	}
	
	/* sql 표준 점검 결과 - 좌측 테이블 품질 집계현황(new) */
	@RequestMapping(value = "/loadQualityStatus", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadQualityStatus(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) {
		List<LinkedHashMap<String, Object>> resultList = Collections.emptyList();
		int dataCount4NextBtn = 0;
		JSONObject jobj = null;
		
		try {
			resultList = execSqlStdChkService.loadQualityStatus(sqlStandards);
			
			if (resultList != null && resultList.size() > sqlStandards.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(sqlStandards.getPagePerCount());	// 리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함
			}
			jobj = getJSONResult(resultList, true).toJSONObject();
			jobj.put("dataCount4NextBtn", dataCount4NextBtn);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
		
		} finally {
			resultList = null;
		}
		
		return jobj.toString();
	}
	
	/* sql 표준 점검 결과 - 하단 테이블 프로젝트별 점검지표(new) */
	@RequestMapping(value = "/loadIndexList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadIndexList(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) {
		List<SQLStandards> resultList = Collections.emptyList();
		JSONObject jobj = null;
		
		try {
			resultList = execSqlStdChkService.loadIndexList(sqlStandards);
			jobj = success(resultList).toJSONObject();
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
			
		} finally {
			resultList = null;
		}
		
		return jobj.toString();
	}
	
	/* sql 표준 점검 결과 - 하단 테이블 점검 결과 데이터(new) */
	@RequestMapping(value = "/loadResultCnt", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadResultCnt(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) {
		List<LinkedHashMap<String, Object>> resultList = Collections.emptyList();
		JSONObject jobj = null;
		
		try {
			resultList = execSqlStdChkService.loadResultCnt(sqlStandards);
			jobj = getJSONResult(resultList, true).toJSONObject();
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
		
		} finally {
			resultList = null;
		}
		
		return jobj.toString();
	}
	
	/* sql 표준 점검 결과 - 엑셀 다운 */
	@RequestMapping(value = "/loadResultCnt/excelDownload", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public void excelDownloadResult(@ModelAttribute("sqlStandards") SQLStandards sqlStandards
			, Model model, HttpServletRequest req, HttpServletResponse res) {
		
		try {
			model.addAttribute("fileName", "실행기반_SQL_표준_점검_결과");
			execSqlStdChkService.excelDownloadResult(sqlStandards, req, res, model);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
		}
	}
	
	/* 표준 미준수 SQL - 스케줄러 리스트 */
	@RequestMapping(value = "/loadSchedulerCombo", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadSchedulerCombo(@RequestParam(required = false) Map<String, String> param) {
		List<JobSchedulerBase> resultList = new ArrayList<JobSchedulerBase>();
		JSONObject jobj = null;
		
		try {
			resultList = execSqlStdChkService.loadSchedulerList( param );
			jobj = success(resultList).toJSONObject();
			
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
			
		}finally {
			resultList = null;
		}
		return jobj.get("rows").toString();
	}
	
	/* 표준 미준수 SQL - 표준 미준수 SQL 조회 */
	@RequestMapping(value = "/loadNonStdSql", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadNonStdSql(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) {
		List<LinkedHashMap<String, Object>> resultList = Collections.emptyList();
		int dataCount4NextBtn = 0;
		JSONObject jobj = null;
		
		try {
			resultList = execSqlStdChkService.loadNonStdSql(sqlStandards);
			
			if (resultList != null && resultList.size() > sqlStandards.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(sqlStandards.getPagePerCount());	// 리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함
			}
			
			jobj = getJSONResult(resultList, true).toJSONObject();
			jobj.put("dataCount4NextBtn", dataCount4NextBtn);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
		
		} finally {
			resultList = null;
		}
		
		return jobj.toString();
	}
	
	/* 표준 미준수 SQL - 표준 미준수 SQL 조회 */
	@RequestMapping(value = "/loadSqlFullText", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadSqlFullText(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) {
		List<LinkedHashMap<String, Object>> resultList = Collections.emptyList();
		JSONObject jobj = null;
		
		try {
			resultList = execSqlStdChkService.loadSqlFullText(sqlStandards);
			jobj = getJSONResult(resultList, true).toJSONObject();
		
		}catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
			
		} finally {
			resultList = null;
		}
		
		return jobj.toString();
	}
	
	/* SQL 표준 준수 현황 - 좌측 그래프 데이터 로드 */
	@RequestMapping(value = "/loadStdComplianceState", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadStdComplianceState(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) {
		List<LinkedHashMap<String, Object>> resultList = Collections.emptyList();
		JSONObject jobj = null;
		
		try {
			resultList = execSqlStdChkService.loadStdComplianceState(sqlStandards);
			jobj = getJSONResult(resultList, true).toJSONObject();
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
			
		} finally {
			resultList = null;
		}
		
		return jobj.toString();
	}
	
	/* SQL 표준 준수 현황 - 우측 그래프 데이터 로드 */
	@RequestMapping(value = "/loadCountByIndex", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadCountByIndex(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) {
		List<LinkedHashMap<String, Object>> resultList = Collections.emptyList();
		JSONObject jobj = null;
		
		try {
			resultList = execSqlStdChkService.loadCountByIndex(sqlStandards);
			jobj = getJSONResult(resultList, true).toJSONObject();
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
			
		} finally {
			resultList = null;
		}
		
		return jobj.toString();
	}
	
	/* SQL 표준 점검 예외 대상 관리 action */
	@RequestMapping(value = "/loadExceptionList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadExceptionList(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) {
		List<SQLStandards> resultList = Collections.emptyList();
		int dataCount4NextBtn = 0;
		JSONObject jobj = null;
		
		try {
			resultList = execSqlStdChkService.loadExceptionList(sqlStandards);

			if (resultList != null && resultList.size() > sqlStandards.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(sqlStandards.getPagePerCount());	// 리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함
			}
			jobj = success(resultList).toJSONObject();
			jobj.put("dataCount4NextBtn", dataCount4NextBtn);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
		
		}finally {
			resultList = null;
		}
		
		return jobj.toString();
	}
	
	/* SQL 표준 점검 예외 대상 관리 엑셀다운 */
	@RequestMapping(value = "/loadExceptionList/excelDownload", method = RequestMethod.POST)
	public ModelAndView exceptionExcelDown(@ModelAttribute("sqlStandards") SQLStandards sqlStandards
			, Model model) {
		
		List<LinkedHashMap<String, Object>> resultList = Collections.emptyList();
		
		try {
			resultList = execSqlStdChkService.exceptionExcelDown(sqlStandards);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
		}
		
		model.addAttribute("fileName", "SQL_표준_점검_예외_대상_관리");
		model.addAttribute("sheetName", "SQL_표준_점검_예외_대상_관리");
		model.addAttribute("excelId", "SQL_STD_QTY_EXCEPTION");
		
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* SQL 표준 점검 예외 대상 관리 - 품질 점검 지표 코드 조회 */
	@RequestMapping(value = "/getQtyChkIdxCd", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getQtyChkIdxCd(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) {
		List<SQLStandards> resultList = Collections.emptyList();
		String resultStr = "";
		
		try {
			resultList = execSqlStdChkService.getQtyChkIdxCd(sqlStandards);
			
			if ( "Y".equals(sqlStandards.getDml_yn()) ) {
				SQLStandards tempSqlStandards = new SQLStandards();
				
				tempSqlStandards.setQty_chk_idt_cd("");
				tempSqlStandards.setQty_chk_idt_cd_nm("전체");
				
				resultList.add(0, tempSqlStandards);
			}
			resultStr = success(resultList).toJSONObject().get("rows").toString();
			
		}catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
		
		}finally {
			resultList = null;
		}
		
		return resultStr;
	}
	
	/* SQL 표준 점검 예외 대상 관리 저장 */
	@RequestMapping(value = "/saveException", method = RequestMethod.POST)
	@ResponseBody
	public Result saveException(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) {
		Result result = new Result();
		
		try {
			result = execSqlStdChkService.saveException(sqlStandards);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* SQL 표준 점검 예외 대상 관리 수정 */
	@RequestMapping(value = "/modifyException", method = RequestMethod.POST)
	@ResponseBody
	public Result modifyException(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) {
		Result result = new Result();
		
		try {
			result = execSqlStdChkService.modifyException(sqlStandards);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* SQL 표준 점검 예외 대상 관리 삭제 */
	@RequestMapping(value = "/deleteException", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteException(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) {
		Result result = new Result();
		
		try {
			int deleteResult = execSqlStdChkService.deleteException(sqlStandards);
			
			if (deleteResult > 0) {
				result.setResult(true);
				
			}else {
				result.setResult(false);
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* SQL 표준 점검 예외 대상 관리 - 엑셀파일 업로드 */
	@RequestMapping(value = "/excelUpload", method = RequestMethod.POST, headers = ("content-type=multipart/*"))
	@ResponseBody
	public Result excelUpload(@RequestParam("uploadFile") MultipartFile file) {
		Result result = new Result();
		
		if ( file.isEmpty() == false ) {
			if ( file.getSize() > maxUploadSize ) {
				result.setResult(false);
				result.setMessage("파일 용량이 너무 큽니다.\\n" + maxUploadMegaBytes + "메가 이하로 선택해 주세요.");
				
			} else {
				try {
					result = execSqlStdChkService.excelUpload(file);
					
				} catch (Exception ex) {
					String methodName = new Object() {}.getClass().getEnclosingClass().getName();
					logger.error( "{} 예외발생 ==> ", methodName, ex );
					
					result.setResult(false);
				}
			}
		}
		return result;
	}
}