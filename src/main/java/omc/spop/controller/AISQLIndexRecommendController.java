package omc.spop.controller;

import com.google.gson.Gson;
import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.*;
import omc.spop.service.AISQLPVIndexRecommendService;
import omc.spop.service.impl.AISQLPV.AISQLPVIndexRecommendServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/********************************************************
 * Full Name	AutoIndexSQLPerformanceVerificationIndexRecommend
 ********************************************************/

@RequestMapping(value="/AISQLPVIndexRecommend")
@Controller
public class AISQLIndexRecommendController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(AISQLIndexRecommendController.class);

	private final AISQLPVIndexRecommendService aisqlpvIndexRecommendService;

	public AISQLIndexRecommendController(AISQLPVIndexRecommendService aisqlpvIndexRecommendService) {
		this.aisqlpvIndexRecommendService = aisqlpvIndexRecommendService;
	}

	/* 인덱스 Recommend 좌측 그리드 */
	@RequestMapping(value = "/getIndexRecommend", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getIndexRecommend(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
									Model model) {

		List<SQLAutoPerformanceCompare> resultList = null;

		try {
			resultList = aisqlpvIndexRecommendService.getIndexRecommend(sqlAutoPerformanceCompare);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName, ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* Index Data .  */
	@RequestMapping(value = "/getIndexDataList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getIndexDataList(@RequestParam HashMap<String, String> paramMap, Model model) {

		String result = "";
		try {
			result = aisqlpvIndexRecommendService.getIndexDataList(paramMap, AISQLPVIndexRecommendServiceImpl.PAGING_Y);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName, ex);
		}

		return result;
	}

	/*get Current Created Indexes*/
	@RequestMapping(value = "/getCurrentCreatedIndexes", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getCurrentCreatedIndexes(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
										   Model model) {

		String result = "";

		try {
			result = aisqlpvIndexRecommendService.getCurrentCreatedIndexes(sqlAutoPerformanceCompare);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName, ex);
		}

		return result;
	}

	/*자동 인덱스 생성 내역*/
	@RequestMapping(value = "/getAutoIndexCreateHistory", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getAutoIndexCreateHistory(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
											Model model) {

		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();

		try {
			resultList = aisqlpvIndexRecommendService.getAutoIndexCreateHistory(sqlAutoPerformanceCompare);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName, ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/*자동 인덱스 생성 내역*/
	@RequestMapping(value = "/getAutoIndexCreateHistoryAll", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getAutoIndexCreateHistoryAll(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
											Model model) {

		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();

		try {
			resultList = aisqlpvIndexRecommendService.getAutoIndexCreateHistoryAll(sqlAutoPerformanceCompare);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName, ex);
		}

		return success(resultList).toJSONObject().toString();
	}


	/*자동 인덱스 삭제 내역*/
	@RequestMapping(value = "/getAutoIndexDropHistory", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getAutoIndexDeleteHistory(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
											Model model) {

		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();

		try {
			resultList = aisqlpvIndexRecommendService.getAutoIndexDropHistory(sqlAutoPerformanceCompare);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName, ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/*자동 인덱스 삭제 내역*/
	@RequestMapping(value = "/getAutoIndexDropHistoryAll", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getAutoIndexDropHistoryAll(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
											Model model) {

		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();

		try {
			resultList = aisqlpvIndexRecommendService.getAutoIndexDropHistoryAll(sqlAutoPerformanceCompare);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName, ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/*인덱스 생성 스크립트 */
	@RequestMapping(value = "/getCreateScript", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getCreateScript(@RequestParam HashMap<String, String> paramMap, Model model) {

		try {
			return aisqlpvIndexRecommendService.getCreateScript(paramMap);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName, ex);
		}
	}
	/*인덱스 삭제 스크립트 */
	@RequestMapping(value = "/getDropScript", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDropScript(@RequestParam HashMap<String, String> paramMap, Model model) {

		try {
			return aisqlpvIndexRecommendService.getDropScript(paramMap);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName, ex);
		}
	}

	/*인덱스 자동 생성 */
	@RequestMapping(value = "/autoGenerateIndex", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String autoGenerateIndex(@RequestParam HashMap<String, String> paramMap, Model model) {

		try {
			return aisqlpvIndexRecommendService.autoGenerateIndex(paramMap);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName, ex);
		}
	}

	/*인덱스 자동 생성 */
	@RequestMapping(value = "/autoIndexDrop", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String autoIndexDrop(@RequestParam HashMap<String, String> paramMap, Model model) {

		try {
			return aisqlpvIndexRecommendService.autoIndexDrop(paramMap);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName, ex);
		}
	}


	public String getErrorJsonString(String methodName, Exception ex) {
		logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		return getErrorJsonString(ex);
	}

	/*테이블 스페이스 존재 여부 확인 */
	@RequestMapping(value = "/checkTableSpaceExists", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String checkTableSpaceExists(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
										Model model) {
		try {
			return aisqlpvIndexRecommendService.checkTableSpaceExists(sqlAutoPerformanceCompare);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName, ex);
		}
	}


	/*테이블 스페이스 존재 여부 확인 */
	@RequestMapping(value = "/getPerformanceAnalysis", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getPerformanceAnalysis(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
										 Model model) {

		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();

		try {
			resultList = aisqlpvIndexRecommendService.getPerformanceAnalysis(sqlAutoPerformanceCompare);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName, ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* SQL별 성능 영향도 분석 - 인덱스 Recommend - 엑셀 다운로드 (그리드 데이터) */
	@RequestMapping(value = "/excelDownload", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public ModelAndView excelDownload(@RequestParam HashMap<String, String> paramMap, Model model) {
		ArrayList<HashMap<String, String>> resultList = null;

		try {
			resultList = aisqlpvIndexRecommendService.loadResultListExcel(paramMap, model);

		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			ex.printStackTrace();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "인덱스_검증");
		model.addAttribute("sheetName", "인덱스_검증");
		model.addAttribute("excelId", "AISQLPV_INDEX_RECOMMEND");

		return new ModelAndView("xlsxView", "resultList", resultList);
	}

	/* 인덱스 자동 분석 실행 */
	@RequestMapping(value = "/setSqlAutoPerfChk", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String updateSqlAutoPerfChk(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model) {
		int result = 0;

		try {

//			result = aisqlpvIndexRecommendService.updateSqlAutoPerfChk( sqlAutoPerformanceCompare );

			if(result <= 0){
				return "{\"result\":false,\"message\":\"Update 대상 건이 존재하지 않음\"}";
			}

		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName , ex);
		}
		return getSuccessJsonString(String.valueOf(result));
	}

	/*테이블 스페이스 존재 여부 확인 */
	@RequestMapping(value = "/forceCompleteAuto", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String forceCompleteAuto(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
										 @RequestParam String type, Model model) {
		String result ="";
		try {
			result = aisqlpvIndexRecommendService.forceCompleteAuto(sqlAutoPerformanceCompare, type);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName, ex);
		}

		return result;
	}

	/* 인덱스 자동 분석 실행 */
	@RequestMapping(value = "/getVisibleIndexInfo", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getVisibleIndexInfo(SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model) {
		String result = "";
		try {
			result = aisqlpvIndexRecommendService.getVisibleIndexInfo( sqlAutoPerformanceCompare );

		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName , ex);
		}

		return result;
	}

	/* 인덱스 자동 분석 실행 */
	@RequestMapping(value = "/autoCreateIndexInvisible", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String autoCreateIndexInvisible(@RequestParam HashMap<String, String> paramMap, Model model) {
		String result = "";
		try {
			result = aisqlpvIndexRecommendService.autoCreateIndexInvisible( paramMap );

		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName , ex);
		}

		return result;
	}

	/* 인덱스 생성 여부 update 요청 */
	@RequestMapping(value = "/setCreateIndexYN", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String setCreateIndexYN(SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model) {
		String result = "";

		Users users = SessionManager.getLoginSession().getUsers();
		String userId = users.getUser_id();

		try {
			result = aisqlpvIndexRecommendService.setCreateIndexYN( sqlAutoPerformanceCompare );
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = null;
			jsonObj = (JSONObject) jsonParser.parse(result);

			if(jsonObj.get("is_error")!= null){
				String err_msg = jsonObj.get("is_error").toString();
				if(StringUtils.isNotEmpty(err_msg)){
					if("0".equals(err_msg)){
						logger.info("IDX_AD_NO = [{}] / UPDATE ROW = 0");
					}
				}
			}
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			return getErrorJsonString(methodName , ex);
		}

		return result;
	}

	/* 강제완료처리 UPDATE */
	@RequestMapping(value = "/forceUpdateSqlAutoPerformance", method = RequestMethod.POST)
	@ResponseBody
	public Result forceUpdateSqlAutoPerformance(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck )
			throws Exception {
		Result result = new Result();

		try {
			result = aisqlpvIndexRecommendService.forceUpdateSqlAutoPerformance( sqlAutomaticPerformanceCheck );

		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}
	/* 프로젝트에 자동성능점검 수행중인 회차가 있는지 조회 */
	@RequestMapping(value = "/countExecuteTms", method = RequestMethod.POST)
	@ResponseBody
	public Result countExecuteTms( SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		Result result = new Result();

		try{
			// 수행중 count
			int Ecount = aisqlpvIndexRecommendService.countExecuteTms( sqlAutoPerformanceCompare );

			// 수행중일 때
			if ( Ecount > 0 ) {
				result.setTxtValue( "false" );
			}

		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			result.setResult( false );
			result.setMessage( ex.getMessage() );
		}

		return result;
	}

	/* getAuthoritySQL action */
	@RequestMapping(value = "/getAuthoritySQL", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getAuthoritySQL(@ModelAttribute("authoritySQL") AuthoritySQL authoritySQL, Model model) {
		List<AuthoritySQL> resultList = new ArrayList<AuthoritySQL>();
		int dataCount4NextBtn = 0;
		logger.debug(authoritySQL.toString());

		try {
			resultList = aisqlpvIndexRecommendService.getAuthoritySQL(authoritySQL);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* getAuthoritySQL action */
	@RequestMapping(value = "/setExcuteAnalyzeSqlAutoPerfChk", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getAuthoritySQL(@RequestParam HashMap<String, String> paramMap, Model model) {
		int result = 0;
		try {

			result =  aisqlpvIndexRecommendService.setExcuteAnalyzeSqlAutoPerfChk(paramMap);
			if(result > 0 ){
				return getSuccessJsonString("");
			}else{
				return getErrorJsonString("", "Update Result 0");
			}
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

	}

	/* getAuthoritySQL action */
	@RequestMapping(value = "/getPerfChkAutoIndexingV2", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getPerfChkAutoIndexingV2(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		String result = "";
		try {
			result =  aisqlpvIndexRecommendService.getPerfChkAutoIndexingV2(sqlAutoPerformanceCompare);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return result;
	}


	/* getAuthoritySQL action */
	@RequestMapping(value = "/getAutoError", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getAutoError(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		ArrayList<HashMap<String,String>> result = null;
		String strResult = "";
		try {
			result =  aisqlpvIndexRecommendService.getAutoError(sqlAutoPerformanceCompare);

			Gson gson = new Gson();
			strResult = gson.toJson(result);

		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return getSuccessJsonString(strResult);
	}

}
