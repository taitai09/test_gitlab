package omc.spop.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import omc.spop.model.BeforeAccidentCheck;
import omc.spop.model.DeployPerfCheck;

/***********************************************************
 * 2018.03.14	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface PreDeploymentCheckService {	
	/** 소스점검 리스트 */
	List<BeforeAccidentCheck> sourceCheckList(BeforeAccidentCheck beforeAccidentCheck) throws Exception;
	
	/** 소스점검 상세 리스트 */
	BeforeAccidentCheck sourceCheckView(BeforeAccidentCheck beforeAccidentCheck) throws Exception;
	
	/** 소스점검 - 점검결과 UPDATE */
	void updateSourceCheck(BeforeAccidentCheck beforeAccidentCheck) throws Exception;	
	
	/** 애플리케이션 성능점검 리스트 */
	List<DeployPerfCheck> applicationCheckList(DeployPerfCheck deployPerfCheck) throws Exception;
	
	/** 애플리케이션 성능점검 요청 */
	void applicationRequestCheck(DeployPerfCheck deployPerfCheck) throws Exception;
	
	/** 애플리케이션 성능점검 상세 리스트 */
	List<DeployPerfCheck> applicationDetailCheckList(DeployPerfCheck deployPerfCheck) throws Exception;
	
	/** 애플리케이션 DBIO 성능점검 상세 리스트 */
	List<DeployPerfCheck> applicationDBIOCheckList(DeployPerfCheck deployPerfCheck) throws Exception;
	
	/** DBIO 성능점검 리스트 */
	List<DeployPerfCheck> dbioCheckList(DeployPerfCheck deployPerfCheck) throws Exception;
	
	/** DBIO 성능점검 요청 */
	void dbioRequestCheck(DeployPerfCheck deployPerfCheck) throws Exception;	
	
	/** DBIO 성능점검 상세 리스트 */
	List<DeployPerfCheck> dbioDetailCheckList(DeployPerfCheck deployPerfCheck) throws Exception;
	
	/** 성능점검등록 엑셀 업르도 및 리스트 조회 */
	List<DeployPerfCheck> uploadExcelPerformanceFile(MultipartFile file) throws Exception;
	
	/** 성능점검등록 저장 */
	int insertExcelPerformanceCheck(HttpServletRequest req) throws Exception;	
}
