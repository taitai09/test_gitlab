package omc.spop.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import omc.spop.model.AccPathIndexDesign;
import omc.spop.model.ActiveSessionHistory;
import omc.spop.model.DbaHistSqlstat;
import omc.spop.model.DbioByResourceUsage;
import omc.spop.model.IdxAdMst;
import omc.spop.model.IdxAdRecommendIndex;
import omc.spop.model.Module;
import omc.spop.model.OdsHistSqlstat;
import omc.spop.model.Session;
import omc.spop.model.SqlStatByPlan;
import omc.spop.model.TrTop;
import omc.spop.model.TrcdByTrerrSum;
import omc.spop.model.TrcdPerfSum;
import omc.spop.model.WrkJobCd;
 
/***********************************************************
 * 2017.09.28	이원식	최초작성
 **********************************************************/

/**
 * Excel(xls, xlsx) 파일을 쓸 때, 필요한 옵션을 정의한다.
 * 여기에 정의된 옵션으로 실제 파일을 작성한다.
 *
 */
public class WriteOption {
	
	private String IndexDesign = "TABLE_OWNER|TABLE_NAME|INDEX_SEQ|INDEX_COLUMN_LIST";
	private String SessionMonitoring = "SID|QCSID|INST_ID|SERVER|QCINST_ID|SERIAL#|STATUS|SQL_ID|PLAN_HASH_VALUE|SQL_HASH_VALUE|SQL_ADDRESS|USERNAME|SPID|OSUSER|NAME|EVENT|MACHINE|MODULE|ACTION|PROGRAM|LOGON TIME|LOGICAL READS|PGA|UGA|SORTS|WORKAREA MEM ALLOC|ONEPASS|MULTIPASS|CPU|SQL PROFILE|SQL STARTIME|SQL DURATION|AWR_SQLID|SQL_PLAN_HASH_VALUE|SQL CURSOR PURGE|SQL_TEXT";
	private String ModulePerformance = "MODULE|SQL_ID|PLAN_HASH_VALUE|평균소요시간|평균블록읽기수|평균결과수|수행횟수|MAX_ELAP|AVG_CPU|AVG_ORDS|LAST_TIME|PARSING_SCHEMA_NAME";
	private String ModulePerformanceDtl = "실행시간|SNAP_ID|실행노드|SQL_ID|PLAN_HASH_VALUE|MODULE|PARSING_USER|응답시간|CPU|BUFFER GETS|DISK READS|결과건수|CLWAIT_TIME|IOWAIT_TIME|APWAIT_TIME|CCWAIT_TIME|CPU_RATE|CLWAIT_RATE|IOWAIT_RATE|APWAIT_RATE|CCWAIT_RATE|EXECUTIONS|PARSE_CALLS|FETCHES|OPTIMIZER_ENV_HASH_VALUE";
	private String SQLPerformHistory = "실행시간|SNAP_ID|실행노드|SQL_ID|PLAN_HASH_VALUE|MODULE|PARSING_USER|응답시간|CPU|BUFFER GETS|DISK READS|결과건수|CLWAIT_TIME|IOWAIT_TIME|APWAIT_TIME|CCWAIT_TIME|CPU_RATE|CLWAIT_RATE|IOWAIT_RATE|APWAIT_RATE|CCWAIT_RATE|EXECUTIONS|PARSE_CALLS|FETCHES|OPTIMIZER_ENV_HASH_VALUE";
	private String SQLPerformPlanHistory = "SQL_ID|PLAN_HASH_VALUE|EXECUTIONS|PARSE_CALLS|FETCHES|ELAPSED_TIME|CPU_TIME|BUFFER_GETS|DISK READS|ROW_PROCESSED|CLWAIT_TIME|IOWAIT_TIME|APWAIT_TIME|CCWAIT_TIME|CPU_RATE|CLWAIT_RATE|IOWAIT_RATE|APWAIT_RATE|CCWAIT_RATE";
	private String ASHPerformance = "INST_ID|SAMPLE_ID|SAMPLE_TIME|IS_AWR_SAMPLE|SESSION_ID|SESSION_SERIAL#|SESSION_TYPE|FLAGS|USER_ID|SQL_ID|IS_SQLID_CURRENT|SQL_CHILD_NUMBER|SQL_OPCODE|FORCE_MATCHING_SIGNATURE|TOP_LEVEL_SQL_ID|TOP_LEVEL_SQL_OPCODE|SQL_OPNAME|SQL_PLAN_HASH_VALUE|SQL_PLAN_LINE_ID|SQL_PLAN_OPERATION|SQL_PLAN_OPTIONS|SQL_EXEC_ID|SQL_EXEC_START|PLSQL_ENTRY_OBJECT_ID|PLSQL_ENTRY_SUBPROGRAM_ID|PLSQL_OBJECT_ID|PLSQL_SUBPROGRAM_ID|QC_INSTANCE_ID|QC_SESSION_ID|QC_SESSION_SERIAL#|EVENT|EVENT_ID|EVENT#|SEQ#|P1TEXT|P1|P2TEXT|P2|P3TEXT|P3|WAIT_CLASS|WAIT_TIME|SESSION_STATE|TIME_WAITED|BLOCKING_SESSION_STATUS|BLOCKING_SESSION|BLOCKING_SESSION_SERIAL#|BLOCKING_INST_ID|BLOCKING_HANGCHAIN_INFO|TIME_MODEL|PROGRAM|MODULE|ACTION|CLIENT_ID|MACHINE|TM_DELTA_TIME|TM_DELTA_CPU_TIME|TM_DELTA_DB_TIME|DELTA_TIME|DELTA_READ_IO_REQUESTS|DELTA_WRITE_IO_REQUESTS|DELTA_READ_IO_BYTES|DELTA_WRITE_IO_BYTES|DELTA_INTERCONNECT_IO_BYTES|PGA_ALLOCATED|TEMP_SPACE_ALLOCATED";
	private String TopSql = "SQL_ID|PLAN_HASH_VALUE|ACTIVITY(%)|SQL_TEXT";
	private String TopSession = "SESSION_ID|PROGRAM|MODULE|USERNAME|ACTIVITY(%)";
	private String ErrorTrading = "거래코드|거래명|담당자|오류코드|오류명|오류건수";
	private String Timeout = "기준일자|거래코드|거래명|담당자|타임아웃건수";
	private String ElapsedTimeDelay = "기준일자|거래코드|거래명|담당자|응답지연건수|이전응답시간|현재응답시간|지연응답시간|응답시간증가율";
	private String CPUUseTop = "DBIO|CPU사용시간(일)|실행수|평균응답시간";
	private String IOUseTop = "DBIO|메모리|실행수|평균응답시간";
	private String SelfIndexAutoDesign = "NO|테이블명|추천 유형|인덱스 컬럼";
	private String IndexRecommend = "순번|테이블명|추천유형|인덱스컬럼|변경 인덱스명|변경전 인덱스 컬럼";
    /**
     * 엑셀 파일이 만들어질 위치를 지정한다.
     */
    private String filePath;

