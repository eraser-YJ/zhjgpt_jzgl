var projectPlanTodoList = {};
projectPlanTodoList.pageRows = null;
//重复提交标识
projectPlanTodoList.subState = false;

//分页处理 start
//分页对象
projectPlanTodoList.oTable = null;
//显示列信息

projectPlanTodoList.oTableAoColumns = [
		{'mData': 'tableRowNo'},
		{'mData':'planYear' },
		{'mData':'planAreaName' },
		{'mData':'planSeqnonameValue' },
		{'mData':'createDate' },
		//设置权限按钮
    	{ "mData": function(source) {
    		var buttonStr = "<a class='a-icon i-new' onclick=projectPlanTodoList.openForm('/instance/toOpenForm.action?definitionId_="+source.workflowBean.definitionId_+"&business_Key_="+source.workflowBean.business_Key_+"&curNodeId_="+source.workflowBean.curNodeId_+"&taskId_="+source.workflowBean.taskId_+"&instanceId_="+source.workflowBean.instanceId_+"&openType_=TODO') href='javascript:void(0)' >办理</a>";
			buttonStr += "<a class='a-icon i-new' target='_blank' href='"+getRootPath()+"/workflowHistory/showHistory.action?instanceId="+source.workflowBean.instanceId_+"&definitionId="+source.workflowBean.definitionId_+"' >流程历史</a>";
			return buttonStr;
    	}}
];
                  
projectPlanTodoList.openForm = function(url){
	JCFF.loadPage({url:url});
}

//组装后台参数
projectPlanTodoList.oTableFnServerParams = function(aoData){
	//排序条件
	getTableParameters(projectPlanTodoList.oTable, aoData);
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

projectPlanTodoList.workflowTodoList = function () {

	$.ajaxSetup ({
		cache: false //设置成false将不会从浏览器缓存读取信息
	});
	$('#sendPassTransact-list').fadeIn();
	if (projectPlanTodoList.oTable == null) {
		var pagingInfo = pagingDataForGoBack();
		var queryData =pagingInfo.queryData;
		if(queryData!=null){
			//需要针对自己的查询搜索框进行填入值
			//如果有人员选择框的特殊内容需要特殊实现
			$("#projectPlanForm").fill(queryData);
		}
		projectPlanTodoList.oTable = $('#projectPlanTable').dataTable( {
			"iDisplayLength": projectPlanTodoList.pageRows,//每页显示多少条记录
			"iDisplayStart" : pagingInfo.curPage * projectPlanTodoList.pageRows,
			"sAjaxSource": getRootPath()+"/plan/projectYearPlan/manageTodoList.action",
			"fnServerData": oTableRetrieveData,//查询数据回调函数
			"aoColumns": projectPlanTodoList.oTableAoColumns,//table显示列
			aaSorting:[],//设置表格默认排序列
			//传参
			"fnServerParams": function ( aoData ) {
				projectPlanTodoList.oTableFnServerParams(aoData);
			},
			//默认不排序列
	        "aoColumnDefs": [{"bSortable": false, "aTargets": [0,1,2,3,4,5]}]
		} );
	} else {
		projectPlanTodoList.oTable.fnDraw();
	}
};

projectPlanTodoList.queryReset = function(){
	$('#projectPlanQueryForm')[0].reset();
};

jQuery(function($) {
	//计算分页显示条数
	projectPlanTodoList.pageRows = TabNub>0 ? TabNub : 10;

	
	$("#queryProjectPlan").click(projectPlanTodoList.workflowTodoList);
	$("#queryReset").click(projectPlanTodoList.queryReset);
	
	//日历控件重新初始化
	$(".datepicker-input").datepicker();
	projectPlanTodoList.workflowTodoList();
});

