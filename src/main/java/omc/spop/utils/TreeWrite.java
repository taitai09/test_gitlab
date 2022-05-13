package omc.spop.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import omc.spop.model.ApplicationCheckCombined;
import omc.spop.model.DbParameterHistory;
import omc.spop.model.ExplainPlanTree;
import omc.spop.model.Menu;
import omc.spop.model.MyMenu;
import omc.spop.model.OdsHistParameter;
import omc.spop.model.SelftunPlanTable;
import omc.spop.model.Session;
import omc.spop.model.SolutionProgramMng;
import omc.spop.model.SqlCheckCombined;
import omc.spop.model.SqlGrid;
import omc.spop.model.Sqls;
import omc.spop.model.WrkJobCd;

/**
 * Tree 형태의 리턴값 생성
 * 2021.01.22	황예지	buildSqlTree메소드 추가 : 배포SQL성능추적 메뉴
 */

public class TreeWrite {
	private static final Logger logger = LoggerFactory.getLogger(TreeWrite.class);
	/* My메뉴 구조 재정의  */
    public static List<MyMenu> buildMyMenuTree(List<MyMenu> list, String parent_id){
		List<MyMenu> targetList = new ArrayList<MyMenu>();
		
		for(MyMenu orgMenu : list){			
			MyMenu temp = new MyMenu();
			temp.setMenu_level(orgMenu.getMenu_level());
			temp.setMenu_id(orgMenu.getMenu_id());
			temp.setParent_menu_id(orgMenu.getParent_menu_id());
			temp.setMenu_nm(orgMenu.getMenu_nm());
			temp.setMenu_desc(orgMenu.getMenu_desc());
			temp.setMenu_url_addr(orgMenu.getMenu_url_addr());
			temp.setMenu_image_nm(orgMenu.getMenu_image_nm());
			temp.setTop_menu_id(orgMenu.getTop_menu_id());

			if(parent_id.equals(orgMenu.getParent_menu_id())){
				temp.setChildMenu(buildMyMenuTree(list, temp.getMenu_id()));
				targetList.add(temp);
			}
		}
		
		return targetList;
	}
    
    public static List<MyMenu> buildMyMenuTree2(List<MyMenu> list, String parent_id){
		List<MyMenu> targetList = new ArrayList<MyMenu>();
		
		for(MyMenu orgMenu : list){			
			MyMenu temp = new MyMenu();
			temp.setMenu_level(orgMenu.getMenu_level());
			temp.setMenu_id(orgMenu.getMenu_id());
			temp.setId(orgMenu.getMenu_id());
			temp.setParent_menu_id(orgMenu.getParent_menu_id());
			temp.setParent_id(orgMenu.getParent_menu_id());
			temp.setMenu_nm(orgMenu.getMenu_nm());
			temp.setText(orgMenu.getMenu_nm());
			temp.setMenu_desc(orgMenu.getMenu_desc());
			temp.setMenu_url_addr(orgMenu.getMenu_url_addr());
			temp.setMenu_image_nm(orgMenu.getMenu_image_nm());
			temp.setTop_menu_id(orgMenu.getTop_menu_id());
			temp.setChecked(orgMenu.getChecked());

			if(parent_id.equals(orgMenu.getParent_menu_id())){
				temp.setChildren(buildMyMenuTree2(list, temp.getMenu_id()));
				targetList.add(temp);
			}
		}
		
		return targetList;
	}	
	/* 메뉴 구조 재정의  */
    public static List<Menu> buildMenuTree(List<Menu> list, String parent_id){
		List<Menu> targetList = new ArrayList<Menu>();
		
		for(Menu orgMenu : list){			
			Menu temp = new Menu();
			temp.setMenu_id(orgMenu.getMenu_id());			
			temp.setParent_menu_id(orgMenu.getParent_menu_id());
			temp.setMenu_nm(orgMenu.getMenu_nm());
			temp.setMenu_desc(orgMenu.getMenu_desc());
			temp.setMenu_url_addr(orgMenu.getMenu_url_addr());
			temp.setMenu_image_nm(orgMenu.getMenu_image_nm());

			if(parent_id.equals(orgMenu.getParent_menu_id())){
				temp.setChildMenu(buildMenuTree(list, temp.getMenu_id()));
				targetList.add(temp);
			}
		}
		
		return targetList;
	}	
    
