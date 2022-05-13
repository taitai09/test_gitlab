package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.PerformanceImprovementReportDao;
import omc.spop.model.PerfList;
import omc.spop.service.PerformanceImprovementReportService;

/***********************************************************
 * 2018.03.14	이원식	OPENPOP V2 최초작업
 * 2021.09.27	이재우	성능개선현황 보고서 엑셀다운 추가
 **********************************************************/

@Service("PerformanceImprovementReportService")
public class PerformanceImprovementReportServiceImpl implements PerformanceImprovementReportService {
	
	private static final Logger logger = LoggerFactory.getLogger(PerformanceImprovementReportServiceImpl.class);

	@Autowired
	private PerformanceImprovementReportDao performanceImprovementReportDao;

	@Override
	public List<PerfList> performanceImprovementReportList(PerfList perfList) throws Exception {
		return performanceImprovementReportDao.performanceImprovementReportList(perfList);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> getPerformanceImprovementReportByExcelDown(PerfList perfList)
			throws Exception {
		return performanceImprovementReportDao.getPerformanceImprovementReportByExcelDown( perfList );
	}

	@Override
	public List<LinkedHashMap<String, Object>> getByRequestTypeReport(PerfList perfList) throws Exception {
		List<PerfList> tempList = new ArrayList<PerfList>();
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String,Object>>();

		tempList = performanceImprovementReportDao.getByRequestTypeReport(perfList);
		
		int tempListSize = 0;
		tempListSize = tempList.size();
		try{
			if(tempListSize > 0){
				String firstWrkjobCdNm = StringUtils.defaultString(tempList.get(0).getWrkjob_cd_nm());
				String newWrkjobCdNm = "";
				String coulmnValue = "";
				int listIndex = 0;  //리스트에 들어갈 인덱스값.
				LinkedHashMap<String, Object> tempMap = null;
				boolean startTempMapInit = false;
				int firstWrkjobNmCount = Integer.parseInt(tempList.get(0).getWrkjob_count());
				int wrkjobNmCount = Integer.parseInt(tempList.get(0).getWrkjob_count());
				for(int i = 0; i < tempListSize; i++){
					
					//비교할 wrkjob_cd_nm을 넣어준다.
					newWrkjobCdNm = StringUtils.defaultString(tempList.get(i).getWrkjob_cd_nm());
	
					//tempMap 에 들어갈 객채 시작하는 위치
					//첫번째 포문 돌때 실행, wrkjob_cd_nm 에 변경되었을때 실행
					//한사이클돌때 한번만 실행됨.
					if(i == 0 || !firstWrkjobCdNm.equals(newWrkjobCdNm)){
						if(i == 0 || startTempMapInit){
							tempMap = new LinkedHashMap<String,Object>(); //맵객채에 넣을 공간 초기화
						}
						firstWrkjobCdNm = newWrkjobCdNm;  //변경해줌.`
						tempMap.put("wrkjob_cd_nm", newWrkjobCdNm); 
						startTempMapInit = true;
					}//한사이클에 한번만 실행.
	
					coulmnValue = tempList.get(i).getCnt();
					tempMap.put("coulmn_"+listIndex, coulmnValue);
					listIndex ++;
					
					if((i+1) == wrkjobNmCount){
	
						listIndex = 0; //다시 리스트에 들어갈 index 를 초기화
						resultList.add(tempMap);
						wrkjobNmCount += firstWrkjobNmCount ;
					}
					
				}
			}else{
				//System.out.println("################ tempList null ");
			}
		}catch(Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw new Exception(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		return resultList;
	}
	
	/*@Override
	public List<LinkedHashMap<String, Object>> getByRequestTypeReportByExcelDown(PerfList perfList) throws Exception {
		List<PerfList> tempList = new ArrayList<PerfList>();
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String,Object>>();

		tempList = performanceImprovementReportDao.getByRequestTypeReport(perfList);
		int tempListSize = tempList.size();
		
		try{
			String firstWrkjobCdNm = tempList.get(0).getWrkjob_cd_nm();
			String newWrkjobCdNm = "";
			String coulmnValue = "";
			int listIndex = 0;  //리스트에 들어갈 인덱스값.
			LinkedHashMap<String, Object> tempMap = null;
			boolean startTempMapInit = false;
			int firstWrkjobNmCount = Integer.parseInt(tempList.get(0).getWrkjob_count());
			int wrkjobNmCount = Integer.parseInt(tempList.get(0).getWrkjob_count());
			for(int i = 0; i < tempListSize; i++){
				
				//비교할 wrkjob_cd_nm을 넣어준다.
				newWrkjobCdNm = tempList.get(i).getWrkjob_cd_nm();

				//tempMap 에 들어갈 객채 시작하는 위치
				//첫번째 포문 돌때 실행, wrkjob_cd_nm 에 변경되었을때 실행
				//한사이클돌때 한번만 실행됨.
				if(i == 0 || !firstWrkjobCdNm.equals(newWrkjobCdNm)){
					if(i == 0 || startTempMapInit){
						tempMap = new LinkedHashMap<String,Object>(); //맵객채에 넣을 공간 초기화
					}
					firstWrkjobCdNm = newWrkjobCdNm;  //변경해줌.`
					tempMap.put("wrkjob_cd_nm", newWrkjobCdNm); 
					startTempMapInit = true;
				}//한사이클에 한번만 실행.

				coulmnValue = tempList.get(i).getCnt();
				tempMap.put("coulmn_"+listIndex, coulmnValue);
				listIndex ++;
				
				if((i+1) == wrkjobNmCount){

					listIndex = 0; //다시 리스트에 들어갈 index 를 초기화
					resultList.add(tempMap);
					wrkjobNmCount += firstWrkjobNmCount ;
				}
				
			}
			
		}catch(Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw new Exception(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		return resultList;
	}*/

	
	@Override
	public List<PerfList> getByProgramTypeReport(PerfList perfList) {
		return performanceImprovementReportDao.getByProgramTypeReport(perfList);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getByProgramTypeReportByExcelDown(PerfList perfList) {
		return performanceImprovementReportDao.getByProgramTypeReportByExcelDown(perfList);
	}

	@Override
	public List<PerfList> makeColumnsValues(PerfList perfList) {
		return performanceImprovementReportDao.makeColumnsValues(perfList);
	}



	@Override
	public List<LinkedHashMap<String, Object>> getByImprovementTypeReport(PerfList perfList) throws Exception {
		List<PerfList> tempList = new ArrayList<PerfList>();
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String,Object>>();

		tempList = performanceImprovementReportDao.getByImprovementTypeReport(perfList);
		int tempListSize = tempList.size();
		
		try{
			if(tempListSize > 0){
				String firstWrkjobCdNm = StringUtils.defaultString(tempList.get(0).getWrkjob_cd_nm());
				String newWrkjobCdNm = "";
				String coulmnValue = "";
				int listIndex = 0;  //리스트에 들어갈 인덱스값.
				LinkedHashMap<String, Object> tempMap = null;
				boolean startTempMapInit = false;
				int firstWrkjobNmCount = Integer.parseInt(tempList.get(0).getWrkjob_count());
				int wrkjobNmCount = Integer.parseInt(tempList.get(0).getWrkjob_count());
				for(int i = 0; i < tempListSize; i++){
					
					//비교할 wrkjob_cd_nm을 넣어준다.
					newWrkjobCdNm = StringUtils.defaultString(tempList.get(i).getWrkjob_cd_nm());
	
					//tempMap 에 들어갈 객채 시작하는 위치
					//첫번째 포문 돌때 실행, wrkjob_cd_nm 에 변경되었을때 실행
					//한사이클돌때 한번만 실행됨.
					if(i == 0 || !firstWrkjobCdNm.equals(newWrkjobCdNm)){
						if(i == 0 || startTempMapInit){
							tempMap = new LinkedHashMap<String,Object>(); //맵객채에 넣을 공간 초기화
						}
						firstWrkjobCdNm = newWrkjobCdNm;  //변경해줌.`
						tempMap.put("wrkjob_cd_nm", newWrkjobCdNm); 
						startTempMapInit = true;
					}//한사이클에 한번만 실행.
	
					coulmnValue = tempList.get(i).getCnt();
					tempMap.put("coulmn_"+listIndex, coulmnValue);
					listIndex ++;
					
					if((i+1) == wrkjobNmCount){
	
						listIndex = 0; //다시 리스트에 들어갈 index 를 초기화
						resultList.add(tempMap);
						wrkjobNmCount += firstWrkjobNmCount ;
					}
					
				}
			}
			
		}catch(Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw new Exception(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		return resultList;
	}



	/*@Override
	public List<LinkedHashMap<String, Object>> getByImprovementTypeReportByExcelDown(PerfList perfList)	throws Exception {
		List<PerfList> tempList = new ArrayList<PerfList>();
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String,Object>>();

		tempList = performanceImprovementReportDao.getByImprovementTypeReport(perfList);
		int tempListSize = tempList.size();
		
		try{
			String firstWrkjobCdNm = tempList.get(0).getWrkjob_cd_nm();
			String newWrkjobCdNm = "";
			String coulmnValue = "";
			int listIndex = 0;  //리스트에 들어갈 인덱스값.
			LinkedHashMap<String, Object> tempMap = null;
			boolean startTempMapInit = false;
			int firstWrkjobNmCount = Integer.parseInt(tempList.get(0).getWrkjob_count());
			int wrkjobNmCount = Integer.parseInt(tempList.get(0).getWrkjob_count());
			for(int i = 0; i < tempListSize; i++){
				
				//비교할 wrkjob_cd_nm을 넣어준다.
				newWrkjobCdNm = tempList.get(i).getWrkjob_cd_nm();

				//tempMap 에 들어갈 객채 시작하는 위치
				//첫번째 포문 돌때 실행, wrkjob_cd_nm 에 변경되었을때 실행
				//한사이클돌때 한번만 실행됨.
				if(i == 0 || !firstWrkjobCdNm.equals(newWrkjobCdNm)){
					if(i == 0 || startTempMapInit){
						tempMap = new LinkedHashMap<String,Object>(); //맵객채에 넣을 공간 초기화
					}
					firstWrkjobCdNm = newWrkjobCdNm;  //변경해줌.`
					tempMap.put("wrkjob_cd_nm", newWrkjobCdNm); 
					startTempMapInit = true;
				}//한사이클에 한번만 실행.

				coulmnValue = tempList.get(i).getCnt();
				tempMap.put("coulmn_"+listIndex, coulmnValue);
				listIndex ++;
				
				if((i+1) == wrkjobNmCount){

					listIndex = 0; //다시 리스트에 들어갈 index 를 초기화
					resultList.add(tempMap);
					wrkjobNmCount += firstWrkjobNmCount ;
				}
				
			}
			
		}catch(Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw new Exception(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		return resultList;
	}*/








}
