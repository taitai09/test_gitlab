package omc.spop.service;

import java.util.List;

import omc.spop.model.AdvancedAnalysis;
import omc.spop.model.AwrOsStat;
import omc.spop.model.DbaHistBaseline;
import omc.spop.model.DbaHistSnapshot;
import omc.spop.model.DownLoadFile;
import omc.spop.model.Result;

/***********************************************************
 * 2018.03.05	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface AWRAdvancedAnalysisService {
	/** AWR Report 리스트 */
	List<AdvancedAnalysis> selectAWRReport(AdvancedAnalysis advancedAnalysis) throws Exception;
	
	/** AWR Report - OS Stat Chart 리스트 */
	List<AwrOsStat> oSStatChartList(AdvancedAnalysis advancedAnalysis) throws Exception;

	/** AWR Report - Foreground Wait Class Chart 리스트 */
	Result foreWaitClassChartList(AdvancedAnalysis advancedAnalysis) throws Exception;
	
	/** AWR Report - Background Wait Class Chart 리스트 */
	Result backWaitClassChartList(AdvancedAnalysis advancedAnalysis) throws Exception;

	/** AWR Report - Foreground TOP Event Chart 리스트 */
	Result foreTOPEventChartList(AdvancedAnalysis advancedAnalysis) throws Exception;

	/** AWR Report - Background TOP Event Chart 리스트 */
	Result backTOPEventChartList(AdvancedAnalysis advancedAnalysis) throws Exception;

	/** ADDM Report 리스트 */
	List<AdvancedAnalysis> selectADDMReport(AdvancedAnalysis advancedAnalysis) throws Exception;
	
	/** ASH Report 리스트 */
	List<AdvancedAnalysis> selectASHReport(AdvancedAnalysis advancedAnalysis) throws Exception;	
	
	/** AWR SQL Report 리스트 */
	List<AdvancedAnalysis> selectAWRSQLReport(AdvancedAnalysis advancedAnalysis) throws Exception;
	
	/** AWR Diff Report 리스트 */
	List<AdvancedAnalysis> selectAWRDiffReport(AdvancedAnalysis advancedAnalysis) throws Exception;		
	
	/** Report Save Action */
	DownLoadFile getReportSave(AdvancedAnalysis advancedAnalysis) throws Exception;
	
	/** SNAP_ID 조회 팝업 */
	List<DbaHistSnapshot> snapShotList(DbaHistSnapshot dbaHistSnapshot) throws Exception;	
	
	/** BASELINE 조회 팝업 */
	List<DbaHistBaseline> baseLineList(AdvancedAnalysis advancedAnalysis) throws Exception;
	
	/** ASH Report MinMaxDateTime 조회 */
	AdvancedAnalysis setMinMaxDateTime(AdvancedAnalysis advancedAnalysis) throws Exception;	
}
