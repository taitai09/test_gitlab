<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="messagePop" class="easyui-window popWin" title="" data-options="iconCls:'icon-blank'" style="background-color:#ffffff;width:700px;height:280px;">
	<!--  open하는 js에서 height를 300으로 지정해야할 이유를 찾지 못함. -->
	<form:form method="post" id="message_form" name="messaget_form" class="form-inline">
		<div data-options="region:'center',split:false,collapsible:false,border:false" style="height: 220px;overflow-x: hidden; overflow-y: hidden;">
			<textarea name="message" id="message" rows="12" style="overlow:scroll;padding:5px;width:98%;height:208px;font-family:'굴림체';font-size: 12px;" wrap="off"></textarea>
		</div>
		<div data-options="region:'south',split:false,collapsible:false,border:false" style="height:30px;padding:5px;margin-top: 4px;">
			<div class="searchBtn">
				<a href="javascript:;" class="w60 easyui-linkbutton" onClick="Btn_OnClose();"><i class="btnIcon fa fa-window-close fa-lg fa-fw"></i> 확인</a>
			</div>
		</div>
	</form:form>
</div>