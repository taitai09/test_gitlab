package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import omc.spop.dao.AnalyzeDbChangeSqlImpactDao;
import omc.spop.model.AnalyzeDbChangeSqlImpact;
import omc.spop.model.Cd;
import omc.spop.model.SQLAutoPerformanceCompare;
import omc.spop.service.AnalyzeDbChangeSqlImpactService;
import omc.spop.utils.ExcelDownHandler;
import omc.spop.utils.StringUtil;
import oracle.sql.CLOB;

/***********************************************************
 * 2021.02.16	이재우	최초작성
 **********************************************************/

@Service("AnalyzeDbChangeSqlImpactService")
public class AnalyzeDbChangeSqlImpactServiceImpl implements AnalyzeDbChangeSqlImpactService {
	private static final Logger logger = LoggerFactory.getLogger(
			AutoPerformanceCompareBetweenDbServiceImpl.class );
	
	@Autowired
	private AnalyzeDbChangeSqlImpactDao analyzeDbChangeSqlImpactDao;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Override
	public List<Cd> getOracleVersionList(Cd cd) {
		return analyzeDbChangeSqlImpactDao.getOracleVersionList(cd);
	}
	
	@Override
	public List<AnalyzeDbChangeSqlImpact> loadAnalyzeDbChangeSqlImpactList(
			AnalyzeDbChangeSqlImpact analyzeDbChangeSqlImpact) throws Exception {
		List<AnalyzeDbChangeSqlImpact> resultList = new ArrayList<AnalyzeDbChangeSqlImpact>();
		
		try {
			resultList = analyzeDbChangeSqlImpactDao.loadAnalyzeDbChangeSqlImpactList( analyzeDbChangeSqlImpact );
			
			for ( int sqlIdx = 0; sqlIdx < resultList.size(); sqlIdx++ ) {
				String sqlText = resultList.get(sqlIdx).getSql_text_web();
				
				if ( sqlText != null ) {
					sqlText = StringUtil.removeHTML(sqlText);
					sqlText = StringUtil.removeSpecialChar(sqlText);
					sqlText = StringEscapeUtils.unescapeHtml( sqlText );
					
					if ( sqlText.length() > 40 ) {
						resultList.get(sqlIdx).setSql_text_web( sqlText.substring(0, 40) );
					} else {
						resultList.get(sqlIdx).setSql_text_web( sqlText );
					}
				}
			}
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			
			logger.error(methodName + " 예외 발생 ==> " + ex.getMessage());
			throw ex;
		}
		
		return resultList;
		
	}

	@Override
	public boolean excelDownload( 
			AnalyzeDbChangeSqlImpact analyzeDbChangeSqlImpact, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		boolean resultSuccess = true;
		ExcelDownHandler handler = new ExcelDownHandler(model, request, response);
		String sqlId = "omc.spop.dao.AnalyzeDbChangeSqlImpactDao.excelDownload";
		String sql = "";
		
		try {
			handler.open();
			
			handler.buildInit("성능_영향도_분석_결과", "SQL_AUTO_PERF_COMP");
			handler.addRemoveHTML(new String[] { "SQL_TEXT" });
			
			analyzeDbChangeSqlImpact.setSql_id(" ");
			sql = handler.getSql(sqlSessionFactory, sqlId, analyzeDbChangeSqlImpact);
			handler.buildDocument(sqlSessionFactory, sql);
				
			handler.writeDoc();
			
		} catch ( Exception e ) {
			e.printStackTrace();
			resultSuccess = false;
			
		}finally {
			handler.close();
		}
		
		return resultSuccess;
	}
	
}
