package omc.spop.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.base.Config;
import omc.spop.dao.ImprovementManagementDao;
import omc.spop.dao.PerformanceImprovementOutputsDao;
import omc.spop.model.DownLoadFile;
import omc.spop.model.FullscanSql;
import omc.spop.model.NewSql;
import omc.spop.model.PlanChangeSql;
import omc.spop.model.TempUsageSql;
import omc.spop.model.TuningTargetSql;
import omc.spop.service.PerformanceImprovementOutputsService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.FileMngUtil;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.06.01	이원식	최초작업
 * 2021.07.14	이재우	성능개선실적 > 산출물 상세조회 추가.
 **********************************************************/

@Service("PerformanceImprovementOutputsService")
public class PerformanceImprovementOutputsServiceImpl implements PerformanceImprovementOutputsService {
	private static final Logger logger = LoggerFactory.getLogger(
			PerformanceImprovementOutputsServiceImpl.class );
	
	@Autowired
	private PerformanceImprovementOutputsDao performanceImprovementOutputsDao;
	
	@Autowired
	private ImprovementManagementDao improvementManagementDao;

	@Override
	public List<TuningTargetSql> performanceImprovementOutputsList( TuningTargetSql tuningTargetSql )
			throws Exception {
		return performanceImprovementOutputsDao.performanceImprovementOutputsList(tuningTargetSql);
	}

	@Override
	public List<TuningTargetSql> performanceImprovementOutputsListAll(TuningTargetSql tuningTargetSql)
			throws Exception {
		return performanceImprovementOutputsDao.performanceImprovementOutputsListAll( tuningTargetSql );
	}

	@Override
	public List<TuningTargetSql> performanceImprovementOutputsListAll_V2(TuningTargetSql tuningTargetSql) {
		return performanceImprovementOutputsDao.performanceImprovementOutputsListAll_V2( tuningTargetSql );
	}

	@Override
	public List<TuningTargetSql> performanceImprovementOutputsList_V2( TuningTargetSql tuningTargetSql )
			throws Exception {
		List<TuningTargetSql> resultList = performanceImprovementOutputsDao.performanceImprovementOutputsList_V2(tuningTargetSql);
		
		for (TuningTargetSql targetSql : resultList) {
			if ( targetSql.getControversialist() != null && "".equals( targetSql.getControversialist() ) == false ) {
				targetSql.setControversialist( removeSpecialChar( targetSql.getControversialist() ));
			}
			if ( targetSql.getImpr_sbst() != null && "".equals( targetSql.getImpr_sbst() ) == false ) {
				targetSql.setImpr_sbst( removeSpecialChar( targetSql.getImpr_sbst() ));
			}
		}
		return resultList;
	}

