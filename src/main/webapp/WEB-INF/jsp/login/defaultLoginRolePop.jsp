<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page session="false" %>
	<script type="text/javascript" src="/resources/js/ui/login/defaultLoginRole.js"></script>
<!-- position:absolute;z-index:1000;background-color:#ffffff; -->
			<div id="selectRolePop" style="position:absolute;z-index:1000;background-color:#ffffff; width: 450px;height:150px;cursor:default;background:white;top:35%;left:38%;text-align:center;vertical-align:middle;border-radius:10px;border:1px solid gray;">
				<div class="easyui-layout" data-options="fit:true">
					<div data-options="plain:true,region:'north',split:false,border:false" style="padding:10px;height:150px;">
<%-- 						<form:form method="post" id="selectRolePop_form" name="selectRolePop_form" class="form-inline"> --%>
						<form:form method="post" class="form-inline"> 
							<div class="easyui-layout" data-options="fit:true" style="height:120px;">
								<div data-options="plain:true,region:'north',split:false,border:false" style="padding:10px;height:50px;">
									<span style="padding:0px 10px;float:left;" data-options="title:'권한선택'" id="selectDefaultAuthComboSpan">
										<select id="selectDefaultAuthCombo" name="selectDefaultAuthCombo" data-options="panelHeight:'200px',editable:false" style="height:;" class="w150 easyui-combobox"></select>
									</span>
									<span style="padding:0px 10px;float:right;" data-options="title:'업무선택'" id="selectDefaultWrkjobCdSpan">
										<select id="selectDefaultWrkjobCd" name="selectDefaultWrkjobCd" data-options="panelHeight:'200px',editable:false" style="height:;" class="w150 easyui-combobox"></select>
									</span>
								</div>
								<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;padding-left:20px;font-size:12px;">
									<input type="checkbox" id="role_save" name="role_save">&nbsp;&nbsp;로그인시 기본설정으로 저장
								</div>
							</div>
						</form:form>
					</div>
					<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
						<div class="searchBtn" style="padding-right:35px;">
							<a href="javascript:;" class="w100 easyui-linkbutton" data-options="iconCls:'icon-ok'" onClick="fnSaveDefaultRole();">변경</a>
						</div>
					</div>
				</div>
			</div>