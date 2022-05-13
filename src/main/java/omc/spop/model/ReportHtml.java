package omc.spop.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2019.10.23 명성태 최초작성
 **********************************************************/

@Alias("reportHtml")
public class ReportHtml extends Base implements Jsonable {
	public String excelTag(int slt_program_sql_number) {
//		return "<button class=\"button\" onclick=\"Excel_Download(" + slt_program_sql_number + "); return false;\">Excel</button>\n";
		return "";
	}
	
	public String tableStyle1(int level) {
		return "\n\t\t<table class=\"om" + level + "\" >\n" +
				"\t\t\t<thead>\n\t\t\t\t<tr>\n";
	}
	
	public String tableStyle1Fixed(int level) {
		return "\n\t\t<table class=\"om" + level + "\" style=\"table-layout: fixed\" >\n" +
				"\t\t\t<thead>\n\t\t\t\t<tr>\n";
	}
	
	public String tableStyle2(int level) {
		return "\n\t\t<table class=\"om" + level + "-b20\" >\n" +
				"\t\t\t<thead>\n\t\t\t\t<tr>\n";
	}
	
	public String tableStyle2Fixed(int level) {
		return "\n\t\t<table class=\"om" + level + "-b20\" style=\"table-layout: fixed\" >\n" +
				"\t\t\t<thead>\n\t\t\t\t<tr>\n";
	}
	public String tableStyle4(int level) {
		return "\n\t\t<table class=\"om" + level + "\" style='table-layout:fixed;word-wrap:break-word;word-break:break-all;margin-bottom:0px;' >\n" +
				"\t\t\t<thead>\n\t\t\t\t<tr>\n";
	}
	public String tableStyle5(int level , String id) {
		return "\n\t\t<table class=\"om" + level + "\" id='"+id+"'style='word-wrap:break-word;word-break:break-all;margin-bottom:0px;' >\n" +
				"\t\t\t<tbody>\n";
	}

	public String tableStyle1Head(int position, String name) {
		return tableStyle1Head(position, "", name);
	}
	
	public String tableStyle1Head(int position, String field, String name) {
		StringBuffer html = new StringBuffer("\t\t\t\t\t<th class=\"");
		
		switch(position) {
		case 1:	// Left
			html.append("om-left");
			html.append("\" data-options=\"field:'").append(field).append("'\">").append(name).append("</th>\n");
			break;
		case 2:	// Center
			html.append("om-center");
			html.append("\" data-options=\"field:'").append(field).append("'\">").append(name).append("</th>\n");
			break;
		case 3:	// Right
			html.append("om-right");
			html.append("\" data-options=\"field:'").append(field).append("'\">").append(name).append("</th>\n").append("\t\t\t\t</tr>\n\t\t\t</thead>\n");
			break;
		}
		
		return html.toString();
	}
	public String tableStyle1Head(int position, String name , boolean is_SqlId) {
		return tableStyle1Head(position, "", name , is_SqlId);
	}
	
	public String tableStyle1Head(int position, String field, String name, boolean is_SqlId) {
		StringBuffer html = new StringBuffer("\t\t\t\t\t<th class=\"");
		String style="";
		if(is_SqlId) {
			style =  " style='width:100px;' " ;
		}
		
		switch(position) {
		case 1:	// Left
			html.append("om-left");
			html.append("\"").append(style).append("data-options=\"field:'").append(field).append("'\">").append(name).append("</th>\n");
			break;
		case 2:	// Center
			html.append("om-center");
			html.append("\"").append(style).append("data-options=\"field:'").append(field).append("'\">").append(name).append("</th>\n");
			break;
		case 3:	// Right
			html.append("om-right");
			html.append("\"").append(style).append("data-options=\"field:'").append(field).append("'\">").append(name).append("</th>\n").append("\t\t\t\t</tr>\n\t\t\t</thead>\n");
			break;
		}
		
		return html.toString();
	}

	public String tableStyle1Head(int position, String field, String name, int width) {
		StringBuffer html = new StringBuffer("\t\t\t\t\t<th width=\"");
		
		html.append(width).append("%\" class=\"");
		
		switch(position) {
		case 1:	// Left
			html.append("om-left");
			html.append("\" data-options=\"field:'").append(field).append("'\">").append(name).append("</th>\n");
			break;
		case 2:	// Center
			html.append("om-center");
			html.append("\" data-options=\"field:'").append(field).append("'\">").append(name).append("</th>\n");
			break;
		case 3:	// Right
			html.append("om-right");
			html.append("\" data-options=\"field:'").append(field).append("'\">").append(name).append("</th>\n").append("\t\t\t\t</tr>\n\t\t\t</thead>\n");
			break;
		}
		
		return html.toString();
	}
	
	/**
	 * 
	 * @param position
	 * @param value
	 * @param align 0 : left, 1 : center, 2 : right
	 * @return
	 */
	public String tableStyle1Body(int position, String value, int align) {
		StringBuffer html = new StringBuffer();
		String alignText = "";
		
		switch(align) {
		case 0: alignText = " style=\"text-align:left;word-break:break-all;white-space:pre-line\""; break;
		case 1: alignText = " style=\"text-align:center;word-break:break-all;\""; break;
		case 2: alignText = " style=\"text-align:right;word-break:break-all;\""; break;
		}
		
		if(value.equalsIgnoreCase("NULL")) {
			value = "";
		}
		
		switch(position) {
		case 1:	// Left
			html.append("\t\t\t<tr>\n\t\t\t\t<td class=\"");
			html.append("om-left");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>");
			break;
		case 2:	// Center
			html.append("\t<td class=\"");
			html.append("om-center");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>");
			break;
		case 3:	// Right
			html.append("\t<td class=\"");
			html.append("om-right");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>\n\t\t\t</tr>\n");
			break;
		}
		
		return html.toString();
	}
	
	public String tableStyle1BodyClose(int position, String value, int align) {
		StringBuffer html = new StringBuffer();
		String alignText = "";
		
		if(value.equalsIgnoreCase("NULL")) {
			value = "";
		}
		
		switch(align) {
		case 0: alignText = " style=\"text-align:left;word-break:break-all;\""; break;
		case 1: alignText = " style=\"text-align:center;word-break:break-all;\""; break;
		case 2: alignText = " style=\"text-align:right;word-break:break-all;\""; break;
		}
		
		switch(position) {
		case 1:	// Left
			html.append("\t\t\t<tr>\n\t\t\t\t<td class=\"");
			html.append("om-left-close");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>");
			break;
		case 2:	// Center
			html.append("\t<td class=\"");
			html.append("om-center-close");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>");
			break;
		case 3:	// Right
			html.append("\t<td class=\"");
			html.append("om-right-close");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>\n\t\t\t</tr>\n\t\t</table>\n");
			break;
		}
		
		return html.toString();
	}
	
