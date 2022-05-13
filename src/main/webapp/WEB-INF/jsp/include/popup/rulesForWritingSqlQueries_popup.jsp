<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div id="rulesForWritingSqlQueriesPopup" class="easyui-window popWin" style="background-color:#ffffff;width:530px;height:475px;">
	<div class="easyui-layout" data-options="fit:true">
		<div region="center" style="width:100%;height:100%;">
<!-- 			<table id="tableTest" border="1"> -->
<!-- 				<tbody> -->
					<pre>
 1. 화면에 출력할 항목인 HEAD는 반드시 첫 번째 컬럼에 위치하고 ALIAS명은 HEAD로 한다.
   예) SELECT ‘N;0;고객번호/C/50;고객명/L/100;고객 성별/C/50;고객 나이/R/50’ <span style="color:red">AS HEAD</span>

 2. HEAD의 구분자는 세미콜론(<span style="color:red">;</span>)이며 HEAD의 최종 항목 뒤에는 세미콜론(<span style="color:red">;</span>)을 붙이지 않는다.

 3. HEAD의 세미콜론(;) 뒤 첫 번째 문자는 공백을 허용하지 않는다.
    예) ‘N;0;  고객번호/C/50’ (X) &rarr; ‘N;0;고객번호/C/50’ (O)

 4. HEAD 항목 규칙
    - 1라인: PIVOT여부(Y: PIVOT, N: 타이틀 고정)
    - 2라인: 고정 항목수(고정 항목수 다음부터 PIVOT이 적용된다. PIVOT 테이블이 아닌 경우 0)
    - 3라인: PIVOT여부 = 'Y'일 경우 &rarr; 고정 항목수 만큼 <span style="color:red">항목명/(L:좌측,C:중앙,R:우측)/항목길이</span>를 반복하고,
                                   <span style="color:red">PIVOT_NM/(L:좌측,C:중앙,R:우측)/항목길이</span>로 종료한다.
               예) ‘Y;3;라이브러리명/L/100;모델명/L/100;주제영역명/L/100;PIVOT_NM/R/100’
                 PIVOT여부 = 'N'일 경우 &rarr; 타이틀 고정 항목만큼 <span style="color:red">항목명/(L:좌측,C:중앙,R:우측)/항목길이</span>를 반복한다.
               예) ‘N;0;고객번호/C/50;고객명/L/100;고객 성별/C/50;고객 나이/R/50’

 5. PIVOT여부 = 'Y'일 경우 PIVOT되는 항목의 ROW 수는 항상 동일해야 한다.

 6. 컬럼의 ALIAS명은 HEAD명과 동일해야 한다.

 7. 컬럼 ALIAS명은 특수문자를 사용할 수 없다.

 8. 조건절을 입력 받을 경우 <span style="color:red">LIKE '&#36;'||&#35;{컬럼명}||'%'</span>으로 구현한다.
      예) AND CUST_NM LIKE '&#36;'||&#35;{CUST_NM}||'%'
					</pre>
<!-- 				</tbody> -->
<!-- 			</table> -->
		</div>
		<div region='south' style="height:40px;" >
			<div class="searchBtn" style="padding-right:1px;padding-top:6px;">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_ClosePopup();">확인</a>
			</div>
		</div>
	</div>
</div>