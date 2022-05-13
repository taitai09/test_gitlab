package omc.spop.controller;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Queue;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
//import org.docx4j.openpackaging.exceptions.InvalidFormatException;
//import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
//import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.zeroturnaround.zip.ZipUtil;

import omc.spop.base.InterfaceController;
import omc.spop.model.DiagnosisReport;
import omc.spop.model.ReportHtml;
import omc.spop.model.Report.DiagnosisOverview;
import omc.spop.model.Report.DiagnosisResult;
import omc.spop.model.Report.DiagnosisResultDetailDb1;
import omc.spop.model.Report.DiagnosisResultDetailDb2;
import omc.spop.model.Report.DiagnosisResultDetailFault;
import omc.spop.model.Report.DiagnosisResultDetailObject;
import omc.spop.model.Report.DiagnosisResultDetailParameter;
import omc.spop.model.Report.DiagnosisResultDetailSQL;
import omc.spop.service.DiagnosisReportService;
import omc.spop.service.DiagnosisResultDetailDb1Service;
import omc.spop.service.DiagnosisResultDetailDb2Service;
import omc.spop.service.DiagnosisResultDetailFaultService;
import omc.spop.service.DiagnosisResultDetailObjectService;
import omc.spop.service.DiagnosisResultDetailParameterService;
import omc.spop.service.DiagnosisResultDetailSQLService;
import omc.spop.service.DiagnosisResultService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2018.03.13 이원식 OPENPOP V2 최초작업
 **********************************************************/

@Controller
@RequestMapping(value = "/DiagnosisReport")
public class DiagnosisReportController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(DiagnosisReportController.class);
	
	@Autowired
	private DiagnosisReportService diagnosisReportService;

	@Autowired
	private DiagnosisResultService diagnosisResultService;

	@Autowired
	private DiagnosisResultDetailDb1Service diagnosisResultDetailDb1Service;

	@Autowired
	private DiagnosisResultDetailDb2Service diagnosisResultDetailDb2Service;

	@Autowired
	private DiagnosisResultDetailObjectService diagnosisResultDetailObjectService;

	@Autowired
	private DiagnosisResultDetailParameterService diagnosisResultDetailParameterService;

	@Autowired
	private DiagnosisResultDetailSQLService diagnosisResultDetailSQLService;

	@Autowired
	private DiagnosisResultDetailFaultService diagnosisResultDetailFaultService;

	private static String webPath = "";
	private static String rootPath = "";
	private String WEB_PATH = "";
	private static String zipFileName = "";

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String report(@ModelAttribute("reportHtml") ReportHtml reportHtml, Model model) throws Exception {
		
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", nowDate, -30);

		model.addAttribute("startDate", startDate);
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", reportHtml.getMenu_id());
		model.addAttribute("menu_nm", reportHtml.getMenu_nm());
		
		return "report/diagnosisReport";
	}
	
