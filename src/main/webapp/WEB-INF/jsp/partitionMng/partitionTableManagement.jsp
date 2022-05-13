<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.09.03	임호경	최초작성
  **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>파티션 테이블 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/partitionMng/partitionTableManagement.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<div class="title paddingT5">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
		</div>
		<div class="easyui-layout" data-options="border:false" style="width:100%;height:750px;">
			<div data-options="region:'west',border:false" style="width:70%;padding:5px 5px 0px 0px;">
				<div class="well marginB10">
					<form:form method="post" id="left_form" name="left_form" class="form-inline">
						<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>

						<input type="hidden" id="dbid" name="dbid"/>
						<label>검색 조건</label>
						<select id="searchKey" name="searchKey" data-options="panelHeight:'auto',editable:false" class="w110 easyui-combobox">
							<option value="">전체</option>
							<option value="01">테이블명</option>
							<option value="02">파티션키구성값</option>
						</select>
						<input type="text" id="searchValue" name="searchValue" class="w150 easyui-textbox"/>
						<span class="searchBtnLeft">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_getPartitionList();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조회</a>
						</span>
					</form:form>								
				</div>
				<div class="easyui-panel" data-options="border:false" style="min-height:650px;margin-bottom:10px;">
					<table id="partitionList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			<div data-options="region:'center',border:false" style="padding:5px">
				<div class="well marginB10">
					<div class="dtl_title"><span id="subTitle" class="h3" style="margin-left:0px;">▶ 파티션 테이블 추가/수정</span></div>
				</div>
				
				<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
					<div class="easyui-panel" data-options="border:false" style="height:600">
<%-- 								<input type="hidden" id="user_id" name="user_id" value="${user_id}"> --%>
<!-- 								<input type="text" id="partition_work_type_cd" name="partition_work_type_cd"/> -->
<!-- 								<input type="text" id="partition_interval_type_cd" name="partition_interval_type_cd"/> -->
<!-- 								<input type="text" id="shelf_life_type_cd" name="shelf_life_type_cd" /> -->


						<input type="hidden" name="crud_flag" id="crud_flag" value="C"/>
						
						<table id="right_partitionList" class="detailT click" style="margin-left:0px;margin-top:0px;">
							<colgroup>
								<col style="width:30%;">
								<col style="width:70%;">
							</colgroup>
							
							<tr>
								<th>테이블명</th>
<!-- 		나중에 이것으로 바꾸어야 함			<td><select id="table_name" name="table_name" data-options="panelHeight:500,editable:false" class="w200 easyui-combotree" ></select></td> -->
								<td><input type="text" id="table_name" name="table_name" class="w200 easyui-textbox" required="required"/></td>
							</tr>
							<tr>
								<th>파티션 작업유형</th>
								<td><select id="partition_work_type_cd_nm" name="partition_work_type_cd" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combotree" required></select></td>
							</tr>
							<tr>
								<th>파티션 간격</th>
								<td><input type="number" id="partition_interval" name="partition_interval" class="w200 easyui-textbox" required/></td>
							</tr>
							<tr>
								<th>파티션 간격유형</th>
									<td><select id="partition_interval_type_cd_nm" name="partition_interval_type_cd" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combotree" required></select></td>
							</tr>
							<tr>
								<th>압축여부</th>
								<td>	<select id="compress_yn" name="compress_yn" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox" required >
										<option value=""></option>
										<option value="Y">Y</option>
										<option value="N">N</option>
									</select>
									
								</td>
							</tr>
							<tr>
								<th>보관기간</th>
								<td><input type="number" id="shelf_life_cnt" name="shelf_life_cnt" class="w200 easyui-textbox" required/></td>
							</tr>
							<tr>
								<th>보관기간유형</th>
								<td><select id="shelf_life_type_cd_nm" name="shelf_life_type_cd" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combotree" required></select></td>
							</tr>
							<tr>
								<th>파티션키구성값</th>
								<td><textarea id="partition_key_composite_value" name="partition_key_composite_value" style="width:300px;height:60px" ></textarea></td>
							</tr>
							<tr>
								<th>예비파티션수</th>
								<td><input type="number" id="spare_partition_cnt" name="spare_partition_cnt" class="w200 easyui-textbox" required></td>
							</tr>
						</table>
						<div class="searchBtn innerBtn2">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_savePartition();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_deletePartition();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
							<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>