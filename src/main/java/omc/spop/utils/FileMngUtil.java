package omc.spop.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import omc.spop.base.Config;
import omc.spop.model.Board;
import omc.spop.model.PerfGuide;
import omc.spop.model.PerfGuideAttachFile;
import omc.spop.model.SqlTuningAttachFile;
import omc.spop.model.TuningTargetSql;

@Component("FileMngUtil")
public class FileMngUtil {
	private static final Logger logger = LoggerFactory.getLogger(FileMngUtil.class);
	
	public String upload(MultipartFile file, String file_no) throws Exception{		
		String fileSavePath = "";
		String filePath = "";
		String newName = "";
		 
		fileSavePath = Config.getString("upload.file.dir");
		 
		File saveFolder = new File(fileSavePath);
		
		if (!saveFolder.exists() || saveFolder.isFile()) {
			saveFolder.mkdirs();
		}
		 
		String orginFileName = file.getOriginalFilename();
		 
		if ("".equals(orginFileName)) {
			return "";
		}else{
			int index = orginFileName.lastIndexOf(".");
			String fileExt = orginFileName.substring(index + 1);
			 
			newName = file_no + "." + fileExt.toLowerCase(); 
			 
			filePath = fileSavePath + newName;
			//이클립스에서 transferTo 메소드는 
			// upload.file.dir=/home/openpop/dev_home/web/ROOT/upload/file/ 로 정의된 property를
			//C:\home\openpop\dev_home\web\ROOT\\upload\file\201902070002.sql 경로로 정상으로 인식시키지 못하고
			//Caused by: java.io.FileNotFoundException: C:\OMCPROJECT\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp1\work\Catalina\localhost\ROOT\home\openpop\dev_home\web\ROOT\\upload\file\201902070002.sql (지정된 경로를 찾을 수 없습니다)
			//오류를 발생시키므로 FileOutputStream으로 변경
			//file.transferTo(new File(filePath));

			File convFile = new File(filePath);
			convFile.createNewFile(); 
			FileOutputStream fos = new FileOutputStream(convFile); 
			fos.write(file.getBytes());
			fos.close(); 
			
			return newName;
		}
	}
	
	public PerfGuideAttachFile uploadGuide(MultipartFile file, PerfGuide perfGuide) throws Exception{		
		PerfGuideAttachFile temp = new PerfGuideAttachFile();
		String fileSavePath = "";
		String filePath = "";
		String newName = "";
		 
		fileSavePath = Config.getString("upload.guide.dir");
		 
		File saveFolder = new File(fileSavePath);
		
		if (!saveFolder.exists() || saveFolder.isFile()) {
			saveFolder.mkdirs();
		}
		 
		String orginFileName = file.getOriginalFilename();
		 
		if ("".equals(orginFileName)) {
			return temp;
		}else{
			long _size = file.getSize();
			int index = orginFileName.lastIndexOf(".");
			String fileExt = orginFileName.substring(index + 1);
			 
			newName = DateUtil.getNowDate("yyMMddHHmmssSSS") + "." + fileExt.toLowerCase(); 
			 
			filePath = fileSavePath + newName;
			//이클립스에서 transferTo 메소드는 
			// upload.guide.dir=/home/openpop/dev_home/web/ROOT/upload/guide/ 로 정의된 property를
			//C:\home\openpop\dev_home\web\ROOT\\upload\guide\190207142009611.pdf 경로로 정상으로 인식시키지 못하고
			//Caused by: java.io.FileNotFoundException: C:\OMCPROJECT\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp1\work\Catalina\localhost\ROOT\home\openpop\dev_home\web\ROOT\\upload\guide\190207142009611.pdf (지정된 경로를 찾을 수 없습니다)
			//오류를 발생시키므로 FileOutputStream으로 변경
			//file.transferTo(new File(filePath));

			File convFile = new File(filePath);
			convFile.createNewFile(); 
			FileOutputStream fos = new FileOutputStream(convFile); 
			fos.write(file.getBytes());
			fos.close(); 
			
			temp.setFile_nm(newName);
			temp.setOrg_file_nm(orginFileName);
			temp.setFile_size(Long.toString(_size));
			temp.setFile_ext_nm(fileExt);
				
			return temp;
		}
	}
	
