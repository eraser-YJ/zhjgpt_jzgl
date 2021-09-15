var projectPlanDoneList = {};
projectPlanDoneList.pageRows = null;
//重复提交标识
projectPlanDoneList.subState = false;

//分页处理 start
//分页对象
projectPlanDoneList.oTable = null;
//显示列信息

projectPlanDoneList.oTableAoColumns = [
		{'mData': 'tableRowNo'},
		{'mData':'planYear' },
		{'mData':'planAreaName' },
		{'mData':'planSeqnonameValue' },
		{'mData':'createDate' },
		//设置权限按钮
    	{ "mData": function(source) {
    		var buttonStr = "<a class='a-icon i-new' onclick=projectPlanDoneList.openForm('/instance/toOpenForm.action?definitionId_="+source.workflowBean.definitionId_+"&business_Key_="+source.workflowBean.business_Key_+"&curNodeId_="+source.workflowBean.curNodeId_+"&taskId_="+source.workflowBean.taskId_+"&instanceId_="+source.workflowBean.instanceId_+"&openType_=DONE') href='javascript:void(0)' >查看</a>";
			buttonStr += "<a class='a-icon i-new' target='_blank' href='"+getRootPath()+"/workflowHistory/showHistory.action?instanceId="+source.workflowBean.instanceId_+"&definitionId="+source.workflowBean.definitionId_+"' >流程历史</a>";
			return buttonStr;
    	}}
];

projectPlanDoneList.openForm = function(url){
    JCFF.loadPage({url:url});
}

//组装后台参数
projectPlanDoneList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(projectPlanDoneList.oTable, aoData);
	//组装查询条件
	var query_planYear = $('#query_planYear').val();
	if(query_planYear.length > 0){
		aoData.push({ 'name': 'planYear', 'value': query_planYear});
	}
	var query_planAreaCode = $('#query_planAreaCode').val();
	if(query_planAreaCode.length > 0){
		aoData.push({ 'name': 'planAreaCode', 'value': query_planAreaCode});
	}
};

projectPlanDoneList.workflowDoneList = function () {

	$.ajaxSetup ({
		cache: false //设置成false将不会从浏览器缓存读取信息
	});
	$('#sendPassTransact-list').fadeIn();
	if (projectPlanDoneList.oTable == null) {
		var pagingInfo = pagingDataForGoBack();
		var queryData =pagingInfo.queryData;
		if(queryData!=null){
			//需要针对自己的查询搜索框进行填入值
			//如果有人员选择框的特殊内容需要特殊实现
			$("#projectPlanForm").fill(queryData);
		}
		projectPlanDoneList.oTable = $('#projectPlanTable').dataTable( {
			"iDisplayLength": projectPlanDoneList.pageRows,//每页显示多少条记录
			"iDisplayStart" : pagingInfo.curPage * projectPlanDoneList.pageRows,
			"sAjaxSource": getRootPath()+"/plan/projectYearPlan/manageDoneList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": projectPlanDoneList.oTableAoColumns,//table显示列
			aaSorting:[],//设置表格默认排序列
			//传参
			"fnServerParams": function ( aoData ) {
				projectPlanDoneList.oTableFnServerParams(aoData);
			},
			//默认不排序列
	        "aoColumnDefs": [{"bSortable": false, "aTargets": [0,1,2,3,4,5]}]
		} );
	} else {
		projectPlanDoneList.oTable.fnDraw();
	}
};

projectPlanDoneList.queryReset = function(){
	$('#projectPlanQueryForm')[0].reset();
};

jQuery(function($) {
	//计算分页显示条数
	projectPlanDoneList.pageRows = TabNub>0 ? TabNub : 10;

	
	$("#queryProjectPlan").click(projectPlanDoneList.workflowDoneList);
	$("#queryReset").click(projectPlanDoneList.queryReset);
	
	//日历控件重新初始化
	$(".datepicker-input").datepicker();
	projectPlanDoneList.workflowDoneList();
});

