package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.PerfGuide;
import omc.spop.model.PerfGuideAttachFile;
import omc.spop.model.PerfGuideUse;
import omc.spop.model.TuningTargetSqlBind;

/***********************************************************
 * 2018.03.08 이원식 OPENPOP V2 최초작업
 **********************************************************/

public interface PrecedentDao {
	public List<PerfGuide> sqlTuningGuideList(PerfGuide perfGuide);

	public List<PerfGuide> precedentList(PerfGuide perfGuide);

	public void updatePerfGuideRetvCnt(PerfGuide perfGuide);

	public String getMaxUseSeq(PerfGuide perfGuide);

	public void insertPerfGuideUse(PerfGuideUse perfGuideUse);

	public List<TuningTargetSqlBind> bindSetList(PerfGuide perfGuide);

	public List<TuningTargetSqlBind> sqlBindList(PerfGuide perfGuide);

	public PerfGuide readPerfGuide(PerfGuide perfGuide);

	public PerfGuideAttachFile readPerfGuideFile(PerfGuide perfGuide);

	public List<PerfGuideAttachFile> readPerfGuideFiles(PerfGuide perfGuide);

	public void updatePerfGuideUse(PerfGuideUse perfGuideUse);

	public String getMaxPerfGuideNo(PerfGuide perfGuide);

	public void insertPerfGuide(PerfGuide perfGuide);

	public String getMaxGuideAttachFileSeq(PerfGuideAttachFile perfGuideAttachFile);

	public void insertPerfGuideAttachFile(PerfGuideAttachFile perfGuideAttachFile);

	public void updatePerfGuide(PerfGuide perfGuide);

	public void updatePerfGuideAttachFile(PerfGuideAttachFile perfGuideAttachFile);

	public void deletePerfGuide(PerfGuide perfGuide);

	public int deletePerfGuideAttachFile(PerfGuideAttachFile perfGuideAttachFile);

	public List<LinkedHashMap<String, Object>> sqlTuningGuideList4Excel(PerfGuide perfGuide);

	public List<LinkedHashMap<String, Object>> precedentList4Excel(PerfGuide perfGuide);

}