	public Board uploadBoard(MultipartFile file, Board board) throws Exception{		
		Board temp = new Board();
		String fileSavePath = "";
		String filePath = "";
		String newName = "";
		 
		fileSavePath = Config.getString("upload.board.dir");
		 
		File saveFolder = new File(fileSavePath);
		
		if (!saveFolder.exists() || saveFolder.isFile()) {
			saveFolder.mkdirs();
		}
		 
		String orginFileName = file.getOriginalFilename();
		 
		if ("".equals(orginFileName)) {
			return temp;
		}else{
			long _size = file.getSize();
			int index = orginFileName.lastIndexOf(".");
			String fileExt = orginFileName.substring(index + 1);
			 
			newName = DateUtil.getNowDate("yyMMddHHmmssSSS") + "." + fileExt.toLowerCase(); 
			 
			filePath = fileSavePath + newName;
			//이클립스에서 transferTo 메소드는 
			// upload.file.dir=/home/openpop/dev_home/web/ROOT/upload/file/ 로 정의된 property를
			//C:\home\openpop\dev_home\web\ROOT\\upload\file\201902070002.sql 경로로 정상으로 인식시키지 못하고
			//Caused by: java.io.FileNotFoundException: C:\OMCPROJECT\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp1\work\Catalina\localhost\ROOT\home\openpop\dev_home\web\ROOT\\upload\file\201902070002.sql (지정된 경로를 찾을 수 없습니다)
			//오류를 발생시키므로 FileOutputStream으로 변경
			//file.transferTo(new File(filePath));

			File convFile = new File(filePath);
			convFile.createNewFile(); 
			FileOutputStream fos = new FileOutputStream(convFile); 
			fos.write(file.getBytes());
			fos.close(); 
			
			temp.setFile_nm(newName);
			temp.setOrg_file_nm(orginFileName);
			temp.setFile_size(Long.toString(_size));
			temp.setFile_ext_nm(fileExt);
			temp.setBoard_mgmt_no(board.getBoard_mgmt_no());
			
			return temp;
		}
	}	
	
	public String uploadExcel(MultipartFile file) throws Exception{		
		String fileSavePath = "";
		String filePath = "";
		String newName = "";
		 
		fileSavePath = Config.getString("upload.excel.dir");
		 
		File saveFolder = new File(fileSavePath);
		
		if (!saveFolder.exists() || saveFolder.isFile()) {
			saveFolder.mkdirs();
		}
		 
		String orginFileName = file.getOriginalFilename();
		 
		if ("".equals(orginFileName)) {
			return "";
		}else{
			int index = orginFileName.lastIndexOf(".");
			String fileExt = orginFileName.substring(index + 1);
			 
			newName = DateUtil.getNowDate("yyMMddHHmmssSSS") + "." + fileExt.toLowerCase(); 
			 
			filePath = fileSavePath + newName;
			//이클립스에서 transferTo 메소드는 
			// upload.file.dir=/home/openpop/dev_home/web/ROOT/upload/file/ 로 정의된 property를
			//C:\home\openpop\dev_home\web\ROOT\\upload\file\201902070002.sql 경로로 정상으로 인식시키지 못하고
			//Caused by: java.io.FileNotFoundException: C:\OMCPROJECT\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp1\work\Catalina\localhost\ROOT\home\openpop\dev_home\web\ROOT\\upload\file\201902070002.sql (지정된 경로를 찾을 수 없습니다)
			//오류를 발생시키므로 FileOutputStream으로 변경
			//file.transferTo(new File(filePath));

			File convFile = new File(filePath);
			convFile.createNewFile(); 
			FileOutputStream fos = new FileOutputStream(convFile); 
			fos.write(file.getBytes());
			fos.close(); 
			
			return filePath;
		}
	}
	
