Ext.onReady(function(){
	Ext.create('Ext.grid.Panel',{
//	Ext.create('Ext.panel.Panel',{
		width:800,
		height:500,
		layout:'fit',
		title : '작업스케쥴러 상세관리',
		columns : [{
			flex : 1,
			text : '작업스케쥴러 유형코드',
			dataIndex : 'job_scheduler_type_cd'
		},{
			flex : 1,
			text : '작업스케쥴러 유형',
			dataIndex : 'job_scheduler_type_cd_nm'
		},{
			flex : 1,
			text : '작업스케쥴러 상세유형코드',
			dataIndex : 'job_scheduler_detail_type_cd'
		},{
			flex : 1,
			text : '작업스케쥴러 상세유형',
			dataIndex : 'job_scheduler_detail_type_nm'
		},{
			flex : 1,
			text : '작업스케쥴러 상세유형 설명',
			dataIndex : 'job_scheduler_detail_type_desc'
		}],
		store : Ext.create('Ext.data.Store',{
				fields : ['job_scheduler_type_cd','job_scheduler_type_cd_nm','job_scheduler_detail_type_cd','job_scheduler_detail_type_nm','job_scheduler_detail_type_desc'],
				autoLoad : true,
				proxy : {
					type : 'ajax',
					url : "/getJobSchedulerdetailTypeCdList_extjs",
//					rootProperty : 'rows',
//					totalProperty : 'totalCount'
				}
//				data : [['1-1','1-2','1-3','1-4','1-5'],['2-1','2-2','2-3'],['3-1','3-2','3-3'],['3-1','3-2','3-3'],['3-1','3-2','3-3']]
		}),
		renderTo : Ext.getBody(),
	})
})