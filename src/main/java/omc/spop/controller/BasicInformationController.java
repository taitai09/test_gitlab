package omc.spop.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import omc.spop.base.InterfaceController;
import omc.spop.model.Result;
import omc.spop.model.SpopPreferences;
import omc.spop.service.BasicInformationService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2017.12.05	이원식	최초작성
 * 2018.03.07	이원식	OPENPOP V2 최초작업
 * 2020.12.22	황예지	InsertWorkJobInfo 추가(업무기준정보 설정 변경 메소드)
 **********************************************************/

@Controller
public class BasicInformationController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(BasicInformationController.class);
	
	@Autowired
	private BasicInformationService basicInformationService;
	
	/* 시스템 기준정보 */
	@RequestMapping(value="/SystemInfo")
	public String SystemInfo(@ModelAttribute("spopPreferences") SpopPreferences spopPreferences, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		List<SpopPreferences> menuGroupList = new ArrayList<SpopPreferences>();
		List<SpopPreferences> basicInfoList = new ArrayList<SpopPreferences>();
		
		spopPreferences.setPref_mgmt_type_cd("1");
		try{
			menuGroupList = basicInformationService.selectMenuGroupList(spopPreferences);
		}catch(Exception e){
			logger.error("MENU GROUP ERROR ==> " + e.getMessage());
		}
		
		try{
			basicInfoList = basicInformationService.selectBasicSystemInfoList(spopPreferences);
		}catch(Exception e){
			logger.error("BASIC INFO ERROR ==> " + e.getMessage());
		}
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menuGroupList", menuGroupList );
		model.addAttribute("basicInfoList", basicInfoList );
		model.addAttribute("menu_id", spopPreferences.getMenu_id() );
		model.addAttribute("menu_nm", spopPreferences.getMenu_nm() );

		return "basicInformation/systemInfo";
	}
	
	/* DB기준정보 */
	@RequestMapping(value="/DBInfo")
	public String DBInfo(@ModelAttribute("spopPreferences") SpopPreferences spopPreferences, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		List<SpopPreferences> menuGroupList = new ArrayList<SpopPreferences>();
		List<SpopPreferences> basicInfoList = new ArrayList<SpopPreferences>();
		
		spopPreferences.setPref_mgmt_type_cd("2");
		try{
			menuGroupList = basicInformationService.selectMenuGroupList(spopPreferences);
		}catch(Exception e){
			logger.error("MENU GROUP ERROR ==> " + e.getMessage());
		}
		
		if(spopPreferences.getDbid() != null){		
			try{
				basicInfoList = basicInformationService.selectBasicDBInfoList(spopPreferences);
			}catch(Exception e){
				logger.error("BASIC INFO ERROR ==> " + e.getMessage());
			}
		}
		
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menuGroupList", menuGroupList );
		model.addAttribute("basicInfoList", basicInfoList );
		model.addAttribute("menu_id", spopPreferences.getMenu_id() );
		model.addAttribute("menu_nm", spopPreferences.getMenu_nm() );
		
		return "basicInformation/dbInfo";
	}	
	
	/* 업무기준정보 */
	@RequestMapping(value="/WorkJobInfo")
	public String WorkJobInfo(@ModelAttribute("spopPreferences") SpopPreferences spopPreferences, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		List<SpopPreferences> menuGroupList = new ArrayList<SpopPreferences>();
		List<SpopPreferences> basicInfoList = new ArrayList<SpopPreferences>();
		
		spopPreferences.setPref_mgmt_type_cd("3");
		try{
			menuGroupList = basicInformationService.selectMenuGroupList(spopPreferences);
		}catch(Exception e){
			logger.error("MENU GROUP ERROR ==> " + e.getMessage());
		}
		
		if(spopPreferences.getWrkjob_cd() != null){		
			try{
				basicInfoList = basicInformationService.selectBasicWorkJobInfoList(spopPreferences);
			}catch(Exception e){
				logger.error("BASIC INFO ERROR ==> " + e.getMessage());
			}
		}
		
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menuGroupList", menuGroupList );
		model.addAttribute("basicInfoList", basicInfoList );
		model.addAttribute("menu_id", spopPreferences.getMenu_id() );
		model.addAttribute("menu_nm", spopPreferences.getMenu_nm() );
		
		return "basicInformation/workJobInfo";
	}	
	
	/* 기준정보관리 - 저장  */
	@RequestMapping(value="/InsertBasicInformation", method=RequestMethod.POST)
	@ResponseBody
	public Result InsertBasicInformation(@ModelAttribute("spopPreferences") SpopPreferences spopPreferences,  Model model) {
		String isOk = "N";
		Result result = new Result();
		try{
			isOk = basicInformationService.insertBasicInfomation(spopPreferences);
			result.setResult(isOk.equals("Y") ? true : false);
			result.setMessage("기준정보 저장에 실패하였습니다.");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 기준정보관리 - 저장 (JSON 사용) */
	@RequestMapping(value="/InsertBasicInformationJson", method=RequestMethod.POST)
	@ResponseBody
	public Result InsertBasicInformationJson(@RequestBody String jData,  Model model) {
		String isOk = "N";
		Result result = new Result();
		try{
			JSONArray jArr = JSONArray.fromObject(jData);	//JsonArray 받아오기
			
			List<SpopPreferences> spopPreferencesList = new ArrayList<SpopPreferences>();	//Vo 넣어줄 배열
			
			for(int i=0; i<jArr.size(); i++){
				SpopPreferences spopPreference = new SpopPreferences();	//Json 변환시켜 매핑할 Vo
				
				JSONObject jValue = (JSONObject)jArr.get(i);
				
				spopPreference.setPref_mgmt_type_cd(jValue.getString("pref_mgmt_type_cd"));
				spopPreference.setPref_id(jValue.getString("pref_id"));
				spopPreference.setPref_value(jValue.getString("pref_value"));
				
				if( jValue.containsKey("wrkjob_cd") ) {
					spopPreference.setWrkjob_cd(jValue.getString("wrkjob_cd"));
				}
				if( jValue.containsKey("dbid") ) {
					spopPreference.setDbid(jValue.getString("dbid"));
				}
				
				spopPreferencesList.add(spopPreference);
			}
			isOk = basicInformationService.insertBasicInfomation(spopPreferencesList);
			
			result.setResult(isOk.equals("Y") ? true : false);
			result.setMessage("기준정보 저장에 실패하였습니다.");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value="/InsertWorkJobInfo", method=RequestMethod.POST)
	@ResponseBody
	public Result InsertWorkJobInfo(@RequestBody String jData, Model model) {
		String isOk = "N";
		Result result = new Result();
		
		try{
			JSONArray jArr = JSONArray.fromObject(jData);	//JsonArray 받아오기
			
			List<SpopPreferences> spopPreferencesList = new ArrayList<SpopPreferences>();	//Vo 넣어줄 배열
			
			for(int i=0; i<jArr.size(); i++){
				SpopPreferences spopPreference = new SpopPreferences();	//Json 변환시켜 매핑할 Vo
				
				JSONObject jValue = (JSONObject)jArr.get(i);
				
				spopPreference.setPref_mgmt_type_cd(jValue.getString("pref_mgmt_type_cd"));
				spopPreference.setWrkjob_cd(jValue.getString("wrkjob_cd"));
				spopPreference.setPref_id(jValue.getString("pref_id"));
				spopPreference.setPref_value(jValue.getString("pref_value"));
				
				spopPreferencesList.add(spopPreference);
			}
			isOk = basicInformationService.insertBasicInfomation(spopPreferencesList);
			result.setResult(isOk.equals("Y") ? true : false);
			result.setMessage("기준정보 저장에 실패하였습니다.");
			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
}