	public SqlTuningAttachFile uploadTuningFile(MultipartFile file, TuningTargetSql tuningTargetSql) throws Exception{		
		SqlTuningAttachFile temp = new SqlTuningAttachFile();
		String fileSavePath = "";
		String filePath = "";
		String newName = "";
		
		fileSavePath = Config.getString("upload.file.dir");
		 
		File saveFolder = new File(fileSavePath);
		
		if (!saveFolder.exists() || saveFolder.isFile()) {
			saveFolder.mkdirs();
		}
		 
		String orginFileName = file.getOriginalFilename();
		if( orginFileName.contains(Character.toString((char)92)) ){
			orginFileName = orginFileName.substring(orginFileName.lastIndexOf( Character.toString( (char)92) ) + 1 );
		}
		
		if ("".equals(orginFileName)) {
			return temp;
		}else{
			long _size = file.getSize();
			int index = orginFileName.lastIndexOf(".");
			String fileExt = orginFileName.substring(index + 1);
			 
			newName = DateUtil.getNowDate("yyMMddHHmmssSSS") + "." + fileExt.toLowerCase(); 
			 
			filePath = fileSavePath + newName;
			//이클립스에서 transferTo 메소드는 
			// upload.tuningFile.dir=/home/openpop/dev_home/web/ROOT/upload/tuningFile/ 로 정의된 property를
			//C:\home\openpop\dev_home\web\ROOT\\upload\guide\190207142009611.pdf 경로로 정상으로 인식시키지 못하고
			//Caused by: java.io.FileNotFoundException: C:\OMCPROJECT\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp1\work\Catalina\localhost\ROOT\home\openpop\dev_home\web\ROOT\\upload\guide\190207142009611.pdf (지정된 경로를 찾을 수 없습니다)
			//오류를 발생시키므로 FileOutputStream으로 변경
			//file.transferTo(new File(filePath));

			File convFile = new File(filePath);
			convFile.createNewFile(); 
			FileOutputStream fos = new FileOutputStream(convFile); 
			fos.write(file.getBytes());
			fos.close(); 
			
			temp.setFile_nm(newName);
			temp.setOrg_file_nm(orginFileName);
			temp.setFile_size(Long.toString(_size));
			temp.setFile_ext_nm(fileExt);
			
			return temp;
		}
	}

	public static List<String> fileLineRead(String name) throws IOException{
		List<String> retStr = new ArrayList<String>();
		
		File f = new File(name);
		FileInputStream fis = null;
		fis = new FileInputStream(f);
		 
		byte[] byteData = new byte[(int) f.length()];
		 
		fis.read(byteData);
		fis.close();	
		
		CharsetDetector cd = new CharsetDetector();
		cd.setText(byteData);
		CharsetMatch cm = cd.detect();

		String charset="UTF-8";
		if (cm != null) {
		   charset = cm.getName();
		}		
		if(!charset.equals("UTF-8")){
			charset = "EUC-KR";
		}
		//BufferedReader in = new BufferedReader(new FileReader(name));
		BufferedReader in = new BufferedReader( new InputStreamReader( new FileInputStream(name), charset));		
		String s;
		while ((s = in.readLine()) != null) {
			retStr.add(s);
		}
		in.close();
		return retStr;  
	}
	