	/* Tree Plan  */
    public static List<SqlGrid> buildSQLTree(List<SqlGrid> list, String parent_id){
		List<SqlGrid> targetList = new ArrayList<SqlGrid>();
		
		for(SqlGrid sqlGrid : list){			
			SqlGrid grid = new SqlGrid();
			grid.setId(sqlGrid.getId());			
			grid.setParent_id(sqlGrid.getParent_id());
			grid.setImid(sqlGrid.getImid());
			grid.setText(sqlGrid.getText());

			if(parent_id.equals(sqlGrid.getParent_id())){
				grid.setChildren(buildSQLTree(list, grid.getId()));
				targetList.add(grid);
			}
		}
		
		return targetList;
	}
    
    /* Tree Plan  */
    public static List<ExplainPlanTree> buildExplainPlanTreeAutoPerfCheck(List<ExplainPlanTree> list, String parent_id){
		List<ExplainPlanTree> targetList = new ArrayList<ExplainPlanTree>();
		
		for(ExplainPlanTree explainPlanTree : list){
			ExplainPlanTree inExplainPlanTree = new ExplainPlanTree();
			inExplainPlanTree.setId(explainPlanTree.getId());
			inExplainPlanTree.setParent_id(explainPlanTree.getParent_id());
			inExplainPlanTree.setImid(explainPlanTree.getImid());
			inExplainPlanTree.setText(explainPlanTree.getText());

			if(parent_id.equals(explainPlanTree.getParent_id())){
				inExplainPlanTree.setChildren(buildExplainPlanTreeAutoPerfCheck(list, inExplainPlanTree.getId()));
				targetList.add(inExplainPlanTree);
			}
		}
		
		return targetList;
	}
	
	/* Grid Plan  */
    public static List<SqlGrid> buildSqlGrid(List<SqlGrid> list, String parent_id){
		List<SqlGrid> targetList = new ArrayList<SqlGrid>();
		
		for(SqlGrid sqlGrid : list){			
			SqlGrid grid = new SqlGrid();
			grid.setId(sqlGrid.getId());			
			grid.setParent_id(sqlGrid.getParent_id());
			grid.setImid(sqlGrid.getImid());
			grid.setOperation(sqlGrid.getOperation());
			
			grid.setObject_node(sqlGrid.getObject_node());
			grid.setObject(sqlGrid.getObject());
			grid.setObject_owner(sqlGrid.getObject_owner());
			grid.setObject_name(sqlGrid.getObject_name());
			grid.setObject_type(sqlGrid.getObject_type());
			grid.setOptimizer(sqlGrid.getOptimizer());
			grid.setCost(sqlGrid.getCost());
			grid.setCardinality(sqlGrid.getCardinality());
			grid.setBytes(sqlGrid.getBytes());
			grid.setOther_tag(sqlGrid.getOther_tag());
			grid.setPartition_start(sqlGrid.getPartition_start());
			grid.setPartition_stop(sqlGrid.getPartition_stop());
			grid.setCpu_cost(sqlGrid.getCpu_cost());
			grid.setIo_cost(sqlGrid.getIo_cost());
			grid.setAccess_predicates(sqlGrid.getAccess_predicates());
			grid.setFilter_predicates(sqlGrid.getFilter_predicates());
			grid.setProjection(sqlGrid.getProjection());
			grid.setTime(sqlGrid.getTime());
			grid.setQblock_name(sqlGrid.getQblock_name());
			grid.setTimestamp(sqlGrid.getTimestamp());
			grid.setLevel(sqlGrid.getLevel());
			grid.setQuery_output(sqlGrid.getQuery_output());
			
			if(parent_id.equals(sqlGrid.getParent_id())){
				grid.setChildren(buildSqlGrid(list, grid.getId()));
				targetList.add(grid);
			}
		}
		
		return targetList;
	} 
	
	
	public static List<SelftunPlanTable> buildExplainPlanTree(List<SelftunPlanTable> list, String parent_id){
		List<SelftunPlanTable> targetList = new ArrayList<SelftunPlanTable>();
		
		for(SelftunPlanTable treeNode : list){
			SelftunPlanTable selftunPlanTable = new SelftunPlanTable();
			SelftunPlanTable attributes = new SelftunPlanTable();
			
			selftunPlanTable.setId(treeNode.getId());
			selftunPlanTable.setParent_id(treeNode.getParent_id());
			selftunPlanTable.setText(treeNode.getText());

			attributes.setTable_owner(treeNode.getTable_owner());
			attributes.setTable_name(treeNode.getTable_name());
			attributes.setPartition_start(treeNode.getPartition_start());
			attributes.setDistribution(treeNode.getDistribution());
			attributes.setAccess_predicates(treeNode.getAccess_predicates());
			attributes.setFilter_predicates(treeNode.getFilter_predicates());
			
			selftunPlanTable.setAttributes(attributes);

			if(parent_id.equals(treeNode.getParent_id())){
				selftunPlanTable.setChildren(buildExplainPlanTree(list, selftunPlanTable.getId()));
				targetList.add(selftunPlanTable);
			}
		}
		
		return targetList;
	}	
	
