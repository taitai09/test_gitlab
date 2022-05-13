<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div id="messageListPop" class="easyui-window popWin" style="background-color:#ffffff;width:900px;height:550px;">
	<div class="easyui-layout" data-options="fit:true">
		<div class="easyui-layout" data-options="region:'north',split:false,border:false" style="padding:5px;height:65px;">
			<div class="well" style="padding:10px;">
				<form:form method="post" id="msgList_form" name="msgList_form" class="form-inline">
					<input type="hidden" id="send_user_id" name="send_user_id"/>
					<input type="hidden" id="recv_user_id" name="recv_user_id"/>
					<input type="hidden" id="send_dt" name="send_dt"/>
					<input type="hidden" id="read_yn" name="read_yn"/>
					<input type="hidden" id="send_yn" name="send_yn"/>
					<label>검색 조건</label>
					<select id="searchKey" name="searchKey" data-options="panelHeight:'auto'" class="w80 easyui-combobox">
						<option value="">선택</option>
						<option value="01">보낸사람ID</option>
						<option value="02">보낸사람명</option>
						<option value="03">제목</option>
						<option value="04">내용</option>
					</select>
					<input type="text" id="searchValue" name="searchValue" class="w200 easyui-textbox"/>
					<label>쪽지 유형</label>
					<select id="note_type_cd" name="note_type_cd" data-options="panelHeight:'auto'" class="w110 easyui-combobox"></select>
					<label>읽음 여부</label>
					<select id="selReadYn" name="selReadYn" data-options="panelHeight:'auto'" class="w110 easyui-combobox">
						<option value="">전체</option>
						<option value="N">읽지않음</option>
						<option value="Y">읽음</option>
					</select>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SearchMessageList();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조회</a>
					</span>
				</form:form>
			</div>
		</div>	
		<div data-options="plain:true,region:'center',split:false,border:false" style="padding:5px;">
			<div id="messagePopTab" class="easyui-tabs" data-options="fit:true,border:false">
				<div title="받은 쪽지함" style="padding:5px;">
					<table id="recvMsgList" class="tbl easyui-datagrid" style="height:100%" data-options="fit:true,border:false">
					</table>
				</div>
				<div title="보낸 쪽지함" style="padding:5px;">
					<table id="sendMsgList" class="tbl easyui-datagrid" style="height:100%" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
		</div>
		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
			<div class="searchBtn" style="padding-right:35px;">
				<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_ShowSendMessage();"><i class="btnIcon far fa-paper-plane fa-lg fa-fw"></i> 쪽지 보내기</a>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_MessageListClose();"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>
<div id="sendMessagePop" class="easyui-window popWin" style="background-color:#ffffff;width:550px;height:430px;">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="plain:true,region:'center',split:false,border:false" style="margin-top:10px;padding:5px;">
			<form:form method="post" id="message_form" name="message_form" class="form-inline">
				<input type="hidden" id="note_type_cd" name="note_type_cd" value="1"/>
				<input type="hidden" id="sendUserName" name="sendUserName" value="${users.user_nm}"/>
				<input type="hidden" id="recvUserArry" name="recvUserArry"/>
				<table class="detailT" style="width:96%;margin-top:5px;">
					<colgroup>	
						<col style="width:20%;">
						<col style="width:80%;">
					</colgroup>
					<tr>	
						<th>받는 사람</th>
						<td>
							<input type="text" id="recv_user_nm" name="recv_user_nm" data-options="disabled:'true'" class="easyui-textbox" style="width:90%"/>
							&nbsp;<a href="javascript:;" id="findUserBtn" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onClick="Btn_ShowRecvMessageUser();"></a>
						</td>
					</tr>
					<tr>	
						<th>제 목</th>
						<td><input type="text" id="note_title" name="note_title" class="easyui-textbox" style="width:99%"/></td>
					</tr>
					<tr>	
						<td colspan="2">
							<textarea name="note_contents" id="note_contents" rows="15" onkeyup="ContentSize();" onmouseup="ContentSize();" style="margin-top:5px;width:99%;height:200px"></textarea>
						</td>
					</tr>
				</table>
				<div id="contSizeDiv" style="float:right;margin-top:10px;margin-right:20px;">0 / 4,000 Bytes</div>		
			</form:form>
		</div>
		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
			<div class="searchBtn" style="padding-right:35px;">
				<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_SendMessage('pop');"><i class="btnIcon far fa-paper-plane fa-lg fa-fw"></i> 보내기</a>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SendMessageClose();"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 취소</a>
			</div>
		</div>
	</div>