	/** 산출물 다운로드 통합 탬플릿*/
	public void makePerformanceImprovementOutputs( String outputsRoot, String fileName, TuningTargetSql targetSql, TuningTargetSql improvements) throws Exception{
		BufferedWriter writer = null;
		File saveFolder = new File(outputsRoot);
		
		if ( !saveFolder.exists() || saveFolder.isFile() ) {
			saveFolder.mkdirs();
		}
		
		StringBuffer strbuf = new StringBuffer();
		try {
			
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputsRoot + fileName), "euc-kr"));
			writer.write("\r\n기본정보\r\n");
			writer.write(rightPad("",140,"-")+"\r\n");
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("튜닝번호",24,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + rightPad( StringUtils.defaultString(targetSql.getTuning_no(),""),24," "));
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("요청자",23,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + rightPad( StringUtils.defaultString(targetSql.getTuning_requester_nm(),""),22," "));
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("담당튜너",20,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + StringUtils.defaultString(targetSql.getPerfr_nm(),"")+"\r\n");
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("요청일",23,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + rightPad( StringUtils.defaultString(targetSql.getTuning_request_dt(),""),24," "));
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("완료일",23,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + rightPad( StringUtils.defaultString(targetSql.getTuning_complete_dt(),""),22," "));
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("적용일",19,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + StringUtils.defaultString(targetSql.getTuning_apply_dt(),"")+"\r\n");
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("업무담당자",25,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + rightPad( StringUtils.defaultString(targetSql.getWrkjob_mgr_nm(),""),24," "));
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("담당업무",24,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + rightPad( StringUtils.defaultString(targetSql.getWrkjob_mgr_wrkjob_nm(),""),22," "));
			strbuf.setLength(0);
			/*
			strbuf.append("▶ ").append( fixLength("업무담당자연락처",24,' ',true) ).append(" : ");	// 2021.12.20 보안 프로그램의 전화번호 패턴에 잡혀 보안 위배 파일로 도출되어 항목 삭제
			writer.write( strbuf.toString() + StringUtils.defaultString(targetSql.getWrkjob_mgr_tel_num(),"")+"\r\n");
			*/
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("요청유형",24,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + StringUtils.defaultString(targetSql.getChoice_div_cd_nm(),"")+"\r\n");
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("업무특이사항",26,' ',true) );
			writer.write( strbuf.toString()+"\r\n" + StringUtils.defaultString(targetSql.getWrkjob_peculiar_point(),"")+"\r\n\r\n");
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("요청사유",24,' ',true) );
			writer.write( strbuf.toString()+"\r\n" + StringUtils.defaultString(targetSql.getRequest_why(),"")+"\r\n\r\n");
			
			writer.write("\r\n프로그램 정보\r\n");
			writer.write(rightPad("",140,"-")+"\r\n");
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("프로그램유형",26,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + rightPad( StringUtils.defaultString(targetSql.getProgram_type_cd_nm(),""),24," "));
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("배치작업주기",26,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + StringUtils.defaultString(targetSql.getBatch_work_div_cd_nm(),"")+"\r\n");
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("수행시간(초)",25,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + rightPad( StringUtils.defaultString(
					targetSql.getCurrent_elap_time()==null?"0":targetSql.getCurrent_elap_time().toString(),""),24," "));
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("결과건수",24,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + rightPad( StringUtils.defaultString(targetSql.getForecast_result_cnt(),""),22," "));
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("목표수행시간(초)",23,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + StringUtils.defaultString(targetSql.getGoal_elap_time(),"")+"\r\n\r\n");
			
			writer.write("\r\nSQL 정보\r\n");
			writer.write(rightPad("",140,"-")+"\r\n");
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("DB",20,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + rightPad( StringUtils.defaultString(targetSql.getDb_name(),""),24," "));
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("SQL_ID",20,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + rightPad( StringUtils.defaultString(targetSql.getSql_id(),""),22," "));
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("PLAN_HASH_VALUE",16,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + rightPad( StringUtils.defaultString(targetSql.getPlan_hash_value(),""),22," "));
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("PARSING_SCHEMA_NAME",20,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + StringUtils.defaultString(targetSql.getParsing_schema_name(),"")+"\r\n");
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("MODULE",20,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + StringUtils.defaultString(targetSql.getModule(),"")+"\r\n");
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("소스파일명",25,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + StringUtils.defaultString(targetSql.getTr_cd(),"")+"\r\n");
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("SQL식별자",23,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + StringUtils.defaultString(targetSql.getDbio(),"")+"\r\n\r\n");
			
			writer.write("\r\n개선 결과\r\n");
			writer.write(rightPad("",140,"-")+"\r\n");
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("개선전 수행시간(초)",28,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + rightPad( StringUtils.defaultString(targetSql.getImprb_elap_time(),""),24," "));
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("개선전 블럭수",26,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + rightPad( StringUtils.defaultString(targetSql.getImprb_buffer_cnt(),""),22," "));
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("개선전 PGA사용량(MB)",29,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + StringUtils.defaultString(targetSql.getImprb_pga_usage(),"")+"\r\n");
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("개선후 수행시간(초)",28,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + rightPad( StringUtils.defaultString(targetSql.getImpra_elap_time(),""),24," "));
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("개선후 블럭수",26,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + rightPad( StringUtils.defaultString(targetSql.getImpra_buffer_cnt(),""),22," "));
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("개선후 PGA사용량(MB)",29,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + StringUtils.defaultString(targetSql.getImpra_pga_usage(),"")+"\r\n");
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("수행시간 개선율(%)",27,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + rightPad( StringUtils.defaultString(targetSql.getElap_time_impr_ratio(),""),24," "));
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("블럭수 개선율(%)",26,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + rightPad( StringUtils.defaultString(targetSql.getBuffer_impr_ratio(),""),22," "));
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("PGA 개선율(%)",26,' ',true) ).append(" : ");
			writer.write( strbuf.toString() + StringUtils.defaultString(targetSql.getPga_impr_ratio(),"")+"\r\n");
			
			writer.write("\r\n개선 상세\r\n");
			writer.write(rightPad("",140,"-")+"\r\n");
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("문제점",24,' ',true) );
			writer.write( strbuf.toString()+"\r\n" + removeHTML( removeSpecialChar( StringUtils.defaultString(targetSql.getControversialist(),"")) )+"\r\n\r\n");
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("개선 내용",24,' ',true) );
			writer.write( strbuf.toString()+"\r\n" + removeHTML( removeSpecialChar( StringUtils.defaultString(targetSql.getImpr_sbst(),"")) )+"\r\n\r\n");
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("원본 SQL",24,' ',true) );
			writer.write( strbuf.toString()+"\r\n" + removeHTML( removeSpecialChar( StringUtils.defaultString(targetSql.getSql_text(),"")) )+"\r\n\r\n");
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("개선 SQL",24,' ',true) );
			writer.write( strbuf.toString()+"\r\n" + StringEscapeUtils.unescapeHtml( removeSpecialChar( removeSpecialChar( removeHTML( StringUtils.defaultString(targetSql.getImpr_sql_text(),""))) ))+"\r\n\r\n\r\n");
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("개선전 실행계획",24,' ',true) );
			writer.write( strbuf.toString()+"\r\n" + StringEscapeUtils.unescapeHtml( removeSpecialChar( removeSpecialChar( removeHTML( StringUtils.defaultString(targetSql.getImprb_exec_plan(),""))) ))+"\r\n\r\n\r\n");
			strbuf.setLength(0);
			strbuf.append("▶ ").append( fixLength("개선후 실행계획",24,' ',true) );
			writer.write( strbuf.toString()+"\r\n" + StringEscapeUtils.unescapeHtml( removeSpecialChar( removeSpecialChar( removeHTML( StringUtils.defaultString(targetSql.getImpra_exec_plan(),""))) ))+"\r\n\r\n\r\n");
			
		} catch( Exception e ) {
			logger.error("FILE WRITE ERROR ==> " + e.getMessage());
			e.printStackTrace();
		} finally {
			strbuf = null;
			try { if (writer != null) writer.close(); } catch( Exception e ) { logger.error("FILE WRITE ERROR ==> " + e.getMessage()); }
		}
	}
	
	public void makeZipFile(String outputsRoot, String fileName, String[] fileNameArry) throws Exception{
		byte[] buf = new byte[1024];
		  
		try{
			// 압축파일명
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputsRoot + fileName));
			
			// 파일 압축
			for (int i = 0 ; i < fileNameArry.length; i++) {
				FileInputStream in = new FileInputStream(outputsRoot + fileNameArry[i]);
				
				// 압축 항목추가
				out.putNextEntry(new ZipEntry(fileNameArry[i]));
				
				// 바이트 전송
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				
				out.closeEntry();
				in.close();
			}
			
			// 압축파일 작성
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * String 의 길이 를 체크 한 후 원하는 길이가 되도록  원하는 문자를 오른쪽에 채움 
	 * @param data
	 * @return
	 */
	public String rightPad( String str, int len, String strFillText ) {
		try {
			if ( str.length() != str.getBytes("euc-kr").length ) {
				String[] strArr = str.split("");
				
				for (int lengIdx = 0; lengIdx < strArr.length; lengIdx++) {
					if ( strArr[lengIdx].getBytes("euc-kr").length == 1 ) {
						len += 1;
					};
				}
				len = len-str.length();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for ( int i = str.length() ; i < len ; i++ ) {
			str += strFillText;
		}
		
		return str;
	}
	
	/**
	 * 특수문자 제거
	 *
	 * @param strText  특수문자가 포함된 문자열
	 * @return 문자열
	 */
	private String removeSpecialChar(String strText) {
		String returnValue = strText;
		
		if ( strText != null && "".equals( strText ) == false ) {
			returnValue = returnValue.replaceAll("(<P>)|(</P>)","");
			returnValue = returnValue.replaceAll("&lt;","<");
			returnValue = returnValue.replaceAll("&gt;",">");
			returnValue = returnValue.replaceAll("&amp;","&");
			returnValue = returnValue.replaceAll("&quot;","\"");
			returnValue = returnValue.replaceAll("&#035;","#");
			
		}
		
		return returnValue;
	}
	
	/**
	 * HTML 테크 제거후 문자열 리턴
	 *
	 * @param strText  HTML이 포함된 문자열
	 * @return 문자열
	 */
	public String removeHTML(String strText) {
		String returnValue;
		
		returnValue = strText;
//		returnValue = returnValue.replaceAll("\\s"," ");
		returnValue = returnValue.replaceAll("&nbsp;"," ");
		returnValue = returnValue.replaceAll("&#39;","'");
		returnValue = returnValue.replaceAll("<>","@@");
		returnValue = returnValue.replaceAll("<(\\/br|br|br\\/|br \\/| br\\/[^>]*)>","\n");
		returnValue = returnValue.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>","");
		returnValue = returnValue.replaceAll("@@","<>");
		returnValue = returnValue.replaceAll("\n","\r\n");
		returnValue = returnValue.replaceAll("\r\r\n","\r\n");
		returnValue = returnValue.replaceAll("\r\n\r\n","\r\n");
		
		return returnValue;
	}
	/**
	 * String byte로 변환하여 byte길이로 length까지 문자 채움
	 * true면 오른쪽채움 false면 왼쪽채움
	 * */
	public static String fixLength(String str, int length, char ch, boolean leftAlign) {
		str = str != null ? str : "";
		
		byte newBytes[] = new byte[length];
		byte strBytes[] = str.getBytes();
		int len = strBytes.length;
		
		if (len == length) {
			return str;
		}
		
		byte chByte = (byte) ch;
		
		if (len > length) {
			System.arraycopy(strBytes, 0, newBytes, 0, length);
		} else if (leftAlign) {
			System.arraycopy(strBytes, 0, newBytes, 0, len);
			
			for (int i = len; i < length; i++) {
				newBytes[i] = chByte;
			}
		} else {
			int temp = length - len;
			
			for (int i = 0; i < temp; i++) {
				newBytes[i] = chByte;
			}
			System.arraycopy(strBytes, 0, newBytes, temp, len);
		}

	return new String(newBytes);

	}
}