	/* 파라미터 tree Grid  */
    public static List<OdsHistParameter> buildParameterGrid(List<OdsHistParameter> list, String parent_id){
		List<OdsHistParameter> targetList = new ArrayList<OdsHistParameter>();
		
		for(OdsHistParameter parameter : list){			
			OdsHistParameter result = new OdsHistParameter();
			result.setId(parameter.getId());
			result.setP_id(parameter.getP_id());
			result.setDbid(parameter.getDbid());
			result.setParameter_name(parameter.getParameter_name());
			result.setValue(parameter.getValue());
			result.setModify_time(parameter.getModify_time());

			if(parent_id.equals(parameter.getP_id())){
				result.setChildren(buildParameterGrid(list, result.getId()));
				targetList.add(result);
			}
		}
		
		return targetList;
	} 	
    
	/* WorkJob tree Grid  */
    public static List<WrkJobCd> buildWorkJob(List<WrkJobCd> list, String parent_id){
		List<WrkJobCd> targetList = new ArrayList<WrkJobCd>();
		
		for(WrkJobCd wrkJobCd : list){
			WrkJobCd result = new WrkJobCd();
			result.setId(wrkJobCd.getId());
			result.setParent_id(wrkJobCd.getParent_id());
			result.setText(wrkJobCd.getText());
			result.setWrkjob_cd_nm(wrkJobCd.getWrkjob_cd_nm());
			result.setWrkjob_div_cd(wrkJobCd.getWrkjob_div_cd());
			result.setDbid(wrkJobCd.getDbid());
			result.setDb_name(wrkJobCd.getDb_name());
			result.setPerf_check_threshold_type_nm(wrkJobCd.getPerf_check_threshold_type_nm());
			result.setPerf_check_threshold_type_cd(wrkJobCd.getPerf_check_threshold_type_cd());
			result.setBuffer_gets_threshold(wrkJobCd.getBuffer_gets_threshold());
			result.setElapsed_time_threshold(wrkJobCd.getElapsed_time_threshold());
			result.setUse_yn(wrkJobCd.getUse_yn());
			result.setDeploy_check_target_yn(wrkJobCd.getDeploy_check_target_yn());
			result.setDb1_id(wrkJobCd.getDb1_id());
			result.setDb1_name(wrkJobCd.getDb1_name());
			result.setDb2_id(wrkJobCd.getDb2_id());
			result.setDb2_name(wrkJobCd.getDb2_name());
			result.setDb3_id(wrkJobCd.getDb3_id());
			result.setDb3_name(wrkJobCd.getDb3_name());
			
			if(parent_id.equals(wrkJobCd.getParent_id())){
				result.setChildren(buildWorkJob(list, result.getId()));
				targetList.add(result);
			}
		}
		
		return targetList;
	}    
	
    public static List<WrkJobCd> buildWrkJobTree(List<WrkJobCd> list, String parent_id){
		List<WrkJobCd> targetList = new ArrayList<WrkJobCd>();
		
		for(WrkJobCd grid : list){			
			WrkJobCd wrkJobCd = new WrkJobCd();
			wrkJobCd.setId(grid.getId());			
			wrkJobCd.setParent_id(grid.getParent_id());
			wrkJobCd.setText(grid.getText());
			wrkJobCd.setDbid(grid.getDbid());
			wrkJobCd.setWrkjob_div_cd(grid.getWrkjob_div_cd());

			if(parent_id.equals(grid.getParent_id())){
				wrkJobCd.setChildren(buildWrkJobTree(list, wrkJobCd.getId()));
				targetList.add(wrkJobCd);
			}
		}
		
		return targetList;
	}
    
