package omc.spop.service;

import omc.spop.model.TuningTargetSqlPlugIn;

/***********************************************************
 * 2021.10.18	명성태	OPENPOP V2 최초작업
 **********************************************************/

public interface ImprovementManagementPlugInService {
	/** 튜닝요청 INSERT*/
	int insertImprovement(TuningTargetSqlPlugIn tuningTargetSqlPlugIn) throws Exception;
	
}
