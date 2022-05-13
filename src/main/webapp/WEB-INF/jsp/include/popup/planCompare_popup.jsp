<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<style>
	/* 플랜비교 팝업 */
	#planComparePop table {
		margin: 13px;
		min-width: 745px;
	}
	#planComparePop table tr th,
	#planComparePop table tr td {
		border: 1px solid #E6E6E6;
		padding: 4px 5px;
	}
	#planComparePop table tbody tr {
		/* height: 20px; */
	}
	#planComparePop table tbody tr th {
		width: 30px;
		text-align: right;
	}
	#planComparePop table tbody tr td {
		min-width: 715px;
		white-space: nowrap;
	}
	#planComparePop table tbody .CHANGE1 td,
	#planComparePop table tbody .CHANGE2 td,
	#planComparePop table tbody .INSERT2 td,
	#planComparePop table tbody .DELETE1 td {
		background-color: #FAEBD9;
	}
	#planComparePop table tbody .INSERT1 td,
	#planComparePop table tbody .DELETE2 td,
	#planComparePop table tbody .EMPTY td {
		background-color: #F2F2F2;
	}
</style>
<div id="planComparePop" class="easyui-window popWin" style="background-color:#ffffff;width:1600px;height:550px;">
	<div class="easyui-layout" data-options="fit:true" style="height:100%;">
		<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%">
			<div data-options="region:'west',border:false" style="width:50%;height:100%;padding-right:2.5px;">
				<div class="easyui-tabs" data-options="fit:true,plain:true,border:false">
					<div class="tabTxt scrollLeft" title="ASIS PLAN" style="width:100%;height:400px;">
						<table>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div data-options="region:'east',border:false" style="width:50%;height:100%;padding-left:2.5px;">
				<div class="easyui-tabs" data-options="fit:true,plain:true,border:false">
					<div class="tabTxt scrollRight" title="TOBE PLAN" style="width:100%;height:400px;">
						<table>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div data-options="region:'south'" style="height:46px;" >
				<div class="inlineBtn" style="padding-top:8px;padding-right:5px;float: right;">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_ClosePlanComparePop();"><i class="btnRBIcon fas fa-times fa-lg fa-fw"></i> 닫기</a>
				</div>
			</div>
		</div>
	</div>
</div>