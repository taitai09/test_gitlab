package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.maven.artifact.ant.shaded.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.base.SessionManager;
import omc.spop.dao.CommonDao;
import omc.spop.dao.SystemMngDao;
import omc.spop.model.Database;
import omc.spop.model.Menu;
import omc.spop.model.Project;
import omc.spop.model.Users;
import omc.spop.service.SystemMngService;
import omc.spop.service.WatchLogService;
import omc.spop.utils.SHA256Util;

/***********************************************************
 * 2018.08.23	임호경	최초작성
 **********************************************************/

@Service("systemMngService")
public class SystemMngServiceImpl implements SystemMngService {
	private static final Logger logger = LoggerFactory.getLogger(SystemMngServiceImpl.class);

	@Autowired
	private SystemMngDao systemMngDao;

	@Autowired
	private WatchLogService watchLogService;
	
	@Autowired
	private CommonDao commonDao;

	@Override
	public List<Menu> menuTree(Menu menu) throws Exception {
		return systemMngDao.menuTree(menu);
	}

	@Override
	public List<Menu> getMenuList(Menu menu) throws Exception {
		return systemMngDao.getMenuList(menu);
	}

	@Override
	public int updateMenuInfo(Menu menu) throws Exception {
//		systemMngDao.mergeIntoMenuAuth(menu);
		return systemMngDao.updateMenuInfo(menu);
	}

	@Override
	public int insertMenuInfo(Menu menu) throws Exception {
		return systemMngDao.insertMenuInfo(menu);
	}

	@Override
	public int deleteMenuInfo(Menu menu) throws Exception {
		return systemMngDao.deleteMenuInfo(menu);
	}

	@Override
	public List<Integer> getAuthNm(Menu menu) {
		return systemMngDao.getAuthNm(menu);
	}

	@Override
	public int insertMenuAuth(Menu menu) {
		return systemMngDao.insertMenuAuth(menu);
	}

	@Override
	public int deleteMenuAuth(Menu menu) {
		return systemMngDao.deleteMenuAuth(menu);
	}

	@Override
	public boolean menuIsEmpty(Menu menu) {
		int checkList = systemMngDao.menuIsEmpty(menu);
		System.out.println("하위메뉴 갯수 확인 : " + checkList);
		return (checkList == 0) ? true : false;    // true면 삭제가능 ,
	}

	@Override
	public Users getUserInfo(Users users) {
		/*users = systemMngDao.getUserInfo(users);
		if(users.getAuth_id().equals("4")){
			String userWrkjobCd = systemMngDao.getUserWrkjobCd(users);
			users.setWrkjob_cd(userWrkjobCd);
		}
		return users;*/
		return systemMngDao.getUserInfo(users);
	}

