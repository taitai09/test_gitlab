package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import omc.spop.model.PerfGuide;
import omc.spop.model.PerfGuideAttachFile;
import omc.spop.model.PerfGuideUse;
import omc.spop.model.TuningTargetSqlBind;

/***********************************************************
 * 2018.03.08 이원식 OPENPOP V2 최초작업
 **********************************************************/

public interface PrecedentService {
	/** 사례/가이드 리스트 */
	List<PerfGuide> sqlTuningGuideList(PerfGuide perfGuide) throws Exception;

	List<LinkedHashMap<String, Object>> sqlTuningGuideList4Excel(PerfGuide perfGuide);

	/** 사례/가이드 리스트 */
	List<PerfGuide> precedentList(PerfGuide perfGuide) throws Exception;

	List<LinkedHashMap<String, Object>> precedentList4Excel(PerfGuide perfGuide);

	/** 사례/가이드 사용내역 저장 */
	String updatePrecedentUse(PerfGuide perfGuide) throws Exception;

	/** 첨부파일 다운로드 여부 저장 */
	void updatePerfGuideUse(PerfGuideUse perfGuideUse) throws Exception;

	/** BIND SET LIST */
	List<TuningTargetSqlBind> bindSetList(PerfGuide perfGuide) throws Exception;

	/** 사례/가이드 SQLBIND 상세 */
	List<TuningTargetSqlBind> sqlBindList(PerfGuide perfGuide) throws Exception;

	/** 사례/가이드 상세 */
	PerfGuide readPerfGuide(PerfGuide perfGuide) throws Exception;

	/** 사례/가이드 첨부파일,Singular */
	PerfGuideAttachFile readPerfGuideFile(PerfGuide perfGuide) throws Exception;

	/** 사례/가이드 첨부파일,Multiple */
	List<PerfGuideAttachFile> readPerfGuideFiles(PerfGuide perfGuide) throws Exception;

	/** 사례/가이드 INSERT single file */
	void insertPrecedentGuide(MultipartFile file, PerfGuide perfGuide) throws Exception;

	/** 사례/가이드 INSERT multiple file */
	void insertPrecedentGuide(MultipartHttpServletRequest multipartRequest, PerfGuide perfGuide) throws Exception;

	/** 사례/가이드 single file UPDATE */
	void updatePrecedentGuide(MultipartFile file, PerfGuide perfGuide) throws Exception;

	/** 사례/가이드 multiple file UPDATE */
	void updatePrecedentGuide(MultipartHttpServletRequest multipartRequest, PerfGuide perfGuide) throws Exception;

	/** 사례/가이드 DELETE */
	void deletePrecedentGuide(PerfGuide perfGuide) throws Exception;

	int deletePerfGuideAttachFile(PerfGuideAttachFile perfGuideAttachFile);

}