	/**
	 * 
	 * @param position
	 * @param value
	 * @param align 0 : left, 1 : center, 2 : right
	 * @return
	 */
	public String tableStyle1BodyFont(int position, String value, int align) {
		StringBuffer html = new StringBuffer();
		String alignText = "";
		
		switch(align) {
		case 0: alignText = " style=\"text-align:left;word-break:break-all;white-space:pre-line\""; break;
		case 1: alignText = " style=\"text-align:center;word-break:break-all;\""; break;
		case 2: alignText = " style=\"text-align:right;word-break:break-all;\""; break;
		}
		
		if(value.equalsIgnoreCase("NULL")) {
			value = "";
		}
		
		switch(position) {
		case 1:	// Left
			html.append("\t\t\t<tr>\n\t\t\t\t<td class=\"");
			html.append("om-left");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>");
			break;
		case 2:	// Center
			html.append("\t<td class=\"");
			html.append("om-center");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>");
			break;
		case 3:	// Right
			html.append("\t<td class=\"");
			html.append("om-right-font");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>\n\t\t\t</tr>\n");
			break;
		}
		
		return html.toString();
	}
	
	public String tableStyle1BodyCloseFont(int position, String value, int align) {
		StringBuffer html = new StringBuffer();
		String alignText = "";
		
		if(value.equalsIgnoreCase("NULL")) {
			value = "";
		}
		
		switch(align) {
		case 0: alignText = " style=\"text-align:left;word-break:break-all;\""; break;
		case 1: alignText = " style=\"text-align:center;word-break:break-all;\""; break;
		case 2: alignText = " style=\"text-align:right;word-break:break-all;\""; break;
		}
		
		switch(position) {
		case 1:	// Left
			html.append("\t\t\t<tr>\n\t\t\t\t<td class=\"");
			html.append("om-left-close");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>");
			break;
		case 2:	// Center
			html.append("\t<td class=\"");
			html.append("om-center-close");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>");
			break;
		case 3:	// Right
			html.append("\t<td class=\"");
			html.append("om-right-close-font");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>\n\t\t\t</tr>\n\t\t</table>\n");
			break;
		}
		
		return html.toString();
	}
	
	/**
	 * colspan style
	 * @param position
	 * @param value
	 * @param align
	 * @return
	 */
	public String tableStyleNoDataClose(int colspan, String value) {
		StringBuffer html = new StringBuffer();
		
		html.append("\t\t\t<td colspan=\"").append(colspan).append("\" class=\"");
		html.append("om-colspan");
		html.append("\"");
//		html.append(">").append(value).append("</td>\n\t\t\t</tr>\n\t\t</table>");
		html.append(">").append(value).append("</td>\n\t\t</table>");
		
		return html.toString();
	}
	
	public String tableNoLineStyle(int level) {
		return "<table class=\"nl" + level + "\">\n" +
				"\t<thead>\n\t\t<tr>\n";
	}
	
	public String tableNoLineNoHeadStyle(int level) {
		return "<table class=\"nl" + level + "\">\n";
	}
	
	public String tableNoLineStyleBody(int position, String value) {
		StringBuffer html = new StringBuffer();
		
		switch(position) {
		case 1:	// Left
			html.append("\t<tr><td class=\"");
			html.append("nl-left");
			html.append("\"");
			html.append(">").append(value).append("</td>");
			break;
		case 2:	// Center
			html.append("\t<td class=\"");
			html.append("nl-center");
			html.append("\"");
			html.append(">").append(value).append("</td>");
			break;
		case 3:	// Right
			html.append("\t<td class=\"");
			html.append("nl-right");
			html.append("\"");
			html.append(">").append(value).append("</td></tr>\n");
			break;
		case 4:	// Only One
			html.append("\t<tr><td class=\"");
			html.append("nl-center");
			html.append("\"");
			html.append(">").append(value).append("</td></tr>\n");
			break;
		}
		
		return html.toString();
	}
	
	public String tableNoLineStyleBodyClose(int position, String value) {
		StringBuffer html = new StringBuffer();
		
		switch(position) {
		case 1:	// Left
			html.append("\t<tr><td class=\"");
			html.append("nl-left");
			html.append("\"");
			html.append(">").append(value).append("</td>");
			break;
		case 2:	// Center
			html.append("\t<td class=\"");
			html.append("nl-center");
			html.append("\"");
			html.append(">").append(value).append("</td>");
			break;
		case 3:	// Right
			html.append("\t<td class=\"");
			html.append("nl-right");
			html.append("\"");
			html.append(">").append(value).append("</td></tr>\n</table>\n");
			break;
		case 4:	// Only One
			html.append("\t<tr><td class=\"");
			html.append("nl-center");
			html.append("\"");
			html.append(">").append(value).append("</td></tr>\n</table>\n");
			break;
		}
		
		return html.toString();
	}
	
	public String replaceVariable(String html, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL, int slt_program_sql_number) {
		int loadSQLSize = loadSQL.size();
		
		if(loadSQLSize > 0) {
			if(html.indexOf(slt_program_sql_number + "_var1") > 0) {
				html = html.replace(slt_program_sql_number + "_var1", loadSQL.get(0).get("VAR1") + "");
			}
			
			if(html.indexOf(slt_program_sql_number + "_var2") > 0) {
				html = html.replace(slt_program_sql_number + "_var2", loadSQL.get(0).get("VAR2") + "");
			}
			
			if(html.indexOf(slt_program_sql_number + "_var3") > 0) {
				html = html.replace(slt_program_sql_number + "_var3", loadSQL.get(0).get("VAR3") + "");
			}
			
			if(html.indexOf(slt_program_sql_number + "_var4") > 0) {
				html = html.replace(slt_program_sql_number + "_var4", loadSQL.get(0).get("VAR4") + "");
			}
			
			if(html.indexOf(slt_program_sql_number + "_var5") > 0) {
				html = html.replace(slt_program_sql_number + "_var5", loadSQL.get(0).get("VAR5") + "");
			}
		} else {
			if(html.indexOf(slt_program_sql_number + "_var1") > 0) {
				html = html.replace(slt_program_sql_number + "_var1", "0");
			}
			
			if(html.indexOf(slt_program_sql_number + "_var2") > 0) {
				html = html.replace(slt_program_sql_number + "_var2", "0");
			}
			
			if(html.indexOf(slt_program_sql_number + "_var3") > 0) {
				html = html.replace(slt_program_sql_number + "_var3", "0");
			}
			
			if(html.indexOf(slt_program_sql_number + "_var4") > 0) {
				html = html.replace(slt_program_sql_number + "_var4", "0");
			}
			
			if(html.indexOf(slt_program_sql_number + "_var5") > 0) {
				html = html.replace(slt_program_sql_number + "_var5", "0");
			}
		}
		
		return html;
	}
	