    /**
     * 엑셀 파일의 이름을 정의한다. 확장자까지 포함해야 한다.
     */
    private String fileName;

    /**
     * 엑셀 Document의 Sheet 명을 정의 한다.
     */
    private String sheetName;

    /**
     * 엑셀 문서가 타이틀(헤더)을 정의한다.
     */
    private List<String> titles;

    /**
     * 엑셀 문서의 내용을 정의한다.
     */
    private List<String[]> contents;

    /**
     * 엑셀 파일의 경로를 가져온다.
     * @return String 엑셀 파일의 절대 경로
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * 엑셀 파일의 절대 경로를 정의한다.
     * @param String 파일 시스템의 물리적 위치. 예>D:\\tempExcelFolder\\
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 엑셀 파일의 이름을 가져온다. 확장자를 포함한다.
     * @return String 확장자를 포함한 엑셀 파일의 이름
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 엑셀 파일의 이름을 정의한다. 확장자를 포함해야 한다.
     * @param String 확장자를 포함한 엑셀 파일의 이름
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 엑셀 문서 내의 Sheet 이름을 가져온다.
     * @return String 엑셀 문서내의 Sheet 명
     */
    public String getSheetName() {
        return sheetName;
    }

    /**
     * 엑셀 문서 내의 Sheet 명을 정의한다. 단 하나의 Sheet만 생성할 수 있다.
     * @param String 엑셀 문서의 Sheet 이름
     */
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * 엑셀 문서의 타이틀 정보를 가져온다.
     * @return List<String> 타이틀 정보를 가진 List
     */
    public List<String> getTitles() {
        List<String> temp = new ArrayList<String>();
        temp.addAll(this.titles);
        return temp;
    }

