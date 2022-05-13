<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2017.12.05	이원식	최초작성
 * 2018.01.17	이원식	기준정보관리 메뉴 분리
 * 2018.03.07	이원식	OPENPOP V2 최초작업 
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>시스템 기준정보</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/basicInformation/systemInfo.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="title" style="margin-top:15px;">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
		</div>
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="pref_grp" name="pref_grp" value="${spopPreferences.pref_grp}"/>
			<input type="hidden" id="pref_mgmt_type_cd" name="pref_mgmt_type_cd" value="1"/>
			
			<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
		</form:form>
		<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
			<input type="hidden" id="prefIdArry" name="prefIdArry"/>
			<input type="hidden" id="prefValueArry" name="prefValueArry"/>
			<input type="hidden" id="pref_mgmt_type_cd" name="pref_mgmt_type_cd" value="1"/>
			<div id="basicInfoTab" class="easyui-tabs" style="width:100%;height:720px;margin-top:5px;">
				<c:forEach items="${menuGroupList}" var="result" varStatus="status">
					<div title="${result.pref_div_nm}" style="display:none;">
						<div class="tbl_title marginL20 marginT10 paddingB10" style="width:90%"><span class="h3">◎ ${result.pref_div_nm}</span></div>
						<div class="marginL20">
							<c:choose>
								<c:when test="${basicInfoList.size() > 1}">
									<table class="basicInfoT">
										<colgroup>	
											<col style="width:27%;">
											<col style="width:73%;">
										</colgroup>
										<c:forEach items="${basicInfoList}" var="basic" varStatus="b_status">
											<c:if test="${result.pref_grp eq basic.pref_grp}">
												<tr>
													<th>
														▶ ${basic.pref_detail_div_nm}
														<c:if test="${basic.pref_data_type_cd eq '1'}">
															<div class="marginT10 marginB3">[ 입력범위 :  <fmt:formatNumber value="${basic.min_pref_value}" type="number" maxFractionDigits="5"/> ~ <fmt:formatNumber value="${basic.max_pref_value}" type="number" maxFractionDigits="5"/> ]</div>
														</c:if>
													</th>
													<td>
														<input type="hidden" class="${basic.pref_grp}_pref_sub_id" name="${basic.pref_grp}_pref_sub_id" value="${basic.pref_sub_id}"/>
														<input type="hidden" id="${basic.pref_id}_pref_data_type_cd" name="${basic.pref_id}_pref_data_type_cd" value="${basic.pref_data_type_cd}"/>
														<c:choose>
															<c:when test="${basic.pref_data_type_cd eq '1'}">
																<input type="hidden" id="${basic.pref_id}_pre_pref_value" name="${basic.pref_id}_pre_pref_value" value="<fmt:formatNumber value='${basic.pre_pref_value}' type='number' groupingUsed='false' maxFractionDigits='5'/>"/>
																<input type="hidden" id="${basic.pref_id}_min_pref_value" name="${basic.pref_id}_min_pref_value" value="<fmt:formatNumber value='${basic.min_pref_value}' type='number' groupingUsed='false' maxFractionDigits='5'/>"/>
																<input type="hidden" id="${basic.pref_id}_max_pref_value" name="${basic.pref_id}_max_pref_value" value="<fmt:formatNumber value='${basic.max_pref_value}' type='number' groupingUsed='false' maxFractionDigits='5'/>"/>																	
															</c:when>
															<c:otherwise>
																<input type="hidden" id="${basic.pref_id}_pre_pref_value" name="${basic.pref_id}_pre_pref_value" value="${basic.pre_pref_value}"/>
																<input type="hidden" id="${basic.pref_id}_min_pref_value" name="${basic.pref_id}_min_pref_value" value="${basic.min_pref_value}"/>
																<input type="hidden" id="${basic.pref_id}_max_pref_value" name="${basic.pref_id}_max_pref_value" value="${basic.max_pref_value}"/>																	
															</c:otherwise>
														</c:choose>
														<c:choose>
															<c:when test="${basic.pref_id eq '16003' or basic.pref_id eq '16006'}">
																<select id="${basic.pref_id}" name="${basic.pref_id}" data-options="panelHeight:'auto'" class="w200 easyui-combobox">
																	<option value="AND" <c:if test="${basic.pref_value eq 'AND'}">selected</c:if>>AND</option>
																	<option value="OR" <c:if test="${basic.pref_value eq 'OR'}">selected</c:if>>OR</option>
																</select>
															</c:when>
															<c:otherwise>
																<input type="text" id="${basic.pref_id}" name="${result.pref_id}" value="${basic.pref_value}" class="w200 easyui-textbox"/>
															</c:otherwise>
														</c:choose>
														<c:if test="${basic.unit ne null}">(${basic.unit})</c:if>
														<div class="marginT10 marginB3"><b>※ Note</b> :  ${basic.pref_desc}</div>
													</td>
												</tr>
											</c:if>
										</c:forEach>
									</table>
									<div class="dtlBtn inlineBtn marginR30">
										<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveBasic('${result.pref_grp}');"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 적용</a>
									</div>
								</c:when>
								<c:otherwise>
									<table class="basicInfoT">
										<tr>
											<td class="ctext">"조회"를 해주세요.</td>
										</tr>
									</table>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</c:forEach>
			</div>
		</form:form>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>