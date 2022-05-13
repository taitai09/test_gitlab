package omc.spop.controller;

import omc.spop.base.InterfaceController;
import omc.spop.model.Project;
import omc.spop.model.Result;
import omc.spop.model.SQLAutoPerformanceCompare;
import omc.spop.model.SQLAutomaticPerformanceCheck;
import omc.spop.service.AISQLPVAnalyzeService;
import omc.spop.service.AISQLPVService;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/********************************************************
 * Full Name	AutoIndexSQLPerformanceVerificationAnalyzeController
 ********************************************************/

@RequestMapping(value="/AISQLPVAnalyze")
@Controller
public class AISQLPVAnalyzeController extends InterfaceController{
	private static final Logger logger = LoggerFactory.getLogger(AISQLPVAnalyzeController.class);

	private final AISQLPVService aisqlpvService;
	private final AISQLPVAnalyzeService aisqlpvAnalyzeService;

	public AISQLPVAnalyzeController(AISQLPVService aisqlpvService, AISQLPVAnalyzeService aisqlpvAnalyzeService) {
		this.aisqlpvService = aisqlpvService;
		this.aisqlpvAnalyzeService = aisqlpvAnalyzeService;
	}

	//하단 그리드
	@RequestMapping(value="/getProjectPerformancePacData", method = RequestMethod.POST , produces = "application/text; charset=utf8")
	@ResponseBody
	public String getProjectPerformancePacData( @ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare ){
		String returnStr = "";
		int dataCount4NextBtn = 0;
		List<SQLAutoPerformanceCompare> projectList = new ArrayList<SQLAutoPerformanceCompare>();

		try {
			projectList = aisqlpvAnalyzeService.getProjectPerformancePacData(sqlAutoPerformanceCompare);

			if ( projectList != null && projectList.size() > 0 ) {
				if( projectList.size() > sqlAutoPerformanceCompare.getPagePerCount()) {
					dataCount4NextBtn = projectList.size();
					projectList.remove(sqlAutoPerformanceCompare.getPagePerCount());
					/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
				}

				for ( SQLAutoPerformanceCompare sqlCom : projectList ) {
					if ( "완료".equals(sqlCom.getExec_status() ) ) {
						sqlCom.setDefaultText("<a href='javascript:;' onclick='moveToOtherTab("+
							sqlAutoPerformanceCompare.getProject_id()+","+
							sqlCom.getSql_auto_perf_check_id()+","+
							sqlCom.getDatabase_kinds_cd()
							+");'><img src='/resources/images/report.png' style='height:15px;vertical-align:middle;'/></a>");
					}
				}
			}

		}catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			return getErrorJsonString(methodName , ex);
		}
		JSONObject jobj = success(projectList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);

		return jobj.toString();
	}

	@RequestMapping(value="/getSelectedProjectPerformancePacData", method = RequestMethod.POST , produces = "application/text; charset=utf8")
	@ResponseBody
	public String getSelectedProjectPerformancePacData( @ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare ){
		String returnStr = "";

		try {
			List<SQLAutoPerformanceCompare> projectList = aisqlpvAnalyzeService.getProjectPerformancePacData(sqlAutoPerformanceCompare);
			returnStr = success( projectList ).toJSONObject().get("rows").toString();
		}catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			return getErrorJsonString(methodName , ex);
		}

		return returnStr;
	}

	@RequestMapping(value="/getPerformancePacData", method = RequestMethod.POST , produces = "application/text; charset=utf8")
	@ResponseBody
	public String getPerformancePacData( @ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare ){
		String returnStr = "";

		try {
			List<SQLAutoPerformanceCompare> projectList = aisqlpvAnalyzeService.getPerformancePacData(sqlAutoPerformanceCompare);
			returnStr = success( projectList ).toJSONObject().get("rows").toString();
		}catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			return getErrorJsonString(methodName , ex);
		}

		return returnStr;
	}

	@RequestMapping(value="/getProjectList", method = RequestMethod.GET , produces = "application/text; charset=utf8")
	@ResponseBody
	public String getProjectList(){
		String returnStr = "";

		try {
			List<Project> projectList = aisqlpvAnalyzeService.getProjectList(new Project());
			returnStr = success(projectList).toJSONObject().get("rows").toString();
		}catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			return getErrorJsonString(methodName , ex);
		}

		return returnStr;
	}

	@RequestMapping(value="/getSqlPerformancePacList", method = RequestMethod.GET , produces = "application/text; charset=utf8")
	@ResponseBody
	public String getSqlPerformancePacList(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare ){
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		String returnStr = "";
		
		try {
			resultList = aisqlpvService.getSqlPerformancePacList( sqlAutoPerformanceCompare );
			returnStr = success( resultList ).toJSONObject().get("rows").toString();
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			return getErrorJsonString(methodName , ex);
			
		}finally {
			resultList = null;
		}
		
		return returnStr;
	}

	/* 원천 DB */
	@RequestMapping(value = "/getOriginalDB", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadOriginalDb(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model) {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();

		try {
			resultList = aisqlpvAnalyzeService.getOriginalDB( sqlAutoPerformanceCompare );
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName , ex);
		}

		return success(resultList).toJSONObject().get("rows").toString();
	}

	/* 인덱스 자동 분석 실행 */
	@RequestMapping(value = "/getExcuteAnalyzeConstraint", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getExcuteAnalyzeConstraint(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model) {
	List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();

		try {
			resultList = aisqlpvAnalyzeService.getExcuteAnalyzeConstraint( sqlAutoPerformanceCompare );
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName , ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 인덱스 자동 분석 실행 */
	@RequestMapping(value = "/setSqlAutoPerfChk", method = RequestMethod.POST)
	@ResponseBody
	public String updateSqlAutoPerfChk(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model) {
		int result = 0;

		try {
			result = aisqlpvAnalyzeService.updateSqlAutoPerfChk( sqlAutoPerformanceCompare );

			if(result <= 0){
				return "{\"result\":false,\"message\":\"Update 대상 건이 존재하지 않음\"}";
			}

		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName , ex);
		}
		return getSuccessJsonString(String.valueOf(result));
	}


	/* 인덱스 자동 분석 실행 */
	@RequestMapping(value = "/excuteAnalyze", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String excuteAnalyze(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model) {
		Result result = new Result();
		try {
			aisqlpvAnalyzeService.excuteAnalyze( sqlAutoPerformanceCompare );
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName , ex);
		}
		return success(new ArrayList<SQLAutoPerformanceCompare>(), RESULT_OK, "").toJSONObject().toString();
	}

	/* 인덱스 자동 분석 실행 */
	@RequestMapping(value = "/forceCompleteAnalyze", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String forceCompleteAnalyze(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model) {
		String msg = "";
		try {
			msg = aisqlpvAnalyzeService.forceCompleteAnalyze( sqlAutoPerformanceCompare );
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName , ex);
		}

		return msg;
	}

	/* 인덱스 자동 분석 실행 */
	@RequestMapping(value = "/getRecommendIndexDbYn", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getRecommendIndexDbYn(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model) {
		String result = "";
		try {
			result = aisqlpvAnalyzeService.getRecommendIndexDbYn( sqlAutoPerformanceCompare );

		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName , ex);
		}

		return result;
	}

	private String getErrorJsonString(String methodName, Exception ex){
		logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
		return getErrorJsonString( ex );
	}

}