	@Override
	public int saveUserInfo(Users users) {
		int check = 0;
		check = systemMngDao.saveUserInfo(users);
		
		try {
			watchLogService.WatchUpdateUsersInfo(users.getUser_id());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return check;
	}

	@Override
	public int changeUserPassword(Users users) {
		int check = 0;
		check = systemMngDao.changeUserPassword(users);

		try {
			watchLogService.WatchUpdateUsersPassword();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return check;
	}

	@Override
	public boolean checkPassword(Users users) {
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWithMD5AndDES");  
		pbeEnc.setPassword("madeopen"); // PBE 값(XML PASSWORD설정)
		String user_password = systemMngDao.checkPassword(users);
		Users user = new Users();
		
		user = commonDao.getSimpleUserInfo(users);
		
		
		if(SHA256Util.isAppliedSHA256(user.getPassword_chg_dt())){
			logger.debug("#################### NEW ALGO CHECK PASSWD ####################");
			String salt = pbeEnc.decrypt(user.getSalt_value());
			String input_password = SHA256Util.getEncrypt(users.getPassword(), salt);
			return input_password.equals(user_password) ? true : false; 
		}else{
			logger.debug("#################### OLD ALGO CHECK PASSWD ####################");
			return users.getPassword().equals(pbeEnc.decrypt(user_password)) ? true : false;
		}
	}

	@Override
	public List<Menu> getMenuInfo(Menu menu) {
		return systemMngDao.getMenuInfo(menu);
	}

	@Override
	public List<Users> defaultAuth(Users users) {
		return systemMngDao.defaultAuth(users);
	}

	@Override
	public List<Users> defaultWrkjobCd(Users users) {
		return systemMngDao.defaultWrkjobCd(users);
	}

	@Override
	public int saveUserAuthWrkjob(Users users) {
		return systemMngDao.saveUserAuthWrkjob(users);
	}

	@Override
	public String getUserAuthId(String user_id) {
		return systemMngDao.getUserAuthId(user_id);
	}

	@Override
	public List<Project> getProjectMngList(Project project) {
		return systemMngDao.getProjectMngList(project);
	}

	@Override
	public int saveProjectMng(Project project) throws Exception {
		int check = 0;
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		if(project.getCrud_flag().equals("C")){
			project.setUser_id(user_id);
			check = systemMngDao.insertProjectMng(project);
		}else{
			check = systemMngDao.updateProjectMng(project);
		}
		
		return check;
	}

	@Override
	public List<LinkedHashMap<String, Object>> getProjectMngListByExcelDown(Project project) {
		return systemMngDao.getProjectMngListByExcelDown(project);
	}

	@Override
	public int saveMenuInfo(Menu menu, List<String> auth_id_list) throws Exception {
		int check = 0; boolean keepGoing = false;
		List<String> parentMenuIdList = new ArrayList<String>();
		List<String> finalParentMenuIdList = null;
		List<String> checkMenuIdList = null;
		List<String> haveToCheckList = null;
		
		if(menu.getMenu_id().equals(StringUtils.defaultString(menu.getParent_menu_id()))){
			throw new Exception("해당메뉴를 상위메뉴로 저장할 수 없습니다.");
		}
		parentMenuIdList = systemMngDao.getParentMenuIdList(menu.getMenu_id());
		if(parentMenuIdList.size() > 0){
			keepGoing = true;
			finalParentMenuIdList = new ArrayList<String>();
			haveToCheckList = new ArrayList<String>();
			finalParentMenuIdList.addAll(parentMenuIdList);
		}
		
		while(keepGoing){
			for(int i = 0; i < parentMenuIdList.size(); i++){
				checkMenuIdList = new ArrayList<String>();
				checkMenuIdList = systemMngDao.getParentMenuIdList(parentMenuIdList.get(i));
				if(checkMenuIdList.size() > 0){
					finalParentMenuIdList.addAll(checkMenuIdList);
					haveToCheckList.addAll(checkMenuIdList);
				}
			}
		
			if(haveToCheckList.size() > 0){
				parentMenuIdList = haveToCheckList;
				haveToCheckList = new ArrayList<String>();
			}else{
				break;
			}
		}
		if(keepGoing){
			logger.debug("###### 자신의 하위메뉴 :"+finalParentMenuIdList.toString());
			logger.debug("###### 변경할 상위메뉴:"+menu.getParent_menu_id());
			if(finalParentMenuIdList.contains(menu.getParent_menu_id())){
				throw new Exception("자신의 하위메뉴를 상위메뉴로 지정 할 수 없습니다.");
			}
		}
		
		
		if (menu.getCrud_flag().equals("U")) { // 메뉴 수정시(update)
			check += systemMngDao.updateMenuInfo(menu);
			check += systemMngDao.deleteMenuAuth(menu);

			if (!auth_id_list.isEmpty()) { // auth_id가 체크되었을때
				for (int i = 0; i < auth_id_list.size(); i++) {
					menu.setAuth_id(auth_id_list.get(i));
					check += systemMngDao.insertMenuAuth(menu);
				}
			}
		} else { // 메뉴 생성시(insert)
			check += systemMngDao.insertMenuInfo(menu);
			menu.setMenu_id(null);

			if (!auth_id_list.isEmpty()) { // auth_id가 체크되었을때
				for (int i = 0; i < auth_id_list.size(); i++) {
					menu.setAuth_id(auth_id_list.get(i));
					check += systemMngDao.insertMenuAuth(menu);
				}
			}
		}
		return check;
	}

	@Override
	public int saveMultiMenuInfo(Menu menu, List<String> auth_id_list) throws Exception {
		int check = 0;
		boolean keepGoing = false;
		List<String> parentMenuIdList = new ArrayList<String>();
		List<String> finalParentMenuIdList = null;
		List<String> checkMenuIdList = null;
		List<String> haveToCheckList = null;
		finalParentMenuIdList = new ArrayList<String>();
		for(int x = 0; x < menu.getMenu_id_list().split(",").length; x++){

			if(menu.getMenu_id_list().split(",")[x].equals(StringUtils.defaultString(menu.getParent_menu_id()))){
				throw new Exception("해당메뉴를 상위메뉴로 저장할 수 없습니다.");
			}
			
			parentMenuIdList = systemMngDao.getParentMenuIdList(StringUtils.defaultString(menu.getMenu_id_list().split(",")[x]));
			if(parentMenuIdList.size() > 0){
				keepGoing = true;
//				finalParentMenuIdList = new ArrayList<String>();
				haveToCheckList = new ArrayList<String>();
				finalParentMenuIdList.addAll(parentMenuIdList);
			}
			
			while(keepGoing){
				for(int i = 0; i < parentMenuIdList.size(); i++){
					checkMenuIdList = new ArrayList<String>();
					checkMenuIdList = systemMngDao.getParentMenuIdList(parentMenuIdList.get(i));
					if(checkMenuIdList.size() > 0){
						finalParentMenuIdList.addAll(checkMenuIdList);
						haveToCheckList.addAll(checkMenuIdList);
					}
				}
			
				if(haveToCheckList.size() > 0){
					parentMenuIdList = haveToCheckList;
					haveToCheckList = new ArrayList<String>();
				}else{
					break;
				}
			}
			
		}

		if(keepGoing){
			logger.debug("###### 자신의 하위메뉴 :"+finalParentMenuIdList.toString()+" [ n ]번째 ######");
			logger.debug("###### 변경할 상위메뉴:"+menu.getParent_menu_id()+" [ n ]번째 ######");
			if(finalParentMenuIdList.contains(menu.getParent_menu_id())){
				throw new Exception("자신의 하위메뉴를 상위메뉴로 지정 할 수 없습니다.");
			}
		}
		
		for(int i = 0; i < menu.getMenu_id_list().split(",").length; i++){
				menu.setMenu_id(menu.getMenu_id_list().split(",")[i]);
				menu.setMenu_ordering(menu.getMenu_ordering_list().split(",")[i]);
				menu.setMenu_image_nm(null);
				menu.setMenu_desc(null);
				menu.setMenu_image_nm(null);
				menu.setMenu_url_addr(null);
				
				check += systemMngDao.updateMenuInfo(menu);
				check += systemMngDao.deleteMenuAuth(menu);
				if (!auth_id_list.isEmpty()) { // auth_id가 체크되었을때
					for (int j = 0; j < auth_id_list.size(); j++) {
						menu.setAuth_id(auth_id_list.get(j));
						check += systemMngDao.insertMenuAuth(menu);
					}
				}
			}
		return check;
	}

	@Override
	public List<Database> getDatabase(Database database) {
		return systemMngDao.getDatabase(database);
	}

}
