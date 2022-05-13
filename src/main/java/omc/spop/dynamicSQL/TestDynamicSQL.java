package omc.spop.dynamicSQL;

import omc.spop.dynamicsql.QueryGenerator;
import omc.spop.dynamicsql.sqlQuery.SqlQuery;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class TestDynamicSQL {
	private static final Logger logger = LoggerFactory.getLogger(TestDynamicSQL.class);
	
	public static final String LINE_SEPERATOR=System.getProperty("line.separator");
	
	public static void main(String[] args) {
		
//		bind();
//		include();
//		invalidComment();
		getTest();
		
//		String queryString1;
//		
//		try {
//
//			logger.debug("System.getProperty(\"line.separator]\") [" + System.getProperty("line.separator]") + "]");
//			StringBuilder sb = new StringBuilder();
//			
//			sb.append("ABC");
//			sb.append(System.getProperty("line.separator]"));
//			sb.append("ZXC");
//			sb.append(LINE_SEPERATOR);
//			sb.append("123");
//			
//			logger.debug("sb[" + sb + "]");
//			queryString1 = QueryGenerator.createNamedQuery("omc.spop.dao.TestDao", "selectCombobox5")
//					.getQueryString();
//			
//			/**
//		     * print result: 'SELECT siteUUID FROM  SITES WHERE url = 'github.com''
//		     */
//			logger.debug("queryString1 : {}",queryString1);
//		} catch(DocumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

/*		String queryString1;
		
		try {
			queryString1 = QueryGenerator.createNamedQuery("com.dao.testDaoImpl", "selectSitesByDate")
			        .setParameter(1, "github.com")
			        .getQueryString();
		    
			*//**
		     * print result: 'SELECT siteUUID FROM  SITES WHERE url = 'github.com''
		     *//*
			logger.debug("queryString1 : {}",queryString1);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String queryString2;
		try {
			queryString2 = QueryGenerator.createNamedQuery("com.dao.testDaoImpl", "selectSitesByWhere")
			        .setParameter("date", "2019-08-20")
			        .setParameter("siteUUIDs", 1234567890)
			        .getQueryString();
	        *//**
	         * print result: 'SELECT siteUUID FROM  SITES WHERE url = 'github.com' AND date = '2019-08-20''
	         *//*
			logger.debug("queryString2 : {}",queryString2);
			
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		{
			// searchProjectList
			String queryString3;
			try {
				queryString3 = QueryGenerator.createNamedQuery("omc.spop.dao.IqmsDao", "searchProjectList")
//				        .setParameter("deploy_id", 123456)
				        .getQueryString();

				logger.debug("queryString3 : {}",queryString3);
				
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String queryString3;
		try {
			queryString3 = QueryGenerator.createNamedQuery("omc.spop.dao.IqmsDao", "selectMaxPerfCheckId___")
			        .setParameter("deploy_id", 123456)
			        .getQueryString();

			logger.debug("queryString3 : {}",queryString3);
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String queryString4;
		try {
			queryString4 = QueryGenerator.createNamedQuery("omc.spop.dao.IqmsDao", "updateDeployPerfChk___")
			        .setParameter("deploy_check_status_cd", 123456)
			        .setParameter("perf_check_id", 654321)
			        .getQueryString();

			logger.debug("queryString4 : {}",queryString4);
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String queryString5;
		try {
			queryString5 = QueryGenerator.createNamedQuery("omc.spop.dao.IqmsDao", "getApmList_old")
			        .setParameter("searchKey", "01")
			        .setParameter("searchValue", "CODE_01")
			        .getQueryString();

			logger.debug("queryString5 : {}",queryString5);
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String queryString6;
		try {
			queryString6 = QueryGenerator.createNamedQuery("omc.spop.dao.IqmsDao", "getTrimPrefixTest")
			        .setParameter("para1", "1")
			        .setParameter("para2", "2")
			        .setParameter("data1", "01")
			        .setParameter("data2", "02")
			        .getQueryString();

			logger.debug("queryString6 : {}",queryString6);
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Test.class...args.
		String queryString7;
		try {
			SqlQuery sqlQuery = QueryGenerator.createNamedQuery("omc.spop.dao.IqmsDao", "getIncludeSQL");
			sqlQuery.setParameter("para1", "1");
			sqlQuery.setParameter("para2", "2");
			sqlQuery.setParameter("data1", "01");
			sqlQuery.setParameter("data2", "02");
			
			queryString7 = sqlQuery.getQueryString();
			
			logger.debug("queryString7 : {}",queryString7);
						
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String selectProgramCodeList;
		try {
			SqlQuery sqlQuery = QueryGenerator.createNamedQuery("kr.nexin.biz.com.syst.mapper.ComSyst02001Mapper", "selectProgramCodeList");
			sqlQuery.setParameter("PGM_NM", "테스트");
			
			selectProgramCodeList = sqlQuery.getQueryString();
			
			logger.debug("selectProgramCodeList : {}",selectProgramCodeList);
						
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String selectProgramList;
		try {
			SqlQuery sqlQuery = QueryGenerator.createNamedQuery("kr.nexin.biz.com.syst.mapper.ComSyst02001Mapper", "selectProgramList");
			//sqlQuery.setParameter("PGM_NM", "테스트");
			
			selectProgramList = sqlQuery.getQueryString();
			
			logger.debug("selectProgramList : {}",selectProgramList);
						
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String selectProgramDetail;
		try {
			SqlQuery sqlQuery = QueryGenerator.createNamedQuery("kr.nexin.biz.com.syst.mapper.ComSyst02001Mapper", "selectProgramDetail");
			//sqlQuery.setParameter("PGM_NM", "테스트");
			
			selectProgramDetail = sqlQuery.getQueryString();
			
			logger.debug("selectProgramDetail : {}",selectProgramDetail);
						
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String insertProgram;
		try {
			SqlQuery sqlQuery = QueryGenerator.createNamedQuery("kr.nexin.biz.com.syst.mapper.ComSyst02001Mapper", "insertProgram");
			//sqlQuery.setParameter("PGM_NM", "테스트");
			
			insertProgram = sqlQuery.getQueryString();
			
			logger.debug("updateProgram : {}",insertProgram);
						
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String updateProgram;
		try {
			SqlQuery sqlQuery = QueryGenerator.createNamedQuery("kr.nexin.biz.com.syst.mapper.ComSyst02001Mapper", "updateProgram");
			//sqlQuery.setParameter("PGM_NM", "테스트");
			
			updateProgram = sqlQuery.getQueryString();
			
			logger.debug("updateProgram : {}",updateProgram);
						
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String deleteProgram;
		try {
			SqlQuery sqlQuery = QueryGenerator.createNamedQuery("kr.nexin.biz.com.syst.mapper.ComSyst02001Mapper", "deleteProgram");
			//sqlQuery.setParameter("PGM_NM", "테스트");
			
			deleteProgram = sqlQuery.getQueryString();
			
			logger.debug("deleteProgram : {}",deleteProgram);
						
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	private static void bind() {
		String queryString1;
		
		try {

			queryString1 = QueryGenerator.createNamedQuery("omc.spop.dao.TestDao", "getTest")
					.getQueryString();
			
			/**
		     * print result: 'SELECT * FROM WHERE id like :ids AND subject like :subjects'
		     */
			logger.debug("queryString1 : {}",queryString1);
		} catch(DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void include() {
		// Test.class...args.
		String queryString7;
		try {
			SqlQuery sqlQuery = QueryGenerator.createNamedQuery("omc.spop.dao.IqmsDao", "getIncludeSQL");
			queryString7 = sqlQuery.getQueryString();
			
			logger.debug("queryString7 : {}",queryString7);
						
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void invalidComment() {
		// Test.class...args.
		String queryString7;
		try {
			SqlQuery sqlQuery = QueryGenerator.createNamedQuery("omc.spop.dao.TestDao", "abnormal_comment2");
			queryString7 = sqlQuery.getQueryString();
			
			logger.debug("queryString7 : {}",queryString7);
						
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void getTest() {
		// Test.class...args.
		String queryString7;
		try {
			SqlQuery sqlQuery = QueryGenerator.createNamedQuery("omc.spop.dao.TestDao", "getTest");
			queryString7 = sqlQuery.getQueryString();
			
			logger.debug("queryString7 : {}",queryString7);
						
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
