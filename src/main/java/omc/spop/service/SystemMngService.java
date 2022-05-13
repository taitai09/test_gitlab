package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.Database;
import omc.spop.model.Menu;
import omc.spop.model.Project;
import omc.spop.model.Result;
import omc.spop.model.UserWrkjob;
import omc.spop.model.Users;

/***********************************************************
 * 2018.08.23	임호경	최초작성
 **********************************************************/

public interface SystemMngService {
	
	/** 메뉴 관리 **/
	public List<Menu> menuTree(Menu menu) throws Exception;

	/** 메뉴 관리 리스트 */
	public List<Menu> getMenuList(Menu menu) throws Exception;

	/** 메뉴 리스트 업데이트 */
	public int updateMenuInfo(Menu menu) throws Exception;

	public int insertMenuInfo(Menu menu) throws Exception;

	public int deleteMenuInfo(Menu menu) throws Exception;
		
	/** 메뉴에 읜한 권한 정보 리스트*/
	public List<Integer> getAuthNm(Menu menu);

	/** 메뉴 권한여부 추가*/
	public int insertMenuAuth(Menu menu);
	
	/** 메뉴 권한여부 삭제 (메뉴에대한 권한 업데이트마다 menu_id 값에 해당하는 권한여부 삭제 추가할때마다 수정시 일일이 체크 할 수 없기때문)*/
	public int deleteMenuAuth(Menu menu);

	/** 삭제하려는 메뉴가 자식이 있는지 없는지 체크 여부 : 자식이 있다면 자식을 먼저 삭제후 삭제하기 위함*/
	public boolean menuIsEmpty(Menu menu);

	public Users getUserInfo(Users users);

	public int saveUserInfo(Users users);

	public int changeUserPassword(Users users);

	public boolean checkPassword(Users users);

	public List<Menu> getMenuInfo(Menu menu);

	public List<Users> defaultAuth(Users users);

	public List<Users> defaultWrkjobCd(Users users);

	public int saveUserAuthWrkjob(Users users);

	public String getUserAuthId(String user_id);

	/** 기준정보 설정 - 프로젝트 관리 - 리스트 */
	public List<Project> getProjectMngList(Project project);

	/** 기준정보 설정 - 프로젝트 관리 리스트 저장
	 * @throws Exception */
	public int saveProjectMng(Project project) throws Exception;

	/** 기준정보 설정 - 프로젝트 관리 - 엑셀 다운로드 */
	public List<LinkedHashMap<String, Object>> getProjectMngListByExcelDown(Project project);

	public int saveMenuInfo(Menu menu, List<String> auth_id_list) throws Exception;

	public int saveMultiMenuInfo(Menu menu, List<String> auth_id_list) throws Exception;

	public List<Database> getDatabase(Database database);

}