    /**
     * 엑셀 문서의 타이틀 정보를 정의한다.
     * @param List<String> List 형태의 타이틀 정보
     */
    public void setTitles(List<String> titles) {
        List<String> temp = new ArrayList<String>();
        temp.addAll(titles);
        this.titles = temp;
    }

    /**
     * 엑셀 문서의 타이틀 정보를 정의한다.
     * @param String 가변 인자 형태의 타이틀 정보
     */
    public void setTitles(String ... titles) {
        List<String> temp = Arrays.asList(titles);
        this.titles = temp;
    }
    
    /**
     * 엑셀 문서의 타이틀 정보를 정의한다.
     * @param String 가변 인자 형태의 타이틀 정보
     */
    public void setTitle(String titleName) {
    	String title = "";
    	

    	if(titleName.equals("IndexDesign")){
    		title = IndexDesign;
    	}else if(titleName.equals("SessionMonitoring")){
    		title = SessionMonitoring;
    	}else if(titleName.equals("ModulePerformance")){
    		title = ModulePerformance;
    	}else if(titleName.equals("ModulePerformanceDtl")){
    		title = ModulePerformanceDtl;
    	}else if(titleName.equals("SQLPerformHistory")){
    		title = SQLPerformHistory;
    	}else if(titleName.equals("SQLPerformPlanHistory")){
    		title = SQLPerformPlanHistory;    		
    	}else if(titleName.equals("ASHPerformance")){
    		title = ASHPerformance;
    	}else if(titleName.equals("TopSql")){
    		title = TopSql;
    	}else if(titleName.equals("TopSession")){
    		title = TopSession;
    	}else if(titleName.equals("ErrorTrading")){
    		title = ErrorTrading;
    	}else if(titleName.equals("Timeout")){
    		title = Timeout;
    	}else if(titleName.equals("ElapsedTimeDelay")){
    		title = ElapsedTimeDelay;    		
    	}else if(titleName.equals("CPUUseTop")){
    		title = CPUUseTop;
    	}else if(titleName.equals("IOUseTop")){
    		title = IOUseTop;
    	}else if(titleName.equals("SelfIndexAutoDesign")){
    		title = SelfIndexAutoDesign;    		    		
    	}else if(titleName.equals("IndexRecommend")){
    		title = IndexRecommend;
    	}

    	StringTokenizer st = new StringTokenizer(title,"|");
    	List<String> tempTitles = new ArrayList<String>();
    	while(st.hasMoreTokens()){
    		tempTitles.add(st.nextToken());
		}
    	
    	this.titles = tempTitles;
    }   

    /**
     * 엑셀 문서에 포함될 내용을 가져온다.
     * @return List<Stirng[]> 엑셀 문서에 포함될 내용
     */
    public List<String[]> getContents() {
        List<String[]> temp = new ArrayList<String[]>();
        temp.addAll(this.contents);
        return temp;
    }

    /**
     * 엑셀 문서의 내용을 정의한다.
     * @param List<String[]> 리스트 형태의 내용 정보. 하나의 Row는 하나의 배열로 정의한다.
     */
    public void setContents(List<String[]> contents) {
        List<String[]> temp = new ArrayList<String[]>();
        temp.addAll(contents);
        this.contents = temp;
    }

    /**
     * 엑셀 문서의 내용을 정의한다.
     * @param String[] 가변 길이 형태. 한번 호출 할 때마다 한 Row씩 증가한다.
     */
    public void setContents(String ... contents) {
        List<String[]> temp = new ArrayList<String[]>();
        temp.add(contents);
        if ( this.contents == null ) {
            this.contents = new ArrayList<String[]>();
        }
        this.contents.addAll(temp);
    }
    
    /* 인덱스설계 엑셀 컨텐츠 */
    public void setIndexDesignContents(List<AccPathIndexDesign> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[4];
	    	AccPathIndexDesign result = resultList.get(i);
	    	
	    	content[0] = result.getTable_owner();
	    	content[1] = result.getTable_name();
	    	content[2] = result.getIndex_seq();
	    	content[3] = result.getIndex_column_list();

	    	tempContents.add(content);
		}
		
