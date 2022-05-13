<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div id="colorPopup" class="easyui-window popWin" style="background-color:#ffffff;width:500px;height:475px;">
	<div class="easyui-layout" data-options="fit:true">
		<!-- <div region="center" style="width:450px;height:300px;"> -->
		<div region="center" style="width:100%;height:100%;">
			<table id="tableTest" border="1">
				<tbody>
					<!-- Test Code -->
					<!-- <tr>
						<td id='td_1_1' style='background-color:#222222;width:50px;' onclick="setColor('#222222')">&nbsp;</td>
						<td id='td_1_2' style='background-color:#555555;width:50px;'>&nbsp;</td>
						<td id='td_1_3' style='background-color:#aaaaaa;width:50px;'>&nbsp;</td>
					</tr>
					<tr>
						<td id='td_2_1' style='background-color:#aaa222;width:50px;'>&nbsp;</td>
						<td id='td_2_2' style='background-color:#cccc55;width:50px;'>&nbsp;</td>
						<td id='td_2_3' style='background-color:#fffaaa;width:50px;'>&nbsp;</td>
					</tr> -->
				</tbody>
			</table>
		</div>
	</div>
</div>