//	@RequestMapping(value = "/word", method = { RequestMethod.GET, RequestMethod.POST })
//	public void word(@ModelAttribute("reportHtml") ReportHtml reportHtml ,HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String htmlFileName = "sample2.html";
//		
//		try {
//			String inputfilepath = webPath + "/" + htmlFileName;
//			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
//			
//			NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
//			wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
//			ndp.unmarshalDefaultNumbering();
//			
//			XHTMLImporterImpl xHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
//			xHTMLImporter.setHyperlinkStyle("Hyperlink");
//			wordMLPackage.getMainDocumentPart().getContent().addAll(xHTMLImporter.convert(new File(inputfilepath), null));
//			
//			File output = new java.io.File(webPath + "/html_output.docx");
//			wordMLPackage.save(output);
//			logger.debug("wordMLPackage done");
//			
//			logger.debug("file path where it is stored is" + " " + output.getAbsolutePath());
//		} catch (InvalidFormatException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public String addMonthByOracle(int year, int mon, int day, int month) {
		Calendar c = Calendar.getInstance();
		c.set(year, mon - 1, day);
		c.add(Calendar.DATE, 1);
		c.add(Calendar.MONTH, month);
		c.add(Calendar.DATE, -1);
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
		
		return dateFormatter.format(c.getTime());
	}
	
	@RequestMapping(value = "/view", method = { RequestMethod.GET, RequestMethod.POST })
	public void generated(@ModelAttribute("reportHtml") ReportHtml reportHtml, HttpServletRequest request, HttpServletResponse response) throws Exception {
		rootPath = request.getSession().getServletContext().getRealPath("/");
		webPath = request.getSession().getServletContext().getRealPath("/resources/db_report");
		reportHtml.setWebReportPath(webPath);
		
		
		mkdirIncludeDir();
		deleteZipFile();
		deleteHtmlFile();
		
		int base_period_value = reportHtml.getBase_period_value();
		String strEndDt = reportHtml.getStrEndDt();
		String strStartDt = "";
		
		switch(base_period_value) {
		case 1:	// 주간 기준
			strStartDt = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", strEndDt, -6);
			break;
		case 2:	// 월간 기준
			int year  = Integer.parseInt(strEndDt.substring(0, 4));
			int month = Integer.parseInt(strEndDt.substring(5, 7));
			int date  = Integer.parseInt(strEndDt.substring(8, 10));
			
			strStartDt = addMonthByOracle(year, month, date, -1);
			break;
		}
		
		strStartDt = strStartDt.replace("-", "");
		strEndDt = strEndDt.replace("-", "");
		
		reportHtml.setStrStartDt(strStartDt);
		reportHtml.setStrEndDt(strEndDt);
		
		zipFileName = "db_report_" + strEndDt + ".zip";
		
		logger.debug("base_period_value[" + base_period_value + "] startDt[" + strStartDt + "] endDt[" + strEndDt + "]");
		
		Date d = new java.text.SimpleDateFormat("yyyyMMdd").parse(strEndDt);
		
		String dd = addMonthToDateLikeOracle(d, 1);
		logger.debug("dd:" + dd);
		
		
		String html = writeHtml(reportHtml);
		
		InputStream in = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
		int len = -1;
		byte[]  byt = new byte[1024];
		
		ServletOutputStream outputStream = response.getOutputStream();
		while((len = in.read(byt)) != -1){
			outputStream.write(byt, 0, len);
		}
		outputStream.flush();
		outputStream.close();
	}
	private void mkdirIncludeDir(){
		File include = new File(webPath + "/include/");
		if(include.exists() == false) {
			include.mkdir();
		}
	}
	private void deleteHtmlFile() throws Exception {
		File file = new File(webPath);
		File include = new File(webPath + "/include/");
		String fileList[] = file.list();
		String includeList[] = include.list();
		
		logger.debug("check Delete HtmlFile directory[" + webPath + "]");
		
		try {
			for(int fileIndex = 0; fileIndex < fileList.length; fileIndex++) {
				String fileName = fileList[fileIndex];
				
				if(fileName.contains(".html")) {
					logger.debug("Delete fileName[" + fileName + "]");
					File deleteFile = new File(webPath + "/" + fileName);
					deleteFile.delete();
				}
			}
			
			for(int fileIndex = 0; fileIndex < includeList.length; fileIndex++) {
				String fileName = includeList[fileIndex];
				
				if(fileName.contains(".html")) {
					logger.debug("Delete fileName[" + fileName + "]");
					File deleteFile = new File(webPath + "/include/" + fileName);
					deleteFile.delete();
				}
			}

		} catch(Exception ex) {
			throw ex;
		}
	}
	
	private String addMonthToDateLikeOracle(Date date, int months) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		Date retval;
		
		if (isLastDayOfMonth(date)) {
			c.add(Calendar.MONTH, 1);
//			c.add(Calendar.MONTH, 1);
			c.add(Calendar.MONTH, -1);
			c.set(Calendar.DATE, 1);
			c.add(Calendar.DATE, -1);
			retval = c.getTime();
		} else {
//			retval = LocalDate.fromDateFields(date).plusMonths(months).toDate();
			retval = LocalDate.fromDateFields(date).minusMonths(months).toDate();
		}
		
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");

		String to = transFormat.format(retval);
		
		return to;
	}
	
	private boolean isLastDayOfMonth(Date date) {
		if (date == null) {
			throw new RuntimeException("The date parameter cannot be null!");
		}
		
		Date endOfMonth = getEndOfMonth(date);
		return endOfMonth.equals(date);
	}
	
	private Date getEndOfMonth(Date date) {
		Date startOfMonth = getStartOfMonth(date);
		Date startOfNextMonth = LocalDate.fromDateFields(startOfMonth).plusMonths(1).toDate();
		return LocalDate.fromDateFields(startOfNextMonth).plusDays(-1).toDate();
	}
	
	private Date getStartOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}
	
	private String section2 = "";
	
	public String writeHtml(ReportHtml reportHtml) throws Exception {
		WEB_PATH = reportHtml.getWebReportPath();
		
		String html = "";
		section2 = "";
		String nav = writeNav(reportHtml);
		String section = section2;
		
		html = "" +
				"<!DOCTYPE HTML>\n" +
				"<html>\n" +
				"<head>\n" +
				"\t<title>SAMPLE</title>\n" +
				reportHtml.getWrapper_css() +
				referenceCSSTreeMenu() +	// 정리가 되면 .jsp에 추가해야 함.
				referenceCSS(false) +	// 정리가 되면 .jsp에 추가해야 함.
//				referenceLib() +
				"</head>\n" +
				"<body>\n" +
				"\t<div id=\"wrapper\">\n" +
				"\t\t<nav>\n" +
				nav +
				
				"\t\t</nav>\n" +
				"\t\t<section>\n" +
				"\t\t\t<div id=\"section\">\n" +
				section +
				"\t\t\t</div>\n" +
				"\t\t</section>\n" +
				"\t</div>\n" +
				"</body>\n" +
				"</html>";
		
		return html;
	}
	
	private String writeNav(ReportHtml reportHtml) throws Exception {
		StringBuffer nav = new StringBuffer();
		
		String contents_id = "";
		String contents_name= "";
		
		String html = "";
		String center_body = "";
		String dbid = reportHtml.getDbid();
		String strStartDt = reportHtml.getStrStartDt();
		String strEndDt = reportHtml.getStrEndDt();
		
		int level = 0;

		DiagnosisReport dignosisReport = null;
		DiagnosisReport tempDiagnosisReport = new DiagnosisReport();
		
		tempDiagnosisReport.setExadata_yn(reportHtml.getExadata_yn());

		List<DiagnosisReport> sltProgramContents = diagnosisReportService.loadSLTProgramContents(tempDiagnosisReport);
		int sltProgramContentsSize = sltProgramContents.size();
		
		StringBuffer tree = new StringBuffer();
		tree.append("<div id=\"tree\" style=\"overflow:auto;height:600px;\">\n");
		tree.append(referenceCSSTreeMenu());
		
		StringBuilder str = new StringBuilder(htmlTop(reportHtml));
		intermediateHtmlWrite(str);

		int prev_level = 0;
//		section2 = setTabbedPane();
		String top_body = setTabbedPane();
		
		for(int index = 0; index < sltProgramContentsSize; index++) {
			logger.debug("^^contentsId = " + contents_id);
			dignosisReport = sltProgramContents.get(index);
			contents_id = dignosisReport.getContents_id();
			contents_name = dignosisReport.getContents_name();
			level = dignosisReport.getLevel();
			
			if(reportHtml.isCollected()) {
				center_body += section(level, contents_id, contents_name, dbid, strStartDt, strEndDt, reportHtml);
			}
			
//			// Plus Contents
			html += hardCodingContents(prev_level, level, contents_id, contents_name, reportHtml);
			
			if(index == sltProgramContentsSize -1) {
				html += "</div></li>\n\t\t</ul>\n\t</li>\n</ul>";
			}
			
			prev_level = level;
		}
		
		center_body += "</div>\n";
		
		if(reportHtml.isCollected()) {
			section2 += top_body;
			section2 += "<div id=\"inbody\" style=\"overflow:hidden; width:1050px; height:605px; border-right:1px solid #cbcbcb; border-top:1px solid #cbcbcb; border-left:1px solid #cbcbcb;\">\n" ;
			section2 += "<script>";
			section2 += getWritingHtmlScript(reportHtml);
			section2 += "</script>";
			section2 += center_body;
			section2 += "</div>";
//			sectionPlus(reportHtml);
		} else {
			section2 += "<script>$(\"#section\").addClass(\"not_exist\")</script>";
//			referencePlusSection();
		}
		
		tree.append(html);
		tree.append("</div>\n");
		nav.append(navInfo(reportHtml));
		nav.append(tree.toString());
		
//		Zip 파일 제공으로 불필요한 html 생성을 차단함.	
		replaceRTCAndWrite(reportHtml);
		writingHtml(reportHtml, nav.toString() , center_body);
		
		return nav.toString();
	}
	private void replaceRTCAndWrite(ReportHtml reportHtml) throws Exception {

		File p020File = new File(rootPath +"/resources/db_report/include/P020.html");
		
		if(p020File.exists()) {
			StringBuilder sb = new StringBuilder();
			sb.append("<script>");
			sb.append(rtcReplace(reportHtml));
			sb.append("</script>");
			intermediateHtmlWrite_include(sb.toString(),"P020");
		}
	}
	public String rtcReplace(ReportHtml reportHtml) {
		StringBuilder sb = new StringBuilder();
		LinkedHashMap rtcMap = reportHtml.getRtcMap();

		
		int rtc_1 = 0;
		int rtc_2 = 0;
		int rtc_3 = 0;

		if(rtcMap.containsKey("9661")) {		// 긴급조치
			rtc_1 = (Integer) rtcMap.get("9661");
		}
		
		if(rtcMap.containsKey("9651")) {	// 조치필요
			rtc_2 = (Integer) rtcMap.get("9651");	
		}
		
		if(rtcMap.containsKey("9633")) {	// 확인필요
			rtc_3 = (Integer) rtcMap.get("9633");	
		}
		
		sb.append("$(document).ready (function(){\n");
		sb.append("\t$('#inbody dd').css('font-size','13px')\n");
		sb.append("\t$('#inbody dd').css('line-height','22px')\n");
		sb.append("\t$('#inbody').css('background','white')\n");
		sb.append("\t$('#rtc_1').html('" + rtc_1 + " 건')\n");
		sb.append("\t$('#rtc_2').html('" + rtc_2 + " 건')\n");
		sb.append("\t$('#rtc_3').html('" + rtc_3 + " 건')\n");
		sb.append("})\n");
		
		return sb.toString();
	}
	public void intermediateHtmlWrite(StringBuilder str) throws Exception {
		
		File file1 = new File(rootPath + "/resources/db_report/db_report.html");
		
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(file1, true);
			
			fos.write(str.toString().getBytes());
			fos.flush();
		} catch(Exception ex) {
			throw ex;
		} finally {
			if(fos != null)
				fos.close();
			if(str != null)
				str = null;
			fos = null;
		}
		
	}
	
	
	private String htmlTop(ReportHtml reportHtml) {
		StringBuilder str =new StringBuilder();
		str.append("<!DOCTYPE HTML>\n");
		str.append("<html>\n");
		str.append("<head>\n");
		str.append("\t<title>DB/SQL 종합 진단</title>\n");
		str.append(reportHtml.getWrapper_css());
		str.append(referenceCSSTreeMenu());	// 정리가 되면 .jsp에 추가해야 함.
//		str.append(referenceCSS());	// 정리가 되면 .jsp에 추가해야 함.
//				referenceLib());
		str.append(	"</head>\n");
		str.append(	"<body style='background:#f9f9f9;'>\n");
		str.append("\t<div id='container' style='padding:15px;'>\n");
		str.append(getWritingHtmlTitle());
		str.append("\t\t<section style='text-align:left;margin-left:15px'>\n");
		str.append("\t\t\t<div id=\"section\">\n");
		str.append("<!DOCTYPE HTML>\n");
		str.append("<html>\n");
		str.append("<!DOCTYPE HTML>\n");
		str.append("<html>\n");
		str.append("<head>\n");
		str.append("<meta charset=\"utf-8\">");
		str.append(referenceCSS(true));	// Debug시 중복되어 참조되는 현상 때문에 주석 처리
		str.append(referenceLib());	// Debug시 중복되어 참조되는 현상 때문에 주석 처리
		str.append("</head>\n");
		str.append("<body>\n");
		str.append(setTabbedPane());
		str.append("<div id=\"inbody\" style=\"overflow:hidden; width:1050px; height:605px; border-right:1px solid #cbcbcb; border-top:1px solid #cbcbcb; border-left:1px solid #cbcbcb;\">\n");
	
		return str.toString();
	}
	private String getWritingHtmlTitle() {
		StringBuilder sb = new StringBuilder();
		sb.append("<div id='titleDiv' style='");
		sb.append(" display: flex;");
		sb.append(" margin-bottom: 10px;");
		sb.append(" align-items: center;'> \n");
		sb.append("\t<img src='./images/report.png' style='border : 0 ;  margin-right: 5px; font-size: 4em;'>\n");
		sb.append("\t<h2 class='title' style='margin-top: 0px;margin-left: 0px; background : none;'>\n");
		sb.append("\tDB/SQL 종합 진단\n");
		sb.append("\t</h2></div>\n");
		
		return sb.toString();
	}
	public void intermediateHtmlWrite_include(String str , String contentId) throws Exception {
		
		File file = new File(rootPath +"/resources/db_report/include/"+ contentId + ".html");
		
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(file, true);
			
			fos.write(str.getBytes());
			fos.flush();
		} catch(Exception ex) {
			throw ex;
		} finally {
			if(fos != null)
				fos.close();
			if(str != null)
				str = null;
			fos = null;
		}		
	}

	public void intermediateHtmlWrite_include(Queue<String> queue , String contentId) throws Exception {
		
		File file = new File(rootPath +"/resources/db_report/include/"+ contentId + ".html");

		StringBuilder sb = new StringBuilder();
		FileOutputStream fos = null;
		
		int len = queue.size();
		try {
			fos = new FileOutputStream(file, true);
			
			for(int i = 0 ; i < len ; i ++) {
				sb.append(queue.poll());
			}

			fos.write(sb.toString().getBytes());
			fos.flush();
		} catch(Exception ex) {
			throw ex;
		} finally {
			if(fos != null)
				fos.close();
			if(sb != null)
				sb = null;
			fos = null;
		}
		
	}
	public String getTextListJs() {
		String str = "<style type=\"text/css\">\n" +
		"\t.hl {background: #fffc92;color: black;}" + 
		"</style>\n" +
		"<script>\n" +
		"window.addEventListener( 'message', receiveMsg );\n" +
		"function receiveMsg(msg) {\n"+
		"let msgJson = JSON.parse(msg.data);\n" +
		"\tif(msgJson && msgJson.func){\n"+
		"\t\tif(msgJson.func==='setHlClass'){\n" +
		"\t\t\tsetHlClass(msgJson.param1);\n"+
		"\t\t}\n" +
		"\t}\n" +
		"}\n" +
		"function setHlClass(id) {\n" +
		"\t$('.hl').removeClass('hl');\n" +
		"\t$('#'+id +' td span').addClass('hl')\n " +
		"}\n" +
		"function removeHlClass() {\n" +
		"\tif($('.hl')){" +
		"\t\t$('.hl').removeClass('hl');" + 
		"\t}\n " +
		"}\n" +
		"</script>\n";
		
		return str;

	}

	public String sectionPlus(String html ,ReportHtml reportHtml) {
		String strBasePeriod = "";
		
		if(reportHtml.getBase_period_value() == 1) {
			strBasePeriod = "1주일";
		} else {
			strBasePeriod = "1개월";
		}
		
		html = html.replace("BASE_PERIOD_VALUE", strBasePeriod);
		
//		html = referencePlusSection();
		
		return html;
	}
	
	private String navInfo(ReportHtml reportHtml) {
		String html = 
//				"<div id=\"desc\" style=\"height:20px;background-color:#f1f1f1;padding:5px 2px 2px 2px;\">\n" +
				"<div id=\"desc\" style=\"border : 1px solid #d2d2d2;height:20px;background-color:#f1f1f1;padding:2px 2px 9px 2px;margin-bottom:5px;\">\n" +
					"<table>\n" +
						"<tr>\n" +
							"<th style='color:black; font-weight:bold'>\n" +
								"&nbsp;&nbsp;&nbsp;&nbsp;" +
								reportHtml.getIconAction() + "&nbsp;&nbsp;:&nbsp;&nbsp;비 긴급성 조치 필요&nbsp;&nbsp;&nbsp;&nbsp;\n" +
								reportHtml.getIconEmergency() + "&nbsp;&nbsp;:&nbsp;&nbsp;긴급성 조치 필요"
								+ "\n" +
								"<label class=\"checkbox-wrap\"><input type=\"checkbox\" name=\"\" value=\"\" onclick=\"eventTreeMenu(this)\"><i class=\"check-icon\"></i></label>\n" + 
							"</th>\n" +
						"</tr>\n" +
					"</table>\n" +
				"</div>\n";
		
		return html;
	}
