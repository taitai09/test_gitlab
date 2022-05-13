<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="sqlConsiderationsPop" class="easyui-window popWin" style="background-color:#ffffff;width:1200px;height:700px;">
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'center'" style="height:295px;padding:10px;">
			<div id="sqlConsideration">
			
				<div class="well" style="margin-bottom: 10px;">
					<h3 style="font: 20px bold;"> BYPASS_UJVC 힌트 </h3>
				</div>
				<ul>
					<li> 미지원 버전 : 11g 이상 </li>
					<li> BYPASS_UJVC 힌트 </li>
					<ul class="minus">
						<li> Updatable Join View에서 사용하는 힌트 </li>
						<li> 부모 테이블의 조인 컬럼이 PK 또는 UK로 설정되어야 자식 테이블(키보존테이블)에 업데이트가 가능한데, 이런 제약조건이 없더라도 Update가 가능하도록 하는 힌트 </li>
					</ul>
					<li> 키-보존 테이블(Key-Preserved Table) 이란?</li>
					<ul class="minus">
						<li> 조인된 결과 집합에서도 중복 값없이 unique 하게 식별가능한 테이블 </li>
						<li> 테이블의 모든 key 가 조인된 결과에서도 key(unique) 가 될 수 있으므로, 키가 보존된 테이블이라고 함</li>
					</ul>
					<li>SQL Guide</li>
					<ul class="minus">
						<li> Updatable Join View 에 BYPASS_UJVC 힌트 사용</li>
							<img style="margin-left:30px;" src="/resources/images/sqlConsideration/image001.png">
						<li> Updatable Join View 를 Merge 로 변경</li>
							<img style="margin-left:30px;" src="/resources/images/sqlConsideration/image002.png">
						<li class="non"/>
					</ul>
					
				</ul>
				
				
				<div class="well" style="margin-bottom: 10px;">
					<h3 style="font: 20px bold;"> WM_CONCAT </h3>
				</div>
				<ul>
					<li> 미지원 버전 : 11gR2 이상 </li>
					<li> ORA-00904 : "WM_COUMCAT" : 부적합한 식별자 </li>
					<li> 오라클 9i 이하 = XMLAGG 사용 <br>
						<span style="margin-left:21px;font-size: inherit;"> 오라클 10g ~ 11gR1 = WM_CONCAT 사용 </span><br>
						<span style="margin-left:21px;font-size: inherit;"> 오라클 11gR2 이상 = WM_CONCAT 함수 사용불가  &gt;&gt;  LISTAGG, XMLAGG 사용 </span>
					</li>
					<li> 10g~11gR1 에서 WM_CONCAT 함수를 사용할 경우 오류가 발생되지 않지만, <br>
						<span style="margin-left:21px;font-size: inherit;"> 11gR2 이상, 12c 버전에서 WM_CONCAT 함수를 사용할 경우 ORA-00904 오류가 발생함. </span>
					</li>
					<li>SQL Guide
						<ul class="minus">
							<li>
								 WM_COUNT <br>
								 <img style="margin-left:30px;" src="/resources/images/sqlConsideration/image020.png"> <br>
								 <img style="border:0px; padding:0px; margin-left:150px;" src="/resources/images/sqlConsideration/arrow_down.png">
							</li>
							<li>
								 LISTAGG <br>
								 <img style="margin-left:30px;" src="/resources/images/sqlConsideration/image021.png">
							</li>
							<li>
								 XMLAGG <br>
								 <img style="margin-left:30px;" src="/resources/images/sqlConsideration/image022.png">
							</li>
						</ul>
					</li>
				</ul>
				
				<div class="well" style="margin:10px 0px;">
					<h3 style="font: 20px bold;"> OR, IN 절의 Outer Join (ORA-01719) </h3>
				</div>
				<ul>
					<li> 미지원 버전 : 11gR2이상 </li>
					<li> ORA-01719 : 포괄 조인 운영 (+)는 OR 또는 IN의 연산수를 허용하지 않습니다.</li>
					<li> 10g 에서는 OR 또는 IN 조건에서 1개의 테이블이 포괄적 Outer조인 사용시 문법 오류가 발생하나, 2개 이상을 포괄적 Outer조인 사용시 오류메시지가 발생하지 않는다. </li>
					<li> 11gR2 이후 부터는 2개 이상을 포괄적 Outer조인 사용시에 오류메시지가 발생한다. </li>
					<li> SQL Guide </li>
					<ul class="minus">
						<li> OR 또는 IN 조건에서 포괄적 Outer 조인 사용으로 ORA-01719 오류발생 </li>
							<img style="margin-left:30px;" src="/resources/images/sqlConsideration/image003.png">
						<li> AND 조건으로 변경</li>
							<img style="margin-left:30px;" src="/resources/images/sqlConsideration/image004.png">
							<li class="non"/>
					</ul>
				</ul>
				
				<div class="well" style="margin:10px 0px;">
					<h3 style="font: 20px bold;"> ORDER BY 절의 Outer 기호 사용(ORA-30563) </h3>
				</div>
				<ul>
					<li> 미지원 버전 : 12c 이상 </li>
					<li> ORA-30563 : 여기에서는 포괄 조인 연산자(+)가 허용되지 않습니다. </li>
					<li> 11g 에서는 ORDER BY절에서 Outer 기호 사용시 문법 오류가 발생하지 않으나 12c 이상에서는 ORA-30563 오류가 발생한다. </li>
					<li> SQL Guide </li>
					<ul class="minus">
						<li> ORDER BY절에 Outer 기호 사용으로 ORA-30563 오류발생 </li>
							<img style="margin-left:30px;" src="/resources/images/sqlConsideration/image023.png">
						<li> Outer 기호 제거 </li>
							<img style="margin-left:30px;" src="/resources/images/sqlConsideration/image024.png">
							<li class="non"/>
					</ul>
				</ul>
				
				<div class="well" style="margin:10px 0px;">
					<h3 style="font: 20px bold;"> ORDER BY 추가 : Batch Nested Loop Join </h3>
				</div>
				<ul>
					<li> 미지원 버전 : 11g 이상 </li>
					<li> 11g 이전 버전에서는 Index Range Scan을 통해 Data를 조회할 경우, Index 순서대로 Data를 Fetch하기 때문에 Order by를 사용하지 않아도 데이터가 Index 순서대로 정렬되었다. </li>
					<li> 11g에서 성능 향상을 위해 추가된 Batch Nested Loop Join기능에 의해, Join되는 Table의 데이터가 Index 순서대로 Fetch하지 않을 수 있다. </li>
					<li> 즉, Index Range Scan을 수행하더라도 Index 순서대로 Data가 조회되지 않을 수 있다. </li>
					<li> Batch Nested Loop Join 기능이 활성화 되어있는 경우 SQL의 결과를 순서대로 조회하기 위해서는 SQL에 Order By를 사용해야 한다. </li>
					<li> 관련 파라미터  </li>
					<ul class="minus">
						<li> _nlj_batching_enabled : 0으로 설정하면 Index Range Scan 오퍼레이션에서 Oracle 10g의 Nested loop Join방식으로 수행됨. </li>
						<li> no_nlj_batching : 세션 수준에서 Batch Nested Loop Join 기능 비활성화 </li>
					</ul>
					<li> SQL Guide </li>
					<ul class="minus">
						<li> Batch Nested Loop Join 으로 결과 데이터가 정렬되지 않는 SQL </li>
							<img style="margin-left:30px;" src="/resources/images/sqlConsideration/image005.png">
						<li class="non">
							<img style="border:0px;" src="/resources/images/sqlConsideration/image006.png">
						</li>
						<li> 데이터를 정렬하는 방법 </li>
						<ul class="square">
							<li> no_nlj_batching 힌트를 사용하여 Batch Nested Loop Join 비활성화 </li>
							<img style="margin-left:30px;" src="/resources/images/sqlConsideration/image007.png">
							<li> 명시적 Order By 사용 </li>
							<img style="margin-left:30px;" src="/resources/images/sqlConsideration/image008.png">
							<li class="non"/>
						</ul>
					</ul>
				</ul>
				
				
				<div class="well" style="margin:10px 0px;">
					<h3 style="font: 20px bold;"> ORDER BY 추가 : Hash Group By </h3>
				</div>
				<ul>
					<li> 알고리즘 변경 버전 : 10g 이상 </li>
					<li> 9i 이하 버전에서는 Group By 가 “SORT GROUP BY”로, Distinct 가 “SORT UNIQUE” 방식으로 동작하여 Sort 오퍼레이션에 의해 데이터가 정렬되어 조회된다. </li>
					<li> 10g 이상의 버전에서는 Group By의 성능을 개선하기 위하여 “HASH GROUP BY”, “HASH UNIQUE” 방식으로 오퍼레이션이 변경되어 데이터가 정렬되어 조회되지 않는다. </li>
					<li> 9i 이하의 버전이 10g 이상의 버전으로 업그레이드 될 경우 Group By 와 Distinct 의 결과 데이터에 정렬이 필요한 경우 명시적으로 Order By를 추가한다. </li>
					<li> SQL Guide </li>
					<ul class="minus">
						<li> “HASH GROUP BY” 동작에 의해 결과 데이터가 정렬되지 않는 SQL </li>
							<img style="margin-left:30px;" src="/resources/images/sqlConsideration/image009.png">
						<li class="non">
							<img style="border:0px;" src="/resources/images/sqlConsideration/image009-1.png">
						</li>
						<li> 데이터를 정렬하는 방법 </li>
						<ul class="square">
							<li> _GBY_HASH_AGGREGATION_ENABLED 파라미터를 설정하여 “SORT GROUP BY” 동작 </li>
							<img style="margin-left:30px;" src="/resources/images/sqlConsideration/image010.png">
							<li> NO_USE_HASH_AGGREGATION 힌트를 사용하여 “SORT GROUP BY” 동작 </li>
							<img style="margin-left:30px;" src="/resources/images/sqlConsideration/image011.png">
							<li> SQL문에 “ORDER BY “를 명시적으로 추가하여 “SORT GROUP BY” 동작 </li>
							<img style="margin-left:30px;" src="/resources/images/sqlConsideration/image012.png">
							<li class="non"/>
						</ul>
					</ul>
				</ul>
				
				
				<div class="well" style="margin:10px 0px;">
					<h3 style="font: 20px bold;"> Select-List 절의 그룹 컬럼이 Group By절에 없음(ORA-00979) </h3>
				</div>
				<ul>
					<li> 미지원 버전 : 11g 이상 </li>
					<li> ORA-00979 : not a GROUP BY expression </li>
					<li> 10g에서 inline view와 group by 구문을 사용할 경우 select list 절의 그룹 컬럼이 Group By절에 일부 없어도 오류가 발생되지 않지만, 11g 이상에서 ORA-00979 오류가 발생함 </li>
					<li> SQL Guide </li>
					<ul class="minus">
						<li> 그룹 컬럼 누락으로 ORA-00979 오류 발생 </li>
							<img style="margin-left:30px;" src="/resources/images/sqlConsideration/image013.png">
						<li> Group By절에 그룹 컬럼 추가</li>
							<img style="margin-left:30px;" src="/resources/images/sqlConsideration/image014.png">
							<li class="non"/>
					</ul>
				</ul>
				
				
				<div class="well" style="margin:10px 0px;">
					<h3 style="font: 20px bold;"> Deprecated 힌트 </h3>
				</div>
				<ul>
					<li> 미지원 버전 : 10.1.0.2 이후 </li>
					<li> Deprecated 힌트? 더 이상 사용되지 않는 힌트로 향후 제거될 수 있으므로 새 DB환경에서 사용하지 않아야 하며 가능하면 사용하지 않도록 이전 SQL을 다시 작성합니다. </li>
					<li> Deprecated 힌트 목록 </li>
					<ul class="minus">
						<li class="non">
							<img style="border:0px;" src="/resources/images/sqlConsideration/image015.png">
						</li>
					</ul>
					<li> SQL Guide </li>
					<ul class="minus">
						<li> AND_EQUAL </li>
						<img style="margin-left:30px;" src="/resources/images/sqlConsideration/image016.png">
						<li class="non" style="margin:0px;">
							<img style="border:0px; padding:0px; margin-left:150px;" src="/resources/images/sqlConsideration/arrow_down.png">
						</li>
						<li class="non" style="margin-top:0px;">
							<img style="margin-left:10px;" src="/resources/images/sqlConsideration/image017.png">
						</li>
						<li class="non" />
						<li> HASH_SJ </li>
						<img style="margin-left:30px;" src="/resources/images/sqlConsideration/image018.png">
						<li class="non" style="margin:0px;">
							<img style="border:0px; padding:0px; margin-left:150px;" src="/resources/images/sqlConsideration/arrow_down.png">
						</li>
						<li class="non" style="margin-top:0px;">
							<img style="margin-left:10px;" src="/resources/images/sqlConsideration/image019.png">
						</li>
					</ul>
				</ul>
			</div>
		</div>
		<div data-options="region:'south',split:false,border:false" style="height:35px;">
			<div class="searchBtn" style="margin-top:5px;">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('sqlConsiderationsPop');"><i class="btnRBIcon fas fa-times fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>
