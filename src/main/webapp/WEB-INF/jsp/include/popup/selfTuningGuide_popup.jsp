<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="indexGuidePop" class="easyui-window popWin" style="background-color:#ffffff;width:1200px;height:700px;">
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'center',split:false,border:false" style="height:295px;padding:10px;">
			<table class="detailT">
				<caption>인덱스 가이드</caption>                    
				<colgroup>	
					<col style="width:7%;"/>
					<col style="width:12%;"/>
					<col style="width:16%;"/>
					<col style="width:37%;"/>
					<col style="width:13%;"/>
					<col style="width:15%;"/>
				</colgroup>
				<tr>
                  <th>업무구분</th>
                  <th>구분</th>
                  <th>세부내용</th>
                  <th>확인방법</th>
                  <th>확인기준(임계치)</th>
                  <th>비고</th>
				</tr>
				<tr>
					<td rowspan="15" class="ctext">
						<dl class="paddingB5 paddingT5">
							<dd>인덱스</dd>
							<dd>신규시</dd>
							<dd>사전</dd>
							<dd>점검사항</dd>
						</dl>
					</td>
					<td rowspan="3">표준화 준수여부</td>
					<td>Nextkey</td>
					<td>
						<dl class="paddingB5 paddingT5">
							<dd>전체 OR 구문 누락</dd>
							<dd>ORDER BY 구문 누락</dd>
							<dd>ROWNUM &lt;= :pageRowCouont + 1 구문 누락</dd>
							<dd>ORDER BY 구문의 컬럼과 OR 구문의 컬럼 불일치</dd>
							<dd>ORDER BY 구문의 정렬과 OR 구문의 부등호 불일치</dd>
							<dd>OR 구문 사용시 괄호 누락</dd>
							<dd>pageRowCount 변수 미사용</dd>
						</dl>
					</td>
					<td>해당사항없음</td>
					<td></td>
				</tr>
				<tr>
					<td>입력조건별 분기</td>
				    <td>
						<dl class="paddingB5 paddingT5">
							<dd>기본조회 요건 필수 입력 여부</dd>
							<dd>IS NULL 외 조건(NVL, DECODE, CASE, LIKE 등) 사용 여부</dd>
							<dd>옵션이 4개 이상인 경우만 허용</dd>
						</dl>
				    </td>
					<td>해당사항없음</td>
					<td></td>
				</tr>
				<tr>
					<td>outer 조인</td>
					<td>
						<dl class="paddingB5 paddingT5">
							<dd>불필요 OUTER JOIN 사용 여부</dd>
							<dd>ANSI OUTER JOIN 사용 여부</dd>
							<dd>(+) 기호 누락 여부</dd>
						</dl>
				    </td>
				    <td>해당사항없음</td>
				    <td></td>
				</tr>
				<tr>
				    <td rowspan="4">용량확인</td>
					<td>테이블 용량 확인</td>
				    <td>
						<dl class="paddingB5 paddingT5">
							<dd>SELECT SEGMENT_NAME, SUM(BYTES)/1024/1024 "MB"</dd>
							<dd class="paddingL20">FROM DBA_SEGMENTS</dd>
							<dd>WHERE OWNER = :TABLE_OWNER</dd>
							<dd class="paddingL20">AND SEGMENT_NAME LIKE '%' || :TABLE_NAME || '%'</dd>
							<dd>GROUP BY SEGMENT_NAME ;</dd>
						</dl>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>파티션 테이블 용량 확인</td>
				    <td>
						<dl class="paddingB5 paddingT5 ">
							<dd>SELECT SEGMENT_NAME, SUM(BYTES)/1024/1024 "MB"</dd>
							<dd class="paddingL20">FROM DBA_SEGMENTS</dd>
							<dd>WHERE OWNER = :TABLE_OWNER</dd>
							<dd class="paddingL20">AND SEGMENT_NAME LIKE '%' || :TABLE_NAME || '%'</dd>
							<dd>GROUP BY SEGMENT_NAME ;</dd>
						</dl>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>인덱스 용량 확인</td>
					<td>
						<dl class="paddingB5 paddingT5 ">
							<dd>select segment_name, bytes from dba_segments</dd>
							<dd>where owner = 'CBKOWN'</dd>
							<dd>and segment_name in</dd>
							<dd class="paddingL20">(</dd>
							<dd class="paddingL40">select index_name from dba_indexes</dd>
							<dd class="paddingL40">where table_owner =: table_owner</dd>
							<dd class="paddingL40">and table_name =: table_name</dd>
							<dd class="paddingL20">)</dd>
							<dd>;</dd>
						</dl>
				    </td>
				    <td></td>
				    <td></td>
				</tr>
				<tr>
					<td>파티션 인덱스 용량 확인</td>
				    <td>
				    	<dl class="paddingB5 paddingT5 ">
							<dd>select segment_name, bytes from dba_segments</dd>
							<dd>where owner = 'CBKOWN'</dd>
							<dd>and segment_name in</dd>
							<dd class="paddingL20">(</dd>
							<dd class="paddingL40">select index_name from dba_indexes</dd>
							<dd class="paddingL40">where table_owner =: table_owner</dd>
							<dd class="paddingL40">and table_name =: table_name</dd>
							<dd class="paddingL20">)</dd>
							<dd class="paddingL20">GROUP BY SEGMENT_NAME</dd>
							<dd>;</dd>
						</dl>
					</td>
					<td></td>
					<td>인덱스추가에따른인덱스로인한I/O증가율 및테이블스페이스용량확보</td>
				</tr>
				<tr>
				    <td>인덱스 개수 확인</td>
					<td>과다한 인덱스 추가를<br/> 방지하기 위하여, 정확한 기준을 정할수는 없지만 7개 이상의 인덱스를<br/> 소유한 테이블에 대한 <br/>신규 인덱스 추가시<br/> 액세스패턴에 대한 상세 검토를 수행 하도록 한다.</td>
				    <td>
						<dl class="paddingB5 paddingT5 ">
							<dd>SELECT COUNT(*) FROM DBA_INDEXES</dd>
							<dd class="paddingL20">WHERE TABLE_OWNER = :table_owner</dd>
							<dd class="paddingL40">AND TABLE_NAME = :table_name ;</dd>
						</dl>
					</td>
					<td>5</td>
					<td></td>
				</tr>
				<tr>
				    <td>통계정보 수집일 확인</td>
					<td>인덱스를 추가하는<br/> 테이블 및 인덱스의<br/> 통계정보 수집일을 확인</td>
				    <td>
				    	<dl class="paddingB5 paddingT5 ">
							<dd>SELECT * FROM 테이블명;</dd>
							<dd class="paddingL10">-> 오렌지툴에서 Ctl + E (실행계획확인)</dd>
							<dd>LAST_ANALYZED날짜확인</dd>
							<dd class="paddingT10">SELECT ‘TB’ TYPE, TABLE_NAME, LAST_ANALYZED</dd>
							<dd class="paddingL10">FROM DBA_TABLES</dd>
							<dd>WHERE TABLE_OWNER = :table_owner</dd>
							<dd class="paddingL10">AND TABLE_NAME = :table_name</dd>
							<dd>UNION ALL</dd>
							<dd>SELECT ‘IX’, INDEX_NAME, LAST_ANALYZED</dd>
							<dd class="paddingL10">FROM DBA_INDEXES</dd>
							<dd>WHERE TABLE_OWNER = :table_owner</dd>
							<dd class="paddingL10">AND TABLE_NAME = :table_name ;</dd>
						</dl>
					</td>
					<td>2015/10/18</td>
					<td>통계정보수집이후상당시간이경과하였거나,그에따른데이터변동량이많을경우,테이블관련통계정보의수집주기를확인하여통계정보갱신시점에맞추어인덱스추가를수행한다.</td>
				</tr>
				<tr>
				    <td>데이터 일변동량 확인</td>
					<td>데이터 일변동량을 확인하여, 해당 테이블 및 인덱스의 통계정보 수집후 데이터 변동량이 통계정보수집일 대략 10% 이상 변경한 경우 신규인덱스 생성으로 인한 의도하지 않은 실행계획 변경이 발생할 수 있음.</td>
				    <td>
						<dl class="paddingB5 paddingT5 ">
							<dd>배치 BCV 로그인</dd>
							<dd>SELECT BASE_DATE, TABLE_NAME, SUM(NUM_ROWS)</dd>
							<dd class="paddingL20">FROM CBKDBA.TB_DBA_HIST_SEGMENTS</dd>
							<dd class="paddingL10">WHERE OWNER = :TABLE_OWNER</dd>
							<dd class="paddingL20">AND TABLE_NAME = :TABLE_NAME</dd>
							<dd class="paddingL20">AND BASE_DATE = :LAST_ANALYZED</dd>
							<dd class="paddingL10">GROUP BY BASE_DATE, TABLE_NAME</dd>
							<dd>UNION ALL</dd>
							<dd>SELECT BASE_DATE, TABLE_NAME, SUM(NUM_ROWS)</dd>
							<dd class="paddingL20">FROM CBKDBA.TB_DBA_HIST_SEGMENTS</dd>
							<dd class="paddingL10">WHERE OWNER = :TABLE_OWNER</dd>
							<dd class="paddingL20">AND TABLE_NAME = :TABLE_NAME</dd>
							<dd class="paddingL20">AND BASE_DATE = TO_CHAR(SYSDATE - 1,'YYYYMMDD')</dd>
							<dd class="paddingL10">GROUP BY BASE_DATE, TABLE_NAME ;</dd>
				        </dl>
				    </td>
				    <td>1.14~115（３００９４）</td>
				    <td></td>
				</tr>
				<tr>
					<td>유사인덱스 존재유무 확인</td>
					<td>기존인덱스의컬럼구성을확인하여,신규인덱스의컬럼과중복되는인덱스인지확인필요. 유사인덱스를사용하는DAO의SQL PLAN변경유발가능성이있으며,인덱스과다로인한I/O부하발생함.</td>
					<td>
						<dl class="paddingB5 paddingT5 ">
							<dd>SELECT INDEX_NAME, LISTAGG(COLUMN_NAME || ' ' ||</dd>
							<dd class="paddingL40">DESCEND, ', ') WITHIN GROUP(ORDER BY</dd>
							<dd class="paddingL40">COLUMN_POSITION) INDEX_COLUMN</dd>
							<dd class="paddingL30">FROM DBA_IND_COLUMNS</dd>
							<dd class="paddingL10">WHERE TABLE_OWNER = :TABLE_OWNER</dd>
							<dd class="paddingL20">AND TABLE_NAME = :TABLE_NAME</dd>
							<dd class="paddingL10">GROUP BY INDEX_NAME ;</dd>
						</dl></td>
					<td>pk인덱스 첫 번째컬럼 유사</td>
					<td></td>
				</tr>
				<tr>
				    <td>인덱스 효율성 확인</td>
					<td>인덱스 자체의 Distinct 율 과 테이블 전체건수를 비교하여, 인덱스를 사용하는 엑세스 패턴에 따라 평균 추출되는 결과건수의 타당성을 확인한다.</td>
				    <td>
						<dl class="paddingB5 paddingT5 ">
							<dd>(예제)</dd>
							<dd>전체건수 : 16,947,462</dd>
							<dd>신규 인덱스 : Distinct = 147</dd>
							<dd class="paddingL20">Case1) 이퀄검색시 평균 7.6 건 추출</dd>
							<dd class="paddingL20">Case2) Range검색시 평균 7.6 * N 건 추출</dd>
							<dd>사용SQL의조건검색특성을확인하여복합컬럼의필요유무를검토.</dd>
							<dd class="paddingL20">-> 기존 튜닝가이드 사례를 기준으로 컬럼이 부족한 경우가</dd>
							<dd>발생함.</dd>
							<dd>/* SELECT COUNT(*)</dd>
							<dd>FROM (</dd>
							<dd>SELECT /*+ FULL(A) PARALLEL(A 16) */DISTINCT FNBB_TRN_AMT,</dd>
							<dd>FNBB_TRN_DSNC_NO, HTDSP_DCD FROM TB_CBK_EXNEF1L A); */</dd>
							<dd><img src="/resources/images/img_TI01.gif" alt="샘플" width="368px" height="153px"></dd>
							<dd><img src="/resources/images/img_TI02.gif" alt="샘플" width="368px" height="153px"></dd>
				        </dl>
				    </td>
				    <td>39,639</td>
				    <td></td>
				</tr>
				<tr>
				    <td>결합인덱스의 컬럼우선순위 확인</td>
					<td>
						<dl class="paddingB5 paddingT5 ">
							<dd>- 결합인덱스의 컬럼 순서가 검색조건 연산자 우선순위에 부합하는지 확인.</dd>
							<dd>- 공통 Access 컬럼을 앞쪽에 위치하게 하여, Index Skip Scan 발생억제</dd>
							<dd>- 컬럼수가 많을 경우 Distinct가 낮은 컬럼 불필요하게 추가 유무 등</dd>
							<dd>- 목록성 쿼리의 페이징 처리를 위한 Sort처리를 대체할 수 있는 컬럼구성 Order By 구문의 컬럼순서 확인</dd>
				        </dl>
				    </td>
				    <td>
						<dl class="paddingB5 paddingT5 ">
							<dd>- 결합인덱스의 컬럼 순서가 검색조건 연산자 우선순위에 부합 하는지 확인.</dd>
							<dd>- 공통 Access 컬럼을 앞쪽에 위치하게 하여, Index Skip Scan 발생억제</dd>
							<dd>- 공통 Access 컬럼을 앞쪽에 위치하게 하여, Index Skip Scan 발생억제</dd>
							<dd>- 목록성 쿼리의 페이징 처리를 위한 Sort처리를 대체할 수 있는 컬럼구성 Order By 구문의 컬럼순서 확인</dd>
				        </dl>
				    </td>
				    <td></td>
				    <td></td>
				</tr>
				<tr>
					<td>파티션 인덱스의 특성확인</td>
					<td></td>
					<td>
						<dl class="paddingB5 paddingT5 ">
							<dd>- 성능적 관점의 Global Index를 사용이 꼭 필요한지 확인</dd>
							<dd class="paddingL20">-> 튜너 검토</dd>
							<dd>- 운영적 관점에서 Local Index를 꼭 사용하여야 하는지 확인</dd>
							<dd class="paddingL20">-> DBA 검토</dd>
						</dl>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>업무팀 확인</td>
					<td>업무팀에 해당 테이블을 사용하는 관련기능에 대하여 단위 테스트 수행여부를 확인한다.</td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td rowspan="11" class="center">
						<dl class="paddingB5 paddingT5 ">
							<dd>인덱스</dd>
							<dd>변경시</dd>
							<dd>사전</dd>
							<dd>점검사항</dd>
						</dl>
					</td>
					<td rowspan="3">표준화 준수유무 확인</td>
					<td>Nextkey</td>
					<td>
						<dl class="paddingB5 paddingT5 ">
							<dd>전체 OR 구문 누락</dd>
							<dd>ORDER BY 구문 누락</dd>
							<dd>ROWNUM &lt;= :pageRowCouont + 1 구문 누락</dd>
							<dd>ORDER BY 구문의 컬럼과 OR 구문의 컬럼 불일치</dd>
							<dd>ORDER BY 구문의 정렬과 OR 구문의 부등호 불일치</dd>
							<dd>OR 구문 사용시 괄호 누락</dd>
							<dd>pageRowCount 변수 미사용</dd>
						</dl>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>입력조건별 분기</td>
					<td>
						<dl class="paddingB5 paddingT5 ">
							<dd>기본조회 요건 필수 입력 여부</dd>
							<dd>IS NULL 외 조건(NVL, DECODE, CASE, LIKE 등) 사용 여부</dd>
							<dd>옵션이 4개 이상인 경우만 허용</dd>
						</dl>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>outer 조인</td>
					<td>
						<dl class="paddingB5 paddingT5 ">
							<dd>불필요 OUTER JOIN 사용 여부</dd>
							<dd>ANSI OUTER JOIN 사용 여부</dd>
							<dd>(+) 기호 누락 여부</dd>
						</dl>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>인덱스 용량확인</td>
					<td></td>
					<td>
						<dl class="paddingB5 paddingT5 ">
							<dd>SELECT SEGMENT_NAME, SUM(BYTES)/1024/1024 "MB"</dd>
							<dd class="paddingL20">FROM DBA_SEGMENTS</dd>
							<dd>WHERE OWNER = :TABLE_OWNER</dd>
							<dd class="paddingL20">AND SEGMENT_NAME LIKE '%' || :TABLE_NAME || '%'</dd>
							<dd>GROUP BY SEGMENT_NAME ;</dd>
						</dl>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>해당 인덱스를 사용하는 DAO의 SQL영향도 분석</td>
					<td>
						<dl class="paddingB5 paddingT5 ">
							<dd>관련SQL문을추출하여,인덱스컬럼구성의변경으로인한실행계획영향도확인</dd>
							<dd class="paddingL10">- 첨부: 특정 테이블의인덱스를 사용하는 DAO 추출 SQL.sql</dd>
						</dl>
					</td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>통계정보 수집일 확인</td>
					<td></td>
					<td></td>
					<td></td>
				    <td></td>
				</tr>
				<tr>
					<td>데이터 일변동량 확인</td>
					<td></td>
					<td>
						<dl class="paddingB5 paddingT5 ">
							<dd>배치 BCV 로그인</dd>
							<dd>SELECT BASE_DATE, TABLE_NAME, SUM(NUM_ROWS)</dd>
							<dd class="paddingL20">FROM CBKDBA.TB_DBA_HIST_SEGMENTS</dd>
							<dd class="paddingL10">WHERE OWNER = :TABLE_OWNER</dd>
							<dd class="paddingL20">AND TABLE_NAME = :TABLE_NAME</dd>
							<dd class="paddingL20">AND BASE_DATE = :LAST_ANALYZED</dd>
							<dd class="paddingL10">GROUP BY BASE_DATE, TABLE_NAME</dd>
							<dd>UNION ALL</dd>
							<dd>SELECT BASE_DATE, TABLE_NAME, SUM(NUM_ROWS)</dd>
							<dd class="paddingL20">FROM CBKDBA.TB_DBA_HIST_SEGMENTS</dd>
							<dd class="paddingL10">WHERE OWNER = :TABLE_OWNER</dd>
							<dd class="paddingL20">AND TABLE_NAME = :TABLE_NAME</dd>
							<dd class="paddingL20">AND BASE_DATE = TO_CHAR(SYSDATE - 1,'YYYYMMDD')</dd>
							<dd class="paddingL10">GROUP BY BASE_DATE, TABLE_NAME ;</dd>
						</dl>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>유사인덱스 존재유무 확인</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>인덱스 효율성 확인</td>
					<td></td>
					<td>
						<dl class="paddingB5 paddingT5 ">
							<dd>/* SELECT COUNT(*)</dd>
							<dd>FROM (</dd>
							<dd class="paddingL20">SELECT /*+ FULL(A) PARALLEL(A 16) */DISTINCT FNBB_TRN_AMT,</dd>
							<dd class="paddingL20">FNBB_TRN_DSNC_NO, HTDSP_DCD FROM TB_CBK_EXNEF1L</dd>
							<dd>); */</dd>
						</dl>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>결합인덱스의 컬럼우선순위 확인</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>업무팀 확인</td>
					<td></td>
					<td></td>
					<td></td>				    
					<td></td>
				</tr>
				<tr>
					<td rowspan="2" class="center">
						<dl class="paddingB5 paddingT5 ">
							<dd>인덱스</dd>
							<dd>삭제시</dd>
							<dd>사전</dd>
							<dd>점검사항</dd>
						</dl>
					</td>
					<td>해당 인덱스를 사용하는 DAO의 SQL영향도 분석</td>
					<td>
						<dl class="paddingB5 paddingT5 ">
							<dd>관련SQL문을추출하여해당인덱스를사용하는DAO가없음을확인</dd>
							<dd class="paddingL10">- 첨부: 특정 테이블의 인덱스를 사용하는 DAO 추출 SQL.sql</dd>
						</dl>
					</td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>업무팀 확인</td>
					<td>DBMS에서 추출한 SQL문만으로는 해당 인덱스를 사용하는 모든 SQL문을 발췌할 수 없으므로, 업무팀에 삭제인덱스에 대한 사용유무를 확인한다.</td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
			</table>	
		</div>
		<div data-options="region:'south',split:false,border:false" style="height:55px;">
			<div class="searchBtn" style="margin-top:5px;padding-right:10px;">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('indexGuidePop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>		
		</div>	
	</div>
</div>
<div id="tuningCheckPop" class="easyui-window popWin" style="background-color:#ffffff;width:1200px;height:700px;">
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'center',split:false,border:false" style="height:295px;padding:10px;">
			<table class="detailT">
				<caption>인덱스 가이드</caption>                    
				<colgroup>	
					<col style="width:20%;"/>
					<col style="width:60%;"/>
					<col style="width:20%;"/>
				</colgroup>
				<tr>
					<th>업무구분</th>
                    <th>체크항목</th>
                    <th>확인연결정보</th>
				</tr>
				<tr>
				    <td class="ctext">사전점검</td>
					<td>
						<dl class="paddingB5 paddingT5">
							<dt>대상 SQL의 업무적 특성을 확인</dt>
							<dl class="marginL10">- 대량의 CUD 발생 업무</dl>
							<dl class="marginL10">- 단순 조회 업무</dl>
							<dl class="marginL10">- 대량 리스트 조회</dl>
							<dl class="marginL10">- 수행이 매우 많은 중요한 조회 업무</dl>
							<dl class="marginL10">- 집계성(통계적) 업무</dl>
				            <dl class="marginL10">- 월단위로 처리하는 배치 업무</dl>
				        </dl>
				    </td>
				    <td>
						<dl class="paddingB5 paddingT5">
							<dl>실행횟수</dl>
							<dl>요청서의</dl>
							<dl>특이사항</dl>
							<dl>테이블및컬럼명세서</dl>
						</dl>
					</td>
				</tr>
				<tr>
					<td rowspan="5" class="ctext">실행계획점검</td>
					<td>Table Full Access 하는 부분이 존재하는가?</td>
					<td>테이블통계정보</td>
				</tr>
				<tr>
					<td class="ltext">드라이빙 테이블이 적절한가?</td>
					<td>테이블통계정보</td>
				</tr>
				<tr>
					<td>Buffers 가 비교적 큰 부분이 존재하는가?</td>
					<td></td>
				</tr>
				<tr>
					<td>최종 결과 A-Rows에 비해서 A-Rows값이 큰 곳이 존재하는가?</td>
					<td></td>
				</tr>
				<tr>	
					<td>불필요하거나 잘못된 힌트문을 사용하고 있는가?</td>
					<td></td>
				</tr>
				<tr>
					<td rowspan="8" class="ctext">SQL구조점검</td>
					<td>페이징 처리의 부분범위 수행 처리가 잘되는가?</td>
					<td></td>
				</tr>
				<tr>
					<td>Order By 절의 컬럼과 드라이빙 테이블의 인덱스 컬럼과 일치하는가?</td>
					<td>
						<dl class="paddingB5 paddingT5">
							<dl>인덱스목록</dl>
							<dl>인덱스통계정보</dl>
						</dl>
					</td>
				</tr>
				<tr>
					<td>스칼라 쿼리를 사용한다면 조인으로 대체할 수 있는가?</td>
					<td></td>
				</tr>
				<tr>
					<td>동일구간을 반복 또는 중복적으로 Access 하는 서브쿼리, 인라인뷰가 존재하는가?</td>
					<td></td>
				</tr>
				<tr>
					<td>인라인뷰 및 서브쿼리가 JPPD(Join Predicate Push Down) 동작 가능한가?</td>
					<td></td>
				</tr>
				<tr>
					<td>인라인뷰 및 서브쿼리가 Unnesting(중첩되지 않음) 으로 동작되는 쿼리인가?</td>
					<td></td>
				</tr>
				<tr>
					<td>페이징 처리 구문이 적절하게 작성되어 있는가?</td>
					<td></td>
				</tr>
				<tr>
					<td>논리적으로 잘못 작성된 곳이 존재하는가?<br/>(예로 항상 false 가 되어 결과 값이 나올 수 없는 경우)</td>
					<td></td>
				</tr>
				<tr>
					<td rowspan="4" class="ctext">조인점검</td>
					<td>조건에 의해서 대상 데이터의 수량을 줄일 수 있는 테이블의 순서대로 조인되는가?</td>
					<td></td>
				</tr>
				<tr>
					<td>사용하지 않는 테이블을 조인하지 않는가?</td>
					<td></td>
				</tr>
				<tr>
					<td>조인조건 누락에 의한 Merge Join Cartesian 이 존재하는가?</td>
					<td></td>
				</tr>
				<tr>
					<td>Nested Loop 조인과 Hash 조인이 목적에 맞게 잘 선택되었는가?<br/>(온라인은 NL 조인을 대량 배치 조인은 Hash 를 추천)</td>
					<td></td>
				</tr>
				<tr>
					<td rowspan="4" class="ctext">인덱스점검</td>
					<td>드라이빙 테이블의 조건에 해당되는 적절한 인덱스가 존재하는가?</td>
					<td>
						<dl class="paddingB5 paddingT5">
							<dl>인덱스목록</dl>
							<dl>인덱스통계정보</dl>
						</dl>
					</td>
				</tr>
				<tr>
					<td>조인 컬럼에 적절한 인덱스를 사용하는가?<br/>(hash조인은 조인 컬럼의 인덱스를 사용하지 않음)</td>
					<td>
						<dl class="paddingB5 paddingT5">
							<dl>인덱스목록</dl>
							<dl>인덱스통계정보</dl>
							<dl>테이블통계정보</dl>
						</dl>
    				</td>
				</tr>
				<tr>
					<td>인덱스 부재 또는 인덱스 재사용으로 인하여 인덱스 생성 및 변경시 컬럼의 분포도를 고려하였는가?</td>
					<td>
						<dl class="paddingB5 paddingT5">
							<dl>인덱스목록</dl>
							<dl>인덱스통계정보</dl>
							<dl>테이블통계정보</dl>
						</dl>
					</td>
				</tr>
				<tr>
					<td>속성 분리가 안된 조건(Substr, Concat 등의 함수)에 의해서 FUNCTION BASED INDEX 를 사용해야 하는가?</td>
					<td></td>
				</tr>
			</table>	
		</div>
		<div data-options="region:'south',split:false,border:false" style="height:55px;">
			<div class="searchBtn" style="margin-top:5px;padding-right:10px;">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('tuningCheckPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>		
		</div>	
	</div>
</div>