//	
//	private String referencePlusSection() {
//		StringBuffer html = new StringBuffer();
//		
//		html.append("<!DOCTYPE HTML>\n");
//		html.append("<html>\n");
//		html.append("<head>\n");
//		html.append("<meta charset=\"utf-8\">");
//		html.append(referenceCSS());	// Debug시 중복되어 참조되는 현상 때문에 주석 처리
//		html.append(referenceLib());	// Debug시 중복되어 참조되는 현상 때문에 주석 처리
//		html.append("</head>\n");
//		html.append("<body>\n");
//		html.append(section2);
//		html.append("</body>\n");
//		html.append("</html>");
//		
//		return html.toString();
//	}
	
	private String section(int level, String contents_id, String contents_name, String dbid, String strStartDt, String strEndDt, ReportHtml reportHtml) {
		String html = "";
		DiagnosisReport tempDiagnosisReport = new DiagnosisReport();
		DiagnosisOverview diagnosisOverview = new DiagnosisOverview();
		DiagnosisResult diagnosisResult = new DiagnosisResult();
		DiagnosisResultDetailDb1 diagnosisResultDetailDb1 = new DiagnosisResultDetailDb1();
		DiagnosisResultDetailDb2 diagnosisResultDetailDb2 = new DiagnosisResultDetailDb2();
		DiagnosisResultDetailObject diagnosisResultDetailObject = new DiagnosisResultDetailObject();
		DiagnosisResultDetailParameter diagnosisResultDetailParameter = new DiagnosisResultDetailParameter();
		DiagnosisResultDetailSQL diagnosisResultDetailSQL = new DiagnosisResultDetailSQL();
		DiagnosisResultDetailFault diagnosisResultDetailFault = new DiagnosisResultDetailFault();
		
		tempDiagnosisReport.setContents_id(contents_id);
		tempDiagnosisReport.setDbid(dbid);
		tempDiagnosisReport.setStrStartDt(strStartDt);
		tempDiagnosisReport.setStrEndDt(strEndDt);
		
		List<DiagnosisReport> sqlList;
		
		try {
			
			int rcount = isCollected(dbid, strStartDt, strEndDt);
			
			if(rcount == 0) {
				reportHtml.setCollected(false);
				
				return html;
			}
			
			sqlList = diagnosisReportService.getSQL(tempDiagnosisReport);
			
			int id = Integer.parseInt(contents_id.substring(1) + "");
			
			if(sqlList.size() > 0) {
				if(id == 10 || (id >= 101 && id <= 104)) {
					diagnosisReportService.loadSQL(changeVariable(sqlList, dbid, strStartDt, strEndDt), contents_id, diagnosisOverview, reportHtml);
				} else if(id == 20 || (id >= 106 && id <= 110)) {
					diagnosisResultService.loadSQL(changeVariable(sqlList, dbid, strStartDt, strEndDt), contents_id, diagnosisResult, reportHtml);
				} else if(id == 30 || (id >= 112 && id <= 148) || (id >= 178 && id <= 247)) {
					// P030(DB 진단 결과 상세 내용)
					if(id == 30 || (id >= 112 && id <= 130) || (id >= 178 && id <= 209)) {
						// #01
						diagnosisResultDetailDb1Service.loadSQL(changeVariable(sqlList, dbid, strStartDt, strEndDt), contents_id, diagnosisResultDetailDb1, reportHtml);
					} else if((id >= 131 && id <= 148) || (id >= 210 && id <= 247)) {
						// #02
						diagnosisResultDetailDb2Service.loadSQL(changeVariable(sqlList, dbid, strStartDt, strEndDt), contents_id, diagnosisResultDetailDb2, reportHtml);
					}
				} else if((id >= 149 && id <= 152) || (id >= 248 && id <= 253)) {
					// P149(오브젝트 진단)
						diagnosisResultDetailObjectService.loadSQL(changeVariable(sqlList, dbid, strStartDt, strEndDt), contents_id, diagnosisResultDetailObject, reportHtml);
				} else if((id >= 153 && id <= 154) || (id == 254)) {
					// P153(파라미터 진단)
					diagnosisResultDetailParameterService.loadSQL(changeVariable(sqlList, dbid, strStartDt, strEndDt), contents_id, diagnosisResultDetailParameter, reportHtml);
				} else if((id >= 155 && id <= 171) || (id >= 255 && id <= 282)) {
					// P155(SQL 성능 진단)
					diagnosisResultDetailSQLService.loadSQL(changeVariable(sqlList, dbid, strStartDt, strEndDt), contents_id, diagnosisResultDetailSQL, reportHtml);
				} else if((id >= 172 && id <= 177) || (id >= 283 && id <= 291)) {
					if(id == 177) {
						html = ("<iframe src='/resources/db_report/include/P177.html' id=\"tab-7\" class=\"tab-content\"></iframe>\n");
						diagnosisResultDetailFaultService.loadSQL(changeVariable(sqlList, dbid, strStartDt, strEndDt), contents_id, diagnosisResultDetailFault, reportHtml);
					} else {
						// P172(장애 예측 분석)
						diagnosisResultDetailFaultService.loadSQL(changeVariable(sqlList, dbid, strStartDt, strEndDt), contents_id, diagnosisResultDetailFault, reportHtml);
					}
				}
			} else {
				if(id == 10 || (id >= 101 && id <= 104)) {
					if(id == 10) {
						html = ("<iframe src='/resources/db_report/include/P010.html' id=\"tab-8\" class=\"tab-content\"></iframe>\n");
						diagnosisReportService.getReportHtml(tempDiagnosisReport, diagnosisOverview, reportHtml);
					} else {
						diagnosisReportService.getReportHtml(tempDiagnosisReport, diagnosisOverview, reportHtml);
					}
				} else if(id == 20 || (id >= 106 && id <= 110)) {
					if(id == 20) {
						html = ("<iframe src='/resources/db_report/include/P020.html' id=\"tab-1\" class=\"tab-content\"></iframe>\n");
						diagnosisResultService.getReportHtml(tempDiagnosisReport, diagnosisResult, reportHtml);
					} else {
						diagnosisResultService.getReportHtml(tempDiagnosisReport, diagnosisResult, reportHtml);
					}
				} else if(id == 30 || (id >= 112 && id <= 148) || (id >= 178 && id <= 247)) {
					// P030(DB 진단 결과 상세 내용)
					if(id == 30 || (id >= 112 && id <= 130) || (id >= 178 && id <= 209)) {
						if(id == 30) {
							;
						} else if(id == 112) {
							html = ("<iframe src='/resources/db_report/include/P112.html' id=\"tab-2\" class=\"tab-content\"></iframe>\n");
							diagnosisResultDetailDb1Service.getReportHtml(tempDiagnosisReport, diagnosisResultDetailDb1, reportHtml);
						} else {
							// #01
							diagnosisResultDetailDb1Service.getReportHtml(tempDiagnosisReport, diagnosisResultDetailDb1, reportHtml);
						}
					} else if((id >= 131 && id <= 148) || (id >= 210 && id <= 247)) {
						// #02
						diagnosisResultDetailDb2Service.getReportHtml(tempDiagnosisReport, diagnosisResultDetailDb2, reportHtml);
					}
				} else if((id >= 149 && id <= 152) || (id >= 248 && id <= 253)) {
					if(id == 149) {
						html = ("<iframe src='/resources/db_report/include/P149.html' id=\"tab-3\" class=\"tab-content\"></iframe>\n");
						diagnosisResultDetailObjectService.getReportHtml(tempDiagnosisReport, diagnosisResultDetailObject, reportHtml);
					} else {
						// P149(오브젝트 진단)
						diagnosisResultDetailObjectService.getReportHtml(tempDiagnosisReport, diagnosisResultDetailObject, reportHtml);
					}
				} else if((id >= 153 && id <= 154) || (id == 254)) {
					if(id == 153) {
						html = ("<iframe src='/resources/db_report/include/P153.html' id=\"tab-4\" class=\"tab-content\"></iframe>\n");
						diagnosisResultDetailParameterService.getReportHtml(tempDiagnosisReport, diagnosisResultDetailParameter, reportHtml);
					} else {
						// P153(파라미터 진단)
						diagnosisResultDetailParameterService.getReportHtml(tempDiagnosisReport, diagnosisResultDetailParameter, reportHtml);
					}
				} else if((id >= 155 && id <= 171) || (id >= 255 && id <= 282)) {
					if(id == 155) {
						html = ("<iframe src='/resources/db_report/include/P155.html' id=\"tab-5\" class=\"tab-content\"></iframe>\n");
						diagnosisResultDetailSQLService.getReportHtml(tempDiagnosisReport, diagnosisResultDetailSQL, reportHtml);
					} else {
						// P155(SQL 성능 진단)
						diagnosisResultDetailSQLService.getReportHtml(tempDiagnosisReport, diagnosisResultDetailSQL, reportHtml);
					}
				} else if((id >= 172 && id <= 177) || (id >= 283 && id <= 291)) {
					if(id == 172) {
						html = ("<iframe src='/resources/db_report/include/P172.html' id=\"tab-6\" class=\"tab-content\"></iframe>\n");
						diagnosisResultDetailFaultService.getReportHtml(tempDiagnosisReport, diagnosisResultDetailFault, reportHtml);
					} else {
						if(id == 177) {
							html = ("<iframe src='/resources/db_report/include/P177.html' id=\"tab-7\" class=\"tab-content\"></iframe>\n");
						}
						// P172(장애 예측 분석)
						diagnosisResultDetailFaultService.getReportHtml(tempDiagnosisReport, diagnosisResultDetailFault, reportHtml);
					}
				}
			}
		} catch(Exception ex) {
			html = "<span style=\"color:green\">" + ex.getMessage() + "</span>";
		}
		
		return html;
	}
	
	private String setTabbedPane() {
		StringBuffer html = new StringBuffer();
		
		html.append("<script>\n");
		html.append("$(document).ready(function(){\n");
		html.append("\t\t").append("$(\"#tab-1\").addClass('current');\n");
//		html.append("\t").append("$('ul.tabs li').on(\"click\", function() {\n");
//		html.append("\t").append("$('ul.tabs li').click(function(){\n");
		html.append("\t").append("$('ul.tabbed li').click(function(){\n");
		html.append("\t\t").append("var tab_id = $(this).attr('data-tab');\n");
//		html.append("\t\t").append("$('ul.tabs li').removeClass('current');\n");
		html.append("\t\t").append("$('ul.tabbed li').removeClass('current');\n");
		html.append("\t\t").append("$('.tab-content').removeClass('current');\n");
		html.append("\t\t").append("$(this).addClass('current');\n");
		html.append("\t\t").append("$(\"#\"+tab_id).addClass('current');\n");
		html.append("\t").append("})\n");
		html.append("})\n");
		html.append("\n");
		html.append("function callFunction2(contents_id, level) {\n");
		html.append("\tvar id = Number(contents_id.substring(1));\n");
		html.append("\tlet obj;\n");
		html.append("\tif( level ){;\n");
		html.append("\treverseTreeMenu(contents_id);\n");
		html.append("\treturn ;\n");
		html.append("\t}\n");
		html.append("\n");
		html.append("\t$('ul.tabbed li').removeClass('current');\n");
		html.append("\t$('.tab-content').removeClass('current');\n");
		html.append("\n");
		html.append("\tif(id == 10 || (id >= 101 && id <= 104)) {\n");
		html.append("\t\t$('#li-tab-8').addClass('current');\n");
		html.append("\t\t$('#tab-8').addClass('current');\n");
		html.append("\t\tobj = '#tab-8';\n");
		html.append("\t} else if(id == 20 || (id >= 106 && id <= 110)) {\n");
		html.append("\t\t$('#li-tab-1').addClass('current');\n");
		html.append("\t\t$('#tab-1').addClass('current');\n");
		html.append("\t\tobj = '#tab-1';\n");
		html.append("\t} else if(id == 30 || (id >= 112 && id <= 148) || (id >= 178 && id <= 247)) {\n");
		html.append("\t\tif(id == 30) {\n");
		html.append("\t\t\tcontents_id = 'P112';\n");
		html.append("\t\t}\n");
		html.append("\n");
		html.append("\t\t$('#li-tab-2').addClass('current');\n");
		html.append("\t\t$('#tab-2').addClass('current');\n");
		html.append("\t\tobj = '#tab-2';\n");
		html.append("\t} else if((id >= 149 && id <= 152) || (id >= 248 && id <= 253)) {\n");
		html.append("\t\t$('#li-tab-3').addClass('current');\n");
		html.append("\t\t$('#tab-3').addClass('current');\n");
		html.append("\t\tobj = '#tab-3';\n");
		html.append("\t} else if((id >= 153 && id <= 154) || (id == 254)) {\n");
		html.append("\t\t$('#li-tab-4').addClass('current');\n");
		html.append("\t\t$('#tab-4').addClass('current');\n");
		html.append("\t\tobj = '#tab-4';\n");
		html.append("\t} else if((id >= 155 && id <= 171) || (id >= 255 && id <= 282)) {\n");
		html.append("\t\t$('#li-tab-5').addClass('current');\n");
		html.append("\t\t$('#tab-5').addClass('current');\n");
		html.append("\t\tobj = '#tab-5';\n");
		html.append("\t} else if((id >= 172 && id <= 176) || (id >= 283 && id <= 291)) {\n");
		html.append("\t\t$('#li-tab-6').addClass('current');\n");
		html.append("\t\t$('#tab-6').addClass('current');\n");
		html.append("\t\tobj = '#tab-6';\n");
		html.append("\t} else if(id == 177) {\n");
		html.append("\t\t$('#li-tab-7').addClass('current');\n");
		html.append("\t\t$('#tab-7').addClass('current');\n");
		html.append("\t\tobj = '#tab-7';\n");
		html.append("\t}\n");
		html.append("\n");
		html.append("\tlet idx = $(obj).attr('src').indexOf('#');\n");
		html.append("\tif(idx > -1){\n");
		html.append("\t$(obj).attr('src', $(obj).attr('src').substring(0,$(obj).attr('src').indexOf('#'))+'#'+contents_id);\n");
		html.append("}else{\n");
		html.append("\t$(obj).attr('src', $(obj).attr('src')+'#'+contents_id);\n");
		html.append("\t}\n");
		html.append("\n");
		html.append("\treverseTreeMenu(contents_id);\n");
		html.append("}\n");
		html.append("function callFunction3(sqlId,contentsId){\n");
		html.append("\tlet tab = ''\n");
		html.append("\tif(contentsId === 'P177'){\n");
		html.append("\t\ttab = 'tab-7';\n");
		html.append("\t}\n");
		html.append("\telse if(contentsId === 'P320'){\n");
		html.append("\t\ttab = 'tab-10';\n");
		html.append("\t}\n");
		html.append("\tlet obj = $('#'+tab);\n");
		html.append("\t$('ul.tabbed li').removeClass('current');\n");
		html.append("\t$('.tab-content').removeClass('current');\n");
		html.append("\n\n");
		html.append("\t$('#li-'+tab).addClass('current');\n");
		html.append("\t$('#'+tab).addClass('current');\n");
		html.append("\n\n");
		html.append("\tlet idx = $(obj).attr('src').indexOf('#');\n");
		html.append("\tif(idx > -1){\n");
		html.append("\t\t$(obj).attr('src', $(obj).attr('src').substring(0,$(obj).attr('src').indexOf('#'))+'#'+sqlId);\n");
		html.append("\t}else{\n");
		html.append("\t\t$(obj).attr('src', $(obj).attr('src')+'#'+sqlId);\n");
		html.append("\t}\n");
		html.append("document.getElementById(''+tab).contentWindow.postMessage( '{\"func\":\"setHlClass\",\"param1\":\"'+sqlId+'\"}', '*' );");
		html.append("}\n");
		html.append("\n");
		html.append("function eventTreeMenu(box) {\n");
		html.append("\tif(box.checked) {\n");
		html.append("\t\t$('ul.nested a.level3').css('display', 'none')\n");
		html.append("\t\t$('ul.nested a.level3 span').parent().css('display', '')\n");
		html.append("\t} else {\n");
		html.append("\t\t$('ul.nested a.level3').css('display', '')\n");
		html.append("\t}\n");
		html.append("}");
		html.append("function reverseTreeMenu(id) {\n");
		html.append("\t$('ul.nested a').removeClass('reverse');\n");
		html.append("\t$('#menu_a_'+id).addClass('reverse');\n");
		html.append("}\n");
		html.append("</script>\n");
		
//		html.append("<ul class=\"tabs\">\n");
		html.append("<div>");
		html.append("<ul class=\"tabbed\">\n");
		html.append("\t").append("<li id=\"li-tab-1\" class=\"tab-link current\" data-tab=\"tab-1\" onclick=\"fnMove('P020', 1); return false;\">진단 결과 요약</li>\n");
		html.append("\t").append("<li id=\"li-tab-2\" class=\"tab-link\" data-tab=\"tab-2\" onclick=\"fnMove('P112', 2); return false;\">DB 진단</li>\n");
		html.append("\t").append("<li id=\"li-tab-3\" class=\"tab-link\" data-tab=\"tab-3\" onclick=\"fnMove('P149', 2); return false;\">오브젝트 진단</li>\n");
		html.append("\t").append("<li id=\"li-tab-4\" class=\"tab-link\" data-tab=\"tab-4\" onclick=\"fnMove('P153', 2); return false;\">파라미터 진단</li>\n");
		html.append("\t").append("<li id=\"li-tab-5\" class=\"tab-link\" data-tab=\"tab-5\" onclick=\"fnMove('P155', 2); return false;\">SQL 성능 진단</li>\n");
		html.append("\t").append("<li id=\"li-tab-6\" class=\"tab-link\" data-tab=\"tab-6\" onclick=\"fnMove('P172', 2); return false;\">장애 예측(자원한계점) 분석</li>\n");
		html.append("\t").append("<li id=\"li-tab-7\" class=\"tab-link\" data-tab=\"tab-7\" onclick=\"fnMove('P177', 2); return false;\">SQL Text List</li>\n");
		html.append("\t").append("<li id=\"li-tab-8\" class=\"tab-link\" data-tab=\"tab-8\" onclick=\"fnMove('P010', 1); return false;\">진단 개요</li>\n");
		html.append("</ul>\n");
		html.append("</div>");
		
		return html.toString();
	}
	
	private int isCollected(String dbid, String strStartDt, String strEndDt) {
		List<DiagnosisReport> sqlList = null;
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		DiagnosisReport tempDiagnosisReport = new DiagnosisReport();
		int count = -1;
		
		tempDiagnosisReport.setSlt_program_sql_number(10234);
		tempDiagnosisReport.setDbid(dbid);
		tempDiagnosisReport.setStrStartDt(strStartDt);
		tempDiagnosisReport.setStrEndDt(strEndDt);
		
		try {
			sqlList = diagnosisReportService.getSQLUnit(tempDiagnosisReport);
			
			tempDiagnosisReport.setSql(sqlList.get(0).getSlt_program_chk_sql());
			
			resultList = diagnosisReportService.loadSQLUnit(changeVariableUnit(tempDiagnosisReport));
			
			String rcount = resultList.get(0).get("RCOUNT") + "";
			
			logger.debug("rcount[" + rcount + "]");
			
			count = Integer.parseInt(rcount);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	private DiagnosisReport changeVariableUnit(DiagnosisReport temp) {
		String sql = temp.getSql();
		logger.debug("changeVariableUnit in sql[" + sql + "]");
		
		sql = sql.replace("#{dbid}", temp.getDbid());
		sql = sql.replace("#{start_day}", "'" + temp.getStrStartDt() + "'");
		sql = sql.replace("#{end_day}", "'" + temp.getStrEndDt() + "'");
		sql = sql.replace("#{check_day}", "'" + temp.getStrEndDt() + "'");
		
		logger.debug("changeVariableUnit out sql[" + sql + "]");
		
		temp.setSql(sql);
		
		return temp;
	}
	
	private List<DiagnosisReport> changeVariable(List<DiagnosisReport> sqlList, String dbid, String strStartDt, String strEndDt) {
		DiagnosisReport temp = null;
		
		for(int sqlListIndex = 0; sqlListIndex < sqlList.size(); sqlListIndex++) {
			temp = sqlList.get(sqlListIndex);
			String sql = temp.getSlt_program_chk_sql();
			logger.debug("index[" + sqlListIndex + "] sql[" + sql + "]");
			
			sql = sql.replace("#{dbid}", dbid);
			sql = sql.replace("#{start_day}", "'" + strStartDt + "'");
			sql = sql.replace("#{end_day}", "'" + strEndDt + "'");
			sql = sql.replace("#{check_day}", "'" + strEndDt + "'");
			
			logger.debug("sql[" + sql + "]");
			
			temp.setSql(sql);
		}
		
		return sqlList;
	}
	
//	private List<DiagnosisReport> changeVariable(List<DiagnosisReport> sqlList, String dbid, String check_day) {
//		DiagnosisReport temp = null;
//		
//		for(int sqlListIndex = 0; sqlListIndex < sqlList.size(); sqlListIndex++) {
//			temp = sqlList.get(sqlListIndex);
//			String sql = temp.getSlt_program_chk_sql();
//			int beginDbidIndex = -1;
//			
//			while(sql.indexOf("#{dbid}") > 0) {
//				beginDbidIndex = sql.indexOf("#{dbid}");
//				
//				sql = sql.substring(0, beginDbidIndex) + dbid + sql.substring(beginDbidIndex + 7);
//			}
//			
//			int beginCheckDayIndex = -1;
//			
//			while(sql.indexOf("#{check_day}") > 0) {
//				beginCheckDayIndex = sql.indexOf("#{check_day}");
//				
//				sql = sql.substring(0, beginCheckDayIndex) + "'" + check_day + "'" + sql.substring(beginCheckDayIndex + 12);
//			}
//			
//			logger.debug("sql[" + sql + "]");
//			
//			temp.setSql(sql);
//		}
//		
//		
//		return sqlList;
//	}
	private void writingHtml(ReportHtml reportHtml , String nav ,String center_body) throws Exception {
		String downloadHtml = center_body;
		downloadHtml = downloadHtml.replaceAll("/resources/db_report/include/", "include/");
		logger.debug("writingHtml WEB_PATH[" + WEB_PATH + "]");
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append(getWritingHtmlScript(reportHtml)); 
		sb.append("</script>"); 
		sb.append(downloadHtml);
		sb.append("</section>");
		sb.append("\t\t<nav style='background: white;'>\n");
		sb.append(nav);
		sb.append("\t\t</nav>\n");
		sb.append("\t\t\t</div>\n");
		sb.append("\t</div>\n");
		sb.append("</body>\n");
		sb.append("</html>");

		intermediateHtmlWrite(sb);
		compress();
	}
	private String getWritingHtmlScript(ReportHtml reportHtml) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("$(document).ready (function(){\n");
		sb.append("\twindowReset()\n");
		sb.append("})\n");
		
		sb.append("$( window ).resize(function() {\n");
		sb.append("\twindowReset()\n");
		sb.append("})\n");

		sb.append("function fnMove(id, level){\n");
		sb.append("\treverseTreeMenu(id)\n");
		sb.append("\tlocation.href='#menu_' + id;\n");
		sb.append("\tcallFunction2(id, level);\n");
		sb.append("}\n");
		
		sb.append("function windowReset(){\n");
		sb.append("\t$('#tree').height((window.innerHeight - $('#titleDiv').height() - 95))\n");
		sb.append("\t$('#inbody').css('border-bottom' , '1px solid #cbcbcb')\n");
		sb.append("\t$('#inbody').height(window.innerHeight - 73 - $('#li-tab-8').height() - $('#titleDiv').height())\n");
		sb.append("\t$('#inbody').width(window.innerWidth - $('#tree').parent().outerWidth() - 45)\n");
		sb.append("\t$(\"#inbody table\").width($(\"#inbody\").innerWidth() - 100)\n");
		sb.append("}\n");
		
		sb.append("window.addEventListener( 'message', receiveMsgFromChild );");

		sb.append("function receiveMsgFromChild( e ) {\n");
		sb.append("\tlet msgJson;\n");
		sb.append("\tif(e.data){\n");
		sb.append("\t\tmsgJson = JSON.parse(e.data)\n");
		sb.append("\t}\n" );
		sb.append("\tif(msgJson && msgJson.func ) { \n");
		sb.append("\t\tif(msgJson.func === 'callFunction2'){\n");
		sb.append("\t\t\tcallFunction2(msgJson.param1)\n");
		sb.append("\t\t}else if(msgJson.func === 'callFunction3'){\n");
		sb.append("\t\t\tcallFunction3(msgJson.param1, msgJson.param2)\n");
		sb.append("\t\t}");
		sb.append("\t}\n");
		sb.append("}");

		return sb.toString();
	}

	/* 엑셀 다운로드 */
	@RequestMapping(value = "/excelDownload", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView excelDownload(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("reportHtml") ReportHtml reportHtml, Model model)
					throws Exception {
		DiagnosisReport diagnosisReport = new DiagnosisReport();
		List<DiagnosisReport> sqlList = null;
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		String slt_program_sql_name = "";
		
		try {
			diagnosisReport.setSlt_program_sql_number(reportHtml.getSlt_program_sql_number());
			sqlList = diagnosisReportService.getSQLUnit(diagnosisReport);
			slt_program_sql_name = sqlList.get(0).getSlt_program_sql_name();
			diagnosisReport.setSql(sqlList.get(0).getSlt_program_chk_sql());
			resultList = diagnosisReportService.loadSQLUnit(diagnosisReport);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", slt_program_sql_name);
		model.addAttribute("sheetName", slt_program_sql_name);
//		model.addAttribute("excelId", "SQL_AUTOMATIC_PERFORMANCE_CHECK");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	private String referenceCSSTreeMenu() {
		String html = 
				"<style type=\"text/css\">\n" + 
				"	li.base1		{font:18pt Arial,Helvetica,Geneva,sans-serif;color:#1b1b1b;background-color:White;width:calc(100% - 20px);display:block;text-align:left;}\n" +
				"	li.base2		{font:14pt Arial,Helvetica,Geneva,sans-serif;color:#1b1b1b;background-color:White;width:calc(100% - 20px);display:block;margin-left:20px;text-align:left;}\n" +
				"	li.base3		{font:14pt Arial,Helvetica,Geneva,sans-serif;color:#1b1b1b;background-color:White;width:calc(100% - 20px);display:block;margin-left:30px;text-align:left;}\n" +
				"	li.base4		{font:14pt Arial,Helvetica,Geneva,sans-serif;color:#1b1b1b;background-color:White;width:calc(100% - 20px);display:block;margin-left:30px;text-align:left;}\n" +
				"	a.level1		{font:bold 13px Arial,Helvetica,sans-serif; color:white; vertical-align:top; margin-top:0px; margin-bottom:0px; line-height:26px;text-align:left;display:block;width:100%;border:1px solid #d2d2d2;padding:3px;background-color:#1067a4;}\n" +
				"	a.level1red		{font:bold 13px Arial,Helvetica,sans-serif; color:red; vertical-align:top; margin-top:0px; margin-bottom:0px; line-height:26px;text-align:left;}\n" +
				"	a.level2		{font:bold 13px Arial,Helvetica,sans-serif; color:#383838; vertical-align:top; margin-top:0px; margin-bottom:0px; line-height:22px;text-align:left;}\n" +
				"	a.level3		{font:13px Arial,Helvetica,sans-serif; color:#383838; vertical-align:top; margin-top:0px; margin-bottom:0px; line-height:22px;text-align:left;}\n" +
				"	a.level4		{font:13px Arial,Helvetica,sans-serif; color:#383838; vertical-align:top; margin-top:0px; margin-bottom:0px; line-height:20px;text-align:left;}\n" +
				"	a.level1:hover	{text-decoration:underline;}\n" +
				"	a.level2:hover	{text-decoration:underline;}\n" +
				"	a.level3:hover	{text-decoration:underline;}\n" +
				"	a.level4:hover	{text-decoration:underline;}\n" +
				"	.nested			{position: relative;}\n" +
				"	.caret			{cursor: pointer; user-select: none;}\n" +
				"	.caret::before	{content: \"\\25B6\"; color: black; display: inline-block; margin-right: 6px;}\n" +
				"	.caret-down::before {transform: rotate(90deg);}\n" +
				"	a.reverse		{background-color:#4289bb; color:white;}\n" +
				"</style>\n";
		
		return html;
	}
	
	private String loadLiContents(int level, String contents_id, String contents_name, ReportHtml reportHtml) {
		String li = "";
		
		if(level == 1) {
//			li = "<li class=\"base1\"><a class=\"level1\" href=\"#" + contents_id + "\">" + "▶" + contents_name + "</a>";
//			li = "<li class=\"base1\"><a class=\"level1\" href=\"javascript:;\" onClick=\"callFunction('" + contents_id + "'); return false;\">" + "▶" + contents_name + "</a>";
//			li = "<li class=\"base1\"><a class=\"level1\" href=\"javascript:void(0);\" onClick=\"callFunction('" + contents_id + "'); return false;\">" + "▶" + contents_name + "</a>";
//			li = "<li class=\"base1\"><a class=\"level1\" onclick=\"location.href='#" + contents_id + "';\" style=\"cursor: pointer\">" + "▶" + contents_name + "</a>";
//			li = "<li class=\"base1\"><a class=\"level1\" onclick=\"callFunction2('" + contents_id + "'); return false;\" style=\"cursor: pointer\">" + "▶" + contents_name + "</a>";
			li = "<li class=\"base1\" id=\"menu_" + contents_id + "\" name=\"menu_" + contents_id + "\"><a class=\"level1\" id=\"menu_a_" + contents_id + "\" onclick=\"callFunction2('" + contents_id + "'); return false;\" style=\"cursor: pointer\">" + "▶" + contents_name + "</a>";
		} else if(level == 2) {
//			li = "<li class=\"base2\"><a class=\"level2\" href=\"#" + contents_id + "\">" + "<strong>&nbsp;·&nbsp;</strong>" + contents_name + "</a>";
//			li = "<li class=\"base2\"><a class=\"level2\" onclick=\"location.href='#" + contents_id + "';\" style=\"cursor: pointer\">" + "<strong>&nbsp;·&nbsp;</strong>" + contents_name + "</a>";
//			li = "<li class=\"base2\"><a class=\"level2\" onclick=\"callFunction2('" + contents_id + "'); return false;\" style=\"cursor: pointer\">" + "<strong>&nbsp;·&nbsp;</strong>" + contents_name + "</a>";
			li = "<li class=\"base2\" id=\"menu_" + contents_id + "\" name=\"menu_" + contents_id + "\"><a class=\"level2\" id=\"menu_a_" + contents_id + "\" onclick=\"callFunction2('" + contents_id + "'); return false;\" style=\"cursor: pointer\">" + "▷" + contents_name + "</a>";
		} else if(level == 3) {
//			li = "<li class=\"base3\"><a class=\"level3\" href=\"#" + contents_id + "\">" + "▷" + contents_name + "</a>";
			
			LinkedHashMap<String, Object> resultSummary = reportHtml.getResultSummary();
			
			if(resultSummary.containsKey(contents_id)) {
				String icon = resultSummary.get(contents_id) + "";
				
//				li = "<li class=\"base3\"><a class=\"level3\" href=\"#" + contents_id + "\">" + "▷" + contents_name + "&nbsp; <span>" + icon + "</span>" + "</a>";
//				li = "<li class=\"base3\"><a class=\"level3\" onclick=\"location.href='#" + contents_id + "';\" style=\"cursor: pointer\">" + "▷" + contents_name + "&nbsp; <span>" + icon + "</span>" + "</a>";
				li = "<li class=\"base3\" id=\"menu_" + contents_id + "\"><a class=\"level3\" id=\"menu_a_" + contents_id + "\" onclick=\"callFunction2('" + contents_id + "'); return false;\" style=\"cursor: pointer\">" + "<strong>&nbsp;-&nbsp;</strong>" + contents_name + "&nbsp; <span>" + icon + "</span>" + "</a>";
			} else {
//				li = "<li class=\"base3\"><a class=\"level3\" onclick=\"location.href='#" + contents_id + "';\" style=\"cursor: pointer\">" + "▷" + contents_name + "</a>";
				li = "<li class=\"base3\" id=\"menu_" + contents_id + "\"><a class=\"level3\" id=\"menu_a_" + contents_id + "\" onclick=\"callFunction2('" + contents_id + "'); return false;\" style=\"cursor: pointer\">" + "<strong>&nbsp;-&nbsp;</strong>" + contents_name + "</a>";
			}
//		} else if(level == 4) {
//			LinkedHashMap<String, Object> resultSummary = reportHtml.getResultSummary();
//			
//			if(resultSummary.containsKey(contents_id)) {
//				String icon = resultSummary.get(contents_id) + "";
//				
//				li = "<li class=\"base4\"><a class=\"level4\" href=\"#" + contents_id + "\">" + "&nbsp;-&nbsp;" + contents_name + "&nbsp; <span>" + icon + "</span>" + "</a>";
//			} else {
//				li = "<li class=\"base4\"><a class=\"level4\" href=\"#" + contents_id + "\">" + "&nbsp;-&nbsp;" + contents_name + "</a>";
//			}
		}
		
		return li;
	}
	
	private String hardCodingContents(int prev_level, int current_level, String contents_id, String contents_name, ReportHtml reportHtml) {
		StringBuffer sb = new StringBuffer();
		String horizonTab = "\t";
		String newLine = "\n";
		
		if(prev_level == 0) {
			sb.append(newLine).append("<ul class=\"nested\">").append(newLine).append(horizonTab).append(loadLiContents(1, contents_id, contents_name, reportHtml));
		} else if(prev_level < current_level) {
			if(current_level == 2) {
				sb.append(newLine).append(horizonTab).append(horizonTab).append("<ul class=\"nested\">").append(newLine).append(horizonTab).append(horizonTab).append(horizonTab);
			} else if(current_level == 3) {
				sb.append(newLine).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append("<ul class=\"nested\">").append(newLine).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab);
			} else if(current_level == 4) {
				sb.append(newLine).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append("<ul class=\"nested\">").append(newLine).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab);
			}
			
			sb.append(loadLiContents(current_level, contents_id, contents_name, reportHtml));
		} else if(prev_level == current_level) {
			if(current_level == 2) {
				sb.append("</li>").append(newLine).append(horizonTab).append(horizonTab).append(horizonTab);
			} else if(current_level == 3) {
				sb.append("</li>").append(newLine).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab);
			} else if(current_level == 4) {
				sb.append("</li>").append(newLine).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab);
			}
			sb.append(loadLiContents(current_level, contents_id, contents_name, reportHtml));
		} else if(prev_level > current_level) {
			if(current_level == 1) {
				sb.append("</li>").append(newLine).append(horizonTab).append(horizonTab).append("</ul>").append(newLine).append(horizonTab).append("</li class=\"depth-1\">").append(newLine).append(horizonTab);
			} else if(current_level == 2) {
				if(prev_level - current_level == 2) {
					sb.append("</li>").append(newLine);
					sb.append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append("</ul>").append(newLine);
					sb.append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append("</li class=\"depth-3\">").append(newLine);
					sb.append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append("</ul>").append(newLine);
					sb.append(horizonTab).append(horizonTab).append(horizonTab).append("</li class=\"depth-2\">").append(newLine);
					sb.append(horizonTab).append(horizonTab).append(horizonTab);
				} else {
					sb.append("</li>").append(newLine).append(horizonTab).append(horizonTab).append(horizonTab).append("</ul>").append(newLine).append(horizonTab).append(horizonTab).append("</li class=\"depth-2\">").append(newLine).append(horizonTab).append(horizonTab);
				}
			} else if(current_level == 3) {
				sb.append("</li>").append(newLine).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append("</ul>").append(newLine).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append("</li class=\"depth-3\">").append(newLine).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab).append(horizonTab);
			}
			
			sb.append(loadLiContents(current_level, contents_id, contents_name, reportHtml));
		}
	
		return sb.toString();
	}
	
	private String editContents(String html, int prev_level, int current_level, String contents_id, String contents_name) {
		StringBuffer sb = new StringBuffer();
		
		String closeTag = recursiveCloseTag(prev_level, current_level, "");
		
		sb.append(closeTag);
		
		if(prev_level == 0) {
			sb.append("\n<ul>\n").append(liTag(contents_id, contents_name, false));
		} else if(prev_level == current_level) {
			sb.append(liTag(contents_id, contents_name, true));
		} else if(prev_level < current_level){
			sb.append("\n<ul>\n").append(liTag(contents_id, contents_name, false));
		}
		
		return html + sb.toString();
	}
	
	private String liTag(String contents_id, String contents_name, boolean isSame) {
		return (isSame ? "</li>\n" : "") + "<li><a href=\"" + contents_id + "\">" + contents_name + "</a>";
	}
	
	private String recursiveCloseTag(int prev_level, int current_level, String html) {
		if(prev_level > current_level) {
			html = html + "</li>\n</ul>\n";
			recursiveCloseTag(prev_level - 1, current_level, html);
//		} else if(prev_level == current_level) {
//			html = html + "</li>\n";
		}
		
		return html;
	}
	
	private String referenceLib() {
		StringBuffer html = new StringBuffer();
		
		html.append("<script type=\"text/javascript\" src=\"./lib/chartjs/Chart.min.js\"></script>\n");
		html.append("<script type=\"text/javascript\" src=\"./lib/chartjs/chartjs-plugin-labels.min.js\"></script>\n");
		html.append("<script type=\"text/javascript\" src=\"./lib/jquery-3.4.1.min.js\"></script>\n");
		html.append("<script type=\"text/javascript\" src=\"./lib/jquery-migrate-3.1.0.min.js\"></script>\n");
		html.append("<link rel=\"stylesheet\" href=\"./lib/css/common.css\"></script>\n");
		html.append("<link rel=\"stylesheet\" href=\"./lib/css/layout.css\"></script>\n");
		html.append("<link rel=\"stylesheet\" href=\"./lib/css/reset.css\"></script>\n");
		html.append("<link rel=\"stylesheet\" href=\"./lib/css/nanumbarungothic.css\"></script>\n");
		html.append("<link rel=\"stylesheet\" href=\"./lib/css/opensans.css\"></script>\n");
		html.append("<link rel=\"stylesheet\" href=\"./lib/css/fontawesome-all.css\">\n");
		html.append("<link rel=\"stylesheet\" href=\"./lib/css/style.css\"></script>\n");

		return html.toString();
	}
	public String referenceLib_include() {
		StringBuilder html = new StringBuilder();
		
		html.append("<script type=\"text/javascript\" src=\"../lib/chartjs/Chart.min.js\"></script>\n");
		html.append("<script type=\"text/javascript\" src=\"../lib/chartjs/chartjs-plugin-labels.min.js\"></script>\n");
		html.append("<script type=\"text/javascript\" src=\"../lib/jquery-3.4.1.min.js\"></script>\n");
		html.append("<script type=\"text/javascript\" src=\"../lib/jquery-migrate-3.1.0.min.js\"></script>\n");

		//김원재 STAND ALONE
		html.append("<link rel=\"stylesheet\" href=\"../lib/css/common.css\"></script>\n");
		html.append("<link rel=\"stylesheet\" href=\"../lib/css/layout.css\"></script>\n");
		html.append("<link rel=\"stylesheet\" href=\"../lib/css/reset.css\"></script>\n");
		html.append("<link rel=\"stylesheet\" href=\"../lib/css/nanumbarungothic.css\"></script>\n");
		html.append("<link rel=\"stylesheet\" href=\"../lib/css/opensans.css\"></script>\n");
		html.append("<link rel=\"stylesheet\" href=\"../lib/css/fontawesome-all.css\">\n");
		html.append("<link rel=\"stylesheet\" href=\"../lib/css/style.css\"></script>\n");


		return html.toString();
	}

	public String referenceCSS(boolean isWrite) {
		String html = 
				"<style type=\"text/css\">\n" +
				"	body.om				{font:bold 14px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; letter-spacing:-2px; text-align:left;}\n" + 
				"	h1.top				{font:bold 30px Arial,Helvetica,Geneva,sans-serif; color:#383838; background-color:White; margin-top:0px; margin-bottom:30px; letter-spacing:-2px; text-align:left;}\n" + 
				"	h1.title			{font:bold 30px Arial,Helvetica,Geneva,sans-serif; color:#383838; background-color:White; margin-top:50px; margin-bottom:30px; letter-spacing:-2px; text-align:left;}\n" + 
				"	h2.title			{font:bold 23px Arial,Helvetica,Geneva,sans-serif; color:#383838; background-color:White; margin-top:30px; margin-bottom:5px; margin-left:15px; letter-spacing:-2px; text-align:left;}\n" + 
				"	h3.title			{font:bold 17px Arial,Helvetica,Geneva,sans-serif; color:#004370; background-color:White; margin-top:22px; margin-bottom:0px; margin-left:25px; letter-spacing:-1px; text-align:left;}\n" + 
				"	h4.title			{font:bold 13px Arial,Helvetica,Geneva,sans-serif;color:#383838;background-color:White; margin-top:5px; margin-bottom:0px; margin-left:35px; letter-spacing:-1px; text-align:left;}\n" + 
				"	t1.title			{font:14px Arial,Helvetica,Geneva,sans-serif;color:black;background-color:White; margin-top:20px; margin-bottom:0px; letter-spacing:-2px; text-align:left;}\n" + 
				"	li.title			{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White;letter-spacing:-1px; text-align:left;}\n" + 
				"	a.om				{font:bold 12px Arial,Helvetica,sans-serif; text-decoration: underline; color:#663300; vertical-align:top; margin-top:0px; margin-bottom:0px; text-align:left;}" +
				"	li.contents			{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; letter-spacing:-1px; text-align:left;}\n" + 
				"	dd.contents2		{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; margin-top:0px; margin-bottom:15px; margin-left:25px; letter-spacing:0px; text-align:left;}\n" + 
				"	dd.contents2-b5		{font: 14px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; margin-top:0px; margin-bottom:5px; margin-left:25px; letter-spacing:0px; text-align:left;}\n" + 
				"	dd.contents3		{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; margin-top:0px; margin-bottom:15px; margin-left:25px; letter-spacing:0px; text-align:left;}\n" + 
				"	dd.contents4		{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; margin-top:0px; margin-bottom:15px; margin-left:25px; letter-spacing:0px; text-align:left;}\n" + 
				"	table.awr			{border-spacing:0px !important;padding-bottom:30px;}\n" + 
				"	table.om			{border-spacing:0px !important; margin-bottom:10px;}\n" + 
				"	table.om2			{border-spacing:0px !important; margin-top:0px; margin-bottom:10px; margin-left:25px; width:975px;width: calc(100% - 50px);}\n" + 
				"	table.om2-b20		{border-spacing:0px !important; margin-top:0px; margin-bottom:20px; margin-left:25px; width:975px;width: calc(100% - 50px);}\n" + 
				"	table.om3			{border-spacing:0px !important; margin-top:0px; margin-bottom:10px; margin-left:25px; width:975px;width: calc(100% - 50px);}\n" + 
				"	table.om4			{border-spacing:0px !important; margin-top:-5px; margin-bottom:10px; margin-left:35px; width:965px;width: calc(100% - 50px);}\n" + 
				"	th.om-left			{font:bold 12px Arial,Helvetica,Geneva,sans-serif; color:#146298; background:#EEF9FF; padding:5px 5px 5px 5px; text-align:center; border-top:2px solid #4289bb; border-right:1px solid #4289bb; border-bottom:1px solid #4289bb; border-left:1px solid #FFFFFF;}\n" + 
				"	th.om-center		{font:bold 12px Arial,Helvetica,Geneva,sans-serif; color:#146298; background:#EEF9FF; padding:5px 5px 5px 5px; text-align:center; border-top:2px solid #4289bb; border-right:1px solid #4289bb; border-bottom:1px solid #4289bb;}\n" + 
				"	th.om-right			{font:bold 12px Arial,Helvetica,Geneva,sans-serif; color:#146298; background:#EEF9FF; padding:5px 5px 5px 5px; text-align:center; border-top:2px solid #4289bb; border-right:1px solid #FFFFFF; border-bottom:1px solid #4289bb;}\n" + 
				"	td.om-left			{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #969696; border-bottom:1px solid #969696; border-left:1px solid #FFFFFF;}\n" + 
				"	td.om-center		{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #969696; border-bottom:1px solid #969696;}\n" + 
				"	td.om-right			{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #FFFFFF; border-bottom:1px solid #969696;}\n" + 
				"	td.om-left-close	{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #969696; border-bottom:2px solid #4289bb; border-left:1px solid #FFFFFF;}\n" + 
				"	td.om-center-close	{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #969696; border-bottom:2px solid #4289bb;}\n" + 
				"	td.om-right-close	{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #FFFFFF; border-bottom:2px solid #4289bb;}\n" + 
				"	td.om-right-font		{font:12px 돋움체, Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #FFFFFF; border-bottom:1px solid #969696;}\n" + 
				"	td.om-right-close-font	{font:12px 돋움체, Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #FFFFFF; border-bottom:2px solid #4289bb;}\n" + 
				"	td.om-colspan		{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #FFFFFF; border-bottom:2px solid #4289bb; text-align:center;}\n" + 
				"	table.nl1			{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; border-spacing:0px !important;margin-top:0px; margin-bottom:10px;}\n" + 
				"	table.nl2			{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; border-spacing:0px !important;margin-top:0px; margin-bottom:20px;margin-left:25px;}\n" + 
				"	table.nl3			{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; border-spacing:0px !important;margin-top:-5px; margin-bottom:10px;margin-left:25px;}\n" + 
				"	table.nl4			{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; border-spacing:0px !important;margin-top:-5px; margin-bottom:10px;margin-left:35px;}\n" + 
				"	td.nl-left			{font:12px Arial,Helvetica,Geneva,sans-serif;color:black;background:White;vertical-align:top;padding:0px 0px 5px 0px;border-top:1px solid #FFFFFF;border-right:1px solid #FFFFFF;border-bottom:1px solid #FFFFFF;border-left:1px solid #FFFFFF;}\n" + 
				"	td.nl-center		{font:12px Arial,Helvetica,Geneva,sans-serif;color:black;background:White;vertical-align:top;padding:0px 0px 5px 0px;border-top:1px solid #FFFFFF;border-right:1px solid #FFFFFF;border-bottom:1px solid #FFFFFF;border-left:1px solid #FFFFFF;}\n" + 
				"	td.nl-right			{font:12px Arial,Helvetica,Geneva,sans-serif;color:black;background:White;vertical-align:top;padding:0px 0px 5px 0px;border-top:1px solid #FFFFFF;border-right:1px solid #FFFFFF;border-bottom:1px solid #FFFFFF;border-left:1px solid #FFFFFF;}\n" + 
				"	.chart3				{padding-left:25px;}" +
				"	.chart4				{padding-left:35px;}" +
				"	.button {" +
				"		width:50px;\n" + 
				"		height:22px;\n" +
				"		background-color: #FFF;\n" + 
				"		border: 1px solid #217346;\n" + 
				"		color:#217346;\n" + 
				"		text-align: center;\n" + 
				"		text-decoration: none;\n" + 
				"		display: inline-block;\n" + 
				"		font-size: 15px;\n" + 
				"		margin: 4px;\n" + 
				"		cursor: pointer;\n" + 
				"		border-radius:7px;\n" +
				"		float: right;\n" +
				"	}\n" + 
				"	.button:hover {background-color: #217346;color:#FFF;}\n" + 
				"	.button:hover {background-color: #217346;color:#FFF;}\n" + 
				"	ul.tabbed{\n" + 
				"		margin: 0px;\n" + 
				"		padding: 0px;\n" + 
				"		list-style: none;\n" + 
				"		padding-bottom: 0px;\n" +
				"		margin-bottom: 5px;\n" +
				"	}\n" + 
				"	ul.tabbed li{\n" + 
				"		background: #f2f2f2;\n" + 
				"		color: #222;\n" + 
				"		display: inline-block;\n" + 
				"		padding: 10px 15px;\n" + 
				"		cursor: pointer;\n" + 
				"		border-width: 1px;\n" +
				"		border-color: #38312a;\n" +
				"		border-style: solid;\n" +
				"		border-radius: 5px 5px 0px 0px;\n" +
				"		padding-bottom: 11px" +
				"	}\n" + 
				"	ul.tabbed li.current{\n" + 
				"		background: #fff;\n" + 
				"		color: #222;\n" + 
				"		border-width: 1px 1px 0px 1px;\n" +
				"		border-color: #38312a;\n" +
				"		border-style: solid;\n" +
				"		border-radius: 5px 5px 0px 0px;\n" +
				"		padding-bottom: 11px" +
				"	}\n" + 
				"	.tab-content{\n" + 
				"		visibility: hidden;\n" + 
				"		background: #fff;\n" + 
				"		padding: 0px;\n" + 
				"		display: block;\n" + 
				"		width: calc(100% - 30px);\n" + 
				"		height: 0px;\n" + 
				"		position: relative;\n" + 
				"	}\n" + 
				"	.tab-content.current{\n" + 
				"		visibility: visible;\n" + 
				"		padding: 15px;\n" + 
				"		display: block;\n" + 
				"		height: calc(100% - 30px);\n" + 
				"	}\n" +
//				"	.result_status{float:right;width:321px;height:17px;margin:8px 15px 0px 0px;background:url(../images/result_status.png);}" +
				"	.result_status{float:right;width:321px;height:17px;margin:8px 15px 0px 0px;}" +
				"	.result_status_on{opacity : 100%}" +
				"   .result_status_off{opacity : 50%}" +
				"	.not_exist{display:block;height:600px;background:url(/db_report/images/not_exist.png);}\n" +
				"	.result_total_count_1{display:inline-block;width:143px;height:143px;margin:0px;text-align:center;background:url(../images/result_total_count_1.png);}\n" + 
				"	.result_total_count_2{display:inline-block;width:143px;height:143px;margin:0px;text-align:center;background:url(../images/result_total_count_2.png);}\n" + 
				"	.result_total_count_3{display:inline-block;width:143px;height:143px;margin:0px;text-align:center;background:url(../images/result_total_count_3.png);}\n" +
				"	.rtc_title{cursor:default !important;text-align:center;padding-top:50px;color:white;font-size:13px;font-weight:600;}\n" +
				"	.rtc_value{cursor:default !important;text-align:center;padding-top:5px;color:white;font-size:20px;font-weight:600;}\n" +
				"	/* Reset - HTML 4 & HTML 5 reset ----------- */\n" + 
				"	html, body, div, span, object, iframe, h1, h2, h3, h4, h5, h6, p,\n" + 
				"	blockquote, pre, abbr, address, cite, code, del, dfn, em, img, ins, kbd,\n" + 
				"	a, q, samp, small, strong, sub, sup, var, b, i, dl, dt, dd, ol, ul, li,\n" + 
				"	fieldset, form, label, table, caption, article, aside, details,\n" + 
				"	figcaption, figure, footer, header, hgroup, menu, section, summary,\n" + 
				"	time, mark, audio, video {\n" + 
				"		margin: 0;\n" + 
				"		padding: 0;\n" + 
				"		border: 0;\n" + 
				"		font-size: 11px;\n" + 
				"		vertical-align: baseline;\n" + 
				"		background: transparent;\n" + 
				"		font-family: 'Open Sans', 'Open Sans Bold', 'Nanum Barun Gothic',\n" + 
				"		'Nanum Barun Gothic Bold', Arial, Helvetica, sans-serif, AppleGothic;\n" + 
				"	}\n" + 
				"	li {\n" + 
				"		list-style: none;\n" + 
				"	}\n" +
				"	ul, ol {\n" + 
				"		list-style: none;\n" + 
				"	}\n" +
				"	.title{width: 100%;height: 28px;}\n";
				if(isWrite) {
					html = html + ".checkbox-wrap { cursor: pointer; display: contents;}\r\n" + 
							".checkbox-wrap .check-icon  { display: inline-block; width: 73px; height: 27px; margin-left: 76px; background: url(images/action_step_01.png) left center no-repeat !important; vertical-align: middle; transition-duration: .3s; }\r\n" +
							".checkbox-wrap input[type=checkbox] { display: none; }\r\n" + 
							".checkbox-wrap input[type=checkbox]:checked + .check-icon { background-image: url(images/action_step_02.png) !important; }\r\n" +
							"}";
				}
				html = html + "</style>\n";
		
		return html;
	}
	public String referenceCSS_include() {
		String html = 
				"<style type=\"text/css\">\n" +
				"	body				{overflow-x:hidden;}"+
				"	body.om				{font:bold 14px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; letter-spacing:-2px; text-align:left;}\n" + 
				"	h1.top				{font:bold 30px Arial,Helvetica,Geneva,sans-serif; color:#383838; background-color:White; margin-top:0px; margin-bottom:30px; letter-spacing:-2px; text-align:left;}\n" + 
				"	h1.title			{font:bold 30px Arial,Helvetica,Geneva,sans-serif; color:#383838; background-color:White; margin-top:50px; margin-bottom:30px; letter-spacing:-2px; text-align:left;}\n" + 
				"	h2.title			{font:bold 23px Arial,Helvetica,Geneva,sans-serif; color:#383838; background-color:White; margin-top:30px; margin-bottom:5px; margin-left:15px; letter-spacing:-2px; text-align:left;}\n" + 
				"	h3.title			{font:bold 17px Arial,Helvetica,Geneva,sans-serif; color:#004370; background-color:White; margin-top:22px; margin-bottom:0px; margin-left:25px; letter-spacing:-1px; text-align:left;}\n" + 
				"	h4.title			{font:bold 13px Arial,Helvetica,Geneva,sans-serif;color:#383838;background-color:White; margin-top:5px; margin-bottom:0px; margin-left:35px; letter-spacing:-1px; text-align:left;}\n" + 
				"	t1.title			{font:14px Arial,Helvetica,Geneva,sans-serif;color:black;background-color:White; margin-top:20px; margin-bottom:0px; letter-spacing:-2px; text-align:left;}\n" + 
				"	li.title			{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White;letter-spacing:-1px; text-align:left;}\n" + 
				"	a.om				{font:bold 12px Arial,Helvetica,sans-serif; text-decoration: underline; color:#663300; vertical-align:top; margin-top:0px; margin-bottom:0px; text-align:left;}" +
				"	li.contents			{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; letter-spacing:-1px; text-align:left;}\n" + 
				"	dd.contents2		{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; margin-top:0px; margin-bottom:15px; margin-left:25px; letter-spacing:0px; text-align:left;}\n" + 
				"	dd.contents2-b5		{font: 14px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; margin-top:0px; margin-bottom:5px; margin-left:25px; letter-spacing:0px; text-align:left;}\n" + 
				"	dd.contents3		{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; margin-top:0px; margin-bottom:15px; margin-left:25px; letter-spacing:0px; text-align:left;}\n" + 
				"	dd.contents4		{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; margin-top:0px; margin-bottom:15px; margin-left:25px; letter-spacing:0px; text-align:left;}\n" + 
				"	table.awr			{border-spacing:0px !important;padding-bottom:30px;}\n" + 
				"	table.om			{border-spacing:0px !important; margin-bottom:10px;}\n" + 
				"	table.om2			{border-spacing:0px !important; margin-top:0px; margin-bottom:10px; margin-left:25px; width:975px;width: calc(100% - 50px);}\n" + 
				"	table.om2-b20		{border-spacing:0px !important; margin-top:0px; margin-bottom:20px; margin-left:25px; width:975px;width: calc(100% - 50px);}\n" + 
				"	table.om3			{border-spacing:0px !important; margin-top:0px; margin-bottom:10px; margin-left:25px; width:975px;width: calc(100% - 50px);}\n" + 
				"	table.om4			{border-spacing:0px !important; margin-top:-5px; margin-bottom:10px; margin-left:35px; width:965px;width: calc(100% - 50px);}\n" + 
				"	th.om-left			{font:bold 12px Arial,Helvetica,Geneva,sans-serif; color:#146298; background:#EEF9FF; padding:5px 5px 5px 5px; text-align:center; border-top:2px solid #4289bb; border-right:1px solid #4289bb; border-bottom:1px solid #4289bb; border-left:1px solid #FFFFFF;}\n" + 
				"	th.om-center		{font:bold 12px Arial,Helvetica,Geneva,sans-serif; color:#146298; background:#EEF9FF; padding:5px 5px 5px 5px; text-align:center; border-top:2px solid #4289bb; border-right:1px solid #4289bb; border-bottom:1px solid #4289bb;}\n" + 
				"	th.om-right			{font:bold 12px Arial,Helvetica,Geneva,sans-serif; color:#146298; background:#EEF9FF; padding:5px 5px 5px 5px; text-align:center; border-top:2px solid #4289bb; border-right:1px solid #FFFFFF; border-bottom:1px solid #4289bb;}\n" + 
				"	td.om-left			{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #969696; border-bottom:1px solid #969696; border-left:1px solid #FFFFFF;}\n" + 
				"	td.om-center		{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #969696; border-bottom:1px solid #969696;}\n" + 
				"	td.om-right			{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #FFFFFF; border-bottom:1px solid #969696;}\n" + 
				"	td.om-left-close	{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #969696; border-bottom:2px solid #4289bb; border-left:1px solid #FFFFFF;}\n" + 
				"	td.om-center-close	{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #969696; border-bottom:2px solid #4289bb;}\n" + 
				"	td.om-right-close	{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #FFFFFF; border-bottom:2px solid #4289bb;}\n" + 
				"	td.om-right-font		{font:12px 돋움체, Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #FFFFFF; border-bottom:1px solid #969696;}\n" + 
				"	td.om-right-close-font	{font:12px 돋움체, Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #FFFFFF; border-bottom:2px solid #4289bb;}\n" + 
				"	td.om-colspan		{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #FFFFFF; border-bottom:2px solid #4289bb; text-align:center;}\n" + 
				"	table.nl1			{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; border-spacing:0px !important;margin-top:0px; margin-bottom:10px;}\n" + 
				"	table.nl2			{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; border-spacing:0px !important;margin-top:0px; margin-bottom:20px;margin-left:25px;}\n" + 
				"	table.nl3			{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; border-spacing:0px !important;margin-top:-5px; margin-bottom:10px;margin-left:25px;}\n" + 
				"	table.nl4			{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; border-spacing:0px !important;margin-top:-5px; margin-bottom:10px;margin-left:35px;}\n" + 
				"	td.nl-left			{font:12px Arial,Helvetica,Geneva,sans-serif;color:black;background:White;vertical-align:top;padding:0px 0px 5px 0px;border-top:1px solid #FFFFFF;border-right:1px solid #FFFFFF;border-bottom:1px solid #FFFFFF;border-left:1px solid #FFFFFF;}\n" + 
				"	td.nl-center		{font:12px Arial,Helvetica,Geneva,sans-serif;color:black;background:White;vertical-align:top;padding:0px 0px 5px 0px;border-top:1px solid #FFFFFF;border-right:1px solid #FFFFFF;border-bottom:1px solid #FFFFFF;border-left:1px solid #FFFFFF;}\n" + 
				"	td.nl-right			{font:12px Arial,Helvetica,Geneva,sans-serif;color:black;background:White;vertical-align:top;padding:0px 0px 5px 0px;border-top:1px solid #FFFFFF;border-right:1px solid #FFFFFF;border-bottom:1px solid #FFFFFF;border-left:1px solid #FFFFFF;}\n" + 
				"	.chart3				{padding-left:25px;}" +
				"	.chart4				{padding-left:35px;}" +
				"	.button {" +
				"		width:50px;\n" + 
				"		height:22px;\n" +
				"		background-color: #FFF;\n" + 
				"		border: 1px solid #217346;\n" + 
				"		color:#217346;\n" + 
				"		text-align: center;\n" + 
				"		text-decoration: none;\n" + 
				"		display: inline-block;\n" + 
				"		font-size: 15px;\n" + 
				"		margin: 4px;\n" + 
				"		cursor: pointer;\n" + 
				"		border-radius:7px;\n" +
				"		float: right;\n" +
				"	}\n" + 
				"	.button:hover {background-color: #217346;color:#FFF;}\n" + 
				"	.button:hover {background-color: #217346;color:#FFF;}\n" + 
				"	ul.tabbed{\n" + 
				"		margin: 0px;\n" + 
				"		padding: 0px;\n" + 
				"		list-style: none;\n" + 
				"		padding-bottom: 0px;\n" +
				"		margin-bottom: 5px;\n" +
				"	}\n" + 
				"	ul.tabbed li{\n" + 
				"		background: #f2f2f2;\n" + 
				"		color: #222;\n" + 
				"		display: inline-block;\n" + 
				"		padding: 10px 15px;\n" + 
				"		cursor: pointer;\n" + 
				"		border-width: 1px;\n" +
				"		border-color: #38312a;\n" +
				"		border-style: solid;\n" +
				"		border-radius: 5px 5px 0px 0px;\n" +
				"		padding-bottom: 11px" +
				"	}\n" + 
				"	ul.tabbed li.current{\n" + 
				"		background: #fff;\n" + 
				"		color: #222;\n" + 
				"		border-width: 1px 1px 0px 1px;\n" +
				"		border-color: #38312a;\n" +
				"		border-style: solid;\n" +
				"		border-radius: 5px 5px 0px 0px;\n" +
				"		padding-bottom: 11px" +
				"	}\n" + 
				"	.tab-content{\n" + 
				"		visibility: hidden;\n" + 
				"		background: #fff;\n" + 
				"		padding: 0px;\n" + 
				"		display: block;\n" + 
				"		width: calc(100% - 30px);\n" + 
				"		height: 0px;\n" + 
				"		position: relative;\n" + 
				"	}\n" + 
				"	.tab-content.current{\n" + 
				"		visibility: visible;\n" + 
				"		padding: 15px;\n" + 
				"		display: block;\n" + 
				"		height: calc(100% - 30px);\n" + 
				"	}\n" +
//				"	.result_status{float:right;width:321px;height:17px;margin:8px 15px 0px 0px;background:url(../images/result_status.png);}" +
				"	.result_status{float:right;width:321px;height:17px;margin:8px 15px 0px 0px;}" +
				"	.result_status_on{opacity : 100%}" +
				"   .result_status_off{opacity : 50%}" +
				"	.not_exist{display:block;height:600px;background:url(/db_report/images/not_exist.png);}\n" +
				"	.result_total_count_1{display:inline-block;width:143px;height:143px;margin:0px;text-align:center;background:url(../images/result_total_count_1.png);}\n" + 
				"	.result_total_count_2{display:inline-block;width:143px;height:143px;margin:0px;text-align:center;background:url(../images/result_total_count_2.png);}\n" + 
				"	.result_total_count_3{display:inline-block;width:143px;height:143px;margin:0px;text-align:center;background:url(../images/result_total_count_3.png);}\n" +
				"	.rtc_title{cursor:default !important;text-align:center;padding-top:50px;color:white;font-size:13px;font-weight:600;}\n" +
				"	.rtc_value{cursor:default !important;text-align:center;padding-top:5px;color:white;font-size:20px;font-weight:600;}\n" +
				"	/* Reset - HTML 4 & HTML 5 reset ----------- */\n" + 
				"	html, body, div, span, object, iframe, h1, h2, h3, h4, h5, h6, p,\n" + 
				"	blockquote, pre, abbr, address, cite, code, del, dfn, em, img, ins, kbd,\n" + 
				"	a, q, samp, small, strong, sub, sup, var, b, i, dl, dt, dd, ol, ul, li,\n" + 
				"	fieldset, form, label, table, caption, article, aside, details,\n" + 
				"	figcaption, figure, footer, header, hgroup, menu, section, summary,\n" + 
				"	time, mark, audio, video {\n" + 
				"		margin: 0;\n" + 
				"		padding: 0;\n" + 
				"		border: 0;\n" + 
				"		font-size: 11px;\n" + 
				"		vertical-align: baseline;\n" + 
				"		background: transparent;\n" + 
				"		font-family: 'Open Sans', 'Open Sans Bold', 'Nanum Barun Gothic',\n" + 
				"		'Nanum Barun Gothic Bold', Arial, Helvetica, sans-serif, AppleGothic;\n" + 
				"	}\n" + 
				"	li {\n" + 
				"		list-style: none;\n" + 
				"	}\n" +
				"	ul, ol {\n" + 
				"		list-style: none;\n" + 
				"	}\n" +
				"	.title{width: 100%;height: 28px;}\n" +
				"</style>\n";
		
		return html;
	}
	private void deleteZipFile() throws Exception {
		File file = new File(rootPath);
		String fileList[] = file.list();
		
		logger.debug("check Delete zipFileName[" + zipFileName + "] directory[" + rootPath + "]");
		
		try {
			for(int fileIndex = 0; fileIndex < fileList.length; fileIndex++) {
				String fileName = fileList[fileIndex];
				
				if(fileName.contains(".zip")) {
					logger.debug("Delete zipFileName[" + zipFileName + "] directory[" + rootPath + "]");
					File deleteFile = new File(rootPath + fileName);
					deleteFile.delete();
				}
			}
		} catch(Exception ex) {
			throw ex;
		}
	}
	
	private void compress() throws Exception {
		File sourceDir = new File(rootPath + "/resources/db_report/");
		File targetDir = new File(rootPath + zipFileName);
		
		ZipUtil.pack(sourceDir, targetDir);
	}
	
	@RequestMapping(value = "/download", method = { RequestMethod.GET, RequestMethod.POST })
	public void download(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("reportHtml") ReportHtml reportHtml, Model model) throws Exception {
		String zipPath = req.getSession().getServletContext().getRealPath("/") + zipFileName;
		File downloadFile = new File(zipPath);
		FileInputStream inputstream = new FileInputStream(downloadFile);
		String mimetype = req.getServletContext().getMimeType(zipPath);
		
		if(mimetype == null) {
			mimetype = "application/octet-stream";
		}
		
		res.setContentType(mimetype);
		res.setContentLength((int) downloadFile.length());
		res.setHeader("Content-Disposition", "attachment;filename=" + zipFileName);
		
		ServletOutputStream outStream = res.getOutputStream();
		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		
		while((bytesRead = inputstream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}
		
		inputstream.close();
		outStream.close();
	}
}