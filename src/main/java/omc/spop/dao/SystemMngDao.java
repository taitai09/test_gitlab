package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.Database;
import omc.spop.model.Menu;
import omc.spop.model.Project;
import omc.spop.model.Users;

/***********************************************************
 * 2018.08.23	임호경	최초작성
 **********************************************************/

public interface SystemMngDao {

	List<Menu> menuTree(Menu menu);

	List<Menu> getMenuList(Menu menu);

	int updateMenuInfo(Menu menu);

	int insertMenuInfo(Menu menu);

	int deleteMenuInfo(Menu menu);

	int mergeIntoMenuAuth(Menu menu);

	List<Integer> getAuthNm(Menu menu);

	int insertMenuAuth(Menu menu);

	int deleteMenuAuth(Menu menu);

	int menuIsEmpty(Menu menu);

	Users getUserInfo(Users users);

	int saveUserInfo(Users users);

	String checkPassword(Users users);

	int changeUserPassword(Users users);

	int saveUsersWrkJob(Users wrkJob);

	String getUserWrkjobCd(Users users);

	List<Menu> getMenuInfo(Menu menu);

	List<Users> defaultAuth(Users users);

	List<Users> defaultWrkjobCd(Users users);

	int saveUserAuthWrkjob(Users users);

	String getUserAuthId(String user_id);

	List<Project> getProjectMngList(Project project);

	int insertProjectMng(Project project);

	int updateProjectMng(Project project);

	List<LinkedHashMap<String, Object>> getProjectMngListByExcelDown(Project project);

	List<Database> getDatabase(Database database);

	List<String> getParentMenuIdList(String string);

}