	public String processSingleData(int level, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL, String clm_label, String clm_legend) {
		int loadSQLSize = loadSQL.size();
		
		ArrayList labelList = new ArrayList();
		ArrayList legend = new ArrayList();
		ArrayList data = new ArrayList();
//		ArrayList dataList1 = new ArrayList();
		ArrayList dataList2 = new ArrayList();
		
		for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
			String label = loadSQL.get(loadSQLIndex).get(clm_label) + "";
//			String data1 = loadSQL.get(loadSQLIndex).get("Instance") + "";
			String data2 = loadSQL.get(loadSQLIndex).get(clm_legend) + "";
			
			labelList.add(label);
//			dataList1.add(data1);
			dataList2.add(data2);
		}
		
		legend.add(clm_legend);
		data.add(dataList2);
		
		return reportHtml.lineChartStyle(level, contents_id, labelList, legend, data);
	}
	
	public String processMultiData(int level, String tableHtml, ReportHtml reportHtml, String contents_id, List<LinkedHashMap<String, Object>> loadSQL, String clm_label, String clm_legend, String clm_value) {

		int loadSQLSize = loadSQL.size();
		ArrayList labelList = new ArrayList();
		ArrayList legend = new ArrayList();
		ArrayList data = new ArrayList();
		ArrayList unitData = new ArrayList();
		LinkedHashMap<String, Object> multiData = new LinkedHashMap<String, Object>();
		
		for(int loadSQLIndex = 0; loadSQLIndex < loadSQLSize; loadSQLIndex++) {
			String label = loadSQL.get(loadSQLIndex).get(clm_label) + "";
			String code = loadSQL.get(loadSQLIndex).get(clm_legend) + "";
			String count = loadSQL.get(loadSQLIndex).get(clm_value) + "";
			
			if(!labelList.contains(label)) {
				labelList.add(label);
			}
			
			if(!legend.contains(code)) {
				legend.add(code);
			}
			
			if(!multiData.containsKey(code)) {
				unitData = new ArrayList();
				
				unitData.add(count);
				
				multiData.put(code, unitData);
			} else if(multiData.containsKey(code)) {
				unitData = (ArrayList) multiData.get(code);
				
				unitData.add(count);
			}
		}
		
		for(int index = 0; index < legend.size(); index++) {
			String tmpLegend = legend.get(index) + "";
			
			data.add(multiData.get(tmpLegend));
		}
		
		return reportHtml.lineChartStyle(level, contents_id, labelList, legend, data, true);
	}
	
	private ArrayList colorList() {
		ArrayList colorList = new ArrayList();
		
		colorList.add("crimson");
		colorList.add("teal");
		colorList.add("purple");
		colorList.add("#0099a4");	// 울트라 마린
		colorList.add("lime");
		colorList.add("#998000");	// Heart Gold
		colorList.add("wheat");
		colorList.add("orange");
		colorList.add("#003CB3");	// Royal azure
		colorList.add("coral");
		colorList.add("magenta");
		colorList.add("olive");
		colorList.add("cyan");
		
		return colorList;
	}
	
	public String noDataChartStyle() {
		return "<dd class=\"contents2\"> - N/A : 해당 진단 항목에 대한 데이터가 없습니다.</dd>";
	}
	
	public String lineChartStyle(int level, String contents_id, ArrayList label, ArrayList legend, ArrayList data) {
		return lineChartStyle(level, contents_id, label, legend, data, false);
	}
	
	public String lineChartStyle(int level, String contents_id, ArrayList label, ArrayList legend, ArrayList data, boolean legendFlag) {

		StringBuffer html = new StringBuffer();
		int paddingLeft = 0;
		
		if(level == 3) {
			paddingLeft = 25;
		} else if(level == 4) {
			paddingLeft = 35;
		}
		
		
		html.append("<canvas id=\"lineChart_").append(contents_id).append("\" width=\"300\" height=\"50\" padding-left=\"").append(paddingLeft).append("\">\n");
		html.append("		This text is displayed if your browser does not support HTML5 Canvas. lineChart_").append(contents_id).append(".\n");
		html.append("</canvas>\n");
		html.append("<script>\n");
		html.append("\t// 우선 컨텍스트를 가져옵니다.\n");
		html.append("\t").append("var ctx = document.getElementById(\"lineChart_").append(contents_id).append("\").getContext('2d');\n");
		html.append("\t/*\n");
		html.append("\t").append("- Chart를 생성하면서, \n");
		html.append("\t").append("- ctx를 첫번째 argument로 넘겨주고, \n");
		html.append("\t").append("- 두번째 argument로 그림을 그릴때 필요한 요소들을 모두 넘겨줍니다. \n");
		html.append("*/\n");
		html.append("\t").append("var lineChartData = {\n");
		html.append("\t\t").append("labels:").append(label).append(",\n");
		
		ArrayList colorList = colorList();
		
		html.append("\t\t").append("datasets: [\n");
		
		for(int dataIndex = 0; dataIndex < data.size(); dataIndex++) {
			html.append("\t\t\t{\n");
			
			html.append("\t\t\t\t").append("label: \"").append(legend.get(dataIndex)).append("\",\n");
			html.append("\t\t\t\t").append("data:").append(data.get(dataIndex)).append(",\n");
			html.append("\t\t\t\t").append("fill: false,\n");
			html.append("\t\t\t\t").append("borderColor: \"").append(colorList.get(dataIndex)).append("\",\n");
			html.append("\t\t\t\t").append("backgroundColor: 'transparent',\n");
			html.append("\t\t\t\t").append("borderDash: [],\n");
//			html.append("\t\t\t\t").append("pointBorderColor: 'orange',\n");
//			html.append("\t\t\t\t").append("pointBackgroundColor: 'rgba(255,150,0,0.5)',\n");
//			html.append("\t\t\t\t").append("pointRadius: 5,\n");
//			html.append("\t\t\t\t").append("pointHoverRadius: 10,\n");
//			html.append("\t\t\t\t").append("pointHitRadius: 30,\n");
//			html.append("\t\t\t\t").append("pointBorderWidth: 2,\n");
//			html.append("\t\t\t\t").append("pointStyle: 'rectRounded'\n");
			
			html.append("\t\t\t}");
			
			if(dataIndex < (data.size() - 1)) {
				html.append(",");
			}
			
			html.append("\n");
		}
		
		html.append("\t\t").append("]\n");
		
		html.append("\t").append("};\n");
		html.append("\t").append("var options = {\n");
		
		if(!legendFlag) {
			html.append("\t\t").append("legend: {display: false},\n");
		}
		
		html.append("\t\t").append("elements: {\n");
		html.append("\t\t\t").append("point: {\n");
		html.append("\t\t\t\t").append("radius: 0\n");
		html.append("\t\t\t").append("}\n");
		html.append("\t\t").append("},\n");
		html.append("\t\t").append("scales: {\n");
		html.append("\t\t\t").append("yAxes: [{\n");
		html.append("\t\t\t\t").append("ticks: {\n");
		html.append("\t\t\t\t\t").append("beginAtZero: true,\n");
//		html.append("\t\t\t\t\t").append("stepSize: 20");
		
//		html.append("\t\t\t\t\t").append("userCallback: function(value, index, values) {\n");
//		html.append("\t\t\t\t\t\t").append("value = value.toString();\n");
////		html.append("\t\t\t\t\t\t").append("value = value.split(/(?=(?:...)*$)/);\n");
//		html.append("\t\t\t\t\t\t").append("var pointIndex = value.indexOf(\"\\.\");\n");
//		html.append("\t\t\t\t\t\t").append("if(pointIndex > 0) {\n");
//		html.append("\t\t\t\t\t\t\t").append("var len = value.length;\n");
//		html.append("\t\t\t\t\t\t\t").append("var range = len - pointIndex;\n");
//		html.append("\t\t\t\t\t\t\t").append("var pointRange = \"\";\n");
//		html.append("\t\t\t\t\t\t\t").append("for(var i = 0; i < range; i++) {\n");
//		html.append("\t\t\t\t\t\t\t\t").append("pointRange += \".\";\n");
//		html.append("\t\t\t\t\t\t\t").append("}\n");
//		html.append("\t\t\t\t\t\t\t").append("var pattern = new RegExp('/(?=(?:' + pointRange + ')*$)/');\n");
//		html.append("\t\t\t\t\t\t\t").append("value = value.split(pattern);\n");
//		html.append("\t\t\t\t\t\t").append("} else {\n");
//		html.append("\t\t\t\t\t\t\t").append("value = value.split(/(?=(?:...)*$)/);\n");
//		html.append("\t\t\t\t\t\t\t").append("value = value.join(',');\n");
//		html.append("\t\t\t\t\t\t").append("}\n");
//		html.append("\t\t\t\t\t\t").append("return value;\n");
//		html.append("\t\t\t\t\t").append("}\n");
		
		html.append("\t\t\t\t").append("}\n");
		html.append("\t\t\t").append("}]\n");
		html.append("\t\t").append("}\n");
		html.append("\t").append("};\n");
		html.append("\t").append("var lineChart_").append(contents_id).append(" = new Chart(ctx, {\n");
		html.append("\t\t").append("type: 'line',\n");
		html.append("\t\t").append("data: lineChartData,\n");
		html.append("\t\t").append("options: options\n");
		html.append("\t").append("});\n");
		html.append("</script>\n");
		
		return html.toString();
	}
	
	public String pieChartStyle(int level, String contents_id, ArrayList<String> label, ArrayList data) {
		StringBuffer html = new StringBuffer();
		ArrayList colorList = colorList();
		
		html.append("<canvas id=\"pieChart_").append(contents_id).append("\" width=\"300\" height=\"50\" padding-left=\"25\">\n");
		html.append("	This text is displayed if your browser does not support HTML5 Canvas. samplePieChart1.\n");
		html.append("</canvas>\n");
		html.append("<script>\n");
		html.append("\t").append("// 우선 컨텍스트를 가져옵니다. \n");
		html.append("\t").append("").append("var ctx = document.getElementById(\"pieChart_").append(contents_id).append("\").getContext('2d');\n");
		html.append("\t").append("/*\n");
		html.append("\t").append("- Chart를 생성하면서, \n");
		html.append("\t").append("- ctx를 첫번째 argument로 넘겨주고, \n");
		html.append("\t").append("- 두번째 argument로 그림을 그릴때 필요한 요소들을 모두 넘겨줍니다. \n");
		html.append("\t").append("*/\n");
		html.append("\t").append("var pieDataSets = {\n");
		html.append("\t\t").append("label: 'PIE',\n");
		html.append("\t\t").append("data:" + data + ",\n");
		html.append("\t\t").append("backgroundColor:[\n");
		html.append("\t\t\t");
		
		for(int index = 0; index < data.size(); index++) {
			html.append("\"").append(colorList.get(index)).append("\"");
			
			if(index < data.size() - 1) {
				html.append(", ");
			}
		}
		
		html.append("\t\t").append("\n]\n\t};\n");
		html.append("\t").append("var pieData = {\n");
		html.append("\t\t").append("labels:[\n");
		html.append("\t\t\t");
		
		for(int index = 0; index < label.size(); index++) {
			html.append("\"").append(label.get(index)).append("\"");
			
			if(index < label.size() - 1) {
				html.append(", ");
			}
		}
		
		html.append("\t\t").append("\n],\n");
		html.append("\t\t").append("datasets: [pieDataSets]\n");
		html.append("\t").append("};\n");
		html.append("\t").append("var chartOptions = {\n");
		html.append("\t\t").append("plugins: {\n");
		html.append("\t\t\t").append("labels: {\n");
		html.append("\t\t\t\t").append("render: 'value',\n");
		html.append("\t\t\t\t").append("fontColor: '#fff'");
		html.append("\t\t\t").append("}\n");
		html.append("\t\t").append("},\n");
//		html.append("\t\t").append("legend: {display: false},\n");
		html.append("\t\t").append("title: {\n");
		html.append("\t\t\t").append("display: false,\n");
		html.append("\t\t").append("}\n");
		html.append("\t").append("};\n");
		html.append("\t").append("var pieChart_").append(contents_id).append(" = new Chart(ctx, {\n");
		html.append("\t\t").append("type: 'pie',\n");
		html.append("\t\t").append("data: pieData,\n");
		html.append("\t\t").append("options: chartOptions\n");
		html.append("\t").append("});\n");
		html.append("</script>");
		
		return html.toString();
	}
	
	public String barChartStyle(int level, String contents_id, ArrayList label, ArrayList legend, ArrayList data) {
		StringBuffer html = new StringBuffer();
		
		html.append("<canvas id=\"barChart_" + contents_id + "\" width=\"300\" height=\"50\" padding-left=\"25\">\n");
		html.append("	This text is displayed if your browser does not support HTML5 Canvas. barChart_\"" + contents_id + "\".\n");
		html.append("</canvas>\n");
		html.append("<script>\n");
		html.append("\t").append("// 우선 컨텍스트를 가져옵니다. \n");
		html.append("\t").append("var ctx = document.getElementById(\"barChart_" + contents_id + "\").getContext('2d');\n");
		html.append("\t").append("/*\n");
		html.append("\t").append("- Chart를 생성하면서, \n");
		html.append("\t").append("- ctx를 첫번째 argument로 넘겨주고, \n");
		html.append("\t").append("- 두번째 argument로 그림을 그릴때 필요한 요소들을 모두 넘겨줍니다. \n");
		html.append("\t").append("*/\n");
		html.append("\t").append("var barChartData = {\n");
//		html.append("\t\t").append("labels:").append(label).append(",\n");
		
		html.append("\t\t").append("labels:[");
		
		for(int index = 0; index < label.size(); index++) {
			html.append("\"").append(label.get(index)).append("\"");
			
			if(index < label.size() - 1) {
				html.append(", ");
			}
		}
		
		ArrayList colorList = colorList();
		
		html.append("],\n");
		html.append("\t\t").append("datasets:[\n");
		
		for(int index = 0; index < data.size(); index++) {
			html.append("\t\t\t{\n");
			html.append("\t\t\t\t").append("label:\"").append(legend.get(index)).append("\",\n");
			html.append("\t\t\t\t").append("data:").append(data.get(index)).append(",\n");
			html.append("\t\t\t\t").append("backgroundColor:\"").append(colorList.get(index)).append("\",\n");
			html.append("\t\t\t\t").append("borderColor: \"").append(colorList.get(index)).append("\",\n");
			html.append("\t\t\t\t").append("yAxisID: \"y-axis\"\n");
			
			html.append("\t\t\t}");
			
			if(index < (data.size() - 1)) {
				html.append(",");
			}
			
			html.append("\n");
		}
		
		html.append("\t\t").append("]\n");
		html.append("\t").append("};\n");
		
		html.append("\t").append("var chartOptions = {\n");
		
		html.append("\t\t").append("animation: {\r\n");
		html.append("\t\t\t").append("onComplete: function() {\r\n");
		html.append("\t\t\t\t").append("var chartInstance = this.chart;\r\n");
		html.append("\t\t\t\t").append("ctx = chartInstance.ctx;\r\n");
		html.append("\t\t\t\t").append("ctx.font = Chart.helpers.fontString(Chart.defaults.global.defaultFontSize, Chart.defaults.global.defaultFontStyle, Chart.defaults.global.defaultFontFamily);\r\n");
		html.append("\t\t\t\t").append("ctx.textAlign = 'center';\r\n");
		html.append("\t\t\t\t").append("ctx.textBaseline = 'bottom';\r\n");
		html.append("\t\t\t\t").append("//ctx.fillStyle = '#000';\r\n");
		html.append("\t\t\t\t").append("this.data.datasets.forEach(function(dataset, i) {\r\n");
		html.append("\t\t\t\t\t").append("var meta = chartInstance.controller.getDatasetMeta(i);\r\n");
		html.append("\t\t\t\t\t").append("var isHidden = meta.hidden; //'hidden' property of dataset\r\n");
		html.append("\t\t\t\t\t").append("if (!isHidden) { //if dataset is not hidden\r\n");
		html.append("\t\t\t\t\t\t").append("var meta = chartInstance.controller.getDatasetMeta(i);\r\n");
		html.append("\t\t\t\t\t\t").append("meta.data.forEach(function(bar, index) {\r\n");
		html.append("\t\t\t\t\t\t\t").append("var data = dataset.data[index];\r\n");
		html.append("\t\t\t\t\t\t\t").append("//ctx.fillText(data, bar._model.x, bar._model.y - 5);\r\n");
		html.append("\t\t\t\t\t\t\t").append("ctx.fillText(data, bar._model.x, bar._model.y);\r\n");
		html.append("\t\t\t\t\t\t").append("});\r\n");
		html.append("\t\t\t\t\t").append("}\r\n");
		html.append("\t\t\t\t").append("});\r\n");
		html.append("\t\t\t").append("}\r\n");
		html.append("\t\t").append("},");
		
		html.append("\t\t").append("plugins: {\r\n");
		html.append("\t\t\t").append("labels: false	// remove percentage data label\r\n");
		html.append("\t\t").append("},");
		
		html.append("\t\t").append("scales: {\r\n");
		html.append("\t\t\t").append("xAxes: [{\r\n");
		html.append("\t\t\t\t").append("//barPercentage: 1,\r\n");
		html.append("\t\t\t\t").append("//categoryPercentage: 0.8,\r\n");
		html.append("\t\t\t\t").append("ticks: {\r\n");
		html.append("\t\t\t\t\t").append("autoSkip: false,\r\n");
		html.append("\t\t\t\t").append("}\r\n");
		html.append("\t\t\t").append("}],\r\n");
		html.append("\t\t\t").append("yAxes: [{\r\n");
		html.append("\t\t\t\t").append("id: \"y-axis\",\r\n");
		html.append("\t\t\t\t\t").append("ticks: {\r\n");
		html.append("\t\t\t\t\t\t").append("beginAtZero: true,\r\n");
		html.append("\t\t\t\t\t\t").append("display: true\r\n");
		html.append("\t\t\t\t\t").append("}\r\n");
		html.append("\t\t\t\t").append("}]\r\n");
		html.append("\t\t\t").append("}");
		html.append("\t").append("};\n");
		
		html.append("\t").append("var barChart_" + contents_id + " = new Chart(ctx, {\n");
		html.append("\t\t").append("type: 'bar',\n");
		html.append("\t\t").append("data: barChartData,\n");
		html.append("\t\t").append("options: chartOptions\n");
		html.append("\t").append("});\n");
		html.append("</script>");
		
		return html.toString();
	}
	
	public String areaLineChartStyle(int level, String contents_id, ArrayList label, ArrayList legend, ArrayList data) {
		StringBuffer html = new StringBuffer();
		
		html.append("<canvas id=\"areaLineChart_").append(contents_id).append("\" width=\"300\" height=\"50\" padding-left=\"25\">\n");
		html.append("	This text is displayed if your browser does not support HTML5 Canvas. areaLineChart_").append(contents_id).append(".\n");
		html.append("</canvas>");
		html.append("\n");
//		html.append("</div>\n");
		html.append("<script>\n");
		html.append("\t").append("// 우선 컨텍스트를 가져옵니다. \n");
		html.append("\t").append("var ctx = document.getElementById(\"areaLineChart_").append(contents_id).append("\").getContext('2d');\n");
		html.append("\t").append("/*\n");
		html.append("\t").append("- Chart를 생성하면서, \n");
		html.append("\t").append("- ctx를 첫번째 argument로 넘겨주고, \n");
		html.append("\t").append("- 두번째 argument로 그림을 그릴때 필요한 요소들을 모두 넘겨줍니다. \n");
		html.append("\t").append("*/\n");
		html.append("\t").append("var areaLineChartData = {\n");
		html.append("\t\t").append("labels:").append(label).append(",\n");
		
		ArrayList colorList = colorList();
		
		html.append("\t\t").append("datasets: [\n");
		
		for(int index = 0; index < data.size(); index++) {
			html.append("\t\t\t").append("{\n");
			
			html.append("\t\t\t\t").append("label:\"").append(legend.get(index)).append("\",\n");
			html.append("\t\t\t\t").append("data:").append(data.get(index)).append(",\n");
			html.append("\t\t\t\t").append("fill: true,\n");
			html.append("\t\t\t\t").append("borderColor: \"").append(colorList.get(index)).append("\",\n");
			html.append("\t\t\t\t").append("backgroundColor: \"").append(colorList.get(index)).append("\"\n");
			
			html.append("\t\t\t").append("}");
			
			if(index < (data.size() - 1)) {
				html.append(",");
			}
			
			html.append("\n");
		}
		
		html.append("\t\t").append("]\n");
		html.append("\t").append("};\n");
		
		html.append("\t").append("var options = {\n");
		html.append("\t\t").append("elements: {\n");
		html.append("\t\t\t").append("point: {\n");
		html.append("\t\t\t\t").append("radius: 0\n");
		html.append("\t\t\t").append("}\n");
		html.append("\t\t").append("},\n");
		html.append("\t\t").append("scales: {\n");
		html.append("\t\t\t").append("yAxes: [{\n");
		html.append("\t\t\t\t").append("ticks: {\n");
		html.append("\t\t\t\t\t").append("beginAtZero: true\n");
		html.append("\t\t\t\t").append("}\n");
		html.append("\t\t\t").append("}]\n");
		html.append("\t\t").append("}\n");
		html.append("\t").append("};\n");
		html.append("\t").append("var areaLineChart_").append(contents_id).append(" = new Chart(ctx, {\n");
		html.append("\t\t").append("type: 'line',\n");
		html.append("\t\t").append("data: areaLineChartData,\n");
		html.append("\t\t").append("options: options\n");
		html.append("\t").append("});\n");
		html.append("</script>");
		
		return html.toString();
	}
	
	public String getResultColorIcon(int iconNumber) {
		String icon = "！";
		
		switch(iconNumber) {
		case 9678:	// 우수
			icon = this.getIconExcellent();
			break;
		case 9675:	// 양호
			icon = this.getIconGood();
			break;
		case 9651:	// 조치필요
			icon = this.getIconAction();
			break;
		case 9661:	// 긴급조치
			icon = this.getIconEmergency();
			break;
		case 9633:	// 확인필요
			icon = this.getIconConfirm();
			break;
		default:	// 정의되지 않은
			break;
		}
		
		return icon;
	}
	
	private void setRtcMap(int iconNumber) {
		LinkedHashMap rtcMap = this.getRtcMap();
		
		if(rtcMap.containsKey(Integer.toString(iconNumber)) == true) {
			int count = (Integer) rtcMap.get(Integer.toString(iconNumber)) + 1;
			rtcMap.put(Integer.toString(iconNumber), count);
		} else {
			rtcMap.put(Integer.toString(iconNumber), 1);
		}
		
		this.setRtcMap(rtcMap);
	}
	
	public String getResultIcon(int iconNumber, int slt_program_sql_number) {
		String icon = "！";
		LinkedHashMap<String, Object> resultSummary;
		
		switch(iconNumber) {
		case 9678:	// 우수
			icon = this.getIconExcellent();
			break;
		case 9675:	// 양호
			icon = this.getIconGood();
			break;
		case 9651:	// 조치필요
			icon = this.getIconAction();
			setRtcMap(iconNumber);
			resultSummary = this.getResultSummary();
			
			if(slt_program_sql_number == 10021 && resultSummary.containsKey("P128")) {
				break;
			}
			
			resultSummary.put(this.redefinitionKey(slt_program_sql_number) + "", icon);
			break;
		case 9661:	// 긴급조치
			icon = this.getIconEmergency();
			setRtcMap(iconNumber);
			resultSummary = this.getResultSummary();
			
			resultSummary.put(this.redefinitionKey(slt_program_sql_number) + "", icon);
			break;
		case 9633:	// 확인필요
			icon = this.getIconConfirm();
			setRtcMap(iconNumber);
			if((slt_program_sql_number >= 10239 && slt_program_sql_number <= 10312)) {
				resultSummary = this.getResultSummary(); 
				
				resultSummary.put(this.redefinitionKey(slt_program_sql_number) + "", icon);
			}
			break;
		default:	// 정의되지 않은
			break;
		}
		
		return icon;
	}
	
	private String redefinitionKey(int slt_program_sql_number) {
		String contentsId = "";
		
		switch(slt_program_sql_number) {
		case 10006:	// Expired(grace) 계정
			contentsId = "P113";
			break;
		case 10007:	// 파라미터 변경
			contentsId = "P115";
			break;
		case 10008:	// DB File 생성율
			contentsId = "P116";
			break;
		case 10009:	// Library Cache Hit Ratio
			contentsId = "P117";
			break;
		case 10010:	// Dictionary Cache Hit Ratio
			contentsId = "P118";
			break;
		case 10011:	// Buffer Cache Hit Ratio
			contentsId = "P119";
			break;
		case 10012:	// Latch Hit Ratio
			contentsId = "P120";
			break;
		case 10013:	// Parse CPU To Parse Elapsed Ratio
			contentsId = "P121";
			break;
		case 10014:	// Disk Sort Ratio
			contentsId = "P122";
			break;
		case 10015:	// Shared Pool Usage
			contentsId = "P123";
			break;
		case 10016:	// Resource Limit
			contentsId = "P124";
			break;
		case 10020:	// FRA Space
			contentsId = "P128";
			break;
		case 10021:	// FRA Usage Detail
			contentsId = "P128";
			break;
		case 10022:	// ASM Diskgroup Space
			contentsId = "P129";
			break;
		case 10023:	// Tablespace Space
			contentsId = "P130";
			break;
		case 10024:	// Recyclebin Object
			contentsId = "P131";
			break;
		case 10025:	// Invalid Object
			contentsId = "P132";
			break;
		case 10026:	// Nologging Object
			contentsId = "P133";
			break;
		case 10027:	// Parallel Object
			contentsId = "P134";
			break;
		case 10028:	// Unusable Index
			contentsId = "P135";
			break;
		case 10029:	// Chained Rows Table
			contentsId = "P136";
			break;
		case 10030:	// Corrupt Block
			contentsId = "P137";
			break;
		case 10031:	// Sequence Threshold Exceeded
			contentsId = "P138";
			break;
		case 10032:	// Foreign Keys Without Index
			contentsId = "P139";
			break;
		case 10033:	// Disabled Constraint
			contentsId = "P140";
			break;
		case 10034:	// Stale Statistics
			contentsId = "P141";
			break;
		case 10035:	// Statistics Locked Table
			contentsId = "P142";
			break;
		case 10036:	// Long Running Operation
			contentsId = "P143";
			break;
		case 10037:	// Long Running Job
			contentsId = "P144";
			break;
		case 10038:	// Alert Log Error
			contentsId = "P146";
			break;
		case 10039:	// Active Incident
			contentsId = "P147";
			break;
		case 10040:	// Outstanding Alert
			contentsId = "P148";
			break;
		case 10041:	// 오브젝트 진단 - Reorg 대상 점검
			contentsId = "P150";
			break;
		case 10042:	// 파티셔닝 대상 점검
			contentsId = "P151";
			break;
		case 10043:	// 인덱스 사용 점검
			contentsId = "P152";
			break;
		case 10044:	// 파라미터 진단 - RAC 인스턴스 간 다른 파라미터
			contentsId = "P154";
			break;
		case 10045:	// SQL 성능 진단 - Plan 변경
		case 10301:
			contentsId = "P158";
			break;
		case 10046:	// 신규 SQL
		case 10302:	
			contentsId = "P159";
			break;
		case 10047:	// Literal SQL
		case 10303:	
			contentsId = "P160";
			break;
		case 10048:	// Temp 과다 사용 SQL
		case 10304:	
			contentsId = "P161";
			break;
		case 10049:	// Full Scan SQL
		case 10305:	
			contentsId = "P162";
			break;
		case 10050:	// 조건 없는 Delete
			contentsId = "P163";
			break;
		case 10051:	// Offload 비효율 SQL
			contentsId = "P164";
			break;
		case 10052:	// Offload 효율저항 SQL
			contentsId = "P165";
			break;
		case 10053:	// TOP Elapsed Time SQL
		case 10308:	
			contentsId = "P166";
			break;
		case 10054:	// TOP CPU Time SQL
		case 10309:
			contentsId = "P167";
			break;
		case 10055:	// TOP Buffer Gets SQL
		case 10310:
			contentsId = "P168";
			break;
		case 10056:	// TOP Physical Reads SQL
		case 10311:
			contentsId = "P169";
			break;
		case 10057:	// TOP Executions SQL
		case 10312:
			contentsId = "P170";
			break;
		case 10058:	// TOP Cluster Wait SQL
		case 10239:
			contentsId = "P171";
			break;
		case 10059:	// 장애 예측 분석 - CPU 한계점 예측
			contentsId = "P173";
			break;
		case 10060:	// Sequence 한계점 예측
			contentsId = "P174";
			break;
		case 10061:	// Tablespace 한계점 예측
			contentsId = "P175";
			break;
		case 10062:	// 신규 SQL 타임아웃 예측
			contentsId = "P176";
			break;
		case 10273:
			contentsId = "P289";
			break;
		case 10274:
			contentsId = "C144";
			break;
		case 10275:
			contentsId = "C145";
			break;
		case 10276:
			contentsId = "C146";
			break;
		case 10277:
			contentsId = "C147";
			break;
		case 10279:
			contentsId = "C148";
			break;
		case 10278:
			contentsId = "C149";
			break;
		case 10280:
			contentsId = "C150";
			break;
		case 10281:
			contentsId = "C151";
			break;
		case 10282:
			contentsId = "C152";
			break;
		case 10283:
			contentsId = "C153";
			break;
		case 10284:
			contentsId = "C154";
			break;
		case 10285:
			contentsId = "C155";
			break;
		case 10286:
			contentsId = "C156";
			break;
		case 10287:
			contentsId = "C157";
			break;
		case 10288:
			contentsId = "C158";
			break;
		case 10289:
			contentsId = "C159";
			break;
		case 10290:
			contentsId = "C160";
			break;
		case 10291:
			contentsId = "C161";
			break;
		case 10292:
			contentsId = "C162";
			break;
		case 10293:
			contentsId = "C163";
			break;
		case 10294:
			contentsId = "C164";
			break;
		}
		
		
		return contentsId;
	}
	
	private String section2 ="";
	
	private String wrapper_css;
	private String webReportPath;
	private String exadata_yn;
	private int slt_program_sql_number;
	private String excellent = "◎";	// 9678
	private String good = "○";		// 9675
	private String action = "△";	// 9651
	private String emergency = "▽";	// 9661
	private String confirm = "□";	// 9633
	private String iconExcellent = "<strong><font color=#0073c6>◎</font></strong>";
	private String iconGood = "<strong><font color=#0073c6>○</font></strong>";
	private String iconAction = "<strong><font color=\"orange\">△</font></strong>";
	private String iconEmergency = "<strong><font color=\"red\">▽</font></strong>";
	private String iconConfirm = "<strong><font color=#0073c6>□</font></strong>";
	private LinkedHashMap<String, Object> resultSummary = new LinkedHashMap<String, Object>();
	private int base_period_value;
	private boolean isCollected = true;
	private LinkedHashMap<String, Object> rtcMap = new LinkedHashMap<String, Object>();
	private HashSet<String> sqlIdSet = new HashSet<String>();
	
	
	public HashSet<String> getSqlIdSet() {
		return sqlIdSet;
	}

	public void setSqlIdSet(HashSet<String> sqlIdSet) {
		this.sqlIdSet = sqlIdSet;
	}

	public String getSection2() {
		return section2;
	}
	public void setSection2(String section2) {
		this.section2 = section2;
	}
	public String getWrapper_css() {
		wrapper_css = "<style type=\"text/css\">\n" + 
				"    body {\n" + 
				"        text-align: center;\n" + 
				"        color: #FFF;\n" + 
//				"        width: 900px;\n" + 
				"        width: 100%;\n" + 
				"    }\n" + 
				"    div#wapper {\n" + 
				"        width: 100%;\n" + 
				"        text-align: left;\n" + 
				"        min-height: 600px;\n" + 
				"        margin: 0 auto;\n" + 
				"    }\n" + 
				"    header, footer, nav, aside, section {\n" + 
//				"        border: 1px solid #999;\n" + 
//				"        margin: 5px;\n" + 
//				"        padding: 10px;\n" + 
				"    }\n" + 
				"    header {\n" + 
				"        height: 50px;\n" + 
				"        background-color: red;\n" + 
				"    }\n" + 
				"    nav, section, aside {\n" + 
				"        float: left;\n" + 
//				"        height: 606px;\n" + 
				"    }\n" + 
				"    nav {\n" + 
//				"        background-color: goldenrod;\n" + 
				"        margin: 0px;\n" + 
				"        width: 418px;\n" + 
				"        border: 1px solid #cbcbcb;\n" +
				"        position: fixed;\n" +
				"        padding: 10px;\n" +
				"    }\n" + 
				"    section {\n" + 
//				"        background-color: green;   \n" + 
				"        width: 1000px;\n" + 
//				"        border: 1px solid #cbcbcb;\n" +
//				"        position: relative;\n" +		// link 관련된 곳에서 화면이 올라가는 문제가 있슴
				"        position: fixed;\n" +
				"        left: 450px;" +
//				"        padding: 10px;\n" +
//				"        top: 10px;\n" +
				"    }\n" + 
				"    aside {\n" + 
				"        background-color: goldenrod;\n" + 
				"        width: 100px;\n" + 
				"    }\n" + 
				"    footer {\n" + 
				"        height: 50px;\n" + 
				"        background-color: blue;\n" + 
				"        position: relatiev;\n" + 
				"        clear: both;\n" + 
				"    }\n" + 
				"    article {\n" + 
				"        width: 90%;\n" + 
				"        margin: 20px;\n" + 
				"        background-color: #999;\n" + 
				"    }\n" + 
				"</style>\n";
		
		return wrapper_css;
	}

	public void setWrapper_css(String wrapper_css) {
		this.wrapper_css = wrapper_css;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();

		// object -> Map
		ObjectMapper oMapper = new ObjectMapper();
		Map<String, Object> map = oMapper.convertValue(this, Map.class);
		Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
		String strJson = gson.toJson(map);
		try {
			objJson = (JSONObject) new JSONParser().parse(strJson);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return objJson;
	}

	public String getWebReportPath() {
		return webReportPath;
	}

	public void setWebReportPath(String webReportPath) {
		this.webReportPath = webReportPath;
	}

	public String getExadata_yn() {
		return exadata_yn;
	}

	public void setExadata_yn(String exadata_yn) {
		this.exadata_yn = exadata_yn;
	}

	public int getSlt_program_sql_number() {
		return slt_program_sql_number;
	}

	public void setSlt_program_sql_number(int slt_program_sql_number) {
		this.slt_program_sql_number = slt_program_sql_number;
	}

	public String getExcellent() {
		return excellent;
	}

	public void setExcellent(String excellent) {
		this.excellent = excellent;
	}

	public String getGood() {
		return good;
	}

	public void setGood(String good) {
		this.good = good;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getEmergency() {
		return emergency;
	}

	public void setEmergency(String emergency) {
		this.emergency = emergency;
	}

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	public String getIconExcellent() {
		return iconExcellent;
	}

	public void setIconExcellent(String iconExcellent) {
		this.iconExcellent = iconExcellent;
	}

	public String getIconGood() {
		return iconGood;
	}

	public void setIconGood(String iconGood) {
		this.iconGood = iconGood;
	}

	public String getIconAction() {
		return iconAction;
	}

	public void setIconAction(String iconAction) {
		this.iconAction = iconAction;
	}

	public String getIconEmergency() {
		return iconEmergency;
	}

	public void setIconEmergency(String iconEmergency) {
		this.iconEmergency = iconEmergency;
	}

	public String getIconConfirm() {
		return iconConfirm;
	}

	public void setIconConfirm(String iconConfirm) {
		this.iconConfirm = iconConfirm;
	}

	public LinkedHashMap<String, Object> getResultSummary() {
		return resultSummary;
	}

	public void setResultSummary(LinkedHashMap<String, Object> resultSummary) {
		this.resultSummary = resultSummary;
	}

	public int getBase_period_value() {
		return base_period_value;
	}

	public void setBase_period_value(int base_period_value) {
		this.base_period_value = base_period_value;
	}

	public boolean isCollected() {
		return isCollected;
	}

	public void setCollected(boolean isCollected) {
		this.isCollected = isCollected;
	}

	public LinkedHashMap<String, Object> getRtcMap() {
		return rtcMap;
	}

	public void setRtcMap(LinkedHashMap<String, Object> rtcMap) {
		this.rtcMap = rtcMap;
	}

}