    /* 세션모니터링 - Session List */
    public static List<Session> buildSessionGrid(List<Session> list, String parent_id){
		List<Session> targetList = new ArrayList<Session>();
		
		for(Session grid : list){			
			Session session = new Session();
			session.setSid(grid.getSid());			
			session.setQcsid(grid.getQcsid());
			session.setInst_id(grid.getInst_id());
			session.setServer(grid.getServer());
			session.setQcinst_id(grid.getQcinst_id());
			session.setUsername(grid.getUsername());
			session.setSerial(grid.getSerial());
			session.setSql_id(grid.getSql_id());
			session.setPlan_hash_value(grid.getPlan_hash_value());
			session.setSql_hash_value(grid.getSql_hash_value());
			session.setSql_address(grid.getSql_address());
			session.setEvent(grid.getEvent());
			session.setSpid(grid.getSpid());
			session.setOsuser(grid.getOsuser());
			session.setName(grid.getName());
			session.setStatus(grid.getStatus());
			session.setSql_text(grid.getSql_text());
			session.setMachine(grid.getMachine());
			session.setModule(grid.getModule());
			session.setAction(grid.getAction());
			session.setProgram(grid.getProgram());
			session.setLogon_time(grid.getLogon_time());
			session.setLogical_reads(grid.getLogical_reads());
			session.setPga(grid.getPga());
			session.setUga(grid.getUga());
			session.setSorts(grid.getSorts());
			session.setWorkarea_mem_alloc(grid.getWorkarea_mem_alloc());
			session.setOnepass(grid.getOnepass());
			session.setMultipass(grid.getMultipass());
			session.setCpu(grid.getCpu());
			session.setAwr_sqlid(grid.getAwr_sqlid());
			session.setSql_plan_hash_value(grid.getSql_plan_hash_value());
			session.setSql_cursor_purge(grid.getSql_cursor_purge());
			session.setSql_profile(grid.getSql_profile());
			session.setSql_startime(grid.getSql_startime());
			session.setSql_duration(grid.getSql_duration());

			if(parent_id.equals(grid.getQcsid())){
				session.setChildren(buildSessionGrid(list, session.getSid()));
				targetList.add(session);
			}
		}
		
		return targetList;
	}
    
	/* Menu tree Grid  
	public static List<Menu> buildMenu(List<Menu> list, String parent_id, String check, int cnt) {
		List<Menu> targetList = new ArrayList<Menu>();
		logger.debug("list.size:" + list.size());
		Menu result = null;
		String isFirst = check;
		
		for (Menu menu : list) {
			result = new Menu();
			try {
				result.setId(menu.getId());
				result.setParent_id(menu.getParent_id());
				result.setMenu_id(menu.getMenu_id());
				result.setText(menu.getText());

				result.setMenu_desc(menu.getMenu_desc());
				result.setMenu_url_addr(menu.getMenu_url_addr());
				result.setMenu_image_nm(menu.getMenu_image_nm());
				result.setMenu_ordering(menu.getMenu_ordering());
				result.setUse_yn(menu.getUse_yn());
				System.out.println("[(초기)cnt=]"+cnt);
					
				if(isFirst.equals("Y") && cnt == 0 && !menu.getParent_id().equals("-1")){
					isFirst = "N";
				}
				
				if(isFirst.equals("Y") && parent_id.equals(menu.getParent_id())) {
					cnt +=1;
					result.setChildren(buildMenu(list, result.getId(), "Y", cnt));
					targetList.add(result);
				}
				
				if(isFirst.equals("N") && cnt == 0 && !parent_id.equals(menu.getParent_id())){
					System.out.println("[(인)cnt=]"+cnt);
					result.setChildren(null);
					result.setParent_id("-1");
					targetList.add(result);
				}

				
			} catch (Exception e) {
				logger.debug("error message:" + e.getMessage());
			}
		}
		return targetList;
	}*/
	
