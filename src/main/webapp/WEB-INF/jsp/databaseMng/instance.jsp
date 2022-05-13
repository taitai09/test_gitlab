<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2017.12.08	이원식	최초작성
 * 2018.03.07	이원식	OPENPOP V2 최초작업
 * 2021.09.08	이재우	COLLECT 인스턴스 순번 로직변경
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>설정 :: OPEN-POP 설정 :: 인스턴스 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/lib/jquery.color.js?ver=<%=today%>"></script>
    <script type="text/javascript" src="/resources/js/ui/include/popup/color_popup.js?ver<%=today%>"></script>
    <script type="text/javascript" src="/resources/js/ui/databaseMng/instance.js?ver=<%=today%>"></script>    
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>					
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>

					<!-- 이전, 다음 처리 -->
					<input type="hidden" id="currentPage" name="currentPage" value="${instance.currentPage}"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="${instance.pagePerCount}"/>
					
					<label>검색 조건</label>
					<select id="searchKey" name="searchKey" data-options="panelHeight:'auto',editable:false" class="w120 easyui-combobox">
						<option value="">전체</option>
						<option value="01">인스턴스명</option>
					</select>
					<input type="text" id="searchValue" name="searchValue" class="w200 easyui-textbox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조회</a>
					</span>	
					<div class="searchBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</div>
				</form:form>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:350px;margin-bottom:10px;">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="searchBtn" data-options="region:'south',split:false,border:false" style="width:100%;height:6%;padding:10px 0px;text-align:right;">
			<a href="javascript:;" id="prevBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
			<a href="javascript:;" id="prevBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
			<a href="javascript:;" id="nextBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			<a href="javascript:;" id="nextBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:320px">
			<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
				<input type="hidden" id="rgb_color_id" name="rgb_color_id"/>
				<input type="hidden" id="dbid" name="dbid"/>
				<input type="hidden" id="inst_id" name="inst_id"/>
				<input type="hidden" id="crud_flag" name="crud_flag"  value="C"/>
				<input type="hidden" id="old_inst_name" name="old_inst_name"  value=""/>
				<input type="hidden" id="db_name" name="db_name"  value=""/>
				
				<table class="detailT">
					<colgroup>
						<col style="width:15%;">
						<col style="width:18%;">
						<col style="width:15%;">
						<col style="width:18%;">
						<col style="width:15%;">
						<col style="width:19%;">
						<col style="width:15%;">
						<col style="width:19%;">
					</colgroup>
					<tr>
						<th>DB명</th>
						<td class="ltext"><select id="selectDbid" name="selectDbid" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" required="required"></select></td>
						<th>인스턴스 번호</th>
						<td class="ltext"><input type="number" id="ui_inst_id" name="ui_inst_id" class="w200 easyui-textbox"required/></td>
						<th>인스턴스명</th>
						<td class="ltext"><input type="text" id="inst_name" name="inst_name" class="w200 easyui-textbox"required/></td>
						<th>화면표시명</th>
						<td class="ltext"><input type="text" id="display_nm" name="display_nm" class="w200 easyui-textbox"required/></td>
					</tr>
					<tr>
						<th>호스트명</th>
						<td class="ltext"><input type="text" id="host_nm" name="host_nm" class="w200 easyui-textbox" required/></td>
						<th>인스턴스 설명</th>
						<td class="ltext"><input type="text" id="instance_desc" name="instance_desc" class="w200 easyui-textbox"/></td>
						<th>컬러</th>
						<td >
							<a href="javascript:;" class="w70 easyui-linkbutton" onClick="update();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 색상</a>
							<input type="text" id="colorDiv" name="colorDiv" data-options="disabled:false" class="w120 easyui-textbox" style="text-align:center" required/>
						</td>
						<th>COLLECT IP</th>  <!-- 이전 : AGENT IP -->
						<td class="ltext"><input type="text" id="agent_ip" name="agent_ip" class="w200 easyui-textbox" required/></td>
					</tr>
					<tr>
						<th>COLLECT PORT</th>
						<td class="ltext"><input type="number" id="agent_port" name="agent_port" class="w200 easyui-textbox" required/></td>
						<!-- <th>GATHER PORT</th>
						<td class="ltext"><input type="number" id="gather_agent_port" name="gather_agent_port" class="w200 easyui-textbox"/></td> -->
						<th>COLLECT 이중화 IP 그룹</th>  <!-- 이전 : AGENT 이중화 IP 그룹 -->
						<td class="ltext"><input type="text" id="dplx_agent_ips" name="dplx_agent_ips" class="w200 easyui-textbox"/></td>
						<th>COLLECT 이중화 PORT 그룹</th> <!-- 이전 : COLLECT PORT 그룹 -->
						<td class="ltext"><input type="text" id="dplx_gather_agent_ports" name="dplx_gather_agent_ports" class="w200 easyui-textbox"/></td>
						<!-- <th> DB컬럼명</th> 추가 COLLECT ID -->
						<th>COLLECT AGENT ID</th> <!-- 추가 COLLECT ID-->
						<td class="ltext"><input type="text" id="collect_agent_id" name="collect_agent_id" class="w200 easyui-textbox" required/></td>
					</tr>
					<tr>
						<th>COLLECT PATH</th> <!-- 추가 -->
						<td class="ltext"><input type="text" id="collect_agent_path" name="collect_agent_path" class="w200 easyui-textbox"/></td>
						<th>COLLECT 인스턴스 순번</th> <!-- 추가 -->
						<td class="ltext"><input type="number" id="collect_instance_seq" name="collect_instance_seq" class="w200 easyui-textbox"/></td>
						<th>GATHER PORT</th>
						<td class="ltext"><input type="number" id="gather_agent_port" name="gather_agent_port" class="w200 easyui-textbox" required/></td>
						<th>GATHER 이중화 PORT 그룹</th>  <!-- 이전 : GATHER PORT 그룹 -->
						<td class="ltext" colspan="1" style="border:1px solid #ddd;"><input type="text" id="dplx_agent_ports" name="dplx_agent_ports" class="w200 easyui-textbox"></td>
					</tr>
				</table>
				<div class="searchBtn innerBtn2">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveSetting();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_DeleteInstance();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
				</div>
			</form:form>
		</div>
	</div>
	<!-- contents END -->
</div>
<%@include file="/WEB-INF/jsp/include/popup/color_popup.jsp" %>
<!-- container END -->
</body>
</html>