		setContents(tempContents);
    }    
    
    /* 세션 모니터링 엑셀 컨텐츠 */
    public void setSessionMonitoringContents(List<Session> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[36];
	    	Session result = resultList.get(i);
	    	
	    	content[0] = result.getSid();
	    	content[1] = result.getQcsid().equals("-1") ? "" : result.getQcsid(); // -1일경우 엑셀에서 공백 처리
	    	content[2] = result.getInst_id();
	    	content[3] = result.getServer();
	    	content[4] = result.getQcinst_id();
	    	content[5] = result.getSerial();
	    	content[6] = result.getStatus();
	    	content[7] = result.getSql_id();
	    	content[8] = result.getPlan_hash_value();
	    	content[9] = result.getSql_hash_value();
	    	content[10] = result.getSql_address();
	    	content[11] = result.getUsername();
	    	content[12] = result.getSpid();
	    	content[13] = result.getOsuser();
	    	content[14] = result.getName();
	    	content[15] = result.getEvent();
	    	content[16] = result.getMachine();
	    	content[17] = result.getModule();
	    	content[18] = result.getAction();
	    	content[19] = result.getProgram();
	    	content[20] = result.getLogon_time();
	    	content[21] = result.getLogical_reads();
	    	content[22] = result.getPga();
	    	content[23] = result.getUga();
	    	content[24] = result.getSorts();
	    	content[25] = result.getWorkarea_mem_alloc();
	    	content[26] = result.getOnepass();
	    	content[27] = result.getMultipass();
	    	content[28] = result.getCpu();
	    	content[29] = result.getSql_profile();
	    	content[30] = result.getSql_startime();
	    	content[31] = result.getSql_duration();
	    	content[32] = result.getAwr_sqlid();
	    	content[33] = result.getSql_plan_hash_value();
	    	content[34] = result.getSql_cursor_purge();
	    	content[35] = result.getSql_text();

	    	tempContents.add(content);
		}
		
		setContents(tempContents);
    }
    
    /* 모듈성능분석 마스터 엑셀 컨텐츠 */
    public void setModulePerformanceContents(List<Module> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[12];
	    	Module result = resultList.get(i);
	    	
	    	content[0] = result.getModule();
	    	content[1] = result.getSql_id();
	    	content[2] = result.getPlan_hash_value();
	    	content[3] = result.getAvg_elapsed_time();
	    	content[4] = result.getAvg_buffer_gets();
	    	content[5] = result.getAvg_row_processed();
	    	content[6] = result.getExecutions();
	    	content[7] = result.getMax_elapsed_time();
	    	content[8] = result.getAvg_cpu_time();
	    	content[9] = result.getAvg_phyiscal_reads();
	    	content[10] = result.getLast_exec_time();
	    	content[11] = result.getParsing_schema_name();

	    	tempContents.add(content);
		}
		
		setContents(tempContents);
    }
    
    /* 모듈성능분석 상세 엑셀 컨텐츠 */
    public void setDtlModulePerformanceContents(List<DbaHistSqlstat> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[25];
	    	DbaHistSqlstat result = resultList.get(i);
	    	
	    	content[0] = result.getBegin_interval_time();
	    	content[1] = result.getSnap_id();
	    	content[2] = result.getInstance_number();
	    	content[3] = result.getSql_id();
	    	content[4] = result.getPlan_hash_value();
	    	content[5] = result.getModule();
	    	content[6] = result.getParsing_schema_name();
	    	content[7] = result.getElapsed_time();
	    	content[8] = result.getCpu_time();
	    	content[9] = result.getBuffer_gets();
	    	content[10] = result.getDisk_reads();
	    	content[11] = result.getRows_processed();
	    	content[12] = result.getClwait_time();
	    	content[13] = result.getIowait_time();
	    	content[14] = result.getApwait_time();
	    	content[15] = result.getCcwait_time();
	    	content[16] = result.getCpu_rate();
	    	content[17] = result.getClwait_rate();
	    	content[18] = result.getIowait_rate();
	    	content[19] = result.getApwait_rate();
	    	content[20] = result.getCcwait_rate();
	    	content[21] = result.getExecutions();
	    	content[22] = result.getParse_calls();
	    	content[23] = result.getFetches();
	    	content[24] = result.getOptimizer_env_hash_value();

	    	tempContents.add(content);
		}
		
		setContents(tempContents);
    } 
    
    /* SQL성능분석 엑셀 컨텐츠 */
    public void setSQLPerformHistoryContents(List<OdsHistSqlstat> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[25];
	    	OdsHistSqlstat result = resultList.get(i);
	    	
	    	content[0] = result.getBegin_interval_time();
	    	content[1] = result.getSnap_id();
	    	content[2] = result.getInstance_number();
	    	content[3] = result.getSql_id();
	    	content[4] = result.getPlan_hash_value();
	    	content[5] = result.getModule();
	    	content[6] = result.getParsing_schema_name();
	    	content[7] = result.getElapsed_time();
	    	content[8] = result.getCpu_time();
	    	content[9] = result.getBuffer_gets();
	    	content[10] = result.getDisk_reads();
	    	content[11] = result.getRows_processed();
	    	content[12] = result.getClwait_time();
	    	content[13] = result.getIowait_time();
	    	content[14] = result.getApwait_time();
	    	content[15] = result.getCcwait_time();
	    	content[16] = result.getCpu_rate();
	    	content[17] = result.getClwait_rate();
	    	content[18] = result.getIowait_rate();
	    	content[19] = result.getApwait_rate();
	    	content[20] = result.getCcwait_rate();
	    	content[21] = result.getExecutions();
	    	content[22] = result.getParse_calls();
	    	content[23] = result.getFetches();
	    	content[24] = result.getOptimizer_env_hash_value();

	    	tempContents.add(content);
		}
		
		setContents(tempContents);
    }
    
    /* SQL성능분석 엑셀 컨텐츠 */
    public void setSQLPerformPlanHistoryContents(List<SqlStatByPlan> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[19];
	    	SqlStatByPlan result = resultList.get(i);
	    	
	    	content[0] = result.getSql_id();
	    	content[1] = result.getPlan_hash_value();
	    	content[2] = result.getExecutions();
	    	content[3] = result.getParse_calls();
	    	content[4] = result.getFetches();
	    	content[5] = result.getElapsed_time();
	    	content[6] = result.getCpu_time();
	    	content[7] = result.getBuffer_gets();
	    	content[8] = result.getDisk_reads();
	    	content[9] = result.getRows_processed();
	    	content[10] = result.getClwait_time();
	    	content[11] = result.getIowait_time();
	    	content[12] = result.getApwait_time();
	    	content[13] = result.getCcwait_time();
	    	content[14] = result.getCpu_rate();
	    	content[15] = result.getClwait_rate();
	    	content[16] = result.getIowait_rate();
	    	content[17] = result.getApwait_rate();
	    	content[18] = result.getCcwait_rate();

	    	tempContents.add(content);
		}
		
		setContents(tempContents);
    }     
    
    /* ASH성능분석 All Session 엑셀 컨텐츠 */
    public void setASHPerformanceContents(List<ActiveSessionHistory> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[66];
	    	ActiveSessionHistory result = resultList.get(i);
	    	
	    	content[0] = result.getInst_id();
	    	content[1] = result.getSample_id();
	    	content[2] = result.getSample_time();
	    	content[3] = result.getIs_awr_sample();
	    	content[4] = result.getSession_id();
	    	content[5] = result.getSerial();
	    	content[6] = result.getSession_type();
	    	content[7] = result.getFlags();
	    	content[8] = result.getUser_id();
	    	content[9] = result.getSql_id();
	    	content[10] = result.getIs_sqlid_current();
	    	content[11] = result.getSql_child_number();
	    	content[12] = result.getSql_opcode();
	    	content[13] = result.getForce_matching_signature();
	    	content[14] = result.getTop_level_sql_id();
	    	content[15] = result.getTop_level_sql_opcode();
	    	content[16] = result.getSql_opname();
	    	content[17] = result.getSql_plan_hash_value();
	    	content[18] = result.getSql_plan_line_id();
	    	content[19] = result.getSql_plan_operation();
	    	content[20] = result.getSql_plan_options();
	    	content[21] = result.getSql_exec_id();
	    	content[22] = result.getSql_exec_start();
	    	content[23] = result.getPlsql_entry_object_id();
	    	content[24] = result.getPlsql_entry_subprogram_id();
	    	content[25] = result.getPlsql_object_id();
	    	content[26] = result.getPlsql_subprogram_id();
	    	content[27] = result.getQc_instance_id();
	    	content[28] = result.getQc_session_id();
	    	content[29] = result.getQc_session_serial();
	    	content[30] = result.getEvent();
	    	content[31] = result.getEvent_id();
	    	content[32] = result.getEvent_sharp();
	    	content[33] = result.getSeq_sharp();
	    	content[34] = result.getP1text();
	    	content[35] = result.getP1();
	    	content[36] = result.getP2text();
	    	content[37] = result.getP2();
	    	content[38] = result.getP3text();
	    	content[39] = result.getP3();
	    	content[40] = result.getWait_class();
	    	content[41] = result.getWait_time();
	    	content[42] = result.getSession_state();
	    	content[43] = result.getTime_waited();
	    	content[44] = result.getBlocking_session_status();
	    	content[45] = result.getBlocking_session();
	    	content[46] = result.getBlocking_session_serial();
	    	content[47] = result.getBlocking_inst_id();
	    	content[48] = result.getBlocking_hangchain_info();
	    	content[49] = result.getTime_model();
	    	content[50] = result.getProgram();
	    	content[51] = result.getModule();
	    	content[52] = result.getAction();
	    	content[53] = result.getClient_id();
	    	content[54] = result.getMachine();
	    	content[55] = result.getTm_delta_time();
	    	content[56] = result.getTm_delta_cpu_time();
	    	content[57] = result.getTm_delta_db_time();
	    	content[58] = result.getDelta_time();
	    	content[59] = result.getDelta_read_io_requests();
	    	content[60] = result.getDelta_write_io_requests();
	    	content[61] = result.getDelta_read_io_bytes();					
	    	content[62] = result.getDelta_write_io_bytes();
	    	content[63] = result.getDelta_interconnect_io_bytes();
	    	content[64] = result.getPga_allocated();
	    	content[65] = result.getTemp_space_allocated();
	    	
	    	/*content[50] = result.getCurrent_obj();
	    	content[51] = result.getCurrent_file();
	    	content[52] = result.getCurrent_block();
	    	content[53] = result.getCurrent_row();
	    	content[54] = result.getTop_level_call();
	    	content[55] = result.getTop_level_call_name();
	    	content[56] = result.getConsumer_group_id();
	    	content[57] = result.getXid();
	    	content[58] = result.getRemote_instance();
	    	content[60] = result.getIn_connection_mgmt();
	    	content[61] = result.getIn_parse();
	    	content[62] = result.getIn_hard_parse();
	    	content[63] = result.getIn_sql_execution();
	    	content[64] = result.getIn_plsql_execution();
	    	content[65] = result.getIn_plsql_rpc();
	    	content[66] = result.getIn_plsql_compilation();
	    	content[67] = result.getIn_java_execution();
	    	content[68] = result.getIn_bind();
	    	content[69] = result.getIn_cursor_close();
	    	content[70] = result.getIn_sequence_load();
	    	content[71] = result.getCapture_overhead();
	    	content[72] = result.getReplay_overhead();
	    	content[73] = result.getIs_captured();
	    	content[74] = result.getIs_replayed();
	    	content[75] = result.getService_hash();
	    
	    	content[81] = result.getPort();
	    	content[82] = result.getEcid();*/



	    	tempContents.add(content);
		}
		
		setContents(tempContents);
    } 
    
    /* ASH성능분석 Top Sql 엑셀 컨텐츠 */
    public void setTopSqlContents(List<ActiveSessionHistory> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[4];
	    	ActiveSessionHistory result = resultList.get(i);
	    	
	    	content[0] = result.getSql_id();
	    	content[1] = result.getPlan_hash_value();
	    	content[2] = result.getActivity();
	    	content[3] = result.getSql_text();
	    	
	    	tempContents.add(content);
		}
		
		setContents(tempContents);
    }
    
    /* ASH성능분석 Top Session 엑셀 컨텐츠 */
    public void setTopSessionContents(List<ActiveSessionHistory> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[5];
	    	ActiveSessionHistory result = resultList.get(i);
	    	
	    	content[0] = result.getSession_id();
	    	content[1] = result.getProgram();
	    	content[2] = result.getModule();
	    	content[3] = result.getUsername();
	    	content[4] = result.getActivity();
	    	
	    	tempContents.add(content);
		}
		
		setContents(tempContents);
    }    
    
    /* 거래현황 - 업무별 거래현황 엑셀 컨텐츠 */
    public void setTaskContents(List<WrkJobCd> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[8];
	    	WrkJobCd result = resultList.get(i);
	    	
	    	content[0] = result.getWrkjob_cd_nm();
	    	content[1] = result.getTr_type();
	    	content[2] = result.getDay0();
	    	content[3] = result.getDay1();
	    	content[4] = result.getDay2();
	    	content[5] = result.getDay3();
	    	content[6] = result.getDay4();
	    	content[7] = result.getDay5();

	    	tempContents.add(content);
		}
		
		setContents(tempContents);
    }  
    
    
    /* 거래현황 - 업무별 피크일 TOP5 엑셀 컨텐츠 */
    public void setTaskPeakDayTop5Contents(String wrkjob_cd, List<WrkJobCd> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[6];
	    	WrkJobCd result = resultList.get(i);
	    	
	    	if(result.getWrkjob_cd().equals(wrkjob_cd)){
		    	content[0] = result.getWrkjob_cd_nm();
		    	content[1] = result.getDay1();
		    	content[2] = result.getDay2();
		    	content[3] = result.getDay3();
		    	content[4] = result.getDay4();
		    	content[5] = result.getDay5();
	
		    	tempContents.add(content);
	    	}
		}
		
		setContents(tempContents);
    }     
    
    /* 거래현황 - 오류거래현황 엑셀 컨텐츠 */
    public void setErrorTradingContents(List<WrkJobCd> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[7];
	    	WrkJobCd result = resultList.get(i);
	    	
	    	content[0] = result.getWrkjob_cd_nm();
	    	content[1] = result.getDay0();
	    	content[2] = result.getDay1();
	    	content[3] = result.getDay2();
	    	content[4] = result.getDay3();
	    	content[5] = result.getDay4();
	    	content[6] = result.getDay5();

	    	tempContents.add(content);
		}
		
		setContents(tempContents);
    }
    
    /* 거래현황 - 타임아웃거래현황 엑셀 컨텐츠 */
    public void setTimeoutContents(List<WrkJobCd> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[7];
	    	WrkJobCd result = resultList.get(i);
	    	
	    	content[0] = result.getWrkjob_cd_nm();
	    	content[1] = result.getDay0();
	    	content[2] = result.getDay1();
	    	content[3] = result.getDay2();
	    	content[4] = result.getDay3();
	    	content[5] = result.getDay4();
	    	content[6] = result.getDay5();

	    	tempContents.add(content);
		}
		
		setContents(tempContents);
    }  
    
    /* 거래상세현황 - 거래응답시간TOP 엑셀 컨텐츠 */
    public void setResTimeTopContents(List<TrTop> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[7];
	    	TrTop result = resultList.get(i);
	    	
	    	content[0] = result.getTr_cd();
	    	content[1] = result.getTr_cd_nm();
	    	content[2] = result.getMgr_id();
	    	content[3] = result.getPrev_year_avg_elap_time();
	    	content[4] = result.getPrev_month_avg_elap_time();
	    	content[5] = result.getAvg_elap_time();
	    	content[6] = result.getTot_tr_cnt();

	    	tempContents.add(content);
		}
		
		setContents(tempContents);
    }  
    
    /* 거래상세현황 - 과거대비응답시간증가TOP 엑셀 컨텐츠 */
    public void setRiseResTimeTopContents(List<TrTop> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[8];
	    	TrTop result = resultList.get(i);
	    	
	    	content[0] = result.getTr_cd();
	    	content[1] = result.getTr_cd_nm();
	    	content[2] = result.getMgr_id();
	    	content[3] = result.getPrev_year_avg_elap_time();
	    	content[4] = result.getPrev_month_avg_elap_time();
	    	content[5] = result.getAvg_elap_time();
	    	content[6] = result.getGap_avg_elap_time();
	    	content[7] = result.getTot_tr_cnt();

	    	tempContents.add(content);
		}
		
		setContents(tempContents);
    }
    
    /* 거래상세현황 - 오류거래 엑셀 컨텐츠 */
    public void setErrorTradingDtlContents(List<TrcdByTrerrSum> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[6];
	    	TrcdByTrerrSum result = resultList.get(i);
	    	
	    	content[0] = result.getTr_cd();
	    	content[1] = result.getTr_cd_nm();
	    	content[2] = result.getUser_nm();
	    	content[3] = result.getErr_cd();
	    	content[4] = result.getErr_nm();
	    	content[5] = result.getErr_cnt();

	    	tempContents.add(content);
		}
		
		setContents(tempContents);
    }
    
    /* 거래상세현황 - 타임아웃거래 엑셀 컨텐츠 */
    public void setTimeoutDtlContents(List<TrcdPerfSum> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[5];
	    	TrcdPerfSum result = resultList.get(i);
	    	
	    	content[0] = result.getBase_day();
	    	content[1] = result.getTr_cd();
	    	content[2] = result.getTr_cd_nm();
	    	content[3] = result.getUser_nm();
	    	content[4] = result.getExec_cnt();

	    	tempContents.add(content);
		}
		
		setContents(tempContents);
    } 
    
    /* 거래상세현황 - 응답시간지연 엑셀 컨텐츠 */
    public void setElapsedTimeDelayContents(List<TrcdPerfSum> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[9];
	    	TrcdPerfSum result = resultList.get(i);
	    	
	    	content[0] = result.getBase_day();
	    	content[1] = result.getTr_cd();
	    	content[2] = result.getTr_cd_nm();
	    	content[3] = result.getUser_nm();
	    	content[4] = result.getExec_cnt();
	    	content[5] = result.getPrev_elap_time();
	    	content[6] = result.getCur_elap_time();
	    	content[7] = result.getDelay_elap_time();
	    	content[8] = result.getElap_time_increase_ratio();

	    	tempContents.add(content);
		}
		
		setContents(tempContents);
    }     
    
    /* 거래상세현황 - DB CPU자원사용TOP 엑셀 컨텐츠 */
    public void setCPUUseTopContents(List<DbioByResourceUsage> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[4];
	    	DbioByResourceUsage result = resultList.get(i);
	    	
	    	content[0] = result.getDbio_nm();
	    	content[1] = result.getCpu_time();
	    	content[2] = result.getExec_cnt();
	    	content[3] = result.getAvg_elap_time();

	    	tempContents.add(content);
		}
		
		setContents(tempContents);
    }
    
    /* 거래상세현황 - DB CPU자원사용TOP 엑셀 컨텐츠 */
    public void setIOUseTopContents(List<DbioByResourceUsage> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[4];
	    	DbioByResourceUsage result = resultList.get(i);
	    	
	    	content[0] = result.getDbio_nm();
	    	content[1] = result.getMem_use_qnt();
	    	content[2] = result.getExec_cnt();
	    	content[3] = result.getAvg_elap_time();

	    	tempContents.add(content);
		}
		
		setContents(tempContents);
    }
    
    /* 성능 개선 관리 - 인덱스자동설계 엑셀 컨텐츠 */
    public void setSelfIndexAutoDesignContents(List<IdxAdMst> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[6];
	    	IdxAdMst result = resultList.get(i);
	    	
	    	content[0] = result.getSeq();
	    	content[1] = result.getTable_name();
	    	content[2] = result.getRecommend_type();
	    	content[3] = result.getAccess_path_column_list();
	    	content[4] = result.getSource_index_name();
	    	content[5] = result.getSource_index_column_list();

	    	tempContents.add(content);
		}
		
		setContents(tempContents);
    }
    
    /* 성능관리 - 인덱스자동설계현황 - 인덱스 Recommend현황 엑셀 컨텐츠 */
    public void setIndexRecommendContents(List<IdxAdRecommendIndex> resultList){
		List<String[]> tempContents = new ArrayList<String[]>();
		
		for(int i = 0 ; i < resultList.size() ; i++){
	    	String[] content = new String[6];
	    	IdxAdRecommendIndex result = resultList.get(i);
	    	
	    	content[0] = result.getRnum();
	    	content[1] = result.getTable_name();
	    	content[2] = result.getRecommend_type();
	    	content[3] = result.getAccess_path_column_list();
	    	content[4] = result.getSource_index_name();
	    	content[5] = result.getSource_index_column_list();

	    	tempContents.add(content);
		}
		
		setContents(tempContents);    	
    }
}