	/* Menu tree Grid  */
	public static List<Menu> buildMenu(List<Menu> list, String parent_id) {
		List<Menu> targetList = new ArrayList<Menu>();
//		logger.debug("list.size:" + list.size());
		Menu result = null;
		for (Menu menu : list) {
			result = new Menu();
			try {
				result.setRnum(menu.getRnum());
				result.setId(menu.getId());
				result.setParent_id(menu.getParent_id());
				result.setMenu_id(menu.getMenu_id());
				result.setText(menu.getText());
				
				result.setMenu_desc(menu.getMenu_desc());
				result.setMenu_url_addr(menu.getMenu_url_addr());
				result.setMenu_image_nm(menu.getMenu_image_nm());
				result.setMenu_ordering(menu.getMenu_ordering());
				result.setUse_yn(menu.getUse_yn());
				
				if(parent_id.equals(menu.getParent_id())) {
					result.setChildren(buildMenu(list, result.getId()));
					targetList.add(result);
				}
				
			} catch (Exception e) {
				logger.error("error message:" + e.getMessage());
			}
		}
		return targetList;
	}
	
	/* Menu tree Grid  */
	public static List<SolutionProgramMng> buildSolutionProgramMngList(List<SolutionProgramMng> list, String parent_id) {
		List<SolutionProgramMng> targetList = new ArrayList<SolutionProgramMng>();
//		logger.debug("list.size:" + list.size());
		SolutionProgramMng result = null;
		for (SolutionProgramMng programList : list) {
			result = new SolutionProgramMng();
			try {
				result.setLevel(programList.getLevel());
				result.setId(programList.getId());
				result.setText(programList.getText());
				result.setContents_name(programList.getContents_name());
				result.setParent_contents_id(programList.getParent_contents_id());
				result.setSlt_program_div_cd(programList.getSlt_program_div_cd());
				
				result.setRnum(programList.getRnum());
				result.setSlt_program_div_nm(programList.getSlt_program_div_nm());
				result.setContents_id(programList.getContents_id());
				result.setPath(programList.getPath());
				result.setContents_desc(programList.getContents_desc());
				result.setContents_url_addr(programList.getContents_url_addr());
				result.setExadata_contents_yn(programList.getExadata_contents_yn());
				result.setContents_ordering(programList.getContents_ordering());
				result.setUse_yn(programList.getUse_yn());
				
				if(parent_id.equals(programList.getParent_contents_id())) {
					result.setChildren(buildSolutionProgramMngList(list, result.getContents_id()));
					targetList.add(result);
				}
				
			} catch (Exception e) {
				logger.error("error message:" + e.getMessage());
			}
		}
		return targetList;
	}
    
	public static List<DbParameterHistory> buildDbParameterGrid(List<DbParameterHistory> list, String parent_id) {
		List<DbParameterHistory> targetList = new ArrayList<DbParameterHistory>();
		
		for(DbParameterHistory parameter : list){			
			DbParameterHistory result = new DbParameterHistory();
			result.setId(parameter.getId());
			result.setP_id(parameter.getP_id());
			result.setParameter_name(parameter.getParameter_name());
			result.setDbid(parameter.getDbid());
			result.setDb_name(parameter.getDb_name());
			result.setInst_id(parameter.getInst_id());
			result.setValue(parameter.getValue());
			result.setParameter_chg_dt(parameter.getParameter_chg_dt());

			if(parent_id.equals(parameter.getP_id())){
				result.setChildren(buildDbParameterGrid(list, result.getId()));
				targetList.add(result);
			}
		}
		
		return targetList;
	}

	public static List<ApplicationCheckCombined> buildApplicationCheckCombinedGrid(
			List<ApplicationCheckCombined> list, String parent_id) throws CloneNotSupportedException {
		List<ApplicationCheckCombined> targetList = new ArrayList<ApplicationCheckCombined>();
		
		for(ApplicationCheckCombined parameter : list){			
			ApplicationCheckCombined temp = (ApplicationCheckCombined)parameter.clone();
			if(parent_id.equals(parameter.getParent_wrkjob_cd())){
				temp.setChildren(buildApplicationCheckCombinedGrid(list, temp.getWrkjob_cd()));
				targetList.add(temp);
			}
		}
		
		return targetList;
	}

