package omc.spop.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import omc.spop.base.SessionManager;
import omc.spop.dao.PrecedentDao;
import omc.spop.model.PerfGuide;
import omc.spop.model.PerfGuideAttachFile;
import omc.spop.model.PerfGuideUse;
import omc.spop.model.TuningTargetSqlBind;
import omc.spop.service.PrecedentService;
import omc.spop.utils.FileMngUtil;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.03.08 이원식 OPENPOP V2 최초작업
 **********************************************************/

@Service("PrecedentService")
public class PrecedentServiceImpl implements PrecedentService {

	private static final Logger logger = LoggerFactory.getLogger(PrecedentServiceImpl.class);

	@Autowired
	private PrecedentDao precedentDao;

	@Override
	public List<PerfGuide> sqlTuningGuideList(PerfGuide perfGuide) throws Exception {
		return precedentDao.sqlTuningGuideList(perfGuide);
	}

	@Override
	public List<LinkedHashMap<String, Object>> sqlTuningGuideList4Excel(PerfGuide perfGuide) {
		return precedentDao.sqlTuningGuideList4Excel(perfGuide);
	}
	
	@Override
	public List<PerfGuide> precedentList(PerfGuide perfGuide) throws Exception {
		return precedentDao.precedentList(perfGuide);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> precedentList4Excel(PerfGuide perfGuide) {
		return precedentDao.precedentList4Excel(perfGuide);
	}

	@Override
	public String updatePrecedentUse(PerfGuide perfGuide) throws Exception {
		PerfGuideUse temp = new PerfGuideUse();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String wrkjob_cd = StringUtil.nvl(SessionManager.getLoginSession().getUsers().getWrkjob_cd(), "");

		// 1. PERF_GUIDE RETV_CNT UPDATE
		precedentDao.updatePerfGuideRetvCnt(perfGuide);

		// 2. PERF_GUIDE_USE USE_SEQ 가져오기
		String maxUseSeq = precedentDao.getMaxUseSeq(perfGuide);

		// 3. PERF_GUIDE_USE INSERT
		temp.setGuide_no(perfGuide.getGuide_no());
		temp.setUse_seq(maxUseSeq);
		temp.setWrkjob_cd(wrkjob_cd);
		temp.setRetv_user_id(user_id);

		precedentDao.insertPerfGuideUse(temp);

		return maxUseSeq;
	}

	@Override
	public List<TuningTargetSqlBind> bindSetList(PerfGuide perfGuide) throws Exception {
		return precedentDao.bindSetList(perfGuide);
	}

	@Override
	public List<TuningTargetSqlBind> sqlBindList(PerfGuide perfGuide) throws Exception {
		return precedentDao.sqlBindList(perfGuide);
	}

	@Override
	public PerfGuide readPerfGuide(PerfGuide perfGuide) throws Exception {
		return precedentDao.readPerfGuide(perfGuide);
	}

	@Override
	public PerfGuideAttachFile readPerfGuideFile(PerfGuide perfGuide) throws Exception {
		return precedentDao.readPerfGuideFile(perfGuide);
	}

	@Override
	public List<PerfGuideAttachFile> readPerfGuideFiles(PerfGuide perfGuide) throws Exception {
		return precedentDao.readPerfGuideFiles(perfGuide);
	}

	@Override
	public void updatePerfGuideUse(PerfGuideUse perfGuideUse) throws Exception {
		precedentDao.updatePerfGuideUse(perfGuideUse);
	}

	/**
	 * single file upload
	 */
	@Override
	public void insertPrecedentGuide(MultipartFile file, PerfGuide perfGuide) throws Exception {
		FileMngUtil fileMng = new FileMngUtil();
		PerfGuideAttachFile perfGuideAttachFile = new PerfGuideAttachFile();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String fileSeq = "";
		String guideNo = "";

		// 1-1. PERF_GUIDE MAX GUIDE_NO 조회
		guideNo = precedentDao.getMaxPerfGuideNo(perfGuide);

		perfGuide.setGuide_no(guideNo);
		perfGuide.setReg_user_id(user_id);
		// 1-2. PERF_GUIDE INSERT
		precedentDao.insertPerfGuide(perfGuide);

		if (!file.isEmpty()) {
			// 2. 파일 업로드 정보 조회
			try {
				perfGuideAttachFile = fileMng.uploadGuide(file, perfGuide);
				perfGuideAttachFile.setGuide_no(guideNo);
			} catch (Exception ex) {
				logger.error("error => " + ex.getMessage());
				ex.printStackTrace();
				throw ex;
			}
			// 2-1. 파일 존재시 PERF_GUIDE_ATTACH_FILE MAX FILE_SEQ 조회
			fileSeq = precedentDao.getMaxGuideAttachFileSeq(perfGuideAttachFile);

			// 2-2. 파일 존재시 PERF_GUIDE_ATTACH_FILE INSERT
			perfGuideAttachFile.setFile_seq(fileSeq);
			precedentDao.insertPerfGuideAttachFile(perfGuideAttachFile);
		}
	}

	/**
	 * multiple file upload
	 * 
	 * @throws Exception
	 */
	@Override
	public void insertPrecedentGuide(MultipartHttpServletRequest multipartRequest, PerfGuide perfGuide)
			throws Exception {
		FileMngUtil fileMng = new FileMngUtil();
		PerfGuideAttachFile perfGuideAttachFile = new PerfGuideAttachFile();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String fileSeq = "";
		String guideNo = "";

		// 1-1. PERF_GUIDE MAX GUIDE_NO 조회
		guideNo = precedentDao.getMaxPerfGuideNo(perfGuide);

		perfGuide.setGuide_no(guideNo);
		perfGuide.setReg_user_id(user_id);
		// 1-2. PERF_GUIDE INSERT
		precedentDao.insertPerfGuide(perfGuide);
		List<MultipartFile> fileList = multipartRequest.getFiles("uploadFile");
		for (MultipartFile file : fileList) {
			if (!file.isEmpty()) {
				// 2. 파일 업로드 정보 조회
				try {
					perfGuideAttachFile = fileMng.uploadGuide(file, perfGuide);
					perfGuideAttachFile.setGuide_no(guideNo);
				} catch (Exception ex) {
					logger.error("error => " + ex.getMessage());
					ex.printStackTrace();
					throw ex;
				}
				// 2-1. 파일 존재시 PERF_GUIDE_ATTACH_FILE MAX FILE_SEQ 조회
				fileSeq = precedentDao.getMaxGuideAttachFileSeq(perfGuideAttachFile);

				// 2-2. 파일 존재시 PERF_GUIDE_ATTACH_FILE INSERT
				perfGuideAttachFile.setFile_seq(fileSeq);
				precedentDao.insertPerfGuideAttachFile(perfGuideAttachFile);
			}
		}
	}

	/**
	 * single file upload
	 * 
	 * @throws Exception
	 */
	@Override
	public void updatePrecedentGuide(MultipartFile file, PerfGuide perfGuide) throws Exception {
		FileMngUtil fileMng = new FileMngUtil();
		PerfGuideAttachFile perfGuideAttachFile = new PerfGuideAttachFile();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String fileSeq = "";

		perfGuide.setReg_user_id(user_id);
		// 1. PERF_GUIDE UPDATE
		precedentDao.updatePerfGuide(perfGuide);

		if (!file.isEmpty()) {
			// 2. 파일 업로드 정보 조회
			try {
				perfGuideAttachFile = fileMng.uploadGuide(file, perfGuide);
				perfGuideAttachFile.setGuide_no(perfGuide.getGuide_no());
			} catch (Exception ex) {
				logger.error("error => " + ex.getMessage());
				ex.printStackTrace();
				throw ex;
			}

			// 3-1. 파일 존재시 PERF_GUIDE_ATTACH_FILE MAX FILE_SEQ 조회
			fileSeq = precedentDao.getMaxGuideAttachFileSeq(perfGuideAttachFile);
			logger.debug("fileSeq1 => " + fileSeq);

			perfGuideAttachFile.setFile_seq(fileSeq);
			precedentDao.insertPerfGuideAttachFile(perfGuideAttachFile);
		}
	}

	/**
	 * multiple file upload
	 * 
	 * @throws Exception
	 */
	@Override
	public void updatePrecedentGuide(MultipartHttpServletRequest multipartRequest, PerfGuide perfGuide)
			throws Exception {
		FileMngUtil fileMng = new FileMngUtil();
		PerfGuideAttachFile perfGuideAttachFile = new PerfGuideAttachFile();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String fileSeq = "";

		perfGuide.setReg_user_id(user_id);
		perfGuide.setUpd_id(user_id);
		// 1. PERF_GUIDE UPDATE
		precedentDao.updatePerfGuide(perfGuide);
		List<MultipartFile> fileList = multipartRequest.getFiles("uploadFile");
		for (MultipartFile file : fileList) {
			if (!file.isEmpty()) {
				// 2. 파일 업로드 정보 조회
				try {
					perfGuideAttachFile = fileMng.uploadGuide(file, perfGuide);
					perfGuideAttachFile.setGuide_no(perfGuide.getGuide_no());
				} catch (Exception ex) {
					logger.error("error => " + ex.getMessage());
					ex.printStackTrace();
					throw ex;
				}

				// 3-1. 파일 존재시 PERF_GUIDE_ATTACH_FILE MAX FILE_SEQ 조회
				fileSeq = precedentDao.getMaxGuideAttachFileSeq(perfGuideAttachFile);
				logger.debug("fileSeq2 => " + fileSeq);

				perfGuideAttachFile.setFile_seq(fileSeq);
				precedentDao.insertPerfGuideAttachFile(perfGuideAttachFile);
			}
		}
	}

	@Override
	public void deletePrecedentGuide(PerfGuide perfGuide) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		perfGuide.setUpd_id(user_id);
		precedentDao.deletePerfGuide(perfGuide);
		precedentDao.deletePerfGuide(perfGuide);
	}

	@Override
	public int deletePerfGuideAttachFile(PerfGuideAttachFile perfGuideAttachFile) {
		return precedentDao.deletePerfGuideAttachFile(perfGuideAttachFile);
	}

}