	@Override
	public List<LinkedHashMap<String, Object>> performanceImprovementOutputsList4Excel(TuningTargetSql tuningTargetSql)
			throws Exception {
		List<LinkedHashMap<String, Object>> resultList = performanceImprovementOutputsDao.performanceImprovementOutputsList4Excel(tuningTargetSql);
		
		for (LinkedHashMap<String, Object> resultMap : resultList) {
			if ( resultMap.get("CONTROVERSIALIST") != null && "".equals( resultMap.get("CONTROVERSIALIST").toString() ) == false ) {
				resultMap.put("CONTROVERSIALIST", removeSpecialChar( resultMap.get("CONTROVERSIALIST").toString() ));
			}
			if ( resultMap.get("IMPR_SBST") != null && "".equals( resultMap.get("IMPR_SBST").toString() ) == false ) {
				resultMap.put("IMPR_SBST", removeSpecialChar( resultMap.get("IMPR_SBST").toString() ));
			}
		}
		
		return resultList;
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> performanceImprovementOutputsList4Excel_V2(TuningTargetSql tuningTargetSql)
			throws Exception {
		List<LinkedHashMap<String, Object>> resultList = performanceImprovementOutputsDao.performanceImprovementOutputsList4Excel_V2(tuningTargetSql);
		
		for (LinkedHashMap<String, Object> resultMap : resultList) {
			if ( resultMap.get("CONTROVERSIALIST") != null && "".equals( resultMap.get("CONTROVERSIALIST").toString() ) == false ) {
				resultMap.put("CONTROVERSIALIST", removeSpecialChar( resultMap.get("CONTROVERSIALIST").toString() ));
			}
			if ( resultMap.get("IMPR_SBST") != null && "".equals( resultMap.get("IMPR_SBST").toString() ) == false ) {
				resultMap.put("IMPR_SBST", removeSpecialChar( resultMap.get("IMPR_SBST").toString() ));
			}
		}
		
		return resultList;
	}
	
	@Override
	public DownLoadFile getPerformanceImprovementOutputs(TuningTargetSql tuningTargetSql) throws Exception {
		DownLoadFile downloadFile = new DownLoadFile();
		String outputsRoot = Config.getString("download.outputs.dir");
		String fileName = tuningTargetSql.getTuning_no() + "_result_"+DateUtil.getNowDate("yyMMddHHmmssSSS")+".sql";
		String orgFileName = tuningTargetSql.getTuning_no() + "_성능개선결과_"+DateUtil.getNowDate("yyMMddHHmmssSSS")+".sql";
		String choickDivCd = tuningTargetSql.getChoice_div_cd();

		// 성능개선결과 파일 생성
		makePerformanceOutputs(choickDivCd, outputsRoot, fileName, tuningTargetSql);

		downloadFile.setFile_path(outputsRoot);
		downloadFile.setFile_nm(fileName);
		downloadFile.setOrg_file_nm(orgFileName);
		
		return downloadFile;
	}

	@Override
	public DownLoadFile getPerformanceImprovementOutputsAll(TuningTargetSql tuningTargetSql) throws Exception {
		DownLoadFile downloadFile = new DownLoadFile();
		String outputsRoot = Config.getString("download.outputs.dir");
		String fileName = DateUtil.getNowDate("yyMMddHHmmssSSS") + "_result.zip";
		String tempFileName = "";
		String orgFileName = DateUtil.getNowDate("yyMMddHHmmssSSS") + "_성능개선결과.zip";
		
		String[] tuningNoArry = StringUtil.split(tuningTargetSql.getTuningNoArry(), "|");
		String[] choiceDivArry = StringUtil.split(tuningTargetSql.getChoiceDivArry(), "|");
		String[] fileNameArry = new String[tuningNoArry.length];
		
		FileMngUtil fileMng = new FileMngUtil();

		for (int i = 0; i < tuningNoArry.length; i++) {
			TuningTargetSql temp = new TuningTargetSql();
			temp.setTuning_no(tuningNoArry[i]);

			tempFileName = tuningNoArry[i] + "_result_"+DateUtil.getNowDate("yyMMddHHmmssSSS")+".sql";
			fileNameArry[i] = tempFileName;

			// 성능개선결과 파일 생성
			makePerformanceOutputs(choiceDivArry[i], outputsRoot, tempFileName, temp);
		}
		
		// zip 파일 생성.
		fileMng.makeZipFile(outputsRoot, fileName, fileNameArry);
		
		downloadFile.setFile_path(outputsRoot);
		downloadFile.setFile_nm(fileName);
		downloadFile.setOrg_file_nm(orgFileName);
		
		return downloadFile;
	}
	
	public void makePerformanceOutputs(String choickDivCd, String outputsRoot, String fileName, TuningTargetSql tuningTargetSql) throws Exception {
		FileMngUtil fileMng = new FileMngUtil();
		TuningTargetSql selection = new TuningTargetSql();
		TuningTargetSql improvements = new TuningTargetSql();

		// SQL 개선사항
		improvements = improvementManagementDao.getImprovements(tuningTargetSql);
		selection = performanceImprovementOutputsDao.getPerformanceImprovementOutputsViewDetail( tuningTargetSql );
		
		fileMng.makePerformanceImprovementOutputs( outputsRoot, fileName, selection, improvements );
		
	}

	@Override
	public TuningTargetSql getPerformanceImprovementOutputsViewDetail(TuningTargetSql tuningTargetSql)
			throws Exception {
		return performanceImprovementOutputsDao.getPerformanceImprovementOutputsViewDetail(tuningTargetSql);
	}
	
	/**
	 * 특수문자 제거
	 *
	 * @param strText  특수문자가 포함된 문자열
	 * @return 문자열
	 */
	public static String removeSpecialChar(String strText) {
		String returnValue;
		
		returnValue = strText;
		returnValue = returnValue.replaceAll("(<P>)|(</P>)","");
		returnValue = returnValue.replaceAll("(<a>)|(</a>)","");
		returnValue = returnValue.replaceAll("(<span>)|(</span>)","");
		returnValue = returnValue.replaceAll("&lt;","<");
		returnValue = returnValue.replaceAll("&gt;",">");
		returnValue = returnValue.replaceAll("<(\\/br|br|br\\/|br \\/| br\\/[^>]*)>","\n");
		returnValue = returnValue.replaceAll("&amp;","&");
		returnValue = returnValue.replaceAll("&quot;","\"");
		returnValue = returnValue.replaceAll("&#035;","#");
		returnValue = returnValue.replaceAll("\n","\r\n");
		
		return returnValue;
	}
}
