<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2017.09.28	이원식	최초작성
 * 2018.03.08	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>사례/가이드</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/precedent/precedentList.js?ver=<%=today%>"></script>
</head>
<body>
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">

		<div class="title">
			<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i>${menu_nm}</span>
		</div>
		
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-left:5px;min-height:730px">
			<div id="sqlTuningTab" class="easyui-tabs" data-options="fit:true,border:false">
			
				<div title="SQL 튜닝 가이드" data-options="fit:true" style="padding:5px;min-height:680px;">
				
					<form:form method="post" id="submit_form1" name="submit_form1" class="form-inline">
						<div class="easyui-panel" data-options="border:false" style="width:100%; height: 62px; padding-top: 5px;">
							<div class="well">
									<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
									<input type="hidden" id="searchBtnClickCount1" name="searchBtnClickCount1" value="${searchBtnClickCount1}"/>
									<input type="hidden" id="searchBtnClickCount2" name="searchBtnClickCount2" value="${searchBtnClickCount2}"/>
									<input type="hidden" id="searchKey" name="searchKey" value="${perfGuide.searchKey}"/>
									<input type="hidden" id="guide_div_cd" name="guide_div_cd" value="${perfGuide.guide_div_cd}"/>
									<input type="hidden" id="guide_no" name="guide_no" value="${perfGuide.guide_no}"/>
									<input type="hidden" id="tuning_no" name="tuning_no" value="${perfGuide.tuning_no}"/>
									<input type="hidden" id="reg_user_id" name="reg_user_id"/>
									<!-- 이전, 다음 처리 -->
									<input type="hidden" id="currentPage" name="currentPage" value="${perfGuide.currentPage}"/>
				<%-- 					<input type="hidden" id="pagePerCount" name="pagePerCount" value="${perfGuide.pagePerCount}"/> --%>
									<input type="hidden" id="pagePerCount" name="pagePerCount" value="20"/>
									
									<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
				
									<label>등록일자</label>
									<input type="text" id="strStartDt" name="strStartDt" value="${perfGuide.strStartDt}" class="w130 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false" /> ~
									<input type="text" id="strEndDt" name="strEndDt" value="${perfGuide.strEndDt}" class="w130 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false,validType:'greaterThan[\'#strStartDt\']'" />
									<label>검색조건</label>
									<select id="selectValue" name="selectValue" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox">
										<option value="">선택</option>
										<option value="01">제목</option>
										<option value="02">내용</option>
									</select>
									<input type="text" id="searchValue" name="searchValue" value="${perfGuide.searchValue}" class="w200 easyui-textbox"/>
									<span class="searchBtnLeft">
										<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick('1');"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
									</span>
									<div class="searchBtn">
										<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_DownClick('1');"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
									</div>
							</div>
						</div>
						
						<sec:authorize access="hasAnyRole('ROLE_DBMANAGER', 'ROLE_TUNER')">
							<div class="easyui-panel" data-options="border:false" style="width:100%; padding-top: 5px;">
								<div class="searchBtn innerBtn"><a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_InsertGuide();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 가이드 등록</a></div>
							</div>
						</sec:authorize>
					
						<div class="easyui-panel" data-options="border:false" style="width:100%; height:530px;padding-top: 5px;">
							<table id="tableList0" class="tbl easyui-datagrid" data-options="fit:true,border:false">
							</table>
						</div>
	
						<div class="searchBtn" data-options="collapsible:false,border:false" style="height:40px;padding-top:10px;text-align:right;">
							<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
							<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
						</div>
					</form:form>
					
				</div>
				
				<div title="SQL 튜닝 사례" data-options="fit:true" style="padding:5px;min-height:680px;">
				
					<form:form method="post" id="submit_form2" name="submit_form2" class="form-inline">
						<div class="easyui-panel" data-options="border:false" style="width:100%; height: 62px; padding-top: 5px;">
							<div class="well">
									<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
									<input type="hidden" id="searchBtnClickCount1" name="searchBtnClickCount1" value="${searchBtnClickCount1}"/>
									<input type="hidden" id="searchBtnClickCount2" name="searchBtnClickCount2" value="${searchBtnClickCount2}"/>
									<input type="hidden" id="searchKey" name="searchKey" value="${perfGuide.searchKey}"/>
									<input type="hidden" id="guide_div_cd" name="guide_div_cd" value="${perfGuide.guide_div_cd}"/>
									<input type="hidden" id="guide_no" name="guide_no" value="${perfGuide.guide_no}"/>
									<input type="hidden" id="tuning_no" name="tuning_no" value="${perfGuide.tuning_no}"/>
									<input type="hidden" id="reg_user_id" name="reg_user_id"/>
									<!-- 이전, 다음 처리 -->
									<input type="hidden" id="currentPage" name="currentPage" value="${perfGuide.currentPage}"/>
				<%-- 					<input type="hidden" id="pagePerCount" name="pagePerCount" value="${perfGuide.pagePerCount}"/> --%>
									<input type="hidden" id="pagePerCount" name="pagePerCount" value="20"/>
									
									<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
				
									<label>등록일자</label>
									<input type="text" id="strStartDt" name="strStartDt" value="${perfGuide.strStartDt}" class="w130 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false" /> ~
									<input type="text" id="strEndDt" name="strEndDt" value="${perfGuide.strEndDt}" class="w130 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false,validType:'greaterThan[\'#strStartDt\']'" />
									<label>검색조건</label>
									<select id="selectValue" name="selectValue" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox">
										<option value="">선택</option>
										<option value="01">제목</option>
										<option value="02">내용</option>
									</select>
									<input type="text" id="searchValue" name="searchValue" value="${perfGuide.searchValue}" class="w200 easyui-textbox"/>
									<span class="searchBtnLeft">
										<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick('2');"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
									</span>
									<div class="searchBtn">
										<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_DownClick('2');"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
									</div>
							</div>
						</div>
						
						<div class="easyui-panel" data-options="border:false" style="width:100%; height:571px;padding-top: 5px;">
							<table id="tableList1" class="tbl easyui-datagrid" data-options="fit:true,border:false">
							</table>
						</div>
																
						<div class="searchBtn" data-options="collapsible:false,border:false" style="height:40px;padding-top:10px;text-align:right;">
							<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
							<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
						</div>
					</form:form>
					
				</div>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>