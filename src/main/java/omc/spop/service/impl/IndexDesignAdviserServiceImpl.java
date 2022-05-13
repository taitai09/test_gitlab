package omc.spop.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.IndexDesignAdviserDao;
import omc.spop.model.AccPathExec;
import omc.spop.model.AccPathExecV2;
import omc.spop.model.IdxAdMst;
import omc.spop.model.IdxAdRecommendIndex;
import omc.spop.server.tune.SPopIndexAutoDesign;
import omc.spop.service.IndexDesignAdviserService;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.03.20	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Service("IndexDesignAdviserService")
public class IndexDesignAdviserServiceImpl implements IndexDesignAdviserService {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexDesignAdviserServiceImpl.class);
	
	@Autowired
	private IndexDesignAdviserDao indexDesignAdviserDao;

	@Override
	public List<IdxAdMst> autoIndexStatusList(IdxAdMst idxAdMst) throws Exception {
		return indexDesignAdviserDao.autoIndexStatusList(idxAdMst);
	}

	@Override
	public List<LinkedHashMap<String, Object>> autoIndexStatusList4Excel(IdxAdMst idxAdMst) throws Exception {
		return indexDesignAdviserDao.autoIndexStatusList4Excel(idxAdMst);
	}
	
	@Override
	public List<IdxAdRecommendIndex> indexRecommendStatusList(IdxAdRecommendIndex idxAdRecommendIndex) throws Exception {
		return indexDesignAdviserDao.indexRecommendStatusList(idxAdRecommendIndex);
	}	
	
	@Override
	public int updateForceComplete(Map<String, List<String>> param) throws Exception {
		return indexDesignAdviserDao.updateForceComplete(param);
	}
	
	@Override
	public int updateForceCompleteList(List<?> paramList) throws Exception {
		return indexDesignAdviserDao.updateForceCompleteList(paramList);
	}
	
	@Override
	public List<AccPathExecV2> autoCollectionIndexDesignList(AccPathExec accPathExec) throws Exception {
		return indexDesignAdviserDao.autoCollectionIndexDesignList(accPathExec);
	}
	
	@Override
	public List<AccPathExecV2> autoLoadIndexDesignList(AccPathExec accPathExec) throws Exception {
		//return indexDesignAdviserDao.autoLoadIndexDesignList(accPathExec);
		List<AccPathExecV2> list = indexDesignAdviserDao.autoLoadIndexDesignList(accPathExec);
		logger.debug("=== autoLoadIndexDesignList ==================");
		logger.debug(list + "");
		
		return list;
	}
	
	@Override
	public List<AccPathExecV2> isTaskStartIndexAutoDesign(AccPathExec accPathExec) throws Exception {
		return indexDesignAdviserDao.isTaskStartIndexAutoDesign(accPathExec);
	}
	
	@Override
	public void startIndexAutoDesign(AccPathExec accPathExec) throws Exception {
		try{
			SPopIndexAutoDesign.startIndexDesign(StringUtil.parseLong(accPathExec.getDbid(), 0),
					StringUtil.parseLong(accPathExec.getExec_seq().toString(), 0), 
					accPathExec.getTable_owner(), 
					null,
					StringUtil.parseDouble(accPathExec.getNdv_ratio(), 0),
					StringUtil.parseLong(accPathExec.getCol_null(), 0));
		}catch(Exception ex){
			logger.error("error ==> " + ex.getMessage());
			throw ex;
		}		
	}

	@Override
	public List<LinkedHashMap<String, Object>> autoCollectionIndexDesignList4Excel(AccPathExec accPathExec) {
		return indexDesignAdviserDao.autoCollectionIndexDesignList4Excel(accPathExec);
	}

	@Override
	public void startIndexAutoDesign2(AccPathExec accPathExec) throws Exception {
		try{
			SPopIndexAutoDesign.startIndexDesign2(StringUtil.parseLong(accPathExec.getDbid(), 0),
					StringUtil.parseLong(accPathExec.getExec_seq().toString(), 0), 
					accPathExec.getTable_owner(), 
					null,
					StringUtils.defaultString(accPathExec.getAccess_path_type()),
					accPathExec.getSelectivity_calc_method());
		}catch(Exception ex){
			logger.error("error ==> " + ex.getMessage());
			throw ex;
		}
	}
	
	 
}