</div>
<div id="recvMessagePop" class="easyui-window popWin" style="background-color:#ffffff;width:550px;height:430px;">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="plain:true,region:'center',split:false,border:false" style="margin-top:10px;padding:5px;">
			<form:form method="post" id="recvMessage_form" name="recvMessage_form" class="form-inline">
				<input type="hidden" id="note_type_cd" name="note_type_cd" value="1"/>
				<input type="hidden" id="send_user_id" name="send_user_id"/>
				<input type="hidden" id="send_yn" name="send_yn"/>
				<table class="detailT" style="width:96%;margin-top:5px;">
					<colgroup>	
						<col style="width:20%;">
						<col style="width:30%;">
						<col style="width:20%;">
						<col style="width:30%;">
					</colgroup>
					<tr>	
						<th>보낸 시간</th>
						<td><input type="text" id="send_dt" name="send_dt" data-options="readonly:'true'" class="easyui-textbox" style="width:99%"/></td>
						<th>보낸 사람</th>
						<td><input type="text" id="send_user_nm" name="send_user_nm" data-options="readonly:'true'" class="easyui-textbox" style="width:99%"/></td>
					</tr>
					<tr>	
						<th>제 목</th>
						<td colspan="3"><input type="text" id="note_title" name="note_title"  data-options="readonly:'true'" class="easyui-textbox" style="width:99%"/></td>
					</tr>
					<tr>	
						<td colspan="4">
							<textarea name="note_contents" id="note_contents" rows="15" style="margin-top:5px;width:99%;height:200px" readonly></textarea>
						</td>
					</tr>
				</table>
				<div id="recvSizeDiv" style="float:right;margin-top:10px;margin-right:20px;">0 / 4,000 Bytes</div>		
			</form:form>
		</div>
		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
			<div class="searchBtn" style="padding-right:35px;">
				<a href="javascript:;" id="replyBtn" class="w110 easyui-linkbutton" onClick="Btn_ReplyMessage();"><i class="btnIcon far fa-paper-plane fa-lg fa-fw"></i> 답장 보내기</a>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_DeleteMessage();"><i class="btnIcon fas fa-trash-alt fa-lg fa-fw"></i> 삭제</a>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_RecvMessageClose();"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>
<div id="recvMessageUserPop" class="easyui-window popWin" style="background-color:#ffffff;width:750px;height:500px;">
	<div class="easyui-layout" data-options="fit:true">
		<div class="easyui-layout" data-options="region:'north',split:false,border:false" style="padding:5px;height:65px;">
			<div class="well" style="padding:10px;">
				<form:form method="post" id="selUser_form" name="selUser_form" class="form-inline">
					<label>검색 조건</label>
					<select id="popSearchKey" name="popSearchKey" data-options="panelHeight:'auto'" class="w70 easyui-combobox">
						<option value="">선택</option>
						<option value="01">사용자ID</option>
						<option value="02">사용자명</option>
					</select>
					<input type="text" id="popSearchValue" name="popSearchValue" class="w100 easyui-textbox"/>
					<label>권한</label>
					<select id="popAuth_cd" name="popAuth_cd" data-options="panelHeight:'auto'" class="w90 easyui-combobox"></select>
					<label>업무</label>
					<select id="popWrkjob_cd" name="popWrkjob_cd" data-options="panelHeight:'auto'" class="w170 easyui-combotree"></select>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SearchRecvUser();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조회</a>
					</span>
				</form:form>
			</div>
		</div>
		<div data-options="plain:true,region:'center',split:false,border:false" style="padding:5px;">
			<table id="recvUserList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
			<div class="searchBtn" style="padding-right:35px;">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SelectRecvUser();"><i class="btnIcon fas fa-check fa-lg fa-fw"></i> 확인</a>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_RecvMessageUserClose();"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 취소</a>
			</div>
		</div>
	</div>
</div>
