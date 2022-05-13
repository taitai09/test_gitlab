package omc.spop.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConvertRecord {
	private ConvertRecord() {
		System.out.println("ok, create constructor");
	}
	
	private String head = "";
	private ArrayList<String> headNameList = null;
	private List<LinkedHashMap<String, Object>> resultList = null;
	private int headNameListSize = 0;
	
	private boolean isPivotData = false;	// substitute variable isFixedClm
	private int groupByNumber = 0;			// substitute variable sector and fixedSector
	
	private ArrayList<String> optionList = new ArrayList<String>();
	
	private static class Singleton {
		private static final ConvertRecord instance = new ConvertRecord();
	}
	
	public static ConvertRecord getInstance() {
		System.out.println("create instance to ConvertRecord");
		return Singleton.instance;
	}
	
	public void converRecord(List<LinkedHashMap<String, Object>> recordData) {
		resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
//		System.out.println("/// recordData /////////////////////////");
//		System.out.println(recordData);
//		System.out.println("\n\n\n");
		
		int recordDataSize = recordData.size();
		LinkedHashMap<String, Object> record = null;
		
		for(int recordDataIndex = 0; recordDataIndex < recordDataSize; recordDataIndex++) {
			record = (LinkedHashMap<String, Object>) recordData.get(recordDataIndex);
			
			if(recordDataIndex == 0) {
				setOfInfoData(record);
			}
			
			pullOutRecord(record, recordDataIndex);
		}
		
		LinkedHashMap<String, Object> row = new LinkedHashMap<String, Object>();
		
		row.put("HEAD", head);
		
		resultList.add(row);
	}
	
	public List<LinkedHashMap<String, Object>> getResultData() {
		return resultList;
	}
	
	private void pullOutRecord(Map<String, Object> record, int recordDataIndex) {
		LinkedHashMap<String, Object> tempData = null;
		int resultListSize = resultList.size();
		String pivotTitle = "";
		
		Iterator<String> recordIt = record.keySet().iterator();
		String key = "";
		String value = "";
		
		while(recordIt.hasNext()) {
			key = recordIt.next() + "";
			value = (record.get(key) == null) ? "" : record.get(key) + "";
			
			if(key.equalsIgnoreCase("HEAD")) {
				continue;
			}
			
			if(isPivotData) {
				if(isGroupByColumnOfKey(key)) {
					tempData = pullOutGroupBy(resultListSize, tempData, key, value);
				} else {
					if(key.equalsIgnoreCase("PIVOT_NM")) {
						updatePivotHead(value);
						
						pivotTitle = value;
					} else {
						tempData.put(pivotTitle, value);
					}
				}
			} else {
				if(tempData == null) {	// 새로운 tempData를 생성
					tempData = initializeTempData(tempData);
				}
				
				tempData.put(key, value);
			}
		}
		
		resultList.add(tempData);
	}
	
	private void updatePivotHead(String headName) {
		System.out.println("head:" + head);
		System.out.println("headNameList:" + headNameList);
		
		final String targetHeadName = "PIVOT_NM";
		
		if(head.indexOf(headName) > 0 || headNameList.contains(headName)) {
			return;
		}
		
		if(head.indexOf(targetHeadName) > 0) {
			String optionStr = head.substring(head.indexOf(targetHeadName));
			String[] options = optionStr.split("\\/");
			
			options = replaceOptions(options);
			
			optionList.add(options[1]);
			optionList.add(options[2]);
			
			head = head.substring(0, head.indexOf(targetHeadName));
			
			if(headNameList.contains(targetHeadName)) {
				headNameList.remove(targetHeadName);
			}
		}
		
		if(head.lastIndexOf(";") != head.length() - 1) {
			head = head.concat(";");
		}
		
		head = head.concat(headName).concat("/").concat(optionList.get(0)).concat("/").concat(optionList.get(1));
		updateHeadNameList(headName);
		
		System.out.println("> head[" + head + "] headNameList:" + headNameList);
	}
	
	private LinkedHashMap<String, Object> pullOutGroupBy(int resultListSize, LinkedHashMap<String, Object>tempData, String key, String value) {
		if(resultListSize == 0) {	// 새로운 tempData를 생성
			if(tempData == null) {
				tempData = initializeTempData(tempData);
			}
			
			tempData.put(key, value);
		} else {
			if(tempData == null) {
				tempData = resultList.remove(resultList.size() - 1);
			}
			
			if(tempData.containsValue(value)) {
				// 무시
			} else {
				// 기존의 tempData를 resultList에 저장하고, dummyData에 headIndex 전까지의 헤더 정보를 추가한 후 tempData에 복사
				tempData = dummyGround(tempData, key);
				
				tempData.put(key, value);
			}
		}
		
		return tempData;
	}
	
	private LinkedHashMap<String, Object> initializeTempData(LinkedHashMap<String, Object>tempData) {
		tempData = new LinkedHashMap<String, Object>();
		return tempData; 
	}
	
	private LinkedHashMap<String, Object> dummyGround(LinkedHashMap<String, Object> tempData, String key) {
		LinkedHashMap<String, Object> dummyData = new LinkedHashMap<String, Object>();
//		if(headIndex == (sector - 1))
//		if(headIndex < (sector - 1))
//		if(headIndex <= (sector - 1))
//		if(headIndex < sector)
		
		if(tempData.size() == headNameListSize) {
			resultList.add(tempData);
		}
		
		for(int copyIndex = 0; copyIndex < headNameList.indexOf(key); copyIndex++) {
			String dummyKey = headNameList.get(copyIndex);
			String value = tempData.get(dummyKey) + "";
			
			dummyData.put(dummyKey, value);
		}
		
		return dummyData;
	}
	
	private boolean isGroupByColumnOfKey(String key) {
		if(headNameList.indexOf(key) != -1 && headNameList.indexOf(key) < groupByNumber) {
			return true;
		} else {
			return false;
		}
	}
	
	private void setOfInfoData(Map<String, Object> record) {
		/*
		 * sector character	: ';'
		 * sector 1			: PIVOT 판단 (Y:PIVOT, N:STATIC)
		 * sector 2			: GROUP_BY 컬럼 수
		 * after sector 3	: Title Information (Grid 타이틀 / 문자열 정렬 방향 / WIDTH)
		 */
		String tempHead = record.get("HEAD") + "";
		String[] tempHeadArray = tempHead.split("\\;");
		String[] options = null;
		String str = "";
		StringBuffer sb = new StringBuffer();
		headNameList = new ArrayList<String>();
		
		try {
			if(tempHeadArray[0].equalsIgnoreCase("Y")) {
				isPivotData = true;
				groupByNumber = Integer.parseInt(tempHeadArray[1] + "");
			}
			
			for(int tempIndex = 2; tempIndex < tempHeadArray.length; tempIndex++) {
				str = tempHeadArray[tempIndex];
				options = str.split("\\/");
				updateHeadNameList(options[0]);
				
				if(!isPivotData) {
					options = replaceOptions(options);
				}
				
				sb.append(options[0]).append("/").append(options[1]).append("/").append(options[2]).append(tempIndex < (tempHeadArray.length - 1) ? ";" : "");
			}
			
			head = sb.toString();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
//	private String[] replaceOptions(String[] options) {
//		switch(options[1]) {
//		case "C":
//			options[1] = "center";
//			break;
//		case "L":
//			options[1] = "left";
//			break;
//		case "R":
//			options[1] = "right";
//			break;
//		}
//		
//		switch(options[2]) {
//		default:
//			options[2] = options[2].concat("px");
//			break;
//		}
//		
//		return options;
//	}
	private String[] replaceOptions(String[] options) {
		if(options[1].equals("C")) {
			options[1] = "center";
		}else if(options[1].equals("L")) {
			options[1] = "left";
		}else if(options[1].equals("R")) {
			options[1] = "right";
		}

		options[2] = options[2].concat("px");
		
		return options;
	}
	
	private void updateHeadNameList(String headName) {
		headNameList.add(headName);
		headNameListSize = headNameList.size();
	}
}