	public static List<SqlCheckCombined> buildSqlCheckCombinedGrid(List<SqlCheckCombined> list,
			String parent_id) throws CloneNotSupportedException {
		List<SqlCheckCombined> targetList = new ArrayList<SqlCheckCombined>();
		
		for(SqlCheckCombined parameter : list){			
			SqlCheckCombined temp = (SqlCheckCombined)parameter.clone();
			if(parent_id.equals(parameter.getParent_wrkjob_cd())){
				temp.setChildren(buildSqlCheckCombinedGrid(list, temp.getWrkjob_cd()));
				targetList.add(temp);
			}
		}
		
		return targetList;
	}  
	
	/* 배포 SQL 성능점검 treeGrid  */
	public static List<Sqls> buildSqlTree(List<Sqls> list, String parent_id){
		List<Sqls> targetList = new ArrayList<Sqls>();
		
		for(Sqls sql : list){			
			Sqls result = new Sqls();
			result.setId(sql.getId());
			result.setParent_id(sql.getParent_id());
			result.setDbio(sql.getDbio());
			result.setProgram_nm(sql.getProgram_nm());
			result.setAfter_prd_sql_id(sql.getAfter_prd_sql_id());
			result.setAfter_prd_plan_hash_value(sql.getAfter_prd_plan_hash_value());
			result.setPrd_plan_change_yn(sql.getPrd_plan_change_yn());
			result.setAfter_executions(sql.getAfter_executions());
			result.setExcept_yn(sql.getExcept_yn());
			result.setException_prc_meth_nm(sql.getException_prc_meth_nm());
			result.setAfter_fail_yn(sql.getAfter_fail_yn());
			result.setBefore_fail_yn(sql.getBefore_fail_yn());
			result.setProgram_exec_dt(sql.getProgram_exec_dt());
			result.setDeploy_complete_dt(sql.getDeploy_complete_dt());
			result.setPerf_regressed_metric(sql.getPerf_regressed_metric());
			result.setElapsed_time_metirc(sql.getElapsed_time_metirc());
			result.setException_prc_meth_cd(sql.getException_prc_meth_cd());
			result.setTest_sql_id(sql.getTest_sql_id());
			result.setTest_plan_hash_value(sql.getTest_plan_hash_value());
			result.setBefore_prd_sql_id(sql.getBefore_prd_sql_id());
			result.setBefore_prd_plan_hash_value(sql.getBefore_prd_plan_hash_value());
			result.setElapsed_time_regress_yn(sql.getElapsed_time_regress_yn());
			result.setBuffer_gets_regress_yn(sql.getBuffer_gets_regress_yn());
			result.setPrd_buffer_gets_increase_ratio(sql.getPrd_buffer_gets_increase_ratio());
			result.setPrd_elap_time_increase_ratio(sql.getPrd_elap_time_increase_ratio());
			result.setPrd_rows_proc_increase_ratio(sql.getPrd_rows_proc_increase_ratio());
			result.setWrkjob_cd(sql.getWrkjob_cd());
			result.setTop_wrkjob_cd(sql.getTop_wrkjob_cd());
			result.setDbid(sql.getDbid());
			result.setProgram_id(sql.getProgram_id());
			result.setPerf_check_id(sql.getPerf_check_id());
			result.setPerf_check_step_id(sql.getPerf_check_step_id());
			result.setProgram_execute_tms(sql.getProgram_execute_tms());
			result.setBefore_prd_elapsed_time(sql.getBefore_prd_elapsed_time());
			result.setBefore_prd_buffer_gets(sql.getBefore_prd_buffer_gets());
			result.setBefore_prd_rows_processed(sql.getBefore_prd_rows_processed());
			result.setTest_elapsed_time(sql.getTest_elapsed_time());
			result.setAfter_prd_elapsed_time(sql.getAfter_prd_elapsed_time());
			result.setElapsed_time_activity(sql.getElapsed_time_activity());
			result.setTest_buffer_gets(sql.getTest_buffer_gets());
			result.setAfter_prd_buffer_gets(sql.getAfter_prd_buffer_gets());
			result.setBuffer_gets_activity(sql.getBuffer_gets_activity());
			result.setTest_rows_processed(sql.getTest_rows_processed());
			result.setAfter_prd_rows_processed(sql.getAfter_prd_rows_processed());
			
			if(parent_id.equals(sql.getParent_id())){
				result.setChildren(buildSqlTree(list, result.getId()));
				targetList.add(result);
			}
		}
		
		return targetList;
	}
	
}